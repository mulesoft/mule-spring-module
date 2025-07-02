package org.mule.extension.spring.internal;

import org.mule.runtime.api.meta.MuleVersion;

public final class SpringModuleConstants {
    public static final String VERSION = "@project.version@";
    public static final MuleVersion MIN_MULE_VERSION = new MuleVersion("4.9");
    public static final String SPRING_VERSION = "@springVersion@";
    public static final String SPRING_SECURITY_VERSION = "@springSecurityVersion@";

    private SpringModuleConstants() {}
} 