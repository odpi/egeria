/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;

/**
 * This interface indicates that a class can process events on behalf of a catalog target.
 */
public interface CatalogTargetEventProcessor extends CatalogTargetIntegrator
{
    /**
     * Perform the required integration logic for the assigned catalog target.  When an event is received,
     * this method is called for each catalog target.
     *
     * @param requestedCatalogTarget the catalog target
     * @param event event to process
     * @throws ConnectorCheckedException there is an unrecoverable error and the connector should stop processing.
     */
    void passEventToCatalogTarget(RequestedCatalogTarget    requestedCatalogTarget,
                                  OpenMetadataOutTopicEvent event) throws ConnectorCheckedException;
}
