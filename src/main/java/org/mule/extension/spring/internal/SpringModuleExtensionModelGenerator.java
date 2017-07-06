/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.internal;

import static org.mule.runtime.api.meta.Category.COMMUNITY;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclarer;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.ExtensionLoadingDelegate;

/**
 * Spring module {@link org.mule.runtime.api.meta.model.ExtensionModel} generator.
 * 
 * @since 1.0
 */
public class SpringModuleExtensionModelGenerator implements ExtensionLoadingDelegate {

  public static final String EXTENSION_NAME = "Spring";
  public static final String PREFIX_NAME = "spring";
  public static final String EXTENSION_DESCRIPTION = "Spring Module Plugin";
  public static final String VENDOR = "Mulesoft";
  public static final String VERSION = "1.0.0-SNAPSHOT";
  public static final MuleVersion MIN_MULE_VERSION = new MuleVersion("4.0");
  public static final String XSD_FILE_NAME = "mule-spring.xsd";
  private static final String UNESCAPED_LOCATION_PREFIX = "http://";
  private static final String SCHEMA_LOCATION = "www.mulesoft.org/schema/mule/spring";
  private static final String SCHEMA_VERSION = "current";

  @Override
  public void accept(ExtensionDeclarer extensionDeclarer, ExtensionLoadingContext extensionLoadingContext) {
    XmlDslModel xmlDslModel = XmlDslModel.builder()
        .setPrefix(PREFIX_NAME)
        .setXsdFileName(XSD_FILE_NAME)
        .setSchemaVersion(VERSION)
        .setSchemaLocation(String.format("%s/%s/%s", UNESCAPED_LOCATION_PREFIX + SCHEMA_LOCATION, SCHEMA_VERSION, XSD_FILE_NAME))
        .setNamespace(UNESCAPED_LOCATION_PREFIX + SCHEMA_LOCATION)
        .build();
    ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

    extensionDeclarer.named(EXTENSION_NAME)
        .describedAs(EXTENSION_DESCRIPTION)
        .fromVendor(VENDOR)
        .onVersion(VERSION)
        .withCategory(COMMUNITY)
        .withMinMuleVersion(MIN_MULE_VERSION)
        .withXmlDsl(xmlDslModel);

    // config
    ConfigurationDeclarer springConfig = extensionDeclarer.withConfig("config")
        .describedAs("Spring configuration that allows to define a set of spring XML files and create an application context with objects to be used in the mule artifact.");
    ParameterGroupDeclarer parameterGroupDeclarer = springConfig.onDefaultParameterGroup();
    parameterGroupDeclarer.withRequiredParameter("name").withExpressionSupport(ExpressionSupport.NOT_SUPPORTED)
        .withRole(BEHAVIOUR).ofType(typeLoader.load(String.class));
    parameterGroupDeclarer.withRequiredParameter("files").withExpressionSupport(ExpressionSupport.NOT_SUPPORTED)
        .withRole(BEHAVIOUR).ofType(typeLoader.load(String.class));
  }
}
