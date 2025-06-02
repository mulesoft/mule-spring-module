/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.internal.util;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

public class CustomPreAuthenticationChecks implements UserDetailsChecker {

  @Override
  public void check(UserDetails user) {
    if (!user.isAccountNonLocked()) {
      throw new LockedException("User account is locked");
    }
    if (!user.isEnabled()) {
      throw new DisabledException("User is disabled");
    }
    if (!user.isAccountNonExpired()) {
      throw new AccountExpiredException("User account has expired");
    }
  }
}
