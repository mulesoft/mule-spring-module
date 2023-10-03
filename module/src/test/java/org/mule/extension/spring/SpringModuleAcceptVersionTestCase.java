/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.mule.extension.spring.internal.SpringModuleExtensionModelGenerator;
import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;

import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.junit.Test;

public class SpringModuleAcceptVersionTestCase {

  private static final String SPRING_COORDINATES_PREFIX = "org.springframework:";
  private static final String SPRING_SECURITY_COORDINATES_PREFIX = "org.springframework.security:";

  private static final String SPRING_VERSION_PROPERTY = "springVersion";
  private static final String SPRING_SECURITY_VERSION_PROPERTY = "springSecurityVersion";

  private static final Supplier<File> POM_SUPPLIER = () -> {
    try {
      return new File(
                      SpringModuleExtensionModelGenerator.class
                          .getProtectionDomain()
                          .getCodeSource()
                          .getLocation()
                          .toURI())
                              .getParentFile()
                              .getParentFile()
                              .getParentFile();
    } catch (URISyntaxException e) {
      throw new MuleRuntimeException(e);
    }
  };

  private final ExtensionDeclaration springExtensionModelDeclaration;

  public SpringModuleAcceptVersionTestCase() {
    SpringModuleExtensionModelGenerator delegate = new SpringModuleExtensionModelGenerator();
    ExtensionDeclarer declarer = new ExtensionDeclarer();
    delegate.accept(declarer, null);
    this.springExtensionModelDeclaration = declarer.getDeclaration();
  }

  @Test
  public void testVersion() {
    assertThat(
               buildErrorMessage("spring-module"),
               springExtensionModelDeclaration.getVersion(), is(getArtifactVersion()));
  }

  @Test
  public void testSpringVersion() {
    testVersionProperty(SPRING_VERSION_PROPERTY, SPRING_COORDINATES_PREFIX);
  }

  @Test
  public void testSpringSecurityVersion() {
    testVersionProperty(SPRING_SECURITY_VERSION_PROPERTY, SPRING_SECURITY_COORDINATES_PREFIX);
  }

  private void testVersionProperty(String propertyName, String coordinatesPrefix) {
    String expectedSpringProperty = getMavenProperty(propertyName)
        .orElseThrow(() -> new RuntimeException("Unable to find property: " + propertyName + " declared in pom"));
    springExtensionModelDeclaration.getExternalLibraryModels()
        .stream()
        .map(em -> em.getSuggestedCoordinates().orElse(EMPTY))
        .filter(c -> c.startsWith(coordinatesPrefix))
        .forEach(vc -> assertThat(buildErrorMessage(vc), vc, containsString(expectedSpringProperty)));
  }

  private String buildErrorMessage(String module) {
    return module + " version declared in the extension model does not match the one defined in the pom.xml, check constants in "
        + SpringModuleExtensionModelGenerator.class.getName();
  }

  private static String getArtifactVersion() {
    return mavenProject().getVersion();
  }

  private static Optional<String> getMavenProperty(String propertyName) {
    Model mavenProject = mavenProject();
    return ofNullable(mavenProject.getProperties().getProperty(propertyName));
  }

  private static Model mavenProject() {
    return createMavenProject(new File(POM_SUPPLIER.get(), "pom.xml"));
  }

  private static Model createMavenProject(File pomFile) {
    if (pomFile == null || !pomFile.exists()) {
      throw new IllegalArgumentException("pom file doesn't exits for path: " + pomFile);
    }
    MavenXpp3Reader mavenReader = new MavenXpp3Reader();

    try (FileReader reader = new FileReader(pomFile)) {
      Model model = mavenReader.read(reader);
      model.setPomFile(pomFile);
      return model;
    } catch (Exception e) {
      throw new RuntimeException("Couldn't get Maven Artifact from pom: " + pomFile, e);
    }
  }

}
