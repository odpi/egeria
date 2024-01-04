/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.converters;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.FeedbackTargetElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.RatingElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.RatingProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.StarRating;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
 * RatingConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an RatingElement bean.
 */
public class RatingConverter<B> extends AssetManagerOMASConverter<B>
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

            if (returnBean instanceof RatingElement)
            {
                RatingElement bean = (RatingElement) returnBean;
                RatingProperties properties = new RatingProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                InstanceProperties instanceProperties;

                /*
                 * The initial set of values come from the entity.
                 */
                if (entity != null)
                {
                    instanceProperties = new InstanceProperties(entity.getProperties());
                    properties.setUser(entity.getCreatedBy());

                    properties.setReview(this.removeReview(instanceProperties));
                    properties.setStarRating(this.removeStarRatingFromProperties(instanceProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }

                bean.setProperties(properties);

                if (relationship != null)
                {
                    FeedbackTargetElement feedbackTargetElement = new FeedbackTargetElement();

                    instanceProperties = new InstanceProperties(relationship.getProperties());

                    feedbackTargetElement.setRelationshipHeader(super.getMetadataElementHeader(beanClass, relationship, null, methodName));
                    feedbackTargetElement.setRelatedElement(super.getElementStub(beanClass, relationship.getEntityOneProxy(), methodName));
                    feedbackTargetElement.setIsPublic(this.getIsPublic(instanceProperties));

                    bean.setFeedbackTargetElement(feedbackTargetElement);
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
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        return getNewBean(beanClass, entity, null, methodName);
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
                instancePropertiesMap.remove(OpenMetadataType.STARS_PROPERTY_NAME);
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
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(OpenMetadataType.STARS_PROPERTY_NAME);

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
