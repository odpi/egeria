/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.api;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionGraph;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionMember;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The CollectionsInterface adds methods for managing collections.  Collections are managed lists of elements. They can be used to create a folder
 * type hierarchy for, say, assets and digital products.  They are used to represent digital products and lists of favourites.
 */
public interface CollectionsInterface
{
    /**
     * Returns the list of collections that are linked off of the supplied element.
     *
     * @param userId     userId of user making request
     * @param parentGUID unique identifier of referenceable object (typically a personal profile, project or
     *                   community) that the collections hang off of
     * @param collectionType filter response by collection type - if null, any value will do
     * @param templateFilter  should templates be returned?
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionElement> getAttachedCollections(String              userId,
                                                   String              parentGUID,
                                                   String              collectionType,
                                                   TemplateFilter      templateFilter,
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
     * Returns the list of collections with a particular classification.
     *
     * @param userId     userId of user making request
     * @param classificationName name of the classification - if null, all collections are returned
     * @param templateFilter  should templates be returned?
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionElement> getClassifiedCollections(String              userId,
                                                     String              classificationName,
                                                     TemplateFilter      templateFilter,
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
     * Returns the list of collections matching the search string - this is coded as a regular expression.
     *
     * @param userId     userId of user making request
     * @param classificationName option name of a collection classification
     * @param searchString string to search for
     * @param templateFilter  should templates be returned?
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionElement> findCollections(String              userId,
                                            String              classificationName,
                                            String              searchString,
                                            TemplateFilter      templateFilter,
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
     * Returns the list of collections with a particular name.
     *
     * @param userId     userId of user making request
     * @param classificationName option name of a collection classification
     * @param name       name of the collections to return - match is full text match in qualifiedName or name
     * @param templateFilter  should templates be returned?
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionElement> getCollectionsByName(String              userId,
                                                 String              classificationName,
                                                 String              name,
                                                 TemplateFilter      templateFilter,
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
     * Returns the list of collections with a particular collectionType.  This is an optional text field in the collection element.
     *
     * @param userId     userId of user making request
     * @param classificationName option name of a collection classification
     * @param collectionType the collection type value to match on.  If it is null, all collections with a null collectionType are returned
     * @param templateFilter  should templates be returned?
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return a list of collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionElement> getCollectionsByType(String              userId,
                                                 String              classificationName,
                                                 String              collectionType,
                                                 TemplateFilter      templateFilter,
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
     * Return the properties of a specific collection.
     *
     * @param userId                 userId of user making request
     * @param collectionGUID         unique identifier of the required collection
     * @param asOfTime               repository time to use
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return collection properties
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    CollectionElement getCollection(String  userId,
                                    String  collectionGUID,
                                    Date    asOfTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException;


    /**
     * Create a new generic collection.
     *
     * @param userId                 userId of user making request.
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID optional scope of the anchor
     * @param optionalClassification classification of the collections - typically RootCollection, Set or Folder
     * @param properties             properties for the collection.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the newly created Collection
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createCollection(String               userId,
                            String               anchorGUID,
                            boolean              isOwnAnchor,
                            String               anchorScopeGUID,
                            String               optionalClassification,
                            CollectionProperties properties,
                            String               parentGUID,
                            String               parentRelationshipTypeName,
                            ElementProperties    parentRelationshipProperties,
                            boolean              parentAtEnd1,
                            Date                 effectiveTime) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;



    /**
     * Create a new metadata element to represent a collection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new collection.
     *
     * @param userId             calling user
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID optional scope of the anchor
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
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createCollectionFromTemplate(String              userId,
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
                                        Date                effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;



    /**
     * Create a new collection that represents a digital product.
     *
     * @param userId                   userId of user making request.
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID optional scope of the anchor
     * @param collectionProperties     properties for the collection.
     * @param digitalProductProperties properties for the attached DigitalProduct classification
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the newly created Collection
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.*
     */
    String createDigitalProduct(String                   userId,
                                String                   anchorGUID,
                                boolean                  isOwnAnchor,
                                String                   anchorScopeGUID,
                                CollectionProperties     collectionProperties,
                                DigitalProductProperties digitalProductProperties,
                                String                   parentGUID,
                                String                   parentRelationshipTypeName,
                                ElementProperties        parentRelationshipProperties,
                                boolean                  parentAtEnd1,
                                Date                     effectiveTime) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException;


    /**
     * Update the properties of a collection.
     *
     * @param userId                 userId of user making request.
     * @param collectionGUID         unique identifier of the collection (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the collection.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   updateCollection(String               userId,
                            String               collectionGUID,
                            boolean              replaceAllProperties,
                            CollectionProperties properties,
                            Date                 effectiveTime) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;


    /**
     * Update the properties of the DigitalProduct classification attached to a collection.
     *
     * @param userId                 userId of user making request.
     * @param collectionGUID         unique identifier of the collection (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the DigitalProduct classification.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   updateDigitalProduct(String                   userId,
                                String                   collectionGUID,
                                boolean                  replaceAllProperties,
                                DigitalProductProperties properties,
                                Date                     effectiveTime) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException;


    /**
     * Connect an existing collection to an element using the ResourceList relationship (0019).
     *
     * @param userId          userId of user making request
     * @param collectionGUID  unique identifier of the collection
     * @param parentGUID      unique identifier of referenceable object that the collection should be attached to
     * @param collectionUse   description of how the collection will be used.
     * @param makeAnchor      like the lifecycle of the collection to that of the parent so that if the parent is deleted, so is the collection
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void attachCollection(String                 userId,
                          String                 collectionGUID,
                          String                 parentGUID,
                          ResourceListProperties collectionUse,
                          boolean                makeAnchor,
                          Date                   effectiveTime) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;


    /**
     * Detach an existing collection from an element.  If the collection is anchored to the element, it is deleted.
     *
     * @param userId          userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param parentGUID      unique identifier of referenceable object that the collection should be attached to.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void detachCollection(String userId,
                          String collectionGUID,
                          String parentGUID,
                          Date   effectiveTime) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException;


    /**
     * Delete a collection.  It is detached from all parent elements.  If members are anchored to the collection
     * then they are also deleted.
     *
     * @param userId         userId of user making request.
     * @param collectionGUID unique identifier of the collection
     * @param cascadedDelete should nested collections be deleted? If false, the delete fails if there are nested
     *                       collections.  If true, nested collections are delete - but not member elements
     *                       unless they are anchored to the collection
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void deleteCollection(String userId,
                          String  collectionGUID,
                          boolean cascadedDelete) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException;


    /**
     * Return a list of elements that are a member of a collection.
     *
     * @param userId     userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return list of collection details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<CollectionMember> getCollectionMembers(String              userId,
                                                String              collectionGUID,
                                                List<ElementStatus> limitResultsByStatus,
                                                Date                asOfTime,
                                                String              sequencingProperty,
                                                SequencingOrder     sequencingOrder,
                                                boolean             forLineage,
                                                boolean             forDuplicateProcessing,
                                                Date                effectiveTime,
                                                int                 startFrom,
                                                int                 pageSize) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException;


    /**
     * Return a graph of elements that are the nested members of a collection along
     * with elements immediately connected to the starting collection.  The result
     * includes a mermaid graph of the returned elements.
     *
     * @param userId     userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return list of asset details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    CollectionGraph getCollectionGraph(String              userId,
                                       String              collectionGUID,
                                       List<ElementStatus> limitResultsByStatus,
                                       Date                asOfTime,
                                       String              sequencingProperty,
                                       SequencingOrder     sequencingOrder,
                                       boolean             forLineage,
                                       boolean             forDuplicateProcessing,
                                       Date                effectiveTime,
                                       int                 startFrom,
                                       int                 pageSize) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException;


    /**
     * Add an element to a collection.
     *
     * @param userId     userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param membershipProperties properties describing the membership characteristics.
     * @param elementGUID  unique identifier of the element.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  addToCollection(String                         userId,
                          String                         collectionGUID,
                          CollectionMembershipProperties membershipProperties,
                          String                         elementGUID,
                          Date                           effectiveTime) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException;


    /**
     * Update an element's membership to a collection.
     *
     * @param userId     userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param membershipProperties properties describing the membership characteristics.
     * @param elementGUID  unique identifier of the element.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  updateCollectionMembership(String                         userId,
                                     String                         collectionGUID,
                                     boolean                        replaceAllProperties,
                                     CollectionMembershipProperties membershipProperties,
                                     String                         elementGUID,
                                     Date                           effectiveTime) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException;


    /**
     * Remove an element from a collection.
     *
     * @param userId     userId of user making request.
     * @param collectionGUID  unique identifier of the collection.
     * @param elementGUID  unique identifier of the element.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  removeFromCollection(String userId,
                               String collectionGUID,
                               String elementGUID,
                               Date   effectiveTime) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException;
}
