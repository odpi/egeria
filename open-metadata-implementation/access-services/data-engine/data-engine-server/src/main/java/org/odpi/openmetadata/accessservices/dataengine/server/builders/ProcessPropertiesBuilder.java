/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.commonservices.generichandlers.AssetBuilder;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * ProcessPropertiesBuilder is able to build the properties for an Process entity.
 */
public class ProcessPropertiesBuilder extends AssetBuilder {
    private final String processDisplayName;
    private final String formula;
    private final String implementationLanguage;

    public ProcessPropertiesBuilder(String qualifiedName, String processDisplayName, String technicalName, String technicalDescription,
                                    String typeGUID, String typeName, String formula, String implementationLanguage,
                                    Map<String, String> additionalProperties, OMRSRepositoryHelper repositoryHelper, String serviceName,
                                    String serverName) {
        super(qualifiedName, technicalName, null, technicalDescription, additionalProperties, typeGUID, typeName, null, repositoryHelper,
                serviceName, serverName);

        this.processDisplayName = processDisplayName;
        this.formula = formula;
        this.implementationLanguage = implementationLanguage;
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
                    OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME, processDisplayName, methodName);
        }

        if (formula != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME, formula, methodName);
        }

        if (implementationLanguage != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    OpenMetadataAPIMapper.IMPLEMENTATION_LANGUAGE_PROPERTY_NAME, formula, methodName);
        }
        return properties;
    }
}