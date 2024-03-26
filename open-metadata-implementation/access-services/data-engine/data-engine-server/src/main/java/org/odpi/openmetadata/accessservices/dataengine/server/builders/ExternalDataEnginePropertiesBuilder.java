/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;


import org.apache.commons.collections4.MapUtils;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * ExternalDataEnginePropertiesBuilder is able to build the properties for an Engine entity.
 */
public class ExternalDataEnginePropertiesBuilder extends ReferenceableBuilder {

    private final String name;
    private final String description;
    private final String deployedImplementationType;
    private final String capabilityVersion;
    private final String source;
    private final String patchLevel;

    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName              unique name
     * @param name                       new value for the display name.
     * @param description                new description for the discovery engine.
     * @param deployedImplementationType new description of the type of discovery engine.
     * @param capabilityVersion          new version number for the discovery engine implementation.
     * @param patchLevel                 new patch level for the discovery engine implementation.
     * @param source                     new source description for the implementation of the discovery engine.
     * @param additionalProperties       additional properties
     * @param repositoryHelper           helper methods
     * @param serviceName                name of this OMAS
     * @param serverName                 name of local server
     */
    public ExternalDataEnginePropertiesBuilder(String qualifiedName, String name, String description,
                                               String deployedImplementationType, String capabilityVersion, String patchLevel, String source,
                                               Map<String, String> additionalProperties,
                                               OMRSRepositoryHelper repositoryHelper, String serviceName,
                                               String serverName) {

        super(qualifiedName, additionalProperties, repositoryHelper, serviceName, serverName);

        this.name = name;
        this.description = description;
        this.deployedImplementationType = deployedImplementationType;
        this.capabilityVersion = capabilityVersion;
        this.patchLevel = patchLevel;
        this.source = source;
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

        if (name != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                                                                      OpenMetadataProperty.NAME.name, name, methodName);
        }

        if (description != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                                                                      OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        }

        if (deployedImplementationType != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                                                                      OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType, methodName);
        }

        if (capabilityVersion != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                                                                      OpenMetadataProperty.CAPABILITY_VERSION.name, capabilityVersion, methodName);
        }

        if (patchLevel != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                                                                      OpenMetadataProperty.PATCH_LEVEL.name, patchLevel, methodName);
        }

        if (source != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                                                                      OpenMetadataProperty.SOURCE.name, source, methodName);
        }

        if(MapUtils.isNotEmpty(additionalProperties)) {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName, properties,
                                                                         OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        }

        return properties;
    }

    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     *
     * @return InstanceProperties object
     */
    @Override
    public InstanceProperties getNameInstanceProperties(String methodName) {
        InstanceProperties properties = super.getNameInstanceProperties(methodName);

        if (name != null) {
            String literalName = repositoryHelper.getExactMatchRegex(name);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                                                                      OpenMetadataProperty.NAME.name, literalName, methodName);
        }

        return properties;
    }
}
