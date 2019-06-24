/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.spring;

import org.apache.commons.lang3.time.StopWatch;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.accessservices.glossaryview.server.service.TermService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;

import static org.odpi.openmetadata.accessservices.glossaryview.server.spring.GlossaryResource.PAGE_FROM_DEFAULT_VALUE;
import static org.odpi.openmetadata.accessservices.glossaryview.server.spring.GlossaryResource.PAGE_SIZE_DEFAULT_VALUE;

/**
 * Spring Rest Controller defining 'GlossaryTerm' oriented endpoints
 */
@RestController
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
     * Extract all term definitions for the given category GUID
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param categoryGUID category GUID
     * @param from from
     * @param size size
     *
     * @return terms
     */
    @RequestMapping(method = RequestMethod.GET, path = "/terms")
    public GlossaryViewEntityDetailResponse getTerms(@PathVariable("serverName") String serverName,
                                                     @PathVariable("userId") String userId,
                                                     @RequestParam("categoryGUID") String categoryGUID,
                                                     @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                     @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(10000) Integer size) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  termService.getTerms(userId, serverName, categoryGUID, from, size);

        watch.stop();
        log.debug("Method: getTerms; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
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
    @RequestMapping(method = RequestMethod.GET, path = "/terms/{termGUID}")
    public GlossaryViewEntityDetailResponse getTerm(@PathVariable("serverName") String serverName,
                                                    @PathVariable("userId") String userId,
                                                    @PathVariable("termGUID") String termGUID) {
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
    @RequestMapping(method = RequestMethod.GET, path = "/glossaries/{glossaryGUID}/terms")
    public GlossaryViewEntityDetailResponse getTermsViaTermAnchorRelationships(@PathVariable("serverName") String serverName,
                                                                               @PathVariable("userId") String userId, @PathVariable("glossaryGUID") String glossaryGUID,
                                                                               @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                               @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(10000) Integer size) {
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
    @RequestMapping(method = RequestMethod.GET, path = "/categories/{categoryGUID}/terms")
    public GlossaryViewEntityDetailResponse getTermsViaTermCategorizationRelationships(@PathVariable("serverName") String serverName,
                                                                                       @PathVariable("userId") String userId,
                                                                                       @PathVariable("categoryGUID") String categoryGUID,
                                                                                       @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                                       @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(10000) Integer size) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getTermsViaTermCategorizationRelationships(userId, serverName, categoryGUID, from, size);

        watch.stop();
        log.debug("Method: getTermsViaTermCategorizationRelationships; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

    /**
     * Extract external glossary definitions for the given term
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     * @param from from
     * @param size size
     *
     * @return external glossaries
     */
    @RequestMapping(method = RequestMethod.GET, path = "/terms/{termGUID}/external-glossaries")
    public GlossaryViewEntityDetailResponse getExternalGlossaries(@PathVariable("serverName") String serverName,
                                                                  @PathVariable("userId") String userId,
                                                                  @PathVariable("termGUID") String termGUID,
                                                                  @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                  @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(10000) Integer size) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getExternalGlossaries(userId, serverName, termGUID, from, size);

        watch.stop();
        log.debug("Method: getExternalGlossaries; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

}
