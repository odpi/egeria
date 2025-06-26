/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataClassElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * DataClassConverter generates a DataClassElement from a DataClass entity
 * and related elements.
 */
public class DataClassConverter<B> extends DataDefinitionConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public DataClassConverter(PropertyHelper propertyHelper,
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

            if (returnBean instanceof DataClassElement bean)
            {
                bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryElement, methodName));

                if (primaryElement == null)
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }
                else
                {
                    bean.setProperties(this.getDataClassProperties(primaryElement));
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
     * Retrieve the data class properties from the retrieved element.
     *
     * @param primaryElement element
     *
     * @return dataStructure properties
     */
    private DataClassProperties getDataClassProperties(OpenMetadataElement primaryElement)
    {
        if (primaryElement.getElementProperties() != null)
        {
            ElementProperties elementProperties = new ElementProperties(primaryElement.getElementProperties());
            DataClassProperties dataClassProperties = new DataClassProperties();

            dataClassProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
            dataClassProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            dataClassProperties.setDisplayName(this.removeDisplayName(elementProperties));
            dataClassProperties.setDescription(this.removeDescription(elementProperties));
            dataClassProperties.setNamespace(this.removeNamespace(elementProperties));
            dataClassProperties.setMatchPropertyNames(this.removeMatchPropertyNames(elementProperties));
            dataClassProperties.setMatchThreshold(this.removeMatchThreshold(elementProperties));
            dataClassProperties.setSpecification(this.removeSpecification(elementProperties));
            dataClassProperties.setSpecificationDetails(this.removeSpecificationDetails(elementProperties));
            dataClassProperties.setDataType(this.removeDataType(elementProperties));
            dataClassProperties.setAllowsDuplicateValues(this.removeAllowsDuplicateValues(elementProperties));
            dataClassProperties.setIsCaseSensitive(this.removeIsCaseSensitive(elementProperties));
            dataClassProperties.setIsNullable(this.removeIsNullable(elementProperties));
            dataClassProperties.setDefaultValue(this.removeDefaultValue(elementProperties));
            dataClassProperties.setAverageValue(this.removeAverageValue(elementProperties));
            dataClassProperties.setValueList(this.removeValueList(elementProperties));
            dataClassProperties.setValueRangeFrom(this.removeValueRangeFrom(elementProperties));
            dataClassProperties.setValueRangeTo(this.removeValueRangeTo(elementProperties));
            dataClassProperties.setSampleValues(this.removeSampleValues(elementProperties));
            dataClassProperties.setDataPatterns(this.removeDataPatterns(elementProperties));
            dataClassProperties.setEffectiveFrom(primaryElement.getEffectiveFromTime());
            dataClassProperties.setEffectiveTo(primaryElement.getEffectiveToTime());

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            dataClassProperties.setTypeName(primaryElement.getType().getTypeName());
            dataClassProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

            return dataClassProperties;
        }

        return null;
    }


    /**
     * Summarize the relationships for data class.
     *
     * @param beanClass bean class
     * @param relatedMetadataElements elements to summarize
     * @throws PropertyServerException problem in converter
     */
    protected void addRelationshipsToBean(Class<B>                     beanClass,
                                          List<RelatedMetadataElement> relatedMetadataElements,
                                          DataClassElement             dataClassElement) throws PropertyServerException
    {
        final String methodName = "addRelationshipToBean";

        if (relatedMetadataElements != null)
        {
            /*
             * These are the relationships for the data definition element
             */
            List<RelatedMetadataElementSummary> dataClassHierarchy    = new ArrayList<>();
            List<RelatedMetadataElementSummary> dataClassCompositions = new ArrayList<>();
            List<RelatedMetadataElement>        others                = new ArrayList<>();

            /*
             * Step through the relationships processing those that relate directly to attributed elements
             */
            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.DATA_CLASS_HIERARCHY.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                    {
                        dataClassHierarchy.add(this.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                    }
                    else if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.DATA_CLASS_COMPOSITION.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                    {
                        dataClassCompositions.add(this.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                    }
                    else
                    {
                        others.add(relatedMetadataElement);
                    }
                }
            }

            if (! dataClassHierarchy.isEmpty())
            {
                dataClassElement.setSpecializedDataClasses(dataClassHierarchy);
            }
            if (! dataClassCompositions.isEmpty())
            {
                dataClassElement.setNestedDataClasses(dataClassHierarchy);
            }

            super.addRelationshipsToBean(beanClass, others, dataClassElement);
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

            if (returnBean instanceof DataClassElement bean)
            {

                /*
                 * The initial set of values come from the openMetadataElement.
                 */
                if (primaryElement != null)
                {
                    bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryElement, methodName));
                    bean.setProperties(this.getDataClassProperties(primaryElement));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                if (relationships != null)
                {
                    this.addRelationshipsToBean(beanClass, relationships, bean);
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
