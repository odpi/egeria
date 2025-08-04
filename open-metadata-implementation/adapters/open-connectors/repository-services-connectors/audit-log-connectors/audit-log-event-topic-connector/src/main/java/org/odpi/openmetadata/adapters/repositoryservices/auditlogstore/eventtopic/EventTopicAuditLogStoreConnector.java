/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.eventtopic;


import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreConnectorBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;

import java.util.ArrayList;
import java.util.List;

/**
 * EventTopicAuditLogStoreConnector provides a connector implementation for an event topic audit log destination.
 */
public class EventTopicAuditLogStoreConnector extends OMRSAuditLogStoreConnectorBase
{
    private final List<OpenMetadataTopicConnector> topicConnectors = new ArrayList<>();

    /**
     * Default constructor used by the connector provider.
     */
    public EventTopicAuditLogStoreConnector()
    {
    }


    /**
     * Store the audit log record in the audit log store.
     *
     * @param logRecord  log record to store
     * @return unique identifier assigned to the log record
     * @throws InvalidParameterException indicates that the logRecord parameter is invalid.
     */
    @Override
    public String storeLogRecord(OMRSAuditLogRecord logRecord) throws InvalidParameterException
    {
        final String   methodName = "storeLogRecord";

        super.validateLogRecord(logRecord, methodName);

        if (super.isSupportedSeverity(logRecord))
        {
            for (OpenMetadataTopicConnector topicConnector : topicConnectors)
            {
                if (topicConnector != null)
                {
                    try
                    {
                        topicConnector.sendEvent(super.getJSONLogRecord(logRecord, methodName));
                    }
                    catch (Exception error)
                    {
                        // Ignore errors - need to come up with an approach to handle audit log errors
                    }
                }
            }
        }

        return logRecord.getGUID();
    }


    /**
     * Set up the list of connectors that this virtual connector will use to support its interface.
     * The connectors are initialized waiting to start.  When start() is called on the
     * virtual connector, it needs to pass start() to each of the embedded connectors. Similarly for
     * disconnect().
     *
     * @param embeddedConnectors  list of connectors
     */
    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        this.embeddedConnectors = embeddedConnectors;

        if (embeddedConnectors != null)
        {
            for (Connector  embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector != null)
                {
                    if (embeddedConnector instanceof OpenMetadataTopicConnector realTopicConnector)
                    {
                        /*
                         * Successfully found an event bus connector of the right type.
                         */
                        topicConnectors.add(realTopicConnector);
                    }
                }
            }
        }
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * OMRSTopicConnector needs to pass on the start() to its embedded connectors.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        /*
         * Step through the embedded connectors and start them.
         */
        for (Connector topicConnector : topicConnectors)
        {
            if (topicConnector != null)
            {
                topicConnector.start();
            }
        }
    }
}
