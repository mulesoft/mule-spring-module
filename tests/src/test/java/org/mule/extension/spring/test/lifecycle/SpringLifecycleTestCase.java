/*
 * Copyright © MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test.lifecycle;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mule.extension.spring.AllureConstants.SpringFeature.SPRING_EXTENSION;
import static org.mule.extension.spring.AllureConstants.SpringFeature.LifecycleAndDependencyInjectionStory.LIFECYCLE_AND_DEPENDENCY_INJECTION;

import org.mule.extension.spring.test.SpringPluginFunctionalTestCase;
import org.mule.tck.junit4.rule.SystemProperty;

import org.hamcrest.core.IsNull;
import org.junit.ClassRule;
import org.junit.Test;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Provider;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;

@Feature(SPRING_EXTENSION)
@Story(LIFECYCLE_AND_DEPENDENCY_INJECTION)
public class SpringLifecycleTestCase extends SpringPluginFunctionalTestCase {

  @ClassRule
  public static SystemProperty springConfig = new SystemProperty("spring.config.file", "spring-lifecycle-config.xml");

  @Override
  protected String getConfigFile() {
    return "mule-spring-config.xml";
  }

  @Description("Tests that injection and init/dispose methods are executed in the right order")
  @Test
  public void verifyLifecycleAndInjectionOrder() {
    SpringLifecycleObject lifecycleObject = registry.<SpringLifecycleObject>lookupByName("lifecycleObject").get();
    muleContext.dispose();
    assertThat(lifecycleObject.getCallsBeforeInit(),
               containsInAnyOrder("serviceDiscoverer", "configurationComponentLocator"));
    assertThat(lifecycleObject.getLifecycleCalls(),
               contains("init", "destroy"));
  }

  @Test
  public void springObjectDependingOnArtifactObject() {
    SpringLifecycleObject lifecycleObject =
        registry.<SpringLifecycleObject>lookupByName("springObjectReferencingArtifactObject").get();
    assertThat(lifecycleObject.getReference(), instanceOf(AtomicInteger.class));
  }

  @Test
  public void springObjectDependingOnArtifactObjectThatDependsOnSpringObject() {
    SpringLifecycleObject lifecycleObject =
        registry.<SpringLifecycleObject>lookupByName("artifactObjectThatDependsOnSpringObject").get();
    assertThat(lifecycleObject.getReference(), instanceOf(SpringLifecycleObject.class));
    SpringLifecycleObject springObjectThatDependsOnArtifactObject = (SpringLifecycleObject) lifecycleObject.getReference();
    assertThat(springObjectThatDependsOnArtifactObject.getReference(), instanceOf(AtomicInteger.class));
  }

  @Test
  public void springObjectWithOptionalInjectionAttributes() {
    SpringLifecycleObject lifecycleObject = registry.<SpringLifecycleObject>lookupByName("lifecycleObject").get();
    assertThat(lifecycleObject.getNonExistentOptionalObject().isPresent(), is(false));
    assertThat(lifecycleObject.getExistentExistentOptionalObject().isPresent(), is(true));
  }

  @Test
  public void springObjectProviderAttribute() {
    SpringLifecycleObject lifecycleObject = registry.<SpringLifecycleObject>lookupByName("lifecycleObject").get();
    Provider<AtomicLong> objectProvider = lifecycleObject.getObjectProvider();
    assertThat(objectProvider, notNullValue());
    assertThat(objectProvider.get(), is(not(objectProvider.get())));
  }

  @Test
  public void springObjectCollectionAttribute() {
    SpringLifecycleObject lifecycleObject = registry.<SpringLifecycleObject>lookupByName("lifecycleObject").get();
    Collection<Number> numberObjects = lifecycleObject.getNumberObjects();
    assertThat(numberObjects, hasSize(3));
  }

  @Test
  public void springObjectCollectionEmptyAttribute() {
    SpringLifecycleObject lifecycleObject = registry.<SpringLifecycleObject>lookupByName("lifecycleObject").get();
    Collection<Number> atomicDoubles = lifecycleObject.getOptionalNumberObjects();
    assertThat(atomicDoubles, hasSize(3));
  }

  @Test
  public void springObjectCollectionOptionalEmptyAttribute() {
    SpringLifecycleObject lifecycleObject = registry.<SpringLifecycleObject>lookupByName("lifecycleObject").get();
    Collection<AtomicDouble> atomicDoubles = lifecycleObject.getAtomicDoubles();
    assertThat(atomicDoubles, hasSize(0));
  }

  @Test
  public void objectAwareOfSpringInjectableInterfaces() {
    SpringAwareObject springObjectAware = registry.<SpringAwareObject>lookupByName("springObjectAware").get();
    assertThat(springObjectAware.getApplicationContext(), notNullValue());
    assertThat(springObjectAware.getApplicationEventPublisher(), notNullValue());
    assertThat(springObjectAware.getEmbeddedValueResolver(), notNullValue());
    assertThat(springObjectAware.getEnvironment(), notNullValue());
    assertThat(springObjectAware.getMessageSource(), notNullValue());
    assertThat(springObjectAware.getResourceLoader(), notNullValue());
  }
}
