/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.server;

import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineAuditCode;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.AssetHandler;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers.AnnotationHandler;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers.DiscoveryAnalysisReportHandler;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.AnnotationStatus;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.slf4j.LoggerFactory;


/**
 * The DiscoveryEngineRESTServices provides the server-side implementation of the services used by the discovery
 * engine as it is managing requests to execute open discovery services in the discovery server.
 * These services align with the interface definitions from the Open Discovery Framework (ODF).
 */
public class DiscoveryEngineRESTServices
{
    private static DiscoveryEngineServiceInstanceHandler instanceHandler = new DiscoveryEngineServiceInstanceHandler();

    private        RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private static RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(DiscoveryEngineRESTServices.class),
                                                                                  instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public DiscoveryEngineRESTServices()
    {
    }


    /**
     * Return the next set of assets to process.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param startFrom starting point of the query
     * @param pageSize maximum number of results to return
     * @param requestBody null request body
     * @return list of unique identifiers for located assets or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public GUIDListResponse getAssets(String          serverName,
                                      String          userId,
                                      int             startFrom,
                                      int             pageSize,
                                      NullRequestBody requestBody)
    {
        final String   methodName = "getAssets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog         auditLog = null;
        GUIDListResponse response = new GUIDListResponse();

        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGUIDs(handler.assetGUIDsScan(userId, null, null, startFrom, pageSize, methodName));
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
     * Return the assets with the same qualified name.  If all is well there should be only one
     * returned.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param name the qualified name to query on
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @return list of unique identifiers for matching assets or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public GUIDListResponse  getAssetsByQualifiedName(String   serverName,
                                                      String   userId,
                                                      String   name,
                                                      int      startFrom,
                                                      int      pageSize)
    {
        final String   methodName = "getAssetsByQualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog         auditLog = null;
        GUIDListResponse response = new GUIDListResponse();

        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGUIDs(handler.getAssetGUIDsByQualifiedName(userId,
                                                                   name,
                                                                   startFrom,
                                                                   pageSize,
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
     * Return the list of matching assets that have the supplied name as either the
     * qualified name or display name.  This is an exact match retrieval.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param name name to query for
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @return list of unique identifiers for matching assets or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public GUIDListResponse  getAssetsByName(String   serverName,
                                             String   userId,
                                             String   name,
                                             int      startFrom,
                                             int      pageSize)
    {
        final String   methodName = "getAssetsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog         auditLog = null;
        GUIDListResponse response = new GUIDListResponse();

        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGUIDs(handler.getAssetGUIDsByName(userId,
                                                          name,
                                                          startFrom,
                                                          pageSize,
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
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public GUIDListResponse  findAssets(String   serverName,
                                        String   userId,
                                        String   searchString,
                                        int      startFrom,
                                        int      pageSize)
    {
        final String   methodName = "findAssets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog         auditLog = null;
        GUIDListResponse response = new GUIDListResponse();

        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGUIDs(handler.findAssetGUIDs(userId,
                                                     searchString,
                                                     startFrom,
                                                     pageSize,
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
     * Return the list of assets that have the same endpoint address.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param networkAddress address to query on
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @return list of unique identifiers for matching assets or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public  GUIDListResponse getAssetsByEndpoint(String   serverName,
                                                 String   userId,
                                                 String   networkAddress,
                                                 int      startFrom,
                                                 int      pageSize)
    {
        final String   methodName = "findAssetsByEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog         auditLog = null;
        GUIDListResponse response = new GUIDListResponse();

        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGUIDs(handler.getAssetGUIDsByEndpoint(userId,
                                                              networkAddress,
                                                              startFrom,
                                                              pageSize,
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
     * Log an audit message about this asset.
     *
     * @param serverName     name of server instance to route request to
     * @param userId         userId of user making request.
     * @param assetGUID      unique identifier for asset.
     * @param discoveryService name of discovery service
     * @param message        message to log
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public VoidResponse logAssetAuditMessage(String    serverName,
                                             String    userId,
                                             String    assetGUID,
                                             String    discoveryService,
                                             String    message)
    {
        final String   methodName = "logAssetAuditMessage";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            auditLog.logMessage(methodName, DiscoveryEngineAuditCode.ASSET_AUDIT_LOG.getMessageDefinition(assetGUID, discoveryService, message));
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
     * Create a new discovery analysis report and chain it to its asset, discovery engine and discovery service.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset being analysed
     * @param requestBody  all of the other parameters
     *
     * @return The unique identifier of the new discovery report or
     *
     *  InvalidParameterException one of the parameters is invalid or
     *  UserNotAuthorizedException the user is not authorized to access the asset and/or report or
     *  PropertyServerException there was a problem in the store whether the asset/report properties are kept.
     */
    public GUIDResponse createDiscoveryAnalysisReport(String                             serverName,
                                                      String                             userId,
                                                      String                             assetGUID,
                                                      DiscoveryAnalysisReportRequestBody requestBody)
    {
        final String   methodName = "createDiscoveryAnalysisReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog     auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
            else
            {
                DiscoveryAnalysisReportHandler handler = instanceHandler.getDiscoveryAnalysisReportHandler(userId,
                                                                                                           serverName,
                                                                                                           methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                response.setGUID(handler.createDiscoveryAnalysisReport(userId,
                                                                       requestBody.getQualifiedName(),
                                                                       requestBody.getDisplayName(),
                                                                       requestBody.getDescription(),
                                                                       requestBody.getCreationDate(),
                                                                       requestBody.getAnalysisParameters(),
                                                                       requestBody.getDiscoveryRequestStatus(),
                                                                       assetGUID,
                                                                       requestBody.getDiscoveryEngineGUID(),
                                                                       requestBody.getDiscoveryServiceGUID(),
                                                                       requestBody.getAdditionalProperties(),
                                                                       methodName));
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

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the properties of the discovery analysis report.
     *
     * @param serverName name of server instance to route request to
     * @param userId calling user
     * @param discoveryReportGUID unique identifier of the report to update
     * @param requestBody updated report - this will replace what was previous stored
     *
     * @return  void or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public VoidResponse updateDiscoveryAnalysisReport(String                  serverName,
                                                      String                  userId,
                                                      String                  discoveryReportGUID,
                                                      DiscoveryAnalysisReport requestBody)
    {
        final String   methodName = "updateDiscoveryAnalysisReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog     auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }

            DiscoveryAnalysisReportHandler handler = instanceHandler.getDiscoveryAnalysisReportHandler(userId,
                                                                                                       serverName,
                                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.updateDiscoveryAnalysisReport(userId, discoveryReportGUID, requestBody);
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
     * Request the discovery report for a discovery request that has completed.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     *
     * @return discovery report or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public DiscoveryAnalysisReportResponse getDiscoveryAnalysisReport(String   serverName,
                                                                      String   userId,
                                                                      String   discoveryReportGUID)
    {
        final String   methodName = "getDiscoveryAnalysisReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog                        auditLog = null;
        DiscoveryAnalysisReportResponse response = new DiscoveryAnalysisReportResponse();

        try
        {
            DiscoveryAnalysisReportHandler handler = instanceHandler.getDiscoveryAnalysisReportHandler(userId,
                                                                                                       serverName,
                                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setAnalysisReport(handler.getDiscoveryAnalysisReport(userId,
                                                                          discoveryReportGUID,
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
     * Return the annotation subtype names.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @return list of type names that are subtypes of annotation or
     * throws InvalidParameterException full path or userId is null or
     * throws PropertyServerException problem accessing property server or
     * throws UserNotAuthorizedException security access problem.
     */
    public NameListResponse getTypesOfAnnotation(String serverName,
                                                 String userId)
    {
        final String   methodName = "getTypesOfAnnotation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NameListResponse response = new NameListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            response.setNames(handler.getTypesOfAnnotation());
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
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
    public StringMapResponse getTypesOfAnnotationWithDescriptions(String serverName, String userId)
    {
        final String   methodName = "getTypesOfAnnotationWithDescriptions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        StringMapResponse response = new StringMapResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId, serverName, methodName);

            response.setStringMap(handler.getTypesOfAnnotationDescriptions());
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
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
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    public AnnotationListResponse getAnnotationsForAssetByStatus(String            serverName,
                                                                 String            userId,
                                                                 String            assetGUID,
                                                                 int               startingFrom,
                                                                 int               maximumResults,
                                                                 StatusRequestBody requestBody)
    {
        final String   methodName = "getAnnotationsForAssetByStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog               auditLog = null;
        AnnotationListResponse response = new AnnotationListResponse();
        AnnotationStatus       status   = null;

        if (requestBody != null)
        {
            status = requestBody.getAnnotationStatus();
        }

        try
        {
            DiscoveryAnalysisReportHandler handler = instanceHandler.getDiscoveryAnalysisReportHandler(userId,
                                                                                                       serverName,
                                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setAnnotations(handler.getAnnotationsForAssetByStatus(userId,
                                                                           assetGUID,
                                                                           status,
                                                                           startingFrom,
                                                                           maximumResults,
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
     * Return the annotations linked directly to the report.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public AnnotationListResponse getDiscoveryReportAnnotations(String   serverName,
                                                                String   userId,
                                                                String   discoveryReportGUID,
                                                                int      startingFrom,
                                                                int      maximumResults)
    {
        final String   methodName = "getDiscoveryReportAnnotations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog               auditLog = null;
        AnnotationListResponse response = new AnnotationListResponse();

        try
        {
            DiscoveryAnalysisReportHandler handler = instanceHandler.getDiscoveryAnalysisReportHandler(userId,
                                                                                                       serverName,
                                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setAnnotations(handler.getDiscoveryReportAnnotations(userId,
                                                                          discoveryReportGUID,
                                                                          null,
                                                                          startingFrom,
                                                                          maximumResults,
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
     * Return any annotations attached to this annotation.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public  AnnotationListResponse  getExtendedAnnotations(String   serverName,
                                                           String   userId,
                                                           String   annotationGUID,
                                                           int      startingFrom,
                                                           int      maximumResults)
    {
        final String   methodName = "getExtendedAnnotations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog               auditLog = null;
        AnnotationListResponse response = new AnnotationListResponse();

        try
        {
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId,
                                                                             serverName,
                                                                             methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setAnnotations(handler.getExtendedAnnotations(userId,
                                                                   annotationGUID,
                                                                   null,
                                                                   startingFrom,
                                                                   maximumResults,
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
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    public  AnnotationResponse getAnnotation(String   serverName,
                                             String   userId,
                                             String   annotationGUID)
    {
        final String   methodName = "getAnnotation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog           auditLog = null;
        AnnotationResponse response = new AnnotationResponse();

        try
        {
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId,
                                                                             serverName,
                                                                             methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setAnnotation(handler.getAnnotation(userId,
                                                         annotationGUID,
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
     * Add a new annotation to the annotation store as a top level annotation linked directly off of the report.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param discoveryReportGUID unique identifier of the discovery analysis report
     * @param requestBody annotation object
     *
     * @return unique identifier of new annotation or
     *
     *  InvalidParameterException the annotation is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem retrieving adding the annotation to the annotation store.
     */
    public  GUIDResponse  addAnnotationToDiscoveryReport(String     serverName,
                                                         String     userId,
                                                         String     discoveryReportGUID,
                                                         Annotation requestBody)
    {
        final String   methodName = "addAnnotationToDiscoveryReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog     auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }

            DiscoveryAnalysisReportHandler handler = instanceHandler.getDiscoveryAnalysisReportHandler(userId,
                                                                                                       serverName,
                                                                                                       methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGUID(handler.addAnnotationToDiscoveryReport(userId,
                                                                    discoveryReportGUID,
                                                                    requestBody,
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
     * Add a new annotation and link it to an existing annotation.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param anchorAnnotationGUID unique identifier of the annotation that this new one os to be attached to
     * @param requestBody annotation object
     *
     * @return unique identifier of new annotation or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem saving annotations in the annotation store.
     */
    public  GUIDResponse  addAnnotationToAnnotation(String     serverName,
                                                    String     userId,
                                                    String     anchorAnnotationGUID,
                                                    Annotation requestBody)
    {
        final String   methodName = "addAnnotationToAnnotation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog     auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId,
                                                                             serverName,
                                                                             methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setGUID(handler.addAnnotationToAnnotation(userId,
                                                               anchorAnnotationGUID,
                                                               requestBody,
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
     * Replace the current properties of an annotation.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param annotationGUID identifier of the annotation to change
     * @param requestBody new properties
     *
     * @return fully filled out annotation or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem updating the annotation in the annotation store.
     */
    public VoidResponse  updateAnnotation(String     serverName,
                                          String     userId,
                                          String     annotationGUID,
                                          Annotation requestBody)
    {
        final String   methodName = "updateAnnotation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog     auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId,
                                                                             serverName,
                                                                             methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.updateAnnotation(userId, annotationGUID, requestBody, methodName);
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
     * Remove an annotation from the annotation store.
     *
     * @param serverName name of server instance to route request to
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation
     * @param requestBody null request body to satisfy POST semantics
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the user id not authorized to issue this request
     *  PropertyServerException there was a problem deleting the annotation from the annotation store.
     */
    public VoidResponse  deleteAnnotation(String          serverName, 
                                          String          userId, 
                                          String          annotationGUID, 
                                          NullRequestBody requestBody)
    {
        final String   methodName = "deleteAnnotation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog     auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            AnnotationHandler handler = instanceHandler.getAnnotationHandler(userId,
                                                                             serverName,
                                                                             methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.deleteAnnotation(userId, annotationGUID, methodName);
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
}
