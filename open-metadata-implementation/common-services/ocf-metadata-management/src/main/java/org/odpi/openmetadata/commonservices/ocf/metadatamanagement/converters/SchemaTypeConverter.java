/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ComplexSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.PrimitiveSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * SchemaTypeConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an SchemaType bean.
 */
public class SchemaTypeConverter extends ReferenceableConverter
{
    int schemaAttributeCount;

    /**
     * Constructor captures the initial content
     *
     * @param mainEntity properties to convert
     * @param schemaAttributeCount number of attributes nexted inside this schema type
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public SchemaTypeConverter(EntityDetail             mainEntity,
                               int                      schemaAttributeCount,
                               OMRSRepositoryHelper     repositoryHelper,
                               String                   serviceName)
    {
        super(mainEntity,
              repositoryHelper,
              serviceName);

        this.schemaAttributeCount = schemaAttributeCount;
    }

    
    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    public SchemaType getBean()
    {

        if (entity != null)
        {
            InstanceType entityType = entity.getType();

            if (entityType != null)
            {
                String entityTypeName = entityType.getTypeDefName();

                if (repositoryHelper.isTypeOf(serviceName,
                                              entityTypeName,
                                              SchemaElementMapper.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME))
                {
                    return this.getPrimitiveSchemaTypeBean();
                }
                else if (repositoryHelper.isTypeOf(serviceName,
                                                   entityTypeName,
                                                   SchemaElementMapper.COMPLEX_SCHEMA_TYPE_TYPE_NAME))
                {
                    getComplexSchemaTypeBean();
                }
                else
                {
                    // todo - need to support all types of schema
                    return this.getBasicSchemaTypeBean();
                }
            }
        }

        return null;
    }


    /**
     * Set up the properties found in every schema type.
     *
     * @param bean bean to fill
     * @param instanceProperties properties from the entity
     * @param methodName calling method
     */
    private void updateBasicSchemaTypeProperties(SchemaType         bean,
                                                 InstanceProperties instanceProperties,
                                                 String             methodName)
    {

        if (instanceProperties != null)
        {
            bean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                      SchemaElementMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      instanceProperties,
                                                                      methodName));

            bean.setVersionNumber(repositoryHelper.removeStringProperty(serviceName,
                                                                        SchemaElementMapper.VERSION_NUMBER_PROPERTY_NAME,
                                                                        instanceProperties,
                                                                        methodName));

            bean.setAuthor(repositoryHelper.removeStringProperty(serviceName,
                                                                 SchemaElementMapper.AUTHOR_PROPERTY_NAME,
                                                                 instanceProperties,
                                                                 methodName));

            bean.setUsage(repositoryHelper.removeStringProperty(serviceName,
                                                                SchemaElementMapper.USAGE_PROPERTY_NAME,
                                                                instanceProperties,
                                                                methodName));

            bean.setEncodingStandard(repositoryHelper.removeStringProperty(serviceName,
                                                                           SchemaElementMapper.ENCODING_STANDARD_PROPERTY_NAME,
                                                                           instanceProperties,
                                                                           methodName));
        }
    }


    /**
     * Create a primitive schema type bean,
     *
     * @return the bean
     */
    SchemaType getPrimitiveSchemaTypeBean()
    {
        final String  methodName = "getPrimitiveSchemaTypeBean";

        PrimitiveSchemaType  bean = new PrimitiveSchemaType();

        super.updateBean(bean);

        InstanceProperties instanceProperties = entity.getProperties();

        if (instanceProperties != null)
        {
            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            this.updateBasicSchemaTypeProperties(bean, instanceProperties, methodName);

            bean.setDataType(repositoryHelper.removeStringProperty(serviceName,
                                                                   SchemaElementMapper.DATA_TYPE_PROPERTY_NAME,
                                                                   instanceProperties,
                                                                   methodName));
            bean.setDefaultValue(repositoryHelper.removeStringProperty(serviceName,
                                                                       SchemaElementMapper.DEFAULT_VALUE_PROPERTY_NAME,
                                                                       instanceProperties,
                                                                       methodName));

            bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
        }

        return bean;
    }


    /**
     * Create a complex schema type bean,
     *
     * @return the bean
     */
    SchemaType getComplexSchemaTypeBean()
    {
        final String  methodName = "getComplexSchemaTypeBean";

        ComplexSchemaType bean = new ComplexSchemaType();

        super.updateBean(bean);

        InstanceProperties instanceProperties = entity.getProperties();

        if (instanceProperties != null)
        {
            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            this.updateBasicSchemaTypeProperties(bean, instanceProperties, methodName);

            bean.setAttributeCount(schemaAttributeCount);

            bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
        }

        return bean;
    }


    /**
     * Create a basic schema type bean,
     *
     * @return the bean
     */
    SchemaType getBasicSchemaTypeBean()
    {
        final String  methodName = "getBasicSchemaTypeBean";

        SchemaType  bean = new SchemaType();

        super.updateBean(bean);

        InstanceProperties instanceProperties = entity.getProperties();

        if (instanceProperties != null)
        {
            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            this.updateBasicSchemaTypeProperties(bean, instanceProperties, methodName);

            bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
        }

        return bean;
    }
}
