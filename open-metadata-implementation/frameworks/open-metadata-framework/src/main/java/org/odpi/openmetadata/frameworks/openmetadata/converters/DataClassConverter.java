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
public class DataClassConverter<B> extends OpenMetadataConverterBase<B>
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
    protected DataClassProperties getDataClassProperties(OpenMetadataElement primaryElement)
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
     * Summarize the elements linked off of the data class in specialization hierarchy.
     *
     * @param beanClass bean class
     * @param relatedMetadataElements elements to summarize
     * @return list or null
     * @throws PropertyServerException problem in converter
     */
    protected List<RelatedMetadataElementSummary> getDataClassHierarchy(Class<B>                     beanClass,
                                                                        List<RelatedMetadataElement> relatedMetadataElements) throws PropertyServerException
    {
        final String methodName = "getDataClassHierarchy";

        if (relatedMetadataElements != null)
        {
            List<RelatedMetadataElementSummary> elementSummaries = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if ((relatedMetadataElement != null) && (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.DATA_CLASS_HIERARCHY.typeName)))
                {
                    elementSummaries.add(super.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                }
            }

            return elementSummaries;
        }

        return null;
    }


    /**
     * Summarize the elements linked off of the data class in specialization hierarchy.
     *
     * @param beanClass bean class
     * @param relatedMetadataElements elements to summarize
     * @return list or null
     * @throws PropertyServerException problem in converter
     */
    protected List<RelatedMetadataElementSummary> getDataClassComposition(Class<B>                     beanClass,
                                                                          List<RelatedMetadataElement> relatedMetadataElements) throws PropertyServerException
    {
        final String methodName = "getDataClassComposition";

        if (relatedMetadataElements != null)
        {
            List<RelatedMetadataElementSummary> elementSummaries = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if ((relatedMetadataElement != null) && (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.DATA_CLASS_COMPOSITION.typeName)))
                {
                    elementSummaries.add(super.getRelatedElementSummary(beanClass, relatedMetadataElement, methodName));
                }
            }

            return elementSummaries;
        }

        return null;
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
                    bean.setNestedDataClasses(this.getDataClassComposition(beanClass, relationships));
                    bean.setSpecializedDataClasses(this.getDataClassHierarchy(beanClass, relationships));
                    bean.setExternalReferences(this.getAttribution(beanClass, relationships));
                    bean.setOtherRelatedElements(this.getOtherRelatedElements(beanClass,
                                                                              relationships,
                                                                              Arrays.asList(OpenMetadataType.DATA_CLASS_HIERARCHY.typeName,
                                                                                            OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                                                                            OpenMetadataType.DATA_CLASS_COMPOSITION.typeName)));
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
