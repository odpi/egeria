/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.AssetBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * ProcessPropertiesBuilder is able to build the properties for an Process entity.
 */
public class ProcessPropertiesBuilder extends AssetBuilder {
    private final String processDisplayName;
    private final String formula;

    public ProcessPropertiesBuilder(String qualifiedName, String displayName, String processDisplayName, String description, String owner,
                                    OwnerType ownerType, List<String> zoneMembership, String latestChange, String formula,
                                    Map<String, String> additionalProperties, Map<String, Object> extendedProperties,
                                    OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(qualifiedName, displayName, description, owner, ownerType, zoneMembership, null, latestChange, additionalProperties,
                extendedProperties, repositoryHelper, serviceName, serverName);
        this.processDisplayName = processDisplayName;
        this.formula = formula;
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

        if (processDisplayName != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    ProcessPropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, processDisplayName, methodName);
        }

        if (formula != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    ProcessPropertiesMapper.FORMULA_PROPERTY_NAME, formula, methodName);
        }
        return properties;
    }
}
