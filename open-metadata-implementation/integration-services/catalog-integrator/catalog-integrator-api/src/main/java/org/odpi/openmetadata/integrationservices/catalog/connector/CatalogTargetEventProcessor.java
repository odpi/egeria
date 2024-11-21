/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;

/**
 * This interface indicates that a class can process events on behalf of a catalog target.
 */
public interface CatalogTargetEventProcessor extends CatalogTargetIntegrator
{
    /**
     * Perform the required integration logic for the assigned catalog target.
     *
     * @param requestedCatalogTarget the catalog target
     * @param event event to process
     * @throws ConnectorCheckedException there is an unrecoverable error and the connector should stop processing.
     */
    void passEventToCatalogTarget(RequestedCatalogTarget    requestedCatalogTarget,
                                  AssetManagerOutTopicEvent event) throws ConnectorCheckedException;
}
