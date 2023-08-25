/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test.parsers;

import static org.hamcrest.Matchers.containsString;

import org.mule.extension.spring.test.SpringPluginFunctionalTestCase;
import org.mule.runtime.core.api.MuleContext;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DuplicateBeanNameTestCase extends SpringPluginFunctionalTestCase {

  @Rule
  public ExpectedException expected = ExpectedException.none();

  @Override
  protected boolean doTestClassInjection() {
    return false;
  }

  @Override
  protected MuleContext createMuleContext() throws Exception {
    return null;
  }

  public void assertErrorContains(String phrase) throws Exception {
    expected.expectMessage(containsString(phrase));

    parseConfig();
  }

  protected void parseConfig() throws Exception {
    super.createMuleContext();
  }

  @Override
  protected String getConfigFile() {
    return "parsers/duplicate-bean-name-test.xml";
  }

  @Test
  public void testBeanError() throws Exception {
    assertErrorContains("Bean name 'child1' is already used in this <beans> element");
  }

}
