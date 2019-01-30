/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.outtopic;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetlineage.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.events.AssetLineageEventHeader;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AssetLineagePublisher is responsible for publishing events about lineage.  It is called
 * when an interesting OMRS Event is added to the Enterprise OMRS Topic.  It adds events to the Asset lineage OMAS
 * out topic.
 */
public class AssetLineagePublisher {

    private static final Logger log = LoggerFactory.getLogger(AssetLineagePublisher.class);

    private OMRSRepositoryHelper repositoryHelper;
    private OMRSRepositoryValidator repositoryValidator;
    private String serviceName;
    private OpenMetadataTopicConnector connector;


    /**
     * The constructor is given the connection to the out topic for Asset lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetLineageOutTopic connection to the out topic
     * @param repositoryConnector  provides methods for retrieving metadata instances
     * @param repositoryHelper     provides methods for working with metadata instances
     * @param repositoryValidator  provides validation of metadata instance
     * @param serviceName          name of this service.
     */
    public AssetLineagePublisher(Connection assetLineageOutTopic,
                                 OMRSRepositoryConnector repositoryConnector,
                                 OMRSRepositoryHelper repositoryHelper,
                                 OMRSRepositoryValidator repositoryValidator,
                                 String serviceName,
                                 OMRSAuditLog auditLog) throws OMAGConfigurationErrorException {
        this.repositoryHelper = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.serviceName = serviceName;
        if (assetLineageOutTopic != null)
        {
            connector = this.getTopicConnector(assetLineageOutTopic, auditLog);
        }
    }

    public void processNewEntity(EntityDetail entity) { }
    public void processUpdatedEntity(EntityDetail entity) {}
    public void processUpdatedEntity(EntityDetail originalEntity, EntityDetail newEntity) {}
    public void processDeletedEntity(EntityDetail entity) { }
    public void processRestoredEntity(EntityDetail entity) {}
    public void processNewRelationship(Relationship relationship) {}
    public void processNewRelationship(OMRSInstanceEvent event) {
        try{
            if (connector != null)            {
                connector.sendEvent(this.getJSONPayload(event));
            }
        }
        catch (Throwable  error){
            log.error("Unable to publish new asset event: " + event.toString() + "; error was " + error.toString());
        }
    }
    public void processUpdatedRelationship(Relationship relationship) {}
    public void processUpdatedRelationship(Relationship originalRelationship, Relationship newRelationship) { }
    public void processDeletedRelationship(Relationship relationship) {  }
    public void processRestoredRelationship(Relationship relationship) {}

    /**
     * Create the topic connector.
     *
     * @param topicConnection connection to create the connector
     * @param auditLog audit log for the connector
     * @return open metadata topic connector
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection   topicConnection,
                                                         OMRSAuditLog auditLog) throws OMAGConfigurationErrorException
    {
        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker();
            Connector connector       = connectorBroker.getConnector(topicConnection);
            OpenMetadataTopicConnector topicConnector  = (OpenMetadataTopicConnector)connector;
            topicConnector.setAuditLog(auditLog);
            topicConnector.start();
            return topicConnector;
        }
        catch (Throwable   error)
        {
            String methodName = "getTopicConnector";
            log.error("Unable to create topic connector: " + error.toString());
            AssetLineageErrorCode errorCode = AssetLineageErrorCode.BAD_OUT_TOPIC_CONNECTION;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(topicConnection.toString(), error.getClass().getName(), error.getMessage());

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    error);
        }
    }

    /**
     * Return the event as a String where the field contents are encoded in JSON.   The event beans
     * contain annotations that mean the whole event, down to the lowest subclass, is serialized.
     *
     * @return JSON payload (as String)
     */
    private String getJSONPayload(AssetLineageEventHeader event)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;
        try
        {
            jsonString = objectMapper.writeValueAsString(event);
        }
        catch (Throwable  error)
        {
            log.error("Unable to create event payload: " + error.toString());
        }
        return jsonString;
    }


}


