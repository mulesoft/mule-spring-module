/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.test.jndi;

import org.mule.functional.jndi.InMemoryContextFactory;
import org.mule.tck.testmodels.fruit.Apple;
import org.mule.tck.testmodels.fruit.Banana;
import org.mule.tck.testmodels.fruit.Orange;

import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Hashtable;

/**
 * Creates an in-memory context and populates it with test data.
 */
public class TestContextFactory extends InMemoryContextFactory {

  @Override
  public Context getInitialContext() throws NamingException {
    Context context = super.getInitialContext();
    populateTestData(context);
    return context;
  }

  @Override
  public Context getInitialContext(Hashtable environment) throws NamingException {
    Context context = super.getInitialContext(environment);
    populateTestData(context);
    return context;
  }

  protected void populateTestData(Context context) throws NamingException {
    context.bind("fruit/apple", new Apple());
    context.bind("fruit/banana", new Banana());
    context.bind("fruit/orange", new Orange(new Integer(8), new Double(10), "Florida Sunny"));
  }
}
