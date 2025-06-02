/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.internal.util;

import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

public class CustomPostAuthenticationChecks implements UserDetailsChecker {

  @Override
  public void check(UserDetails user) {
    if (!user.isCredentialsNonExpired()) {
      throw new CredentialsExpiredException("User credentials have expired");
    }
  }
}
