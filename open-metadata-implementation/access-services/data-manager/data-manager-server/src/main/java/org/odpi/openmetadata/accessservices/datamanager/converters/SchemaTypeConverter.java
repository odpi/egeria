/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.converters;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * SchemaTypeConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from SchemaTypeElement.
 */
public class SchemaTypeConverter<B> extends DataManagerOMASConverter<B>
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
     * @param externalSchemaTypeGUID unique identifier of the external schema type
     * @param externalSchemaType bean containing the properties of the schema type that is shared by multiple attributes/assets
     * @param mapFromSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param mapToSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param schemaTypeOptions list of schema types that could be the type for this attribute
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewSchemaTypeBean(Class<B>             beanClass,
                                  InstanceHeader       schemaRootHeader,
                                  String               schemaTypeTypeName,
                                  InstanceProperties   instanceProperties,
                                  List<Classification> schemaRootClassifications,
                                  int                  attributeCount,
                                  String               validValueSetGUID,
                                  String               externalSchemaTypeGUID,
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
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof SchemaTypeElement)
            {
                if ((schemaRootHeader != null) && (instanceProperties != null))
                {
                    /*
                     * The schema type has many different subtypes.
                     * This next piece of logic sorts out which type of schema bean to create.
                     */
                    SchemaTypeElement bean = (SchemaTypeElement)returnBean;

                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, schemaRootHeader, schemaRootClassifications, methodName));
                    bean.setSchemaTypeProperties(this.getSchemaTypeProperties(beanClass,
                                                                              schemaTypeTypeName,
                                                                              instanceProperties,
                                                                              schemaRootClassifications,
                                                                              attributeCount,
                                                                              validValueSetGUID,
                                                                              externalSchemaTypeGUID,
                                                                              externalSchemaType,
                                                                              mapFromSchemaType,
                                                                              mapToSchemaType,
                                                                              schemaTypeOptions,
                                                                              methodName));

                    return returnBean;
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Return the converted bean.  This is a special method used for schema types since they are stored
     * as a collection of instances.
     *
     * @param beanClass name of the class to create
     * @param schemaTypeTypeName name of type of the schema type to create
     * @param instanceProperties properties describing the schema type
     * @param attributeCount number of attributes (for a complex schema type)
     * @param validValueSetGUID unique identifier of the set of valid values (for an enum schema type)
     * @param externalSchemaTypeGUID unique identifier of the external schema type
     * @param externalSchemaType bean containing the properties of the schema type that is shared by multiple attributes/assets
     * @param mapFromSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param mapToSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param schemaTypeOptions list of schema types that could be the type for this attribute
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private SchemaTypeProperties getSchemaTypeProperties(Class<B>             beanClass,
                                                         String               schemaTypeTypeName,
                                                         InstanceProperties   instanceProperties,
                                                         List<Classification> schemaRootClassifications,
                                                         int                  attributeCount,
                                                         String               validValueSetGUID,
                                                         String               externalSchemaTypeGUID,
                                                         B                    externalSchemaType,
                                                         B                    mapFromSchemaType,
                                                         B                    mapToSchemaType,
                                                         List<B>              schemaTypeOptions,
                                                         String               methodName) throws PropertyServerException
    {
        SchemaTypeProperties returnBean = null;

        /*
         * The schema type has many different subtypes.
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
            returnBean = this.getExternalSchemaType(instanceProperties, externalSchemaTypeGUID, externalSchemaType);
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
            InstanceProperties classificationProperties =
                    super.getClassificationProperties(OpenMetadataAPIMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                      schemaRootClassifications);

            returnBean.setFormula(this.getFormula(classificationProperties));
        }

        return returnBean;
    }



    /**
     * Return the converted bean.
     *
     * @param instanceProperties properties describing the schema type
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaTypeProperties getLiteralSchemaType(InstanceProperties   instanceProperties)
    {
        LiteralSchemaTypeProperties schemaType = new LiteralSchemaTypeProperties();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        schemaType.setDataType(this.removeDataType(propertiesCopy));
        schemaType.setFixedValue(this.removeFixedValue(propertiesCopy));

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
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaTypeProperties getPrimitiveSchemaType(InstanceProperties   instanceProperties)
    {
        PrimitiveSchemaTypeProperties schemaType = new PrimitiveSchemaTypeProperties();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        schemaType.setDataType(this.removeDataType(propertiesCopy));
        schemaType.setDefaultValue(this.removeDefaultValue(propertiesCopy));

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
     * @param attributeCount number of attributes (for a complex schema type)
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaTypeProperties getComplexSchemaType(InstanceProperties instanceProperties,
                                                      int                attributeCount)
    {
        ComplexSchemaTypeProperties schemaType = new ComplexSchemaTypeProperties();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        schemaType.setAttributeCount(attributeCount);

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
     * @param validValueSetGUID unique identifier of the set of valid values (for an enum schema type)
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaTypeProperties getEnumSchemaType(InstanceProperties   instanceProperties,
                                                   String               validValueSetGUID)
    {
        EnumSchemaTypeProperties schemaType = new EnumSchemaTypeProperties();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        schemaType.setDataType(this.removeDataType(propertiesCopy));
        schemaType.setDefaultValue(this.removeDefaultValue(propertiesCopy));
        schemaType.setValidValueSetGUID(validValueSetGUID);

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
     * @param schemaTypeOptions list of schema types that could be the type for this attribute
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaTypeProperties getSchemaTypeChoice(InstanceProperties   instanceProperties,
                                                     List<B>              schemaTypeOptions)
    {
        SchemaTypeChoiceProperties schemaType = new SchemaTypeChoiceProperties();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        if ((schemaTypeOptions != null) && (! schemaTypeOptions.isEmpty()))
        {
            List<SchemaTypeProperties> schemaTypeOptionBeans = new ArrayList<>();

            for (B optionBean : schemaTypeOptions)
            {
                if (optionBean != null)
                {
                    schemaTypeOptionBeans.add((SchemaTypeProperties) optionBean);
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
        schemaType.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

        return schemaType;
    }


    /**
     * Return the converted bean.
     *
     * @param instanceProperties properties describing the schema type
     * @param externalSchemaTypeGUID unique identifier of the external schema type
     * @param externalSchemaType bean containing the properties of the schema type that is shared by multiple attributes/assets
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaTypeProperties getExternalSchemaType(InstanceProperties   instanceProperties,
                                                       String               externalSchemaTypeGUID,
                                                       B                    externalSchemaType)
    {
        ExternalSchemaTypeProperties schemaType = new ExternalSchemaTypeProperties();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        if (externalSchemaType != null)
        {
            schemaType.setExternalSchemaTypeGUID(externalSchemaTypeGUID);
            schemaType.setExternalSchemaType((SchemaTypeProperties)externalSchemaType);
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
    private SchemaTypeProperties getMapSchemaType(InstanceProperties   instanceProperties,
                                                  B                    mapFromSchemaType,
                                                  B                    mapToSchemaType)
    {
        MapSchemaTypeProperties schemaType = new MapSchemaTypeProperties();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

        if (mapFromSchemaType != null)
        {
            schemaType.setMapFromElement((SchemaTypeProperties) mapFromSchemaType);
        }

        if (mapToSchemaType != null)
        {
            schemaType.setMapFromElement((SchemaTypeProperties) mapFromSchemaType);
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
    private void updateBasicSchemaTypeProperties(SchemaTypeProperties bean,
                                                 InstanceProperties   instanceProperties)
    {
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
