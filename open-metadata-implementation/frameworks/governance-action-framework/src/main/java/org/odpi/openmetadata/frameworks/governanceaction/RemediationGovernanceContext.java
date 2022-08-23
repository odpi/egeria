/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;

import java.util.Date;
import java.util.List;


/**
 * RemediationGovernanceContext provides access to the remediation request along with access to the
 * metadata store and APIs to enable changes to a wide range of metadata elements.
 *
 * A remediation service is typically making updates to the actionTargetElements. It may use
 * information from the requestSourceElements to make the changes.  For example,
 * a remediation service that is correcting the schema elements for an asset element could find the
 * asset to update in the actionTargetElements and maybe the schema elements in error.
 * The requestSourceElements could then have the schema annotations created by a discovery service.
 * These would guide the
 */
public interface RemediationGovernanceContext extends GovernanceContext
{
    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This metadata element will be given an initial status of ACTIVE which is sufficient for most types of elements.
     *
     * @param metadataElementTypeName type name of the new metadata element
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
    String createMetadataElement(String            metadataElementTypeName,
                                 ElementProperties properties,
                                 String            templateGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


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
    String createMetadataElement(String            metadataElementTypeName,
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
    void updateMetadataElement(String            metadataElementGUID,
                               boolean           replaceProperties,
                               boolean           forLineage,
                               boolean           forDuplicateProcessing,
                               ElementProperties properties,
                               Date              effectiveTime) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
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
    void updateMetadataElementStatus(String        metadataElementGUID,
                                     boolean       forLineage,
                                     boolean       forDuplicateProcessing,
                                     ElementStatus newElementStatus,
                                     Date          effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
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
    void updateMetadataElementEffectivity(String        metadataElementGUID,
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
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void deleteMetadataElement(String  metadataElementGUID,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void classifyMetadataElement(String            metadataElementGUID,
                                 String            classificationName,
                                 boolean           forLineage,
                                 boolean           forDuplicateProcessing,
                                 ElementProperties properties,
                                 Date              effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
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
    void classifyMetadataElement(String            metadataElementGUID,
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
    void reclassifyMetadataElement(String            metadataElementGUID,
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
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
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
    void updateClassificationStatus(String  metadataElementGUID,
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
    void unclassifyMetadataElement(String  metadataElementGUID,
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
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
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
    String createRelatedElements(String            relationshipTypeName,
                                 String            metadataElement1GUID,
                                 String            metadataElement2GUID,
                                 boolean           forLineage,
                                 boolean           forDuplicateProcessing,
                                 ElementProperties properties,
                                 Date              effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
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
    String createRelatedElements(String            relationshipTypeName,
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
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties new properties for the classification
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void updateRelatedElements(String            relationshipGUID,
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
    void updateRelatedElementsStatus(String  relationshipGUID,
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
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void deleteRelatedElements(String  relationshipGUID,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


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
    void linkElementsAsPeerDuplicates(String  metadataElement1GUID,
                                      String  metadataElement2GUID,
                                      int     statusIdentifier,
                                      String  steward,
                                      String  stewardTypeName,
                                      String  stewardPropertyName,
                                      String  source,
                                      String  notes,
                                      boolean setKnownDuplicate) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Identify an element that acts as a consolidated version for a set of duplicate elements.
     * (The consolidated element is created using createMetadataElement.)
     * Creates a simple relationship between the elements. If the ConsolidatedDuplicate
     * classification already exists, the properties are updated.
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
    void linkConsolidatedDuplicate(String       consolidatedElementGUID,
                                   int          statusIdentifier,
                                   String       steward,
                                   String       stewardTypeName,
                                   String       stewardPropertyName,
                                   String       source,
                                   String       notes,
                                   List<String> sourceElementGUIDs) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;
}
