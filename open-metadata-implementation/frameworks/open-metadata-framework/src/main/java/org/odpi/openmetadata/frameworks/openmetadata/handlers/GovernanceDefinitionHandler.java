/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionExecutorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionProcessFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.NextGovernanceActionProcessStepProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.TargetForGovernanceActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementationResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * GovernanceDefinitionHandler is the handler for managing governance definitions.
 */
public class GovernanceDefinitionHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public GovernanceDefinitionHandler(String             localServerName,
                                       AuditLog           auditLog,
                                       String             serviceName,
                                       OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.GOVERNANCE_DEFINITION.typeName);
    }


    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     * @param specificTypeName   subtype to control handler
     */
    public GovernanceDefinitionHandler(String             localServerName,
                                       AuditLog           auditLog,
                                       String             serviceName,
                                       OpenMetadataClient openMetadataClient,
                                       String             specificTypeName)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, specificTypeName);
    }


    /**
     * Create a new handler.
     *
     * @param template        properties to copy
     * @param specificTypeName   subtype to control handler
     */
    public GovernanceDefinitionHandler(GovernanceDefinitionHandler template,
                                       String                      specificTypeName)
    {
        super(template, specificTypeName);
    }


    /**
     * Create a new governance definition.
     *
     * @param userId                       userId of user making request.
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createGovernanceDefinition(String                                userId,
                                             NewElementOptions                     newElementOptions,
                                             Map<String, ClassificationProperties> initialClassifications,
                                             GovernanceDefinitionProperties        properties,
                                             RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                        PropertyServerException,
                                                                                                                        UserNotAuthorizedException
    {
        final String methodName = "createGovernanceDefinition";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a governance definition using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new governance definition.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createGovernanceDefinitionFromTemplate(String                 userId,
                                                         TemplateOptions        templateOptions,
                                                         String                 templateGUID,
                                                         EntityProperties       replacementProperties,
                                                         Map<String, String>    placeholderProperties,
                                                         RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of a governance definition.
     *
     * @param userId                 userId of user making request.
     * @param governanceDefinitionGUID      unique identifier of the governance definition (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateGovernanceDefinition(String                         userId,
                                              String                         governanceDefinitionGUID,
                                              UpdateOptions                  updateOptions,
                                              GovernanceDefinitionProperties properties) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "updateGovernanceDefinition";
        final String guidParameterName = "governanceDefinitionGUID";

        return super.updateElement(userId,
                                   governanceDefinitionGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Attach two peer governance definitions.
     *
     * @param userId                  userId of user making request
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  additional properties for the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeerDefinitions(String                    userId,
                                    String                    governanceDefinitionOneGUID,
                                    String                    governanceDefinitionTwoGUID,
                                    String                    relationshipTypeName,
                                    MakeAnchorOptions         makeAnchorOptions,
                                    PeerDefinitionProperties  relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "linkPeerDefinitions";
        final String end1GUIDParameterName = "governanceDefinitionOneGUID";
        final String end2GUIDParameterName = "governanceDefinitionTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceDefinitionOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(governanceDefinitionTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        relationshipTypeName,
                                                        governanceDefinitionOneGUID,
                                                        governanceDefinitionTwoGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a governance definition from one of its peers.
     *
     * @param userId                 userId of user making request.
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPeerDefinitions(String        userId,
                                      String        governanceDefinitionOneGUID,
                                      String        governanceDefinitionTwoGUID,
                                      String        relationshipTypeName,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "detachPeerDefinitions";

        final String end1GUIDParameterName = "governanceDefinitionOneGUID";
        final String end2GUIDParameterName = "governanceDefinitionTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceDefinitionOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(governanceDefinitionTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        relationshipTypeName,
                                                        governanceDefinitionOneGUID,
                                                        governanceDefinitionTwoGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a supporting governance definition.
     *
     * @param userId                  userId of user making request
     * @param governanceDefinitionOneGUID unique identifier of the parent governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the child governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  additional properties for the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void attachSupportingDefinition(String                         userId,
                                           String                         governanceDefinitionOneGUID,
                                           String                         governanceDefinitionTwoGUID,
                                           String                         relationshipTypeName,
                                           MakeAnchorOptions              makeAnchorOptions,
                                           SupportingDefinitionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "attachSupportingDefinition";
        final String end1GUIDParameterName = "governanceDefinitionOneGUID";
        final String end2GUIDParameterName = "governanceDefinitionTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceDefinitionOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(governanceDefinitionTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        relationshipTypeName,
                                                        governanceDefinitionOneGUID,
                                                        governanceDefinitionTwoGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));

    }


    /**
     * Detach a governance definition from a supporting governance definition.
     *
     * @param userId                 userId of user making request.
     * @param governanceDefinitionOneGUID unique identifier of the parent governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the child governance definition
     * @param relationshipTypeName name of the relationship to use
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSupportingDefinition(String        userId,
                                           String        governanceDefinitionOneGUID,
                                           String        governanceDefinitionTwoGUID,
                                           String        relationshipTypeName,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "detachSupportingDefinition";

        final String end1GUIDParameterName = "governanceDefinitionOneGUID";
        final String end2GUIDParameterName = "governanceDefinitionTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceDefinitionOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(governanceDefinitionTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        relationshipTypeName,
                                                        governanceDefinitionOneGUID,
                                                        governanceDefinitionTwoGUID,
                                                        deleteOptions);
    }



    /**
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to link
     * @param definitionGUID identifier of the governance definition to link
     * @param makeAnchorOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public void addGovernanceDefinitionToElement(String                userId,
                                                 String                elementGUID,
                                                 String                definitionGUID,
                                                 MakeAnchorOptions     makeAnchorOptions,
                                                 GovernedByProperties  properties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        definitionGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     * @param definitionGUID identifier of the governance definition to link
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public void removeGovernanceDefinitionFromElement(String        userId,
                                                      String        elementGUID,
                                                      String        definitionGUID,
                                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName            = "removeGovernanceDefinitionFromElement";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "definitionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(definitionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        definitionGUID,
                                                        deleteOptions);
    }


    /**
     * Attach an actor to an element that describes its scope.
     *
     * @param userId                        userId of user making request
     * @param scopeElementGUID            unique identifier of the element
     * @param actorGUID unique identifier of the actor
     * @param makeAnchorOptions         options to control access to open metadata
     * @param relationshipProperties        description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAssignmentScope(String                    userId,
                                    String                    scopeElementGUID,
                                    String                    actorGUID,
                                    MakeAnchorOptions         makeAnchorOptions,
                                    AssignmentScopeProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName            = "linkAssignmentScope";
        final String end1GUIDParameterName = "scopeElementGUID";
        final String end2GUIDParameterName = "actorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(scopeElementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(actorGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                        actorGUID,
                                                        scopeElementGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an actor from the element that describes its scope.
     *
     * @param userId                      userId of user making request.
     * @param scopeElementGUID            unique identifier of the element
     * @param actorGUID                   unique identifier of the actor
     * @param deleteOptions               options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAssignmentScope(String        userId,
                                      String        scopeElementGUID,
                                      String        actorGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "detachAssignmentScope";

        final String end1GUIDParameterName = "scopeElementGUID";
        final String end2GUIDParameterName = "actorGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(scopeElementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(actorGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                        actorGUID,
                                                        scopeElementGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a monitored resource to a notification type.
     *
     * @param userId                        userId of user making request
     * @param notificationTypeGUID            unique identifier of the notification type
     * @param elementGUID             unique identifier of the element to monitor
     * @param makeAnchorOptions         options to control access to open metadata
     * @param relationshipProperties        description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkMonitoredResource(String                      userId,
                                      String                      notificationTypeGUID,
                                      String                      elementGUID,
                                      MakeAnchorOptions           makeAnchorOptions,
                                      MonitoredResourceProperties relationshipProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String methodName            = "linkMonitoredResource";
        final String end1GUIDParameterName = "notificationTypeGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(notificationTypeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName,
                                                        notificationTypeGUID,
                                                        elementGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a monitored resource from a notification type.
     *
     * @param userId                      userId of user making request.
     * @param notificationTypeGUID            unique identifier of the notification type
     * @param elementGUID             unique identifier of the element to monitor
     * @param deleteOptions               options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachMonitoredResource(String        userId,
                                        String        notificationTypeGUID,
                                        String        elementGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "detachMonitoredResource";

        final String end1GUIDParameterName = "notificationTypeGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(notificationTypeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName,
                                                        notificationTypeGUID,
                                                        elementGUID,
                                                        deleteOptions);
    }


    /**
     * Attach subscriber to a notification type.
     *
     * @param userId                        userId of user making request
     * @param notificationTypeGUID            unique identifier of the notification type
     * @param elementGUID             unique identifier of the subscriber element
     * @param makeAnchorOptions         options to control access to open metadata
     * @param relationshipProperties        description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNotificationSubscriber(String                           userId,
                                           String                           notificationTypeGUID,
                                           String                           elementGUID,
                                           MakeAnchorOptions                makeAnchorOptions,
                                           NotificationSubscriberProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName            = "linkNotificationSubscriber";
        final String end1GUIDParameterName = "notificationTypeGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(notificationTypeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName,
                                                        notificationTypeGUID,
                                                        elementGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a subscriber from a notification type.
     *
     * @param userId                      userId of user making request.
     * @param notificationTypeGUID            unique identifier of the notification type
     * @param elementGUID             unique identifier of the subscriber element
     * @param deleteOptions               options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNotificationSubscriber(String        userId,
                                             String        notificationTypeGUID,
                                             String        elementGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "detachNotificationSubscriber";

        final String end1GUIDParameterName = "notificationTypeGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(notificationTypeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName,
                                                        notificationTypeGUID,
                                                        elementGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a governance metric to an asset that represents the data store where the measurements are located.
     *
     * @param userId                        userId of user making request
     * @param governanceMetricGUID            unique identifier of the metric
     * @param dataSourceGUID unique identifier of the asset
     * @param makeAnchorOptions         options to control access to open metadata
     * @param relationshipProperties        description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkGovernanceResults(String                      userId,
                                      String                      governanceMetricGUID,
                                      String                      dataSourceGUID,
                                      MakeAnchorOptions           makeAnchorOptions,
                                      GovernanceResultsProperties relationshipProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String methodName            = "linkGovernanceResults";
        final String end1GUIDParameterName = "governanceMetricGUID";
        final String end2GUIDParameterName = "dataSourceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceMetricGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataSourceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.GOVERNANCE_RESULTS_RELATIONSHIP.typeName,
                                                        dataSourceGUID,
                                                        governanceMetricGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a governance metric from an asset that represents the data store where the measurements are located.
     *
     * @param userId                      userId of user making request.
     * @param governanceMetricGUID            unique identifier of the metric
     * @param dataSourceGUID                   unique identifier of the asset
     * @param deleteOptions               options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachGovernanceResults(String        userId,
                                        String        governanceMetricGUID,
                                        String        dataSourceGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "detachGovernanceResults";

        final String end1GUIDParameterName = "governanceMetricGUID";
        final String end2GUIDParameterName = "dataSourceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceMetricGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataSourceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.GOVERNANCE_RESULTS_RELATIONSHIP.typeName,
                                                        dataSourceGUID,
                                                        governanceMetricGUID,
                                                        deleteOptions);
    }


    /**
     * Attach governance zones in a hierarchy.
     *
     * @param userId                  userId of user making request
     * @param governanceZoneGUID        unique identifier of the parent
     * @param nestedGovernanceZoneGUID             unique identifier of the actor profile
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkGovernanceZones(String                  userId,
                                    String                  governanceZoneGUID,
                                    String                  nestedGovernanceZoneGUID,
                                    MakeAnchorOptions       makeAnchorOptions,
                                    ZoneHierarchyProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "linkGovernanceZones";
        final String end1GUIDParameterName = "governanceZoneGUID";
        final String end2GUIDParameterName = "nestedGovernanceZoneGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceZoneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedGovernanceZoneGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ZONE_HIERARCHY_RELATIONSHIP.typeName,
                                                        governanceZoneGUID,
                                                        nestedGovernanceZoneGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach governance zone from a hierarchical relationship.
     *
     * @param userId                 userId of user making request.
     * @param governanceZoneGUID       unique identifier of the parent actor profile
     * @param nestedGovernanceZoneGUID            unique identifier of the nested actor profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachGovernanceZones(String        userId,
                                      String        governanceZoneGUID,
                                      String        nestedGovernanceZoneGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "detachGovernanceZones";

        final String end1GUIDParameterName = "governanceZoneGUID";
        final String end2GUIDParameterName = "nestedGovernanceZoneGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceZoneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedGovernanceZoneGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.ZONE_HIERARCHY_RELATIONSHIP.typeName,
                                                        governanceZoneGUID,
                                                        nestedGovernanceZoneGUID,
                                                        deleteOptions);
    }


    /**
     * Link subject area definitions in a hierarchy.
     *
     * @param userId                  userId of user making request
     * @param subjectAreaGUID        unique identifier of the parent
     * @param nestedSubjectAreaGUID             unique identifier of the actor profile
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSubjectAreas(String                         userId,
                                 String                         subjectAreaGUID,
                                 String                         nestedSubjectAreaGUID,
                                 MakeAnchorOptions              makeAnchorOptions,
                                 SubjectAreaHierarchyProperties relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "linkSubjectAreas";
        final String end1GUIDParameterName = "subjectAreaGUID";
        final String end2GUIDParameterName = "nestedSubjectAreaGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(subjectAreaGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedSubjectAreaGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName,
                                                        subjectAreaGUID,
                                                        nestedSubjectAreaGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach subject area definitions from their hierarchical relationship.
     *
     * @param userId                 userId of user making request.
     * @param subjectAreaGUID       unique identifier of the parent actor profile
     * @param nestedSubjectAreaGUID            unique identifier of the nested actor profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSubjectAreas(String        userId,
                                   String        subjectAreaGUID,
                                   String        nestedSubjectAreaGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String methodName = "detachSubjectAreas";

        final String end1GUIDParameterName = "subjectAreaGUID";
        final String end2GUIDParameterName = "nestedSubjectAreaGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(subjectAreaGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedSubjectAreaGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName,
                                                        subjectAreaGUID,
                                                        nestedSubjectAreaGUID,
                                                        deleteOptions);
    }


    /**
     * Link a governance action to the element it is to work on (action target).
     *
     * @param userId                  userId of user making request
     * @param governanceActionGUID        unique identifier of the governance action
     * @param elementGUID             unique identifier of the target
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkTargetForGovernanceAction(String                              userId,
                                              String                              governanceActionGUID,
                                              String                              elementGUID,
                                              MakeAnchorOptions                   makeAnchorOptions,
                                              TargetForGovernanceActionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "linkTargetForGovernanceAction";
        final String end1GUIDParameterName = "governanceActionGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceActionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                        governanceActionGUID,
                                                        elementGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a governance action from the element it is to work on (action target).
     *
     * @param userId                 userId of user making request.
     * @param governanceActionGUID        unique identifier of the governance action
     * @param elementGUID             unique identifier of the target
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachTargetForGovernanceAction(String        userId,
                                                String        governanceActionGUID,
                                                String        elementGUID,
                                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "detachTargetForGovernanceAction";

        final String end1GUIDParameterName = "governanceActionGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceActionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                        governanceActionGUID,
                                                        elementGUID,
                                                        deleteOptions);
    }



    /**
     * Link a governance action type to the governance engine that it is to call.
     *
     * @param userId                  userId of user making request
     * @param governanceActionTypeGUID        unique identifier of the governance action type
     * @param governanceEngineGUID             unique identifier of the governance engine to call
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkGovernanceActionExecutor(String                             userId,
                                             String                             governanceActionTypeGUID,
                                             String                             governanceEngineGUID,
                                             MakeAnchorOptions                  makeAnchorOptions,
                                             GovernanceActionExecutorProperties relationshipProperties) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        final String methodName = "linkGovernanceActionExecutor";
        final String end1GUIDParameterName = "governanceActionTypeGUID";
        final String end2GUIDParameterName = "governanceEngineGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceActionTypeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(governanceEngineGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName,
                                                        governanceActionTypeGUID,
                                                        governanceEngineGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a governance action type from the governance engine that it is to call.
     *
     * @param userId                 userId of user making request.
     * @param governanceActionTypeGUID        unique identifier of the governance action type
     * @param governanceEngineGUID             unique identifier of the governance engine to call
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachGovernanceActionExecutor(String        userId,
                                               String        governanceActionTypeGUID,
                                               String        governanceEngineGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "detachGovernanceActionExecutor";

        final String end1GUIDParameterName = "governanceActionTypeGUID";
        final String end2GUIDParameterName = "governanceEngineGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceActionTypeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(governanceEngineGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName,
                                                        governanceActionTypeGUID,
                                                        governanceEngineGUID,
                                                        deleteOptions);
    }



    /**
     * Link a governance action process to the first step in the process.
     *
     * @param userId                  userId of user making request
     * @param governanceActionProcessGUID        unique identifier of the governance action process
     * @param firstProcessStepGUID             unique identifier of the first step in the process
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkGovernanceActionProcessFlow(String                                userId,
                                                String                                governanceActionProcessGUID,
                                                String                                firstProcessStepGUID,
                                                MakeAnchorOptions                     makeAnchorOptions,
                                                GovernanceActionProcessFlowProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                     PropertyServerException,
                                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "linkGovernanceActionProcessFlow";
        final String end1GUIDParameterName = "governanceActionProcessGUID";
        final String end2GUIDParameterName = "firstProcessStepGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceActionProcessGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(firstProcessStepGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName,
                                                        governanceActionProcessGUID,
                                                        firstProcessStepGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a governance action process from the first step in the process.
     *
     * @param userId                 userId of user making request.
     * @param governanceActionProcessGUID        unique identifier of the governance action process
     * @param firstProcessStepGUID             unique identifier of the first step in the process
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachGovernanceActionProcessFlow(String        userId,
                                                  String        governanceActionProcessGUID,
                                                  String        firstProcessStepGUID,
                                                  DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "detachGovernanceActionProcessFlow";

        final String end1GUIDParameterName = "governanceActionProcessGUID";
        final String end2GUIDParameterName = "firstProcessStepGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceActionProcessGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(firstProcessStepGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName,
                                                        governanceActionProcessGUID,
                                                        firstProcessStepGUID,
                                                        deleteOptions);
    }


    /**
     * Create a link between a governance action process step and its follow-on process step.
     *
     * @param userId calling user
     * @param processStepGUID unique identifier of the element
     * @param nextProcessStepGUID unique identifier of the license type
     * @param properties   additional information, endorsements etc
     * @param makeAnchorOptions  options to control access to open metadata
     *
     * @return guid of license relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String linkNextProcessStep(String                                    userId,
                                      String                                    processStepGUID,
                                      String                                    nextProcessStepGUID,
                                      MakeAnchorOptions                         makeAnchorOptions,
                                      NextGovernanceActionProcessStepProperties properties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_RELATIONSHIP.typeName,
                                                               processStepGUID,
                                                               nextProcessStepGUID,
                                                               makeAnchorOptions,
                                                               relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Update the relationship between two governance action process steps.
     *
     * @param userId calling user
     * @param relationshipGUID unique identifier for the relationship
     * @param updateOptions options for the request
     * @param properties properties of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public void updateNextProcessStep(String                                    userId,
                                      String                                    relationshipGUID,
                                      UpdateOptions                             updateOptions,
                                      NextGovernanceActionProcessStepProperties properties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        openMetadataClient.updateRelationshipInStore(userId,
                                                     relationshipGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove a relationship between two governance action process steps.
     *
     * @param userId calling user
     * @param relationshipGUID unique identifier of the relationship
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public void detachNextProcessStep(String        userId,
                                      String        relationshipGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        openMetadataClient.deleteRelationshipInStore(userId, relationshipGUID, deleteOptions);
    }


    /**
     * Create a link between a license type and an element that has achieved the license.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param licenseTypeGUID unique identifier of the license type
     * @param properties   additional information, endorsements etc
     * @param makeAnchorOptions  options to control access to open metadata
     *
     * @return guid of license relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String licenseElement(String            userId,
                                 String            elementGUID,
                                 String            licenseTypeGUID,
                                 MakeAnchorOptions makeAnchorOptions,
                                 LicenseProperties properties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               OpenMetadataType.LICENSE_RELATIONSHIP.typeName,
                                                               elementGUID,
                                                               licenseTypeGUID,
                                                               makeAnchorOptions,
                                                               relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Update the license relationship.
     *
     * @param userId calling user
     * @param licenseGUID unique identifier for the relationship
     * @param updateOptions options for the request
     * @param properties properties of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public void updateLicense(String            userId,
                              String            licenseGUID,
                              UpdateOptions     updateOptions,
                              LicenseProperties properties) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        openMetadataClient.updateRelationshipInStore(userId,
                                                     licenseGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove a relationship between two definitions.
     *
     * @param userId calling user
     * @param licenseGUID unique identifier of the license relationship
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public void unlicenseElement(String        userId,
                                 String        licenseGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        openMetadataClient.deleteRelationshipInStore(userId, licenseGUID, deleteOptions);
    }


    /**
     * Create a link between a certification type and an element that has achieved the certification.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param certificationTypeGUID unique identifier of the certification type
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties additional information, endorsements etc
     *
     * @return guid of certification relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String certifyElement(String                  userId,
                                 String                  elementGUID,
                                 String                  certificationTypeGUID,
                                 MakeAnchorOptions       metadataSourceOptions,
                                 CertificationProperties properties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName,
                                                               elementGUID,
                                                               certificationTypeGUID,
                                                               metadataSourceOptions,
                                                               relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Update the certification relationship.
     *
     * @param userId calling user
     * @param certificationGUID unique identifier for the relationship
     * @param properties additional information, endorsements etc
     * @param updateOptions provides a structure for the additional options when updating a relationship.
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public void updateCertification(String                  userId,
                                    String                  certificationGUID,
                                    UpdateOptions           updateOptions,
                                    CertificationProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        openMetadataClient.updateRelationshipInStore(userId,
                                                     certificationGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove a certification relationship.
     *
     * @param userId calling user
     * @param certificationGUID unique identifier of the certification relationship
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public void decertifyElement(String        userId,
                                 String        certificationGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        openMetadataClient.deleteRelationshipInStore(userId, certificationGUID, deleteOptions);
    }



    /**
     * Delete a governance definition.
     *
     * @param userId                 userId of user making request.
     * @param governanceDefinitionGUID      unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteGovernanceDefinition(String        userId,
                                           String        governanceDefinitionGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "deleteGovernanceDefinition";
        final String guidParameterName = "governanceDefinitionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(governanceDefinitionGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, governanceDefinitionGUID, deleteOptions);
    }


    /**
     * Returns the list of governance definitions with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getGovernanceDefinitionsByName(String       userId,
                                                                        String       name,
                                                                        QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        final String methodName = "getGovernanceDefinitionsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Retrieve the list of governance definitions metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findGovernanceDefinitions(String        userId,
                                                                   String        searchString,
                                                                   SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "findGovernanceDefinitions";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Return the properties of a specific governance definition.
     *
     * @param userId                 userId of user making request
     * @param governanceDefinitionGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getGovernanceDefinitionByGUID(String     userId,
                                                                 String     governanceDefinitionGUID,
                                                                 GetOptions getOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "getGovernanceDefinitionByGUID";

        return super.getRootElementByGUID(userId, governanceDefinitionGUID, getOptions, methodName);
    }


    /**
     * Attach a design object such as a solution component or governance definition to its implementation via the ImplementedBy relationship.
     *
     * @param userId                  userId of user making request
     * @param designGUID              unique identifier of the  governance definition or solution component etc
     * @param implementationGUID      unique identifier of the implementation
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  additional properties for the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDesignToImplementation(String                  userId,
                                           String                  designGUID,
                                           String                  implementationGUID,
                                           MakeAnchorOptions       makeAnchorOptions,
                                           ImplementedByProperties relationshipProperties) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "linkDesignToImplementation";
        final String end1GUIDParameterName = "designGUID";
        final String end2GUIDParameterName = "implementationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(designGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(implementationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                        designGUID,
                                                        implementationGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a design object such as a solution component or governance definition from its implementation.
     *
     * @param userId                 userId of user making request.
     * @param designGUID             unique identifier of the  governance definition, solution component etc
     * @param implementationGUID     unique identifier of the implementation
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDesignFromImplementation(String        userId,
                                               String        designGUID,
                                               String        implementationGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "detachDefinitionImplementation";

        final String end1GUIDParameterName = "designGUID";
        final String end2GUIDParameterName = "implementationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(designGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(implementationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                        designGUID,
                                                        implementationGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a design object such as a solution component or governance definition to one of its implementation resources via the ImplementationResource relationship.
     *
     * @param userId                  userId of user making request
     * @param designGUID              unique identifier of the  governance definition or solution component etc
     * @param implementationResourceGUID      unique identifier of the implementation
     * @param relationshipProperties  additional properties for the relationship.
     * @param makeAnchorOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkImplementationResource(String                           userId,
                                           String                           designGUID,
                                           String                           implementationResourceGUID,
                                           MakeAnchorOptions                makeAnchorOptions,
                                           ImplementationResourceProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName = "linkImplementationResource";
        final String end1GUIDParameterName = "designGUID";
        final String end2GUIDParameterName = "implementationResourceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(designGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(implementationResourceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName,
                                                        designGUID,
                                                        implementationResourceGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a design object such as a solution component or governance definition from one of its implementation resources.
     *
     * @param userId                 userId of user making request.
     * @param designGUID             unique identifier of the  governance definition, solution component etc
     * @param implementationResourceGUID     unique identifier of the implementation
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachImplementationResource(String        userId,
                                             String        designGUID,
                                             String        implementationResourceGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "detachImplementationResource";

        final String end1GUIDParameterName = "designGUID";
        final String end2GUIDParameterName = "implementationResourceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(designGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(implementationResourceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName,
                                                        designGUID,
                                                        implementationResourceGUID,
                                                        deleteOptions);
    }
}
