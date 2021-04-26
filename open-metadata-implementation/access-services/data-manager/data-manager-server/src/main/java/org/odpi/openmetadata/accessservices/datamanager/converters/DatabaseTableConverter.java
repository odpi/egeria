/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.converters;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseTableElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.ComplexSchemaTypeProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseTableProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;


/**
 * DatabaseTableConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an DatabaseTable bean.
 */
public class DatabaseTableConverter<B> extends DataManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public DatabaseTableConverter(OMRSRepositoryHelper repositoryHelper,
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
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof DatabaseTableElement)
            {
                DatabaseTableElement bean = (DatabaseTableElement) returnBean;
                DatabaseTableProperties databaseTableProperties = new DatabaseTableProperties();

                if (schemaAttributeEntity != null)
                {
                    /*
                     * Check that the entity is of the correct type.
                     */
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, schemaAttributeEntity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(schemaAttributeEntity.getProperties());

                    databaseTableProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    databaseTableProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    databaseTableProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    databaseTableProperties.setDescription(this.removeDescription(instanceProperties));

                    databaseTableProperties.setAliases(this.removeAliases(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    databaseTableProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    databaseTableProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setDatabaseTableProperties(databaseTableProperties);

                    if (schemaType instanceof SchemaTypeElement)
                    {
                        SchemaTypeProperties schemaTypeProperties = ((SchemaTypeElement) schemaType).getSchemaTypeProperties();

                        if (schemaTypeProperties instanceof ComplexSchemaTypeProperties)
                        {
                            bean.setDatabaseColumnCount(((ComplexSchemaTypeProperties) schemaTypeProperties).getAttributeCount());
                        }
                    }
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
