/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
