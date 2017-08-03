/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.internal.factory;

import static org.mule.runtime.api.util.Preconditions.checkArgument;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * A {@link FactoryBean} which returns a fixed instanced obtained through the constructor. {@link #isSingleton()} always returns
 * {@code true}.
 *
 * @param <T>
 * @since 1.0
 */
public class ConstantFactoryBean<T> implements FactoryBean<T> {

  private final T value;

  public ConstantFactoryBean(T value) {
    checkArgument(value != null, "value cannot be null");
    this.value = value;
  }

  @Override
  public T getObject() throws Exception {
    return value;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public Class<?> getObjectType() {
    return value.getClass();
  }

  /**
   * Helper method to create a bean definition from an already created object.
   * 
   * @param value the object to be returned by the factory bean
   * @return the {@link BeanDefinition}.
   */
  public static <T> BeanDefinition getBeanDefinition(T value) {
    return rootBeanDefinition(ConstantFactoryBean.class).addConstructorArgValue(value).getBeanDefinition();
  }
}
