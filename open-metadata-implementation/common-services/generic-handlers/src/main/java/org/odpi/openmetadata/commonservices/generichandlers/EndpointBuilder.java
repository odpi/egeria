/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * EndpointBuilder is able to build the properties for an Endpoint entity from an Endpoint bean.
 */
class EndpointBuilder extends ReferenceableBuilder
{
    private final String displayName;
    private final String description;
    private final String networkAddress;

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
     * Create constructor - when templating
     *
     * @param qualifiedName unique name for the endpoint
     * @param displayName short display name for the endpoint
     * @param description description of the endpoint
     * @param networkAddress location of the resource
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    EndpointBuilder(String               qualifiedName,
                    String               displayName,
                    String               description,
                    String               networkAddress,
                    OMRSRepositoryHelper repositoryHelper,
                    String               serviceName,
                    String               serverName)
    {
        super(qualifiedName,
              null,
              OpenMetadataType.ENDPOINT.typeGUID,
              OpenMetadataType.ENDPOINT.typeName,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.networkAddress = networkAddress;
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
                                                                  displayName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                                  networkAddress,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.PROTOCOL.name,
                                                                  protocol,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ENCRYPTION_METHOD.name,
                                                                  encryptionMethod,
                                                                  methodName);

        return properties;
    }
}
