/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.outtopic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.accessservices.subjectarea.events.SubjectAreaEvent;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SubjectAreaPublisher is responsible for publishing org.odpi.openmetadata.accessservices.subjectarea.common.events about glossary artifacts.  It is called
 * when an interesting OMRS Event is added to the Enterprise OMRS Topic.  It adds org.odpi.openmetadata.accessservices.subjectarea.common.events to the Subject Area OMAS
 * out topic.
 */
public class SubjectAreaPublisher
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaPublisher.class);

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    private OpenMetadataTopicConnector connector = null;


    /**
     * The constructor is given the connection to the out topic for Subject Area OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param subjectAreaOutTopic connection to the out topic
     * @param auditLog log file for the connector.
     * @throws OMAGConfigurationErrorException problems creating the connector for the outTopic
     */
    public SubjectAreaPublisher(Connection subjectAreaOutTopic,
                               AuditLog auditLog) throws OMAGConfigurationErrorException
    {
        if (subjectAreaOutTopic != null)
        {
            connector = this.getTopicConnector(subjectAreaOutTopic, auditLog);
        }
    }


//    /**
//     * Determine whether a new entity is a type we are interested in.  If it is then publish a subject area Event relating to it.
//     * We are interested in any entity that is connected to glossary, glossaryCategory or glossaryTerm (including via Referencable
//     * relationships) via a relationship.
//     *
//     * If there is a new comment associated with a Node then have an event for
//     * Node changes
//     * One for associated entities passing through the glossary term identifier.
//     *
//     * @param entity - entity object that has just been created.
//     */
//    public void processNewEntity(EntityDetail entity)
//    {
//        // TODO
//    }
//
//
//    /**
//     *
//     * @param entity - entity object that has just been updated.
//     */
//    public void processUpdatedEntity(EntityDetail   entity)
//    {
//        // TODO
//    }
//
//
//    /**
//     *
//     * @param entity - entity object that has just been deleted.
//     */
//    public void processDeletedEntity(EntityDetail   entity)
//    {
//        // TODO
//    }
//
//
//    /**
//     *
//     * @param entity - entity object that has just been restored.
//     */
//    public void processRestoredEntity(EntityDetail   entity)
//    {
//        // TODO
//    }
//
//
//    /**
//     *
//     * @param relationship - relationship object that has just been created.
//     */
//    public void processNewRelationship(Relationship relationship)
//    {
//        // TODO
//    }
//
//
//    /**
//     *
//     * @param relationship - relationship object that has just been updated.
//     */
//    public void processUpdatedRelationship(Relationship   relationship)
//    {
//        // TODO
//    }
//
//
//    /**
//     *
//     * @param relationship - relationship object that has just been deleted.
//     */
//    public void processDeletedRelationship(Relationship   relationship)
//    {
//        // todo
//    }
//
//
//    /**
//     *
//     * @param relationship - relationship object that has just been restored.
//     */
//    public void processRestoredRelationship(Relationship   relationship)
//    {
//        // todo
//    }
//
//


    /**
     * Create the topic connector.
     *
     * @param topicConnection connection to create the connector
     * @param auditLog audit log for the connector
     * @return open metadata topic connector
     * @throws OMAGConfigurationErrorException problems creating the connector for the outTopic
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection  topicConnection,
                                                         AuditLog    auditLog) throws OMAGConfigurationErrorException
    {
        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);
            Connector connector       = connectorBroker.getConnector(topicConnection);

            OpenMetadataTopicConnector topicConnector  = (OpenMetadataTopicConnector)connector;

            topicConnector.start();

            return topicConnector;
        }
        catch (Exception   error)
        {
            String methodName = "getTopicConnector";

            log.error("Unable to create topic connector: " + error.toString());

            throw new OMAGConfigurationErrorException(SubjectAreaErrorCode.BAD_OUT_TOPIC_CONNECTION.getMessageDefinition(topicConnection.toString(),
                                                                                                                         error.getClass().getName(),
                                                                                                                         error.getMessage()),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      error);
        }
    }


    /**
     * Return the event as a String where the field contents are encoded in JSON.   The event beans
     * contain annotations that mean the whole event, down to the lowest subclass, is serialized.
     *
     * @param event event to serialize
     * @return JSON payload (as String)
     */
    private String getJSONPayload(SubjectAreaEvent event)
    {
        String       jsonString   = null;

        /*
         * This class
         */
        try
        {
            jsonString = OBJECT_WRITER.writeValueAsString(event);
        }
        catch (Exception  error)
        {
            log.error("Unable to create event payload: " + error.toString());
        }

        return jsonString;
    }





}

