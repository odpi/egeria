/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.NETWORK_ADDRESS_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROTOCOL_PROPERTY_NAME;

/**
 * Builder for an Endpoint properties
 */
public class EndpointBuilder extends ReferenceableBuilder {

    private final String protocol;
    private final String networkAddress;

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
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException {

        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (networkAddress != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, NETWORK_ADDRESS_PROPERTY_NAME,
                    networkAddress, methodName);
        }

        if (protocol != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, PROTOCOL_PROPERTY_NAME,
                    protocol, methodName);
        }

        return properties;
    }

}
