/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * EndpointBuilder is able to build the properties for an Endpoint entity from an Endpoint bean.
 */
class EndpointBuilder extends ReferenceableBuilder
{
    private String displayName;
    private String description;
    private String networkAddress;
    private String protocol;
    private String encryptionMethod;


    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name of the endpoint
     * @param displayName new value for the display name
     * @param description new description for the endpoint
     * @param networkAddress location of the resource
     * @param protocol protocol supported by the endpoint
     * @param encryptionMethod algorithm used to encrypt data or null for clear data
     * @param additionalProperties additional properties
     * @param typeId unique identifier of the endpoint's type
     * @param typeName unique name of the endpoint's type
     * @param extendedProperties  properties from the subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public EndpointBuilder(String               qualifiedName,
                           String               displayName,
                           String               description,
                           String               networkAddress,
                           String               protocol,
                           String               encryptionMethod,
                           Map<String, String>  additionalProperties,
                           String               typeId,
                           String               typeName,
                           Map<String, Object>  extendedProperties,
                           OMRSRepositoryHelper repositoryHelper,
                           String               serviceName,
                           String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeId,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.networkAddress = networkAddress;
        this.protocol = protocol;
        this.encryptionMethod = encryptionMethod;
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

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ENDPOINT_DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (networkAddress != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.NETWORK_ADDRESS_PROPERTY_NAME,
                                                                      networkAddress,
                                                                      methodName);
        }

        if (protocol != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.PROTOCOL_PROPERTY_NAME,
                                                                      protocol,
                                                                      methodName);
        }

        if (encryptionMethod != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ENCRYPTION_METHOD_PROPERTY_NAME,
                                                                      encryptionMethod,
                                                                      methodName);
        }

        return properties;
    }
}
