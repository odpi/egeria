/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;


import org.odpi.openmetadata.accessservices.dataengine.server.mappers.SoftwareServerPropertiesMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;


public class SoftwareServerPropertiesBuilder extends ReferenceableBuilder {

    private String guid;
    private String name;
    private String description;
    private String type;
    private String version;
    private String source;
    private String patchLevel;

    /**
     * Constructor when only the qualified name is known.
     *
     * @param qualifiedName    unique name
     * @param repositoryHelper helper methods
     * @param serviceName      name of this OMAS
     * @param serverName       name of local server
     */
    protected SoftwareServerPropertiesBuilder(String qualifiedName, OMRSRepositoryHelper repositoryHelper,
                                              String serviceName, String serverName) {
        super(qualifiedName, repositoryHelper, serviceName, serverName);
    }

    /**
     * Constructor when basic properties are known.
     *
     * @param qualifiedName    unique name
     * @param repositoryHelper helper methods
     * @param name             display name of the data engine engine
     * @param description      description of discovery engine
     * @param serviceName      name of this OMAS
     * @param serverName       name of local server
     */
    public SoftwareServerPropertiesBuilder(String qualifiedName, String name, String description,
                                           OMRSRepositoryHelper repositoryHelper, String serviceName,
                                           String serverName) {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.name = name;
        this.description = description;
    }

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
     * @param extendedProperties   properties from the subtype.
     * @param repositoryHelper     helper methods
     * @param serviceName          name of this OMAS
     * @param serverName           name of local server
     */
    public SoftwareServerPropertiesBuilder(String qualifiedName, String name, String description,
                                           String type, String version, String patchLevel, String source,
                                           Map<String, String> additionalProperties,
                                           Map<String, Object> extendedProperties,
                                           OMRSRepositoryHelper repositoryHelper, String serviceName,
                                           String serverName) {

        super(qualifiedName, additionalProperties, extendedProperties, repositoryHelper, serviceName, serverName);

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
    public InstanceProperties getInstanceProperties(String methodName) throws InvalidParameterException {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (name != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    SoftwareServerPropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, name, methodName);
        }

        if (description != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    SoftwareServerPropertiesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        }

        if (type != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    SoftwareServerPropertiesMapper.TYPE_DESCRIPTION_PROPERTY_NAME, type, methodName);
        }

        if (version != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    SoftwareServerPropertiesMapper.VERSION_PROPERTY_NAME, version, methodName);
        }

        if (patchLevel != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    SoftwareServerPropertiesMapper.PATCH_LEVEL_PROPERTY_NAME, patchLevel, methodName);
        }

        if (source != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    SoftwareServerPropertiesMapper.SOURCE_PROPERTY_NAME, source, methodName);
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
    public InstanceProperties getNameInstanceProperties(String methodName) {
        InstanceProperties properties = super.getNameInstanceProperties(methodName);

        if (name != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    SoftwareServerPropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, name, methodName);
        }

        return properties;
    }
}
