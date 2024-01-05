/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.accessservices.glossaryview.server.service.CategoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.odpi.openmetadata.accessservices.glossaryview.server.spring.OmasRegistration.PAGE_FROM_DEFAULT_VALUE;
import static org.odpi.openmetadata.accessservices.glossaryview.server.spring.OmasRegistration.PAGE_SIZE_DEFAULT_VALUE;
import static org.odpi.openmetadata.accessservices.glossaryview.server.spring.OmasRegistration.PAGE_SIZE_MAX_VALUE;

/**
 * Spring Rest Controller defining 'GlossaryCategory' oriented endpoints
 */
@RestController
@Validated
@RequestMapping("/servers/{serverName}/open-metadata/access-services/glossary-view/users/{userId}")

@Tag(name="Metadata Access Server: Glossary View OMAS",
        description="The Glossary View OMAS provides APIs and events for retrieving glossaries, categories and terms.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omas/glossary-view/overview/"))
@Deprecated
public class CategoryResource {

    private final CategoryService categoryService;

    /**
     * Called by Spring
     */
    public CategoryResource() {
        categoryService = new CategoryService();
    }

    /**
     * Extract all categories
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param from from
     * @param size size
     *
     * @return categories
     */
    @GetMapping( path = "/categories")
    public GlossaryViewEntityDetailResponse getAllCategories(@PathVariable("serverName") String serverName,
                                                     @PathVariable("userId") String userId,
                                                     @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                     @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return categoryService.getAllCategories(userId, serverName, from, size);
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
    @GetMapping( path = "/categories/{categoryGUID}")
    public GlossaryViewEntityDetailResponse getCategory(@PathVariable("serverName") String serverName,
                                                        @PathVariable("userId") String userId,
                                                        @PathVariable("categoryGUID") @NotBlank String categoryGUID) {
        return categoryService.getCategory(userId, serverName, categoryGUID);
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
    @GetMapping( path = "/glossaries/{glossaryGUID}/categories")
    public GlossaryViewEntityDetailResponse getCategoriesViaCategoryAnchorRelationships(@PathVariable("serverName") String serverName,
                                                                                        @PathVariable("userId") String userId,
                                                                                        @PathVariable("glossaryGUID") @NotBlank String glossaryGUID,
                                                                                        @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                                        @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return categoryService.getCategoriesViaCategoryAnchorRelationships(userId, serverName, glossaryGUID, from, size);
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
    @GetMapping( path = "/categories/{categoryGUID}/subcategories")
    public GlossaryViewEntityDetailResponse getSubcategories(@PathVariable("serverName") String serverName,
                                                             @PathVariable("userId") String userId,
                                                             @PathVariable("categoryGUID") @NotBlank String categoryGUID,
                                                             @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                             @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return categoryService.getSubcategories(userId, serverName, categoryGUID, from, size);
    }

    /**
     * Extract external glossary link definitions for the given category
     *
     * @param serverName instance to call
     * @param userId calling user
     * @param categoryGUID category GUID
     * @param from from
     * @param size size
     *
     * @return external glossary links
     */
    @GetMapping( path = "/categories/{categoryGUID}/external-glossary-links")
    public GlossaryViewEntityDetailResponse getExternalGlossaryLinks(@PathVariable("serverName") String serverName,
                                                                     @PathVariable("userId") String userId,
                                                                     @PathVariable("categoryGUID") @NotBlank String categoryGUID,
                                                                     @RequestParam(name="from", defaultValue=PAGE_FROM_DEFAULT_VALUE) @PositiveOrZero Integer from,
                                                                     @RequestParam(name="size", defaultValue=PAGE_SIZE_DEFAULT_VALUE) @PositiveOrZero @Max(PAGE_SIZE_MAX_VALUE) Integer size) {
        return categoryService.getExternalGlossaryLinks(userId, serverName, categoryGUID, from, size);
    }

}
