/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.api;


import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentActorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintCompositionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainSegmentProperties;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ManageSolutions provides methods to define information supply chains, solution components and their supporting objects
 * The interface supports the following types of objects
 *
 * <ul>
 *     <li>InformationSupplyChains</li>
 *     <li>InformationSupplyChainSegments</li>
 *     <li>SolutionBlueprints</li>
 *     <li>SolutionComponents</li>
 *     <li>SolutionPorts</li>
 *     <li>SolutionRoles</li>
 * </ul>
 */
public interface ManageSolutions
{
    /**
     * Create a new information supply chain.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of any anchor scope to use for searching
     * @param properties             properties for the new element.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return unique identifier of the newly created element
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createInformationSupplyChain(String                           userId,
                                        String                           externalSourceGUID,
                                        String                           externalSourceName,
                                        String                           anchorGUID,
                                        boolean                          isOwnAnchor,
                                        String                           anchorScopeGUID,
                                        InformationSupplyChainProperties properties,
                                        String                           parentGUID,
                                        String                           parentRelationshipTypeName,
                                        ElementProperties                parentRelationshipProperties,
                                        boolean                          parentAtEnd1,
                                        boolean                          forLineage,
                                        boolean                          forDuplicateProcessing,
                                        Date                             effectiveTime) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException;

    /**
     * Create a new metadata element to represent an information supply chain using an existing  element as a template.
     * The template defines additional classifications and relationships that should be added to the new information supply chain.
     *
     * @param userId             calling user
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of any anchor scope to use for searching
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createInformationSupplyChainFromTemplate(String              userId,
                                                    String              externalSourceGUID,
                                                    String              externalSourceName,
                                                    String              anchorGUID,
                                                    boolean             isOwnAnchor,
                                                    String              anchorScopeGUID,
                                                    Date                effectiveFrom,
                                                    Date                effectiveTo,
                                                    String              templateGUID,
                                                    ElementProperties   replacementProperties,
                                                    Map<String, String> placeholderProperties,
                                                    String              parentGUID,
                                                    String              parentRelationshipTypeName,
                                                    ElementProperties   parentRelationshipProperties,
                                                    boolean             parentAtEnd1,
                                                    boolean             forLineage,
                                                    boolean             forDuplicateProcessing,
                                                    Date                effectiveTime) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;

    /**
     * Update the properties of an information supply chain.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param informationSupplyChainGUID         unique identifier of the information supply chain (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the element.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   updateInformationSupplyChain(String                           userId,
                                        String                           externalSourceGUID,
                                        String                           externalSourceName,
                                        String                           informationSupplyChainGUID,
                                        boolean                          replaceAllProperties,
                                        InformationSupplyChainProperties properties,
                                        boolean                          forLineage,
                                        boolean                          forDuplicateProcessing,
                                        Date                             effectiveTime) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException;


    /**
     * Create a new information supply chain segment.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param properties             properties for the new element.
     * @param informationSupplyChainGUID unique identifier of optional parent information supply chain
     *
     * @return unique identifier of the newly created element
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createInformationSupplyChainSegment(String                                  userId,
                                               String                                  externalSourceGUID,
                                               String                                  externalSourceName,
                                               InformationSupplyChainSegmentProperties properties,
                                               String                                  informationSupplyChainGUID,
                                               boolean                                 forLineage,
                                               boolean                                 forDuplicateProcessing,
                                               Date                                    effectiveTime) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException;

    /**
     * Update the properties of an information supply chain.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param segmentGUID         unique identifier of the information supply chain (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the element.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   updateInformationSupplyChainSegment(String                                  userId,
                                               String                                  externalSourceGUID,
                                               String                                  externalSourceName,
                                               String                                  segmentGUID,
                                               boolean                                 replaceAllProperties,
                                               InformationSupplyChainSegmentProperties properties,
                                               boolean                                 forLineage,
                                               boolean                                 forDuplicateProcessing,
                                               Date                                    effectiveTime) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException;


    /**
     * Connect two information supply chain segments.
     *
     * @param userId          userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param segment1GUID  unique identifier of the first segment
     * @param segment2GUID      unique identifier of the second segment
     * @param linkProperties   description of the relationship.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void linkSegments(String                               userId,
                      String                               externalSourceGUID,
                      String                               externalSourceName,
                      String                               segment1GUID,
                      String                               segment2GUID,
                      InformationSupplyChainLinkProperties linkProperties,
                      boolean                              forLineage,
                      boolean                              forDuplicateProcessing,
                      Date                                 effectiveTime) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException;


    /**
     * Detach two information supply chain segments from one another.
     *
     * @param userId          userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param segment1GUID  unique identifier of the first segment.
     * @param segment2GUID      unique identifier of the second segment.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void detachSegments(String  userId,
                        String  externalSourceGUID,
                        String  externalSourceName,
                        String  segment1GUID,
                        String  segment2GUID,
                        boolean forLineage,
                        boolean forDuplicateProcessing,
                        Date    effectiveTime) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;


    /**
     * Delete an information supply chain segment.
     *
     * @param userId   userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param segmentGUID  unique identifier of the element.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void deleteInformationSupplyChainSegment(String  userId,
                                             String  externalSourceGUID,
                                             String  externalSourceName,
                                             String  segmentGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException;


    /**
     * Delete an information supply chain and all of its segments.
     *
     * @param userId   userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param informationSupplyChainGUID  unique identifier of the element
     * @param cascadedDelete can information supply chains be deleted if segments are attached?
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void deleteInformationSupplyChain(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  informationSupplyChainGUID,
                                      boolean cascadedDelete,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException;


    /**
     * Returns the list of information supply chains with a particular name.
     *
     * @param userId     userId of user making request
     * @param name       name of the element to return - match is full text match in qualifiedName or name
     * @param addImplementation    should details of the implementation of the information supply chain be extracted too?
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return a list of elements
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<InformationSupplyChainElement> getInformationSupplyChainsByName(String              userId,
                                                                         String              name,
                                                                         boolean             addImplementation,
                                                                         List<ElementStatus> limitResultsByStatus,
                                                                         Date                asOfTime,
                                                                         SequencingOrder     sequencingOrder,
                                                                         String              sequencingProperty,
                                                                         int                 startFrom,
                                                                         int                 pageSize,
                                                                         boolean             forLineage,
                                                                         boolean             forDuplicateProcessing,
                                                                         Date                effectiveTime) throws InvalidParameterException,
                                                                                                                   PropertyServerException,
                                                                                                                   UserNotAuthorizedException;


    /**
     * Return the properties of a specific information supply chain.
     *
     * @param userId            userId of user making request
     * @param informationSupplyChainGUID    unique identifier of the required element
     * @param addImplementation    should details of the implementation of the information supply chain be extracted too?
     * @param asOfTime             repository time to use
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    InformationSupplyChainElement getInformationSupplyChainByGUID(String  userId,
                                                                  String  informationSupplyChainGUID,
                                                                  boolean addImplementation,
                                                                  Date    asOfTime,
                                                                  boolean forLineage,
                                                                  boolean forDuplicateProcessing,
                                                                  Date    effectiveTime) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException;


    /**
     * Retrieve the list of information supply chains metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime repository time to use
     * @param sequencingProperty property to use for sequencing order
     * @param sequencingOrder order to retrieve results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<InformationSupplyChainElement> findInformationSupplyChains(String              userId,
                                                                    String              searchString,
                                                                    boolean             addImplementation,
                                                                    List<ElementStatus> limitResultsByStatus,
                                                                    Date                asOfTime,
                                                                    SequencingOrder     sequencingOrder,
                                                                    String              sequencingProperty,
                                                                    int                 startFrom,
                                                                    int                 pageSize,
                                                                    boolean             forLineage,
                                                                    boolean             forDuplicateProcessing,
                                                                    Date                effectiveTime) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException;

    /**
     * Create a new solution blueprint.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of any anchor scope to use for searching
     * @param properties             properties for the new element.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return unique identifier of the newly created element
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createSolutionBlueprint(String                      userId,
                                   String                      externalSourceGUID,
                                   String                      externalSourceName,
                                   String                      anchorGUID,
                                   boolean                     isOwnAnchor,
                                   String                      anchorScopeGUID,
                                   SolutionBlueprintProperties properties,
                                   String                      parentGUID,
                                   String                      parentRelationshipTypeName,
                                   ElementProperties           parentRelationshipProperties,
                                   boolean                     parentAtEnd1,
                                   boolean                     forLineage,
                                   boolean                     forDuplicateProcessing,
                                   Date                        effectiveTime) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException;


    /**
     * Create a new metadata element to represent a solution blueprint using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new solution blueprint.
     *
     * @param userId             calling user
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of any anchor scope to use for searching
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createSolutionBlueprintFromTemplate(String              userId,
                                               String              externalSourceGUID,
                                               String              externalSourceName,
                                               String              anchorGUID,
                                               boolean             isOwnAnchor,
                                               String              anchorScopeGUID,
                                               Date                effectiveFrom,
                                               Date                effectiveTo,
                                               String              templateGUID,
                                               ElementProperties   replacementProperties,
                                               Map<String, String> placeholderProperties,
                                               String              parentGUID,
                                               String              parentRelationshipTypeName,
                                               ElementProperties   parentRelationshipProperties,
                                               boolean             parentAtEnd1,
                                               boolean             forLineage,
                                               boolean             forDuplicateProcessing,
                                               Date                effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;

    /**
     * Update the properties of a solution blueprint.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param solutionBlueprintGUID         unique identifier of the solution blueprint (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the element.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   updateSolutionBlueprint(String                      userId,
                                   String                      externalSourceGUID,
                                   String                      externalSourceName,
                                   String                      solutionBlueprintGUID,
                                   boolean                     replaceAllProperties,
                                   SolutionBlueprintProperties properties,
                                   boolean                     forLineage,
                                   boolean                     forDuplicateProcessing,
                                   Date                        effectiveTime) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException;



    /**
     * Connect a solution component into the solution blueprint.
     *
     * @param userId          userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param parentSolutionBlueprintGUID  unique identifier of the parent
     * @param memberSolutionComponentGUID      unique identifier of the solution component
     * @param relationshipProperties   description of the relationship.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void linkSolutionComponentToBlueprint(String                                 userId,
                                          String                                 externalSourceGUID,
                                          String                                 externalSourceName,
                                          String                                 parentSolutionBlueprintGUID,
                                          String                                 memberSolutionComponentGUID,
                                          SolutionBlueprintCompositionProperties relationshipProperties,
                                          boolean                                forLineage,
                                          boolean                                forDuplicateProcessing,
                                          Date                                   effectiveTime) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException;


    /**
     * Detach a solution component from a solution blueprint.
     *
     * @param userId          userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param solutionBlueprintGUID  unique identifier of the  solution blueprint.
     * @param solutionComponentGUID      unique identifier of the solution component.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void detachSolutionComponentFromBlueprint(String  userId,
                                              String  externalSourceGUID,
                                              String  externalSourceName,
                                              String  solutionBlueprintGUID,
                                              String  solutionComponentGUID,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException;



    /**
     * Delete a solution blueprint.
     *
     * @param userId   userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param solutionBlueprintGUID  unique identifier of the element
     * @param cascadedDelete can the solution blueprint be deleted if it has solution components linked to it?
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void deleteSolutionBlueprint(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  solutionBlueprintGUID,
                                 boolean cascadedDelete,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException;


    /**
     * Returns the list of solution blueprints with a particular name.
     *
     * @param userId     userId of user making request
     * @param name       name of the element to return - match is full text match in qualifiedName or name
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return a list of elements
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<SolutionBlueprintElement> getSolutionBlueprintsByName(String              userId,
                                                               String              name,
                                                               List<ElementStatus> limitResultsByStatus,
                                                               Date                asOfTime,
                                                               SequencingOrder     sequencingOrder,
                                                               String              sequencingProperty,
                                                               int                 startFrom,
                                                               int                 pageSize,
                                                               boolean             forLineage,
                                                               boolean             forDuplicateProcessing,
                                                               Date                effectiveTime) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException;


    /**
     * Return the properties of a specific solution blueprint.
     *
     * @param userId            userId of user making request
     * @param solutionBlueprintGUID    unique identifier of the required element
     * @param asOfTime             repository time to use
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    SolutionBlueprintElement getSolutionBlueprintByGUID(String  userId,
                                                        String  solutionBlueprintGUID,
                                                        Date    asOfTime,
                                                        boolean forLineage,
                                                        boolean forDuplicateProcessing,
                                                        Date    effectiveTime) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException;


    /**
     * Retrieve the list of solution blueprint metadata elements that contain the search string.
     * The returned blueprints include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime repository time to use
     * @param sequencingProperty property to use for sequencing order
     * @param sequencingOrder order to retrieve results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<SolutionBlueprintElement> findSolutionBlueprints(String              userId,
                                                          String              searchString,
                                                          List<ElementStatus> limitResultsByStatus,
                                                          Date                asOfTime,
                                                          SequencingOrder     sequencingOrder,
                                                          String              sequencingProperty,
                                                          int                 startFrom,
                                                          int                 pageSize,
                                                          boolean             forLineage,
                                                          boolean             forDuplicateProcessing,
                                                          Date                effectiveTime) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;

    /**
     * Create a new solution role.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of any anchor scope to use for searching
     * @param properties             properties for the new element.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return unique identifier of the newly created element
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createSolutionRole(String                  userId,
                              String                  externalSourceGUID,
                              String                  externalSourceName,
                              String                  anchorGUID,
                              boolean                 isOwnAnchor,
                              String                  anchorScopeGUID,
                              ActorRoleProperties     properties,
                              String                  parentGUID,
                              String                  parentRelationshipTypeName,
                              ElementProperties       parentRelationshipProperties,
                              boolean                 parentAtEnd1,
                              boolean                 forLineage,
                              boolean                 forDuplicateProcessing,
                              Date                    effectiveTime) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException;


    /**
     * Create a new metadata element to represent a solution role using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new solution role.
     *
     * @param userId             calling user
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of any anchor scope to use for searching
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createSolutionRoleFromTemplate(String              userId,
                                          String              externalSourceGUID,
                                          String              externalSourceName,
                                          String              anchorGUID,
                                          boolean             isOwnAnchor,
                                          String              anchorScopeGUID,
                                          Date                effectiveFrom,
                                          Date                effectiveTo,
                                          String              templateGUID,
                                          ElementProperties   replacementProperties,
                                          Map<String, String> placeholderProperties,
                                          String              parentGUID,
                                          String              parentRelationshipTypeName,
                                          ElementProperties   parentRelationshipProperties,
                                          boolean             parentAtEnd1,
                                          boolean             forLineage,
                                          boolean             forDuplicateProcessing,
                                          Date                effectiveTime) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;

    /**
     * Update the properties of a solution role.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param solutionRoleGUID         unique identifier of the solution role (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the element.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   updateSolutionRole(String                  userId,
                              String                  externalSourceGUID,
                              String                  externalSourceName,
                              String                  solutionRoleGUID,
                              boolean                 replaceAllProperties,
                              ActorRoleProperties     properties,
                              boolean                 forLineage,
                              boolean                 forDuplicateProcessing,
                              Date                    effectiveTime) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException;



    /**
     * Connect a solution component to show that it is a part of the solution role.
     *
     * @param userId          userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param solutionRoleGUID  unique identifier of the parent
     * @param solutionComponentGUID      unique identifier of the solution component
     * @param relationshipProperties   description of the relationship.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void linkSolutionComponentActor(String                           userId,
                                    String                           externalSourceGUID,
                                    String                           externalSourceName,
                                    String                           solutionRoleGUID,
                                    String                           solutionComponentGUID,
                                    SolutionComponentActorProperties relationshipProperties,
                                    boolean                          forLineage,
                                    boolean                          forDuplicateProcessing,
                                    Date                             effectiveTime) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException;


    /**
     * Detach a solution component from a solution role.
     *
     * @param userId          userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param solutionRoleGUID  unique identifier of the parent solution role.
     * @param solutionComponentGUID      unique identifier of the member98 solution component.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void detachSolutionComponentActor(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  solutionRoleGUID,
                                      String  solutionComponentGUID,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException;



    /**
     * Delete a solution role.
     *
     * @param userId   userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param solutionRoleGUID  unique identifier of the element
     * @param cascadedDelete can the solution role be deleted if it has solution components linked to it?
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void deleteSolutionRole(String  userId,
                            String  externalSourceGUID,
                            String  externalSourceName,
                            String  solutionRoleGUID,
                            boolean cascadedDelete,
                            boolean forLineage,
                            boolean forDuplicateProcessing,
                            Date    effectiveTime) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;


    /**
     * Returns the list of solution roles with a particular name.
     *
     * @param userId     userId of user making request
     * @param name       name of the element to return - match is full text match in qualifiedName or name
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return a list of elements
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<SolutionRoleElement> getSolutionRolesByName(String              userId,
                                                     String              name,
                                                     List<ElementStatus> limitResultsByStatus,
                                                     Date                asOfTime,
                                                     SequencingOrder     sequencingOrder,
                                                     String              sequencingProperty,
                                                     int                 startFrom,
                                                     int                 pageSize,
                                                     boolean             forLineage,
                                                     boolean             forDuplicateProcessing,
                                                     Date                effectiveTime) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException;


    /**
     * Return the properties of a specific solution role.
     *
     * @param userId            userId of user making request
     * @param solutionRoleGUID    unique identifier of the required element
     * @param asOfTime             repository time to use
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    SolutionRoleElement getSolutionRoleByGUID(String  userId,
                                              String  solutionRoleGUID,
                                              Date    asOfTime,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException;



    /**
     * Retrieve the list of actor roles metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned solution roles include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime repository time to use
     * @param sequencingProperty property to use for sequencing order
     * @param sequencingOrder order to retrieve results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<SolutionRoleElement> findSolutionRoles(String              userId,
                                                String              searchString,
                                                List<ElementStatus> limitResultsByStatus,
                                                Date                asOfTime,
                                                SequencingOrder     sequencingOrder,
                                                String              sequencingProperty,
                                                int                 startFrom,
                                                int                 pageSize,
                                                boolean             forLineage,
                                                boolean             forDuplicateProcessing,
                                                Date                effectiveTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;

    /**
     * Create a new solution component.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of any anchor scope to use for searching
     * @param properties             properties for the new element.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return unique identifier of the newly created element
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createSolutionComponent(String                      userId,
                                   String                      externalSourceGUID,
                                   String                      externalSourceName,
                                   String                      anchorGUID,
                                   boolean                     isOwnAnchor,
                                   String                      anchorScopeGUID,
                                   SolutionComponentProperties properties,
                                   String                      parentGUID,
                                   String                      parentRelationshipTypeName,
                                   ElementProperties           parentRelationshipProperties,
                                   boolean                     parentAtEnd1,
                                   boolean                     forLineage,
                                   boolean                     forDuplicateProcessing,
                                   Date                        effectiveTime) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException;


    /**
     * Create a new metadata element to represent a solution component using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new solution component.
     *
     * @param userId             calling user
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of any anchor scope to use for searching
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createSolutionComponentFromTemplate(String              userId,
                                               String              externalSourceGUID,
                                               String              externalSourceName,
                                               String              anchorGUID,
                                               boolean             isOwnAnchor,
                                               String              anchorScopeGUID,
                                               Date                effectiveFrom,
                                               Date                effectiveTo,
                                               String              templateGUID,
                                               ElementProperties   replacementProperties,
                                               Map<String, String> placeholderProperties,
                                               String              parentGUID,
                                               String              parentRelationshipTypeName,
                                               ElementProperties   parentRelationshipProperties,
                                               boolean             parentAtEnd1,
                                               boolean             forLineage,
                                               boolean             forDuplicateProcessing,
                                               Date                effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;

    /**
     * Update the properties of a solution component.
     *
     * @param userId                 userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param solutionComponentGUID         unique identifier of the solution component (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the element.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   updateSolutionComponent(String                      userId,
                                   String                      externalSourceGUID,
                                   String                      externalSourceName,
                                   String                      solutionComponentGUID,
                                   boolean                     replaceAllProperties,
                                   SolutionComponentProperties properties,
                                   boolean                     forLineage,
                                   boolean                     forDuplicateProcessing,
                                   Date                        effectiveTime) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException;


    /**
     * Connect two solution components to show that one is a nested subcomponent.
     *
     * @param userId          userId of user making request
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param parentSolutionComponentGUID  unique identifier of the parent
     * @param subcomponentGUID      unique identifier of the solution component
     * @param relationshipProperties   description of the relationship.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void linkSubcomponent(String                 userId,
                          String                 externalSourceGUID,
                          String                 externalSourceName,
                          String                 parentSolutionComponentGUID,
                          String                 subcomponentGUID,
                          RelationshipProperties relationshipProperties,
                          boolean                forLineage,
                          boolean                forDuplicateProcessing,
                          Date                   effectiveTime) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;


    /**
     * Detach a solution component from a solution component.
     *
     * @param userId          userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param parentSolutionComponentGUID  unique identifier of the parent solution component.
     * @param subcomponentGUID      unique identifier of the nested solution component.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void detachSubcomponent(String  userId,
                            String  externalSourceGUID,
                            String  externalSourceName,
                            String  parentSolutionComponentGUID,
                            String  subcomponentGUID,
                            boolean forLineage,
                            boolean forDuplicateProcessing,
                            Date    effectiveTime) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;



    /**
     * Delete a solution component.
     *
     * @param userId   userId of user making request.
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param solutionComponentGUID  unique identifier of the element
     * @param cascadedDelete can the solution component be deleted if it has solution components linked to it?
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void deleteSolutionComponent(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  solutionComponentGUID,
                                 boolean cascadedDelete,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException;


    /**
     * Returns the list of solution components with a particular name.
     *
     * @param userId     userId of user making request
     * @param name       name of the element to return - match is full text match in qualifiedName or name
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return a list of elements
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<SolutionComponentElement> getSolutionComponentsByName(String              userId,
                                                               String              name,
                                                               List<ElementStatus> limitResultsByStatus,
                                                               Date                asOfTime,
                                                               SequencingOrder     sequencingOrder,
                                                               String              sequencingProperty,
                                                               int                 startFrom,
                                                               int                 pageSize,
                                                               boolean             forLineage,
                                                               boolean             forDuplicateProcessing,
                                                               Date                effectiveTime) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException;


    /**
     * Return the properties of a specific solution component.
     *
     * @param userId            userId of user making request
     * @param solutionComponentGUID    unique identifier of the required element
     * @param asOfTime             repository time to use
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    SolutionComponentElement getSolutionComponentByGUID(String  userId,
                                                        String  solutionComponentGUID,
                                                        Date    asOfTime,
                                                        boolean forLineage,
                                                        boolean forDuplicateProcessing,
                                                        Date    effectiveTime) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException;


    /**
     * Retrieve the list of solution component metadata elements that contain the search string.  The solutions components returned include information about the consumers, actors and other solution components that are associated with them.
     * The returned solution components include a list of the subcomponents, peer components and solution roles that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime repository time to use
     * @param sequencingProperty property to use for sequencing order
     * @param sequencingOrder order to retrieve results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<SolutionComponentElement> findSolutionComponents(String              userId,
                                                          String              searchString,
                                                          List<ElementStatus> limitResultsByStatus,
                                                          Date                asOfTime,
                                                          SequencingOrder     sequencingOrder,
                                                          String              sequencingProperty,
                                                          int                 startFrom,
                                                          int                 pageSize,
                                                          boolean             forLineage,
                                                          boolean             forDuplicateProcessing,
                                                          Date                effectiveTime) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;

    /**
     * Retrieve the list of  metadata elements that are associated with the solution component via the ImplementedBy relationship.
     *
     * @param userId calling user
     * @param solutionComponentGUID unique identifier of the solution component to query
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime repository time to use
     * @param sequencingProperty property to use for sequencing order
     * @param sequencingOrder order to retrieve results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedMetadataElementSummary> getSolutionComponentImplementations(String              userId,
                                                                            String             solutionComponentGUID,
                                                                            List<ElementStatus> limitResultsByStatus,
                                                                            Date                asOfTime,
                                                                            SequencingOrder     sequencingOrder,
                                                                            String              sequencingProperty,
                                                                            int                 startFrom,
                                                                            int                 pageSize,
                                                                            boolean             forLineage,
                                                                            boolean             forDuplicateProcessing,
                                                                            Date                effectiveTime) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException;


}
