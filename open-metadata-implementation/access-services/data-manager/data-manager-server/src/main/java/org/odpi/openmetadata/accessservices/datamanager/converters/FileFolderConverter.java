/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.converters;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.FileFolderElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.FileFolderProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * FileFolderConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a FileFolderElement bean.
 */
public class FileFolderConverter<B> extends DataManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public FileFolderConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof FileFolderElement)
            {
                FileFolderElement bean = (FileFolderElement) returnBean;
                FileFolderProperties fileFolderProperties = new FileFolderProperties();

                if (entity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    fileFolderProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    fileFolderProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    fileFolderProperties.setName(this.removeName(instanceProperties));
                    fileFolderProperties.setDisplayName(fileFolderProperties.getName());
                    fileFolderProperties.setVersionIdentifier(this.removeVersionIdentifier(instanceProperties));
                    fileFolderProperties.setDescription(this.removeDescription(instanceProperties));
                    fileFolderProperties.setPathName(this.removePathName(instanceProperties));
                    fileFolderProperties.setCreateTime(this.removeStoreCreateTime(instanceProperties));
                    fileFolderProperties.setModifiedTime(this.removeStoreUpdateTime(instanceProperties));


                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    fileFolderProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    fileFolderProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));


                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.DATA_STORE_ENCODING_CLASSIFICATION_NAME, entity);

                    fileFolderProperties.setEncodingType(this.getDataStoreEncodingType(instanceProperties));
                    fileFolderProperties.setEncodingLanguage(this.getDataStoreEncodingLanguage(instanceProperties));
                    fileFolderProperties.setEncodingDescription(this.getDataStoreEncodingDescription(instanceProperties));
                    fileFolderProperties.setEncodingProperties(this.getEncodingProperties(instanceProperties));

                    bean.setFileFolderProperties(fileFolderProperties);
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
