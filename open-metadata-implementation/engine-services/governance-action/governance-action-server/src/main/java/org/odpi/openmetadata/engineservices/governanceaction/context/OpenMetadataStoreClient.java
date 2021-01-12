/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.context;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.WatchdogGovernanceListener;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogEventType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SequencingOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * GovernanceEngineClient sits in the governance context of a governance action service when it is running in the engine host OMAG server.
 * It is however shared by all of the governance action services running in an engine service so that we only need one connector to the topic
 * listener for the watchdog governance services.
 */
public class OpenMetadataStoreClient extends OpenMetadataClient
{

    private String userId;                   /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private String                    connectorId = UUID.randomUUID().toString();
    private GovernanceEngineClient    governanceEngineClient;    /* Initialized in constructor */
    private GovernanceListenerManager governanceListenerManager; /* Initialized in constructor */


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param governanceEngineClient client that does all of the work
     * @param governanceListenerManager governance listener manager
     * @param userId caller's userId
     * @throws InvalidParameterException the server name of platform URL root is null.
     */
    public OpenMetadataStoreClient(String                    serverName,
                                   String                    serverPlatformURLRoot,
                                   GovernanceEngineClient    governanceEngineClient,
                                   GovernanceListenerManager governanceListenerManager,
                                   String                    userId) throws InvalidParameterException
    {
        super(serverPlatformURLRoot, serverName);

        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        this.governanceEngineClient = governanceEngineClient;
        this.governanceListenerManager = governanceListenerManager;
        this.userId = userId;
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param elementGUID unique identifier for the metadata element
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataElement getMetadataElementByGUID(String elementGUID) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return governanceEngineClient.getMetadataElementByGUID(userId, elementGUID);
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param searchString name to retrieve
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public List<OpenMetadataElement> findMetadataElementsWithString(String searchString,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        return governanceEngineClient.findMetadataElementsWithString(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param elementGUID unique identifier for the starting metadata element
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public List<RelatedMetadataElement> getRelatedMetadataElements(String elementGUID,
                                                                   String relationshipTypeName,
                                                                   int    startFrom,
                                                                   int    pageSize) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return governanceEngineClient.getRelatedMetadataElements(userId, elementGUID, relationshipTypeName, startFrom, pageSize);
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param metadataElementSubtypeName optional list of the subtypes of the metadataElementTypeName to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param matchClassifications Optional list of classifications to match.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public List<OpenMetadataElement> findMetadataElements(String                metadataElementTypeName,
                                                          List<String>          metadataElementSubtypeName,
                                                          SearchProperties      searchProperties,
                                                          List<ElementStatus>   limitResultsByStatus,
                                                          SearchClassifications matchClassifications,
                                                          String                sequencingProperty,
                                                          SequencingOrder       sequencingOrder,
                                                          int                   startFrom,
                                                          int                   pageSize) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return governanceEngineClient.findMetadataElements(userId,
                                                           metadataElementTypeName,
                                                           metadataElementSubtypeName,
                                                           searchProperties,
                                                           limitResultsByStatus,
                                                           matchClassifications,
                                                           sequencingProperty,
                                                           sequencingOrder,
                                                           startFrom,
                                                           pageSize);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param relationshipTypeName relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param searchProperties Optional list of relationship property conditions to match.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public  List<RelatedMetadataElement> findRelationshipsBetweenMetadataElements(String           relationshipTypeName,
                                                                                  SearchProperties searchProperties,
                                                                                  String           sequencingProperty,
                                                                                  SequencingOrder  sequencingOrder,
                                                                                  int              startFrom,
                                                                                  int              pageSize) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        return governanceEngineClient.findRelationshipsBetweenMetadataElements(userId,
                                                                               relationshipTypeName,
                                                                               searchProperties,
                                                                               sequencingProperty,
                                                                               sequencingOrder,
                                                                               startFrom,
                                                                               pageSize);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all of the attachments such as nested content, schema
     *                     connection etc)
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementInStore(String            metadataElementTypeName,
                                               ElementStatus     initialStatus,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               String            templateGUID) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return governanceEngineClient.createMetadataElementInStore(userId,
                                                                   metadataElementTypeName,
                                                                   initialStatus,
                                                                   effectiveFrom,
                                                                   effectiveTo,
                                                                   properties,
                                                                   templateGUID);
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all of them by
     * the value used in the replaceProperties flag.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementInStore(String            metadataElementGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        governanceEngineClient.updateMetadataElementInStore(userId, metadataElementGUID, replaceProperties, properties);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param newElementStatus new status value - or null to leave as is
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementStatusInStore(String        metadataElementGUID,
                                                   ElementStatus newElementStatus,
                                                   Date          effectiveFrom,
                                                   Date          effectiveTo) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        governanceEngineClient.updateMetadataElementStatusInStore(userId, metadataElementGUID, newElementStatus, effectiveFrom, effectiveTo);
    }


    /**
     * Delete a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void deleteMetadataElementInStore(String metadataElementGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        governanceEngineClient.deleteMetadataElementInStore(userId, metadataElementGUID);
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param effectiveFrom the date when this classification is active - null for active now
     * @param effectiveTo the date when this classification becomes inactive - null for active until deleted
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void classifyMetadataElementInStore(String            metadataElementGUID,
                                               String            classificationName,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        governanceEngineClient.classifyMetadataElementInStore(userId, metadataElementGUID, classificationName, effectiveFrom, effectiveTo, properties);
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the classification
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void reclassifyMetadataElementInStore(String            metadataElementGUID,
                                                  String            classificationName,
                                                  boolean           replaceProperties,
                                                  ElementProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        governanceEngineClient.reclassifyMetadataElementInStore(userId, metadataElementGUID, classificationName, replaceProperties, properties);
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateClassificationStatusInStore(String metadataElementGUID,
                                                  String classificationName,
                                                  Date   effectiveFrom,
                                                  Date   effectiveTo) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        governanceEngineClient.updateClassificationStatusInStore(userId, metadataElementGUID, classificationName, effectiveFrom, effectiveTo);
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to remove this classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void unclassifyMetadataElementInStore(String metadataElementGUID,
                                                  String classificationName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        governanceEngineClient.unclassifyMetadataElementInStore(userId, metadataElementGUID, classificationName);
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties the properties of the relationship
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createRelatedElementsInStore(String            relationshipTypeName,
                                               String            metadataElement1GUID,
                                               String            metadataElement2GUID,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return governanceEngineClient.createRelatedElementsInStore(userId, relationshipTypeName, metadataElement1GUID, metadataElement2GUID, effectiveFrom, effectiveTo, properties);
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the classification
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateRelatedElementsInStore(String            relationshipGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        governanceEngineClient.updateRelatedElementsInStore(userId, relationshipGUID, replaceProperties, properties);
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void updateRelatedElementsStatusInStore(String relationshipGUID,
                                                    Date   effectiveFrom,
                                                    Date   effectiveTo) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        governanceEngineClient.updateRelatedElementsStatusInStore(userId, relationshipGUID, effectiveFrom, effectiveTo);
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param relationshipGUID unique identifier of the relationship to delete
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void deleteRelatedElementsInStore(String relationshipGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        governanceEngineClient.deleteRelatedElementsInStore(userId, relationshipGUID);
    }


    /**
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance action service.  However, if the governance action service has to process name
     * target elements, then setting the status on each individual target will show the progress of the
     * governance action service.
     *
     * @param actionTargetGUID unique identifier of the governance action service.
     * @param status status enum to show its progress
     * @param startDate date/time that the governance action service started processing the target
     * @param completionDate date/time that the governance process completed processing this target.
     *
     * @throws InvalidParameterException the action target GUID is not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public void updateActionTargetStatus(String                 actionTargetGUID,
                                         GovernanceActionStatus status,
                                         Date                   startDate,
                                         Date                   completionDate) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        governanceEngineClient.updateActionTargetStatus(userId, actionTargetGUID, status, startDate, completionDate);
    }


    /**
     * Declare that all of the processing for the governance action service is finished and the status of the work.
     *
     * @param governanceActionGUID unique identifier of the governance action that triggered this governance service
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newActionTargetGUIDs list of additional elements to add to the action targets for the next phase
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public void recordCompletionStatus(String           governanceActionGUID,
                                       CompletionStatus status,
                                       List<String>     outputGuards,
                                       List<String>     newActionTargetGUIDs) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        governanceEngineClient.recordCompletionStatus(userId, governanceActionGUID, status, outputGuards, newActionTargetGUIDs);
    }


    /**
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param qualifiedName unique identifier to give this governance action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargetGUIDs list of action targets for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine to run the request
     * @param requestType request type to identify the governance action service to run
     * @param requestProperties properties to pass to the governance action service
     *
     * @return unique identifier of the governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateGovernanceAction(String              qualifiedName,
                                           int                 domainIdentifier,
                                           String              displayName,
                                           String              description,
                                           List<String>        requestSourceGUIDs,
                                           List<String>        actionTargetGUIDs,
                                           Date                startTime,
                                           String              governanceEngineName,
                                           String              requestType,
                                           Map<String, String> requestProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return governanceEngineClient.initiateGovernanceAction(userId,
                                                               qualifiedName,
                                                               domainIdentifier,
                                                               displayName,
                                                               description,
                                                               requestSourceGUIDs,
                                                               actionTargetGUIDs,
                                                               startTime,
                                                               governanceEngineName,
                                                               requestType,
                                                               requestProperties);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param processQualifiedName unique name of the governance action process to use
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargetGUIDs list of action targets for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible".
     *
     * @return unique identifier of the first governance action of the process
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateGovernanceActionProcess(String       processQualifiedName,
                                                  List<String> requestSourceGUIDs,
                                                  List<String> actionTargetGUIDs,
                                                  Date         startTime) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return governanceEngineClient.initiateGovernanceActionProcess(userId, processQualifiedName, requestSourceGUIDs, actionTargetGUIDs, startTime);
    }


    /**
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param qualifiedName unique identifier to give this new incident report
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param background description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param incidentClassifiers initial classifiers for the incident report
     * @param additionalProperties additional arbitrary properties for the incident reports
     *
     * @return unique identifier of the resulting incident report
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a incident report
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  String createIncidentReport(String                        qualifiedName,
                                        int                           domainIdentifier,
                                        String                        background,
                                        List<IncidentImpactedElement> impactedResources,
                                        List<IncidentDependency>      previousIncidents,
                                        Map<String, Integer>          incidentClassifiers,
                                        Map<String, String>           additionalProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return governanceEngineClient.createIncidentReport(userId,
                                                           qualifiedName,
                                                           domainIdentifier,
                                                           background,
                                                           impactedResources,
                                                           previousIncidents,
                                                           incidentClassifiers,
                                                           additionalProperties);
    }


    /**
     * Register a listener to receive events about changes to metadata elements in the open metadata store.
     * There can be only one registered listener.  If this method is called more than once, the new parameters
     * replace the existing parameters.  This means the watchdog governance action service can change the
     * listener and the parameters that control the types of events received while it is running.
     *
     * The types of events passed to the listener are controlled by the combination of the interesting event types and
     * the interesting metadata types.  That is an event is only passed to the listener if it matches both
     * the interesting event types and the interesting metadata types.
     *
     * If specific instance, interestingEventTypes or interestingMetadataTypes are null, it defaults to "any".
     * If the listener parameter is null, no more events are passed to the listener.
     *
     * @param listener listener object to receive events
     * @param interestingEventTypes types of events that should be passed to the listener
     * @param interestingMetadataTypes types of elements that are the subject of the interesting event types
     * @param specificInstance unique identifier of a specific instance to watch for
     *
     * @throws InvalidParameterException one or more of the type names are unrecognized
     */
    @Override
    public  void registerListener(WatchdogGovernanceListener listener,
                                  List<WatchdogEventType>    interestingEventTypes,
                                  List<String>               interestingMetadataTypes,
                                  String                     specificInstance) throws InvalidParameterException
    {
        governanceListenerManager.registerListener(connectorId, listener, interestingEventTypes, interestingMetadataTypes, specificInstance);
    }
}
