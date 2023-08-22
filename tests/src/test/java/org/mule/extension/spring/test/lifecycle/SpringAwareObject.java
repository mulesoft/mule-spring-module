/*
 * Copyright © MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringValueResolver;

public class SpringAwareObject implements EnvironmentAware, EmbeddedValueResolverAware, ResourceLoaderAware,
    ApplicationEventPublisherAware, MessageSourceAware, ApplicationContextAware {


  private ApplicationContext applicationContext;
  private ApplicationEventPublisher applicationEventPublisher;
  private StringValueResolver embeddedValueResolver;
  private Environment environment;
  private MessageSource messageSource;
  private ResourceLoader resourceLoader;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
    this.embeddedValueResolver = stringValueResolver;
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public ApplicationEventPublisher getApplicationEventPublisher() {
    return applicationEventPublisher;
  }

  public StringValueResolver getEmbeddedValueResolver() {
    return embeddedValueResolver;
  }

  public Environment getEnvironment() {
    return environment;
  }

  public MessageSource getMessageSource() {
    return messageSource;
  }

  public ResourceLoader getResourceLoader() {
    return resourceLoader;
  }
}

