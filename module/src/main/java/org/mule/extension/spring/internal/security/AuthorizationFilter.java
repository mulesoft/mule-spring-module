/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.internal.security;

import static org.mule.extension.spring.internal.config.i18n.SpringSecurityMessages.noGrantedAuthority;
import static org.mule.extension.spring.internal.config.i18n.SpringSecurityMessages.springAuthenticationRequired;
import static org.mule.runtime.core.api.config.i18n.CoreMessages.authNoCredentials;

import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.security.Authentication;
import org.mule.runtime.api.security.SecurityException;
import org.mule.runtime.api.security.SecurityProviderNotFoundException;
import org.mule.runtime.api.security.UnknownAuthenticationTypeException;
import org.mule.runtime.core.api.event.CoreEvent;
import org.mule.runtime.core.api.security.AbstractSecurityFilter;
import org.mule.runtime.core.api.security.CryptoFailureException;
import org.mule.runtime.core.api.security.EncryptionStrategyNotFoundException;
import org.mule.runtime.api.security.NotPermittedException;
import org.mule.runtime.api.security.SecurityContext;
import org.mule.runtime.api.security.UnauthorisedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Authorizes user access based on the required authorities for a user.
 */
public class AuthorizationFilter extends AbstractSecurityFilter {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private Collection<String> requiredAuthorities = new HashSet<>();

  public AuthorizationFilter(Collection<String> requiredAuthorities) {
    this.requiredAuthorities = requiredAuthorities;
  }

  @Override
  public SecurityContext doFilter(CoreEvent event)
      throws SecurityException, UnknownAuthenticationTypeException, CryptoFailureException,
      SecurityProviderNotFoundException, EncryptionStrategyNotFoundException, InitialisationException {
    Authentication auth = event.getSecurityContext().getAuthentication();
    if (auth == null) {
      throw new UnauthorisedException(authNoCredentials());
    }

    if (!(auth instanceof SpringAuthenticationAdapter)) {
      throw new UnauthorisedException(springAuthenticationRequired());
    }

    SpringAuthenticationAdapter springAuth = (SpringAuthenticationAdapter) auth;

    String principalName = springAuth.getName();
    GrantedAuthority[] authorities = springAuth.getAuthorities();

    // If the principal has at least one of the granted authorities,
    // then return.
    boolean authorized = false;
    if (authorities != null) {
      if (logger.isDebugEnabled()) {
        logger.debug("Found authorities '" + Arrays.toString(authorities) + "' for principal '" + principalName + "'.");
      }

      for (GrantedAuthority authority : authorities) {
        if (requiredAuthorities.contains(authority.getAuthority())) {
          authorized = true;
        }
      }
    }

    if (!authorized) {
      logger.info(MessageFormat
          .format("Could not find required authorities for {0}. Required authorities: {1}. Authorities found: {2}.",
                  principalName, Arrays.toString(requiredAuthorities.toArray()), Arrays.toString(authorities)));
      throw new NotPermittedException(noGrantedAuthority(principalName));
    }

    return event.getSecurityContext();
  }

}
