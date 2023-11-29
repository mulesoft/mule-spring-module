/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.internal.inject;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;

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

  @Override
  protected boolean determineRequiredStatus(AnnotationAttributes ann) {
    return false;
  }
}
