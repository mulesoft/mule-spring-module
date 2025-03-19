/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
