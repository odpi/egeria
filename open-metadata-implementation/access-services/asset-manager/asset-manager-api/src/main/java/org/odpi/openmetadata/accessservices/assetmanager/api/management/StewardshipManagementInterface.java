/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.api.management;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetOriginProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GovernanceClassificationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.OwnerProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.RetentionClassificationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SecurityTagsProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SemanticAssignmentProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SubjectAreaMemberProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.util.Date;
import java.util.List;


/**
 * The StewardshipManagementInterface supports the exchange of relationships (such as SemanticAssignment)
 * and classifications that are added by stewards (or automated stewardship processes) such as Confidentiality.
 */
public interface StewardshipManagementInterface
{
    /**
     * Classify/reclassify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param properties details of the classification
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setConfidenceClassification(String                             userId,
                                     String                             elementGUID,
                                     GovernanceClassificationProperties properties,
                                     Date                               effectiveTime,
                                     boolean                            forLineage,
                                     boolean                            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException;


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearConfidenceClassification(String  userId,
                                       String  elementGUID,
                                       Date    effectiveTime,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Return information about the elements classified with the confidence classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ElementStub> getConfidenceClassifiedElements(String  userId,
                                                      boolean returnSpecificLevel,
                                                      int     levelIdentifier,
                                                      int     startFrom,
                                                      int     pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Classify/reclassify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param properties details of the classification
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setCriticalityClassification(String                             userId,
                                      String                             elementGUID,
                                      GovernanceClassificationProperties properties,
                                      Date                               effectiveTime,
                                      boolean                            forLineage,
                                      boolean                            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException;


    /**
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearCriticalityClassification(String  userId,
                                        String  elementGUID,
                                        Date    effectiveTime,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Return information about the elements classified with the criticality classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ElementStub> getCriticalityClassifiedElements(String  userId,
                                                       boolean returnSpecificLevel,
                                                       int     levelIdentifier,
                                                       int     startFrom,
                                                       int     pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Classify/reclassify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param properties details of the classification
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setConfidentialityClassification(String                             userId,
                                          String                             elementGUID,
                                          GovernanceClassificationProperties properties,
                                          Date                               effectiveTime,
                                          boolean                            forLineage,
                                          boolean                            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException;


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearConfidentialityClassification(String  userId,
                                            String  elementGUID,
                                            Date    effectiveTime,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;


    /**
     * Return information about the elements classified with the confidentiality classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ElementStub> getConfidentialityClassifiedElements(String  userId,
                                                           boolean returnSpecificLevel,
                                                           int     levelIdentifier,
                                                           int     startFrom,
                                                           int     pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Classify/reclassify the element (typically an asset) to indicate how long the element (or associated resource)
     * is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis
     * property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter
     * properties respectively.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param properties details of the classification
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setRetentionClassification(String                            userId,
                                    String                            elementGUID,
                                    RetentionClassificationProperties properties,
                                    Date                              effectiveTime,
                                    boolean                           forLineage,
                                    boolean                           forDuplicateProcessing) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException;


    /**
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearRetentionClassification(String  userId,
                                      String  elementGUID,
                                      Date    effectiveTime,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;


    /**
     * Return information about the elements classified with the retention classification.
     *
     * @param userId calling user
     * @param returnSpecificBasisIdentifier should the results be filtered by basisIdentifier?
     * @param basisIdentifier the identifier to filter by (if returnSpecificBasisIdentifier=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ElementStub> getRetentionClassifiedElements(String  userId,
                                                     boolean returnSpecificBasisIdentifier,
                                                     int     basisIdentifier,
                                                     int     startFrom,
                                                     int     pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;



    /**
     * Add or replace the security tags for an element.
     *
     * @param userId calling user
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param properties details of the security tags
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  addSecurityTags(String                 userId,
                          String                 elementGUID,
                          SecurityTagsProperties properties,
                          Date                   effectiveTime,
                          boolean                forLineage,
                          boolean                forDuplicateProcessing) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Remove the security tags classification from an element.
     *
     * @param userId calling user
     * @param elementGUID element where the security tags need to be removed.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void clearSecurityTags(String  userId,
                           String  elementGUID,
                           Date    effectiveTime,
                           boolean forLineage,
                           boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ElementStub> getSecurityTaggedElements(String userId,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Add or replace the ownership classification for an element.
     *
     * @param userId calling user
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param properties details of the ownership
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  addOwnership(String          userId,
                       String          elementGUID,
                       OwnerProperties properties,
                       Date            effectiveTime,
                       boolean         forLineage,
                       boolean         forDuplicateProcessing) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Remove the ownership classification from an element.
     *
     * @param userId calling user
     * @param elementGUID element where the classification needs to be removed.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void clearOwnership(String  userId,
                        String  elementGUID,
                        Date    effectiveTime,
                        boolean forLineage,
                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param owner unique identifier for the owner
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ElementStub> getOwnersElements(String userId,
                                        String owner,
                                        int    startFrom,
                                        int    pageSize) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Add or replace the origin classification for an asset.
     *
     * @param userId calling user
     * @param assetGUID element to link it to - its type must inherit from Asset.
     * @param properties details of the origin
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  addAssetOrigin(String                userId,
                         String                assetGUID,
                         AssetOriginProperties properties,
                         Date                  effectiveTime,
                         boolean               forLineage,
                         boolean               forDuplicateProcessing) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Remove the origin classification from an asset.
     *
     * @param userId calling user
     * @param assetGUID element where the classification needs to be removed.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void clearAssetOrigin(String  userId,
                          String  assetGUID,
                          Date    effectiveTime,
                          boolean forLineage,
                          boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Return information about the assets from a specific origin.
     *
     * @param userId calling user
     * @param properties values to search on - null means any value
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of the assets
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<AssetElement> getAssetsByOrigin(String                userId,
                                         AssetOriginProperties properties,
                                         int                   startFrom,
                                         int                   pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Classify the element to assert that it is part of a subject area definition.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     * @param properties qualified name of subject area
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void addElementToSubjectArea(String                      userId,
                                 String                      elementGUID,
                                 SubjectAreaMemberProperties properties,
                                 Date                        effectiveTime,
                                 boolean                     forLineage,
                                 boolean                     forDuplicateProcessing) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;


    /**
     * Remove the subject area designation from the identified element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeElementFromSubjectArea(String  userId,
                                      String  elementGUID,
                                      Date    effectiveTime,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param subjectAreaName unique identifier for the subject area
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ElementStub> getMembersOfSubjectArea(String userId,
                                              String subjectAreaName,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param properties properties for relationship
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupSemanticAssignment(String                       userId,
                                 String                       elementGUID,
                                 String                       glossaryTermGUID,
                                 SemanticAssignmentProperties properties,
                                 Date                         effectiveTime,
                                 boolean                      forLineage,
                                 boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Remove a semantic assignment relationship between an element and its glossary term.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearSemanticAssignment(String  userId,
                                 String  elementGUID,
                                 String  glossaryTermGUID,
                                 Date    effectiveTime,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GlossaryTermElement> getMeanings(String userId,
                                          String elementGUID,
                                          int    startFrom,
                                          int    pageSize) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Retrieve the elements linked via a "SemanticAssignment" relationship to the requested glossary term.
     *
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedElement> getSemanticAssignees(String userId,
                                              String glossaryTermGUID,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param userId calling user
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to link
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void addGovernanceDefinitionToElement(String  userId,
                                          String  definitionGUID,
                                          String  elementGUID,
                                          Date    effectiveTime,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param userId calling user
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to update
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeGovernanceDefinitionFromElement(String  userId,
                                               String  definitionGUID,
                                               String  elementGUID,
                                               Date    effectiveTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


    /**
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to the requested element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceDefinitionElement> getGovernedByDefinitions(String userId,
                                                               String elementGUID,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;


    /**
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param userId calling user
     * @param governanceDefinitionGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedElement> getGovernedElements(String userId,
                                             String governanceDefinitionGUID,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;
}
