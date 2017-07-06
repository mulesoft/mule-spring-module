/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.api.config;

import org.mule.runtime.dsl.api.xml.XmlNamespaceInfo;
import org.mule.runtime.dsl.api.xml.XmlNamespaceInfoProvider;

import java.util.Collection;
import java.util.Collections;

/**
 * XML information for the spring module.
 *
 * @since 4.0
 */
public class SpringXmlNamespaceInfoProvider implements XmlNamespaceInfoProvider {

  public static final String SPRING_NAMESPACE = "spring";

  @Override
  public Collection<XmlNamespaceInfo> getXmlNamespacesInfo() {
    return Collections.singleton(new XmlNamespaceInfo() {

      @Override
      public String getNamespaceUriPrefix() {
        return "http://www.mulesoft.org/schema/mule/spring/";
      }

      @Override
      public String getNamespace() {
        return SPRING_NAMESPACE;
      }
    });
  }

}
