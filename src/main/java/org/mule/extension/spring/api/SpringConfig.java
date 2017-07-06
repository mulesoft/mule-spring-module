/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.api;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import org.mule.runtime.api.ioc.ObjectProvider;
import org.mule.runtime.api.ioc.ObjectProviderConfiguration;
import org.mule.runtime.api.lifecycle.Disposable;
import org.mule.runtime.api.meta.AbstractAnnotatedObject;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;
import java.util.Optional;

public class SpringConfig extends AbstractAnnotatedObject implements ObjectProvider, Disposable {

  private Map<String, String> parameters;
  private ClassPathXmlApplicationContext applicationContext;

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  @Override
  public void configure(ObjectProviderConfiguration configuration) {
    String files = parameters.get("files");
    String[] configFiles = files.split(",");
    applicationContext = new ClassPathXmlApplicationContext(configFiles) {

      @Override
      protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        defaultListableBeanFactory.setAutowireCandidateResolver(new QualifierAnnotationAutowireCandidateResolver());
        defaultListableBeanFactory.registerBeanDefinition("_autowiredPostProcessor", BeanDefinitionBuilder
            .rootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class).getBeanDefinition());
        registerArtifactObjects(beanFactory, configuration);
        registerArtifactPropertiesPlaceholder(defaultListableBeanFactory, configuration);
      }
    };
  }

  private void registerArtifactPropertiesPlaceholder(DefaultListableBeanFactory defaultListableBeanFactory,
                                                     ObjectProviderConfiguration configuration) {
    BeanDefinitionBuilder artifactPlaceholderBeanDefinitionBuilder =
        BeanDefinitionBuilder.rootBeanDefinition(ArtifactPropertiesPlaceholder.class)
            .addConstructorArgValue(configuration.getConfigurationProperties());
    defaultListableBeanFactory.registerBeanDefinition("_artifactPropertyPlaceholder",
                                                      artifactPlaceholderBeanDefinitionBuilder.getBeanDefinition());
  }

  private void registerArtifactObjects(ConfigurableListableBeanFactory beanFactory,
                                       ObjectProviderConfiguration configuration) {
    configuration.getArtifactObjects().entrySet().stream()
        .forEach(entry -> {
          beanFactory.registerSingleton(entry.getKey(), entry.getValue());
        });
  }

  @Override
  public Optional<Object> getObject(String name) {
    try {
      return of(applicationContext.getBean(name));
    } catch (NoSuchBeanDefinitionException e) {
      return empty();
    }
  }

  @Override
  public Optional<Object> getObjectByType(Class<?> objectType) {
    try {
      return of(applicationContext.getBean(objectType));
    } catch (NoSuchBeanDefinitionException e) {
      return empty();
    }
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
  public <T> Map<String, T> getObjectsByType(Class<T> type) {
    return applicationContext.getBeansOfType(type);
  }

  @Override
  public void dispose() {
    if (applicationContext != null) {
      applicationContext.destroy();
      applicationContext = null;
    }
  }
}
