/*
 * Copyright © MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
