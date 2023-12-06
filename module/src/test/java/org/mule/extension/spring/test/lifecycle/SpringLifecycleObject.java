/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.test.lifecycle;

import static java.util.Collections.emptyList;

import org.mule.runtime.api.artifact.Registry;
import org.mule.runtime.api.component.location.ConfigurationComponentLocator;
import org.mule.runtime.api.component.AbstractComponent;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;
import javax.inject.Provider;


public class SpringLifecycleObject extends AbstractComponent {

  protected List<String> lifecycleCalls = new ArrayList<>();
  private List<String> injectCalls = new ArrayList<>();
  private List<String> callsBeforeInit = new ArrayList<>();

  private Registry registry;
  private ConfigurationComponentLocator configurationComponentLocator;
  private Object reference;

  public void init() {
    callsBeforeInit.addAll(injectCalls);
    callsBeforeInit.addAll(lifecycleCalls);
    lifecycleCalls.add("init");
  }

  public void destroy() {
    lifecycleCalls.add("destroy");
  }


  public List<String> getLifecycleCalls() {
    return lifecycleCalls;
  }

  public List<String> getCallsBeforeInit() {
    return callsBeforeInit;
  }

  @Inject
  public void setRegistry(Registry registry) {
    injectCalls.add("serviceDiscoverer");
    this.registry = registry;
  }

  @Inject
  public void setConfigurationComponentLocator(ConfigurationComponentLocator configurationComponentLocator) {
    injectCalls.add("configurationComponentLocator");
    this.configurationComponentLocator = configurationComponentLocator;
  }

  public Object getReference() {
    return reference;
  }

  public void setReference(Object reference) {
    this.reference = reference;
  }

}
