/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.EndpointMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * EndpointBuilder is able to build the properties for an Endpoint entity from an Endpoint bean.
 */
public class EndpointBuilder extends ReferenceableBuilder
{
    private String displayName;
    private String description;
    private String networkAddress   = null;
    private String protocol         = null;
    private String encryptionMethod = null;



    /**
     * Constructor when basic properties are known.
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param displayName display name of discovery engine
     * @param description description of discovery engine
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public EndpointBuilder(String               qualifiedName,
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
     * @param networkAddress new description of the type ofg discovery engine.
     * @param protocol protocol supported by the endpoint.
     * @param encryptionMethod new patch level for the discovery engine implementation.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
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
                           Map<String, Object>  extendedProperties,
                           OMRSRepositoryHelper repositoryHelper,
                           String               serviceName,
                           String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              EndpointMapper.ENDPOINT_TYPE_NAME,
              EndpointMapper.ENDPOINT_TYPE_GUID,
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
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      EndpointMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      EndpointMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (networkAddress != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      EndpointMapper.NETWORK_ADDRESS_PROPERTY_NAME,
                                                                      networkAddress,
                                                                      methodName);
        }

        if (protocol != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      EndpointMapper.PROTOCOL_PROPERTY_NAME,
                                                                      protocol,
                                                                      methodName);
        }

        if (encryptionMethod != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      EndpointMapper.ENCRYPTION_METHOD_PROPERTY_NAME,
                                                                      encryptionMethod,
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
    public InstanceProperties getQualifiedNameInstanceProperties(String  methodName)
    {
        return super.getNameInstanceProperties(methodName);
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
                                                                      EndpointMapper.DISPLAY_NAME_PROPERTY_NAME,
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
    public InstanceProperties getNetworkAddressInstanceProperties(String  methodName)
    {
        InstanceProperties properties = null;

        if (networkAddress != null)
        {
            String literalName = repositoryHelper.getExactMatchRegex(displayName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      EndpointMapper.NETWORK_ADDRESS_PROPERTY_NAME,
                                                                      literalName,
                                                                      methodName);
        }

        return properties;
    }
}
