/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * ToDoConverter generates a ToDoElement from a "To Do" entity
 */
public class RelatedMetadataElementSummaryConverter<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public RelatedMetadataElementSummaryConverter(PropertyHelper propertyHelper,
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
     * @param relatedMetadataElement openMetadataElement containing the properties
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

            if (returnBean instanceof RelatedMetadataElementSummary relatedElementSummary)
            {
                MetadataElementSummary        elementSummary = new MetadataElementSummary();
                ElementHeader                 elementHeader  = new ElementHeader(relatedMetadataElement);

                elementHeader.setGUID(relatedMetadataElement.getRelationshipGUID());

                relatedElementSummary.setRelationshipHeader(elementHeader);
                if (relatedMetadataElement.getRelationshipProperties() != null)
                {
                    relatedElementSummary.setRelationshipProperties(relatedMetadataElement.getRelationshipProperties().getPropertiesAsStrings());
                }

                elementHeader = new ElementHeader(relatedMetadataElement.getElement());
                elementHeader.setGUID(relatedMetadataElement.getElement().getElementGUID());
                elementHeader.setClassifications(this.getElementClassifications(relatedMetadataElement.getElement().getClassifications()));

                elementSummary.setElementHeader(elementHeader);
                if (relatedMetadataElement.getElement().getElementProperties() != null)
                {
                    elementSummary.setProperties(relatedMetadataElement.getElement().getElementProperties().getPropertiesAsStrings());
                }

                relatedElementSummary.setRelatedElement(elementSummary);
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
     * @param relatedMetadataElements list of relatedMetadataElements containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public List<B> getNewBeans(Class<B>                     beanClass,
                               List<RelatedMetadataElement> relatedMetadataElements,
                               String                       methodName) throws PropertyServerException
    {
        List<B> results = null;

        if (relatedMetadataElements != null)
        {
            results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    results.add(this.getNewBean(beanClass, relatedMetadataElement, methodName));
                }
            }
        }

        return results;
    }


    /**
     * Using the supplied openMetadataElement, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param relatedMetadataElements list of relatedMetadataElements containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public List<B> getNewBeans(Class<B>                     beanClass,
                               RelatedMetadataElementList relatedMetadataElements,
                               String                       methodName) throws PropertyServerException
    {
        if (relatedMetadataElements != null)
        {
            return this.getNewBeans(beanClass, relatedMetadataElements.getElementList(), methodName);
        }

        return null;
    }
}
