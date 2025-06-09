package org.mule.extension.spring.internal.util;

import static org.mockito.Mockito.*;

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
