/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
import org.mule.extension.spring.internal.util.CompositeClassLoader;
import org.mule.extension.spring.internal.util.ExcludeFromGeneratedCoverage;
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

  @Override
  public void configure(ObjectProviderConfiguration configuration) {
    // Use RegionClassLoader to ensure Spring module right access to resources and only access classes exported in the application
    // Using MuleArtifactClassLoader fails on get Spring module resources when it's defined on both application and domain.
    final ClassLoader regionClassLoader = getRegionClassLoader();
    final ClassLoader springClassLoader = SpringModuleApplicationContext.class.getSuperclass().getClassLoader();

    ClassLoader springAppCtxClassLoader;
    if (!springClassLoader.equals(regionClassLoader)) {
      springAppCtxClassLoader = new CompositeClassLoader(regionClassLoader, springClassLoader);
    } else {
      springAppCtxClassLoader = regionClassLoader;
    }

    withContextClassLoader(springAppCtxClassLoader, () -> {
      String files = parameters.get("files");
      String[] configFiles = files.split(",");
      applicationContext = new SpringModuleApplicationContext(configFiles, configuration);
      applicationContext.setClassLoader(springAppCtxClassLoader);
      applicationContext.refresh();
    });
  }

  /**
   * Find RegionClassLoader based on ArtifactClassLoader. If RegionClassLoader is not found on ArtifactClassLoader's hierarchy
   * ArtifactClassLoader will be returned instead. This ensure Spring module only access to exported classes and resources.
   *
   * Using Thread.currentContext.getContextClassLoader() enables access to NOT exported classes.
   * 
   * @return RegionClassLoader
   */
  private ClassLoader getRegionClassLoader() {
    ClassLoader artifactClassLoader = currentThread().getContextClassLoader();
    return getRegionClassLoader(artifactClassLoader, artifactClassLoader);
  }

  /**
   * Try to find RegionClassLoader based on <code>current</code> argument.
   *
   * @param base    Default classLoader to be returned if RegionClassLoader is not found on <code>current</code> argument
   * @param current The initial ClassLoader.
   * @return RegionClassLoader or <code>base</code> argument if RegionClassLoader is not found on <code>current</code> argument
   *         classloader's hierarchy
   */
  private ClassLoader getRegionClassLoader(ClassLoader base, ClassLoader current) {
    if (current instanceof RegionClassLoader) {
      return current;
    }
    if (current.getParent() == null) {
      return base;
    }
    return getRegionClassLoader(base, current.getParent());
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

  @ExcludeFromGeneratedCoverage
  public Map<String, String> getParameters() {
    return parameters;
  }

  @ExcludeFromGeneratedCoverage
  public ClassPathXmlApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @ExcludeFromGeneratedCoverage
  public void setApplicationContext(ClassPathXmlApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public void setName(String name) {
    this.name = name;
  }
}
