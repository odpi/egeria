/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.LatestChange;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.List;
import java.util.Map;

/**
 * AssetBuilder creates the parts of a root repository entity for an asset.
 */
public class AssetBuilder extends ReferenceableBuilder
{
    private String              displayName;
    private String              description;
    private String              owner          = null;
    private OwnerType           ownerType      = null;
    private List<String>        zoneMembership = null;


    /**
     * Classification constructor
     *
     * @param assetProperties asset bean
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     * @throws InvalidParameterException bad properties
     */
    public AssetBuilder(Asset                assetProperties,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName) throws InvalidParameterException
    {
        super(assetProperties, repositoryHelper, serviceName, serverName);

        this.displayName = assetProperties.getDisplayName();
        this.description = assetProperties.getDescription();
        this.owner = assetProperties.getOwner();
        this.ownerType = assetProperties.getOwnerType();
        this.zoneMembership = assetProperties.getZoneMembership();
    }



    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the asset.
     * @param owner name of the owner.
     * @param ownerType type of owner.
     * @param zoneMembership list of zones that this asset belongs to.
     * @param origin properties that describe the origin of the asset.
     * @param latestChange description of the last change to the asset.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public AssetBuilder(String               qualifiedName,
                        String               displayName,
                        String               description,
                        String               owner,
                        OwnerType            ownerType,
                        List<String>         zoneMembership,
                        Map<String, String>  origin,
                        String               latestChange,
                        Map<String, String>  additionalProperties,
                        Map<String, Object>  extendedProperties,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              AssetMapper.ASSET_TYPE_NAME,
              AssetMapper.ASSET_TYPE_GUID,
              extendedProperties,
              latestChange,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.owner = owner;
        this.ownerType = ownerType;
        this.zoneMembership = zoneMembership;
    }




    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      AssetMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      AssetMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        return properties;
    }




    /**
     * Return the bean properties describing the asset's zone membership in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getZoneMembershipProperties(String  methodName)
    {
        InstanceProperties properties = null;

        if (zoneMembership != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           null,
                                                                           AssetMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                           zoneMembership,
                                                                           methodName);
        }

        return properties;
    }



    /**
     * Return the bean properties describing the asset's owner in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getOwnerProperties(String  methodName)
    {
        InstanceProperties properties = null;

        if (owner != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      AssetMapper.OWNER_PROPERTY_NAME,
                                                                      owner,
                                                                      methodName);
        }

        if (ownerType != null)
        {
            properties = this.addOwnerTypeToProperties(properties, methodName);
        }

        return properties;
    }

    /**
     * Add the OwnerType enum to the properties.
     *
     * @param properties current properties
     * @param methodName calling method
     * @return updated properties
     */
    private InstanceProperties addOwnerTypeToProperties(InstanceProperties properties,
                                                        String             methodName)
    {
        InstanceProperties resultingProperties = properties;

        switch (ownerType)
        {
            case USER_ID:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 AssetMapper.OWNER_TYPE_PROPERTY_NAME,
                                                                                 0,
                                                                                 "UserId",
                                                                                 "The owner's userId is specified (default).",
                                                                                 methodName);
                break;

            case PROFILE_ID:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 AssetMapper.OWNER_TYPE_PROPERTY_NAME,
                                                                                 1,
                                                                                 "ProfileId",
                                                                                 "The unique identifier (guid) of the profile of the owner.",
                                                                                 methodName);
                break;

            case OTHER:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 AssetMapper.OWNER_TYPE_PROPERTY_NAME,
                                                                                 99,
                                                                                 "Other",
                                                                                 "Another type of owner identifier, probably not supported by open metadata.",
                                                                                 methodName);
                break;
        }

        return resultingProperties;
    }
}
