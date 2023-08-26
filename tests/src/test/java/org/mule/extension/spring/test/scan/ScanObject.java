/*
 * Copyright © MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.test.scan;

import org.mule.extension.spring.test.scan.inner.ScanInnerObject;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

@Component
public class ScanObject {

  @Inject
  private ScanInnerObject innerObject;

  public ScanInnerObject getInnerObject() {
    return innerObject;
  }
}
