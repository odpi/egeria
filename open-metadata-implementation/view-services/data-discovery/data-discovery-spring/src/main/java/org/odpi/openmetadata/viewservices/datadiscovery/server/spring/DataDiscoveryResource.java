/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.datadiscovery.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.datadiscovery.server.DataDiscoveryRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DataDiscoveryResource provides part of the server-side implementation of the Data Discovery OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/data-discovery")

@Tag(name="API: Data Discovery OMVS", description="Survey reports capture the analysis of IT resources and data. Egeria supports many types of surveys, but this API enables external survey and analysis engines (such as data quality analysers) to load their findings into open metadata.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/data-discovery/overview/"))

public class DataDiscoveryResource
{
    private final DataDiscoveryRESTServices restAPI = new DataDiscoveryRESTServices();

    /**
     * Default constructor
     */
    public DataDiscoveryResource()
    {
    }


    /**
     * Create an annotation.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the annotation.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/annotations")

    @Operation(summary="createAnnotation",
            description="Create an annotation.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public GUIDResponse createAnnotation(@PathVariable String                               serverName,
                                         @RequestBody (required = false)
                                         NewElementRequestBody requestBody)
    {
        return restAPI.createAnnotation(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent an annotation using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/annotations/from-template")
    @Operation(summary="createAnnotationFromTemplate",
            description="Create a new metadata element to represent an annotation using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public GUIDResponse createAnnotationFromTemplate(@PathVariable
                                                     String              serverName,
                                                     @RequestBody (required = false)
                                                     TemplateRequestBody requestBody)
    {
        return restAPI.createAnnotationFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of an annotation.
     *
     * @param serverName         name of called server.
     * @param annotationGUID unique identifier of the annotation (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/annotations/{annotationGUID}/update")
    @Operation(summary="updateAnnotation",
            description="Update the properties of an annotation.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse updateAnnotation(@PathVariable
                                         String                                  serverName,
                                         @PathVariable
                                         String                                  annotationGUID,
                                         @RequestBody (required = false)
                                         UpdateElementRequestBody requestBody)
    {
        return restAPI.updateAnnotation(serverName, annotationGUID, requestBody);
    }


    /**
     * Create a relationship that links a new annotation to its survey report.  This relationship is typically
     * established during the createAnnotation as the parent relationship.  It is included for completeness.
     *
     * @param serverName         name of called server
     * @param surveyReportGUID       unique identifier of the report
     * @param newAnnotationGUID           unique identifier of the  annotation
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/survey-reports/{surveyReportGUID}/new-annotations/{newAnnotationGUID}/attach")
    @Operation(summary="attachAnnotationToReport",
            description="Create a relationship that links a new annotation to its survey report.  This relationship is typically " +
                    "established during the createAnnotation as the parent relationship.  It is included for completeness.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse attachAnnotationToReport(@PathVariable
                                           String                     serverName,
                                           @PathVariable
                                           String surveyReportGUID,
                                           @PathVariable
                                           String newAnnotationGUID,
                                           @RequestBody (required = false)
                                           NewRelationshipRequestBody requestBody)
    {
        return restAPI.attachAnnotationToReport(serverName, surveyReportGUID, newAnnotationGUID, requestBody);
    }


    /**
     * Detach an annotation from its report (ReportedAnnotation relationship).
     *
     * @param serverName         name of called server
     * @param surveyReportGUID       unique identifier of the report
     * @param newAnnotationGUID           unique identifier of the  annotation
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/survey-reports/{surveyReportGUID}/new-annotations/{newAnnotationGUID}/detach")
    @Operation(summary="detachAnnotationFromReport",
            description="Detach an annotation from its report (ReportedAnnotation relationship).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse detachAnnotationFromReport(@PathVariable
                                              String                    serverName,
                                              @PathVariable
                                              String surveyReportGUID,
                                              @PathVariable
                                                  String newAnnotationGUID,
                                              @RequestBody (required = false)
                                              DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachAnnotationFromReport(serverName, surveyReportGUID, newAnnotationGUID, requestBody);
    }


    /**
     * Attach an annotation to the element that it is describing (via AssociatedAnnotation relationship).
     *
     * @param serverName         name of called server
     * @param elementGUID          unique identifier of the described element
     * @param annotationGUID          unique identifier of the annotation
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/associated-annotations/{annotationGUID}/attach")
    @Operation(summary="linkAnnotationToDescribedElement",
            description="Attach an annotation to the element that it is describing (via AssociatedAnnotation relationship).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse linkAnnotationToDescribedElement(@PathVariable String                     serverName,
                                                         @PathVariable String                     elementGUID,
                                                         @PathVariable String                     annotationGUID,
                                                         @RequestBody (required = false)
                                                             NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkAnnotationToDescribedElement(serverName, elementGUID, annotationGUID, requestBody);
    }


    /**
     * Detach an annotation from the element that it is describing (via AssociatedAnnotation relationship).
     *
     * @param serverName         name of called server
     * @param elementGUID          unique identifier of the described element
     * @param annotationGUID          unique identifier of the annotation
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/associated-annotations/{annotationGUID}/detach")
    @Operation(summary="detachAnnotationFromDescribedElement",
            description="Detach an annotation from the element that it is describing (via AssociatedAnnotation relationship).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse detachAnnotationFromDescribedElement(@PathVariable String                        serverName,
                                                             @PathVariable String                        elementGUID,
                                                             @PathVariable String                        annotationGUID,
                                                             @RequestBody (required = false)
                                                                 DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachAnnotationFromDescribedElement(serverName, elementGUID, annotationGUID, requestBody);
    }


    /**
     * Attach an annotation to the equivalent annotation from the previous run of the survey.
     *
     * @param serverName         name of called server
     * @param previousAnnotationGUID          unique identifier of the annotation from the previous run of the survey
     * @param newAnnotationGUID            unique identifier of the annotation from this run of the survey
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/annotations/{previousAnnotationGUID}/later-annotations/{newAnnotationGUID}/attach")
    @Operation(summary="linkAnnotationToItsPredecessor",
            description="Attach an annotation to the equivalent annotation from the previous run of the survey.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse linkAnnotationToItsPredecessor(@PathVariable String                     serverName,
                                                       @PathVariable String                     previousAnnotationGUID,
                                                       @PathVariable String                     newAnnotationGUID,
                                                       @RequestBody (required = false)
                                                           NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkAnnotationToItsPredecessor(serverName, previousAnnotationGUID, newAnnotationGUID, requestBody);
    }


    /**
     * Detach an annotation from an annotation from the previous run of the survey.
     *
     * @param serverName         name of called server
     * @param previousAnnotationGUID          unique identifier of the annotation from the previous run of the survey
     * @param newAnnotationGUID            unique identifier of the annotation from this run of the survey
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/annotations/{previousAnnotationGUID}/later-annotations/{newAnnotationGUID}/detach")
    @Operation(summary="detachAnnotationFromItsPredecessor",
            description="Detach an annotation from an annotation from the previous run of the survey.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse detachAnnotationFromItsPredecessor(@PathVariable String                        serverName,
                                                           @PathVariable String                        previousAnnotationGUID,
                                                           @PathVariable String                        newAnnotationGUID,
                                                           @RequestBody (required = false)
                                                           DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachAnnotationFromItsPredecessor(serverName, previousAnnotationGUID, newAnnotationGUID, requestBody);
    }


    /**
     * Attach a schema analysis annotation to a matching schema type.
     *
     * @param serverName         name of called server
     * @param annotationGUID       unique identifier of the annotation
     * @param schemaTypeGUID            unique identifier of the schema type
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/annotations/{annotationGUID}/discovered-schema-type/{schemaTypeGUID}/attach")
    @Operation(summary="linkDiscoveredSchemaType",
            description="Attach a schema analysis annotation to a matching schema type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse linkDiscoveredSchemaType(@PathVariable String                     serverName,
                                                 @PathVariable String                     annotationGUID,
                                                 @PathVariable String                     schemaTypeGUID,
                                                 @RequestBody (required = false)
                                                 NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDiscoveredSchemaType(serverName, annotationGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Detach a schema analysis annotation from a matching schema type.
     *
     * @param serverName         name of called server
     * @param annotationGUID       unique identifier of the annotation
     * @param schemaTypeGUID            unique identifier of the schema type
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/annotations/{annotationGUID}/discovered-schema-type/{schemaTypeGUID}/detach")
    @Operation(summary="detachDiscoveredSchemaType",
            description="Detach a schema analysis annotation from a matching schema type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse detachDiscoveredSchemaType(@PathVariable String                        serverName,
                                                   @PathVariable String                        annotationGUID,
                                                   @PathVariable String                        schemaTypeGUID,
                                                   @RequestBody (required = false)
                                                   DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDiscoveredSchemaType(serverName, annotationGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Attach a resource profile log annotation to an asset where the profile data is stored.
     *
     * @param serverName         name of called server
     * @param annotationGUID               unique identifier of the annotation
     * @param assetGUID         unique identifier of the associated asset
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/annotations/{annotationGUID}/resource-profile-data-assets/{assetGUID}/attach")
    @Operation(summary="linkResourceProfileData",
            description="Attach a resource profile log annotation to an asset where the profile data is stored.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse linkResourceProfileData(@PathVariable String                     serverName,
                                                @PathVariable String                     annotationGUID,
                                                @PathVariable String                      assetGUID,
                                                @RequestBody (required = false)
                                                NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkResourceProfileData(serverName, annotationGUID, assetGUID, requestBody);
    }


    /**
     * Detach a resource profile log annotation from an asset where the profile data is stored.
     *
     * @param serverName         name of called server
     * @param annotationGUID               unique identifier of the annotation
     * @param assetGUID         unique identifier of the associated asset
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/annotations/{annotationGUID}/resource-profile-data-assets/{assetGUID}/detach")
    @Operation(summary="detachResourceProfileData",
            description="Detach a resource profile log annotation from an asset where the profile data is stored.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse detachResourceProfileData(@PathVariable String                        serverName,
                                                  @PathVariable String                        annotationGUID,
                                                  @PathVariable String                        assetGUID,
                                                  @RequestBody (required = false)
                                                  DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachResourceProfileData(serverName, annotationGUID, assetGUID, requestBody);
    }


    /**
     * Attach a data class annotation to a data class.
     *
     * @param serverName         name of called server
     * @param annotationGUID               unique identifier of the annotation
     * @param dataClassGUID         unique identifier of the associated data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/annotations/{annotationGUID}/matched-data-classes/{dataClassGUID}/attach")
    @Operation(summary="linkDataClassMatch",
            description="Attach a data class annotation to a data class.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse linkDataClassMatch(@PathVariable String                     serverName,
                                           @PathVariable String                     annotationGUID,
                                           @PathVariable String                     dataClassGUID,
                                           @RequestBody (required = false)
                                           NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDataClassMatch(serverName, annotationGUID, dataClassGUID, requestBody);
    }


    /**
     * Detach a data class annotation from a data class.
     *
     * @param serverName         name of called server
     * @param annotationGUID     unique identifier of the annotation
     * @param dataClassGUID     unique identifier of the associated data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/annotations/{annotationGUID}/matched-data-classes/{dataClassGUID}/detach")
    @Operation(summary="detachDataClassMatch",
            description="Detach a data class annotation from a data class.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse detachDataClassMatch(@PathVariable String                        serverName,
                                             @PathVariable String                        annotationGUID,
                                             @PathVariable String                        dataClassGUID,
                                             @RequestBody (required = false)
                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDataClassMatch(serverName, annotationGUID, dataClassGUID, requestBody);
    }


    /**
     * Attach a request for action annotation to the element that needs attention.
     *
     * @param serverName         name of called server
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/annotations/{annotationGUID}/request-for-action-targets/{elementGUID}/attach")
    @Operation(summary="linkRequestForActionTarget",
            description="Attach a request for action annotation to the element that needs attention.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse linkRequestForActionTarget(@PathVariable String                     serverName,
                                                   @PathVariable String                     annotationGUID,
                                                   @PathVariable String                     elementGUID,
                                                   @RequestBody (required = false)
                                                   NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkRequestForActionTarget(serverName, annotationGUID, elementGUID, requestBody);
    }


    /**
     * Detach a request for action annotation from its intended target element.
     *
     * @param serverName         name of called server
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/annotations/{annotationGUID}/request-for-action-targets/{elementGUID}/detach")
    @Operation(summary="detachRequestForActionTarget",
            description="Detach a request for action annotation from its intended target element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse detachRequestForActionTarget(@PathVariable String                        serverName,
                                                     @PathVariable String                        annotationGUID,
                                                     @PathVariable String                        elementGUID,
                                                     @RequestBody (required = false)
                                                     DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachRequestForActionTarget(serverName, annotationGUID, elementGUID, requestBody);
    }


    /**
     * Delete an annotation.
     *
     * @param serverName         name of called server
     * @param annotationGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/annotations/{annotationGUID}/delete")
    @Operation(summary="deleteAnnotation",
            description="Delete an annotation.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public VoidResponse deleteAnnotation(@PathVariable
                                         String                    serverName,
                                         @PathVariable
                                         String                    annotationGUID,
                                         @RequestBody (required = false)
                                         DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteAnnotation(serverName, annotationGUID, requestBody);
    }


    /**
     * Returns the list of annotations with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/annotations/by-name")
    @Operation(summary="getAnnotationsByName",
            description="Returns the list of annotations with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public OpenMetadataRootElementsResponse getAnnotationsByName(@PathVariable
                                                                 String            serverName,
                                                                 @RequestBody (required = false)
                                                                 FilterRequestBody requestBody)
    {
        return restAPI.getAnnotationsByName(serverName, requestBody);
    }


    /**
     * Returns the list of annotations associated with a particular analysis step.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/annotations/by-analysis-step")
    @Operation(summary="getAnnotationsByAnalysisStep",
            description="Returns the list of annotations associated with a particular analysis step.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public OpenMetadataRootElementsResponse getAnnotationsByAnalysisStep(@PathVariable
                                                                         String            serverName,
                                                                         @RequestBody (required = false)
                                                                         FilterRequestBody requestBody)
    {
        return restAPI.getAnnotationsByAnalysisStep(serverName, requestBody);
    }


    /**
     * Returns the list of annotations with a particular annotation type property.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/annotations/by-annotation-type")
    @Operation(summary="getAnnotationsByAnnotationType",
            description="Returns the list of annotations with a particular annotation type property.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public OpenMetadataRootElementsResponse getAnnotationsByAnnotationType(@PathVariable
                                                                           String            serverName,
                                                                           @RequestBody (required = false)
                                                                           FilterRequestBody requestBody)
    {
        return restAPI.getAnnotationsByAnnotationType(serverName, requestBody);
    }


    /**
     * Returns the list of annotations that describe the supplied element (AssociatedAnnotation relationship).
     *
     * @param serverName name of the service to route the request to
     * @param elementGUID              unique identifier of the starting element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/associated-annotations")
    @Operation(summary="getAnnotationsForElement",
            description="Returns the list of annotations that describe the supplied element (AssociatedAnnotation relationship).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public OpenMetadataRootElementsResponse getAnnotationsForElement(@PathVariable String             serverName,
                                                                     @PathVariable String             elementGUID,
                                                                     @RequestBody (required = false)
                                                                     ResultsRequestBody requestBody)
    {
        return restAPI.getAnnotationsForElement(serverName, elementGUID, requestBody);
    }


    /**
     * Returns the annotations created under the supplied survey report.
     *
     * @param serverName name of the service to route the request to
     * @param surveyReportGUID              unique identifier of the starting element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/survey-reports/{surveyReportGUID}/new-annotations")
    @Operation(summary="getNewAnnotations",
            description="Returns the annotations created under the supplied survey report.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public OpenMetadataRootElementsResponse getNewAnnotations(@PathVariable
                                                              String             serverName,
                                                              @PathVariable
                                                              String             surveyReportGUID,
                                                              @RequestBody (required = false)
                                                              ResultsRequestBody requestBody)
    {
        return restAPI.getNewAnnotations(serverName, surveyReportGUID, requestBody);
    }


    /**
     * Returns the list of annotations that extend the supplied annotation (AnnotationExtension relationship).
     *
     * @param serverName name of the service to route the request to
     * @param annotationGUID              unique identifier of the starting element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/annotations/{annotationGUID}/annotation-extensions")
    @Operation(summary="getAnnotationExtensions",
            description="Returns the list of annotations that extend the supplied annotation (AnnotationExtension relationship).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public OpenMetadataRootElementsResponse getAnnotationExtensions(@PathVariable
                                                                    String             serverName,
                                                                    @PathVariable
                                                                    String             annotationGUID,
                                                                    @RequestBody (required = false)
                                                                    ResultsRequestBody requestBody)
    {
        return restAPI.getAnnotationExtensions(serverName, annotationGUID, requestBody);
    }


    /**
     * Returns the list of annotations that are extended by the supplied annotation (AnnotationExtension relationship).
     *
     * @param serverName name of the service to route the request to
     * @param annotationGUID              unique identifier of the starting element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/annotations/{annotationGUID}/previous-annotations")
    @Operation(summary="getPreviousAnnotations",
            description="Returns the list of annotations that are extended by the supplied annotation (AnnotationExtension relationship).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public OpenMetadataRootElementsResponse getPreviousAnnotations(@PathVariable
                                                                   String             serverName,
                                                                   @PathVariable
                                                                   String             annotationGUID,
                                                                   @RequestBody (required = false)
                                                                   ResultsRequestBody requestBody)
    {
        return restAPI.getPreviousAnnotations(serverName, annotationGUID, requestBody);
    }


    /**
     * Retrieve the list of annotation metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/annotations/by-search-string")
    @Operation(summary="findAnnotations",
            description="Retrieve the list of annotation metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public OpenMetadataRootElementsResponse findAnnotations(@PathVariable
                                                            String                  serverName,
                                                            @RequestBody (required = false)
                                                            SearchStringRequestBody requestBody)
    {
        return restAPI.findAnnotations(serverName, requestBody);
    }


    /**
     * Return the properties of a specific annotation.
     *
     * @param serverName name of the service to route the request to
     * @param annotationGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/annotations/{annotationGUID}/retrieve")
    @Operation(summary="getAnnotationByGUID",
            description="Return the properties of a specific annotation.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/annotation"))

    public OpenMetadataRootElementResponse getAnnotationByGUID(@PathVariable
                                                               String             serverName,
                                                               @PathVariable
                                                               String             annotationGUID,
                                                               @RequestBody (required = false)
                                                               GetRequestBody requestBody)
    {
        return restAPI.getAnnotationByGUID(serverName, annotationGUID, requestBody);
    }


}
