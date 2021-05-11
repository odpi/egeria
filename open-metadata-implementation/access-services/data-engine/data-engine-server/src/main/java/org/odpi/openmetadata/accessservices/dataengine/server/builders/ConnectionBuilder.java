/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME;

public class ConnectionBuilder extends ReferenceableBuilder {

    private final String name;

    public ConnectionBuilder(String name, String qualifiedName, String typeId, String typeName,
                             OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(qualifiedName, typeId, typeName, repositoryHelper, serviceName, serverName);
        this.name = name;
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

        if (name != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, DISPLAY_NAME_PROPERTY_NAME,
                    name, methodName);
        }

        return properties;
    }

}
