/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.AssetHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers.DiscoveryAnalysisReportHandler;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The DiscoveryEngineServices provides the server-side implementation of the services used by the discovery
 * engine as it is managing requests to execute open discovery services in the discovery server.
 * These services align with the interface definitions from the Open Discovery Framework (ODF).
 */
public class DiscoveryEngineServices
{
    private static DiscoveryEngineServiceInstanceHandler instanceHandler = new DiscoveryEngineServiceInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(DiscoveryEngineServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public DiscoveryEngineServices()
    {
    }


    /**
     * Return the connection information for the asset.  This is used to create the connector.  The connector
     * is an Open Connector Framework (OCF) connector that provides access to the asset's data and metadata properties.
     *
     * @return Connection bean or
     * InvalidParameterException the asset guid is not recognized or
     * UserNotAuthorizedException the user is not authorized to access the asset and/or connection or
     * PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    protected ConnectionResponse getConnectionForAsset(String    serverName,
                                                       String    userId,
                                                       String    assetGUID)
    {
        final String   methodName = "getConnectionForAsset";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog       auditLog = null;
        ConnectionResponse response = new ConnectionResponse();

        try
        {
            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setConnection(handler.getConnectionForAsset(userId, assetGUID));
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
     * Create a new discovery analysis report and chain it to its asset, discovery engine and discovery service.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset being analysed
     * @param requestBody  all of the other parameters
     *
     * @return The new discovery report or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to access the asset and/or report or
     * PropertyServerException there was a problem in the store whether the asset/report properties are kept.
     */
    public DiscoveryAnalysisReportResponse createDiscoveryAnalysisReport(String                             serverName,
                                                                         String                             userId,
                                                                         String                             assetGUID,
                                                                         DiscoveryAnalysisReportRequestBody requestBody)
    {
        final String   methodName = "createDiscoveryAnalysisReport";

        log.debug("Calling method: " + methodName);

        OMRSAuditLog                    auditLog = null;
        DiscoveryAnalysisReportResponse response = new DiscoveryAnalysisReportResponse();

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

            response.setAnalysisReport(handler.createDiscoveryAnalysisReport(userId,
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
                                                                             requestBody.getClassifications()));
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
