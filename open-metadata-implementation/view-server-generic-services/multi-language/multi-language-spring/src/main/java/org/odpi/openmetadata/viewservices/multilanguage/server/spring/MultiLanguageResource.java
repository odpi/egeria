/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.multilanguage.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.TranslationDetailResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.TranslationListResponse;
import org.odpi.openmetadata.viewservices.multilanguage.server.MultiLanguageRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The MultiLanguageResource provides part of the server-side implementation of the Multi Language OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="API: Multi Language", description="Supports the maintenance of translations of text attributes in open metadata.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/multi-language/overview/"))

public class MultiLanguageResource
{
    private final MultiLanguageRESTServices restAPI = new MultiLanguageRESTServices();

    /**
     * Default constructor
     */
    public MultiLanguageResource()
    {
    }


    /**
     * Create or update the translation for a particular language/locale for a metadata element.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param requestBody properties of the translation
     *
     * @return void or
     * InvalidParameterException  the unique identifier is null or not known
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @PostMapping(path = "/elements/{elementGUID}/translations")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setTranslation",
            description="Create or update the translation for a particular language/locale for a metadata element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/multi-language/overview/"))

    public VoidResponse setTranslation(@PathVariable String                serverName,
                                       @PathVariable String                urlMarker,
                                       @PathVariable String                elementGUID,
                                       @RequestBody  NewElementRequestBody requestBody)
    {
        return restAPI.setTranslation(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the translation for a particular language/locale for a metadata element.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language
     *
     * @return void or
     * InvalidParameterException  the language is null or not known or not unique (add locale)
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @PostMapping(path = "/elements/{elementGUID}/translations/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearTranslation",
            description="Remove the translation for a particular language/locale for a metadata element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/multi-language/overview/"))

    public VoidResponse clearTranslation(@PathVariable String serverName,
                                         @PathVariable String urlMarker,
                                         @PathVariable String elementGUID,
                                         @RequestParam(required = false)
                                                       String language,
                                         @RequestParam(required = false)
                                                       String locale)
    {
        return restAPI.clearTranslation(serverName, urlMarker, elementGUID, language, locale);
    }


    /**
     * Retrieve the translation for the matching language/locale.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language
     *
     * @return the properties of the translation or null if there is none or
     * InvalidParameterException  the unique identifier is null or not known
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @GetMapping(path = "/elements/{elementGUID}/translations/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getTranslation",
            description="Retrieve the translation for the matching language/locale.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/multi-language/overview/"))

    public TranslationDetailResponse getTranslation(@PathVariable String serverName,
                                                    @PathVariable String urlMarker,
                                                    @PathVariable String elementGUID,
                                                    @RequestParam(required = false)
                                                                  String language,
                                                    @RequestParam(required = false)
                                                                  String locale)
    {
        return restAPI.getTranslation(serverName, urlMarker, elementGUID, language, locale);
    }


    /**
     * Retrieve all translations associated with a metadata element.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return list of translation properties or null if there are none or
     * InvalidParameterException  the unique identifier is null or not known
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @GetMapping(path = "/elements/{elementGUID}/translations")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getTranslations",
            description="Retrieve all translations associated with a metadata element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/multi-language/overview/"))

    public TranslationListResponse getTranslations(@PathVariable String serverName,
                                                   @PathVariable String urlMarker,
                                                   @PathVariable String elementGUID,
                                                   @RequestParam int    startFrom,
                                                   @RequestParam int    pageSize)
    {
        return restAPI.getTranslations(serverName, urlMarker, elementGUID, startFrom, pageSize);
    }
}
