/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test.lifecycle;

import static java.util.Collections.emptyList;
import org.mule.runtime.api.artifact.ServiceDiscoverer;
import org.mule.runtime.api.component.location.ConfigurationComponentLocator;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.lifecycle.Lifecycle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;
import javax.inject.Provider;

import com.google.common.util.concurrent.AtomicDouble;

public class SpringLifecycleObject implements Lifecycle {

  private List<String> lifecycleCalls = new ArrayList<>();
  private List<String> injectCalls = new ArrayList<>();
  private List<String> callsBeforeInit = new ArrayList<>();

  private ServiceDiscoverer serviceDiscoverer;
  private ConfigurationComponentLocator configurationComponentLocator;
  private Object reference;

  @Inject
  private Optional<AtomicBoolean> nonExistentOptionalObject;
  @Inject
  private Optional<AtomicInteger> existentExistentOptionalObject;
  @Inject
  private Provider<AtomicLong> objectProvider;
  @Inject
  private Collection<Number> numberObjects;
  @Inject
  private Optional<Collection<AtomicDouble>> atomicDoubles;
  @Inject
  private Optional<Collection<Number>> optionalNumberObjects;


  public void init() {
    callsBeforeInit.addAll(injectCalls);
    callsBeforeInit.addAll(lifecycleCalls);
    lifecycleCalls.add("init");
  }

  public void destroy() {
    lifecycleCalls.add("destroy");
  }


  @Override
  public void stop() throws MuleException {
    lifecycleCalls.add("stop");
  }

  @Override
  public void dispose() {
    lifecycleCalls.add("dispose");
  }

  @Override
  public void start() throws MuleException {
    lifecycleCalls.add("start");
  }

  public List<String> getLifecycleCalls() {
    return lifecycleCalls;
  }

  public List<String> getCallsBeforeInit() {
    return callsBeforeInit;
  }

  @Inject
  public void setServiceDiscoverer(ServiceDiscoverer serviceDiscoverer) {
    injectCalls.add("serviceDiscoverer");
    this.serviceDiscoverer = serviceDiscoverer;
  }

  @Inject
  public void setConfigurationComponentLocator(ConfigurationComponentLocator configurationComponentLocator) {
    injectCalls.add("configurationComponentLocator");
    this.configurationComponentLocator = configurationComponentLocator;
  }

  @Override
  public void initialise() throws InitialisationException {
    lifecycleCalls.add("initialise");
  }

  public Object getReference() {
    return reference;
  }

  public void setReference(Object reference) {
    this.reference = reference;
  }

  public Optional<AtomicBoolean> getNonExistentOptionalObject() {
    return nonExistentOptionalObject;
  }

  public Optional<AtomicInteger> getExistentExistentOptionalObject() {
    return existentExistentOptionalObject;
  }

  public Provider<AtomicLong> getObjectProvider() {
    return objectProvider;
  }

  public Collection<Number> getNumberObjects() {
    return numberObjects;
  }

  public Collection<AtomicDouble> getAtomicDoubles() {
    return atomicDoubles.orElse(emptyList());
  }

  public Collection<Number> getOptionalNumberObjects() {
    return optionalNumberObjects.orElse(emptyList());
  }
}