/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OCFConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * SchemaTypeConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an SchemaType bean.
 */
public class SchemaTypeConverter<B> extends OCFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public SchemaTypeConverter(OMRSRepositoryHelper repositoryHelper,
                               String               serviceName,
                               String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Return the converted bean.  This is a special method used for schema types since they are stored
     * as a collection of instances.
     *
     * @param beanClass name of the class to create
     * @param schemaRootHeader unique identifier of the schema element that holds the root information
     * @param schemaTypeTypeName name of type of the schema type to create
     * @param instanceProperties properties describing the schema type
     * @param attributeCount number of attributes (for a complex schema type)
     * @param validValueSetGUID unique identifier of the set of valid values (for an enum schema type)
     * @param externalSchemaType bean containing the properties of the schema type that is shared by multiple attributes/assets
     * @param mapFromSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param mapToSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param schemaTypeOptions list of schema types that could be the type for this attribute
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    @SuppressWarnings(value = "unchecked")
    public B getNewSchemaTypeBean(Class<B>             beanClass,
                                  InstanceHeader       schemaRootHeader,
                                  String               schemaTypeTypeName,
                                  InstanceProperties   instanceProperties,
                                  List<Classification> schemaRootClassifications,
                                  int                  attributeCount,
                                  String               validValueSetGUID,
                                  B                    externalSchemaType,
                                  B                    mapFromSchemaType,
                                  B                    mapToSchemaType,
                                  List<B>              schemaTypeOptions,
                                  String               methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B testBean = beanClass.getDeclaredConstructor().newInstance();

            if (testBean instanceof SchemaType)
            {
                if ((schemaRootHeader != null) && (instanceProperties != null))
                {
                    SchemaType returnBean = null;

                    /*
                     * The schema type has different subtypes.
                     * This next piece of logic sorts out which type of schema bean to create.
                     */

                    if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME))
                    {
                        returnBean = this.getPrimitiveSchemaType(instanceProperties);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.LITERAL_SCHEMA_TYPE_TYPE_NAME))
                    {
                        returnBean = this.getLiteralSchemaType(instanceProperties);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.API_SCHEMA_TYPE_TYPE_NAME))
                    {
                        returnBean = this.getAPISchemaType(instanceProperties, attributeCount);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.COMPLEX_SCHEMA_TYPE_TYPE_NAME))
                    {
                        returnBean = this.getComplexSchemaType(instanceProperties, attributeCount);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.LITERAL_SCHEMA_TYPE_TYPE_NAME))
                    {
                        returnBean = this.getEnumSchemaType(instanceProperties, validValueSetGUID);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.MAP_SCHEMA_TYPE_TYPE_NAME))
                    {
                        returnBean = this.getMapSchemaType(instanceProperties, mapFromSchemaType, mapToSchemaType);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME))
                    {
                        returnBean = this.getSchemaTypeChoice(instanceProperties, schemaTypeOptions);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.EXTERNAL_SCHEMA_TYPE_TYPE_NAME))
                    {
                        returnBean = this.getExternalSchemaType(instanceProperties, externalSchemaType);
                    }
                    else
                    {
                        /*
                         * This will throw an exception
                         */
                        super.validateInstanceType(OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                   beanClass.getName(),
                                                   schemaTypeTypeName,
                                                   methodName);
                    }

                    if (returnBean != null)
                    {
                        super.setUpElementHeader(returnBean,
                                                 schemaRootHeader,
                                                 schemaRootClassifications,
                                                 methodName);

                        InstanceProperties classificationProperties =
                                super.getClassificationProperties(OpenMetadataAPIMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                                  schemaRootClassifications);

                        returnBean.setFormula(this.getFormula(classificationProperties));
                        returnBean.setFormulaType(this.getFormulaType(classificationProperties));
                    }

                    return (B)returnBean;
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Return the converted bean.
     *
     * @param instanceProperties properties describing the schema type
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaType getLiteralSchemaType(InstanceProperties   instanceProperties)
    {
        LiteralSchemaType schemaType = new LiteralSchemaType();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        schemaType.setDataType(this.removeDataType(propertiesCopy));
        schemaType.setFixedValue(this.removeFixedValue(propertiesCopy));

        /*
         * Any remaining properties are returned in the extended properties.  They are
         * assumed to be defined in a subtype.
         */
        schemaType.setExtendedProperties(this.getRemainingExtendedProperties(propertiesCopy));

        return schemaType;
    }



    /**
     * Return the converted bean.
     *
     * @param instanceProperties properties describing the schema type
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaType getPrimitiveSchemaType(InstanceProperties   instanceProperties)
    {
        PrimitiveSchemaType schemaType = new PrimitiveSchemaType();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        schemaType.setDataType(this.removeDataType(propertiesCopy));
        schemaType.setDefaultValue(this.removeDefaultValue(propertiesCopy));

        /*
         * Any remaining properties are returned in the extended properties.  They are
         * assumed to be defined in a subtype.
         */
        schemaType.setExtendedProperties(this.getRemainingExtendedProperties(propertiesCopy));

        return schemaType;
    }


    /**
     * Return the converted bean.
     *
     * @param instanceProperties properties describing the schema type
     * @param attributeCount number of attributes (for a complex schema type)
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaType getComplexSchemaType(InstanceProperties instanceProperties,
                                            int                attributeCount)
    {
        ComplexSchemaType schemaType = new ComplexSchemaType();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        schemaType.setAttributeCount(attributeCount);

        /*
         * Any remaining properties are returned in the extended properties.  They are
         * assumed to be defined in a subtype.
         */
        schemaType.setExtendedProperties(this.getRemainingExtendedProperties(propertiesCopy));

        return schemaType;
    }



    /**
     * Return the converted bean.
     *
     * @param instanceProperties properties describing the schema type
     * @param attributeCount number of attributes (for a complex schema type)
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaType getAPISchemaType(InstanceProperties instanceProperties,
                                        int                attributeCount)
    {
        APISchemaType schemaType = new APISchemaType();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        schemaType.setOperationCount(attributeCount);

        /*
         * Any remaining properties are returned in the extended properties.  They are
         * assumed to be defined in a subtype.
         */
        schemaType.setExtendedProperties(this.getRemainingExtendedProperties(propertiesCopy));

        return schemaType;
    }


    /**
     * Return the converted bean.
     *
     * @param instanceProperties properties describing the schema type
     * @param validValueSetGUID unique identifier of the set of valid values (for an enum schema type)
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaType getEnumSchemaType(InstanceProperties   instanceProperties,
                                         String               validValueSetGUID)
    {
        EnumSchemaType schemaType = new EnumSchemaType();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        schemaType.setDataType(this.removeDataType(propertiesCopy));
        schemaType.setDefaultValue(this.removeDefaultValue(propertiesCopy));
        schemaType.setValidValueSetGUID(validValueSetGUID);

        /*
         * Any remaining properties are returned in the extended properties.  They are
         * assumed to be defined in a subtype.
         */
        schemaType.setExtendedProperties(this.getRemainingExtendedProperties(propertiesCopy));

        return schemaType;
    }


    /**
     * Return the converted bean.
     *
     * @param instanceProperties properties describing the schema type
     * @param schemaTypeOptions list of schema types that could be the type for this attribute
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaType getSchemaTypeChoice(InstanceProperties   instanceProperties,
                                           List<B>              schemaTypeOptions)
    {
        SchemaTypeChoice schemaType = new SchemaTypeChoice();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        if ((schemaTypeOptions != null) && (! schemaTypeOptions.isEmpty()))
        {
            List<SchemaType> schemaTypeOptionBeans = new ArrayList<>();

            for (B optionBean : schemaTypeOptions)
            {
                if (optionBean != null)
                {
                    schemaTypeOptionBeans.add((SchemaType)optionBean);
                }
            }

            if (! schemaTypeOptionBeans.isEmpty())
            {
                schemaType.setSchemaOptions(schemaTypeOptionBeans);
            }

        }

        /*
         * Any remaining properties are returned in the extended properties.  They are
         * assumed to be defined in a subtype.
         */
        schemaType.setExtendedProperties(this.getRemainingExtendedProperties(propertiesCopy));

        return schemaType;
    }


    /**
     * Return the converted bean.
     *
     * @param instanceProperties properties describing the schema type
     * @param externalSchemaType bean containing the properties of the schema type that is shared by multiple attributes/assets
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaType getExternalSchemaType(InstanceProperties   instanceProperties,
                                             B                    externalSchemaType)
    {
        ExternalSchemaType schemaType = new ExternalSchemaType();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        if (externalSchemaType != null)
        {
            schemaType.setLinkedSchemaType((SchemaType)externalSchemaType);
        }

        /*
         * Any remaining properties are returned in the extended properties.  They are
         * assumed to be defined in a subtype.
         */
        schemaType.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

        return schemaType;
    }


    /**
     * Return the converted bean.
     *
     * @param instanceProperties properties describing the schema type
     * @param mapFromSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param mapToSchemaType bean containing the properties of the schema type that is part of a map definition
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaType getMapSchemaType(InstanceProperties   instanceProperties,
                                        B                    mapFromSchemaType,
                                        B                    mapToSchemaType)
    {
        MapSchemaType schemaType = new MapSchemaType();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        if (mapFromSchemaType != null)
        {
            schemaType.setMapFromElement((SchemaType)mapFromSchemaType);
        }

        if (mapToSchemaType != null)
        {
            schemaType.setMapFromElement((SchemaType)mapFromSchemaType);
        }

        /*
         * Any remaining properties are returned in the extended properties.  They are
         * assumed to be defined in a subtype.
         */
        schemaType.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

        return schemaType;
    }


    /**
     * Set up the properties found in every schema type.
     *
     * @param bean bean to fill
     * @param instanceProperties properties from the entity
     */
    private void updateBasicSchemaTypeProperties(SchemaType         bean,
                                                 InstanceProperties instanceProperties)
    {
        bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
        bean.setDisplayName(this.removeDisplayName(instanceProperties));
        bean.setDescription(this.removeDescription(instanceProperties));
        bean.setIsDeprecated(this.removeIsDeprecated(instanceProperties));
        bean.setVersionNumber(this.removeVersionNumber(instanceProperties));
        bean.setAuthor(this.removeAuthor(instanceProperties));
        bean.setUsage(this.removeUsage(instanceProperties));
        bean.setEncodingStandard(this.removeEncodingStandard(instanceProperties));
        bean.setNamespace(this.removeNamespace(instanceProperties));
        bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
    }
}
