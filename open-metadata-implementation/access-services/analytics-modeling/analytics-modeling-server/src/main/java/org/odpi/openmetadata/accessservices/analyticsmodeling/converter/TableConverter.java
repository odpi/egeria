/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.converter;

import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.metadata.TableBean;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * DatabaseConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Database bean.
 */
public class TableConverter extends OpenMetadataAPIGenericConverter<TableBean>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public TableConverter(OMRSRepositoryHelper repositoryHelper,
                             String               serviceName,
                             String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that have
     * contain a combination of the properties from an entity.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public TableBean getNewBean(Class<TableBean>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        if (entity == null) {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }
        
        TableBean bean = new TableBean();
		InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());
		
		bean.setGuid(entity.getGUID());
		bean.setName(removeName(instanceProperties));
		bean.setType(removeSchemaType(instanceProperties));

		return bean;
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that have
     * contain a combination of the properties from an entity and a that os a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public TableBean getNewBean(Class<TableBean>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        return getNewBean(beanClass, entity, methodName);
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
    public <T> TableBean getNewSchemaAttributeBean(Class<TableBean>           beanClass,
                                           EntityDetail       schemaAttributeEntity,
                                           Class<T>           typeClass,
                                           T                  schemaType,
                                           List<Relationship> schemaAttributeRelationships,
                                           String             methodName) throws PropertyServerException
    {
        TableBean bean = new TableBean();
		InstanceProperties instanceProperties = new InstanceProperties(schemaAttributeEntity.getProperties());

		bean.setName(this.removeDisplayName(instanceProperties));
		bean.setGuid(schemaAttributeEntity.getGUID());

		return bean;
    }

}

