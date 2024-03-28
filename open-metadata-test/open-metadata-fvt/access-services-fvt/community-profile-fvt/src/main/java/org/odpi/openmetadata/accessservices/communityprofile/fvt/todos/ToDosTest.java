/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.fvt.todos;


import org.odpi.openmetadata.accessservices.communityprofile.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.communityprofile.client.ToDoActionManagement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ActionTargetElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ToDoElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.NewActionTargetProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ToDoProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ToDoStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateToDoTest calls the ToDoManagementClient to create a "to do" 
 * and then retrieve the results. 
 */
public class ToDosTest
{
    private final static String testCaseName    = "ToDosTest";
    private final static String toDoName        = "TestToDo";
    private final static String toDoDisplayName = "ToDo displayName";
    private final static String toDoDescription = "ToDo description";
    private final static String toDoType        = "ToDo Type";
    private final static String badToDoType     = "ToDo Type Bad";

    private static final String assetTypeName      = "DataFile";
    private final static String assetQualifiedName = "TestAssetQName";
    private final static String actionTargetName   = "Asset To Work On";

    private static final String originatorTypeName      = "GovernanceActionService";
    private final static String originatorQualifiedName = "GovernanceActionServiceQName";
    
    private static final String sponsorTypeName      = "Project";
    private final static String sponsorQualifiedName = "ProjectQName";

    private static final String userTypeName      = "UserIdentity";
    private static final String userQualifiedName = "TestUser Qualified Name";


    private final PropertyHelper propertyHelper = new PropertyHelper();
    

    /**
     * Run all the defined tests and capture the results.
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
            ToDosTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
            results.incrementNumberOfSuccesses();
        }
        catch (Exception error)
        {
            results.addCapturedError(error);
        }

        return results;
    }


    /**
     * Run all the tests in this class.
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
        ToDosTest thisTest = new ToDosTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceWiki());

        ToDoActionManagement toDoClient = thisTest.getToDoClient(serverName, serverPlatformRootURL, auditLog);
        OpenMetadataClient   openMetadataClient = thisTest.getOpenMetadataClient(serverName, serverPlatformRootURL, 200);

        String assetGUID = thisTest.getAsset(openMetadataClient, userId);
        String originatorGUID = thisTest.getOriginator(openMetadataClient, userId);
        String actorGUID = thisTest.getActor(openMetadataClient, userId);
        String sponsorGUID = thisTest.getSponsor(openMetadataClient, userId);
        String toDoGUID = thisTest.getToDo(toDoClient, userId, assetGUID, sponsorGUID, originatorGUID, actorGUID);

        String activityName = "create";

        thisTest.checkToDoOK(activityName, toDoClient, toDoGUID,0, ToDoStatus.OPEN, assetGUID, sponsorGUID, originatorGUID, actorGUID,  userId);

        activityName = "update";

        try
        {
            ToDoProperties toDoProperties = new ToDoProperties();

            toDoProperties.setPriority(100);
            toDoProperties.setStatus(ToDoStatus.IN_PROGRESS);
            toDoClient.updateToDo(userId, toDoGUID, true, toDoProperties);

            thisTest.checkToDoOK(activityName, toDoClient, toDoGUID, 100, ToDoStatus.IN_PROGRESS, assetGUID, sponsorGUID, originatorGUID, actorGUID, userId);

            activityName = "delete";
            toDoClient.deleteToDo(userId, toDoGUID);

            thisTest.checkToDoGone(toDoClient, toDoGUID, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create and return a client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private ToDoActionManagement getToDoClient(String   serverName,
                                               String   serverPlatformRootURL,
                                               AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getToDoActionManagement";

        try
        {
            return new ToDoActionManagement(serverName, serverPlatformRootURL, auditLog, 100);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create and return a client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param maxPageSize maximum value allowed for page size
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private OpenMetadataClient getOpenMetadataClient(String   serverName,
                                                     String   serverPlatformRootURL,
                                                     int      maxPageSize) throws FVTUnexpectedCondition
    {
        final String activityName = "getOpenMetadataClient";

        try
        {
            return new OpenMetadataStoreClient(serverName, serverPlatformRootURL, maxPageSize);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create an asset and return its GUID.
     *
     * @param client interface to Community Profile OMAS
     * @param userId calling user
     * @return GUID of asset
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getAsset(OpenMetadataClient  client,
                           String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getAsset";
        
        try
        {
            ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            assetQualifiedName);
            
            String assetGUID = client.createMetadataElementInStore(userId,
                                                                   assetTypeName,
                                                                   ElementStatus.ACTIVE,
                                                                   null,
                                                                   null,
                                                                   properties);

            if (assetGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no assetGUID for Create)");
            }

            return assetGUID;
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
     * Create a governance action service and return its GUID.
     *
     * @param client interface to Community Profile OMAS
     * @param userId calling user
     * @return GUID of asset
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getOriginator(OpenMetadataClient  client,
                                 String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getOriginator";

        try
        {
            ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            originatorQualifiedName);

            String originatorGUID = client.createMetadataElementInStore(userId,
                                                                   originatorTypeName,
                                                                   ElementStatus.ACTIVE,
                                                                   null,
                                                                   null,
                                                                   properties);

            if (originatorGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no originatorGUID for Create)");
            }

            return originatorGUID;
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
     * Create an actor and return its GUID.
     *
     * @param client interface to Community Profile OMAS
     * @param userId calling user
     * @return GUID of asset
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getActor(OpenMetadataClient  client,
                            String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getActor";

        try
        {
            ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            userQualifiedName);

            String actorGUID = client.createMetadataElementInStore(userId,
                                                                   userTypeName,
                                                                   ElementStatus.ACTIVE,
                                                                   null,
                                                                   null,
                                                                   properties);

            if (actorGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no actorGUID for Create)");
            }

            return actorGUID;
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
     * Create a sponsor and return its GUID.
     *
     * @param client interface to Community Profile OMAS
     * @param userId calling user
     * @return GUID of asset
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getSponsor(OpenMetadataClient  client,
                              String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "getSponsor";

        try
        {
            ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            sponsorQualifiedName);

            String sponsorGUID = client.createMetadataElementInStore(userId,
                                                                   sponsorTypeName,
                                                                   ElementStatus.ACTIVE,
                                                                   null,
                                                                   null,
                                                                   properties);

            if (sponsorGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no sponsorGUID for Create)");
            }

            return sponsorGUID;
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
     * Create a "to Do" and return its GUID.
     *
     * @param client interface to Community Profile OMAS
     * @param userId calling user
     * @param assetGUID asset action target
     * @param sponsorGUID unique identifier of sponsor
     * @param originatorGUID unique identifier of originator
     * @param actorGUID unique identifier of actor assigned to work
     * @return GUID of to Do
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String getToDo(ToDoActionManagement client,
                           String               userId,
                           String               assetGUID,
                           String               sponsorGUID,
                           String               originatorGUID,
                           String               actorGUID) throws FVTUnexpectedCondition
    {
        final String activityName = "getToDo";

        try
        {
            ToDoProperties properties = new ToDoProperties();

            properties.setQualifiedName(toDoName);
            properties.setName(toDoDisplayName);
            properties.setDescription(toDoDescription);
            properties.setToDoType(toDoType);

            List<NewActionTargetProperties> actionTargetPropertiesList = new ArrayList<>();

            NewActionTargetProperties newActionTargetProperties = new NewActionTargetProperties();
            newActionTargetProperties.setActionTargetGUID(assetGUID);
            newActionTargetProperties.setActionTargetName(actionTargetName);

            actionTargetPropertiesList.add(newActionTargetProperties);


            String toDoGUID = client.createToDo(userId, originatorGUID, sponsorGUID, actorGUID, actionTargetPropertiesList, properties);

            if (toDoGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }

            return toDoGUID;
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
     * Check to Do is ok.
     *
     * @param activityName name of caller
     * @param client interface to Community Profile OMAS
     * @param toDoGUID unique id of the to do
     * @param priority priority of the to do
     * @param toDoStatus expected status of to do
     * @param assetGUID asset action target
     * @param sponsorGUID unique identifier of sponsor
     * @param originatorGUID unique identifier of originator
     * @param actorGUID unique identifier of actor assigned to work
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkToDoOK(String               activityName,
                             ToDoActionManagement client,
                             String               toDoGUID,
                             int                  priority,
                             ToDoStatus           toDoStatus,
                             String               assetGUID,
                             String               sponsorGUID,
                             String               originatorGUID,
                             String               actorGUID,
                             String               userId) throws FVTUnexpectedCondition
    {
        try
        {
            ToDoElement retrievedElement = client.getToDo(userId, toDoGUID);

            System.out.println(activityName + " element: " + retrievedElement);
            checkToDoElementOK(retrievedElement,
                               toDoGUID,
                               priority,
                               toDoStatus,
                               assetGUID,
                               sponsorGUID,
                               originatorGUID,
                               actorGUID);

            List<ToDoElement> toDoElements = client.getAssignedActions(userId, actorGUID, ToDoStatus.ABANDONED, 0, 0);

            if (toDoElements != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Elements for actor retrieved from bad status) =>> " + toDoElements);
            }

            toDoElements = client.getAssignedActions(userId, actorGUID, toDoStatus, 0, 0);

            if (toDoElements == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(No elements for actor retrieved from current status)");
            }
            if (toDoElements.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of elements for actor retrieved from current status) =>> " + toDoElements);
            }

            checkToDoElementOK(toDoElements.get(0),
                               toDoGUID,
                               priority,
                               toDoStatus,
                               assetGUID,
                               sponsorGUID,
                               originatorGUID,
                               actorGUID);

            toDoElements = client.getAssignedActions(userId, actorGUID, null, 0, 0);

            if (toDoElements == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(No elements for actor retrieved from no status)");
            }
            if (toDoElements.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of elements for actor retrieved from no status) =>> " + toDoElements);
            }

            checkToDoElementOK(toDoElements.get(0),
                               toDoGUID,
                               priority,
                               toDoStatus,
                               assetGUID,
                               sponsorGUID,
                               originatorGUID,
                               actorGUID);

            /*--*/

            toDoElements = client.getActionsForSponsor(userId, sponsorGUID, ToDoStatus.ABANDONED, 0, 0);

            if (toDoElements != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Elements for sponsor retrieved from bad status) =>> " + toDoElements);
            }

            toDoElements = client.getActionsForSponsor(userId, sponsorGUID, toDoStatus, 0, 0);

            if (toDoElements == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(No elements for sponsor retrieved from current status)");
            }
            if (toDoElements.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of elements for sponsor retrieved from current status) =>> " + toDoElements);
            }

            checkToDoElementOK(toDoElements.get(0),
                               toDoGUID,
                               priority,
                               toDoStatus,
                               assetGUID,
                               sponsorGUID,
                               originatorGUID,
                               actorGUID);

            toDoElements = client.getActionsForSponsor(userId, sponsorGUID, null, 0, 0);

            if (toDoElements == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(No elements for sponsor retrieved from no status)");
            }
            if (toDoElements.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of elements for sponsor retrieved from no status) =>> " + toDoElements);
            }

            checkToDoElementOK(toDoElements.get(0),
                               toDoGUID,
                               priority,
                               toDoStatus,
                               assetGUID,
                               sponsorGUID,
                               originatorGUID,
                               actorGUID);

            /*--*/

            toDoElements = client.getActionsForActionTarget(userId, assetGUID, ToDoStatus.ABANDONED, 0, 0);

            if (toDoElements != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Elements for action target retrieved from bad status) =>> " + toDoElements);
            }

            toDoElements = client.getActionsForActionTarget(userId, assetGUID, toDoStatus, 0, 0);

            if (toDoElements == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(No elements for action target retrieved from current status)");
            }
            if (toDoElements.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of elements for action target retrieved from current status) =>> " + toDoElements);
            }

            checkToDoElementOK(toDoElements.get(0),
                               toDoGUID,
                               priority,
                               toDoStatus,
                               assetGUID,
                               sponsorGUID,
                               originatorGUID,
                               actorGUID);

            toDoElements = client.getActionsForActionTarget(userId, assetGUID, null, 0, 0);

            if (toDoElements == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(No elements for action target retrieved from no status)");
            }
            if (toDoElements.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of elements for action target retrieved from no status) =>> " + toDoElements);
            }

            checkToDoElementOK(toDoElements.get(0),
                               toDoGUID,
                               priority,
                               toDoStatus,
                               assetGUID,
                               sponsorGUID,
                               originatorGUID,
                               actorGUID);
            /*--*/

            toDoElements = client.getToDosByType(userId, badToDoType, null, 0, 0);

            if (toDoElements != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Elements for bad toDoType retrieved) =>> " + toDoElements);
            }

            toDoElements = client.getToDosByType(userId, toDoType, ToDoStatus.ABANDONED, 0, 0);

            if (toDoElements != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Elements for toDoType retrieved from bad status) =>> " + toDoElements);
            }

            toDoElements = client.getToDosByType(userId, toDoType, toDoStatus, 0, 0);

            if (toDoElements == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(No elements for toDoType retrieved from current status)");
            }
            if (toDoElements.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of elements for toDoType retrieved from current status) =>> " + toDoElements);
            }

            checkToDoElementOK(toDoElements.get(0),
                               toDoGUID,
                               priority,
                               toDoStatus,
                               assetGUID,
                               sponsorGUID,
                               originatorGUID,
                               actorGUID);

            toDoElements = client.getToDosByType(userId, toDoType, null, 0, 0);

            if (toDoElements == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(No elements for toDoType retrieved from no status)");
            }
            if (toDoElements.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of elements for toDoType retrieved from no status) =>> " + toDoElements);
            }

            checkToDoElementOK(toDoElements.get(0),
                               toDoGUID,
                               priority,
                               toDoStatus,
                               assetGUID,
                               sponsorGUID,
                               originatorGUID,
                               actorGUID);

            /*--*/

            toDoElements = client.findToDos(userId, ".*", ToDoStatus.ABANDONED, 0, 0);

            if (toDoElements != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Elements for sponsor retrieved from bad status) =>> " + toDoElements);
            }

            toDoElements = client.findToDos(userId, ".*", toDoStatus, 0, 0);

            if (toDoElements == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(No elements for sponsor retrieved from current status)");
            }
            if (toDoElements.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of elements for sponsor retrieved from current status) =>> " + toDoElements);
            }

            checkToDoElementOK(toDoElements.get(0),
                               toDoGUID,
                               priority,
                               toDoStatus,
                               assetGUID,
                               sponsorGUID,
                               originatorGUID,
                               actorGUID);

            toDoElements = client.findToDos(userId, ".*", null, 0, 0);

            if (toDoElements == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(No elements for sponsor retrieved from no status)");
            }
            if (toDoElements.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Wrong number of elements for sponsor retrieved from no status) =>> " + toDoElements);
            }

            checkToDoElementOK(toDoElements.get(0),
                               toDoGUID,
                               priority,
                               toDoStatus,
                               assetGUID,
                               sponsorGUID,
                               originatorGUID,
                               actorGUID);
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
     * Check to Do is ok.
     *
     * @param retrievedElement retrieved element
     * @param toDoGUID unique id of the to Do
     * @param priority priority of the to do
     * @param toDoStatus status
     * @param assetGUID asset action target
     * @param sponsorGUID unique identifier of sponsor
     * @param originatorGUID unique identifier of originator
     * @param actorGUID unique identifier of actor assigned to work
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkToDoElementOK(ToDoElement          retrievedElement,
                                    String               toDoGUID,
                                    int                  priority,
                                    ToDoStatus           toDoStatus,
                                    String               assetGUID,
                                    String               sponsorGUID,
                                    String               originatorGUID,
                                    String               actorGUID) throws FVTUnexpectedCondition
    {
        String activityName = "checkToDoElementOK";

        try
        {
            if (retrievedElement == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no ToDoElement from Retrieve)");
            }

            if (!toDoGUID.equals(retrievedElement.getElementHeader().getGUID()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad GUID from Retrieve) =>> " + retrievedElement.getElementHeader().getGUID());
            }

            ToDoProperties properties = retrievedElement.getProperties();

            if (properties == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no ToDoProperties from Retrieve)");
            }


            if (!toDoName.equals(properties.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve) =>> " + properties);
            }
            if (!toDoDisplayName.equals(properties.getName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve) =>>" + properties);
            }
            if (!toDoDescription.equals(properties.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve) =>> " + properties);
            }
            if (priority != properties.getPriority())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad priority from Retrieve) =>> " + properties);
            }
            if (toDoStatus != properties.getStatus())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad status from Retrieve) =>> " + properties.getStatus() + " rather than " + toDoStatus + ": " + properties);
            }

            if (retrievedElement.getToDoSource() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Null ToDoSource from Retrieve)");
            }
            if (! originatorGUID.equals(retrievedElement.getToDoSource().getGUID()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad ToDoSource GUID from Retrieve) =>>" + retrievedElement.getToDoSource().getGUID());
            }
            if (! originatorQualifiedName.equals(retrievedElement.getToDoSource().getUniqueName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad ToDoSource Unique name from Retrieve) =>>" + retrievedElement.getToDoSource().getUniqueName());
            }

            if (retrievedElement.getAssignedActors() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Null AssignedActors from Retrieve)");
            }
            if (retrievedElement.getAssignedActors().size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bas Number of AssignedActors from Retrieve) =>> ");
            }

            ElementStub assignedActor = retrievedElement.getAssignedActors().get(0);

            if (assignedActor == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Null AssignedActor from Retrieve)");
            }
            if (! actorGUID.equals(assignedActor.getGUID()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad AssignedActor GUID from Retrieve) =>> " + assignedActor.getGUID());
            }
            if (! userQualifiedName.equals(assignedActor.getUniqueName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad AssignedActor Unique Name from Retrieve) =>> " + assignedActor.getUniqueName());
            }


            if (retrievedElement.getSponsors() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Null Sponsors from Retrieve)");
            }
            if (retrievedElement.getSponsors().size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad number of Sponsors from Retrieve) =>> ");
            }

            ElementStub sponsor = retrievedElement.getSponsors().get(0);

            if (sponsor == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Null Sponsor from Retrieve)");
            }
            if (! sponsorGUID.equals(sponsor.getGUID()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad Sponsor GUID from Retrieve) =>> " + sponsor.getGUID());
            }
            if (! sponsorQualifiedName.equals(sponsor.getUniqueName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad Sponsor Unique Name from Retrieve) =>> " + sponsor.getUniqueName());
            }

            if (retrievedElement.getActionTargets() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Null action targets from Retrieve)");
            }
            if (retrievedElement.getActionTargets().size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad number of action targets from Retrieve) =>> ");
            }

            ActionTargetElement actionTarget = retrievedElement.getActionTargets().get(0);

            if (actionTarget == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Null action target from Retrieve)");
            }
            if (! assetGUID.equals(actionTarget.getTargetElement().getElementGUID()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad action target GUID from Retrieve) =>> " + actionTarget.getTargetElement().getElementGUID());
            }
            
            if (! actionTargetName.equals(actionTarget.getRelationshipProperties().getActionTargetName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad action target name from Retrieve) =>> " + actionTarget.getRelationshipProperties().getActionTargetName());
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
     * Check a "to Do" is gone.
     *
     * @param client interface to Community Profile OMAS
     * @param toDoGUID unique id of the to Do to test
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void checkToDoGone(ToDoActionManagement client,
                               String               toDoGUID,
                               String               userId) throws FVTUnexpectedCondition
    {
        String activityName = "checkToDoGone";

        try
        {
            ToDoElement retrievedElement = client.getToDo(userId, toDoGUID);

            if (retrievedElement != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(ToDo returned from Retrieve)");
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
}
