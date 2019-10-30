/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mule.extension.spring.internal.utils.MavenUtils.getArtifactVersion;

import org.mule.extension.spring.internal.SpringModuleExtensionModelGenerator;
import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;

import java.io.File;
import java.net.URISyntaxException;

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
    return getArtifactVersion(() -> {
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
    });
  }

}
