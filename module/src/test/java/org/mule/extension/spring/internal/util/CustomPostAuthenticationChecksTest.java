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
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomPostAuthenticationChecksTest {

  private CustomPostAuthenticationChecks checker;
  private UserDetails userDetails;

  @Before
  public void setUp() {
    checker = new CustomPostAuthenticationChecks();
    userDetails = mock(UserDetails.class);
  }

  @Test
  public void testCheckWithNonExpiredCredentials() {
    when(userDetails.isCredentialsNonExpired()).thenReturn(true);

    checker.check(userDetails);

    verify(userDetails).isCredentialsNonExpired();
  }

  @Test(expected = CredentialsExpiredException.class)
  public void testCheckWithExpiredCredentials() {
    when(userDetails.isCredentialsNonExpired()).thenReturn(false);

    checker.check(userDetails);
  }

  @Test
  public void testCheckWithExpiredCredentialsExceptionMessage() {
    when(userDetails.isCredentialsNonExpired()).thenReturn(false);

    try {
      checker.check(userDetails);
    } catch (CredentialsExpiredException e) {
      assert e.getMessage().equals("User credentials have expired");
    }
  }

  @Test(expected = NullPointerException.class)
  public void testCheckWithNullUserDetails() {
    checker.check(null);
  }
}
