/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.api;

import static java.lang.Thread.currentThread;
import static java.util.Collections.emptyMap;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toMap;
import static org.mule.runtime.api.util.Preconditions.checkState;
import static org.mule.runtime.core.api.util.ClassUtils.withContextClassLoader;
import org.mule.extension.spring.internal.context.SpringModuleApplicationContext;
import org.mule.runtime.api.component.AbstractComponent;
import org.mule.runtime.api.ioc.ConfigurableObjectProvider;
import org.mule.runtime.api.ioc.ObjectProvider;
import org.mule.runtime.api.ioc.ObjectProviderConfiguration;
import org.mule.runtime.api.lifecycle.Disposable;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.module.artifact.api.classloader.RegionClassLoader;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Implementation of {@link ObjectProvider} that gives access to object to the mule artifact from an spring
 * {@link org.springframework.context.ApplicationContext}.
 * 
 * @since 1.0
 */
public class SpringConfig extends AbstractComponent
    implements ConfigurableObjectProvider, Disposable, NamedObject {

  private static final String SPRING_NAMESPACE_PREFIX = "org.springframework";
  private Map<String, String> parameters;
  private ClassPathXmlApplicationContext applicationContext;
  private String name;

  public void setParameters(Map<String, String> parameters) {
    this.name = parameters.get("name");
    checkState(this.name != null, "spring config name cannot be null");
    this.parameters = parameters;
  }

  private Optional<ClassLoader> getRegionClassLoader() {
    ClassLoader contextClassLoader = currentThread().getContextClassLoader();
    while (contextClassLoader != null) {
      if (RegionClassLoader.class.isAssignableFrom(contextClassLoader.getClass())) {
        return of(contextClassLoader);
      }
      contextClassLoader = contextClassLoader.getParent();
    }
    return empty();
  }

  @Override
  public void configure(ObjectProviderConfiguration configuration) {
    getRegionClassLoader().ifPresent(
                                     rcl -> withContextClassLoader(
                                                                   rcl,
                                                                   () -> {
                                                                     String files = parameters.get("files");
                                                                     String[] configFiles = files.split(",");
                                                                     applicationContext =
                                                                         new SpringModuleApplicationContext(configFiles,
                                                                                                            configuration);
                                                                     applicationContext
                                                                         .setClassLoader(rcl);
                                                                     applicationContext.refresh();
                                                                   }));
  }

  @Override
  public Optional<Object> getObject(String name) {
    try {
      if (!applicationContext.containsBean(name)) {
        return empty();
      }
      return of(applicationContext.getBean(name));
    } catch (NoSuchBeanDefinitionException e) {
      return empty();
    }
  }

  @Override
  public Optional<Object> getObjectByType(Class<?> objectType) {
    try {
      if (isSpringInternalType(objectType)) {
        return empty();
      }
      return of(applicationContext.getBean(objectType));
    } catch (NoSuchBeanDefinitionException e) {
      return empty();
    }
  }

  private boolean isSpringInternalType(Class<?> objectType) {
    return objectType.getClass().getName().startsWith(SPRING_NAMESPACE_PREFIX)
        || BeanPostProcessor.class.isAssignableFrom(objectType)
        || BeanFactoryPostProcessor.class.isAssignableFrom(objectType);
  }

  @Override
  public Optional<Boolean> isObjectSingleton(String name) {
    try {
      return of(applicationContext.isSingleton(name));
    } catch (NoSuchBeanDefinitionException e) {
      return empty();
    }
  }

  @Override
  public boolean containsObject(String name) {
    return applicationContext.containsBean(name);
  }

  @Override
  public <T> Map<String, T> getObjectsByType(Class<T> type) {
    if (isSpringInternalType(type)) {
      return emptyMap();
    }
    Map<String, T> beans = applicationContext.getBeansOfType(type);
    return beans.entrySet().stream().filter(entry -> applicationContext.getBeanFactory().containsBeanDefinition(entry.getKey()))
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public void dispose() {
    if (applicationContext != null) {
      applicationContext.destroy();
      applicationContext = null;
    }
  }

  @Override
  public String getName() {
    return name;
  }
}
