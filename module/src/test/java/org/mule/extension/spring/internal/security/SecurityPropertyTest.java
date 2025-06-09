package org.mule.extension.spring.internal.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SecurityPropertyTest {

  @Test
  public void testConstructorAndGetters() {
    String name = "testName";
    String value = "testValue";
    SecurityProperty property = new SecurityProperty(name, value);

    assertEquals("getName should return the correct name", name, property.getName());
    assertEquals("getValue should return the correct value", value, property.getValue());
  }

  @Test
  public void testConstructorWithNullName() {
    String value = "testValue";
    SecurityProperty property = new SecurityProperty(null, value);

    assertNull("getName should return null when constructed with null name", property.getName());
    assertEquals("getValue should return the correct value", value, property.getValue());
  }

  @Test
  public void testConstructorWithNullValue() {
    String name = "testName";
    SecurityProperty property = new SecurityProperty(name, null);

    assertEquals("getName should return the correct name", name, property.getName());
    assertNull("getValue should return null when constructed with null value", property.getValue());
  }

  @Test
  public void testConstructorWithEmptyStrings() {
    String name = "";
    String value = "";
    SecurityProperty property = new SecurityProperty(name, value);

    assertEquals("getName should return an empty string", name, property.getName());
    assertEquals("getValue should return an empty string", value, property.getValue());
  }
}
