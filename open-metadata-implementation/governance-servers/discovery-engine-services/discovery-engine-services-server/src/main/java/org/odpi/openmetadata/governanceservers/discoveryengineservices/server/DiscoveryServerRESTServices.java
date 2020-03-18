/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.discoveryengineservices.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.discovery.ffdc.DiscoveryEngineException;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.handlers.DiscoveryEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.rest.DiscoveryEngineStatusResponse;
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

    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(DiscoveryServerRESTServices.class),
                                                                      instanceHandler.getServiceName());
    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Request that the discovery engine refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * discovery server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineName unique name of the discovery engine.
     * @param userId identifier of calling user
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  VoidResponse refreshConfig(String                       serverName,
                                       String                       discoveryEngineName,
                                       String                       userId)
    {
        final String        methodName = "refreshConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            DiscoveryEngineHandler handler = instanceHandler.getDiscoveryEngineHandler(userId,
                                                                                       serverName,
                                                                                       discoveryEngineName,
                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.refreshConfig();
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return a summary of each of the discovery engines' status.
     *
     * @param serverName discovery server name
     * @param userId calling user
     * @return list of statuses - on for each assigned discovery engines or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     */
    public DiscoveryEngineStatusResponse getDiscoveryEngineStatuses(String   serverName,
                                                                    String   userId)
    {
        final String methodName = "getDiscoveryEngineStatuses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DiscoveryEngineStatusResponse response = new DiscoveryEngineStatusResponse();
        AuditLog                      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setDiscoveryEngineSummaries(instanceHandler.getDiscoveryEngineStatuses(userId,
                                                                                       serverName,
                                                                                       methodName));
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineName unique name of the discovery engine.
     * @param userId identifier of calling user
     * @param assetGUID identifier of the asset to analyze.
     * @param discoveryRequestType identifier of the type of asset to analyze - this determines which discovery service to run.
     * @param requestBody containing analysisParameters and annotationTypes
     *
     * @return unique id for the discovery request or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  GUIDResponse discoverAsset(String                      serverName,
                                       String                      discoveryEngineName,
                                       String                      userId,
                                       String                      assetGUID,
                                       String                      discoveryRequestType,
                                       DiscoveryRequestRequestBody requestBody)
    {
        final String        methodName = "discoverAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            DiscoveryEngineHandler handler = instanceHandler.getDiscoveryEngineHandler(userId,
                                                                                       serverName,
                                                                                       discoveryEngineName,
                                                                                       methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
            else
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setGUID(handler.discoverAsset(assetGUID,
                                                       discoveryRequestType,
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
        catch (DiscoveryEngineException error)
        {
            this.captureDiscoveryEngineException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineName unique name of the discovery engine.
     * @param userId identifier of calling user
     * @param discoveryRequestType identifier of the type of asset to analyze - this determines which discovery service to run.
     * @param requestBody containing analysisParameters and annotationTypes
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid or
     *  UserNotAuthorizedException user not authorized to issue this request or
     *  DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public VoidResponse scanAllAssets(String                      serverName,
                                      String                      discoveryEngineName,
                                      String                      userId,
                                      String                      discoveryRequestType,
                                      DiscoveryRequestRequestBody requestBody)
    {
        final String  methodName = "scanAllAssets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            DiscoveryEngineHandler handler = instanceHandler.getDiscoveryEngineHandler(userId,
                                                                                       serverName,
                                                                                       discoveryEngineName,
                                                                                       methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
            else
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.scanAllAssets(discoveryRequestType,
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
        catch (DiscoveryEngineException error)
        {
            this.captureDiscoveryEngineException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DiscoveryAnalysisReportResponse getDiscoveryAnalysisReport(String   serverName,
                                                                      String   discoveryEngineName,
                                                                      String   userId,
                                                                      String   discoveryRequestGUID)
    {
        final String        methodName = "getDiscoveryAnalysisReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DiscoveryAnalysisReportResponse response = new DiscoveryAnalysisReportResponse();
        AuditLog                        auditLog = null;

        try
        {
            DiscoveryEngineHandler handler = instanceHandler.getDiscoveryEngineHandler(userId,
                                                                                       serverName,
                                                                                       discoveryEngineName,
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public AnnotationListResponse getDiscoveryReportAnnotations(String   serverName,
                                                                String   discoveryEngineName,
                                                                String   userId,
                                                                String   discoveryRequestGUID,
                                                                int      startingFrom,
                                                                int      maximumResults)
    {
        final String        methodName = "getDiscoveryReportAnnotations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AnnotationListResponse response = new AnnotationListResponse();
        AuditLog               auditLog = null;

        try
        {
            DiscoveryEngineHandler handler = instanceHandler.getDiscoveryEngineHandler(userId,
                                                                                       serverName,
                                                                                       discoveryEngineName,
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public AnnotationListResponse getExtendedAnnotations(String   serverName,
                                                         String   discoveryEngineName,
                                                         String   userId,
                                                         String   discoveryRequestGUID,
                                                         String   annotationGUID,
                                                         int      startingFrom,
                                                         int      maximumResults)
    {
        final String        methodName = "getExtendedAnnotations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AnnotationListResponse response = new AnnotationListResponse();
        AuditLog               auditLog = null;

        try
        {
            DiscoveryEngineHandler handler = instanceHandler.getDiscoveryEngineHandler(userId,
                                                                                       serverName,
                                                                                       discoveryEngineName,
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public AnnotationResponse getAnnotation(String   serverName,
                                            String   discoveryEngineName,
                                            String   userId,
                                            String   discoveryRequestGUID,
                                            String   annotationGUID)
    {
        final String        methodName = "getAnnotation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AnnotationResponse response = new AnnotationResponse();
        AuditLog           auditLog = null;

        try
        {
            DiscoveryEngineHandler handler = instanceHandler.getDiscoveryEngineHandler(userId,
                                                                                       serverName,
                                                                                       discoveryEngineName,
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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureDiscoveryEngineException(FFDCResponseBase         response,
                                                 DiscoveryEngineException error)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(PropertyServerException.class.getName());
        response.setExceptionErrorMessage(error.getReportedErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }
}
