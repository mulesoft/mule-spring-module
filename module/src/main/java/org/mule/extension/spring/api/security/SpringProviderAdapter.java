/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.api.security;

import org.mule.extension.spring.internal.security.SecurityProperty;
import org.mule.extension.spring.internal.security.SpringAuthenticationAdapter;
import org.mule.extension.spring.internal.security.SpringSecurityContextFactory;
import org.mule.extension.spring.internal.util.ExcludeFromGeneratedCoverage;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.security.Authentication;
import org.mule.runtime.api.security.SecurityException;
import org.mule.runtime.core.api.security.AbstractSecurityProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticationException;


/**
 * <code>SpringProviderAdapter</code> is a wrapper for a Spring Security provider to use with the SecurityManager
 * 
 * @since 1.0
 */
public class SpringProviderAdapter extends AbstractSecurityProvider implements AuthenticationProvider {

  private AuthenticationManager delegate;
  private Map securityProperties;
  private SpringAuthenticationProvider authenticationProvider;

  /** For Spring IoC only */
  public SpringProviderAdapter() {
    super("spring-security");
  }

  public SpringProviderAdapter(AuthenticationManager delegate) {
    this(delegate, "spring-security");
  }

  public SpringProviderAdapter(AuthenticationManager delegate, String name) {
    super(name);
    this.delegate = delegate;
  }

  @Override
  protected void doInitialise() throws InitialisationException {
    setSecurityContextFactory(new SpringSecurityContextFactory());
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws SecurityException {
    org.springframework.security.core.Authentication auth = null;
    if (authentication instanceof SpringAuthenticationAdapter) {
      auth = ((SpringAuthenticationAdapter) authentication).getDelegate();
    } else {
      auth = this.getAuthenticationProvider().getAuthentication(authentication);

    }
    auth = delegate.authenticate(auth);
    return new SpringAuthenticationAdapter(auth, getSecurityProperties());
  }

  @Override
  public org.springframework.security.core.Authentication authenticate(org.springframework.security.core.Authentication authentication)
      throws AuthenticationException {
    return delegate.authenticate(authentication);
  }

  public AuthenticationManager getDelegate() {
    return delegate;
  }

  public void setDelegate(AuthenticationManager delegate) {
    this.delegate = delegate;
  }

  public Map getSecurityProperties() {
    return securityProperties;
  }

  public void setSecurityProperties(List<SecurityProperty> securityProperties) {
    this.securityProperties = new HashMap();
    for (SecurityProperty securityProperty : securityProperties) {
      this.securityProperties.put(securityProperty.getName(), securityProperty.getValue());
    }
  }

  public SpringAuthenticationProvider getAuthenticationProvider() {
    if (this.authenticationProvider == null) {
      this.authenticationProvider = new UserAndPasswordAuthenticationProvider();
    }
    return authenticationProvider;
  }

  public void setAuthenticationProvider(SpringAuthenticationProvider authenticationProvider) {
    this.authenticationProvider = authenticationProvider;
  }

  @ExcludeFromGeneratedCoverage
  public void setSecurityProperties(Map securityProperties) {
    this.securityProperties = securityProperties;
  }

}
