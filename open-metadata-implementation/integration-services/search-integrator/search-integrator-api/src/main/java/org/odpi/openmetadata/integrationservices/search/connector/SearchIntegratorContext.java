/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.search.connector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

/**
 * LineageIntegratorContext provides a wrapper around the Asset Manager OMAS client.
 * It provides the simplified interface to open metadata needed by the LineageIntegratorConnector.
 */
public class SearchIntegratorContext
{
    private String                        userId;
    private String                        assetManagerGUID;
    private String                        assetManagerName;
    private String                        connectorName;
    private String                        integrationServiceName;
    private AuditLog                      auditLog;


    /**
     * Create a new context for a connector.
     *
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param integrationServiceName name of this service
     * @param auditLog logging destination
     */
    public SearchIntegratorContext(
                                   String                  userId,
                                   String                  assetManagerGUID,
                                   String                  assetManagerName,
                                   String                  connectorName,
                                   String                  integrationServiceName,
                                   AuditLog                auditLog)
    {

        this.userId                  = userId;
        this.assetManagerGUID        = assetManagerGUID;
        this.assetManagerName        = assetManagerName;
        this.connectorName           = connectorName;
        this.integrationServiceName  = integrationServiceName;
        this.auditLog                = auditLog;
    }
}
