/*
 * Copyright 2025 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.internal.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.mule.runtime.dsl.api.xml.XmlNamespaceInfo;
import org.mule.runtime.dsl.api.xml.XmlNamespaceInfoProvider;

import java.util.Collection;

import org.junit.Test;

public class SpringXmlNamespaceInfoProviderTest {

  @Test
  public void testGetXmlNamespacesInfo() {
    XmlNamespaceInfoProvider provider = new SpringXmlNamespaceInfoProvider();
    Collection<XmlNamespaceInfo> namespaceInfos = provider.getXmlNamespacesInfo();

    assertNotNull("Namespace info collection should not be null", namespaceInfos);
    assertEquals("Should return exactly one namespace info", 1, namespaceInfos.size());

    XmlNamespaceInfo namespaceInfo = namespaceInfos.iterator().next();
    assertNotNull("Namespace info should not be null", namespaceInfo);

    assertEquals("Namespace URI prefix should match",
                 "http://www.mulesoft.org/schema/mule/spring/",
                 namespaceInfo.getNamespaceUriPrefix());
    assertEquals("Namespace should match",
                 SpringXmlNamespaceInfoProvider.SPRING_NAMESPACE,
                 namespaceInfo.getNamespace());
  }

  @Test
  public void testSpringNamespaceConstant() {
    assertEquals("SPRING_NAMESPACE constant should have correct value",
                 "spring",
                 SpringXmlNamespaceInfoProvider.SPRING_NAMESPACE);
  }

  @Test
  public void testSingletonCollection() {
    XmlNamespaceInfoProvider provider = new SpringXmlNamespaceInfoProvider();
    Collection<XmlNamespaceInfo> namespaceInfos = provider.getXmlNamespacesInfo();

    assertFalse("Namespace info collection should not be empty", namespaceInfos.isEmpty());
    assertEquals("Namespace info collection should contain exactly one element",
                 1,
                 namespaceInfos.size());

    // Attempt to add another element should throw an exception
    try {
      namespaceInfos.add(null);
      fail("Should not be able to add elements to the collection");
    } catch (UnsupportedOperationException e) {
      // Expected exception
    }
  }
}
