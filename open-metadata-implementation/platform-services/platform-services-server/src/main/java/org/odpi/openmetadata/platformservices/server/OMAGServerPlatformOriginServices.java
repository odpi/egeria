/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server;

/**
 * OMAGServerPlatformOriginServices support the origin services for Egeria's OMAG Server.  It is overridden in
 * other server platform implementations.
 */
public class OMAGServerPlatformOriginServices
{
    final String   implementationOrigin = "Egeria OMAG Server Platform (version 5.1)\n";

    /**
     * Return the origin of this server platform implementation.
     *
     * @param userId name of the user making the request
     * @return String description
     */
    public String getServerPlatformOrigin(String    userId)
    {
        return implementationOrigin;
    }
}
