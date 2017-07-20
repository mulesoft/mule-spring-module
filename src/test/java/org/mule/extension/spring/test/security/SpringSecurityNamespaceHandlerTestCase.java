/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test.security;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

import org.mule.extension.spring.api.security.SpringProviderAdapter;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.config.spring.CustomSecurityProviderDelegate;
import org.mule.runtime.core.api.security.SecurityManager;
import org.mule.runtime.core.api.security.SecurityProvider;

import java.util.Iterator;

import org.junit.Test;
import org.slf4j.Logger;

public class SpringSecurityNamespaceHandlerTestCase extends MuleArtifactFunctionalTestCase {

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

  @Test
  public void testCustom() {
    Iterator<SecurityProvider> providers = muleContext.getSecurityManager().getProviders().iterator();
    while (providers.hasNext()) {
      SecurityProvider provider = providers.next();
      LOGGER.debug(provider.getName());
    }

    knownProperties(getProvider("willAlsoOverwriteName"));
    knownProperties(getProvider("willOverwriteName"));
  }

  protected void knownProperties(SecurityProvider provider) {
    assertNotNull(provider);
    assertTrue(provider instanceof CustomSecurityProviderDelegate);
  }

}
