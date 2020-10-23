/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.ReviewMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.Review;
import org.odpi.openmetadata.accessservices.communityprofile.properties.StarRating;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ReviewConverter generates an Review bean from an Review entity and its attachment to a Referenceable.
 */
public class ReviewConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(ReviewConverter.class);

    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param serviceName name of this component
     */
    public ReviewConverter(EntityDetail         entity,
                           Relationship         relationship,
                           OMRSRepositoryHelper repositoryHelper,
                           String               serviceName)
    {
        super(entity, relationship, repositoryHelper, serviceName);
    }


    /**
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public Review getBean()
    {
        final String methodName = "getBean";

        Review  bean = new Review();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setReview(repositoryHelper.removeStringProperty(serviceName, ReviewMapper.REVIEW_PROPERTY_NAME, instanceProperties, methodName));
                bean.setStars(this.getStarRatingFromProperties(instanceProperties));
            }
        }

        if (relationship != null)
        {
            bean.setUserId(relationship.getCreatedBy());
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }


    /**
     * Retrieve the StarRating enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return StarRating  enum value
     */
    private StarRating getStarRatingFromProperties(InstanceProperties   properties)
    {
        StarRating   rating = null;

        if (properties != null)
        {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(ReviewMapper.STARS_PROPERTY_NAME);

            if (instancePropertyValue instanceof EnumPropertyValue)
            {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue)instancePropertyValue;

                switch (enumPropertyValue.getOrdinal())
                {
                    case 0:
                        rating = StarRating.NO_RECOMMENDATION;
                        break;

                    case 1:
                        rating = StarRating.ONE_STAR;
                        break;

                    case 2:
                        rating = StarRating.TWO_STARS;
                        break;

                    case 3:
                        rating = StarRating.THREE_STARS;
                        break;

                    case 4:
                        rating = StarRating.FOUR_STARS;
                        break;

                    case 5:
                        rating = StarRating.FIVE_STARS;
                        break;
                }
            }
        }

        log.debug("StarRating: " + rating.getName());

        return rating;
    }
}
