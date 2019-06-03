/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.api;

import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.AnnotationStatus;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;

import java.util.List;

/**
 * The AssetReviewInterface is used by the asset owner to review the state of the asset including any quality and usage
 * metrics associated with the asset.
 */
public interface AssetReviewInterface
{
    /**
     * Return the basic attributes of an asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @return basic asset properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    Asset   getAssetSummary(String  userId,
                            String  assetGUID) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException;


    /**
     * Return everything that is known about the asset
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @return asset properties with links to other attached content
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    AssetUniverse  getAssetProperties(String  userId,
                                      String  assetGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Return a connector for the asset to enable the calling user to access the content.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @return connector object
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    Connector getConnectorToAsset(String  userId,
                                  String  assetGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


    /**
     * Return the discovery analysis reports about the asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maximumResults maximum number of elements to return an this call
     * @return list of discovery analysis reports
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    List<DiscoveryAnalysisReport>   getDiscoveryAnalysisReports(String  userId,
                                                                String  assetGUID,
                                                                int     startingFrom,
                                                                int     maximumResults) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Return the annotations linked directly to the report.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param annotationStatus status of the desired annotations - null means all statuses.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    List<Annotation> getDiscoveryReportAnnotations(String           userId,
                                                   String           discoveryReportGUID,
                                                   AnnotationStatus annotationStatus,
                                                   int              startingFrom,
                                                   int              maximumResults) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Return any annotations attached to this annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param annotationStatus status of the desired annotations - null means all statuses.
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    List<Annotation>  getExtendedAnnotations(String           userId,
                                             String           annotationGUID,
                                             AnnotationStatus annotationStatus,
                                             int              startingFrom,
                                             int              maximumResults) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;

}
