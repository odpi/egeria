/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ToDoStatus;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;

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
public abstract class OpenMetadataConverterBase<B>
{
    protected PropertyHelper propertyHelper;
    protected String         serviceName;
    protected String         serverName;


    /**
     * Constructor captures the initial content
     *
     * @param propertyHelper helper object to parse element
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    public OpenMetadataConverterBase(PropertyHelper propertyHelper,
                                     String         serviceName,
                                     String         serverName)
    {
        this.propertyHelper = propertyHelper;
        this.serviceName = serviceName;
        this.serverName = serverName;
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
    public B getNewComplexBean(Class<B>                      beanClass,
                               OpenMetadataElement           primaryElement,
                               List<OpenMetadataElement>     supplementaryEntities,
                               List<OpenMetadataRelationship> relationships,
                               String                        methodName) throws PropertyServerException
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
    public B getNewRelatedMetadataElementsBean(Class<B>                beanClass,
                                               OpenMetadataRelationship relationship,
                                               String                  methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewRelatedMetadataElementsBean";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Extract the properties from the schema attribute element.  Each API creates a specialization of this method for its beans.
     *
     * @param beanClass name of the class to create
     * @param schemaAttributeElement element containing the properties for the main schema attribute
     * @param typeClass name of type used to describe the schema type
     * @param schemaType bean containing the properties of the schema type - this is filled out by the schema type converter
     * @param schemaAttributeOpenMetadataElements relationships containing the links to other schema attributes
     * @param methodName calling method
     * @param <T> bean type used to create the schema type
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public <T> B getNewSchemaAttributeBean(Class<B>                       beanClass,
                                           OpenMetadataElement            schemaAttributeElement,
                                           Class<T>                       typeClass,
                                           T                              schemaType,
                                           List<OpenMetadataRelationship> schemaAttributeOpenMetadataElements,
                                           String                         methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewSchemaAttributeBean";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /* ==========================================================
     * This method throws the exception that occurs if an OMAS fails to implement one of the updateXXXBean methods or
     * the converter is configured with an invalid bean class.
     */

    /**
     * Throw an exception to indicate that one of the update methods has not been implemented by an OMAS.
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
        throw new PropertyServerException(GAFErrorCode.MISSING_CONVERTER_METHOD.getMessageDefinition(serviceName,
                                                                                                     missingMethodName,
                                                                                                     converterClassName,
                                                                                                     beanClassName,
                                                                                                     methodName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Throw an exception to indicate that one of the update methods has not been implemented by an OMAS.
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
        throw new PropertyServerException(GAFErrorCode.INVALID_BEAN_CLASS.getMessageDefinition(beanClassName,
                                                                                               methodName,
                                                                                               serviceName,
                                                                                               serverName,
                                                                                               error.getMessage()),
                                          this.getClass().getName(),
                                          methodName,
                                          error);
    }


    /**
     * Throw an exception to indicate that one of the update methods has not been implemented by an OMAS.
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
        throw new PropertyServerException(GAFErrorCode.UNEXPECTED_BEAN_CLASS.getMessageDefinition(beanClassName,
                                                                                                  methodName,
                                                                                                  serviceName,
                                                                                                  serverName,
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
        throw new PropertyServerException(GAFErrorCode.MISSING_METADATA_INSTANCE.getMessageDefinition(serviceName,
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
            throw new PropertyServerException(GAFErrorCode.BAD_ENTITY.getMessageDefinition(methodName,
                                                                                                       serviceName,
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
            throw new PropertyServerException(GAFErrorCode.BAD_RELATIONSHIP.getMessageDefinition(methodName,
                                                                                                 serviceName,
                                                                                                 relationship.toString()),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /* ======================================================
     * The methods that follow are used by the subclasses to extract specific properties from the element properties.
     * They are used for all properties except enums which need a specific method in the OMAS converters.
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
     * Extract the properties for the requested classification from the list of classifications.
     *
     * @param classificationName name of classification
     * @param elementClassifications list of classifications from an element
     * @return list of properties for the named classification
     */
    protected ElementProperties getClassificationProperties(String               classificationName,
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
            ElementHeader elementHeader = new ElementHeader(header);

            elementHeader.setGUID(elementGUID);
            elementHeader.setClassifications(this.getElementClassifications(classifications));

            return elementHeader;
        }
        else
        {
            this.handleMissingMetadataInstance(beanClass.getName(), ElementControlHeader.class.getName(), methodName);
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
            ElementHeader          elementHeader  = new ElementHeader(element);

            elementHeader.setGUID(element.getElementGUID());
            elementHeader.setClassifications(this.getElementClassifications(element.getClassifications()));

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
     * @param beanClass name of the class to create
     * @param relatedElement from the repository
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public RelatedMetadataElementSummary getRelatedElementSummary(Class<B>               beanClass,
                                                                  RelatedMetadataElement relatedElement,
                                                                  String                 methodName) throws PropertyServerException
    {
        if ((relatedElement != null) && (relatedElement.getElement() != null))
        {
            RelatedMetadataElementSummary relatedElementSummary = new RelatedMetadataElementSummary();
            MetadataElementSummary        elementSummary = new MetadataElementSummary();
            ElementHeader                 elementHeader  = new ElementHeader(relatedElement);

            elementHeader.setGUID(relatedElement.getRelationshipGUID());

            relatedElementSummary.setRelationshipHeader(elementHeader);
            if (relatedElement.getRelationshipProperties() != null)
            {
                relatedElementSummary.setRelationshipProperties(relatedElement.getRelationshipProperties().getPropertiesAsStrings());
            }

            elementHeader = new ElementHeader(relatedElement.getElement());
            elementHeader.setGUID(relatedElement.getElement().getElementGUID());
            elementHeader.setClassifications(this.getElementClassifications(relatedElement.getElement().getClassifications()));

            elementSummary.setElementHeader(elementHeader);
            if (relatedElement.getElement().getElementProperties() != null)
            {
                elementSummary.setProperties(relatedElement.getElement().getElementProperties().getPropertiesAsStrings());
            }

            relatedElementSummary.setRelatedElement(elementSummary);

            return relatedElementSummary;
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
            ElementHeader elementHeader = getMetadataElementHeader(beanClass, element, methodName);
            ElementStub   elementStub   = new ElementStub(elementHeader);

            elementStub.setUniqueName(propertyHelper.getStringProperty(serviceName,
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
     * Extract the properties from the relationship.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return filled out element header
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is incompatible with the handler
     */
    public ElementStub getElementStub(Class<B>                beanClass,
                                      OpenMetadataRelationship relationship,
                                      String                  methodName) throws PropertyServerException
    {
        if (relationship != null)
        {
            ElementHeader elementHeader = getMetadataElementHeader(beanClass,
                                                                   relationship,
                                                                   relationship.getRelationshipGUID(),
                                                                   null,
                                                                   methodName);

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
     * @param element element containing the classifications
     * @return list of bean classifications
     */
    List<ElementClassification> getEntityClassifications(OpenMetadataElement element)
    {
        if (element != null)
        {
            return this.getElementClassifications(element.getClassifications());
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
        List<ElementClassification> beanClassifications = null;

        if (attachedClassifications != null)
        {
            beanClassifications = new ArrayList<>();

            for (AttachedClassification attachedClassification : attachedClassifications)
            {
                if (attachedClassification != null)
                {
                    ElementClassification beanClassification = new ElementClassification(attachedClassification);
                    
                    beanClassification.setClassificationName(attachedClassification.getClassificationName());
                    beanClassification.setClassificationProperties(propertyHelper.getElementPropertiesAsMap(attachedClassification.getClassificationProperties()));

                    beanClassifications.add(beanClassification);
                }
            }

        }

        return beanClassifications;
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
    public RelatedElement getRelatedElement(Class<B>                beanClass,
                                            OpenMetadataElement     element,
                                            OpenMetadataRelationship relationship,
                                            String                  methodName) throws PropertyServerException
    {
        RelatedElement  relatedElement = new RelatedElement();

        relatedElement.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, relationship.getRelationshipGUID(), null, methodName));

        if (relationship != null)
        {
            ElementProperties instanceProperties = new ElementProperties(relationship.getRelationshipProperties());

            RelationshipProperties relationshipProperties = new RelationshipProperties();

            relationshipProperties.setEffectiveFrom(relationship.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(relationship.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedElement.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), OpenMetadataRelationship.class.getName(), methodName);
        }

        if (element != null)
        {
            ElementStub elementStub = this.getElementStub(beanClass, element, methodName);

            relatedElement.setRelatedElement(elementStub);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
        }

        return relatedElement;
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
    public RelatedElement getRelatedElement(Class<B>               beanClass,
                                            RelatedMetadataElement relatedMetadataElement,
                                            String                 methodName) throws PropertyServerException
    {
        RelatedElement  relatedElement = new RelatedElement();

        relatedElement.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relatedMetadataElement, relatedMetadataElement.getRelationshipGUID(), null, methodName));

        if (relatedMetadataElement != null)
        {
            ElementProperties instanceProperties = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

            RelationshipProperties relationshipProperties = new RelationshipProperties();

            relationshipProperties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedElement.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), OpenMetadataRelationship.class.getName(), methodName);
        }

        return relatedElement;
    }




    /**
     * Extract and delete the ToDoStatus property from the supplied element properties.
     *
     * @param elementProperties properties from entity
     * @return KeyPattern enum
     */
    protected ToDoStatus removeToDoStatus(ElementProperties elementProperties)
    {
        final String methodName = "removeToDoStatus";

        if (elementProperties != null)
        {
            String retrievedProperty = propertyHelper.removeEnumProperty(serviceName,
                                                                         OpenMetadataProperty.TO_DO_STATUS.name,
                                                                         elementProperties,
                                                                         methodName);

            for (ToDoStatus status : ToDoStatus.values())
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringMapFromProperty(serviceName,
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
    protected Map<String, Object> getRemainingExtendedProperties(ElementProperties  elementProperties)
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataProperty.DISPLAY_NAME.name,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract and delete the name property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeName(ElementProperties  elementProperties)
    {
        final String methodName = "removeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.NAME.name,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
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
    protected String removeDescription(ElementProperties  elementProperties)
    {
        final String methodName = "removeDescription";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.DESCRIPTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the toDoType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeToDoType(ElementProperties  elementProperties)
    {
        final String methodName = "removeToDoType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.TO_DO_TYPE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the collectionType property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCollectionType(ElementProperties  elementProperties)
    {
        final String methodName = "removeCollectionType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.COLLECTION_TYPE.name,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeIntProperty(serviceName,
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
            return propertyHelper.removeIntProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.INITIALS.name,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.PREFERRED_LANGUAGE.name,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.IDENTIFIER.name,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeDateProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeDateProperty(serviceName,
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
            return propertyHelper.removeLongProperty(serviceName,
                                                     OpenMetadataProperty.EXT_INSTANCE_VERSION.name,
                                                     elementProperties,
                                                     methodName);
        }

        return 0L;
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.URL.name,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.ORGANIZATION_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the owningOrganization property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeOwningOrganization(ElementProperties  elementProperties)
    {
        final String methodName = "removeOwningOrganization";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.ORGANIZATION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the referenceVersion property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeReferenceVersion(ElementProperties  elementProperties)
    {
        final String methodName = "removeReferenceVersion";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.REFERENCE_VERSION.name,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.REFERENCE_ID.name,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.MEMBERSHIP_RATIONALE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the teamRole property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeTeamRole(ElementProperties  elementProperties)
    {
        final String methodName = "removeTeamRole";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.TEAM_ROLE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }

    /**
     * Extract and delete the createdBy property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCreatedBy(ElementProperties  elementProperties)
    {
        final String methodName = "removeCreatedBy";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.CREATED_BY_PROPERTY_NAME,
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
            return propertyHelper.removeStringMapFromProperty(serviceName,
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
            return propertyHelper.removeDateProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.SUPPORTED_ASSET_TYPE_NAME.name,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.EXPECTED_DATA_FORMAT.name,
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
            String connectorFrameworkName = propertyHelper.removeStringProperty(serviceName,
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
            String connectorInterfaceLanguage = propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataProperty.CONNECTOR_INTERFACES.name,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
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
            return propertyHelper.removeStringMapFromProperty(serviceName,
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
            return propertyHelper.removeMapFromProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.getMapFromProperty(serviceName,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Retrieve the zoneName from the properties.
     *
     * @param elementProperties properties from the element
     * @return zone name
     */
    protected String removeZoneName(ElementProperties elementProperties)
    {
        final String methodName = "removeZoneName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.ZONE_NAME_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.SUBJECT_AREA_NAME_PROPERTY_NAME,
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
            return propertyHelper.getStringArrayProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.getStringArrayProperty(serviceName,
                                                         OpenMetadataType.GROUPS_PROPERTY_NAME,
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
            return propertyHelper.getStringArrayProperty(serviceName,
                                                         OpenMetadataType.SECURITY_LABELS_PROPERTY_NAME,
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
            return propertyHelper.getStringMapFromProperty(serviceName,
                                                           OpenMetadataType.SECURITY_PROPERTIES_PROPERTY_NAME,
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
            return propertyHelper.removeIntProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.ORGANIZATION_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.BUSINESS_CAPABILITY_PROPERTY_NAME,
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
            return propertyHelper.getStringMapFromProperty(serviceName,
                                                           OpenMetadataType.OTHER_ORIGIN_VALUES_PROPERTY_NAME,
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
            return propertyHelper.removeDateProperty(serviceName,
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
            return propertyHelper.removeDateProperty(serviceName,
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
            return propertyHelper.removeDateProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
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
            Date createTime1 = propertyHelper.removeDateProperty(serviceName,
                                                                 OpenMetadataProperty.STORE_CREATE_TIME.name,
                                                                 elementProperties,
                                                                 methodName);
            Date createTime2 = propertyHelper.removeDateProperty(serviceName,
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
            Date modifiedTime1 = propertyHelper.removeDateProperty(serviceName,
                                                                   OpenMetadataProperty.STORE_UPDATE_TIME.name,
                                                                   elementProperties,
                                                                   methodName);
            Date modifiedTime2 = propertyHelper.removeDateProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataProperty.ENCODING.name,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.getStringMapFromProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.FORMAT_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.ENCRYPTION_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
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
    protected String removeCapabilityType(ElementProperties  elementProperties)
    {
        final String methodName = "removeCapabilityType";

        if (elementProperties != null)
        {
            String type = propertyHelper.removeStringProperty(serviceName,
                                                              OpenMetadataProperty.CAPABILITY_TYPE.name,
                                                              elementProperties,
                                                              methodName);
            if (type == null)
            {
                type = propertyHelper.getStringProperty(serviceName,
                                                        OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                        elementProperties,
                                                        methodName);
            }

            return type;
        }

        return null;
    }


    /**
     * Extract and delete the capabilityVersion property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCapabilityVersion(ElementProperties  elementProperties)
    {
        final String methodName = "removeCapabilityVersion";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.CAPABILITY_VERSION.name,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.PATCH_LEVEL.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Retrieve the isDeprecated flag from the properties from the supplied element properties.
     *
     * @param elementProperties properties from the classification
     * @return boolean - default is false
     */
    protected boolean removeIsDeprecated(ElementProperties  elementProperties)
    {
        final String methodName = "removeIsDeprecated";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(serviceName,
                                                        OpenMetadataType.IS_DEPRECATED_PROPERTY_NAME,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
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
            return propertyHelper.removeBooleanProperty(serviceName,
                                                        OpenMetadataType.IS_DEFAULT_VALUE_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.DATA_TYPE_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.DEFAULT_VALUE_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.FIXED_VALUE_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.QUERY_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.QUERY_ID_PROPERTY_NAME,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }



    /**
     * Extract and delete the version number property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeVersionNumber(ElementProperties  elementProperties)
    {
        final String methodName = "removeVersionNumber";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.VERSION_NUMBER_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the id property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeId(ElementProperties  elementProperties)
    {
        final String methodName = "removeId";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.ID_PROPERTY_NAME,
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
            return propertyHelper.removeDateProperty(serviceName,
                                                     OpenMetadataType.CREATED_TIME_PROPERTY_NAME,
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
            return propertyHelper.removeDateProperty(serviceName,
                                                     OpenMetadataType.LAST_MODIFIED_TIME_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.LAST_MODIFIER_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.AUTHOR_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.ENCODING_STANDARD_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.NAMESPACE_PROPERTY_NAME,
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
            return propertyHelper.removeIntProperty(serviceName,
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
            return propertyHelper.getIntProperty(serviceName,
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
            return propertyHelper.removeIntProperty(serviceName,
                                                    OpenMetadataType.MIN_CARDINALITY_PROPERTY_NAME,
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
            return propertyHelper.removeIntProperty(serviceName,
                                                    OpenMetadataType.MAX_CARDINALITY_PROPERTY_NAME,
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
            return propertyHelper.removeBooleanProperty(serviceName,
                                                        OpenMetadataType.ALLOWS_DUPLICATES_PROPERTY_NAME,
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
            return propertyHelper.removeBooleanProperty(serviceName,
                                                        OpenMetadataType.ORDERED_VALUES_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.DEFAULT_VALUE_OVERRIDE_PROPERTY_NAME,
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
            return propertyHelper.removeIntProperty(serviceName,
                                                    OpenMetadataType.MIN_LENGTH_PROPERTY_NAME,
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
            return propertyHelper.removeIntProperty(serviceName,
                                                    OpenMetadataType.LENGTH_PROPERTY_NAME,
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
            int retrievedValue = propertyHelper.removeIntProperty(serviceName,
                                                                  OpenMetadataType.PRECISION_PROPERTY_NAME,
                                                                  elementProperties,
                                                                  methodName);

            if (retrievedValue == 0)
            {
                retrievedValue = propertyHelper.removeIntProperty(serviceName,
                                                                  OpenMetadataType.SIGNIFICANT_DIGITS_PROPERTY_NAME,
                                                                  elementProperties,
                                                                  methodName);
            }

            return retrievedValue;
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
            return propertyHelper.removeBooleanProperty(serviceName,
                                                        OpenMetadataType.IS_NULLABLE_PROPERTY_NAME,
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
            return propertyHelper.removeBooleanProperty(serviceName,
                                                        OpenMetadataType.REQUIRED_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.NATIVE_CLASS_PROPERTY_NAME,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataType.ALIASES_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.GUARD_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.IMPLEMENTATION_LANGUAGE.name,
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
            return propertyHelper.removeBooleanProperty(serviceName,
                                                        OpenMetadataType.USES_BLOCKING_CALLS_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.LANGUAGE_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.SUMMARY.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and remove the publishVersionIdentifier property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String removePublishVersionIdentifier(ElementProperties elementProperties)
    {
        final String methodName = "removePublishVersionIdentifier";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.PUBLISH_VERSION_ID.name,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.EXAMPLES.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the title property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String removeTitle(ElementProperties elementProperties)
    {
        final String methodName = "removeTitle";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.TITLE.name,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.PRONOUNS.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the text property from the supplied element properties.
     *
     * @param elementProperties properties from governance entities
     * @return string property or null
     */
    protected String removeText(ElementProperties elementProperties)
    {
        final String methodName = "removeText";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.TEXT.name,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeIntProperty(serviceName,
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
            return propertyHelper.removeIntProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataType.IMPLICATIONS_PROPERTY_NAME,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataType.OUTCOMES_PROPERTY_NAME,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataType.RESULTS_PROPERTY_NAME,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataType.BUSINESS_IMPERATIVES_PROPERTY_NAME,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the jurisdiction property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeJurisdiction(ElementProperties elementProperties)
    {
        final String methodName = "removeJurisdiction";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.JURISDICTION_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the "details" property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDetails(ElementProperties elementProperties)
    {
        final String methodName = "removeDetails";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.DETAILS_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.RATIONALE_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.IMPLEMENTATION_DESCRIPTION_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.CRITERIA_PROPERTY_NAME,
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
            return propertyHelper.removeIntProperty(serviceName,
                                                    OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME,
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
            return propertyHelper.removeIntProperty(serviceName,
                                                    OpenMetadataType.LEVEL_IDENTIFIER_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.MEASUREMENT_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.TARGET_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringMapFromProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.EXECUTOR_ENGINE_GUID_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.EXECUTOR_ENGINE_NAME_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.PROCESS_NAME.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requiredRequestParameters property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map of request parameters
     */
    protected Map<String, String> removeRequiredRequestParameters(ElementProperties elementProperties)

    {
        final String methodName = "removeRequiredRequestParameters";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(serviceName,
                                                              OpenMetadataType.REQUIRED_REQUEST_PARAMETERS_PROPERTY_NAME,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requiredActionTargets property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map of action targets
     */
    protected Map<String, String> removeRequiredActionTargets(ElementProperties elementProperties)

    {
        final String methodName = "removeRequiredActionTargets";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(serviceName,
                                                              OpenMetadataType.REQUIRED_ACTION_TARGETS_PROPERTY_NAME,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the producedRequestParameters property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map of request params
     */
    protected Map<String, String> removeProducedRequestParameters(ElementProperties elementProperties)

    {
        final String methodName = "removeProducedRequestParameters";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(serviceName,
                                                              OpenMetadataType.PRODUCED_REQUEST_PARAMETERS_PROPERTY_NAME,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the producedActionTargets property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map of action targets
     */
    protected Map<String, String> removeProducedActionTargets(ElementProperties elementProperties)

    {
        final String methodName = "removeProducedActionTargets";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(serviceName,
                                                              OpenMetadataType.PRODUCED_ACTION_TARGETS_PROPERTY_NAME,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the producedGuards property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return map of guards
     */
    protected Map<String, String> removeProducedGuards(ElementProperties elementProperties)

    {
        final String methodName = "removeProducedGuards";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(serviceName,
                                                              OpenMetadataType.PRODUCED_GUARDS_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.GUARD_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the mandatoryGuards property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return array of guards
     */
    protected List<String> removeMandatoryGuards(ElementProperties elementProperties)

    {
        final String methodName = "removeMandatoryGuards";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataType.MANDATORY_GUARDS_PROPERTY_NAME,
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
            return propertyHelper.removeBooleanProperty(serviceName,
                                                        OpenMetadataType.MANDATORY_GUARD_PROPERTY_NAME,
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
            return propertyHelper.removeBooleanProperty(serviceName,
                                                        OpenMetadataType.IGNORE_MULTIPLE_TRIGGERS_PROPERTY_NAME,
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
            return propertyHelper.removeIntProperty(serviceName,
                                                    OpenMetadataType.WAIT_TIME_PROPERTY_NAME,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }


    /**
     * Extract and delete the receivedGuards property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return array of guards
     */
    protected List<String> removeReceivedGuards(ElementProperties elementProperties)

    {
        final String methodName = "removeReceivedGuards";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataType.RECEIVED_GUARDS_PROPERTY_NAME,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the completionGuards property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return array of guards
     */
    protected List<String> removeCompletionGuards(ElementProperties elementProperties)

    {
        final String methodName = "removeCompletionGuards";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataType.COMPLETION_GUARDS_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.COMPLETION_MESSAGE_PROPERTY_NAME,
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
            return propertyHelper.removeDateProperty(serviceName,
                                                     OpenMetadataType.START_DATE_PROPERTY_NAME,
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
            return propertyHelper.removeDateProperty(serviceName,
                                                     OpenMetadataType.PLANNED_END_DATE_PROPERTY_NAME,
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
            return propertyHelper.removeDateProperty(serviceName,
                                                     OpenMetadataProperty.CREATION_TIME.name,
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
            return propertyHelper.removeDateProperty(serviceName,
                                                     OpenMetadataProperty.LAST_REVIEW_TIME.name,
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
            return propertyHelper.removeDateProperty(serviceName,
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
            return propertyHelper.removeDateProperty(serviceName,
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
            return propertyHelper.removeDateProperty(serviceName,
                                                     OpenMetadataProperty.COMPLETION_DATE.name,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.PROJECT_PHASE.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the requestSourceName property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String removeRequestSourceName(ElementProperties elementProperties)

    {
        final String methodName = "removeRequestSourceName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.REQUEST_SOURCE_NAME_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.ACTION_TARGET_NAME_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the originGovernanceService property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String removeOriginGovernanceService(ElementProperties elementProperties)

    {
        final String methodName = "removeOriginGovernanceService";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.ORIGIN_GOVERNANCE_SERVICE_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the originGovernanceEngine property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return String text or null
     */
    protected String removeOriginGovernanceEngine(ElementProperties elementProperties)

    {
        final String methodName = "removeOriginGovernanceEngine";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.ORIGIN_GOVERNANCE_ENGINE_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.LICENSE_GUID_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.CERTIFICATE_GUID_PROPERTY_NAME,
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
            return propertyHelper.getDateProperty(serviceName,
                                                  OpenMetadataType.START_PROPERTY_NAME,
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
            return propertyHelper.getDateProperty(serviceName,
                                                  OpenMetadataType.END_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.CONDITIONS_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.CUSTODIAN_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.CERTIFIED_BY_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.RECIPIENT_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.LICENSED_BY_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.LICENSEE_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeBooleanProperty(serviceName,
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
            return propertyHelper.getBooleanProperty(serviceName,
                                                     OpenMetadataType.IS_STRICT_REQUIREMENT_PROPERTY_NAME,
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
            return propertyHelper.getIntProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.ATTRIBUTE_NAME_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.POINT_TYPE_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.ASSOCIATION_DESCRIPTION_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.SYMBOLIC_NAME_PROPERTY_NAME,
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
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataType.IMPLEMENTATION_VALUE_PROPERTY_NAME,
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
            return propertyHelper.getStringMapFromProperty(serviceName,
                                                           OpenMetadataType.ADDITIONAL_VALUES_PROPERTY_NAME,
                                                           elementProperties,
                                                           methodName);
        }

        return null;
    }


    /**
     * Extract and delete the commentText property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeCommentText(ElementProperties elementProperties)
    {
        final String methodName = "removeCommentText";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.TEXT.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the isPublic property from the supplied element properties.
     *
     * @param elementProperties properties from feedback relationships
     * @return boolean
     */
    protected boolean getIsPublic(ElementProperties elementProperties)
    {
        final String methodName = "getIsPublic";

        if (elementProperties != null)
        {
            return propertyHelper.getBooleanProperty(serviceName,
                                                     OpenMetadataProperty.IS_PUBLIC.name,
                                                     elementProperties,
                                                     methodName);
        }

        return false;
    }


    /**
     * Extract the isPublic property from the supplied element properties.
     *
     * @param elementProperties properties from feedback relationships
     * @return boolean
     */
    protected boolean removeIsPublic(ElementProperties elementProperties)
    {
        final String methodName = "removeIsPublic";

        if (elementProperties != null)
        {
            return propertyHelper.removeBooleanProperty(serviceName,
                                                        OpenMetadataProperty.IS_PUBLIC.name,
                                                        elementProperties,
                                                        methodName);
        }

        return false;
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.REVIEW.name,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.TAG_NAME.name,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.TAG_DESCRIPTION.name,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the executionDate property from the supplied element properties.
     *
     * @param elementProperties properties from discovery analysis report entities
     * @return string property or null
     */
    protected Date removeExecutionDate(ElementProperties elementProperties)
    {
        final String methodName = "removeExecutionDate";

        if (elementProperties != null)
        {
            return propertyHelper.removeDateProperty(serviceName,
                                                     OpenMetadataType.EXECUTION_DATE_PROPERTY_NAME,
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
            return propertyHelper.removeStringMapFromProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeIntProperty(serviceName,
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
            return propertyHelper.removeIntProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.ATTRIBUTE_NAME_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeDateProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringMapFromProperty(serviceName,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeIntProperty(serviceName,
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
            return propertyHelper.removeIntProperty(serviceName,
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
            return propertyHelper.removeIntProperty(serviceName,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataProperty.PROFILE_PROPERTY_NAMES.name,
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
            return propertyHelper.removeDateProperty(serviceName,
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
            return propertyHelper.removeDateProperty(serviceName,
                                                     OpenMetadataProperty.PROFILE_END_DATE.name,
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
            return propertyHelper.removeStringMapFromProperty(serviceName,
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
            return propertyHelper.removeBooleanMapFromProperty(serviceName,
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
            return propertyHelper.removeDateMapFromProperty(serviceName,
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
            return propertyHelper.removeLongMapFromProperty(serviceName,
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
            return propertyHelper.removeDoubleMapFromProperty(serviceName,
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
            return propertyHelper.removeStringArrayProperty(serviceName,
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
            return propertyHelper.removeIntegerMapFromProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringMapFromProperty(serviceName,
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
            return propertyHelper.removeLongProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataProperty.ENCODING.name,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.PARAMETER_TYPE_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeIntProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringMapFromProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
            return propertyHelper.removeStringMapFromProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
     * @return map of name-value pairs
     */
    protected List<String> removeCandidateGlossaryTermGUIDs(ElementProperties elementProperties)
    {
        final String methodName = "removeCandidateGlossaryTermGUIDs";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
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
            return propertyHelper.removeStringProperty(serviceName,
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
     * @return map of name-value pairs
     */
    protected List<String> removeCandidateGlossaryCategoryGUIDs(ElementProperties elementProperties)
    {
        final String methodName = "removeCandidateGlossaryCategoryGUIDs";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataProperty.CANDIDATE_GLOSSARY_CATEGORY_GUIDS.name,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the dataFieldName standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDataFieldName(ElementProperties  elementProperties)
    {
        final String methodName = "removeDataFieldName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.DATA_FIELD_NAME_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the dataFieldType standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDataFieldType(ElementProperties  elementProperties)
    {
        final String methodName = "removeDataFieldType";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.DATA_FIELD_TYPE_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the dataFieldDescription standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDataFieldDescription(ElementProperties  elementProperties)
    {
        final String methodName = "removeDataFieldDescription";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataType.DATA_FIELD_DESCRIPTION_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the dataFieldAliases property from the supplied element properties.
     *
     * @param elementProperties properties from data field entities
     * @return map of name-value pairs
     */
    protected List<String> removeDataFieldAliases(ElementProperties elementProperties)
    {
        final String methodName = "removeDataFieldAliases";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataType.DATA_FIELD_ALIASES_PROPERTY_NAME,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }

}

