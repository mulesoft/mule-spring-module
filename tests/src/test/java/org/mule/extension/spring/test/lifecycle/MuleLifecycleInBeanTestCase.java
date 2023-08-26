/*
 * Copyright © MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test.lifecycle;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mule.extension.spring.AllureConstants.SpringFeature.LifecycleAndDependencyInjectionStory.LIFECYCLE_AND_DEPENDENCY_INJECTION;
import static org.mule.extension.spring.AllureConstants.SpringFeature.SPRING_EXTENSION;
import org.mule.extension.spring.test.SpringPluginFunctionalTestCase;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.core.api.MuleContext;
import org.mule.tck.junit4.rule.SystemProperty;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.ClassRule;
import org.junit.Test;

@Feature(SPRING_EXTENSION)
@Story(LIFECYCLE_AND_DEPENDENCY_INJECTION)
public class MuleLifecycleInBeanTestCase extends SpringPluginFunctionalTestCase {

  private static boolean testRaisedWantedException = false;

  @ClassRule
  public static SystemProperty springConfig = new SystemProperty("spring.config.file", "spring-lifecycle-failing-config.xml");


  @Override
  protected boolean doTestClassInjection() {
    return false;
  }

  @Override
  protected String getConfigFile() {
    return "mule-spring-config.xml";
  }

  @Override
  protected void doSetUp() throws Exception {
    //do nothing
  }


  @Override
  protected MuleContext createMuleContext() throws Exception {
    try {
      super.createMuleContext();
    } catch (InitialisationException e) {
      assertThat(e.getDetailedMessage(), containsString("should not implement mule Lifecycle"));
      testRaisedWantedException = true;
    }
    return null;
  }

  /*
    What we actually want to test is that there is an exception when parsing the xml file during the test class setup.
   */
  @Test
  public void muleLifecycleBeanIsInvalid() throws Exception {
    if (!testRaisedWantedException) {
      fail("Test should have failed with expected exception");
    }
  }

}
