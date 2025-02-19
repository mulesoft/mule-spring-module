/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.internal.beanfactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.logging.LogFactory;
import org.mule.extension.spring.internal.util.CustomPostAuthenticationChecks;
import org.mule.extension.spring.internal.util.CustomPreAuthenticationChecks;
import org.mule.extension.spring.internal.util.MyCustomLogger;
import org.mule.runtime.api.ioc.ObjectProvider;
import org.mule.runtime.api.lifecycle.Disposable;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.lifecycle.Startable;
import org.mule.runtime.api.lifecycle.Stoppable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

/**
 * {@link DefaultListableBeanFactory} implementation that takes into account the objects provided by the mule artifact for
 * dependency resolution.
 * 
 * @since 1.0
 */
public class ArtifactObjectsAwareBeanFactory extends DefaultListableBeanFactory {

  private final ObjectProvider artifactObjectProvider;
  private boolean destroying = false;

  public ArtifactObjectsAwareBeanFactory(BeanFactory parentBeanFactory, ObjectProvider artifactObjectProvider) {
    super(parentBeanFactory);
    this.artifactObjectProvider = artifactObjectProvider;
  }

  @Override
  public Object doResolveDependency(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames,
                                    TypeConverter typeConverter)
      throws BeansException {
    Object springDependency = null;
    NoSuchBeanDefinitionException exception = null;
    try {
      springDependency = super.doResolveDependency(descriptor, beanName, autowiredBeanNames, typeConverter);
    } catch (NoSuchBeanDefinitionException e) {
      exception = e;
    }
    if (springDependency == null) {
      return artifactObjectProvider.getObjectByType(descriptor.getDependencyType()).orElse(null);
    } else if (Collection.class.isAssignableFrom(descriptor.getDependencyType())) {
      // When a found dependency is a collection then we must also search for all the possible objects in the
      // artifact ObjectProvider.
      Class<?> dependencyType = descriptor.getResolvableType().getGenerics().length == 1
          ? descriptor.getResolvableType().getGenerics()[0].getRawClass()
          : Object.class;
      Map<String, ?> objects = artifactObjectProvider.getObjectsByType(dependencyType);
      if (springDependency instanceof Collection) {
        List collectionValue = new ArrayList();
        if (springDependency != null) {
          collectionValue.addAll((Collection) springDependency);
        }
        collectionValue.addAll(objects.values());
        return collectionValue;
      }
      return objects;
    }
    if (exception != null && springDependency == null) {
      throw exception;
    }
    return springDependency;
  }

  /*
   * Overrides spring method so we can search in the artifact ObjectProvider for the bean if exists there.
   */
  @Override
  protected <T> T doGetBean(String name, Class<T> requiredType, Object[] args, boolean typeCheckOnly) throws BeansException {
    if ("fips140-2".equals(System.getProperty("mule.security.model"))) {
      if (name.contains(DaoAuthenticationProvider.class.getName()) || DaoAuthenticationProvider.class.equals(requiredType)) {
        UserDetailsService userDetailsService = (UserDetailsService) super.getBean("userService");
        return (T) authenticationProvider(userDetailsService);
      }
    }
    if (!containsBean(name) && artifactObjectProvider.containsObject(name) && !destroying) {
      return (T) artifactObjectProvider.getObject(name).get();
    }
    return super.doGetBean(name, requiredType, args, typeCheckOnly);
  }

  /**
   * Overrides spring method to first check if the bean to be registered is a valid one. As for now, we are not supporting beans
   * with a class that implements mule Lifecycle.
   * 
   * @param beanName
   * @param beanDefinition
   * @throws BeanDefinitionStoreException
   */
  @Override
  public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
    if (implementsLifecycle(beanDefinition)) {
      throw new BeanDefinitionStoreException(String
          .format("%s should not implement mule Lifecycle (Initialisable, Startable, Stoppable, Disposable)", beanName));
    }
    super.registerBeanDefinition(beanName, beanDefinition);
  }

  /*
   * Evaluates if the class in the bean definition implements mule lifecycle
   */
  private boolean implementsLifecycle(BeanDefinition beanDefinition) {
    if (beanDefinition.getBeanClassName() == null) {
      // There is no class to check
      return false;
    }
    try {
      Class<?> beanClass = Class.forName(beanDefinition.getBeanClassName());
      return Initialisable.class.isAssignableFrom(beanClass) ||
          Startable.class.isAssignableFrom(beanClass) ||
          Stoppable.class.isAssignableFrom(beanClass) ||
          Disposable.class.isAssignableFrom(beanClass);
    } catch (ClassNotFoundException e) {
      // Do nothing, let the parsers handle this.
    }
    return false;
  }

  /**
   * Marks the beginning of destroy of the context.
   */
  public void markForDestroy() {
    this.destroying = true;
  }

  //TODO improve this code error check
  //ugly hack to bypass non FIPS compliant security algorithms
  static DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
    try {
      Class c = Class.forName("sun.misc.Unsafe");
      Field field = Arrays.stream(c.getDeclaredFields()).filter(f -> f.getName().equals("theUnsafe"))
          .findFirst().orElseThrow(() -> new RuntimeException("Field not found"));
      field.setAccessible(true);
      Method allocateInstance = c.getDeclaredMethod("allocateInstance", Class.class);
      DaoAuthenticationProvider authProvider =
          (DaoAuthenticationProvider) allocateInstance.invoke(field.get(null), DaoAuthenticationProvider.class);
      authProvider.setPasswordEncoder(createDelegatingPasswordEncoder());
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setUserCache(new NullUserCache());
      authProvider.setAuthoritiesMapper(new NullAuthoritiesMapper());

      // Setting custom pre and post authentication checks
      authProvider.setPreAuthenticationChecks(new CustomPreAuthenticationChecks());
      authProvider.setPostAuthenticationChecks(new CustomPostAuthenticationChecks());

      Field loggerField = AbstractUserDetailsAuthenticationProvider.class.getDeclaredField("logger");
      loggerField.setAccessible(true);
      loggerField.set(authProvider, LogFactory.getLog(MyCustomLogger.class));

      return authProvider;
    } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException
        | NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  static PasswordEncoder createDelegatingPasswordEncoder() {
    String encodingId = "bcrypt";
    Map<String, PasswordEncoder> encoders = new HashMap();
    encoders.put(encodingId, new BCryptPasswordEncoder());
    encoders.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_5());
    encoders.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v4_1());
    encoders.put("scrypt@SpringSecurity_v5_8", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
    encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_2());
    encoders.put("argon2@SpringSecurity_v5_8", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
    return new DelegatingPasswordEncoder(encodingId, encoders);
  }
}
