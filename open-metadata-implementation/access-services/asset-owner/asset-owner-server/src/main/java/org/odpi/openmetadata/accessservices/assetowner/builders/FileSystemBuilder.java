/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.builders;

import org.odpi.openmetadata.accessservices.assetowner.mappers.FileSystemMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * FileSystemBuilder creates the parts for an entity that represents a file system definition.
 */
public class FileSystemBuilder extends ReferenceableBuilder
{
    private String displayName;
    private String description;
    private String type;
    private String version;
    private String patchLevel;
    private String source;
    private String format;
    private String encryption;


    /**
     * Minimal constructor used for searching
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public FileSystemBuilder(String               qualifiedName,
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
        this.format = null;
        this.encryption = null;
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
     * @param format format of files on this file system
     * @param encryption encryption type - null for unencrypted
     * @param additionalProperties additional properties for a governance zone
     * @param extendedProperties  properties for a governance zone subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public FileSystemBuilder(String               uniqueName,
                             String               displayName,
                             String               description,
                             String               type,
                             String               version,
                             String               patchLevel,
                             String               source,
                             String               format,
                             String               encryption,
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
        this.format = format;
        this.encryption = encryption;
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
                                                                      FileSystemMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      FileSystemMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (type != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      FileSystemMapper.TYPE_PROPERTY_NAME,
                                                                      type,
                                                                      methodName);
        }

        if (version != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      FileSystemMapper.VERSION_PROPERTY_NAME,
                                                                      version,
                                                                      methodName);
        }

        if (patchLevel != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      FileSystemMapper.PATCH_LEVEL_PROPERTY_NAME,
                                                                      patchLevel,
                                                                      methodName);
        }

        if (source != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      FileSystemMapper.SOURCE_PROPERTY_NAME,
                                                                      source,
                                                                      methodName);
        }
        return properties;
    }



    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getClassificationInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = null;

        if (format != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      FileSystemMapper.FORMAT_PROPERTY_NAME,
                                                                      format,
                                                                      methodName);
        }

        if (encryption != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      FileSystemMapper.ENCRYPTION_PROPERTY_NAME,
                                                                      encryption,
                                                                      methodName);
        }

        return properties;
    }
}
