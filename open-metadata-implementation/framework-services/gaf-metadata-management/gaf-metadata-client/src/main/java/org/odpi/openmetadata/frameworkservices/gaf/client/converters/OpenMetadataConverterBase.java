/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementControlHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataTypesMapper;
import org.odpi.openmetadata.frameworks.governanceaction.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworkservices.gaf.ffdc.OpenMetadataStoreErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;

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
    public B getNewBean(Class<B>               beanClass,
                        RelatedMetadataElement openMetadataElement,
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
    public B getNewBean(Class<B>                beanClass,
                        OpenMetadataElement     element,
                        RelatedMetadataElements relationship,
                        String                  methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewBean(element, relationship)";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or DataField bean which combine knowledge from the element and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewComplexBean(Class<B>                      beanClass,
                               OpenMetadataElement           primaryEntity,
                               List<RelatedMetadataElements> relationships,
                               String                        methodName) throws PropertyServerException
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
     * @param primaryEntity element that is the root of the collection of entities that make up the content of the bean
     * @param supplementaryEntities entities connected to the primary element by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewComplexBean(Class<B>           beanClass,
                               OpenMetadataElement       primaryEntity,
                               List<OpenMetadataElement> supplementaryEntities,
                               List<RelatedMetadataElements> relationships,
                               String             methodName) throws PropertyServerException
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
    public B getNewRelatedMetadataElementsBean(Class<B>     beanClass,
                                    RelatedMetadataElements relationship,
                                    String       methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewRelatedMetadataElementsBean";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Return the converted bean.  This is a special method used for schema types since they are stored
     * as a collection of instances.
     *
     * @param beanClass name of the class to create
     * @param schemaRootHeader header of the schema element that holds the root information
     * @param schemaTypeTypeName name of type of the schema type to create
     * @param elementProperties properties describing the schema type
     * @param schemaRootClassifications classifications from the schema root element
     * @param attributeCount number of attributes (for a complex schema type)
     * @param validValueSetGUID unique identifier of the set of valid values (for an enum schema type)
     * @param externalSchemaType unique identifier for the properties of the schema type that is shared by multiple attributes/assets
     * @param mapFromSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param mapToSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param schemaTypeOptions list of schema types that could be the type for this attribute
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewSchemaTypeBean(Class<B>             beanClass,
                                  InstanceHeader       schemaRootHeader,
                                  String               schemaTypeTypeName,
                                  ElementProperties   elementProperties,
                                  List<Classification> schemaRootClassifications,
                                  int                  attributeCount,
                                  String               validValueSetGUID,
                                  B                    externalSchemaType,
                                  B                    mapFromSchemaType,
                                  B                    mapToSchemaType,
                                  List<B>              schemaTypeOptions,
                                  String               methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewSchemaTypeBean";

        handleUnimplementedConverterMethod(beanClass.getName(), thisMethodName, this.getClass().getName(), methodName);

        return null;
    }


    /**
     * Return the converted bean.  This is a special method used for schema types since they are stored
     * as a collection of instances.  For external schema types and map elements, both the GUID and the bean are returned to
     * allow the consuming OMAS to choose whether it is returning GUIDs of the linked to schema or the schema type bean itself.
     *
     * @param beanClass name of the class to create
     * @param schemaRootHeader header of the schema element that holds the root information
     * @param schemaTypeTypeName name of type of the schema type to create
     * @param elementProperties properties describing the schema type
     * @param schemaRootClassifications classifications from the schema root element
     * @param attributeCount number of attributes (for a complex schema type)
     * @param validValueSetGUID unique identifier of the set of valid values (for an enum schema type)
     * @param externalSchemaTypeGUID unique identifier of the external schema type
     * @param externalSchemaType unique identifier for the properties of the schema type that is shared by multiple attributes/assets
     * @param mapFromSchemaTypeGUID unique identifier of the mapFrom schema type
     * @param mapFromSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param mapToSchemaTypeGUID unique identifier of the mapTo schema type
     * @param mapToSchemaType bean containing the properties of the schema type that is part of a map definition
     * @param schemaTypeOptionGUIDs list of unique identifiers for schema types that could be the type for this attribute
     * @param schemaTypeOptions list of schema types that could be the type for this attribute
     * @param queryTargets list of relationships to schema types that contain data values used to derive the schema type value(s)
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewSchemaTypeBean(Class<B>             beanClass,
                                  InstanceHeader       schemaRootHeader,
                                  String               schemaTypeTypeName,
                                  ElementProperties    elementProperties,
                                  List<Classification> schemaRootClassifications,
                                  int                  attributeCount,
                                  String               validValueSetGUID,
                                  String               externalSchemaTypeGUID,
                                  B                    externalSchemaType,
                                  String               mapFromSchemaTypeGUID,
                                  B                    mapFromSchemaType,
                                  String               mapToSchemaTypeGUID,
                                  B                    mapToSchemaType,
                                  List<String>         schemaTypeOptionGUIDs,
                                  List<B>              schemaTypeOptions,
                                  List<RelatedMetadataElements>   queryTargets,
                                  String               methodName) throws PropertyServerException
    {
        return this.getNewSchemaTypeBean(beanClass,
                                         schemaRootHeader,
                                         schemaTypeTypeName,
                                         elementProperties,
                                         schemaRootClassifications,
                                         attributeCount,
                                         validValueSetGUID,
                                         externalSchemaType,
                                         mapFromSchemaType,
                                         mapToSchemaType,
                                         schemaTypeOptions,
                                         methodName);
    }


    /**
     * Extract the properties from the schema attribute element.  Each API creates a specialization of this method for its beans.
     *
     * @param beanClass name of the class to create
     * @param schemaAttributeEntity element containing the properties for the main schema attribute
     * @param typeClass name of type used to describe the schema type
     * @param schemaType bean containing the properties of the schema type - this is filled out by the schema type converter
     * @param schemaAttributeRelatedMetadataElements relationships containing the links to other schema attributes
     * @param methodName calling method
     * @param <T> bean type used to create the schema type
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public <T> B getNewSchemaAttributeBean(Class<B>            beanClass,
                                           OpenMetadataElement schemaAttributeEntity,
                                           Class<T>            typeClass,
                                           T                   schemaType,
                                           List<RelatedMetadataElements> schemaAttributeRelatedMetadataElements,
                                           String             methodName) throws PropertyServerException
    {
        final String thisMethodName = "getNewSchemaAttributeBean)";

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
    private void handleUnimplementedConverterMethod(String beanClassName,
                                                    String missingMethodName,
                                                    String converterClassName,
                                                    String methodName) throws PropertyServerException
    {
        throw new PropertyServerException(OpenMetadataStoreErrorCode.MISSING_CONVERTER_METHOD.getMessageDefinition(serviceName,
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
        throw new PropertyServerException(OpenMetadataStoreErrorCode.INVALID_BEAN_CLASS.getMessageDefinition(beanClassName,
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
        throw new PropertyServerException(OpenMetadataStoreErrorCode.UNEXPECTED_BEAN_CLASS.getMessageDefinition(beanClassName,
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
        throw new PropertyServerException(OpenMetadataStoreErrorCode.MISSING_METADATA_INSTANCE.getMessageDefinition(serviceName,
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
            throw new PropertyServerException(OpenMetadataStoreErrorCode.BAD_ENTITY.getMessageDefinition(methodName,
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
                                                    RelatedMetadataElements relationship,
                                                    String                  methodName) throws PropertyServerException
    {
        if (relationship == null)
        {
            handleMissingMetadataInstance(beanClassName, RelatedMetadataElements.class.getName(), methodName);
        }
        else
        {
            throw new PropertyServerException(OpenMetadataStoreErrorCode.BAD_RELATIONSHIP.getMessageDefinition(methodName,
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
    protected ElementProperties getClassificationProperties(String             classificationName,
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
                                                                       OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME,
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
                                      RelatedMetadataElements relationship,
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
                                               RelatedMetadataElements.class.getName(),
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
    protected List<ElementClassification> getElementClassifications(List<AttachedClassification> attachedClassifications)
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
                                                    OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME,
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
                                                              OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.VERSION_IDENTIFIER_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.COLLECTION_TYPE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.KEYWORD_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.TOPIC_TYPE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.OPERATING_SYSTEM_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.OPERATING_SYSTEM_PATCH_LEVEL_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.MINIMUM_INSTANCES_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.MAXIMUM_INSTANCES_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.INITIALS_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.GIVEN_NAMES_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.SURNAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.FULL_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.PREFERRED_LANGUAGE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.JOB_TITLE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.EMPLOYEE_NUMBER_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.EMPLOYEE_TYPE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.CONTACT_TYPE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.CONTACT_METHOD_SERVICE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.CONTACT_METHOD_VALUE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.MISSION_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ASSOCIATION_TYPE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.IDENTIFIER_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.EXT_INSTANCE_CREATED_BY_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.EXT_INSTANCE_CREATION_TIME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.EXT_INSTANCE_LAST_UPDATED_BY_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.EXT_INSTANCE_LAST_UPDATE_TIME_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.EXT_INSTANCE_VERSION_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.URL_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ORGANIZATION_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.REFERENCE_VERSION_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.REFERENCE_ID_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.REFERENCE_ID_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ORDER_PROPERTY_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.MEMBERSHIP_RATIONALE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.CREATED_BY_PROPERTY_NAME,
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
                                                              OpenMetadataTypesMapper.MAPPING_PROPERTIES_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.LAST_SYNCHRONIZED_PROPERTY_NAME,
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
            String networkAddress = propertyHelper.removeStringProperty(serviceName,
                                                                        OpenMetadataTypesMapper.NETWORK_ADDRESS_PROPERTY_NAME,
                                                                        elementProperties,
                                                                        methodName);

            if (networkAddress == null)
            {
                networkAddress = propertyHelper.removeStringProperty(serviceName,
                                                                     OpenMetadataTypesMapper.NETWORK_ADDRESS_PROPERTY_NAME_DEP,
                                                                     elementProperties,
                                                                     methodName);
            }

            return networkAddress;
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
            String postalAddress = propertyHelper.removeStringProperty(serviceName,
                                                                       OpenMetadataTypesMapper.POSTAL_ADDRESS_PROPERTY_NAME,
                                                                       elementProperties,
                                                                       methodName);

            if (postalAddress == null)
            {
                postalAddress = propertyHelper.removeStringProperty(serviceName,
                                                                    OpenMetadataTypesMapper.POSTAL_ADDRESS_PROPERTY_NAME_DEP,
                                                                    elementProperties,
                                                                    methodName);
            }

            return postalAddress;
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
                                                       OpenMetadataTypesMapper.COORDINATES_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.MAP_PROJECTION_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.TIME_ZONE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.LEVEL_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.PROTOCOL_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ENCRYPTION_METHOD_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.CONNECTOR_PROVIDER_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.SUPPORTED_ASSET_TYPE_NAME,
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
                                                       OpenMetadataTypesMapper.EXPECTED_DATA_FORMAT,
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
                                                                                OpenMetadataTypesMapper.CONNECTOR_FRAMEWORK_NAME,
                                                                                elementProperties,
                                                                                methodName);
            if (connectorFrameworkName != null)
            {
                return connectorFrameworkName;
            }
        }

        return OpenMetadataTypesMapper.CONNECTOR_FRAMEWORK_NAME_DEFAULT;
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
                                                                                    OpenMetadataTypesMapper.CONNECTOR_INTERFACE_LANGUAGE,
                                                                                    elementProperties,
                                                                                    methodName);
            if (connectorInterfaceLanguage != null)
            {
                return connectorInterfaceLanguage;
            }
        }

        return OpenMetadataTypesMapper.CONNECTOR_INTERFACE_LANGUAGE_DEFAULT;
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
                                                            OpenMetadataTypesMapper.CONNECTOR_INTERFACES,
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
                                                       OpenMetadataTypesMapper.TARGET_TECHNOLOGY_SOURCE,
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
                                                       OpenMetadataTypesMapper.TARGET_TECHNOLOGY_NAME,
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
                                                            OpenMetadataTypesMapper.TARGET_TECHNOLOGY_INTERFACES,
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
                                                            OpenMetadataTypesMapper.TARGET_TECHNOLOGY_VERSIONS,
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
                                                            OpenMetadataTypesMapper.RECOGNIZED_ADD_PROPS_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.RECOGNIZED_SEC_PROPS_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.RECOGNIZED_CONFIG_PROPS_PROPERTY_NAME,
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
                                                              OpenMetadataTypesMapper.SECURED_PROPERTIES_PROPERTY_NAME,
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
                                                        OpenMetadataTypesMapper.CONFIGURATION_PROPERTIES_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.USER_ID_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.CLEAR_PASSWORD_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ENCRYPTED_PASSWORD_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.ASSET_SUMMARY_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.ARGUMENTS_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ZONE_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.SUBJECT_AREA_NAME_PROPERTY_NAME,
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
                                                         OpenMetadataTypesMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.OWNER_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.OWNER_PROPERTY_NAME,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }



    /**
     * Extract the ownerTypeName property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String getOwnerTypeName(ElementProperties elementProperties)
    {
        final String methodName = "getOwnerTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataTypesMapper.OWNER_TYPE_NAME_PROPERTY_NAME,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the ownerPropertyName property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return string text or null
     */
    protected String getOwnerPropertyName(ElementProperties elementProperties)
    {
        final String methodName = "getOwnerPropertyName";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataTypesMapper.OWNER_PROPERTY_NAME_PROPERTY_NAME,
                                                    elementProperties,
                                                    methodName);
        }

        return null;
    }


    /**
     * Extract the ownerType property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return symbolic name
     */
    protected String getOwnerType(ElementProperties elementProperties)
    {
        final String methodName = "getOwnerType";

        if (elementProperties != null)
        {
            return propertyHelper.getEnumPropertySymbolicName(serviceName,
                                                              OpenMetadataTypesMapper.OWNER_TYPE_PROPERTY_NAME,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the ownerType property from the supplied element properties.
     *
     * @param elementProperties properties from classification
     * @return symbolic name
     */
    protected String removeOwnerType(ElementProperties elementProperties)
    {
        final String methodName = "removeOwnerType";

        if (elementProperties != null)
        {
            return propertyHelper.removeEnumProperty(serviceName,
                                                     OpenMetadataTypesMapper.OWNER_TYPE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.OWNER_PROPERTY_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.OWNER_TYPE_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ROLE_TYPE_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.DISTINGUISHED_NAME_PROPERTY_NAME,
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
                                                         OpenMetadataTypesMapper.GROUPS_PROPERTY_NAME,
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
                                                         OpenMetadataTypesMapper.SECURITY_LABELS_PROPERTY_NAME,
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
                                                           OpenMetadataTypesMapper.SECURITY_PROPERTIES_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.KARMA_POINTS_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.ORGANIZATION_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.BUSINESS_CAPABILITY_PROPERTY_NAME,
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
                                                           OpenMetadataTypesMapper.OTHER_ORIGIN_VALUES_PROPERTY_NAME,
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
    protected Date removeSourceCreateTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeSourceCreateTime";

        if (elementProperties != null)
        {
            Date createTime1 = propertyHelper.removeDateProperty(serviceName,
                                                                 OpenMetadataTypesMapper.SOURCE_CREATE_TIME_PROPERTY_NAME,
                                                                 elementProperties,
                                                                 methodName);
            Date createTime2 = propertyHelper.removeDateProperty(serviceName,
                                                                 OpenMetadataTypesMapper.SOURCE_CREATE_TIME_PROPERTY_NAME_DEP,
                                                                 elementProperties,
                                                                 methodName);
            return createTime1 == null ? createTime2 : createTime1;
        }

        return null;
    }


    /**
     * Extract and delete the sourceUpdateTime property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return date or null
     */
    protected Date removeSourceUpdateTime(ElementProperties  elementProperties)
    {
        final String methodName = "removeSourceUpdateTime";

        if (elementProperties != null)
        {
            Date modifiedTime1 = propertyHelper.removeDateProperty(serviceName,
                                                                   OpenMetadataTypesMapper.SOURCE_UPDATE_TIME_PROPERTY_NAME,
                                                                   elementProperties,
                                                                   methodName);
            Date modifiedTime2 = propertyHelper.removeDateProperty(serviceName,
                                                                   OpenMetadataTypesMapper.SOURCE_UPDATE_TIME_PROPERTY_NAME_DEP,
                                                                   elementProperties,
                                                                   methodName);
            return modifiedTime1 == null ? modifiedTime2 : modifiedTime1;
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
                                                    OpenMetadataTypesMapper.PATH_NAME_PROPERTY_NAME,
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
                                                                 OpenMetadataTypesMapper.STORE_CREATE_TIME_PROPERTY_NAME,
                                                                 elementProperties,
                                                                 methodName);
            Date createTime2 = propertyHelper.removeDateProperty(serviceName,
                                                                 OpenMetadataTypesMapper.STORE_CREATE_TIME_PROPERTY_NAME_DEP,
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
                                                                   OpenMetadataTypesMapper.STORE_UPDATE_TIME_PROPERTY_NAME,
                                                                   elementProperties,
                                                                   methodName);
            Date modifiedTime2 = propertyHelper.removeDateProperty(serviceName,
                                                                   OpenMetadataTypesMapper.STORE_UPDATE_TIME_PROPERTY_NAME_DEP,
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
    protected String getDataStoreEncodingType(ElementProperties  elementProperties)
    {
        final String methodName = "getDataStoreEncodingType";

        if (elementProperties != null)
        {
            return propertyHelper.getStringProperty(serviceName,
                                                    OpenMetadataTypesMapper.ENCODING_TYPE_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.ENCODING_LANGUAGE_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.ENCODING_DESCRIPTION_PROPERTY_NAME,
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
                                                           OpenMetadataTypesMapper.ENCODING_PROPERTIES_PROPERTY_NAME,
                                                           elementProperties,
                                                           methodName);
        }

        return null;
    }


    /**
     * Extract and delete the database type property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeDatabaseType(ElementProperties  elementProperties)
    {
        final String methodName = "removeDatabaseType";

        if (elementProperties != null)
        {
            String type1 = propertyHelper.removeStringProperty(serviceName,
                                                               OpenMetadataTypesMapper.DATABASE_TYPE_PROPERTY_NAME,
                                                               elementProperties,
                                                               methodName);
            String type2 = propertyHelper.removeStringProperty(serviceName,
                                                               OpenMetadataTypesMapper.DATABASE_TYPE_PROPERTY_NAME_DEP,
                                                               elementProperties,
                                                               methodName);
            return type1 == null ? type2 : type1;
        }

        return null;
    }


    /**
     * Extract and delete the database version property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string name or null
     */
    protected String removeDatabaseVersion(ElementProperties  elementProperties)
    {
        final String methodName = "removeDatabaseVersion";

        if (elementProperties != null)
        {
            String version1 = propertyHelper.removeStringProperty(serviceName,
                                                                  OpenMetadataTypesMapper.DATABASE_VERSION_PROPERTY_NAME,
                                                                  elementProperties,
                                                                  methodName);
            String version2 = propertyHelper.removeStringProperty(serviceName,
                                                                  OpenMetadataTypesMapper.DATABASE_VERSION_PROPERTY_NAME_DEP,
                                                                  elementProperties,
                                                                  methodName);
            return version1 == null ? version2 : version1;
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
                                                       OpenMetadataTypesMapper.DATABASE_INSTANCE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.DATABASE_IMPORTED_FROM_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.FILE_TYPE_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.FORMAT_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.ENCRYPTION_PROPERTY_NAME,
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
            String type = propertyHelper.removeStringProperty(serviceName,
                                                              OpenMetadataTypesMapper.DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME,
                                                              elementProperties,
                                                              methodName);
            if (type == null)
            {
                type = propertyHelper.removeStringProperty(serviceName,
                                                           OpenMetadataTypesMapper.DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME_DEP,
                                                           elementProperties,
                                                           methodName);
            }

            return type;
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
                                                              OpenMetadataTypesMapper.CAPABILITY_TYPE_PROPERTY_NAME,
                                                              elementProperties,
                                                              methodName);
            if (type == null)
            {
                type = propertyHelper.removeStringProperty(serviceName,
                                                           OpenMetadataTypesMapper.CAPABILITY_TYPE_PROPERTY_NAME_DEP1,
                                                           elementProperties,
                                                           methodName);
            }

            if (type == null)
            {
                type = propertyHelper.removeStringProperty(serviceName,
                                                           OpenMetadataTypesMapper.CAPABILITY_TYPE_PROPERTY_NAME_DEP2,
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
            String version1 = propertyHelper.removeStringProperty(serviceName,
                                                                  OpenMetadataTypesMapper.CAPABILITY_VERSION_PROPERTY_NAME,
                                                                  elementProperties,
                                                                  methodName);
            String version2 = propertyHelper.removeStringProperty(serviceName,
                                                                  OpenMetadataTypesMapper.CAPABILITY_VERSION_PROPERTY_NAME_DEP,
                                                                  elementProperties,
                                                                  methodName);
            return version1 == null ? version2 : version1;
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
                                                       OpenMetadataTypesMapper.PATCH_LEVEL_PROPERTY_NAME,
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
                                                        OpenMetadataTypesMapper.IS_DEPRECATED_PROPERTY_NAME,
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
                                                        OpenMetadataTypesMapper.IS_DEFAULT_VALUE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ANCHOR_GUID_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.ANCHOR_GUID_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.DATA_TYPE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.DEFAULT_VALUE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.FIXED_VALUE_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.QUERY_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.QUERY_ID_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.VERSION_NUMBER_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ID_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.CREATED_TIME_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.LAST_MODIFIED_TIME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.LAST_MODIFIER_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.AUTHOR_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ENCODING_STANDARD_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.NAMESPACE_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.POSITION_PROPERTY_NAME,
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
                                                 OpenMetadataTypesMapper.POSITION_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.MIN_CARDINALITY_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.MAX_CARDINALITY_PROPERTY_NAME,
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
                                                        OpenMetadataTypesMapper.ALLOWS_DUPLICATES_PROPERTY_NAME,
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
                                                        OpenMetadataTypesMapper.ORDERED_VALUES_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.DEFAULT_VALUE_OVERRIDE_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.MIN_LENGTH_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.LENGTH_PROPERTY_NAME,
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
                                                                  OpenMetadataTypesMapper.PRECISION_PROPERTY_NAME,
                                                                  elementProperties,
                                                                  methodName);

            if (retrievedValue == 0)
            {
                retrievedValue = propertyHelper.removeIntProperty(serviceName,
                                                                  OpenMetadataTypesMapper.SIGNIFICANT_DIGITS_PROPERTY_NAME,
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
                                                        OpenMetadataTypesMapper.IS_NULLABLE_PROPERTY_NAME,
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
                                                        OpenMetadataTypesMapper.REQUIRED_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.NATIVE_CLASS_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.ALIASES_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.GUARD_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.FORMULA_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.FORMULA_TYPE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.FORMULA_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.FORMULA_TYPE_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.IMPLEMENTATION_LANGUAGE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.IMPLEMENTATION_LANGUAGE_PROPERTY_NAME,
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
                                                        OpenMetadataTypesMapper.USES_BLOCKING_CALLS_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.SOURCE_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.USAGE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.USAGE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.LANGUAGE_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.SUMMARY_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.SUMMARY_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.PUBLISH_VERSION_ID_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.ABBREVIATION_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ABBREVIATION_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.EXAMPLES_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.TITLE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.PRONOUNS_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.TEXT_PROPERTY_NAME,
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
    protected String removePriority(ElementProperties elementProperties)
    {
        final String methodName = "removePriority";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataTypesMapper.PRIORITY_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.PRIORITY_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.HEAD_COUNT_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.SCOPE_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.IMPLICATIONS_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.OUTCOMES_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.RESULTS_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.BUSINESS_IMPERATIVES_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.JURISDICTION_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.DETAILS_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.RATIONALE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.IMPLEMENTATION_DESCRIPTION_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.CRITERIA_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.LEVEL_IDENTIFIER_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.MEASUREMENT_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.TARGET_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.CLASSIFICATION_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.CLASSIFICATION_PROPERTY_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.REQUEST_TYPE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.SERVICE_REQUEST_TYPE_PROPERTY_NAME,
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
                                                              OpenMetadataTypesMapper.REQUEST_PARAMETERS_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.EXECUTOR_ENGINE_GUID_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.EXECUTOR_ENGINE_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.PROCESS_NAME_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataTypesMapper.GOVERNANCE_ACTION_TYPE_GUID_PROPERTY_NAME,
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
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataTypesMapper.GOVERNANCE_ACTION_TYPE_NAME_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }



    /**
     * Extract and delete the producedGuards property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return array of guards
     */
    protected List<String> removeProducedGuards(ElementProperties elementProperties)

    {
        final String methodName = "removeProducedGuards";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataTypesMapper.PRODUCED_GUARDS_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.GUARD_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.MANDATORY_GUARDS_PROPERTY_NAME,
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
                                                        OpenMetadataTypesMapper.MANDATORY_GUARD_PROPERTY_NAME,
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
                                                        OpenMetadataTypesMapper.IGNORE_MULTIPLE_TRIGGERS_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.WAIT_TIME_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.RECEIVED_GUARDS_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.COMPLETION_GUARDS_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.COMPLETION_MESSAGE_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.START_DATE_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.PLANNED_END_DATE_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.CREATION_TIME_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.DUE_TIME_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.COMPLETION_TIME_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.COMPLETION_DATE_PROPERTY_NAME,
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
    protected String removeStatus(ElementProperties elementProperties)

    {
        final String methodName = "removeStatus";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataTypesMapper.STATUS_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.REQUEST_SOURCE_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ACTION_TARGET_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ORIGIN_GOVERNANCE_SERVICE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ORIGIN_GOVERNANCE_ENGINE_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.LICENSE_GUID_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.CERTIFICATE_GUID_PROPERTY_NAME,
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
                                                  OpenMetadataTypesMapper.START_PROPERTY_NAME,
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
                                                  OpenMetadataTypesMapper.END_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.CONDITIONS_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.CUSTODIAN_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.CERTIFIED_BY_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.RECIPIENT_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.LICENSED_BY_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.LICENSEE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.PREFERRED_VALUE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.CATEGORY_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.IS_CASE_SENSITIVE_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.IS_STRICT_REQUIREMENT_PROPERTY_NAME,
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
                                                 OpenMetadataTypesMapper.CONFIDENCE_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.STEWARD_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.STEWARD_TYPE_NAME_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.STEWARD_PROPERTY_NAME_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.NOTES_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.ATTRIBUTE_NAME_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.POINT_TYPE_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.ASSOCIATION_DESCRIPTION_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.SYMBOLIC_NAME_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.IMPLEMENTATION_VALUE_PROPERTY_NAME,
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
                                                           OpenMetadataTypesMapper.ADDITIONAL_VALUES_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.COMMENT_TEXT_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.IS_PUBLIC_PROPERTY_NAME,
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
                                                        OpenMetadataTypesMapper.IS_PUBLIC_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.REVIEW_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.TAG_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.TAG_DESCRIPTION_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.EXECUTION_DATE_PROPERTY_NAME,
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
                                                              OpenMetadataTypesMapper.ANALYSIS_PARAMS_PROPERTY_NAME,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract the analysis step property from the supplied element properties.
     *
     * @param elementProperties properties from discovery analysis report entities
     * @return string property or null
     */
    protected String removeAnalysisStep(ElementProperties elementProperties)
    {
        final String methodName = "removeAnalysisStep";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataTypesMapper.ANALYSIS_STEP_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ANNOTATION_TYPE_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.CONFIDENCE_LEVEL_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.CONFIDENCE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ANNOTATION_TYPE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ATTRIBUTE_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.EXPLANATION_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.JSON_PROPERTIES_PROPERTY_NAME,
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
                                                     OpenMetadataTypesMapper.REVIEW_DATE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.STEWARD_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.STEWARD_TYPE_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.STEWARD_PROPERTY_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.NOTES_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.COMMENT_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.SCHEMA_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.SCHEMA_TYPE_PROPERTY_NAME,
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
                                                              OpenMetadataTypesMapper.CANDIDATE_CLASSIFICATIONS_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.CANDIDATE_DATA_CLASS_GUIDS_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.INFERRED_DATA_TYPE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.INFERRED_FORMAT_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.INFERRED_LENGTH_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.INFERRED_PRECISION_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.INFERRED_SCALE_PROPERTY_NAME,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
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
                                                              OpenMetadataTypesMapper.PROFILE_PROPERTIES_PROPERTY_NAME,
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
                                                               OpenMetadataTypesMapper.PROFILE_FLAGS_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.PROFILE_COUNTS_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.VALUE_LIST_PROPERTY_NAME,
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
                                                               OpenMetadataTypesMapper.VALUE_COUNT_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.VALUE_RANGE_FROM_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.VALUE_RANGE_TO_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.AVERAGE_VALUE_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the dataSourceProperties property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name value pairs
     */
    protected Map<String, String> removeDataSourceProperties(ElementProperties elementProperties)
    {
        final String methodName = "removeDataSourceProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(serviceName,
                                                              OpenMetadataTypesMapper.DATA_SOURCE_PROPERTIES_PROPERTY_NAME,
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
    protected int removeSize(ElementProperties elementProperties)
    {
        final String methodName = "removeSize";

        if (elementProperties != null)
        {
            return propertyHelper.removeIntProperty(serviceName,
                                                    OpenMetadataTypesMapper.DS_PHYSICAL_SIZE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ENCODING_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.PARAMETER_TYPE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.QUALITY_DIMENSION_PROPERTY_NAME,
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
                                                    OpenMetadataTypesMapper.QUALITY_SCORE_PROPERTY_NAME,
                                                    elementProperties,
                                                    methodName);
        }

        return 0;
    }



    /**
     * Extract and delete the duplicateAnchorGUID standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDuplicateAnchorGUID(ElementProperties  elementProperties)
    {
        final String methodName = "removeDuplicateAnchorGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataTypesMapper.DUPLICATE_ANCHOR_GUID_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the divergentPropertyNames property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of values
     */
    protected List<String> removeDivergentPropertyNames(ElementProperties elementProperties)
    {
        final String methodName = "removeDivergentPropertyNames";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataTypesMapper.DIVERGENT_PROPERTY_NAMES_PROPERTY_NAME,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract and delete the divergentClassificationName standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDivergentClassificationName(ElementProperties  elementProperties)
    {
        final String methodName = "removeDivergentClassificationName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataTypesMapper.DIVERGENT_CLASSIFICATION_NAME_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the divergentClassificationPropertyNames property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of values
     */
    protected List<String> removeDivergentClassificationPropertyNames(ElementProperties elementProperties)
    {
        final String methodName = "removeDivergentClassificationPropertyNames";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataTypesMapper.DIVERGENT_CLASSIFICATION_PROPERTY_NAMES_PROPERTY_NAME,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }



    /**
     * Extract and delete the divergentRelatedMetadataElementsGUID standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDivergentRelatedMetadataElementsGUID(ElementProperties  elementProperties)
    {
        final String methodName = "removeDivergentRelatedMetadataElementsGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataTypesMapper.DIVERGENT_RELATIONSHIP_GUID_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract the divergentRelatedMetadataElementsPropertyNames property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return list of values
     */
    protected List<String> removeDivergentRelatedMetadataElementsPropertyNames(ElementProperties elementProperties)
    {
        final String methodName = "removeDivergentRelatedMetadataElementsPropertyNames";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataTypesMapper.DIVERGENT_RELATIONSHIP_PROPERTY_NAMES_PROPERTY_NAME,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
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
                                                       OpenMetadataTypesMapper.ATTACHMENT_GUID_PROPERTY_NAME,
                                                       elementProperties,
                                                       methodName);
        }

        return null;
    }


    /**
     * Extract and delete the duplicateAttachmentGUID standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDuplicateAttachmentGUID(ElementProperties  elementProperties)
    {
        final String methodName = "removeDuplicateAttachmentGUID";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataTypesMapper.DUPLICATE_ATTACHMENT_GUID_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.RELATED_ENTITY_GUID_PROPERTY_NAME,
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
    protected String removeRelatedMetadataElementsTypeName(ElementProperties  elementProperties)
    {
        final String methodName = "removeRelatedMetadataElementsTypeName";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataTypesMapper.RELATIONSHIP_TYPE_NAME_PROPERTY_NAME,
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
    protected Map<String, String> removeRelatedMetadataElementsProperties(ElementProperties elementProperties)
    {
        final String methodName = "removeRelatedMetadataElementsProperties";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringMapFromProperty(serviceName,
                                                              OpenMetadataTypesMapper.RELATIONSHIP_PROPERTIES_PROPERTY_NAME,
                                                              elementProperties,
                                                              methodName);
        }

        return null;
    }


    /**
     * Extract and delete the discoveryActivity standing property from the supplied element properties.
     *
     * @param elementProperties properties from element
     * @return string text or null
     */
    protected String removeDiscoveryActivity(ElementProperties  elementProperties)
    {
        final String methodName = "removeDiscoveryActivity";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringProperty(serviceName,
                                                       OpenMetadataTypesMapper.DISCOVERY_ACTIVITY_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.ACTION_REQUESTED_PROPERTY_NAME,
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
                                                              OpenMetadataTypesMapper.ACTION_PROPERTIES_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.INFORMAL_TERM_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.CANDIDATE_GLOSSARY_TERM_GUIDS_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.INFORMAL_TOPIC_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.CANDIDATE_GLOSSARY_CATEGORY_GUIDS_PROPERTY_NAME,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the duplicateAnchorGUIDs property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected List<String> removeDuplicateAnchorGUIDs(ElementProperties elementProperties)
    {
        final String methodName = "removeDuplicateAnchorGUIDs";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataTypesMapper.DUPLICATE_ANCHOR_GUIDS_PROPERTY_NAME,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the matchingPropertyNames property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected List<String> removeMatchingPropertyNames(ElementProperties elementProperties)
    {
        final String methodName = "removeMatchingPropertyNames";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataTypesMapper.MATCHING_PROPERTY_NAMES_PROPERTY_NAME,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the matchingClassificationNames property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected List<String> removeMatchingClassificationNames(ElementProperties elementProperties)
    {
        final String methodName = "removeMatchingClassificationNames";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataTypesMapper.MATCHING_CLASSIFICATION_NAMES_PROPERTY_NAME,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the matchingAttachmentGUIDs property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected List<String> removeMatchingAttachmentGUIDs(ElementProperties elementProperties)
    {
        final String methodName = "removeMatchingAttachmentGUIDs";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataTypesMapper.MATCHING_ATTACHMENT_GUIDS_PROPERTY_NAME,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }


    /**
     * Extract the matchingRelatedMetadataElementsGUIDs property from the supplied element properties.
     *
     * @param elementProperties properties from annotation entities
     * @return map of name-value pairs
     */
    protected List<String> removeMatchingRelatedMetadataElementsGUIDs(ElementProperties elementProperties)
    {
        final String methodName = "removeMatchingRelatedMetadataElementsGUIDs";

        if (elementProperties != null)
        {
            return propertyHelper.removeStringArrayProperty(serviceName,
                                                            OpenMetadataTypesMapper.MATCHING_RELATIONSHIP_GUIDS_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.DATA_FIELD_NAME_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.DATA_FIELD_TYPE_PROPERTY_NAME,
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
                                                       OpenMetadataTypesMapper.DATA_FIELD_DESCRIPTION_PROPERTY_NAME,
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
                                                            OpenMetadataTypesMapper.DATA_FIELD_ALIASES_PROPERTY_NAME,
                                                            elementProperties,
                                                            methodName);
        }

        return null;
    }

}

