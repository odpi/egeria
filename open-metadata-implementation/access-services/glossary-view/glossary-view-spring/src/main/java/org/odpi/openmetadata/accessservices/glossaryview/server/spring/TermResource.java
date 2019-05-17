/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.spring;

import org.apache.commons.lang3.time.StopWatch;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.accessservices.glossaryview.server.service.TermService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Provides the server-side implementation of the Glossary View Open Metadata Access Service
 * (OMAS). This interface facilitates the retrieval of glossary categories
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
        termService = TermService.getInstance();
    }

    /**
     * Extract all term definitions for the given category GUID
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param categoryGUID category GUID
     *
     * @return all categories
     */
    @RequestMapping(method = RequestMethod.GET, path = "/terms")
    public GlossaryViewEntityDetailResponse getTerms(@PathVariable("serverName") String serverName,
                                                     @PathVariable("userId") String userId,
                                                     @RequestParam("categoryGUID") String categoryGUID) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  termService.getTerms(userId, serverName, categoryGUID);

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
     * @return a category
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
     * Extract external glossary definitions for the given term
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param termGUID term GUID
     *
     * @return subcategories
     */
    @RequestMapping(method = RequestMethod.GET, path = "/terms/{termGUID}/external-glossaries")
    public GlossaryViewEntityDetailResponse getExternalGlossaries(@PathVariable("serverName") String serverName,
                                                                  @PathVariable("userId") String userId,
                                                                  @PathVariable("termGUID") String termGUID) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = termService.getExternalGlossaries(userId, serverName, termGUID);

        watch.stop();
        log.debug("Method: getExternalGlossaries; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

}
