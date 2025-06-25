/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataStructureElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataStructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * DataStructureConverter generates a DataStructureElement from a DataStructure entity
 * and related elements.
 */
public class DataStructureConverter<B> extends DataDefinitionConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public DataStructureConverter(PropertyHelper propertyHelper,
                                  String         serviceName,
                                  String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or To Do bean which combine knowledge from the element and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewBean(Class<B>                     beanClass,
                        OpenMetadataElement          primaryElement,
                        String                       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof DataStructureElement bean)
            {
                bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryElement, methodName));

                if (primaryElement == null)
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }
                else
                {
                    bean.setProperties(this.getDataStructureProperties(primaryElement));
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
     * Retrieve the dataStructure properties from the retrieved element.
     *
     * @param primaryElement element
     *
     * @return dataStructure properties
     */
    protected DataStructureProperties getDataStructureProperties(OpenMetadataElement primaryElement)
    {
        if (primaryElement.getElementProperties() != null)
        {
            ElementProperties elementProperties = new ElementProperties(primaryElement.getElementProperties());
            DataStructureProperties dataStructureProperties = new DataStructureProperties();

            dataStructureProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
            dataStructureProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            dataStructureProperties.setDisplayName(this.removeDisplayName(elementProperties));
            dataStructureProperties.setDescription(this.removeDescription(elementProperties));
            dataStructureProperties.setNamespace(this.removeNamespace(elementProperties));
            dataStructureProperties.setVersionIdentifier(this.removeVersionIdentifier(elementProperties));
            dataStructureProperties.setEffectiveFrom(primaryElement.getEffectiveFromTime());
            dataStructureProperties.setEffectiveTo(primaryElement.getEffectiveToTime());

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            dataStructureProperties.setTypeName(primaryElement.getType().getTypeName());
            dataStructureProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

            return dataStructureProperties;
        }

        return null;
    }


    /**
     * Summarize the relationships that have no special processing by the subtype.
     *
     * @param beanClass bean class
     * @param relatedMetadataElements elements to summarize
     * @throws PropertyServerException problem in converter
     */
    protected void addRelationshipsToBean(Class<B>                     beanClass,
                                          List<RelatedMetadataElement> relatedMetadataElements,
                                          DataStructureElement         dataStructureElement) throws PropertyServerException
    {
        final String methodName = "addRelationshipToBean";

        if (relatedMetadataElements != null)
        {
            /*
             * These are the relationships for the data definition element
             */
            List<RelatedMetadataElement> others = new ArrayList<>();

            /*
             * Step through the relationships processing those that relate directly to attributed elements
             */
            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.SCHEMA_TYPE_DEFINITION.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                    {
                        dataStructureElement.setEquivalentSchemaType(this.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                    }
                    else
                    {
                        others.add(relatedMetadataElement);
                    }
                }
            }

            super.addRelationshipsToBean(beanClass, others, null, dataStructureElement);
        }
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
                               OpenMetadataElement          primaryElement,
                               List<RelatedMetadataElement> relationships,
                               String                       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof DataStructureElement bean)
            {

                /*
                 * The initial set of values come from the openMetadataElement.
                 */
                if (primaryElement != null)
                {
                    bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryElement, methodName));
                    bean.setProperties(this.getDataStructureProperties(primaryElement));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                if (relationships != null)
                {
                    addRelationshipsToBean(beanClass, relationships, bean);
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
}
