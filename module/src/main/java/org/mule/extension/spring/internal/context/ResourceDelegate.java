/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.internal.context;

import static org.mule.runtime.core.api.util.ClassUtils.getResourceOrFail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.springframework.core.io.Resource;

/**
 * Class used to wrap spring resource loading from the file system so if there is an error loading a file we show the same error
 * message as in other parts of mule
 */
public class ResourceDelegate implements Resource {

  private Resource delegate;

  public ResourceDelegate(Resource delegate) {
    this.delegate = delegate;
  }

  @Override
  public boolean exists() {
    return delegate.exists();
  }

  @Override
  public boolean isReadable() {
    return delegate.isReadable();
  }

  @Override
  public boolean isOpen() {
    return delegate.isOpen();
  }

  @Override
  public URL getURL() throws IOException {
    return delegate.getURL();
  }

  @Override
  public URI getURI() throws IOException {
    return delegate.getURI();
  }

  @Override
  public File getFile() throws IOException {
    return delegate.getFile();
  }

  @Override
  public long contentLength() throws IOException {
    return delegate.contentLength();
  }

  @Override
  public long lastModified() throws IOException {
    return delegate.lastModified();
  }

  @Override
  public Resource createRelative(String s) throws IOException {
    return delegate.createRelative(s);
  }

  @Override
  public String getFilename() {
    return delegate.getFilename();
  }

  @Override
  public String getDescription() {
    return delegate.getDescription();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    try {
      return delegate.getInputStream();
    } catch (Exception e) {
      getResourceOrFail(getFilename(), false);
      throw e;
    }
  }
}
