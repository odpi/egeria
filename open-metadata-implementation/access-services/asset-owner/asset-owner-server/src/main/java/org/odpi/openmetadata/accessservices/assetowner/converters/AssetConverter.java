/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.converters;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.assetowner.properties.AssetProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * AssetConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from AssetProperties.
 */
public class AssetConverter<B> extends AssetOwnerOMASConverter<B>
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

            if (returnBean instanceof AssetElement)
            {
                AssetElement bean = (AssetElement) returnBean;
                AssetProperties assetProperties = new AssetProperties();

                if (entity != null)
                {
                    bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    assetProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    assetProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    assetProperties.setName(this.removeName(instanceProperties));
                    assetProperties.setDisplayName(assetProperties.getName());
                    assetProperties.setVersionIdentifier(this.removeVersionIdentifier(instanceProperties));
                    assetProperties.setDescription(this.removeDescription(instanceProperties));

                    /* Note this value should be in the classification */
                    assetProperties.setOwner(this.removeOwner(instanceProperties));
                    /* Note this value should be in the classification */
                    assetProperties.setOwnerType(this.removeOwnerTypeFromProperties(instanceProperties));
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
                    assetProperties.setOwnerType(this.getOwnerTypeFromProperties(instanceProperties));

                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.OWNERSHIP_CLASSIFICATION_TYPE_NAME, entity);

                    assetProperties.setOwner(this.getOwner(instanceProperties));
                    assetProperties.setOwnerTypeName(this.getOwnerTypeName(instanceProperties));
                    assetProperties.setOwnerPropertyName(this.getOwnerPropertyName(instanceProperties));

                    instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME, entity);

                    assetProperties.setOriginOrganizationGUID(this.getOriginOrganizationGUID(instanceProperties));
                    assetProperties.setOriginBusinessCapabilityGUID(this.getOriginBusinessCapabilityGUID(instanceProperties));
                    assetProperties.setOtherOriginValues(this.getOtherOriginValues(instanceProperties));

                    bean.setAssetProperties(assetProperties);
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
