/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;


/**
 * SchemaAttributeConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an SchemaAttribute bean.
 */
public class SchemaAttributeConverter extends ReferenceableConverter
{
    private SchemaType                        attributeType;
    private SchemaLink                        externalAttributeType;
    private List<SchemaAttributeRelationship> attributeRelationships;

    /**
     * Constructor captures the initial content
     *
     * @param mainEntity properties to convert
     * @param attributeType schema type for the attribute
     * @param externalAttributeType attribute type for a link
     * @param attributeRelationships links to other attributes
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public SchemaAttributeConverter(EntityDetail                      mainEntity,
                                    SchemaType                        attributeType,
                                    SchemaLink                        externalAttributeType,
                                    List<SchemaAttributeRelationship> attributeRelationships,
                                    OMRSRepositoryHelper              repositoryHelper,
                                    String                            serviceName)
    {
        super(mainEntity,
              repositoryHelper,
              serviceName);

        this.attributeType = attributeType;
        this.externalAttributeType = externalAttributeType;
        this.attributeRelationships = attributeRelationships;
    }

    
    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    public SchemaAttribute getBean()
    {
        final String  methodName = "getBean";

        SchemaAttribute      bean = null;

        if (entity != null)
        {
            bean = new SchemaAttribute();

            super.updateBean(bean);
            bean.setAttributeType(attributeType);
            bean.setExternalAttributeType(externalAttributeType);
            bean.setAttributeRelationships(attributeRelationships);

            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                            ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));
                bean.setAttributeName(repositoryHelper.removeStringProperty(serviceName,
                                                                            SchemaElementMapper.ATTRIBUTE_NAME_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));
                if (bean.getAttributeName() == null)
                {
                    /*
                     * If no display name is present then check to see if deprecated name was used
                     */
                    bean.setAttributeName(repositoryHelper.removeStringProperty(serviceName,
                                                                                SchemaElementMapper.OLD_ATTRIBUTE_NAME_PROPERTY_NAME,
                                                                                instanceProperties,
                                                                                methodName));
                }
                bean.setElementPosition(repositoryHelper.removeIntProperty(serviceName,
                                                                           SchemaElementMapper.ELEMENT_POSITION_PROPERTY_NAME,
                                                                           instanceProperties,
                                                                           methodName));
                bean.setCardinality(repositoryHelper.removeStringProperty(serviceName,
                                                                          SchemaElementMapper.CARDINALITY_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setMinCardinality(repositoryHelper.removeIntProperty(serviceName,
                                                                          SchemaElementMapper.MIN_CARDINALITY_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setMaxCardinality(repositoryHelper.removeIntProperty(serviceName,
                                                                          SchemaElementMapper.MAX_CARDINALITY_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDeprecated(repositoryHelper.removeBooleanProperty(serviceName,
                                                                          SchemaElementMapper.IS_DEPRECATED_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDefaultValueOverride(repositoryHelper.removeStringProperty(serviceName,
                                                                                   SchemaElementMapper.DEFAULT_VALUE_OVERRIDE_PROPERTY_NAME,
                                                                                   instanceProperties,
                                                                                   methodName));
                bean.setAllowsDuplicateValues(repositoryHelper.removeBooleanProperty(serviceName,
                                                                          SchemaElementMapper.ALLOWS_DUPLICATES_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setOrderedValues(repositoryHelper.removeBooleanProperty(serviceName,
                                                                          SchemaElementMapper.ORDERED_VALUES_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setSortOrder(this.removeSortOrderFromProperties(instanceProperties));
                bean.setMinimumLength(repositoryHelper.removeIntProperty(serviceName,
                                                                         SchemaElementMapper.MIN_LENGTH_PROPERTY_NAME,
                                                                         instanceProperties,
                                                                         methodName));
                bean.setLength(repositoryHelper.removeIntProperty(serviceName,
                                                                  SchemaElementMapper.LENGTH_PROPERTY_NAME,
                                                                  instanceProperties,
                                                                  methodName));
                bean.setSignificantDigits(repositoryHelper.removeIntProperty(serviceName,
                                                                             SchemaElementMapper.SIGNIFICANT_DIGITS_PROPERTY_NAME,
                                                                             instanceProperties,
                                                                             methodName));
                bean.setNullable(repositoryHelper.removeBooleanProperty(serviceName,
                                                                        SchemaElementMapper.IS_NULLABLE_PROPERTY_NAME,
                                                                        instanceProperties,
                                                                        methodName));

                bean.setNativeJavaClass(repositoryHelper.removeStringProperty(serviceName,
                                                                              SchemaElementMapper.NATIVE_CLASS_PROPERTY_NAME,
                                                                              instanceProperties,
                                                                              methodName));
                bean.setAliases(repositoryHelper.removeStringArrayProperty(serviceName,
                                                                           SchemaElementMapper.ALIASES_PROPERTY_NAME,
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
