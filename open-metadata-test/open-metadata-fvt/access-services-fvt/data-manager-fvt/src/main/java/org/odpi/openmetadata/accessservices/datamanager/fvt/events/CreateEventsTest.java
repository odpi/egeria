/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.fvt.events;

import org.odpi.openmetadata.accessservices.datamanager.client.EventBrokerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.MetadataSourceClient;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.TopicElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.EventTypeElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.EventBrokerProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.SchemaAttributeProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.TopicProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.EventTypeProperties;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.List;

/**
 * CreateTopicTest calls the EventBrokerClient to create a topic with schemas tables and nested attributes
 * and then retrieve the results.
 */
public class CreateEventsTest
{
    private final static String testCaseName       = "CreateEventsTest";

    private final static int    maxPageSize        = 100;

    /*
     * The event broker name is constant - the guid is created as part of the test.
     */
    private final static String eventBrokerName            = "TestEventBroker";
    private final static String eventBrokerDisplayName     = "eventBroker displayName";
    private final static String eventBrokerDescription     = "eventBroker description";
    private final static String eventBrokerTypeDescription = "eventBroker type";
    private final static String eventBrokerVersion         = "eventBroker version";

    private final static String topicName        = "TestTopic";
    private final static String topicDisplayName = "Topic displayName";
    private final static String topicDescription = "Topic description";
    private final static String topicType        = "Topic type";
    private final static String topicVersion     = "Topic version";

    private final static String eventTypeName        = "TestEventType";
    private final static String eventTypeDisplayName = "EventType displayName";
    private final static String eventTypeDescription = "EventType description";

    private final static String schemaAttributeName        = "TestSchemaAttribute";
    private final static String schemaAttributeDisplayName = "SchemaAttribute displayName";
    private final static String schemaAttributeDescription = "SchemaAttribute description";


    private final static String nestedSchemaAttributeName        = "TestNestedSchemaAttribute";
    private final static String nestedSchemaAttributeDisplayName = "Nested SchemaAttribute displayName";
    private final static String nestedSchemaAttributeDescription = "Nested SchemaAttribute description";
    private final static String nestedSchemaAttributeType = "string";


    /**
     * Run all of the defined tests and capture the results.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @return  results of running test
     */
    public static FVTResults performFVT(String   serverName,
                                        String   serverPlatformRootURL,
                                        String   userId)
    {
        FVTResults results = new FVTResults(testCaseName);

        results.incrementNumberOfTests();
        try
        {
            CreateEventsTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
            results.incrementNumberOfSuccesses();
        }
        catch (Exception error)
        {
            results.addCapturedError(error);
        }

        return results;
    }


    /**
     * Run all of the tests in this class.
     *
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLogDestination logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private static void runIt(String                 serverPlatformRootURL,
                              String                 serverName,
                              String                 userId,
                              FVTAuditLogDestination auditLogDestination) throws FVTUnexpectedCondition
    {
        CreateEventsTest thisTest = new CreateEventsTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.DATA_MANAGER_OMAS.getAccessServiceWiki());

        EventBrokerClient client = thisTest.getEventBrokerClient(serverName, serverPlatformRootURL, auditLog);

        String eventBrokerGUID = thisTest.getEventBroker(serverName, serverPlatformRootURL, userId, auditLog);
        String topicGUID = thisTest.getTopic(client, eventBrokerGUID, userId);
        String eventTypeGUID = thisTest.getEventType(client, eventBrokerGUID, topicGUID, userId);
        String schemaAttributeGUID = thisTest.createSchemaAttribute(client, eventBrokerGUID, eventTypeGUID, userId);
        String nestedSchemaAttributeGUID = thisTest.createNestedSchemaAttribute(client, eventBrokerGUID, schemaAttributeGUID, userId);


        /*
         * Check that all elements are deleted when the topic is deleted.
         */
        String activityName = "cascadedDelete";
        try
        {
            client.removeTopic(userId, eventBrokerGUID, eventBrokerName, topicGUID, topicName);

            thisTest.checkTopicGone(client, topicGUID, activityName, userId);
            thisTest.checkEventTypeGone(client, eventTypeGUID, null, activityName, userId);
            thisTest.checkSchemaAttributeGone(client, schemaAttributeGUID, null, activityName, userId);
            thisTest.checkNestedSchemaAttributeGone(client, null, nestedSchemaAttributeGUID, activityName, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }


        /*
         * Recreate topic
         */
        activityName= "deleteOneByOne";

        topicGUID = thisTest.getTopic(client, eventBrokerGUID, userId);
        System.out.println("Topic GUID = " + topicGUID);
        eventTypeGUID = thisTest.getEventType(client, eventBrokerGUID, topicGUID, userId);
        System.out.println("EventType GUID = " + eventTypeGUID);
        schemaAttributeGUID = thisTest.createSchemaAttribute(client, eventBrokerGUID, eventTypeGUID, userId);
        System.out.println("SchemaAttribute GUID = " + schemaAttributeGUID);
        nestedSchemaAttributeGUID = thisTest.createNestedSchemaAttribute(client, eventBrokerGUID, schemaAttributeGUID, userId);
        System.out.println("NestedSchemaAttribute GUID = " + nestedSchemaAttributeGUID);


        /*
         * Check that elements can be deleted one by one
         */

        try
        {
            activityName = "deleteOneByOne - prevalidate";
            thisTest.checkSchemaAttributeOK(client, schemaAttributeGUID, eventTypeGUID, activityName, userId);
            thisTest.checkEventTypeOK(client, eventTypeGUID, topicGUID, activityName, userId);
            thisTest.checkTopicOK(client, topicGUID, activityName, userId);

            client.removeSchemaAttribute(userId, eventBrokerGUID, eventBrokerName, nestedSchemaAttributeGUID);

            activityName = "deleteOneByOne - nested attribute gone";
            thisTest.checkNestedSchemaAttributeGone(client, schemaAttributeGUID, nestedSchemaAttributeGUID, activityName, userId);
            thisTest.checkSchemaAttributeOK(client, schemaAttributeGUID, eventTypeGUID, activityName, userId);
            thisTest.checkEventTypeOK(client, eventTypeGUID, topicGUID, activityName, userId);
            thisTest.checkTopicOK(client, topicGUID, activityName, userId);

            client.removeSchemaAttribute(userId, eventBrokerGUID, eventBrokerName, schemaAttributeGUID);

            activityName = "deleteOneByOne - top-level schema gone";
            thisTest.checkNestedSchemaAttributeGone(client, null, nestedSchemaAttributeGUID, activityName, userId);
            thisTest.checkSchemaAttributeGone(client, schemaAttributeGUID, eventTypeGUID, activityName, userId);
            thisTest.checkEventTypeOK(client, eventTypeGUID, topicGUID, activityName, userId);
            thisTest.checkTopicOK(client, topicGUID, activityName, userId);

            client.removeEventType(userId, eventBrokerGUID, eventBrokerName, eventTypeGUID, eventTypeName);

            activityName = "deleteOneByOne - event Type gone";
            thisTest.checkNestedSchemaAttributeGone(client, null, nestedSchemaAttributeGUID, activityName, userId);
            thisTest.checkSchemaAttributeGone(client, schemaAttributeGUID, null, activityName, userId);
            thisTest.checkEventTypeGone(client, eventTypeGUID, topicGUID, activityName, userId);
            thisTest.checkTopicOK(client, topicGUID, activityName, userId);

            client.removeTopic(userId, eventBrokerGUID, eventBrokerName, topicGUID, topicName);

            activityName = "deleteOneByOne - topic gone";
            thisTest.checkNestedSchemaAttributeGone(client, null, nestedSchemaAttributeGUID, activityName, userId);
            thisTest.checkSchemaAttributeGone(client, schemaAttributeGUID, null, activityName, userId);
            thisTest.checkEventTypeGone(client, eventTypeGUID, null, activityName, userId);
            thisTest.checkTopicGone(client, topicGUID, activityName, userId);

        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }

    }


    /**
     * Create and return a event broker client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private EventBrokerClient getEventBrokerClient(String   serverName,
                                                           String   serverPlatformRootURL,
                                                           AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getEventBrokerClient";

        try
        {
            DataManagerRESTClient restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL);

            return new EventBrokerClient(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a event broker entity and return its GUID.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @param auditLog logging destination
     * @return unique identifier of the event broker entity
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getEventBroker(String   serverName,
                                  String   serverPlatformRootURL,
                                  String   userId,
                                  AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getEventBroker";

        try
        {
            DataManagerRESTClient restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL);
            MetadataSourceClient  client     = new MetadataSourceClient(serverName, serverPlatformRootURL, restClient, maxPageSize);

            EventBrokerProperties properties = new EventBrokerProperties();
            properties.setQualifiedName(eventBrokerName);
            properties.setDisplayName(eventBrokerDisplayName);
            properties.setDescription(eventBrokerDescription);
            properties.setTypeDescription(eventBrokerTypeDescription);
            properties.setVersion(eventBrokerVersion);

            String eventBrokerGUID = client.createEventBroker(userId, null, null, properties);

            if (eventBrokerGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }

            String retrievedeventBrokerGUID = client.getMetadataSourceGUID(userId, eventBrokerName);

            if (retrievedeventBrokerGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Retrieve)");
            }

            if (! retrievedeventBrokerGUID.equals(eventBrokerGUID))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Inconsistent GUIDs)");
            }

            return eventBrokerGUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Check a topic is gone.
     *
     * @param client interface to Data Manager OMAS
     * @param topicGUID unique id of the topic to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkTopicGone(EventBrokerClient client,
                                   String                topicGUID,
                                   String                activityName,
                                   String                userId) throws FVTUnexpectedCondition
    {
        try
        {
            TopicElement retrievedElement = client.getTopicByGUID(userId, topicGUID);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Topic returned from Retrieve)");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Retrieve returned)");
        }
        catch (InvalidParameterException expectedException)
        {
            // all ok
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }



    /**
     * Check topic is ok.
     *
     * @param client interface to Data Manager OMAS
     * @param topicGUID unique id of the topic
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkTopicOK(EventBrokerClient client,
                              String            topicGUID,
                              String            activityName,
                              String            userId) throws FVTUnexpectedCondition
    {
        try
        {
            TopicElement retrievedElement = client.getTopicByGUID(userId, topicGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no TopicElement from Retrieve)");
            }

            TopicProperties retrievedTopic = retrievedElement.getProperties();

            if (retrievedTopic == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no TopicProperties from Retrieve)");
            }

            if (! topicName.equals(retrievedTopic.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! topicDisplayName.equals(retrievedTopic.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! topicDescription.equals(retrievedTopic.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }
            if (! topicType.equals(retrievedTopic.getTopicType()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad topicType from Retrieve " + retrievedTopic.getTopicType() + ")");
            }

            List<TopicElement> topicList = client.getTopicsByName(userId, topicName, 0, maxPageSize);

            if (topicList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Topic for RetrieveByName)");
            }
            else if (topicList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty Topic list for RetrieveByName)");
            }
            else if (topicList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Topic list for RetrieveByName contains" + topicList.size() +
                                                         " elements)");
            }

            retrievedElement = topicList.get(0);
            retrievedTopic = retrievedElement.getProperties();

            if (! topicName.equals(retrievedTopic.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! topicDisplayName.equals(retrievedTopic.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! topicDescription.equals(retrievedTopic.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }
            if (! topicType.equals(retrievedTopic.getTopicType()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad topicType from RetrieveByName)");
            }

            topicList = client.getTopicsByName(userId, topicName, 1, maxPageSize);

            if (topicList != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Topic for RetrieveByName)");
            }
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a topic and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param eventBrokerGUID unique id of the event broker
     * @param userId calling user
     * @return GUID of topic
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getTopic(EventBrokerClient client,
                            String            eventBrokerGUID,
                            String             userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getTopic";

        try
        {
            TopicProperties properties = new TopicProperties();

            properties.setQualifiedName(topicName);
            properties.setDisplayName(topicDisplayName);
            properties.setDescription(topicDescription);
            properties.setTopicType(topicType);

            String topicGUID = client.createTopic(userId, eventBrokerGUID, eventBrokerName, true, properties);

            if (topicGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }
            else
            {
                checkTopicOK(client, topicGUID, activityName, userId);
            }

            return topicGUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Check a topic nested attribute is gone.
     *
     * @param client interface to Data Manager OMAS
     * @param eventTypeGUID unique id of the topic schema to test
     * @param topicGUID unique id of the topic to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkEventTypeGone(EventBrokerClient client,
                                         String                eventTypeGUID,
                                         String                topicGUID,
                                         String                activityName,
                                         String                userId) throws FVTUnexpectedCondition
    {
        try
        {
            EventTypeElement retrievedElement = client.getEventTypeByGUID(userId, eventTypeGUID);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(EventType returned from Retrieve)");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(getEventTypeByGUID returned");
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (InvalidParameterException expectedError)
        {
            // all ok
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }

        if (topicGUID != null)
        {
            try
            {
                /*
                 * Only one schema created so nothing should be tied to the topic.
                 */
                List<EventTypeElement> eventTypeList = client.getEventTypesForTopic(userId, topicGUID, 0, maxPageSize);

                if (eventTypeList != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(EventType returned for getEventTypesForTopic)");
                }
            }
            catch(FVTUnexpectedCondition testCaseError)
            {
                throw testCaseError;
            }
            catch(Exception unexpectedError)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
            }
        }
    }


    /**
     * Check a topic schema is correctly stored.
     *
     * @param client interface to Data Manager OMAS
     * @param eventTypeGUID unique id of the topic schema
     * @param topicGUID unique id of the topic
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void   checkEventTypeOK(EventBrokerClient client,
                                    String            eventTypeGUID,
                                    String            topicGUID,
                                    String            activityName,
                                    String            userId) throws FVTUnexpectedCondition
    {
        try
        {
            EventTypeElement    retrievedElement  = client.getEventTypeByGUID(userId, eventTypeGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no EventTypeElement from Retrieve)");
            }

            EventTypeProperties retrievedSchema = retrievedElement.getProperties();

            if (retrievedSchema == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no EventTypeProperties from Retrieve)");
            }

            if (! eventTypeName.equals(retrievedSchema.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve: " + retrievedSchema.getQualifiedName() + ")");
            }
            if (! eventTypeDisplayName.equals(retrievedSchema.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve: " + retrievedSchema.getDisplayName() + ")");
            }
            if (! eventTypeDescription.equals(retrievedSchema.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve: " + retrievedSchema.getDescription() + ")");
            }


            List<EventTypeElement> eventTypeList = client.getEventTypesByName(userId, eventTypeName, 0, maxPageSize);

            if (eventTypeList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no EventType for RetrieveByName)");
            }
            else if (eventTypeList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty EventType list for RetrieveByName)");
            }
            else if (eventTypeList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(EventType list for RetrieveByName contains" + eventTypeList.size() +
                                                         " elements)");
            }

            retrievedElement = eventTypeList.get(0);
            retrievedSchema = retrievedElement.getProperties();

            if (! eventTypeName.equals(retrievedSchema.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! eventTypeDisplayName.equals(retrievedSchema.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! eventTypeDescription.equals(retrievedSchema.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }

            eventTypeList = client.getEventTypesForTopic(userId, topicGUID, 0, maxPageSize);

            if (eventTypeList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no EventType for getSchemasForTopic)");
            }
            else if (eventTypeList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty EventType list for getSchemasForTopic)");
            }
            else if (eventTypeList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(EventType list for getSchemasForTopic contains" + eventTypeList.size() +
                                                         " elements)");
            }
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a topic schema and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param eventBrokerGUID unique id of the event broker
     * @param topicGUID unique id of the topic
     * @param userId calling user
     * @return GUID of topic
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getEventType(EventBrokerClient client,
                                String            eventBrokerGUID,
                                String            topicGUID,
                                String            userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getEventType";

        try
        {
            EventTypeProperties properties = new EventTypeProperties();

            properties.setQualifiedName(eventTypeName);
            properties.setDisplayName(eventTypeDisplayName);
            properties.setDescription(eventTypeDescription);

            String eventTypeGUID = client.createEventType(userId, eventBrokerGUID, eventBrokerName, topicGUID, properties);

            if (eventTypeGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for schemaCreate)");
            }
            else
            {
                checkEventTypeOK(client, eventTypeGUID, topicGUID, activityName, userId);
            }

            return eventTypeGUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Check a top-level scheam attribute is gone.
     *
     * @param client interface to Data Manager OMAS
     * @param schemaAttributeGUID unique id of the top-level attribute to test
     * @param eventTypeGUID unique id of the topic schema to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkSchemaAttributeGone(EventBrokerClient client,
                                          String            schemaAttributeGUID,
                                          String            eventTypeGUID,
                                          String            activityName,
                                          String            userId) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaAttributeElement retrievedElement = client.getSchemaAttributeByGUID(userId, schemaAttributeGUID);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(SchemaAttribute returned from Retrieve)");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(getSchemaAttributeByGUID returned");
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (InvalidParameterException expectedError)
        {
            // all ok
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }

        if (eventTypeGUID != null)
        {
            try
            {
                /*
                 * Only one table created so nothing should be tied to the schema.
                 */
                List<SchemaAttributeElement> schemaAttributeList = client.getNestedAttributes(userId, eventTypeGUID, 0, maxPageSize);

                if (schemaAttributeList != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + "(SchemaAttribute returned for getNestedAttributes)");
                }
            }
            catch(FVTUnexpectedCondition testCaseError)
            {
                throw testCaseError;
            }
            catch(Exception unexpectedError)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
            }
        }
    }


    /**
     * Check a top-level attribute is stored OK.
     *
     * @param client interface to Data Manager OMAS
     * @param schemaAttributeGUID unique id of the eventType
     * @param eventTypeGUID unique id of the eventType
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkSchemaAttributeOK(EventBrokerClient client,
                                        String            schemaAttributeGUID,
                                        String            eventTypeGUID,
                                        String            activityName,
                                        String            userId) throws FVTUnexpectedCondition
    {

        try
        {
            SchemaAttributeElement retrievedElement = client.getSchemaAttributeByGUID(userId, schemaAttributeGUID);

            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no SchemaAttributeElement from Retrieve)");
            }

            SchemaAttributeProperties retrievedTable  = retrievedElement.getProperties();

            if (retrievedTable == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no SchemaAttributeProperties from Retrieve)");
            }

            if (! schemaAttributeName.equals(retrievedTable.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! schemaAttributeDisplayName.equals(retrievedTable.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! schemaAttributeDescription.equals(retrievedTable.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }

            List<SchemaAttributeElement> schemaAttributeList = client.getSchemaAttributesByName(userId, schemaAttributeName, "EventSchemaAttribute",0, maxPageSize);

            if (schemaAttributeList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no SchemaAttribute for RetrieveByName)");
            }
            else if (schemaAttributeList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty SchemaAttribute list for RetrieveByName)");
            }
            else if (schemaAttributeList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(SchemaAttribute list for RetrieveByName contains" + schemaAttributeList.size() +
                                                         " elements)");
            }

            retrievedElement = schemaAttributeList.get(0);
            retrievedTable = retrievedElement.getProperties();

            if (! schemaAttributeName.equals(retrievedTable.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! schemaAttributeDisplayName.equals(retrievedTable.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! schemaAttributeDescription.equals(retrievedTable.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }

            schemaAttributeList = client.getNestedAttributes(userId, eventTypeGUID, 0, maxPageSize);

            if (schemaAttributeList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no SchemaAttribute for getTablesForEventType)");
            }
            else if (schemaAttributeList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty SchemaAttribute list for getTablesForEventType)");
            }
            else if (schemaAttributeList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(SchemaAttribute list for getTablesForEventType contains" + schemaAttributeList.size() +
                                                         " elements)");
            }
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a top-level attribute and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param eventBrokerGUID unique id of the event broker
     * @param eventTypeGUID unique id of the eventType
     * @param userId calling user
     * @return GUID of topic
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createSchemaAttribute(EventBrokerClient client,
                                         String            eventBrokerGUID,
                                         String            eventTypeGUID,
                                         String            userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createSchemaAttribute";

        try
        {
            SchemaAttributeProperties properties = new SchemaAttributeProperties();

            properties.setQualifiedName(schemaAttributeName);
            properties.setDisplayName(schemaAttributeDisplayName);
            properties.setDescription(schemaAttributeDescription);


            String schemaAttributeGUID = client.createSchemaAttribute(userId, eventBrokerGUID, eventBrokerName, eventTypeGUID, properties);

            if (schemaAttributeGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for tableCreate)");
            }
            else
            {
                checkSchemaAttributeOK(client, schemaAttributeGUID, eventTypeGUID, activityName, userId);
            }

            return schemaAttributeGUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Check a nested schema attribute is gone.
     *
     * @param client interface to Data Manager OMAS
     * @param schemaAttributeGUID unique id of the topic nested attribute to test
     * @param schemaAttributeGUID unique id of the top-level attribute to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkNestedSchemaAttributeGone(EventBrokerClient client,
                                                String            schemaAttributeGUID,
                                                String            nestedSchemaAttributeGUID,
                                                String            activityName,
                                                String            userId) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaAttributeElement retrievedElement = client.getSchemaAttributeByGUID(userId, nestedSchemaAttributeGUID);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(SchemaAttribute returned from Retrieve - " + retrievedElement.toString() + ")");
            }

            throw new FVTUnexpectedCondition(testCaseName, activityName + "(getSchemaAttributeByGUID returned");
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (InvalidParameterException expectedError)
        {
            // all ok
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }

        if (schemaAttributeGUID != null)
        {
            try
            {
                /*
                 * Only one nested attribute created so nothing should be tied to the table.
                 */
                List<SchemaAttributeElement> schemaAttributeList = client.getNestedAttributes(userId, schemaAttributeGUID, 0, maxPageSize);

                if (schemaAttributeList != null)
                {
                    throw new FVTUnexpectedCondition(testCaseName, activityName + " (SchemaAttribute returned for getNestedAttributes)");
                }
            }
            catch(FVTUnexpectedCondition testCaseError)
            {
                throw testCaseError;
            }
            catch(Exception unexpectedError)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " - exception from getNestedAttributes(" + schemaAttributeGUID + ")", unexpectedError);
            }
        }
    }


    /**
     * Create a topic nested attribute and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param schemaAttributeGUID unique id of the topic nested attribute to test
     * @param schemaAttributeGUID unique id of the top-level attribute to test
     * @param activityName name of calling activity
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkNestedSchemaAttributeOK(EventBrokerClient client,
                                              String            schemaAttributeGUID,
                                              String            nestedSchemaAttributeGUID,
                                              String            activityName,
                                              String            userId) throws FVTUnexpectedCondition
    {
        try
        {
            SchemaAttributeElement  retrievedElement = client.getSchemaAttributeByGUID(userId, nestedSchemaAttributeGUID);
            
            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no SchemaAttributeElement from Retrieve)");
            }
            
            SchemaAttributeProperties retrievedAttribute  = retrievedElement.getProperties();

            if (retrievedAttribute == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no SchemaAttributeProperties from Retrieve)");
            }

            if (! nestedSchemaAttributeName.equals(retrievedAttribute.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve: " + retrievedAttribute.getQualifiedName() + ")");
            }
            if (! nestedSchemaAttributeDisplayName.equals(retrievedAttribute.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve: " + retrievedAttribute.getDisplayName() + ")");
            }
            if (! nestedSchemaAttributeDescription.equals(retrievedAttribute.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve: " + retrievedAttribute.getDescription() + ")");
            }

            List<SchemaAttributeElement> schemaAttributeList = client.getSchemaAttributesByName(userId, nestedSchemaAttributeName, "EventSchemaAttribute", 0, maxPageSize);

            if (schemaAttributeList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no SchemaAttribute for RetrieveByName)");
            }
            else if (schemaAttributeList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty SchemaAttribute list for RetrieveByName)");
            }
            else if (schemaAttributeList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(SchemaAttribute list for RetrieveByName contains" + schemaAttributeList.size() +
                                                         " elements)");
            }

            retrievedElement = schemaAttributeList.get(0);
            retrievedAttribute = retrievedElement.getProperties();

            if (! nestedSchemaAttributeName.equals(retrievedAttribute.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! nestedSchemaAttributeDisplayName.equals(retrievedAttribute.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! nestedSchemaAttributeDescription.equals(retrievedAttribute.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }

            schemaAttributeList = client.getNestedAttributes(userId, schemaAttributeGUID, 0, maxPageSize);

            if (schemaAttributeList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no SchemaAttribute for getColumnsForSchemaAttribute)");
            }
            else if (schemaAttributeList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty SchemaAttribute list for getColumnsForSchemaAttribute)");
            }
            else if (schemaAttributeList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(SchemaAttribute list for getColumnsForSchemaAttribute contains" + schemaAttributeList.size() +
                                                         " elements)");
            }

            retrievedElement = schemaAttributeList.get(0);
            retrievedAttribute = retrievedElement.getProperties();

            if (! nestedSchemaAttributeName.equals(retrievedAttribute.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from getColumnsForSchemaAttribute)");
            }
            if (! nestedSchemaAttributeDisplayName.equals(retrievedAttribute.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from getColumnsForSchemaAttribute)");
            }
            if (! nestedSchemaAttributeDescription.equals(retrievedAttribute.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from getColumnsForSchemaAttribute)");
            }
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a nested schema attribute and return its GUID.
     *
     * @param client interface to Data Manager OMAS
     * @param eventBrokerGUID unique id of the event broker
     * @param schemaAttributeGUID unique id of the top-level attribute to connect the nested attribute to
     * @param userId calling user
     * @return GUID of nested schema attribute
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createNestedSchemaAttribute(EventBrokerClient client,
                                               String            eventBrokerGUID,
                                               String            schemaAttributeGUID,
                                               String            userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createNestedSchemaAttribute";

        try
        {
            SchemaAttributeProperties  properties = new SchemaAttributeProperties();

            properties.setQualifiedName(nestedSchemaAttributeName);
            properties.setDisplayName(nestedSchemaAttributeDisplayName);
            properties.setDescription(nestedSchemaAttributeDescription);
            properties.setDataType(nestedSchemaAttributeType);

            String nestedSchemaAttributeGUID = client.createSchemaAttribute(userId, eventBrokerGUID, eventBrokerName, schemaAttributeGUID, properties);

            if (nestedSchemaAttributeGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for nested attribute create)");
            }
            else
            {
                checkNestedSchemaAttributeOK(client, schemaAttributeGUID, nestedSchemaAttributeGUID, activityName, userId);
            }

            return nestedSchemaAttributeGUID;
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }
}
