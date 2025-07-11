/*
 * Copyright 2025 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.spring.api.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mule.extension.spring.internal.security.SecurityProperty;
import org.mule.extension.spring.internal.security.SpringAuthenticationAdapter;
import org.mule.runtime.api.security.Authentication;
import org.mule.runtime.api.security.SecurityException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;

public class SpringProviderAdapterTest {

  private static final String PROVIDER_NAME = "test-provider";
  private SpringProviderAdapter adapter;
  private AuthenticationManager mockDelegate;
  private Authentication mockAuthentication;
  private org.springframework.security.core.Authentication mockSpringAuth;

  @Before
  public void setUp() {
    mockDelegate = mock(AuthenticationManager.class);
    mockAuthentication = mock(Authentication.class);
    mockSpringAuth = mock(org.springframework.security.core.Authentication.class);
    adapter = new SpringProviderAdapter(mockDelegate, PROVIDER_NAME);
  }

  @Test
  public void testDefaultConstructor() {
    SpringProviderAdapter defaultAdapter = new SpringProviderAdapter();
    assertThat(defaultAdapter.getName(), is("spring-security"));
  }

  @Test
  public void testConstructorWithDelegate() {
    SpringProviderAdapter delegateAdapter = new SpringProviderAdapter(mockDelegate);
    assertThat(delegateAdapter.getName(), is("spring-security"));
    assertThat(delegateAdapter.getDelegate(), is(mockDelegate));
  }

  @Test
  public void testConstructorWithDelegateAndName() {
    assertThat(adapter.getName(), is(PROVIDER_NAME));
    assertThat(adapter.getDelegate(), is(mockDelegate));
  }

  @Test
  public void testDoInitialise() throws Exception {
    adapter.doInitialise();
    assertThat(adapter.getSecurityContextFactory(), notNullValue());
  }

  @Test
  public void testAuthenticateWithSpringAuthenticationAdapter() throws SecurityException {
    SpringAuthenticationAdapter springAuth = mock(SpringAuthenticationAdapter.class);
    when(springAuth.getDelegate()).thenReturn(mockSpringAuth);
    when(mockDelegate.authenticate(mockSpringAuth)).thenReturn(mockSpringAuth);

    Authentication result = adapter.authenticate(springAuth);

    assertThat(result, notNullValue());
    verify(mockDelegate).authenticate(mockSpringAuth);
  }

  @Test
  public void testAuthenticateWithRegularAuthentication() throws SecurityException {
    when(mockDelegate.authenticate(any())).thenReturn(mockSpringAuth);

    Authentication result = adapter.authenticate(mockAuthentication);

    assertThat(result, notNullValue());
    verify(mockDelegate).authenticate(any());
  }

  @Test
  public void testSpringAuthenticate() throws AuthenticationException {
    when(mockDelegate.authenticate(mockSpringAuth)).thenReturn(mockSpringAuth);

    org.springframework.security.core.Authentication result = adapter.authenticate(mockSpringAuth);

    assertThat(result, is(mockSpringAuth));
    verify(mockDelegate).authenticate(mockSpringAuth);
  }

  @Test(expected = AuthenticationException.class)
  public void testSpringAuthenticateWithException() throws AuthenticationException {
    when(mockDelegate.authenticate(mockSpringAuth)).thenThrow(new AuthenticationException("Test exception") {});

    assertNotNull(adapter.authenticate(mockSpringAuth));
  }

  @Test
  public void testSetSecurityPropertiesWithList() {
    List<SecurityProperty> properties = new ArrayList<>();
    SecurityProperty prop1 = new SecurityProperty("key1", "value1");
    SecurityProperty prop2 = new SecurityProperty("key2", "value2");
    properties.add(prop1);
    properties.add(prop2);

    adapter.setSecurityProperties(properties);
    Map result = adapter.getSecurityProperties();

    assertThat(result, notNullValue());
    assertThat(result.get("key1"), is("value1"));
    assertThat(result.get("key2"), is("value2"));
  }

  @Test
  public void testSetSecurityPropertiesWithMap() {
    Map<String, String> properties = new HashMap<>();
    properties.put("key1", "value1");
    properties.put("key2", "value2");

    adapter.setSecurityProperties(properties);
    Map result = adapter.getSecurityProperties();

    assertThat(result, is(properties));
  }

  @Test
  public void testGetAuthenticationProvider() {
    SpringAuthenticationProvider provider = adapter.getAuthenticationProvider();
    assertThat(provider, notNullValue());
    assertThat(provider instanceof UserAndPasswordAuthenticationProvider, is(true));
  }

  @Test
  public void testSetAuthenticationProvider() {
    SpringAuthenticationProvider mockProvider = mock(SpringAuthenticationProvider.class);
    adapter.setAuthenticationProvider(mockProvider);
    assertThat(adapter.getAuthenticationProvider(), is(mockProvider));
  }

  @Test
  public void testGetDelegate() {
    assertThat(adapter.getDelegate(), is(mockDelegate));
  }

  @Test
  public void testSetDelegate() {
    AuthenticationManager newDelegate = mock(AuthenticationManager.class);
    adapter.setDelegate(newDelegate);
    assertThat(adapter.getDelegate(), is(newDelegate));
  }
}
