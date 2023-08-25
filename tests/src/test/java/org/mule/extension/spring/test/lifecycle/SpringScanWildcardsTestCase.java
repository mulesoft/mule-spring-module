/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test.lifecycle;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mule.extension.spring.AllureConstants.SpringFeature.LifecycleAndDependencyInjectionStory.LIFECYCLE_AND_DEPENDENCY_INJECTION;
import static org.mule.extension.spring.AllureConstants.SpringFeature.SPRING_EXTENSION;
import org.mule.extension.spring.test.SpringPluginFunctionalTestCase;
import org.mule.extension.spring.test.scan.ScanObject;
import org.mule.runtime.api.artifact.Registry;

import java.util.Optional;

import javax.inject.Inject;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

@Feature(SPRING_EXTENSION)
@Story(LIFECYCLE_AND_DEPENDENCY_INJECTION)
public class SpringScanWildcardsTestCase extends SpringPluginFunctionalTestCase {

  @Inject
  private Registry registry;

  @Override
  protected String getConfigFile() {
    return "spring-scan-wildcards-config.xml";
  }

  @Test
  public void scanPackagesWithWildcard() {
    Optional<ScanObject> scanObject = registry.lookupByName("scanObject");
    assertThat(scanObject.isPresent(), is(true));
    Optional<ScanObject> scanInnerObject = registry.lookupByName("scanInnerObject");
    assertThat(scanInnerObject.isPresent(), is(true));
    assertThat(scanObject.get().getInnerObject(), notNullValue());
  }

}
