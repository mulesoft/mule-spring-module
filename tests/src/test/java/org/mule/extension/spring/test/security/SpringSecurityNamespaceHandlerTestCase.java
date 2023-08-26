/*
 * Copyright © MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test.security;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mule.extension.spring.AllureConstants.SpringFeature.SPRING_EXTENSION;
import static org.mule.extension.spring.AllureConstants.SpringFeature.SpringSecurityStory.SPRING_SECURITY_STORY;
import static org.slf4j.LoggerFactory.getLogger;

import org.mule.extension.spring.api.security.SpringProviderAdapter;
import org.mule.extension.spring.test.SpringPluginFunctionalTestCase;
import org.mule.runtime.api.security.Authentication;
import org.mule.runtime.api.security.SecurityContext;
import org.mule.runtime.api.security.SecurityException;
import org.mule.runtime.api.security.UnknownAuthenticationTypeException;
import org.mule.runtime.core.api.security.SecurityManager;
import org.mule.runtime.core.api.security.SecurityProvider;

import org.junit.Test;
import org.slf4j.Logger;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;

@Feature(SPRING_EXTENSION)
@Story(SPRING_SECURITY_STORY)
public class SpringSecurityNamespaceHandlerTestCase extends SpringPluginFunctionalTestCase {

  private static final Logger LOGGER = getLogger(SpringSecurityNamespaceHandlerTestCase.class);

  @Override
  protected String getConfigFile() {
    return "security/spring-security-namespace-config.xml";
  }

  @Test
  public void testProvider() {
    SecurityProvider provider = getProvider("memory-dao");
    assertNotNull(provider);
    assertThat(provider, instanceOf(SpringProviderAdapter.class));
  }

  protected SecurityProvider getProvider(String providerName) {
    SecurityManager securityManager = muleContext.getSecurityManager();
    return securityManager.getProvider(providerName);
  }

  public static class CustomSecurityProvider implements SecurityProvider {

    private String name;

    public CustomSecurityProvider() {
      this("dummyProvider");
    }

    public CustomSecurityProvider(String name) {
      setName(name);
    }

    @Override
    public void setName(String name) {
      this.name = name;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws SecurityException {
      return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
      return false;
    }

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public SecurityContext createSecurityContext(Authentication auth) throws UnknownAuthenticationTypeException {
      return null;
    }
  }

}
