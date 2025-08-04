/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.converters.ToDoConverter;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ToDoElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ToDoActionHandler extends OpenMetadataHandlerBase
{
    final private ToDoConverter<ToDoElement> toDoConverter;
    final private Class<ToDoElement>         toDoBeanClass = ToDoElement.class;

    /**
     * Create a new handler.
     *
     * @param localServerName    name of this server (view server)
     * @param auditLog           logging destination
     * @param serviceName        local service name
     * @param openMetadataClient access to open metadata
     */
    public ToDoActionHandler(String             localServerName,
                             AuditLog           auditLog,
                             String             serviceName,
                             OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.TO_DO.typeName);

        this.toDoConverter = new ToDoConverter<>(propertyHelper, serviceName, this.localServerName);
    }


    /**
     * Create a new to do action and link it to the supplied role and targets (if applicable).
     *
     * @param userId                    calling user
     * @param originatorGUID            optional originator element (such as a person or Governance Service)
     * @param actionSponsorGUID         optional element that maintains the "To Do" on their list
     * @param assignToActorGUID         optional actor to assign the action to
     * @param toDoActionTargetProperties optional list of elements that the action is to target
     * @param properties                properties of the to do action
     * @return unique identifier of the to do
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public String createToDo(String                              userId,
                             String                              originatorGUID,
                             String                              actionSponsorGUID,
                             String                              assignToActorGUID,
                             AnchorOptions                       anchorOptions,
                             Map<String, NewElementProperties>   initialClassifications,
                             List<NewToDoActionTargetProperties> toDoActionTargetProperties,
                             ToDoProperties                      properties) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName                 = "createToDo";
        final String toDoPropertiesName         = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateObject(properties, toDoPropertiesName, methodName);
        propertyHelper.validateMandatoryName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);


        NewElementOptions newElementOptions = new NewElementOptions(anchorOptions);

        if (properties.getRequestedTime() == null)
        {
            properties.setRequestedTime(new Date());
        }

        NewElementProperties parentRelationshipProperties = null;

        if (originatorGUID != null)
        {
            parentRelationshipProperties = relationshipBuilder.getNewElementProperties(new ActionRequesterProperties());

            newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
            newElementOptions.setOpenMetadataTypeName(metadataElementTypeName);
            newElementOptions.setParentGUID(originatorGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName);
        }
        String toDoGUID = openMetadataClient.createMetadataElementInStore(userId,
                                                                          newElementOptions,
                                                                          initialClassifications,
                                                                          elementBuilder.getNewElementProperties(properties),
                                                                          parentRelationshipProperties);

        if (toDoGUID != null)
        {
            if (assignToActorGUID != null)
            {
                openMetadataClient.createRelatedElementsInStore(userId,
                                                                OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                assignToActorGUID,
                                                                toDoGUID,
                                                                anchorOptions,
                                                                relationshipBuilder.getNewElementProperties(new ActionAssignmentProperties()));
            }

            if (actionSponsorGUID != null)
            {
                openMetadataClient.createRelatedElementsInStore(userId,
                                                                OpenMetadataType.ACTION_SPONSOR_RELATIONSHIP.typeName,
                                                                actionSponsorGUID,
                                                                toDoGUID,
                                                                anchorOptions,
                                                                relationshipBuilder.getNewElementProperties(new ActionSponsorProperties()));
            }

            if (toDoActionTargetProperties != null)
            {
                for (NewToDoActionTargetProperties newActionTarget : toDoActionTargetProperties)
                {
                    if (newActionTarget != null)
                    {
                        ToDoActionTargetProperties actionTargetProperties = new ToDoActionTargetProperties();

                        actionTargetProperties.setActionTargetName(newActionTarget.getActionTargetName());

                        openMetadataClient.createRelatedElementsInStore(userId,
                                                                        OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                        toDoGUID,
                                                                        newActionTarget.getActionTargetGUID(),
                                                                        anchorOptions,
                                                                        relationshipBuilder.getNewElementProperties(actionTargetProperties));
                    }
                }
            }
        }

        return toDoGUID;
    }


    /**
     * Update the properties associated with a "To Do".
     *
     * @param userId        calling user
     * @param toDoGUID      unique identifier of the to do
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties    properties to change
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public void updateToDo(String         userId,
                           String         toDoGUID,
                           UpdateOptions  updateOptions,
                           ToDoProperties properties) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final String methodName                 = "updateToDo";
        final String collectionPropertiesName   = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateObject(properties, collectionPropertiesName, methodName);

        if ((updateOptions != null) && (! updateOptions.getMergePropertyUpdate()))
        {
            propertyHelper.validateMandatoryName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        openMetadataClient.updateMetadataElementInStore(userId,
                                                        toDoGUID,
                                                        updateOptions,
                                                        elementBuilder.getNewElementProperties(properties));
    }


    /**
     * Update the properties associated with an Action Target.
     *
     * @param userId                 calling user
     * @param actionTargetGUID       unique identifier of the action target relationship
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param actionTargetProperties properties to change
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public void updateActionTargetProperties(String                     userId,
                                             String                     actionTargetGUID,
                                             UpdateOptions              updateOptions,
                                             ToDoActionTargetProperties actionTargetProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName     = "updateActionTargetProperties";
        final String propertiesName = "actionTargetProperties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateObject(actionTargetProperties, propertiesName, methodName);

        openMetadataClient.updateRelationshipInStore(userId,
                                                     actionTargetGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getNewElementProperties(actionTargetProperties));
    }


    /**
     * Assign a "To Do" to a new actor.
     *
     * @param userId    calling user
     * @param toDoGUID  unique identifier of the to do
     * @param actorGUID actor to assign the action to
     * @param updateOptions  options to control access to open metadata
     * @param relationshipProperties the properties of the relationship
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public void reassignToDo(String                     userId,
                             String                     toDoGUID,
                             String                     actorGUID,
                             UpdateOptions              updateOptions,
                             ActionAssignmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName              = "reassignToDo";
        final String toDoGUIDParameterName   = "toDoGUID";
        final String parentGUIDParameterName = "actorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(toDoGUID, toDoGUIDParameterName, methodName);
        propertyHelper.validateGUID(actorGUID, parentGUIDParameterName, methodName);

        RelatedMetadataElementList assignedActors = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                  toDoGUID,
                                                                                                  2,
                                                                                                  OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                  new QueryOptions(updateOptions));

        if ((assignedActors != null) && (assignedActors.getElementList() != null))
        {
            for (RelatedMetadataElement assignedActor : assignedActors.getElementList())
            {
                openMetadataClient.deleteRelationshipInStore(userId,
                                                             assignedActor.getRelationshipGUID(),
                                                             new DeleteOptions(updateOptions));
            }
        }

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP.typeName,
                                                        actorGUID,
                                                        toDoGUID,
                                                        updateOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Delete an existing to do.
     *
     * @param userId   calling user
     * @param toDoGUID unique identifier of the to do
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public void deleteToDo(String        userId,
                           String        toDoGUID,
                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String methodName        = "deleteToDo";
        final String guidParameterName = "toDoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(toDoGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, toDoGUID, deleteOptions);
    }


    /**
     * Retrieve a "To Do" by unique identifier.
     *
     * @param userId   calling user
     * @param toDoGUID unique identifier of the to do
     * @param getOptions multiple options to control the query
     * @return to do bean
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public ToDoElement getToDo(String     userId,
                               String     toDoGUID,
                               GetOptions getOptions) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final String methodName        = "getToDo";
        final String guidParameterName = "toDoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(toDoGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByGUID(userId,
                                                                                              toDoGUID,
                                                                                              getOptions);

        return getToDoElement(userId, openMetadataElement, new QueryOptions(getOptions));
    }


    /**
     * Retrieve a "To Do" element starting with the retrieved element.
     *
     * @param userId              calling user
     * @param openMetadataElement the to do element
     * @return to do bean
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    private ToDoElement getToDoElement(String              userId,
                                       OpenMetadataElement openMetadataElement,
                                       QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "getToDoElement";

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, metadataElementTypeName)))
        {
            return toDoConverter.getNewComplexBean(toDoBeanClass, openMetadataElement, super.getElementRelatedElements(userId, openMetadataElement, queryOptions), methodName);
        }

        return null;
    }


    /**
     * Retrieve the "To Dos" that are chained off of an action target element.
     *
     * @param userId      calling user
     * @param elementGUID unique identifier of the element to start with
     * @param activityStatus  optional "To Do" status
     * @param queryOptions           multiple options to control the query
     * @return list of to do beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<ToDoElement> getActionsForActionTarget(String       userId,
                                                       String       elementGUID,
                                                       ActivityStatus activityStatus,
                                                       QueryOptions queryOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName        = "getActionsForActionTarget";
        final String guidParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           elementGUID,
                                                                                                           2,
                                                                                                           OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                                                                           queryOptions);

        return this.convertRelatedToDos(userId, relatedMetadataElements, activityStatus, queryOptions, methodName);
    }


    /**
     * Retrieve the "To Dos" that are chained off of a sponsor's element.
     *
     * @param userId      calling user
     * @param elementGUID unique identifier of the element to start with
     * @param activityStatus  optional "To Do" status
     * @param queryOptions           multiple options to control the query
     * @return list of to do beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<ToDoElement> getActionsForSponsor(String       userId,
                                                  String       elementGUID,
                                                  ActivityStatus   activityStatus,
                                                  QueryOptions queryOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName        = "getActionsForSponsor";
        final String guidParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           elementGUID,
                                                                                                           1,
                                                                                                           OpenMetadataType.ACTION_SPONSOR_RELATIONSHIP.typeName,
                                                                                                           queryOptions);

        return this.convertRelatedToDos(userId, relatedMetadataElements, activityStatus, queryOptions, methodName);
    }


    /**
     * Retrieve the "To Dos" for a particular actor.
     *
     * @param userId     calling user
     * @param actorGUID  unique identifier of the role
     * @param activityStatus optional "To Do" status
     * @param queryOptions           multiple options to control the query
     * @return list of to do beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<ToDoElement> getAssignedActions(String       userId,
                                                String       actorGUID,
                                                ActivityStatus   activityStatus,
                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName        = "getAssignedActions";
        final String guidParameterName = "actorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(actorGUID, guidParameterName, methodName);

        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           actorGUID,
                                                                                                           1,
                                                                                                           OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                           queryOptions);

        return this.convertRelatedToDos(userId, relatedMetadataElements, activityStatus, queryOptions, methodName);
    }


    /**
     * Retrieve the "To Dos" that match the search string.
     *
     * @param userId       calling user
     * @param searchString string to search for (may include RegExs)
     * @param activityStatus   optional "To Do" status
     * @param searchOptions           multiple options to control the query
     * @return list of to do beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<ToDoElement> findToDos(String        userId,
                                       String        searchString,
                                       ActivityStatus    activityStatus,
                                       SearchOptions searchOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName                = "findToDos";
        final String searchStringParameterName = "searchString";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateSearchString(searchString, searchStringParameterName, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId,
                                                                                                           searchString,
                                                                                                           super.addDefaultType(searchOptions));

        return convertToDos(userId, openMetadataElements, activityStatus, searchOptions, methodName);
    }


    /**
     * Retrieve the "To Dos" that match the type name and status.
     *
     * @param userId     calling user
     * @param category   type to search for
     * @param activityStatus optional "To Do" status
     * @param queryOptions multiple options to control the query
     * @param startFrom  initial position of the results to return
     * @param pageSize   maximum number of results to return
     * @return list of to do beans
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    public List<ToDoElement> getToDosByCategory(String       userId,
                                                String       category,
                                                ActivityStatus   activityStatus,
                                                QueryOptions queryOptions,
                                                int          startFrom,
                                                int          pageSize) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "getToDosByCategory";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validatePaging(startFrom, pageSize, openMetadataClient.getMaxPagingSize(), methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.getMetadataElementsByPropertyValue(userId,
                                                                                                               OpenMetadataProperty.CATEGORY.name,
                                                                                                               category,
                                                                                                               queryOptions);

        return convertToDos(userId, openMetadataElements, activityStatus, queryOptions, methodName);
    }


    /**
     * Convert to do objects from the OpenMetadataClient to local beans.
     *
     * @param userId               calling user
     * @param openMetadataElements retrieved elements
     * @param activityStatus           optional "To Do" status
     * @param queryOptions multiple options to control the query
     * @param methodName           calling method
     * @return list of collection elements
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    private List<ToDoElement> convertToDos(String                    userId,
                                           List<OpenMetadataElement> openMetadataElements,
                                           ActivityStatus                activityStatus,
                                           QueryOptions              queryOptions,
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
                    ToDoElement toDoElement = toDoConverter.getNewComplexBean(toDoBeanClass,
                                                                              openMetadataElement,
                                                                              super.getElementRelatedElements(userId, openMetadataElement, queryOptions),
                                                                              methodName);

                    if ((activityStatus == null) ||
                            ((toDoElement.getProperties() instanceof ToDoProperties toDoProperties) &&
                                    (activityStatus == toDoProperties.getActivityStatus())))
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
     * Convert to do objects from the OpenMetadataClient to local beans.
     *
     * @param userId                  calling user
     * @param relatedMetadataElements retrieved elements
     * @param activityStatus              optional "To Do" status
     * @param queryOptions           multiple options to control the query
     * @param methodName           calling method
     * @return list of collection elements
     * @throws InvalidParameterException  a parameter is invalid
     * @throws PropertyServerException    the server is not available
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    private List<ToDoElement> convertRelatedToDos(String                     userId,
                                                  RelatedMetadataElementList relatedMetadataElements,
                                                  ActivityStatus                 activityStatus,
                                                  QueryOptions               queryOptions,
                                                  String                     methodName) throws PropertyServerException,
                                                                                                InvalidParameterException,
                                                                                                UserNotAuthorizedException
    {
        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<ToDoElement> toDoElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    ToDoElement toDoElement = toDoConverter.getNewComplexBean(toDoBeanClass,
                                                                              relatedMetadataElement,
                                                                              super.getElementRelatedElements(userId, relatedMetadataElement.getElement(), queryOptions),
                                                                              methodName);

                    if ((activityStatus == null) ||
                            ((toDoElement.getProperties() instanceof ToDoProperties toDoProperties) &&
                                    (activityStatus == toDoProperties.getActivityStatus())))                    {
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
}