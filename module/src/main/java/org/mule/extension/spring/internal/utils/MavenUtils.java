/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.internal.utils;

import static java.util.Optional.ofNullable;

import java.io.File;
import java.io.FileReader;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

/**
 * Simple utils class to access information declared in a pom.xml
 */
public class MavenUtils {

  public static String getArtifactVersion(Supplier<File> pomFolderFinder) {
    return mavenProject(pomFolderFinder).getVersion();
  }

  public static Optional<String> getMavenProperty(String propertyName, Supplier<File> pomFolderFinder) {
    Model mavenProject = mavenProject(pomFolderFinder);
    return ofNullable(mavenProject.getProperties().getProperty(propertyName));
  }

  private static Model mavenProject(Supplier<File> pomFolderFinder) {
    return createMavenProject(new File(pomFolderFinder.get(), "pom.xml"));
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
