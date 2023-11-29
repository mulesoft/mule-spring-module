/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.api.security;

import org.mule.runtime.api.security.Authentication;

/**
 * A provider for spring authentication
 * 
 * @since 1.0
 */
public interface SpringAuthenticationProvider {

  /**
   * Provides a spring authentication according to mule's authentication
   * 
   * @param authentication the mule's authentication
   * @return the spring's authentication
   */
  org.springframework.security.core.Authentication getAuthentication(Authentication authentication);
}


