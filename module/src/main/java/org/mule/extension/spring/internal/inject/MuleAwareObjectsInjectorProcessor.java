/*
 * Copyright © MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
