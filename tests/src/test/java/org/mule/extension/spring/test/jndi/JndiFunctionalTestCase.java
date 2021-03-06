/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test.jndi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.mule.extension.spring.test.SpringPluginFunctionalTestCase;
import org.mule.tck.testmodels.fruit.Apple;
import org.mule.tck.testmodels.fruit.Orange;

import org.junit.Test;

public class JndiFunctionalTestCase extends SpringPluginFunctionalTestCase {

  @Override
  protected String getConfigFile() {
    return "jndi/jndi-functional-test.xml";
  }

  @Test
  public void testJndi() {
    Object obj;

    obj = registry.lookupByName("apple").get();
    assertNotNull(obj);
    assertEquals(Apple.class, obj.getClass());

    obj = registry.lookupByName("orange").get();
    assertNotNull(obj);
    assertEquals(Orange.class, obj.getClass());
    assertEquals(new Integer(8), ((Orange) obj).getSegments());
    assertEquals("Florida Sunny", ((Orange) obj).getBrand());
  }
}


