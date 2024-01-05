/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
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
    private String versionIdentifier    = null;
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
        super(OpenMetadataType.ASSET.typeGUID,
              OpenMetadataType.ASSET.typeName,
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
     * @param versionIdentifier new value for the versionIdentifier
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
                           String               versionIdentifier,
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
        this.versionIdentifier = versionIdentifier;
        this.technicalDescription = technicalDescription;
    }


    /**
     * Constructor supporting all entity properties. (Classifications are added separately.)
     *
     * @param qualifiedName unique name
     * @param technicalName new value for the name
     * @param versionIdentifier new value for the versionIdentifier
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
                           String               versionIdentifier,
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
        this.versionIdentifier = versionIdentifier;
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
                                                                                  OpenMetadataType.ASSET_ZONES_CLASSIFICATION_NAME,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  getZoneMembershipProperties(zoneMembership, methodName));
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataType.ASSET_ZONES_CLASSIFICATION_NAME);
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
                                                                           OpenMetadataType.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                           zoneMembership,
                                                                           methodName);
        }

        setEffectivityDates(properties);

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
            if (ownerType == 0)
            {
                setOwnershipClassification(userId, owner, OpenMetadataType.USER_IDENTITY_TYPE_NAME, null, methodName);
            }
            else if (ownerType == 1)
            {
                setOwnershipClassification(userId, owner, OpenMetadataType.ACTOR_PROFILE_TYPE_NAME, null, methodName);
            }

            setOwnershipClassification(userId, owner, null, null, methodName);
        }
    }


    /**
     * Return the bean properties describing the asset's owner in an InstanceProperties object.
     *
     * @param owner name of the owner
     * @param ownerType Enum ordinal for type of owner - 0=userId; 1= profileId; 99=other.
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    @Deprecated
    InstanceProperties getOwnerProperties(String owner,
                                          int    ownerType,
                                          String methodName)
    {
        if (owner != null)
        {
            if (ownerType == 0)
            {
                return getOwnershipProperties(owner, OpenMetadataType.USER_IDENTITY_TYPE_NAME, null, methodName);
            }
            else if (ownerType == 1)
            {
                return getOwnershipProperties(owner, OpenMetadataType.ACTOR_PROFILE_TYPE_NAME, null, methodName);
            }

            return getOwnershipProperties(owner, null, null, methodName);
        }

        return null;
    }


    /**
     * Add the asset origin classification to an asset.
     *
     * @param userId calling user
     * @param organization Unique identifier  of the organization where this asset originated from - or null
     * @param organizationPropertyName property name used for the identifier of the organization
     * @param businessCapability  Unique identifier of the business capability where this asset originated from
     * @param businessCapabilityPropertyName property name used for the identifier of the businessCapability
     * @param otherOriginValues Descriptive labels describing origin of the asset
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     */
    void  setAssetOrigin(String                userId,
                         String                organization,
                         String                organizationPropertyName,
                         String                businessCapability,
                         String                businessCapabilityPropertyName,
                         Map<String, String>   otherOriginValues,
                         String                methodName) throws InvalidParameterException
    {
        if ((organization != null) || (businessCapability != null) || ((otherOriginValues != null) && (! otherOriginValues.isEmpty())))
        {
            try
            {
                Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                      null,
                                                                                      null,
                                                                                      InstanceProvenanceType.LOCAL_COHORT,
                                                                                      userId,
                                                                                      OpenMetadataType.ASSET_ORIGIN_CLASSIFICATION_NAME,
                                                                                      typeName,
                                                                                      ClassificationOrigin.ASSIGNED,
                                                                                      null,
                                                                                      getOriginProperties(organization,
                                                                                                          organizationPropertyName,
                                                                                                          businessCapability,
                                                                                                          businessCapabilityPropertyName,
                                                                                                          otherOriginValues,
                                                                                                          methodName));
                newClassifications.put(classification.getName(), classification);
            }
            catch (TypeErrorException error)
            {
                errorHandler.handleUnsupportedType(error, methodName, OpenMetadataType.ASSET_ORIGIN_CLASSIFICATION_NAME);
            }
        }
    }


    /**
     * Return the bean properties describing the asset's owner in an InstanceProperties object.
     *
     * @param organization Unique identifier  of the organization where this asset originated from - or null
     * @param organizationPropertyName property name used for the identifier of the organization
     * @param businessCapability  Unique identifier of the business capability where this asset originated from
     * @param businessCapabilityPropertyName property name used for the identifier of the businessCapability
     * @param otherOriginValues Descriptive labels describing origin of the asset
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getOriginProperties(String                organization,
                                           String                organizationPropertyName,
                                           String                businessCapability,
                                           String                businessCapabilityPropertyName,
                                           Map<String, String>   otherOriginValues,
                                           String                methodName)
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataType.ORGANIZATION_PROPERTY_NAME,
                                                                                     organization,
                                                                                     methodName);

        if (organization != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataType.ORGANIZATION_PROPERTY_NAME_PROPERTY_NAME,
                                                                      organizationPropertyName,
                                                                      methodName);
        }

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.BUSINESS_CAPABILITY_PROPERTY_NAME,
                                                                  businessCapability,
                                                                  methodName);

        if (businessCapability != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataType.BUSINESS_CAPABILITY_PROPERTY_NAME_PROPERTY_NAME,
                                                                      businessCapabilityPropertyName,
                                                                      methodName);
        }

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataType.OTHER_ORIGIN_VALUES_PROPERTY_NAME,
                                                                     otherOriginValues,
                                                                     methodName);

        setEffectivityDates(properties);

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
                                                                                  OpenMetadataType.REFERENCE_DATA_CLASSIFICATION_TYPE_NAME,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  null);
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataType.REFERENCE_DATA_CLASSIFICATION_TYPE_NAME);
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

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.NAME.name,
                                                                  technicalName,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                                                  versionIdentifier,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  technicalDescription,
                                                                  methodName);

        return properties;
    }
}
