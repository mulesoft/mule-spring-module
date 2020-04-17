/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.api;

import static java.lang.Thread.currentThread;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mule.extension.spring.AllureConstants.SpringFeature.SPRING_EXTENSION;
import static org.mule.extension.spring.AllureConstants.SpringFeature.ArtifactAndSpringModuleInteroperabilityStory.ARTIFACT_AND_SPRING_MODULE_INTEROPERABILITY;

import org.mule.runtime.api.component.ConfigurationProperties;
import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.runtime.api.ioc.ObjectProvider;
import org.mule.runtime.api.ioc.ObjectProviderConfiguration;
import org.mule.tck.junit4.AbstractMuleTestCase;

import java.io.FileNotFoundException;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;

@Feature(SPRING_EXTENSION)
@Story(ARTIFACT_AND_SPRING_MODULE_INTEROPERABILITY)
public class SpringConfigTestCase extends AbstractMuleTestCase {

  private static final String NAME = "name";
  private static final String FILES = "files";
  private static final String CONFIG_NAME = "SpringConfig";
  private static final String FILE_NAME = "beans.xml";

  private SpringConfig config;
  private ClassLoader originalClassLoader;

  @Before
  public void setup() {
    config = new SpringConfig();
    originalClassLoader = currentThread().getContextClassLoader();
  }

  @After
  public void tearDown() {
    currentThread().setContextClassLoader(originalClassLoader);
  }

  @Description("SpringConfig should use current thread Context ClassLoader to load resources")
  @Test
  public void springConfigShouldUseThreadContextClassLoader() {
    // Mock class loader
    URLClassLoader fakeClassLoader = mock(URLClassLoader.class);
    currentThread().setContextClassLoader(fakeClassLoader);

    // Set parameters
    Map<String, String> parameters = new HashMap<>();
    parameters.put(NAME, CONFIG_NAME);
    parameters.put(FILES, FILE_NAME);
    config.setParameters(parameters);

    ConfigurationProperties configurationProperties = mock(ConfigurationProperties.class);
    ObjectProvider artifactObjectProvider = mock(ObjectProvider.class);
    ObjectProviderConfiguration configuration =
        new ImmutableObjectProviderConfiguration(configurationProperties, artifactObjectProvider);

    try {
      config.configure(configuration);
      fail("Expected exception");
    } catch (MuleRuntimeException e) {
      assertEquals(FileNotFoundException.class, getRootCause(e).getClass());
    }

    verify(fakeClassLoader, atLeastOnce()).getResourceAsStream(anyString());

    // Restore context ClassLoader
    currentThread().setContextClassLoader(originalClassLoader);
    config.configure(configuration);

    Optional<Object> customer = config.getObject("customer");

    assertThat(CONFIG_NAME, is(config.getName()));
    assertThat("customer name", is(((Customer) customer.get()).getName()));
  }

  public static class Customer {

    private String name;

    public Customer() {}

    public void setName(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  /**
   * Immutable implementation of {@link ObjectProviderConfiguration}
   */
  public class ImmutableObjectProviderConfiguration implements ObjectProviderConfiguration {

    private final ObjectProvider artifactObjectProvider;
    private final ConfigurationProperties configurationProperties;

    public ImmutableObjectProviderConfiguration(ConfigurationProperties configurationProperties,
                                                ObjectProvider artifactObjectProvider) {
      this.configurationProperties = configurationProperties;
      this.artifactObjectProvider = artifactObjectProvider;
    }

    @Override
    public ObjectProvider getArtifactObjectProvider() {
      return artifactObjectProvider;
    }

    @Override
    public ConfigurationProperties getConfigurationProperties() {
      return configurationProperties;
    }

  }
}


