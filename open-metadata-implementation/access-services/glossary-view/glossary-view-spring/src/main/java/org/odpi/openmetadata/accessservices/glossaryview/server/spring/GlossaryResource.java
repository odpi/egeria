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
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides the server-side implementation of the Glossary View Open Metadata Access Service
 * (OMAS). This interface facilitates the retrieval of glossaries
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/glossary-view/users/{userId}")
public class GlossaryResource {

    private static final Logger log = LoggerFactory.getLogger(GlossaryResource.class);


    private GlossaryService glossaryService;

    /**
     * Called by Spring
     */
    public GlossaryResource() {
        glossaryService = GlossaryService.getInstance();
    }

    /**
     * Extract all glossary definitions
     *
     * @param serverName instance to call
     * @param userId calling user
     *
     * @return all glossaries
     */
    @RequestMapping(method = RequestMethod.GET, path = "/glossaries")
    public GlossaryViewEntityDetailResponse getAllGlossaries(@PathVariable("serverName") String serverName,
                                                             @PathVariable("userId") String userId) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  glossaryService.getAllGlossaries(userId, serverName);

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
     * Extract the external glossary definitions
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param glossaryGUID glossary guid
     *
     * @return external glossary
     */
    @RequestMapping(method = RequestMethod.GET, path = "/glossaries/{glossaryGUID}/external-glossaries")
    public GlossaryViewEntityDetailResponse getExternalGlossaries(@PathVariable("serverName") String serverName,
                                                                  @PathVariable("userId") String userId,
                                                                  @PathVariable("glossaryGUID") String glossaryGUID) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = glossaryService.getExternalGlossaries(userId, serverName, glossaryGUID);

        watch.stop();
        log.debug("Method: getExternalGlossaries; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

}
