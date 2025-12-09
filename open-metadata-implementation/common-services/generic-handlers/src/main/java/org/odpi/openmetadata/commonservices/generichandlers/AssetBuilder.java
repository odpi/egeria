/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
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
    private String name                       = null;
    private String resourceName               = null;
    private String versionIdentifier          = null;
    private String technicalDescription       = null;
    private String deployedImplementationType = null;


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
     * @param name new value for the name
     * @param resourceName the full name from the resource
     * @param versionIdentifier new value for the versionIdentifier
     * @param technicalDescription new description for the asset
     * @param deployedImplementationType technology type
     * @param additionalProperties additional properties
     * @param typeGUID unique identifier for the type of this asset
     * @param typeName unique name for the type of this asset
     * @param extendedProperties  properties from the subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected AssetBuilder(String               qualifiedName,
                           String               name,
                           String               resourceName,
                           String               versionIdentifier,
                           String               technicalDescription,
                           String               deployedImplementationType,
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

        this.name                       = name;
        this.resourceName               = resourceName;
        this.versionIdentifier          = versionIdentifier;
        this.technicalDescription       = technicalDescription;
        this.deployedImplementationType = deployedImplementationType;
    }


    /**
     * Constructor supporting all entity properties. (Classifications are added separately.)
     *
     * @param qualifiedName unique name
     * @param name new value for the name
     * @param resourceName the full name from the resource
     * @param versionIdentifier new value for the versionIdentifier
     * @param technicalDescription new description for the asset
     * @param deployedImplementationType technology type
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
                           String               name,
                           String               resourceName,
                           String               versionIdentifier,
                           String               technicalDescription,
                           String               deployedImplementationType,
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

        this.name                       = name;
        this.resourceName               = resourceName;
        this.versionIdentifier          = versionIdentifier;
        this.technicalDescription       = technicalDescription;
        this.deployedImplementationType = deployedImplementationType;
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
                setOwnershipClassification(userId, owner, OpenMetadataType.USER_IDENTITY.typeName, null, methodName);
            }
            else if (ownerType == 1)
            {
                setOwnershipClassification(userId, owner, OpenMetadataType.ACTOR_PROFILE.typeName, null, methodName);
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
                return getOwnershipProperties(owner, OpenMetadataType.USER_IDENTITY.typeName, null, methodName);
            }
            else if (ownerType == 1)
            {
                return getOwnershipProperties(owner, OpenMetadataType.ACTOR_PROFILE.typeName, null, methodName);
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
                                                                                      OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName,
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
                errorHandler.handleUnsupportedType(error, methodName, OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName);
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
                                                                                     OpenMetadataProperty.ORGANIZATION.name,
                                                                                     organization,
                                                                                     methodName);

        if (organization != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataProperty.ORGANIZATION_PROPERTY_NAME.name,
                                                                      organizationPropertyName,
                                                                      methodName);
        }

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.BUSINESS_CAPABILITY.name,
                                                                  businessCapability,
                                                                  methodName);

        if (businessCapability != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataProperty.BUSINESS_CAPABILITY_PROPERTY_NAME.name,
                                                                      businessCapabilityPropertyName,
                                                                      methodName);
        }

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.OTHER_ORIGIN_VALUES.name,
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
                                                                                  OpenMetadataType.REFERENCE_DATA_CLASSIFICATION.typeName,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  null);
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataType.REFERENCE_DATA_CLASSIFICATION.typeName);
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
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  name,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.RESOURCE_NAME.name,
                                                                  resourceName,
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
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                  deployedImplementationType,
                                                                  methodName);

        return properties;
    }
}
