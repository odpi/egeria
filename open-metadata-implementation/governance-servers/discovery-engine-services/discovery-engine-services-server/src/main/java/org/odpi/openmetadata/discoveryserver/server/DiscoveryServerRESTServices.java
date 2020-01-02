/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.discoveryserver.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.*;
import org.odpi.openmetadata.discoveryserver.handlers.DiscoveryEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * DiscoveryServerRESTServices provides the external service implementation for a discovery engine.
 * Each method contains the discovery server name and the discovery engine identifier (guid).
 * The DiscoveryServerRESTServices locates the correct discovery engine instance within the correct
 * discovery server instance and delegates the request.
 */
public class DiscoveryServerRESTServices
{
    private static DiscoveryServerInstanceHandler instanceHandler = new DiscoveryServerInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(DiscoveryServerRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param userId identifier of calling user
     * @param assetGUID identifier of the asset to analyze.
     * @param assetDiscoveryType identifier of the type of asset to analyze - this determines which discovery service to run.
     * @param requestBody containing analysisParameters and annotationTypes
     *
     * @return unique id for the discovery request or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  GUIDResponse discoverAsset(String                      serverName,
                                       String                      discoveryEngineGUID,
                                       String                      userId,
                                       String                      assetGUID,
                                       String                      assetDiscoveryType,
                                       DiscoveryRequestRequestBody requestBody)
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

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
            else
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setGUID(handler.discoverAsset(assetGUID,
                                                       assetDiscoveryType,
                                                       requestBody.getAnalysisParameters(),
                                                       requestBody.getAnnotationTypes()));
            }
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
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param userId identifier of calling user
     * @param assetDiscoveryType identifier of the type of asset to analyze - this determines which discovery service to run.
     * @param requestBody containing analysisParameters and annotationTypes
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public VoidResponse scanAllAssets(String                      serverName,
                                      String                      discoveryEngineGUID,
                                      String                      userId,
                                      String                      assetDiscoveryType,
                                      DiscoveryRequestRequestBody requestBody)
    {
        final String        methodName = "scanAllAssets";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        OMRSAuditLog auditLog = null;

        try
        {
            DiscoveryEngineHandler handler = instanceHandler.getDiscoveryEngineHandler(userId,
                                                                                       serverName,
                                                                                       discoveryEngineGUID,
                                                                                       methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
            else
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.scanAllAssets(assetDiscoveryType,
                                      requestBody.getAnalysisParameters(),
                                      requestBody.getAnnotationTypes());
            }
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
     * Request the discovery report for a discovery request that has completed.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param userId calling user
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
        final String        methodName = "getDiscoveryAnalysisReport";

        log.debug("Calling method: " + methodName);

        DiscoveryAnalysisReportResponse response = new DiscoveryAnalysisReportResponse();
        OMRSAuditLog                    auditLog = null;

        try
        {
            DiscoveryEngineHandler handler = instanceHandler.getDiscoveryEngineHandler(userId,
                                                                                       serverName,
                                                                                       discoveryEngineGUID,
                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setAnalysisReport(handler.getDiscoveryReport(discoveryRequestGUID));
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
     * Return the annotations linked direction to the report.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param userId calling user
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
        final String        methodName = "getDiscoveryReportAnnotations";

        log.debug("Calling method: " + methodName);

        AnnotationListResponse response = new AnnotationListResponse();
        OMRSAuditLog           auditLog = null;

        try
        {
            DiscoveryEngineHandler handler = instanceHandler.getDiscoveryEngineHandler(userId,
                                                                                       serverName,
                                                                                       discoveryEngineGUID,
                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setAnnotations(handler.getDiscoveryReportAnnotations(discoveryRequestGUID, startingFrom, maximumResults));
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
     * Return any annotations attached to this annotation.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
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
    public AnnotationListResponse getExtendedAnnotations(String   serverName,
                                                         String   discoveryEngineGUID,
                                                         String   userId,
                                                         String   discoveryRequestGUID,
                                                         String   annotationGUID,
                                                         int      startingFrom,
                                                         int      maximumResults)
    {
        final String        methodName = "getExtendedAnnotations";

        log.debug("Calling method: " + methodName);

        AnnotationListResponse response = new AnnotationListResponse();
        OMRSAuditLog           auditLog = null;

        try
        {
            DiscoveryEngineHandler handler = instanceHandler.getDiscoveryEngineHandler(userId,
                                                                                       serverName,
                                                                                       discoveryEngineGUID,
                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setAnnotations(handler.getExtendedAnnotations(discoveryRequestGUID, annotationGUID, startingFrom, maximumResults));
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
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param userId calling user
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object or
     *
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public AnnotationResponse getAnnotation(String   serverName,
                                            String   discoveryEngineGUID,
                                            String   userId,
                                            String   discoveryRequestGUID,
                                            String   annotationGUID)
    {
        final String        methodName = "getAnnotation";

        log.debug("Calling method: " + methodName);

        AnnotationResponse response = new AnnotationResponse();
        OMRSAuditLog       auditLog = null;

        try
        {
            DiscoveryEngineHandler handler = instanceHandler.getDiscoveryEngineHandler(userId,
                                                                                       serverName,
                                                                                       discoveryEngineGUID,
                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setAnnotation(handler.getAnnotation(discoveryRequestGUID, annotationGUID));
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
}
