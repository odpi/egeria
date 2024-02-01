/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.fvt.utilities;

/**
 * Constants that provide defaults to the FVT Suite.
 */
public class FVTConstants
{
    /**
     * testUser
     */
    public static final String USERID              = "testUser";

    /**
     * "<a href="https://localhost:9443">"https://localhost:9443"</a>"
     */
    public static final String DEFAULT_URL         = "https://localhost:9443";

    /**
     * fvtMDS
     */
    public static final String DEFAULT_SERVER_NAME = "fvtMDS";

    /*
     * These values are use in the automatic FVT execution in the build using failsafe.
     */

    /**
     * <a href="https://localhost:10443">https://localhost:10443</a>
     */
    public final static String SERVER_PLATFORM_URL_ROOT = "https://localhost:10443";

    /**
     * serverinmem
     */
    public final static String IN_MEMORY_SERVER         = "serverinmem";
}
