/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DatabaseColumnElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DerivedSchemaTypeQueryTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseForeignKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabasePrimaryKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseColumnConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a DatabaseColumnElement bean.
 */
public class DatabaseColumnConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public DatabaseColumnConverter(OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Extract the properties from the schema attribute entity.  Each API creates a specialization of this method for its beans.
     *
     * @param beanClass name of the class to create
     * @param schemaAttributeEntity entity containing the properties for the main schema attribute
     * @param typeClass name of type used to describe the schema type
     * @param schemaType bean containing the properties of the schema type - this is filled out by the schema type converter
     * @param schemaAttributeRelationships relationships containing the links to other schema attributes
     * @param methodName calling method
     * @param <T> bean type used to create the schema type
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public <T> B getNewSchemaAttributeBean(Class<B>           beanClass,
                                           EntityDetail       schemaAttributeEntity,
                                           Class<T>           typeClass,
                                           T                  schemaType,
                                           List<Relationship> schemaAttributeRelationships,
                                           String             methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();
            if (returnBean instanceof DatabaseColumnElement bean)
            {
                DatabaseColumnProperties properties = new DatabaseColumnProperties();

                if (schemaAttributeEntity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, schemaAttributeEntity, methodName));
                    super.setUpSchemaAttribute(schemaAttributeEntity, null, properties);

                    properties.setTypeName(bean.getElementHeader().getType().getTypeName());

                    if (schemaType instanceof SchemaTypeElement schemaTypeElement)
                    {
                        super.addSchemaTypeToAttribute(schemaTypeElement, properties);

                        properties.setFormula(schemaTypeElement.getFormula());
                        if ((schemaTypeElement.getQueries() != null) && (! schemaTypeElement.getQueries().isEmpty()))
                        {
                            List<DatabaseQueryProperties> databaseQueryPropertiesList = new ArrayList<>();

                            for (DerivedSchemaTypeQueryTargetProperties derivedSchemaTypeQueryTargetProperties : schemaTypeElement.getQueries())
                            {
                                if (derivedSchemaTypeQueryTargetProperties != null)
                                {
                                    DatabaseQueryProperties databaseQueryProperties = new DatabaseQueryProperties();

                                    databaseQueryProperties.setQuery(derivedSchemaTypeQueryTargetProperties.getQuery());
                                    databaseQueryProperties.setQueryId(derivedSchemaTypeQueryTargetProperties.getQueryId());
                                    databaseQueryProperties.setQueryTargetGUID(derivedSchemaTypeQueryTargetProperties.getQueryTargetGUID());

                                    databaseQueryPropertiesList.add(databaseQueryProperties);
                                }
                            }

                            properties.setQueries(databaseQueryPropertiesList);
                        }
                    }

                    InstanceProperties classificationProperties = super.getClassificationProperties(OpenMetadataType.PRIMARY_KEY_CLASSIFICATION.typeName, schemaAttributeEntity);

                    if (classificationProperties != null)
                    {
                        DatabasePrimaryKeyProperties primaryKeyProperties = new DatabasePrimaryKeyProperties();

                        primaryKeyProperties.setName(this.removeName(classificationProperties));
                        primaryKeyProperties.setKeyPattern(this.removeKeyPattern(classificationProperties));

                        primaryKeyProperties.setExtendedProperties(this.getRemainingExtendedProperties(classificationProperties));

                        bean.setPrimaryKeyProperties(primaryKeyProperties);
                    }

                    if (schemaAttributeRelationships != null)
                    {
                        for (Relationship relationship : schemaAttributeRelationships)
                        {
                            if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName))
                            {
                                /*
                                 * Foreign key properties are only set up in the column that contains the  foreign key which is at end 2 of the relationship.
                                 */
                                if (schemaAttributeEntity.getGUID().equals(relationship.getEntityTwoProxy().getGUID()))
                                {
                                    DatabaseForeignKeyProperties databaseForeignKeyProperties = new DatabaseForeignKeyProperties();

                                    InstanceProperties relationshipProperties = relationship.getProperties();

                                    databaseForeignKeyProperties.setEffectiveFrom(relationshipProperties.getEffectiveFromTime());
                                    databaseForeignKeyProperties.setEffectiveTo(relationshipProperties.getEffectiveToTime());

                                    databaseForeignKeyProperties.setName(this.removeName(relationshipProperties));
                                    databaseForeignKeyProperties.setDescription(this.removeDataFieldDescription(relationshipProperties));
                                    databaseForeignKeyProperties.setConfidence(this.removeConfidence(relationshipProperties));
                                    databaseForeignKeyProperties.setSteward(this.removeSteward(relationshipProperties));
                                    databaseForeignKeyProperties.setSource(this.removeSource(relationshipProperties));

                                    databaseForeignKeyProperties.setExtendedProperties(this.getRemainingExtendedProperties(relationshipProperties));

                                    bean.setForeignKeyProperties(databaseForeignKeyProperties);

                                    /*
                                     * These values reference the column that is the primary key.
                                     */
                                    bean.setReferencedColumnGUID(relationship.getEntityOneProxy().getGUID());
                                    bean.setReferencedColumnQualifiedName(this.getQualifiedName(relationship.getEntityOneProxy().getUniqueProperties()));
                                }
                            }
                        }
                    }

                    bean.setDatabaseColumnProperties(properties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
