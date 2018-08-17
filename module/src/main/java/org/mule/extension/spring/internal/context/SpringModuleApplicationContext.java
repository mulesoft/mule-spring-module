/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.internal.context;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;
import static org.springframework.context.annotation.AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME;
import org.mule.extension.spring.api.ArtifactPropertiesPlaceholder;
import org.mule.extension.spring.internal.beanfactory.ArtifactObjectsAwareBeanFactory;
import org.mule.extension.spring.internal.inject.MuleAwareObjectsInjectorProcessor;
import org.mule.runtime.api.ioc.ObjectProviderConfiguration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * {@link ClassPathXmlApplicationContext} extension that configures {@link org.springframework.beans.factory.BeanFactory} to use
 * {@link ArtifactObjectsAwareBeanFactory} so objects from the spring configuration can see object from the mule artifact.
 * <p/>
 * In addition, it adds some {@link org.springframework.beans.factory.config.BeanPostProcessor} to add interoperability with
 * features in the runtime as the mechanism to process placeholders.
 * 
 * @since 1.0
 */
public class SpringModuleApplicationContext extends ClassPathXmlApplicationContext {

  private static final String AUTOWIRED_POST_PROCESSOR_OBJECT_KEY = "_autowiredPostProcessor";
  private static final String ARTIFACT_PROPERTY_PLACEHOLDER_OBJECT_KEY = "_artifactPropertyPlaceholder";

  private ObjectProviderConfiguration configuration;
  private ArtifactObjectsAwareBeanFactory beanFactory;

  public SpringModuleApplicationContext(String[] configLocations, ObjectProviderConfiguration configuration)
      throws BeansException {
    super(configLocations, false);
    this.configuration = configuration;
  }

  @Override
  protected DefaultListableBeanFactory createBeanFactory() {
    beanFactory = new ArtifactObjectsAwareBeanFactory(getInternalParentBeanFactory(), configuration.getArtifactObjectProvider());
    return beanFactory;
  }

  @Override
  protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    super.prepareBeanFactory(beanFactory);
    DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
    defaultListableBeanFactory.registerBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME,
                                                      rootBeanDefinition(MuleAwareObjectsInjectorProcessor.class)
                                                          .getBeanDefinition());
    defaultListableBeanFactory.setAutowireCandidateResolver(new QualifierAnnotationAutowireCandidateResolver());
    defaultListableBeanFactory.registerBeanDefinition(AUTOWIRED_POST_PROCESSOR_OBJECT_KEY,
                                                      rootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class)
                                                          .getBeanDefinition());
    registerArtifactPropertiesPlaceholder(defaultListableBeanFactory, configuration);
  }

  private void registerArtifactPropertiesPlaceholder(DefaultListableBeanFactory defaultListableBeanFactory,
                                                     ObjectProviderConfiguration configuration) {
    BeanDefinitionBuilder artifactPlaceholderBeanDefinitionBuilder =
        rootBeanDefinition(ArtifactPropertiesPlaceholder.class)
            .addConstructorArgValue(configuration.getConfigurationProperties());
    defaultListableBeanFactory.registerBeanDefinition(ARTIFACT_PROPERTY_PLACEHOLDER_OBJECT_KEY,
                                                      artifactPlaceholderBeanDefinitionBuilder.getBeanDefinition());
  }

  @Override
  public void destroy() {
    beanFactory.markForDestroy();
    super.destroy();
  }
}
