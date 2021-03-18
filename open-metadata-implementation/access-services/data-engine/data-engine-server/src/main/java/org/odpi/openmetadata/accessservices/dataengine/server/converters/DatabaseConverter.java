package org.odpi.openmetadata.accessservices.dataengine.server.converters;

import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class DatabaseConverter<B> extends OpenMetadataAPIGenericConverter<B> {
    public DatabaseConverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(repositoryHelper, serviceName, serverName);
    }

    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that have
     * contain a combination of the properties from an entity and a that os a connected relationship.
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
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof Database)
            {
                Database bean = (Database) returnBean;

                if (entity != null)
                {
                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    bean.setDisplayName(this.removeName(instanceProperties));
                    bean.setDescription(this.removeDescription(instanceProperties));
                    bean.setPathName(this.removePathName(instanceProperties));
                    bean.setCreateTime(this.removeStoreCreateTime(instanceProperties));
                    bean.setModifiedTime(this.removeStoreUpdateTime(instanceProperties));

                    bean.setDatabaseType(this.removeDatabaseType(instanceProperties));
                    bean.setDatabaseVersion(this.removeDatabaseVersion(instanceProperties));
                    bean.setDatabaseInstance(this.removeDatabaseInstance(instanceProperties));
                    bean.setDatabaseImportedFrom(this.removeDatabaseImportedFrom(instanceProperties));

                    /* Note this value should be in the classification */
                    bean.setOwner(this.removeOwner(instanceProperties));
                    /* Note this value should be in the classification */
                    bean.setZoneMembership(this.removeZoneMembership(instanceProperties));


                    InstanceType type = entity.getType();
                    bean.setTypeGUID(type.getTypeDefGUID());
                    bean.setTypeName(type.getTypeDefName());

                    /*
                     * The values in the classifications override the values in the main properties of the Asset's entity.
                     * Having these properties in the main entity is deprecated.
                     */
                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME, entity);

                    bean.setZoneMembership(this.getZoneMembership(instanceProperties));

                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME, entity);

                    bean.setOwner(this.getOwner(instanceProperties));


                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.DATA_STORE_ENCODING_CLASSIFICATION_NAME, entity);

                    bean.setEncodingType(this.getDataStoreEncodingType(instanceProperties));
                    bean.setEncodingLanguage(this.getDataStoreEncodingLanguage(instanceProperties));
                    bean.setEncodingDescription(this.getDataStoreEncodingDescription(instanceProperties));
                    bean.setEncodingProperties(this.getEncodingProperties(instanceProperties));
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
