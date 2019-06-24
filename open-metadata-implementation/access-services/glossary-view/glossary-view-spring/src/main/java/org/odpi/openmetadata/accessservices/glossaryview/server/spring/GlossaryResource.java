/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.spring;

import org.apache.commons.lang3.time.StopWatch;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.accessservices.glossaryview.server.service.GlossaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;

/**
 * Spring Rest Controller defining 'Glossary' oriented endpoints
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/glossary-view/users/{userId}")
public class GlossaryResource {

    public static final String PAGE_FROM_DEFAULT_VALUE = "0";
    public static final String PAGE_SIZE_DEFAULT_VALUE = "100";
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
    @RequestMapping(method = RequestMethod.GET, path = "/glossaries")
    public GlossaryViewEntityDetailResponse getAllGlossaries(@PathVariable("serverName") String serverName,
                                                             @PathVariable("userId") String userId,
                                                             @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                             @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(10000) Integer size) {
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
     * @param glossaryGUID glossary guid
     *
     * @return a glossary
     */
    @RequestMapping(method = RequestMethod.GET, path = "/glossaries/{glossaryGUID}")
    public GlossaryViewEntityDetailResponse getGlossary(@PathVariable("serverName") String serverName,
                                                        @PathVariable("userId") String userId,
                                                        @PathVariable("glossaryGUID") String glossaryGUID) {
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
     * @param termGUID from
     *
     * @return glossaries
     */
    @RequestMapping(method = RequestMethod.GET, path = "/terms/{categoryGUID}/glossary")
    public GlossaryViewEntityDetailResponse getTermHomeGlossary(@PathVariable("serverName") String serverName,
                                                            @PathVariable("userId") String userId,
                                                            @PathVariable("categoryGUID") String termGUID) {
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
     * @param categoryGUID from
     *
     * @return glossaries
     */
    @RequestMapping(method = RequestMethod.GET, path = "/categories/{categoryGUID}/glossary")
    public GlossaryViewEntityDetailResponse getCategoryHomeGlossary(@PathVariable("serverName") String serverName,
                                                                    @PathVariable("userId") String userId,
                                                                    @PathVariable("categoryGUID") String categoryGUID) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  glossaryService.getCategoryHomeGlossary(userId, serverName, categoryGUID);

        watch.stop();
        log.debug("Method: getCategoryHomeGlossary; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

    /**
     * Extract the external glossary definitions
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param glossaryGUID glossary guid
     * @param from from
     * @param size size
     *
     * @return external glossaries
     */
    @RequestMapping(method = RequestMethod.GET, path = "/glossaries/{glossaryGUID}/external-glossaries")
    public GlossaryViewEntityDetailResponse getExternalGlossaries(@PathVariable("serverName") String serverName,
                                                                  @PathVariable("userId") String userId,
                                                                  @PathVariable("glossaryGUID") String glossaryGUID,
                                                                  @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                  @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(10000) Integer size) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = glossaryService.getExternalGlossaries(userId, serverName, glossaryGUID, from, size);

        watch.stop();
        log.debug("Method: getExternalGlossaries; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

}
