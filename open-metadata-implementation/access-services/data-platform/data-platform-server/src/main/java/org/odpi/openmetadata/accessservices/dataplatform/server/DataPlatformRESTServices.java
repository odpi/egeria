/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server;

import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapabilitiesProperties;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;

import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.SoftwareServerCapabilityHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SoftwareServerCapabilityMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.SoftwareServerCapability;
import org.slf4j.LoggerFactory;


/**
 * The DiscoveryConfigurationServices provides the server-side implementation of the configuration services
 * from the Open Discovery Framework (ODF).  These services configure discovery engines and discovery services.
 */
public class DataPlatformRESTServices
{
    private static DataPlatformInstanceHandler instanceHandler = new DataPlatformInstanceHandler();

    private static RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(DataPlatformRESTServices.class),
                                                                                  instanceHandler.getServiceName());
    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DataPlatformRESTServices()
    {
    }


    /**
     * Return the connection object for the Data Platform OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    public ConnectionResponse getOutTopicConnection(String serverName,
                                                    String userId)
    {
        final String methodName = "getOutTopicConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionResponse response = new ConnectionResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(instanceHandler.getOutTopicConnection(userId, serverName, methodName));
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
     * Create information about the integration daemon that is managing the acquisition of metadata from the
     * data platform.  Typically this is Egeria's data platform proxy.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param integratorCapabilities description of the integration daemon (specify qualified name at a minimum)
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse createDataPlatformIntegrator(String                               serverName,
                                                     String                               userId,
                                                     SoftwareServerCapabilitiesProperties integratorCapabilities)
    {
        final String methodName = "createDataPlatformIntegrator";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

            response.setGUID(handler.createMetadataIntegrator(userId,
                                                              integratorCapabilities.getQualifiedName(),
                                                              integratorCapabilities.getDisplayName(),
                                                              integratorCapabilities.getDescription(),
                                                              integratorCapabilities.getTypeDescription(),
                                                              integratorCapabilities.getVersion(),
                                                              integratorCapabilities.getPatchLevel(),
                                                              integratorCapabilities.getSource(),
                                                              integratorCapabilities.getAdditionalProperties(),
                                                              integratorCapabilities.getVendorProperties(),
                                                              SoftwareServerCapabilityMapper.DATA_PLATFORM_INTEGRATION_CLASSIFICATION_GUID,
                                                              SoftwareServerCapabilityMapper.DATA_PLATFORM_INTEGRATION_CLASSIFICATION_NAME,
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
     * Retrieve the unique identifier of the integration daemon.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param qualifiedName unique name of the integration daemon
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse  getDataPlatformIntegratorGUID(String serverName,
                                                       String userId,
                                                       String qualifiedName)
    {
        final String methodName = "createDataPlatformIntegrator";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

            SoftwareServerCapability capability = handler.getSoftwareServerCapabilityByUniqueName(userId, qualifiedName, methodName);

            if (capability != null)
            {
                response.setGUID(capability.getGUID());
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
}
