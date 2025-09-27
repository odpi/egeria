/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.ArrayList;
import java.util.List;


/**
 * OpenMetadataConverterBase provides the generic methods for the bean converters used to provide translation between
 * specific API beans and the Open Metadata services beans from the Open Survey Framework (OGF).
 * Generic classes have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing an Open Metadata API bean.
 */
public class OpenMetadataConverterBase<B> extends OpenMetadataPropertyConverterBase
{
    protected String         localServerName;


    /**
     * Constructor captures the initial content
     *
     * @param propertyHelper helper object to parse element
     * @param localServiceName name of this component
     * @param localServerName name of this server
     */
    public OpenMetadataConverterBase(PropertyHelper propertyHelper,
                                     String         localServiceName,
                                     String         localServerName)
    {
        super(propertyHelper, localServiceName);

        this.localServerName = localServerName;
    }


    /* ====================================================================================================
     * This first set of methods represents the external interface of the converter.  These are the methods
     * called from the clients.  They define which type of bean is required and provide a set
     * of open metadata instances to use to fill the bean.  These methods are overridden by the specific
     * converters.
     */


    /**
     * Using the supplied openMetadataElement, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param openMetadataElement openMetadataElement containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the openMetadataElement supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewBean(Class<B>            beanClass,
                        OpenMetadataElement openMetadataElement,
                        String              methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewBean(openMetadataElement)";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

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

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

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
    public B getNewBean(Class<B>                 beanClass,
                        OpenMetadataElement      element,
                        OpenMetadataRelationship relationship,
                        String                   methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewBean(element, relationship)";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an AnnotationProperties or To Do bean which combine knowledge from the element and its linked relationships.
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
    public B getNewComplexBean(Class<B>                   beanClass,
                               OpenMetadataElement        primaryElement,
                               RelatedMetadataElementList relationships,
                               String                     methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewComplexBean";

        if ((relationships != null) && (relationships.getElementList() != null))
        {
            return getNewComplexBean(beanClass, primaryElement, relationships.getElementList(), methodName);
        }
        else
        {
            return getNewComplexBean(beanClass, primaryElement, (List<RelatedMetadataElement>)null, methodName);
        }
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an AnnotationProperties or To Do bean which combine knowledge from the element and its linked relationships.
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
    public B getNewComplexBean(Class<B>                   beanClass,
                               RelatedMetadataElement     primaryElement,
                               RelatedMetadataElementList relationships,
                               String                     methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewComplexBean";

        if ((relationships != null) && (relationships.getElementList() != null))
        {
            return getNewComplexBean(beanClass, primaryElement, relationships.getElementList(), methodName);
        }
        else
        {
            return getNewComplexBean(beanClass, primaryElement, (List<RelatedMetadataElement>)null, methodName);
        }
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an AnnotationProperties or To Do bean which combine knowledge from the element and its linked relationships.
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
        final String thisMethodName = "getNewComplexBean";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an AnnotationProperties or To Do bean which combine knowledge from the element and its linked relationships.
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
        final String thisMethodName = "getNewComplexBean";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * a connection bean which made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted if they do not have any properties.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the content of the bean
     * @param supplementaryEntities entities connected to the primary element by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewComplexBean(Class<B>                       beanClass,
                               OpenMetadataElement            primaryElement,
                               List<OpenMetadataElement>      supplementaryEntities,
                               List<OpenMetadataRelationship> relationships,
                               String                         methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewComplexBean(with supplementary entities)";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
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

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /* ==========================================================
     * This method throws the exception that occurs if an OMVS fails to implement one of the updateXXXBean methods or
     * the converter is configured with an invalid bean class.
     */

    /**
     * Throw an exception to indicate that one of the update methods has not been implemented by a handler.
     *
     * @param beanClassName class name of bean
     * @param missingMethodName method tha has not been implemented
     * @param converterClassName class that detected the missing method
     * @param methodName method that is missing
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been called with a method that is unexpected for the specific type of
     * bean that this converter is implemented for.
     */
    protected void handleUnimplementedConverterMethod(String beanClassName,
                                                      String missingMethodName,
                                                      String converterClassName,
                                                      String methodName) throws PropertyServerException
    {
        throw new PropertyServerException(OMFErrorCode.MISSING_CONVERTER_METHOD.getMessageDefinition(localServiceName,
                                                                                                     missingMethodName,
                                                                                                     converterClassName,
                                                                                                     beanClassName,
                                                                                                     methodName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Throw an exception to indicate that one of the update methods has not been implemented by a handler.
     *
     * @param beanClassName class name of bean
     * @param error exception generated when the new bean is created
     * @param methodName method that is missing
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is not a known class
     */
    protected void handleInvalidBeanClass(String    beanClassName,
                                          Exception error,
                                          String    methodName) throws PropertyServerException
    {
        throw new PropertyServerException(OMFErrorCode.INVALID_BEAN_CLASS.getMessageDefinition(beanClassName,
                                                                                               methodName,
                                                                                               localServiceName,
                                                                                               localServerName,
                                                                                               error.getMessage()),
                                          this.getClass().getName(),
                                          methodName,
                                          error);
    }


    /**
     * Throw an exception to indicate that a retrieved element has missing information.
     *
     * @param beanClassName class name of bean
     * @param element the element with the bad header
     * @param methodName calling method
     * @throws PropertyServerException an invalid instance has been returned from the metadata repositories
     */
    protected void handleBadEntity(String                 beanClassName,
                                   OpenMetadataElement    element,
                                   String                 methodName) throws PropertyServerException
    {
        if (element == null)
        {
            handleMissingMetadataInstance(beanClassName, OpenMetadataElement.class.getName(), methodName);
        }
        else
        {
            throw new PropertyServerException(OMFErrorCode.BAD_ENTITY.getMessageDefinition(methodName,
                                                                                           localServiceName,
                                                                                                       element.toString()),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Throw an exception to indicate that a critical instance (typically the main element) has not been passed
     * to the converter.
     *
     * @param beanClassName class name of bean
     * @param relationship the relationship with the bad header
     * @param methodName calling method
     * @throws PropertyServerException an invalid instance has been returned from the metadata repositories
     */
    protected void handleBadRelatedMetadataElements(String                  beanClassName,
                                                    OpenMetadataRelationship relationship,
                                                    String                  methodName) throws PropertyServerException
    {
        if (relationship == null)
        {
            handleMissingMetadataInstance(beanClassName, OpenMetadataRelationship.class.getName(), methodName);
        }
        else
        {
            throw new PropertyServerException(OMFErrorCode.BAD_RELATIONSHIP.getMessageDefinition(methodName,
                                                                                                 localServiceName,
                                                                                                 relationship.toString()),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /* ======================================================
     * The methods that follow are used by the subclasses to extract specific properties from the element properties.
     * They are used for all properties except enums which need a specific method in the converters.
     */

    /**
     * Extract the properties for the requested classification from the element.
     *
     * @param classificationName name of classification
     * @param element element containing classification
     * @return list of properties for the named classification
     */
    protected ElementProperties getClassificationProperties(String              classificationName,
                                                            OpenMetadataElement element)
    {
        if (element != null)
        {
            List<AttachedClassification> elementClassifications = element.getClassifications();

            if (elementClassifications != null)
            {
                return getClassificationProperties(classificationName, elementClassifications);
            }
        }

        return null;
    }


    /**
     * Extract the properties for the requested classification from the list of classifications from
     * OpenMetadataElement.
     *
     * @param classificationName name of classification
     * @param elementClassifications list of classifications from an element
     * @return list of properties for the named classification
     */
    protected ElementProperties getClassificationProperties(String                       classificationName,
                                                            List<AttachedClassification> elementClassifications)
    {
        if (elementClassifications != null)
        {
            for (AttachedClassification elementClassification : elementClassifications)
            {
                if (elementClassification != null)
                {
                    if (classificationName.equals(elementClassification.getClassificationName()))
                    {
                        return elementClassification.getClassificationProperties();
                    }
                }
            }
        }

        return null;
    }


    /**
     * Extract the properties from the element.
     *
     * @param beanClass name of the class to create
     * @param element element containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    protected ElementHeader getMetadataElementHeader(Class<B>            beanClass,
                                                     OpenMetadataElement element,
                                                     String              methodName) throws PropertyServerException
    {
        if (element != null)
        {
            return getMetadataElementHeader(beanClass,
                                            element,
                                            element.getElementGUID(),
                                            element.getClassifications(),
                                            methodName);
        }
        else
        {
            this.handleMissingMetadataInstance(beanClass.getName(),
                                               OpenMetadataElement.class.getName(),
                                               methodName);
        }

        return null;
    }


    /**
     * Extract the properties from the element.
     *
     * @param beanClass name of the class to create
     * @param header header from the element containing the properties
     * @param elementGUID unique identifier of the element
     * @param classifications classification if this is an element
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public ElementHeader getMetadataElementHeader(Class<B>                     beanClass,
                                                  ElementControlHeader         header,
                                                  String                       elementGUID,
                                                  List<AttachedClassification> classifications,
                                                  String                       methodName) throws PropertyServerException
    {
        if (header != null)
        {
            return propertyHelper.getElementHeader(header, elementGUID, classifications);
        }
        else
        {
            this.handleMissingMetadataInstance(beanClass.getName(), ElementControlHeader.class.getName(), methodName);
        }

        return null;
    }


    /**
     * Convert a list of relationships
     * @param beanClass specific class
     * @param retrievedRelationships values retrieved from the metadata repository
     * @param methodName calling method
     * @return list of relationships
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public List<RelationshipElement> getRelationshipElements(Class<B>                       beanClass,
                                                             List<OpenMetadataRelationship> retrievedRelationships,
                                                             String                         methodName) throws PropertyServerException
    {
        if (retrievedRelationships != null)
        {
            List<RelationshipElement> relationshipElements = new ArrayList<>();

            for (OpenMetadataRelationship retrievedRelationship : retrievedRelationships)
            {
                if (retrievedRelationship != null)
                {
                    RelationshipElement relationshipElement = new RelationshipElement();

                    relationshipElement.setRelationshipHeader(this.getMetadataElementHeader(beanClass,
                                                                                            retrievedRelationship,
                                                                                            retrievedRelationship.getRelationshipGUID(),
                                                                                            null,
                                                                                            methodName));
                    RelationshipBeanProperties relationshipProperties = new RelationshipBeanProperties();

                    relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(retrievedRelationship.getRelationshipProperties()));
                    relationshipProperties.setEffectiveFrom(retrievedRelationship.getEffectiveFromTime());
                    relationshipProperties.setEffectiveTo(retrievedRelationship.getEffectiveToTime());

                    relationshipElement.setRelationshipProperties(relationshipProperties);

                    relationshipElement.setEnd1(getElementStub(beanClass, retrievedRelationship.getElementAtEnd1(), methodName));
                    relationshipElement.setEnd2(getElementStub(beanClass, retrievedRelationship.getElementAtEnd2(), methodName));

                    relationshipElements.add(relationshipElement);
                }
            }

            return relationshipElements;
        }

        return null;
    }


    /**
     * Extract the properties from the element.
     *
     * @param beanClass name of the class to create
     * @param element from the repository
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public MetadataElementSummary getElementSummary(Class<B>            beanClass,
                                                    OpenMetadataElement element,
                                                    String              methodName) throws PropertyServerException
    {
        if (element != null)
        {
            MetadataElementSummary elementSummary = new MetadataElementSummary();
            ElementHeader          elementHeader  = getMetadataElementHeader(beanClass, element, methodName);

            elementSummary.setElementHeader(elementHeader);
            elementSummary.setProperties(getBeanProperties(element));

            return elementSummary;
        }
        else
        {
            this.handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
        }

        return null;
    }


    /**
     * Extract the properties from the element.
     *
     * @param relatedElement from the repository
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public RelatedMetadataElementSummary getRelatedElementSummary(RelatedMetadataElement relatedElement) throws PropertyServerException
    {
        if ((relatedElement != null) && (relatedElement.getElement() != null))
        {
            return propertyHelper.getRelatedElementSummary(relatedElement);
        }

        return null;
    }



    /**
     * Summarize the related elements of the requested type
     *
     * @param requestedRelationshipType relationship type to extract
     * @param relatedMetadataElements elements to summarize
     * @return list or null
     * @throws PropertyServerException problem in converter
     */
    protected List<RelatedMetadataElementSummary> getRelatedElements(String                       requestedRelationshipType,
                                                                     List<RelatedMetadataElement> relatedMetadataElements) throws PropertyServerException
    {
        if (relatedMetadataElements != null)
        {
            List<RelatedMetadataElementSummary> matchingElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if ((relatedMetadataElement != null) && (propertyHelper.isTypeOf(relatedMetadataElement, requestedRelationshipType)))
                {
                    matchingElements.add(this.getRelatedElementSummary(relatedMetadataElement));
                }
            }

            if (! matchingElements.isEmpty())
            {
                return matchingElements;
            }
        }

        return null;
    }


    /**
     * Summarize the related elements of the requested type
     *
     * @param requestedRelationshipType relationship type to extract
     * @param relatedMetadataElements elements to summarize
     * @param relatedElementAtEnd1 which end is it connected to
     * @return list or null
     * @throws PropertyServerException problem in converter
     */
    protected List<RelatedMetadataElementSummary> getRelatedElements(String                       requestedRelationshipType,
                                                                     List<RelatedMetadataElement> relatedMetadataElements,
                                                                     boolean                      relatedElementAtEnd1) throws PropertyServerException
    {
        if (relatedMetadataElements != null)
        {
            List<RelatedMetadataElementSummary> matchingElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if ((relatedMetadataElement != null) && (propertyHelper.isTypeOf(relatedMetadataElement, requestedRelationshipType)))
                {
                    if (relatedElementAtEnd1 == relatedMetadataElement.getElementAtEnd1())
                    {
                        matchingElements.add(this.getRelatedElementSummary(relatedMetadataElement));
                    }
                }
            }

            if (! matchingElements.isEmpty())
            {
                return matchingElements;
            }
        }

        return null;
    }


    /**
     * Summarize the related elements of the requested type
     *
     * @param requestedRelationshipType relationship type to extract
     * @param relatedMetadataElements elements to summarize
     * @param relatedElementAtEnd1 which end is it connected to
     * @return list or null
     * @throws PropertyServerException problem in converter
     */
    protected RelatedMetadataElementSummary getRelatedElement(String                       requestedRelationshipType,
                                                              List<RelatedMetadataElement> relatedMetadataElements,
                                                              boolean                      relatedElementAtEnd1) throws PropertyServerException
    {
        if (relatedMetadataElements != null)
        {
            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if ((relatedMetadataElement != null) && (propertyHelper.isTypeOf(relatedMetadataElement, requestedRelationshipType)))
                {
                    if (relatedElementAtEnd1 == relatedMetadataElement.getElementAtEnd1())
                    {
                        return this.getRelatedElementSummary(relatedMetadataElement);
                    }
                }
            }
        }

        return null;
    }


    /**
     * Summarize the related elements of the requested type
     *
     * @param requestedRelationshipTypes relationship types to extract
     * @param relatedMetadataElements elements to summarize
     * @param relatedElementAtEnd1 which end is it connected to
     * @return list or null
     * @throws PropertyServerException problem in converter
     */
    protected List<RelatedMetadataElementSummary> getRelatedElements(List<String>                 requestedRelationshipTypes,
                                                                     List<RelatedMetadataElement> relatedMetadataElements,
                                                                     boolean                      relatedElementAtEnd1) throws PropertyServerException
    {
        if (relatedMetadataElements != null)
        {
            List<RelatedMetadataElementSummary> matchingElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    if (propertyHelper.isTypeOf(relatedMetadataElement, requestedRelationshipTypes))
                    {
                        if (relatedElementAtEnd1 == relatedMetadataElement.getElementAtEnd1())
                        {
                            matchingElements.add(this.getRelatedElementSummary(relatedMetadataElement));
                        }
                    }
                }
            }

            return matchingElements;
        }

        return null;
    }



    /**
     * Summarize the related elements of the requested type
     *
     * @param requestedRelationshipTypes relationship types to extract
     * @param relatedMetadataElements elements to summarize
     * @return list or null
     * @throws PropertyServerException problem in converter
     */
    protected List<RelatedMetadataElementSummary> getRelatedElements(List<String>                 requestedRelationshipTypes,
                                                                     List<RelatedMetadataElement> relatedMetadataElements) throws PropertyServerException
    {
        if (relatedMetadataElements != null)
        {
            List<RelatedMetadataElementSummary> matchingElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    if (propertyHelper.isTypeOf(relatedMetadataElement, requestedRelationshipTypes))
                    {
                        matchingElements.add(this.getRelatedElementSummary(relatedMetadataElement));
                    }
                }
            }

            if (! matchingElements.isEmpty())
            {
                return matchingElements;
            }
        }

        return null;
    }


    /**
     * Summarize the list of collections that the element is a member of (linked via the CollectionMembership) relationship.
     *
     * @param relatedMetadataElements elements to summarize
     * @param processedRelationshipTypes list of relationships that have already been processed
     * @return list or null
     * @throws PropertyServerException problem in converter
     */
    protected List<RelatedMetadataElementSummary> getOtherRelatedElements(List<RelatedMetadataElement> relatedMetadataElements,
                                                                          List<String>                 processedRelationshipTypes) throws PropertyServerException
    {
        if (relatedMetadataElements != null)
        {
            List<RelatedMetadataElementSummary> others = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    if (! propertyHelper.isTypeOf(relatedMetadataElement, processedRelationshipTypes))
                    {
                        others.add(this.getRelatedElementSummary(relatedMetadataElement));
                    }
                }
            }

            if (! others.isEmpty())
            {
                return others;
            }
        }

        return null;
    }



    /**
     * Extract the properties from the element.
     *
     * @param beanClass name of the class to create
     * @param element element containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public ElementStub getElementStub(Class<B>            beanClass,
                                      OpenMetadataElement element,
                                      String              methodName) throws PropertyServerException
    {
        if (element != null)
        {
            ElementHeader elementHeader = propertyHelper.getElementHeader(element);
            ElementStub   elementStub   = new ElementStub(elementHeader);

            elementStub.setUniqueName(propertyHelper.getStringProperty(localServiceName,
                                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                       element.getElementProperties(),
                                                                       methodName));

            return elementStub;
        }
        else
        {
            this.handleMissingMetadataInstance(beanClass.getName(),
                                               OpenMetadataElement.class.getName(),
                                               methodName);
        }

        return null;
    }


    /**
     * Extract the properties from the element.
     *
     * @param beanClass name of the class to create
     * @param element element containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public ElementStub getElementStub(Class<B>                beanClass,
                                      OpenMetadataElementStub element,
                                      String                  methodName) throws PropertyServerException
    {
        if (element != null)
        {
            ElementHeader elementHeader = propertyHelper.getElementHeader(element, element.getGUID(), element.getClassifications());
            ElementStub   elementStub   = new ElementStub(elementHeader);

            elementStub.setUniqueName(element.getUniqueName());

            return elementStub;
        }
        else
        {
            this.handleMissingMetadataInstance(beanClass.getName(),
                                               OpenMetadataElement.class.getName(),
                                               methodName);
        }

        return null;
    }


    /**
     * Extract the properties from the relationship.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public ElementStub getElementStub(Class<B>                 beanClass,
                                      OpenMetadataRelationship relationship,
                                      String                   methodName) throws PropertyServerException
    {
        if (relationship != null)
        {
            ElementHeader elementHeader = propertyHelper.getElementHeader(relationship,
                                                                          relationship.getRelationshipGUID(),
                                                                          null);

            return new ElementStub(elementHeader);
        }
        else
        {
            this.handleMissingMetadataInstance(beanClass.getName(),
                                               OpenMetadataRelationship.class.getName(),
                                               methodName);
        }

        return null;
    }


    /**
     * Extract the classifications from the element.
     *
     * @param attachedClassifications classifications direct from the element
     * @return list of bean classifications
     */
    public List<ElementClassification> getElementClassifications(List<AttachedClassification> attachedClassifications)
    {
        return propertyHelper.getElementClassifications(attachedClassifications);
    }


    /**
     * Return an element classification from an attached classification.  The difference is that the
     * element classification converts the properties to a name-value pair map.
     *
     * @param attachedClassification attached classification from OMF OpenMetadataStore
     * @return element classification for the beans
     */
    protected ElementClassification getElementClassification(AttachedClassification attachedClassification)
    {
        return propertyHelper.getElementClassification(attachedClassification);
    }



    /**
     * Using the supplied instances, return a new instance of a relatedElement bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedBy getRelatedBy(Class<B>                 beanClass,
                                  OpenMetadataRelationship relationship,
                                  String                   methodName) throws PropertyServerException
    {
        if (relationship != null)
        {
            RelatedBy relatedBy = new RelatedBy();

            relatedBy.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, relationship.getRelationshipGUID(), null, methodName));

            RelationshipBeanProperties relationshipProperties = getRelationshipProperties(relationship, relationship.getRelationshipProperties());

            relationshipProperties.setEffectiveFrom(relationship.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(relationship.getEffectiveToTime());

            relatedBy.setRelationshipProperties(relationshipProperties);

            return relatedBy;
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), OpenMetadataRelationship.class.getName(), methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of a relatedElement bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param relatedMetadataElement results containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedBy getRelatedBy(Class<B>               beanClass,
                                  RelatedMetadataElement relatedMetadataElement,
                                  String                 methodName) throws PropertyServerException
    {
        RelatedBy relatedBy = new RelatedBy();

        relatedBy.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relatedMetadataElement, relatedMetadataElement.getRelationshipGUID(), null, methodName));

        if (relatedMetadataElement != null)
        {
            ElementProperties instanceProperties = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

            RelationshipBeanProperties relationshipProperties = new RelationshipBeanProperties();

            relationshipProperties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedBy.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), OpenMetadataRelationship.class.getName(), methodName);
        }

        return relatedBy;
    }
}

