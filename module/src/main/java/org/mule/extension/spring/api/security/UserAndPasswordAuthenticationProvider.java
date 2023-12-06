/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.api.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * A spring authentication provider that return
 * 
 * @since 1.0
 */
public class UserAndPasswordAuthenticationProvider implements SpringAuthenticationProvider {

  @Override
  public Authentication getAuthentication(org.mule.runtime.api.security.Authentication authentication) {
    return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials());
  }
}


