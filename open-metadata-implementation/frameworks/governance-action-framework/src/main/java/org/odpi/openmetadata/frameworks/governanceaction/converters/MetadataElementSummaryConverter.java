/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.ToDoActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.ToDoProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * MetadataElementSummaryConverter generates a MetadataElementSummary from an OPenMetadataElement.
 */
public class MetadataElementSummaryConverter<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public MetadataElementSummaryConverter(PropertyHelper propertyHelper,
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
        if (openMetadataElement != null)
        {
            try
            {
                /*
                 * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
                 */
                B returnBean = beanClass.getDeclaredConstructor().newInstance();

                if (returnBean instanceof MetadataElementSummary elementSummary)
                {
                    ElementHeader          elementHeader  = new ElementHeader(openMetadataElement);

                    elementHeader.setGUID(openMetadataElement.getElementGUID());
                    elementHeader.setClassifications(this.getElementClassifications(openMetadataElement.getClassifications()));

                    elementSummary.setElementHeader(elementHeader);
                    if (openMetadataElement.getElementProperties() != null)
                    {
                        elementSummary.setProperties(openMetadataElement.getElementProperties().getPropertiesAsStrings());
                    }
                }
                return returnBean;
            }
            catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
            {
                super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
            }
        }

        return null;
    }


    /**
     * Using the supplied openMetadataElement, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param openMetadataElements list of openMetadataElements containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public List<B> getNewBeans(Class<B>                  beanClass,
                               List<OpenMetadataElement> openMetadataElements,
                               String                    methodName) throws PropertyServerException
    {
        List<B> results = null;

        if (openMetadataElements != null)
        {
            results = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    results.add(this.getNewBean(beanClass, openMetadataElement, methodName));
                }
            }
        }

        return results;
    }
}
