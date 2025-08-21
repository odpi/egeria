/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.api;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * MetadataElementInterface provides an interface to the open metadata store.  This is part of the Open Survey Framework (OGF)
 * and provides a comprehensive interface for working with all types of metadata, subject to the user's (and this OMAS's) security permissions.
 * The interface supports search, maintenance of metadata elements, classifications and relationships.
 */
public interface MetadataElementInterface
{
    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier for the metadata element
     * @param getOptions multiple options to control the query
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    OpenMetadataElement getMetadataElementByGUID(String     userId,
                                                 String     elementGUID,
                                                 GetOptions getOptions) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param userId caller's userId
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param getOptions multiple options to control the query
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    OpenMetadataElement getMetadataElementByUniqueName(String     userId,
                                                       String     uniqueName,
                                                       String     uniquePropertyName,
                                                       GetOptions getOptions) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;

    /**
     * Retrieve the metadata element using its unique name (typically the qualified name) and the DELETED status.
     * This method assumes all effective dates, and forLineage and forDuplicateProcessing is set to false,
     * to cast the widest net.
     *
     * @param userId                 caller's userId
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     *
     * @return metadata element properties or null if not found
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    OpenMetadataElement getDeletedElementByUniqueName(String  userId,
                                                      String  uniqueName,
                                                      String  uniquePropertyName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;

    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param userId caller's userId
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param getOptions multiple options to control the query
     *
     * @return metadata element unique identifier (guid)
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    String getMetadataElementGUIDByUniqueName(String     userId,
                                              String     uniqueName,
                                              String     uniquePropertyName,
                                              GetOptions getOptions) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Retrieve the metadata elements of the requested type that contain the requested string.
     *
     * @param userId caller's userId
     * @param searchString name to retrieve
     * @param searchOptions multiple options to control the query
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    List<OpenMetadataElement> findMetadataElementsWithString(String        userId,
                                                             String        searchString,
                                                             SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied anchorGUID.
     *
     * @param userId caller's userId
     * @param searchString name to retrieve
     * @param anchorGUID unique identifier of anchor
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    AnchorSearchMatches findElementsForAnchor(String              userId,
                                              String              searchString,
                                              String              anchorGUID,
                                              QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied domain name. The results are organized by anchor element.
     *
     * @param userId caller's userId
     * @param searchString name to retrieve
     * @param anchorDomainName name of open metadata type for the domain
     * @param searchOptions multiple options to control the query
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    List<AnchorSearchMatches> findElementsInAnchorDomain(String        userId,
                                                         String        searchString,
                                                         String        anchorDomainName,
                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied scope guid. The results are organized by anchor element.
     *
     * @param userId caller's userId
     * @param searchString name to retrieve
     * @param anchorScopeGUID unique identifier of the scope to use
     * @param searchOptions multiple options to control the query
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    List<AnchorSearchMatches> findElementsInAnchorScope(String        userId,
                                                        String        searchString,
                                                        String        anchorScopeGUID,
                                                        SearchOptions searchOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier for the starting metadata element
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    RelatedMetadataElementList getRelatedMetadataElements(String       userId,
                                                          String       elementGUID,
                                                          int          startingAtEnd,
                                                          String       relationshipTypeName,
                                                          QueryOptions queryOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;


    /**
     * Retrieve the metadata element connected to the supplied element for a relationship type that only allows one
     * relationship to be attached.
     *
     * @param userId                 caller's userId
     * @param elementGUID            unique identifier for the starting metadata element
     * @param startingAtEnd          indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName   type name of relationships to follow (or null for all)
     * @param getOptions             multiple options to control the query
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store or multiple relationships have been returned
     */
    RelatedMetadataElement getRelatedMetadataElement(String     userId,
                                                     String     elementGUID,
                                                     int        startingAtEnd,
                                                     String     relationshipTypeName,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;


    /**
     * Return all the elements that are anchored to a requested element plus relationships between these elements and to other elements.
     *
     * @param userId name of the server instances for this request
     * @param elementGUID  unique identifier for the element
     * @param queryOptions multiple options to control the query
     *
     * @return graph of elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    OpenMetadataElementGraph getAnchoredElementsGraph(String       userId,
                                                      String       elementGUID,
                                                      QueryOptions queryOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Return each of the versions of a metadata element.
     *
     * @param userId caller's userId
     * @param elementGUID            unique identifier for the metadata element
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    List<OpenMetadataElement> getMetadataElementHistory(String                 userId,
                                                        String                 elementGUID,
                                                        HistoricalQueryOptions queryOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;


    /**
     * Retrieve the relationships linking to the supplied elements.
     *
     * @param userId caller's userId
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    OpenMetadataRelationshipList getMetadataElementRelationships(String              userId,
                                                                 String              metadataElementAtEnd1GUID,
                                                                 String              metadataElementAtEnd2GUID,
                                                                 String              relationshipTypeName,
                                                                 QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException;

    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId caller's userId
     * @param searchProperties Optional list of entity property conditions to match.
     * @param matchClassifications Optional list of classifications to match.
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    List<OpenMetadataElement> findMetadataElements(String                userId,
                                                   SearchProperties      searchProperties,
                                                   SearchClassifications matchClassifications,
                                                   QueryOptions          queryOptions) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param userId caller's userId
     * @param relationshipTypeName relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param searchProperties Optional list of relationship property conditions to match.
     * @param queryOptions multiple options to control the query
     *
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    OpenMetadataRelationshipList findRelationshipsBetweenMetadataElements(String           userId,
                                                                          String           relationshipTypeName,
                                                                          SearchProperties searchProperties,
                                                                          QueryOptions     queryOptions) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException;


    /**
     * Retrieve the relationship using its unique identifier.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier for the relationship
     * @param getOptions multiple options to control the query
     *
     * @return relationship properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    OpenMetadataRelationship getRelationshipByGUID(String     userId,
                                                   String     relationshipGUID,
                                                   GetOptions getOptions) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Return each of the versions of a relationship.
     *
     * @param userId caller's userId
     * @param relationshipGUID            unique identifier for the relationship
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    OpenMetadataRelationshipList getRelationshipHistory(String                 userId,
                                                        String                 relationshipGUID,
                                                        HistoricalQueryOptions queryOptions) throws InvalidParameterException,
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
     * @param properties properties of the new metadata element
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String createMetadataElementInStore(String               userId,
                                        String               metadataElementTypeName,
                                        ElementStatus        initialStatus,
                                        NewElementProperties properties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param properties properties of the new metadata element
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String createMetadataElementInStore(String               userId,
                                        String               externalSourceGUID,
                                        String               externalSourceName,
                                        String               metadataElementTypeName,
                                        ElementStatus        initialStatus,
                                        NewElementProperties properties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId caller's userId
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties properties of the new metadata element
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String createMetadataElementInStore(String                            userId,
                                        String                            metadataElementTypeName,
                                        NewElementOptions                 newElementOptions,
                                        Map<String, NewElementProperties> initialClassifications,
                                        NewElementProperties              properties,
                                        NewElementProperties              parentRelationshipProperties) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException;


    /**
     * Create a new metadata element in the metadata store using the template identified by the templateGUID.
     * The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * The template and any similar anchored objects are
     * copied in this process.
     *
     * @param userId caller's userId
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the placeholder values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String createMetadataElementFromTemplate(String               userId,
                                             String               metadataElementTypeName,
                                             TemplateOptions      templateOptions,
                                             String               templateGUID,
                                             ElementProperties    replacementProperties,
                                             Map<String, String>  placeholderProperties,
                                             NewElementProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException;


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceAllProperties flag.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void updateMetadataElementInStore(String            userId,
                                      String            metadataElementGUID,
                                      UpdateOptions     updateOptions,
                                      ElementProperties properties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param metadataSourceOptions  options to control access to open metadata
     * @param newElementStatus new status value - or null to leave as is
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void updateMetadataElementStatusInStore(String                userId,
                                            String                metadataElementGUID,
                                            MetadataSourceOptions metadataSourceOptions,
                                            ElementStatus         newElementStatus) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param metadataSourceOptions  options to control access to open metadata
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void updateMetadataElementEffectivityInStore(String                userId,
                                                 String                metadataElementGUID,
                                                 MetadataSourceOptions metadataSourceOptions,
                                                 Date                  effectiveFrom,
                                                 Date                  effectiveTo) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Delete a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void deleteMetadataElementInStore(String        userId,
                                      String        metadataElementGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;

    /**
     * Archive a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param deleteOptions description of the archiving process
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void archiveMetadataElementInStore(String        userId,
                                       String        metadataElementGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void classifyMetadataElementInStore(String                userId,
                                        String                metadataElementGUID,
                                        String                classificationName,
                                        MetadataSourceOptions metadataSourceOptions,
                                        NewElementProperties  properties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param updateOptions provides a structure for the additional options when updating a classification.
     * @param properties new properties for the classification
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void reclassifyMetadataElementInStore(String            userId,
                                          String            metadataElementGUID,
                                          String            classificationName,
                                          UpdateOptions     updateOptions,
                                          ElementProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param metadataSourceOptions  options to control access to open metadata
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void updateClassificationEffectivityInStore(String                userId,
                                                String                metadataElementGUID,
                                                String                classificationName,
                                                MetadataSourceOptions metadataSourceOptions,
                                                Date                  effectiveFrom,
                                                Date                  effectiveTo) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void declassifyMetadataElementInStore(String                userId,
                                          String                metadataElementGUID,
                                          String                classificationName,
                                          MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
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
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties the properties of the relationship
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String createRelatedElementsInStore(String                userId,
                                        String                relationshipTypeName,
                                        String                metadataElement1GUID,
                                        String                metadataElement2GUID,
                                        MetadataSourceOptions metadataSourceOptions,
                                        NewElementProperties  properties) throws InvalidParameterException,
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
     * @param makeAnchorOptions  options to control access to open metadata
     * @param properties the properties of the relationship
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    String createRelatedElementsInStore(String                userId,
                                        String                relationshipTypeName,
                                        String                metadataElement1GUID,
                                        String                metadataElement2GUID,
                                        MakeAnchorOptions     makeAnchorOptions,
                                        NewElementProperties  properties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Update the properties associated with a relationship.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param updateOptions provides a structure for the additional options when updating a relationship.
     * @param properties new properties for the relationship
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void updateRelationshipInStore(String            userId,
                                   String            relationshipGUID,
                                   UpdateOptions     updateOptions,
                                   ElementProperties properties) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Delete all relationships of a particular type between two metadata elements.
     *
     * @param userId caller's userId
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param updateOptions provides a structure for the additional options when updating a relationship.
     * @param properties new properties for the relationship
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void updateRelatedElementsInStore(String            userId,
                                      String            relationshipTypeName,
                                      String            metadataElement1GUID,
                                      String            metadataElement2GUID,
                                      UpdateOptions     updateOptions,
                                      ElementProperties properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param metadataSourceOptions  options to control access to open metadata
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void updateRelationshipEffectivityInStore(String                userId,
                                              String                relationshipGUID,
                                              MetadataSourceOptions metadataSourceOptions,
                                              Date                  effectiveFrom,
                                              Date                  effectiveTo) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void deleteRelationshipInStore(String        userId,
                                   String        relationshipGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;


    /**
     * Delete all relationships of a particular type between two metadata elements.
     *
     * @param userId caller's userId
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void detachRelatedElementsInStore(String        userId,
                                      String        relationshipTypeName,
                                      String        metadataElement1GUID,
                                      String        metadataElement2GUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;
}
