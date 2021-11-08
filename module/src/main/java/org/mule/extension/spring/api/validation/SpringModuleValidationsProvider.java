/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.api.validation;

import static java.util.Arrays.asList;

import org.mule.extension.spring.internal.validation.SpringContextStartableValidation;
import org.mule.runtime.ast.api.validation.Validation;
import org.mule.runtime.ast.api.validation.ValidationsProvider;

import java.util.List;

public class SpringModuleValidationsProvider implements ValidationsProvider {

  private ClassLoader artifactRegionClassLoader;

  @Override
  public List<Validation> get() {
    return asList(new SpringContextStartableValidation(artifactRegionClassLoader));
  }

  @Override
  public void setArtifactRegionClassLoader(ClassLoader artifactRegionClassLoader) {
    this.artifactRegionClassLoader = artifactRegionClassLoader;
  }

}
