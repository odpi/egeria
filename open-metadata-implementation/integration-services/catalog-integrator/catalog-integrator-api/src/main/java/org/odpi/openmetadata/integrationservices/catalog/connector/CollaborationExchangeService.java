/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.CollaborationExchangeClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;


/**
 * CollaborationExchangeService is the context for managing user collaboration elements such as comments, ratings, likes and tags.
 */
public class CollaborationExchangeService
{
    private final CollaborationExchangeClient collaborationExchangeClient;
    private final String                      userId;
    private final String                      assetManagerGUID;
    private final String                      assetManagerName;
    private final String                   connectorName;
    private final PermittedSynchronization permittedSynchronization;
    private final AuditLog                 auditLog;

    private boolean forLineage             = false;
    private boolean forDuplicateProcessing = false;

    /**
     * Create a new client to exchange data asset content with open metadata.
     *
     * @param collaborationExchangeClient client for exchange requests
     * @param permittedSynchronization direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    CollaborationExchangeService(CollaborationExchangeClient collaborationExchangeClient,
                                 PermittedSynchronization permittedSynchronization,
                                 String                      userId,
                                 String                      assetManagerGUID,
                                 String                      assetManagerName,
                                 String                      connectorName,
                                 AuditLog                    auditLog)
    {
        this.collaborationExchangeClient = collaborationExchangeClient;
        this.permittedSynchronization    = permittedSynchronization;
        this.userId                      = userId;
        this.assetManagerGUID            = assetManagerGUID;
        this.assetManagerName            = assetManagerName;
        this.connectorName               = connectorName;
        this.auditLog                    = auditLog;
    }


    /* ========================================================
     * Set up the forLineage flag
     */

    /**
     * Return whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @return boolean flag
     */
    public boolean isForLineage()
    {
        return forLineage;
    }


    /**
     * Set up whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @param forLineage boolean flag
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /* ========================================================
     * Set up the forDuplicateProcessing flag
     */

    /**
     * Return whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @return boolean flag
     */
    public boolean isForDuplicateProcessing()
    {
        return forDuplicateProcessing;
    }


    /**
     * Set up whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @param forDuplicateProcessing boolean flag
     */
    public void setForDuplicateProcessing(boolean forDuplicateProcessing)
    {
        this.forDuplicateProcessing = forDuplicateProcessing;
    }


}
