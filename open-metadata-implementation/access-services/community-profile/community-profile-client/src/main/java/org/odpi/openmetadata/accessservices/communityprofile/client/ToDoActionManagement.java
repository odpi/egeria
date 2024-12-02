/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.api.ToDoManagementInterface;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ToDoElement;
import org.odpi.openmetadata.frameworks.governanceaction.converters.ToDoConverter;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.ToDoActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.NewToDoActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.ToDoProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ToDoStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;

import java.util.*;

public class ToDoActionManagement extends CommunityProfileBaseClient implements ToDoManagementInterface
{
    final private ToDoConverter<ToDoElement> toDoConverter;
    final private Class<ToDoElement>         toDoBeanClass = ToDoElement.class;

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog              logging destination
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ToDoActionManagement(String   serverName,
                                String   serverPlatformURLRoot,
                                AuditLog auditLog,
                                int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog, maxPageSize);

        toDoConverter = new ToDoConverter<>(propertyHelper,
                                            AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceName(),
                                            serverName);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ToDoActionManagement(String serverName,
                                String serverPlatformURLRoot,
                                int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);

        toDoConverter = new ToDoConverter<>(propertyHelper,
                                            AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceName(),
                                            serverName);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ToDoActionManagement(String serverName,
                                String serverPlatformURLRoot,
                                String userId,
                                String password,
                                int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);

        toDoConverter = new ToDoConverter<>(propertyHelper,
                                            AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceName(),
                                            serverName);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param auditLog              logging destination
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ToDoActionManagement(String   serverName,
                                String   serverPlatformURLRoot,
                                String   userId,
                                String   password,
                                AuditLog auditLog,
                                int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog, maxPageSize);

        toDoConverter = new ToDoConverter<>(propertyHelper,
                                            AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceName(),
                                            serverName);
    }


    /**
     * Create a new to do action and link it to the supplied role and targets (if applicable).
     *
     * @param userId                    calling user
     * @param originatorGUID            optional originator element (such as a person or Governance Service)
     * @param actionSponsorGUID           optional element that maintains the "To Do" on their list
     * @param assignToActorGUID         optional actor to assign the action to
     * @param newActionTargetProperties optional list of elements that the action is to target
     * @param properties                properties of the to do action
     * @return unique identifier of the to do
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @Override
    public String createToDo(String                          userId,
                             String                          originatorGUID,
                             String                          actionSponsorGUID,
                             String                          assignToActorGUID,
                             List<NewToDoActionTargetProperties> newActionTargetProperties,
                             ToDoProperties                  properties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName                 = "createToDo";
        final String toDoPropertiesName         = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, toDoPropertiesName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String toDoTypeName = OpenMetadataType.TO_DO.typeName;

        if (properties.getTypeName() != null)
        {
            toDoTypeName = properties.getTypeName();
        }

        if (properties.getCreationTime() == null)
        {
            properties.setCreationTime(new Date());
        }

        String toDoGUID = openMetadataStoreClient.createMetadataElementInStore(userId,
                                                                               null,
                                                                               null,
                                                                               toDoTypeName,
                                                                               ElementStatus.ACTIVE,
                                                                               null,
                                                                               null,
                                                                               true,
                                                                               properties.getEffectiveFrom(),
                                                                               properties.getEffectiveTo(),
                                                                               this.getToDoProperties(properties),
                                                                               originatorGUID,
                                                                               OpenMetadataType.TO_DO_SOURCE_RELATIONSHIP.typeName,
                                                                               null,
                                                                               true);

        if (toDoGUID != null)
        {
            if (assignToActorGUID != null)
            {
                openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                     null,
                                                                     null,
                                                                     OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                     assignToActorGUID,
                                                                     toDoGUID,
                                                                     false,
                                                                     false,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     null);
            }

            if (actionSponsorGUID != null)
            {
                openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                     null,
                                                                     null,
                                                                     OpenMetadataType.ACTION_SPONSOR_RELATIONSHIP.typeName,
                                                                     actionSponsorGUID,
                                                                     toDoGUID,
                                                                     false,
                                                                     false,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     null);
            }

            if (newActionTargetProperties != null)
            {
                for (NewToDoActionTargetProperties newActionTarget : newActionTargetProperties)
                {
                    if (newActionTarget != null)
                    {
                        ElementProperties actionTargetProperties = propertyHelper.addStringProperty(null,
                                                                                                    OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                                                    newActionTarget.getActionTargetName());

                        openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                             null,
                                                                             null,
                                                                             OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                             toDoGUID,
                                                                             newActionTarget.getActionTargetGUID(),
                                                                             false,
                                                                             false,
                                                                             null,
                                                                             null,
                                                                             actionTargetProperties,
                                                                             null);
                    }
                }
            }
        }


        return toDoGUID;
    }


    /**
     * Update the properties associated with a "To Do".
     *
     * @param userId         calling user
     * @param toDoGUID       unique identifier of the to do
     * @param isMergeUpdate  should the properties overlay the existing stored properties or replace them
     * @param properties properties to change
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @Override
    public void updateToDo(String         userId,
                           String         toDoGUID,
                           boolean        isMergeUpdate,
                           ToDoProperties properties) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final String methodName = "updateToDo";
        final String collectionPropertiesName = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, collectionPropertiesName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        openMetadataStoreClient.updateMetadataElementInStore(userId,
                                                             toDoGUID,
                                                             !isMergeUpdate,
                                                             false,
                                                             false,
                                                             this.getToDoProperties(properties),
                                                             new Date());
    }


    /**
     * Update the properties associated with an Action Target.
     *
     * @param userId                 calling user
     * @param actionTargetGUID               unique identifier of the action target relationship
     * @param isMergeUpdate          should the actionTargetProperties overlay the existing stored properties or replace them
     * @param actionTargetProperties properties to change
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @Override
    public void updateActionTargetProperties(String                 userId,
                                             String                 actionTargetGUID,
                                             boolean                isMergeUpdate,
                                             ToDoActionTargetProperties actionTargetProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "updateActionTargetProperties";
        final String propertiesName = "actionTargetProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(actionTargetProperties, propertiesName, methodName);

        openMetadataStoreClient.updateRelatedElementsInStore(userId,
                                                             null,
                                                             null,
                                                             actionTargetGUID,
                                                             ! isMergeUpdate,
                                                             false,
                                                             false,
                                                             this.getActionTargetProperties(actionTargetProperties),
                                                             new Date());
    }


    /**
     * Assign a "To Do" to a new actor.
     *
     * @param userId    calling user
     * @param toDoGUID  unique identifier of the to do
     * @param actorGUID actor to assign the action to
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @Override
    public void reassignToDo(String userId,
                             String toDoGUID,
                             String actorGUID) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException
    {
        final String methodName = "reassignToDo";
        final String toDoGUIDParameterName = "toDoGUID";
        final String parentGUIDParameterName = "actorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(toDoGUID, toDoGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(actorGUID, parentGUIDParameterName, methodName);

        List<RelatedMetadataElement> assignedActors = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                         toDoGUID,
                                                                                                         2,
                                                                                                         OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                         null,
                                                                                                         null,
                                                                                                         null,
                                                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                                                         false,
                                                                                                         false,
                                                                                                         new Date(),
                                                                                                         0,
                                                                                                         0);

        if (assignedActors != null)
        {
            for (RelatedMetadataElement assignedActor : assignedActors)
            {
                openMetadataStoreClient.deleteRelatedElementsInStore(userId,
                                                                     null,
                                                                     null,
                                                                     assignedActor.getRelationshipGUID(),
                                                                     false,
                                                                     false,
                                                                     new Date());
            }
        }

        openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                             null,
                                                             null,
                                                             OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP.typeName,
                                                             actorGUID,
                                                             toDoGUID,
                                                             false,
                                                             false,
                                                             null,
                                                             null,
                                                             null,
                                                             new Date());
    }


    /**
     * Delete an existing to do.
     *
     * @param userId   calling user
     * @param toDoGUID unique identifier of the to do
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @Override
    public void deleteToDo(String userId,
                           String toDoGUID) throws InvalidParameterException,
                                                   PropertyServerException,
                                                   UserNotAuthorizedException
    {
        final String methodName = "deleteToDo";
        final String guidParameterName = "toDoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(toDoGUID, guidParameterName, methodName);

        openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                             toDoGUID,
                                                             false,
                                                             false,
                                                             new Date());
    }


    /**
     * Retrieve a "To Do" by unique identifier.
     *
     * @param userId   calling user
     * @param toDoGUID unique identifier of the to do
     * @return to do bean
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @Override
    public ToDoElement getToDo(String userId,
                               String toDoGUID) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        final String methodName        = "getToDo";
        final String guidParameterName = "toDoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(toDoGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   toDoGUID,
                                                                                                   false,
                                                                                                   false,
                                                                                                   null,
                                                                                                   new Date());

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.TO_DO.typeName)))
        {
            List<RelatedMetadataElement> relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                      openMetadataElement.getElementGUID(),
                                                                                                                      0,
                                                                                                                      null,
                                                                                                                      null,
                                                                                                                      null,
                                                                                                                      null,
                                                                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                      false,
                                                                                                                      false,
                                                                                                                      new Date(),
                                                                                                                      0,
                                                                                                                      0);

            return toDoConverter.getNewComplexBean(toDoBeanClass, openMetadataElement, relatedMetadataElements, methodName);
        }

        return null;
    }


    /**
     * Retrieve the "To Dos" that are chained off of an action target element.
     *
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element to start with
     * @param toDoStatus         optional "To Do" status
     * @param startFrom          initial position of the results to return
     * @param pageSize           maximum number of results to return
     * @return list of to do beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @Override
    public List<ToDoElement> getActionsForActionTarget(String     userId,
                                                       String     elementGUID,
                                                       ToDoStatus toDoStatus,
                                                       int        startFrom,
                                                       int        pageSize) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName        = "getActionsForActionTarget";
        final String guidParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        List<RelatedMetadataElement> relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                  elementGUID,
                                                                                                                  2,
                                                                                                                  OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                  false,
                                                                                                                  false,
                                                                                                                  new Date(),
                                                                                                                  startFrom,
                                                                                                                  pageSize);
        return this.convertRelatedToDos(userId, relatedMetadataElements, toDoStatus, methodName);
    }


    /**
     * Retrieve the "To Dos" that are chained off of a sponsor's element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element to start with
     * @param toDoStatus optional "To Do" status
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     *
     * @return list of to do beans
     *
     * @throws InvalidParameterException a parameter is invalid
     * @throws PropertyServerException the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @Override
    public List<ToDoElement> getActionsForSponsor(String     userId,
                                                  String     elementGUID,
                                                  ToDoStatus toDoStatus,
                                                  int        startFrom,
                                                  int        pageSize) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName        = "getActionsForSponsor";
        final String guidParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        List<RelatedMetadataElement> relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                  elementGUID,
                                                                                                                  1,
                                                                                                                  OpenMetadataType.ACTION_SPONSOR_RELATIONSHIP.typeName,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                  false,
                                                                                                                  false,
                                                                                                                  new Date(),
                                                                                                                  startFrom,
                                                                                                                  pageSize);
        return this.convertRelatedToDos(userId, relatedMetadataElements, toDoStatus, methodName);
    }


    /**
     * Retrieve the "To Dos" for a particular actor.
     *
     * @param userId     calling user
     * @param actorGUID  unique identifier of the role
     * @param toDoStatus optional "To Do" status
     * @param startFrom  initial position of the results to return
     * @param pageSize   maximum number of results to return
     * @return list of to do beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @Override
    public List<ToDoElement> getAssignedActions(String     userId,
                                                String     actorGUID,
                                                ToDoStatus toDoStatus,
                                                int        startFrom,
                                                int        pageSize) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName        = "getAssignedActions";
        final String guidParameterName = "actorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actorGUID, guidParameterName, methodName);

        List<RelatedMetadataElement> relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                  actorGUID,
                                                                                                                  1,
                                                                                                                  OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                  false,
                                                                                                                  false,
                                                                                                                  new Date(),
                                                                                                                  startFrom,
                                                                                                                  pageSize);
        return this.convertRelatedToDos(userId, relatedMetadataElements, toDoStatus, methodName);
    }



    /**
     * Retrieve the "To Dos" that match the search string.
     *
     * @param userId       calling user
     * @param searchString string to search for (may include RegExs)
     * @param toDoStatus   optional "To Do" status
     * @param startFrom    initial position of the results to return
     * @param pageSize     maximum number of results to return
     * @return list of to do beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @Override
    public List<ToDoElement> findToDos(String     userId,
                                       String     searchString,
                                       ToDoStatus toDoStatus,
                                       int        startFrom,
                                       int        pageSize) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName                = "findToDos";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                OpenMetadataType.TO_DO.typeName,
                                                                                                                null,
                                                                                                                null,
                                                                                                                null,
                                                                                                                null,
                                                                                                                false,
                                                                                                                false,
                                                                                                                new Date(),
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertToDos(userId, openMetadataElements, toDoStatus, methodName);
    }


    /**
     * Retrieve the "To Dos" that match the type name and status.
     *
     * @param userId     calling user
     * @param toDoType   type to search for
     * @param toDoStatus optional "To Do" status
     * @param startFrom  initial position of the results to return
     * @param pageSize   maximum number of results to return
     * @return list of to do beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @Override
    public List<ToDoElement> getToDosByType(String     userId,
                                            String     toDoType,
                                            ToDoStatus toDoStatus,
                                            int        startFrom,
                                            int        pageSize) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "getToDosByType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.getMetadataElementsByPropertyValue(userId,
                                                                                                                    OpenMetadataType.TO_DO.typeName,
                                                                                                                    null,
                                                                                                                    OpenMetadataProperty.TO_DO_TYPE.name,
                                                                                                                    toDoType,
                                                                                                                    null,
                                                                                                                    null,
                                                                                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                    SequencingOrder.PROPERTY_ASCENDING,
                                                                                                                    false,
                                                                                                                    false,
                                                                                                                    new Date(),
                                                                                                                    startFrom,
                                                                                                                    pageSize);

        return convertToDos(userId, openMetadataElements, toDoStatus, methodName);
    }


    /**
     * Convert to do objects from the OpenMetadataClient to local beans.
     *
     * @param userId calling user
     * @param openMetadataElements retrieved elements
     * @param toDoStatus           optional "To Do" status
     * @param methodName           calling method
     * @return list of collection elements
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    private List<ToDoElement> convertToDos(String                    userId,
                                           List<OpenMetadataElement> openMetadataElements,
                                           ToDoStatus                toDoStatus,
                                           String                    methodName) throws PropertyServerException,
                                                                                        InvalidParameterException,
                                                                                        UserNotAuthorizedException
    {
        if (openMetadataElements != null)
        {
            List<ToDoElement> toDoElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    List<RelatedMetadataElement> relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                              openMetadataElement.getElementGUID(),
                                                                                                                              0,
                                                                                                                              null,
                                                                                                                              null,
                                                                                                                              null,
                                                                                                                              null,
                                                                                                                              SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                              false,
                                                                                                                              false,
                                                                                                                              new Date(),
                                                                                                                              0,
                                                                                                                              0);
                    ToDoElement toDoElement = toDoConverter.getNewComplexBean(toDoBeanClass,
                                                                              openMetadataElement,
                                                                              relatedMetadataElements,
                                                                              methodName);

                    if ((toDoStatus == null) || (toDoStatus == toDoElement.getProperties().getToDoStatus()))
                    {
                        toDoElements.add(toDoElement);
                    }
                }
            }

            if (! toDoElements.isEmpty())
            {
                return toDoElements;
            }
        }

        return null;
    }


    /**
     * Convert to do objects from the OpenMetadataClient to local beans.
     *
     * @param userId calling user
     * @param relatedMetadataElements retrieved elements
     * @param toDoStatus           optional "To Do" status
     * @param methodName           calling method
     * @return list of collection elements
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    private List<ToDoElement> convertRelatedToDos(String                       userId,
                                                  List<RelatedMetadataElement> relatedMetadataElements,
                                                  ToDoStatus                   toDoStatus,
                                                  String                       methodName) throws PropertyServerException,
                                                                                                  InvalidParameterException,
                                                                                                  UserNotAuthorizedException
    {
        if (relatedMetadataElements != null)
        {
            List<ToDoElement> toDoElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    List<RelatedMetadataElement> relatedElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                      relatedMetadataElement.getElement().getElementGUID(),
                                                                                                                      0,
                                                                                                                      null,
                                                                                                                      null,
                                                                                                                      null,
                                                                                                                      null,
                                                                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                      false,
                                                                                                                      false,
                                                                                                                      new Date(),
                                                                                                                      0,
                                                                                                                      0);
                    ToDoElement toDoElement = toDoConverter.getNewComplexBean(toDoBeanClass,
                                                                              relatedMetadataElement.getElement(),
                                                                              relatedElements,
                                                                              methodName);

                    if ((toDoStatus == null) || (toDoStatus == toDoElement.getProperties().getToDoStatus()))
                    {
                        toDoElements.add(toDoElement);
                    }
                }
            }

            if (!toDoElements.isEmpty())
            {
                return toDoElements;
            }
        }

        return null;
    }


    /**
     * Convert the to do properties into a set of element properties for the open metadata client.
     *
     * @param toDoProperties supplied to do properties
     * @return element properties
     */
    private ElementProperties getToDoProperties(ToDoProperties toDoProperties)
    {
        if (toDoProperties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   toDoProperties.getQualifiedName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.NAME.name,
                                                                 toDoProperties.getName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DESCRIPTION.name,
                                                                 toDoProperties.getDescription());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.TO_DO_TYPE.name,
                                                                 toDoProperties.getToDoType());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.PRIORITY.name,
                                                              toDoProperties.getPriority());

            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                               OpenMetadataProperty.CREATION_TIME.name,
                                                               toDoProperties.getCreationTime());

            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                               OpenMetadataProperty.LAST_REVIEW_TIME.name,
                                                               toDoProperties.getLastReviewTime());

            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                               OpenMetadataProperty.DUE_TIME.name,
                                                               toDoProperties.getDueTime());

            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                               OpenMetadataProperty.COMPLETION_TIME.name,
                                                               toDoProperties.getCompletionTime());

            elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                    toDoProperties.getAdditionalProperties());

            if (toDoProperties.getToDoStatus() != null)
            {
                elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                   OpenMetadataProperty.TO_DO_STATUS.name,
                                                                   ToDoStatus.getOpenTypeName(),
                                                                   toDoProperties.getToDoStatus().getName());
            }

            elementProperties = propertyHelper.addPropertyMap(elementProperties,
                                                              toDoProperties.getExtendedProperties());

            return elementProperties;
        }

        return null;
    }


    /**
     * Convert the action target properties into a set of element properties for the open metadata client.
     *
     * @param actionTargetProperties supplied to do properties
     * @return element properties
     */
    private ElementProperties getActionTargetProperties(ToDoActionTargetProperties actionTargetProperties)
    {
        if (actionTargetProperties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                                   actionTargetProperties.getActionTargetName());

            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                               OpenMetadataProperty.START_DATE.name,
                                                               actionTargetProperties.getStartDate());

            elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                               OpenMetadataProperty.COMPLETION_DATE.name,
                                                               actionTargetProperties.getCompletionDate());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.COMPLETION_MESSAGE.name,
                                                                 actionTargetProperties.getCompletionMessage());

            if (actionTargetProperties.getStatus() != null)
            {
                elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                   OpenMetadataProperty.TO_DO_STATUS.name,
                                                                   ToDoStatus.getOpenTypeName(),
                                                                   actionTargetProperties.getStatus().getName());
            }

            return elementProperties;
        }

        return null;
    }
}
