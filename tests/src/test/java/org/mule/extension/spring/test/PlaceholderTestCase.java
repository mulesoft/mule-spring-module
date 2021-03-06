/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mule.extension.spring.AllureConstants.SpringFeature.ArtifactAndSpringModuleInteroperabilityStory.ARTIFACT_AND_SPRING_MODULE_INTEROPERABILITY;
import static org.mule.extension.spring.AllureConstants.SpringFeature.SPRING_EXTENSION;
import org.mule.tck.junit4.rule.SystemProperty;
import org.mule.tck.testmodels.fruit.GrapeFruit;
import org.mule.tck.testmodels.fruit.Orange;

import java.util.Map;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Rule;
import org.junit.Test;

@Feature(SPRING_EXTENSION)
@Story(ARTIFACT_AND_SPRING_MODULE_INTEROPERABILITY)
public class PlaceholderTestCase extends SpringPluginFunctionalTestCase {

  @Rule
  public SystemProperty systemProperty = new SystemProperty("systemProperty", "3.0");

  @Override
  protected String getConfigFile() {
    return "placeholders-test-case.xml";
  }

  @Test
  public void artifactPropertiesCanBeUsedInSpringBeans() throws Exception {
    Orange orange = registry.<Orange>lookupByName("orange").get();
    assertThat(orange.getBrand(), is("propertyAValue"));
    assertThat(orange.getSegments(), is(12));
    assertThat(orange.getRadius(), is(3.0));
  }

  @Test
  public void springCanUseItOwnPlaceholder() throws Exception {
    Orange orange = registry.<Orange>lookupByName("orange").get();
    Map mapProperties = orange.getMapProperties();
    assertThat(mapProperties.get("springPropertyA"), is("springPropertyAValue"));
    assertThat(mapProperties.get("springPropertyB"), is("springPropertyBValue"));
  }

  @Test
  public void spelCanBeUsedInSpringBeans() throws Exception {
    GrapeFruit grapeFruit = registry.<GrapeFruit>lookupByName("grapefruit").get();
    assertThat(grapeFruit.getBrand(), is("123"));
    assertThat(grapeFruit.getRadius(), is(1.2));
    assertThat(grapeFruit.getSegments(), is(3));
    assertThat(grapeFruit.isRed(), is(true));
  }

}
