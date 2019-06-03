/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.server.spring;


import org.odpi.openmetadata.accessservices.assetowner.rest.ZoneRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.rest.ZoneResponse;
import org.odpi.openmetadata.accessservices.assetowner.server.AssetOwnerRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.*;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.AnnotationListResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.DiscoveryAnalysisReportResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.StatusRequestBody;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AssetOwnerResource provides the generic server-side interface for the Asset Owner Open Metadata Access Service (OMAS).
 * There are other resources that provide specialized methods for specific types of Asset.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-owner/users/{userId}")
public class AssetOwnerResource
{
    private AssetOwnerRESTServices restAPI = new AssetOwnerRESTServices();


    /**
     * Default constructor
     */
    public AssetOwnerResource()
    {
    }

    /*
     * ==============================================
     * AssetOnboardingInterface
     * ==============================================
     */


    /**
     * Add a simple asset description to the catalog.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user (assumed to be the owner)
     * @param typeName specific type of the asset - this must match a defined subtype
     * @param requestBody other properties for asset
     *
     * @return unique identifier (guid) of the asset or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{typeName}")

    public GUIDResponse addAssetToCatalog(@PathVariable String           serverName,
                                          @PathVariable String           userId,
                                          @PathVariable String           typeName,
                                          @RequestBody  AssetRequestBody requestBody)
    {
        return restAPI.addAssetToCatalog(serverName, userId, typeName, requestBody);
    }


    /**
     * Links the supplied schema to the asset.  If the schema is not defined in the metadata repository, it
     * is created.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaType schema to attach - a new schema is always created because schema can not be shared
     *                   between assets.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{assetGUID}/schema-type")

    public GUIDResponse   addSchemaToAsset(@PathVariable String     serverName,
                                           @PathVariable String     userId,
                                           @PathVariable String     assetGUID,
                                           @RequestBody  SchemaType schemaType)
    {
        return restAPI.addSchemaToAsset(serverName, userId, assetGUID, schemaType);
    }


    /**
     * Adds attributes to a complex schema type like a relational table or a structured document.
     * This method can be called repeatedly to add many attributes to a schema.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier if the schema to anchor these attributes to.
     * @param schemaAttributes list of schema attribute objects.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.POST, path = "/schemas/{schemaTypeGUID}/schema-attributes")

    public VoidResponse addSchemaAttributesToSchema(@PathVariable String                 serverName,
                                                    @PathVariable String                 userId,
                                                    @PathVariable String                 schemaTypeGUID,
                                                    @RequestBody  List<SchemaAttribute>  schemaAttributes)
    {
        return restAPI.addSchemaAttributesToSchema(serverName, userId, schemaTypeGUID, schemaAttributes);
    }



    /**
     * Adds a connection to an asset.  Assets can have multiple connections attached.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to attach the connection to
     * @param connection connection object.  If the connection is already stored (matching guid)
     *                   then the existing connection is used.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{assetGUID}/connection")

    public VoidResponse addConnectionToAsset(@PathVariable String     serverName,
                                             @PathVariable String     userId,
                                             @PathVariable String     assetGUID,
                                             @RequestBody  Connection connection)
    {
        return restAPI.addConnectionToAsset(serverName, userId, assetGUID, connection);
    }



    /*
     * ==============================================
     * AssetClassificationInterface
     * ==============================================
     */


    /**
     * Create a simple relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that is being described
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody null request body to satisfy POST request.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{assetGUID}/meanings/{glossaryTermGUID}/elements/{assetElementGUID}")

    public VoidResponse  addSemanticAssignment(@PathVariable String          serverName,
                                               @PathVariable String          userId,
                                               @PathVariable String          assetGUID,
                                               @PathVariable String          glossaryTermGUID,
                                               @PathVariable String          assetElementGUID,
                                               @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.addSemanticAssignment(serverName,
                                             userId,
                                             assetGUID,
                                             glossaryTermGUID,
                                             assetElementGUID,
                                             requestBody);
    }


    /**
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param requestBody Descriptive labels describing origin of the asset
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{assetGUID}/origin")

    public VoidResponse  addAssetOrigin(@PathVariable String            serverName,
                                        @PathVariable String            userId,
                                        @PathVariable String            assetGUID,
                                        @RequestBody  OriginRequestBody requestBody)
    {
        return restAPI.addAssetOrigin(serverName, userId, assetGUID, requestBody);
    }


    /*
     * ==============================================
     * AssetVisibilityInterface
     * ==============================================
     */


    /**
     * Create a definition of a governance zone.  The qualified name of these governance zones can be added
     * to the supportedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody other properties for a governance zone
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.POST, path = "/governance-zones/")

    public VoidResponse  createGovernanceZone(@PathVariable String          serverName,
                                              @PathVariable String          userId,
                                              @RequestBody  ZoneRequestBody requestBody)
    {
        return restAPI.createGovernanceZone(serverName, userId, requestBody);
    }



    /**
     * Return information about a specific governance zone.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param qualifiedName unique name for the zone
     *
     * @return properties of the governance zone or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.GET, path = "/governance-zones/name/{qualifiedName}")

    public ZoneResponse getGovernanceZone(@PathVariable String   serverName,
                                          @PathVariable String   userId,
                                          @PathVariable String   qualifiedName)
    {
        return restAPI.getGovernanceZone(serverName, userId, qualifiedName);
    }



    /**
     * Update the zones for a specific asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetZones list of zones for the asset - these values override the current values - null means belongs
     *                   to no zones.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{assetGUID}/governance-zones")

    public VoidResponse updateAssetZones(@PathVariable String        serverName,
                                         @PathVariable String        userId,
                                         @PathVariable String        assetGUID,
                                         @RequestBody  List<String>  assetZones)
    {
        return restAPI.updateAssetZones(serverName, userId, assetGUID, assetZones);
    }



    /**
     * Update the owner information for a specific asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param requestBody values describing the new owner
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{assetGUID}/owner")

    public VoidResponse  updateAssetOwner(@PathVariable String           serverName,
                                          @PathVariable String           userId,
                                          @PathVariable String           assetGUID,
                                          @RequestBody  OwnerRequestBody requestBody)
    {
        return restAPI.updateAssetOwner(serverName, userId, assetGUID, requestBody);
    }



    /*
     * ==============================================
     * AssetReviewInterface
     * ==============================================
     */


    /**
     * Return the basic attributes of an asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     *
     * @return basic asset properties or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/summary")

    public AssetResponse getAssetSummary(@PathVariable String  serverName,
                                         @PathVariable String  userId,
                                         @PathVariable String  assetGUID)
    {
        return restAPI.getAssetSummary(serverName, userId, assetGUID);
    }



    /**
     * Return a connector for the asset to enable the calling user to access the content.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     *
     * @return connection object or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/connection")

    public ConnectionResponse getConnectionForAsset(@PathVariable String  serverName,
                                                    @PathVariable String  userId,
                                                    @PathVariable String  assetGUID)
    {
        return restAPI.getConnectionForAsset(serverName, userId, assetGUID);
    }



    /**
     * Return the discovery analysis reports about the asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maximumResults maximum number of elements to return an this call
     *
     * @return list of discovery analysis reports or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/{assetGUID}/discovery-analysis-reports")

    public DiscoveryAnalysisReportResponse getDiscoveryAnalysisReports(@PathVariable String  serverName,
                                                                       @PathVariable String  userId,
                                                                       @PathVariable String  assetGUID,
                                                                       @RequestParam int     startingFrom,
                                                                       @RequestParam int     maximumResults)
    {
        return restAPI.getDiscoveryAnalysisReports(serverName,
                                                   userId,
                                                   assetGUID,
                                                   startingFrom,
                                                   maximumResults);
    }



    /**
     * Return the annotations linked directly to the report.
     *
     * @param serverName name of the server instance to connect to
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param requestBody status of the desired annotations - null means all statuses.
     *
     * @return list of annotations or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.GET, path = "/discovery-analysis-reports/{discoveryReportGUID}/annotations")

    public AnnotationListResponse getDiscoveryReportAnnotations(@PathVariable String            serverName,
                                                                @PathVariable String            userId,
                                                                @PathVariable String            discoveryReportGUID,
                                                                @RequestParam int               startingFrom,
                                                                @RequestParam int               maximumResults,
                                                                @RequestBody  StatusRequestBody requestBody)
    {
        return restAPI.getDiscoveryReportAnnotations(serverName,
                                                     userId,
                                                     discoveryReportGUID,
                                                     startingFrom,
                                                     maximumResults,
                                                     requestBody);
    }



    /**
     * Return any annotations attached to this annotation.
     *
     * @param serverName name of the server instance to connect to
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     * @param requestBody status of the desired annotations - null means all statuses.
     *
     * @return list of Annotation objects or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @RequestMapping(method = RequestMethod.GET, path = "/annotations/{annotationGUID}/annotations")

    public AnnotationListResponse  getExtendedAnnotations(@PathVariable String            serverName,
                                                          @PathVariable String            userId,
                                                          @PathVariable String            annotationGUID,
                                                          @RequestParam int               startingFrom,
                                                          @RequestParam int               maximumResults,
                                                          @RequestBody  StatusRequestBody requestBody)
    {
        return restAPI.getExtendedAnnotations(serverName,
                                              userId,
                                              annotationGUID,
                                              startingFrom,
                                              maximumResults,
                                              requestBody);
    }



    /*
     * ==============================================
     * AssetDecommissioningInterface
     * ==============================================
     */


}
