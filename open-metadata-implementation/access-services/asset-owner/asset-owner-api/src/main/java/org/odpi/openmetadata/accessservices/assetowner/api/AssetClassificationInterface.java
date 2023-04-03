/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.api;


import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;

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
    void  removeAssetOrigin(String                userId,
                            String                assetGUID) throws InvalidParameterException,
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
     * @param ownerId userId or profileGUID of the owner - or null to clear the field
     * @param ownerType indicator of the type of Id provided above - or null to clear the field
     *
     * @throws InvalidParameterException userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    void updateAssetOwner(String    userId,
                          String    assetGUID,
                          String    ownerId,
                          OwnerType ownerType) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException;


    /**
     * Update the owner information for a specific asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param ownerId unique identifier/property of the owner - or null to clear the field
     * @param ownerTypeName name of the type of Id provided above - or null to clear the field
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
    void  removeSecurityTags(String                userId,
                             String                assetGUID,
                             String                assetElementGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;

    /**
     * Classify an asset as suitable to be used as a template for cataloguing assets of a similar types.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to classify
     * @param name name of the template
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
}
