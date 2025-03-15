/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SoftwareCapabilityElement;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.OCFConnectionResponse;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.AssetManagerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The AssetManagerRESTServices provides the server-side implementation of the services
 * that are generic for all types of asset managers.
 */
public class AssetManagerRESTServices
{
    private static final AssetManagerInstanceHandler instanceHandler = new AssetManagerInstanceHandler();
    private static final RESTCallLogger              restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(AssetManagerRESTServices.class),
                                                                                          instanceHandler.getServiceName());
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public AssetManagerRESTServices()
    {
    }


    /**
     * Return the client side connection object for the Asset Manager OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    public OCFConnectionResponse getOutTopicConnection(String serverName,
                                                       String userId,
                                                       String callerId)
    {
        final String methodName = "getOutTopicConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        OCFConnectionResponse response = new OCFConnectionResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(instanceHandler.getOutTopicConnection(userId, serverName, methodName, callerId));
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
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create information about the external asset manager.  This is represented as a software server capability
     * and all information that is specific to the external asset manager (such as the identifiers of the
     * metadata elements it stores) will be linked to it.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetManagerProperties description of the integration daemon (specify qualified name at a minimum)
     *
     * @return unique identifier of the asset management's software server capability or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse createExternalAssetManager(String                 serverName,
                                                   String                 userId,
                                                   AssetManagerProperties assetManagerProperties)
    {
        final String methodName = "createExternalAssetManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (assetManagerProperties != null)
            {
                Date effectiveTime = new Date();

                SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId,
                                                                                                                      serverName,
                                                                                                                      methodName);

                String softwareCapabilityGUID = handler.createSoftwareCapability(userId,
                                                                                 null,
                                                                                 null,
                                                                                 OpenMetadataType.INVENTORY_CATALOG.typeName,
                                                                                 OpenMetadataType.ASSET_MANAGER.typeName,
                                                                                 assetManagerProperties.getQualifiedName(),
                                                                                 assetManagerProperties.getResourceName(),
                                                                                 assetManagerProperties.getResourceDescription(),
                                                                                 assetManagerProperties.getDeployedImplementationType(),
                                                                                 assetManagerProperties.getVersion(),
                                                                                 assetManagerProperties.getPatchLevel(),
                                                                                 assetManagerProperties.getSource(),
                                                                                 assetManagerProperties.getAdditionalProperties(),
                                                                                 null,
                                                                                 assetManagerProperties.getVendorProperties(),
                                                                                 assetManagerProperties.getEffectiveFrom(),
                                                                                 assetManagerProperties.getEffectiveTo(),
                                                                                 false,
                                                                                 false,
                                                                                 effectiveTime,
                                                                                 methodName);

                final String softwareCapabilityGUIDParameterName = "softwareCapabilityGUID";

                handler.maintainSupplementaryProperties(userId,
                                                        softwareCapabilityGUID,
                                                        softwareCapabilityGUIDParameterName,
                                                        OpenMetadataType.INVENTORY_CATALOG.typeName,
                                                        OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                        assetManagerProperties.getQualifiedName(),
                                                        assetManagerProperties.getResourceName(),
                                                        assetManagerProperties.getDisplaySummary(),
                                                        assetManagerProperties.getResourceDescription(),
                                                        assetManagerProperties.getAbbreviation(),
                                                        assetManagerProperties.getUsage(),
                                                        false,
                                                        false,
                                                        false,
                                                        effectiveTime,
                                                        methodName);

                response.setGUID(softwareCapabilityGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
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
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the unique identifier of the external asset manager from its qualified name.
     * Typically, the qualified name comes from the integration connector configuration.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param qualifiedName unique name to use for the external asset
     *
     * @return unique identifier of the external asset manager's software server capability or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse  getExternalAssetManagerGUID(String serverName,
                                                     String userId,
                                                     String qualifiedName)
    {
        final String methodName = "getExternalAssetManagerGUID";
        final String nameParameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId,
                                                                                                                  serverName,
                                                                                                                  methodName);

            response.setGUID(handler.getBeanGUIDByQualifiedName(userId,
                                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeGUID,
                                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                qualifiedName,
                                                                nameParameterName,
                                                                false,
                                                                false,
                                                                new Date(),
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
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
