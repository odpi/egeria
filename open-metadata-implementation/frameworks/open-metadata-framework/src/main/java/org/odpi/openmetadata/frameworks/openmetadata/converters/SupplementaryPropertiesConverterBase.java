/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AttributedMetadataElement;
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
 * object into a bean that inherits from SupplementaryProperties.
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
            this.addRelationshipsToBean(beanClass, relationships, bean);
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
            this.addRelationshipsToBean(beanClass, relationships, bean);
        }

        return returnBean;
    }
}
