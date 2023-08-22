/*
 * Copyright © MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test.cache;

import org.mule.extension.spring.test.SpringPluginFunctionalTestCase;
import org.mule.tck.junit4.rule.SystemProperty;

import org.junit.ClassRule;
import org.junit.Test;

import io.qameta.allure.Description;

public class SpringCacheTestCase extends SpringPluginFunctionalTestCase {

  @ClassRule
  public static SystemProperty springConfig = new SystemProperty("spring.config.file", "spring-cache-config.xml");

  @Override
  protected String getConfigFile() {
    return "mule-spring-config.xml";
  }

  @Test
  @Description("Check that the beans config file that uses the cache namespace can be properly parsed")
  public void startUp() {
    // Nothing to do
  }
}
