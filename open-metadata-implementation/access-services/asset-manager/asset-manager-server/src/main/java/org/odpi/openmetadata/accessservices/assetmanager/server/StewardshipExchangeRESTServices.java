/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SoftwareCapabilityElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SecurityTagsProperties;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * AssetManagerRESTServices provides part of the server-side support for the Asset Owner Open Metadata Access Service (OMAS).
 * There are other REST services that provide specialized methods for specific types of Asset.
 */
public class StewardshipExchangeRESTServices
{
    private static final AssetManagerInstanceHandler instanceHandler      = new AssetManagerInstanceHandler();
    private static final RESTExceptionHandler        restExceptionHandler = new RESTExceptionHandler();
    private static final RESTCallLogger              restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(StewardshipExchangeRESTServices.class),
                                                                                               instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public StewardshipExchangeRESTServices()
    {
    }


    /**
     * Add or replace the security tags for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId      calling user
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addSecurityTags(String                 serverName,
                                        String                 userId,
                                        String                 elementGUID,
                                        SecurityTagsProperties requestBody)
    {
        final String methodName             = "addSecurityTags";
        final String assetGUIDParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

                handler.addSecurityTags(userId,
                                        elementGUID,
                                        assetGUIDParameterName,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                        requestBody.getSecurityLabels(),
                                        requestBody.getSecurityProperties(),
                                        requestBody.getAccessGroups(),
                                        false,
                                        false,
                                        new Date(),
                                        methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
     * Remove the security tags classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId      calling user
     * @param elementGUID   unique identifier of element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeSecurityTags(String          serverName,
                                           String          userId,
                                           String          elementGUID,
                                           NullRequestBody requestBody)
    {
        final String methodName             = "removeSecurityTags";
        final String assetGUIDParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

            handler.removeSecurityTags(userId,
                                       elementGUID,
                                       assetGUIDParameterName,
                                       OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                       false,
                                       false,
                                       new Date(),
                                       methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
