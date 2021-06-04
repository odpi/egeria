/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.List;
import java.util.Map;

/**
 * DataFieldBuilder is able to build the properties for an DataField entity.
 */
class DataFieldBuilder extends OpenMetadataAPIGenericBuilder
{
    private String              dataFieldName;
    private String              dataFieldType;
    private String              dataFieldDescription;
    private List<String>        dataFieldAliases;
    private int                 dataFieldSortOrder;
    private String              defaultValue;
    private Map<String, String> additionalProperties;

    /**
     * Constructor supporting all properties.
     *
     * @param dataFieldName the name of the data field
     * @param dataFieldType the type of the data field
     * @param dataFieldDescription a description of the data field
     * @param dataFieldAliases any aliases associated with the data field
     * @param dataFieldSortOrder any sort order
     * @param defaultValue default value of the field
     * @param additionalProperties any additional properties
     * @param typeId unique identifier of the endpoint's type
     * @param typeName unique name of the endpoint's type
     * @param extendedProperties  properties from the subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DataFieldBuilder(String               dataFieldName,
                            String               dataFieldType,
                            String               dataFieldDescription,
                            List<String>         dataFieldAliases,
                            int                  dataFieldSortOrder,
                            String               defaultValue,
                            Map<String, String>  additionalProperties,
                            String               typeId,
                            String               typeName,
                            Map<String, Object>  extendedProperties,
                            OMRSRepositoryHelper repositoryHelper,
                            String               serviceName,
                            String               serverName)
    {
        super(typeId,
              typeName,
              extendedProperties,
              null,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.dataFieldName = dataFieldName;
        this.dataFieldType = dataFieldType;
        this.dataFieldDescription = dataFieldDescription;
        this.dataFieldAliases = dataFieldAliases;
        this.dataFieldSortOrder = dataFieldSortOrder;
        this.defaultValue = defaultValue;
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (dataFieldName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DATA_FIELD_NAME_PROPERTY_NAME,
                                                                      dataFieldName,
                                                                      methodName);
        }

        if (dataFieldType != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DATA_FIELD_TYPE_PROPERTY_NAME,
                                                                      dataFieldType,
                                                                      methodName);
        }

        if (dataFieldDescription != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DATA_FIELD_DESCRIPTION_PROPERTY_NAME,
                                                                      dataFieldDescription,
                                                                      methodName);
        }

        if (dataFieldAliases != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.DATA_FIELD_ALIASES_PROPERTY_NAME,
                                                                           dataFieldAliases,
                                                                           methodName);
        }

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.DATA_FIELD_SORT_ORDER_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.DATA_ITEM_SORT_ORDER_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.DATA_ITEM_SORT_ORDER_TYPE_NAME,
                                                                    dataFieldSortOrder,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataAPIMapper.SORT_ORDER_PROPERTY_NAME);
        }

        if (defaultValue != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DEFAULT_VALUE_PROPERTY_NAME,
                                                                      defaultValue,
                                                                      methodName);
        }

        if (additionalProperties != null)
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                         additionalProperties,
                                                                         methodName);
        }

        return properties;
    }
}
