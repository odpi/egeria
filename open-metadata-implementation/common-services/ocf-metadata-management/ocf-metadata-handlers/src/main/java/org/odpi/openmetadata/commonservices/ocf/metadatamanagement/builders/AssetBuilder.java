/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * AssetBuilder creates the root repository entity for an asset.
 */
public class AssetBuilder extends ReferenceableBuilder
{
    private String              displayName;
    private String              description;
    private String              owner          = null;
    private OwnerType           ownerType      = null;
    private List<String>        zoneMembership = null;
    private String              latestChange   = null;
    private Map<String, String> origin         = null;


    /**
     * Minimal constructor
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public AssetBuilder(String               qualifiedName,
                        String               displayName,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
        this.description = null;
    }


    /**
     * Creation constructor
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the asset.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public AssetBuilder(String               qualifiedName,
                        String               displayName,
                        String               description,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Constructor supporting all properties but origin.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the asset.
     * @param owner name of the owner
     * @param ownerType type of owner
     * @param zoneMembership list of zones that this asset belongs to.
     * @param latestChange description of the last change to the asset.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    @Deprecated
    public AssetBuilder(String               qualifiedName,
                        String               displayName,
                        String               description,
                        String               owner,
                        OwnerType            ownerType,
                        List<String>         zoneMembership,
                        String               latestChange,
                        Map<String, String>  additionalProperties,
                        Map<String, Object>  extendedProperties,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.owner = owner;
        this.ownerType = ownerType;
        this.zoneMembership = zoneMembership;
        this.latestChange = latestChange;
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
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.owner = owner;
        this.ownerType = ownerType;
        this.zoneMembership = zoneMembership;
        this.latestChange = latestChange;
        this.origin = origin;
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

        if (latestChange != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      AssetMapper.LATEST_CHANGE_PROPERTY_NAME,
                                                                      latestChange,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getNameInstanceProperties(String  methodName)
    {
        InstanceProperties properties = super.getNameInstanceProperties(methodName);

        if (displayName != null)
        {
            String literalName = repositoryHelper.getExactMatchRegex(displayName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      AssetMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      literalName,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getSearchInstanceProperties(String  methodName)
    {
        InstanceProperties properties = null;

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      qualifiedName,
                                                                      methodName);
        }

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
     * Return the bean properties describing the asset's owner in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getOriginProperties(String  methodName)
    {
        InstanceProperties properties = null;

        if (origin != null)
        {
            if (origin.get(AssetMapper.ORGANIZATION_GUID_PROPERTY_NAME) != null)
            {
                properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          null,
                                                                          AssetMapper.ORGANIZATION_GUID_PROPERTY_NAME,
                                                                          origin.get(AssetMapper.ORGANIZATION_GUID_PROPERTY_NAME),
                                                                          methodName);
                origin.remove(AssetMapper.ORGANIZATION_GUID_PROPERTY_NAME);
            }
            if (origin.get(AssetMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME) != null)
            {
                properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          null,
                                                                          AssetMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME,
                                                                          origin.get(AssetMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME),
                                                                          methodName);
                origin.remove(AssetMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME);
            }
            if (! origin.isEmpty())
            {
                properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                             properties,
                                                                             AssetMapper.OTHER_ORIGIN_VALUES_PROPERTY_NAME,
                                                                             origin,
                                                                             methodName);
            }
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
