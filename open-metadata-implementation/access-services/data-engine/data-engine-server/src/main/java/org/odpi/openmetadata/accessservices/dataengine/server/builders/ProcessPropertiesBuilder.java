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

public class ProcessPropertiesBuilder extends AssetBuilder {
    private String processDisplayName;
    private String formula;

    public ProcessPropertiesBuilder(String qualifiedName, String displayName, OMRSRepositoryHelper repositoryHelper,
                                    String serviceName, String serverName) {
        super(qualifiedName, displayName, repositoryHelper, serviceName, serverName);
    }


    public ProcessPropertiesBuilder(String qualifiedName, String displayName, String processDisplayName,
                                    String formula, OMRSRepositoryHelper repositoryHelper, String serviceName,
                                    String serverName) {
        super(qualifiedName, displayName, repositoryHelper, serviceName, serverName);
        this.processDisplayName = processDisplayName;
        this.formula = formula;
    }

    public ProcessPropertiesBuilder(String qualifiedName, String displayName, String processDisplayName,
                                    String description, String owner, OwnerType ownerType, List<String> zoneMembership,
                                    String latestChange, String formula, Map<String, String> additionalProperties,
                                    Map<String, Object> extendedProperties, OMRSRepositoryHelper repositoryHelper,
                                    String serviceName, String serverName) {
        super(qualifiedName, displayName, description, owner, ownerType, zoneMembership, latestChange,
                additionalProperties, extendedProperties, repositoryHelper, serviceName, serverName);
        this.processDisplayName = processDisplayName;
        this.formula = formula;
    }

    @Override
    public InstanceProperties getInstanceProperties(String methodName) throws InvalidParameterException {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (processDisplayName != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    ProcessPropertiesMapper.DISPLAY_NAME, processDisplayName, methodName);
        }

        if (formula != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    ProcessPropertiesMapper.FORMULA, formula, methodName);
        }
        return properties;
    }
}
