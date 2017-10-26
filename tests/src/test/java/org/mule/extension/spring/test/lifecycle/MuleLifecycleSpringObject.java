/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test.lifecycle;

import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.lifecycle.Lifecycle;

public class MuleLifecycleSpringObject extends SpringLifecycleObject implements Lifecycle {

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

  @Override
  public void initialise() throws InitialisationException {
    lifecycleCalls.add("initialise");
  }

}
