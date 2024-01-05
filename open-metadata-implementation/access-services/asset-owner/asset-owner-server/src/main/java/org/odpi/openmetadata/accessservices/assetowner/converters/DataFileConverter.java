/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.converters;

import org.odpi.openmetadata.accessservices.assetowner.metadataelements.FileElement;
import org.odpi.openmetadata.accessservices.assetowner.properties.FileProperties;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * DataFileConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a DataFileElement bean.
 */
public class DataFileConverter<B> extends AssetOwnerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public DataFileConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof FileElement)
            {
                FileElement bean = (FileElement) returnBean;
                FileProperties fileProperties = new FileProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                InstanceProperties instanceProperties;

                /*
                 * The initial set of values come from the entity.
                 */
                if (entity != null)
                {
                    instanceProperties = new InstanceProperties(entity.getProperties());

                    fileProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    fileProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    fileProperties.setName(this.removeName(instanceProperties));
                    fileProperties.setDisplayName(fileProperties.getName());
                    fileProperties.setVersionIdentifier(this.removeVersionIdentifier(instanceProperties));
                    fileProperties.setDescription(this.removeDescription(instanceProperties));
                    fileProperties.setPathName(this.removePathName(instanceProperties));
                    fileProperties.setCreateTime(this.removeStoreCreateTime(instanceProperties));
                    fileProperties.setModifiedTime(this.removeStoreUpdateTime(instanceProperties));

                    /* Note this value should be in the classification */
                    fileProperties.setOwner(this.removeOwner(instanceProperties));
                    /* Note this value should be in the classification */
                    fileProperties.setOwnerType(this.removeOwnerTypeFromProperties(instanceProperties));
                    /* Note this value should be in the classification */
                    fileProperties.setZoneMembership(this.removeZoneMembership(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    fileProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    fileProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    /*
                     * The values in the classifications override the values in the main properties of the Asset's entity.
                     * Having these properties in the main entity is deprecated.
                     */
                    instanceProperties = super.getClassificationProperties(OpenMetadataType.ASSET_ZONES_CLASSIFICATION_NAME, entity);

                    fileProperties.setZoneMembership(this.getZoneMembership(instanceProperties));

                    instanceProperties = super.getClassificationProperties(OpenMetadataType.ASSET_OWNERSHIP_CLASSIFICATION_NAME, entity);

                    fileProperties.setOwner(this.getOwner(instanceProperties));
                    fileProperties.setOwnerType(this.getOwnerTypeFromProperties(instanceProperties));

                    instanceProperties = super.getClassificationProperties(OpenMetadataType.ASSET_ORIGIN_CLASSIFICATION_NAME, entity);

                    fileProperties.setOriginOrganizationGUID(this.getOriginOrganizationGUID(instanceProperties));
                    fileProperties.setOriginBusinessCapabilityGUID(this.getOriginBusinessCapabilityGUID(instanceProperties));
                    fileProperties.setOtherOriginValues(this.getOtherOriginValues(instanceProperties));

                    instanceProperties = super.getClassificationProperties(OpenMetadataType.DATA_STORE_ENCODING_CLASSIFICATION_NAME, entity);

                    fileProperties.setEncodingType(this.getDataStoreEncodingType(instanceProperties));
                    fileProperties.setEncodingLanguage(this.getDataStoreEncodingLanguage(instanceProperties));
                    fileProperties.setEncodingDescription(this.getDataStoreEncodingDescription(instanceProperties));
                    fileProperties.setEncodingProperties(this.getEncodingProperties(instanceProperties));

                    bean.setFileProperties(fileProperties);
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
