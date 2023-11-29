/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.api;

import org.mule.extension.spring.internal.util.ExcludeFromGeneratedCoverage;
import org.mule.runtime.api.component.ConfigurationProperties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * {@link PropertyPlaceholderConfigurer} implementation that resolves properties against the mule artifact properties where the
 * spring module is defined.
 * 
 * @since 1.0
 */
public class ArtifactPropertiesPlaceholder extends PropertyPlaceholderConfigurer {

  private final ConfigurationProperties configurationProperties;

  /**
   * @param configurationProperties {@link ConfigurationProperties} of the artifact
   */
  public ArtifactPropertiesPlaceholder(ConfigurationProperties configurationProperties) {
    this.configurationProperties = configurationProperties;
    this.ignoreUnresolvablePlaceholders = true;
  }

  @Override
  protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
    return configurationProperties.resolveStringProperty(placeholder).orElse(null);
  }

  @Override
  protected String resolvePlaceholder(String placeholder, Properties props) {
    return configurationProperties.resolveStringProperty(placeholder).orElse(null);
  }

  @Override
  public int getOrder() {
    return HIGHEST_PRECEDENCE;
  }

  @ExcludeFromGeneratedCoverage
  public ConfigurationProperties getConfigurationProperties() {
    return configurationProperties;
  }
}
