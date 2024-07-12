/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.client.converters;

import org.odpi.openmetadata.accessservices.digitalservice.metadataelements.CollectionElement;
import org.odpi.openmetadata.accessservices.digitalservice.properties.CollectionProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * CollectionConverter generates a CollectionElement from a Collection entity
 */
public class CollectionConverter<B> extends DigitalServiceConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public CollectionConverter(PropertyHelper propertyHelper,
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

            if (returnBean instanceof CollectionElement bean)
            {
                CollectionProperties collectionProperties = new CollectionProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, openMetadataElement, methodName));

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the openMetadataElement.
                 */
                if (openMetadataElement != null)
                {
                    elementProperties = new ElementProperties(openMetadataElement.getElementProperties());

                    collectionProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    collectionProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
                    collectionProperties.setName(this.removeName(elementProperties));
                    collectionProperties.setDescription(this.removeDescription(elementProperties));
                    collectionProperties.setCollectionType(this.removeCollectionType(elementProperties));
                    collectionProperties.setEffectiveFrom(openMetadataElement.getEffectiveFromTime());
                    collectionProperties.setEffectiveTo(openMetadataElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    collectionProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    collectionProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                bean.setProperties(collectionProperties);
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

            if (returnBean instanceof CollectionElement bean)
            {
                CollectionProperties collectionProperties = new CollectionProperties();
                OpenMetadataElement  openMetadataElement  = relatedMetadataElement.getElement();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, openMetadataElement, methodName));

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the openMetadataElement.
                 */
                if (openMetadataElement != null)
                {
                    elementProperties = new ElementProperties(openMetadataElement.getElementProperties());

                    collectionProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    collectionProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
                    collectionProperties.setName(this.removeName(elementProperties));
                    collectionProperties.setDescription(this.removeDescription(elementProperties));
                    collectionProperties.setCollectionType(this.removeCollectionType(elementProperties));
                    collectionProperties.setEffectiveFrom(openMetadataElement.getEffectiveFromTime());
                    collectionProperties.setEffectiveTo(openMetadataElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    collectionProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    collectionProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                bean.setProperties(collectionProperties);

                bean.setRelatedElement(super.getRelatedElement(beanClass, relatedMetadataElement, methodName));
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
     * contain a combination of the properties from an element and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param element element containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewBean(Class<B>                beanClass,
                        OpenMetadataElement     element,
                        OpenMetadataRelationship relationship,
                        String                  methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, element, methodName);

        if (returnBean instanceof CollectionElement bean)
        {
            bean.setRelatedElement(super.getRelatedElement(beanClass, element, relationship, methodName));
        }

        return returnBean;
    }
}
