/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.assetanalysis.server;

import org.odpi.openmetadata.accessservices.discoveryengine.rest.AnnotationListResponse;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.AnnotationResponse;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.DiscoveryAnalysisReportResponse;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.DiscoveryRequestRequestBody;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorTypeResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.engineservices.assetanalysis.handlers.DiscoveryEngineHandler;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryService;
import org.slf4j.LoggerFactory;


/**
 * AssetAnalysisRESTServices provides the external service implementation for a discovery engine.
 * Each method contains the engine host server name and the discovery engine identifier (guid).
 * The AssetAnalysisRESTServices locates the correct discovery engine instance within the correct
 * engine host server instance and delegates the request.
 */
public class AssetAnalysisRESTServices
{
    private static AssetAnalysisInstanceHandler instanceHandler = new AssetAnalysisInstanceHandler();

    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(AssetAnalysisRESTServices.class),
                                                                      instanceHandler.getServiceName());
    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Validate the connector and return its connector type.
     *
     * @param serverName integration daemon server name
     * @param userId calling user
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector type or
     *
     *  InvalidParameterException the connector provider class name is not a valid connector fo this service
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException there was a problem detected by the integration service
     */
    public ConnectorTypeResponse validateConnector(String serverName,
                                                   String userId,
                                                   String connectorProviderClassName)
    {
        final String methodName = "validateConnector";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectorTypeResponse response = new ConnectorTypeResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setConnectorType(instanceHandler.validateConnector(connectorProviderClassName,
                                                                        DiscoveryService.class,
                                                                        EngineServiceDescription.ASSET_ANALYSIS_OMES.getEngineServiceFullName()));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param serverName name of the engine host server.
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param serverName name of the engine host server.
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public AnnotationListResponse getExtendedAnnotations(String   serverName,
                                                         String   discoveryEngineName,
                                                         String   userId,
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
            response.setAnnotations(handler.getExtendedAnnotations(annotationGUID, startingFrom, maximumResults));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public AnnotationResponse getAnnotation(String   serverName,
                                            String   discoveryEngineName,
                                            String   userId,
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
            response.setAnnotation(handler.getAnnotation(annotationGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
