/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.internal.beanfactory;

import org.mule.runtime.api.ioc.ObjectProvider;

import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    } else if (descriptor.getCollectionType() != null) {
      // When a found dependency is a collection then we must also search for all the possible objects in the
      // artifact ObjectProvider.
      Class<?> dependencyType = Collection.class.isAssignableFrom(descriptor.getDependencyType()) ? descriptor.getCollectionType()
          : descriptor.getDependencyType();
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
    if (containsBean(name) || !artifactObjectProvider.containsObject(name) || destroying) {
      return super.doGetBean(name, requiredType, args, typeCheckOnly);
    } else {
      return (T) artifactObjectProvider.getObject(name).get();
    }
  }

  /**
   * Marks the beginning of destroy of the context.
   */
  public void markForDestroy() {
    this.destroying = true;
  }
}
