/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionMember;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * CollectionMemberConverter generates a CollectionMember bean from a RelatedMetadataElement.
 */
public class CollectionMemberConverter<B> extends OpenMetadataRootConverter<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public CollectionMemberConverter(PropertyHelper propertyHelper,
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
     * @param relatedMetadataElement the properties of an open metadata element plus details of the relationship used to navigate to it
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>               beanClass,
                        RelatedMetadataElement relatedMetadataElement,
                        String                 methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof CollectionMember bean)
            {
                bean.setRelationshipHeader(super.getMetadataElementHeader(beanClass,
                                                                          relatedMetadataElement,
                                                                          relatedMetadataElement.getRelationshipGUID(),
                                                                          null,
                                                                          methodName));

                bean.setRelationshipProperties(super.getCollectionMembershipProperties(relatedMetadataElement));
                bean.setElementHeader(super.getElementStub(beanClass, relatedMetadataElement.getElement(), methodName));
                bean.setProperties(super.getBeanProperties(beanClass, relatedMetadataElement.getElement(), methodName));
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
