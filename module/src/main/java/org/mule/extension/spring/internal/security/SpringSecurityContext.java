/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.internal.security;

import org.mule.runtime.api.security.Authentication;
import org.mule.runtime.api.security.SecurityContext;

import org.springframework.security.core.context.SecurityContextHolder;


/**
 * <code>SpringSecurityContext</code> is a SecurityContext wrapper used to interface with an Spring's
 * {@link org.springframework.security.core.context.SecurityContext}.
 * 
 * @since 1.0
 */
public class SpringSecurityContext implements SecurityContext {

  private static final long serialVersionUID = 1721127514499593129L;

  private org.springframework.security.core.context.SecurityContext delegate;
  private SpringAuthenticationAdapter authentication;

  public SpringSecurityContext(org.springframework.security.core.context.SecurityContext delegate) {
    this.delegate = delegate;
    SecurityContextHolder.setContext(this.delegate);
  }

  @Override
  public void setAuthentication(Authentication authentication) {
    this.authentication = ((SpringAuthenticationAdapter) authentication);
    delegate.setAuthentication(this.authentication.getDelegate());
    SecurityContextHolder.setContext(delegate);
  }

  @Override
  public Authentication getAuthentication() {
    return this.authentication;
  }
}
