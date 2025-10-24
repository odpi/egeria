/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementationResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with governance definition elements.
 */
public class GovernanceDefinitionClient extends ConnectorContextClientBase
{
    protected final GovernanceDefinitionHandler governanceDefinitionHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public GovernanceDefinitionClient(ConnectorContextBase     parentContext,
                                      String                   localServerName,
                                      String                   localServiceName,
                                      String                   connectorUserId,
                                      String                   connectorGUID,
                                      String                   externalSourceGUID,
                                      String                   externalSourceName,
                                      OpenMetadataClient       openMetadataClient,
                                      AuditLog                 auditLog,
                                      int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.governanceDefinitionHandler = new GovernanceDefinitionHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template client to copy
     * @param specificTypeName type name override
     */
    public GovernanceDefinitionClient(GovernanceDefinitionClient template,
                                      String                     specificTypeName)
    {
        super(template);

        this.governanceDefinitionHandler = new GovernanceDefinitionHandler(template.governanceDefinitionHandler, specificTypeName);
    }


    /**
     * Create a new governance definition.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createGovernanceDefinition(NewElementOptions                     newElementOptions,
                                             Map<String, ClassificationProperties> initialClassifications,
                                             GovernanceDefinitionProperties        properties,
                                             RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                        PropertyServerException,
                                                                                                                        UserNotAuthorizedException
    {
        String elementGUID = governanceDefinitionHandler.createGovernanceDefinition(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a governance definition using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new governance definition.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing governance definition to copy (this will copy all the attachments such as nested content, schema
     *                     governance definition etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGovernanceDefinitionFromTemplate(TemplateOptions        templateOptions,
                                                         String                 templateGUID,
                                                         ElementProperties      replacementProperties,
                                                         Map<String, String>    placeholderProperties,
                                                         RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        String elementGUID = governanceDefinitionHandler.createGovernanceDefinitionFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a governance definition.
     *
     * @param governanceDefinitionGUID       unique identifier of the governance definition (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateGovernanceDefinition(String                         governanceDefinitionGUID,
                                           UpdateOptions                  updateOptions,
                                           GovernanceDefinitionProperties properties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        governanceDefinitionHandler.updateGovernanceDefinition(connectorUserId, governanceDefinitionGUID, updateOptions, properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(governanceDefinitionGUID);
        }
    }


    /**
     * Attach two peer governance definitions.
     *
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  additional properties for the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeerDefinitions(String                    governanceDefinitionOneGUID,
                                    String                    governanceDefinitionTwoGUID,
                                    String                    relationshipTypeName,
                                    MetadataSourceOptions     metadataSourceOptions,
                                    PeerDefinitionProperties  relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        governanceDefinitionHandler.linkPeerDefinitions(connectorUserId,
                                                        governanceDefinitionOneGUID,
                                                        governanceDefinitionTwoGUID,
                                                        relationshipTypeName,
                                                        metadataSourceOptions,
                                                        relationshipProperties);
    }


    /**
     * Detach a governance definition from one of its peers.
     *
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPeerDefinitions(String        governanceDefinitionOneGUID,
                                      String        governanceDefinitionTwoGUID,
                                      String        relationshipTypeName,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        governanceDefinitionHandler.detachPeerDefinitions(connectorUserId,
                                                          governanceDefinitionOneGUID,
                                                          governanceDefinitionTwoGUID,
                                                          relationshipTypeName,
                                                          deleteOptions);
    }


    /**
     * Attach a supporting governance definition.
     *
     * @param governanceDefinitionOneGUID unique identifier of the parent governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the child governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  additional properties for the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void attachSupportingDefinition(String                         governanceDefinitionOneGUID,
                                           String                         governanceDefinitionTwoGUID,
                                           String                         relationshipTypeName,
                                           MetadataSourceOptions          metadataSourceOptions,
                                           SupportingDefinitionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        governanceDefinitionHandler.attachSupportingDefinition(connectorUserId,
                                                               governanceDefinitionOneGUID,
                                                               governanceDefinitionTwoGUID,
                                                               relationshipTypeName,
                                                               metadataSourceOptions,
                                                               relationshipProperties);

    }


    /**
     * Detach a governance definition from a supporting governance definition.
     *
     * @param governanceDefinitionOneGUID unique identifier of the parent governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the child governance definition
     * @param relationshipTypeName name of the relationship to use
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSupportingDefinition(String        governanceDefinitionOneGUID,
                                           String        governanceDefinitionTwoGUID,
                                           String        relationshipTypeName,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        governanceDefinitionHandler.detachSupportingDefinition(connectorUserId,
                                                               governanceDefinitionOneGUID,
                                                               governanceDefinitionTwoGUID,
                                                               relationshipTypeName,
                                                               deleteOptions);
    }



    /**
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param elementGUID unique identifier of the metadata element to link
     * @param definitionGUID identifier of the governance definition to link
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addGovernanceDefinitionToElement(String                elementGUID,
                                                 String                definitionGUID,
                                                 MetadataSourceOptions metadataSourceOptions,
                                                 GovernedByProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        governanceDefinitionHandler.addGovernanceDefinitionToElement(connectorUserId,
                                                                     elementGUID,
                                                                     definitionGUID,
                                                                     metadataSourceOptions,
                                                                     properties);
    }


    /**
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param definitionGUID identifier of the governance definition to link
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGovernanceDefinitionFromElement(String        elementGUID,
                                                      String        definitionGUID,
                                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        governanceDefinitionHandler.removeGovernanceDefinitionFromElement(connectorUserId, elementGUID, definitionGUID, deleteOptions);
    }


    /**
     * Attach an actor to an element that describes its scope.
     *
     * @param scopeElementGUID            unique identifier of the element
     * @param actorGUID unique identifier of the actor
     * @param metadataSourceOptions         options to control access to open metadata
     * @param relationshipProperties        description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAssignmentScope(String                    scopeElementGUID,
                                    String                    actorGUID,
                                    MetadataSourceOptions     metadataSourceOptions,
                                    AssignmentScopeProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        governanceDefinitionHandler.linkAssignmentScope(connectorUserId,
                                                        actorGUID,
                                                        scopeElementGUID,
                                                        metadataSourceOptions,
                                                        relationshipProperties);
    }


    /**
     * Detach an actor from the element that describes its scope.
     *
     * @param scopeElementGUID            unique identifier of the element
     * @param actorGUID                   unique identifier of the actor
     * @param deleteOptions               options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAssignmentScope(String        scopeElementGUID,
                                      String        actorGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        governanceDefinitionHandler.detachAssignmentScope(connectorUserId, actorGUID, scopeElementGUID, deleteOptions);
    }


    /**
     * Attach monitored resource to a notification type.
     *
     * @param notificationTypeGUID            unique identifier of the notification type
     * @param elementGUID             unique identifier of the element to monitor
     * @param metadataSourceOptions         options to control access to open metadata
     * @param relationshipProperties        description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkMonitoredResource(String                      notificationTypeGUID,
                                      String                      elementGUID,
                                      MetadataSourceOptions       metadataSourceOptions,
                                      MonitoredResourceProperties relationshipProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        governanceDefinitionHandler.linkMonitoredResource(connectorUserId, notificationTypeGUID, elementGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a monitored resource from a notification type.
     *
     * @param notificationTypeGUID            unique identifier of the notification type
     * @param elementGUID             unique identifier of the element to monitor
     * @param deleteOptions               options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachMonitoredResource(String        notificationTypeGUID,
                                        String        elementGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        governanceDefinitionHandler.detachMonitoredResource(connectorUserId, notificationTypeGUID, elementGUID, deleteOptions);
    }


    /**
     * Attach subscriber to a notification type.
     *
     * @param notificationTypeGUID            unique identifier of the notification type
     * @param elementGUID             unique identifier of the element to monitor
     * @param metadataSourceOptions         options to control access to open metadata
     * @param relationshipProperties        description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNotificationSubscriber(String                           notificationTypeGUID,
                                           String                           elementGUID,
                                           MetadataSourceOptions            metadataSourceOptions,
                                           NotificationSubscriberProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        governanceDefinitionHandler.linkNotificationSubscriber(connectorUserId, notificationTypeGUID, elementGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a subscriber from a notification type.
     *
     * @param notificationTypeGUID            unique identifier of the notification type
     * @param elementGUID             unique identifier of the element to monitor
     * @param deleteOptions               options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNotificationSubscriber(String        notificationTypeGUID,
                                             String        elementGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        governanceDefinitionHandler.detachNotificationSubscriber(connectorUserId, notificationTypeGUID, elementGUID, deleteOptions);
    }



    /**
     * Attach a governance metric to an asset that represents the data store where the measurements are located.
     *
     * @param governanceMetricGUID            unique identifier of the metric
     * @param dataSourceGUID unique identifier of the asset
     * @param metadataSourceOptions         options to control access to open metadata
     * @param relationshipProperties        description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkGovernanceResults(String                      governanceMetricGUID,
                                      String                      dataSourceGUID,
                                      MetadataSourceOptions       metadataSourceOptions,
                                      GovernanceResultsProperties relationshipProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        governanceDefinitionHandler.linkGovernanceResults(connectorUserId,
                                                          dataSourceGUID,
                                                          governanceMetricGUID,
                                                          metadataSourceOptions,
                                                          relationshipProperties);
    }


    /**
     * Detach a governance metric from an asset that represents the data store where the measurements are located.
     *
     * @param governanceMetricGUID            unique identifier of the metric
     * @param dataSourceGUID                   unique identifier of the asset
     * @param deleteOptions               options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachGovernanceResults(String        governanceMetricGUID,
                                        String        dataSourceGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        governanceDefinitionHandler.detachGovernanceResults(connectorUserId, dataSourceGUID, governanceMetricGUID, deleteOptions);
    }


    /**
     * Attach governance zones in a hierarchy.
     *
     * @param governanceZoneGUID        unique identifier of the parent
     * @param nestedGovernanceZoneGUID             unique identifier of the actor profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkGovernanceZones(String                  governanceZoneGUID,
                                    String                  nestedGovernanceZoneGUID,
                                    MetadataSourceOptions   metadataSourceOptions,
                                    ZoneHierarchyProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        governanceDefinitionHandler.linkGovernanceZones(connectorUserId,
                                                        governanceZoneGUID,
                                                        nestedGovernanceZoneGUID,
                                                        metadataSourceOptions,
                                                        relationshipProperties);
    }


    /**
     * Detach governance zone from a hierarchical relationship.
     *
     * @param governanceZoneGUID       unique identifier of the parent actor profile
     * @param nestedGovernanceZoneGUID            unique identifier of the nested actor profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachGovernanceZones(String        governanceZoneGUID,
                                      String        nestedGovernanceZoneGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        governanceDefinitionHandler.detachGovernanceZones(connectorUserId, governanceZoneGUID, nestedGovernanceZoneGUID, deleteOptions);
    }


    /**
     * Link subject area definitions in a hierarchy.
     *
     * @param subjectAreaGUID        unique identifier of the parent
     * @param nestedSubjectAreaGUID             unique identifier of the actor profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSubjectAreas(String                         subjectAreaGUID,
                                 String                         nestedSubjectAreaGUID,
                                 MetadataSourceOptions          metadataSourceOptions,
                                 SubjectAreaHierarchyProperties relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        governanceDefinitionHandler.linkSubjectAreas(connectorUserId,
                                                     subjectAreaGUID,
                                                     nestedSubjectAreaGUID,
                                                     metadataSourceOptions,
                                                     relationshipProperties);
    }


    /**
     * Detach subject area definitions from their hierarchical relationship..
     *
     * @param subjectAreaGUID       unique identifier of the parent actor profile
     * @param nestedSubjectAreaGUID            unique identifier of the nested actor profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSubjectAreas(String        subjectAreaGUID,
                                   String        nestedSubjectAreaGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        governanceDefinitionHandler.detachSubjectAreas(connectorUserId,
                                                       subjectAreaGUID,
                                                       nestedSubjectAreaGUID,
                                                       deleteOptions);
    }



    /**
     * Classify the element to assert that the definitions it represents are part of a subject area definition.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param properties qualified name of subject area
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addElementToSubjectArea(String                elementGUID,
                                        SubjectAreaProperties properties,
                                        MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        governanceDefinitionHandler.addElementToSubjectArea(connectorUserId, elementGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the subject area designation from the identified element.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeElementFromSubjectArea(String                elementGUID,
                                             MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        governanceDefinitionHandler.removeElementFromSubjectArea(connectorUserId, elementGUID, metadataSourceOptions);
    }



    /**
     * Create a link between a license type and an element that has achieved the license.
     *
     * @param elementGUID unique identifier of the element
     * @param licenseTypeGUID unique identifier of the license type
     * @param properties   additional information, endorsements etc
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @return guid of license relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String licenseElement(String                elementGUID,
                                 String                licenseTypeGUID,
                                 MetadataSourceOptions metadataSourceOptions,
                                 LicenseProperties     properties) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return governanceDefinitionHandler.licenseElement(connectorUserId,
                                                          elementGUID,
                                                          licenseTypeGUID,
                                                          metadataSourceOptions,
                                                          properties);
    }


    /**
     * Update the license relationship.
     *
     * @param licenseGUID unique identifier for the relationship
     * @param updateOptions options for the request
     * @param properties properties of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateLicense(String            licenseGUID,
                              UpdateOptions     updateOptions,
                              LicenseProperties properties) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        governanceDefinitionHandler.updateLicense(connectorUserId, licenseGUID, updateOptions, properties);
    }


    /**
     * Remove a relationship between two definitions.
     *
     * @param licenseGUID unique identifier of the license relationship
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void unlicenseElement(String        licenseGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        governanceDefinitionHandler.unlicenseElement(connectorUserId, licenseGUID, deleteOptions);
    }


    /**
     * Create a link between a certification type and an element that has achieved the certification.
     *
     * @param elementGUID unique identifier of the element
     * @param certificationTypeGUID unique identifier of the certification type
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties additional information, endorsements etc
     *
     * @return guid of certification relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String certifyElement(String                  elementGUID,
                                 String                  certificationTypeGUID,
                                 MetadataSourceOptions   metadataSourceOptions,
                                 CertificationProperties properties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return governanceDefinitionHandler.certifyElement(connectorUserId,
                                                          elementGUID,
                                                          certificationTypeGUID,
                                                          metadataSourceOptions,
                                                          properties);
    }


    /**
     * Update the certification relationship.
     *
     * @param certificationGUID unique identifier for the relationship
     * @param properties additional information, endorsements etc
     * @param updateOptions provides a structure for the additional options when updating a relationship.
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateCertification(String                  certificationGUID,
                                    UpdateOptions           updateOptions,
                                    CertificationProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        governanceDefinitionHandler.updateCertification(connectorUserId, certificationGUID, updateOptions, properties);
    }


    /**
     * Remove a certification relationship.
     *
     * @param certificationGUID unique identifier of the certification relationship
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void decertifyElement(String        certificationGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        governanceDefinitionHandler.decertifyElement(connectorUserId, certificationGUID, deleteOptions);
    }




    /**
     * Attach a design object such as a solution component or governance definition to its implementation via the ImplementedBy relationship.
     *
     * @param designGUID              unique identifier of the  governance definition or solution component etc
     * @param implementationGUID      unique identifier of the implementation
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  additional properties for the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDesignToImplementation(String                  designGUID,
                                           String                  implementationGUID,
                                           MetadataSourceOptions   metadataSourceOptions,
                                           ImplementedByProperties relationshipProperties) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        governanceDefinitionHandler.linkDesignToImplementation(connectorUserId, designGUID, implementationGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a design object such as a solution component or governance definition from its implementation.
     *
     * @param designGUID             unique identifier of the  governance definition, solution component etc
     * @param implementationGUID     unique identifier of the implementation
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDesignFromImplementation(String        designGUID,
                                               String        implementationGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        governanceDefinitionHandler.detachDesignFromImplementation(connectorUserId, designGUID, implementationGUID, deleteOptions);
    }


    /**
     * Attach a design object such as a solution component or governance definition to one of its implementation resources via the ImplementationResource relationship.
     *
     * @param designGUID              unique identifier of the  governance definition or solution component etc
     * @param implementationResourceGUID      unique identifier of the implementation
     * @param relationshipProperties  additional properties for the relationship.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkImplementationResource(String                           designGUID,
                                           String                           implementationResourceGUID,
                                           MetadataSourceOptions            metadataSourceOptions,
                                           ImplementationResourceProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        governanceDefinitionHandler.linkImplementationResource(connectorUserId, designGUID, implementationResourceGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a design object such as a solution component or governance definition from one of its implementation resources.
     *
     * @param designGUID             unique identifier of the  governance definition, solution component etc
     * @param implementationResourceGUID     unique identifier of the implementation
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachImplementationResource(String        designGUID,
                                             String        implementationResourceGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        governanceDefinitionHandler.detachImplementationResource(connectorUserId, designGUID, implementationResourceGUID, deleteOptions);
    }



    /**
     * Delete a governance definition.
     *
     * @param governanceDefinitionGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteGovernanceDefinition(String        governanceDefinitionGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        governanceDefinitionHandler.deleteGovernanceDefinition(connectorUserId, governanceDefinitionGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(governanceDefinitionGUID);
        }
    }


    /**
     * Returns the list of governance definitions with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getGovernanceDefinitionsByName(String       name,
                                                                        QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        return governanceDefinitionHandler.getGovernanceDefinitionsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific governance definition.
     *
     * @param governanceDefinitionGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getGovernanceDefinitionByGUID(String     governanceDefinitionGUID,
                                                                 GetOptions getOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        return governanceDefinitionHandler.getGovernanceDefinitionByGUID(connectorUserId, governanceDefinitionGUID, getOptions);
    }


    /**
     * Retrieve the list of governance definitions metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned governance definitions include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findGovernanceDefinitions(String        searchString,
                                                                   SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        return governanceDefinitionHandler.findGovernanceDefinitions(connectorUserId, searchString, searchOptions);
    }
}
