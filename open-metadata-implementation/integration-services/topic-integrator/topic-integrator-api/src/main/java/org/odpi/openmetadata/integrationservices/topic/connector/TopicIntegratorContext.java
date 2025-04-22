/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.topic.connector;

import org.odpi.openmetadata.accessservices.datamanager.api.DataManagerEventListener;
import org.odpi.openmetadata.accessservices.datamanager.client.ConnectionManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.client.EventBrokerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.ValidValueManagement;
import org.odpi.openmetadata.frameworks.governanceaction.client.ActionControlInterface;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.events.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueProperties;

import java.util.List;
import java.util.Map;


/**
 * TopicIntegratorContext is the context for cataloging topics from an event broker server.
 */
public class TopicIntegratorContext extends IntegrationContext
{
    private final ConnectionManagerClient connectionManagerClient;
    private final ValidValueManagement    validValueManagement;
    private final EventBrokerClient       eventBrokerClient;
    private final DataManagerEventClient  eventClient;

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param serverName name of the integration daemon
     * @param openIntegrationClient client for calling the metadata server
     * @param governanceConfiguration client for managing catalog targets
     * @param openMetadataStoreClient client for calling the metadata server
     * @param actionControlInterface client for initiating governance actions
     * @param eventBrokerClient client to map request to
     * @param connectionManagerClient client for managing connections
     * @param validValueManagement client for managing valid value sets and definitions
     * @param eventClient client to register for events
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization the direction of integration permitted by the integration connector
     * @param integrationConnectorGUID unique identifier for the integration connector if it is started via an integration group (otherwise it is
     *                                 null).
     * @param externalSourceGUID unique identifier of the software server capability for the asset manager
     * @param externalSourceName unique name of the software server capability for the asset manager
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public TopicIntegratorContext(String                       connectorId,
                                  String                       connectorName,
                                  String                       connectorUserId,
                                  String                       serverName,
                                  OpenIntegrationClient        openIntegrationClient,
                                  GovernanceConfiguration      governanceConfiguration,
                                  OpenMetadataClient           openMetadataStoreClient,
                                  ActionControlInterface       actionControlInterface,
                                  EventBrokerClient            eventBrokerClient,
                                  ConnectionManagerClient      connectionManagerClient,
                                  ValidValueManagement         validValueManagement,
                                  DataManagerEventClient       eventClient,
                                  boolean                      generateIntegrationReport,
                                  PermittedSynchronization     permittedSynchronization,
                                  String                       integrationConnectorGUID,
                                  String                       externalSourceGUID,
                                  String                       externalSourceName,
                                  AuditLog                     auditLog,
                                  int                          maxPageSize)
    {
        super(connectorId,
              connectorName,
              connectorUserId,
              serverName,
              openIntegrationClient,
              governanceConfiguration,
              openMetadataStoreClient,
              actionControlInterface,
              generateIntegrationReport,
              permittedSynchronization,
              externalSourceGUID,
              externalSourceName,
              integrationConnectorGUID,
              auditLog,
              maxPageSize);

        this.eventBrokerClient       = eventBrokerClient;
        this.connectionManagerClient = connectionManagerClient;
        this.validValueManagement    = validValueManagement;
        this.eventClient             = eventClient;
    }



    /* ========================================================
     * Returning the event broker name from the configuration
     */

    /**
     * Return the qualified name of the event broker that is supplied in the configuration
     * document.
     *
     * @return string name
     */
    public String getEventBrokerName()
    {
        return externalSourceName;
    }


    /**
     * Return the unique identifier of the event broker's entity that corresponds to the eventBrokerName.
     *
     * @return string name
     */
    public String getEventBrokerGUID()
    {
        return externalSourceGUID;
    }


    /* ========================================================
     * Set up whether topic/event metadata is owned by the event broker
     */


    /**
     * Set up the flag that controls the ownership of metadata created for this Event Broker. Default is true.
     *
     * @param eventBrokerIsHome should the topic metadata be marked as owned by the event broker so others can not update?
     */
    public void setEventBrokerIsHome(boolean eventBrokerIsHome)
    {
        super.externalSourceIsHome = eventBrokerIsHome;
    }


    /* ========================================================
     * Register for inbound events from the Data Manager OMAS OutTopic
     */


    /**
     * Register a listener object that will be passed each of the events published by the Data Manager OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(DataManagerEventListener listener) throws InvalidParameterException,
                                                                           ConnectionCheckedException,
                                                                           ConnectorCheckedException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }


    /* ========================================================
     * The topic is the top level asset on a topic server
     */


    /**
     * Create a new metadata element to represent a topic.
     *
     * @param topicProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createTopic(TopicProperties topicProperties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        String topicGUID = eventBrokerClient.createTopic(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, topicProperties);

        if ((topicGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(topicGUID);
        }

        return topicGUID;
    }


    /**
     * Create a new metadata element to represent a topic using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createTopicFromTemplate(String             templateGUID,
                                          TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        String topicGUID = eventBrokerClient.createTopicFromTemplate(userId,
                                                                     externalSourceGUID,
                                                                     externalSourceName,
                                                                     externalSourceIsHome,
                                                                     templateGUID,
                                                                     templateProperties);

        if ((topicGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(topicGUID);
        }

        return topicGUID;
    }


    /**
     * Update the metadata element representing a topic.
     *
     * @param topicGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param topicProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateTopic(String          topicGUID,
                            boolean         isMergeUpdate,
                            TopicProperties topicProperties) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        eventBrokerClient.updateTopic(userId, externalSourceGUID, externalSourceName, topicGUID, isMergeUpdate, topicProperties);

        if ((topicGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementUpdate(topicGUID);
        }
    }


    /**
     * Update the zones for the topic asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param topicGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishTopic(String topicGUID) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException
    {
        eventBrokerClient.publishTopic(userId, topicGUID);

        if ((topicGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementUpdate(topicGUID);
        }
    }


    /**
     * Update the zones for the topic asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the topic is first created).
     *
     * @param topicGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawTopic(String topicGUID) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        eventBrokerClient.withdrawTopic(userId, topicGUID);

        if ((topicGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementUpdate(topicGUID);
        }
    }


    /**
     * Remove the metadata element representing a topic.
     *
     * @param topicGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeTopic(String topicGUID,
                            String qualifiedName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        eventBrokerClient.removeTopic(userId, externalSourceGUID, externalSourceName, topicGUID, qualifiedName);

        if ((topicGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementDelete(topicGUID);
        }
    }


    /**
     * Retrieve the list of topic metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<TopicElement> findTopics(String searchString,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        return eventBrokerClient.findTopics(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of topic metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<TopicElement>   getTopicsByName(String name,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return eventBrokerClient.getTopicsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of topics created by this caller.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<TopicElement>   getMyTopics(int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return eventBrokerClient.getTopicsForEventBroker(userId, externalSourceGUID, externalSourceName, startFrom, pageSize);
    }


    /**
     * Retrieve the topic metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public TopicElement getTopicByGUID(String guid) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        return eventBrokerClient.getTopicByGUID(userId, guid);
    }


    /* ============================================================================
     * A topic may host one or more event types depending on its capability
     */

    /**
     * Create a new metadata element to represent a event type.
     *
     * @param topicGUID unique identifier of the topic where the event type is located
     * @param properties properties about the event type
     *
     * @return unique identifier of the new event type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEventType(String              topicGUID,
                                  EventTypeProperties properties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        String eventTypeGUID;
        if (externalSourceIsHome)
        {
            eventTypeGUID = eventBrokerClient.createEventType(userId, externalSourceGUID, externalSourceName, topicGUID, properties);
        }
        else
        {
            eventTypeGUID = eventBrokerClient.createEventType(userId, null, null, topicGUID, properties);
        }

        if ((eventTypeGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(eventTypeGUID);
        }

        return eventTypeGUID;
    }



    /**
     * Create a new metadata element to represent an event type using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param topicGUID unique identifier of the topic where the event type is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new event type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEventTypeFromTemplate(String             templateGUID,
                                              String             topicGUID,
                                              TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        String eventTypeGUID;

        if (externalSourceIsHome)
        {
            eventTypeGUID = eventBrokerClient.createEventTypeFromTemplate(userId,
                                                                          externalSourceGUID,
                                                                          externalSourceName,
                                                                          templateGUID,
                                                                          topicGUID,
                                                                          templateProperties);
        }
        else
        {
            eventTypeGUID = eventBrokerClient.createEventTypeFromTemplate(userId,
                                                                          null,
                                                                          null,
                                                                          templateGUID,
                                                                          topicGUID,
                                                                          templateProperties);
        }

        if ((eventTypeGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(eventTypeGUID);
        }

        return eventTypeGUID;
    }


    /**
     * Update the metadata element representing an event type.
     *
     * @param eventTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateEventType(String              eventTypeGUID,
                                boolean             isMergeUpdate,
                                EventTypeProperties properties) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        eventBrokerClient.updateEventType(userId, externalSourceGUID, externalSourceName, eventTypeGUID, isMergeUpdate, properties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(eventTypeGUID);
        }
    }


    /**
     * Remove the metadata element representing an event type.
     *
     * @param eventTypeGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeEventType(String eventTypeGUID,
                                String qualifiedName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        eventBrokerClient.removeEventType(userId, externalSourceGUID, externalSourceName, eventTypeGUID, qualifiedName);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(eventTypeGUID);
        }
    }


    /**
     * Retrieve the list of event type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EventTypeElement>   findEventTypes(String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return eventBrokerClient.findEventTypes(userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of event types associated with a topic.
     *
     * @param eventSetGUID unique identifier of the topic to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the event types associated with the requested topic
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EventTypeElement> getEventTypesForEventSet(String eventSetGUID,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return eventBrokerClient.getEventTypesForEventSet(userId, eventSetGUID, startFrom, pageSize);
    }



    /**
     * Return the list of event types associated with a topic.
     *
     * @param topicGUID unique identifier of the topic to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the event types associated with the requested topic
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EventTypeElement> getEventTypesForTopic(String topicGUID,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return eventBrokerClient.getEventTypesForTopic(userId, topicGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of event type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EventTypeElement>   getEventTypesByName(String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return eventBrokerClient.getEventTypesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the event type metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EventTypeElement getEventTypeByGUID(String guid) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        return eventBrokerClient.getEventTypeByGUID(userId, guid);
    }


    /* =====================================================================================================================
     * A schemaType is used to describe complex structures found in the schema of an event type
     */

    /**
     * Create a new metadata element to represent a primitive schema type such as a string, integer or character.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createPrimitiveSchemaType(PrimitiveSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        String schemaTypeGUID;
        if (externalSourceIsHome)
        {
            schemaTypeGUID = eventBrokerClient.createPrimitiveSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeProperties);
        }
        else
        {
            schemaTypeGUID = eventBrokerClient.createPrimitiveSchemaType(userId, null, null, schemaTypeProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed value.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createLiteralSchemaType(LiteralSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = eventBrokerClient.createLiteralSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeProperties);
        }
        else
        {
            schemaTypeGUID = eventBrokerClient.createLiteralSchemaType(userId, null, null, schemaTypeProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed set of values that are described by a valid value set.
     *
     * @param schemaTypeProperties properties about the schema type to store
     * @param validValuesSetGUID unique identifier of the valid values set to used
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEnumSchemaType(EnumSchemaTypeProperties schemaTypeProperties,
                                       String                   validValuesSetGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = eventBrokerClient.createEnumSchemaType(userId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    schemaTypeProperties,
                                                                    validValuesSetGUID);
        }
        else
        {
            schemaTypeGUID = eventBrokerClient.createEnumSchemaType(userId,
                                                                    null,
                                                                    null,
                                                                    schemaTypeProperties,
                                                                    validValuesSetGUID);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Retrieve the list of valid value set metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueSetElement> getValidValueSetByName(String name,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return eventBrokerClient.getValidValueSetByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of valid value set metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueSetElement> findValidValueSet(String searchString,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return eventBrokerClient.findValidValueSet(userId, searchString, startFrom, pageSize);
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createStructSchemaType(StructSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = eventBrokerClient.createStructSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeProperties);
        }
        else
        {
            schemaTypeGUID =  eventBrokerClient.createStructSchemaType(userId, null, null, schemaTypeProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Create a new metadata element to represent a list of possible schema types that can be used for the attached API parameter.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     * @param schemaTypeOptionGUIDs list of unique identifiers for schema types to link to
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeChoice(SchemaTypeChoiceProperties schemaTypeProperties,
                                         List<String>               schemaTypeOptionGUIDs) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = eventBrokerClient.createSchemaTypeChoice(userId, externalSourceGUID, externalSourceName, schemaTypeProperties,
                                                                      schemaTypeOptionGUIDs);
        }
        else
        {
            schemaTypeGUID = eventBrokerClient.createSchemaTypeChoice(userId, null, null, schemaTypeProperties, schemaTypeOptionGUIDs);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param schemaTypeProperties properties about the schema type to store
     * @param mapFromSchemaTypeGUID unique identifier of the domain of the map
     * @param mapToSchemaTypeGUID unique identifier of the range of the map
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createMapSchemaType(MapSchemaTypeProperties schemaTypeProperties,
                                      String                  mapFromSchemaTypeGUID,
                                      String                  mapToSchemaTypeGUID) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = eventBrokerClient.createMapSchemaType(userId,
                                                                   externalSourceGUID,
                                                                   externalSourceName,
                                                                   schemaTypeProperties,
                                                                   mapFromSchemaTypeGUID,
                                                                   mapToSchemaTypeGUID);
        }
        else
        {
            schemaTypeGUID = eventBrokerClient.createMapSchemaType(userId,
                                                                   null,
                                                                   null,
                                                                   schemaTypeProperties,
                                                                   mapFromSchemaTypeGUID,
                                                                   mapToSchemaTypeGUID);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(String             templateGUID,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = eventBrokerClient.createSchemaTypeFromTemplate(userId,
                                                                            externalSourceGUID,
                                                                            externalSourceName,
                                                                            templateGUID,
                                                                            templateProperties);
        }
        else
        {
            schemaTypeGUID = eventBrokerClient.createSchemaTypeFromTemplate(userId,
                                                                            null,
                                                                            null,
                                                                            templateGUID,
                                                                            templateProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
    }


    /**
     * Update the metadata element representing a schema type.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaTypeProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaType(String               schemaTypeGUID,
                                 boolean              isMergeUpdate,
                                 SchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        eventBrokerClient.updateSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeGUID, isMergeUpdate, schemaTypeProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(schemaTypeGUID);
        }
    }


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaType(String schemaTypeGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        eventBrokerClient.removeSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(schemaTypeGUID);
        }
    }


    /**
     * Create a relationship between two schema elements.  The name of the desired relationship, and any properties (including effectivity dates)
     * are passed on the API.
     *
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to create
     * @param properties relationship properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaElementRelationship(String                 endOneGUID,
                                               String                 endTwoGUID,
                                               String                 relationshipTypeName,
                                               RelationshipProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        eventBrokerClient.setupSchemaElementRelationship(userId,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         endOneGUID,
                                                         endTwoGUID,
                                                         relationshipTypeName,
                                                         properties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(endOneGUID);
        }
    }


    /**
     * Remove a relationship between two schema elements.  The name of the desired relationship is passed on the API.
     *
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to delete
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaElementRelationship(String endOneGUID,
                                               String endTwoGUID,
                                               String relationshipTypeName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        eventBrokerClient.clearSchemaElementRelationship(userId, externalSourceGUID, externalSourceName, endOneGUID, endTwoGUID, relationshipTypeName);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement> findSchemaType(String searchString,
                                                  String typeName,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return eventBrokerClient.findSchemaType(userId, searchString, typeName, startFrom, pageSize);
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is connected to
     *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeForElement(String parentElementGUID,
                                                     String parentElementTypeName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return eventBrokerClient.getSchemaTypeForElement(userId, parentElementGUID, parentElementTypeName);
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement>   getSchemaTypeByName(String name,
                                                         String typeName,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return eventBrokerClient.getSchemaTypeByName(userId, name, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeByGUID(String schemaTypeGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return eventBrokerClient.getSchemaTypeByGUID(userId, schemaTypeGUID);
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return header for parent element (data asset, process, port)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeader getSchemaTypeParent(String schemaTypeGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return eventBrokerClient.getSchemaTypeParent(userId, schemaTypeGUID);
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.  These are called
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is nested underneath
     * @param schemaAttributeProperties properties for the schema attribute
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttribute(String                    schemaElementGUID,
                                        SchemaAttributeProperties schemaAttributeProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        String schemaAttributeGUID;

        if (externalSourceIsHome)
        {
            schemaAttributeGUID = eventBrokerClient.createSchemaAttribute(userId,
                                                                          externalSourceGUID,
                                                                          externalSourceName,
                                                                          schemaElementGUID,
                                                                          schemaAttributeProperties);
        }
        else
        {
            schemaAttributeGUID = eventBrokerClient.createSchemaAttribute(userId,
                                                                          null,
                                                                          null,
                                                                          schemaElementGUID,
                                                                          schemaAttributeProperties);
        }

        if ((schemaAttributeGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(schemaElementGUID);
        }

        return schemaAttributeGUID;
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttributeFromTemplate(String             schemaElementGUID,
                                                    String             templateGUID,
                                                    TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        String schemaAttributeGUID;

        if (externalSourceIsHome)
        {
            schemaAttributeGUID = eventBrokerClient.createSchemaAttributeFromTemplate(userId,
                                                                                      externalSourceGUID,
                                                                                      externalSourceName,
                                                                                      schemaElementGUID,
                                                                                      templateGUID,
                                                                                      templateProperties);
        }
        else
        {
            schemaAttributeGUID = eventBrokerClient.createSchemaAttributeFromTemplate(userId,
                                                                                      null,
                                                                                      null,
                                                                                      schemaElementGUID,
                                                                                      templateGUID,
                                                                                      templateProperties);
        }

        if ((schemaAttributeGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(schemaElementGUID);
        }

        return schemaAttributeGUID;
    }


    /**
     * Connect a schema type to a schema attribute.
     *
     * @param relationshipTypeName name of relationship to create
     * @param schemaAttributeGUID unique identifier of the schema attribute
     * @param schemaTypeGUID unique identifier of the schema type to connect
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaType(String relationshipTypeName,
                                String schemaAttributeGUID,
                                String schemaTypeGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        if (externalSourceIsHome)
        {
            eventBrokerClient.setupSchemaType(userId, externalSourceGUID, externalSourceName, relationshipTypeName, schemaAttributeGUID, schemaTypeGUID);
        }
        else
        {
            eventBrokerClient.setupSchemaType(userId, null, null, relationshipTypeName, schemaAttributeGUID, schemaTypeGUID);
        }
    }


    /**
     * Remove the linked schema types from a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaTypes(String schemaAttributeGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        eventBrokerClient.clearSchemaTypes(userId, externalSourceGUID, externalSourceName, schemaAttributeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(schemaAttributeGUID);
        }
    }


    /**
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaAttributeProperties new properties for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaAttribute(String                    schemaAttributeGUID,
                                      boolean                   isMergeUpdate,
                                      SchemaAttributeProperties schemaAttributeProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        eventBrokerClient.updateSchemaAttribute(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                schemaAttributeGUID,
                                                isMergeUpdate,
                                                schemaAttributeProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(schemaAttributeGUID);
        }
    }


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaAttribute(String schemaAttributeGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        eventBrokerClient.removeSchemaAttribute(userId, externalSourceGUID, externalSourceName, schemaAttributeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(schemaAttributeGUID);
        }
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement> findSchemaAttributes(String searchString,
                                                             String typeName,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return eventBrokerClient.findSchemaAttributes(userId, searchString, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the list of schema attributes associated with a StructSchemaType or nested underneath a schema attribute.
     *
     * @param parentSchemaElementGUID unique identifier of the schemaType of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement> getNestedAttributes(String parentSchemaElementGUID,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return eventBrokerClient.getNestedAttributes(userId, parentSchemaElementGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement> getSchemaAttributesByName(String name,
                                                                  String typeName,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return eventBrokerClient.getSchemaAttributesByName(userId, name, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeElement getSchemaAttributeByGUID(String schemaAttributeGUID) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return eventBrokerClient.getSchemaAttributeByGUID(userId, schemaAttributeGUID);
    }


    /* =====================================================================================================================
     * A Connection is the top level object for working with connectors
     */

    /**
     * Create a new metadata element to represent a connection.
     *
     * @param connectionProperties properties about the connection to store
     *
     * @return unique identifier of the new connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnection(ConnectionProperties connectionProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        String connectionGUID = connectionManagerClient.createConnection(userId, null, null, connectionProperties);

        if ((connectionGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(connectionGUID);
        }

        return connectionGUID;
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectionFromTemplate(String             templateGUID,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        String connectionGUID = connectionManagerClient.createConnectionFromTemplate(userId,
                                                                                     null,
                                                                                     null,
                                                                                     templateGUID,
                                                                                     templateProperties);

        if ((connectionGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(connectionGUID);
        }

        return connectionGUID;
    }


    /**
     * Update the metadata element representing a connection.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param connectionGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectionProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateConnection(String               connectionGUID,
                                 boolean              isMergeUpdate,
                                 ConnectionProperties connectionProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        connectionManagerClient.updateConnection(userId, externalSourceGUID, externalSourceName, connectionGUID, isMergeUpdate, connectionProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupConnectorType(String  connectionGUID,
                                   String  connectorTypeGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        connectionManagerClient.setupConnectorType(userId, null, null, connectionGUID, connectorTypeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Remove a relationship between a connection and a connector type.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearConnectorType(String connectionGUID,
                                   String connectorTypeGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        connectionManagerClient.clearConnectorType(userId, externalSourceGUID, externalSourceName, connectionGUID, connectorTypeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupEndpoint(String  connectionGUID,
                              String  endpointGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        connectionManagerClient.setupEndpoint(userId, null, null, connectionGUID, endpointGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearEndpoint(String connectionGUID,
                              String endpointGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        connectionManagerClient.clearEndpoint(userId, externalSourceGUID, externalSourceName, connectionGUID, endpointGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Create a relationship between a virtual connection and an embedded connection.
     *
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param position which order should this connection be processed
     * @param arguments What additional properties should be passed to the embedded connector via the configuration properties
     * @param displayName what does this connector signify?
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupEmbeddedConnection(String              connectionGUID,
                                        int                 position,
                                        String              displayName,
                                        Map<String, Object> arguments,
                                        String              embeddedConnectionGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        connectionManagerClient.setupEmbeddedConnection(userId, null, null, connectionGUID, position, displayName, arguments, embeddedConnectionGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(embeddedConnectionGUID);
        }
    }


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearEmbeddedConnection(String connectionGUID,
                                        String embeddedConnectionGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        connectionManagerClient.clearEmbeddedConnection(userId, externalSourceGUID, externalSourceName, connectionGUID, embeddedConnectionGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(embeddedConnectionGUID);
        }
    }


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the  connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAssetConnection(String  assetGUID,
                                     String  connectionGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        connectionManagerClient.setupAssetConnection(userId, null, null, assetGUID, connectionGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Remove a relationship between an asset and its connection.
     *
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAssetConnection(String assetGUID,
                                     String connectionGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        connectionManagerClient.clearAssetConnection(userId, externalSourceGUID, externalSourceName, assetGUID, connectionGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Remove the metadata element representing a connection.
     *
     * @param connectionGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnection(String connectionGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        connectionManagerClient.removeConnection(userId, externalSourceGUID, externalSourceName, connectionGUID);
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> findConnections(String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return connectionManagerClient.findConnections(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name used to retrieve the connection
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> getConnectionsByName(String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return connectionManagerClient.getConnectionsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param connectionGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionElement getConnectionByGUID(String connectionGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return connectionManagerClient.getConnectionByGUID(userId, connectionGUID);
    }


    /**
     * Create a new metadata element to represent an endpoint
     *
     * @param endpointProperties properties about the endpoint to store
     *
     * @return unique identifier of the new endpoint
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpoint(EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        String endpointGUID = connectionManagerClient.createEndpoint(userId, null, null, endpointProperties);

        if ((endpointGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(endpointGUID);
        }

        return endpointGUID;
    }


    /**
     * Create a new metadata element to represent a endpoint using an existing metadata element as a template.
     *
     * @param networkAddress location of the endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties descriptive properties that override the template
     *
     * @return unique identifier of the new endpoint
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpointFromTemplate(String             networkAddress,
                                             String             templateGUID,
                                             TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String endpointGUID = connectionManagerClient.createEndpointFromTemplate(userId,
                                                                                 externalSourceGUID,
                                                                                 externalSourceName,
                                                                                 networkAddress,
                                                                                 templateGUID,
                                                                                 templateProperties);

        if ((endpointGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(endpointGUID);
        }

        return endpointGUID;
    }


    /**
     * Update the metadata element representing an endpoint.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param endpointGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateEndpoint(boolean            isMergeUpdate,
                               String             endpointGUID,
                               EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        connectionManagerClient.updateEndpoint(userId, externalSourceGUID, externalSourceName, isMergeUpdate, endpointGUID, endpointProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(endpointGUID);
        }
    }


    /**
     * Remove the metadata element representing a endpoint.
     *
     * @param endpointGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeEndpoint(String endpointGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        connectionManagerClient.removeEndpoint(userId, externalSourceGUID, externalSourceName, endpointGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(endpointGUID);
        }
    }


    /**
     * Retrieve the list of endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> findEndpoints(String searchString,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return connectionManagerClient.findEndpoints(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> getEndpointsByName(String name,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return connectionManagerClient.getEndpointsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the endpoint metadata element with the supplied unique identifier.
     *
     * @param endpointGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointElement getEndpointByGUID(String endpointGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return connectionManagerClient.getEndpointByGUID(userId, endpointGUID);
    }


    /**
     * Retrieve the list of connector type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> findConnectorTypes(String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return connectionManagerClient.findConnectorTypes(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of connector type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> getConnectorTypesByName(String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return connectionManagerClient.getConnectorTypesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the connector type metadata element with the supplied unique identifier.
     *
     * @param connectorTypeGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypeElement getConnectorTypeByGUID(String connectorTypeGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return connectionManagerClient.getConnectorTypeByGUID(userId, connectorTypeGUID);
    }


    /* =====================================================================================================================
     * A ValidValue is the top level object for working with valid values
     */

    /**
     * Create a new metadata element to represent a valid value.
     *
     * @param validValueProperties properties about the valid value to store
     *
     * @return unique identifier of the new valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createValidValue(ValidValueProperties validValueProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        String validValueGUID = validValueManagement.createValidValue(userId, externalSourceGUID, externalSourceName, validValueProperties);

        if ((validValueGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(validValueGUID);
        }

        return validValueGUID;
    }


    /**
     * Update the metadata element representing a valid value.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param validValueGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param validValueProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateValidValue(String               validValueGUID,
                                 boolean              isMergeUpdate,
                                 ValidValueProperties validValueProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        validValueManagement.updateValidValue(userId, externalSourceGUID, externalSourceName, validValueGUID, isMergeUpdate, validValueProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(validValueGUID);
        }
    }


    /**
     * Create a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param validValueSetGUID unique identifier of the valid value set
     * @param properties describes the properties of the membership
     * @param validValueMemberGUID unique identifier of the member
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupValidValueMember(String                         validValueSetGUID,
                                      ValidValueMembershipProperties properties,
                                      String                         validValueMemberGUID) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        validValueManagement.setupValidValueMember(userId, externalSourceGUID, externalSourceName, validValueSetGUID, properties, validValueMemberGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(validValueSetGUID);
        }
    }


    /**
     * Remove a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param validValueSetGUID unique identifier of the valid value set
     * @param validValueMemberGUID unique identifier of the member
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearValidValueMember(String validValueSetGUID,
                                      String validValueMemberGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        validValueManagement.clearValidValueMember(userId, externalSourceGUID, externalSourceName, validValueSetGUID, validValueMemberGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(validValueSetGUID);
        }
    }


    /**
     * Create a valid value assignment relationship between an element and a valid value (typically, a valid value set) to show that
     * the valid value defines the values that can be stored in the data item that the element represents.
     *
     * @param elementGUID unique identifier of the element
     * @param properties describes the permissions that the role has in the validValue
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupValidValues(String                         elementGUID,
                                 ValidValueAssignmentProperties properties,
                                 String                         validValueGUID) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        validValueManagement.setupValidValues(userId, externalSourceGUID, externalSourceName, elementGUID, properties, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove a valid value assignment relationship between an element and a valid value.
     *
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearValidValues(String elementGUID,
                                 String validValueGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        validValueManagement.clearValidValues(userId, externalSourceGUID, externalSourceName, elementGUID, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Create a reference value assignment relationship between an element and a valid value to show that
     * the valid value is a semiformal tag/classification.
     *
     * @param elementGUID unique identifier of the element
     * @param properties describes the quality of the assignment
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupReferenceValueTag(String                             elementGUID,
                                       ReferenceValueAssignmentProperties properties,
                                       String                             validValueGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        validValueManagement.setupReferenceValueTag(userId, externalSourceGUID, externalSourceName, elementGUID, properties, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove a reference value assignment relationship between an element and a valid value.
     *
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearReferenceValueTag(String elementGUID,
                                       String validValueGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        validValueManagement.clearReferenceValueTag(userId, externalSourceGUID, externalSourceName, elementGUID, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove the metadata element representing a valid value.
     *
     * @param validValueGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeValidValue(String validValueGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        validValueManagement.removeValidValue(userId, externalSourceGUID, externalSourceName, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(validValueGUID);
        }
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueElement> findValidValues(String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return validValueManagement.findValidValues(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueElement> getValidValuesByName(String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return validValueManagement.getValidValuesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of valid values.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ValidValueElement> getAllValidValues(int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return validValueManagement.getAllValidValues(userId, startFrom, pageSize);
    }


    /**
     * Page through the members of a valid value set.
     *
     * @param validValueSetGUID          unique identifier of the valid value set
     * @param startFrom                  paging starting point
     * @param pageSize                   maximum number of return values.
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<ValidValueElement> getValidValueSetMembers(String  validValueSetGUID,
                                                           int     startFrom,
                                                           int     pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return validValueManagement.getValidValueSetMembers(userId, validValueSetGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param validValueGUID          unique identifier of valid value to query
     * @param startFrom               paging starting point
     * @param pageSize                maximum number of return values.
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<ValidValueElement> getSetsForValidValue(String  validValueGUID,
                                                        int     startFrom,
                                                        int     pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return validValueManagement.getSetsForValidValue(userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Return information about the valid value set linked to an element as its set of valid values.
     *
     * @param elementGUID unique identifier for the element using the valid value set
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public ValidValueElement getValidValuesForConsumer(String elementGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return validValueManagement.getValidValuesForConsumer(userId, elementGUID);
    }


    /**
     * Return information about the consumers linked to a validValue.
     *
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<RelatedElementStub> getConsumersOfValidValue(String validValueGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return validValueManagement.getConsumersOfValidValue(userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Return information about the valid values linked as reference value tags to an element.
     *
     * @param elementGUID unique identifier for the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of valid values
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<ValidValueElement> getReferenceValues(String elementGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return validValueManagement.getReferenceValues(userId, elementGUID, startFrom, pageSize);
    }


    /**
     * Return information about the person roles linked to a validValue.
     *
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<RelatedElementStub> getAssigneesOfReferenceValue(String validValueGUID,
                                                                 int    startFrom,
                                                                 int    pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return validValueManagement.getAssigneesOfReferenceValue(userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param validValueGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ValidValueElement getValidValueByGUID(String validValueGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return validValueManagement.getValidValueByGUID(userId, validValueGUID);
    }
}
