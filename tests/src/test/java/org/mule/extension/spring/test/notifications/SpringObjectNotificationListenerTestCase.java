/*
 * Copyright © MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test.notifications;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mule.extension.spring.AllureConstants.SpringFeature.SPRING_EXTENSION;
import static org.mule.extension.spring.AllureConstants.SpringFeature.ArtifactAndSpringModuleInteroperabilityStory.ARTIFACT_AND_SPRING_MODULE_INTEROPERABILITY;

import org.mule.extension.spring.test.SpringPluginFunctionalTestCase;

import org.junit.Test;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;

@Feature(SPRING_EXTENSION)
@Story(ARTIFACT_AND_SPRING_MODULE_INTEROPERABILITY)
public class SpringObjectNotificationListenerTestCase extends SpringPluginFunctionalTestCase {

  @Override
  protected String getConfigFile() {
    return "notification-config.xml";
  }

  @Test
  public void notificationObjectsInSpringConfigArePickUpByMule() throws Exception {
    flowRunner("flow").run();
    assertThat(registry.lookupByType(ProcessorNotificationStore.class).get().getNotifications(), hasSize(2));
  }

}
