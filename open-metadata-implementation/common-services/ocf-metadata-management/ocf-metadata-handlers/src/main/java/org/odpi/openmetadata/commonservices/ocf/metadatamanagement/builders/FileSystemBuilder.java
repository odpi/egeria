/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SoftwareServerCapabilityMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * FileSystemBuilder creates the parts for an entity that represents a file system definition.
 */
public class FileSystemBuilder extends SoftwareServerCapabilityBuilder
{
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
        super(uniqueName,
              displayName,
              description,
              type,
              version,
              patchLevel,
              source,
              additionalProperties,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.format = format;
        this.encryption = encryption;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getClassificationInstanceProperties(String  methodName)
    {
        InstanceProperties properties = null;

        if (format != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      SoftwareServerCapabilityMapper.FORMAT_PROPERTY_NAME,
                                                                      format,
                                                                      methodName);
        }

        if (encryption != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SoftwareServerCapabilityMapper.ENCRYPTION_PROPERTY_NAME,
                                                                      encryption,
                                                                      methodName);
        }

        return properties;
    }
}
