/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.*;
import org.odpi.openmetadata.accessservices.discoveryengine.server.DiscoveryEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.DataField;
import org.odpi.openmetadata.frameworks.discovery.properties.DataFieldLink;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.springframework.web.bind.annotation.*;


/**
 * DiscoveryMetadataStoreResource provides the generic server-side interface for the Discovery Engine
 * Open Metadata Access Service (OMAS).
 * This provides the support for the AssetCatalogStore, AssetStore and AnnotationsStore defined in the ODF.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/discovery-engine/users/{userId}")

@Tag(name="Metadata Access Server: Discovery Engine OMAS", description="The Discovery Engine OMAS provides APIs and events for metadata discovery tools that are surveying the data landscape and recording information in metadata repositories.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/discovery-engine/overview/"))

public class DiscoveryMetadataStoreResource
{
    private final DiscoveryEngineRESTServices restAPI = new DiscoveryEngineRESTServices();


    /**
     * Default constructor
     */
    public DiscoveryMetadataStoreResource()
    {
    }


    /**
     * Return the next set of assets to process.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param startFrom starting point of the query
     * @param pageSize maximum number of results to return
     * @return list of unique identifiers for located assets or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @GetMapping(path = "/assets")

    public GUIDListResponse getAssets(@PathVariable String serverName,
                                      @PathVariable String userId,
                                      @RequestParam int    startFrom,
                                      @RequestParam int    pageSize)
    {
        return restAPI.getAssets(serverName, userId, startFrom, pageSize);
    }


    /**
     * Return the assets with the same qualified name.  If all is well there should be only one
     * returned.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param name the qualified name to query on
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @return list of unique identifiers for matching assets or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @PostMapping(path = "/assets/by-qualified-name")

    public GUIDListResponse getAssetsByQualifiedName(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @RequestBody  String name,
                                                     @RequestParam int    startFrom,
                                                     @RequestParam int    pageSize)
    {
        return restAPI.getAssetsByQualifiedName(serverName, userId, name, startFrom, pageSize);
    }


    /**
     * Return the list of matching assets that have the supplied name as either the
     * qualified name or display name.  This is an exact match retrieval.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param name name to query for
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @return list of unique identifiers for matching assets or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @PostMapping(path = "/assets/by-name")

    public GUIDListResponse  getAssetsByName(@PathVariable String serverName,
                                             @PathVariable String userId,
                                             @RequestBody  String name,
                                             @RequestParam int    startFrom,
                                             @RequestParam int    pageSize)
    {
        return restAPI.getAssetsByName(serverName, userId, name, startFrom, pageSize);
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.  The search string is interpreted as a regular expression (RegEx).
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param searchString string to search for in text
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of assets that match the search string or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @PostMapping(path = "/assets/by-search-string")

    public GUIDListResponse  findAssets(@PathVariable String serverName,
                                        @PathVariable String userId,
                                        @RequestBody  String searchString,
                                        @RequestParam int    startFrom,
                                        @RequestParam int    pageSize)
    {
        return restAPI.findAssets(serverName, userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of assets that have the same endpoint address.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param networkAddress address to query on
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @return list of unique identifiers for matching assets or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @PostMapping(path = "/assets/by-endpoint-address")

    public  GUIDListResponse findAssetsByEndpoint(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @RequestBody  String networkAddress,
                                                  @RequestParam int    startFrom,
                                                  @RequestParam int    pageSize)
    {
        return restAPI.findAssetsByEndpoint(serverName, userId, networkAddress, startFrom, pageSize);
    }


    /**
     * Log an audit message about this asset.
     *
     * @param serverName            name of server instance to route request to
     * @param userId                userId of user making request.
     * @param assetGUID             unique identifier for asset.
     * @param discoveryService      unique name for discoveryService.
     * @param message               message to log
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @PostMapping(path = "/assets/{assetGUID}/log-records/{discoveryService}")

    public VoidResponse logAssetAuditMessage(@PathVariable String serverName,
                                             @PathVariable String userId,
                                             @PathVariable String assetGUID,
                                             @PathVariable String discoveryService,
                                             @RequestBody  String message)
    {
        return restAPI.logAssetAuditMessage(serverName, userId, assetGUID, discoveryService, message);
    }


    /**
     * Create a new discovery analysis report and chain it to its asset, discovery engine and discovery service.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset being analysed
     * @param requestBody  all the other parameters
     *
     * @return the unique identifier of the new discovery report or
     *  InvalidParameterException one of the parameters is invalid or
     *  UserNotAuthorizedException the user is not authorized to access the asset and/or report or
     *  PropertyServerException there was a problem in the store whether the asset/report properties are kept.
     */
    @PostMapping(path = "/assets/{assetGUID}/discovery-analysis-reports")

    public GUIDResponse createDiscoveryAnalysisReport(@PathVariable String                             serverName,
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
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @PostMapping(path = "/discovery-analysis-reports/{discoveryReportGUID}")

    public VoidResponse updateDiscoveryAnalysisReport(@PathVariable String                  serverName,
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
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @GetMapping(path = "/discovery-analysis-reports/{discoveryReportGUID}")

    public DiscoveryAnalysisReportResponse getDiscoveryReport(@PathVariable String   serverName,
                                                              @PathVariable String   userId,
                                                              @PathVariable String   discoveryReportGUID)
    {
        return restAPI.getDiscoveryAnalysisReport(serverName, userId, discoveryReportGUID);
    }


    /**
     * Return the annotation subtype names.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @return list of type names that are subtypes of annotation or
     * throws InvalidParameterException full path or userId is null or
     * throws PropertyServerException problem accessing property server or
     * throws UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/annotations/sub-types")

    public NameListResponse getTypesOfAnnotation(@PathVariable String  serverName,
                                                 @PathVariable String  userId)
    {
        return restAPI.getTypesOfAnnotation(serverName, userId);
    }



    /**
     * Return the annotation subtype names mapped to their descriptions.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @return list of type names that are subtypes of annotation or
     * throws InvalidParameterException full path or userId is null or
     * throws PropertyServerException problem accessing property server or
     * throws UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/annotations/sub-types/descriptions")

    public StringMapResponse getTypesOfAnnotationWithDescriptions(@PathVariable String  serverName,
                                                                  @PathVariable String  userId)
    {
        return restAPI.getTypesOfAnnotationWithDescriptions(serverName, userId);
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
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    @GetMapping(path = "/assets/{assetGUID}/annotations")

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
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @GetMapping(path = "/discovery-analysis-reports/{discoveryReportGUID}/annotations")

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
     * @param annotationGUID parent annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @GetMapping(path = "/annotations/{annotationGUID}/extended-annotations")

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
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @GetMapping(path = "/annotations/{annotationGUID}")

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
     *  InvalidParameterException the annotation is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem retrieving adding the annotation to the annotation store.
     */
    @PostMapping(path = "/discovery-analysis-reports/{discoveryReportGUID}/annotations")

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
     * @param parentAnnotationGUID unique identifier of the annotation that this new one os to be attached to
     * @param requestBody annotation object
     *
     * @return unique identifier of new annotation or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem saving annotations in the annotation store.
     */
    @PostMapping(path = "/annotations/{parentAnnotationGUID}/extended-annotations")

    public  GUIDResponse  addAnnotationToAnnotation(@PathVariable String     serverName,
                                                    @PathVariable String     userId,
                                                    @PathVariable String     parentAnnotationGUID,
                                                    @RequestBody  Annotation requestBody)
    {
        return restAPI.addAnnotationToAnnotation(serverName,
                                                 userId,
                                                 parentAnnotationGUID,
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
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem updating the annotation in the annotation store.
     */
    @PostMapping(path = "/annotations/{annotationGUID}/update")

    public VoidResponse updateAnnotation(@PathVariable String     serverName,
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
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem deleting the annotation from the annotation store.
     */
    @PostMapping(path = "/annotations/{annotationGUID}/delete")

    public VoidResponse  deleteAnnotation(@PathVariable                   String          serverName,
                                          @PathVariable                   String          userId,
                                          @PathVariable                   String          annotationGUID,
                                          @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.deleteAnnotation(serverName, userId, annotationGUID, requestBody);
    }




    /**
     * Return the list of data fields from previous runs of the discovery service.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param discoveryReportGUID unique identifier of the discovery analysis report
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     *
     * @return list of data fields (or null if none are registered) or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem retrieving data fields from the annotation store.
     */
    @GetMapping(path = "/discovery-analysis-reports/{discoveryReportGUID}/data-fields/previous")

    public DataFieldListResponse getPreviousDataFieldsForAsset(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @PathVariable String discoveryReportGUID,
                                                               @RequestParam int    startingFrom,
                                                               @RequestParam int    maximumResults)
    {
        return restAPI.getPreviousDataFieldsForAsset(serverName, userId, discoveryReportGUID, startingFrom, maximumResults);
    }


    /**
     * Return the current list of data fields for this discovery run.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param discoveryReportGUID unique identifier of the discovery analysis report
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     *
     * @return list of data fields (or null if none are registered) or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem retrieving data fields from the annotation store.
     */
    @GetMapping(path = "/discovery-analysis-reports/{discoveryReportGUID}/data-fields")

    public DataFieldListResponse getNewDataFieldsForAsset(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String discoveryReportGUID,
                                                          @RequestParam int    startingFrom,
                                                          @RequestParam int    maximumResults)
    {
        return restAPI.getNewDataFieldsForAsset(serverName, userId, discoveryReportGUID, startingFrom, maximumResults);
    }


    /**
     * Return any child data fields attached to this data field.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param parentDataFieldGUID parent data field identifier
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of data fields that can be returned.
     *
     * @return list of DataField objects or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @GetMapping(path = "/data-fields/{parentDataFieldGUID}/nested-data-fields")

    public DataFieldListResponse getNestedDataFields(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @PathVariable String parentDataFieldGUID,
                                                     @RequestParam int    startingFrom,
                                                     @RequestParam int    maximumResults)
    {
        return restAPI.getNestedDataFields(serverName, userId, parentDataFieldGUID, startingFrom, maximumResults);
    }


    /**
     * Return any peer data fields attached to this data field.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param dataFieldGUID starting data field identifier
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of data fields that can be returned.
     *
     * @return list of DataField objects or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @GetMapping(path = "/data-fields/{dataFieldGUID}/linked-data-fields")

    public RelatedDataFieldListResponse geLinkedDataFields(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @PathVariable String dataFieldGUID,
                                                     @RequestParam int    startingFrom,
                                                     @RequestParam int    maximumResults)
    {
        return restAPI.getLinkedDataFields(serverName, userId, dataFieldGUID, startingFrom, maximumResults);
    }


    /**
     * Return a specific data field stored in the annotation store (previous or new).
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param dataFieldGUID unique identifier of the data field
     *
     * @return data field object or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem retrieving the data field from the annotation store.
     */
    @GetMapping(path = "/data-fields/{dataFieldGUID}")

    public DataFieldResponse  getDataField(@PathVariable String serverName,
                                           @PathVariable String userId,
                                           @PathVariable String dataFieldGUID)
    {
        return restAPI.getDataField(serverName, userId, dataFieldGUID);
    }


    /**
     * Add a new data field to the Annotation store linked off of an annotation (typically SchemaAnalysisAnnotation).
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation that the data field is to be linked to
     * @param dataField dataField object
     *
     * @return unique identifier of new data field or
     *  InvalidParameterException the dataField is invalid or the annotation GUID points to an annotation
     *                                   that can not be associated with a data field.
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem  adding the data field to the Annotation store.
     */
    @PostMapping(path = "/annotations/{annotationGUID}/data-fields")

    public GUIDResponse  addDataFieldToDiscoveryReport(@PathVariable String    serverName,
                                                       @PathVariable String    userId,
                                                       @PathVariable String    annotationGUID,
                                                       @RequestBody  DataField dataField)
    {
        return restAPI.addDataFieldToDiscoveryReport(serverName, userId, annotationGUID, dataField);
    }


    /**
     * Add a new data field and link it to an existing data field.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param parentDataFieldGUID unique identifier of the data field that this new one is to be attached to
     * @param dataField data field object
     * @return unique identifier of new data field or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem saving data fields in the annotation store.
     */
    @PostMapping(path = "/data-fields/{parentDataFieldGUID}/nested-data-fields")

    public GUIDResponse  addDataFieldToDataField(@PathVariable String    serverName,
                                                 @PathVariable String    userId,
                                                 @PathVariable String    parentDataFieldGUID,
                                                 @RequestBody  DataField dataField)
    {
        return restAPI.addDataFieldToDataField(serverName, userId, parentDataFieldGUID, dataField);
    }


    /**
     * Link two exising data fields together in a peer relationship.
     *
     * @param userId identifier of calling user
     * @param linkFromDataFieldGUID unique identifier of the data field that is at end 1 of the relationship
     * @param relationshipProperties optional properties for the relationship
     * @param linkToDataFieldGUID unique identifier of the data field that is at end 1 of the relationship
     * @return void or
     *  InvalidParameterException one of the parameters is invalid or
     *  UserNotAuthorizedException the user id not authorized to issue this request or
     *  PropertyServerException there was a problem deleting the data field from the annotation store.
     */
    @PostMapping(path = "/data-fields/{linkFromDataFieldGUID}/linked-data-fields/{linkToDataFieldGUID}")

    public VoidResponse linkDataFields(@PathVariable String        serverName,
                                       @PathVariable String        userId,
                                       @PathVariable String        linkFromDataFieldGUID,
                                       @PathVariable String        linkToDataFieldGUID,
                                       @RequestBody (required = false)
                                                     DataFieldLink relationshipProperties)
    {
        return restAPI.linkDataFields(serverName, userId, linkFromDataFieldGUID ,linkToDataFieldGUID, relationshipProperties);
    }


    /**
     * Add a new annotation and link it to an existing data field.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param parentDataFieldGUID unique identifier of the data field that this new one is to be attached to
     * @param annotation data field object
     *
     * @return unique identifier of new annotation or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem saving data fields in the annotation store.
     */
    @PostMapping(path = "/data-fields/{parentDataFieldGUID}/annotations")

    public GUIDResponse addAnnotationToDataField(@PathVariable String     serverName,
                                                 @PathVariable String     userId,
                                                 @PathVariable String     parentDataFieldGUID,
                                                 @RequestBody  Annotation annotation)
    {
        return restAPI.addAnnotationToDataField(serverName, userId, parentDataFieldGUID, annotation);
    }


    /**
     * Replace the current properties of a data field.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param dataFieldGUID unique identifier of data field
     * @param dataField new properties
     * @return fully filled out data field or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem updating the data field in the annotation store.
     */
    @PostMapping(path = "/data-fields/{dataFieldGUID}/update")

    public VoidResponse updateDataField(@PathVariable String    serverName,
                                        @PathVariable String    userId,
                                        @PathVariable String    dataFieldGUID,
                                        @RequestBody  DataField dataField)
    {
        return restAPI.updateDataField(serverName, userId, dataFieldGUID, dataField);
    }


    /**
     * Remove a data field from the annotation store.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param dataFieldGUID unique identifier of the data field
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException one of the parameters is invalid or
     *  UserNotAuthorizedException the user id not authorized to issue this request or
     *  PropertyServerException there was a problem deleting the data field from the annotation store.
     */
    @PostMapping(path = "/data-fields/{dataFieldGUID}/delete")

    public VoidResponse  deleteDataField(@PathVariable                  String          serverName,
                                         @PathVariable                  String          userId,
                                         @PathVariable                  String          dataFieldGUID,
                                         @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.deleteDataField(serverName, userId, dataFieldGUID, requestBody);
    }
}

