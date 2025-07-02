/*
 * Copyright 2025 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.internal.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomPreAuthenticationChecksTest {

  private CustomPreAuthenticationChecks preAuthChecks;
  private UserDetails userDetails;

  @Before
  public void setUp() {
    preAuthChecks = new CustomPreAuthenticationChecks();
    userDetails = mock(UserDetails.class);
  }

  @Test
  public void testValidUser() {
    when(userDetails.isAccountNonLocked()).thenReturn(true);
    when(userDetails.isEnabled()).thenReturn(true);
    when(userDetails.isAccountNonExpired()).thenReturn(true);

    preAuthChecks.check(userDetails);

    verify(userDetails).isAccountNonLocked();
    verify(userDetails).isEnabled();
    verify(userDetails).isAccountNonExpired();
  }

  @Test(expected = LockedException.class)
  public void testLockedAccount() {
    when(userDetails.isAccountNonLocked()).thenReturn(false);

    preAuthChecks.check(userDetails);
  }

  @Test(expected = DisabledException.class)
  public void testDisabledUser() {
    when(userDetails.isAccountNonLocked()).thenReturn(true);
    when(userDetails.isEnabled()).thenReturn(false);

    preAuthChecks.check(userDetails);
  }

  @Test(expected = AccountExpiredException.class)
  public void testExpiredAccount() {
    when(userDetails.isAccountNonLocked()).thenReturn(true);
    when(userDetails.isEnabled()).thenReturn(true);
    when(userDetails.isAccountNonExpired()).thenReturn(false);

    preAuthChecks.check(userDetails);
  }

  @Test
  public void testExceptionMessages() {
    try {
      when(userDetails.isAccountNonLocked()).thenReturn(false);
      preAuthChecks.check(userDetails);
    } catch (LockedException e) {
      assert e.getMessage().equals("User account is locked");
    }

    try {
      when(userDetails.isAccountNonLocked()).thenReturn(true);
      when(userDetails.isEnabled()).thenReturn(false);
      preAuthChecks.check(userDetails);
    } catch (DisabledException e) {
      assert e.getMessage().equals("User is disabled");
    }

    try {
      when(userDetails.isAccountNonLocked()).thenReturn(true);
      when(userDetails.isEnabled()).thenReturn(true);
      when(userDetails.isAccountNonExpired()).thenReturn(false);
      preAuthChecks.check(userDetails);
    } catch (AccountExpiredException e) {
      assert e.getMessage().equals("User account has expired");
    }
  }
}
