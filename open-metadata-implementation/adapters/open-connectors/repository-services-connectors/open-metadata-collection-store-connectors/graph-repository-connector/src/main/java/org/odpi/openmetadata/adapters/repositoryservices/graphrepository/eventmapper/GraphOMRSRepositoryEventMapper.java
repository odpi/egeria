/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.eventmapper;

import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperBase;

import java.util.ArrayList;
import java.util.List;


/**
 * GraphOMRSRepositoryEventMapper supports the event mapper function for the default graph store open
 * metadata repository.
 */
public class GraphOMRSRepositoryEventMapper extends OMRSRepositoryEventMapperBase implements VirtualConnectorExtension,
                                                                                             OpenMetadataTopicListener
{
    private static final OMRSAuditLog auditLog = new OMRSAuditLog(OMRSAuditingComponent.LOCAL_REPOSITORY_EVENT_MAPPER);

    private List<OpenMetadataTopicConnector> eventBusConnectors = new ArrayList<>();

    /**
     * Default constructor
     */
    public GraphOMRSRepositoryEventMapper()
    {
        super();
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException
    {
        super.start();
    }

    /**
     * Registers itself as a listener of any OpenMetadataTopicConnectors that are passed as
     * embedded connectors.
     *
     * @param embeddedConnectors  list of connectors
     */
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        final String  methodName = "initializeEmbeddedConnectors";

        /*
         * Step through the embedded connectors, selecting only the OpenMetadataTopicConnectors
         * to use.
         */
        if (embeddedConnectors != null)
        {
            for (Connector  embeddedConnector : embeddedConnectors)
            {
                if ((embeddedConnector != null) && (embeddedConnector instanceof OpenMetadataTopicConnector))
                {
                    /*
                     * Successfully found an event bus connector of the right type.
                     */
                    OpenMetadataTopicConnector realTopicConnector = (OpenMetadataTopicConnector)embeddedConnector;

                    String   topicName = realTopicConnector.registerListener(this);
                    this.eventBusConnectors.add(realTopicConnector);

                    OMRSAuditCode auditCode = OMRSAuditCode.EVENT_MAPPER_LISTENER_REGISTERED;
                    auditLog.logRecord(methodName,
                                       auditCode.getLogMessageId(),
                                       auditCode.getSeverity(),
                                       auditCode.getFormattedLogMessage(repositoryEventMapperName, topicName),
                                       this.getConnection().toString(),
                                       auditCode.getSystemAction(),
                                       auditCode.getUserAction());
                }
            }
        }

        /*
         * OMRSTopicConnector needs at least one event bus connector to operate successfully.
         */
        if (this.eventBusConnectors.isEmpty())
        {
            OMRSAuditCode auditCode = OMRSAuditCode.EVENT_MAPPER_LISTENER_DEAF;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(repositoryEventMapperName),
                               this.getConnection().toString(),
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Method to pass an event received on topic.
     *
     * @param event inbound event
     */
    public void processEvent(String event)
    {

    }

    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
