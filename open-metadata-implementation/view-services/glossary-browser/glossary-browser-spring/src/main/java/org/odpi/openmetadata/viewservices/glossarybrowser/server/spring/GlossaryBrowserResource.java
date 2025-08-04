/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.glossarybrowser.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryTermActivityTypeListResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryTermRelationshipStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryTermStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.server.GlossaryBrowserRESTServices;
import org.springframework.web.bind.annotation.*;



/**
 * The GlossaryBrowserResource provides the Spring API endpoints of the Glossary Browser Open Metadata View Service (OMVS).
 * This interface provides a service for Egeria UIs.
 */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/glossary-browser")

@Tag(name="API: Glossary Browser OMVS",
        description="Explore the contents of a glossary, such as its top-level glossary element, glossary categories and glossary terms, along with the elements that are linked to the terms, such assets.  Each operation includes optional forLineage and forDuplicateProcessing request parameters and an optional request body that includes an effective time field.  These affect the elements that are returned on the query.",
        externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/services/omvs/glossary-browser/overview/"))

public class GlossaryBrowserResource
{

    private final GlossaryBrowserRESTServices restAPI = new GlossaryBrowserRESTServices();


    /**
     * Default constructor
     */
    public GlossaryBrowserResource()
    {
    }


    /**
     * Return the list of glossary term status enum values.
     *
     * @param serverName name of the server to route the request to
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/status-list")

    @Operation(summary="getGlossaryTermStatuses",
            description="Return the list of glossary term status enum values.  These values are used in a glossary workflow to describe the state of the content of the term.",
            externalDocs=@ExternalDocumentation(description="Controlled glossary terms",
                    url="https://egeria-project.org/services/omvs/glossary-manager/overview/#controlled-glossary-terms"))


    public GlossaryTermStatusListResponse getGlossaryTermStatuses(@PathVariable String serverName)
    {
        return restAPI.getGlossaryTermStatuses(serverName);
    }


    /**
     * Return the list of glossary term relationship status enum values.
     *
     * @param serverName name of the server to route the request to
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/relationships/status-list")

    @Operation(summary="getGlossaryTermRelationshipStatuses",
            description="Return the list of glossary term relationship status enum values.  These values are stored in a term-to-term, or term-to-category, relationship and are used to indicate how much the relationship should be trusted.",
            externalDocs=@ExternalDocumentation(description="Relationship statuses",
                    url="https://egeria-project.org/services/omvs/glossary-manager/overview/#relationship-statuses"))

    public GlossaryTermRelationshipStatusListResponse getGlossaryTermRelationshipStatuses(@PathVariable String serverName)
    {
        return restAPI.getGlossaryTermRelationshipStatuses(serverName);
    }


    /**
     * Return the list of glossary term activity type enum values.
     *
     * @param serverName name of the server to route the request to
     * @return list of enum values
     */
    @GetMapping(path = "/glossaries/terms/activity-types")

    @Operation(summary="getGlossaryTermActivityTypes",
            description="Return the list of glossary term activity type enum values.  These values are used in the ActivityDescription classification that is attached to a glossary term that represents some type of activity.",
            externalDocs=@ExternalDocumentation(description="Activity description",
                    url="https://egeria-project.org/types/3/0340-Dictionary/#activitydescription"))

    public GlossaryTermActivityTypeListResponse getGlossaryTermActivityTypes(@PathVariable String serverName)
    {
        return restAPI.getGlossaryTermActivityTypes(serverName);
    }


    /**
     * Retrieve the list of glossary metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/by-search-string")

    @Operation(summary="findGlossaries",
            description="Retrieve the list of glossary metadata elements that contain the search string.  The search string is located in the request body and is interpreted as a plain string.  The request parameters, startsWith, endsWith and ignoreCase can be used to allow a fuzzy search.",
            externalDocs=@ExternalDocumentation(description="Glossary metadata element",
                    url="https://egeria-project.org/types/3/0310-Glossary/"))

    public OpenMetadataRootElementsResponse findGlossaries(@PathVariable String                          serverName,
                                                           @RequestBody  SearchStringRequestBody        requestBody)
    {
        return restAPI.findGlossaries(serverName, requestBody);
    }


    /**
     * Retrieve the list of glossary metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/by-name")

    @Operation(summary="getGlossariesByName",
            description="Retrieve the list of glossary metadata elements with an exactly matching qualified or display name. There are no wildcards supported on this request.",
            externalDocs=@ExternalDocumentation(description="Glossary metadata element",
                    url="https://egeria-project.org/types/3/0310-Glossary/"))

    public OpenMetadataRootElementsResponse   getGlossariesByName(@PathVariable String                  serverName,
                                                          @RequestBody(required = false)  FilterRequestBody         requestBody)
    {
        return restAPI.getGlossariesByName(serverName, requestBody);
    }


    /**
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the requested metadata element
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/{glossaryGUID}/retrieve")

    @Operation(summary="getGlossaryByGUID",
            description="Retrieve the glossary metadata element with the supplied unique identifier.  The optional request body allows you to specify that the glossary element should only be returned if it was effective at a particular time.",
            externalDocs=@ExternalDocumentation(description="Glossary metadata element",
                    url="https://egeria-project.org/types/3/0310-Glossary/"))

    public OpenMetadataRootElementResponse getGlossaryByGUID(@PathVariable String                        serverName,
                                                     @PathVariable String                        glossaryGUID,
                                                     @RequestBody(required = false) GetRequestBody requestBody)
    {
        return restAPI.getGlossaryByGUID(serverName, glossaryGUID, requestBody);
    }

    /**
     * Retrieve the glossary metadata element for the requested term.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the requested metadata element
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/for-term/{glossaryTermGUID}/retrieve")

    @Operation(summary="getGlossaryForTerm",
            description="Retrieve the glossary metadata element for the requested term.  The optional request body allows you to specify that the glossary element should only be returned if it was effective at a particular time.",
            externalDocs=@ExternalDocumentation(description="Glossary metadata element",
                    url="https://egeria-project.org/types/3/0310-Glossary/"))

    public OpenMetadataRootElementResponse getGlossaryForTerm(@PathVariable String                        serverName,
                                                      @PathVariable String                        glossaryTermGUID,
                                                      @RequestBody(required = false)
                                                                  GetRequestBody requestBody)
    {
        return restAPI.getGlossaryForTerm(serverName, glossaryTermGUID, requestBody);
    }


    /**
     * Retrieve the list of glossary term metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody asset manager identifiers and search string
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/by-search-string")

    @Operation(summary="findGlossaryTerms",
            description="Retrieve the list of glossary term metadata elements that contain the search string.  The search string is located in the request body and is interpreted as a plain string.  The request parameters, startsWith, endsWith and ignoreCase can be used to allow a fuzzy search.  The request body also supports the specification of a glossaryGUID to restrict the search to within a single glossary.",
            externalDocs=@ExternalDocumentation(description="Glossary term metadata element",
                    url="https://egeria-project.org/types/3/0330-Terms/"))

    public OpenMetadataRootElementsResponse findGlossaryTerms(@PathVariable String                  serverName,
                                                              @RequestBody(required = false)  SearchStringRequestBody requestBody)
    {
        return restAPI.findGlossaryTerms(serverName, requestBody);
    }


    /**
     * Retrieve the list of glossary term metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody asset manager identifiers and name
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/by-name")

    public OpenMetadataRootElementsResponse   getGlossaryTermsByName(@PathVariable String             serverName,
                                                                     @RequestBody(required = false)  FilterRequestBody requestBody)
    {
        return restAPI.getGlossaryTermsByName(serverName, requestBody);
    }


    /**
     * Retrieve the glossary term metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the requested metadata element
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/glossaries/terms/{glossaryTermGUID}/retrieve")

    public OpenMetadataRootElementResponse getGlossaryTermByGUID(@PathVariable String                             serverName,
                                                             @PathVariable String                             glossaryTermGUID,
                                                             @RequestBody(required = false) GetRequestBody requestBody)
    {
        return restAPI.getGlossaryTermByGUID(serverName, glossaryTermGUID, requestBody);
    }
}

