/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.accessservices.dataengine.server.mappers.SchemaTypePropertiesMapper;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * SchemaAttributePropertiesBuilder is able to build the properties for an Process entity.
 */
public class SchemaAttributePropertiesBuilder extends SchemaAttributeBuilder {
    private final String anchorGUID;

    public SchemaAttributePropertiesBuilder(String qualifiedName, String displayName, String description, int elementPosition, int minCardinality,
                                            int maxCardinality, boolean isDeprecated, String defaultValueOverride, boolean allowsDuplicateValues,
                                            boolean orderedValues, int sortOrder, int minimumLength, int length, int significantDigits,
                                            boolean isNullable, String nativeJavaClass, List<String> aliases,
                                            Map<String, String> additionalProperties, String typeId, String typeName,
                                            Map<String, Object> extendedProperties, OMRSRepositoryHelper repositoryHelper, String serviceName,
                                            String serverName, String anchorGUID) {
        super(qualifiedName, displayName, description, elementPosition, minCardinality, maxCardinality, isDeprecated, defaultValueOverride,
                allowsDuplicateValues, orderedValues, sortOrder, minimumLength, length, significantDigits, isNullable, nativeJavaClass, aliases,
                additionalProperties, typeId, typeName, extendedProperties, repositoryHelper, serviceName, serverName);
        this.anchorGUID = anchorGUID;
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

        if (anchorGUID != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, SchemaTypePropertiesMapper.ANCHOR_GUID, anchorGUID,
                    methodName);
        }

        return properties;
    }
}
