/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.connectors;

import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;

/**
 * CatalogTargetIntegrator is an optional interface that an integration connector can implement to walk through
 * the catalog targets assigned to the integration connector.
 */
public interface CatalogTargetIntegrator
{
    /**
     * Perform the required integration logic for the assigned catalog target.
     *
     * @param requestedCatalogTarget the catalog target
     * @throws ConnectorCheckedException there is an unrecoverable error and the connector should stop processing.
     */
    void integrateCatalogTarget(RequestedCatalogTarget requestedCatalogTarget) throws ConnectorCheckedException;


    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param connectorToTarget connector to access the target resource
     * @return new processor based on the catalog target information
     * @throws ConnectorCheckedException problem setting up target
     */
    default RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget retrievedCatalogTarget,
                                                                        Connector     connectorToTarget) throws ConnectorCheckedException
    {
        return new RequestedCatalogTarget(retrievedCatalogTarget, connectorToTarget);
    }
}
