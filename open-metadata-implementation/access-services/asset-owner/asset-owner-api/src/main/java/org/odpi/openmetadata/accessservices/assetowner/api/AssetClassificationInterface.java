/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.api;


import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DataFieldValuesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.RetentionClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SemanticAssignmentProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * The AssetClassificationInterface is used by the asset owner to add classifications and detailed definitions
 * to the asset.  Typically, this interface is used after the discovery services have explored the asset's content
 * and created helpful annotations to guide the asset owner.
 */
public interface AssetClassificationInterface
{
    /**
     * Create a simple relationship between a glossary term and an element in an Asset description (typically
     * an attribute in the schema).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     *                         If null then the assetGUID is used.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  addSemanticAssignment(String    userId,
                                String    assetGUID,
                                String    glossaryTermGUID,
                                String    assetElementGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetElementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param properties properties for relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void addSemanticAssignment(String                       userId,
                               String                       assetGUID,
                               String                       assetElementGUID,
                               String                       glossaryTermGUID,
                               SemanticAssignmentProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Remove the relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     *                         If null then the assetGUID is used.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  removeSemanticAssignment(String    userId,
                                   String    assetGUID,
                                   String    glossaryTermGUID,
                                   String    assetElementGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Add or replace the asset origin classification to an asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param organizationGUID Unique identifier (GUID) of the organization where this asset originated from - or null
     * @param businessCapabilityGUID  Unique identifier (GUID) of the business capability where this asset originated from.
     * @param otherOriginValues Descriptive labels describing origin of the asset
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
    */
    void  addAssetOrigin(String                userId,
                         String                assetGUID,
                         String                organizationGUID,
                         String                businessCapabilityGUID,
                         Map<String, String>   otherOriginValues) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Remove the asset origin classification to an asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  removeAssetOrigin(String userId,
                            String assetGUID) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException;


    /**
     * Update the zones for a specific asset to the zone list specified in the publishZones.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void publishAsset(String                userId,
                      String                assetGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


    /**
     * Update the zones for a specific asset to the zone list specified in the defaultZones.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void withdrawAsset(String                userId,
                       String                assetGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Update the zones for a specific asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetZones list of zones for the asset - these values override the current values - null means belongs
     *                   to no zones.
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateAssetZones(String        userId,
                          String        assetGUID,
                          List<String>  assetZones) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Update the owner information for a specific asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param ownerId unique identifier/property of the owner - or null to clear the field
     * @param ownerTypeName name of the type of id provided above - or null to clear the field
     * @param ownerPropertyName name of the property that describes the ownerId
     *
     * @throws InvalidParameterException userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateAssetOwner(String    userId,
                          String    assetGUID,
                          String    ownerId,
                          String    ownerTypeName,
                          String    ownerPropertyName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;



    /**
     * Remove the ownership classification from an asset.
     *
     * @param userId calling user
     * @param assetGUID asset where the classification needs to be removed.
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void removeAssetOwner(String  userId,
                          String  assetGUID) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException;


    /**
     * Add or replace the security tags for an asset or one of its elements.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     *                         If null then the assetGUID is used.
     * @param securityLabels list of security labels defining the security characteristics of the element
     * @param securityProperties Descriptive labels describing the security properties of the element
     * @param accessGroups map from operation to list of security groups
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  addSecurityTags(String                    userId,
                          String                    assetGUID,
                          String                    assetElementGUID,
                          List<String>              securityLabels,
                          Map<String, Object>       securityProperties,
                          Map<String, List<String>> accessGroups) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Remove the security tags classification to an asset or one of its elements.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetElementGUID element where the security tags need to be removed.
     *                         If null then the assetGUID is used
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  removeSecurityTags(String userId,
                             String assetGUID,
                             String assetElementGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Classify an asset as suitable to be used as a template for cataloguing assets of a similar types.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to classify
     * @param name name of the template
     * @param versionIdentifier version identifier for the template eg 1.0
     * @param description description of when, where and how to use the template
     * @param additionalProperties any additional properties
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void addTemplateClassification(String              userId,
                                   String              assetGUID,
                                   String              name,
                                   String              versionIdentifier,
                                   String              description,
                                   Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Remove the classification that indicates that this asset can be used as a template.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to declassify
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void removeTemplateClassification(String userId,
                                      String assetGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Classify the element to indicate that it describes a data field and supply
     * properties that describe the characteristics of the data values found within.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     * @param properties descriptive properties for the data field
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setElementAsDataField(String                    userId,
                               String                    elementGUID,
                               DataFieldValuesProperties properties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Remove the data field designation from the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearElementAsDataField(String  userId,
                                 String  elementGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Classify/reclassify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param properties details of the classification
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setConfidenceClassification(String                             userId,
                                     String                             elementGUID,
                                     GovernanceClassificationProperties properties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearConfidenceClassification(String  userId,
                                       String  elementGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Classify/reclassify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param properties details of the classification
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setCriticalityClassification(String                             userId,
                                      String                             elementGUID,
                                      GovernanceClassificationProperties properties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;


    /**
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearCriticalityClassification(String  userId,
                                        String  elementGUID) throws InvalidParameterException,
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setConfidentialityClassification(String                             userId,
                                          String                             elementGUID,
                                          GovernanceClassificationProperties properties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearConfidentialityClassification(String  userId,
                                            String  elementGUID) throws InvalidParameterException,
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setRetentionClassification(String                            userId,
                                    String                            elementGUID,
                                    RetentionClassificationProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearRetentionClassification(String  userId,
                                      String  elementGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param userId calling user
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to link
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void addGovernanceDefinitionToElement(String  userId,
                                          String  definitionGUID,
                                          String  elementGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param userId calling user
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeGovernanceDefinitionFromElement(String  userId,
                                               String  definitionGUID,
                                               String  elementGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;
}
