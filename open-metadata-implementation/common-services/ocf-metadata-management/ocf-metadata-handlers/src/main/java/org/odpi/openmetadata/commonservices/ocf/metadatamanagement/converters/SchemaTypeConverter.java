/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
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
    private InstanceProperties schemaTypeProperties;
    private int                schemaAttributeCount;


    /**
     * Constructor captures the initial content - it is used when the
     *
     * @param typeName type name of bean to manufacture
     * @param properties properties to convert
     * @param schemaAttributeCount number of attributes nested inside this schema type
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName called server
     */
    public SchemaTypeConverter(String                   typeName,
                               InstanceProperties       properties,
                               int                      schemaAttributeCount,
                               OMRSRepositoryHelper     repositoryHelper,
                               String                   serviceName,
                               String                   serverName)
    {
        super(null, repositoryHelper, serviceName, serverName);

        super.typeName = typeName;
        this.schemaTypeProperties = properties;
        this.schemaAttributeCount = schemaAttributeCount;
    }


    /**
     * Constructor captures the initial content
     *
     * @param entity schema type entity
     * @param schemaAttributeCount number of attributes nested inside this schema type
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName called server
     */
    public SchemaTypeConverter(EntityDetail             entity,
                               int                      schemaAttributeCount,
                               OMRSRepositoryHelper     repositoryHelper,
                               String                   serviceName,
                               String                   serverName)
    {
        super(entity, repositoryHelper, serviceName, serverName);

        if (entity != null)
        {
            InstanceType type = entity.getType();
            if (type != null)
            {
                this.typeName = type.getTypeDefName();
            }

            this.schemaTypeProperties = entity.getProperties();
            this.schemaAttributeCount = schemaAttributeCount;
        }
    }

    
    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    @Override
    public SchemaType getBean()
    {

        if ((typeName != null) && (schemaTypeProperties != null))
        {
            if (repositoryHelper.isTypeOf(serviceName,
                                          typeName,
                                          SchemaElementMapper.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME))
            {
                return this.getPrimitiveSchemaTypeBean();
            }
            else if (repositoryHelper.isTypeOf(serviceName,
                                               typeName,
                                               SchemaElementMapper.ENUM_SCHEMA_TYPE_TYPE_NAME))
            {
                return this.getEnumSchemaTypeBean();
            }
            else if (repositoryHelper.isTypeOf(serviceName,
                                               typeName,
                                               SchemaElementMapper.SIMPLE_SCHEMA_TYPE_TYPE_NAME))
            {
                return this.getSimpleSchemaTypeBean();
            }
            else if (repositoryHelper.isTypeOf(serviceName,
                                               typeName,
                                               SchemaElementMapper.STRUCT_SCHEMA_TYPE_TYPE_NAME))
            {
                return this.getStructSchemaTypeBean();
            }
            else if (repositoryHelper.isTypeOf(serviceName,
                                               typeName,
                                               SchemaElementMapper.COMPLEX_SCHEMA_TYPE_TYPE_NAME))
            {
                return this.getComplexSchemaTypeBean();
            }
            else if (repositoryHelper.isTypeOf(serviceName,
                                               typeName,
                                               SchemaElementMapper.LITERAL_SCHEMA_TYPE_TYPE_NAME))
            {
                return this.getLiteralSchemaTypeBean();
            }
            else if (repositoryHelper.isTypeOf(serviceName,
                                               typeName,
                                               SchemaElementMapper.MAP_SCHEMA_TYPE_TYPE_NAME))
            {
                return this.getMapSchemaTypeBean();
            }
            else if (repositoryHelper.isTypeOf(serviceName,
                                               typeName,
                                               SchemaElementMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME))
            {
                return this.getSchemaTypeChoiceBean();
            }
            else
            {
                return this.getBasicSchemaTypeBean();
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
            bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                        ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                        instanceProperties,
                                                                        methodName));
            bean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                      SchemaElementMapper.SCHEMA_DISPLAY_NAME_PROPERTY_NAME,
                                                                      instanceProperties,
                                                                      methodName));

            bean.setVersionNumber(repositoryHelper.removeStringProperty(serviceName,
                                                                        SchemaElementMapper.SCHEMA_VERSION_NUMBER_PROPERTY_NAME,
                                                                        instanceProperties,
                                                                        methodName));

            bean.setAuthor(repositoryHelper.removeStringProperty(serviceName,
                                                                 SchemaElementMapper.SCHEMA_AUTHOR_PROPERTY_NAME,
                                                                 instanceProperties,
                                                                 methodName));

            bean.setUsage(repositoryHelper.removeStringProperty(serviceName,
                                                                SchemaElementMapper.SCHEMA_USAGE_PROPERTY_NAME,
                                                                instanceProperties,
                                                                methodName));

            bean.setEncodingStandard(repositoryHelper.removeStringProperty(serviceName,
                                                                           SchemaElementMapper.SCHEMA_ENCODING_STANDARD_PROPERTY_NAME,
                                                                           instanceProperties,
                                                                           methodName));

            bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                      ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                      instanceProperties,
                                                                                      methodName));
        }
    }


    /**
     * Set up the common properties for a literal schema type.  Processed properties are removed from the instance
     * properties object.
     *
     * @param bean bean to fill
     * @param instanceProperties properties from the repository
     * @param methodName calling method
     */
    private void updateLiteralSchemaTypeProperties(LiteralSchemaType  bean,
                                                   InstanceProperties instanceProperties,
                                                   String             methodName)
    {
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
            bean.setFixedValue(repositoryHelper.removeStringProperty(serviceName,
                                                                     SchemaElementMapper.FIXED_VALUE_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName));
        }
    }


    /**
     * Set up the common properties for a literal schema type.  Processed properties are removed from the instance
     * properties object.
     *
     * @param bean bean to fill
     * @param instanceProperties properties from the repository
     * @param methodName calling method
     */
    private void updateSimpleSchemaTypeProperties(SimpleSchemaType  bean,
                                                  InstanceProperties instanceProperties,
                                                  String             methodName)
    {
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
        }
    }


    /**
     * Set up the common properties for a complex schema type.  Processed properties are removed from the instance
     * properties object.
     *
     * @param bean bean to fill
     * @param instanceProperties properties from the repository
     * @param methodName calling method
     */
    private void updateComplexSchemaTypeProperties(ComplexSchemaType  bean,
                                                   InstanceProperties instanceProperties,
                                                   String             methodName)
    {
        if (instanceProperties != null)
        {
            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            this.updateBasicSchemaTypeProperties(bean, instanceProperties, methodName);
        }

        bean.setAttributeCount(schemaAttributeCount);
    }


    /**
     * Create a basic schema type bean.
     *
     * @return the bean
     */
    SchemaType getBasicSchemaTypeBean()
    {
        final String  methodName = "getBasicSchemaTypeBean";

        SchemaType  bean = new SchemaType();

        super.updateBean(bean);

        InstanceProperties instanceProperties = new InstanceProperties(schemaTypeProperties);;

        /*
         * The properties are removed from the instance properties and stowed in the bean.
         * Any remaining properties are stored in extendedProperties.
         */
        this.updateBasicSchemaTypeProperties(bean, instanceProperties, methodName);

        bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));

        return bean;
    }


    /**
     * Create a schema type choice bean.
     *
     * @return the bean
     */
    SchemaType getSchemaTypeChoiceBean()
    {
        final String  methodName = "getSchemaTypeChoiceBean";

        SchemaTypeChoice  bean = new SchemaTypeChoice();

        super.updateBean(bean);

        InstanceProperties instanceProperties = new InstanceProperties(schemaTypeProperties);;

        /*
         * The properties are removed from the instance properties and stowed in the bean.
         * Any remaining properties are stored in extendedProperties.
         */
        this.updateBasicSchemaTypeProperties(bean, instanceProperties, methodName);

        bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));

        return bean;
    }


    /**
     * Create a schema type choice bean.
     *
     * @return the bean
     */
    SchemaType getMapSchemaTypeBean()
    {
        final String  methodName = "getMapSchemaTypeBean";

        MapSchemaType  bean = new MapSchemaType();

        super.updateBean(bean);

        InstanceProperties instanceProperties = new InstanceProperties(schemaTypeProperties);;

        /*
         * The properties are removed from the instance properties and stowed in the bean.
         * Any remaining properties are stored in extendedProperties.
         */
        this.updateBasicSchemaTypeProperties(bean, instanceProperties, methodName);

        bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));

        return bean;
    }


    /**
     * Create a simple schema type bean.
     *
     * @return the bean
     */
    SchemaType getSimpleSchemaTypeBean()
    {
        final String  methodName = "getSimpleSchemaTypeBean";

        SimpleSchemaType  bean = new SimpleSchemaType();

        super.updateBean(bean);

        InstanceProperties instanceProperties = new InstanceProperties(schemaTypeProperties);;

        /*
         * The properties are removed from the instance properties and stowed in the bean.
         * Any remaining properties are stored in extendedProperties.
         */
        this.updateSimpleSchemaTypeProperties(bean, instanceProperties, methodName);

        bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));

        return bean;
    }


    /**
     * Create a enum schema type bean.
     *
     * @return the bean
     */
    private SchemaType getEnumSchemaTypeBean()
    {
        final String  methodName = "getEnumSchemaTypeBean";

        EnumSchemaType  bean = new EnumSchemaType();

        super.updateBean(bean);

        InstanceProperties instanceProperties = new InstanceProperties(schemaTypeProperties);;

        /*
         * The properties are removed from the instance properties and stowed in the bean.
         * Any remaining properties are stored in extendedProperties.
         */
        this.updateSimpleSchemaTypeProperties(bean, instanceProperties, methodName);

        bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));


        return bean;
    }


    /**
     * Create a primitive schema type bean.
     *
     * @return the bean
     */
    private SchemaType getPrimitiveSchemaTypeBean()
    {
        final String  methodName = "getPrimitiveSchemaTypeBean";

        PrimitiveSchemaType  bean = new PrimitiveSchemaType();

        super.updateBean(bean);

        InstanceProperties instanceProperties = new InstanceProperties(schemaTypeProperties);;

        /*
         * The properties are removed from the instance properties and stowed in the bean.
         * Any remaining properties are stored in extendedProperties.
         */
        this.updateSimpleSchemaTypeProperties(bean, instanceProperties, methodName);

        bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));

        return bean;
    }


    /**
     * Create a literal schema type bean.
     *
     * @return the bean
     */
    private SchemaType getLiteralSchemaTypeBean()
    {
        final String  methodName = "getLiteralSchemaTypeBean";

        LiteralSchemaType  bean = new LiteralSchemaType();

        super.updateBean(bean);

        InstanceProperties instanceProperties = new InstanceProperties(schemaTypeProperties);;

        /*
         * The properties are removed from the instance properties and stowed in the bean.
         * Any remaining properties are stored in extendedProperties.
         */
        this.updateLiteralSchemaTypeProperties(bean, instanceProperties, methodName);

        bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));

        return bean;
    }


    /**
     * Create a complex schema type bean,
     *
     * @return the bean
     */
    private SchemaType getComplexSchemaTypeBean()
    {
        final String  methodName = "getComplexSchemaTypeBean";

        ComplexSchemaType bean = new ComplexSchemaType();

        super.updateBean(bean);

        InstanceProperties instanceProperties = new InstanceProperties(schemaTypeProperties);

        /*
         * The properties are removed from the instance properties and stowed in the bean.
         * Any remaining properties are stored in extendedProperties.
         */
        this.updateComplexSchemaTypeProperties(bean, instanceProperties, methodName);

        bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));

        return bean;
    }


    /**
     * Create a complex schema type bean,
     *
     * @return the bean
     */
    private SchemaType getStructSchemaTypeBean()
    {
        final String  methodName = "getStructSchemaTypeBean";

        StructSchemaType bean = new StructSchemaType();

        super.updateBean(bean);

        InstanceProperties instanceProperties = new InstanceProperties(schemaTypeProperties);

        /*
         * The properties are removed from the instance properties and stowed in the bean.
         * Any remaining properties are stored in extendedProperties.
         */
        this.updateComplexSchemaTypeProperties(bean, instanceProperties, methodName);

        bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));

        return bean;
    }
}
