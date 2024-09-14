/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.ffdc.DistributeKafkaAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.ffdc.DistributeKafkaErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetChangeListener;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreConnectorBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.ArrayList;
import java.util.List;

/**
 * Distributes audit log events from one or more embedded topic connectors to one or more embedded audit log destinations.
 */
public class AuditLogDestinationCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    private final OMRSAuditLogStoreConnectorBase auditLogDestination;


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     */
    public AuditLogDestinationCatalogTargetProcessor(CatalogTarget template,
                                                     String        connectorName,
                                                     AuditLog      auditLog)
    {
        super(template, connectorName, auditLog);

        if (super.getCatalogTargetConnector() instanceof OMRSAuditLogStoreConnectorBase auditLogStoreConnectorBase)
        {
            this.auditLogDestination = auditLogStoreConnectorBase;
        }
        else
        {
            this.auditLogDestination = null;
        }
    }


    /**
     * Store the audit log record in the audit log store.
     *
     * @param logRecord log record to store
     */
    public void storeLogRecord(OMRSAuditLogRecord logRecord)
    {
        final String methodName = "storeLogRecord";

        if (auditLogDestination != null)
        {
            try
            {
                auditLogDestination.storeLogRecord(logRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      DistributeKafkaAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),

                                                                                                         error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     */
    @Override
    public void refresh()
    {
        // nothing to do
    }
}
