/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * PrimitiveSchemaTypeBuilder creates repository entities and relationships from properties for a primitive schema type.
 */
public class PrimitiveSchemaTypeBuilder extends SchemaTypeBuilder
{
    private String       dataType         = null;
    private String       defaultValue     = null;


    /**
     * Minimal constructor
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public PrimitiveSchemaTypeBuilder(String               qualifiedName,
                                      String               displayName,
                                      OMRSRepositoryHelper repositoryHelper,
                                      String               serviceName,
                                      String               serverName)
    {
        super(qualifiedName, displayName, repositoryHelper, serviceName, serverName);
    }


    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param versionNumber version of the schema type.
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param dataType type name for the data stored in this schema element.
     * @param defaultValue initial value for data stored in this schema element.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public PrimitiveSchemaTypeBuilder(String               qualifiedName,
                                      String               displayName,
                                      String               versionNumber,
                                      String               author,
                                      String               usage,
                                      String               encodingStandard,
                                      String               dataType,
                                      String               defaultValue,
                                      Map<String, String>  additionalProperties,
                                      Map<String, Object>  extendedProperties,
                                      OMRSRepositoryHelper repositoryHelper,
                                      String               serviceName,
                                      String               serverName)
    {
        super(qualifiedName,
              displayName,
              versionNumber,
              author,
              usage,
              encodingStandard,
              additionalProperties,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.dataType = dataType;
        this.defaultValue = defaultValue;
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

        if (dataType != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.DATA_TYPE_PROPERTY_NAME,
                                                                      dataType,
                                                                      methodName);
        }

        if (defaultValue != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.DEFAULT_VALUE_PROPERTY_NAME,
                                                                      defaultValue,
                                                                      methodName);
        }

        return properties;
    }
}
