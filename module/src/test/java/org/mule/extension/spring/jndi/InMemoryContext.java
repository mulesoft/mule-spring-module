/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.jndi;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Simple in-memory JNDI context for unit testing.
 */
public class InMemoryContext implements Context {

  private final Map context = new HashMap();

  @Override
  public Object lookup(Name name) throws NamingException {
    return context.get(name);
  }

  @Override
  public Object lookup(String name) throws NamingException {
    return context.get(name);
  }

  @Override
  public void bind(Name name, Object obj) throws NamingException {
    context.put(name, obj);
  }

  @Override
  public void bind(String name, Object obj) throws NamingException {
    context.put(name, obj);
  }

  @Override
  public void unbind(Name name) throws NamingException {
    context.remove(name);
  }

  @Override
  public void unbind(String name) throws NamingException {
    context.remove(name);
  }

  @Override
  public void rebind(Name name, Object obj) throws NamingException {
    unbind(name);
    bind(name, obj);
  }

  @Override
  public void rebind(String name, Object obj) throws NamingException {
    unbind(name);
    bind(name, obj);
  }

  //////////////////////////////////////////////////////////////////////
  // The remaining methods are not implemented.
  //////////////////////////////////////////////////////////////////////

  @Override
  public Object addToEnvironment(String propName, Object propVal) throws NamingException {
    return null;
  }

  @Override
  public void close() throws NamingException {
    // nop
  }

  @Override
  public Name composeName(Name name, Name prefix) throws NamingException {
    return null;
  }

  @Override
  public String composeName(String name, String prefix) throws NamingException {
    return null;
  }

  @Override
  public Context createSubcontext(Name name) throws NamingException {
    return null;
  }

  @Override
  public Context createSubcontext(String name) throws NamingException {
    return null;
  }

  @Override
  public void destroySubcontext(Name name) throws NamingException {
    // nop
  }

  @Override
  public void destroySubcontext(String name) throws NamingException {
    // nop
  }

  @Override
  public Hashtable getEnvironment() throws NamingException {
    return null;
  }

  @Override
  public String getNameInNamespace() throws NamingException {
    return null;
  }

  @Override
  public NameParser getNameParser(Name name) throws NamingException {
    return null;
  }

  @Override
  public NameParser getNameParser(String name) throws NamingException {
    return null;
  }

  @Override
  public NamingEnumeration list(Name name) throws NamingException {
    return null;
  }

  @Override
  public NamingEnumeration list(String name) throws NamingException {
    return null;
  }

  @Override
  public NamingEnumeration listBindings(Name name) throws NamingException {
    return null;
  }

  @Override
  public NamingEnumeration listBindings(String name) throws NamingException {
    return null;
  }

  @Override
  public Object lookupLink(Name name) throws NamingException {
    return null;
  }

  @Override
  public Object lookupLink(String name) throws NamingException {
    return null;
  }

  @Override
  public Object removeFromEnvironment(String propName) throws NamingException {
    return null;
  }

  @Override
  public void rename(Name oldName, Name newName) throws NamingException {
    // nop
  }

  @Override
  public void rename(String oldName, String newName) throws NamingException {
    // nop
  }
}


