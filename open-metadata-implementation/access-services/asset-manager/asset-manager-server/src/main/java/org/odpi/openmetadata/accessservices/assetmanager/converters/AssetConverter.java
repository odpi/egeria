/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.converters;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.DataAssetElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.DataAssetProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * AssetConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an DataAssetElement bean.
 */
public class AssetConverter<B> extends AssetManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public AssetConverter(OMRSRepositoryHelper repositoryHelper,
                          String               serviceName,
                          String               serverName)
    {
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

            if (returnBean instanceof DataAssetElement)
            {
                DataAssetElement    bean            = (DataAssetElement) returnBean;
                DataAssetProperties assetProperties = new DataAssetProperties();

                if (entity != null)
                {
                    bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    assetProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    assetProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    assetProperties.setTechnicalName(this.removeName(instanceProperties));
                    assetProperties.setTechnicalDescription(this.removeDescription(instanceProperties));

                    /* Note this value should be in the classification */
                    assetProperties.setOwner(this.removeOwner(instanceProperties));
                    /* Note this value should be in the classification */
                    assetProperties.setOwnerCategory(this.removeOwnerCategoryFromProperties(instanceProperties));
                    /* Note this value should be in the classification */
                    assetProperties.setZoneMembership(this.removeZoneMembership(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    assetProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    assetProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    /*
                     * The values in the classifications override the values in the main properties of the Asset's entity.
                     * Having these properties in the main entity is deprecated.
                     */
                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME, entity);

                    assetProperties.setZoneMembership(this.getZoneMembership(instanceProperties));

                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME, entity);

                    assetProperties.setOwner(this.getOwner(instanceProperties));
                    assetProperties.setOwnerCategory(this.getOwnerCategoryFromProperties(instanceProperties));

                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME, entity);

                    assetProperties.setOriginOrganizationGUID(this.getOriginOrganizationGUID(instanceProperties));
                    assetProperties.setOriginBusinessCapabilityGUID(this.getOriginBusinessCapabilityGUID(instanceProperties));
                    assetProperties.setOtherOriginValues(this.getOtherOriginValues(instanceProperties));

                    bean.setDataAssetProperties(assetProperties);
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
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        return getNewBean(beanClass, entity, methodName);
    }
}
