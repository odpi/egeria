/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.automatedcuration.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.OpenMetadataHandlerBase;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.HierarchyMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ReferenceableElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.viewservices.automatedcuration.converters.ReferenceableConverter;
import org.odpi.openmetadata.viewservices.automatedcuration.converters.TechnologyTypeSummaryConverter;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.CatalogTemplate;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.TechnologyTypeHierarchy;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.TechnologyTypeReport;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.TechnologyTypeSummary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides additional support for retrieving technology types
 */
public class TechnologyTypeHandler extends OpenMetadataHandlerBase
{
    private static final PropertyHelper          propertyHelper          = new PropertyHelper();
    private static final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    

    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public TechnologyTypeHandler(String             localServerName,
                                 AuditLog           auditLog,
                                 String             localServiceName,
                                 OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.VALID_VALUE_DEFINITION.typeName);
    }


    /**
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param userId calling user
     * @param searchString string value to look for
     * @param searchOptions multiple options to control the query

     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<TechnologyTypeSummary> findTechnologyTypes(String        userId,
                                                           String        searchString,
                                                           SearchOptions searchOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "findTechnologyTypes";

        SearchOptions workingSearchOptions = new SearchOptions(searchOptions);

        workingSearchOptions.setMetadataElementTypeName(OpenMetadataType.VALID_VALUE_DEFINITION.typeName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId, searchString, workingSearchOptions);

        return this.convertTechTypeSummaries(openMetadataElements, methodName);
    }


    /**
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param userId calling user
     * @param typeName type name value to look for
     * @param queryOptions multiple options to control the query
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<TechnologyTypeSummary> getTechnologyTypesForOpenMetadataType(String       userId,
                                                                             String       typeName,
                                                                             QueryOptions queryOptions) throws InvalidParameterException,
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

        QueryOptions workingQueryOptions = new QueryOptions(queryOptions);

        workingQueryOptions.setMetadataElementTypeName(OpenMetadataType.VALID_VALUE_DEFINITION.typeName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElements(userId,
                                                                                          searchProperties,
                                                                                          null,
                                                                                          workingQueryOptions);

        return this.convertTechTypeSummaries(openMetadataElements, methodName);
    }


    /**
     * Retrieve detailed information about a specific technology type.  This includes
     * templates, connectors and external references.
     *
     * @param userId calling user
     * @param technologyTypeName string value to look for
     * @param queryOptions multiple options to control the query
     *
     * @return detailed report for technology type
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public TechnologyTypeReport getTechnologyTypeDetail(String       userId,
                                                        String       technologyTypeName,
                                                        QueryOptions queryOptions) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "getTechnologyTypeDetail";
        final String parameterName = "technologyTypeName";

        invalidParameterHandler.validateName(technologyTypeName, parameterName, methodName);

        QueryOptions workingQueryOptions = new QueryOptions(queryOptions);

        workingQueryOptions.setMetadataElementTypeName(OpenMetadataType.VALID_VALUE_DEFINITION.typeName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElements(userId,
                                                                                          this.getTechnologyTypeConditions(technologyTypeName, PropertyComparisonOperator.EQ),
                                                                                          null,
                                                                                          workingQueryOptions);

        if (openMetadataElements != null)
        {
            TechnologyTypeReport report = null;

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    report = new TechnologyTypeReport(this.convertTechTypeSummary(openMetadataElement));

                    workingQueryOptions.setMetadataElementTypeName(null);

                    RelatedMetadataElementList relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                                   openMetadataElement.getElementGUID(),
                                                                                                                   1,
                                                                                                                   null,
                                                                                                                   workingQueryOptions);


                    if ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
                    {
                        List<RelatedMetadataElementSummary> resources = new ArrayList<>();
                        List<CatalogTemplate>               catalogTemplates = new ArrayList<>();
                        List<RelatedMetadataElementSummary> externalReferences = new ArrayList<>();

                        for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                        {
                            if (relatedMetadataElement != null)
                            {
                                if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName))
                                {
                                    resources.add(propertyHelper.getRelatedElementSummary(relatedMetadataElement));
                                }
                                else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName))
                                {
                                    CatalogTemplate catalogTemplate = new CatalogTemplate();

                                    catalogTemplate.setRelatedElement(propertyHelper.getMetadataElementSummary(relatedMetadataElement.getElement()));

                                    if (catalogTemplate.getRelatedElement().getElementHeader().getTemplate() != null)
                                    {
                                        if (catalogTemplate.getRelatedElement().getElementHeader().getTemplate().getClassificationProperties() instanceof TemplateProperties templateProperties)
                                        {
                                            catalogTemplate.setName(templateProperties.getDisplayName());
                                            catalogTemplate.setDescription(templateProperties.getDescription());
                                            catalogTemplate.setVersionIdentifier(templateProperties.getVersionIdentifier());
                                        }
                                    }

                                    catalogTemplate.setSpecification(openMetadataClient.getSpecification(userId, relatedMetadataElement.getElement().getElementGUID()));

                                    catalogTemplates.add(catalogTemplate);
                                }
                                else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName))
                                {
                                    externalReferences.add(propertyHelper.getRelatedElementSummary(relatedMetadataElement));
                                }
                            }
                        }

                        if (!resources.isEmpty())
                        {
                            report.setResourceList(resources);
                        }

                        if (!catalogTemplates.isEmpty())
                        {
                            report.setCatalogTemplates(catalogTemplates);
                        }

                        if (!externalReferences.isEmpty())
                        {
                            report.setExternalReferences(externalReferences);
                        }
                    }
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
     * @param queryOptions multiple options to control the query
     *
     * @return detailed report for technology type
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public TechnologyTypeHierarchy getTechnologyTypeHierarchy(String       userId,
                                                              String       technologyTypeName,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "getTechnologyTypeHierarchy";
        final String parameterName = "technologyTypeName";

        invalidParameterHandler.validateName(technologyTypeName, parameterName, methodName);

        QueryOptions workingQueryOptions = new QueryOptions(queryOptions);

        workingQueryOptions.setMetadataElementTypeName(OpenMetadataType.VALID_VALUE_DEFINITION.typeName);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElements(userId,
                                                                                          this.getTechnologyTypeConditions(technologyTypeName, PropertyComparisonOperator.EQ),
                                                                                          null,
                                                                                          workingQueryOptions);

        if (openMetadataElements != null)
        {
            TechnologyTypeHierarchy technologyTypeHierarchy = null;

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    technologyTypeHierarchy = new TechnologyTypeHierarchy(this.convertTechTypeSummary(openMetadataElement));

                    technologyTypeHierarchy.setSubTypes(this.getSubTypes(userId,
                                                                         openMetadataElement.getElementGUID(),
                                                                         queryOptions));

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
                                                                                                technologyTypeHierarchy.getDisplayName());

            if (technologyTypeHierarchy.getCategory() != null)
            {
                mermaidGraphBuilder.appendMermaidNode(technologyTypeHierarchy.getTechnologyTypeGUID(),
                                                      technologyTypeHierarchy.getDisplayName(),
                                                      technologyTypeHierarchy.getCategory());
            }
            else
            {
                {
                    mermaidGraphBuilder.appendMermaidNode(technologyTypeHierarchy.getTechnologyTypeGUID(),
                                                          technologyTypeHierarchy.getDisplayName(),
                                                          "TechnologyType");
                }
            }

            this.addTechnologyTypeHierarchyNodesMermaidString(mermaidGraphBuilder,
                                                              technologyTypeHierarchy.getSubTypes());

            if (technologyTypeHierarchy.getSubTypes() != null)
            {
                for (TechnologyTypeHierarchy subType : technologyTypeHierarchy.getSubTypes())
                {
                    mermaidGraphBuilder.appendMermaidLine(null,
                                                          technologyTypeHierarchy.getTechnologyTypeGUID(),
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
                                                      technologyTypeHierarchy.getDisplayName(),
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
                        mermaidGraphBuilder.appendMermaidLine(null,
                                                              technologyTypeHierarchy.getTechnologyTypeGUID(),
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
     * @param queryOptions multiple options to control the query
     * @return subtype list or null
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    private List<TechnologyTypeHierarchy> getSubTypes(String       userId,
                                                      String       technologyTypeGUID,
                                                      QueryOptions queryOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getSubTypes";

        RelatedMetadataElementList relatedElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                            technologyTypeGUID,
                                                                                            2,
                                                                                            OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeName,
                                                                                            queryOptions);

        if ((relatedElements != null) && (relatedElements.getElementList() != null))
        {
            List<TechnologyTypeHierarchy> technologyTypeHierarchies = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    String associationName = propertyHelper.getStringProperty(localServiceName,
                                                                              OpenMetadataProperty.ASSOCIATION_NAME.name,
                                                                              relatedMetadataElement.getRelationshipProperties(),
                                                                              methodName);

                    if (OpenMetadataValidValues.VALID_METADATA_VALUE_IS_TYPE_OF.equals(associationName))
                    {
                        TechnologyTypeHierarchy technologyTypeHierarchy = new TechnologyTypeHierarchy(this.convertTechTypeSummary(relatedMetadataElement.getElement()));

                        technologyTypeHierarchy.setSubTypes(this.getSubTypes(userId,
                                                                             relatedMetadataElement.getElement().getElementGUID(),
                                                                             queryOptions));

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

        if (propertyValue != null)
        {
            PropertyCondition          preferredValueCondition = new PropertyCondition();
            PrimitiveTypePropertyValue preferredValue          = new PrimitiveTypePropertyValue();
            preferredValueCondition.setProperty(OpenMetadataProperty.PREFERRED_VALUE.name);
            preferredValueCondition.setOperator(operator);
            preferredValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
            preferredValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());
            preferredValue.setPrimitiveValue(propertyValue);
            preferredValueCondition.setValue(preferredValue);
            propertyConditions.add(preferredValueCondition);
        }

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
        categoryCondition.setProperty(OpenMetadataProperty.NAMESPACE.name);
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
     * @param queryOptions multiple options to control the query
     *
     * @return detailed report for technology type
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<OpenMetadataRootElement> getTechnologyTypeElements(String              userId,
                                                                   String              technologyTypeName,
                                                                   QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "getTechnologyTypeElements";
        final String parameterName = "technologyTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(technologyTypeName, parameterName, methodName);

        return super.getRootElementsByName(userId,
                                           technologyTypeName,
                                           Collections.singletonList(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name),
                                           queryOptions,
                                           methodName);
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
        TechnologyTypeSummaryConverter<TechnologyTypeSummary> converter = new TechnologyTypeSummaryConverter<>(propertyHelper, localServiceName, localServerName);

        if (openMetadataElements != null)
        {
            List<TechnologyTypeSummary> technologyTypeSummaries = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    String namespace = propertyHelper.getStringProperty(localServiceName, OpenMetadataProperty.NAMESPACE.name, openMetadataElement.getElementProperties(), methodName);
                    String qualifiedName = propertyHelper.getStringProperty(localServiceName, OpenMetadataProperty.QUALIFIED_NAME.name, openMetadataElement.getElementProperties(), methodName);

                    if ((namespace != null) && (namespace.contains(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name)) && (qualifiedName != null) && (qualifiedName.contains("(") && (qualifiedName.contains(")"))))
                    {
                        TechnologyTypeSummary technologyTypeSummary = converter.getNewBean(TechnologyTypeSummary.class, openMetadataElement, methodName);

                        if (technologyTypeSummary != null)
                        {
                            technologyTypeSummaries.add(technologyTypeSummary);
                        }
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

        TechnologyTypeSummaryConverter<TechnologyTypeSummary> converter = new TechnologyTypeSummaryConverter<>(propertyHelper, localServiceName, localServerName);

        if (openMetadataElement != null)
        {
            return converter.getNewBean(TechnologyTypeSummary.class, openMetadataElement, methodName);


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
