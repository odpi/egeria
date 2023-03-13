/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.assetanalysis.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.AnnotationListResponse;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.AnnotationResponse;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.DiscoveryAnalysisReportResponse;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.DiscoveryRequestRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.engineservices.assetanalysis.server.AssetAnalysisRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * DiscoveryEngineResource provides the server-side catcher for REST calls using Spring that target a specific
 * discovery engine hosted in a engine host server.
 * The engine host server routes these requests to the named discovery engine.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/engine-services/asset-analysis/users/{userId}/discovery-engines/{discoveryEngineName}")

@Tag(name="Asset Analysis OMES", description="The Asset Analysis OMES provide the core subsystem for driving requests for automated metadata discovery services.",
     externalDocs=@ExternalDocumentation(description="Asset Analysis Open Metadata Engine Services (OMES)",
                                         url="https://egeria-project.org/services/omes/asset-analysis/overview/"))

public class DiscoveryEngineResource
{
    private AssetAnalysisRESTServices restAPI = new AssetAnalysisRESTServices();


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param serverName name of the engine host server.
     * @param discoveryEngineName unique name of the discovery engine.
     * @param userId identifier of calling user
     * @param assetGUID identifier of the asset to analyze.
     * @param discoveryRequestType identifier of the type of discovery request to run - this determines which discovery service to run.
     * @param requestBody containing analysisParameters and annotationTypes
     *
     * @return unique id for the discovery request or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    @PostMapping(path = "/discovery-request-types/{discoveryRequestType}/assets/{assetGUID}")

    public  GUIDResponse discoverAsset(@PathVariable String                      serverName,
                                       @PathVariable String                      discoveryEngineName,
                                       @PathVariable String                      userId,
                                       @PathVariable String                      assetGUID,
                                       @PathVariable String                      discoveryRequestType,
                                       @RequestBody  DiscoveryRequestRequestBody requestBody)
    {
        return restAPI.discoverAsset(serverName,
                                     discoveryEngineName,
                                     userId,
                                     assetGUID,
                                     discoveryRequestType,
                                     requestBody);
    }


    /**
     * Request the execution of a discovery service for each asset that is stored in the asset catalog.
     *
     * @param serverName name of the engine host server.
     * @param discoveryEngineName unique name of the discovery engine.
     * @param userId identifier of calling user
     * @param discoveryRequestType identifier of the type of discovery request to run - this determines which discovery service to run.
     * @param requestBody containing analysisParameters and annotationTypes
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    @PostMapping(path = "/discovery-request-types/{discoveryRequestType}/assets")

    public VoidResponse scanAllAssets(@PathVariable                  String                       serverName,
                                      @PathVariable                  String                       discoveryEngineName,
                                      @PathVariable                  String                       userId,
                                      @PathVariable                  String                       discoveryRequestType,
                                      @RequestBody(required = false) DiscoveryRequestRequestBody  requestBody)
    {
        return restAPI.scanAllAssets(serverName,
                                     discoveryEngineName,
                                     userId,
                                     discoveryRequestType,
                                     requestBody);
    }


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param serverName name of the engine host server.
     * @param discoveryEngineName unique name of the discovery engine.
     * @param userId calling user
     * @param discoveryRequestGUID identifier of the discovery request.
     *
     * @return discovery report or
     *
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    @GetMapping(path = "/discovery-analysis-reports/{discoveryRequestGUID}")

    public DiscoveryAnalysisReportResponse getDiscoveryAnalysisReport(@PathVariable String   serverName,
                                                                      @PathVariable String   discoveryEngineName,
                                                                      @PathVariable String   userId,
                                                                      @PathVariable String   discoveryRequestGUID)
    {
        return restAPI.getDiscoveryAnalysisReport(serverName, discoveryEngineName, userId, discoveryRequestGUID);
    }


    /**
     * Return the annotations linked direction to the report.
     *
     * @param serverName name of the engine host server.
     * @param discoveryEngineName unique name of the discovery engine.
     * @param userId calling user
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations or
     *
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    @GetMapping(path = "/discovery-analysis-reports/{discoveryRequestGUID}/annotations")

    public AnnotationListResponse getDiscoveryReportAnnotations(@PathVariable String   serverName,
                                                                @PathVariable String   discoveryEngineName,
                                                                @PathVariable String   userId,
                                                                @PathVariable String   discoveryRequestGUID,
                                                                @RequestParam int      startingFrom,
                                                                @RequestParam int      maximumResults)
    {
        return restAPI.getDiscoveryReportAnnotations(serverName,
                                                     discoveryEngineName,
                                                     userId,
                                                     discoveryRequestGUID,
                                                     startingFrom,
                                                     maximumResults);
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param serverName name of the engine host server.
     * @param discoveryEngineName unique name of the discovery engine.
     * @param userId calling user
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects or
     *
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    @GetMapping(path = "/annotations/{annotationGUID}/extended-annotations")

    public AnnotationListResponse getExtendedAnnotations(@PathVariable String   serverName,
                                                         @PathVariable String   discoveryEngineName,
                                                         @PathVariable String   userId,
                                                         @PathVariable String   annotationGUID,
                                                         @RequestParam int      startingFrom,
                                                         @RequestParam int      maximumResults)
    {
        return restAPI.getExtendedAnnotations(serverName,
                                              discoveryEngineName,
                                              userId,
                                              annotationGUID,
                                              startingFrom,
                                              maximumResults);
    }


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param serverName name of the engine host server.
     * @param discoveryEngineName unique name of the discovery engine.
     * @param userId calling user
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object or
     *
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    @GetMapping(path = "/annotations/{annotationGUID}")

    public AnnotationResponse getAnnotation(@PathVariable String   serverName,
                                            @PathVariable String   discoveryEngineName,
                                            @PathVariable String   userId,
                                            @PathVariable String   annotationGUID)
    {
        return restAPI.getAnnotation(serverName, discoveryEngineName, userId, annotationGUID);
    }
}
