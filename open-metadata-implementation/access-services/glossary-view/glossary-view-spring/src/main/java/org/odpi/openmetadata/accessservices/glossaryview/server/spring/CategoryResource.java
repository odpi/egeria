/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.spring;

import org.apache.commons.lang3.time.StopWatch;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.accessservices.glossaryview.server.service.CategoryService;
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
 * Spring Rest Controller defining 'GlossaryCategory' oriented endpoints
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
        categoryService = new CategoryService();
    }

    /**
     * Extract all category definitions for the given glossary GUID
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param glossaryGUID glossary GUID
     * @param from from
     * @param size size
     *
     * @return categories
     */
    @RequestMapping(method = RequestMethod.GET, path = "/categories")
    public GlossaryViewEntityDetailResponse getCategories(@PathVariable("serverName") String serverName,
                                                          @PathVariable("userId") String userId,
                                                          @RequestParam("glossaryGUID") String glossaryGUID,
                                                          @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                          @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(10000) Integer size) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  categoryService.getCategories(userId, serverName, glossaryGUID, from, size);

        watch.stop();
        log.debug("Method: getCategories; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

    /**
     * Extract the category definition for the given GUID
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

    /**
     * Extract category definitions for the given glossary GUID via the 'CategoryAnchor' type relationships
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param glossaryGUID glossary GUID
     * @param from from
     * @param size size
     *
     * @return categories
     */
    @RequestMapping(method = RequestMethod.GET, path = "/glossaries/{glossaryGUID}/categories")
    public GlossaryViewEntityDetailResponse getCategoriesViaCategoryAnchorRelationships(@PathVariable("serverName") String serverName,
                                                                                        @PathVariable("userId") String userId, @PathVariable("glossaryGUID") String glossaryGUID,
                                                                                        @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                                        @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(10000) Integer size) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  categoryService.getCategoriesViaCategoryAnchorRelationships(userId, serverName, glossaryGUID, from, size);

        watch.stop();
        log.debug("Method: getCategoriesViaCategoryAnchorRelationships; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

    /**
     * Extract subcategory definitions for the given GUID
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param categoryGUID category GUID
     * @param from from
     * @param size size
     *
     * @return subcategories
     */
    @RequestMapping(method = RequestMethod.GET, path = "/categories/{categoryGUID}/subcategories")
    public GlossaryViewEntityDetailResponse getSubcategories(@PathVariable("serverName") String serverName,
                                                             @PathVariable("userId") String userId,
                                                             @PathVariable("categoryGUID") String categoryGUID,
                                                             @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                             @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(10000) Integer size) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response =  categoryService.getSubcategories(userId, serverName, categoryGUID, from, size);

        watch.stop();
        log.debug("Method: getSubcategories; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

    /**
     * Extract external glossary definitions for the given category
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param categoryGUID category GUID
     * @param from from
     * @param size size
     *
     * @return external glossaries
     */
    @RequestMapping(method = RequestMethod.GET, path = "/categories/{categoryGUID}/external-glossaries")
    public GlossaryViewEntityDetailResponse getExternalGlossaries(@PathVariable("serverName") String serverName,
                                                                  @PathVariable("userId") String userId,
                                                                  @PathVariable("categoryGUID") String categoryGUID,
                                                                  @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                  @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(10000) Integer size) {
        StopWatch watch = StopWatch.createStarted();

        GlossaryViewEntityDetailResponse response = categoryService.getExternalGlossaries(userId, serverName, categoryGUID, from, size);

        watch.stop();
        log.debug("Method: getExternalGlossaries; Duration: " + watch.getTime()/1000 + "seconds");

        return response;
    }

}
