/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataConverterBase provides the generic methods for the bean converters used to provide translation between
 * specific API beans and the Open Metadata services beans from the Governance Action Framework (GAF).
 * Generic classes have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing an Open Metadata API bean.
 */
public class OpenMetadataConverterBase<B>
{
    protected PropertyHelper propertyHelper;
    protected String         localServiceName;
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
        this.propertyHelper   = propertyHelper;
        this.localServiceName = localServiceName;
        this.localServerName  = localServerName;
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
        final String thisMethodName = "getNewComplexBean";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

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
     * Throw an exception to indicate that one of the update methods has not been implemented by a handler.
     *
     * @param beanClassName class name of bean
     * @param expectedBeanClass class name that the converter is able to process
     * @param methodName method that is missing
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    protected void handleUnexpectedBeanClass(String    beanClassName,
                                             String    expectedBeanClass,
                                             String    methodName) throws PropertyServerException
    {
        throw new PropertyServerException(OMFErrorCode.UNEXPECTED_BEAN_CLASS.getMessageDefinition(beanClassName,
                                                                                                  methodName,
                                                                                                  localServiceName,
                                                                                                  localServerName,
                                                                                                  expectedBeanClass),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Throw an exception to indicate that a critical instance (typically the main element) has not been passed
     * to the converter.
     *
     * @param beanClassName class name of bean
     * @param elementClassName class name that the converter is able to process
     * @param methodName method that is missing
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    protected void handleMissingMetadataInstance(String          beanClassName,
                                                 String          elementClassName,
                                                 String          methodName) throws PropertyServerException
    {
        throw new PropertyServerException(OMFErrorCode.MISSING_METADATA_INSTANCE.getMessageDefinition(localServiceName,
                                                                                                      beanClassName,
                                                                                                      elementClassName,
                                                                                                      methodName),
                                          this.getClass().getName(),
                                          methodName);
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
            if (element.getElementProperties() != null)
            {
                elementSummary.setProperties(element.getElementProperties().getPropertiesAsStrings());
            }

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
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public RelatedMetadataElementSummary getRelatedElementSummary(RelatedMetadataElement relatedElement,
                                                                  String                 methodName) throws PropertyServerException
    {
        if ((relatedElement != null) && (relatedElement.getElement() != null))
        {
            return propertyHelper.getRelatedElementSummary(relatedElement, methodName);
        }

        return null;
    }



    /**
     * Summarize the related external references.
     *
     * @param relatedMetadataElements elements to summarize
     * @return list or null
     * @throws PropertyServerException problem in converter
     */
    protected List<RelatedMetadataElementSummary> getExternalReferences(List<RelatedMetadataElement> relatedMetadataElements) throws PropertyServerException
    {
        return getRelatedElements(OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName, relatedMetadataElements);
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
        final String methodName = "getRelatedElements";

        if (relatedMetadataElements != null)
        {
            List<RelatedMetadataElementSummary> matchingElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if ((relatedMetadataElement != null) && (propertyHelper.isTypeOf(relatedMetadataElement, requestedRelationshipType)))
                {
                    matchingElements.add(this.getRelatedElementSummary(relatedMetadataElement, methodName));
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
        final String methodName = "getRelatedElements";

        if (relatedMetadataElements != null)
        {
            List<RelatedMetadataElementSummary> matchingElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if ((relatedMetadataElement != null) && (propertyHelper.isTypeOf(relatedMetadataElement, requestedRelationshipType)))
                {
                    if (relatedElementAtEnd1 == relatedMetadataElement.getElementAtEnd1())
                    {
                        matchingElements.add(this.getRelatedElementSummary(relatedMetadataElement, methodName));
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
        final String methodName = "getRelatedElements";

        if (relatedMetadataElements != null)
        {
            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if ((relatedMetadataElement != null) && (propertyHelper.isTypeOf(relatedMetadataElement, requestedRelationshipType)))
                {
                    if (relatedElementAtEnd1 == relatedMetadataElement.getElementAtEnd1())
                    {
                        return this.getRelatedElementSummary(relatedMetadataElement, methodName);
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
        final String methodName = "getRelatedElements";

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
                            matchingElements.add(this.getRelatedElementSummary(relatedMetadataElement, methodName));
                        }
                    }
                }
            }

            return matchingElements;
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
        final String methodName = "getOtherRelatedElements";

        if (relatedMetadataElements != null)
        {
            List<RelatedMetadataElementSummary> others = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    if (! propertyHelper.isTypeOf(relatedMetadataElement, processedRelationshipTypes))
                    {
                        others.add(this.getRelatedElementSummary(relatedMetadataElement, methodName));
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
     * @param element entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedElementStub getRelatedElementStub(Class<B>                 beanClass,
                                                    OpenMetadataElement      element,
                                                    OpenMetadataRelationship relationship,
                                                    String                   methodName) throws PropertyServerException
    {
        RelatedElementStub relatedElementStub = new RelatedElementStub();

        relatedElementStub.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, relationship.getRelationshipGUID(), null, methodName));

        if (relationship != null)
        {
            ElementProperties instanceProperties = new ElementProperties(relationship.getRelationshipProperties());

            RelationshipBeanProperties relationshipProperties = new RelationshipBeanProperties();

            relationshipProperties.setEffectiveFrom(relationship.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(relationship.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedElementStub.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), OpenMetadataRelationship.class.getName(), methodName);
        }

        if (element != null)
        {
            ElementStub elementStub = this.getElementStub(beanClass, element, methodName);

            relatedElementStub.setRelatedElement(elementStub);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
        }

        return relatedElementStub;
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
    public RelatedBy getRelatedElementStub(Class<B>               beanClass,
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
        RelatedBy relatedBy = new RelatedBy();

        relatedBy.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, relationship.getRelationshipGUID(), null, methodName));

        if (relationship != null)
        {
            ElementProperties instanceProperties = new ElementProperties(relationship.getRelationshipProperties());

            RelationshipBeanProperties relationshipProperties = new RelationshipBeanProperties();

            relationshipProperties.setEffectiveFrom(relationship.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(relationship.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedBy.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), OpenMetadataRelationship.class.getName(), methodName);
        }

        return relatedBy;
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


    /**
     * Extract and delete the ActivityStatus property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return KeyPattern enum
     */
    protected ActivityStatus removeActivityStatus(ElementProperties elementProperties)
    {
        final String methodName = "removeActivityStatus";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (ActivityStatus status : ActivityStatus.values())
            {
                if (status.getName().equals(retrievedProperty))
                {
                    return status;
                }
            }
        }

        return null;
    }


    /**
     * Extract the qualifiedName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String getQualifiedName(ElementProperties  elementProperties)
    {
        final String methodName = "getQualifiedName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }



    /**
     * Extract and delete the qualifiedName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeQualifiedName(ElementProperties  elementProperties)
    {
        final String methodName = "removeQualifiedName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the qualifiedName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map or null
     */
    protected Map<String, String> removeAdditionalProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeAdditionalProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Convert the remaining properties into a map that is returned as the extended properties.
     *
     * @param elementProperties properties from element
     * @return map or null
     */
    public Map<String, Object> getRemainingExtendedProperties(ElementProperties  elementProperties)
    {
        if (elementProperties != null)
        {
            return propertyHelper.getElementPropertiesAsMap(elementProperties);
        }

        return null;
    }


    /**
     * Extract and delete the displayName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeDisplayName(ElementProperties  elementProperties)
    {
        final String methodName = "removeDisplayName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DISPLAY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the displayName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String getDisplayName(ElementProperties  elementProperties)
    {
        final String methodName = "getDisplayName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.DISPLAY_NAME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the resource name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeResourceName(ElementProperties  elementProperties)
    {
        final String methodName = "removeResourceName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RESOURCE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the version identifier property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeVersionIdentifier(ElementProperties  elementProperties)
    {
        final String methodName = "removeVersionIdentifier";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the description property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getDescription(ElementProperties  elementProperties)
    {
        final String methodName = "getDescription";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.DESCRIPTION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the description property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    public String removeDescription(ElementProperties  elementProperties)
    {
        final String methodName = "removeDescription";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DESCRIPTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the keyword property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeKeyword(ElementProperties  elementProperties)
    {
        final String methodName = "removeKeyword";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.KEYWORD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the topicType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTopicType(ElementProperties  elementProperties)
    {
        final String methodName = "removeTopicType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TOPIC_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the topicName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTopicName(ElementProperties  elementProperties)
    {
        final String methodName = "removeTopicName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TOPIC_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the operatingSystem property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOperatingSystem(ElementProperties  elementProperties)
    {
        final String methodName = "removeOperatingSystem";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OPERATING_SYSTEM.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the operatingSystemPatchLevel property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOperatingSystemPatchLevel(ElementProperties  elementProperties)
    {
        final String methodName = "removeOperatingSystemPatchLevel";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OPERATING_SYSTEM_PATCH_LEVEL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the minimumInstances property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return int
     */
    protected int removeMinimumInstances(ElementProperties  elementProperties)
    {
        final String methodName = "removeMinimumInstances";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.MINIMUM_INSTANCES.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }

    /**
     * Extract and delete the maximumInstances property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return int
     */
    protected int removeMaximumInstances(ElementProperties  elementProperties)
    {
        final String methodName = "removeMaximumInstances";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.MAXIMUM_INSTANCES.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the initials property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeInitials(ElementProperties  elementProperties)
    {
        final String methodName = "removeInitials";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INITIALS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the courtesyTitle property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCourtesyTitle(ElementProperties  elementProperties)
    {
        final String methodName = "removeCourtesyTitle";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.COURTESY_TITLE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the givenNames property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeGivenNames(ElementProperties  elementProperties)
    {
        final String methodName = "removeGivenNames";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.GIVEN_NAMES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the surname property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeSurname(ElementProperties  elementProperties)
    {
        final String methodName = "removeSurname";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SURNAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the fullName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeFullName(ElementProperties  elementProperties)
    {
        final String methodName = "removeFullName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.FULL_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the preferredLanguage property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePreferredLanguage(ElementProperties  elementProperties)
    {
        final String methodName = "removePreferredLanguage";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PREFERRED_LANGUAGE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the residentCountry property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeResidentCountry(ElementProperties  elementProperties)
    {
        final String methodName = "removeResidentCountry";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RESIDENT_COUNTRY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the jobTitle property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeJobTitle(ElementProperties  elementProperties)
    {
        final String methodName = "removeJobTitle";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.JOB_TITLE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the employeeNumber property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEmployeeNumber(ElementProperties  elementProperties)
    {
        final String methodName = "removeEmployeeNumber";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EMPLOYEE_NUMBER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the employeeType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEmployeeType(ElementProperties  elementProperties)
    {
        final String methodName = "removeEmployeeType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EMPLOYEE_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the contactType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeContactType(ElementProperties  elementProperties)
    {
        final String methodName = "removeContactType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONTACT_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the contactMethodService property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeContactMethodService(ElementProperties  elementProperties)
    {
        final String methodName = "removeContactMethodService";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONTACT_METHOD_SERVICE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the contactMethodValue property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeContactMethodValue(ElementProperties  elementProperties)
    {
        final String methodName = "removeContactMethodValue";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONTACT_METHOD_VALUE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the mission property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMission(ElementProperties  elementProperties)
    {
        final String methodName = "removeMission";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MISSION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }




    /**
     * Extract and delete the associationType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeAssociationType(ElementProperties  elementProperties)
    {
        final String methodName = "removeAssociationType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ASSOCIATION_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the identifier property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeIdentifier(ElementProperties  elementProperties)
    {
        final String methodName = "removeIdentifier";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.IDENTIFIER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the background property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeBackground(ElementProperties  elementProperties)
    {
        final String methodName = "removeBackground";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.BACKGROUND.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the incidentClassifiers map property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map
     */
    protected Map<String, Integer> removeIncidentClassifiers(ElementProperties  elementProperties)
    {
        final String methodName = "removeIncidentClassifiers";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntegerMapFromProperty(localServiceName,
                                                               OpenMetadataProperty.INCIDENT_CLASSIFIERS.name,
                                                               elementProperties,
                                                               methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceCreatedBy property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeExternalInstanceCreatedBy(ElementProperties  elementProperties)
    {
        final String methodName = "removeExternalInstanceCreatedBy";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXT_INSTANCE_CREATED_BY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceCreationTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeExternalInstanceCreationTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeExternalInstanceCreationTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.EXT_INSTANCE_CREATION_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceLastUpdatedBy property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeExternalInstanceLastUpdatedBy(ElementProperties  elementProperties)
    {
        final String methodName = "removeExternalInstanceLastUpdatedBy";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATED_BY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceLastUpdateTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeExternalInstanceLastUpdateTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeExternalInstanceCreationTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.EXT_INSTANCE_LAST_UPDATE_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the externalInstanceVersion property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected long removeExternalInstanceVersion(ElementProperties  elementProperties)
    {
        final String methodName = "removeExternalInstanceVersion";

        if (elementProperties != null)
        {
            return propertyHelper.removeLongProperty(localServiceName,
                                                     OpenMetadataProperty.EXT_INSTANCE_VERSION.name,
                                                     elementProperties,
                                                     methodName);
        }

        return 0L;
    }


    /**
     * Extract and delete the threshold property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return int
     */
    protected int removeThreshold(ElementProperties  elementProperties)
    {
        final String methodName = "removeThreshold";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.THRESHOLD.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the matchThreshold property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return int
     */
    protected int removeMatchThreshold(ElementProperties  elementProperties)
    {
        final String methodName = "removeMatchThreshold";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.MATCH_THRESHOLD.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 100;
    }



    /**
     * Extract and delete the numberOfPages property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return int
     */
    protected int removeNumberOfPages(ElementProperties  elementProperties)
    {
        final String methodName = "removeNumberOfPages";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.NUMBER_OF_PAGES.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the URL property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeURL(ElementProperties  elementProperties)
    {
        final String methodName = "removeURL";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.URL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePageRange(ElementProperties  elementProperties)
    {
        final String methodName = "removePageRange";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PAGE_RANGE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePublicationSeries(ElementProperties  elementProperties)
    {
        final String methodName = "removePublicationSeries";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PUBLICATION_SERIES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePublicationSeriesVolume(ElementProperties  elementProperties)
    {
        final String methodName = "removePublicationSeriesVolume";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PUBLICATION_SERIES_VOLUME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePublisher(ElementProperties  elementProperties)
    {
        final String methodName = "removePublisher";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PUBLISHER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEdition(ElementProperties  elementProperties)
    {
        final String methodName = "removeEdition";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EDITION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeFirstPublicationDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeFirstPublicationDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.FIRST_PUB_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removePublicationDate(ElementProperties  elementProperties)
    {
        final String methodName = "removePublicationDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.PUBLICATION_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePublicationCity(ElementProperties  elementProperties)
    {
        final String methodName = "removePublicationCity";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PUBLICATION_CITY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePublicationYear(ElementProperties  elementProperties)
    {
        final String methodName = "removePublicationYear";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PUBLICATION_YEAR.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected List<String> removePublicationNumbers(ElementProperties  elementProperties)
    {
        final String methodName = "removePublicationNumbers";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.PUBLICATION_NUMBERS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the organization property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOrganization(ElementProperties  elementProperties)
    {
        final String methodName = "removeOrganization";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ORGANIZATION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the referenceId property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeReferenceId(ElementProperties  elementProperties)
    {
        final String methodName = "removeReferenceId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REFERENCE_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the referenceTitle property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeReferenceTitle(ElementProperties  elementProperties)
    {
        final String methodName = "removeReferenceTitle";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REFERENCE_TITLE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the license property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLicense(ElementProperties  elementProperties)
    {
        final String methodName = "removeLicense";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LICENSE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mediaTypeOtherId property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMediaTypeOtherId(ElementProperties  elementProperties)
    {
        final String methodName = "removeMediaTypeOtherId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MEDIA_TYPE_OTHER_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }




    /**
     * Extract and delete the defaultMediaUsageOtherId property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDefaultMediaUsageOtherId(ElementProperties  elementProperties)
    {
        final String methodName = "removeDefaultMediaUsageOtherId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DEFAULT_MEDIA_USAGE_OTHER_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the copyright property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCopyright(ElementProperties  elementProperties)
    {
        final String methodName = "removeCopyright";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.COPYRIGHT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the attribution property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeAttribution(ElementProperties  elementProperties)
    {
        final String methodName = "removeAttribution";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ATTRIBUTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the authors property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected List<String> removeAuthors(ElementProperties  elementProperties)
    {
        final String methodName = "removeAuthors";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.AUTHORS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the sources property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Map<String, String> removeSources(ElementProperties  elementProperties)
    {
        final String methodName = "removeSources";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.SOURCES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }



    /**
     * Extract and delete the referenceAbstract property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeReferenceAbstract(ElementProperties  elementProperties)
    {
        final String methodName = "removeReferenceAbstract";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REFERENCE_ABSTRACT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the referenceId property from the supplied element properties.
     *
     * @param elementProperties properties from relationship
     * @return string text or null
     */
    protected String getReferenceId(ElementProperties  elementProperties)
    {
        final String methodName = "getReferenceId";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.REFERENCE_ID.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the orderPropertyName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOrderPropertyName(ElementProperties  elementProperties)
    {
        final String methodName = "removeOrderPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ORDER_BY_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the membershipRationale property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMembershipRationale(ElementProperties  elementProperties)
    {
        final String methodName = "removeMembershipRationale";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MEMBERSHIP_RATIONALE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the teamType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTeamType(ElementProperties  elementProperties)
    {
        final String methodName = "removeTeamType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TEAM_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mappingProperties property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map or null
     */
    protected Map<String, String> removeMappingProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeMappingProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.MAPPING_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }



    /**
     * Extract and delete the lastSynchronized property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map or null
     */
    protected Date removeLastSynchronized(ElementProperties  elementProperties)
    {
        final String methodName = "removeLastSynchronized";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.LAST_SYNCHRONIZED.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }



    /**
     * Extract and delete the networkAddress property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeNetworkAddress(ElementProperties  elementProperties)
    {
        final String methodName = "removeNetworkAddress";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the postalAddress property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePostalAddress(ElementProperties  elementProperties)
    {
        final String methodName = "removePostalAddress";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.POSTAL_ADDRESS.name,
                                                       elementProperties,
                                                       methodName);

        }

        return null;
    }


    /**
     * Extract and delete the "coordinates" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCoordinates(ElementProperties  elementProperties)
    {
        final String methodName = "removeCoordinates";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.COORDINATES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mapProjection property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMapProjection(ElementProperties  elementProperties)
    {
        final String methodName = "removeMapProjection";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MAP_PROJECTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the timeZone property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTimeZone(ElementProperties  elementProperties)
    {
        final String methodName = "removeTimeZone";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TIME_ZONE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the level property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLevel(ElementProperties  elementProperties)
    {
        final String methodName = "removeLevel";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LEVEL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the protocol property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeProtocol(ElementProperties  elementProperties)
    {
        final String methodName = "removeProtocol";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROTOCOL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the encryption method property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEncryptionMethod(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncryptionMethod";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ENCRYPTION_METHOD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the connector provider class name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeConnectorProviderClassName(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorProviderClassName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONNECTOR_PROVIDER_CLASS_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the supported asset type name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeSupportedAssetTypeName(ElementProperties  elementProperties)
    {
        final String methodName = "removeSupportedAssetTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SUPPORTED_ASSET_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the supported deployed implementation type name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeSupportedDeployedImplementationType(ElementProperties  elementProperties)
    {
        final String methodName = "removeSupportedDeployedImplementationType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SUPPORTED_DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the expected data format property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeExpectedDataFormat(ElementProperties  elementProperties)
    {
        final String methodName = "removeExpectedDataFormat";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXPECTED_DATA_FORMAT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the connector id property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeConnectorId(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONNECTOR_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the connector name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeConnectorName(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CONNECTOR_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the server name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeServerName(ElementProperties  elementProperties)
    {
        final String methodName = "removeServerName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SERVER_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the connector start date property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeConnectorStartDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorStartDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.CONNECTOR_START_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }



    /**
     * Extract and delete the refresh start date property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeRefreshStartDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeRefreshStartDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REFRESH_START_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }



    /**
     * Extract and delete the refresh completion data  property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeRefreshCompletionDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeRefreshCompletionDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REFRESH_COMPLETION_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }



    /**
     * Extract and delete the connector disconnect data property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeConnectorDisconnectDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorDisconnectDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.CONNECTOR_DISCONNECT_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }



    /**
     * Extract and delete the connector framework name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeConnectorFrameworkName(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorFrameworkName";

        if (elementProperties != null)
        {
            String connectorFrameworkName = propertyHelper.removeStringProperty(localServiceName,
                                                                                OpenMetadataProperty.CONNECTOR_FRAMEWORK_NAME.name,
                                                                                elementProperties,
                                                                                methodName);
            if (connectorFrameworkName != null)
            {
                return connectorFrameworkName;
            }
        }

        return OpenMetadataValidValues.CONNECTOR_FRAMEWORK_NAME_DEFAULT;
    }


    /**
     * Extract and delete the connector interface language property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeConnectorInterfaceLanguage(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorInterfaceLanguage";

        if (elementProperties != null)
        {
            String connectorInterfaceLanguage = propertyHelper.removeStringProperty(localServiceName,
                                                                                    OpenMetadataProperty.CONNECTOR_INTERFACE_LANGUAGE.name,
                                                                                    elementProperties,
                                                                                    methodName);
            if (connectorInterfaceLanguage != null)
            {
                return connectorInterfaceLanguage;
            }
        }

        return OpenMetadataValidValues.CONNECTOR_INTERFACE_LANGUAGE_DEFAULT;
    }


    /**
     * Extract and delete the connector interfaces property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeConnectorInterfaces(ElementProperties  elementProperties)
    {
        final String methodName = "removeConnectorInterfaces";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.CONNECTOR_INTERFACES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract and delete the created elements property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeCreatedElements(ElementProperties  elementProperties)
    {
        final String methodName = "removeCreatedElements";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.CREATED_ELEMENTS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the updated elements property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeUpdatedElements(ElementProperties  elementProperties)
    {
        final String methodName = "removeUpdatedElements";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.UPDATED_ELEMENTS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the updated elements property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeDeletedElements(ElementProperties  elementProperties)
    {
        final String methodName = "removeDeletedElements";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.DELETED_ELEMENTS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }

    /**
     * Extract and delete the target technology source property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTargetTechnologySource(ElementProperties  elementProperties)
    {
        final String methodName = "removeTargetTechnologySource";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TARGET_TECHNOLOGY_SOURCE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the target technology name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTargetTechnologyName(ElementProperties  elementProperties)
    {
        final String methodName = "removeTargetTechnologyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TARGET_TECHNOLOGY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the target technology interfaces property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeTargetTechnologyInterfaces(ElementProperties  elementProperties)
    {
        final String methodName = "removeTargetTechnologyInterfaces";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.TARGET_TECHNOLOGY_INTERFACES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the target technology versions property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeTargetTechnologyVersions(ElementProperties  elementProperties)
    {
        final String methodName = "removeTargetTechnologyVersions";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.TARGET_TECHNOLOGY_VERSIONS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the recognizedAdditionalProperties property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeRecognizedAdditionalProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeRecognizedAdditionalProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.RECOGNIZED_ADDITIONAL_PROPERTIES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the recognizedSecuredProperties property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeRecognizedSecuredProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeRecognizedSecuredProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.RECOGNIZED_SECURED_PROPERTIES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract and delete the recognized configuration properties property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeRecognizedConfigurationProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeRecognizedConfigurationProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.RECOGNIZED_CONFIGURATION_PROPERTIES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the securedProperties property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected Map<String, String> removeSecuredProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeSecuredProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.SECURED_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the  configuration properties property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected Map<String, Object> removeConfigurationProperties(ElementProperties  elementProperties)
    {
        final String methodName = "removeConfigurationProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeMapFromProperty(localServiceName,
                                                        OpenMetadataProperty.CONFIGURATION_PROPERTIES.name,
                                                        elementProperties,
                                                        methodName);
        }

        return null;
    }


    /**
     * Extract and delete the userId property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeUserId(ElementProperties  elementProperties)
    {
        final String methodName = "removeUserId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.USER_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the user property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeUser(ElementProperties  elementProperties)
    {
        final String methodName = "removeUser";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.USER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the user property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePurpose(ElementProperties  elementProperties)
    {
        final String methodName = "removePurpose";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PURPOSE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the clear password property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeClearPassword(ElementProperties  elementProperties)
    {
        final String methodName = "removeClearPassword";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CLEAR_PASSWORD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the encrypted password property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEncryptedPassword(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncryptedPassword";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ENCRYPTED_PASSWORD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the assetSummary property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getAssetSummary(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncryptedPassword";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ASSET_SUMMARY.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the "arguments" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Map<String, Object> getArguments(ElementProperties  elementProperties)
    {
        final String methodName = "getArguments";

        if (elementProperties != null)
        {
            return propertyHelper.getMapFromProperty(localServiceName,
                                                     OpenMetadataProperty.ARGUMENTS.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Retrieve the zone membership from the properties of the zone membership classification.
     *
     * @param elementProperties properties from the classification
     * @return list of zone names
     */
    protected List<String> removeZoneMembership(ElementProperties elementProperties)
    {
        final String methodName = "removeZoneMembership";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Retrieve the subjectAreaName from the properties.
     *
     * @param elementProperties properties from the element
     * @return subject area name
     */
    protected String removeSubjectAreaName(ElementProperties elementProperties)
    {
        final String methodName = "removeSubjectAreaName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SUBJECT_AREA_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Retrieve the zone membership from the properties of the zone membership classification.
     *
     * @param elementProperties properties from the classification
     * @return list of zone names
     */
    protected List<String> getZoneMembership(ElementProperties elementProperties)
    {
        final String methodName = "getZoneMembership";

        if (elementProperties != null)
        {
            return propertyHelper.getStringArrayProperty(localServiceName,
                                                         OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                         elementProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract and delete the owner property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOwner(ElementProperties elementProperties)
    {
        final String methodName = "removeOwner";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OWNER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the owner property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String getOwner(ElementProperties elementProperties)
    {
        final String methodName = "getOwner";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.OWNER.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the ownerPropertyName property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string
     */
    protected String removeOwnerPropertyName(ElementProperties elementProperties)

    {
        final String methodName = "removeClassificationPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OWNER_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the ownerTypeName property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string
     */
    protected String removeOwnerTypeName(ElementProperties elementProperties)

    {
        final String methodName = "removeTypePropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OWNER_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the roleTypeName property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string
     */
    protected String removeRoleTypeName(ElementProperties elementProperties)

    {
        final String methodName = "removeRoleTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ROLE_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the distinguishedName property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string
     */
    protected String removeDistinguishedName(ElementProperties elementProperties)

    {
        final String methodName = "removeDistinguishedName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DISTINGUISHED_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the "groups" property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string map or null
     */
    protected List<String> getGroups(ElementProperties  elementProperties)
    {
        final String methodName = "getGroups";

        if (elementProperties != null)
        {
            return propertyHelper.getStringArrayProperty(localServiceName,
                                                         OpenMetadataProperty.GROUPS.name,
                                                         elementProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the securityLabels property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string map or null
     */
    protected List<String> getSecurityLabels(ElementProperties  elementProperties)
    {
        final String methodName = "getSecurityLabels";

        if (elementProperties != null)
        {
            return propertyHelper.getStringArrayProperty(localServiceName,
                                                         OpenMetadataProperty.SECURITY_LABELS.name,
                                                         elementProperties,
                                                         methodName);
        }

        return null;
    }


    /**
     * Extract the securityProperties property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string map or null
     */
    protected Map<String, String> getSecurityProperties(ElementProperties  elementProperties)
    {
        final String methodName = "getSecurityProperties";

        if (elementProperties != null)
        {
            return propertyHelper.getStringMapFromProperty(localServiceName,
                                                           OpenMetadataProperty.SECURITY_PROPERTIES.name,
                                                           elementProperties,
                                                           methodName);
        }

        return null;
    }


    /**
     * Extract the karmaPoints property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return ordinal or 0 for not specified
     */
    protected int removeKarmaPoints(ElementProperties elementProperties)
    {
        final String methodName = "removeKarmaPoints";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.KARMA_POINTS.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the organizationGUID property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String getOriginOrganizationGUID(ElementProperties  elementProperties)
    {
        final String methodName = "getOriginOrganizationGUID";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ORGANIZATION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the businessCapabilityGUID property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String getOriginBusinessCapabilityGUID(ElementProperties  elementProperties)
    {
        final String methodName = "getOriginBusinessCapabilityGUID";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.BUSINESS_CAPABILITY.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the otherOriginValues property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string map or null
     */
    protected Map<String, String> getOtherOriginValues(ElementProperties  elementProperties)
    {
        final String methodName = "getOtherOriginValues";

        if (elementProperties != null)
        {
            return propertyHelper.getStringMapFromProperty(localServiceName,
                                                           OpenMetadataProperty.OTHER_ORIGIN_VALUES.name,
                                                           elementProperties,
                                                           methodName);
        }

        return null;
    }



    /**
     * Extract and delete the resourceCreateTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date or null
     */
    protected Date removeResourceCreateTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeResourceCreateTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.RESOURCE_CREATE_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the resourceUpdateTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date or null
     */
    protected Date removeResourceUpdateTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeResourceUpdateTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.RESOURCE_UPDATE_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the resourceLastAccessedTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date or null
     */
    protected Date removeResourceLastAccessedTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeResourceLastAccessedTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.RESOURCE_LAST_ACCESSED_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the pathName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string or null
     */
    protected String removePathName(ElementProperties  elementProperties)
    {
        final String methodName = "removePathName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.PATH_NAME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the sourceCreateTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date or null
     */
    protected Date removeStoreCreateTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeStoreCreateTime";

        if (elementProperties != null)
        {
            Date createTime1 = propertyHelper.removeDateProperty(localServiceName,
                                                                 OpenMetadataProperty.STORE_CREATE_TIME.name,
                                                                 elementProperties,
                                                                 methodName);
            Date createTime2 = propertyHelper.removeDateProperty(localServiceName,
                                                                 OpenMetadataProperty.CREATE_TIME.name,
                                                                 elementProperties,
                                                                 methodName);
            return createTime1 == null ? createTime2 : createTime1;
        }

        return null;
    }


    /**
     * Extract and delete the storeUpdateTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date or null
     */
    protected Date removeStoreUpdateTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeStoreUpdateTime";

        if (elementProperties != null)
        {
            Date modifiedTime1 = propertyHelper.removeDateProperty(localServiceName,
                                                                   OpenMetadataProperty.STORE_UPDATE_TIME.name,
                                                                   elementProperties,
                                                                   methodName);
            Date modifiedTime2 = propertyHelper.removeDateProperty(localServiceName,
                                                                   OpenMetadataProperty.UPDATE_TIME.name,
                                                                   elementProperties,
                                                                   methodName);
            return modifiedTime1 == null ? modifiedTime2 : modifiedTime1;
        }

        return null;
    }



    /**
     * Extract the encoding property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String getDataStoreEncoding(ElementProperties  elementProperties)
    {
        final String methodName = "getDataStoreEncoding";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ENCODING_TYPE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the encoding language property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String getDataStoreEncodingLanguage(ElementProperties  elementProperties)
    {
        final String methodName = "getDataStoreEncodingLanguage";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ENCODING_LANGUAGE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the encoding description property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String getDataStoreEncodingDescription(ElementProperties  elementProperties)
    {
        final String methodName = "getDataStoreEncodingDescription";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ENCODING_DESCRIPTION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the encoding properties property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string map or null
     */
    protected Map<String, String> getEncodingProperties(ElementProperties  elementProperties)
    {
        final String methodName = "getEncodingProperties";

        if (elementProperties != null)
        {
            return propertyHelper.getStringMapFromProperty(localServiceName,
                                                           OpenMetadataProperty.ENCODING_PROPERTIES.name,
                                                           elementProperties,
                                                           methodName);
        }

        return null;
    }



    /**
     * Extract and delete the database instance property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeDatabaseInstance(ElementProperties  elementProperties)
    {
        final String methodName = "removeDatabaseInstance";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INSTANCE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the database importedFrom property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeDatabaseImportedFrom(ElementProperties  elementProperties)
    {
        final String methodName = "removeDatabaseImportedFrom";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.IMPORTED_FROM.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the fileType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeFileType(ElementProperties  elementProperties)
    {
        final String methodName = "removeFileType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.FILE_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the format property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getFormat(ElementProperties  elementProperties)
    {
        final String methodName = "getFormat";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.FORMAT.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the encryption property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getEncryption(ElementProperties  elementProperties)
    {
        final String methodName = "getEncryption";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ENCRYPTION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the type property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDeployedImplementationType(ElementProperties  elementProperties)
    {
        final String methodName = "removeDeployedImplementationType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the user defined status property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeUserDefinedStatus(ElementProperties  elementProperties)
    {
        final String methodName = "removeUserDefinedStatus";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the user defined activity status property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeUserDefinedActivityStatus(ElementProperties  elementProperties)
    {
        final String methodName = "removeUserDefinedActivityStatus";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.USER_DEFINED_ACTIVITY_STATUS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the product name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeProductName(ElementProperties  elementProperties)
    {
        final String methodName = "removeProductName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PRODUCT_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the maturity property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeMaturity(ElementProperties  elementProperties)
    {
        final String methodName = "removeMaturity";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MATURITY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the service life property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeServiceLife(ElementProperties  elementProperties)
    {
        final String methodName = "removeServiceLife";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SERVICE_LIFE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the current version property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCurrentVersion(ElementProperties  elementProperties)
    {
        final String methodName = "removeCurrentVersion";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CURRENT_VERSION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the introduction date property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeIntroductionDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeIntroductionDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.INTRODUCTION_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the next version date property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeNextVersionDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeNextVersionDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.NEXT_VERSION_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the withdraw date property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeWithdrawDate(ElementProperties  elementProperties)
    {
        final String methodName = "removeWithdrawDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.WITHDRAW_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the support levels property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeSupportLevel(ElementProperties  elementProperties)
    {
        final String methodName = "removeSupportLevel";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SUPPORT_LEVEL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the service levels property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Map<String, String> removeServiceLevels(ElementProperties  elementProperties)
    {
        final String methodName = "removeServiceLevels";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.SERVICE_LEVELS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }



    /**
     * Extract and delete the patchLevel property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePatchLevel(ElementProperties  elementProperties)
    {
        final String methodName = "removePatchLevel";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PATCH_LEVEL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Retrieve the isDefaultValue flag from the properties from the supplied element properties.
     *
     * @param elementProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeIsDefaultValue(ElementProperties  elementProperties)
    {
        final String methodName = "removeIsDefaultValue";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.IS_DEFAULT_VALUE.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract and delete the anchorGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeAnchorGUID(ElementProperties  elementProperties)
    {
        final String methodName = "removeAnchorGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ANCHOR_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the anchorGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String getAnchorGUID(ElementProperties  elementProperties)
    {
        final String methodName = "getAnchorGUID";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ANCHOR_GUID.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the data type property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeDataType(ElementProperties  elementProperties)
    {
        final String methodName = "removeDataType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DATA_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the units property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeUnits(ElementProperties  elementProperties)
    {
        final String methodName = "removeDataType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.UNITS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the defaultValue property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDefaultValue(ElementProperties  elementProperties)
    {
        final String methodName = "removeDefaultValue";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DEFAULT_VALUE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the defaultValue property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeFixedValue(ElementProperties  elementProperties)
    {
        final String methodName = "removeFixedValue";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.FIXED_VALUE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the query property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getQuery(ElementProperties  elementProperties)
    {
        final String methodName = "setQuery";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.QUERY.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the queryId property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getQueryId(ElementProperties  elementProperties)
    {
        final String methodName = "setQueryId";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.QUERY_ID.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }



    /**
     * Extract and delete the createdTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeCreatedTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeCreatedTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.CREATED_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the createdTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected Date removeLastModifiedTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeLastModifiedTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.LAST_MODIFIED_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the lastModifier property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLastModifier(ElementProperties  elementProperties)
    {
        final String methodName = "removeId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LAST_MODIFIER.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the author property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeAuthor(ElementProperties  elementProperties)
    {
        final String methodName = "removeAuthor";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.AUTHOR.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the encoding standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEncodingStandard(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncodingStandard";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ENCODING_STANDARD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the namespace property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeNamespace(ElementProperties  elementProperties)
    {
        final String methodName = "removeNamespace";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.NAMESPACE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the specification property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeSpecification(ElementProperties  elementProperties)
    {
        final String methodName = "removeSpecification";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SPECIFICATION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the position property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default 0
     */
    protected int removePosition(ElementProperties  elementProperties)
    {
        final String methodName = "removePosition";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.POSITION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the position property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default 0
     */
    protected int getPosition(ElementProperties  elementProperties)
    {
        final String methodName = "getPosition";

        if (elementProperties != null)
        {
            return propertyHelper.getIntProperty(localServiceName,
                                                 OpenMetadataProperty.POSITION.name,
                                                 elementProperties,
                                                 methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the minCardinality property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default 0
     */
    protected int removeMinCardinality(ElementProperties  elementProperties)
    {
        final String methodName = "removeMinCardinality";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.MIN_CARDINALITY.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the maxCardinality property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default -1 which is unlimited
     */
    protected int removeMaxCardinality(ElementProperties  elementProperties)
    {
        final String methodName = "removeMaxCardinality";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.MAX_CARDINALITY.name,
                                                    elementProperties,
                                                    methodName);
        }

        return -1;
    }


    /**
     * Retrieve the allowsDuplicateValues flag from the properties of the zone membership classification.
     *
     * @param elementProperties properties from the classification
     * @return boolean - default is true
     */
    protected boolean removeAllowsDuplicateValues(ElementProperties  elementProperties)
    {
        final String methodName = "removeAllowsDuplicateValues";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.ALLOWS_DUPLICATE_VALUES.name,
                                                        elementProperties,
                                                        methodName);
        }

        return true;
    }


    /**
     * Retrieve the orderedValues flag from the properties of the zone membership classification.
     *
     * @param elementProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeOrderedValues(ElementProperties  elementProperties)
    {
        final String methodName = "removeOrderedValues";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.ORDERED_VALUES.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract and delete the defaultValueOverride property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDefaultValueOverride(ElementProperties  elementProperties)
    {
        final String methodName = "removeDefaultValueOverride";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DEFAULT_VALUE_OVERRIDE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the minimumLength property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default 0
     */
    protected int removeMinimumLength(ElementProperties  elementProperties)
    {
        final String methodName = "removeMinimumLength";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.MINIMUM_LENGTH.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the length property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default 0
     */
    protected int removeLength(ElementProperties  elementProperties)
    {
        final String methodName = "removeLength";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.LENGTH.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the precision/significantDigits property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer - default 0
     */
    protected int removePrecision(ElementProperties  elementProperties)
    {
        final String methodName = "removePrecision";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.PRECISION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Retrieve the isNullable flag from the properties from the supplied element properties.
     *
     * @param elementProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeIsNullable(ElementProperties  elementProperties)
    {
        final String methodName = "removeIsNullable";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.IS_NULLABLE.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Retrieve the required flag from the properties from the supplied element properties.
     *
     * @param elementProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeRequired(ElementProperties  elementProperties)
    {
        final String methodName = "removeRequired";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.REQUIRED.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract and delete the native class property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeNativeClass(ElementProperties  elementProperties)
    {
        final String methodName = "removeNativeClass";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.NATIVE_CLASS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "aliases" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected List<String> removeAliases(ElementProperties  elementProperties)
    {
        final String methodName = "removeAliases";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.ALIASES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract and delete the "minutes" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected List<String> removeMinutes(ElementProperties  elementProperties)
    {
        final String methodName = "removeMinutes";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.MINUTES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "decisions" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected List<String> removeDecisions(ElementProperties  elementProperties)
    {
        final String methodName = "removeDecisions";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.DECISIONS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract the guard property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getGuard(ElementProperties  elementProperties)
    {
        final String methodName = "getGuard";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.GUARD.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the formula property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getFormula(ElementProperties  elementProperties)
    {
        final String methodName = "getFormula";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.FORMULA.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the formulaType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getFormulaType(ElementProperties  elementProperties)
    {
        final String methodName = "getFormulaType";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.FORMULA_TYPE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the formula property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeFormula(ElementProperties  elementProperties)
    {
        final String methodName = "removeFormula";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.FORMULA.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the formulaType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeFormulaType(ElementProperties  elementProperties)
    {
        final String methodName = "removeFormulaType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.FORMULA_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the implementationLanguage property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getImplementationLanguage(ElementProperties  elementProperties)
    {
        final String methodName = "getImplementationLanguage";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.IMPLEMENTATION_LANGUAGE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and remove the implementationLanguage property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeImplementationLanguage(ElementProperties  elementProperties)
    {
        final String methodName = "removeImplementationLanguage";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.IMPLEMENTATION_LANGUAGE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and remove the objective property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeObjective(ElementProperties  elementProperties)
    {
        final String methodName = "removeObjective";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.OBJECTIVE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and remove the usesBlockingCalls property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected boolean removeUsesBlockingCalls(ElementProperties  elementProperties)
    {
        final String methodName = "removeUsesBlockingCalls";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.USES_BLOCKING_CALLS.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract and delete the type property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeSource(ElementProperties  elementProperties)
    {
        final String methodName = "removeSource";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SOURCE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the usage property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getUsage(ElementProperties elementProperties)
    {
        final String methodName = "getUsage";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.USAGE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the usage property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeUsage(ElementProperties elementProperties)
    {
        final String methodName = "removeUsage";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.USAGE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the language property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeLanguage(ElementProperties elementProperties)
    {
        final String methodName = "removeLanguage";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LANGUAGE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }




    /**
     * Extract the summary property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String getSummary(ElementProperties elementProperties)
    {
        final String methodName = "getSummary";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.SUMMARY.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and remove the summary property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String removeSummary(ElementProperties elementProperties)
    {
        final String methodName = "removeSummary";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SUMMARY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the abbreviation property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String getAbbreviation(ElementProperties elementProperties)
    {
        final String methodName = "getAbbreviation";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ABBREVIATION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and remove the abbreviation property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String removeAbbreviation(ElementProperties elementProperties)
    {
        final String methodName = "removeAbbreviation";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ABBREVIATION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and remove the "examples" property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String removeExamples(ElementProperties elementProperties)
    {
        final String methodName = "removeExamples";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXAMPLES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the "pronouns" property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String removePronouns(ElementProperties elementProperties)
    {
        final String methodName = "removePronouns";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PRONOUNS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the priority property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String removeImportance(ElementProperties elementProperties)
    {
        final String methodName = "removeImportance";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.IMPORTANCE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the priority integer property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected int removeIntPriority(ElementProperties elementProperties)
    {
        final String methodName = "removeIntPriority";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.PRIORITY.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the headcount integer property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected int removeHeadCount(ElementProperties elementProperties)
    {
        final String methodName = "removeHeadCount";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.HEAD_COUNT.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the scope property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeScope(ElementProperties elementProperties)
    {
        final String methodName = "removeScope";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SCOPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "implications" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeImplications(ElementProperties elementProperties)
    {
        final String methodName = "removeImplications";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.IMPLICATIONS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "outcomes" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeOutcomes(ElementProperties elementProperties)
    {
        final String methodName = "removeOutcomes";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.OUTCOMES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "results" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeResults(ElementProperties elementProperties)
    {
        final String methodName = "removeResults";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.RESULTS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the businessImperatives property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string list or null
     */
    protected List<String> removeBusinessImperatives(ElementProperties elementProperties)
    {
        final String methodName = "removeBusinessImperatives";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.BUSINESS_IMPERATIVES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the regulationSource property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeRegulationSource(ElementProperties elementProperties)
    {
        final String methodName = "removeRegulationSource";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REGULATION_SOURCE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the regulators property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected List<String> removeRegulators(ElementProperties elementProperties)
    {
        final String methodName = "removeRegulators";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.REGULATORS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the entitlements property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected Map<String, String> removeEntitlements(ElementProperties elementProperties)

    {
        final String methodName = "removeEntitlements";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.ENTITLEMENTS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }




    /**
     * Extract and delete the restrictions property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected Map<String, String> removeRestrictions(ElementProperties elementProperties)

    {
        final String methodName = "removeRestrictions";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.RESTRICTIONS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the obligations property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected Map<String, String> removeObligations(ElementProperties elementProperties)

    {
        final String methodName = "removeObligations";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.OBLIGATIONS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the rational property from the supplied element properties.
     *
     * @param elementProperties properties from relationship
     * @return string text or null
     */
    protected String getRationale(ElementProperties elementProperties)
    {
        final String methodName = "removeRationale";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.RATIONALE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the implementationDescription property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeImplementationDescription(ElementProperties elementProperties)
    {
        final String methodName = "removeImplementationDescription";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.IMPLEMENTATION_DESCRIPTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the criteria property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCriteria(ElementProperties elementProperties)
    {
        final String methodName = "removeCriteria";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CRITERIA.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the domain identifier property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer = default is 0 which is ALL
     */
    protected int removeDomainIdentifier(ElementProperties elementProperties)

    {
        final String methodName = "removeDomainIdentifier";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.DOMAIN_IDENTIFIER.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the level identifier property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return integer = default is 0 which is ALL
     */
    protected int removeLevelIdentifier(ElementProperties elementProperties)

    {
        final String methodName = "removeLevelIdentifier";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.LEVEL_IDENTIFIER.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the measurement property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeMeasurement(ElementProperties elementProperties)

    {
        final String methodName = "removeMeasurement";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.MEASUREMENT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the target property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeTarget(ElementProperties elementProperties)

    {
        final String methodName = "removeTarget";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TARGET.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the classificationName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeClassificationName(ElementProperties elementProperties)

    {
        final String methodName = "removeClassificationName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CLASSIFICATION_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the classificationPropertyName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeClassificationPropertyName(ElementProperties elementProperties)

    {
        final String methodName = "removeClassificationPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CLASSIFICATION_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the processingEngineUserId property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeProcessingEngineUserId(ElementProperties elementProperties)

    {
        final String methodName = "removeProcessingEngineUserId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REQUESTER_USER_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requesterUserId property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeRequesterUserId(ElementProperties elementProperties)

    {
        final String methodName = "removeRequesterUserId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REQUESTER_USER_ID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requestType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeRequestType(ElementProperties elementProperties)
    {
        final String methodName = "removeRequestType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REQUEST_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the serviceRequestType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeServiceRequestType(ElementProperties elementProperties)
    {
        final String methodName = "removeServiceRequestType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SERVICE_REQUEST_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requestParameters property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected Map<String, String> removeRequestParameters(ElementProperties elementProperties)

    {
        final String methodName = "removeRequestParameters";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }




    /**
     * Extract and delete the executorEngineGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeExecutorEngineGUID(ElementProperties elementProperties)

    {
        final String methodName = "removeExecutorEngineGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXECUTOR_ENGINE_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the engineActionGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeEngineActionGUID(ElementProperties elementProperties)

    {
        final String methodName = "removeEngineActionGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ENGINE_ACTION_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the assetGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeAssetGUID(ElementProperties elementProperties)

    {
        final String methodName = "removeAssetGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ASSET_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the executorEngineName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeExecutorEngineName(ElementProperties elementProperties)

    {
        final String methodName = "removeExecutorEngineName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXECUTOR_ENGINE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the processName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeProcessName(ElementProperties elementProperties)

    {
        final String methodName = "removeProcessName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROCESS_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the processStepName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeProcessStepName(ElementProperties elementProperties)

    {
        final String methodName = "removeProcessStepName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROCESS_STEP_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the processStepGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeProcessStepGUID(ElementProperties elementProperties)

    {
        final String methodName = "removeProcessStepGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROCESS_STEP_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the governanceActionTypeGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeGovernanceActionTypeGUID(ElementProperties elementProperties)

    {
        final String methodName = "removeGovernanceActionTypeGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the governanceActionTypeName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeGovernanceActionTypeName(ElementProperties elementProperties)

    {
        final String methodName = "removeGovernanceActionTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.GOVERNANCE_ACTION_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }




    /**
     * Extract and delete the guard property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeGuard(ElementProperties elementProperties)

    {
        final String methodName = "removeGuard";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.GUARD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mandatoryGuard property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return flag
     */
    protected boolean removeMandatoryGuard(ElementProperties elementProperties)

    {
        final String methodName = "removeMandatoryGuard";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.MANDATORY_GUARD.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract and delete the ignoreMultipleTriggers property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return flag
     */
    protected boolean removeIgnoreMultipleTriggers(ElementProperties elementProperties)

    {
        final String methodName = "removeIgnoreMultipleTriggers";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.IGNORE_MULTIPLE_TRIGGERS.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract and delete the waitTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return flag
     */
    protected int removeWaitTime(ElementProperties elementProperties)

    {
        final String methodName = "removeWaitTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.WAIT_TIME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the receivedGuards property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return list of guards
     */
    protected List<String> removeReceivedGuards(ElementProperties elementProperties)

    {
        final String methodName = "removeReceivedGuards";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.RECEIVED_GUARDS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mandatoryGuards property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return list of guards
     */
    protected List<String> removeMandatoryGuards(ElementProperties elementProperties)

    {
        final String methodName = "removeMandatoryGuards";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.MANDATORY_GUARDS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the completionGuards property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return list of guards
     */
    protected List<String> removeCompletionGuards(ElementProperties elementProperties)

    {
        final String methodName = "removeCompletionGuards";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.COMPLETION_GUARDS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the completionMessage property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string
     */
    protected String removeCompletionMessage(ElementProperties elementProperties)

    {
        final String methodName = "removeCompletionMessage";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.COMPLETION_MESSAGE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the processStartTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeStartTime(ElementProperties elementProperties)

    {
        final String methodName = "removeProcessStartTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.START_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requested property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeRequestedTime(ElementProperties elementProperties)

    {
        final String methodName = "removeRequestedTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REQUESTED_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }



    /**
     * Extract and delete the requested property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeRequestedStartTime(ElementProperties elementProperties)

    {
        final String methodName = "removeRequestedStartTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REQUESTED_START_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the startDate property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeStartDate(ElementProperties elementProperties)

    {
        final String methodName = "removeStartDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.START_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the plannedEndDate property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removePlannedEndDate(ElementProperties elementProperties)

    {
        final String methodName = "removePlannedEndDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.PLANNED_END_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the creationTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeCreationTime(ElementProperties elementProperties)

    {
        final String methodName = "removeCreationTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REQUESTED_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the lastReviewTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeLastReviewTime(ElementProperties elementProperties)

    {
        final String methodName = "removeLastReviewTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.LAST_REVIEW_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }




    /**
     * Extract and delete the lastPauseTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeLastPauseTime(ElementProperties elementProperties)

    {
        final String methodName = "removeLastPauseTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.LAST_PAUSE_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the lastResumeTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeLastResumeTime(ElementProperties elementProperties)

    {
        final String methodName = "removeLastResumeTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.LAST_RESUME_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the dueTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeDueTime(ElementProperties elementProperties)

    {
        final String methodName = "removeDueTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.DUE_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the completionTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeCompletionTime(ElementProperties elementProperties)

    {
        final String methodName = "removeCompletionTime";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.COMPLETION_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the completionDate property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date
     */
    protected Date removeCompletionDate(ElementProperties elementProperties)

    {
        final String methodName = "removeCompletionDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.COMPLETION_TIME.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract and delete the status property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String removeProjectStatus(ElementProperties elementProperties)

    {
        final String methodName = "removeProjectStatus";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROJECT_STATUS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the health property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String removeProjectHealth(ElementProperties elementProperties)
    {
        final String methodName = "removeProjectHealth";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROJECT_HEALTH.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the phase property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String removeProjectPhase(ElementProperties elementProperties)
    {
        final String methodName = "removeProjectPhase";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PROJECT_PHASE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the actionTargetName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String removeActionTargetName(ElementProperties elementProperties)

    {
        final String methodName = "removeActionTargetName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the licenseGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String getLicenseGUID(ElementProperties elementProperties)

    {
        final String methodName = "getLicenseGUID";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.LICENSE_GUID.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the certificationGUID property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String getCertificationGUID(ElementProperties elementProperties)

    {
        final String methodName = "getCertificationGUID";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.CERTIFICATE_GUID.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the start property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return Date/timestamp or null
     */
    protected Date getStart(ElementProperties elementProperties)

    {
        final String methodName = "getStart";

        if (elementProperties != null)
        {
            return propertyHelper.getDateProperty(localServiceName,
                                                  OpenMetadataProperty.COVERAGE_START.name,
                                                  elementProperties,
                                                  methodName);
        }

        return null;
    }


    /**
     * Extract the end property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return Date/timestamp or null
     */
    protected Date getEnd(ElementProperties elementProperties)

    {
        final String methodName = "getEnd";

        if (elementProperties != null)
        {
            return propertyHelper.getDateProperty(localServiceName,
                                                  OpenMetadataProperty.COVERAGE_END.name,
                                                  elementProperties,
                                                  methodName);
        }

        return null;
    }


    /**
     * Extract the "conditions" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getConditions(ElementProperties elementProperties)
    {
        final String methodName = "getConditions";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.CONDITIONS.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the custodian property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getCustodian(ElementProperties elementProperties)
    {
        final String methodName = "getCustodian";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.CUSTODIAN.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the certifiedBy property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getCertifiedBy(ElementProperties elementProperties)
    {
        final String methodName = "getCertifiedBy";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.CERTIFIED_BY.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the recipient property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getRecipient(ElementProperties elementProperties)
    {
        final String methodName = "getRecipient";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.RECIPIENT.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the licensedBy property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getLicensedBy(ElementProperties elementProperties)
    {
        final String methodName = "getLicensedBy";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.LICENSED_BY.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the licensee property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String getLicensee(ElementProperties elementProperties)
    {
        final String methodName = "getLicensee";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.LICENSEE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the description property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removePreferredValue(ElementProperties elementProperties)
    {
        final String methodName = "removePreferredValue";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PREFERRED_VALUE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the category property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCategory(ElementProperties elementProperties)
    {
        final String methodName = "removeCategory";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.CATEGORY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the isCaseSensitive property from the supplied element properties.
     *
     * @param elementProperties properties from ValidValueDefinition entity
     * @return boolean
     */
    protected boolean removeIsCaseSensitive(ElementProperties  elementProperties)
    {
        final String methodName = "getStrictRequirement";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(localServiceName,
                                                        OpenMetadataProperty.IS_CASE_SENSITIVE.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
    }


    /**
     * Extract the strictRequirement property from the supplied element properties.
     *
     * @param elementProperties properties from ValidValuesAssignment relationship
     * @return boolean
     */
    protected boolean getStrictRequirement(ElementProperties  elementProperties)
    {
        final String methodName = "getStrictRequirement";

        if (elementProperties != null)
        {
            return propertyHelper.getBooleanProperty(localServiceName,
                                                     OpenMetadataProperty.STRICT_REQUIREMENT.name,
                                                     elementProperties,
                                                     methodName);
        }

        return false;
    }


    /**
     * Extract the confidence property from the supplied element properties.
     *
     * @param elementProperties properties from ReferenceValueAssignment or ValidValuesMapping relationship
     * @return int
     */
    protected int getConfidence(ElementProperties  elementProperties)
    {
        final String methodName = "getConfidence";

        if (elementProperties != null)
        {
            return propertyHelper.getIntProperty(localServiceName,
                                                 OpenMetadataProperty.CONFIDENCE.name,
                                                 elementProperties,
                                                 methodName);
        }

        return 0;
    }


    /**
     * Extract the steward property from the supplied element properties.
     *
     * @param elementProperties properties from ReferenceValueAssignment or ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getSteward(ElementProperties  elementProperties)
    {
        final String methodName = "getSteward";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.STEWARD.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }



    /**
     * Extract the stewardTypeName property from the supplied element properties.
     *
     * @param elementProperties properties from ReferenceValueAssignment or ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getStewardTypeName(ElementProperties  elementProperties)
    {
        final String methodName = "getStewardTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the stewardTypeName property from the supplied element properties.
     *
     * @param elementProperties properties from ReferenceValueAssignment or ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getStewardPropertyName(ElementProperties  elementProperties)
    {
        final String methodName = "getStewardPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the "notes" property from the supplied element properties.
     *
     * @param elementProperties properties from GovernanceRuleImplementation, GovernanceProcessImplementation,
     *                           ReferenceValueAssignment or ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getNotes(ElementProperties  elementProperties)
    {
        final String methodName = "getNotes";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.NOTES.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the "attributeName" property from the supplied element properties.
     *
     * @param elementProperties properties from ReferenceValueAssignment relationship
     * @return string text or null
     */
    protected String getAttributeName(ElementProperties  elementProperties)
    {
        final String methodName = "getAttributeName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ATTRIBUTE_NAME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the pointType property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String getPointType(ElementProperties  elementProperties)
    {
        final String methodName = "getPointType";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.POINT_TYPE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the associationDescription property from the supplied element properties.
     *
     * @param elementProperties properties from ValidValuesMapping relationship
     * @return string text or null
     */
    protected String getAssociationDescription(ElementProperties  elementProperties)
    {
        final String methodName = "getAssociationDescription";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.ASSOCIATION_DESCRIPTION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the symbolicName property from the supplied element properties.
     *
     * @param elementProperties properties from ValidValuesImplementation relationship
     * @return string text or null
     */
    protected String getSymbolicName(ElementProperties  elementProperties)
    {
        final String methodName = "getSymbolicName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.SYMBOLIC_NAME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the implementationValue property from the supplied element properties.
     *
     * @param elementProperties properties from ValidValuesImplementation relationship
     * @return string text or null
     */
    protected String getImplementationValue(ElementProperties  elementProperties)
    {
        final String methodName = "getImplementationValue";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(localServiceName,
                                                    OpenMetadataProperty.IMPLEMENTATION_VALUE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the additionalValues property from the supplied element properties.
     *
     * @param elementProperties properties from ValidValuesImplementation relationship
     * @return map of name-value pairs
     */
    protected Map<String, String> getAdditionalValues(ElementProperties  elementProperties)
    {
        final String methodName = "getAdditionalValues";

        if (elementProperties != null)
        {
            return propertyHelper.getStringMapFromProperty(localServiceName,
                                                           OpenMetadataProperty.ADDITIONAL_VALUES.name,
                                                           elementProperties,
                                                           methodName);
        }

        return null;
    }


    /**
     * Extract the review property from the supplied element properties.
     *
     * @param elementProperties properties from review/rating entities
     * @return string property or null
     */
    protected String removeReview(ElementProperties elementProperties)
    {
        final String methodName = "removeReview";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.REVIEW.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the emoji property from the supplied element properties.
     *
     * @param elementProperties properties from like entities
     * @return string property or null
     */
    protected String removeEmoji(ElementProperties elementProperties)
    {
        final String methodName = "removeEmoji";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EMOJI.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the tagName property from the supplied element properties.
     *
     * @param elementProperties properties from informal tag entities
     * @return string property or null
     */
    protected String removeTagName(ElementProperties elementProperties)
    {
        final String methodName = "removeTagName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DISPLAY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the tagDescription property from the supplied element properties.
     *
     * @param elementProperties properties from informal tag entities
     * @return string property or null
     */
    protected String removeTagDescription(ElementProperties elementProperties)
    {
        final String methodName = "removeTagDescription";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DESCRIPTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the analysis parameters property from the supplied element properties.
     *
     * @param elementProperties properties from discovery analysis report entities
     * @return string property or null
     */
    protected Map<String, String> removeAnalysisParameters(ElementProperties elementProperties)
    {
        final String methodName = "removeAnalysisParameters";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.ANALYSIS_PARAMETERS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the analysis step property from the supplied element properties.
     *
     * @param elementProperties properties from entities
     * @return string property or null
     */
    protected String removeAnalysisStep(ElementProperties elementProperties)
    {
        final String methodName = "removeAnalysisStep";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ANALYSIS_STEP.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the annotation type property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeAnnotationType(ElementProperties elementProperties)
    {
        final String methodName = "removeAnnotationType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the confidence level property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return integer or 0
     */
    protected int removeConfidenceLevel(ElementProperties elementProperties)
    {
        final String methodName = "removeConfidenceLevel";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.CONFIDENCE_LEVEL.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the confidence property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return integer or 0
     */
    protected int removeConfidence(ElementProperties elementProperties)
    {
        final String methodName = "removeConfidence";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.CONFIDENCE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the expression property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeExpression(ElementProperties elementProperties)
    {
        final String methodName = "removeExpression";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXPRESSION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the attributeName property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeAttributeName(ElementProperties elementProperties)
    {
        final String methodName = "removeAttributeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ATTRIBUTE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the explanation property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeExplanation(ElementProperties elementProperties)
    {
        final String methodName = "removeExplanation";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.EXPLANATION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the jsonProperties property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeJsonProperties(ElementProperties elementProperties)
    {
        final String methodName = "removeJsonProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.JSON_PROPERTIES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the reviewDate property from the supplied element properties.
     *
     * @param elementProperties properties from annotation review entities
     * @return date or null
     */
    protected Date removeReviewDate(ElementProperties elementProperties)
    {
        final String methodName = "removeReviewDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.REVIEW_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract the steward property from the supplied element properties.
     *
     * @param elementProperties properties from annotation review entities
     * @return string property or null
     */
    protected String removeSteward(ElementProperties elementProperties)
    {
        final String methodName = "removeSteward";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.STEWARD.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the stewardTypeName property from the supplied element properties.
     *
     * @param elementProperties properties from annotation review entities
     * @return string property or null
     */
    protected String removeStewardTypeName(ElementProperties elementProperties)
    {
        final String methodName = "removeStewardTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the stewardPropertyName property from the supplied element properties.
     *
     * @param elementProperties properties from annotation review entities
     * @return string property or null
     */
    protected String removeStewardPropertyName(ElementProperties elementProperties)
    {
        final String methodName = "removeStewardPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the notes property from the supplied element properties.
     *
     * @param elementProperties properties from annotation review entities
     * @return string property or null
     */
    protected String removeNotes(ElementProperties elementProperties)
    {
        final String methodName = "removeNotes";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.NOTES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract the comment property from the supplied element properties.
     *
     * @param elementProperties properties from annotation review entities
     * @return string property or null
     */
    protected String removeComment(ElementProperties elementProperties)
    {
        final String methodName = "removeComment";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.COMMENT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the schemaName property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeSchemaName(ElementProperties elementProperties)
    {
        final String methodName = "removeSchemaName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SCHEMA_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the schemaType property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeSchemaType(ElementProperties elementProperties)
    {
        final String methodName = "removeSchemaType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SCHEMA_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the candidateClassifications property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name value pairs
     */
    protected Map<String, String> removeCandidateClassifications(ElementProperties elementProperties)
    {
        final String methodName = "removeCandidateClassifications";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.CANDIDATE_CLASSIFICATIONS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the candidateDataClassGUIDs property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of string guids
     */
    protected List<String> removeCandidateDataClassGUIDs(ElementProperties elementProperties)
    {
        final String methodName = "removeCandidateDataClassGUIDs";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.CANDIDATE_DATA_CLASS_GUIDS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the inferredDataType property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeInferredDataType(ElementProperties elementProperties)
    {
        final String methodName = "removeInferredDataType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INFERRED_DATA_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the inferredFormat property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeInferredFormat(ElementProperties elementProperties)
    {
        final String methodName = "removeInferredFormat";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INFERRED_FORMAT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the inferredLength property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return int property or 0
     */
    protected int removeInferredLength(ElementProperties elementProperties)
    {
        final String methodName = "removeInferredLength";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.INFERRED_LENGTH.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the inferredPrecision property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return int property or 0
     */
    protected int removeInferredPrecision(ElementProperties elementProperties)
    {
        final String methodName = "removeInferredPrecision";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.INFERRED_PRECISION.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the inferredScale property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return int property or 0
     */
    protected int removeInferredScale(ElementProperties elementProperties)
    {
        final String methodName = "removeInferredScale";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.INFERRED_SCALE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract the profilePropertyNames property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of names
     */
    protected List<String> removeProfilePropertyNames(ElementProperties elementProperties)
    {
        final String methodName = "removeProfilePropertyNames";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.PROFILE_PROPERTY_NAMES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the matchPropertyNames property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of names
     */
    protected List<String> removeMatchPropertyNames(ElementProperties elementProperties)
    {
        final String methodName = "removeMatchPropertyNames";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.MATCH_PROPERTY_NAMES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract the sampleValues property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of names
     */
    protected List<String> removeSampleValues(ElementProperties elementProperties)
    {
        final String methodName = "removeSampleValues";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.SAMPLE_VALUES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract the dataPatterns property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of names
     */
    protected List<String> removeDataPatterns(ElementProperties elementProperties)
    {
        final String methodName = "removeDataPatterns";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.DATA_PATTERNS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract the namePatterns property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of names
     */
    protected List<String> removeNamePatterns(ElementProperties elementProperties)
    {
        final String methodName = "removeNamePatterns";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.NAME_PATTERNS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the profileStartDate property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return date
     */
    protected Date removeProfileStartDate(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileStartDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.PROFILE_START_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }


    /**
     * Extract the profileEndDate property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return date
     */
    protected Date removeProfileEndDate(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileEndDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(localServiceName,
                                                     OpenMetadataProperty.PROFILE_END_DATE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return null;
    }




    /**
     * Extract the specificationDetails property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name value pairs
     */
    protected Map<String, String> removeSpecificationDetails(ElementProperties elementProperties)
    {
        final String methodName = "removeSpecificationDetails";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.SPECIFICATION_DETAILS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the profileProperties property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name value pairs
     */
    protected Map<String, String> removeProfileProperties(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.PROFILE_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the profileFlags property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name to boolean pairs
     */
    protected Map<String, Boolean> removeProfileFlags(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileFlags";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanMapFromProperty(localServiceName,
                                                               OpenMetadataProperty.PROFILE_FLAGS.name,
                                                               elementProperties,
                                                               methodName);
        }

        return null;
    }


    /**
     * Extract the profileDates property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name to boolean pairs
     */
    protected Map<String, Date> removeProfileDates(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileDates";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateMapFromProperty(localServiceName,
                                                            OpenMetadataProperty.PROFILE_DATES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the profileCounts property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name to long pairs
     */
    protected Map<String, Long> removeProfileCounts(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileCounts";

        if (elementProperties != null)
        {
            return propertyHelper.removeLongMapFromProperty(localServiceName,
                                                            OpenMetadataProperty.PROFILE_COUNTS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the profileCounts property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name to long pairs
     */
    protected Map<String, Double> removeProfileDoubles(ElementProperties elementProperties)
    {
        final String methodName = "removeProfileDoubles";

        if (elementProperties != null)
        {
            return propertyHelper.removeDoubleMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.PROFILE_DOUBLES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the valueList property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of values
     */
    protected List<String> removeValueList(ElementProperties elementProperties)
    {
        final String methodName = "removeValueList";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.VALUE_LIST.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the valueCount property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name to integer pairs
     */
    protected Map<String, Integer> removeValueCount(ElementProperties elementProperties)
    {
        final String methodName = "removeValueCount";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntegerMapFromProperty(localServiceName,
                                                               OpenMetadataProperty.VALUE_COUNT.name,
                                                               elementProperties,
                                                               methodName);
        }

        return null;
    }


    /**
     * Extract and delete the valueRangeFrom property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeValueRangeFrom(ElementProperties elementProperties)
    {
        final String methodName = "removeValueRangeFrom";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.VALUE_RANGE_FROM.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the valueRangeTo property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeValueRangeTo(ElementProperties elementProperties)
    {
        final String methodName = "removeValueRangeTo";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.VALUE_RANGE_TO.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the averageValue property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return string property or null
     */
    protected String removeAverageValue(ElementProperties elementProperties)
    {
        final String methodName = "removeAverageValue";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.AVERAGE_VALUE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the resourceProperties property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name value pairs
     */
    protected Map<String, String> removeResourceProperties(ElementProperties elementProperties)
    {
        final String methodName = "removeResourceProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.RESOURCE_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the size property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return int property or 0
     */
    protected long removeSize(ElementProperties elementProperties)
    {
        final String methodName = "removeSize";

        if (elementProperties != null)
        {
            return propertyHelper.removeLongProperty(localServiceName,
                                                     OpenMetadataProperty.SIZE.name,
                                                     elementProperties,
                                                     methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the encoding property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeEncoding(ElementProperties  elementProperties)
    {
        final String methodName = "removeEncoding";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ENCODING_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the parameterType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeParameterType(ElementProperties  elementProperties)
    {
        final String methodName = "removeParameterType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PARAMETER_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the qualityDimension standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeQualityDimension(ElementProperties  elementProperties)
    {
        final String methodName = "removeQualityDimension";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.QUALITY_DIMENSION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the qualityScore property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return int property or 0
     */
    protected int removeQualityScore(ElementProperties elementProperties)
    {
        final String methodName = "removeQualityScore";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(localServiceName,
                                                    OpenMetadataProperty.QUALITY_SCORE.name,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the attachmentGUID standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeAttachmentGUID(ElementProperties  elementProperties)
    {
        final String methodName = "removeAttachmentGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ATTACHMENT_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the relatedEntityGUID standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeRelatedEntityGUID(ElementProperties  elementProperties)
    {
        final String methodName = "removeRelatedEntityGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RELATED_ENTITY_GUID.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the relationshipTypeName standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeRelationshipTypeName(ElementProperties  elementProperties)
    {
        final String methodName = "removeRelationshipTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.RELATIONSHIP_TYPE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the relationshipProperties property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected Map<String, String> removeRelationshipProperties(ElementProperties elementProperties)
    {
        final String methodName = "removeRelationshipProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.RELATIONSHIP_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the actionSourceName standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeActionSourceName(ElementProperties  elementProperties)
    {
        final String methodName = "removeActionSourceName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ACTION_SOURCE_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the actionRequested standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeActionRequested(ElementProperties  elementProperties)
    {
        final String methodName = "removeActionRequested";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ACTION_REQUESTED.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the actionProperties property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected Map<String, String> removeActionProperties(ElementProperties elementProperties)
    {
        final String methodName = "removeActionProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.ACTION_PROPERTIES.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the informalTerm standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeInformalTerm(ElementProperties  elementProperties)
    {
        final String methodName = "removeInformalTerm";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INFORMAL_TERM.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the candidateGlossaryTermGUIDs property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of values
     */
    protected List<String> removeCandidateGlossaryTermGUIDs(ElementProperties elementProperties)
    {
        final String methodName = "removeCandidateGlossaryTermGUIDs";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.CANDIDATE_GLOSSARY_TERM_GUIDS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the informalTopic standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeInformalTopic(ElementProperties  elementProperties)
    {
        final String methodName = "removeInformalTopic";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INFORMAL_CATEGORY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the candidateGlossaryCategoryGUIDs property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of values
     */
    protected List<String> removeCandidateGlossaryCategoryGUIDs(ElementProperties elementProperties)
    {
        final String methodName = "removeCandidateGlossaryCategoryGUIDs";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.CANDIDATE_GLOSSARY_CATEGORY_GUIDS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the solutionComponentType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeSolutionComponentType(ElementProperties  elementProperties)
    {
        final String methodName = "removeSolutionComponentType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.SOLUTION_COMPONENT_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the plannedDeployedImplementationType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removePlannedDeployedImplementationType(ElementProperties  elementProperties)
    {
        final String methodName = "removePlannedDeployedImplementationType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.PLANNED_DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the integrationStyle property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeIntegrationStyle(ElementProperties  elementProperties)
    {
        final String methodName = "removeIntegrationStyle";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.INTEGRATION_STYLE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the estimatedVolumetrics property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected Map<String, String> removeEstimatedVolumetrics(ElementProperties elementProperties)
    {
        final String methodName = "removeEstimatedVolumetrics";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(localServiceName,
                                                              OpenMetadataProperty.ESTIMATED_VOLUMETRICS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the role property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removeRole(ElementProperties  elementProperties)
    {
        final String methodName = "removeRole";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.ROLE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the designStep property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removeDesignStep(ElementProperties  elementProperties)
    {
        final String methodName = "removeDesignStep";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.DESIGN_STEP.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the transformation property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removeTransformation(ElementProperties  elementProperties)
    {
        final String methodName = "removeTransformation";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.TRANSFORMATION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the label property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public String removeLabel(ElementProperties  elementProperties)
    {
        final String methodName = "removeLabel";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(localServiceName,
                                                       OpenMetadataProperty.LABEL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the purposes property from the supplied element properties.
     *
     * @param elementProperties properties from entities
     * @return list of name-value pairs
     */
    protected List<String> removePurposes(ElementProperties elementProperties)
    {
        final String methodName = "removePurposes";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.PURPOSES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }

    /**
     * Extract and delete the iscQualifiedNames property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    public List<String> removeISCQualifiedNames(ElementProperties  elementProperties)
    {
        final String methodName = "removeISCQualifiedNames";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(localServiceName,
                                                            OpenMetadataProperty.ISC_QUALIFIED_NAMES.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract (and remove) the solution direction enum from element properties.
     *
     * @param elementProperties properties
     * @return enum
     */
    protected SolutionPortDirection removeSolutionPortDirection(ElementProperties elementProperties)
    {
        final String methodName = "removeSolutionPortDirection";

        if (elementProperties != null)
        {
            String enumValue = propertyHelper.removeEnumProperty(localServiceName,
                                                                 OpenMetadataProperty.DIRECTION.name,
                                                                 elementProperties,
                                                                 methodName);

            for (SolutionPortDirection portDirection : SolutionPortDirection.values())
            {
                if (portDirection.getName().equals(enumValue))
                {
                    return portDirection;
                }
            }

            return SolutionPortDirection.UNKNOWN;
        }

        return null;
    }


    /**
     * Extract (and remove) the sort order enum from element properties.
     *
     * @param elementProperties properties
     * @return enum
     */
    protected DataItemSortOrder removeDataItemSortOrder(ElementProperties elementProperties)
    {
        final String methodName = "removeDataItemSortOrder";

        if (elementProperties != null)
        {
            String enumValue = propertyHelper.removeEnumProperty(localServiceName,
                                                                 OpenMetadataProperty.SORT_ORDER.name,
                                                                 elementProperties,
                                                                 methodName);

            for (DataItemSortOrder portDirection : DataItemSortOrder.values())
            {
                if (portDirection.getName().equals(enumValue))
                {
                    return portDirection;
                }
            }

            return DataItemSortOrder.UNSORTED;
        }

        return null;
    }


    /**
     * Retrieve the member collection member properties from the retrieved relationship.
     *
     * @param relatedMetadataElement element
     *
     * @return dataStructure properties
     */
    public CollectionMembershipProperties getCollectionMembershipProperties(RelatedMetadataElement relatedMetadataElement)
    {
        if (relatedMetadataElement.getRelationshipProperties() != null)
        {
            CollectionMembershipProperties membershipProperties = new CollectionMembershipProperties();

            ElementProperties elementProperties = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

            membershipProperties.setMembershipRationale(this.removeMembershipRationale(elementProperties));
            membershipProperties.setCreatedBy(relatedMetadataElement.getVersions().getCreatedBy());
            membershipProperties.setExpression(this.removeExpression(elementProperties));
            membershipProperties.setConfidence(this.removeConfidence(elementProperties));
            membershipProperties.setSteward(this.removeSteward(elementProperties));
            membershipProperties.setStewardTypeName(this.removeStewardTypeName(elementProperties));
            membershipProperties.setStewardPropertyName(this.removeStewardPropertyName(elementProperties));
            membershipProperties.setSource(this.removeSource(elementProperties));
            membershipProperties.setNotes(this.removeNotes(elementProperties));
            membershipProperties.setStatus(this.removeCollectionMemberStatus(elementProperties));
            membershipProperties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
            membershipProperties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            membershipProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

            return membershipProperties;
        }

        return null;
    }


    /**
     * Extract and delete the CollectionMemberStatus property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return KeyPattern enum
     */
    CollectionMemberStatus removeCollectionMemberStatus(ElementProperties elementProperties)
    {
        final String methodName = "removeCollectionMemberStatus";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.MEMBERSHIP_STATUS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (CollectionMemberStatus status : CollectionMemberStatus.values())
            {
                if (status.getName().equals(retrievedProperty))
                {
                    return status;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the stars property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    StarRating removeStarRating(ElementProperties elementProperties)
    {
        final String methodName = "removeStarRating";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.STARS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (StarRating starRating : StarRating.values())
            {
                if (starRating.getName().equals(retrievedProperty))
                {
                    return starRating;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the CommentType property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    CommentType removeCommentType(ElementProperties elementProperties)
    {
        final String methodName = "removeCommentType";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.COMMENT_TYPE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (CommentType commentType : CommentType.values())
            {
                if (commentType.getName().equals(retrievedProperty))
                {
                    return commentType;
                }
            }
        }

        return null;
    }



    /**
     * Extract and delete the mediaType property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    MediaType removeMediaType(ElementProperties elementProperties)
    {
        final String methodName = "removeMediaType";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.MEDIA_TYPE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (MediaType enumValue : MediaType.values())
            {
                if (enumValue.getName().equals(retrievedProperty))
                {
                    return enumValue;
                }
            }
        }

        return null;
    }



    /**
     * Extract and delete the defaultMediaUsage property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    MediaUsage removeDefaultMediaUsage(ElementProperties elementProperties)
    {
        final String methodName = "removeDefaultMediaUsage";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.DEFAULT_MEDIA_USAGE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (MediaUsage enumValue : MediaUsage.values())
            {
                if (enumValue.getName().equals(retrievedProperty))
                {
                    return enumValue;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the ContactMethodType property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    ContactMethodType removeContactMethodType(ElementProperties elementProperties)
    {
        final String methodName = "removeContactMethodType";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.CONTACT_METHOD_TYPE.name,
                                                                         elementProperties,
                                                                         methodName);

            for (ContactMethodType contactMethodType : ContactMethodType.values())
            {
                if (contactMethodType.getName().equals(retrievedProperty))
                {
                    return contactMethodType;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the ContactMethodType property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return enum
     */
    IncidentReportStatus removeIncidentReportStatus(ElementProperties elementProperties)
    {
        final String methodName = "removeIncidentReportStatus";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(localServiceName,
                                                                         OpenMetadataProperty.INCIDENT_STATUS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (IncidentReportStatus incidentReportStatus : IncidentReportStatus.values())
            {
                if (incidentReportStatus.getName().equals(retrievedProperty))
                {
                    return incidentReportStatus;
                }
            }
        }

        return null;
    }
}

