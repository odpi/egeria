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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
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
     * as a collection of instances.  For external schema types and map elements, both the GUID and the bean are returned to
     * allow the consuming OMAS to choose whether it is returning GUIDs of the linked to schema or the schema type bean itself.
     *
     * @param beanClass name of the class to create
     * @param schemaRootHeader header of the schema element that holds the root information
     * @param schemaTypeTypeName name of type of the schema type to create
     * @param instanceProperties properties describing the schema type
     * @param schemaRootClassifications classifications from the schema root entity
     * @param attributeCount number of attributes (for a complex schema type)
     * @param validValueSetGUID unique identifier of the set of valid values (for an enum schema type)
     * @param externalSchemaTypeGUID unique identifier of the external schema type
     * @param externalSchemaType unique identifier for the properties of the schema type that is shared by multiple attributes/assets
     * @param mapFromSchemaTypeGUID unique identifier of the mapFrom schema type
     * @param mapFromSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param mapToSchemaTypeGUID unique identifier of the mapTo schema type
     * @param mapToSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param schemaTypeOptionGUIDs list of unique identifiers for schema types that could be the type for this attribute
     * @param schemaTypeOptions list of schema types that could be the type for this attribute
     * @param queryTargetRelationships list of relationships to schema types that contain data values used to derive the schema type value(s)
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
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
                                  String               mapFromSchemaTypeGUID,
                                  B                    mapFromSchemaType,
                                  String               mapToSchemaTypeGUID,
                                  B                    mapToSchemaType,
                                  List<String>         schemaTypeOptionGUIDs,
                                  List<B>              schemaTypeOptions,
                                  List<Relationship>   queryTargetRelationships,
                                  String               methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof SchemaTypeElement)
            {
                if ((schemaRootHeader != null) && (instanceProperties != null))
                {
                    /*
                     * The schema type has different subtypes.
                     */
                    SchemaTypeElement bean = (SchemaTypeElement)returnBean;

                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, schemaRootHeader, schemaRootClassifications, methodName));

                    /*
                     * This next piece of logic sorts out which type of schema properties to create.
                     */

                    if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME))
                    {
                        bean.setSchemaTypeProperties(this.getPrimitiveSchemaType(instanceProperties));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.LITERAL_SCHEMA_TYPE_TYPE_NAME))
                    {
                        bean.setSchemaTypeProperties(this.getLiteralSchemaType(instanceProperties));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.COMPLEX_SCHEMA_TYPE_TYPE_NAME))
                    {
                        bean.setSchemaTypeProperties(this.getComplexSchemaType(instanceProperties));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.LITERAL_SCHEMA_TYPE_TYPE_NAME))
                    {
                        bean.setSchemaTypeProperties(this.getEnumSchemaType(instanceProperties, validValueSetGUID));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.MAP_SCHEMA_TYPE_TYPE_NAME))
                    {
                        bean.setSchemaTypeProperties(this.getMapSchemaType(instanceProperties));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.SCHEMA_TYPE_CHOICE_TYPE_NAME))
                    {
                        bean.setSchemaTypeProperties(this.getSchemaTypeChoice(instanceProperties));
                    }
                    else if (repositoryHelper.isTypeOf(serviceName, schemaTypeTypeName, OpenMetadataAPIMapper.EXTERNAL_SCHEMA_TYPE_TYPE_NAME))
                    {
                        bean.setSchemaTypeProperties(this.getExternalSchemaType(instanceProperties));
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

                    bean.setAttributeCount(attributeCount);
                    bean.setMapFromElement((SchemaTypeElement)mapFromSchemaType);
                    bean.setMapToElement((SchemaTypeElement)mapToSchemaType);
                    bean.setExternalSchemaType((SchemaTypeElement)externalSchemaType);

                    if ((schemaTypeOptions != null) && (! schemaTypeOptions.isEmpty()))
                    {
                        List<SchemaTypeElement> schemaTypeOptionBeans = new ArrayList<>();

                        for (B optionBean : schemaTypeOptions)
                        {
                            if (optionBean != null)
                            {
                                schemaTypeOptionBeans.add((SchemaTypeElement) optionBean);
                            }
                        }

                        if (! schemaTypeOptionBeans.isEmpty())
                        {
                            bean.setSchemaOptions(schemaTypeOptionBeans);
                        }
                    }

                    InstanceProperties classificationProperties =
                                super.getClassificationProperties(OpenMetadataAPIMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                                  schemaRootClassifications);

                    bean.setFormula(this.getFormula(classificationProperties));

                    if (queryTargetRelationships != null)
                    {
                        List<DerivedSchemaTypeQueryTargetProperties> queryTargets = new ArrayList<>();

                        for (Relationship relationship : queryTargetRelationships)
                        {
                            if ((relationship != null) && (relationship.getEntityTwoProxy() != null))
                            {
                                DerivedSchemaTypeQueryTargetProperties queryTargetProperties = new DerivedSchemaTypeQueryTargetProperties();

                                queryTargetProperties.setQueryId(this.getQueryId(relationship.getProperties()));
                                queryTargetProperties.setQuery(this.getQuery(relationship.getProperties()));
                                queryTargetProperties.setQueryTargetGUID(relationship.getEntityTwoProxy().getGUID());

                                queryTargets.add(queryTargetProperties);
                            }
                            else
                            {
                                handleBadRelationship(beanClass.getName(), relationship, methodName);
                            }
                        }
                        if (! queryTargets.isEmpty())
                        {
                            bean.setQueries(queryTargets);
                        }
                    }

                    return returnBean;
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
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaTypeProperties getComplexSchemaType(InstanceProperties instanceProperties)
    {
        ComplexSchemaTypeProperties schemaType = new ComplexSchemaTypeProperties();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

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
     * @return bean populated with properties from the instance properties supplied
     */
    private SchemaTypeProperties getSchemaTypeChoice(InstanceProperties   instanceProperties)
    {
        SchemaTypeChoiceProperties schemaType = new SchemaTypeChoiceProperties();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

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
    private SchemaTypeProperties getExternalSchemaType(InstanceProperties   instanceProperties)
    {
        ExternalSchemaTypeProperties schemaType = new ExternalSchemaTypeProperties();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

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
    private SchemaTypeProperties getMapSchemaType(InstanceProperties   instanceProperties)
    {
        MapSchemaTypeProperties schemaType = new MapSchemaTypeProperties();

        InstanceProperties propertiesCopy = new InstanceProperties(instanceProperties);

        updateBasicSchemaTypeProperties(schemaType, propertiesCopy);

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
