/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ContextEventHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with context event elements.
 */
public class ContextEventClient extends ConnectorContextClientBase
{
    private final ContextEventHandler contextEventHandler;


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
    public ContextEventClient(ConnectorContextBase     parentContext,
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

        this.contextEventHandler = new ContextEventHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new context event.
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
    public String createContextEvent(NewElementOptions                     newElementOptions,
                                     Map<String, ClassificationProperties> initialClassifications,
                                     ContextEventProperties                properties,
                                     RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        String elementGUID = contextEventHandler.createContextEvent(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a contextEvent using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new contextEvent.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing contextEvent to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createContextEventFromTemplate(TemplateOptions        templateOptions,
                                                 String                 templateGUID,
                                                 EntityProperties       replacementProperties,
                                                 Map<String, String>    placeholderProperties,
                                                 RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        String elementGUID = contextEventHandler.createContextEventFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a context event.
     *
     * @param contextEventGUID       unique identifier of the contextEvent (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateContextEvent(String                 contextEventGUID,
                                      UpdateOptions          updateOptions,
                                      ContextEventProperties properties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        boolean updateOccurred = contextEventHandler.updateContextEvent(connectorUserId, contextEventGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(contextEventGUID);
        }

        return updateOccurred;
    }



    /**
     * Connect two context events to show that one is dependent on another.
     *
     * @param parentContextEventGUID    unique identifier of the parent context event
     * @param childContextEventGUID     unique identifier of the child context event
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDependentContextEvents(String                          parentContextEventGUID,
                                           String                          childContextEventGUID,
                                           MakeAnchorOptions               makeAnchorOptions,
                                           DependentContextEventProperties relationshipProperties) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        contextEventHandler.linkDependentContextEvents(connectorUserId, parentContextEventGUID, childContextEventGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach two dependent context events from one another.
     *
     * @param parentContextEventGUID    unique identifier of the  parent context event.
     * @param childContextEventGUID     unique identifier of the child context event.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDependentContextEvents(String        parentContextEventGUID,
                                             String        childContextEventGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        contextEventHandler.detachDependentContextEvents(connectorUserId, parentContextEventGUID, childContextEventGUID, deleteOptions);
    }


    /**
     * Connect two context events to show that one is related to the other.
     *
     * @param contextEventOneGUID    unique identifier of the  context event at end 1
     * @param contextEventTwoGUID     unique identifier of the  context event at end 2
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkRelatedContextEvents(String                        contextEventOneGUID,
                                         String                        contextEventTwoGUID,
                                         MakeAnchorOptions             makeAnchorOptions,
                                         RelatedContextEventProperties relationshipProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        contextEventHandler.linkRelatedContextEvents(connectorUserId, contextEventOneGUID, contextEventTwoGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach two context events that are related to one another.
     *
     * @param contextEventOneGUID    unique identifier of the  context event at end 1
     * @param contextEventTwoGUID     unique identifier of the  context event at end 2
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachRelatedContextEvents(String        contextEventOneGUID,
                                           String        contextEventTwoGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        contextEventHandler.detachRelatedContextEvents(connectorUserId, contextEventOneGUID, contextEventTwoGUID, deleteOptions);
    }


    /**
     * Connect a context event to an element that provides evidence that this context event is real.
     *
     * @param contextEventGUID    unique identifier of the context event
     * @param evidenceGUID     unique identifier of the element representing the evidence
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContextEventEvidence(String                         contextEventGUID,
                                         String                         evidenceGUID,
                                         MakeAnchorOptions              makeAnchorOptions,
                                         ContextEventEvidenceProperties relationshipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        contextEventHandler.linkContextEventEvidence(connectorUserId, contextEventGUID, evidenceGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a context event from an element that provides evidence that this context event is real.
     *
     * @param contextEventGUID    unique identifier of the context event
     * @param evidenceGUID     unique identifier of the element representing the evidence
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContextEventEvidence(String        contextEventGUID,
                                           String        evidenceGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        contextEventHandler.detachContextEventEvidence(connectorUserId, contextEventGUID, evidenceGUID, deleteOptions);
    }


    /**
     * Connect a context event to an element that is impacted by this event.
     *
     * @param contextEventGUID    unique identifier of the context event
     * @param impactedElementGUID     unique identifier of the element that is impacted by the event
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContextEventImpact(String                       contextEventGUID,
                                       String                       impactedElementGUID,
                                       MakeAnchorOptions            makeAnchorOptions,
                                       ContextEventImpactProperties relationshipProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        contextEventHandler.linkContextEventImpact(connectorUserId, contextEventGUID, impactedElementGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a context event from an element that is impacted by the event.
     *
     * @param contextEventGUID    unique identifier of the context event
     * @param impactedElementGUID     unique identifier of the element that is impacted by the event
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContextEventImpact(String        contextEventGUID,
                                         String        impactedElementGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        contextEventHandler.detachContextEventImpact(connectorUserId, contextEventGUID, impactedElementGUID, deleteOptions);
    }


    /**
     * Connect a context event to an element whose data is impacted by this event.
     *
     * @param contextEventGUID    unique identifier of the context event
     * @param timelineAffectedElementGUID     unique identifier of the element whose data is impacted by the context event
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkContextEventTimelineEffect(String                                   timelineAffectedElementGUID,
                                               String                                   contextEventGUID,
                                               MakeAnchorOptions                        makeAnchorOptions,
                                               ContextEventForTimelineEffectsProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                       PropertyServerException,
                                                                                                                       UserNotAuthorizedException
    {
        contextEventHandler.linkContextEventTimelineEffect(connectorUserId, timelineAffectedElementGUID, contextEventGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a context event from an element whose data is impacted by the event.
     *
     * @param timelineAffectedElementGUID     unique identifier of the element whose data is impacted by the context event
     * @param contextEventGUID    unique identifier of the context event
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachContextEventTimelineEffect(String        timelineAffectedElementGUID,
                                                 String        contextEventGUID,
                                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        contextEventHandler.detachContextEventTimelineEffect(connectorUserId, timelineAffectedElementGUID, contextEventGUID, deleteOptions);
    }



    /**
     * Delete a contextEvent.
     *
     * @param contextEventGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteContextEvent(String        contextEventGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        contextEventHandler.deleteContextEvent(connectorUserId, contextEventGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(contextEventGUID);
        }
    }


    /**
     * Returns the list of context events with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getContextEventsByName(String       name,
                                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        return contextEventHandler.getContextEventsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific context event.
     *
     * @param contextEventGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getContextEventByGUID(String     contextEventGUID,
                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        return contextEventHandler.getContextEventByGUID(connectorUserId, contextEventGUID, getOptions);
    }


    /**
     * Retrieve the list of contextEvents metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned contextEvents include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findContextEvents(String        searchString,
                                                           SearchOptions searchOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return contextEventHandler.findContextEvents(connectorUserId, searchString, searchOptions);
    }
}
