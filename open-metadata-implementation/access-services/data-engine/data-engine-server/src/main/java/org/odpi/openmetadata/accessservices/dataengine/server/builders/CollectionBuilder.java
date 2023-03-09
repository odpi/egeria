/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


public class CollectionBuilder extends ReferenceableBuilder {

    private final String collectionName;

    public CollectionBuilder(String qualifiedName, String collectionName, String typeId, String typeName, OMRSRepositoryHelper repositoryHelper,
                             String serviceName, String serverName) {

        super(qualifiedName, typeId, typeName, repositoryHelper, serviceName, serverName);
        this.collectionName = collectionName;
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
        if (collectionName != null) {

            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    OpenMetadataAPIMapper.NAME_PROPERTY_NAME, collectionName, methodName);
        }
        return properties;
    }

}
