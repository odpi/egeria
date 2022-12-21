/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.converters;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * DatabaseConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a DatabaseElement bean.
 */
public class DatabaseConverter<B> extends DataManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public DatabaseConverter(OMRSRepositoryHelper repositoryHelper,
                             String               serviceName,
                             String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof DatabaseElement)
            {
                DatabaseElement bean = (DatabaseElement) returnBean;
                DatabaseProperties databaseProperties = new DatabaseProperties();

                if (entity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    databaseProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    databaseProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    databaseProperties.setName(this.removeName(instanceProperties));
                    databaseProperties.setVersionIdentifier(this.removeVersionIdentifier(instanceProperties));
                    databaseProperties.setDescription(this.removeDescription(instanceProperties));
                    databaseProperties.setPathName(this.removePathName(instanceProperties));
                    databaseProperties.setCreateTime(this.removeStoreCreateTime(instanceProperties));
                    databaseProperties.setModifiedTime(this.removeStoreUpdateTime(instanceProperties));

                    databaseProperties.setDatabaseType(this.removeDatabaseType(instanceProperties));
                    databaseProperties.setDatabaseVersion(this.removeDatabaseVersion(instanceProperties));
                    databaseProperties.setDatabaseInstance(this.removeDatabaseInstance(instanceProperties));
                    databaseProperties.setDatabaseImportedFrom(this.removeDatabaseImportedFrom(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    databaseProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    databaseProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.DATA_STORE_ENCODING_CLASSIFICATION_NAME, entity);

                    databaseProperties.setEncodingType(this.getDataStoreEncodingType(instanceProperties));
                    databaseProperties.setEncodingLanguage(this.getDataStoreEncodingLanguage(instanceProperties));
                    databaseProperties.setEncodingDescription(this.getDataStoreEncodingDescription(instanceProperties));
                    databaseProperties.setEncodingProperties(this.getEncodingProperties(instanceProperties));

                    bean.setDatabaseProperties(databaseProperties);
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


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        return getNewBean(beanClass, entity, methodName);
    }
}
