/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.context;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.WatchdogGovernanceListener;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogEventType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SequencingOrder;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * OpenMetadataClient is created for each instance of the governance service.  It interfaces between the governance
 * context of each governance service instance and the clients and handlers of Governance Action Open Metadata Engine Service (OMES).
 *
 * The governanceServiceHandler is also tied to the governance service instance.  The governanceListenerManager and
 * the governanceEngineClient are shared by all services instances running in the Governance Action OMES.
 *
 * There are three groups of methods:
 *
 * <ul>
 *     <li>Methods for working with metadata that are driven by the logic in the governance service.
 *         This is implemented with calls to the Governance Engine OMAS client that is calling the partner OMAS
 *         for Governance Action OMES.</li>
 *
 *     <li>Methods for managing the Governance Actions that drive calls to the governance services.
 *         These methods are passed to the governanceServiceHandler to be handled by the engine host services.
 *         The engine host services may have a different partner OMAS and so they have their own client to Governance Engine OMAS.</li>
 *
 *     <li>Methods for managing Open Watchdog Governance Service's listeners that are passed to the governance listener manager.</li>
 * </ul>
 *
 */
public class OpenMetadataStoreClient extends OpenMetadataClient
{
    private final String                    engineUserId;              /* Initialized in constructor */

    private final String                    connectorId = UUID.randomUUID().toString();
    private final GovernanceEngineClient    governanceEngineClient;    /* Initialized in constructor */
    private final GovernanceListenerManager governanceListenerManager; /* Initialized in constructor */
    private final GovernanceServiceHandler  governanceServiceHandler;  /* Initialized in constructor */


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param governanceEngineClient client that does all the work
     * @param governanceListenerManager governance listener manager
     * @param governanceServiceHandler handler for this service
     * @param engineUserId engine userId
     * @throws InvalidParameterException the server name of platform URL root is null.
     */
    public OpenMetadataStoreClient(String                    serverName,
                                   String                    serverPlatformURLRoot,
                                   GovernanceEngineClient    governanceEngineClient,
                                   GovernanceListenerManager governanceListenerManager,
                                   GovernanceServiceHandler  governanceServiceHandler,
                                   String                    engineUserId) throws InvalidParameterException
    {
        super(serverPlatformURLRoot, serverName);

        final String methodName = "Constructor (no security)";

        InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(engineUserId, methodName);

        this.governanceEngineClient = governanceEngineClient;
        this.governanceListenerManager = governanceListenerManager;
        this.governanceServiceHandler = governanceServiceHandler;
        this.engineUserId = engineUserId;
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param elementGUID unique identifier for the metadata element
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataElement getMetadataElementByGUID(String  elementGUID,
                                                        boolean forLineage,
                                                        boolean forDuplicateProcessing,
                                                        Date    effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return governanceEngineClient.getMetadataElementByGUID(engineUserId, elementGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataElement getMetadataElementByUniqueName(String  uniqueName,
                                                              String  uniquePropertyName,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing,
                                                              Date    effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return governanceEngineClient.getMetadataElementByUniqueName(engineUserId, uniqueName, uniquePropertyName, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element unique identifier (guid)
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public String getMetadataElementGUIDByUniqueName(String  uniqueName,
                                                     String  uniquePropertyName,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return governanceEngineClient.getMetadataElementGUIDByUniqueName(engineUserId,
                                                                         uniqueName,
                                                                         uniquePropertyName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime);
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param searchString name to retrieve
     * @param forLineage the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public List<OpenMetadataElement> findMetadataElementsWithString(String  searchString,
                                                                    boolean forLineage,
                                                                    boolean forDuplicateProcessing,
                                                                    Date    effectiveTime,
                                                                    int     startFrom,
                                                                    int     pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return governanceEngineClient.findMetadataElementsWithString(engineUserId,
                                                                     searchString,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     startFrom,
                                                                     pageSize);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param elementGUID unique identifier for the starting metadata element
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public List<RelatedMetadataElement> getRelatedMetadataElements(String  elementGUID,
                                                                   int     startingAtEnd,
                                                                   String  relationshipTypeName,
                                                                   boolean forLineage,
                                                                   boolean forDuplicateProcessing,
                                                                   Date    effectiveTime,
                                                                   int     startFrom,
                                                                   int     pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return governanceEngineClient.getRelatedMetadataElements(engineUserId,
                                                                 elementGUID,
                                                                 startingAtEnd,
                                                                 relationshipTypeName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 startFrom,
                                                                 pageSize);
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
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
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
                                                          boolean               forLineage,
                                                          boolean               forDuplicateProcessing,
                                                          Date                  effectiveTime,
                                                          int                   startFrom,
                                                          int                   pageSize) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return governanceEngineClient.findMetadataElements(engineUserId,
                                                           metadataElementTypeName,
                                                           metadataElementSubtypeName,
                                                           searchProperties,
                                                           limitResultsByStatus,
                                                           matchClassifications,
                                                           sequencingProperty,
                                                           sequencingOrder,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
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
     * @param forLineage the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public  List<RelatedMetadataElements> findRelationshipsBetweenMetadataElements(String           relationshipTypeName,
                                                                                   SearchProperties searchProperties,
                                                                                   String           sequencingProperty,
                                                                                   SequencingOrder  sequencingOrder,
                                                                                   boolean          forLineage,
                                                                                   boolean          forDuplicateProcessing,
                                                                                   Date             effectiveTime,
                                                                                   int              startFrom,
                                                                                   int              pageSize) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        return governanceEngineClient.findRelationshipsBetweenMetadataElements(engineUserId,
                                                                               relationshipTypeName,
                                                                               searchProperties,
                                                                               sequencingProperty,
                                                                               sequencingOrder,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               effectiveTime,
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
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
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
        return governanceEngineClient.createMetadataElementInStore(engineUserId,
                                                                   metadataElementTypeName,
                                                                   initialStatus,
                                                                   effectiveFrom,
                                                                   effectiveTo,
                                                                   properties,
                                                                   templateGUID);
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties new properties for the metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementInStore(String            metadataElementGUID,
                                             boolean           replaceProperties,
                                             boolean           forLineage,
                                             boolean           forDuplicateProcessing,
                                             ElementProperties properties,
                                             Date              effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        governanceEngineClient.updateMetadataElementInStore(engineUserId,
                                                            metadataElementGUID,
                                                            replaceProperties,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            properties,
                                                            effectiveTime);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param newElementStatus new status value - or null to leave as is
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementStatusInStore(String        metadataElementGUID,
                                                   boolean       forLineage,
                                                   boolean       forDuplicateProcessing,
                                                   ElementStatus newElementStatus,
                                                   Date          effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        governanceEngineClient.updateMetadataElementStatusInStore(engineUserId,
                                                                  metadataElementGUID,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  newElementStatus,
                                                                  effectiveTime);
    }


    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementEffectivityInStore(String        metadataElementGUID,
                                                        boolean       forLineage,
                                                        boolean       forDuplicateProcessing,
                                                        Date          effectiveFrom,
                                                        Date          effectiveTo,
                                                        Date          effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        governanceEngineClient.updateMetadataElementEffectivityInStore(engineUserId,
                                                                       metadataElementGUID,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveFrom,
                                                                       effectiveTo,
                                                                       effectiveTime);
    }


    /**
     * Delete a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void deleteMetadataElementInStore(String  metadataElementGUID,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        governanceEngineClient.deleteMetadataElementInStore(engineUserId,
                                                            metadataElementGUID,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            effectiveTime);
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this classification is active - null for active now
     * @param effectiveTo the date when this classification becomes inactive - null for active until deleted
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void classifyMetadataElementInStore(String            metadataElementGUID,
                                               String            classificationName,
                                               boolean           forLineage,
                                               boolean           forDuplicateProcessing,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               Date              effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        governanceEngineClient.classifyMetadataElementInStore(engineUserId,
                                                              metadataElementGUID,
                                                              classificationName,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              effectiveFrom,
                                                              effectiveTo,
                                                              properties,
                                                              effectiveTime);
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the classification
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
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
                                                  boolean           forLineage,
                                                  boolean           forDuplicateProcessing,
                                                  ElementProperties properties,
                                                  Date              effectiveTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        governanceEngineClient.reclassifyMetadataElementInStore(engineUserId,
                                                                metadataElementGUID,
                                                                classificationName,
                                                                replaceProperties,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                properties,
                                                                effectiveTime);
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateClassificationEffectivityInStore(String  metadataElementGUID,
                                                       String  classificationName,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveFrom,
                                                       Date    effectiveTo,
                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        governanceEngineClient.updateClassificationEffectivityInStore(engineUserId,
                                                                      metadataElementGUID,
                                                                      classificationName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveFrom,
                                                                      effectiveTo,
                                                                      effectiveTime);
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to remove this classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void unclassifyMetadataElementInStore(String  metadataElementGUID,
                                                  String  classificationName,
                                                  boolean forLineage,
                                                  boolean forDuplicateProcessing,
                                                  Date    effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        governanceEngineClient.unclassifyMetadataElementInStore(engineUserId,
                                                                metadataElementGUID,
                                                                classificationName,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveTime);
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties the properties of the relationship
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
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
                                               boolean           forLineage,
                                               boolean           forDuplicateProcessing,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               Date              effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return governanceEngineClient.createRelatedElementsInStore(engineUserId,
                                                                   relationshipTypeName,
                                                                   metadataElement1GUID,
                                                                   metadataElement2GUID,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveFrom,
                                                                   effectiveTo,
                                                                   properties,
                                                                   effectiveTime);
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties new properties for the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateRelatedElementsInStore(String            relationshipGUID,
                                             boolean           replaceProperties,
                                             boolean           forLineage,
                                             boolean           forDuplicateProcessing,
                                             ElementProperties properties,
                                             Date              effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        governanceEngineClient.updateRelatedElementsInStore(engineUserId,
                                                            relationshipGUID,
                                                            replaceProperties,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            properties,
                                                            effectiveTime);
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void updateRelatedElementsEffectivityInStore(String  relationshipGUID,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing,
                                                         Date    effectiveFrom,
                                                         Date    effectiveTo,
                                                         Date    effectiveTime) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        governanceEngineClient.updateRelatedElementsEffectivityInStore(engineUserId,
                                                                       relationshipGUID,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveFrom,
                                                                       effectiveTo,
                                                                       effectiveTime);
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void deleteRelatedElementsInStore(String  relationshipGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        governanceEngineClient.deleteRelatedElementsInStore(engineUserId,
                                                            relationshipGUID,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            effectiveTime);
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
        /*
         * Notice the call goes to the local handler to issue the request from the Engine Host's userId and to direct
         * the metadata update to the governance metadata server.
         */
        governanceServiceHandler.updateActionTargetStatus(actionTargetGUID, status, startDate, completionDate);
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param requestParameters properties to pass to the next governance action service
     * @param newActionTargets list of action target names to GUIDs for the resulting governance action service
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    @Override
    public void recordCompletionStatus(CompletionStatus      status,
                                       List<String>          outputGuards,
                                       Map<String, String>   requestParameters,
                                       List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        /*
         * Notice the call goes to the local handler to issue the request from the Engine Host's userId and to direct
         * the metadata update to the governance metadata server.
         */
        governanceServiceHandler.recordCompletionStatus(status, outputGuards, requestParameters, newActionTargets);
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
     * @param actionTargets list of action target names to GUIDs for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine to run the request
     * @param requestType request type to identify the governance action service to run
     * @param requestParameters properties to pass to the governance action service
     *
     * @return unique identifier of the governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateGovernanceAction(String                qualifiedName,
                                           int                   domainIdentifier,
                                           String                displayName,
                                           String                description,
                                           List<String>          requestSourceGUIDs,
                                           List<NewActionTarget> actionTargets,
                                           Date                  startTime,
                                           String                governanceEngineName,
                                           String                requestType,
                                           Map<String, String>   requestParameters) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return governanceEngineClient.initiateGovernanceAction(engineUserId,
                                                               qualifiedName,
                                                               domainIdentifier,
                                                               displayName,
                                                               description,
                                                               requestSourceGUIDs,
                                                               actionTargets,
                                                               null,
                                                               startTime,
                                                               governanceEngineName,
                                                               requestType,
                                                               requestParameters,
                                                               governanceServiceHandler.getGovernanceServiceName(),
                                                               governanceServiceHandler.getGovernanceEngineName());
    }


    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param processQualifiedName unique name of the governance action process to use
     * @param requestParameters initial set of request parameters to pass to the governance actions
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargets list of action target names to GUIDs for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible".
     *
     * @return unique identifier of the first governance action of the process
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateGovernanceActionProcess(String                processQualifiedName,
                                                  Map<String, String>   requestParameters,
                                                  List<String>          requestSourceGUIDs,
                                                  List<NewActionTarget> actionTargets,
                                                  Date                  startTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return governanceEngineClient.initiateGovernanceActionProcess(engineUserId,
                                                                      processQualifiedName,
                                                                      requestSourceGUIDs,
                                                                      actionTargets,
                                                                      startTime,
                                                                      requestParameters,
                                                                      governanceServiceHandler.getGovernanceServiceName(),
                                                                      governanceServiceHandler.getGovernanceEngineName());
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
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an incident report
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
        return governanceEngineClient.createIncidentReport(engineUserId,
                                                           qualifiedName,
                                                           domainIdentifier,
                                                           background,
                                                           impactedResources,
                                                           previousIncidents,
                                                           incidentClassifiers,
                                                           additionalProperties,
                                                           governanceServiceHandler.getGovernanceServiceGUID());
    }


    /**
     * Link elements as peer duplicates. Create a simple relationship between two elements.
     * If the relationship already exists, the properties are updated.
     *
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param setKnownDuplicate boolean flag indicating whether the KnownDuplicate classification should be set on the linked entities.
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void linkElementsAsPeerDuplicates(String  metadataElement1GUID,
                                             String  metadataElement2GUID,
                                             int     statusIdentifier,
                                             String  steward,
                                             String  stewardTypeName,
                                             String  stewardPropertyName,
                                             String  source,
                                             String  notes,
                                             boolean setKnownDuplicate) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        governanceEngineClient.linkElementsAsPeerDuplicates(engineUserId,
                                                            metadataElement1GUID,
                                                            metadataElement2GUID,
                                                            statusIdentifier,
                                                            steward,
                                                            stewardTypeName,
                                                            stewardPropertyName,
                                                            source,
                                                            notes,
                                                            setKnownDuplicate);
    }


    /**
     * Identify an element that acts as a consolidated version for a set of duplicate elements.
     * (The consolidated element is created using createMetadataElement.)
     *
     * @param consolidatedElementGUID unique identifier of the metadata element
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param sourceElementGUIDs List of the source elements that must be linked to the consolidated element.  It is assumed that they already
     *                           have the KnownDuplicateClassification.
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void linkConsolidatedDuplicate(String       consolidatedElementGUID,
                                          int          statusIdentifier,
                                          String       steward,
                                          String       stewardTypeName,
                                          String       stewardPropertyName,
                                          String       source,
                                          String       notes,
                                          List<String> sourceElementGUIDs) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        governanceEngineClient.linkConsolidatedDuplicate(engineUserId,
                                                         consolidatedElementGUID,
                                                         statusIdentifier,
                                                         steward,
                                                         stewardTypeName,
                                                         stewardPropertyName,
                                                         source,
                                                         notes,
                                                         sourceElementGUIDs);
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


    /**
     * Called during the disconnect processing of the watchdog governance action service.
     */
    public void disconnectListener()
    {
        governanceListenerManager.removeListener(connectorId);
    }
}
