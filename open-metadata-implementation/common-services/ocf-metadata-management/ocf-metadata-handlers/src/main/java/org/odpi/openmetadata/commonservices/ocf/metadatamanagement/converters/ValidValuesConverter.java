/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ValidValuesMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;


/**
 * ValidValuesConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an ValidValuesDefinition or ValidValuesSet bean.
 */
public class ValidValuesConverter extends ReferenceableConverter
{

    /**
     * Constructor captures the initial content
     *
     * @param mainEntity properties to convert
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public ValidValuesConverter(EntityDetail                      mainEntity,
                                OMRSRepositoryHelper              repositoryHelper,
                                String                            serviceName)
    {
        super(mainEntity, repositoryHelper, serviceName);
    }


    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    public ValidValue getBean()
    {
        final String  methodName = "getBean";

        ValidValue bean = null;

        if (entity != null)
        {

            if (repositoryHelper.isTypeOf(serviceName, super.typeName, ValidValuesMapper.VALID_VALUE_SET_TYPE_NAME))
            {
                bean = new ValidValueSet();
            }
            else
            {
                bean = new ValidValueDefinition();
            }

            super.updateBean(bean);

            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                            ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));
                bean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                          ValidValuesMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                           ValidValuesMapper.DESCRIPTION_PROPERTY_NAME,
                                                                           instanceProperties,
                                                                           methodName));
                bean.setDeprecated(repositoryHelper.removeBooleanProperty(serviceName,
                                                                          ValidValuesMapper.IS_DEPRECATED_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setScope(repositoryHelper.removeStringProperty(serviceName,
                                                                    ValidValuesMapper.SCOPE_PROPERTY_NAME,
                                                                    instanceProperties,
                                                                    methodName));
                bean.setUsage(repositoryHelper.removeStringProperty(serviceName,
                                                                    ValidValuesMapper.USAGE_PROPERTY_NAME,
                                                                    instanceProperties,
                                                                    methodName));
                bean.setPreferredValue(repositoryHelper.removeStringProperty(serviceName,
                                                                             ValidValuesMapper.PREFERRED_VALUE_PROPERTY_NAME,
                                                                             instanceProperties,
                                                                             methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                          ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                          instanceProperties,
                                                                                          methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        return bean;
    }


    /**
     * Retrieve the DataItemSortOrder enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return DataItemSortOrder  enum value
     */
    private DataItemSortOrder getSortOrderFromProperties(InstanceProperties   properties)
    {
        DataItemSortOrder sortOrder = DataItemSortOrder.UNKNOWN;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(SchemaElementMapper.SORT_ORDER_PROPERTY_NAME);

                if (instancePropertyValue instanceof EnumPropertyValue)
                {
                    EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                    switch (enumPropertyValue.getOrdinal())
                    {
                        case 0:
                            sortOrder = DataItemSortOrder.ASCENDING;
                            break;

                        case 1:
                            sortOrder = DataItemSortOrder.DESCENDING;
                            break;

                        case 99:
                            sortOrder = DataItemSortOrder.UNSORTED;
                            break;
                    }
                }
            }
        }

        return sortOrder;
    }


    /**
     * Retrieve the DataItemSortOrder enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return DataItemSortOrder  enum value
     */
    private DataItemSortOrder removeSortOrderFromProperties(InstanceProperties   properties)
    {
        DataItemSortOrder sortOrder = this.getSortOrderFromProperties(properties);

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                instancePropertiesMap.remove(SchemaElementMapper.SORT_ORDER_PROPERTY_NAME);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return sortOrder;
    }
}
