/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * AssetBuilder creates the parts of a root repository entity for an asset.
 */
public class AssetBuilder extends ReferenceableBuilder
{
    private String       displayName;
    private String       description;
    private String       owner             = null;
    private String       ownerTypeName     = null;
    private String       ownerPropertyName = null;
    private OwnerType    ownerType         = null;
    private List<String> zoneMembership    = null;


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
        this.ownerTypeName = assetProperties.getOwnerTypeName();
        this.ownerPropertyName = assetProperties.getOwnerPropertyName();
        this.ownerType = assetProperties.getOwnerType();
        this.zoneMembership = assetProperties.getZoneMembership();
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
