/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationshipSummary;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * ToDoConverter generates a ToDoElement from a "To Do" entity
 */
public class MetadataRelationshipSummaryConverter<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public MetadataRelationshipSummaryConverter(PropertyHelper propertyHelper,
                                                String         serviceName,
                                                String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Using the supplied relationship, return a new instance of the bean.  It is used for beans that
     * represent a simple relationship between two entities.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewRelatedMetadataElementsBean(Class<B>                 beanClass,
                                               OpenMetadataRelationship relationship,
                                               String                   methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewRelatedMetadataElementsBean";

        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof MetadataRelationshipSummary elementSummary)
            {
                ElementHeader elementHeader  = new ElementHeader(relationship);

                elementHeader.setGUID(relationship.getRelationshipGUID());

                elementSummary.setElementHeader(elementHeader);
                elementSummary.setEnd1(relationship.getElementAtEnd1());
                elementSummary.setEnd2(relationship.getElementAtEnd2());
                if (relationship.getRelationshipProperties() != null)
                {
                    elementSummary.setProperties(relationship.getRelationshipProperties().getPropertiesAsStrings());
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
     * Using the supplied openMetadataElement, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param openMetadataRelationships list of openMetadataRelationships containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public List<B> getNewBeans(Class<B>                       beanClass,
                               List<OpenMetadataRelationship> openMetadataRelationships,
                               String                         methodName) throws PropertyServerException
    {
        List<B> results = null;

        if (openMetadataRelationships != null)
        {
            results = new ArrayList<>();

            for (OpenMetadataRelationship openMetadataElement : openMetadataRelationships)
            {
                if (openMetadataElement != null)
                {
                    results.add(this.getNewRelatedMetadataElementsBean(beanClass, openMetadataElement, methodName));
                }
            }
        }

        return results;
    }
}
