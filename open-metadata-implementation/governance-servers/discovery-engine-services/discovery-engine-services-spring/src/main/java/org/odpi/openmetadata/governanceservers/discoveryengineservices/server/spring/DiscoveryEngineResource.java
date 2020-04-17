/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.discoveryengineservices.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.AnnotationListResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.AnnotationResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.DiscoveryAnalysisReportResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.DiscoveryRequestRequestBody;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.server.DiscoveryServerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * DiscoveryEngineResource provides the server-side catcher for REST calls using Spring that target a specific
 * discovery engine hosted in a discovery server.
 * The discovery server routes these requests to the named discovery engine.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/discovery-server/users/{userId}/discovery-engines/{discoveryEngineName}")

@Tag(name="Discovery Engine Services", description="The discovery engine services provide the core subsystem for a discovery server. A discovery server is an OMAG Server that hosts automated metadata discovery.", externalDocs=@ExternalDocumentation(description="Discovery Engine Services",url="https://egeria.odpi.org/open-metadata-implementation/governance-servers/discovery-engine-services/"))

public class DiscoveryEngineResource
{
    private DiscoveryServerRESTServices restAPI = new DiscoveryServerRESTServices();


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param serverName name of the discovery server.
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

    public  GUIDResponse discoverAsset(@PathVariable String                       serverName,
                                       @PathVariable String                       discoveryEngineName,
                                       @PathVariable String                       userId,
                                       @PathVariable String                       assetGUID,
                                       @PathVariable String                       discoveryRequestType,
                                       @RequestBody  DiscoveryRequestRequestBody  requestBody)
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
     * @param serverName name of the discovery server.
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
     * @param serverName name of the discovery server.
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
     * @param serverName name of the discovery server.
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
     * @param serverName name of the discovery server.
     * @param discoveryEngineName unique name of the discovery engine.
     * @param userId calling user
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects or
     *
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    @GetMapping(path = "/discovery-analysis-reports/{discoveryRequestGUID}/annotations/{annotationGUID}/extended-annotations")

    public AnnotationListResponse getExtendedAnnotations(@PathVariable String   serverName,
                                                         @PathVariable String   discoveryEngineName,
                                                         @PathVariable String   userId,
                                                         @PathVariable String   discoveryRequestGUID,
                                                         @PathVariable String   annotationGUID,
                                                         @RequestParam int      startingFrom,
                                                         @RequestParam int      maximumResults)
    {
        return restAPI.getExtendedAnnotations(serverName,
                                              discoveryEngineName,
                                              userId,
                                              discoveryRequestGUID,
                                              annotationGUID,
                                              startingFrom,
                                              maximumResults);
    }


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineName unique name of the discovery engine.
     * @param userId calling user
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object or
     *
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    @GetMapping(path = "discovery-analysis-reports/{discoveryRequestGUID}/annotations/{annotationGUID}")

    public AnnotationResponse getAnnotation(@PathVariable String   serverName,
                                            @PathVariable String   discoveryEngineName,
                                            @PathVariable String   userId,
                                            @PathVariable String   discoveryRequestGUID,
                                            @PathVariable String   annotationGUID)
    {
        return restAPI.getAnnotation(serverName, discoveryEngineName, userId, discoveryRequestGUID, annotationGUID);
    }
}
