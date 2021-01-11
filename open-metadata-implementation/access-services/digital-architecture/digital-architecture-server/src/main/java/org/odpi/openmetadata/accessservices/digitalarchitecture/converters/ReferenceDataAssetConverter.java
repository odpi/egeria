/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.converters;

import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ReferenceDataAssetElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.OwnerCategory;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ReferenceDataAssetProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;


/**
 * ReferenceDataAssetConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from AssetProperties.
 */
public class ReferenceDataAssetConverter<B> extends DigitalArchitectureOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ReferenceDataAssetConverter(OMRSRepositoryHelper repositoryHelper,
                                       String serviceName,
                                       String serverName)
    {
        super(repositoryHelper, serviceName, serverName);
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
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof ReferenceDataAssetElement)
            {
                ReferenceDataAssetElement bean = (ReferenceDataAssetElement) returnBean;

                this.updateSimpleMetadataElement(beanClass, bean, entity, methodName);
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

            if (returnBean instanceof ReferenceDataAssetElement)
            {
                ReferenceDataAssetElement bean = (ReferenceDataAssetElement) returnBean;

                this.updateSimpleMetadataElement(beanClass, bean, entity, methodName);
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
     * Extract the properties from the entity.  Each concrete DataManager OMAS converter implements this method.
     * The top level fills in the header
     *
     * @param beanClass name of the class to create
     * @param bean output bean
     * @param entity entity containing the properties
     * @param methodName calling method
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    void updateSimpleMetadataElement(Class<B>                  beanClass,
                                     ReferenceDataAssetElement bean,
                                     EntityDetail              entity,
                                     String                    methodName)  throws PropertyServerException
    {
        if (entity != null)
        {
            bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));
            ReferenceDataAssetProperties referenceDataAssetProperties = new ReferenceDataAssetProperties();

            /*
             * The initial set of values come from the entity.
             */
            InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

            referenceDataAssetProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
            referenceDataAssetProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
            referenceDataAssetProperties.setDisplayName(this.removeName(instanceProperties));
            referenceDataAssetProperties.setDescription(this.removeDescription(instanceProperties));

            /* Note this value should be in the classification */
            referenceDataAssetProperties.setOwner(this.removeOwner(instanceProperties));
            /* Note this value should be in the classification */
            referenceDataAssetProperties.setOwnerCategory(this.removeOwnerCategoryFromProperties(instanceProperties));
            /* Note this value should be in the classification */
            referenceDataAssetProperties.setZoneMembership(this.removeZoneMembership(instanceProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            referenceDataAssetProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
            referenceDataAssetProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            /*
             * The values in the classifications override the values in the main properties of the Asset's entity.
             * Having these properties in the main entity is deprecated.
             */
            instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME, entity);

            referenceDataAssetProperties.setZoneMembership(this.getZoneMembership(instanceProperties));

            instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME, entity);

            referenceDataAssetProperties.setOwner(this.getOwner(instanceProperties));
            referenceDataAssetProperties.setOwnerCategory(this.getOwnerCategoryFromProperties(instanceProperties));

            instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME, entity);

            referenceDataAssetProperties.setOriginOrganizationGUID(this.getOriginOrganizationGUID(instanceProperties));
            referenceDataAssetProperties.setOriginBusinessCapabilityGUID(this.getOriginBusinessCapabilityGUID(instanceProperties));
            referenceDataAssetProperties.setOtherOriginValues(this.getOtherOriginValues(instanceProperties));

            bean.setReferenceDataAssetProperties(referenceDataAssetProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }
    }


    /**
     * Retrieve and delete the OwnerCategory enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return OwnerType  enum value
     */
    private OwnerCategory removeOwnerCategoryFromProperties(InstanceProperties properties)
    {
        OwnerCategory ownerCategory = this.getOwnerCategoryFromProperties(properties);

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                instancePropertiesMap.remove(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return ownerCategory;
    }


    /**
     * Retrieve the OwnerCategory enum property from the instance properties of a classification
     *
     * @param properties  entity properties
     * @return OwnerType  enum value
     */
    private OwnerCategory getOwnerCategoryFromProperties(InstanceProperties properties)
    {
        OwnerCategory ownerCategory = OwnerCategory.OTHER;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);

                if (instancePropertyValue instanceof EnumPropertyValue)
                {
                    EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                    switch (enumPropertyValue.getOrdinal())
                    {
                        case 0:
                            ownerCategory = OwnerCategory.USER_ID;
                            break;

                        case 1:
                            ownerCategory = OwnerCategory.PROFILE_ID;
                            break;

                        case 99:
                            ownerCategory = OwnerCategory.OTHER;
                            break;
                    }
                }
            }
        }

        return ownerCategory;
    }
}
