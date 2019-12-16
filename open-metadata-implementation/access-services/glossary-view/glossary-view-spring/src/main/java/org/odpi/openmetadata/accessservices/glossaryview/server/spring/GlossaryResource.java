/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.spring;

import org.apache.commons.lang3.time.StopWatch;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.accessservices.glossaryview.server.service.GlossaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

/**
 * Spring Rest Controller defining 'Glossary' oriented endpoints
 */
@RestController
@Validated
@RequestMapping("/servers/{serverName}/open-metadata/access-services/glossary-view/users/{userId}")
public class GlossaryResource {

    public static final String PAGE_FROM_DEFAULT_VALUE = "0";
    public static final String PAGE_SIZE_DEFAULT_VALUE = "1000";
    public static final int PAGE_SIZE_MAX_VALUE = 10000;
    private static final Logger log = LoggerFactory.getLogger(GlossaryResource.class);


    private GlossaryService glossaryService;

    /**
     * Called by Spring
     */
    public GlossaryResource() {
        glossaryService = new GlossaryService();
    }

    /**
     * Extract all glossary definitions
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param from from
     * @param size size
     *
     * @return glossaries
     */
    @GetMapping( path = "/glossaries")
    public GlossaryViewEntityDetailResponse getAllGlossaries(@PathVariable("serverName") String serverName,
                                                             @PathVariable("userId") String userId,
                                                             @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                             @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  glossaryService.getAllGlossaries(userId, serverName, from, size);

        watch.stop();
        log.debug("Method: getAllGlossaries; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

    /**
     * Extract a glossary definition
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param glossaryGUID glossary GUID
     *
     * @return a glossary
     */
    @GetMapping( path = "/glossaries/{glossaryGUID}")
    public GlossaryViewEntityDetailResponse getGlossary(@PathVariable("serverName") String serverName,
                                                        @PathVariable("userId") String userId,
                                                        @PathVariable("glossaryGUID") @NotBlank String glossaryGUID) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = glossaryService.getGlossary(userId, serverName, glossaryGUID);

        watch.stop();
        log.debug("Method: getGlossary; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

    /**
     * Extract a term's home glossary
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     *
     * @return glossaries
     */
    @GetMapping( path = "/terms/{termGUID}/home-glossary")
    public GlossaryViewEntityDetailResponse getTermHomeGlossary(@PathVariable("serverName") String serverName,
                                                                @PathVariable("userId") String userId,
                                                                @PathVariable("termGUID") @NotBlank String termGUID) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  glossaryService.getTermHomeGlossary(userId, serverName, termGUID);

        watch.stop();
        log.debug("Method: getTermHomeGlossary; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

    /**
     * Extract a category's home glossary
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param categoryGUID category GUID
     *
     * @return glossaries
     */
    @GetMapping( path = "/categories/{categoryGUID}/home-glossary")
    public GlossaryViewEntityDetailResponse getCategoryHomeGlossary(@PathVariable("serverName") String serverName,
                                                                    @PathVariable("userId") String userId,
                                                                    @PathVariable("categoryGUID") @NotBlank String categoryGUID) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  glossaryService.getCategoryHomeGlossary(userId, serverName, categoryGUID);

        watch.stop();
        log.debug("Method: getCategoryHomeGlossary; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

    /**
     * Extract the external glossary link definitions
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param glossaryGUID glossary GUID
     * @param from from
     * @param size size
     *
     * @return external glossary links
     */
    @GetMapping( path = "/glossaries/{glossaryGUID}/external-glossary-links")
    public GlossaryViewEntityDetailResponse getExternalGlossaryLinks(@PathVariable("serverName") String serverName,
                                                                     @PathVariable("userId") String userId,
                                                                     @PathVariable("glossaryGUID") @NotBlank String glossaryGUID,
                                                                     @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                     @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = glossaryService.getExternalGlossaryLinks(userId, serverName, glossaryGUID, from, size);

        watch.stop();
        log.debug("Method: getExternalGlossaryLinks; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

}
