/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.converters;

import org.odpi.openmetadata.accessservices.assetconsumer.elements.MetadataElement;
import org.odpi.openmetadata.accessservices.assetconsumer.elements.RatingElement;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.StarRating;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.RatingProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;


/**
 * RatingConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from RatingProperties.
 */
public class RatingConverter<B> extends AssetConsumerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public RatingConverter(OMRSRepositoryHelper repositoryHelper,
                           String               serviceName,
                           String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Extract the properties from the entity.  Each DataManager OMAS converter implements this method.
     * The top level fills in the header
     *
     * @param metadataElement output bean
     * @param entity entity containing the properties
     * @param relationship optional relationship containing the properties
     */
    public void updateMetadataElement(MetadataElement metadataElement, EntityDetail entity, Relationship relationship)
    {
        metadataElement.setElementHeader(this.getMetadataElementHeader(entity));

        if (metadataElement instanceof RatingProperties)
        {
            RatingProperties bean = (RatingProperties) metadataElement;

            /*
             * The initial set of values come from the entity.
             */
            InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

            bean.setStarRating(this.removeStarRatingFromProperties(instanceProperties));
            bean.setReview(this.removeReview(instanceProperties));
            bean.setUser(entity.getCreatedBy());

            if (relationship != null)
            {
                instanceProperties = new InstanceProperties(relationship.getProperties());

                bean.setPublic(this.getIsPublic(instanceProperties));
            }
        }
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

            if (returnBean instanceof RatingElement)
            {
                RatingElement bean = (RatingElement) returnBean;

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                InstanceProperties instanceProperties = null;

                /*
                 * The initial set of values come from the entity.
                 */
                if (entity != null)
                {
                    instanceProperties = new InstanceProperties(entity.getProperties());
                }

                bean.setStarRating(this.removeStarRatingFromProperties(instanceProperties));
                bean.setReview(this.removeReview(instanceProperties));
                bean.setUser(entity.getCreatedBy());

                if (relationship != null)
                {
                    instanceProperties = new InstanceProperties(relationship.getProperties());

                    bean.setPublic(this.getIsPublic(instanceProperties));
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
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        return getNewBean(beanClass, entity, null, methodName);
    }


    /**
     * Retrieve and delete the OwnerType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return StarRating  enum value
     */
    private StarRating removeStarRatingFromProperties(InstanceProperties   properties)
    {
        StarRating starRating = this.getStarRatingFromProperties(properties);

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                instancePropertiesMap.remove(OpenMetadataAPIMapper.STARS_PROPERTY_NAME);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return starRating;
    }


    /**
     * Retrieve the StarRating enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return StarRating  enum value
     */
    private StarRating getStarRatingFromProperties(InstanceProperties   properties)
    {
        StarRating starRating = StarRating.NO_RECOMMENDATION;

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
                            starRating = StarRating.NO_RECOMMENDATION;
                            break;

                        case 1:
                            starRating = StarRating.ONE_STAR;
                            break;

                        case 2:
                            starRating = StarRating.TWO_STARS;
                            break;

                        case 3:
                            starRating = StarRating.THREE_STARS;
                            break;

                        case 4:
                            starRating = StarRating.FOUR_STARS;
                            break;

                        case 99:
                            starRating = StarRating.FIVE_STARS;
                            break;
                    }
                }
            }
        }

        return starRating;
    }
}
