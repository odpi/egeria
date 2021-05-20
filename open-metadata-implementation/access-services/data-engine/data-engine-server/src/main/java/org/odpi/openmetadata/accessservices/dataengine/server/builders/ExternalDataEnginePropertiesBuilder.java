/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;


import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;


public class ExternalDataEnginePropertiesBuilder extends ReferenceableBuilder {

    private final String name;
    private final String description;
    private final String type;
    private final String version;
    private final String source;
    private final String patchLevel;

    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName        unique name
     * @param name                 new value for the display name.
     * @param description          new description for the discovery engine.
     * @param type                 new description of the type of discovery engine.
     * @param version              new version number for the discovery engine implementation.
     * @param patchLevel           new patch level for the discovery engine implementation.
     * @param source               new source description for the implementation of the discovery engine.
     * @param additionalProperties additional properties
     * @param repositoryHelper     helper methods
     * @param serviceName          name of this OMAS
     * @param serverName           name of local server
     */
    public ExternalDataEnginePropertiesBuilder(String qualifiedName, String name, String description,
                                               String type, String version, String patchLevel, String source,
                                               Map<String, String> additionalProperties,
                                               OMRSRepositoryHelper repositoryHelper, String serviceName,
                                               String serverName) {

        super(qualifiedName, additionalProperties, repositoryHelper, serviceName, serverName);

        this.name = name;
        this.description = description;
        this.type = type;
        this.version = version;
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
                    OpenMetadataAPIMapper.NAME_PROPERTY_NAME, name, methodName);
        }

        if (description != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        }

        if (type != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    OpenMetadataAPIMapper.DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME_DEP, type, methodName);
        }

        if (version != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    OpenMetadataAPIMapper.VERSION_PROPERTY_NAME, version, methodName);
        }

        if (patchLevel != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    OpenMetadataAPIMapper.PATCH_LEVEL_PROPERTY_NAME, patchLevel, methodName);
        }

        if (source != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME, source, methodName);
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
                    OpenMetadataAPIMapper.NAME_PROPERTY_NAME, literalName, methodName);
        }

        return properties;
    }
}
