/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.builders;

import org.odpi.openmetadata.accessservices.digitalservice.mappers.DigitalServiceMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * FileSystemBuilder creates the parts for an entity that represents a file system definition.
 */
public class DigitalServiceBuilder extends ReferenceableBuilder
{
    private String url;
    private String guid;
    private String typeId;
    private String typeName;
    private String displayName;
    private String typeDescription;
    private Integer typeVersion;
    private String description;

    /**
     * Minimal constructor used for searching
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DigitalServiceBuilder(String               qualifiedName,
                                 OMRSRepositoryHelper repositoryHelper,
                                 String               serviceName,
                                 String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);
        this.url = null;
        this.guid = null;
        this.typeId = null;
        this.typeName = null;
        this.displayName = null;
        this.description = null;
        this.typeDescription = null;
        this.typeVersion = null;
    }


    /**
     * Create constructor
     *
     * @param url url for the Digital Service
     * @param guid display name
     * @param typeId description of the Digital Service
     * @param typeName type of the Digital Service
     * @param typeVersion version of the Digital Service
     * @param displayName version of the digital service
     * @param description description of the digital service
     * @param typeDescription patchLevel of software supporting the file system
     * @param qualifiedName of the Digital Service
     * @param additionalProperties additional properties for a governance zone
     * @param extendedProperties  properties for a governance zone subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DigitalServiceBuilder(String               url,
                                 String               guid,
                                 String               typeId,
                                 String               typeName,
                                 Integer              typeVersion,
                                 String               displayName,
                                 String               description,
                                 String               typeDescription,
                                 String               qualifiedName,
                                 Map<String, String>  additionalProperties,
                                 Map<String, Object>  extendedProperties,
                                 OMRSRepositoryHelper repositoryHelper,
                                 String               serviceName,
                                 String               serverName)
    {
        super(qualifiedName, additionalProperties, extendedProperties, repositoryHelper, serviceName, serverName);

        this.url = url;
        this.guid = guid;
        this.typeId = typeId;
        this.typeName = typeName;
        this.typeVersion = typeVersion;
        this.displayName = displayName;
        this.description = description;
        this.typeDescription = typeDescription;
        this.qualifiedName = qualifiedName;
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

        if (url != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DigitalServiceMapper.URL_PROPERTY_NAME,
                                                                      url,
                                                                      methodName);
        }

        if (guid != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DigitalServiceMapper.GUID_PROPERTY_NAME,
                                                                      guid,
                                                                      methodName);
        }

        if (typeId != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DigitalServiceMapper.TYPE_ID_PROPERTY_NAME,
                                                                      typeId,
                                                                      methodName);
        }

        if (typeName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DigitalServiceMapper.TYPE_PROPERTY_NAME,
                                                                      typeName,
                                                                      methodName);
        }

        if (typeVersion != null)
        {
            properties = repositoryHelper.addLongPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DigitalServiceMapper.TYPE_VERSION_PROPERTY_NAME,
                                                                      typeVersion,
                                                                      methodName);
        }

        if (typeDescription != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DigitalServiceMapper.TYPE_DESCRIPTION_PROPERTY_NAME,
                                                                      typeDescription,
                                                                      methodName);
        }

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DigitalServiceMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      qualifiedName,
                                                                      methodName);

        }

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DigitalServiceMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);

        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DigitalServiceMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
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

        return properties;
    }
}
