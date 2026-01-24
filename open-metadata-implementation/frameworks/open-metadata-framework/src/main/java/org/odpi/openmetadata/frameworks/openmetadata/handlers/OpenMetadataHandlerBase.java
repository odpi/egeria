/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataClassificationBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataElementBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataRelationshipBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.converters.OpenMetadataRootConverter;
import org.odpi.openmetadata.frameworks.openmetadata.converters.SpecificationPropertyConverter;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.OpenMetadataRootMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.SolutionBlueprintMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.SolutionComponentMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.SpecificationMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataHierarchySummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CertificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.SpecificationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * OpenMetadataHandlerBase provides a base class for the handlers.
 */
public class OpenMetadataHandlerBase
{
    protected final OpenMetadataClient openMetadataClient;
    protected final AuditLog           auditLog;

    protected final PropertyHelper                    propertyHelper        = new PropertyHelper();
    protected final OpenMetadataElementBuilder        elementBuilder        = new OpenMetadataElementBuilder();
    protected final OpenMetadataClassificationBuilder classificationBuilder = new OpenMetadataClassificationBuilder();
    protected final OpenMetadataRelationshipBuilder   relationshipBuilder   = new OpenMetadataRelationshipBuilder();

    protected final String localServiceName;
    protected final String localServerName;

    protected final String metadataElementTypeName;


    /**
     * Create a new handler.
     *
     * @param localServerName         name of this server (view server)
     * @param auditLog                logging destination
     * @param localServiceName        local service name
     * @param openMetadataClient      access to open metadata
     * @param metadataElementTypeName type of principle element
     */
    public OpenMetadataHandlerBase(String             localServerName,
                                   AuditLog           auditLog,
                                   String             localServiceName,
                                   OpenMetadataClient openMetadataClient,
                                   String             metadataElementTypeName)
    {
        this.openMetadataClient      = openMetadataClient;
        this.auditLog                = auditLog;
        this.localServerName         = localServerName;
        this.localServiceName        = localServiceName;
        this.metadataElementTypeName = metadataElementTypeName;
    }


    /**
     * Create a new handler and override the principle type.
     *
     * @param template elements to copy
     * @param metadataElementTypeName type of principle element - overrides value from template
     */
    public OpenMetadataHandlerBase(OpenMetadataHandlerBase template,
                                   String                  metadataElementTypeName)
    {
        assert (template != null);

        this.openMetadataClient      = template.openMetadataClient;
        this.auditLog                = template.auditLog;
        this.localServerName         = template.localServerName;
        this.localServiceName        = template.localServiceName;
        this.metadataElementTypeName = metadataElementTypeName;
    }


    /**
     * Create a new handler.
     *
     * @param template elements to copy
     */
    public OpenMetadataHandlerBase(OpenMetadataHandlerBase template)
    {
        assert (template != null);

        this.openMetadataClient      = template.openMetadataClient;
        this.auditLog                = template.auditLog;
        this.localServerName         = template.localServerName;
        this.localServiceName        = template.localServiceName;
        this.metadataElementTypeName = template.metadataElementTypeName;
    }


    /**
     * Return the default query options.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @return default values
     */
    public QueryOptions getQueryOptions(int startFrom,
                                        int pageSize)
    {
        QueryOptions queryOptions =  new QueryOptions();

        queryOptions.setStartFrom(startFrom);
        queryOptions.setPageSize(pageSize);

        return queryOptions;
    }


    /**
     * Set up the default type name if nothing is supplied by the caller.
     *
     * @param suppliedOptions options from caller
     * @return updated options
     */
    public GetOptions addDefaultType(GetOptions suppliedOptions)
    {
        GetOptions getOptions = new GetOptions(suppliedOptions);

        if (getOptions.getMetadataElementTypeName() == null)
        {
            getOptions.setMetadataElementTypeName(metadataElementTypeName);
        }

        return getOptions;
    }


    /**
     * Set up the default type name if nothing is supplied by the caller.
     *
     * @param suppliedOptions options from caller
     * @return updated options
     */
    public QueryOptions addDefaultType(QueryOptions suppliedOptions)
    {
        QueryOptions queryOptions = new QueryOptions(suppliedOptions);

        if (queryOptions.getMetadataElementTypeName() == null)
        {
            queryOptions.setMetadataElementTypeName(metadataElementTypeName);
        }

        return queryOptions;
    }


    /**
     * Set up the default type name if nothing is supplied by the caller.
     *
     * @param suppliedOptions options from caller
     * @return updated options
     */
    public SearchOptions addDefaultType(SearchOptions suppliedOptions)
    {
        SearchOptions searchOptions = new SearchOptions(suppliedOptions);

        if (searchOptions.getMetadataElementTypeName() == null)
        {
            searchOptions.setMetadataElementTypeName(metadataElementTypeName);
        }

        return searchOptions;
    }

    /**
     * Return the default query options.
     *
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @return default values
     */
    public QueryOptions getQueryOptions(String       metadataElementTypeName,
                                        int          startFrom,
                                        int          pageSize)
    {
        QueryOptions queryOptions = this.getQueryOptions(startFrom, pageSize);

        queryOptions.setMetadataElementTypeName(metadataElementTypeName);

        return queryOptions;
    }


    /**
     * Return the default query options.
     *
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                           include in the search results. Null means all subtypes.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @return default values
     */
    public QueryOptions getQueryOptions(String       metadataElementTypeName,
                                        List<String> metadataElementSubtypeNames,
                                        int          startFrom,
                                        int          pageSize)
    {
        QueryOptions queryOptions = this.getQueryOptions(metadataElementTypeName, startFrom, pageSize);

        queryOptions.setMetadataElementSubtypeNames(metadataElementSubtypeNames);

        return queryOptions;
    }


    /**
     * Create a new element.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions            details of the element to create
     * @param initialClassifications       map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param methodName                   calling method
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected String createNewElement(String                                userId,
                                      NewElementOptions                     newElementOptions,
                                      Map<String, ClassificationProperties> initialClassifications,
                                      OpenMetadataRootProperties            properties,
                                      RelationshipProperties                parentRelationshipProperties,
                                      String                                methodName) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String propertiesName             = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateObject(properties, propertiesName, methodName);

        if (properties instanceof ReferenceableProperties referenceableProperties)
        {
            propertyHelper.validateMandatoryName(referenceableProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        String typeName = metadataElementTypeName;

        if ((properties != null) && (properties.getTypeName() != null))
        {
            typeName = properties.getTypeName();
        }

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               typeName,
                                                               newElementOptions,
                                                               classificationBuilder.getInitialClassifications(initialClassifications),
                                                               elementBuilder.getNewElementProperties(properties),
                                                               relationshipBuilder.getNewElementProperties(parentRelationshipProperties));
    }


    /**
     * Create a new metadata element using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param userId                       calling user
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                                     connection etc)
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    protected String createElementFromTemplate(String                 userId,
                                               TemplateOptions        templateOptions,
                                               String                 templateGUID,
                                               EntityProperties       replacementProperties,
                                               Map<String, String>    placeholderProperties,
                                               RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName = "createElementFromTemplate";

        if ((replacementProperties == null) && ((placeholderProperties == null) || (placeholderProperties.isEmpty())))
        {
            throw new InvalidParameterException(OMFErrorCode.NULL_TEMPLATE_INSERTS.getMessageDefinition(metadataElementTypeName),
                                                this.getClass().getName(),
                                                methodName,
                                                "replacementAttributes/placeholderProperties");
        }

        return openMetadataClient.createMetadataElementFromTemplate(userId,
                                                                    metadataElementTypeName,
                                                                    templateOptions,
                                                                    templateGUID,
                                                                    elementBuilder.getElementProperties(replacementProperties),
                                                                    placeholderProperties,
                                                                    relationshipBuilder.getNewElementProperties(parentRelationshipProperties));
    }


    /**
     * Update the properties of an element.
     *
     * @param userId            userId of user making request.
     * @param elementGUID       unique identifier of the element (returned from create)
     * @param guidParameterName name of unique identifier
     * @param updateOptions     provides a structure for the additional options when updating an element.
     * @param properties        properties for the element
     *
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected boolean updateElement(String                     userId,
                                    String                     elementGUID,
                                    String                     guidParameterName,
                                    UpdateOptions              updateOptions,
                                    OpenMetadataRootProperties properties,
                                    String                     methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String propertiesName             = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesName, methodName);

        if ((updateOptions == null) || (!updateOptions.getMergeUpdate()))
        {
            if (properties instanceof ReferenceableProperties referenceableProperties)
            {
                propertyHelper.validateMandatoryName(referenceableProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
            }
        }

        return openMetadataClient.updateMetadataElementInStore(userId,
                                                               elementGUID,
                                                               updateOptions,
                                                               elementBuilder.getElementProperties(properties));
    }


    /**
     * Retrieve the related elements for an item and filter for relevant relationships.  If graphQueryDepth
     * is zero or less, do not retrieve relationships.
     *
     * @param userId                   calling user
     * @param openMetadataElement      element extracted from the repository
     * @param queryOptions             multiple options to control the query
     * @return list of all the relevant related elements of the open metadata element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected List<RelatedMetadataElement> getElementRelatedElements(String              userId,
                                                                     OpenMetadataElement openMetadataElement,
                                                                     QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        List<RelatedMetadataElement> relatedMetadataElements = new ArrayList<>();

        QueryOptions workingQueryOptions = new QueryOptions(queryOptions);
        workingQueryOptions.setMetadataElementTypeName(OpenMetadataType.OPEN_METADATA_ROOT.typeName); // All types of entities

        if (queryOptions.getGraphQueryDepth() > 0)
        {
            workingQueryOptions.setStartFrom(0);
            workingQueryOptions.setPageSize(queryOptions.getRelationshipsPageSize());

            RelatedMetadataElementList relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                                  openMetadataElement.getElementGUID(),
                                                                                                                  0,
                                                                                                                  null,
                                                                                                                  workingQueryOptions);
            if ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                relatedMetadataElements.addAll(this.getRelevantRelationships(relatedMetadataElementList.getElementList(), queryOptions));
            }

            return relatedMetadataElements;
        }

        return null;
    }


    /**
     * Remove any relationships that should not be included.
     *
     * @param retrievedRelationships   list of retrieved
     * @return relevant relationships
     */
    List<RelatedMetadataElement> getRelevantRelationships(List<RelatedMetadataElement> retrievedRelationships,
                                                          GetOptions                   getOptions)
    {
        if (getOptions == null)
        {
            return retrievedRelationships;
        }

        if ((getOptions.getSkipRelationships() == null) && (getOptions.getIncludeOnlyRelationships() == null))
        {
            return retrievedRelationships;
        }

        List<RelatedMetadataElement> relevantRelationships = new ArrayList<>();

        if (getOptions.getIncludeOnlyRelationships() != null)
        {

            for (RelatedMetadataElement relatedMetadataElement : retrievedRelationships)
            {
                if (relatedMetadataElement != null)
                {
                    for (String relationshipType : getOptions.getIncludeOnlyRelationships())
                    {
                        if (propertyHelper.isTypeOf(relatedMetadataElement, relationshipType))
                        {
                            relevantRelationships.add(relatedMetadataElement);
                        }
                    }
                }
            }
        }
        else
        {
            for (RelatedMetadataElement relatedMetadataElement : retrievedRelationships)
            {
                boolean keepRelationship = true;

                if (relatedMetadataElement != null)
                {
                    for (String relationshipType : getOptions.getSkipRelationships())
                    {
                        if (propertyHelper.isTypeOf(relatedMetadataElement, relationshipType))
                        {
                            keepRelationship = false;
                        }
                    }
                }

                if (keepRelationship)
                {
                    relevantRelationships.add(relatedMetadataElement);
                }
            }
        }

        return relevantRelationships;
    }


    /**
     * Retrieve the context in which this component is used.
     *
     * @param userId               caller
     * @param initialParentElement first parent element
     * @param queryOptions         multiple options to control the query
     * @param methodName           calling method
     * @return list of parent elements (including the starting one)
     * @throws PropertyServerException problem with the conversion process
     */
    protected List<RelatedMetadataElement> getFullParentContext(String                 userId,
                                                                RelatedMetadataElement initialParentElement,
                                                                QueryOptions           queryOptions,
                                                                String                 methodName) throws PropertyServerException
    {
        List<RelatedMetadataElement> fullParentContext = new ArrayList<>();

        fullParentContext.add(initialParentElement);

        try
        {
            RelatedMetadataElementList additionalParents = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                         initialParentElement.getElement().getElementGUID(),
                                                                                                         2,
                                                                                                         null,
                                                                                                         queryOptions);

            if ((additionalParents != null) && (additionalParents.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : additionalParents.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName)) && relatedMetadataElement.getElementAtEnd1())
                        {
                            fullParentContext.add(relatedMetadataElement);
                        }
                        else
                        {
                            if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName))
                            {
                                List<RelatedMetadataElement> higherParents = this.getFullParentContext(userId, relatedMetadataElement, queryOptions, methodName);

                                fullParentContext.addAll(higherParents);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OMFAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                       methodName,
                                                                                                       localServiceName,
                                                                                                       error.getMessage()),
                                      error);
            }

            throw new PropertyServerException(OMFErrorCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               localServiceName,
                                                                                                               error.getMessage()),
                                              error.getClass().getName(),
                                              methodName,
                                              error);
        }

        return fullParentContext;
    }

    /*
     * Mapping functions
     */


    /**
     * Convert the open metadata elements retrieve into root elements.
     *
     * @param userId                   calling user
     * @param openMetadataElements     elements extracted from the repository
     * @param queryOptions             multiple options to control the query
     * @param methodName               calling method
     * @return list of root elements (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    protected List<OpenMetadataRootElement> convertRootElements(String                    userId,
                                                                List<OpenMetadataElement> openMetadataElements,
                                                                QueryOptions              queryOptions,
                                                                String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<OpenMetadataRootElement> openMetadataRootElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    OpenMetadataRootElement openMetadataRootElement = convertRootElement(userId,
                                                                                         openMetadataElement,
                                                                                         queryOptions,
                                                                                         methodName);
                    if (openMetadataRootElement != null)
                    {
                        openMetadataRootElements.add(openMetadataRootElement);
                    }
                }
            }

            return openMetadataRootElements;
        }

        return null;
    }


    /**
     * Convert the open metadata elements retrieve into actor role elements.
     *
     * @param userId calling user
     * @param relatedMetadataElementList elements extracted from the repository
     * @param queryOptions multiple options to control the query
     * @param methodName calling method
     * @return list of actor roles (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    List<OpenMetadataRootElement> convertRelatedRootElements(String                     userId,
                                                             RelatedMetadataElementList relatedMetadataElementList,
                                                             QueryOptions               queryOptions,
                                                             String                     methodName) throws PropertyServerException
    {
        if ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
        {
            return convertRelatedRootElements(userId, relatedMetadataElementList.getElementList(), queryOptions, methodName);
        }

        return null;
    }


    /**
     * Convert the open metadata elements retrieve into actor role elements.
     *
     * @param userId calling user
     * @param relatedMetadataElements elements extracted from the repository
     * @param queryOptions multiple options to control the query
     * @param methodName calling method
     * @return list of actor roles (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    List<OpenMetadataRootElement> convertRelatedRootElements(String                       userId,
                                                             List<RelatedMetadataElement> relatedMetadataElements,
                                                             QueryOptions                 queryOptions,
                                                             String                       methodName) throws PropertyServerException
    {
        if (relatedMetadataElements != null)
        {
            List<OpenMetadataRootElement> rootElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    OpenMetadataRootElement rootElement = convertRootElement(userId,
                                                                             relatedMetadataElement,
                                                                             queryOptions,
                                                                             methodName);
                    if (rootElement != null)
                    {
                        /*
                         * Only save the roles that either inherit from SolutionActorRole or are linked to solution components.
                         */
                        rootElements.add(rootElement);
                    }
                }
            }

            return rootElements;
        }

        return null;
    }



    /**
     * Return the generic element extracted from the open metadata element plus linked elements.
     *
     * @param userId                   calling user
     * @param openMetadataElement      element extracted from the repository
     * @param suppliedQueryOptions             multiple options to control the query
     * @param methodName               calling method
     * @return bean or null
     * @throws PropertyServerException problem with the conversion process
     */
    protected OpenMetadataRootElement convertRootElement(String              userId,
                                                         OpenMetadataElement openMetadataElement,
                                                         QueryOptions        suppliedQueryOptions,
                                                         String              methodName) throws PropertyServerException
    {
        QueryOptions queryOptions = new QueryOptions(suppliedQueryOptions);

        if (filterBySubtypes(openMetadataElement, queryOptions) &&
                filterByClassifications(userId, openMetadataElement, queryOptions))
        {
            try
            {
                OpenMetadataRootConverter<OpenMetadataRootElement> converter = new OpenMetadataRootConverter<>(propertyHelper, localServiceName, localServerName);

                OpenMetadataRootElement rootElement = converter.getNewComplexBean(OpenMetadataRootElement.class,
                                                                                  openMetadataElement,
                                                                                  this.getElementRelatedElements(userId, openMetadataElement, queryOptions),
                                                                                  methodName);

                return populateRootElement(userId, rootElement, queryOptions);
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          OMFAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                           methodName,
                                                                                                           localServiceName,
                                                                                                           error.getMessage()),
                                          error);
                }

                throw new PropertyServerException(OMFErrorCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                   methodName,
                                                                                                                   localServiceName,
                                                                                                                   error.getMessage()),
                                                  error.getClass().getName(),
                                                  methodName,
                                                  error);
            }
        }

        return null;
    }


    /**
     * Retrieve details of a requested actor, unless element has already been retrieved.
     *
     * @param userId calling user
     * @param actorName name of actor
     * @param actorPropertyName property name it represents
     * @param actorMap map of other actors already received
     * @param queryOptions               multiple options to control the query
     */
    private void getActorSummary(String                              userId,
                                 String                              actorName,
                                 String                              actorPropertyName,
                                 Map<String, MetadataElementSummary> actorMap,
                                 QueryOptions                        queryOptions)
    {
        if (actorName != null)
        {
            if (! actorMap.containsKey(actorName))
            {
                OpenMetadataElement actorElement;

                if ((actorPropertyName == null) || (actorPropertyName.equals(OpenMetadataProperty.GUID.name)))
                {
                    try
                    {
                        actorElement = openMetadataClient.getMetadataElementByGUID(userId, actorName, queryOptions);
                    }
                    catch (Exception exception)
                    {
                        actorElement = null;
                    }
                }
                else
                {
                    try
                    {
                        actorElement = openMetadataClient.getMetadataElementByUniqueName(userId, actorName, actorPropertyName, queryOptions);
                    }
                    catch (Exception exception)
                    {
                        actorElement = null;
                    }
                }

                if (actorElement != null)
                {
                    MetadataElementSummary actorSummary = propertyHelper.getMetadataElementSummary(actorElement);

                    actorMap.put(actorName, actorSummary);
                }
            }
        }
    }


    /**
     * Check whether the subtype list provided by the caller prevents an element from being returned.
     * Many calls do this filtering at the repository level, but not all - yet :)
     *
     * @param element header of the element
     * @param getOptions options from the caller
     * @return boolean - true to return element
     */
    private boolean filterBySubtypes(OpenMetadataElement element,
                                     GetOptions          getOptions)
    {
        if (element == null)
        {
            return false;
        }

        if ((getOptions != null) && (getOptions.getMetadataElementSubtypeNames() != null))
        {
            for (String typeName : getOptions.getMetadataElementSubtypeNames())
            {
                if ((typeName != null) && (! propertyHelper.isTypeOf(element, typeName)))
                {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * The aim is to push down the filtering requirements to the repository as much as possible.  This
     * function is a last check to make sure the classification requirements have been fulfilled.
     *
     * @param retrievedElement element to test
     * @param queryOptions requested options
     * @return boolean; true if element is to be returned
     */
    private boolean filterByClassifications(String              userId,
                                            OpenMetadataElement retrievedElement,
                                            QueryOptions        queryOptions)
    {
        final String methodName = "filterByClassifications";

        if ((queryOptions != null) && (retrievedElement != null))
        {
            if (queryOptions.getSkipClassifiedElements() != null)
            {
                /*
                 * Ignore elements that have the forbidden classifications
                 */
                for (String forbiddenClassificationName : queryOptions.getSkipClassifiedElements())
                {
                    if (forbiddenClassificationName != null)
                    {
                        AttachedClassification classification = propertyHelper.getClassification(retrievedElement, forbiddenClassificationName);

                        if (classification != null)
                        {
                            return false;
                        }
                    }
                }
            }

            if (queryOptions.getIncludeOnlyClassifiedElements() != null)
            {
                /*
                 * Ignore elements that have are missing a required classification.
                 */
                for (String requiredClassificationName : queryOptions.getIncludeOnlyClassifiedElements())
                {
                    if (requiredClassificationName != null)
                    {
                        AttachedClassification classification = propertyHelper.getClassification(retrievedElement, requiredClassificationName);

                        if (classification == null)
                        {
                            return false;
                        }
                    }
                }
            }

            /*
             * The final check is on the zone membership.
             */
            if (queryOptions.getGovernanceZoneFilter() != null)
            {
                /*
                 * Need to check that the zone membership is set
                 */
                AttachedClassification classification = propertyHelper.getClassification(retrievedElement, OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName);

                if (classification != null)
                {
                    return this.checkElementZoneMembership(classification, queryOptions.getGovernanceZoneFilter());
                }
                else
                {
                    /*
                     * The zone membership classification is not set.  Is this element
                     * an anchor - in which case it is in all zones.
                     */
                    AttachedClassification anchorClassification = propertyHelper.getClassification(retrievedElement, OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);

                    if (anchorClassification == null)
                    {
                        /*
                         * This element is unanchored.
                         */
                        return true;
                    }
                    else
                    {
                        String anchorGUID = propertyHelper.getStringProperty(localServiceName,
                                                                             OpenMetadataProperty.ANCHOR_GUID.name,
                                                                             anchorClassification.getClassificationProperties(),
                                                                             methodName);

                        if ((anchorGUID == null) || anchorGUID.equals(retrievedElement.getElementGUID()))
                        {
                            /*
                             * This element is an anchor.
                             */
                            return true;
                        }
                        else
                        {
                            /*
                             * Need to retrieve the anchor entity to get the zone classification.
                             */
                            try
                            {
                                OpenMetadataElement anchorElement = openMetadataClient.getMetadataElementByGUID(userId, anchorGUID, null);

                                AttachedClassification anchorZoneClassification = propertyHelper.getClassification(anchorElement, OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName);

                                return this.checkElementZoneMembership(anchorZoneClassification, queryOptions.getGovernanceZoneFilter());
                            }
                            catch (Exception error)
                            {
                                /*
                                 * Unable to retrieve the anchor - so just log the message and skip the element.
                                 */
                                auditLog.logException(methodName,
                                                      OMFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(localServiceName,
                                                                                                             error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()),
                                                      error);
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }


    /**
     * Filter element based on zone membership.
     *
     * @param classification Anchors or ZoneMembership classification.
     * @param requiredZones supplied zones to scan for
     * @return flag to indicate if it passes the test
     */
    private boolean checkElementZoneMembership(AttachedClassification classification,
                                               List<String>           requiredZones)
    {
        final String methodName = "checkElementZoneMembership";

        if (classification != null)
        {
            List<String> elementZones = propertyHelper.getStringArrayProperty(localServiceName,
                                                                              OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                              classification.getClassificationProperties(),
                                                                              methodName);
            if ((elementZones == null) || (elementZones.isEmpty()))
            {
                return true;
            }
            else
            {
                for (String requiredZone : requiredZones)
                {
                    if (requiredZone != null)
                    {
                        if (!elementZones.contains(requiredZone))
                        {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }


    /**
     * Return the generic element extracted from the open metadata element plus linked elements.
     *
     * @param userId                   calling user
     * @param relatedMetadataElement      element extracted from the repository
     * @param queryOptions             multiple options to control the query
     * @param methodName               calling method
     * @return bean or null
     * @throws PropertyServerException problem with the conversion process
     */
    protected OpenMetadataRootElement convertRootElement(String                 userId,
                                                         RelatedMetadataElement relatedMetadataElement,
                                                         QueryOptions           queryOptions,
                                                         String                 methodName) throws PropertyServerException
    {
        if (filterByClassifications(userId, relatedMetadataElement.getElement(), queryOptions))
        {
            try
            {
                OpenMetadataRootConverter<OpenMetadataRootElement> converter = new OpenMetadataRootConverter<>(propertyHelper, localServiceName, localServerName);

                OpenMetadataRootElement rootElement = converter.getNewComplexBean(OpenMetadataRootElement.class,
                                                                                  relatedMetadataElement,
                                                                                  this.getElementRelatedElements(userId, relatedMetadataElement.getElement(), queryOptions),
                                                                                  methodName);

                return populateRootElement(userId, rootElement, queryOptions);
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          OMFAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                           methodName,
                                                                                                           localServiceName,
                                                                                                           error.getMessage()),
                                          error);
                }

                throw new PropertyServerException(OMFErrorCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                   methodName,
                                                                                                                   localServiceName,
                                                                                                                   error.getMessage()),
                                                  error.getClass().getName(),
                                                  methodName,
                                                  error);
            }
        }

        return null;
    }


    /**
     * Add a standard mermaid graph to the root element.  This method may be overridden by the subclasses if
     * they have a more fancy graph to display.
     *
     * @param userId calling user
     * @param rootElement new root element
     * @param queryOptions options from the caller
     * @return root element with graph
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected OpenMetadataRootElement populateRootElement(String                  userId,
                                                          OpenMetadataRootElement rootElement,
                                                          QueryOptions            queryOptions) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        if (rootElement != null)
        {
            addGovernanceDefinitionFamily(userId, rootElement, queryOptions);

            addSpecification(rootElement);

            rootElement.setSubDataClasses(this.getElementHierarchies(userId,
                                                                      rootElement.getSubDataClasses(),
                                                                      1,
                                                                      OpenMetadataType.DATA_CLASS_HIERARCHY_RELATIONSHIP.typeName,
                                                                      queryOptions,
                                                                      1));

            rootElement.setPartOfDataClasses(this.getElementHierarchies(userId,
                                                                         rootElement.getPartOfDataClasses(),
                                                                         1,
                                                                         OpenMetadataType.DATA_CLASS_COMPOSITION_RELATIONSHIP.typeName,
                                                                         queryOptions,
                                                                         1));

            rootElement.setNestedDataFields(this.getElementHierarchies(userId,
                                                                        rootElement.getNestedDataFields(),
                                                                        1,
                                                                        OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName,
                                                                        queryOptions,
                                                                        1));

            rootElement.setContainsDataFields(this.getElementHierarchies(userId,
                                                                          rootElement.getContainsDataFields(),
                                                                          1,
                                                                          OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName,
                                                                          queryOptions,
                                                                          1));

            rootElement.setNoteLogs(this.getElementHierarchies(userId,
                                                               rootElement.getNoteLogs(),
                                                               1,
                                                               OpenMetadataType.ATTACHED_NOTE_LOG_ENTRY_RELATIONSHIP.typeName,
                                                               queryOptions,
                                                               1));

            rootElement.setComments(this.getElementHierarchies(userId,
                                                               rootElement.getComments(),
                                                               1,
                                                               OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName,
                                                               queryOptions,
                                                               1));

            rootElement.setManagedProjects(this.getElementHierarchies(userId,
                                                               rootElement.getManagedProjects(),
                                                               1,
                                                               OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                               queryOptions,
                                                               1));

            rootElement.setManagingProjects(this.getElementHierarchies(userId,
                                                                      rootElement.getManagingProjects(),
                                                                      2,
                                                                      OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                                      queryOptions,
                                                                      1));

            rootElement.setDependsOnProjects(this.getElementHierarchies(userId,
                                                                        rootElement.getDependsOnProjects(),
                                                                        1,
                                                                        OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                        queryOptions,
                                                                        1));
            rootElement.setDependentProjects(this.getElementHierarchies(userId,
                                                                        rootElement.getDependentProjects(),
                                                                        2,
                                                                        OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                        queryOptions,
                                                                        1));

            rootElement.setImplementedBy(this.getElementHierarchies(userId,
                                                                    rootElement.getImplementedBy(),
                                                                    1,
                                                                    OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                                    queryOptions,
                                                                    1));

            rootElement.setDerivedFrom(this.getElementHierarchies(userId,
                                                                    rootElement.getDerivedFrom(),
                                                                    2,
                                                                    OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                                    queryOptions,
                                                                    1));

            rootElement.setSchemaAttributes(this.getElementHierarchies(userId,
                                                                        rootElement.getSchemaAttributes(),
                                                                        1,
                                                                        OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                                                                        List.of(OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName,
                                                                                OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName),
                                                                        queryOptions,
                                                                        1));

            rootElement.setSchemaType(this.getElementHierarchy(userId,
                                                               rootElement.getSchemaType(),
                                                               1,
                                                               OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP.typeName,
                                                               List.of(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.ATTACHED_NOTE_LOG_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.API_OPERATIONS_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.API_HEADER_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.API_REQUEST_RELATIONSHIP.typeName,
                                                                       OpenMetadataType.API_RESPONSE_RELATIONSHIP.typeName),
                                                               queryOptions,
                                                               1,
                                                               new ArrayList<>()));

            rootElement.setSupplyTo(this.getElementHierarchies(userId,
                                                               rootElement.getSupplyTo(),
                                                               1,
                                                               OpenMetadataType.INFORMATION_SUPPLY_CHAIN_LINK_RELATIONSHIP.typeName,
                                                               null,
                                                               queryOptions,
                                                               1));

            rootElement.setSupplyFrom(this.getElementHierarchies(userId,
                                                                 rootElement.getSupplyFrom(),
                                                                 2,
                                                                 OpenMetadataType.INFORMATION_SUPPLY_CHAIN_LINK_RELATIONSHIP.typeName,
                                                                 null,
                                                                 queryOptions,
                                                                 1));

            rootElement.setValidValueMembers(this.getElementHierarchies(userId,
                                                                 rootElement.getValidValueMembers(),
                                                                 1,
                                                                 OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                                 null,
                                                                 queryOptions,
                                                                 1));

            if (propertyHelper.isTypeOf(rootElement.getElementHeader(), OpenMetadataType.ASSET.typeName))
            {
                rootElement.setConnections(this.getElementHierarchies(userId,
                                                                      rootElement.getConnections(),
                                                                      1,
                                                                      OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName,
                                                                      List.of(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName,
                                                                              OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName),
                                                                      queryOptions,
                                                                      1));
            }
            else if (propertyHelper.isTypeOf(rootElement.getElementHeader(), OpenMetadataType.PORT.typeName))
            {
                rootElement.setPortDelegatingTo(this.getElementHierarchy(userId,
                                                                         rootElement.getPortDelegatingTo(),
                                                                         1,
                                                                         OpenMetadataType.PORT_DELEGATION_RELATIONSHIP.typeName,
                                                                         null,
                                                                         queryOptions,
                                                                         1,
                                                                         new ArrayList<>()));

                rootElement.setPortDelegatingFrom(this.getElementHierarchies(userId,
                                                                             rootElement.getPortDelegatingFrom(),
                                                                             2,
                                                                             OpenMetadataType.PORT_DELEGATION_RELATIONSHIP.typeName,
                                                                             null,
                                                                             queryOptions,
                                                                             1));

                rootElement.setNestedSolutionComponents(this.getElementHierarchies(userId,
                                                                                   rootElement.getNestedSolutionComponents(),
                                                                                   1,
                                                                                   null,
                                                                                   List.of(OpenMetadataType.SOLUTION_COMPONENT_ACTOR_RELATIONSHIP.typeName,
                                                                                           OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName,
                                                                                           OpenMetadataType.SOLUTION_COMPONENT_PORT_RELATIONSHIP.typeName,
                                                                                           OpenMetadataType.SOLUTION_PORT_DELEGATION_RELATIONSHIP.typeName),
                                                                                   queryOptions,
                                                                                   1));
            }
            else if ((propertyHelper.isTypeOf(rootElement.getElementHeader(), OpenMetadataType.SOLUTION_BLUEPRINT.typeName)) ||
                     (propertyHelper.isTypeOf(rootElement.getElementHeader(), OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName)))
            {
                rootElement.setCollectionMembers(this.getElementHierarchies(userId,
                                                                            rootElement.getCollectionMembers(),
                                                                            1,
                                                                            OpenMetadataType.SOLUTION_COMPOSITION_RELATIONSHIP.typeName,
                                                                            List.of(OpenMetadataType.SOLUTION_COMPONENT_ACTOR_RELATIONSHIP.typeName,
                                                                                    OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName,
                                                                                    OpenMetadataType.SOLUTION_COMPONENT_PORT_RELATIONSHIP.typeName,
                                                                                    OpenMetadataType.SOLUTION_PORT_DELEGATION_RELATIONSHIP.typeName),
                                                                            queryOptions,
                                                                            1));
            }
            else
            {
                rootElement.setCollectionMembers(this.getElementHierarchies(userId,
                                                                            rootElement.getCollectionMembers(),
                                                                            1,
                                                                            OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                            queryOptions,
                                                                            1));
            }


            if (rootElement.getRelatedBy() != null)
            {
                /*
                 * Element has been retrieved through a relationship.  Some of these relationships have actors identified in the properties.
                 */
                if (propertyHelper.isTypeOf(rootElement.getRelatedBy().getRelationshipHeader(), OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName))
                {
                    if (rootElement.getRelatedBy().getRelationshipProperties() instanceof CertificationProperties certificationProperties)
                    {
                        Map<String, MetadataElementSummary> actorMap = new HashMap<>();

                        if (certificationProperties.getCertifiedBy() != null)
                        {
                            this.getActorSummary(userId,
                                                 certificationProperties.getCertifiedBy(),
                                                 certificationProperties.getCertifiedByPropertyName(),
                                                 actorMap,
                                                 queryOptions);
                        }

                        if (certificationProperties.getRecipient() != null)
                        {
                            this.getActorSummary(userId,
                                                 certificationProperties.getRecipient(),
                                                 certificationProperties.getRecipientPropertyName(),
                                                 actorMap,
                                                 queryOptions);
                        }

                        if (certificationProperties.getCustodian() != null)
                        {
                            this.getActorSummary(userId,
                                                 certificationProperties.getCustodian(),
                                                 certificationProperties.getCustodianPropertyName(),
                                                 actorMap,
                                                 queryOptions);
                        }

                        rootElement.getRelatedBy().setAssociatedElements(actorMap);
                    }
                }
                else if (propertyHelper.isTypeOf(rootElement.getRelatedBy().getRelationshipHeader(), OpenMetadataType.LICENSE_RELATIONSHIP.typeName))
                {
                    if (rootElement.getRelatedBy().getRelationshipProperties() instanceof LicenseProperties licenseProperties)
                    {
                        Map<String, MetadataElementSummary> actorMap = new HashMap<>();

                        if (licenseProperties.getLicensedBy() != null)
                        {
                            this.getActorSummary(userId,
                                                 licenseProperties.getLicensedBy(),
                                                 licenseProperties.getLicensedByPropertyName(),
                                                 actorMap,
                                                 queryOptions);
                        }

                        if (licenseProperties.getLicensee() != null)
                        {
                            this.getActorSummary(userId,
                                                 licenseProperties.getLicensee(),
                                                 licenseProperties.getLicenseePropertyName(),
                                                 actorMap,
                                                 queryOptions);
                        }

                        if (licenseProperties.getCustodian() != null)
                        {
                            this.getActorSummary(userId,
                                                 licenseProperties.getCustodian(),
                                                 licenseProperties.getCustodianPropertyName(),
                                                 actorMap,
                                                 queryOptions);
                        }

                        rootElement.getRelatedBy().setAssociatedElements(actorMap);
                    }
                }
            }

            return addMermaidToRootElement(userId, rootElement, queryOptions);
        }

        return null;
    }


    /**
     * Reformat the specification relationships to make it easier for callers.
     *
     * @param rootElement element to update
     */
    private void addSpecification(OpenMetadataRootElement rootElement)
    {
        if (rootElement.getSpecificationProperties() != null)
        {
            SpecificationPropertyConverter<SpecificationProperty> converter = new SpecificationPropertyConverter<>(propertyHelper,
                                                                                                                   localServiceName,
                                                                                                                   localServerName);

            rootElement.setSpecification(converter.getSpecification(rootElement.getSpecificationProperties()));
        }
    }


    /**
     * Add a standard mermaid graph to the root element.  This method may be overridden by the subclasses if
     * they have a more fancy graph to display.
     *
     * @param userId calling user
     * @param rootElement new root element
     * @param queryOptions options from the caller
     * @return root element with graph
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    protected OpenMetadataRootElement addMermaidToRootElement(String                  userId,
                                                              OpenMetadataRootElement rootElement,
                                                              QueryOptions            queryOptions) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        if (rootElement != null)
        {
            OpenMetadataRootMermaidGraphBuilder graphBuilder = new OpenMetadataRootMermaidGraphBuilder(rootElement);

            rootElement.setMermaidGraph(graphBuilder.getMermaidGraph());

            if (rootElement.getSpecificationProperties() != null)
            {
                SpecificationMermaidGraphBuilder specificationMermaidGraphBuilder = new SpecificationMermaidGraphBuilder(rootElement);

                rootElement.setSpecificationMermaidGraph(specificationMermaidGraphBuilder.getMermaidGraph());
            }

            if ((propertyHelper.isTypeOf(rootElement.getElementHeader(), OpenMetadataType.SOLUTION_BLUEPRINT.typeName)) && (rootElement.getCollectionMembers() != null))
            {
                SolutionBlueprintMermaidGraphBuilder solutionBlueprintMermaidGraphBuilder = new SolutionBlueprintMermaidGraphBuilder(rootElement);

                rootElement.setSolutionBlueprintMermaidGraph(solutionBlueprintMermaidGraphBuilder.getMermaidGraph());
            }

            if ((propertyHelper.isTypeOf(rootElement.getElementHeader(), OpenMetadataType.SOLUTION_COMPONENT.typeName)) && (rootElement.getNestedSolutionComponents() != null))
            {
                SolutionComponentMermaidGraphBuilder solutionComponentMermaidGraphBuilder = new SolutionComponentMermaidGraphBuilder(rootElement);

                rootElement.setSolutionSubcomponentMermaidGraph(solutionComponentMermaidGraphBuilder.getMermaidGraph());
            }

        }

        return rootElement;
    }


    /**
     * Create a composite mermaid graph from the returned elements.
     *
     * @param searchString string used to create list - used in title of the mermaid graph
     * @param elements elements returned from the query
     * @return mermaid string
     */
    public String getMermaidGraph(String                        searchString,
                                  List<OpenMetadataRootElement> elements)
    {
        if (elements != null)
        {
            OpenMetadataRootMermaidGraphBuilder graphBuilder = new OpenMetadataRootMermaidGraphBuilder(searchString, elements);

            return graphBuilder.getMermaidGraph();
        }

        return null;
    }


    /**
     * Add a standard mermaid graph to the root element.  This method may be overridden by the subclasses if
     * they have a more fancy graph to display.
     *
     * @param userId calling user
     * @param rootElement new root element
     * @param queryOptions options from the caller
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected void addGovernanceDefinitionFamily(String                  userId,
                                                 OpenMetadataRootElement rootElement,
                                                 QueryOptions            queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        if (rootElement != null)
        {
            List<RelatedMetadataElementSummary> parentElements1 = this.getElementHierarchies(userId,
                                                                                              rootElement.getSupportingGovernanceDefinitions(),
                                                                                              0,
                                                                                              OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName,
                                                                                              queryOptions,
                                                                                              1);

            List<RelatedMetadataElementSummary> parentElements2 = this.getElementHierarchies(userId,
                                                                                              rootElement.getSupportingGovernanceDefinitions(),
                                                                                              0,
                                                                                              OpenMetadataType.GOVERNANCE_MECHANISM_RELATIONSHIP.typeName,
                                                                                              queryOptions,
                                                                                              1);

            if (parentElements1 != null)
            {
                if (parentElements2 != null)
                {
                    parentElements1.addAll(parentElements2);
                }

                rootElement.setSupportingGovernanceDefinitions(parentElements1);
            }
            else if (parentElements2 != null)
            {
                rootElement.setSupportingGovernanceDefinitions(parentElements2);
            }

            List<RelatedMetadataElementSummary> peerElements1 = this.getElementHierarchies(userId,
                                                                                            rootElement.getPeerGovernanceDefinitions(),
                                                                                            0,
                                                                                            OpenMetadataType.GOVERNANCE_DRIVER_LINK_RELATIONSHIP.typeName,
                                                                                            queryOptions,
                                                                                            1);

            List<RelatedMetadataElementSummary> peerElements2 = this.getElementHierarchies(userId,
                                                                                            rootElement.getPeerGovernanceDefinitions(),
                                                                                            0,
                                                                                            OpenMetadataType.GOVERNANCE_POLICY_LINK_RELATIONSHIP.typeName,
                                                                                            queryOptions,
                                                                                            1);

            List<RelatedMetadataElementSummary> peerElements3 = this.getElementHierarchies(userId,
                                                                                            rootElement.getPeerGovernanceDefinitions(),
                                                                                            0,
                                                                                            OpenMetadataType.GOVERNANCE_CONTROL_LINK_RELATIONSHIP.typeName,
                                                                                            queryOptions,
                                                                                            1);

            if (peerElements1 != null)
            {
                if (peerElements2 != null)
                {
                    if (peerElements3 != null)
                    {
                        peerElements2.addAll(peerElements3);
                    }

                    peerElements1.addAll(peerElements2);
                }

                rootElement.setPeerGovernanceDefinitions(peerElements1);
            }
            else if (parentElements2 != null)
            {
                if (peerElements3 != null)
                {
                    peerElements2.addAll(peerElements3);
                }

                rootElement.setPeerGovernanceDefinitions(peerElements2);
            }
            else if (peerElements3 != null)
            {
                rootElement.setPeerGovernanceDefinitions(peerElements3);
            }
        }
    }


    /**
     * Return the nested elements to the required depth.
     *
     * @param userId calling user
     * @param retrievedElements elements to query against
     * @param parentEnd start at end 1 or 2?
     * @param relationshipName name of the relationship to iteratively follow
     * @param queryOptions callers query options
     * @param currentDepth how far away are we from the original element?
     * @return the hierarchy under/over this element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected List<RelatedMetadataElementSummary> getElementHierarchies(String                              userId,
                                                                        List<RelatedMetadataElementSummary> retrievedElements,
                                                                        int                                 parentEnd,
                                                                        String                              relationshipName,
                                                                        QueryOptions                        queryOptions,
                                                                        int                                 currentDepth) throws InvalidParameterException,
                                                                                                                                 PropertyServerException,
                                                                                                                                 UserNotAuthorizedException
    {
        return this.getElementHierarchies(userId, retrievedElements, parentEnd, relationshipName, null, queryOptions, currentDepth);
    }


    /**
     * Return the nested elements to the required depth.
     *
     * @param userId calling user
     * @param retrievedElements elements to query against
     * @param parentEnd start at end 1 or 2?
     * @param relationshipName name of the relationship to iteratively follow
     * @param sideRelationshipNames additional side relationships to capture (maybe null)
     * @param queryOptions callers query options
     * @param currentDepth how far away are we from the original element?
     * @return the hierarchy under/over this element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected List<RelatedMetadataElementSummary> getElementHierarchies(String                              userId,
                                                                        List<RelatedMetadataElementSummary> retrievedElements,
                                                                        int                                 parentEnd,
                                                                        String                              relationshipName,
                                                                        List<String>                        sideRelationshipNames,
                                                                        QueryOptions                        queryOptions,
                                                                        int                                 currentDepth) throws InvalidParameterException,
                                                                                                                                 PropertyServerException,
                                                                                                                                 UserNotAuthorizedException
    {
        if (retrievedElements != null)
        {
            List<RelatedMetadataElementSummary> results = new ArrayList<>();

            for (RelatedMetadataElementSummary retrievedElement : retrievedElements)
            {
                if (retrievedElement != null)
                {
                    List<String> coveredRelationshipsGUIDs = new ArrayList<>();

                    results.add(getElementHierarchy(userId,
                                                    retrievedElement,
                                                    parentEnd,
                                                    relationshipName,
                                                    sideRelationshipNames,
                                                    queryOptions,
                                                    currentDepth,
                                                    coveredRelationshipsGUIDs));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Return the nested elements to the required depth.
     *
     * @param userId calling user
     * @param retrievedElement element to query against
     * @param parentEnd start at end 1 or 2?
     * @param relationshipName name of the relationship to follow
     * @param sideRelationshipNames additional side relationships to capture (maybe null)
     * @param queryOptions callers query options
     * @param currentDepth how far away are we from the original element?
     * @param coveredRelationshipsGUIDs do not revisit relationships already processed
     * @return the hierarchy under/over this element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected RelatedMetadataElementSummary getElementHierarchy(String                        userId,
                                                                RelatedMetadataElementSummary retrievedElement,
                                                                int                           parentEnd,
                                                                String                        relationshipName,
                                                                List<String>                  sideRelationshipNames,
                                                                QueryOptions                  queryOptions,
                                                                int                           currentDepth,
                                                                List<String>                  coveredRelationshipsGUIDs) throws InvalidParameterException,
                                                                                                                           PropertyServerException,
                                                                                                                           UserNotAuthorizedException
    {
        if (retrievedElement != null)
        {
            if (queryOptions.getGraphQueryDepth() > currentDepth)
            {
                QueryOptions workingQueryOptions = new QueryOptions(queryOptions);
                workingQueryOptions.setStartFrom(0);
                workingQueryOptions.setPageSize(queryOptions.getRelationshipsPageSize());
                workingQueryOptions.setMetadataElementTypeName(OpenMetadataType.OPEN_METADATA_ROOT.typeName); // want all types of elements back

                /*
                 * If there are no side relationships then we can optimise and only receive the main hierarchical relationship.
                 */
                String receiveRelationshipName = null;
                int    receiveParentEnd = 0;

                if (sideRelationshipNames == null)
                {
                    receiveRelationshipName = relationshipName;
                    receiveParentEnd = parentEnd;
                }

                List<RelatedMetadataElementSummary> nestedElements = new ArrayList<>();
                List<RelatedMetadataElementSummary> sideLinks = new ArrayList<>();

                RelatedMetadataElementList relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                                      retrievedElement.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                      receiveParentEnd,
                                                                                                                      receiveRelationshipName,
                                                                                                                      workingQueryOptions);
                if ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
                {
                    /*
                     * Remove the relationships that the caller asked to skip.
                     */
                    List<RelatedMetadataElement> relevantRelationships = this.getRelevantRelationships(relatedMetadataElementList.getElementList(), queryOptions);

                    for (RelatedMetadataElement relatedMetadataElement : relevantRelationships)
                    {
                        if (relatedMetadataElement != null)
                        {
                            /*
                             * Look for parent/child relationships
                             */
                            if ((relationshipName != null) && (propertyHelper.isTypeOf(relatedMetadataElement, relationshipName)))
                            {
                                /*
                                 * The relationship is a parent/child relationship type.
                                 * Is this orientated logically down the hierarchy?
                                 */
                                if (((parentEnd != 2) && (! relatedMetadataElement.getElementAtEnd1())) ||
                                        ((parentEnd != 1) && (relatedMetadataElement.getElementAtEnd1())))
                                {
                                    /*
                                     * Check that this relationship has not been covered already.
                                     */
                                    if (! coveredRelationshipsGUIDs.contains(relatedMetadataElement.getRelationshipGUID()))
                                    {
                                        RelatedMetadataElementSummary nestedElement = propertyHelper.getRelatedElementSummary(relatedMetadataElement);

                                        coveredRelationshipsGUIDs.add(relatedMetadataElement.getElement().getElementGUID());
                                        nestedElements.add(getElementHierarchy(userId,
                                                                               nestedElement,
                                                                               parentEnd,
                                                                               relationshipName,
                                                                               sideRelationshipNames,
                                                                               workingQueryOptions,
                                                                               currentDepth + 1,
                                                                               coveredRelationshipsGUIDs));
                                    }
                                }
                            }
                            else if (sideRelationshipNames != null)
                            {
                                /*
                                 * Save any relevant side relationships.
                                 */
                                for (String sideRelationshipName : sideRelationshipNames)
                                {
                                    if (propertyHelper.isTypeOf(relatedMetadataElement, sideRelationshipName))
                                    {
                                        sideLinks.add(propertyHelper.getRelatedElementSummary(relatedMetadataElement));
                                    }
                                }
                            }
                        }
                    }
                }

                if (! nestedElements.isEmpty())
                {
                    if (! sideLinks.isEmpty())
                    {
                        return new RelatedMetadataHierarchySummary(retrievedElement, nestedElements, sideLinks);
                    }
                    else
                    {
                        return new RelatedMetadataHierarchySummary(retrievedElement, nestedElements, null);
                    }
                }
                else if (! sideLinks.isEmpty())
                {
                    return new RelatedMetadataHierarchySummary(retrievedElement, null, sideLinks);
                }
            }
        }

        return retrievedElement;
    }


    /**
     * Returns the list of elements of the appropriate type with a particular name.
     * Caller responsible for mermaid graph.
     *
     * @param userId                   userId of user making request
     * @param elementGUID unique identifier for the starting metadata element
     * @param guidPropertyName name of unique identifier for the starting metadata element
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param attachmentEntityTypeName requested type name for retrieved entities
     * @param suppliedQueryOptions             multiple options to control the query
     * @param methodName               calling method
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getRelatedRootElements(String       userId,
                                                                String       elementGUID,
                                                                String       guidPropertyName,
                                                                int          startingAtEnd,
                                                                String       relationshipTypeName,
                                                                String       attachmentEntityTypeName,
                                                                QueryOptions suppliedQueryOptions,
                                                                String       methodName) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidPropertyName, methodName);

        propertyHelper.validatePaging(suppliedQueryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        QueryOptions queryOptions = new QueryOptions(suppliedQueryOptions);

        if (queryOptions.getMetadataElementTypeName() == null)
        {
            queryOptions.setMetadataElementTypeName(attachmentEntityTypeName);
        }

        RelatedMetadataElementList relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                              elementGUID,
                                                                                                              startingAtEnd,
                                                                                                              relationshipTypeName,
                                                                                                              queryOptions);

        return convertRelatedRootElements(userId, relatedMetadataElementList, queryOptions, methodName);
    }



    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param userId                   userId of user making request
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param suppliedGetOptions multiple options to control the query
     *
     * @return metadata element properties or null if not found
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException a problem accessing the metadata store
     */
    public OpenMetadataRootElement getRootElementByUniqueName(String     userId,
                                                              String     uniqueName,
                                                              String     uniquePropertyName,
                                                              GetOptions suppliedGetOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "getRootElementByUniqueName";

        GetOptions getOptions = new GetOptions(suppliedGetOptions);

        if (getOptions.getMetadataElementTypeName() == null)
        {
            getOptions.setMetadataElementTypeName(metadataElementTypeName);
        }

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByUniqueName(userId,
                                                                                                    uniqueName,
                                                                                                    uniquePropertyName,
                                                                                                    getOptions);

        return convertRootElement(userId, openMetadataElement, new QueryOptions(suppliedGetOptions), methodName);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param userId                   userId of user making request
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param suppliedGetOptions options to control the retrieve
     *
     * @return metadata element properties or null if not found
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException a problem accessing the metadata store
     */
    public OpenMetadataRootElement getLineageElementByUniqueName(String     userId,
                                                                 String     uniqueName,
                                                                 String     uniquePropertyName,
                                                                 GetOptions suppliedGetOptions) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        GetOptions getOptions = new GetOptions(suppliedGetOptions);

        if (getOptions.getMetadataElementTypeName() == null)
        {
            getOptions.setMetadataElementTypeName(metadataElementTypeName);
        }

        getOptions.setForLineage(true);

        return this.getRootElementByUniqueName(userId, uniqueName, uniquePropertyName, getOptions);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name) and the DELETED status.
     * This method assumes all effective dates, and forLineage and forDuplicateProcessing are false,
     * to cast the widest net.
     *
     * @param userId                   userId of user making request
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param suppliedGetOptions options to control the retrieve
     *
     * @return metadata element properties or null if not found
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException a problem accessing the metadata store
     */
    public OpenMetadataRootElement getDeletedElementByUniqueName(String     userId,
                                                                 String     uniqueName,
                                                                 String     uniquePropertyName,
                                                                 GetOptions suppliedGetOptions) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "getDeletedElementByUniqueName";

        QueryOptions queryOptions = new QueryOptions(suppliedGetOptions);

        queryOptions.setLimitResultsByStatus(Collections.singletonList(ElementStatus.DELETED));
        queryOptions.setSequencingOrder(SequencingOrder.LAST_UPDATE_RECENT);

        if (queryOptions.getMetadataElementTypeName() == null)
        {
            queryOptions.setMetadataElementTypeName(metadataElementTypeName);
        }

        List<OpenMetadataRootElement> openMetadataRootElements = this.getRootElementsByName(userId, uniqueName, Collections.singletonList(uniquePropertyName), queryOptions, methodName);

        if (openMetadataRootElements != null)
        {
            for (OpenMetadataRootElement openMetadataRootElement : openMetadataRootElements)
            {
                if (openMetadataRootElement != null)
                {
                    return openMetadataRootElement;
                }
            }
        }

        return null;
    }


    /**
     * Returns the list of elements of the appropriate type with a particular name.
     *
     * @param userId                   userId of user making request
     * @param name                     name of the element to return - match is full text match in qualifiedName or name
     * @param propertyNames            list of property names to consider
     * @param suppliedQueryOptions             multiple options to control the query
     * @param methodName               calling method
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getRootElementsByName(String       userId,
                                                               String       name,
                                                               List<String> propertyNames,
                                                               QueryOptions suppliedQueryOptions,
                                                               String       methodName) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String nameParameterName = "name";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(name, nameParameterName, methodName);
        propertyHelper.validatePaging(suppliedQueryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        QueryOptions queryOptions = new QueryOptions(suppliedQueryOptions);

        if (queryOptions.getMetadataElementTypeName() == null)
        {
            queryOptions.setMetadataElementTypeName(metadataElementTypeName);
        }

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 propertyHelper.getSearchPropertiesByName(propertyNames, name, PropertyComparisonOperator.EQ),
                                                                                                 null,
                                                                                                 queryOptions);

        return convertRootElements(userId,
                                   openMetadataElements,
                                   queryOptions,
                                   methodName);
    }



    /**
     * Returns the named element.
     *
     * @param userId                   userId of user making request
     * @param name                     name of the element to return - match is full text match in qualifiedName or name
     * @param propertyName            property name to consider
     * @param suppliedGetOptions            multiple options to control the query
     * @param methodName               calling method
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getRootElementsByUniqueName(String     userId,
                                                               String     name,
                                                               String     propertyName,
                                                               GetOptions suppliedGetOptions,
                                                               String     methodName) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String nameParameterName = "name";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(name, nameParameterName, methodName);

        String uniqueName = OpenMetadataProperty.QUALIFIED_NAME.name;

        if (propertyName != null)
        {
            uniqueName = propertyName;
        }

        GetOptions getOptions = new GetOptions(suppliedGetOptions);

        if (getOptions.getMetadataElementTypeName() == null)
        {
            getOptions.setMetadataElementTypeName(metadataElementTypeName);
        }

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByUniqueName(userId,
                                                                                                    name,
                                                                                                    uniqueName,
                                                                                                    getOptions);

        return convertRootElement(userId,
                                  openMetadataElement,
                                  new QueryOptions(getOptions),
                                  methodName);
    }


    /**
     * Return the properties of a specific governance definition.
     *
     * @param userId      userId of user making request
     * @param elementGUID unique identifier of the required element
     * @param suppliedGetOptions  multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getRootElementByGUID(String       userId,
                                                        String       elementGUID,
                                                        GetOptions   suppliedGetOptions,
                                                        String       methodName) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String guidParameterName = "elementGUID";

        GetOptions getOptions = new GetOptions(suppliedGetOptions);

        if (getOptions.getMetadataElementTypeName() == null)
        {
            getOptions.setMetadataElementTypeName(metadataElementTypeName);
        }

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByGUID(userId, elementGUID, getOptions);

        if (openMetadataElement != null)
        {
            if (propertyHelper.isTypeOf(openMetadataElement, getOptions.getMetadataElementTypeName()))
            {
                return convertRootElement(userId,
                                          openMetadataElement,
                                          new QueryOptions(getOptions),
                                          methodName);
            }

            throw new InvalidParameterException(OMFErrorCode.WRONG_TYPE_FOR_ELEMENT.getMessageDefinition(elementGUID,
                                                                                                         openMetadataElement.getType().getTypeName(),
                                                                                                         getOptions.getMetadataElementTypeName()),
                                                this.getClass().getName(),
                                                methodName,
                                                OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name);

        }

        return null;
    }


    /**
     * Return the properties of a specific governance definition.
     *
     * @param userId      userId of user making request
     * @param name unique name of the required element
     * @param propertyName name of the property to query (default is qualifiedName)
     * @param suppliedGetOptions  multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getRootElementByUniqueName(String       userId,
                                                              String       name,
                                                              String       propertyName,
                                                              GetOptions   suppliedGetOptions,
                                                              String       methodName) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String nameParameterName = "propertyName";

        GetOptions getOptions = new GetOptions(suppliedGetOptions);

        if (getOptions.getMetadataElementTypeName() == null)
        {
            getOptions.setMetadataElementTypeName(metadataElementTypeName);
        }

        String uniquePropertyName = OpenMetadataProperty.QUALIFIED_NAME.name;

        if (propertyName != null)
        {
            uniquePropertyName = propertyName;
        }

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(name, nameParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByUniqueName(userId, name, uniquePropertyName, getOptions);

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, metadataElementTypeName)))
        {
            return convertRootElement(userId,
                                      openMetadataElement,
                                      new QueryOptions(getOptions),
                                      methodName);
        }

        return null;
    }


    /**
     * Returns the list of elements of the right type, matching the search string.
     *
     * @param userId       userId of user making request
     * @param searchString string to search for
     * @param suppliedSearchOptions             multiple options to control the query
     * @param methodName   calling method
     * @return a list of collections
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> findRootElements(String        userId,
                                                          String        searchString,
                                                          SearchOptions suppliedSearchOptions,
                                                          String        methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        propertyHelper.validateUserId(userId, methodName);

        SearchOptions searchOptions = new SearchOptions(suppliedSearchOptions);

        if (searchOptions.getMetadataElementTypeName() == null)
        {
            searchOptions.setMetadataElementTypeName(metadataElementTypeName);
        }

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId,
                                                                                                           searchString,
                                                                                                           searchOptions);

        return convertRootElements(userId, openMetadataElements, searchOptions, methodName);
    }
}
