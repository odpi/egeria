/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.templatemanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.templatemanager.server.TemplateManagerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The TemplateManagerResource provides part of the server-side implementation of the Template Manager OMVS.
 */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/template-manager")

@Tag(name="API: Template Manager", description="Template Manager provides APIs for retrieving, creating and maintaining templates.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/template-manager/overview/"))

public class TemplateManagerResource
{
    private final TemplateManagerRESTServices restAPI = new TemplateManagerRESTServices();

    /**
     * Default constructor
     */
    public TemplateManagerResource()
    {
    }


    /**
     * Classify an element as suitable to be used as a template for cataloguing elements of a similar types.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addTemplateClassification",
            description="Classify an element as suitable to be used as a template for cataloguing elements of a similar types.",
            externalDocs=@ExternalDocumentation(description="Templated Cataloguing",
                    url="https://egeria-project.org/features/templated-cataloguing/overview/"))

    public VoidResponse addTemplateClassification(@PathVariable String                    serverName,
                                                  @PathVariable String                    elementGUID,
                                                  @RequestBody(required = false) NewClassificationRequestBody requestBody)
    {
        return restAPI.addTemplateClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the Template classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/template/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeTemplateClassification",
            description="Remove the Template classification from the element.",
            externalDocs=@ExternalDocumentation(description="Templated Cataloguing",
                    url="https://egeria-project.org/features/templated-cataloguing/overview/"))

    public VoidResponse removeTemplateClassification(@PathVariable String                    serverName,
                                                     @PathVariable String                    elementGUID,
                                                     @RequestBody  (required = false)
                                                     DeleteClassificationRequestBody requestBody)
    {
        return restAPI.removeTemplateClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Classify an element as suitable to be used as a template substitute for cataloguing elements of a similar types.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/template-substitute")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addTemplateSubstituteClassification",
            description="Classify an element as suitable to be used as a template substitute for cataloguing elements of a similar types.",
            externalDocs=@ExternalDocumentation(description="Templated Cataloguing",
                    url="https://egeria-project.org/features/templated-cataloguing/overview/"))

    public VoidResponse addTemplateSubstituteClassification(@PathVariable String                    serverName,
                                                            @PathVariable String                    elementGUID,
                                                            @RequestBody(required = false) NewClassificationRequestBody requestBody)
    {
        return restAPI.addTemplateSubstituteClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the TemplateSubstitute classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/template-substitute/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeTemplateSubstituteClassification",
            description="Remove the TemplateSubstitute classification from the element.",
            externalDocs=@ExternalDocumentation(description="Templated Cataloguing",
                    url="https://egeria-project.org/features/templated-cataloguing/overview/"))

    public VoidResponse removeTemplateSubstituteClassification(@PathVariable String                    serverName,
                                                               @PathVariable String                    elementGUID,
                                                               @RequestBody  (required = false)
                                                               DeleteClassificationRequestBody requestBody)
    {
        return restAPI.removeTemplateSubstituteClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Attach a template to an element that was created from it.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID     unique identifier of the element
     * @param templateGUID      unique identifier of the template
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/sourced-from/{templateGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSourcedFrom",
            description="Attach a template to an element that was created from it.",
            externalDocs=@ExternalDocumentation(description="Templated Cataloguing",
                    url="https://egeria-project.org/features/templated-cataloguing/overview/"))

    public VoidResponse linkSourcedFrom(@PathVariable String                  serverName,
                                        @PathVariable String                  elementGUID,
                                        @PathVariable String                  templateGUID,
                                        @RequestBody  (required = false)
                                        NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSourcedFrom(serverName, elementGUID, templateGUID, requestBody);
    }


    /**
     * Detach the source template for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID     unique identifier of the element
     * @param templateGUID      unique identifier of the template
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/sourced-from/{templateGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSourcedFrom",
            description="Detach the source template for an element.",
            externalDocs=@ExternalDocumentation(description="Templated Cataloguing",
                    url="https://egeria-project.org/features/templated-cataloguing/overview/"))

    public VoidResponse detachSourcedFrom(@PathVariable String                        serverName,
                                          @PathVariable String                        elementGUID,
                                          @PathVariable String                        templateGUID,
                                          @RequestBody  (required = false)
                                          DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSourcedFrom(serverName, elementGUID, templateGUID, requestBody);
    }



    /**
     * Attach a template to an element that this template is relevant to.   For example, a project.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID     unique identifier of the element
     * @param templateGUID      unique identifier of the template
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/catalog-template/{templateGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkCatalogTemplate",
            description="Attach a template to an element that this template is relevant to.   For example, a project.",
            externalDocs=@ExternalDocumentation(description="Templated Cataloguing",
                    url="https://egeria-project.org/features/templated-cataloguing/overview/"))

    public VoidResponse linkCatalogTemplate(@PathVariable String                  serverName,
                                            @PathVariable String                  elementGUID,
                                            @PathVariable String                  templateGUID,
                                            @RequestBody  (required = false)
                                            NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkCatalogTemplate(serverName, elementGUID, templateGUID, requestBody);
    }


    /**
     * Detach a template for an element that this template is relevant to.   For example, a project.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID     unique identifier of the element
     * @param templateGUID      unique identifier of the template
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/catalog-template/{templateGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachCatalogTemplate",
            description="Detach a template for an element that this template is relevant to.   For example, a project.",
            externalDocs=@ExternalDocumentation(description="Templated Cataloguing",
                    url="https://egeria-project.org/features/templated-cataloguing/overview/"))

    public VoidResponse detachCatalogTemplate(@PathVariable String                        serverName,
                                              @PathVariable String                        elementGUID,
                                              @PathVariable String                        templateGUID,
                                              @RequestBody  (required = false)
                                              DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachCatalogTemplate(serverName, elementGUID, templateGUID, requestBody);
    }
}
