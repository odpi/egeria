/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * CommunityBuilder creates the parts for an entity that represents a community.
 */
public class CommunityBuilder extends ReferenceableBuilder
{
    private String displayName = null;
    private String description = null;
    private String mission = null;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the community
     * @param displayName short display name for the community
     * @param description description of the community
     * @param mission purpose of the community
     * @param additionalProperties additional properties for a community
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a community subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    CommunityBuilder(String               qualifiedName,
                     String               displayName,
                     String               description,
                     String               mission,
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

        this.displayName = displayName;
        this.description = description;
        this.mission = mission;
    }


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the community
     * @param displayName short display name for the community
     * @param description description of the community
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    CommunityBuilder(String               qualifiedName,
                     String               displayName,
                     String               description,
                     OMRSRepositoryHelper repositoryHelper,
                     String               serviceName,
                     String               serverName)
    {
        super(qualifiedName,
              null,
              OpenMetadataAPIMapper.COMMUNITY_TYPE_GUID,
              OpenMetadataAPIMapper.COMMUNITY_TYPE_NAME,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Classification constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    CommunityBuilder(OMRSRepositoryHelper repositoryHelper,
                     String               serviceName,
                     String               serverName)
    {
        super(OpenMetadataAPIMapper.COMMUNITY_TYPE_GUID,
              OpenMetadataAPIMapper.COMMUNITY_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);
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
                                                                  OpenMetadataAPIMapper.NAME_PROPERTY_NAME,
                                                                  displayName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.MISSION_PROPERTY_NAME,
                                                                  mission,
                                                                  methodName);
        return properties;
    }
}
