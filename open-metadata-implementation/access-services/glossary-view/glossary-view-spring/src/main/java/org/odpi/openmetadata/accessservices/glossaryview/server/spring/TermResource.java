/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.accessservices.glossaryview.server.service.TermService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import static org.odpi.openmetadata.accessservices.glossaryview.server.spring.OmasRegistration.*;

/**
 * Spring Rest Controller defining 'GlossaryTerm' oriented endpoints
 */
@RestController
@Validated
@RequestMapping("/servers/{serverName}/open-metadata/access-services/glossary-view/users/{userId}")

@Tag(name="Glossary View OMAS", description="The Glossary View OMAS provides APIs and events for retrieving glossaries, categories and terms.", externalDocs=@ExternalDocumentation(description="Glossary View Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/glossary-view/"))

public class TermResource {

    private TermService termService;

    /**
     * Called by Spring
     */
    public TermResource() {
        termService = new TermService();
    }

    /**
     * Extract all terms definitions
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param from from
     * @param size size
     *
     * @return all terms
     */
    @GetMapping( path = "/terms")
    public GlossaryViewEntityDetailResponse getAllTerms(@PathVariable("serverName") String serverName,
                                                             @PathVariable("userId") String userId,
                                                             @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                             @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getAllTerms(userId, serverName, from, size);
    }

    /**
     * Extract a term definition for the given GUID
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID category GUID
     *
     * @return term
     */
    @GetMapping( path = "/terms/{termGUID}")
    public GlossaryViewEntityDetailResponse getTerm(@PathVariable("serverName") String serverName,
                                                    @PathVariable("userId") String userId,
                                                    @PathVariable("termGUID") @NotBlank String termGUID) {
        return termService.getTerm(userId, serverName, termGUID);
    }


    /**
     * Extract term definitions for the given glossary GUID via the 'TermAnchor' type relationships
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param glossaryGUID glossary GUID
     * @param from from
     * @param size size
     *
     * @return terms
     */
    @GetMapping( path = "/glossaries/{glossaryGUID}/terms")
    public GlossaryViewEntityDetailResponse getTermsViaTermAnchorRelationships(@PathVariable("serverName") String serverName,
                                                                               @PathVariable("userId") String userId,
                                                                               @PathVariable("glossaryGUID") @NotBlank String glossaryGUID,
                                                                               @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                               @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getTermsViaTermAnchorRelationships(userId, serverName, glossaryGUID, from, size);
    }

    /**
     * Extract term definitions for the given GUID via the 'TermCategorization' type relationships
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param categoryGUID category GUID
     * @param from from
     * @param size size
     *
     * @return subcategories
     */
    @GetMapping( path = "/categories/{categoryGUID}/terms")
    public GlossaryViewEntityDetailResponse getTermsViaTermCategorizationRelationships(@PathVariable("serverName") String serverName,
                                                                                       @PathVariable("userId") String userId,
                                                                                       @PathVariable("categoryGUID") @NotBlank String categoryGUID,
                                                                                       @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                                       @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getTermsViaTermCategorizationRelationships(userId, serverName, categoryGUID, from, size);
    }

    /**
     * Extract external glossary link definitions for the given term
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return external glossary links
     */
    @GetMapping( path = "/terms/{termGUID}/external-glossary-links")
    public GlossaryViewEntityDetailResponse getExternalGlossaryLinks(@PathVariable("serverName") String serverName,
                                                                     @PathVariable("userId") String userId,
                                                                     @PathVariable("termGUID") @NotBlank String termGUID,
                                                                     @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                     @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getExternalGlossaryLinks(userId, serverName, termGUID, from, size);
    }

    /**
     * Extract related terms
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return related terms
     */
    @GetMapping( path = "/terms/{termGUID}/see-also")
    public GlossaryViewEntityDetailResponse getRelatedTerms(@PathVariable("serverName") String serverName,
                                                            @PathVariable("userId") String userId,
                                                            @PathVariable("termGUID") @NotBlank String termGUID,
                                                            @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                            @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getRelatedTerms(userId, serverName, termGUID, from, size);
    }

    /**
     * Extract synonyms
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return synonyms
     */
    @GetMapping( path = "/terms/{termGUID}/synonyms")
    public GlossaryViewEntityDetailResponse getSynonyms(@PathVariable("serverName") String serverName,
                                                        @PathVariable("userId") String userId,
                                                        @PathVariable("termGUID") @NotBlank String termGUID,
                                                        @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                        @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getSynonyms(userId, serverName, termGUID, from, size);
    }

    /**
     * Extract antonyms
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return antonyms
     */
    @GetMapping( path = "/terms/{termGUID}/antonyms")
    public GlossaryViewEntityDetailResponse getAntonyms(@PathVariable("serverName") String serverName,
                                                        @PathVariable("userId") String userId,
                                                        @PathVariable("termGUID") @NotBlank String termGUID,
                                                        @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                        @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getAntonyms(userId, serverName, termGUID, from, size);
    }

    /**
     * Extract preferred terms
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return preferred terms
     */
    @GetMapping( path = "/terms/{termGUID}/preferred-terms")
    public GlossaryViewEntityDetailResponse getPreferredTerms(@PathVariable("serverName") String serverName,
                                                              @PathVariable("userId") String userId,
                                                              @PathVariable("termGUID") @NotBlank String termGUID,
                                                              @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                              @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getPreferredTerms(userId, serverName, termGUID, from, size);
    }

    /**
     * Extract replacement terms
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return replacement terms
     */
    @GetMapping( path = "/terms/{termGUID}/replacement-terms")
    public GlossaryViewEntityDetailResponse getReplacementTerms(@PathVariable("serverName") String serverName,
                                                                @PathVariable("userId") String userId,
                                                                @PathVariable("termGUID") @NotBlank String termGUID,
                                                                @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getReplacementTerms(userId, serverName, termGUID, from, size);
    }

    /**
     * Extract translations
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return translations
     */
    @GetMapping( path = "/terms/{termGUID}/translations")
    public GlossaryViewEntityDetailResponse getTranslations(@PathVariable("serverName") String serverName,
                                                            @PathVariable("userId") String userId,
                                                            @PathVariable("termGUID") @NotBlank String termGUID,
                                                            @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                            @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getTranslations(userId, serverName, termGUID, from, size);
    }

    /**
     * Extract "is a"
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return "is a"
     */
    @GetMapping( path = "/terms/{termGUID}/is-a")
    public GlossaryViewEntityDetailResponse getIsA(@PathVariable("serverName") String serverName,
                                                   @PathVariable("userId") String userId,
                                                   @PathVariable("termGUID") @NotBlank String termGUID,
                                                   @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                   @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getIsA(userId, serverName, termGUID, from, size);
    }

    /**
     * Extract valid values
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return valid values
     */
    @GetMapping( path = "/terms/{termGUID}/valid-values")
    public GlossaryViewEntityDetailResponse getValidValues(@PathVariable("serverName") String serverName,
                                                           @PathVariable("userId") String userId,
                                                           @PathVariable("termGUID") @NotBlank String termGUID,
                                                           @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                           @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getValidValues(userId, serverName, termGUID, from, size);
    }

    /**
     * Extract "used in contexts"
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return "used in contexts"
     */
    @GetMapping( path = "/terms/{termGUID}/used-in-contexts")
    public GlossaryViewEntityDetailResponse getUsedInContexts(@PathVariable("serverName") String serverName,
                                                              @PathVariable("userId") String userId,
                                                              @PathVariable("termGUID") @NotBlank String termGUID,
                                                              @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                              @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getUsedInContexts(userId, serverName, termGUID, from, size);
    }

    /**
     * Extract assigned elements
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return assigned elements
     */
    @GetMapping( path = "/terms/{termGUID}/assigned-elements")
    public GlossaryViewEntityDetailResponse getAssignedElements(@PathVariable("serverName") String serverName,
                                                                @PathVariable("userId") String userId,
                                                                @PathVariable("termGUID") @NotBlank String termGUID,
                                                                @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getAssignedElements(userId, serverName, termGUID, from, size);
    }

    /**
     * Extract attributes
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return attributes
     */
    @GetMapping( path = "/terms/{termGUID}/attributes")
    public GlossaryViewEntityDetailResponse getAttributes(@PathVariable("serverName") String serverName,
                                                          @PathVariable("userId") String userId,
                                                          @PathVariable("termGUID") @NotBlank String termGUID,
                                                          @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                          @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getAttributes(userId, serverName, termGUID, from, size);
    }

    /**
     * Extract subtypes
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return subtypes
     */
    @GetMapping( path = "/terms/{termGUID}/subtypes")
    public GlossaryViewEntityDetailResponse getSubtypes(@PathVariable("serverName") String serverName,
                                                        @PathVariable("userId") String userId,
                                                        @PathVariable("termGUID") @NotBlank String termGUID,
                                                        @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                        @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getSubtypes(userId, serverName, termGUID, from, size);
    }

    /**
     * Extract types
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return types
     */
    @GetMapping( path = "/terms/{termGUID}/types")
    public GlossaryViewEntityDetailResponse getTypes(@PathVariable("serverName") String serverName,
                                                     @PathVariable("userId") String userId,
                                                     @PathVariable("termGUID") @NotBlank String termGUID,
                                                     @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                     @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return termService.getTypes(userId, serverName, termGUID, from, size);
    }

}
