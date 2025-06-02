/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.internal.security;

import org.mule.runtime.api.security.Authentication;
import org.mule.runtime.api.security.SecurityContext;
import org.mule.runtime.core.api.security.SecurityContextFactory;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

/**
 * <code>SpringSecurityContextFactory</code> creates an SpringSecurityContext for an Authentication object.
 *
 * @since 1.0
 */
public class SpringSecurityContextFactory implements SecurityContextFactory {

  @Override
  public SecurityContext create(Authentication authentication) {
    org.springframework.security.core.context.SecurityContext context = new SecurityContextImpl();
    context.setAuthentication(((SpringAuthenticationAdapter) authentication).getDelegate());

    if (authentication.getProperties() != null) {
      if (authentication.getProperties().containsKey("securityMode")) {
        String securityMode = (String) authentication.getProperties().get("securityMode");
        SecurityContextHolder.setStrategyName(securityMode);
      }
    }
    SecurityContextHolder.setContext(context);
    return new SpringSecurityContext(context);
  }
}
