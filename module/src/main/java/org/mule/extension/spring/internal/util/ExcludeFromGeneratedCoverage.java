/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.extension.spring.internal.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ExcludeFromGeneratedCoverage {

}


