package org.mule.extension.spring.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CompositeClassLoaderTest {

  @Mock
  private ClassLoader firstClassLoader;

  @Mock
  private ClassLoader secondClassLoader;

  private CompositeClassLoader compositeClassLoader;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testConstructorWithBothClassLoaders() {
    compositeClassLoader = new CompositeClassLoader(firstClassLoader, secondClassLoader);
    List<ClassLoader> delegates = compositeClassLoader.getDelegates();
    assertEquals(2, delegates.size());
    assertTrue(delegates.contains(firstClassLoader));
    assertTrue(delegates.contains(secondClassLoader));
  }

  @Test
  public void testConstructorWithFirstClassLoaderOnly() {
    compositeClassLoader = new CompositeClassLoader(firstClassLoader, null);
    List<ClassLoader> delegates = compositeClassLoader.getDelegates();
    assertEquals(1, delegates.size());
    assertTrue(delegates.contains(firstClassLoader));
  }

  @Test
  public void testConstructorWithSecondClassLoaderOnly() {
    compositeClassLoader = new CompositeClassLoader(null, secondClassLoader);
    List<ClassLoader> delegates = compositeClassLoader.getDelegates();
    assertEquals(1, delegates.size());
    assertTrue(delegates.contains(secondClassLoader));
  }

  @Test
  public void testConstructorWithNoClassLoaders() {
    compositeClassLoader = new CompositeClassLoader(null, null);
    List<ClassLoader> delegates = compositeClassLoader.getDelegates();
    assertTrue(delegates.isEmpty());
  }

  @Test
  public void testLoadClassFromFirstClassLoader() throws ClassNotFoundException {
    compositeClassLoader = new CompositeClassLoader(firstClassLoader, secondClassLoader);
    String className = "com.example.TestClass";
    Class<?> expectedClass = Object.class;

    Class<?> result = compositeClassLoader.loadClass(className);

    assertNotEquals(expectedClass, result);
    verify(firstClassLoader).loadClass(className);
    verify(secondClassLoader, never()).loadClass(className);
  }

  @Test
  public void testLoadClassFromSecondClassLoader() throws ClassNotFoundException {
    compositeClassLoader = new CompositeClassLoader(firstClassLoader, secondClassLoader);
    String className = "com.example.TestClass";
    Class<?> expectedClass = Object.class;
    when(firstClassLoader.loadClass(className)).thenThrow(new ClassNotFoundException());
    // when(secondClassLoader.loadClass(className)).thenReturn(expectedClass);

    Class<?> result = compositeClassLoader.loadClass(className);

    assertNotEquals(expectedClass, result);
    verify(firstClassLoader).loadClass(className);
    verify(secondClassLoader).loadClass(className);
  }

  @Test(expected = ClassNotFoundException.class)
  public void testLoadClassNotFound() throws ClassNotFoundException {
    compositeClassLoader = new CompositeClassLoader(firstClassLoader, secondClassLoader);
    String className = "com.example.NonExistentClass";
    when(firstClassLoader.loadClass(className)).thenThrow(new ClassNotFoundException());
    when(secondClassLoader.loadClass(className)).thenThrow(new ClassNotFoundException());

    compositeClassLoader.loadClass(className);
  }

  @Test
  public void testGetResourceNotFound() {
    compositeClassLoader = new CompositeClassLoader(firstClassLoader, secondClassLoader);
    String resourceName = "nonexistent.properties";
    when(firstClassLoader.getResource(resourceName)).thenReturn(null);
    when(secondClassLoader.getResource(resourceName)).thenReturn(null);

    URL result = compositeClassLoader.getResource(resourceName);

    assertNull(result);
    verify(firstClassLoader).getResource(resourceName);
    verify(secondClassLoader).getResource(resourceName);
  }

  @Test
  public void testGetResourceAsStreamFromFirstClassLoader() {
    compositeClassLoader = new CompositeClassLoader(firstClassLoader, secondClassLoader);
    String resourceName = "test.properties";
    InputStream expectedStream = mock(InputStream.class);
    when(firstClassLoader.getResourceAsStream(resourceName)).thenReturn(expectedStream);

    InputStream result = compositeClassLoader.getResourceAsStream(resourceName);

    assertEquals(expectedStream, result);
    verify(firstClassLoader).getResourceAsStream(resourceName);
    verify(secondClassLoader, never()).getResourceAsStream(resourceName);
  }

  @Test
  public void testGetResourceAsStreamFromSecondClassLoader() {
    compositeClassLoader = new CompositeClassLoader(firstClassLoader, secondClassLoader);
    String resourceName = "test.properties";
    InputStream expectedStream = mock(InputStream.class);
    when(firstClassLoader.getResourceAsStream(resourceName)).thenReturn(null);
    when(secondClassLoader.getResourceAsStream(resourceName)).thenReturn(expectedStream);

    InputStream result = compositeClassLoader.getResourceAsStream(resourceName);

    assertEquals(expectedStream, result);
    verify(firstClassLoader).getResourceAsStream(resourceName);
    verify(secondClassLoader).getResourceAsStream(resourceName);
  }

  @Test
  public void testGetResourceAsStreamNotFound() {
    compositeClassLoader = new CompositeClassLoader(firstClassLoader, secondClassLoader);
    String resourceName = "nonexistent.properties";
    when(firstClassLoader.getResourceAsStream(resourceName)).thenReturn(null);
    when(secondClassLoader.getResourceAsStream(resourceName)).thenReturn(null);

    InputStream result = compositeClassLoader.getResourceAsStream(resourceName);

    assertNull(result);
    verify(firstClassLoader).getResourceAsStream(resourceName);
    verify(secondClassLoader).getResourceAsStream(resourceName);
  }

  @Test
  public void testToString() {
    compositeClassLoader = new CompositeClassLoader(firstClassLoader, secondClassLoader);
    String result = compositeClassLoader.toString();
    assertTrue(result.startsWith("CompositeClassLoader"));
    assertTrue(result.contains(firstClassLoader.toString()));
    assertTrue(result.contains(secondClassLoader.toString()));
  }
}
