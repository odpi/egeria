/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.search.connector;

import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogEvent;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.integrationservices.search.ffdc.SearchIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.search.ffdc.SearchIntegratorErrorCode;

/**
 * SearchIntegratorConnector is the base class for an integration connector that is managed by the
 * Search Integrator OMIS.
 */
public abstract class SearchIntegratorConnector extends IntegrationConnectorBase implements  SearchIntegratorOMISConnector {
    private SearchIntegratorContext context = null;


    public SearchIntegratorConnector() {
        super();
        System.out.println("calling constructor");
    }

    public SearchIntegratorConnector(SearchIntegratorContext context) {
        super();
        this.context = context;
    }

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public synchronized void setContext(SearchIntegratorContext context) {
        this.context = context;
    }

    /**
     * Return the context for this connector.  It is called by the connector.
     *
     * @return context for this connector's private use.
     * @throws ConnectorCheckedException internal issue setting up context
     */
    public synchronized SearchIntegratorContext getContext() throws ConnectorCheckedException {
        final String methodName = "getContext";

        if (context != null) {
            return this.context;
        } else {
            if (auditLog != null) {
                auditLog.logMessage(methodName, SearchIntegratorAuditCode.NULL_CONTEXT.getMessageDefinition(connectorName));
            }

            throw new ConnectorCheckedException(SearchIntegratorErrorCode.NULL_CONTEXT.getMessageDefinition(connectorName),
                    this.getClass().getName(),
                    methodName);
        }
    }

    /**
     * Save the events received from asset catalog to the Elasticsearch service
     *
     * @param assetCatalogEvent the event which contains the asset
     */
    public abstract void saveAsset(AssetCatalogEvent assetCatalogEvent);
}
