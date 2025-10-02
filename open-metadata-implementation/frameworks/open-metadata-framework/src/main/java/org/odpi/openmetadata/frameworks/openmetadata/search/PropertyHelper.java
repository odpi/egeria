/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import org.odpi.openmetadata.frameworks.openmetadata.converters.OpenMetadataPropertyConverterBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFRuntimeException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;

/**
 * PropertyHelper is used by the governance actions services to manage the contents of the ElementProperties structure.
 * It is a stateless object and so there are no threading concerns with declaring it as a static variable.
 */
public class PropertyHelper
{
    private static final String serviceName = "Open Metadata Framework (OMF)";
    private final OpenMetadataPropertyConverterBase propertyConverter = new OpenMetadataPropertyConverterBase(this,
                                                                                                              serviceName);


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId      user name to validate
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the userId is null
     */
    public void validateUserId(String userId,
                               String methodName) throws InvalidParameterException
    {
        if ((userId == null) || (userId.isEmpty()))
        {
            final String parameterName = "userId";

            throw new InvalidParameterException(OMFErrorCode.NULL_USER_ID.getMessageDefinition(methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * Throw an exception if the supplied GUID is null
     *
     * @param guid          unique identifier to validate
     * @param guidParameter name of the parameter that passed the guid.
     * @param methodName    name of the method making the call.
     *
     * @throws InvalidParameterException the guid is null
     */
    public void validateGUID(String guid,
                             String guidParameter,
                             String methodName) throws InvalidParameterException
    {
        if ((guid == null) || (guid.isEmpty()))
        {
            throw new InvalidParameterException(OMFErrorCode.NULL_GUID.getMessageDefinition(guidParameter, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                guidParameter);
        }
    }


    /**
     * Throw an exception if the supplied name is null
     *
     * @param name          unique name to validate
     * @param nameParameter name of the parameter that passed the name.
     * @param methodName    name of the method making the call.
     *
     * @throws InvalidParameterException the name is null
     */
    public void validateMandatoryName(String name,
                                      String nameParameter,
                                      String methodName) throws InvalidParameterException
    {
        if ((name == null) || (name.isEmpty()))
        {
            throw new InvalidParameterException(OMFErrorCode.NULL_NAME.getMessageDefinition(nameParameter, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameter);
        }
    }


    /**
     * Throw an exception if the supplied text field is null
     *
     * @param text  unique name to validate
     * @param parameterName  name of the parameter that passed the name.
     * @param methodName  name of the method making the call.
     *
     * @throws InvalidParameterException the text is null
     */
    public void validateText(String text,
                             String parameterName,
                             String methodName) throws InvalidParameterException
    {
        if (text == null)
        {
            throw new InvalidParameterException(OMFErrorCode.NULL_TEXT.getMessageDefinition(parameterName, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }



    /**
     * Throw an exception if the supplied search string is null
     *
     * @param searchString   searchString to validate
     * @param searchParameter  name of the parameter that passed the searchString.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the searchString is null
     */
    public void validateSearchString(String searchString,
                                     String searchParameter,
                                     String methodName) throws InvalidParameterException
    {
        if ((searchString == null) || (searchString.isEmpty()))
        {
            throw new InvalidParameterException(OMFErrorCode.NULL_SEARCH_STRING.getMessageDefinition(searchParameter,
                                                                                                            methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                searchParameter);
        }
        else
        {
            /*
             * This test just validated that the regular expression in the search parameter is valid.
             */
            final String testString = "abcdefghijklmnopqrstuvwxyz";

            try
            {
                testString.matches(searchString);
            }
            catch (Exception error)
            {
                throw new InvalidParameterException(OMFErrorCode.INVALID_SEARCH_STRING.getMessageDefinition(searchParameter,
                                                                                                                   methodName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    searchParameter);
            }
        }
    }

    /**
     * Throw an exception if the supplied paging values don't make sense. If page size is zero it means return as much as there is.
     * in which case this method sets the page size to the maximum value for this server.  If the server has its max page size set to zero
     * then zero is used.
     *
     * @param queryOptions options to control the query
     * @param maxPagingSize maximum page size allowed by the server
     * @param methodName  name of the method making the call.
     * @return validated page size.
     * @throws InvalidParameterException the paging options are incorrect
     */
    public int  validatePaging(QueryOptions queryOptions,
                               int          maxPagingSize,
                               String       methodName) throws InvalidParameterException
    {
        int startFrom = 0;
        int pageSize = 0;

        if (queryOptions != null)
        {
            startFrom = queryOptions.getStartFrom();
            pageSize = queryOptions.getPageSize();
        }

        return this.validatePaging(startFrom, pageSize, maxPagingSize, methodName);
    }


    /**
     * Throw an exception if the supplied paging values don't make sense. If page size is zero it means return as much as there is.
     * in which case this method sets the page size to the maximum value for this server.  If the server has its max page size set to zero
     * then zero is used.
     *
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param maxPagingSize maximum page size allowed by the server
     * @param methodName  name of the method making the call.
     * @return validated page size.
     * @throws InvalidParameterException the paging options are incorrect
     */
    public int  validatePaging(int    startFrom,
                               int    pageSize,
                               int    maxPagingSize,
                               String methodName) throws InvalidParameterException
    {
        final  String   startFromParameterName = "startFrom";
        final  String   pageSizeParameterName  = "pageSize";

        if (startFrom < 0)
        {
            throw new InvalidParameterException(OMFErrorCode.NEGATIVE_START_FROM.getMessageDefinition(Integer.toString(startFrom),
                                                                                                             startFromParameterName,
                                                                                                             methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                startFromParameterName);
        }


        if (pageSize < 0)
        {
            throw new InvalidParameterException(OMFErrorCode.NEGATIVE_PAGE_SIZE.getMessageDefinition(Integer.toString(pageSize),
                                                                                                            pageSizeParameterName,
                                                                                                            methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                pageSizeParameterName);
        }

        if ((maxPagingSize != 0) && (pageSize > maxPagingSize))
        {
            throw new InvalidParameterException(OMFErrorCode.MAX_PAGE_SIZE.getMessageDefinition(Integer.toString(pageSize),
                                                                                                       pageSizeParameterName,
                                                                                                       methodName,
                                                                                                       Integer.toString(maxPagingSize)),
                                                this.getClass().getName(),
                                                methodName,
                                                pageSizeParameterName);
        }

        if (pageSize == 0)
        {
            return maxPagingSize;
        }
        else
        {
            return pageSize;
        }
    }


    /**
     * Throw an exception if the supplied object is null
     *
     * @param object         object to validate
     * @param nameParameter  name of the parameter that passed the object.
     * @param methodName     name of the method making the call.
     *
     * @throws InvalidParameterException the object is null
     */
    public void validateObject(Object object,
                               String nameParameter,
                               String methodName) throws InvalidParameterException
    {
        if (object == null)
        {
            throw new InvalidParameterException(OMFErrorCode.NULL_OBJECT.getMessageDefinition(nameParameter, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameter);
        }
    }


    /**
     * Test the type of the element to determine if it matches the desired type.  This method works for all categories of elements,
     * ie entities, relationships and classifications.
     *
     * @param elementControlHeader header of the element to test
     * @param expectedType         expected type - null means any type
     *
     * @return boolean result
     */
    public boolean isTypeOf(ElementControlHeader elementControlHeader,
                            String               expectedType)
    {
        if (expectedType == null)
        {
            return true;
        }

        if ((elementControlHeader != null) && (elementControlHeader.getType() != null))
        {
            List<String> typeList;

            if (elementControlHeader.getType().getSuperTypeNames() == null)
            {
                typeList = new ArrayList<>();
            }
            else
            {
                typeList = new ArrayList<>(elementControlHeader.getType().getSuperTypeNames());
            }

            typeList.add(elementControlHeader.getType().getTypeName());

            return typeList.contains(expectedType);
        }

        return false;
    }


    /**
     * Test the type of the element to determine if it matches any of the desired types.
     * This method works for all categories of elements,
     * ie entities, relationships and classifications.
     *
     * @param elementControlHeader header of the element to test
     * @param expectedTypes         expected type - nul means any type
     *
     * @return boolean result
     */
    public boolean isTypeOf(ElementControlHeader elementControlHeader,
                            List<String>         expectedTypes)
    {
        if (expectedTypes == null)
        {
            return true;
        }

        for (String typeName : expectedTypes)
        {
            if (this.isTypeOf(elementControlHeader, typeName))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Extract the domain name from an element's header.
     *
     * @param elementControlHeader header of an open metadata element
     * @return string type name
     */
    public String getDomainName(ElementControlHeader elementControlHeader)
    {
        String domainName = elementControlHeader.getType().getTypeName();

        if (elementControlHeader.getType().getSuperTypeNames() != null)
        {
            /*
             * The super types are listed in increasing levels of abstraction.
             * Eg [DataSet, Asset, Referenceable, OpenMetadataRoot].
             * In this example, the domain is Asset (one below Referenceable).
             */
            for (String superTypeName : elementControlHeader.getType().getSuperTypeNames())
            {
                if ((! superTypeName.equals(OpenMetadataType.OPEN_METADATA_ROOT.typeName)) &&
                    (! superTypeName.equals(OpenMetadataType.REFERENCEABLE.typeName)))
                {
                    domainName = superTypeName;
                }
            }
        }

        return domainName;
    }


    /**
     * Return the anchorGUID for an element.
     *
     * @param elementHeader header of the element
     * @return unique identifier or null;
     */
    public String getAnchorGUID(ElementHeader elementHeader)
    {
        ElementClassification classification = elementHeader.getAnchor();
        if ((classification != null) && (classification.getClassificationProperties() instanceof AnchorsProperties anchorsProperties))
        {
            return anchorsProperties.getAnchorGUID();
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
                    beanClassifications.add(getElementClassification(attachedClassification));
                }
            }
        }

        return beanClassifications;
    }


    /**
     * Return an element classification from an attached classification.  The difference is that the
     * element classification converts the properties to a name-value pair map.
     *
     * @param attachedClassification attached classification from OMF OpenMetadataStore
     * @return element classification for the beans
     */
    public ElementClassification getElementClassification(AttachedClassification attachedClassification)
    {
        if (attachedClassification != null)
        {
            ElementClassification beanClassification = new ElementClassification(attachedClassification);

            beanClassification.setClassificationName(attachedClassification.getClassificationName());
            beanClassification.setClassificationProperties(propertyConverter.getClassificationProperties(attachedClassification));

            return beanClassification;
        }

        return null;
    }


    /**
     * Extract the properties from the element.
     *
     * @param element element containing the properties
     * @return filled out element header
     */
    public ElementHeader getElementHeader(OpenMetadataElement element)
    {
        if (element != null)
        {
            return getElementHeader(element,
                                    element.getElementGUID(),
                                    element.getClassifications());
        }


        return null;
    }


    /**
     * Fill the classification properties in the header for an element.
     *
     * @param elementHeader header to fill in
     * @param classifications classifications
     */
    public void addClassificationsToElementHeader(ElementHeader                elementHeader,
                                                  List<AttachedClassification> classifications)
    {
        if (classifications != null)
        {
            List<ElementClassification> executionPoints            = new ArrayList<>();
            List<ElementClassification> duplicateClassifications   = new ArrayList<>();
            List<ElementClassification> resourceManagersCategories = new ArrayList<>();
            List<ElementClassification> serverPurposes             = new ArrayList<>();
            List<ElementClassification> collectionRoles            = new ArrayList<>();
            List<ElementClassification> projectCategories          = new ArrayList<>();
            List<ElementClassification> otherClassifications       = new ArrayList<>();

            for (AttachedClassification attachedClassification : classifications)
            {
                if (attachedClassification != null)
                {
                    if (this.isTypeOf(attachedClassification, OpenMetadataType.ANCHORS_CLASSIFICATION.typeName))
                    {
                        elementHeader.setAnchor(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName))
                    {
                        elementHeader.setZoneMembership(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.SUBJECT_AREA_CLASSIFICATION.typeName))
                    {
                        elementHeader.setSubjectArea(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.IMPACT_CLASSIFICATION.typeName))
                    {
                        elementHeader.setImpact(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName))
                    {
                        elementHeader.setCriticality(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName))
                    {
                        elementHeader.setConfidentiality(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName))
                    {
                        elementHeader.setConfidence(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.RETENTION_CLASSIFICATION.typeName))
                    {
                        elementHeader.setRetention(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.GOVERNANCE_EXPECTATIONS_CLASSIFICATION.typeName))
                    {
                        elementHeader.setGovernanceExpectations(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION.typeName))
                    {
                        elementHeader.setGovernanceMeasurements(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName))
                    {
                        duplicateClassifications.add(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.CONSOLIDATED_DUPLICATE_CLASSIFICATION.typeName))
                    {
                        duplicateClassifications.add(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.CONTROL_POINT_CLASSIFICATION.typeName))
                    {
                        executionPoints.add(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.VERIFICATION_POINT_CLASSIFICATION.typeName))
                    {
                        executionPoints.add(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.ENFORCEMENT_POINT_CLASSIFICATION.typeName))
                    {
                        executionPoints.add(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName))
                    {
                        elementHeader.setDigitalResourceOrigin(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName))
                    {
                        elementHeader.setOwnership(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.MEMENTO_CLASSIFICATION.typeName))
                    {
                        elementHeader.setMemento(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName))
                    {
                        elementHeader.setTemplate(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName))
                    {
                        elementHeader.setSchemaType(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.DATA_SCOPE_CLASSIFICATION.typeName))
                    {
                        elementHeader.setDataScope(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION.typeName))
                    {
                        elementHeader.setCalculatedValue(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.PRIMARY_KEY_CLASSIFICATION.typeName))
                    {
                        elementHeader.setPrimaryKey(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.RESOURCE_MANAGER_CLASSIFICATION.typeName))
                    {
                        resourceManagersCategories.add(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.SERVER_PURPOSE_CLASSIFICATION.typeName))
                    {
                        serverPurposes.add(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.COLLECTION_ROLE_CLASSIFICATION.typeName))
                    {
                        collectionRoles.add(this.getElementClassification(attachedClassification));
                    }
                    else if (this.isTypeOf(attachedClassification, OpenMetadataType.PROJECT_ROLE_CLASSIFICATION.typeName))
                    {
                        projectCategories.add(this.getElementClassification(attachedClassification));
                    }
                    else
                    {
                        otherClassifications.add(this.getElementClassification(attachedClassification));
                    }
                }
            }

            if (! duplicateClassifications.isEmpty())
            {
                elementHeader.setDuplicateClassifications(duplicateClassifications);
            }

            if (! executionPoints.isEmpty())
            {
                elementHeader.setExecutionPoints(executionPoints);
            }

            if (! resourceManagersCategories.isEmpty())
            {
                elementHeader.setResourceManagerRoles(resourceManagersCategories);
            }

            if (! serverPurposes.isEmpty())
            {
                elementHeader.setServerPurposes(serverPurposes);
            }

            if (! collectionRoles.isEmpty())
            {
                elementHeader.setCollectionRoles(collectionRoles);
            }

            if (! projectCategories.isEmpty())
            {
                elementHeader.setProjectRoles(projectCategories);
            }

            if (! otherClassifications.isEmpty())
            {
                elementHeader.setOtherClassifications(otherClassifications);
            }
        }
    }


    /**
     * Extract the properties from the element.
     *
     * @param header header from the element containing the properties
     * @param elementGUID unique identifier of the element
     * @param classifications classification if this is an element
     * @return filled out element header
     */
    public ElementHeader getElementHeader(ElementControlHeader         header,
                                          String                       elementGUID,
                                          List<AttachedClassification> classifications)
    {
        if (header != null)
        {
            ElementHeader elementHeader = new ElementHeader(header);

            elementHeader.setGUID(elementGUID);
            this.addClassificationsToElementHeader(elementHeader, classifications);

            return elementHeader;
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
            RelatedMetadataElementSummary relatedElementSummary = new RelatedMetadataElementSummary();
            MetadataElementSummary        elementSummary        = new MetadataElementSummary();
            ElementHeader                 elementHeader         = new ElementHeader(relatedElement);

            elementHeader.setGUID(relatedElement.getRelationshipGUID());

            relatedElementSummary.setRelationshipHeader(elementHeader);
            if (relatedElement.getRelationshipProperties() != null)
            {
                relatedElementSummary.setRelationshipProperties(propertyConverter.getRelationshipProperties(relatedElement));
            }

            elementSummary.setElementHeader(this.getElementHeader(relatedElement.getElement()));
            if (relatedElement.getElement().getElementProperties() != null)
            {
                elementSummary.setProperties(propertyConverter.getBeanProperties(relatedElement.getElement()));
            }

            relatedElementSummary.setRelatedElement(elementSummary);
            relatedElementSummary.setEffectiveFromTime(relatedElement.getEffectiveFromTime());
            relatedElementSummary.setEffectiveToTime(relatedElement.getEffectiveToTime());
            relatedElementSummary.setRelatedElementAtEnd1(relatedElement.getElementAtEnd1());

            return relatedElementSummary;
        }

        return null;
    }


    /**
     * Extract the properties from the list of elements.
     *
     * @param startingElementGUID guid of starting element
     * @param relatedElements from the repository
     * @return filled out element header
     */
    public List<RelatedMetadataNodeSummary> getRelatedNodeSummaries(String                       startingElementGUID,
                                                                    List<RelatedMetadataElement> relatedElements)
    {
        if (relatedElements != null)
        {
            List<RelatedMetadataNodeSummary> nodeSummaries = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedElements)
            {
                if (relatedMetadataElement != null)
                {
                    nodeSummaries.add(this.getRelatedNodeSummary(startingElementGUID, relatedMetadataElement));
                }
            }

            return nodeSummaries;
        }

        return null;
    }


    /**
     * Extract the properties from the element.
     *
     * @param startingElementGUID guid of starting element
     * @param relatedElement from the repository
     * @return filled out element header
     */
    public RelatedMetadataNodeSummary getRelatedNodeSummary(String                 startingElementGUID,
                                                            RelatedMetadataElement relatedElement)
    {
        if ((relatedElement != null) && (relatedElement.getElement() != null))
        {
            RelatedMetadataNodeSummary    relatedMetadataNodeSummary = new RelatedMetadataNodeSummary();
            MetadataElementSummary        elementSummary             = new MetadataElementSummary();

            relatedMetadataNodeSummary.setStartingElementGUID(startingElementGUID);

            relatedMetadataNodeSummary.setRelationshipHeader(this.getElementHeader(relatedElement, relatedElement.getRelationshipGUID(), null));
            if (relatedElement.getRelationshipProperties() != null)
            {
                relatedMetadataNodeSummary.setRelationshipProperties(propertyConverter.getRelationshipProperties(relatedElement));
            }

            elementSummary.setElementHeader(this.getElementHeader(relatedElement.getElement()));
            if (relatedElement.getElement().getElementProperties() != null)
            {
                elementSummary.setProperties(propertyConverter.getBeanProperties(relatedElement.getElement()));
            }

            relatedMetadataNodeSummary.setRelatedElement(elementSummary);
            relatedMetadataNodeSummary.setEffectiveFromTime(relatedElement.getEffectiveFromTime());
            relatedMetadataNodeSummary.setEffectiveToTime(relatedElement.getEffectiveToTime());
            relatedMetadataNodeSummary.setRelatedElementAtEnd1(relatedElement.getElementAtEnd1());

            return relatedMetadataNodeSummary;
        }

        return null;
    }


    /**
     * Convert a list of open metadata elements from OMF into a summary object.
     *
     * @param openMetadataElements list
     * @return list
     */
    public List<MetadataElementSummary> getMetadataElementSummaries(List<OpenMetadataElement> openMetadataElements)
    {
        if (openMetadataElements != null)
        {
            List<MetadataElementSummary> metadataElementSummaries = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    metadataElementSummaries.add(this.getMetadataElementSummary(openMetadataElement));
                }
            }

            return metadataElementSummaries;
        }

        return null;
    }

    /**
     * Extract the properties from the element.
     *
     * @param openMetadataElement from the repository
     * @return filled out element header
     */
    public MetadataElementSummary getMetadataElementSummary(OpenMetadataElement openMetadataElement)
    {
        if (openMetadataElement != null)
        {
            MetadataElementSummary elementSummary = new MetadataElementSummary();

            elementSummary.setElementHeader(this.getElementHeader(openMetadataElement));
            elementSummary.setProperties(propertyConverter.getBeanProperties(openMetadataElement));

            return elementSummary;
        }

        return null;
    }


    /**
     * Return the search properties that requests elements with an exactly matching name in any of the listed property names.
     *
     * @param propertyNames list of property names
     * @param value name to match on
     * @param propertyComparisonOperator set to EQ for exact match and LIKE for fuzzy match
     * @return search properties
     */
    public SearchProperties getSearchPropertiesByName(List<String>               propertyNames,
                                                      String                     value,
                                                      PropertyComparisonOperator propertyComparisonOperator)
    {
        if ((propertyNames != null) && (! propertyNames.isEmpty()))
        {
            SearchProperties searchProperties = new SearchProperties();

            PrimitiveTypePropertyValue propertyValue = new PrimitiveTypePropertyValue();
            propertyValue.setTypeName("string");
            propertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);

            if (PropertyComparisonOperator.LIKE.equals(propertyComparisonOperator))
            {
                if ("*".equals(value) || ".*".equals(value))
                {
                    propertyValue.setPrimitiveValue(".*");
                }
                else
                {
                    propertyValue.setPrimitiveValue(".*" + Pattern.quote(value) + ".*");
                }
            }
            else
            {
                propertyValue.setPrimitiveValue(value);
            }

            List<PropertyCondition> propertyConditions = new ArrayList<>();

            for (String propertyName : propertyNames)
            {
                PropertyCondition propertyCondition = new PropertyCondition();

                propertyCondition.setValue(propertyValue);
                propertyCondition.setProperty(propertyName);

                if (propertyComparisonOperator == null)
                {
                    propertyCondition.setOperator(PropertyComparisonOperator.EQ);
                }
                else
                {
                    propertyCondition.setOperator(propertyComparisonOperator);
                }

                propertyConditions.add(propertyCondition);
            }

            searchProperties.setConditions(propertyConditions);
            searchProperties.setMatchCriteria(MatchCriteria.ANY);

            return searchProperties;
        }

        return null;
    }


    /**
     * Build out the search classification to request entities that have the specified classification name.
     * If the classification name is null then no specific classification search properties are used.
     *
     * @param classificationName name of the classification to search for
     *
     * @return search classifications
     */
    public SearchClassifications getSearchClassifications(String classificationName)
    {
        SearchClassifications searchClassifications = null;

        if (classificationName != null)
        {
            searchClassifications = new SearchClassifications();

            List<ClassificationCondition> classificationConditions = new ArrayList<>();
            ClassificationCondition       classificationCondition  = new ClassificationCondition();

            classificationCondition.setName(classificationName);

            classificationConditions.add(classificationCondition);

            searchClassifications.setConditions(classificationConditions);
            searchClassifications.setMatchCriteria(MatchCriteria.ALL);
        }

        return searchClassifications;
    }


    /**
     * Add a new property comparison for another property.
     *
     * @param existingSearchConditions current list
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param operator comparison operator
     * @param propertyCategory category of the primitive
     * @return list of property conditions (maybe null)
     */
    public List<PropertyCondition> addPrimitivePropertyToSearchCondition(List<PropertyCondition>    existingSearchConditions,
                                                                         String                     propertyName,
                                                                         Object                     propertyValue,
                                                                         PropertyComparisonOperator operator,
                                                                         PrimitiveTypeCategory      propertyCategory)
    {
        List<PropertyCondition> searchConditions = existingSearchConditions;

        if (propertyValue != null)
        {
            if (searchConditions == null)
            {
                searchConditions = new ArrayList<>();
            }

            PropertyCondition propertyCondition = new PropertyCondition();

            propertyCondition.setValue(this.getPrimitivePropertyValue(propertyCategory, propertyValue));
            propertyCondition.setOperator(operator);
            propertyCondition.setProperty(propertyName);

            searchConditions.add(propertyCondition);
        }

        return searchConditions;
    }


    /**
     * Add the supplied primitive property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param propertyCategory category of the property
     * @return element properties object.
     */
    private ElementProperties addPrimitivePropertyToInstance(ElementProperties     properties,
                                                             String                propertyName,
                                                             Object                propertyValue,
                                                             PrimitiveTypeCategory propertyCategory)
    {
        ElementProperties  resultingProperties;

        if (propertyValue != null)
        {
            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            resultingProperties.setProperty(propertyName, this.getPrimitivePropertyValue(propertyCategory, propertyValue));

            return resultingProperties;
        }
        else
        {
            return properties;
        }
    }


    /**
     * Create a primitive property value.
     *
     * @param primitiveTypeCategory category of the property
     * @param propertyValue value of the property
     * @return property value
     */
    public PrimitiveTypePropertyValue getPrimitivePropertyValue(PrimitiveTypeCategory primitiveTypeCategory,
                                                                Object                propertyValue)
    {
        PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();

        primitiveTypePropertyValue.setPrimitiveTypeCategory(primitiveTypeCategory);
        primitiveTypePropertyValue.setPrimitiveValue(propertyValue);
        primitiveTypePropertyValue.setTypeName(primitiveTypeCategory.getName());

        return primitiveTypePropertyValue;
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param propertyValue value of property
     * @return element properties object.
     */
    public ElementProperties addStringProperty(ElementProperties properties,
                                               String            propertyName,
                                               String            propertyValue)
    {
        return addPrimitivePropertyToInstance(properties, propertyName, propertyValue, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
    }


    /**
     * Add a string property to a set of search conditions.
     *
     * @param existingSearchConditions search conditions
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param operator type of operator
     * @return new list
     */
    public List<PropertyCondition> addStringProperty(List<PropertyCondition>    existingSearchConditions,
                                                     String                     propertyName,
                                                     String                     propertyValue,
                                                     PropertyComparisonOperator operator)
    {
        return addPrimitivePropertyToSearchCondition(existingSearchConditions, propertyName, propertyValue, operator, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param propertyValue value of property
     * @return resulting element properties object
     */
    public ElementProperties addIntProperty(ElementProperties properties,
                                            String            propertyName,
                                            int               propertyValue)
    {
        return addPrimitivePropertyToInstance(properties, propertyName, propertyValue, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT);
    }


    /**
     * Add an int property to a set of search conditions.
     *
     * @param existingSearchConditions search conditions
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param operator type of operator
     * @return new list
     */
    public List<PropertyCondition> addIntProperty(List<PropertyCondition>    existingSearchConditions,
                                                  String                     propertyName,
                                                  int                        propertyValue,
                                                  PropertyComparisonOperator operator)
    {
        return addPrimitivePropertyToSearchCondition(existingSearchConditions, propertyName, propertyValue, operator, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT);
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties  properties object to add. Property may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @return resulting element properties object
     */
    public ElementProperties addLongProperty(ElementProperties properties,
                                             String            propertyName,
                                             long              propertyValue)
    {
        return addPrimitivePropertyToInstance(properties, propertyName, propertyValue, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG);
    }


    /**
     * Add a long property to a set of search conditions.
     *
     * @param existingSearchConditions search conditions
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param operator type of operator
     * @return new list
     */
    public List<PropertyCondition> addLongProperty(List<PropertyCondition>    existingSearchConditions,
                                                   String                     propertyName,
                                                   long                       propertyValue,
                                                   PropertyComparisonOperator operator)
    {
        return addPrimitivePropertyToSearchCondition(existingSearchConditions, propertyName, propertyValue, operator, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG);
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties  properties object to add. Property may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @return resulting element properties object
     */
    public ElementProperties addFloatProperty(ElementProperties properties,
                                              String            propertyName,
                                              float             propertyValue)
    {
        return addPrimitivePropertyToInstance(properties, propertyName, propertyValue, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_FLOAT);
    }


    /**
     * Add a float property to a set of search conditions.
     *
     * @param existingSearchConditions search conditions
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param operator type of operator
     * @return new list
     */
    public List<PropertyCondition> addFloatProperty(List<PropertyCondition>   existingSearchConditions,
                                                   String                     propertyName,
                                                   float                      propertyValue,
                                                   PropertyComparisonOperator operator)
    {
        return addPrimitivePropertyToSearchCondition(existingSearchConditions, propertyName, propertyValue, operator, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_FLOAT);
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties  properties object to add. Property may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @return resulting element properties object
     */
    public ElementProperties addDateProperty(ElementProperties properties,
                                             String            propertyName,
                                             Date              propertyValue)
    {
        if (propertyValue != null)
        {
            Long longValue = propertyValue.getTime();

            return addPrimitivePropertyToInstance(properties, propertyName, longValue, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE);
        }

        return properties;
    }


    /**
     * Add a date property to a set of search conditions.
     *
     * @param existingSearchConditions search conditions
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param operator type of operator
     * @return new list
     */
    public List<PropertyCondition> addDateProperty(List<PropertyCondition>    existingSearchConditions,
                                                   String                     propertyName,
                                                   Date                       propertyValue,
                                                   PropertyComparisonOperator operator)
    {
        return addPrimitivePropertyToSearchCondition(existingSearchConditions, propertyName, propertyValue, operator, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE);
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param propertyValue value of property
     * @return resulting element properties object
     */
    public ElementProperties addBooleanProperty(ElementProperties properties,
                                                String            propertyName,
                                                boolean           propertyValue)
    {
        return addPrimitivePropertyToInstance(properties, propertyName, propertyValue, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
    }


    /**
     * Add a boolean property to a set of search conditions.
     *
     * @param existingSearchConditions search conditions
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param operator type of operator
     * @return new list
     */
    public List<PropertyCondition> addBooleanProperty(List<PropertyCondition>    existingSearchConditions,
                                                      String                     propertyName,
                                                      boolean                    propertyValue,
                                                      PropertyComparisonOperator operator)
    {
        return addPrimitivePropertyToSearchCondition(existingSearchConditions, propertyName, propertyValue, operator, PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
    }


    /**
     * Add the supplied property to an element properties object.  If the element property object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param propertyType type of enum
     * @param symbolicName symbol name value of property
     * @return resulting element properties object
     */
    public ElementProperties addEnumProperty(ElementProperties properties,
                                             String            propertyName,
                                             String            propertyType,
                                             String            symbolicName)
    {
        ElementProperties  resultingProperties;

        if (properties == null)
        {
            resultingProperties = new ElementProperties();
        }
        else
        {
            resultingProperties = properties;
        }
        
        EnumTypePropertyValue enumTypePropertyValue = new EnumTypePropertyValue();

        enumTypePropertyValue.setSymbolicName(symbolicName);
        enumTypePropertyValue.setTypeName(propertyType);

        resultingProperties.setProperty(propertyName, enumTypePropertyValue);

        return resultingProperties;
    }


    /**
     * Add an enum property to a set of search conditions.
     *
     * @param existingSearchConditions search conditions
     * @param propertyName name of property
     * @param propertyType type of enum
     * @param symbolicName symbol name value of property
     * @param operator type of operator
     * @return new list
     */
    public List<PropertyCondition> addEnumProperty(List<PropertyCondition>    existingSearchConditions,
                                                   String                     propertyName,
                                                   String                     propertyType,
                                                   String                     symbolicName,
                                                   PropertyComparisonOperator operator)
    {
        List<PropertyCondition> searchConditions = existingSearchConditions;

        if (symbolicName != null)
        {
            if (searchConditions == null)
            {
                searchConditions = new ArrayList<>();
            }

            PropertyCondition propertyCondition = new PropertyCondition();

            EnumTypePropertyValue enumTypePropertyValue = new EnumTypePropertyValue();

            enumTypePropertyValue.setSymbolicName(symbolicName);
            enumTypePropertyValue.setTypeName(propertyType);

            propertyCondition.setValue(enumTypePropertyValue);
            propertyCondition.setOperator(operator);
            propertyCondition.setProperty(propertyName);

            searchConditions.add(propertyCondition);
        }

        return searchConditions;
    }


    /**
     * Add the supplied array property to an element properties object.  The supplied array is stored as a single
     * property in the instances properties.   If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param arrayValues contents of the array
     * @return resulting element properties object
     */
    public ElementProperties addStringArrayProperty(ElementProperties properties,
                                                    String            propertyName,
                                                    List<String>      arrayValues)
    {
        if ((arrayValues != null) && (! arrayValues.isEmpty()))
        {
            ElementProperties  resultingProperties;

            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            resultingProperties.setProperty(propertyName, getStringArrayPropertyValue(arrayValues));

            return resultingProperties;
        }

        return properties;
    }


    /**
     * Add a string array into a property value.
     *
     * @param arrayValues list of values
     * @return array property value
     */
    public ArrayTypePropertyValue getStringArrayPropertyValue(List<String> arrayValues)
    {
        ArrayTypePropertyValue arrayTypePropertyValue = new ArrayTypePropertyValue();
        arrayTypePropertyValue.setTypeName("array<string>");
        arrayTypePropertyValue.setArrayCount(arrayValues.size());
        int index = 0;
        for (String arrayValue : arrayValues )
        {
            arrayTypePropertyValue.setArrayValue(index, getPrimitivePropertyValue(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING, arrayValue));
            index++;
        }

        return arrayTypePropertyValue;
    }


    /**
     * Add a new property comparison for another property.
     *
     * @param existingSearchConditions current list
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param operator comparison operator
     * @return list of property conditions (maybe null)
     */
    public List<PropertyCondition> addStringArrayToSearchCondition(List<PropertyCondition>    existingSearchConditions,
                                                                   String                     propertyName,
                                                                   List<String>               propertyValue,
                                                                   PropertyComparisonOperator operator)
    {
        List<PropertyCondition> searchConditions = existingSearchConditions;

        if (propertyValue != null)
        {
            if (searchConditions == null)
            {
                searchConditions = new ArrayList<>();
            }

            PropertyCondition propertyCondition = new PropertyCondition();

            propertyCondition.setValue(this.getStringArrayPropertyValue(propertyValue));
            propertyCondition.setOperator(operator);
            propertyCondition.setProperty(propertyName);

            searchConditions.add(propertyCondition);
        }

        return searchConditions;
    }


    /**
     * Add the supplied map property to an element properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addMapProperty(ElementProperties   properties,
                                            String              propertyName,
                                            Map<String, Object> mapValues)
    {
        if (mapValues != null)
        {
            if (! mapValues.isEmpty())
            {
                ElementProperties  resultingProperties;

                if (properties == null)
                {
                    resultingProperties = new ElementProperties();
                }
                else
                {
                    resultingProperties = properties;
                }


                /*
                 * The values of a map property are stored as an embedded ElementProperties object.
                 */
                ElementProperties  mapElementProperties  = this.addPropertyMap(null, mapValues);

                /*
                 * If there was content in the map then the resulting ElementProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapElementProperties != null)
                {
                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();

                    mapTypePropertyValue.setTypeName("map<string,object>");
                    mapTypePropertyValue.setMapValues(mapElementProperties);
                    resultingProperties.setProperty(propertyName, mapTypePropertyValue);

                    return resultingProperties;
                }
            }
        }

        return properties;
    }



    /**
     * Add the supplied map property to an element properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addStringArrayStringMap(ElementProperties         properties,
                                                     String                    propertyName,
                                                     Map<String, List<String>> mapValues)
    {
        if (mapValues != null)
        {
            if (! mapValues.isEmpty())
            {
                ElementProperties  resultingProperties;

                if (properties == null)
                {
                    resultingProperties = new ElementProperties();
                }
                else
                {
                    resultingProperties = properties;
                }


                /*
                 * The values of a map property are stored as an embedded ElementProperties object.
                 */
                ElementProperties  mapElementProperties  = new ElementProperties();

                for (String mapKey : mapValues.keySet())
                {
                    List<String> mapEntry = mapValues.get(mapKey);
                    ArrayTypePropertyValue arrayPropertyValue = new ArrayTypePropertyValue();

                    arrayPropertyValue.setTypeName("array<string>");
                    arrayPropertyValue.setArrayCount(mapEntry.size());

                    int index = 0;
                    for (String arrayValue : mapEntry)
                    {
                        PrimitiveTypePropertyValue primitivePropertyValue = new PrimitiveTypePropertyValue();

                        primitivePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
                        primitivePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                        primitivePropertyValue.setPrimitiveValue(arrayValue);

                        arrayPropertyValue.setArrayValue(index, primitivePropertyValue);
                        index++;
                    }

                    mapElementProperties.setProperty(mapKey, arrayPropertyValue);
                }

                /*
                 * If there was content in the map then the resulting InstanceProperties are added as
                 * a property to the resulting properties.
                 */
                MapTypePropertyValue mapPropertyValue = new MapTypePropertyValue();

                mapPropertyValue.setMapValues(mapElementProperties);
                mapPropertyValue.setTypeName("map<string,array<string>");
                resultingProperties.setProperty(propertyName, mapPropertyValue);


                return resultingProperties;
            }
        }

        return properties;
    }


    /**
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return instance properties object.
     */
    @SuppressWarnings(value = "unchecked")
    public ElementProperties addPropertyMap(ElementProperties   properties,
                                            Map<String, Object> mapValues)
    {
        if ((mapValues != null) && (! mapValues.isEmpty()))
        {
            ElementProperties  resultingProperties;

            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            int propertyCount = 0;

            for (String mapPropertyName : mapValues.keySet())
            {
                Object mapPropertyValue = mapValues.get(mapPropertyName);

                if (mapPropertyValue instanceof String)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Integer)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Long)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Short)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_SHORT);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_SHORT.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Date)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE.getName());
                    /*
                     * Internally, dates are stored as Java Long.
                     */
                    Long timestamp = ((Date) mapPropertyValue).getTime();
                    primitiveTypePropertyValue.setPrimitiveValue(timestamp);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Character)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_CHAR);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_CHAR.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Byte)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BYTE);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BYTE.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Boolean)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Float)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_FLOAT);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_FLOAT.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof BigDecimal)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(
                            PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof BigInteger)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(
                            PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGINTEGER);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGINTEGER.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Double)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DOUBLE);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DOUBLE.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof List<?> propertyAsList)
                {
                    ArrayTypePropertyValue arrayTypePropertyValue = new ArrayTypePropertyValue();

                    String elementType = "object";

                    if (! propertyAsList.isEmpty())
                    {
                        int index = 0;

                        Map<String, Object> arrayPropertyAsMap = new HashMap<>();

                        for (Object arrayValueObject : propertyAsList)
                        {
                            if (arrayValueObject instanceof String)
                            {
                                elementType = "string";
                            }
                            else if (arrayValueObject instanceof Boolean)
                            {
                                elementType = "boolean";
                            }
                            else if (arrayValueObject instanceof Integer)
                            {
                                elementType = "int";
                            }
                            else if (arrayValueObject instanceof Long)
                            {
                                elementType = "long";
                            }
                            else if (arrayValueObject instanceof Date)
                            {
                                elementType = "date";
                            }


                            arrayPropertyAsMap.put(Integer.toString(index), arrayValueObject);
                            index++;
                        }

                        arrayTypePropertyValue.setArrayValues(addPropertyMap(null, arrayPropertyAsMap));
                        arrayTypePropertyValue.setArrayCount(index);
                    }

                    arrayTypePropertyValue.setTypeName("array<" + elementType + ">");

                    resultingProperties.setProperty(mapPropertyName, arrayTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Map)
                {
                    Map<String, Object> propertyAsMap = (Map<String, Object>)mapPropertyValue;

                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();

                    mapTypePropertyValue.setMapValues(addPropertyMap(null, propertyAsMap));
                    mapTypePropertyValue.setTypeName("map");

                    resultingProperties.setProperty(mapPropertyName, mapTypePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue != null)
                {
                    PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                    primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_UNKNOWN);
                    primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_UNKNOWN.getName());
                    primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                    propertyCount++;
                }
            }

            if (propertyCount > 0)
            {
                return resultingProperties;
            }
        }

        return properties;
    }



    /**
     * Add the supplied map property to an element properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addStringMapProperty(ElementProperties   properties,
                                                  String              propertyName,
                                                  Map<String, String> mapValues)
    {
        if (mapValues != null)
        {
            if (! mapValues.isEmpty())
            {
                ElementProperties  resultingProperties;

                if (properties == null)
                {
                    resultingProperties = new ElementProperties();
                }
                else
                {
                    resultingProperties = properties;
                }

                resultingProperties.setProperty(propertyName, getStringMapPropertyValue(mapValues));

                return resultingProperties;
            }
        }

        return properties;
    }


    /**
     * Create a MapTypePropertyValue.
     *
     * @param mapValues values for the property
     * @return property value object
     */
    public MapTypePropertyValue getStringMapPropertyValue(Map<String, String> mapValues)
    {
        /*
         * The values of a map property are stored as an embedded ElementProperties object.
         */
        ElementProperties  mapElementProperties  = this.addStringPropertyMap(null, mapValues);

        /*
         * If there was content in the map then the resulting ElementProperties are added as
         * a property to the resulting properties.
         */
        if (mapElementProperties != null)
        {
            MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
            mapTypePropertyValue.setMapValues(mapElementProperties);
            mapTypePropertyValue.setTypeName("map<string,string>");

            return mapTypePropertyValue;
        }

        return null;
    }

    /**
     * Create a MapTypePropertyValue.
     *
     * @param mapValues values for the property
     * @return property value object
     */
    public MapTypePropertyValue getObjectMapPropertyValue(Map<String, Object> mapValues)
    {
        /*
         * The values of a map property are stored as an embedded ElementProperties object.
         */
        ElementProperties  mapElementProperties  = this.addPropertyMap(null, mapValues);

        /*
         * If there was content in the map then the resulting ElementProperties are added as
         * a property to the resulting properties.
         */
        if (mapElementProperties != null)
        {
            MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
            mapTypePropertyValue.setMapValues(mapElementProperties);
            mapTypePropertyValue.setTypeName("map<string,object>");

            return mapTypePropertyValue;
        }

        return null;
    }


    /**
     * Create a MapTypePropertyValue.
     *
     * @param mapValues values for the property
     * @return property value object
     */
    public MapTypePropertyValue getListStringMapPropertyValue(Map<String, List<String>> mapValues)
    {
        /*
         * The values of a map property are stored as an embedded ElementProperties object.
         */
        ElementProperties  mapElementProperties  = this.addStringArrayPropertyMap(null, mapValues);

        /*
         * If there was content in the map then the resulting ElementProperties are added as
         * a property to the resulting properties.
         */
        if (mapElementProperties != null)
        {
            MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
            mapTypePropertyValue.setMapValues(mapElementProperties);
            mapTypePropertyValue.setTypeName("map<string,object>");

            return mapTypePropertyValue;
        }

        return null;
    }


    /**
     * Add a new property comparison for another property.
     *
     * @param existingSearchConditions current list
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param operator comparison operator
     * @return list of property conditions (maybe null)
     */
    public List<PropertyCondition> addStringMapToSearchCondition(List<PropertyCondition>    existingSearchConditions,
                                                                 String                     propertyName,
                                                                 Map<String, String>        propertyValue,
                                                                 PropertyComparisonOperator operator)
    {
        List<PropertyCondition> searchConditions = existingSearchConditions;

        if ((propertyValue != null) && (! propertyValue.isEmpty()))
        {
            if (searchConditions == null)
            {
                searchConditions = new ArrayList<>();
            }

            PropertyCondition propertyCondition = new PropertyCondition();

            propertyCondition.setValue(this.getStringMapPropertyValue(propertyValue));
            propertyCondition.setOperator(operator);
            propertyCondition.setProperty(propertyName);

            searchConditions.add(propertyCondition);
        }

        return searchConditions;
    }


    /**
     * Add a new property comparison for another property.
     *
     * @param existingSearchConditions current list
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param operator comparison operator
     * @return list of property conditions (maybe null)
     */
    public List<PropertyCondition> addListStringMapToSearchCondition(List<PropertyCondition>    existingSearchConditions,
                                                                     String                     propertyName,
                                                                     Map<String, List<String>>  propertyValue,
                                                                     PropertyComparisonOperator operator)
    {
        List<PropertyCondition> searchConditions = existingSearchConditions;

        if ((propertyValue != null) && (! propertyValue.isEmpty()))
        {
            if (searchConditions == null)
            {
                searchConditions = new ArrayList<>();
            }

            PropertyCondition propertyCondition = new PropertyCondition();

            propertyCondition.setValue(this.getListStringMapPropertyValue(propertyValue));
            propertyCondition.setOperator(operator);
            propertyCondition.setProperty(propertyName);

            searchConditions.add(propertyCondition);
        }

        return searchConditions;
    }


    /**
     * Add a new property comparison for another property.
     *
     * @param existingSearchConditions current list
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param operator comparison operator
     * @return list of property conditions (maybe null)
     */
    public List<PropertyCondition> addObjectMapToSearchCondition(List<PropertyCondition>    existingSearchConditions,
                                                                 String                     propertyName,
                                                                 Map<String, Object>        propertyValue,
                                                                 PropertyComparisonOperator operator)
    {
        List<PropertyCondition> searchConditions = existingSearchConditions;

        if ((propertyValue != null) && (! propertyValue.isEmpty()))
        {
            if (searchConditions == null)
            {
                searchConditions = new ArrayList<>();
            }

            PropertyCondition propertyCondition = new PropertyCondition();

            propertyCondition.setValue(this.getObjectMapPropertyValue(propertyValue));
            propertyCondition.setOperator(operator);
            propertyCondition.setProperty(propertyName);

            searchConditions.add(propertyCondition);
        }

        return searchConditions;
    }



    /**
     * Add the supplied map property to an element properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addBooleanMapProperty(ElementProperties    properties,
                                                   String               propertyName,
                                                   Map<String, Boolean> mapValues)
    {
        if (mapValues != null)
        {
            if (! mapValues.isEmpty())
            {
                ElementProperties  resultingProperties;

                if (properties == null)
                {
                    resultingProperties = new ElementProperties();
                }
                else
                {
                    resultingProperties = properties;
                }

                /*
                 * The values of a map property are stored as an embedded ElementProperties object.
                 */
                ElementProperties  mapElementProperties  = this.addBooleanPropertyMap(null, mapValues);

                /*
                 * If there was content in the map then the resulting ElementProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapElementProperties != null)
                {
                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
                    mapTypePropertyValue.setMapValues(mapElementProperties);
                    mapTypePropertyValue.setTypeName("map<string,boolean>");
                    resultingProperties.setProperty(propertyName, mapTypePropertyValue);

                    return resultingProperties;
                }
            }
        }

        return properties;
    }


    /**
     * Add the supplied map property to an element properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addIntMapProperty(ElementProperties    properties,
                                               String               propertyName,
                                               Map<String, Integer> mapValues)
    {
        if (mapValues != null)
        {
            if (! mapValues.isEmpty())
            {
                ElementProperties  resultingProperties;

                if (properties == null)
                {
                    resultingProperties = new ElementProperties();
                }
                else
                {
                    resultingProperties = properties;
                }

                /*
                 * The values of a map property are stored as an embedded ElementProperties object.
                 */
                ElementProperties  mapElementProperties  = this.addIntPropertyMap(null, mapValues);

                /*
                 * If there was content in the map then the resulting ElementProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapElementProperties != null)
                {
                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
                    mapTypePropertyValue.setMapValues(mapElementProperties);
                    mapTypePropertyValue.setTypeName("map<string,int>");
                    resultingProperties.setProperty(propertyName, mapTypePropertyValue);

                    return resultingProperties;
                }
            }
        }

        return properties;
    }


    /**
     * Add the supplied map property to an element properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addLongMapProperty(ElementProperties properties,
                                                String            propertyName,
                                                Map<String, Long> mapValues)
    {
        if (mapValues != null)
        {
            if (! mapValues.isEmpty())
            {
                ElementProperties  resultingProperties;

                if (properties == null)
                {
                    resultingProperties = new ElementProperties();
                }
                else
                {
                    resultingProperties = properties;
                }

                /*
                 * The values of a map property are stored as an embedded ElementProperties object.
                 */
                ElementProperties  mapElementProperties  = this.addLongPropertyMap(null, mapValues);

                /*
                 * If there was content in the map then the resulting ElementProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapElementProperties != null)
                {
                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
                    mapTypePropertyValue.setMapValues(mapElementProperties);
                    mapTypePropertyValue.setTypeName("map<string,long>");
                    resultingProperties.setProperty(propertyName, mapTypePropertyValue);

                    return resultingProperties;
                }
            }
        }

        return properties;
    }


    /**
     * Add the supplied map property to an element properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addDateMapProperty(ElementProperties properties,
                                                String            propertyName,
                                                Map<String, Date> mapValues)
    {
        if (mapValues != null)
        {
            if (! mapValues.isEmpty())
            {
                ElementProperties  resultingProperties;

                if (properties == null)
                {
                    resultingProperties = new ElementProperties();
                }
                else
                {
                    resultingProperties = properties;
                }

                /*
                 * The values of a map property are stored as an embedded ElementProperties object.
                 */
                ElementProperties  mapElementProperties  = this.addDatePropertyMap(null, mapValues);

                /*
                 * If there was content in the map then the resulting ElementProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapElementProperties != null)
                {
                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
                    mapTypePropertyValue.setMapValues(mapElementProperties);
                    mapTypePropertyValue.setTypeName("map<string,date>");
                    resultingProperties.setProperty(propertyName, mapTypePropertyValue);

                    return resultingProperties;
                }
            }
        }

        return properties;
    }


    /**
     * Add the supplied map property to an element properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addDoubleMapProperty(ElementProperties   properties,
                                                  String              propertyName,
                                                  Map<String, Double> mapValues)
    {
        if (mapValues != null)
        {
            if (! mapValues.isEmpty())
            {
                ElementProperties  resultingProperties;

                if (properties == null)
                {
                    resultingProperties = new ElementProperties();
                }
                else
                {
                    resultingProperties = properties;
                }

                /*
                 * The values of a map property are stored as an embedded ElementProperties object.
                 */
                ElementProperties  mapElementProperties  = this.addDoublePropertyMap(null, mapValues);

                /*
                 * If there was content in the map then the resulting ElementProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapElementProperties != null)
                {
                    MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();
                    mapTypePropertyValue.setMapValues(mapElementProperties);
                    mapTypePropertyValue.setTypeName("map<string,double>");
                    resultingProperties.setProperty(propertyName, mapTypePropertyValue);

                    return resultingProperties;
                }
            }
        }

        return properties;
    }


    /**
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addStringPropertyMap(ElementProperties   properties,
                                                  Map<String, String> mapValues)
    {
        if ((mapValues != null) && (! mapValues.isEmpty()))
        {
            ElementProperties  resultingProperties;

            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            int propertyCount = 0;

            for (String mapPropertyName : mapValues.keySet())
            {
                String mapPropertyValue = mapValues.get(mapPropertyName);

                resultingProperties.setProperty(mapPropertyName, getPrimitivePropertyValue(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING, mapPropertyValue));
                propertyCount++;
            }

            if (propertyCount > 0)
            {
                return resultingProperties;
            }
        }

        return properties;
    }


    /**
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addStringArrayPropertyMap(ElementProperties         properties,
                                                       Map<String, List<String>> mapValues)
    {
        if ((mapValues != null) && (! mapValues.isEmpty()))
        {
            ElementProperties  resultingProperties;

            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            int propertyCount = 0;

            for (String mapPropertyName : mapValues.keySet())
            {
                List<String> mapPropertyValue = mapValues.get(mapPropertyName);

                resultingProperties.setProperty(mapPropertyName, getStringArrayPropertyValue(mapPropertyValue));
                propertyCount++;
            }

            if (propertyCount > 0)
            {
                return resultingProperties;
            }
        }

        return properties;
    }


    /**
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addIntPropertyMap(ElementProperties    properties,
                                               Map<String, Integer> mapValues)
    {
        if ((mapValues != null) && (! mapValues.isEmpty()))
        {
            ElementProperties  resultingProperties;

            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            int propertyCount = 0;

            for (String mapPropertyName : mapValues.keySet())
            {
                Integer mapPropertyValue = mapValues.get(mapPropertyName);

                PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT);
                primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT.getName());
                resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                propertyCount++;
            }

            if (propertyCount > 0)
            {
                return resultingProperties;
            }
        }

        return properties;
    }



    /**
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addLongPropertyMap(ElementProperties   properties,
                                                Map<String, Long> mapValues)
    {
        if ((mapValues != null) && (! mapValues.isEmpty()))
        {
            ElementProperties  resultingProperties;

            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            int propertyCount = 0;

            for (String mapPropertyName : mapValues.keySet())
            {
                Long mapPropertyValue = mapValues.get(mapPropertyName);

                PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG);
                primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG.getName());
                resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                propertyCount++;
            }

            if (propertyCount > 0)
            {
                return resultingProperties;
            }
        }

        return properties;
    }


    /**
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addDatePropertyMap(ElementProperties   properties,
                                                Map<String, Date>   mapValues)
    {
        if ((mapValues != null) && (! mapValues.isEmpty()))
        {
            ElementProperties  resultingProperties;

            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            int propertyCount = 0;

            for (String mapPropertyName : mapValues.keySet())
            {
                Date mapPropertyValue = mapValues.get(mapPropertyName);

                long time = 0L;

                if (mapPropertyValue != null)
                {
                    time = mapPropertyValue.getTime();
                }

                resultingProperties.setProperty(mapPropertyName, getPrimitivePropertyValue(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE, time));
                propertyCount++;
            }

            if (propertyCount > 0)
            {
                return resultingProperties;
            }
        }

        return properties;
    }


    /**
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addDoublePropertyMap(ElementProperties   properties,
                                                  Map<String, Double> mapValues)
    {
        if ((mapValues != null) && (! mapValues.isEmpty()))
        {
            ElementProperties  resultingProperties;

            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            int propertyCount = 0;

            for (String mapPropertyName : mapValues.keySet())
            {
                Double mapPropertyValue = mapValues.get(mapPropertyName);

                PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DOUBLE);
                primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DOUBLE.getName());
                resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                propertyCount++;
            }

            if (propertyCount > 0)
            {
                return resultingProperties;
            }
        }

        return properties;
    }


    /**
     * Add the supplied property map to an element properties object.  Each of the entries in the map is added
     * as a separate property in element properties.  If the element properties object
     * supplied is null, a new element properties object is created.
     *
     * @param properties properties object to add property to, may be null.
     * @param mapValues contents of the map
     * @return resulting element properties object
     */
    public ElementProperties addBooleanPropertyMap(ElementProperties    properties,
                                                   Map<String, Boolean> mapValues)
    {
        if ((mapValues != null) && (! mapValues.isEmpty()))
        {
            ElementProperties  resultingProperties;

            if (properties == null)
            {
                resultingProperties = new ElementProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            int propertyCount = 0;

            for (String mapPropertyName : mapValues.keySet())
            {
                Boolean mapPropertyValue = mapValues.get(mapPropertyName);

                PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();
                primitiveTypePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
                primitiveTypePropertyValue.setPrimitiveValue(mapPropertyValue);
                primitiveTypePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN.getName());
                resultingProperties.setProperty(mapPropertyName, primitiveTypePropertyValue);
                propertyCount++;
            }

            if (propertyCount > 0)
            {
                return resultingProperties;
            }
        }

        return properties;
    }



    /**
     * Return the requested property or null if property is not found.  If the property is found, it is removed from
     * the InstanceProperties structure.  If the property is not a string property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public String removeStringProperty(String             sourceName,
                                       String             propertyName,
                                       ElementProperties  properties,
                                       String             methodName)
    {
        String  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getStringProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }

    /**
     * Retrieve the ordinal value from an enum property.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return symbolic name
     */
    public String removeEnumProperty(String             sourceName,
                                     String             propertyName,
                                     ElementProperties  properties,
                                     String             methodName)
    {
        String  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getEnumPropertySymbolicName(sourceName, propertyName, properties, methodName);
            this.removeProperty(propertyName, properties);
        }

        return retrievedProperty;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, String> removeStringMapFromProperty(String             sourceName,
                                                           String             propertyName,
                                                           ElementProperties  properties,
                                                           String             methodName)
    {
        Map<String, String>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getStringMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }



    /**
     * Return the requested property or 0 if property is not found.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not an int property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public int    removeIntProperty(String             sourceName,
                                    String             propertyName,
                                    ElementProperties  properties,
                                    String             methodName)
    {
        int  retrievedProperty = 0;

        if (properties != null)
        {
            retrievedProperty = this.getIntProperty(sourceName, propertyName, properties, methodName);

            this.removeProperty(propertyName, properties);
        }

        return retrievedProperty;
    }



    /**
     * Return the requested property or null if property is not found.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a date property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public Date    removeDateProperty(String             sourceName,
                                      String             propertyName,
                                      ElementProperties  properties,
                                      String             methodName)
    {
        Date  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getDateProperty(sourceName, propertyName, properties, methodName);

            this.removeProperty(propertyName, properties);
        }

        return retrievedProperty;
    }



    /**
     * Return the requested property or false if property is not found.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a boolean property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public boolean removeBooleanProperty(String             sourceName,
                                         String             propertyName,
                                         ElementProperties  properties,
                                         String             methodName)
    {
        boolean  retrievedProperty = false;

        if (properties != null)
        {
            retrievedProperty = this.getBooleanProperty(sourceName, propertyName, properties, methodName);

            this.removeProperty(propertyName, properties);
        }

        return retrievedProperty;
    }


    /**
     * Return the requested property or 0 if property is not found.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a long property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public long   removeLongProperty(String             sourceName,
                                     String             propertyName,
                                     ElementProperties properties,
                                     String             methodName)
    {
        long  retrievedProperty = 0;

        if (properties != null)
        {
            retrievedProperty = this.getLongProperty(sourceName, propertyName, properties, methodName);

            this.removeProperty(propertyName, properties);
        }

        return retrievedProperty;
    }


    /**
     * Locates and extracts a string array property and extracts its values.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not an array property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties all the properties of the instance
     * @param methodName method of caller
     * @return array property value or null
     */
    public List<String> removeStringArrayProperty(String             sourceName,
                                                  String             propertyName,
                                                  ElementProperties properties,
                                                  String             methodName)
    {
        List<String>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getStringArrayProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Object> removeMapFromProperty(String             sourceName,
                                                     String             propertyName,
                                                     ElementProperties properties,
                                                     String             methodName)
    {
        Map<String, Object>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }



    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Long> removeLongMapFromProperty(String             sourceName,
                                                       String             propertyName,
                                                       ElementProperties  properties,
                                                       String             methodName)
    {
        Map<String, Long>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getLongMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Double> removeDoubleMapFromProperty(String             sourceName,
                                                           String             propertyName,
                                                           ElementProperties  properties,
                                                           String             methodName)
    {
        Map<String, Double>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getDoubleMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }



    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Integer> removeIntegerMapFromProperty(String             sourceName,
                                                             String             propertyName,
                                                             ElementProperties  properties,
                                                             String             methodName)
    {
        Map<String, Integer>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getIntegerMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Boolean> removeBooleanMapFromProperty(String             sourceName,
                                                             String             propertyName,
                                                             ElementProperties  properties,
                                                             String             methodName)
    {
        Map<String, Boolean>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getBooleanMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Date> removeDateMapFromProperty(String             sourceName,
                                                          String             propertyName,
                                                          ElementProperties  properties,
                                                          String             methodName)
    {
        Map<String, Date>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getDateMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
            }
        }

        return retrievedProperty;
    }


    /**
     * Remove the named property from the instance properties object.
     *
     * @param propertyName name of property to remove
     * @param properties instance properties object to work on
     */
    protected void removeProperty(String    propertyName, ElementProperties properties)
    {
        if (properties != null)
        {
            Map<String, PropertyValue> instancePropertyValueMap = properties.getPropertyValueMap();

            if (instancePropertyValueMap != null)
            {
                instancePropertyValueMap.remove(propertyName);
                properties.setPropertyValueMap(instancePropertyValueMap);
            }
        }
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, String> getStringMapFromProperty(String             sourceName,
                                                        String             propertyName,
                                                        ElementProperties  properties,
                                                        String             methodName)
    {
        Map<String, Object>   mapFromProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

        if (mapFromProperty != null)
        {
            Map<String, String>  stringMapFromProperty = new HashMap<>();

            for (String mapPropertyName : mapFromProperty.keySet())
            {
                Object actualPropertyValue = mapFromProperty.get(mapPropertyName);

                if (actualPropertyValue != null)
                {
                    stringMapFromProperty.put(mapPropertyName, actualPropertyValue.toString());
                }
            }

            if (! stringMapFromProperty.isEmpty())
            {
                return stringMapFromProperty;
            }
        }

        return null;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Boolean> getBooleanMapFromProperty(String             sourceName,
                                                          String             propertyName,
                                                          ElementProperties  properties,
                                                          String             methodName)
    {
        Map<String, Object>   mapFromProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

        if (mapFromProperty != null)
        {
            Map<String, Boolean>  booleanMap = new HashMap<>();

            for (String mapPropertyName : mapFromProperty.keySet())
            {
                Object actualPropertyValue = mapFromProperty.get(mapPropertyName);

                if (actualPropertyValue != null)
                {
                    booleanMap.put(mapPropertyName, (Boolean)actualPropertyValue);
                }
            }

            if (! booleanMap.isEmpty())
            {
                return booleanMap;
            }
        }

        return null;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Date> getDateMapFromProperty(String             sourceName,
                                                    String             propertyName,
                                                    ElementProperties  properties,
                                                    String             methodName)
    {
        Map<String, Object>   mapFromProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

        if (mapFromProperty != null)
        {
            Map<String, Date>  dateMap = new HashMap<>();

            for (String mapPropertyName : mapFromProperty.keySet())
            {
                Object actualPropertyValue = mapFromProperty.get(mapPropertyName);

                if (actualPropertyValue instanceof Date date)
                {
                    dateMap.put(mapPropertyName, date);
                }
                else if (actualPropertyValue instanceof Long time)
                {
                    dateMap.put(mapPropertyName, new Date(time));
                }
            }

            if (! dateMap.isEmpty())
            {
                return dateMap;
            }
        }

        return null;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Long> getLongMapFromProperty(String             sourceName,
                                                    String             propertyName,
                                                    ElementProperties properties,
                                                    String             methodName)
    {
        Map<String, Object>   mapFromProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

        if (mapFromProperty != null)
        {
            Map<String, Long>  longMap = new HashMap<>();

            for (String mapPropertyName : mapFromProperty.keySet())
            {
                Object actualPropertyValue = mapFromProperty.get(mapPropertyName);

                if (actualPropertyValue != null)
                {
                    longMap.put(mapPropertyName, Long.parseLong(actualPropertyValue.toString()));
                }
            }

            if (! longMap.isEmpty())
            {
                return longMap;
            }
        }

        return null;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Double> getDoubleMapFromProperty(String             sourceName,
                                                        String             propertyName,
                                                        ElementProperties properties,
                                                        String             methodName)
    {
        Map<String, Object>   mapFromProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

        if (mapFromProperty != null)
        {
            Map<String, Double>  doubleMap = new HashMap<>();

            for (String mapPropertyName : mapFromProperty.keySet())
            {
                Object actualPropertyValue = mapFromProperty.get(mapPropertyName);

                if (actualPropertyValue != null)
                {
                    doubleMap.put(mapPropertyName, Double.parseDouble(actualPropertyValue.toString()));
                }
            }

            if (! doubleMap.isEmpty())
            {
                return doubleMap;
            }
        }

        return null;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Integer> getIntegerMapFromProperty(String             sourceName,
                                                          String             propertyName,
                                                          ElementProperties properties,
                                                          String             methodName)
    {
        Map<String, Object>   mapFromProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

        if (mapFromProperty != null)
        {
            Map<String, Integer>  integerMap = new HashMap<>();

            for (String mapPropertyName : mapFromProperty.keySet())
            {
                Object actualPropertyValue = mapFromProperty.get(mapPropertyName);

                if (actualPropertyValue != null)
                {
                    integerMap.put(mapPropertyName, (Integer) actualPropertyValue);
                }
            }

            if (! integerMap.isEmpty())
            {
                return integerMap;
            }
        }

        return null;
    }
    

    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties all the properties of the instance
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Object> getMapFromProperty(String            sourceName,
                                                  String            propertyName,
                                                  ElementProperties properties,
                                                  String            methodName)
    {
        final String  thisMethodName = "getMapFromProperty";

        if (properties != null)
        {
            PropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue != null)
            {
                try
                {
                    if (propertyValue instanceof MapTypePropertyValue mapTypePropertyValue)
                    {
                        return this.getElementPropertiesAsMap(mapTypePropertyValue.getMapValues());
                    }
                }
                catch (Exception error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }

        return null;
    }


    /**
     * Retrieve the ordinal value from an enum property.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return int ordinal or -1 if not found
     */
    public String getEnumPropertySymbolicName(String             sourceName,
                                              String             propertyName,
                                              ElementProperties  properties,
                                              String             methodName)
    {
        PropertyValue instancePropertyValue = properties.getPropertyValue(propertyName);

        if (instancePropertyValue instanceof EnumTypePropertyValue enumTypePropertyValue)
        {
            return enumTypePropertyValue.getSymbolicName();
        }

        return null;
    }


    /**
     * Convert an element properties object into a map.
     *
     * @param properties packed properties
     * @return properties stored in Java map
     */
    public Map<String, Object> getElementPropertiesAsMap(ElementProperties    properties)
    {
        if (properties != null)
        {
            Map<String, PropertyValue> propertyValues = properties.getPropertyValueMap();
            Map<String, Object>        resultingMap   = new HashMap<>();

            if (propertyValues != null)
            {
                for (String mapPropertyName : propertyValues.keySet())
                {
                    PropertyValue actualPropertyValue = properties.getPropertyValue(mapPropertyName);

                    if (actualPropertyValue != null)
                    {
                        if (actualPropertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                        {
                            resultingMap.put(mapPropertyName, primitiveTypePropertyValue.getPrimitiveValue());
                        }
                        else
                        {
                            resultingMap.put(mapPropertyName, actualPropertyValue);
                        }
                    }
                }
            }

            return resultingMap;
        }

        return null;
    }


    /**
     * Replace any placeholders found in the string property with the supplied values.
     *
     * @param propertyValue string property from the template
     * @param placeholderProperties map of placeholder names to placeholder values to substitute into the template
     *                              properties
     * @return updated property
     */
    public String replacePrimitiveStringWithPlaceholders(String              propertyValue,
                                                         Map<String, String> placeholderProperties)
    {
        if ((propertyValue == null) || (! propertyValue.contains("~{")))
        {
            /*
             * No placeholders in property.
             */
            return propertyValue;
        }

        if ((placeholderProperties != null) && (! placeholderProperties.isEmpty()))
        {
            for (String placeholderName : placeholderProperties.keySet())
            {
                String placeholderMatchString = "~{"+ placeholderName + "}~";

                if (propertyValue.equals(placeholderMatchString))
                {
                    propertyValue = placeholderProperties.get(placeholderName);
                }
                else
                {
                    String regExMatchString = Pattern.quote(placeholderMatchString);
                    String[] configBits = propertyValue.split(regExMatchString);

                    if (configBits.length == 1)
                    {
                        if (! propertyValue.equals(configBits[0]))
                        {
                            propertyValue = configBits[0] + placeholderProperties.get(placeholderName);
                        }
                    }
                    else if (configBits.length > 1)
                    {
                        StringBuilder newConfigString = new StringBuilder();
                        boolean       firstPart       = true;

                        for (String configBit : configBits)
                        {
                            if (! firstPart)
                            {
                                newConfigString.append(placeholderProperties.get(placeholderName));
                            }

                            firstPart = false;

                            if (configBit != null)
                            {
                                newConfigString.append(configBit);
                            }
                        }

                        if (propertyValue.endsWith(placeholderMatchString))
                        {
                            newConfigString.append(placeholderProperties.get(placeholderName));
                        }

                        propertyValue = newConfigString.toString();
                    }
                }
            }
        }

        return propertyValue;
    }


    /**
     * Return the property name from the template entity that has its placeholder variables resolved.
     *
     * @param sourceName name of calling source
     * @param templateElement template element which has properties that include placeholder values.
     * @param propertyName name of the property to extract
     * @param placeholderProperties placeholder properties to use to resolve the property
     * @return resolved property value
     */
    public String getResolvedStringPropertyFromTemplate(String              sourceName,
                                                        OpenMetadataElement templateElement,
                                                        String              propertyName,
                                                        Map<String, String> placeholderProperties)
    {
        final String methodName = "";

        if (templateElement != null)
        {
            String propertyValue = this.getStringProperty(sourceName,
                                                          propertyName,
                                                          templateElement.getElementProperties(),
                                                          methodName);

            return this.replacePrimitiveStringWithPlaceholders(propertyValue, placeholderProperties);
        }

        return null;
    }


    /**
     * Return the requested property or null if property is not found.  If the property is not
     * a string property then a logic exception is thrown
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public String getStringProperty(String            sourceName,
                                    String            propertyName,
                                    ElementProperties properties,
                                    String            methodName)
    {
        final String  thisMethodName = "getStringProperty";

        if (properties != null)
        {
            PropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue != null)
            {
                try
                {
                    if (propertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                    {
                        if (primitiveTypePropertyValue.getPrimitiveTypeCategory() == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING)
                        {
                            if (primitiveTypePropertyValue.getPrimitiveValue() != null)
                            {
                                return primitiveTypePropertyValue.getPrimitiveValue().toString();
                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }
        
        return null;
    }


    /**
     * Locates and extracts a string array property and extracts its values.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties all the properties of the instance
     * @param callingMethodName method of caller
     * @return array property value or null
     */
    public List<String> getStringArrayProperty(String             sourceName,
                                               String             propertyName,
                                               ElementProperties properties,
                                               String             callingMethodName)
    {
        final String  thisMethodName = "getStringArrayProperty";

        if (properties != null)
        {
            PropertyValue instancePropertyValue = properties.getPropertyValue(propertyName);

            if (instancePropertyValue != null)
            {
                /*
                 * The property exists in the supplied properties.   It should be of category ARRAY.
                 * If it is then it can be cast to an ArrayPropertyValue in order to extract the
                 * array size and the values.
                 */

                try
                {
                    if (instancePropertyValue instanceof ArrayTypePropertyValue arrayPropertyValue)
                    {
                        if (arrayPropertyValue.getArrayCount() > 0)
                        {
                            /*
                             * There are values to extract
                             */
                            return getPropertiesAsArray(arrayPropertyValue.getArrayValues());
                        }
                    }
                }
                catch (Exception error)
                {
                    throwHelperLogicError(sourceName, callingMethodName, thisMethodName);
                }
            }
        }

        return null;
    }


    /**
     * Convert the values in the instance properties into a String Array.  It assumes all the elements are primitives.
     *
     * @param elementProperties instance properties containing the values.  They should all be primitive Strings.
     * @return list of strings
     */
    private List<String> getPropertiesAsArray(ElementProperties     elementProperties)
    {
        if (elementProperties != null)
        {
            Map<String, PropertyValue> instancePropertyValues = elementProperties.getPropertyValueMap();
            List<String>               resultingArray = new ArrayList<>();

            for (String arrayOrdinalName : instancePropertyValues.keySet())
            {
                if (arrayOrdinalName != null)
                {
                    int           arrayOrdinalNumber  = Integer.decode(arrayOrdinalName);
                    PropertyValue actualPropertyValue = elementProperties.getPropertyValue(arrayOrdinalName);

                    if (actualPropertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                    {
                        resultingArray.add(arrayOrdinalNumber, primitiveTypePropertyValue.getPrimitiveValue().toString());
                    }
                }
            }

            return resultingArray;
        }

        return null;
    }


    /**
     * Return the requested property or 0 if property is not found.  If the property is not
     * an int property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public int    getIntProperty(String             sourceName,
                                 String             propertyName,
                                 ElementProperties properties,
                                 String             methodName)
    {
        final String  thisMethodName = "getIntProperty";

        if (properties != null)
        {
            PropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue != null)
            {
                try
                {
                    if (propertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                    {
                        if (primitiveTypePropertyValue.getPrimitiveTypeCategory() == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT)
                        {
                            if (primitiveTypePropertyValue.getPrimitiveValue() != null)
                            {
                                return Integer.parseInt(primitiveTypePropertyValue.getPrimitiveValue().toString());
                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }
        
        return 0;
    }



    /**
     * Return the requested property or 0 if property is not found.  If the property is not
     * a long property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public long  getLongProperty(String             sourceName,
                                 String             propertyName,
                                 ElementProperties properties,
                                 String             methodName)
    {
        final String  thisMethodName = "getLongProperty";

        if (properties != null)
        {
            PropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue != null)
            {
                try
                {
                    if (propertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                    {
                        if (primitiveTypePropertyValue.getPrimitiveTypeCategory() == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG)
                        {
                            if (primitiveTypePropertyValue.getPrimitiveValue() != null)
                            {
                                return Long.parseLong(primitiveTypePropertyValue.getPrimitiveValue().toString());
                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }

        return 0;
    }


    /**
     * Return the requested property or null if property is not found.  If the property is not
     * a date property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public Date getDateProperty(String             sourceName,
                                String             propertyName,
                                ElementProperties properties,
                                String             methodName)
    {
        final String  thisMethodName = "getDateProperty";

        if (properties != null)
        {
            PropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue != null)
            {
                try
                {
                    if (propertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                    {
                        if (primitiveTypePropertyValue.getPrimitiveTypeCategory() == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE)
                        {
                            if (primitiveTypePropertyValue.getPrimitiveValue() != null)
                            {
                                Long timestamp = (Long) primitiveTypePropertyValue.getPrimitiveValue();
                                return new Date(timestamp);

                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }
        
        return null;
    }
    



    /**
     * Return the requested property or false if property is not found.  If the property is not
     * a boolean property then a logic exception is thrown
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public boolean getBooleanProperty(String             sourceName,
                                      String             propertyName,
                                      ElementProperties properties,
                                      String             methodName)
    {
        final String  thisMethodName = "getBooleanProperty";

        if (properties != null)
        {
            PropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue != null)
            {
                try
                {
                    if (propertyValue instanceof PrimitiveTypePropertyValue primitiveTypePropertyValue)
                    {
                        if (primitiveTypePropertyValue.getPrimitiveTypeCategory() == PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN)
                        {
                            if (primitiveTypePropertyValue.getPrimitiveValue() != null)
                            {
                                return Boolean.parseBoolean(primitiveTypePropertyValue.getPrimitiveValue().toString());
                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }
        
        return false;
    }


    /**
     * Build up property parameters for a search that returns the metadata collection name.
     *
     * @return search properties
     */
    public SearchProperties getSearchPropertiesForMetadataCollectionName(String metadataCollectionQualifiedName)
    {
        SearchProperties           searchProperties       = new SearchProperties();
        List<PropertyCondition>    conditions             = new ArrayList<>();
        PropertyCondition          condition              = new PropertyCondition();
        PrimitiveTypePropertyValue primitivePropertyValue = new PrimitiveTypePropertyValue();

        primitivePropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(metadataCollectionQualifiedName);
        primitivePropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        condition.setProperty(OpenMetadataProperty.METADATA_COLLECTION_NAME.name);
        condition.setOperator(PropertyComparisonOperator.EQ);
        condition.setValue(primitivePropertyValue);

        conditions.add(condition);

        searchProperties.setConditions(conditions);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);
        return searchProperties;
    }


    /**
     * Convert the provided instance properties and match criteria into an equivalent SearchProperties object.
     *
     * @param properties properties object to convert
     * @param matchCriteria match criteria to apply
     * @return SearchProperties object.
     */
    public SearchProperties getSearchProperties(ElementProperties properties,
                                                MatchCriteria     matchCriteria)
    {
        SearchProperties matchProperties = null;
        if (properties != null)
        {
            matchProperties = new SearchProperties();
            List<PropertyCondition> conditions = new ArrayList<>();
            Iterator<String> propertyNames = properties.getPropertyNames();

            while (propertyNames.hasNext())
            {
                String propertyName = propertyNames.next();
                PropertyCondition propertyCondition = new PropertyCondition();
                propertyCondition.setProperty(propertyName);
                PropertyValue propertyValue = properties.getPropertyValue(propertyName);

                if ((propertyValue instanceof PrimitiveTypePropertyValue)
                            && ((PrimitiveTypePropertyValue)propertyValue).getPrimitiveTypeCategory().equals(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING) )
                {
                    // Use the LIKE operator for any strings
                    propertyCondition.setOperator(PropertyComparisonOperator.LIKE);
                }
                else
                {
                    // And the EQUAlS operator for any other type
                    propertyCondition.setOperator(PropertyComparisonOperator.EQ);
                }

                // TODO: we may want to default complex types (lists, etc) to other operators than EQ?
                propertyCondition.setValue(propertyValue);
                conditions.add(propertyCondition);
            }

            matchProperties.setConditions(conditions);
            matchProperties.setMatchCriteria(matchCriteria);
        }
        return matchProperties;
    }


    /**
     * Extract a particular classification from an open metadata element.
     *
     * @param openMetadataElement source element
     * @param classificationName name of the classification to extract
     * @return requested classification, or null
     */
    public AttachedClassification getClassification(OpenMetadataElement openMetadataElement,
                                                    String              classificationName)
    {
        if ((openMetadataElement != null) && (classificationName != null))
        {
            List<AttachedClassification> classifications = openMetadataElement.getClassifications();

            if (classifications != null)
            {
                for (AttachedClassification classification : classifications)
                {
                    if (classificationName.equals(classification.getClassificationName()))
                    {
                        return classification;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Extract a particular classification from an open metadata element.
     *
     * @param elementHeader source element
     * @param classificationName name of the classification to extract
     * @return requested classification, or null
     */
    public ElementClassification getClassification(ElementHeader elementHeader,
                                                   String        classificationName)
    {
        if ((elementHeader != null) && (classificationName != null))
        {
            if (OpenMetadataType.ANCHORS_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getAnchor();
            }
            if (OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getSchemaType();
            }
            if (OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getCalculatedValue();
            }
            if (OpenMetadataType.PRIMARY_KEY_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getPrimaryKey();
            }
            if (OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getZoneMembership();
            }
            if (OpenMetadataType.MEMENTO_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getMemento();
            }
            if (OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getTemplate();
            }
            if (OpenMetadataType.SUBJECT_AREA_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getSubjectArea();
            }
            if (OpenMetadataType.IMPACT_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getImpact();
            }
            if (OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getCriticality();
            }
            if (OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getConfidentiality();
            }
            if (OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getConfidence();
            }
            if (OpenMetadataType.RETENTION_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getRetention();
            }
            if (OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getDigitalResourceOrigin();
            }
            if (OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName.equals(classificationName))
            {
                return elementHeader.getOwnership();
            }

            ElementClassification classification = getClassification(elementHeader.getOtherClassifications(), classificationName);

            if (classification != null)
            {
                return classification;
            }

            classification = getClassification(elementHeader.getProjectRoles(), classificationName);

            if (classification != null)
            {
                return classification;
            }

            classification = getClassification(elementHeader.getCollectionRoles(), classificationName);

            if (classification != null)
            {
                return classification;
            }

            classification = getClassification(elementHeader.getServerPurposes(), classificationName);

            if (classification != null)
            {
                return classification;
            }

            classification = getClassification(elementHeader.getResourceManagerRoles(), classificationName);

            if (classification != null)
            {
                return classification;
            }

            classification = getClassification(elementHeader.getDuplicateClassifications(), classificationName);

            if (classification != null)
            {
                return classification;
            }

            return getClassification(elementHeader.getExecutionPoints(), classificationName);
        }

        return null;
    }


    /**
     * Extract a particular classification from a list of classifications.
     *
     * @param classifications list
     * @param classificationName name of desired classification
     * @return requested classification or null
     */
    public ElementClassification getClassification(List<ElementClassification> classifications,
                                                   String                      classificationName)
    {
        if (classifications != null)
        {
            for (ElementClassification classification : classifications)
            {
                if (classificationName.equals(classification.getClassificationName()))
                {
                    return classification;
                }
            }
        }

        return null;
    }


    /**
     * Test whether an element has a particular classification.
     *
     * @param elementHeader header
     * @param classificationName classification to search for
     * @return boolean
     */
    public boolean isClassified(ElementHeader elementHeader,
                                String        classificationName)
    {
        return this.getClassification(elementHeader, classificationName) != null;
    }


    /**
     * Test whether an element has a particular classification.
     *
     * @param openMetadataElement element to test
     * @param classificationName classification to search for
     * @return boolean
     */
    public boolean isClassified(OpenMetadataElement openMetadataElement,
                                String              classificationName)
    {
        if (openMetadataElement == null || openMetadataElement.getClassifications() == null)
        {
            return false;
        }

        for (AttachedClassification classification : openMetadataElement.getClassifications())
        {
            if ((classification != null) && (classificationName.equals(classification.getClassificationName())))
            {
                return true;
            }
        }

        return false;
    }



    /**
     * Extract a particular property from a classification if found attached to an open metadata element.
     *
     * @param openMetadataElement source element
     * @param classificationName name of the classification to extract
     * @param propertyName name of property to extract
     * @param methodName calling method
     * @return string property or null
     */
    public String getStringPropertyFromClassification(OpenMetadataElement openMetadataElement,
                                                      String              classificationName,
                                                      String              propertyName,
                                                      String              methodName)
    {
        AttachedClassification classification = this.getClassification(openMetadataElement, classificationName);

        if (classification != null)
        {
            return this.getStringProperty(classificationName,
                                          propertyName,
                                          classification.getClassificationProperties(),
                                          methodName);
        }

        return null;
    }


    /**
     * Throws a logic error exception when the repository helper is called with invalid parameters.
     * Normally this means the repository helper methods have been called in the wrong order.
     *
     * @param sourceName name of the calling repository or service
     * @param originatingMethodName method that called the repository validator
     * @param localMethodName local method that detected the error
     */
    private void throwHelperLogicError(String     sourceName,
                                       String     originatingMethodName,
                                       String     localMethodName)
    {
        throw new OMFRuntimeException(OMFErrorCode.HELPER_LOGIC_ERROR.getMessageDefinition(sourceName,
                                                                                           localMethodName,
                                                                                           originatingMethodName),
                                      this.getClass().getName(),
                                      localMethodName);
    }


    /**
     * Throws a logic error exception when the repository helper is called with invalid parameters.
     * Normally this means the repository helper methods have been called in the wrong order.
     *
     * @param sourceName name of the calling repository or service
     * @param originatingMethodName method that called the repository validator
     * @param localMethodName local method that detected the error
     * @param unexpectedException unexpected exception caught by the helper logic
     */
    private void throwHelperLogicError(String     sourceName,
                                       String     originatingMethodName,
                                       String     localMethodName,
                                       Exception  unexpectedException)
    {
        throw new OMFRuntimeException(OMFErrorCode.HELPER_LOGIC_EXCEPTION.getMessageDefinition(sourceName,
                                                                                               localMethodName,
                                                                                               originatingMethodName),
                                      this.getClass().getName(),
                                      localMethodName,
                                      unexpectedException);
    }




    /**
     * Set the provided search string to be interpreted as either case-insensitive or case-sensitive.
     *
     * @param searchString the string to set as case-insensitive
     * @param insensitive if true, set the string to be case-insensitive, otherwise leave as case-sensitive
     * @return string ensuring the provided searchString is case-(in)sensitive
     */
    private String setInsensitive(String searchString, boolean insensitive)
    {
        return insensitive ? "(?i)" + searchString : searchString;
    }


    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * to search for the string with a "starts with" semantic. The passed string will NOT be treated as a regular expression;
     * if you intend to use both a "starts with" semantic and a regular expression within the string, simply construct your
     * own regular expression directly (not with this helper method).
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression, but also wrap to obtain a "starts with" semantic
     * @param insensitive set to true to have a case-insensitive "starts with" regular expression
     * @return string that is interpreted literally, wrapped for a "starts with" semantic
     */
    public String getMiddleRegex(String searchString, boolean insensitive)
    {
        return searchString == null ? null : setInsensitive(".*" + getExactMatchRegex(searchString, false) + ".*", insensitive);
    }

    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * to search for the string with a "starts with" semantic. The passed string will NOT be treated as a regular expression;
     * if you intend to use both a "starts with" semantic and a regular expression within the string, simply construct your
     * own regular expression directly (not with this helper method).
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression, but also wrap to obtain a "starts with" semantic
     * @param insensitive set to true to have a case-insensitive "starts with" regular expression
     * @return string that is interpreted literally, wrapped for a "starts with" semantic
     */
    public String getStartsWithRegex(String searchString, boolean insensitive)
    {
        return searchString == null ? null : setInsensitive(getExactMatchRegex(searchString, false) + ".*", insensitive);
    }


    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * to search for the string with an "ends with" semantic. The passed string will NOT be treated as a regular expression;
     * if you intend to use both an "ends with" semantic and a regular expression within the string, simply construct your
     * own regular expression directly (not with this helper method).
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression, but also wrap to obtain an "ends with" semantic
     * @param insensitive set to true to have a case-insensitive "ends with" regular expression
     * @return string that is interpreted literally, wrapped for an "ends with" semantic
     */
    public String getEndsWithRegex(String searchString, boolean insensitive)
    {
        return searchString == null ? null : setInsensitive(".*" + getExactMatchRegex(searchString, false), insensitive);
    }


    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * without being interpreted as a regular expression (i.e. the returned string will be interpreted as a literal --
     * used to find an exact match of the string, irrespective of whether it contains characters that may have special
     * meanings to regular expressions).
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression
     * @param insensitive set to true to have a case-insensitive exact match regular expression
     * @return string that is interpreted literally rather than as a regular expression
     */
    String getExactMatchRegex(String searchString, boolean insensitive)
    {
        return searchString == null ? null : setInsensitive(Pattern.quote(searchString), insensitive);
    }


    /**
     * Construct a regular expression from the string supplied by the caller.
     * If their string includes regular expression characters then
     * they will be ignored.
     *
     * @param requestedSearch the supplied string
     * @param startsWith set to true if the requested string is at the front of the search
     * @param endsWith set to true if the requested string is at the end of the search
     * @param ignoreCase set to true to have a case-insensitive search
     * @return string that is interpreted literally rather than as a regular expression
     */
    public String getSearchString(String requestedSearch, boolean startsWith, boolean endsWith, boolean ignoreCase)
    {
        if ((requestedSearch == null) || (requestedSearch.isBlank()) || "*".equals(requestedSearch) || ("'".equals(requestedSearch) || ("''".equals(requestedSearch)) || "%".equals(requestedSearch)))
        {
            // ignore the flags for an empty search criteria string or SQL special characters - assume we want everything
            requestedSearch = ".*";
        }
        else
        {
            if (startsWith && endsWith)
            {
                requestedSearch = this.getExactMatchRegex(requestedSearch, ignoreCase);
            }
            else if (startsWith)
            {
                requestedSearch = this.getStartsWithRegex(requestedSearch, ignoreCase);
            }
            else if (endsWith)
            {
                requestedSearch = this.getEndsWithRegex(requestedSearch, ignoreCase);
            }
            else
            {
                requestedSearch = this.getMiddleRegex(requestedSearch, ignoreCase);
            }
        }

        return requestedSearch;
    }
}
