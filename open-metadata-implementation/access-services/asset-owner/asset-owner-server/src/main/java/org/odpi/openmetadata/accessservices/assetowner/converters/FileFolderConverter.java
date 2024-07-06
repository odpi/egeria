/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.converters;

import org.odpi.openmetadata.accessservices.assetowner.metadataelements.FolderElement;
import org.odpi.openmetadata.accessservices.assetowner.properties.FolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
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
public class FileFolderConverter<B> extends AssetConverter<B>
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
    @SuppressWarnings(value = "deprecation")
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

            if (returnBean instanceof FolderElement)
            {
                FolderElement bean = (FolderElement) returnBean;
                FolderProperties folderProperties = new FolderProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                InstanceProperties instanceProperties;

                /*
                 * The initial set of values come from the entity.
                 */
                if (entity != null)
                {
                    instanceProperties = new InstanceProperties(entity.getProperties());

                    folderProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    folderProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    folderProperties.setName(this.removeName(instanceProperties));
                    folderProperties.setDisplayName(folderProperties.getName());
                    folderProperties.setVersionIdentifier(this.removeVersionIdentifier(instanceProperties));
                    folderProperties.setDescription(this.removeDescription(instanceProperties));
                    folderProperties.setPathName(this.removePathName(instanceProperties));
                    folderProperties.setCreateTime(this.removeStoreCreateTime(instanceProperties));
                    folderProperties.setModifiedTime(this.removeStoreUpdateTime(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    folderProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    folderProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    instanceProperties = super.getClassificationProperties(OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.typeName, entity);

                    folderProperties.setEncodingType(this.getDataStoreEncodingType(instanceProperties));
                    folderProperties.setEncodingLanguage(this.getDataStoreEncodingLanguage(instanceProperties));
                    folderProperties.setEncodingDescription(this.getDataStoreEncodingDescription(instanceProperties));
                    folderProperties.setEncodingProperties(this.getEncodingProperties(instanceProperties));

                    bean.setFolderProperties(folderProperties);
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
