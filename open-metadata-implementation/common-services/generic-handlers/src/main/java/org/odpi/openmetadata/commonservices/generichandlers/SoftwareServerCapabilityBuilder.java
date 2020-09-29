/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SoftwareServerCapabilityMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * SoftwareServerCapabilityBuilder creates the parts for an entity that represents a software server capability.
 */
public class SoftwareServerCapabilityBuilder extends ReferenceableBuilder
{
    private String displayName;
    private String description;
    private String type;
    private String version;
    private String patchLevel;
    private String source;


    /**
     * Minimal constructor used for searching
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SoftwareServerCapabilityBuilder(String               qualifiedName,
                                           OMRSRepositoryHelper repositoryHelper,
                                           String               serviceName,
                                           String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = null;
        this.description = null;
        this.type = null;
        this.version = null;
        this.patchLevel = null;
        this.source = null;
    }


    /**
     * Constructor when basic properties are known.
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param displayName display name of discovery engine
     * @param description description of discovery engine
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SoftwareServerCapabilityBuilder(String               qualifiedName,
                                           String               displayName,
                                           String               description,
                                           OMRSRepositoryHelper repositoryHelper,
                                           String               serviceName,
                                           String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Create constructor
     *
     * @param uniqueName qualified name for the file system
     * @param displayName short display name
     * @param description description of the file system
     * @param type type of file system
     * @param version version of file system
     * @param patchLevel patchLevel of software supporting the file system
     * @param source supplier of the software for this file system
     * @param additionalProperties additional properties for a governance zone
     * @param extendedProperties  properties for a governance zone subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SoftwareServerCapabilityBuilder(String               uniqueName,
                                           String               displayName,
                                           String               description,
                                           String               type,
                                           String               version,
                                           String               patchLevel,
                                           String               source,
                                           Map<String, String>  additionalProperties,
                                           Map<String, Object>  extendedProperties,
                                           OMRSRepositoryHelper repositoryHelper,
                                           String               serviceName,
                                           String               serverName)
    {
        super(uniqueName, additionalProperties, extendedProperties, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
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
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SoftwareServerCapabilityMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SoftwareServerCapabilityMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (type != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SoftwareServerCapabilityMapper.TYPE_PROPERTY_NAME,
                                                                      type,
                                                                      methodName);
        }

        if (version != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SoftwareServerCapabilityMapper.VERSION_PROPERTY_NAME,
                                                                      version,
                                                                      methodName);
        }

        if (patchLevel != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SoftwareServerCapabilityMapper.PATCH_LEVEL_PROPERTY_NAME,
                                                                      patchLevel,
                                                                      methodName);
        }

        if (source != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SoftwareServerCapabilityMapper.SOURCE_PROPERTY_NAME,
                                                                      source,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getNameInstanceProperties(String  methodName)
    {
        InstanceProperties properties = super.getNameInstanceProperties(methodName);

        if (displayName != null)
        {
            String literalName = repositoryHelper.getExactMatchRegex(displayName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SoftwareServerCapabilityMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      literalName,
                                                                      methodName);
        }

        return properties;
    }
}
