/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.automatedcuration.handlers;

import org.odpi.openmetadata.accessservices.assetowner.client.ExternalReferenceManager;
import org.odpi.openmetadata.accessservices.assetowner.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.mermaid.HierarchyMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.governanceaction.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.viewservices.automatedcuration.converters.ReferenceableConverter;
import org.odpi.openmetadata.viewservices.automatedcuration.converters.TechnologyTypeSummaryConverter;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.*;

import java.util.*;

/**
 * Provides additional support for retrieving technology types
 */
public class TechnologyTypeHandler
{
    private static final PropertyHelper          propertyHelper          = new PropertyHelper();
    private static final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private final ExternalReferenceManager externalRefHandler;
    private final OpenMetadataStoreClient  openHandler;
    private final String                   serverName;
    private final String                   serviceName;


    /**
     * Construct the handler.
     *
     * @param externalRefHandler client for retrieving resources for a technology type
     * @param openHandler Open Metadata Store client
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public TechnologyTypeHandler(ExternalReferenceManager externalRefHandler,
                                 OpenMetadataStoreClient  openHandler,
                                 String                   serviceName,
                                 String                   serverName)
    {
        this.externalRefHandler = externalRefHandler;
        this.openHandler        = openHandler;
        this.serviceName        = serviceName;
        this.serverName         = serverName;
    }


    /**
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param userId calling user
     * @param searchString string value to look for
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param effectiveTime the effective date/time to use for the query
     * @param limitResultsByStatus limit the status values that the elements should be
     * @param asOfTime repository time
     * @param sequencingOrder order of results
     * @param sequencingProperty optional property name
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<TechnologyTypeSummary> findTechnologyTypes(String              userId,
                                                           String              searchString,
                                                           int                 startFrom,
                                                           int                 pageSize,
                                                           Date                effectiveTime,
                                                           List<ElementStatus> limitResultsByStatus,
                                                           Date                asOfTime,
                                                           SequencingOrder     sequencingOrder,
                                                           String              sequencingProperty) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName = "findTechnologyTypes";
        final String parameterName = "searchString";

        invalidParameterHandler.validateSearchString(searchString, parameterName, methodName);

        List<OpenMetadataElement> openMetadataElements = openHandler.findMetadataElements(userId,
                                                                                          OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                                          null,
                                                                                          this.getTechnologyTypeConditions(searchString, PropertyComparisonOperator.LIKE),
                                                                                          limitResultsByStatus,
                                                                                          asOfTime,
                                                                                          null,
                                                                                          sequencingProperty,
                                                                                          sequencingOrder,
                                                                                          false,
                                                                                          false,
                                                                                          effectiveTime,
                                                                                          startFrom,
                                                                                          pageSize);

        return this.convertTechTypeSummaries(openMetadataElements, methodName);
    }



    /**
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param userId calling user
     * @param typeName type name value to look for
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param effectiveTime the effective date/time to use for the query
     * @param limitResultsByStatus limit the status values that the elements should be
     * @param asOfTime repository time
     * @param sequencingOrder order of results
     * @param sequencingProperty optional property name
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<TechnologyTypeSummary> getTechnologyTypesForOpenMetadataType(String              userId,
                                                                             String              typeName,
                                                                             int                 startFrom,
                                                                             int                 pageSize,
                                                                             Date                effectiveTime,
                                                                             List<ElementStatus> limitResultsByStatus,
                                                                             Date                asOfTime,
                                                                             SequencingOrder     sequencingOrder,
                                                                             String              sequencingProperty) throws InvalidParameterException,
                                                                                                                            UserNotAuthorizedException,
                                                                                                                            PropertyServerException
    {
        final String methodName = "getTechnologyTypesForOpenMetadataType";
        final String parameterName = "typeName";

        invalidParameterHandler.validateName(typeName, parameterName, methodName);

        String searchString = "Egeria:ValidMetadataValue:" + typeName + ":deployedImplementationType-.*";

        SearchProperties searchProperties = new SearchProperties();

        PrimitiveTypePropertyValue propertyValue = new PrimitiveTypePropertyValue();
        propertyValue.setTypeName("string");
        propertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        propertyValue.setPrimitiveValue(searchString);

        List<PropertyCondition> propertyConditions = new ArrayList<>();

        PropertyCondition propertyCondition = new PropertyCondition();
        propertyCondition.setValue(propertyValue);
        propertyCondition.setProperty(OpenMetadataProperty.QUALIFIED_NAME.name);
        propertyCondition.setOperator(PropertyComparisonOperator.LIKE);
        propertyConditions.add(propertyCondition);

        searchProperties.setConditions(propertyConditions);
        searchProperties.setMatchCriteria(MatchCriteria.ANY);

        List<OpenMetadataElement> openMetadataElements = openHandler.findMetadataElements(userId,
                                                                                          OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                                          null,
                                                                                          searchProperties,
                                                                                          limitResultsByStatus,
                                                                                          asOfTime,
                                                                                          null,
                                                                                          sequencingProperty,
                                                                                          sequencingOrder,
                                                                                          false,
                                                                                          false,
                                                                                          effectiveTime,
                                                                                          startFrom,
                                                                                          pageSize);

        return this.convertTechTypeSummaries(openMetadataElements, methodName);
    }


    /**
     * Retrieve detailed information about a specific technology type.  This includes
     * templates, connectors and external references.
     *
     * @param userId calling user
     * @param technologyTypeName string value to look for
     * @param effectiveTime the effective date/time to use for the query
     * @param limitResultsByStatus limit the status values that the elements should be
     * @param asOfTime repository time
     * @param sequencingOrder order of results
     * @param sequencingProperty optional property name
     *
     * @return detailed report for technology type
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public TechnologyTypeReport getTechnologyTypeDetail(String              userId,
                                                        String              technologyTypeName,
                                                        Date                effectiveTime,
                                                        List<ElementStatus> limitResultsByStatus,
                                                        Date                asOfTime,
                                                        SequencingOrder     sequencingOrder,
                                                        String              sequencingProperty) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "getTechnologyTypeDetail";
        final String parameterName = "technologyTypeName";

        invalidParameterHandler.validateName(technologyTypeName, parameterName, methodName);

        List<OpenMetadataElement> openMetadataElements = openHandler.findMetadataElements(userId,
                                                                                          OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                                          null,
                                                                                          this.getTechnologyTypeConditions(technologyTypeName, PropertyComparisonOperator.EQ),
                                                                                          limitResultsByStatus,
                                                                                          asOfTime,
                                                                                          null,
                                                                                          sequencingProperty,
                                                                                          sequencingOrder,
                                                                                          false,
                                                                                          false,
                                                                                          effectiveTime,
                                                                                          0,
                                                                                          0);

        if (openMetadataElements != null)
        {
            TechnologyTypeReport report = null;

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    report = new TechnologyTypeReport(this.convertTechTypeSummary(openMetadataElement));

                    List<RelatedElement> resourceList = externalRefHandler.getResourceList(userId,
                                                                                           openMetadataElement.getElementGUID(),
                                                                                           0,
                                                                                           0);

                    if (resourceList != null)
                    {
                        List<ResourceDescription> resources = new ArrayList<>();

                        for (RelatedElement resource : resourceList)
                        {
                            if (resource != null)
                            {
                                if ((resource.getRelationshipProperties() != null) && (resource.getRelationshipProperties().getExtendedProperties() != null))
                                {
                                    Map<String, Object> extendedProperties = resource.getRelationshipProperties().getExtendedProperties();

                                    ResourceDescription resourceDescription = new ResourceDescription();

                                    if (extendedProperties.get(OpenMetadataProperty.RESOURCE_USE.name) != null)
                                    {
                                        resourceDescription.setResourceUse(extendedProperties.get(OpenMetadataProperty.RESOURCE_USE.name).toString());
                                    }
                                    if (extendedProperties.get(OpenMetadataProperty.RESOURCE_USE_DESCRIPTION.name) != null)
                                    {
                                        resourceDescription.setResourceUseDescription(extendedProperties.get(OpenMetadataProperty.RESOURCE_USE_DESCRIPTION.name).toString());
                                    }

                                    resourceDescription.setRelatedElement(resource.getRelatedElement());
                                    resourceDescription.setSpecification(openHandler.getSpecification(userId, resource.getRelatedElement().getGUID()));

                                    resources.add(resourceDescription);
                                }
                            }
                        }

                        if (!resources.isEmpty())
                        {
                            report.setResourceList(resources);
                        }
                    }

                    List<RelatedElement> catalogTemplateList = externalRefHandler.getCatalogTemplateList(userId,
                                                                                                         openMetadataElement.getElementGUID(),
                                                                                                         0,
                                                                                                         0);

                    if (catalogTemplateList != null)
                    {
                        List<CatalogTemplate> catalogTemplates = new ArrayList<>();

                        for (RelatedElement templateElement : catalogTemplateList)
                        {
                            if (templateElement != null)
                            {
                                CatalogTemplate catalogTemplate = new CatalogTemplate();

                                catalogTemplate.setRelatedElement(templateElement.getRelatedElement());

                                List<ElementClassification> classifications = templateElement.getRelatedElement().getClassifications();

                                if (classifications != null)
                                {
                                    for (ElementClassification classification : classifications)
                                    {
                                        if (classification != null)
                                        {
                                            if (classification.getClassificationName().equals(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName))
                                            {
                                                if (classification.getClassificationProperties() != null)
                                                {
                                                    if (classification.getClassificationProperties().get(OpenMetadataProperty.NAME.name) != null)
                                                    {
                                                        catalogTemplate.setName(classification.getClassificationProperties().get(OpenMetadataProperty.NAME.name).toString());
                                                    }
                                                    else if (classification.getClassificationProperties().get(OpenMetadataProperty.DESCRIPTION.name) != null)
                                                    {
                                                        catalogTemplate.setDescription(classification.getClassificationProperties().get(OpenMetadataProperty.DESCRIPTION.name).toString());
                                                    }
                                                    else if (classification.getClassificationProperties().get(OpenMetadataProperty.VERSION_IDENTIFIER.name) != null)
                                                    {
                                                        catalogTemplate.setVersionIdentifier(classification.getClassificationProperties().get(OpenMetadataProperty.VERSION_IDENTIFIER.name).toString());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                catalogTemplate.setSpecification(openHandler.getSpecification(userId, templateElement.getRelatedElement().getGUID()));

                                catalogTemplates.add(catalogTemplate);
                            }
                        }

                        if (! catalogTemplates.isEmpty())
                        {
                            report.setCatalogTemplates(catalogTemplates);
                        }
                    }

                    List<ExternalReferenceElement> externalReferenceElements = externalRefHandler.retrieveAttachedExternalReferences(userId,
                                                                                                                                     openMetadataElement.getElementGUID(),
                                                                                                                                     0,
                                                                                                                                     0);

                    report.setExternalReferences(externalReferenceElements);

                    break;
                }
            }

            return report;
        }

        return null;
    }



    /**
     * Retrieve the requested deployed implementation type metadata element and its subtypes.  A mermaid version if the hierarchy is also returned.
     *
     * @param userId calling user
     * @param technologyTypeName string value to look for
     * @param effectiveTime the effective date/time to use for the query
     * @param limitResultsByStatus limit the status values that the elements should be
     * @param asOfTime repository time
     * @param sequencingOrder order of results
     * @param sequencingProperty optional property name
     *
     * @return detailed report for technology type
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public TechnologyTypeHierarchy getTechnologyTypeHierarchy(String              userId,
                                                              String              technologyTypeName,
                                                              Date                effectiveTime,
                                                              List<ElementStatus> limitResultsByStatus,
                                                              Date                asOfTime,
                                                              SequencingOrder     sequencingOrder,
                                                              String              sequencingProperty) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "getTechnologyTypeHierarchy";
        final String parameterName = "technologyTypeName";

        invalidParameterHandler.validateName(technologyTypeName, parameterName, methodName);

        List<OpenMetadataElement> openMetadataElements = openHandler.findMetadataElements(userId,
                                                                                          OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                                          null,
                                                                                          this.getTechnologyTypeConditions(technologyTypeName, PropertyComparisonOperator.EQ),
                                                                                          limitResultsByStatus,
                                                                                          asOfTime,
                                                                                          null,
                                                                                          sequencingProperty,
                                                                                          sequencingOrder,
                                                                                          false,
                                                                                          false,
                                                                                          effectiveTime,
                                                                                          0,
                                                                                          0);

        if (openMetadataElements != null)
        {
            TechnologyTypeHierarchy technologyTypeHierarchy = null;

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    technologyTypeHierarchy = new TechnologyTypeHierarchy(this.convertTechTypeSummary(openMetadataElement));

                    technologyTypeHierarchy.setSubTypes(this.getSubTypes(userId, openMetadataElement.getElementGUID()));

                    break;
                }
            }

            return technologyTypeHierarchy;
        }

        return null;
    }


    /**
     * Return the mermaid graph for a technology type hierarchy.
     *
     * @param technologyTypeHierarchy hierarchy to parse
     * @return mermaid string or null
     */
    public String getTechnologyTypeHierarchyMermaidString(TechnologyTypeHierarchy technologyTypeHierarchy)
    {
        if (technologyTypeHierarchy != null)
        {
            HierarchyMermaidGraphBuilder mermaidGraphBuilder = new HierarchyMermaidGraphBuilder("Technology type hierarchy",
                                                                                                technologyTypeHierarchy.getTechnologyTypeGUID(),
                                                                                                technologyTypeHierarchy.getName());

            mermaidGraphBuilder.appendMermaidNode(technologyTypeHierarchy.getTechnologyTypeGUID(),
                                                  technologyTypeHierarchy.getName(),
                                                  technologyTypeHierarchy.getCategory());

            this.addTechnologyTypeHierarchyNodesMermaidString(mermaidGraphBuilder,
                                                              technologyTypeHierarchy.getSubTypes());

            if (technologyTypeHierarchy.getSubTypes() != null)
            {
                for (TechnologyTypeHierarchy subType : technologyTypeHierarchy.getSubTypes())
                {
                    mermaidGraphBuilder.appendMermaidLine(technologyTypeHierarchy.getTechnologyTypeGUID(),
                                                          null,
                                                          subType.getTechnologyTypeGUID());
                }

                addTechnologyTypeHierarchyLinesMermaidString(mermaidGraphBuilder,
                                                             technologyTypeHierarchy.getSubTypes());
            }


            return mermaidGraphBuilder.getMermaidGraph();
        }

        return null;
    }


    /**
     * Add the nodes to a mermaid hierarchy
     *
     * @param mermaidGraphBuilder graph builder
     * @param technologyTypeHierarchies hierarchy to scan
     */
    private void addTechnologyTypeHierarchyNodesMermaidString(HierarchyMermaidGraphBuilder   mermaidGraphBuilder,
                                                              List<TechnologyTypeHierarchy>  technologyTypeHierarchies)
    {
        if (technologyTypeHierarchies != null)
        {
            for (TechnologyTypeHierarchy technologyTypeHierarchy : technologyTypeHierarchies)
            {
                mermaidGraphBuilder.appendMermaidNode(technologyTypeHierarchy.getTechnologyTypeGUID(),
                                                      technologyTypeHierarchy.getName(),
                                                      technologyTypeHierarchy.getCategory());

                addTechnologyTypeHierarchyNodesMermaidString(mermaidGraphBuilder,
                                                             technologyTypeHierarchy.getSubTypes());
            }
        }
    }


    /**
     * Add the lines to a mermaid hierarchy
     *
     * @param mermaidGraphBuilder graph builder
     * @param technologyTypeHierarchies hierarchy to scan
     */
    private void addTechnologyTypeHierarchyLinesMermaidString(HierarchyMermaidGraphBuilder   mermaidGraphBuilder,
                                                              List<TechnologyTypeHierarchy>  technologyTypeHierarchies)
    {
        if (technologyTypeHierarchies != null)
        {
            for (TechnologyTypeHierarchy technologyTypeHierarchy : technologyTypeHierarchies)
            {
                if (technologyTypeHierarchy.getSubTypes() != null)
                {
                    for (TechnologyTypeHierarchy subType : technologyTypeHierarchy.getSubTypes())
                    {
                        mermaidGraphBuilder.appendMermaidLine(technologyTypeHierarchy.getTechnologyTypeGUID(),
                                                              null,
                                                              subType.getTechnologyTypeGUID());
                    }

                    addTechnologyTypeHierarchyLinesMermaidString(mermaidGraphBuilder,
                                                                 technologyTypeHierarchy.getSubTypes());
                }
            }
        }
    }


    /**
     * Extract the subtypes for a technology type
     *
     * @param userId calling user
     * @param technologyTypeGUID guid of the super type
     * @return subtype list or null
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    private List<TechnologyTypeHierarchy> getSubTypes(String userId,
                                                      String technologyTypeGUID) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getSubTypes";

        RelatedMetadataElementList relatedElements = openHandler.getRelatedMetadataElements(userId,
                                                                                            technologyTypeGUID,
                                                                                            2,
                                                                                            OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeName,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            SequencingOrder.CREATION_DATE_RECENT,
                                                                                            false,
                                                                                            false,
                                                                                            new Date(),
                                                                                            0,
                                                                                            0);

        if ((relatedElements != null) && (relatedElements.getElementList() != null))
        {
            List<TechnologyTypeHierarchy> technologyTypeHierarchies = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    String associationName = propertyHelper.getStringProperty(serviceName,
                                                                              OpenMetadataProperty.ASSOCIATION_NAME.name,
                                                                              relatedMetadataElement.getRelationshipProperties(),
                                                                              methodName);

                    if (OpenMetadataValidValues.VALID_METADATA_VALUE_IS_TYPE_OF.equals(associationName))
                    {
                        TechnologyTypeHierarchy technologyTypeHierarchy = new TechnologyTypeHierarchy(this.convertTechTypeSummary(relatedMetadataElement.getElement()));

                        technologyTypeHierarchy.setSubTypes(this.getSubTypes(userId, relatedMetadataElement.getElement().getElementGUID()));

                        technologyTypeHierarchies.add(technologyTypeHierarchy);
                    }
                }
            }

            if (! technologyTypeHierarchies.isEmpty())
            {
                return technologyTypeHierarchies;
            }
        }

        return null;
    }


    /**
     * Build out a set of search conditions for finding one or more technology types.
     *
     * @param propertyValue value to search for
     * @param operator comparison operator
     * @return search properties
     */
    private SearchProperties getTechnologyTypeConditions(String                     propertyValue,
                                                         PropertyComparisonOperator operator)
    {
        SearchProperties        searchProperties   = new SearchProperties();
        List<PropertyCondition> propertyConditions = new ArrayList<>();

        PropertyCondition          preferredValueCondition = new PropertyCondition();
        PrimitiveTypePropertyValue preferredValue          = new PrimitiveTypePropertyValue();
        preferredValueCondition.setProperty(OpenMetadataProperty.PREFERRED_VALUE.name);
        preferredValueCondition.setOperator(operator);
        preferredValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        preferredValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());
        preferredValue.setPrimitiveValue(propertyValue);
        preferredValueCondition.setValue(preferredValue);
        propertyConditions.add(preferredValueCondition);

        PropertyCondition          scopeCondition  = new PropertyCondition();
        PrimitiveTypePropertyValue scope           = new PrimitiveTypePropertyValue();
        scopeCondition.setProperty(OpenMetadataProperty.SCOPE.name);
        scopeCondition.setOperator(PropertyComparisonOperator.EQ);
        scope.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        scope.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());
        scope.setPrimitiveValue(OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE);
        scopeCondition.setValue(scope);
        propertyConditions.add(scopeCondition);

        PropertyCondition          categoryCondition = new PropertyCondition();
        PrimitiveTypePropertyValue category          = new PrimitiveTypePropertyValue();
        categoryCondition.setProperty(OpenMetadataProperty.CATEGORY.name);
        categoryCondition.setOperator(PropertyComparisonOperator.LIKE);
        category.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        category.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());
        category.setPrimitiveValue(".*deployedImplementationType");
        categoryCondition.setValue(category);
        propertyConditions.add(categoryCondition);

        searchProperties.setConditions(propertyConditions);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);

        return searchProperties;
    }


    /**
     * Retrieve detailed information about a specific technology type.  This includes
     * templates, connectors and external references.
     *
     * @param userId calling user
     * @param technologyTypeName string value to look for
     * @param getTemplates boolean indicating whether templates or non-template elements should be returned.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param effectiveTime the effective date/time to use for the query
     * @param limitResultsByStatus limit the status values that the elements should be
     * @param asOfTime repository time
     * @param sequencingOrder order of results
     * @param sequencingProperty optional property name
     *
     * @return detailed report for technology type
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ReferenceableElement> getTechnologyTypeElements(String              userId,
                                                                String              technologyTypeName,
                                                                boolean             getTemplates,
                                                                int                 startFrom,
                                                                int                 pageSize,
                                                                Date                effectiveTime,
                                                                List<ElementStatus> limitResultsByStatus,
                                                                Date                asOfTime,
                                                                SequencingOrder     sequencingOrder,
                                                                String              sequencingProperty) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        final String methodName = "getTechnologyTypeElements";
        final String parameterName = "technologyTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(technologyTypeName, parameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);



        List<OpenMetadataElement> openMetadataElements = openHandler.findMetadataElements(userId,
                                                                                          OpenMetadataType.REFERENCEABLE.typeName,
                                                                                          null,
                                                                                          propertyHelper.getSearchPropertiesByName(propertyNames, technologyTypeName, PropertyComparisonOperator.EQ),
                                                                                          limitResultsByStatus,
                                                                                          asOfTime,
                                                                                          this.getMatchClassifications(getTemplates),
                                                                                          sequencingProperty,
                                                                                          sequencingOrder,
                                                                                          false,
                                                                                          false,
                                                                                          effectiveTime,
                                                                                          startFrom,
                                                                                          pageSize);

        return this.convertReferenceables(openMetadataElements, getTemplates);
    }


    /**
     * Build a descriptions of the classification to look for.
     *
     * @param getTemplates look for templates?
     * @return match classifications
     */
    private SearchClassifications getMatchClassifications(boolean getTemplates)
    {
        SearchClassifications matchClassifications = null;

        if (getTemplates)
        {
            /*
             * Attempt to retrieve only elements that have the template classification.
             */
            matchClassifications = new SearchClassifications();

            List<ClassificationCondition> classificationConditions  = new ArrayList<>();
            ClassificationCondition classificationCondition = new ClassificationCondition();
            classificationCondition.setName(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName);
            classificationConditions.add(classificationCondition);
            matchClassifications.setConditions(classificationConditions);
            matchClassifications.setMatchCriteria(MatchCriteria.ALL);
        }

        return matchClassifications;
    }


    /**
     * Convert open metadata objects from the OpenMetadataClient to local beans.
     *
     * @param openMetadataElements retrieved elements
     * @param methodName calling method
     *
     * @return list of validValue elements
     *
     * @throws PropertyServerException the repository is not available or not working properly.
     * */
    private List<TechnologyTypeSummary> convertTechTypeSummaries(List<OpenMetadataElement>  openMetadataElements,
                                                                 String                     methodName) throws PropertyServerException
    {
        TechnologyTypeSummaryConverter<TechnologyTypeSummary> converter = new TechnologyTypeSummaryConverter<>(propertyHelper, serviceName, serverName);

        if (openMetadataElements != null)
        {
            List<TechnologyTypeSummary> technologyTypeSummaries = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    TechnologyTypeSummary technologyTypeSummary = converter.getNewBean(TechnologyTypeSummary.class, openMetadataElement, methodName);

                    if (technologyTypeSummary != null)
                    {
                        technologyTypeSummaries.add(technologyTypeSummary);
                    }
                }
            }

            return technologyTypeSummaries;
        }

        return null;
    }


    /**
     * Convert open metadata object from the OpenMetadataClient to local bean.
     *
     * @param openMetadataElement retrieved element
     *
     * @return technology type summary
     *
     * @throws PropertyServerException the repository is not available or not working properly.
     * */
    private TechnologyTypeSummary convertTechTypeSummary(OpenMetadataElement openMetadataElement) throws PropertyServerException
    {
        final String methodName = "convertTechTypeSummary";

        TechnologyTypeSummaryConverter<TechnologyTypeSummary> converter = new TechnologyTypeSummaryConverter<>(propertyHelper, serviceName, serverName);

        if (openMetadataElement != null)
        {
            return converter.getNewBean(TechnologyTypeSummary.class, openMetadataElement, methodName);


        }

        return null;
    }


    /**
     * Convert open metadata objects from the OpenMetadataClient to local beans.
     *
     * @param openMetadataElements retrieved elements
     * @param getTemplates boolean indicating whether templates or non-template platforms should be returned.
     *
     * @return list of validValue elements
     *
     * @throws PropertyServerException the repository is not available or not working properly.
     * */
    private List<ReferenceableElement> convertReferenceables(List<OpenMetadataElement>  openMetadataElements,
                                                             boolean                    getTemplates) throws PropertyServerException
    {
        final String methodName = "convertReferenceables";

        ReferenceableConverter<ReferenceableElement> converter = new ReferenceableConverter<>(propertyHelper, serviceName, serverName);

        if (openMetadataElements != null)
        {
            List<ReferenceableElement> referenceableElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    boolean isTemplate = this.isTemplate(openMetadataElement);
                    if (isTemplate && (getTemplates))
                    {
                        ReferenceableElement referenceableElement = converter.getNewBean(ReferenceableElement.class, openMetadataElement, methodName);

                        if (referenceableElement != null)
                        {
                            referenceableElements.add(referenceableElement);
                        }
                    }

                    if ((! isTemplate) && (! getTemplates))
                    {
                        ReferenceableElement referenceableElement = converter.getNewBean(ReferenceableElement.class, openMetadataElement, methodName);

                        if (referenceableElement != null)
                        {
                            referenceableElements.add(referenceableElement);
                        }
                    }
                }
            }

            return referenceableElements;
        }

        return null;
    }


    /**
     * Determine whether the element is a template.
     *
     * @param openMetadataElement element header
     * @return boolean flag
     */
    private boolean isTemplate(OpenMetadataElement openMetadataElement)
    {
        if (openMetadataElement.getClassifications() != null)
        {
            for (AttachedClassification classification : openMetadataElement.getClassifications())
            {
                if (classification != null)
                {
                    if (classification.getClassificationName().equals(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
