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
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ContextEvent handler describes how to maintain and query context events and the relationships used to associate
 * then with elements that were affected..
 */
public class ContextEventHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public ContextEventHandler(String             localServerName,
                               AuditLog           auditLog,
                               String             serviceName,
                               OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName,openMetadataClient, OpenMetadataType.CONTEXT_EVENT.typeName);
    }


    /**
     * Create a new context event.
     *
     * @param userId    calling user
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createContextEvent(String                                userId,
                                     NewElementOptions                     newElementOptions,
                                     Map<String, ClassificationProperties> initialClassifications,
                                     ContextEventProperties                properties,
                                     RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        final String methodName = "createContextEvent";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new context event
     *
     * @param userId calling user
     * @param anchorGUID unique identifier for the context event's anchor element
     * @param parentContextEvents which context events should be linked as parents (guid->relationship properties)
     * @param childContextEvents which context events should be linked as children (guid->relationship properties)
     * @param relatedContextEvents which context events should be linked as related (guid->relationship properties)
     * @param impactedElements which elements are impacted by this context event (guid->relationship properties)
     * @param effectedDataResourceGUIDs which data resources are effected by this context event (asset guid->relationship properties)
     * @param contextEventEvidenceGUIDs which elements provide evidence that the context event is happening (element GUIDs)
     * @param contextEventProperties properties for the context event itself
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @return guid of the new context event
     * @throws InvalidParameterException one of the properties are invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException a problem connecting to (or inside) the metadata store
     */
    public String registerContextEvent(String                                       userId,
                                       String                                       anchorGUID,
                                       Map<String, ClassificationProperties>        initialClassifications,
                                       ContextEventProperties                       contextEventProperties,
                                       Map<String, DependentContextEventProperties> parentContextEvents,
                                       Map<String, DependentContextEventProperties> childContextEvents,
                                       Map<String, RelatedContextEventProperties>   relatedContextEvents,
                                       Map<String, ContextEventImpactProperties>    impactedElements,
                                       Map<String, RelationshipProperties>          effectedDataResourceGUIDs,
                                       Map<String, RelationshipProperties>          contextEventEvidenceGUIDs) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        final String methodName = "registerContextEvent";
        final String qualifiedNameParameterName = "qualifiedName";

        /*
         * The qualified name is needed.
         */
        if (contextEventProperties == null)
        {
            propertyHelper.validateMandatoryName(null, qualifiedNameParameterName, methodName);
            // not reached
            return null;
        }
        else
        {
            propertyHelper.validateMandatoryName(contextEventProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

            /*
             * Set up the API options
             */
            MakeAnchorOptions metadataSourceOptions = new MakeAnchorOptions();
            metadataSourceOptions.setEffectiveTime(new Date());
            metadataSourceOptions.setForLineage(true);
            metadataSourceOptions.setMakeAnchor(false);

            /*
             * Create the to do entity
             */
            ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            contextEventProperties.getQualifiedName());

            properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, contextEventProperties.getDisplayName());
            properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, contextEventProperties.getDescription());
            properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.CATEGORY.name, contextEventProperties.getCategory());
            properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.EVENT_EFFECT.name, contextEventProperties.getEventEffect());
            properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.PLANNED_START_DATE.name, contextEventProperties.getPlannedStartDate());
            properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.ACTUAL_START_DATE.name, contextEventProperties.getActualStartDate());
            properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.PLANNED_COMPLETION_DATE.name, contextEventProperties.getPlannedCompletionDate());
            properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.ACTUAL_COMPLETION_DATE.name, contextEventProperties.getActualCompletionDate());
            properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.REFERENCE_EFFECTIVE_FROM.name, contextEventProperties.getReferenceEffectiveFrom());
            properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.REFERENCE_EFFECTIVE_TO.name, contextEventProperties.getReferenceEffectiveTo());
            properties = propertyHelper.addLongProperty(properties, OpenMetadataProperty.PLANNED_DURATION.name, contextEventProperties.getPlannedDuration());
            properties = propertyHelper.addLongProperty(properties, OpenMetadataProperty.ACTUAL_DURATION.name, contextEventProperties.getActualDuration());
            properties = propertyHelper.addLongProperty(properties, OpenMetadataProperty.REPEAT_INTERVAL.name, contextEventProperties.getRepeatInterval());
            properties = propertyHelper.addStringMapProperty(properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, contextEventProperties.getAdditionalProperties());

            NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);
            newElementOptions.setAnchorGUID(anchorGUID);
            newElementOptions.setIsOwnAnchor((anchorGUID == null));

            NewElementProperties newElementProperties = new NewElementProperties(properties);
            newElementProperties.setEffectiveFrom(contextEventProperties.getEffectiveFrom());
            newElementProperties.setEffectiveTo(contextEventProperties.getEffectiveTo());

            String contextEventGUID = openMetadataClient.createMetadataElementInStore(userId,
                                                                                      OpenMetadataType.CONTEXT_EVENT.typeName,
                                                                                      newElementOptions,
                                                                                      classificationBuilder.getInitialClassifications(initialClassifications),
                                                                                      newElementProperties,
                                                                                      null);

            if (contextEventGUID != null)
            {
                if (parentContextEvents != null)
                {
                    for (String guid : parentContextEvents.keySet())
                    {
                        if (guid != null)
                        {
                            DependentContextEventProperties suppliedRelationshipProperties = parentContextEvents.get(guid);

                            if (suppliedRelationshipProperties != null)
                            {
                                ElementProperties elementProperties = relationshipBuilder.getElementProperties(suppliedRelationshipProperties);

                                NewElementProperties relationshipProperties = new NewElementProperties(elementProperties);
                                relationshipProperties.setEffectiveFrom(suppliedRelationshipProperties.getEffectiveFrom());
                                relationshipProperties.setEffectiveTo(suppliedRelationshipProperties.getEffectiveTo());

                                openMetadataClient.createRelatedElementsInStore(userId,
                                                                                OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                                guid,
                                                                                contextEventGUID,
                                                                                metadataSourceOptions,
                                                                                relationshipProperties);
                            }
                            else
                            {
                                openMetadataClient.createRelatedElementsInStore(userId,
                                                                                OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                                guid,
                                                                                contextEventGUID,
                                                                                metadataSourceOptions,
                                                                                null);
                            }
                        }
                    }
                }

                if (childContextEvents != null)
                {
                    for (String guid : childContextEvents.keySet())
                    {
                        if (guid != null)
                        {
                            DependentContextEventProperties suppliedRelationshipProperties = childContextEvents.get(guid);

                            if (suppliedRelationshipProperties != null)
                            {
                                ElementProperties elementProperties = relationshipBuilder.getElementProperties(suppliedRelationshipProperties);

                                NewElementProperties relationshipProperties = new NewElementProperties(elementProperties);
                                relationshipProperties.setEffectiveFrom(suppliedRelationshipProperties.getEffectiveFrom());
                                relationshipProperties.setEffectiveTo(suppliedRelationshipProperties.getEffectiveTo());

                                openMetadataClient.createRelatedElementsInStore(userId,
                                                                                OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                                contextEventGUID,
                                                                                guid,
                                                                                metadataSourceOptions,
                                                                                relationshipProperties);
                            }
                            else
                            {
                                openMetadataClient.createRelatedElementsInStore(userId,
                                                                                OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                                contextEventGUID,
                                                                                guid,
                                                                                metadataSourceOptions,
                                                                                null);
                            }
                        }
                    }
                }

                if (relatedContextEvents != null)
                {
                    for (String guid : relatedContextEvents.keySet())
                    {
                        if (guid != null)
                        {
                            RelatedContextEventProperties suppliedRelationshipProperties = relatedContextEvents.get(guid);

                            if (suppliedRelationshipProperties != null)
                            {
                                ElementProperties elementProperties = relationshipBuilder.getElementProperties(suppliedRelationshipProperties);

                                NewElementProperties relationshipProperties = new NewElementProperties(elementProperties);
                                relationshipProperties.setEffectiveFrom(suppliedRelationshipProperties.getEffectiveFrom());
                                relationshipProperties.setEffectiveTo(suppliedRelationshipProperties.getEffectiveTo());

                                openMetadataClient.createRelatedElementsInStore(userId,
                                                                                OpenMetadataType.RELATED_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                                guid,
                                                                                contextEventGUID,
                                                                                metadataSourceOptions,
                                                                                relationshipProperties);
                            }
                            else
                            {
                                openMetadataClient.createRelatedElementsInStore(userId,
                                                                                OpenMetadataType.RELATED_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                                guid,
                                                                                contextEventGUID,
                                                                                metadataSourceOptions,
                                                                                null);
                            }
                        }
                    }
                }

                if (impactedElements != null)
                {
                    for (String guid : impactedElements.keySet())
                    {
                        if (guid != null)
                        {
                            ContextEventImpactProperties suppliedRelationshipProperties = impactedElements.get(guid);

                            if (suppliedRelationshipProperties != null)
                            {
                                ElementProperties elementProperties = relationshipBuilder.getElementProperties(suppliedRelationshipProperties);

                                NewElementProperties relationshipProperties = new NewElementProperties(elementProperties);
                                relationshipProperties.setEffectiveFrom(suppliedRelationshipProperties.getEffectiveFrom());
                                relationshipProperties.setEffectiveTo(suppliedRelationshipProperties.getEffectiveTo());

                                openMetadataClient.createRelatedElementsInStore(userId,
                                                                                OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP.typeName,
                                                                                guid,
                                                                                contextEventGUID,
                                                                                metadataSourceOptions,
                                                                                relationshipProperties);
                            }
                            else
                            {
                                openMetadataClient.createRelatedElementsInStore(userId,
                                                                                OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP.typeName,
                                                                                guid,
                                                                                contextEventGUID,
                                                                                metadataSourceOptions,
                                                                                null);
                            }
                        }
                    }
                }

                if (effectedDataResourceGUIDs != null)
                {
                    for (String guid : effectedDataResourceGUIDs.keySet())
                    {
                        if (guid != null)
                        {
                            RelationshipProperties suppliedRelationshipProperties = effectedDataResourceGUIDs.get(guid);

                            NewElementProperties relationshipProperties = relationshipBuilder.getNewElementProperties(suppliedRelationshipProperties);
                            relationshipProperties.setEffectiveFrom(suppliedRelationshipProperties.getEffectiveFrom());
                            relationshipProperties.setEffectiveTo(suppliedRelationshipProperties.getEffectiveTo());

                            openMetadataClient.createRelatedElementsInStore(userId,
                                                                            OpenMetadataType.CONTEXT_EVENT_FOR_TIMELINE_EFFECTS_RELATIONSHIP.typeName,
                                                                            guid,
                                                                            contextEventGUID,
                                                                            metadataSourceOptions,
                                                                            relationshipProperties);
                        }
                    }
                }

                if (contextEventEvidenceGUIDs != null)
                {
                    for (String guid : contextEventEvidenceGUIDs.keySet())
                    {
                        if (guid != null)
                        {
                            RelationshipProperties suppliedRelationshipProperties = contextEventEvidenceGUIDs.get(guid);

                            NewElementProperties relationshipProperties = relationshipBuilder.getNewElementProperties(suppliedRelationshipProperties);
                            relationshipProperties.setEffectiveFrom(suppliedRelationshipProperties.getEffectiveFrom());
                            relationshipProperties.setEffectiveTo(suppliedRelationshipProperties.getEffectiveTo());

                            openMetadataClient.createRelatedElementsInStore(userId,
                                                                            OpenMetadataType.CONTEXT_EVENT_EVIDENCE_RELATIONSHIP.typeName,
                                                                            contextEventGUID,
                                                                            guid,
                                                                            metadataSourceOptions,
                                                                            relationshipProperties);
                        }
                    }
                }
            }

            return contextEventGUID;
        }
    }


    /**
     * Create a new metadata element to represent a context event using an existing  element as a template.
     * The template defines additional classifications and relationships that should be added to the new context event.
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
    public String createContextEventFromTemplate(String                 userId,
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
     * Update the properties of a context event.
     *
     * @param userId                 userId of user making request.
     * @param contextEventGUID          unique identifier of the context event (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateContextEvent(String                 userId,
                                      String                 contextEventGUID,
                                      UpdateOptions          updateOptions,
                                      ContextEventProperties properties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "updateContextEvent";
        final String guidParameterName = "contextEventGUID";

        return super.updateElement(userId,
                                   contextEventGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Connect two context events to show that one is dependent on another.
     *
     * @param userId                 userId of user making request
     * @param parentContextEventGUID    unique identifier of the parent context event
     * @param childContextEventGUID     unique identifier of the child context event
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDependentContextEvents(String                         userId,
                                           String                         parentContextEventGUID,
                                           String                         childContextEventGUID,
                                           MakeAnchorOptions              makeAnchorOptions,
                                           DependentContextEventProperties relationshipProperties) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        final String methodName = "linkDependentContextEvents";
        final String end1GUIDParameterName = "parentContextEventGUID";
        final String end2GUIDParameterName = "childContextEventGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentContextEventGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(childContextEventGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                        parentContextEventGUID,
                                                        childContextEventGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach two dependent context events from one another.
     *
     * @param userId                 userId of user making request.
     * @param parentContextEventGUID    unique identifier of the  parent context event.
     * @param childContextEventGUID     unique identifier of the child context event.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDependentContextEvents(String        userId,
                                             String        parentContextEventGUID,
                                             String        childContextEventGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "detachDependentContextEvents";

        final String end1GUIDParameterName = "parentContextEventGUID";
        final String end2GUIDParameterName = "childContextEventGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentContextEventGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(childContextEventGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                        parentContextEventGUID,
                                                        childContextEventGUID,
                                                        deleteOptions);
    }


    /**
     * Connect two context events to show that one is related to the other.
     *
     * @param userId                 userId of user making request
     * @param contextEventOneGUID    unique identifier of the  context event at end 1
     * @param contextEventTwoGUID     unique identifier of the  context event at end 2
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkRelatedContextEvents(String                         userId,
                                         String                         contextEventOneGUID,
                                         String                         contextEventTwoGUID,
                                         MakeAnchorOptions              makeAnchorOptions,
                                         RelatedContextEventProperties relationshipProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "linkRelatedContextEvents";
        final String end1GUIDParameterName = "contextEventOneGUID";
        final String end2GUIDParameterName = "contextEventTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contextEventOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(contextEventTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.RELATED_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                        contextEventOneGUID,
                                                        contextEventTwoGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach two context events that are related to one another.
     *
     * @param userId                 userId of user making request.
     * @param contextEventOneGUID    unique identifier of the  context event at end 1
     * @param contextEventTwoGUID     unique identifier of the  context event at end 2
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachRelatedContextEvents(String        userId,
                                           String        contextEventOneGUID,
                                           String        contextEventTwoGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "detachRelatedContextEvents";

        final String end1GUIDParameterName = "contextEventOneGUID";
        final String end2GUIDParameterName = "contextEventTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contextEventOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(contextEventTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.RELATED_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                        contextEventOneGUID,
                                                        contextEventTwoGUID,
                                                        deleteOptions);
    }


    /**
     * Connect a context event to an element that provides evidence that this context event is real.
     *
     * @param userId                 userId of user making request
     * @param contextEventGUID    unique identifier of the context event
     * @param evidenceGUID     unique identifier of the element representing the evidence
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContextEventEvidence(String                         userId,
                                         String                         contextEventGUID,
                                         String                         evidenceGUID,
                                         MakeAnchorOptions              makeAnchorOptions,
                                         ContextEventEvidenceProperties relationshipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "linkContextEventEvidence";
        final String end1GUIDParameterName = "contextEventGUID";
        final String end2GUIDParameterName = "evidenceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contextEventGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(evidenceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTEXT_EVENT_EVIDENCE_RELATIONSHIP.typeName,
                                                        contextEventGUID,
                                                        evidenceGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a context event from an element that provides evidence that this context event is real.
     *
     * @param userId                 userId of user making request.
     * @param contextEventGUID    unique identifier of the context event
     * @param evidenceGUID     unique identifier of the element representing the evidence
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContextEventEvidence(String        userId,
                                           String        contextEventGUID,
                                           String        evidenceGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "detachContextEventEvidence";

        final String end1GUIDParameterName = "contextEventGUID";
        final String end2GUIDParameterName = "evidenceGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contextEventGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(evidenceGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTEXT_EVENT_EVIDENCE_RELATIONSHIP.typeName,
                                                        contextEventGUID,
                                                        evidenceGUID,
                                                        deleteOptions);
    }


    /**
     * Connect a context event to an element that is impacted by this event.
     *
     * @param userId                 userId of user making request
     * @param contextEventGUID    unique identifier of the context event
     * @param impactedElementGUID     unique identifier of the element that is impacted by the event
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContextEventImpact(String                       userId,
                                       String                       contextEventGUID,
                                       String                       impactedElementGUID,
                                       MakeAnchorOptions            makeAnchorOptions,
                                       ContextEventImpactProperties relationshipProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "linkContextEventImpact";
        final String end1GUIDParameterName = "contextEventGUID";
        final String end2GUIDParameterName = "impactedElementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contextEventGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(impactedElementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP.typeName,
                                                        impactedElementGUID,
                                                        contextEventGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a context event from an element that is impacted by the event.
     *
     * @param userId                 userId of user making request.
     * @param contextEventGUID    unique identifier of the context event
     * @param impactedElementGUID     unique identifier of the element that is impacted by the event
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContextEventImpact(String        userId,
                                         String        contextEventGUID,
                                         String        impactedElementGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "detachContextEventImpact";

        final String end1GUIDParameterName = "contextEventGUID";
        final String end2GUIDParameterName = "impactedElementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contextEventGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(impactedElementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP.typeName,
                                                        impactedElementGUID,
                                                        contextEventGUID,
                                                        deleteOptions);
    }


    /**
     * Connect a context event to an element whose data is impacted by this event.
     *
     * @param userId                 userId of user making request
     * @param contextEventGUID    unique identifier of the context event
     * @param timelineAffectedElementGUID     unique identifier of the element whose data is impacted by the context event
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContextEventTimelineEffect(String                                   userId,
                                               String                                   timelineAffectedElementGUID,
                                               String                                   contextEventGUID,
                                               MakeAnchorOptions                        makeAnchorOptions,
                                               ContextEventForTimelineEffectsProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName = "linkContextEventTimelineEffect";
        final String end1GUIDParameterName = "contextEventGUID";
        final String end2GUIDParameterName = "timelineAffectedElementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contextEventGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(timelineAffectedElementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTEXT_EVENT_FOR_TIMELINE_EFFECTS_RELATIONSHIP.typeName,
                                                        timelineAffectedElementGUID,
                                                        contextEventGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a context event from an element whose data is impacted by the event.
     *
     * @param userId                 userId of user making request.
     * @param timelineAffectedElementGUID     unique identifier of the element whose data is impacted by the context event
     * @param contextEventGUID    unique identifier of the context event
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContextEventTimelineEffect(String        userId,
                                                 String        timelineAffectedElementGUID,
                                                 String        contextEventGUID,
                                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "detachContextEventImpact";

        final String end1GUIDParameterName = "contextEventGUID";
        final String end2GUIDParameterName = "timelineAffectedElementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contextEventGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(timelineAffectedElementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTEXT_EVENT_FOR_TIMELINE_EFFECTS_RELATIONSHIP.typeName,
                                                        timelineAffectedElementGUID,
                                                        contextEventGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a context event.
     *
     * @param userId                 userId of user making request.
     * @param contextEventGUID          unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteContextEvent(String        userId,
                                   String        contextEventGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String methodName = "deleteContextEvent";
        final String guidParameterName = "contextEventGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(contextEventGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, contextEventGUID, deleteOptions);
    }


    /**
     * Returns the list of context events with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getContextEventsByName(String       userId,
                                                                String       name,
                                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "getContextEventsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific context event.
     *
     * @param userId                 userId of user making request
     * @param contextEventGUID          unique identifier of the required element
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getContextEventByGUID(String     userId,
                                                         String     contextEventGUID,
                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getContextEventByGUID";

        return super.getRootElementByGUID(userId, contextEventGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of context events metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findContextEvents(String        userId,
                                                           String        searchString,
                                                           SearchOptions searchOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "findContextEvents";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
