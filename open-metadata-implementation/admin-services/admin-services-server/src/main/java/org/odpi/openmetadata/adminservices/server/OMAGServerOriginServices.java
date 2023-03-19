/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;

/**
 * OMAGServerPlatformOriginServices support the origin services for Egeria's OMAG Server.  It is overridden in
 * other server implementations.
 */
public class OMAGServerOriginServices
{
    final String   implementationOrigin = "Egeria OMAG Server Platform (version 4.0-SNAPSHOT)\n";

    /**
     * Return the origin of this server implementation.
     *
     * @param userId name of the user making the request
     * @return String description
     */
    public String getServerOrigin(String    userId)
    {
        return implementationOrigin;
    }
}
