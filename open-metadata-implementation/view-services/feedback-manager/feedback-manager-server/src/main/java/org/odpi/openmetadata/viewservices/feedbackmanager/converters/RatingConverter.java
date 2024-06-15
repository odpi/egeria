/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.feedbackmanager.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.enums.StarRating;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.viewservices.feedbackmanager.metadataelements.RatingElement;
import org.odpi.openmetadata.viewservices.feedbackmanager.properties.RatingProperties;

import java.lang.reflect.InvocationTargetException;


/**
 * RatingConverter generates a RatingElement from a Rating entity
 */
public class RatingConverter<B> extends FeedbackManagerConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public RatingConverter(PropertyHelper propertyHelper,
                           String         serviceName,
                           String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Using the supplied openMetadataElement, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param openMetadataElement openMetadataElement containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>            beanClass,
                        OpenMetadataElement openMetadataElement,
                        String              methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof RatingElement bean)
            {
                RatingProperties properties = new RatingProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, openMetadataElement, methodName));

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the openMetadataElement.
                 */
                if (openMetadataElement != null)
                {
                    elementProperties = new ElementProperties(openMetadataElement.getElementProperties());

                    properties.setReview(this.removeReview(elementProperties));
                    properties.setStarRating(this.removeStarRating(elementProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                bean.setProperties(properties);
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
     * Extract and delete the stars property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    StarRating removeStarRating(ElementProperties elementProperties)
    {
        final String methodName = "removeStarRating";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(serviceName,
                                                                         OpenMetadataProperty.STARS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (StarRating starRating : StarRating.values())
            {
                if (starRating.getName().equals(retrievedProperty))
                {
                    return starRating;
                }
            }
        }

        return null;
    }
}
