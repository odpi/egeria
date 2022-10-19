/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.caching.repositoryconnector;


import org.odpi.openmetadata.adapters.repositoryservices.caching.auditlog.CachingOMRSAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.caching.auditlog.CachingOMRSErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.List;

/**
 * This sample caching repository connector runs in a repository proxy. It expects to be configured with an embedded repository connector
 * where content is cached.
 */
public class CachingOMRSRepositoryProxyConnector extends OMRSRepositoryConnector
        implements VirtualConnectorExtension {

    private List<Connector> embeddedConnectors = null;

    /**
     * Default constructor used by the OCF Connector Provider.
     */
    public CachingOMRSRepositoryProxyConnector() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void start() throws ConnectorCheckedException {

        super.start();
        final String methodName = "start";
        auditLog.logMessage(methodName, CachingOMRSAuditCode.REPOSITORY_SERVICE_STARTING.getMessageDefinition());
        synchronized (this) {
            if (metadataCollection == null) {
                // If the metadata collection has not yet been created, attempt to create it now
                try {
                    initializeMetadataCollection();
                } catch (RepositoryErrorException e) {
                    raiseConnectorCheckedException(CachingOMRSErrorCode.REPOSITORY_ERROR_EXCEPTION, methodName, e);
                }
            }
            // start the embedded collection
            CachingOMRSMetadataCollection cachingCollection = (CachingOMRSMetadataCollection) metadataCollection;
            cachingCollection.getEmbeddedOMRSConnector().start();
        }

        auditLog.logMessage(methodName, CachingOMRSAuditCode.REPOSITORY_SERVICE_STARTED.getMessageDefinition(getServerName()));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void disconnect() {
        final String methodName = "disconnect";
        auditLog.logMessage(methodName, CachingOMRSAuditCode.REPOSITORY_SERVICE_SHUTDOWN.getMessageDefinition(getServerName()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OMRSMetadataCollection getMetadataCollection() throws RepositoryErrorException {
        synchronized (this) {
            if (metadataCollection == null) {
                // If the metadata collection has not yet been created, attempt to create it now
                initializeMetadataCollection();
            }
        }
        return super.getMetadataCollection();
    }

    private void initializeMetadataCollection() throws RepositoryErrorException {

        metadataCollection = new CachingOMRSMetadataCollection(this,
                serverName,
                repositoryHelper,
                repositoryValidator,
                metadataCollectionId,
                embeddedConnectors);


    }

    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors) {

        this.embeddedConnectors = embeddedConnectors;
    }

    /**
     * Throws a ConnectorCheckedException based on the provided parameters.
     *
     * @param errorCode  the error code for the exception
     * @param methodName the method name throwing the exception
     * @param cause      the underlying cause of the exception (if any, otherwise null)
     * @param params     any additional parameters for formatting the error message
     * @throws ConnectorCheckedException always
     */
    private void raiseConnectorCheckedException(CachingOMRSErrorCode errorCode, String methodName, Exception cause, String... params) throws ConnectorCheckedException {
        if (cause == null) {
            throw new ConnectorCheckedException(errorCode.getMessageDefinition(params),
                    this.getClass().getName(),
                    methodName);
        } else {
            throw new ConnectorCheckedException(errorCode.getMessageDefinition(params),
                    this.getClass().getName(),
                    methodName,
                    cause);
        }
    }
}
