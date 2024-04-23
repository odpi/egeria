/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * EndpointBuilder is able to build the properties for an Endpoint entity.
 */
public class EndpointBuilder extends ReferenceableBuilder {

    private final String protocol;
    private final String networkAddress;

    /**
     * Constructor for endpoint creates.
     *
     * @param protocol         the protocol
     * @param networkAddress   the network address
     * @param qualifiedName    unique name
     * @param typeId           type GUID to use for the entity
     * @param repositoryHelper helper methods
     * @param serviceName      name of this OMAS
     * @param serverName       name of local server
     */
    public EndpointBuilder(String protocol, String networkAddress, String qualifiedName, String typeId, String typeName,
                           OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(qualifiedName, typeId, typeName, repositoryHelper, serviceName, serverName);
        this.protocol = protocol;
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
    public InstanceProperties getInstanceProperties(String methodName) throws InvalidParameterException {

        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (networkAddress != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OpenMetadataType.NETWORK_ADDRESS_PROPERTY_NAME,
                                                                      networkAddress, methodName);
        }

        if (protocol != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OpenMetadataType.PROTOCOL_PROPERTY_NAME,
                    protocol, methodName);
        }

        return properties;
    }

}
