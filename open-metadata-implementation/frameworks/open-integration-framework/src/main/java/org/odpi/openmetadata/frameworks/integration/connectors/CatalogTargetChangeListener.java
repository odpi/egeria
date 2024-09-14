/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.connectors;


import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;

/**
 * An optional interface for an integration connector to implement if it needs to know
 * when its catalog targets change.  They could be added, deleted or updated.  An update
 * is a change to the catalog target relationship only.  Changes to the connector result
 * in the integration daemon restarting the connector.
 */
public interface CatalogTargetChangeListener
{
    /**
     * This catalog target has been created since the last refresh.
     *
     * @param catalogTarget new catalog target
     */
    void newCatalogTarget(RequestedCatalogTarget catalogTarget);

    /**
     * This catalog target has new properties.
     *
     * @param oldCatalogTarget old values
     * @param newCatalogTarget new values
     */
    void updatedCatalogTarget(RequestedCatalogTarget oldCatalogTarget,
                              RequestedCatalogTarget newCatalogTarget);

    /**
     * This catalog target has been removed from the connector.
     *
     * @param catalogTarget removed relationship
     */
    void removedCatalogTarget(RequestedCatalogTarget catalogTarget);
}
