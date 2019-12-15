/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.server.spring;


import org.odpi.openmetadata.accessservices.discoveryengine.server.DiscoveryEngineServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.*;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.springframework.web.bind.annotation.*;

/**
 * DiscoveryEngineResource provides the generic server-side interface for the Discovery Engine Open Metadata Access Service (OMAS).
 * There are other resources that provide specialized methods for specific types of Asset.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/discovery-engine/users/{userId}")
public class DiscoveryEngineResource
{
    private DiscoveryEngineServices restAPI = new DiscoveryEngineServices();


    /**
     * Default constructor
     */
    public DiscoveryEngineResource()
    {
    }


    /**
     * Create a new discovery analysis report and chain it to its asset, discovery engine and discovery service.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset being analysed
     * @param requestBody  all of the other parameters
     *
     * @return the new discovery report or
     *
     *  InvalidParameterException one of the parameters is invalid or
     *  UserNotAuthorizedException the user is not authorized to access the asset and/or report or
     *  PropertyServerException there was a problem in the store whether the asset/report properties are kept.
     */
    @PostMapping( path = "/assets/{assetGUID}/discovery-analysis-reports")

    public DiscoveryAnalysisReportResponse createDiscoveryAnalysisReport(@PathVariable String                             serverName,
                                                                         @PathVariable String                             userId,
                                                                         @PathVariable String                             assetGUID,
                                                                         @RequestBody  DiscoveryAnalysisReportRequestBody requestBody)
    {
        return restAPI.createDiscoveryAnalysisReport(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Update the properties of the discovery analysis report.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param discoveryReportGUID unique identifier of the report to update
     * @param requestBody updated report - this will replace what was previous stored
     *
     * @return the new values stored in the repository or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @PostMapping( path = "/discovery-analysis-reports/{discoveryReportGUID}")

    public DiscoveryAnalysisReportResponse updateDiscoveryAnalysisReport(@PathVariable String                  serverName,
                                                                         @PathVariable String                  userId,
                                                                         @PathVariable String                  discoveryReportGUID,
                                                                         @RequestBody  DiscoveryAnalysisReport requestBody)
    {
        return restAPI.updateDiscoveryAnalysisReport(serverName, userId, discoveryReportGUID, requestBody);
    }


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     *
     * @return discovery report or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @GetMapping( path = "/discovery-analysis-reports/{discoveryReportGUID}")

    public DiscoveryAnalysisReportResponse getDiscoveryReport(@PathVariable String   serverName,
                                                              @PathVariable String   userId,
                                                              @PathVariable String   discoveryReportGUID)
    {
        return restAPI.getDiscoveryAnalysisReport(serverName, userId, discoveryReportGUID);
    }


    /**
     * Return the list of annotations from previous runs of the discovery service that are set to a specific status.
     * If status is null then annotations that have been reviewed, approved and/or actioned are returned from
     * discovery reports that are not waiting or in progress.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     * @param requestBody status value to use on the query
     *
     * @return list of annotation (or null if none are registered) or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    @GetMapping( path = "/assets/{assetGUID}/annotations")

    public AnnotationListResponse getAnnotationsForAssetByStatus(@PathVariable String            serverName,
                                                                 @PathVariable String            userId,
                                                                 @PathVariable String            assetGUID,
                                                                 @RequestParam int               startingFrom,
                                                                 @RequestParam int               maximumResults,
                                                                 @RequestBody  StatusRequestBody requestBody)
    {
        return restAPI.getAnnotationsForAssetByStatus(serverName,
                                                      userId,
                                                      assetGUID,
                                                      startingFrom,
                                                      maximumResults,
                                                      requestBody);
    }


    /**
     * Return the annotations linked directly to the report.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @GetMapping( path = "/discovery-analysis-reports/{discoveryReportGUID}/annotations")

    public AnnotationListResponse getDiscoveryReportAnnotations(@PathVariable String   serverName,
                                                                @PathVariable String   userId,
                                                                @PathVariable String   discoveryReportGUID,
                                                                @RequestParam int      startingFrom,
                                                                @RequestParam int      maximumResults)
    {
        return restAPI.getDiscoveryReportAnnotations(serverName,
                                                     userId,
                                                     discoveryReportGUID,
                                                     startingFrom,
                                                     maximumResults);
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @GetMapping( path = "/annotations/{annotationGUID}/extended-annotations")

    public  AnnotationListResponse  getExtendedAnnotations(@PathVariable String   serverName,
                                                           @PathVariable String   userId,
                                                           @PathVariable String   annotationGUID,
                                                           @RequestParam int      startingFrom,
                                                           @RequestParam int      maximumResults)
    {
        return restAPI.getExtendedAnnotations(serverName,
                                              userId,
                                              annotationGUID,
                                              startingFrom,
                                              maximumResults);
    }


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @GetMapping( path = "/annotations/{annotationGUID}")

    public  AnnotationResponse getAnnotation(@PathVariable String   serverName,
                                             @PathVariable String   userId,
                                             @PathVariable String   annotationGUID)
    {
        return restAPI.getAnnotation(serverName, userId, annotationGUID);
    }


    /**
     * Add a new annotation to the annotation store as a top level annotation linked directly off of the report.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param discoveryReportGUID unique identifier of the discovery analysis report
     * @param requestBody annotation object
     *
     * @return unique identifier of new annotation or
     *
     *  InvalidParameterException the annotation is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem retrieving adding the annotation to the annotation store.
     */
    @PostMapping( path = "/discovery-analysis-reports/{discoveryReportGUID}/annotations")

    public  GUIDResponse  addAnnotationToDiscoveryReport(@PathVariable String     serverName,
                                                         @PathVariable String     userId,
                                                         @PathVariable String     discoveryReportGUID,
                                                         @RequestBody  Annotation requestBody)
    {
        return restAPI.addAnnotationToDiscoveryReport(serverName,
                                                      userId,
                                                      discoveryReportGUID,
                                                      requestBody);
    }


    /**
     * Add a new annotation and link it to an existing annotation.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param anchorAnnotationGUID unique identifier of the annotation that this new one os to be attached to
     * @param requestBody annotation object
     *
     * @return fully filled out annotation or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem saving annotations in the annotation store.
     */
    @PostMapping( path = "/annotations/{anchorAnnotationGUID}/extended-annotations")

    public  AnnotationResponse  addAnnotationToAnnotation(@PathVariable String     serverName,
                                                          @PathVariable String     userId,
                                                          @PathVariable String     anchorAnnotationGUID,
                                                          @RequestBody  Annotation requestBody)
    {
        return restAPI.addAnnotationToAnnotation(serverName,
                                                 userId,
                                                 anchorAnnotationGUID,
                                                 requestBody);
    }


    /**
     * Link an existing annotation to another object.  The anchor object must be a Referenceable.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param anchorGUID unique identifier that the annotation is to be linked to
     * @param annotationGUID unique identifier of the annotation
     * @param requestBody null request body to satisfy POST semantics
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem updating annotations in the annotation store.
     */
    @PostMapping( path = "/annotations/{annotationGUID}/related-instances/{anchorGUID}")

    public VoidResponse  linkAnnotation(@PathVariable String          serverName,
                                        @PathVariable String          userId,
                                        @PathVariable String          anchorGUID,
                                        @PathVariable String          annotationGUID,
                                        @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.linkAnnotation(serverName,
                                      userId,
                                      anchorGUID,
                                      annotationGUID,
                                      requestBody);
    }


    /**
     * Remove the relationship between an annotation and another object.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param anchorGUID unique identifier that the annotation is to be unlinked from
     * @param annotationGUID unique identifier of the annotation
     * @param requestBody null request body to satisfy POST semantics
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem updating annotations in the annotation store.
     */
    @PostMapping( path = "/annotations/{annotationGUID}/related-instances/{anchorGUID}/delete")

    public VoidResponse  unlinkAnnotation(@PathVariable String          serverName,
                                          @PathVariable String          userId,
                                          @PathVariable String          anchorGUID,
                                          @PathVariable String          annotationGUID,
                                          @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.unlinkAnnotation(serverName,
                                        userId,
                                        anchorGUID,
                                        annotationGUID,
                                        requestBody);
    }


    /**
     * Replace the current properties of an annotation.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param annotationGUID identifier of the annotation to change
     * @param requestBody new properties
     *
     * @return fully filled out annotation or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem updating the annotation in the annotation store.
     */
    @PostMapping( path = "/annotations/{annotationGUID}")

    public AnnotationResponse updateAnnotation(@PathVariable String     serverName,
                                               @PathVariable String     userId,
                                               @PathVariable String     annotationGUID,
                                               @RequestBody  Annotation requestBody)
    {
        return restAPI.updateAnnotation(serverName,
                                        userId,
                                        annotationGUID,
                                        requestBody);
    }


    /**
     * Remove an annotation from the annotation store.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation
     * @param requestBody null request body to satisfy POST semantics
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem deleting the annotation from the annotation store.
     */
    @PostMapping( path = "/annotations/{annotationGUID}/delete")

    public VoidResponse  deleteAnnotation(@PathVariable String          serverName,
                                          @PathVariable String          userId,
                                          @PathVariable String          annotationGUID,
                                          @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.deleteAnnotation(serverName, userId, annotationGUID, requestBody);
    }
}

