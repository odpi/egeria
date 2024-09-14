/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;

/**
 * Provides the base class for connector code that processes a single catalog target.  When converting an
 * integration connector implementation that does not support catalog targets to one that does, move
 * the core working code into a subclass of this class and implement CatalogTargetIntegrator (and optionally
 * CatalogTargetChangeListener if you need notifications.  Ensure you override the getNewRequestedCatalogTargetSkeleton
 * method in CatalogTargetIntegrator to return a new instance of your CatalogTargetProcessorBase class.
 */
public abstract class CatalogTargetProcessorBase extends RequestedCatalogTarget
{
    protected final String         connectorName;
    protected final AuditLog       auditLog;
    protected final PropertyHelper propertyHelper = new PropertyHelper();

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public CatalogTargetProcessorBase(CatalogTarget template,
                                      String        connectorName,
                                      AuditLog      auditLog)
    {
        super(template);

        this.connectorName = connectorName;
        this.auditLog = auditLog;
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    public abstract void refresh() throws ConnectorCheckedException;
}
