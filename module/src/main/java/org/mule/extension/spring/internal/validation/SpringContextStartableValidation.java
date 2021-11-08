/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.internal.validation;

import static org.mule.extension.spring.internal.SpringModuleExtensionModelGenerator.CONFIG_FILES_PARAM;
import static org.mule.extension.spring.internal.config.SpringXmlNamespaceInfoProvider.SPRING_NAMESPACE;
import static org.mule.extension.spring.internal.config.SpringXmlNamespaceInfoProvider.SPRING_NAMESPACE_URI;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.ast.api.util.ComponentAstPredicatesFactory.currentElemement;
import static org.mule.runtime.ast.api.validation.Validation.Level.ERROR;
import static org.mule.runtime.ast.api.validation.ValidationResultItem.create;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.ast.api.ArtifactAst;
import org.mule.runtime.ast.api.ComponentAst;
import org.mule.runtime.ast.api.ComponentParameterAst;
import org.mule.runtime.ast.api.validation.Validation;
import org.mule.runtime.ast.api.validation.ValidationResultItem;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContextStartableValidation implements Validation {

  private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SpringContextStartableValidation.class);

  private static final ComponentIdentifier SPRING_CONFIG_IDENTIFIER = ComponentIdentifier.builder()
      .name("config")
      .namespace(SPRING_NAMESPACE)
      .namespaceUri(SPRING_NAMESPACE_URI).build();

  private final ClassLoader artifactRegionClassLoader;

  public SpringContextStartableValidation(ClassLoader artifactRegionClassLoader) {
    this.artifactRegionClassLoader = artifactRegionClassLoader;
  }

  @Override
  public String getName() {
    return "Spring context is startable";
  }

  @Override
  public String getDescription() {
    return "Validates the correctness of the referenced Spring context.";
  }

  @Override
  public Level getLevel() {
    return ERROR;
  }

  @Override
  public Predicate<List<ComponentAst>> applicable() {
    return currentElemement(component -> component.getIdentifier().equals(SPRING_CONFIG_IDENTIFIER));
  }

  @Override
  public Optional<ValidationResultItem> validate(ComponentAst component, ArtifactAst artifact) {
    LOGGER.error("artifactRegionClassLoader: " + artifactRegionClassLoader);

    ComponentParameterAst filesParam = component.getParameter(DEFAULT_GROUP_NAME, CONFIG_FILES_PARAM);
    String files = (String) filesParam.getValue().getRight();
    String[] configFiles = files.split(",");

    try (ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(configFiles)) {
      applicationContext.setClassLoader(artifactRegionClassLoader);
      applicationContext.refresh();
      // applicationContext.getBeansOfType(Object.class);
      return empty();
    } catch (Exception e) {
      return of(create(component, filesParam, this, e.getMessage()));
    }
  }

}
