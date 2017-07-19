/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.internal.config;

import static java.util.Collections.singletonList;
import static org.mule.extension.spring.internal.config.SpringXmlNamespaceInfoProvider.SPRING_NAMESPACE;
import static org.mule.runtime.dsl.api.component.AttributeDefinition.Builder.fromUndefinedSimpleAttributes;
import static org.mule.runtime.dsl.api.component.TypeDefinition.fromType;

import org.mule.extension.spring.api.SpringConfig;
import org.mule.runtime.dsl.api.component.ComponentBuildingDefinition;
import org.mule.runtime.dsl.api.component.ComponentBuildingDefinitionProvider;

import java.util.List;

/**
 * {@link ComponentBuildingDefinition} definitions for the components provided the spring module.
 *
 * @since 4.0
 */
public class SpringComponentBuildingDefinitionProvider implements ComponentBuildingDefinitionProvider {

  @Override
  public void init() {}

  @Override
  public List<ComponentBuildingDefinition> getComponentBuildingDefinitions() {
    return singletonList(new ComponentBuildingDefinition.Builder()
        .withNamespace(SPRING_NAMESPACE)
        .withIdentifier("config")
        .withTypeDefinition(fromType(SpringConfig.class))
        .withSetterParameterDefinition("parameters", fromUndefinedSimpleAttributes().build())
        .build());
  }

}
