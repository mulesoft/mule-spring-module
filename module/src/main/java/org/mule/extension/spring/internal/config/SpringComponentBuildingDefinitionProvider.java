/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.internal.config;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.mule.extension.spring.internal.config.SpringXmlNamespaceInfoProvider.SPRING_NAMESPACE;
import static org.mule.runtime.dsl.api.component.AttributeDefinition.Builder.fromChildCollectionConfiguration;
import static org.mule.runtime.dsl.api.component.AttributeDefinition.Builder.fromReferenceObject;
import static org.mule.runtime.dsl.api.component.AttributeDefinition.Builder.fromSimpleParameter;
import static org.mule.runtime.dsl.api.component.AttributeDefinition.Builder.fromSimpleReferenceParameter;
import static org.mule.runtime.dsl.api.component.AttributeDefinition.Builder.fromUndefinedSimpleAttributes;
import static org.mule.runtime.dsl.api.component.TypeDefinition.fromType;

import org.mule.extension.spring.api.SpringConfig;
import org.mule.extension.spring.api.security.SpringProviderAdapter;
import org.mule.extension.spring.internal.security.AuthorizationFilter;
import org.mule.extension.spring.internal.security.SecurityProperty;
import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.core.api.security.MuleSecurityManagerConfigurator;
import org.mule.runtime.core.api.security.SecurityManager;
import org.mule.runtime.core.api.security.SecurityProvider;
import org.mule.runtime.dsl.api.component.ComponentBuildingDefinition;
import org.mule.runtime.dsl.api.component.ComponentBuildingDefinition.Builder;
import org.mule.runtime.dsl.api.component.ComponentBuildingDefinitionProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ComponentBuildingDefinition} definitions for the components provided the spring module.
 *
 * @since 1.0
 */
public class SpringComponentBuildingDefinitionProvider implements ComponentBuildingDefinitionProvider {

  private final Builder baseDefinition = new ComponentBuildingDefinition.Builder().withNamespace(SPRING_NAMESPACE);

  @Override
  public void init() {}

  @Override
  public List<ComponentBuildingDefinition> getComponentBuildingDefinitions() {
    final List<ComponentBuildingDefinition> definitions = new ArrayList<>();

    definitions.add(baseDefinition.withIdentifier("config")
        .withTypeDefinition(fromType(SpringConfig.class))
        .withSetterParameterDefinition("parameters", fromUndefinedSimpleAttributes().build())
        .alwaysEnabled(true)
        .build());
    definitions.addAll(getSpringSecurityDefinitions());

    return definitions;
  }

  private List<ComponentBuildingDefinition> getSpringSecurityDefinitions() {
    List<ComponentBuildingDefinition> componentBuildingDefinitions = new ArrayList<>();

    componentBuildingDefinitions.add(baseDefinition.withIdentifier("security-manager")
        .alwaysEnabled(true)
        .withTypeDefinition(fromType(SecurityManager.class)).withObjectFactoryType(MuleSecurityManagerConfigurator.class)
        .withSetterParameterDefinition("muleContext", fromReferenceObject(MuleContext.class).build())
        .withSetterParameterDefinition("name", fromSimpleParameter("name").build())
        .withSetterParameterDefinition("providers", fromChildCollectionConfiguration(SecurityProvider.class).build()).build());

    componentBuildingDefinitions.add(baseDefinition.withIdentifier("delegate-security-provider")
        .withTypeDefinition(fromType(SpringProviderAdapter.class))
        .withSetterParameterDefinition("name", fromSimpleParameter("name").build())
        .withSetterParameterDefinition("securityProperties", fromChildCollectionConfiguration(SecurityProperty.class).build())
        .withSetterParameterDefinition("delegate", fromSimpleReferenceParameter("delegate-ref").build())
        .withSetterParameterDefinition("authenticationProvider",
                                       fromSimpleReferenceParameter("authenticationProvider-ref").build())
        .build());

    componentBuildingDefinitions
        .add(baseDefinition.withIdentifier("security-property").withTypeDefinition(fromType(SecurityProperty.class))
            .withConstructorParameterDefinition(fromSimpleParameter("name").build())
            .withConstructorParameterDefinition(fromSimpleParameter("value").build()).build());

    componentBuildingDefinitions.add(baseDefinition.withIdentifier("authorization-filter")
        .withTypeDefinition(fromType(AuthorizationFilter.class))
        .withConstructorParameterDefinition(fromSimpleParameter("requiredAuthorities",
                                                                (value) -> asList(((String) value).split(",")).stream()
                                                                    .map(String::trim).collect(toList())).build())
        .build());
    return componentBuildingDefinitions;
  }

}
