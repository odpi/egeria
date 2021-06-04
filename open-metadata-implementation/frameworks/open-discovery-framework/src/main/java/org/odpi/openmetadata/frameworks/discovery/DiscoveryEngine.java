/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery;

import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.ffdc.DiscoveryEngineException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;

import java.util.List;
import java.util.Map;

/**
 * DiscoveryEngine provides the interface for a discovery engine.  The discovery engine runs inside a discovery server.
 * The discovery server provides the server runtime environment for the discovery engine along with the interfaces for
 * configuring the discovery engine.
 */
public abstract class DiscoveryEngine
{
    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param userId identifier of calling user
     * @param assetGUID identifier of the asset to analyze.
     * @param assetDiscoveryType identifier of the type of analysis - this determines which discovery service to run.
     *
     * @return unique id for the discovery request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract String discoverAsset(String              userId,
                                         String              assetGUID,
                                         String              assetDiscoveryType) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        DiscoveryEngineException;

    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param userId identifier of calling user
     * @param assetGUID identifier of the asset to analyze.
     * @param assetDiscoveryType identifier of the type of analysis - this determines which discovery service to run.
     * @param analysisParameters name value properties to control the analysis
     *
     * @return unique id for the discovery request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract String discoverAsset(String              userId,
                                         String              assetGUID,
                                         String              assetDiscoveryType,
                                         Map<String, String> analysisParameters) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        DiscoveryEngineException;


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param userId identifier of calling user
     * @param assetGUID identifier of the asset to analyze.
     * @param assetDiscoveryType identifier of the type of analysis - this determines which discovery service to run.
     * @param analysisParameters name value properties to control the analysis
     * @param annotationTypes list of the types of annotations to produce (and no others)
     *
     * @return unique id for the discovery request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract String discoverAsset(String              userId,
                                         String              assetGUID,
                                         String              assetDiscoveryType,
                                         Map<String, String> analysisParameters,
                                         List<String>        annotationTypes) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     DiscoveryEngineException;


    /**
     * Explore each of the assets in the asset store.  A new instance of the discovery service is started for
     * each retrieved asset.
     *
     * @param userId identifier of calling user
     * @param assetDiscoveryType identifier of the type of analysis - this determines which discovery service to run.
     * @param analysisParameters name value properties to control the analysis
     * @param annotationTypes list of the types of annotations to produce (and no others)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract void scanAllAssets(String              userId,
                                       String              assetDiscoveryType,
                                       Map<String, String> analysisParameters,
                                       List<String>        annotationTypes) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   DiscoveryEngineException;


    /**
     * Request the status of an executing discovery request.
     *
     * @param userId identifier of calling user
     * @param discoveryRequestGUID identifier of the discovery request.
     *
     * @return status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract DiscoveryRequestStatus getDiscoveryStatus(String   userId,
                                                              String   discoveryRequestGUID) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    DiscoveryEngineException;


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param userId identifier of calling user
     * @param discoveryRequestGUID identifier of the discovery request.
     *
     * @return discovery report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract DiscoveryAnalysisReport getDiscoveryReport(String   userId,
                                                               String   discoveryRequestGUID) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               DiscoveryEngineException;


    /**
     * Return the annotations linked direction to the report.
     *
     * @param userId identifier of calling user
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract List<Annotation>  getDiscoveryReportAnnotations(String   userId,
                                                                    String   discoveryRequestGUID,
                                                                    int      startingFrom,
                                                                    int      maximumResults) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    DiscoveryEngineException;


    /**
     * Return any annotations attached to this annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract List<Annotation>  getExtendedAnnotations(String   userId,
                                                             String   annotationGUID,
                                                             int      startingFrom,
                                                             int      maximumResults) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             DiscoveryEngineException;


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract Annotation        getAnnotation(String   userId,
                                                    String   annotationGUID) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    DiscoveryEngineException;
}
