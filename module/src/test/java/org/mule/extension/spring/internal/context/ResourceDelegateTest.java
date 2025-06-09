package org.mule.extension.spring.internal.context;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;

public class ResourceDelegateTest {

  @Mock
  private Resource mockResource;

  private ResourceDelegate resourceDelegate;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    resourceDelegate = new ResourceDelegate(mockResource);
  }

  @Test
  public void testExists() {
    when(mockResource.exists()).thenReturn(true);
    assertTrue(resourceDelegate.exists());
  }

  @Test
  public void testIsReadable() {
    when(mockResource.isReadable()).thenReturn(true);
    assertTrue(resourceDelegate.isReadable());
  }

  @Test
  public void testIsOpen() {
    when(mockResource.isOpen()).thenReturn(false);
    assertFalse(resourceDelegate.isOpen());
  }

  @Test
  public void testGetURL() throws IOException {
    URL expectedURL = new URL("http://example.com");
    when(mockResource.getURL()).thenReturn(expectedURL);
    assertEquals(expectedURL, resourceDelegate.getURL());
  }

  @Test
  public void testGetURI() throws IOException {
    URI expectedURI = URI.create("http://example.com");
    when(mockResource.getURI()).thenReturn(expectedURI);
    assertEquals(expectedURI, resourceDelegate.getURI());
  }

  @Test
  public void testGetFile() throws IOException {
    File expectedFile = new File("test.txt");
    when(mockResource.getFile()).thenReturn(expectedFile);
    assertEquals(expectedFile, resourceDelegate.getFile());
  }

  @Test
  public void testContentLength() throws IOException {
    when(mockResource.contentLength()).thenReturn(100L);
    assertEquals(100L, resourceDelegate.contentLength());
  }

  @Test
  public void testLastModified() throws IOException {
    long expectedTime = System.currentTimeMillis();
    when(mockResource.lastModified()).thenReturn(expectedTime);
    assertEquals(expectedTime, resourceDelegate.lastModified());
  }

  @Test
  public void testCreateRelative() throws IOException {
    Resource relativeResource = mock(Resource.class);
    when(mockResource.createRelative("relative")).thenReturn(relativeResource);
    assertEquals(relativeResource, resourceDelegate.createRelative("relative"));
  }

  @Test
  public void testGetFilename() {
    when(mockResource.getFilename()).thenReturn("test.txt");
    assertEquals("test.txt", resourceDelegate.getFilename());
  }

  @Test
  public void testGetDescription() {
    when(mockResource.getDescription()).thenReturn("Test Resource");
    assertEquals("Test Resource", resourceDelegate.getDescription());
  }

  @Test
  public void testGetInputStreamSuccess() throws IOException {
    InputStream expectedStream = mock(InputStream.class);
    when(mockResource.getInputStream()).thenReturn(expectedStream);
    assertEquals(expectedStream, resourceDelegate.getInputStream());
  }

  @Test(expected = IOException.class)
  @Ignore
  public void testGetInputStreamFailure() throws IOException {
    when(mockResource.getInputStream()).thenThrow(new IOException("Resource not found"));
    when(mockResource.getFilename()).thenReturn("nonexistent.txt");
    resourceDelegate.getInputStream();
  }
}
