/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.security;

import org.mule.runtime.api.security.Authentication;
import org.mule.runtime.api.security.SecurityContext;
import org.mule.runtime.api.security.SecurityException;
import org.mule.runtime.api.security.UnknownAuthenticationTypeException;
import org.mule.runtime.core.api.security.SecurityProvider;

public class CustomSecurityProvider implements SecurityProvider {
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
