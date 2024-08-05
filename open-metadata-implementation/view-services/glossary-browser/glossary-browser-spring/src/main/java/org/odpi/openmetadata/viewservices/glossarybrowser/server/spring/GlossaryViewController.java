/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarybrowser.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryCategoryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.RelatedMetadataElementSummariesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.gaf.rest.OpenMetadataElementResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossarySearchStringRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.server.GlossaryBrowserRESTServices;
import org.odpi.openmetadata.viewservices.glossarybrowser.server.spring.beans.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This controller serves all requests for glossaries, categories and terms.  The API originated in the Egeria UI Application.  It is a
 * temporary interface until the UI components are updated to call the main Glossary Browser OMVS.
 */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/glossary-browser/old/glossaries")

@Tag(name="API: Glossary Browser OMVS",
     description="Explore the contents of a glossary, such as its top-level glossary element, glossary categories and glossary terms, along with the elements that are linked to the terms, such assets.  Each operation includes optional forLineage and forDuplicateProcessing request parameters and an optional request body that includes an effective time field.  These affect the elements that are returned on the query.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/services/omvs/glossary-browser/overview/"))

@Deprecated
public class GlossaryViewController
{
    private final RESTExceptionHandler exceptionHandler = new RESTExceptionHandler();

    private final GlossaryBrowserRESTServices restAPI = new GlossaryBrowserRESTServices();

    /**
     * Retrieve the list of all glossaries.
     *
     * @param serverName name of the server to route the request to
     * @param from     the starting index, used for pagination
     * @param size number of results returned, used for pagination
     * @return all the glossaries
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping
    public List<Glossary> getAllGlossaries(@PathVariable String serverName,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                    InvalidParameterException,
                                                                                                    PropertyServerException
    {
        final String methodName = "getAllGlossaries";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        GlossaryElementsResponse restResult = restAPI.findGlossaries(serverName,
                                                                     from,
                                                                     size,
                                                                     false,
                                                                     false,
                                                                     true,
                                                                     false,
                                                                     false,
                                                                     requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaries(restResult.getElementList());
    }


    /**
     * Retrieve the list of all glossary terms from all glossaries.
     *
     * @param serverName name of the server to route the request to
     * @param from     the starting index, used for pagination
     * @param size number of results returned, used for pagination
     * @return all the glossary terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/terms")
    public List<GlossaryTerm> getAllGlossaryTerms(@PathVariable String serverName,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                           InvalidParameterException,
                                                                                                           PropertyServerException
    {
        final String methodName = "getAllGlossaryTerms";

        GlossarySearchStringRequestBody requestBody = new GlossarySearchStringRequestBody();

        GlossaryTermElementsResponse restResult = restAPI.findGlossaryTerms(serverName,
                                                                            from,
                                                                            size,
                                                                            false,
                                                                            false,
                                                                            true,
                                                                            false,
                                                                            false,
                                                                            requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * Retrieve the list of all categories from all glossaries.
     *
     * @param serverName name of the server to route the request to
     * @param from     the starting index, used for pagination
     * @param size number of results returned, used for pagination
     * @return all the glossary categories
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/categories")
    public List<GlossaryCategory> getAllGlossaryCategories(@PathVariable String serverName,
                                                           @RequestParam(defaultValue = "0") Integer from,
                                                           @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException, InvalidParameterException, PropertyServerException
    {
        final String methodName = "getAllGlossaryCategories";

        GlossarySearchStringRequestBody requestBody = new GlossarySearchStringRequestBody();

        GlossaryCategoryElementsResponse restResult = restAPI.findGlossaryCategories(serverName,
                                                                                     from,
                                                                                     size,
                                                                                     false,
                                                                                     false,
                                                                                     true,
                                                                                     false,
                                                                                     false,
                                                                                     requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryCategories(restResult.getElementList());
    }


    /**
     * Retrieve a specific glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID GUID of the glossary to be retrieved
     * @return the glossary with the GUID or null
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/{glossaryGUID}")
    public Glossary getGlossary(@PathVariable String serverName,
                                @PathVariable("glossaryGUID") String glossaryGUID) throws UserNotAuthorizedException,
                                                                                          InvalidParameterException,
                                                                                          PropertyServerException
    {
        final String methodName = "getGlossary";

        GlossaryElementResponse restResult = restAPI.getGlossaryByGUID(serverName,
                                                                       glossaryGUID,
                                                                       false,
                                                                       false,
                                                                       null);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossary(restResult.getElement());
    }


    /**
     * Retrieve all the categories from a specific glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID GUID of the glossary
     * @param from     the starting index, used for pagination
     * @param size         number of results returned, used for pagination
     * @return list of categories
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/{glossaryGUID}/categories")
    public List<GlossaryCategory> getCategories(@PathVariable String serverName,
                                                @PathVariable("glossaryGUID") String glossaryGUID,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                         InvalidParameterException,
                                                                                                         PropertyServerException
    {
        final String methodName = "getCategories";

        GlossaryCategoryElementsResponse restResult = restAPI.getCategoriesForGlossary(serverName,
                                                                                       glossaryGUID,
                                                                                       from,
                                                                                       size,
                                                                                       false,
                                                                                       false,
                                                                                       null);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryCategories(restResult.getElementList());
    }


    /**
     * Retrieve all the terms from a specific glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID GUID of the glossary
     * @param from     the starting index, used for pagination
     * @param size         number of results returned, used for pagination
     * @return list of terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     * */
    @GetMapping("/{glossaryGUID}/terms")
    public List<GlossaryTerm> getTermsOfGlossary(@PathVariable String serverName,
                                                 @PathVariable("glossaryGUID") String glossaryGUID,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                          InvalidParameterException,
                                                                                                          PropertyServerException
    {
        final String methodName = "getTermsOfGlossary";

        GlossaryTermElementsResponse restResult = restAPI.getTermsForGlossary(serverName,
                                                                              glossaryGUID,
                                                                              from,
                                                                              size,
                                                                              false,
                                                                              false,
                                                                              null);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * Return the external links for a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID GUID of the glossary
     * @param from     the starting index, used for pagination
     * @param size         number of results returned, used for pagination
     * @return list of external glossary links
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     * */
    @GetMapping("/{glossaryGUID}/externalGlossaryLinks")

    public List<ExternalGlossaryLink> getExternalGlossaryLinksOfGlossary(@PathVariable String serverName,
                                                                         @PathVariable("glossaryGUID") String glossaryGUID,
                                                                         @RequestParam(defaultValue = "0") Integer from,
                                                                         @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                                                  InvalidParameterException,
                                                                                                                                  PropertyServerException
    {
        // todo
        return null;
    }


    /**
     * Return the antonym relationships.
     *
     * @param termGUID GUID of the term
     * @return the term with the GUID
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
    */
    @GetMapping("/terms/{termGUID}")
    public GlossaryTerm getTerm(@PathVariable String serverName,
                                @PathVariable("termGUID") String termGUID) throws UserNotAuthorizedException,
                                                                                  InvalidParameterException,
                                                                                  PropertyServerException
    {
        final String methodName = "getTerm";

        GlossaryTermElementResponse restResult = restAPI.getGlossaryTermByGUID(serverName,
                                                                               termGUID,
                                                                               false,
                                                                               false,
                                                                               null);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerm(restResult.getElement());
    }


    /**
     * Return the terms linked by the Antonym relationship.
     *
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of antonyms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/antonyms")
    public List<GlossaryTerm> getAntonyms(@PathVariable String serverName,
                                          @PathVariable("termGUID") String termGUID,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                   InvalidParameterException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getAntonyms";

        GlossaryTermRelationshipRequestBody requestBody = new GlossaryTermRelationshipRequestBody();

        requestBody.setRelationshipTypeName(OpenMetadataType.ANTONYM_RELATIONSHIP_NAME);

        GlossaryTermElementsResponse restResult = restAPI.getRelatedTerms(serverName,
                                                                          termGUID,
                                                                          from,
                                                                          size,
                                                                          false,
                                                                          false,
                                                                          requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of terms representing the assigned elements
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/assignedElements")
    public List<GlossaryViewEntityDetail> getAssignedElements(@PathVariable String serverName,
                                                              @PathVariable("termGUID") String termGUID,
                                                              @RequestParam(defaultValue = "0") Integer from,
                                                              @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                                       InvalidParameterException,
                                                                                                                       PropertyServerException
    {
        final String methodName = "getAssignedElements";

        RelatedMetadataElementSummariesResponse restResult = restAPI.getSemanticAssignees(serverName,
                                                                                          termGUID,
                                                                                          from,
                                                                                          size,
                                                                                          false,
                                                                                          false,
                                                                                          null);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        if ((restResult != null) && (restResult.getElements() != null))
        {
            List<GlossaryViewEntityDetail> results = new ArrayList<>();

            for (RelatedMetadataElementSummary relatedElement : restResult.getElements())
            {
                OpenMetadataElementResponse openMetadataElement = restAPI.getMetadataElementByGUID(serverName,
                                                                                                   relatedElement.getRelatedElement().getElementHeader().getGUID(),
                                                                                                   false,
                                                                                                   false,
                                                                                                   null);

                exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

                if (openMetadataElement.getElement() != null)
                {
                    results.add(convertOpenMetadataElement(openMetadataElement.getElement()));
                }
            }

            return results;
        }

        return null;
    }



    /**
     * Return glossary terms for the HASA relationship.
     *
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of terms representing the antonyms of the term with GUID
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/attributes")
    public List<GlossaryTerm> getAttributes(@PathVariable String serverName,
                                            @PathVariable("termGUID") String termGUID,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                     InvalidParameterException,
                                                                                                     PropertyServerException
    {
        final String methodName = "getAttributes";

        GlossaryTermRelationshipRequestBody requestBody = new GlossaryTermRelationshipRequestBody();

        requestBody.setRelationshipTypeName(OpenMetadataType.TERM_HAS_A_RELATIONSHIP_NAME);

        GlossaryTermElementsResponse restResult = restAPI.getRelatedTerms(serverName,
                                                                          termGUID,
                                                                          from,
                                                                          size,
                                                                          false,
                                                                          false,
                                                                          requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of is-a terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/isA")

    public List<GlossaryTerm> getIsATerms(@PathVariable String serverName,
                                          @PathVariable("termGUID") String termGUID,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                   InvalidParameterException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getIsATerms";

        GlossaryTermRelationshipRequestBody requestBody = new GlossaryTermRelationshipRequestBody();

        requestBody.setRelationshipTypeName(OpenMetadataType.ISA_RELATIONSHIP_NAME);

        GlossaryTermElementsResponse restResult = restAPI.getRelatedTerms(serverName,
                                                                          termGUID,
                                                                          from,
                                                                          size,
                                                                          false,
                                                                          false,
                                                                          requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of preferred terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/preferredTerms")
    public List<GlossaryTerm> getPreferredTerms(@PathVariable String serverName,
                                                @PathVariable("termGUID") String termGUID,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                         InvalidParameterException,
                                                                                                         PropertyServerException
    {
        final String methodName = "getPreferredTerms";

        GlossaryTermRelationshipRequestBody requestBody = new GlossaryTermRelationshipRequestBody();

        requestBody.setRelationshipTypeName(OpenMetadataType.PREFERRED_TERM_RELATIONSHIP_NAME);

        GlossaryTermElementsResponse restResult = restAPI.getRelatedTerms(serverName,
                                                                          termGUID,
                                                                          from,
                                                                          size,
                                                                          false,
                                                                          false,
                                                                          requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of related terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/relatedTerms")
    public List<GlossaryTerm> getRelatedTerms(@PathVariable String serverName,
                                              @PathVariable("termGUID") String termGUID,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                       InvalidParameterException,
                                                                                                       PropertyServerException
    {
        final String methodName = "getRelatedTerms";

        GlossaryTermRelationshipRequestBody requestBody = new GlossaryTermRelationshipRequestBody();

        requestBody.setRelationshipTypeName(OpenMetadataType.RELATED_TERM_RELATIONSHIP_NAME);

        GlossaryTermElementsResponse restResult = restAPI.getRelatedTerms(serverName,
                                                                          termGUID,
                                                                          from,
                                                                          size,
                                                                          false,
                                                                          false,
                                                                          requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of replacement terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/replacementTerms")
    public List<GlossaryTerm> getReplacementTerms(@PathVariable String serverName,
                                                  @PathVariable("termGUID") String termGUID,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                           InvalidParameterException,
                                                                                                           PropertyServerException
    {
        final String methodName = "getReplacementTerms";

        GlossaryTermRelationshipRequestBody requestBody = new GlossaryTermRelationshipRequestBody();

        requestBody.setRelationshipTypeName(OpenMetadataType.REPLACEMENT_TERM_RELATIONSHIP_NAME);

        GlossaryTermElementsResponse restResult = restAPI.getRelatedTerms(serverName,
                                                                          termGUID,
                                                                          from,
                                                                          size,
                                                                          false,
                                                                          false,
                                                                          requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of subtypes terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/subtypes")
    public List<GlossaryTerm> getSubtypes(@PathVariable String serverName,
                                          @PathVariable("termGUID") String termGUID,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                   InvalidParameterException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getSubtypes";

        GlossaryTermRelationshipRequestBody requestBody = new GlossaryTermRelationshipRequestBody();

        requestBody.setRelationshipTypeName(OpenMetadataType.TERM_IS_A_TYPE_OF_RELATIONSHIP_NAME);

        GlossaryTermElementsResponse restResult = restAPI.getRelatedTerms(serverName,
                                                                          termGUID,
                                                                          from,
                                                                          size,
                                                                          false,
                                                                          false,
                                                                          requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of synonym terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/synonyms")
    public List<GlossaryTerm> getSynonyms(@PathVariable String serverName,
                                          @PathVariable("termGUID") String termGUID,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                   InvalidParameterException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getSynonyms";

        GlossaryTermRelationshipRequestBody requestBody = new GlossaryTermRelationshipRequestBody();

        requestBody.setRelationshipTypeName(OpenMetadataType.SYNONYM_RELATIONSHIP_NAME);

        GlossaryTermElementsResponse restResult = restAPI.getRelatedTerms(serverName,
                                                                          termGUID,
                                                                          from,
                                                                          size,
                                                                          false,
                                                                          false,
                                                                          requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of translations terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/translations")
    public List<GlossaryTerm> getTranslations(@PathVariable String serverName,
                                              @PathVariable("termGUID") String termGUID,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                       InvalidParameterException,
                                                                                                       PropertyServerException
    {
        final String methodName = "getTranslations";

        GlossaryTermRelationshipRequestBody requestBody = new GlossaryTermRelationshipRequestBody();

        requestBody.setRelationshipTypeName(OpenMetadataType.TRANSLATION_RELATIONSHIP_NAME);

        GlossaryTermElementsResponse restResult = restAPI.getRelatedTerms(serverName,
                                                                          termGUID,
                                                                          from,
                                                                          size,
                                                                          false,
                                                                          false,
                                                                          requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of preferred terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     * */
    @GetMapping("/terms/{termGUID}/types")
    public List<GlossaryTerm> getTypedBys(@PathVariable String serverName,
                                       @PathVariable("termGUID") String termGUID,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                InvalidParameterException,
                                                                                                PropertyServerException
    {
        final String methodName = "getTypedBys";

        GlossaryTermRelationshipRequestBody requestBody = new GlossaryTermRelationshipRequestBody();

        requestBody.setRelationshipTypeName(OpenMetadataType.TERM_TYPED_BY_RELATIONSHIP_NAME);

        GlossaryTermElementsResponse restResult = restAPI.getRelatedTerms(serverName,
                                                                          termGUID,
                                                                          from,
                                                                          size,
                                                                          false,
                                                                          false,
                                                                          requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of "used-in-contexts" terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     * */
    @GetMapping("/terms/{termGUID}/usedInContext")
    public List<GlossaryTerm> getUsedInContexts(@PathVariable String serverName,
                                                @PathVariable("termGUID") String termGUID,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                         InvalidParameterException,
                                                                                                         PropertyServerException
    {
        final String methodName = "getUsedInContexts";

        GlossaryTermRelationshipRequestBody requestBody = new GlossaryTermRelationshipRequestBody();

        requestBody.setRelationshipTypeName(OpenMetadataType.USED_IN_CONTEXT_RELATIONSHIP_NAME);

        GlossaryTermElementsResponse restResult = restAPI.getRelatedTerms(serverName,
                                                                          termGUID,
                                                                          from,
                                                                          size,
                                                                          false,
                                                                          false,
                                                                          requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list valid values terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     * */
    @GetMapping("/terms/{termGUID}/validValues")
    public List<GlossaryTerm> getValidValues(@PathVariable String serverName,
                                             @PathVariable("termGUID") String termGUID,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                      InvalidParameterException,
                                                                                                      PropertyServerException
    {
        final String methodName = "getValidValues";

        GlossaryTermRelationshipRequestBody requestBody = new GlossaryTermRelationshipRequestBody();

        requestBody.setRelationshipTypeName(OpenMetadataType.VALID_VALUE_RELATIONSHIP_NAME);

        GlossaryTermElementsResponse restResult = restAPI.getRelatedTerms(serverName,
                                                                          termGUID,
                                                                          from,
                                                                          size,
                                                                          false,
                                                                          false,
                                                                          requestBody);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * @param serverName name of the server to route the request to
     * @param termGUID GUID of the term
     * @return term's home glossary
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     * */
    @GetMapping("/terms/{termGUID}/homeGlossary")
    public Glossary getTermHomeGlossary(@PathVariable String serverName,
                                        @PathVariable("termGUID") String termGUID) throws UserNotAuthorizedException,
                                                                                          InvalidParameterException,
                                                                                          PropertyServerException
    {
        final String methodName = "getTermHomeGlossary";

        GlossaryElementResponse restResult = restAPI.getGlossaryForTerm(serverName,
                                                                        termGUID,
                                                                        false,
                                                                        false,
                                                                        null);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossary(restResult.getElement());

    }


    /**
     * @param serverName name of the server to route the request to
     * @param termGUID GUID of the term
     * @param from     the starting index, used for pagination
     * @param size     number of results returned, used for pagination
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     * @return list of term's external glossary links
     */
    @GetMapping("/terms/{termGUID}/externalGlossaryLinks")
    public List<ExternalGlossaryLink> getExternalGlossaryLinksOfTerm(@PathVariable String serverName,
                                                                     @PathVariable("termGUID") String termGUID,
                                                                     @RequestParam(defaultValue = "0") Integer from,
                                                                     @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                                              InvalidParameterException,
                                                                                                                              PropertyServerException
    {
        return null;
    }


    /**
     * @param serverName name of the server to route the request to
     * @param categoryGUID GUID of the category to be retrieved
     * @return the glossary category with the GUID or null
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
    */
    @GetMapping("/categories/{categoryGUID}")
    public GlossaryCategory getCategory(@PathVariable String serverName,
                                        @PathVariable("categoryGUID") String categoryGUID) throws UserNotAuthorizedException,
                                                                                                  InvalidParameterException,
                                                                                                  PropertyServerException
    {
        final String methodName = "getCategory";

        GlossaryCategoryElementResponse restResult = restAPI.getGlossaryCategoryByGUID(serverName,
                                                                                       categoryGUID,
                                                                                       false,
                                                                                       false,
                                                                                       null);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryCategory(restResult.getElement());
    }


    /**
     * @param serverName name of the server to route the request to
     * @param categoryGUID GUID of the category
     * @return the home glossary of the category
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/categories/{categoryGUID}/homeGlossary")
    public Glossary getCategoryHomeGlossary(@PathVariable String serverName,
                                            @PathVariable("categoryGUID") String categoryGUID) throws UserNotAuthorizedException,
                                                                                                      InvalidParameterException,
                                                                                                      PropertyServerException
    {
        final String methodName = "getCategoryHomeGlossary";

        GlossaryElementResponse restResult = restAPI.getGlossaryForCategory(serverName,
                                                                            categoryGUID,
                                                                            false,
                                                                            false,
                                                                            null);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossary(restResult.getElement());
    }


    /**
     * @param serverName name of the server to route the request to
     * @param categoryGUID GUID of the category
     * @param from     the starting index, used for pagination
     * @param size         number of results returned, used for pagination
     * @return the home glossary of the category
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/categories/{categoryGUID}/subcategories")
    public List<GlossaryCategory> getSubcategories(@PathVariable String serverName,
                                                   @PathVariable("categoryGUID") String categoryGUID,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                            InvalidParameterException,
                                                                                                            PropertyServerException
    {
        final String methodName = "getSubcategories";

        GlossaryCategoryElementsResponse restResult = restAPI.getGlossarySubCategories(serverName,
                                                                                       categoryGUID,
                                                                                       from,
                                                                                       size,
                                                                                       false,
                                                                                       false,
                                                                                       null);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryCategories(restResult.getElementList());
    }


    /**
     * @param serverName name of the server to route the request to
     * @param categoryGUID GUID of the category
     * @param from     the starting index, used for pagination
     * @param size         number of results returned, used for pagination
     * @return list of terms corresponding to the category
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/categories/{categoryGUID}/terms")
    public List<GlossaryTerm> getTermsOfCategory(@PathVariable String serverName,
                                                 @PathVariable("categoryGUID") String categoryGUID,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                          InvalidParameterException,
                                                                                                          PropertyServerException
    {
        final String methodName = "getTermsOfCategory";

        GlossaryTermElementsResponse restResult = restAPI.getTermsForGlossaryCategory(serverName,
                                                                                      categoryGUID,
                                                                                      from,
                                                                                      size,
                                                                                      false,
                                                                                      false,
                                                                                      null);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return this.convertGlossaryTerms(restResult.getElementList());
    }


    /**
     * @param serverName name of the server to route the request to
     * @param categoryGUID GUID of the category
     * @param from     the starting index, used for pagination
     * @param size         number of results returned, used for pagination
     * @return list of a category's external glossary links
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws UserNotAuthorizedException if a problem occurs on the omas backend
     */
    @GetMapping("/categories/{categoryGUID}/externalGlossaryLinks")

    public List<ExternalGlossaryLink> getExternalGlossaryLinks(@PathVariable String serverName,
                                                               @PathVariable("categoryGUID") String categoryGUID,
                                                               @RequestParam(defaultValue = "0") Integer from,
                                                               @RequestParam(defaultValue = "100") Integer size) throws UserNotAuthorizedException,
                                                                                                                        InvalidParameterException,
                                                                                                                        PropertyServerException
    {
        return null;
    }


    /**
     * Convert the list of open metadata glossaries to the output for this controller.
     *
     * @param openMetadataGlossaryElements open metadata glossary elements
     * @return converted elements
     */
    private List<Glossary> convertGlossaries(List<GlossaryElement> openMetadataGlossaryElements)
    {
        if (openMetadataGlossaryElements != null)
        {
            List<Glossary> results = new ArrayList<>();

            for (GlossaryElement openMetadataGlossaryElement : openMetadataGlossaryElements)
            {
                results.add(this.convertGlossary(openMetadataGlossaryElement));
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Convert the open metadata glossary to the output for this controller.
     *
     * @param openMetadataGlossaryElement open metadata glossary elements
     * @return converted element
     */
    private Glossary convertGlossary(GlossaryElement openMetadataGlossaryElement)
    {
        if (openMetadataGlossaryElement != null)
        {
            Glossary glossary = new Glossary();

            glossary.setGuid(openMetadataGlossaryElement.getElementHeader().getGUID());
            glossary.setTypeDefName(openMetadataGlossaryElement.getElementHeader().getType().getTypeName());
            glossary.setVersion(openMetadataGlossaryElement.getElementHeader().getVersions().getVersion());
            glossary.setCreatedBy(openMetadataGlossaryElement.getElementHeader().getVersions().getCreatedBy());
            glossary.setCreateTime(openMetadataGlossaryElement.getElementHeader().getVersions().getCreateTime());
            glossary.setUpdatedBy(openMetadataGlossaryElement.getElementHeader().getVersions().getUpdatedBy());
            glossary.setUpdateTime(openMetadataGlossaryElement.getElementHeader().getVersions().getUpdateTime());
            glossary.setStatus(openMetadataGlossaryElement.getElementHeader().getStatus().getName());
            glossary.setEffectiveFromTime(openMetadataGlossaryElement.getGlossaryProperties().getEffectiveFrom());
            glossary.setEffectiveToTime(openMetadataGlossaryElement.getGlossaryProperties().getEffectiveTo());
            glossary.setClassifications(this.convertClassifications(openMetadataGlossaryElement.getElementHeader().getClassifications()));

            Map<String, String> properties = new HashMap<>();

            properties.put(OpenMetadataProperty.QUALIFIED_NAME.name, openMetadataGlossaryElement.getGlossaryProperties().getQualifiedName());
            properties.put(OpenMetadataProperty.DISPLAY_NAME.name, openMetadataGlossaryElement.getGlossaryProperties().getDisplayName());
            properties.put(OpenMetadataProperty.DESCRIPTION.name, openMetadataGlossaryElement.getGlossaryProperties().getDescription());
            properties.put(OpenMetadataType.LANGUAGE_PROPERTY_NAME, openMetadataGlossaryElement.getGlossaryProperties().getLanguage());
            properties.put(OpenMetadataProperty.USAGE.name, openMetadataGlossaryElement.getGlossaryProperties().getUsage());

            if (openMetadataGlossaryElement.getGlossaryProperties().getExtendedProperties() != null)
            {
                for (String propertyName : openMetadataGlossaryElement.getGlossaryProperties().getExtendedProperties().keySet())
                {
                    if (openMetadataGlossaryElement.getGlossaryProperties().getExtendedProperties().get(propertyName) != null)
                    {
                        properties.put(propertyName, openMetadataGlossaryElement.getGlossaryProperties().getExtendedProperties().get(propertyName).toString());
                    }
                    else
                    {
                        properties.put(propertyName, null);
                    }
                }
            }

            glossary.setProperties(properties);

            return glossary;
        }

        return null;
    }



    /**
     * Convert the list of open metadata glossary terms to the output for this controller.
     *
     * @param openMetadataGlossaryTermElements open metadata glossary term elements
     * @return converted elements
     */
    private List<GlossaryTerm> convertGlossaryTerms(List<GlossaryTermElement> openMetadataGlossaryTermElements)
    {
        if (openMetadataGlossaryTermElements != null)
        {
            List<GlossaryTerm> results = new ArrayList<>();

            for (GlossaryTermElement openMetadataGlossaryTermElement : openMetadataGlossaryTermElements)
            {
                results.add(this.convertGlossaryTerm(openMetadataGlossaryTermElement));
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Convert the open metadata glossary term to the output for this controller.
     *
     * @param openMetadataGlossaryTermElement open metadata glossary term elements
     * @return converted element
     */
    private GlossaryTerm convertGlossaryTerm(GlossaryTermElement openMetadataGlossaryTermElement)
    {
        if (openMetadataGlossaryTermElement != null)
        {
            GlossaryTerm glossaryTerm = new GlossaryTerm();

            glossaryTerm.setGuid(openMetadataGlossaryTermElement.getElementHeader().getGUID());
            glossaryTerm.setTypeDefName(openMetadataGlossaryTermElement.getElementHeader().getType().getTypeName());
            glossaryTerm.setVersion(openMetadataGlossaryTermElement.getElementHeader().getVersions().getVersion());
            glossaryTerm.setCreatedBy(openMetadataGlossaryTermElement.getElementHeader().getVersions().getCreatedBy());
            glossaryTerm.setCreateTime(openMetadataGlossaryTermElement.getElementHeader().getVersions().getCreateTime());
            glossaryTerm.setUpdatedBy(openMetadataGlossaryTermElement.getElementHeader().getVersions().getUpdatedBy());
            glossaryTerm.setUpdateTime(openMetadataGlossaryTermElement.getElementHeader().getVersions().getUpdateTime());
            glossaryTerm.setStatus(openMetadataGlossaryTermElement.getElementHeader().getStatus().getName());
            glossaryTerm.setEffectiveFromTime(openMetadataGlossaryTermElement.getGlossaryTermProperties().getEffectiveFrom());
            glossaryTerm.setEffectiveToTime(openMetadataGlossaryTermElement.getGlossaryTermProperties().getEffectiveTo());
            glossaryTerm.setClassifications(this.convertClassifications(openMetadataGlossaryTermElement.getElementHeader().getClassifications()));

            Map<String, String> properties = new HashMap<>();

            properties.put(OpenMetadataProperty.QUALIFIED_NAME.name, openMetadataGlossaryTermElement.getGlossaryTermProperties().getQualifiedName());
            properties.put(OpenMetadataProperty.DISPLAY_NAME.name, openMetadataGlossaryTermElement.getGlossaryTermProperties().getDisplayName());
            properties.put(OpenMetadataProperty.SUMMARY.name, openMetadataGlossaryTermElement.getGlossaryTermProperties().getSummary());
            properties.put(OpenMetadataProperty.DESCRIPTION.name, openMetadataGlossaryTermElement.getGlossaryTermProperties().getDescription());
            properties.put(OpenMetadataProperty.EXAMPLES.name, openMetadataGlossaryTermElement.getGlossaryTermProperties().getExamples());
            properties.put(OpenMetadataProperty.ABBREVIATION.name, openMetadataGlossaryTermElement.getGlossaryTermProperties().getUsage());
            properties.put(OpenMetadataProperty.USAGE.name, openMetadataGlossaryTermElement.getGlossaryTermProperties().getUsage());
            properties.put(OpenMetadataProperty.PUBLISH_VERSION_ID.name, openMetadataGlossaryTermElement.getGlossaryTermProperties().getPublishVersionIdentifier());

            if (openMetadataGlossaryTermElement.getGlossaryTermProperties().getExtendedProperties() != null)
            {
                for (String propertyName : openMetadataGlossaryTermElement.getGlossaryTermProperties().getExtendedProperties().keySet())
                {
                    if (openMetadataGlossaryTermElement.getGlossaryTermProperties().getExtendedProperties().get(propertyName) != null)
                    {
                        properties.put(propertyName, openMetadataGlossaryTermElement.getGlossaryTermProperties().getExtendedProperties().get(propertyName).toString());
                    }
                    else
                    {
                        properties.put(propertyName, null);
                    }
                }
            }

            glossaryTerm.setProperties(properties);

            return glossaryTerm;
        }

        return null;
    }


    /**
     * Convert the list of open metadata glossary categories to the output for this controller.
     *
     * @param openMetadataGlossaryCategoryElements open metadata glossary category elements
     * @return converted elements
     */
    private List<GlossaryCategory> convertGlossaryCategories(List<GlossaryCategoryElement> openMetadataGlossaryCategoryElements)
    {
        if (openMetadataGlossaryCategoryElements != null)
        {
            List<GlossaryCategory> results = new ArrayList<>();

            for (GlossaryCategoryElement openMetadataGlossaryCategoryElement : openMetadataGlossaryCategoryElements)
            {
                results.add(this.convertGlossaryCategory(openMetadataGlossaryCategoryElement));
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Convert the open metadata glossary category to the output for this controller.
     *
     * @param openMetadataGlossaryCategoryElement open metadata glossary category elements
     * @return converted element
     */
    private GlossaryCategory convertGlossaryCategory(GlossaryCategoryElement openMetadataGlossaryCategoryElement)
    {
        if (openMetadataGlossaryCategoryElement != null)
        {
            GlossaryCategory glossaryCategory = new GlossaryCategory();

            glossaryCategory.setGuid(openMetadataGlossaryCategoryElement.getElementHeader().getGUID());
            glossaryCategory.setTypeDefName(openMetadataGlossaryCategoryElement.getElementHeader().getType().getTypeName());
            glossaryCategory.setVersion(openMetadataGlossaryCategoryElement.getElementHeader().getVersions().getVersion());
            glossaryCategory.setCreatedBy(openMetadataGlossaryCategoryElement.getElementHeader().getVersions().getCreatedBy());
            glossaryCategory.setCreateTime(openMetadataGlossaryCategoryElement.getElementHeader().getVersions().getCreateTime());
            glossaryCategory.setUpdatedBy(openMetadataGlossaryCategoryElement.getElementHeader().getVersions().getUpdatedBy());
            glossaryCategory.setUpdateTime(openMetadataGlossaryCategoryElement.getElementHeader().getVersions().getUpdateTime());
            glossaryCategory.setStatus(openMetadataGlossaryCategoryElement.getElementHeader().getStatus().getName());
            glossaryCategory.setEffectiveFromTime(openMetadataGlossaryCategoryElement.getGlossaryCategoryProperties().getEffectiveFrom());
            glossaryCategory.setEffectiveToTime(openMetadataGlossaryCategoryElement.getGlossaryCategoryProperties().getEffectiveTo());
            glossaryCategory.setClassifications(this.convertClassifications(openMetadataGlossaryCategoryElement.getElementHeader().getClassifications()));

            Map<String, String> properties = new HashMap<>();

            properties.put(OpenMetadataProperty.QUALIFIED_NAME.name, openMetadataGlossaryCategoryElement.getGlossaryCategoryProperties().getQualifiedName());
            properties.put(OpenMetadataProperty.DISPLAY_NAME.name, openMetadataGlossaryCategoryElement.getGlossaryCategoryProperties().getDisplayName());
            properties.put(OpenMetadataProperty.DESCRIPTION.name, openMetadataGlossaryCategoryElement.getGlossaryCategoryProperties().getDescription());

            if (openMetadataGlossaryCategoryElement.getGlossaryCategoryProperties().getExtendedProperties() != null)
            {
                for (String propertyName : openMetadataGlossaryCategoryElement.getGlossaryCategoryProperties().getExtendedProperties().keySet())
                {
                    if (openMetadataGlossaryCategoryElement.getGlossaryCategoryProperties().getExtendedProperties().get(propertyName) != null)
                    {
                        properties.put(propertyName, openMetadataGlossaryCategoryElement.getGlossaryCategoryProperties().getExtendedProperties().get(propertyName).toString());
                    }
                    else
                    {
                        properties.put(propertyName, null);
                    }
                }
            }

            glossaryCategory.setProperties(properties);

            return glossaryCategory;
        }

        return null;
    }




    /**
     * Convert the list of open metadata classifications to the output for this controller.
     *
     * @param openMetadataClassifications open metadata classifications
     * @return converted elements
     */
    private List<GlossaryViewClassification> convertClassifications(List<ElementClassification> openMetadataClassifications)
    {
        if (openMetadataClassifications != null)
        {
            List<GlossaryViewClassification> results = new ArrayList<>();

            for (ElementClassification openMetadataClassification : openMetadataClassifications)
            {
                GlossaryViewClassification glossaryViewClassification = new GlossaryViewClassification();

                glossaryViewClassification.setClassificationType(openMetadataClassification.getClassificationName());
                glossaryViewClassification.setName(openMetadataClassification.getClassificationName());
                glossaryViewClassification.setCreatedBy(openMetadataClassification.getVersions().getCreatedBy());
                glossaryViewClassification.setCreateTime(openMetadataClassification.getVersions().getCreateTime());
                glossaryViewClassification.setUpdatedBy(openMetadataClassification.getVersions().getUpdatedBy());
                glossaryViewClassification.setUpdateTime(openMetadataClassification.getVersions().getUpdateTime());
                glossaryViewClassification.setStatus(openMetadataClassification.getStatus().getName());

                if (openMetadataClassification.getClassificationProperties() != null)
                {
                    Map<String, String> properties = new HashMap<>();

                    for (String propertyName : openMetadataClassification.getClassificationProperties().keySet())
                    {
                        if (openMetadataClassification.getClassificationProperties().get(propertyName) != null)
                        {
                            properties.put(propertyName, openMetadataClassification.getClassificationProperties().get(propertyName).toString());
                        }
                        else
                        {
                            properties.put(propertyName, null);
                        }
                    }

                    glossaryViewClassification.setProperties(properties);
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Convert the open metadata glossary category to the output for this controller.
     *
     * @param openMetadataElement open metadata glossary category elements
     * @return converted element
     */
    private GlossaryViewEntityDetail convertOpenMetadataElement(OpenMetadataElement openMetadataElement)
    {
        if (openMetadataElement != null)
        {
            GlossaryViewEntityDetail glossaryViewEntityDetail = new GlossaryViewEntityDetail();

            glossaryViewEntityDetail.setGuid(openMetadataElement.getElementGUID());
            glossaryViewEntityDetail.setTypeDefName(openMetadataElement.getType().getTypeName());
            glossaryViewEntityDetail.setVersion(openMetadataElement.getVersions().getVersion());
            glossaryViewEntityDetail.setCreatedBy(openMetadataElement.getVersions().getCreatedBy());
            glossaryViewEntityDetail.setCreateTime(openMetadataElement.getVersions().getCreateTime());
            glossaryViewEntityDetail.setUpdatedBy(openMetadataElement.getVersions().getUpdatedBy());
            glossaryViewEntityDetail.setUpdateTime(openMetadataElement.getVersions().getUpdateTime());
            glossaryViewEntityDetail.setStatus(openMetadataElement.getStatus().getName());
            glossaryViewEntityDetail.setEffectiveFromTime(openMetadataElement.getEffectiveFromTime());
            glossaryViewEntityDetail.setEffectiveToTime(openMetadataElement.getEffectiveToTime());
            glossaryViewEntityDetail.setClassifications(this.convertAttachedClassifications(openMetadataElement.getClassifications()));

            Map<String, String> properties = new HashMap<>();

            if (openMetadataElement.getElementProperties() != null)
            {
                for (String propertyName : openMetadataElement.getElementProperties().getPropertyValueMap().keySet())
                {
                    if (openMetadataElement.getElementProperties().getPropertyValue(propertyName) != null)
                    {
                        properties.put(propertyName, openMetadataElement.getElementProperties().getPropertyValue(propertyName).valueAsString());
                    }
                    else
                    {
                        properties.put(propertyName, null);
                    }
                }
            }

            glossaryViewEntityDetail.setProperties(properties);

            return glossaryViewEntityDetail;
        }

        return null;
    }



    /**
     * Convert the list of open metadata classifications to the output for this controller.
     *
     * @param openMetadataClassifications open metadata classifications
     * @return converted elements
     */
    private List<GlossaryViewClassification> convertAttachedClassifications(List<AttachedClassification> openMetadataClassifications)
    {
        if (openMetadataClassifications != null)
        {
            List<GlossaryViewClassification> results = new ArrayList<>();

            for (AttachedClassification openMetadataClassification : openMetadataClassifications)
            {
                GlossaryViewClassification glossaryViewClassification = new GlossaryViewClassification();

                glossaryViewClassification.setClassificationType(openMetadataClassification.getClassificationName());
                glossaryViewClassification.setName(openMetadataClassification.getClassificationName());
                glossaryViewClassification.setCreatedBy(openMetadataClassification.getVersions().getCreatedBy());
                glossaryViewClassification.setCreateTime(openMetadataClassification.getVersions().getCreateTime());
                glossaryViewClassification.setUpdatedBy(openMetadataClassification.getVersions().getUpdatedBy());
                glossaryViewClassification.setUpdateTime(openMetadataClassification.getVersions().getUpdateTime());
                glossaryViewClassification.setStatus(openMetadataClassification.getStatus().getName());

                if (openMetadataClassification.getClassificationProperties() != null)
                {
                    glossaryViewClassification.setProperties(openMetadataClassification.getClassificationProperties().getPropertiesAsStrings());
                }
            }

            return results;
        }

        return null;
    }
}