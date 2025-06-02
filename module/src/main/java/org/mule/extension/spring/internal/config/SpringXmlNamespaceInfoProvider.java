/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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

  @Override
  public Collection<XmlNamespaceInfo> getXmlNamespacesInfo() {
    return singleton(new XmlNamespaceInfo() {

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
