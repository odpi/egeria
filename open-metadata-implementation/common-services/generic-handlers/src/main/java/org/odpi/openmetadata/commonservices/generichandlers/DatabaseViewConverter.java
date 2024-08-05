/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DatabaseViewElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseViewProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * TabularColumnConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a TabularColumnElement bean.
 */
public class DatabaseViewConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public DatabaseViewConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof DatabaseViewElement bean)
            {
                DatabaseViewProperties databaseViewProperties = new DatabaseViewProperties();

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

                    databaseViewProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    databaseViewProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    databaseViewProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    databaseViewProperties.setDescription(this.removeDescription(instanceProperties));
                    databaseViewProperties.setAliases(this.removeAliases(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    databaseViewProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    databaseViewProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setDatabaseViewProperties(databaseViewProperties);
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
