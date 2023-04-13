/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.glossary;

import org.odpi.openmetadata.accessservices.glossaryview.client.GlossaryViewClient;
import org.odpi.openmetadata.accessservices.glossaryview.exception.GlossaryViewOmasException;
import org.odpi.openmetadata.accessservices.glossaryview.rest.ExternalGlossaryLink;
import org.odpi.openmetadata.accessservices.glossaryview.rest.Glossary;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryCategory;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryTerm;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.SecureController;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.CategoryNotFoundException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.GlossaryNotFoundException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.TermNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This controller serves all requests for glossaries, categories and terms.
 */
@RestController
@RequestMapping("/api/glossaries")
public class GlossaryViewController extends SecureController {

    @Autowired
    private GlossaryViewClient glossaryViewClient;

    /**
     * @param from the index from witch the results to start, used for pagination
     * @param size number of results returned, used for pagination
     * @return all the glossaries
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping
    public List<Glossary> getAllGlossaries(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getAllGlossaries(userId, from, size);
    }

    /**
     * @param from the index from witch the results to start, used for pagination
     * @param size number of results returned, used for pagination
     * @return all the glossary terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/terms")
    public List<GlossaryTerm> getAllGlossaryTerms(
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getAllGlossaryTerms(userId, from, size);
    }

    /**
     * @param from the index from witch the results to start, used for pagination
     * @param size number of results returned, used for pagination
     * @return all the glossary categories
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/categories")
    public List<GlossaryCategory> getAllGlossaryCategories(
                                                    @RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getAllCategories(userId, from, size);
    }

    /**
     * @param glossaryGUID GUID of the glossary to be retrieved
     * @return the glossary with the GUID or null
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/{glossaryGUID}")
    public Glossary getGlossary(@PathVariable("glossaryGUID") String glossaryGUID)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        Glossary glossary = glossaryViewClient.getGlossary(userId, glossaryGUID);
        if (glossary == null) {
            throw new GlossaryNotFoundException("Could not find glossary with guid " + glossaryGUID);
        }
        return glossary;
    }

    /**
     * @param glossaryGUID GUID of the glossary
     * @param from         the index from witch the results to start, used for pagination
     * @param size         number of results returned, used for pagination
     * @return list of categories
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/{glossaryGUID}/categories")
    public List<GlossaryCategory> getCategories(@PathVariable("glossaryGUID") String glossaryGUID,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getCategories(userId, glossaryGUID, from, size);
    }

    /**
     * @param glossaryGUID GUID of the glossary
     * @param from         the index from witch the results to start, used for pagination
     * @param size         number of results returned, used for pagination
     * @return list of terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     * */
    @GetMapping("/{glossaryGUID}/terms")
    public List<GlossaryTerm> getTermsOfGlossary(@PathVariable("glossaryGUID") String glossaryGUID,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getTermsOfGlossary(userId, glossaryGUID, from, size);
    }

    /**
     * @param glossaryGUID GUID of the glossary
     * @param from         the index from witch the results to start, used for pagination
     * @param size         number of results returned, used for pagination
     * @return list of external glossary links
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     * */
    @GetMapping("/{glossaryGUID}/externalGlossaryLinks")
    public List<ExternalGlossaryLink> getExternalGlossaryLinksOfGlossary(@PathVariable("glossaryGUID") String glossaryGUID,
                                                                         @RequestParam(defaultValue = "0") Integer from,
                                                                         @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getExternalGlossaryLinksOfGlossary(userId, glossaryGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @return the term with the GUID
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
    */
    @GetMapping("/terms/{termGUID}")
    public GlossaryTerm getAntonyms(@PathVariable("termGUID") String termGUID)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        GlossaryTerm term = glossaryViewClient.getTerm(userId, termGUID);
        if (term == null) {
            throw new TermNotFoundException("Could not find antonym for term with guid " + termGUID);
        }
        return term;
    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of antonyms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/antonyms")
    public List<GlossaryTerm> getAntonyms(@PathVariable("termGUID") String termGUID,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getAntonyms(userId, termGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of terms representing the assigned elements
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/assignedElements")
    public List<GlossaryTerm> getAssignedElements(@PathVariable("termGUID") String termGUID,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getAssignedElements(userId, termGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of terms representing the antonyms of the term with GUID
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/attributes")
    public List<GlossaryTerm> getAttributes(@PathVariable("termGUID") String termGUID,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getAttributes(userId, termGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of is-a terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/isA")
    public List<GlossaryTerm> getIsATerms(@PathVariable("termGUID") String termGUID,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getIsA(userId, termGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of preferred terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/preferredTerms")
    public List<GlossaryTerm> getPreferredTerms(@PathVariable("termGUID") String termGUID,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getPreferredTerms(userId, termGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of related terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/relatedTerms")
    public List<GlossaryTerm> getRelatedTerms(@PathVariable("termGUID") String termGUID,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getRelatedTerms(userId, termGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of replacement terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/replacementTerms")
    public List<GlossaryTerm> getReplacementTerms(@PathVariable("termGUID") String termGUID,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getReplacementTerms(userId, termGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of subtypes terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/subtypes")
    public List<GlossaryTerm> getSubtypes(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getSubtypes(userId, termGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of synonym terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/synonyms")
    public List<GlossaryTerm> getSynonyms(@PathVariable("termGUID") String termGUID,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getSynonyms(userId, termGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of translations terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/terms/{termGUID}/translations")
    public List<GlossaryTerm> getTranslations(@PathVariable("termGUID") String termGUID,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getTranslations(userId, termGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of preferred terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     * */
    @GetMapping("/terms/{termGUID}/types")
    public List<GlossaryTerm> getTypes(@PathVariable("termGUID") String termGUID,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getTypes(userId, termGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list of "used-in-contexts" terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     * */
    @GetMapping("/terms/{termGUID}/usedInContext")
    public List<GlossaryTerm> getUsedInContexts(@PathVariable("termGUID") String termGUID,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getUsedInContexts(userId, termGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @return list valid values terms
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     * */
    @GetMapping("/terms/{termGUID}/validValues")
    public List<GlossaryTerm> getValidValues(@PathVariable("termGUID") String termGUID,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getValidValues(userId, termGUID, from, size);
    }

    /**
     * @param termGUID GUID of the term
     * @return term's home glossary
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     * */
    @GetMapping("/terms/{termGUID}/homeGlossary")
    public Glossary getSynonyms(@PathVariable("termGUID") String termGUID)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        Glossary termHomeGlossary = glossaryViewClient.getTermHomeGlossary(userId, termGUID);
        if (termHomeGlossary == null) {
            throw new GlossaryNotFoundException("The home glossary was not found for term with guid " + termGUID);
        }
        return termHomeGlossary;

    }

    /**
     * @param termGUID GUID of the term
     * @param from     the index from witch the results to start, used for pagination
     * @param size     number of results returned, used for pagination
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     * @return list of term's external glossary links
     */
    @GetMapping("/terms/{termGUID}/externalGlossaryLinks")
    public List<ExternalGlossaryLink> getExternalGlossaryLinksOfTerm(@PathVariable("termGUID") String termGUID,
                                                                     @RequestParam(defaultValue = "0") Integer from,
                                                                     @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getExternalGlossaryLinksOfTerm(userId, termGUID, from, size);
    }

    /**
     * @param categoryGUID GUID of the category to be retrieved
     * @param request the http servlet request
     * @return the glossary category with the GUID or null
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
    */
    @GetMapping("/categories/{categoryGUID}")
    public GlossaryCategory getCategory(@PathVariable("categoryGUID") String categoryGUID, 
                                        HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        GlossaryCategory category = glossaryViewClient.getCategory(userId, categoryGUID);
        if (category == null) {
            throw new CategoryNotFoundException("Could not find the category, please check that the guid is correct " + categoryGUID);
        }
        return category;
    }

    /**
     * @param categoryGUID GUID of the category
     * @return the home glossary of the category
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/categories/{categoryGUID}/homeGlossary")
    public Glossary getCategoryHomeGlossary(@PathVariable("categoryGUID") String categoryGUID)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        Glossary categoryHomeGlossary = glossaryViewClient.getCategoryHomeGlossary(userId, categoryGUID);
        if (categoryHomeGlossary == null) {
            throw new GlossaryNotFoundException("Could not find the home glossary, please check that the guid is correct " + categoryGUID);
        }
        return categoryHomeGlossary;
    }

    /**
     * @param categoryGUID GUID of the category
     * @param from         the index from witch the results to start, used for pagination
     * @param size         number of results returned, used for pagination
     * @return the home glossary of the category
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/categories/{categoryGUID}/subcategories")
    public List<GlossaryCategory> getSubcategories(@PathVariable("categoryGUID") String categoryGUID,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getSubcategories(userId, categoryGUID, from, size);
    }

    /**
     * @param categoryGUID GUID of the catgitegory
     * @param from         the index from witch the results to start, used for pagination
     * @param size         number of results returned, used for pagination
     * @return list of terms corresponding to the category
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/categories/{categoryGUID}/terms")
    public List<GlossaryTerm> getTermsOfCategory(@PathVariable("categoryGUID") String categoryGUID,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getTermsOfCategory(userId, categoryGUID, from, size);
    }

    /**
     * @param categoryGUID GUID of the category
     * @param from         the index from witch the results to start, used for pagination
     * @param size         number of results returned, used for pagination
     * @return list of a category's external glossary links
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    @GetMapping("/categories/{categoryGUID}/externalGlossaryLinks")
    public List<ExternalGlossaryLink> getExternalGlossaryLinks(@PathVariable("categoryGUID") String categoryGUID,
                                                               @RequestParam(defaultValue = "0") Integer from,
                                                               @RequestParam(defaultValue = "100") Integer size)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser();
        return glossaryViewClient.getExternalGlossaryLinksOfCategory(userId, categoryGUID, from, size);
    }
}