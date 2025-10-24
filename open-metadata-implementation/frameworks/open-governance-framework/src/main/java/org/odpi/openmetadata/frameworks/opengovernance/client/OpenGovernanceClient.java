/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance.client;

import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;

/**
 * OpenGovernanceClient provides access to metadata elements stored in the metadata repositories.  It is implemented by a
 * metadata repository provider. In Egeria, this class is implemented in the GAF Metadata Management running in the
 * Metadata Access Server OMAG Server.
 */
public abstract class OpenGovernanceClient  implements ActionControlInterface,
                                                       GovernanceActionProcessInterface,
                                                       DuplicateManagementInterface
{
    protected final String serverName;               /* Initialized in constructor */
    protected final String serviceURLMarker;         /* Initialized in constructor */
    protected final String serverPlatformURLRoot;    /* Initialized in constructor */

    protected PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     */
    public OpenGovernanceClient(String serviceURLMarker,
                                String serverName,
                                String serverPlatformURLRoot)
    {
        this.serviceURLMarker = serviceURLMarker;
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenMetadataClient{" +
                "serverName='" + serverName + '\'' +
                ", serviceURLMarker='" + serviceURLMarker + '\'' +
                ", serverPlatformURLRoot='" + serverPlatformURLRoot + '\'' +
                '}';
    }
}
