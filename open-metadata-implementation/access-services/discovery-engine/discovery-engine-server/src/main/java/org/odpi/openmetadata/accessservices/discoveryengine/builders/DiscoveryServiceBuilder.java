/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.builders;

import org.odpi.openmetadata.accessservices.discoveryengine.mappers.DiscoveryServicePropertiesMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.frameworks.discovery.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * DiscoveryServiceBuilder creates repository entities and relationships from properties for a discovery service.
 * Specifically, a single discovery service is represented as a DiscoveryService entity linked to a Connection
 * entity using an ConnectionToAsset relationship.
 */
public class DiscoveryServiceBuilder extends ReferenceableBuilder
{
    private String       displayName;
    private String       description;
    private String       owner          = null;
    private OwnerType    ownerType      = null;
    private List<String> zoneMembership = null;
    private String       latestChange   = null;


    public DiscoveryServiceBuilder(String               qualifiedName,
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
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the discovery engine.
     * @param owner name of the owner
     * @param ownerType type of owner
     * @param zoneMembership list of zones that this discovery service belongs to.
     * @param latestChange description of the last change to the entity.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DiscoveryServiceBuilder(String               qualifiedName,
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
                                                                      DiscoveryServicePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryServicePropertiesMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (owner != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryServicePropertiesMapper.OWNER_PROPERTY_NAME,
                                                                      owner,
                                                                      methodName);
        }

        if (ownerType != null)
        {
            properties = this.addOwnerTypeToProperties(properties, methodName);
        }

        if (zoneMembership != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           DiscoveryServicePropertiesMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                           zoneMembership,
                                                                           methodName);
        }

        if (latestChange != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryServicePropertiesMapper.LATEST_CHANGE_PROPERTY_NAME,
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
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DiscoveryServicePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
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
                                                                                 DiscoveryServicePropertiesMapper.OWNER_TYPE_PROPERTY_NAME,
                                                                                 0,
                                                                                 "UserId",
                                                                                 "The owner's userId is specified (default).",
                                                                                 methodName);
                break;

            case PROFILE_ID:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 DiscoveryServicePropertiesMapper.OWNER_TYPE_PROPERTY_NAME,
                                                                                 1,
                                                                                 "ProfileId",
                                                                                 "The unique identifier (guid) of the profile of the owner..",
                                                                                 methodName);
                break;

            case OTHER:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 DiscoveryServicePropertiesMapper.OWNER_TYPE_PROPERTY_NAME,
                                                                                 99,
                                                                                 "Other",
                                                                                 "Another type of owner identifier, probably not supported by open metadata.",
                                                                                 methodName);
                break;
        }

        return resultingProperties;
    }
}
