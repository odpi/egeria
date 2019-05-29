/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.discoveryserver.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.AnnotationListResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.AnnotationResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.DiscoveryAnalysisReportResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.DiscoveryRequestStatusResponse;
import org.odpi.openmetadata.discoveryserver.handlers.DiscoveryEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.ffdc.DiscoveryEngineException;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * DiscoveryRESTServices provides the external service implementation for a discovery engine.
 * Each method contains the discovery server name and the discovery engine identifier (guid).
 * The DiscoveryRESTServices locates the correct discovery engine instance within the correct
 * discovery server instance and delegates the request.
 */
public class DiscoveryRESTServices
{
    private static DiscoveryServerInstanceHandler instanceHandler = new DiscoveryServerInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(DiscoveryRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param userId identifier of calling user
     * @param assetGUID identifier of the asset to analyze.
     * @param assetType identifier of the type of asset to analyze - this determines which discovery service to run.
     *
     * @return unique id for the discovery request or
     *
     *  InvalidParameterException the discovery engine is not recognized.
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  GUIDResponse discoverAsset(String   serverName,
                                       String   discoveryEngineGUID,
                                       String   userId,
                                       String   assetGUID,
                                       String   assetType)
    {
        return this.discoverAsset(serverName, discoveryEngineGUID, userId, assetGUID, assetType, null, null);
    }


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param userId identifier of calling user
     * @param assetGUID identifier of the asset to analyze.
     * @param assetType identifier of the type of asset to analyze - this determines which discovery service to run.
     * @param analysisParameters name value properties to control the analysis
     *
     * @return unique id for the discovery request or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  GUIDResponse discoverAsset(String              serverName,
                                       String              discoveryEngineGUID,
                                       String              userId,
                                       String              assetGUID,
                                       String              assetType,
                                       Map<String, String> analysisParameters)
    {
        return this.discoverAsset(serverName, discoveryEngineGUID, userId, assetGUID, assetType, analysisParameters, null);
    }


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param userId identifier of calling user
     * @param assetGUID identifier of the asset to analyze.
     * @param assetType identifier of the type of asset to analyze - this determines which discovery service to run.
     * @param analysisParameters name value properties to control the analysis
     * @param annotationTypes list of the types of annotations to produce (and no others)
     *
     * @return unique id for the discovery request or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  GUIDResponse discoverAsset(String              serverName,
                                       String              discoveryEngineGUID,
                                       String              userId,
                                       String              assetGUID,
                                       String              assetType,
                                       Map<String, String> analysisParameters,
                                       List<String>        annotationTypes)
    {
        final String        methodName = "discoverAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();
        OMRSAuditLog auditLog = null;

        try
        {
            DiscoveryEngineHandler handler = instanceHandler.getDiscoveryEngineHandler(userId,
                                                                                       serverName,
                                                                                       discoveryEngineGUID,
                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUID(handler.discoverAsset(assetGUID, assetType, analysisParameters, annotationTypes));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Request the status of an executing discovery request.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryRequestGUID identifier of the discovery request.
     *
     * @return status enum or
     *
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public DiscoveryRequestStatusResponse getDiscoveryStatus(String   serverName,
                                                             String   discoveryEngineGUID,
                                                             String   userId,
                                                             String   discoveryRequestGUID)
    {
        return null;
    }


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryRequestGUID identifier of the discovery request.
     *
     * @return discovery report or
     *
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public DiscoveryAnalysisReportResponse getDiscoveryReport(String   serverName,
                                                              String   discoveryEngineGUID,
                                                              String   userId,
                                                              String   discoveryRequestGUID)
    {
        return null;
    }


    /**
     * Return the annotations linked direction to the report.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations or
     *
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public AnnotationListResponse getDiscoveryReportAnnotations(String   serverName,
                                                                String   discoveryEngineGUID,
                                                                String   userId,
                                                                String   discoveryRequestGUID,
                                                                int      startingFrom,
                                                                int      maximumResults)
    {
        return null;
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects or
     *
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public AnnotationListResponse getExtendedAnnotations(String   serverName,
                                                         String   discoveryEngineGUID,
                                                         String   userId,
                                                         String   annotationGUID,
                                                         int      startingFrom,
                                                         int      maximumResults)
    {
        return null;
    }


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object or
     *
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public AnnotationResponse getAnnotation(String   serverName,
                                            String   discoveryEngineGUID,
                                            String   userId,
                                            String   annotationGUID)
    {
        return null;
    }
}
