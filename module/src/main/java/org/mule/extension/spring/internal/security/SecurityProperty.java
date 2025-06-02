/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.internal.security;

/**
 * Holder for the values configured in a security property within a security manager.
 *
 * @since 1.0
 */
public class SecurityProperty {

  private String name;
  private String value;

  /**
   * @param name  property name
   * @param value property value
   */
  public SecurityProperty(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * @return property name
   */
  public String getName() {
    return name;
  }

  /**
   * @return property value
   */
  public String getValue() {
    return value;
  }
}
