/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.internal.config;

import static java.util.Collections.singleton;

import org.mule.runtime.dsl.api.xml.XmlNamespaceInfo;
import org.mule.runtime.dsl.api.xml.XmlNamespaceInfoProvider;

import java.util.Collection;

/**
 * XML information for the spring module.
 *
 * @since 4.0
 */
public class SpringXmlNamespaceInfoProvider implements XmlNamespaceInfoProvider {

  public static final String SPRING_NAMESPACE = "spring";
  public static final String SPRING_NAMESPACE_URI = "http://www.mulesoft.org/schema/mule/spring/";

  @Override
  public Collection<XmlNamespaceInfo> getXmlNamespacesInfo() {
    return singleton(new XmlNamespaceInfo() {

      @Override
      public String getNamespaceUriPrefix() {
        return SPRING_NAMESPACE_URI;
      }

      @Override
      public String getNamespace() {
        return SPRING_NAMESPACE;
      }
    });
  }

}
