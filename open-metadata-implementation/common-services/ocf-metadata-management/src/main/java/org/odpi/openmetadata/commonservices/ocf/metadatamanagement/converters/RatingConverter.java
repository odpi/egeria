/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;


import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.RatingMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Rating;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * RatingConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Rating bean.
 */
public class RatingConverter extends ElementHeaderConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     */
    public RatingConverter(EntityDetail         entity,
                           Relationship         relationship,
                           OMRSRepositoryHelper repositoryHelper,
                           String               serviceName)
    {
        super(entity, relationship, repositoryHelper, serviceName);
    }


    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    public Rating getBean()
    {
        final String  methodName = "getBean";

        Rating  bean = null;

        if ((relationship != null) && (entity != null))
        {
            bean = new Rating();

            super.updateBean(bean);

            bean.setUser(relationship.getCreatedBy());

            InstanceProperties instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setPublic(repositoryHelper.getBooleanProperty(serviceName,
                                                                   RatingMapper.IS_PUBLIC_PROPERTY_NAME,
                                                                   instanceProperties,
                                                                   methodName));
            }

            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setReview(repositoryHelper.removeStringProperty(serviceName,
                                                                     RatingMapper.REVIEW_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName));

                bean.setStarRating(this.removeStarRatingFromProperties(instanceProperties));

                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        return bean;
    }


    /**
     * Retrieve the StarRating enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return   enum value
     */
    private StarRating removeStarRatingFromProperties(InstanceProperties   properties)
    {
        StarRating starRating = StarRating.NOT_RECOMMENDED;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(RatingMapper.STARS_PROPERTY_NAME);

            if (instancePropertyValue instanceof EnumPropertyValue)
            {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                switch (enumPropertyValue.getOrdinal())
                {
                    case 0:
                        starRating = StarRating.NOT_RECOMMENDED;
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

                    case 5:
                        starRating = StarRating.FIVE_STARS;
                        break;
                }

                instancePropertiesMap.remove(RatingMapper.STARS_PROPERTY_NAME);

                properties.setInstanceProperties(instancePropertiesMap);
            }
        }

        return starRating;
    }
}
