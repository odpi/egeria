/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.spring;

import org.apache.commons.lang3.time.StopWatch;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.accessservices.glossaryview.server.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Provides the server-side implementation of the Glossary View Open Metadata Access Service
 * (OMAS). This interface facilitates the retrieval of glossary categories
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/glossary-view/users/{userId}")
public class CategoryResource {

    private static final Logger log = LoggerFactory.getLogger(CategoryResource.class);


    private CategoryService categoryService;

    /**
     * Called by Spring
     */
    public CategoryResource() {
        categoryService = CategoryService.getInstance();
    }

    /**
     * Extract all category definitions for the given glossary GUID
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param glossaryGUID glossary GUID
     *
     * @return all categories
     */
    @RequestMapping(method = RequestMethod.GET, path = "/categories")
    public GlossaryViewEntityDetailResponse getCategories(@PathVariable("serverName") String serverName,
                                                          @PathVariable("userId") String userId,
                                                          @RequestParam("glossaryGUID") String glossaryGUID) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  categoryService.getCategories(userId, serverName, glossaryGUID);

        watch.stop();
        log.debug("Method: getCategories; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

    /**
     * Extract a category definition for the given GUID
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param categoryGUID category GUID
     *
     * @return a category
     */
    @RequestMapping(method = RequestMethod.GET, path = "/categories/{categoryGUID}")
    public GlossaryViewEntityDetailResponse getCategory(@PathVariable("serverName") String serverName,
                                                        @PathVariable("userId") String userId,
                                                        @PathVariable("categoryGUID") String categoryGUID) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  categoryService.getCategory(userId, serverName, categoryGUID);

        watch.stop();
        log.debug("Method: getCategory; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

}
