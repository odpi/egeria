/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.List;
import java.util.Map;

/**
 * AssetBuilder creates the parts of a root repository entity for an asset.
 */
public class AssetBuilder extends ReferenceableBuilder
{
    private String technicalName        = null;
    private String technicalDescription = null;


    /**
     * Creation constructor used when working with classifications
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected AssetBuilder(OMRSRepositoryHelper repositoryHelper,
                           String               serviceName,
                           String               serverName)
    {
        super(OpenMetadataAPIMapper.ASSET_TYPE_GUID,
              OpenMetadataAPIMapper.ASSET_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Subtype constructor used when working with classifications
     *
     * @param typeGUID unique identifier for the type of this asset
     * @param typeName unique name for the type of this asset
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected AssetBuilder(String               typeGUID,
                           String               typeName,
                           OMRSRepositoryHelper repositoryHelper,
                           String               serviceName,
                           String               serverName)
    {
        super(typeGUID,
              typeName,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Constructor supporting all entity properties. (Classifications are added separately.)
     *
     * @param qualifiedName unique name
     * @param technicalName new value for the name
     * @param technicalDescription new description for the asset
     * @param additionalProperties additional properties
     * @param typeGUID unique identifier for the type of this asset
     * @param typeName unique name for the type of this asset
     * @param extendedProperties  properties from the subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected AssetBuilder(String               qualifiedName,
                           String               technicalName,
                           String               technicalDescription,
                           Map<String, String>  additionalProperties,
                           String               typeGUID,
                           String               typeName,
                           Map<String, Object>  extendedProperties,
                           OMRSRepositoryHelper repositoryHelper,
                           String               serviceName,
                           String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.technicalName = technicalName;
        this.technicalDescription = technicalDescription;
    }


    /**
     * Constructor supporting all entity properties. (Classifications are added separately.)
     *
     * @param qualifiedName unique name
     * @param technicalName new value for the name
     * @param technicalDescription new description for the asset
     * @param additionalProperties additional properties
     * @param typeGUID unique identifier for the type of this asset
     * @param typeName unique name for the type of this asset
     * @param extendedProperties  properties from the subtype
     * @param initialStatus status used to create the asset
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected AssetBuilder(String               qualifiedName,
                           String               technicalName,
                           String               technicalDescription,
                           Map<String, String>  additionalProperties,
                           String               typeGUID,
                           String               typeName,
                           Map<String, Object>  extendedProperties,
                           InstanceStatus       initialStatus,
                           OMRSRepositoryHelper repositoryHelper,
                           String               serviceName,
                           String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              initialStatus,
              repositoryHelper,
              serviceName,
              serverName);

        this.technicalName = technicalName;
        this.technicalDescription = technicalDescription;
    }


    /**
     * Set up the AssetZones classification for this entity.
     * This method overrides an previously defined AssetZones classification for this entity.
     *
     * @param userId calling user
     * @param zoneMembership list of zone names for the zones this asset is a member of
     * @param methodName calling method
     * @throws InvalidParameterException AssetZones is not supported in the local repository, or any repository
     *                                   connected by an open metadata repository cohort
     */
    protected void setAssetZones(String       userId,
                                 List<String> zoneMembership,
                                 String       methodName) throws InvalidParameterException
    {
        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  getZoneMembershipProperties(zoneMembership, methodName));
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME);
        }
    }


    /**
     * Return the bean properties describing the asset's zone membership in an InstanceProperties object.
     *
     * @param zoneMembership list of zone names for the zones this asset is a member of
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getZoneMembershipProperties(List<String> zoneMembership,
                                                   String       methodName)
    {
        InstanceProperties properties = null;

        if (zoneMembership != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           null,
                                                                           OpenMetadataAPIMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                           zoneMembership,
                                                                           methodName);
        }

        return properties;
    }



    /**
     * Set up the AssetOwnership classification for this entity.
     * This method overrides an previously defined AssetOwnership classification for this entity.
     *
     * @param userId calling user
     * @param owner name of the owner.
     * @param ownerType Enum ordinal for type of owner - 0=userId; 1= profileId; 99=other.
     * @param methodName calling method
     * @throws InvalidParameterException AssetOwnership is not supported in the local repository, or any repository
     *                                   connected by an open metadata repository cohort
     */
    protected void setAssetOwnership(String userId,
                                     String owner,
                                     int    ownerType,
                                     String methodName) throws InvalidParameterException
    {
        if (owner != null)
        {
            try
            {
                Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                      null,
                                                                                      null,
                                                                                      InstanceProvenanceType.LOCAL_COHORT,
                                                                                      userId,
                                                                                      OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME,
                                                                                      typeName,
                                                                                      ClassificationOrigin.ASSIGNED,
                                                                                      null,
                                                                                      getOwnerProperties(userId,
                                                                                                         owner,
                                                                                                         ownerType,
                                                                                                         methodName));
                newClassifications.put(classification.getName(), classification);
            }
            catch (TypeErrorException error)
            {
                errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME);
            }
        }
    }


    /**
     * Return the bean properties describing the asset's owner in an InstanceProperties object.
     *
     * @param userId calling user
     * @param owner name of the owner.
     * @param ownerType Enum ordinal for type of owner - 0=userId; 1= profileId; 99=other.
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException the owner enum type is not supported
     */
    InstanceProperties getOwnerProperties(String userId,
                                          String owner,
                                          int    ownerType,
                                          String methodName) throws InvalidParameterException
    {
        InstanceProperties properties;

        if (owner != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.OWNER_PROPERTY_NAME,
                                                                      owner,
                                                                      methodName);

            try
            {
                properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                        properties,
                                                                        OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME,
                                                                        OpenMetadataAPIMapper.ASSET_OWNER_TYPE_ENUM_TYPE_GUID,
                                                                        OpenMetadataAPIMapper.ASSET_OWNER_TYPE_ENUM_TYPE_NAME,
                                                                        OpenMetadataAPIMapper.USER_ID_OWNER_TYPE_ORDINAL,
                                                                        methodName);
            }
            catch (TypeErrorException error)
            {
                errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.ASSET_OWNER_TYPE_ENUM_TYPE_NAME);
            }
        }
        else
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.OWNER_PROPERTY_NAME,
                                                                      userId,
                                                                      methodName);

            try
            {
                properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                        properties,
                                                                        OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME,
                                                                        OpenMetadataAPIMapper.ASSET_OWNER_TYPE_ENUM_TYPE_GUID,
                                                                        OpenMetadataAPIMapper.ASSET_OWNER_TYPE_ENUM_TYPE_NAME,
                                                                        ownerType,
                                                                        methodName);
            }
            catch (TypeErrorException error)
            {
                errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.ASSET_OWNER_TYPE_ENUM_TYPE_NAME);
            }
        }

        return properties;
    }


    /**
     * Add the asset origin classification to an asset.
     *
     * @param userId calling user
     * @param organizationGUID Unique identifier (GUID) of the organization where this asset originated from - or null
     * @param businessCapabilityGUID  Unique identifier (GUID) of the business capability where this asset originated from.
     * @param otherOriginValues Descriptive labels describing origin of the asset
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     */
    void  setAssetOrigin(String                userId,
                         String                organizationGUID,
                         String                businessCapabilityGUID,
                         Map<String, String>   otherOriginValues,
                         String                methodName) throws InvalidParameterException
    {
        if ((organizationGUID != null) || (businessCapabilityGUID != null) || ((otherOriginValues != null) && (! otherOriginValues.isEmpty())))
        {
            try
            {
                Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                      null,
                                                                                      null,
                                                                                      InstanceProvenanceType.LOCAL_COHORT,
                                                                                      userId,
                                                                                      OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME,
                                                                                      typeName,
                                                                                      ClassificationOrigin.ASSIGNED,
                                                                                      null,
                                                                                      getOriginProperties(organizationGUID,
                                                                                                          businessCapabilityGUID,
                                                                                                          otherOriginValues,
                                                                                                          methodName));
                newClassifications.put(classification.getName(), classification);
            }
            catch (TypeErrorException error)
            {
                errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME);
            }
        }
    }


    /**
     * Return the bean properties describing the asset's owner in an InstanceProperties object.
     *
     * @param organizationGUID Unique identifier (GUID) of the organization where this asset originated from - or null
     * @param businessCapabilityGUID  Unique identifier (GUID) of the business capability where this asset originated from.
     * @param otherOriginValues Descriptive labels describing origin of the asset
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getOriginProperties(String                organizationGUID,
                                           String                businessCapabilityGUID,
                                           Map<String, String>   otherOriginValues,
                                           String                methodName)
    {
        InstanceProperties properties = null;

        if (organizationGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.ORGANIZATION_GUID_PROPERTY_NAME,
                                                                      organizationGUID,
                                                                      methodName);
        }

        if (businessCapabilityGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME,
                                                                      businessCapabilityGUID,
                                                                      methodName);

        }

        if ((otherOriginValues != null ) && (! otherOriginValues.isEmpty()))
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.OTHER_ORIGIN_VALUES_PROPERTY_NAME,
                                                                         otherOriginValues,
                                                                         methodName);
        }

        return properties;
    }


    /**
     * Add the reference data classification to an asset.
     *
     * @param userId calling user
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     */
    void  setReferenceData(String userId,
                           String methodName) throws InvalidParameterException
    {
        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  OpenMetadataAPIMapper.REFERENCE_DATA_CLASSIFICATION_TYPE_NAME,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  null);
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.REFERENCE_DATA_CLASSIFICATION_TYPE_NAME);
        }
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (technicalName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.NAME_PROPERTY_NAME,
                                                                      technicalName,
                                                                      methodName);
        }

        if (technicalDescription != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      technicalDescription,
                                                                      methodName);
        }

        return properties;
    }
}
