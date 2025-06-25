/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AttributedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.SupplementaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;


/**
 * SupplementaryPropertiesConverterBase provides common methods for transferring relevant properties from an Open Metadata Element
 * object into a bean whose properties inherits from SupplementaryProperties.
 */
public class SupplementaryPropertiesConverterBase<B> extends AttributedElementConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public SupplementaryPropertiesConverterBase(PropertyHelper propertyHelper,
                                                String         serviceName,
                                                String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }



    /**
     * Summarize the relationships that have no special processing by the subtype.
     *
     * @param relatedMetadataElements elements to summarize
     * @param supplementaryProperties bean to fill
     * @return unprocessed relationships
     * @throws PropertyServerException problem in converter
     */
    protected List<RelatedMetadataElement> addSupplementaryProperties(List<RelatedMetadataElement> relatedMetadataElements,
                                                                      SupplementaryProperties      supplementaryProperties) throws PropertyServerException
    {
        final String methodName = "addSupplementaryProperties";

        if (relatedMetadataElements != null)
        {
            /*
             * These are the relationships for the attributed element
             */

            List<RelatedMetadataElement> others             = new ArrayList<>();

            /*
             * Step through the relationships processing those that relate directly to attributed elements
             */
            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.SUPPLEMENTARY_PROPERTIES_RELATIONSHIP.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                    {
                        supplementaryProperties.setDisplayName(propertyHelper.getStringProperty(serviceName,
                                                                                                OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                relatedMetadataElement.getElement().getElementProperties(),
                                                                                                methodName));
                        supplementaryProperties.setDisplaySummary(propertyHelper.getStringProperty(serviceName,
                                                                                                  OpenMetadataProperty.SUMMARY.name,
                                                                                                  relatedMetadataElement.getElement().getElementProperties(),
                                                                                                  methodName));
                        supplementaryProperties.setDisplayDescription(propertyHelper.getStringProperty(serviceName,
                                                                                                       OpenMetadataProperty.DESCRIPTION.name,
                                                                                                       relatedMetadataElement.getElement().getElementProperties(),
                                                                                                       methodName));
                        supplementaryProperties.setAbbreviation(propertyHelper.getStringProperty(serviceName,
                                                                                                 OpenMetadataProperty.ABBREVIATION.name,
                                                                                                 relatedMetadataElement.getElement().getElementProperties(),
                                                                                                 methodName));
                        supplementaryProperties.setUsage(propertyHelper.getStringProperty(serviceName,
                                                                                          OpenMetadataProperty.USAGE.name,
                                                                                          relatedMetadataElement.getElement().getElementProperties(),
                                                                                          methodName));
                    }
                    else
                    {
                        others.add(relatedMetadataElement);
                    }
                }
            }

            return others;
        }

        return null;
    }


    /**
     * Using the supplied relatedMetadataElement, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param relatedMetadataElement relatedMetadataElement containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the relatedMetadataElement supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewBean(Class<B>               beanClass,
                        RelatedMetadataElement relatedMetadataElement,
                        String                 methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewBean(relatedMetadataElement)";

        B returnBean = this.getNewBean(beanClass, relatedMetadataElement.getElement(), methodName);

        if (returnBean instanceof AttributedMetadataElement bean)
        {
            bean.setRelatedBy(super.getRelatedBy(beanClass, relatedMetadataElement, methodName));
        }

        return returnBean;
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
    public B getNewBean(Class<B>                 beanClass,
                        OpenMetadataElement      element,
                        OpenMetadataRelationship relationship,
                        String                   methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewBean(entity, relationship)";

        B returnBean = this.getNewBean(beanClass, element, methodName);

        if (returnBean instanceof AttributedMetadataElement bean)
        {
            bean.setRelatedBy(super.getRelatedBy(beanClass, relationship, methodName));
        }

        return returnBean;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or To Do bean which combine knowledge from the element and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewComplexBean(Class<B>                     beanClass,
                               OpenMetadataElement          primaryElement,
                               List<RelatedMetadataElement> relationships,
                               String                       methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, primaryElement, methodName);

        if (returnBean instanceof AttributedMetadataElement bean)
        {
            this.addRelationshipsToBean(beanClass, relationships, null, bean);
        }

        return returnBean;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or To Do bean which combine knowledge from the element and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewComplexBean(Class<B>                     beanClass,
                               RelatedMetadataElement       primaryElement,
                               List<RelatedMetadataElement> relationships,
                               String                       methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, primaryElement, methodName);

        if (returnBean instanceof AttributedMetadataElement bean)
        {
            this.addRelationshipsToBean(beanClass, relationships, null, bean);
        }

        return returnBean;
    }
}
