/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.commonservices.generichandlers.AssetBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * ProcessPropertiesBuilder is able to build the properties for a Process entity.
 */
public class ProcessPropertiesBuilder extends AssetBuilder {
    private final String processDisplayName;
    private final String formula;
    private final String implementationLanguage;

    /**
     * Constructor for process with all properties
     *
     * @param qualifiedName          unique name
     * @param processDisplayName     process display name
     * @param technicalName          technical name
     * @param technicalDescription   description
     * @param typeName               type name to use for the entity
     * @param typeGUID               type GUID to use for the entity
     * @param formula                the process formula
     * @param implementationLanguage the implementation language
     * @param additionalProperties   additional process properties
     * @param repositoryHelper       helper methods
     * @param serviceName            name of this OMAS
     * @param serverName             name of local server
     */
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
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String methodName) throws InvalidParameterException {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (processDisplayName != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                                      processDisplayName, methodName);
        }

        if (formula != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                                                                      OpenMetadataProperty.FORMULA.name,
                                                                      formula, methodName);
        }

        if (implementationLanguage != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                                                                      OpenMetadataType.IMPLEMENTATION_LANGUAGE_PROPERTY_NAME, formula, methodName);
        }
        return properties;
    }
}
