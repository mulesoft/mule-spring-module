/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.mule.extension.spring.internal.SpringModuleExtensionModelGenerator;
import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;

import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.function.Supplier;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.junit.Test;

public class SpringModuleAcceptVersionTestCase {

  @Test
  public void testVersion() {
    SpringModuleExtensionModelGenerator delegate = new SpringModuleExtensionModelGenerator();
    ExtensionDeclarer declarer = new ExtensionDeclarer();
    delegate.accept(declarer, null);
    assertThat(declarer.getDeclaration().getVersion(), is(getModuleVersion()));
  }

  private static String getModuleVersion() {
    return getMavenProperty("version", () -> {
      try {
        return new File(SpringModuleExtensionModelGenerator.class.getProtectionDomain().getCodeSource()
            .getLocation().toURI()).getParentFile().getParentFile().getParentFile();
      } catch (URISyntaxException e) {
        throw new MuleRuntimeException(e);
      }
    });
  }

  private static String getMavenProperty(String property, Supplier<File> pomFolderFinder) {

    Model mavenProject = mavenProject(pomFolderFinder);
    return mavenProject.getVersion();
  }

  private static Model mavenProject(Supplier<File> pomFolderFinder) {
    return createMavenProject(new File(pomFolderFinder.get(), "pom.xml"));
  }

  private static Model createMavenProject(File pomFile) {
    if (pomFile == null || !pomFile.exists()) {
      throw new IllegalArgumentException("pom file doesn't exits for path: " + pomFile);
    }
    MavenXpp3Reader mavenReader = new MavenXpp3Reader();

    try {
      FileReader reader = new FileReader(pomFile);
      try {
        Model model = mavenReader.read(reader);
        model.setPomFile(pomFile);
        return model;
      } finally {
        if (reader != null) {
          reader.close();
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Couldn't get Maven Artifact from pom: " + pomFile, e);
    }
  }

}