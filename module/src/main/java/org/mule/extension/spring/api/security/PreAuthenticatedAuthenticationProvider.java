/*
 * Copyright © MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;


/**
 * Provides a pre authenticated authentication
 * 
 * @since 1.0
 */
public class PreAuthenticatedAuthenticationProvider implements SpringAuthenticationProvider {

  @Override
  public Authentication getAuthentication(org.mule.runtime.api.security.Authentication authentication) {
    return new PreAuthenticatedAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials());
  }
}


