/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.spring;

import org.apache.commons.lang3.time.StopWatch;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.accessservices.glossaryview.server.service.TermService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import static org.odpi.openmetadata.accessservices.glossaryview.server.spring.GlossaryResource.PAGE_FROM_DEFAULT_VALUE;
import static org.odpi.openmetadata.accessservices.glossaryview.server.spring.GlossaryResource.PAGE_SIZE_DEFAULT_VALUE;
import static org.odpi.openmetadata.accessservices.glossaryview.server.spring.GlossaryResource.PAGE_SIZE_MAX_VALUE;

/**
 * Spring Rest Controller defining 'GlossaryTerm' oriented endpoints
 */
@RestController
@Validated
@RequestMapping("/servers/{serverName}/open-metadata/access-services/glossary-view/users/{userId}")
public class TermResource {

    private static final Logger log = LoggerFactory.getLogger(TermResource.class);


    private TermService termService;

    /**
     * Called by Spring
     */
    public TermResource() {
        termService = new TermService();
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  termService.getTerm(userId, serverName, termGUID);

        watch.stop();
        log.debug("Method: getTerm; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  termService.getTermsViaTermAnchorRelationships(userId, serverName, glossaryGUID, from, size);

        watch.stop();
        log.debug("Method: getTermsViaTermAnchorRelationships; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getTermsViaTermCategorizationRelationships(userId, serverName, categoryGUID, from, size);

        watch.stop();
        log.debug("Method: getTermsViaTermCategorizationRelationships; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getExternalGlossaryLinks(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getExternalGlossaryLinks; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getRelatedTerms(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getRelatedTerms; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getSynonyms(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getSynonyms; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getAntonyms(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getAntonyms; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getPreferredTerms(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getPreferredTerms; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getReplacementTerms(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getReplacementTerms; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getTranslations(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getTranslations; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getIsA(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getIsA; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getValidValues(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getValidValues; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getUsedInContexts(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getUsedInContexts; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getAssignedElements(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getAssignedElements; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getAttributes(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getAttributes; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getSubtypes(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getSubtypes; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getTypes(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getTypes; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

}
