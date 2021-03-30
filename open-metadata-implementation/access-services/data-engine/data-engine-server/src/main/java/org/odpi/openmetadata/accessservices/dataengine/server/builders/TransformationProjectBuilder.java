/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.commonservices.generichandlers.AssetBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import static org.odpi.openmetadata.accessservices.dataengine.server.mappers.TransformationProjectMapper.NAME_PROPERTY_NAME;

public class TransformationProjectBuilder extends AssetBuilder {

    private String transformationProjectName;

    public TransformationProjectBuilder(String qualifiedName, String transformationProjectName, String typeName,
                                        OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {

        super(qualifiedName, typeName, repositoryHelper, serviceName, serverName);
        this.transformationProjectName = transformationProjectName;
    }
    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     *
     * @return InstanceProperties object
     *
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String methodName) throws InvalidParameterException {
        InstanceProperties properties = super.getInstanceProperties(methodName);
        if (transformationProjectName != null) {

            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    NAME_PROPERTY_NAME, transformationProjectName, methodName);
        }
        return properties;
    }

}
