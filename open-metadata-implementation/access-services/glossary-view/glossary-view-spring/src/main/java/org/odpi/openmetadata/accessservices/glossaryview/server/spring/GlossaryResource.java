/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.accessservices.glossaryview.server.service.GlossaryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import static org.odpi.openmetadata.accessservices.glossaryview.server.spring.OmasRegistration.*;

/**
 * Spring Rest Controller defining 'Glossary' oriented endpoints
 */
@RestController
@Validated
@RequestMapping("/servers/{serverName}/open-metadata/access-services/glossary-view/users/{userId}")

@Tag(name="Glossary View OMAS", description="The Glossary View OMAS provides APIs and events for retrieving glossaries, categories and terms.", externalDocs=@ExternalDocumentation(description="Glossary View Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/glossary-view/"))

public class GlossaryResource {

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
        return glossaryService.getAllGlossaries(userId, serverName, from, size);
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
        return glossaryService.getGlossary(userId, serverName, glossaryGUID);
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
        return glossaryService.getTermHomeGlossary(userId, serverName, termGUID);
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
        return glossaryService.getCategoryHomeGlossary(userId, serverName, categoryGUID);
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
        return glossaryService.getExternalGlossaryLinks(userId, serverName, glossaryGUID, from, size);
    }

}
