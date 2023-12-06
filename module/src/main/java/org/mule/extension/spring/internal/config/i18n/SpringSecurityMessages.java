/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.internal.config.i18n;

import org.mule.runtime.api.i18n.I18nMessage;
import org.mule.runtime.api.i18n.I18nMessageFactory;

public class SpringSecurityMessages extends I18nMessageFactory {

  private static final SpringSecurityMessages factory = new SpringSecurityMessages();

  private static final String BUNDLE_PATH = getBundlePath("spring-security");

  public static I18nMessage noGrantedAuthority(String authority) {
    return factory.createMessage(BUNDLE_PATH, 3, authority);
  }

  public static I18nMessage springAuthenticationRequired() {
    return factory.createMessage(BUNDLE_PATH, 4);
  }
}


