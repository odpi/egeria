/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DerivedSchemaTypeQueryTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * SchemaTypeConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a SchemaTypeElement bean.
 */
public class SchemaTypeConverter<B> extends OMFConverter<B>
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
     * @param schemaRootProperties properties describing the schema type
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
    @Override
    public B getNewSchemaTypeBean(Class<B>             beanClass,
                                  InstanceHeader       schemaRootHeader,
                                  String               schemaTypeTypeName,
                                  InstanceProperties   schemaRootProperties,
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
                if ((schemaRootHeader != null) && (schemaRootProperties != null))
                {
                    SchemaTypeElement bean = (SchemaTypeElement)returnBean;

                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, schemaRootHeader, schemaRootClassifications, methodName));

                    /*
                     * Set up schema properties to create.
                     */
                    InstanceProperties   instanceProperties   = new InstanceProperties(schemaRootProperties);
                    SchemaTypeProperties schemaTypeProperties = new SchemaTypeProperties();

                    schemaTypeProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    schemaTypeProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    schemaTypeProperties.setDescription(this.removeDescription(instanceProperties));
                    schemaTypeProperties.setIsDeprecated(this.removeIsDeprecated(instanceProperties));
                    schemaTypeProperties.setVersionNumber(this.removeVersionNumber(instanceProperties));
                    schemaTypeProperties.setAuthor(this.removeAuthor(instanceProperties));
                    schemaTypeProperties.setUsage(this.removeUsage(instanceProperties));
                    schemaTypeProperties.setEncodingStandard(this.removeEncodingStandard(instanceProperties));
                    schemaTypeProperties.setNamespace(this.removeNamespace(instanceProperties));
                    schemaTypeProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    schemaTypeProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setSchemaTypeProperties(schemaTypeProperties);

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
                            super.getClassificationProperties(OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                              schemaRootClassifications);

                    bean.setFormula(this.getFormula(classificationProperties));
                    bean.setFormulaType(this.getFormulaType(classificationProperties));

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
}
