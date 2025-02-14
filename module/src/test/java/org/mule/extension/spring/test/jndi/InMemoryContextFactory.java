/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.test.jndi;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

/**
 * Simple in-memory JNDI context for unit testing.
 */
public class InMemoryContextFactory implements InitialContextFactory {

  public Context getInitialContext() throws NamingException {
    return getInitialContext(null);
  }

  @Override
  public Context getInitialContext(Hashtable environment) throws NamingException {
    return new InMemoryContext();
  }
}


