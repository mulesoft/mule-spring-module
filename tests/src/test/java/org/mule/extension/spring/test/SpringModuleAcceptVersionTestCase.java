/*
 * (c) 2003-2019 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
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
