/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SequencingOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OpenMetadataClient provides access to metadata elements stored in the metadata repositories.  It is implemented by a
 * metadata repository provider. In Egeria, this class is implemented in the GAF Metadata Management running in the
 * Metadata Access Server OMAG Server.
 */
public abstract class OpenMetadataClient implements MetadataElementInterface,
                                                    StewardshipActionInterface,
                                                    MultiLanguageInterface,
                                                    ValidMetadataValuesInterface
{
    protected final String serverName;               /* Initialized in constructor */
    protected final String serviceURLMarker;         /* Initialized in constructor */
    protected final String serverPlatformURLRoot;    /* Initialized in constructor */


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     */
    public OpenMetadataClient(String serviceURLMarker,
                              String serverName,
                              String serverPlatformURLRoot)
    {
        this.serviceURLMarker = serviceURLMarker;
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param userId caller's userId
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
    public abstract OpenMetadataElement getMetadataElementByGUID(String  userId,
                                                                 String  elementGUID,
                                                                 boolean forLineage,
                                                                 boolean forDuplicateProcessing,
                                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param userId caller's userId
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or null if not found
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public abstract OpenMetadataElement getMetadataElementByUniqueName(String  userId,
                                                                       String  uniqueName,
                                                                       String  uniquePropertyName,
                                                                       boolean forLineage,
                                                                       boolean forDuplicateProcessing,
                                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException;


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param userId caller's userId
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
    public abstract String getMetadataElementGUIDByUniqueName(String  userId,
                                                              String  uniqueName,
                                                              String  uniquePropertyName,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing,
                                                              Date    effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param userId caller's userId
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
    public abstract List<OpenMetadataElement> findMetadataElementsWithString(String  userId,
                                                                             String  searchString,
                                                                             boolean forLineage,
                                                                             boolean forDuplicateProcessing,
                                                                             Date    effectiveTime,
                                                                             int     startFrom,
                                                                             int     pageSize) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException;


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param userId caller's userId
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
    public abstract List<RelatedMetadataElement> getRelatedMetadataElements(String  userId,
                                                                            String  elementGUID,
                                                                            int     startingAtEnd,
                                                                            String  relationshipTypeName,
                                                                            boolean forLineage,
                                                                            boolean forDuplicateProcessing,
                                                                            Date    effectiveTime,
                                                                            int     startFrom,
                                                                            int     pageSize) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException;


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId caller's userId
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
    public  abstract List<OpenMetadataElement> findMetadataElements(String                userId,
                                                                    String                metadataElementTypeName,
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
                                                                                                           PropertyServerException;


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param userId caller's userId
     * @param relationshipTypeName relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param searchProperties Optional list of relationship property conditions to match.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
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
    public  abstract List<RelatedMetadataElements> findRelationshipsBetweenMetadataElements(String           userId,
                                                                                            String           relationshipTypeName,
                                                                                            SearchProperties searchProperties,
                                                                                            String           sequencingProperty,
                                                                                            SequencingOrder  sequencingOrder,
                                                                                            boolean          forLineage,
                                                                                            boolean          forDuplicateProcessing,
                                                                                            Date             effectiveTime,
                                                                                            int              startFrom,
                                                                                            int              pageSize) throws InvalidParameterException,
                                                                                                                              UserNotAuthorizedException,
                                                                                                                              PropertyServerException;


    /**
     * Retrieve the relationship using its unique identifier.
     *
     * @param relationshipGUID unique identifier for the relationship
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return relationship properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public  abstract RelatedMetadataElements getRelationshipByGUID(String  userId,
                                                                   String  relationshipGUID,
                                                                   boolean forLineage,
                                                                   boolean forDuplicateProcessing,
                                                                   Date    effectiveTime) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId caller's userId
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
    public abstract String createMetadataElementInStore(String            userId,
                                                        String            metadataElementTypeName,
                                                        ElementStatus     initialStatus,
                                                        Date              effectiveFrom,
                                                        Date              effectiveTo,
                                                        ElementProperties properties,
                                                        String            templateGUID) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param userId caller's userId
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
    public abstract void updateMetadataElementInStore(String            userId,
                                                      String            metadataElementGUID,
                                                      boolean           replaceProperties,
                                                      boolean           forLineage,
                                                      boolean           forDuplicateProcessing,
                                                      ElementProperties properties,
                                                      Date              effectiveTime) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param newElementStatus new status value - or null to leave as is
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void updateMetadataElementStatusInStore(String        userId,
                                                            String        metadataElementGUID,
                                                            boolean       forLineage,
                                                            boolean       forDuplicateProcessing,
                                                            ElementStatus newElementStatus,
                                                            Date          effectiveTime) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
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
    public abstract void updateMetadataElementEffectivityInStore(String        userId,
                                                                 String        metadataElementGUID,
                                                                 boolean       forLineage,
                                                                 boolean       forDuplicateProcessing,
                                                                 Date          effectiveFrom,
                                                                 Date          effectiveTo,
                                                                 Date          effectiveTime) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException;


    /**
     * Delete a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void deleteMetadataElementInStore(String  userId,
                                                      String  metadataElementGUID,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing,
                                                      Date    effectiveTime) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this classification is active - null for active now
     * @param effectiveTo the date when this classification becomes inactive - null for active until deleted
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void classifyMetadataElementInStore(String            userId,
                                                        String            metadataElementGUID,
                                                        String            classificationName,
                                                        boolean           forLineage,
                                                        boolean           forDuplicateProcessing,
                                                        Date              effectiveFrom,
                                                        Date              effectiveTo,
                                                        ElementProperties properties,
                                                        Date              effectiveTime) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties new properties for the classification
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void reclassifyMetadataElementInStore(String            userId,
                                                          String            metadataElementGUID,
                                                          String            classificationName,
                                                          boolean           replaceProperties,
                                                          boolean           forLineage,
                                                          boolean           forDuplicateProcessing,
                                                          ElementProperties properties,
                                                          Date              effectiveTime) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;

    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void updateClassificationEffectivityInStore(String  userId,
                                                                String  metadataElementGUID,
                                                                String  classificationName,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing,
                                                                Date    effectiveFrom,
                                                                Date    effectiveTo,
                                                                Date    effectiveTime) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to remove this classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void declassifyMetadataElementInStore(String  userId,
                                                          String  metadataElementGUID,
                                                          String  classificationName,
                                                          boolean forLineage,
                                                          boolean forDuplicateProcessing,
                                                          Date    effectiveTime) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param userId caller's userId
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties the properties of the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract String createRelatedElementsInStore(String            userId,
                                                        String            relationshipTypeName,
                                                        String            metadataElement1GUID,
                                                        String            metadataElement2GUID,
                                                        boolean           forLineage,
                                                        boolean           forDuplicateProcessing,
                                                        Date              effectiveFrom,
                                                        Date              effectiveTo,
                                                        ElementProperties properties,
                                                        Date              effectiveTime) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Update the properties associated with a relationship.
     *
     * @param userId caller's userId
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
    public abstract void updateRelatedElementsInStore(String            userId,
                                                      String            relationshipGUID,
                                                      boolean           replaceProperties,
                                                      boolean           forLineage,
                                                      boolean           forDuplicateProcessing,
                                                      ElementProperties properties,
                                                      Date              effectiveTime) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId caller's userId
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
    public abstract void updateRelatedElementsEffectivityInStore(String  userId,
                                                                 String  relationshipGUID,
                                                                 boolean forLineage,
                                                                 boolean forDuplicateProcessing,
                                                                 Date    effectiveFrom,
                                                                 Date    effectiveTo,
                                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param userId caller's userId
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
    public abstract void deleteRelatedElementsInStore(String  userId,
                                                      String  relationshipGUID,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing,
                                                      Date    effectiveTime) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param userId caller's userId
     * @param processQualifiedName unique name of the governance action process to use
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargets map of action target names to GUIDs for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible"
     * @param requestParameters request properties to be passed to the first governance action
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the first governance action of the process
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public abstract String initiateGovernanceActionProcess(String                userId,
                                                           String                processQualifiedName,
                                                           List<String>          requestSourceGUIDs,
                                                           List<NewActionTarget> actionTargets,
                                                           Date                  startTime,
                                                           Map<String, String>   requestParameters,
                                                           String                originatorServiceName,
                                                           String                originatorEngineName) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException;


    /**
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param userId caller's userId
     * @param qualifiedName unique identifier to give this new incident report
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param background description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param incidentClassifiers initial classifiers for the incident report
     * @param additionalProperties additional arbitrary properties for the incident reports
     * @param originatorGUID the unique identifier of the person or process that created the incident
     *
     * @return unique identifier of the resulting incident report
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a incident report
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract String createIncidentReport(String                        userId,
                                                String                        qualifiedName,
                                                int                           domainIdentifier,
                                                String                        background,
                                                List<IncidentImpactedElement> impactedResources,
                                                List<IncidentDependency>      previousIncidents,
                                                Map<String, Integer>          incidentClassifiers,
                                                Map<String, String>           additionalProperties,
                                                String                        originatorGUID) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException;

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenMetadataClient{" +
                "serverName='" + serverName + '\'' +
                ", serviceURLMarker='" + serviceURLMarker + '\'' +
                ", serverPlatformURLRoot='" + serverPlatformURLRoot + '\'' +
                '}';
    }
}
