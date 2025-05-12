/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.converters;


import org.odpi.openmetadata.commonservices.generichandlers.OMFConverter;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Rating;
import org.odpi.openmetadata.frameworks.openmetadata.enums.StarRating;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * RatingConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Rating bean.
 */
public class RatingConverter<B> extends OMFConverter<B>
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
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof Rating bean)
            {
                /*
                 * Check that the entity is of the correct type.
                 */
                this.setUpElementHeader(bean, entity, OpenMetadataType.RATING.typeName, methodName);

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

                    bean.setIsPublic(this.getIsPublic(instanceProperties));
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
     * Retrieve and delete the StarRating enum property from the instance properties of an entity
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
                instancePropertiesMap.remove(OpenMetadataProperty.STARS.name);
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
        StarRating starRating = StarRating.NOT_RECOMMENDED;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(OpenMetadataProperty.STARS.name);

                if (instancePropertyValue instanceof EnumPropertyValue enumPropertyValue)
                {
                    switch (enumPropertyValue.getOrdinal())
                    {
                        case 0 -> starRating = StarRating.NOT_RECOMMENDED;
                        case 1 -> starRating = StarRating.ONE_STAR;
                        case 2 -> starRating = StarRating.TWO_STARS;
                        case 3 -> starRating = StarRating.THREE_STARS;
                        case 4 -> starRating = StarRating.FOUR_STARS;
                        case 99 -> starRating = StarRating.FIVE_STARS;
                    }
                }
            }
        }

        return starRating;
    }
}
