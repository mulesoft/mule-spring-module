/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test;

import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.test.runner.ArtifactClassLoaderRunnerConfig;

@ArtifactClassLoaderRunnerConfig(sharedRuntimeLibs = {
    "org.mule.tests:mule-tests-unit",
    "org.springframework:spring-beans",
    "org.springframework:spring-context",
    "org.springframework.security:spring-security-core"
})
public abstract class SpringPluginFunctionalTestCase extends MuleArtifactFunctionalTestCase {
}
