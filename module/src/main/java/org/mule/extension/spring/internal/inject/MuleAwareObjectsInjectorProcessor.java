/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.internal.inject;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.MergedAnnotation;

/**
 * Specialization of {@link AutowiredAnnotationBeanPostProcessor} which acts as the default injection post processors to be used
 * in Mule.
 *
 * @since 1.0
 */
public class MuleAwareObjectsInjectorProcessor extends AutowiredAnnotationBeanPostProcessor {

  /**
   * No-Op method
   */
  @Override
  public final void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {}

  // README:
  // https://docs.spring.io/spring-framework/docs/5.2.1.RELEASE/javadoc-api/org/springframework/beans/factory/annotation/AutowiredAnnotationBeanPostProcessor.html#determineRequiredStatus-org.springframework.core.annotation.AnnotationAttributes-
  @Override
  protected boolean determineRequiredStatus(MergedAnnotation ann) {
    return false;
  }
}
