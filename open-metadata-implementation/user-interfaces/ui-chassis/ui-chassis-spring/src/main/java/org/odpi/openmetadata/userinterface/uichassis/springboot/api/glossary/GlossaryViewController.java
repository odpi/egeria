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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/glossaries")
public class GlossaryViewController extends SecureController {

    @Autowired
    private GlossaryViewClient glossaryViewClient;

    @GetMapping
    public List<Glossary> getAllGlossaries(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getAllGlossaries(userId, from, size);
    }


    @GetMapping("/{glossaryGUID}")
    public Glossary getGlossary(@PathVariable("categoryGUID") String glossaryGUID, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getGlossary(userId, glossaryGUID);
    }

    @GetMapping("/{glossaryGUID}/categories")
    public List<GlossaryCategory> getCategories(@PathVariable("glossaryGUID") String glossaryGUID,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getCategories(userId, glossaryGUID, from, size);
    }

    @GetMapping("/{glossaryGUID}/terms")
    public List<GlossaryTerm> getTermsOfGlossary(@PathVariable("glossaryGUID") String glossaryGUID,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getTermsOfGlossary(userId, glossaryGUID, from, size);
    }

    @GetMapping("/{glossaryGUID}/externalGlossaryLinks")
    public List<ExternalGlossaryLink> getExternalGlossaryLinksOfGlossary(@PathVariable("glossaryGUID") String glossaryGUID,
                                                                         @RequestParam(defaultValue = "0") Integer from,
                                                                         @RequestParam(defaultValue = "100") Integer size,
                                                                         HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getExternalGlossaryLinksOfGlossary(userId, glossaryGUID, from, size);
    }

    @GetMapping("/terms/{termGUID}")
    public GlossaryTerm getAntonyms(@PathVariable("termGUID") String termGUID, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getTerm(userId, termGUID);
    }

    @GetMapping("/terms/{termGUID}/antonyms")
    public List<GlossaryTerm> getAntonyms(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getAntonyms(userId, termGUID, from, size);
    }

    @GetMapping("/terms/{termGUID}/assignedElements")
    public List<GlossaryTerm> getAssignedElements(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getAssignedElements(userId, termGUID, from, size);
    }

    @GetMapping("/terms/{termGUID}/attributes")
    public List<GlossaryTerm> getAttributes(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getAttributes(userId, termGUID, from, size);
    }

    @GetMapping("/terms/{termGUID}/isA")
    public List<GlossaryTerm> getIsATerms(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getIsA(userId, termGUID, from, size);
    }

    @GetMapping("/terms/{termGUID}/preferredTerms")
    public List<GlossaryTerm> getPreferredTerms(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getPreferredTerms(userId, termGUID, from, size);
    }

    @GetMapping("/terms/{termGUID}/relatedTerms")
    public List<GlossaryTerm> getRelatedTerms(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getRelatedTerms(userId, termGUID, from, size);
    }

    @GetMapping("/terms/{termGUID}/replacementTerms")
    public List<GlossaryTerm> getReplacementTerms(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getReplacementTerms(userId, termGUID, from, size);
    }

    @GetMapping("/terms/{termGUID}/subtypes")
    public List<GlossaryTerm> getSubtypes(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getSubtypes(userId, termGUID, from, size);
    }

    @GetMapping("/terms/{termGUID}/synonyms")
    public List<GlossaryTerm> getSynonyms(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getSynonyms(userId, termGUID, from, size);
    }

    @GetMapping("/terms/{termGUID}/translations")
    public List<GlossaryTerm> getTranslations(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getTranslations(userId, termGUID, from, size);
    }

    @GetMapping("/terms/{termGUID}/types")
    public List<GlossaryTerm> getTypes(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getTypes(userId, termGUID, from, size);
    }

    @GetMapping("/terms/{termGUID}/usedInContext")
    public List<GlossaryTerm> getUsedInContexts(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getUsedInContexts(userId, termGUID, from, size);
    }

    @GetMapping("/terms/{termGUID}/validValues")
    public List<GlossaryTerm> getValidValues(@PathVariable("termGUID") String termGUID, @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getValidValues(userId, termGUID, from, size);
    }


    @GetMapping("/terms/{termGUID}/homeGlossary")
    public Glossary getSynonyms(@PathVariable("termGUID") String termGUID, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getTermHomeGlossary(userId, termGUID);
    }

    @GetMapping("/terms/{termGUID}/externalGlossaryLinks")
    public List<ExternalGlossaryLink> getExternalGlossaryLinksOfTerm(@PathVariable("termGUID") String termGUID,
                                                                     @RequestParam(defaultValue = "0") Integer from,
                                                                     @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getExternalGlossaryLinksOfTerm(userId, termGUID, from, size);
    }

    @GetMapping("/categories/{categoryGUID}")
    public GlossaryCategory getCategory(@PathVariable("categoryGUID") String categoryGUID, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getCategory(userId, categoryGUID);
    }

    @GetMapping("/categories/{categoryGUID}/homeGlossary")
    public Glossary getCategoryHomeGlossary(@PathVariable("categoryGUID") String categoryGUID, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getCategoryHomeGlossary(userId, categoryGUID);
    }

    @GetMapping("/categories/{categoryGUID}/subcategories")
    public List<GlossaryCategory> getSubcategories(@PathVariable("categoryGUID") String categoryGUID, @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getSubcategories(userId, categoryGUID, from, size);
    }

    @GetMapping("/categories/{categoryGUID}/terms")
    public List<GlossaryTerm> getTermsOfCategory(@PathVariable("termGUID") String categoryGUID, @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getTermsOfCategory(userId, categoryGUID, from, size);
    }

    @GetMapping("/categories/{categoryGUID}/externalGlossaryLinks")
    public List<ExternalGlossaryLink> getExternalGlossaryLinks(@PathVariable("categoryGUID") String categoryGUID,
                                                               @RequestParam(defaultValue = "0") Integer from,
                                                               @RequestParam(defaultValue = "100") Integer size, HttpServletRequest request)
            throws GlossaryViewOmasException, InvalidParameterException, PropertyServerException {
        String userId = getUser(request);
        return glossaryViewClient.getExternalGlossaryLinksOfCategory(userId, categoryGUID, from, size);
    }
}