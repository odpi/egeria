/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;


import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataSourceElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataSourceProperties;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The DataManagerRESTServices provides the server-side implementation of the services
 * that are generic for all types of data managers.
 */
public class CommunityProfileRESTServices
{
    private static final CommunityProfileInstanceHandler instanceHandler = new CommunityProfileInstanceHandler();

    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(CommunityProfileRESTServices.class),
                                                                                  instanceHandler.getServiceName());
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public CommunityProfileRESTServices()
    {
    }


    /**
     * Return the connection object for the Community Profile OMAS's out topic.
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }




    /* ========================================================
     * The metadata source represents the third party technology this integration processing is connecting to
     */

    /**
     * Create information about the metadata source that is providing user profile information.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody description of the metadata source
     *
     * @return unique identifier of the user profile manager's software server capability or
     * InvalidParameterException  the bean requestBody are invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse createMetadataSource(String                   serverName,
                                             String                   userId,
                                             MetadataSourceProperties requestBody)
    {
        final String methodName = "createMetadataSource";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareCapabilityHandler<MetadataSourceElement> handler = instanceHandler.getMetadataSourceHandler(userId, serverName, methodName);

                response.setGUID(handler.createSoftwareCapability(userId,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  requestBody.getQualifiedName(),
                                                                  requestBody.getDisplayName(),
                                                                  requestBody.getDescription(),
                                                                  requestBody.getDeployedImplementationType(),
                                                                  requestBody.getVersion(),
                                                                  requestBody.getPatchLevel(),
                                                                  requestBody.getSource(),
                                                                  requestBody.getAdditionalProperties(),
                                                                  null,
                                                                  requestBody.getVendorProperties(),
                                                                  null,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the unique identifier of the software server capability that describes a metadata source.  This could be
     * a user profile manager, user access directory and/or a master data manager.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param qualifiedName unique name of the metadata source
     *
     * @return unique identifier of the integration daemon's software server capability or
     *  InvalidParameterException  the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException    problem accessing the property server
     */
    public GUIDResponse getMetadataSourceGUID(String serverName,
                                              String userId,
                                              String qualifiedName)
    {
        final String methodName = "getMetadataSourceGUID";
        final String parameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<MetadataSourceElement> handler = instanceHandler.getMetadataSourceHandler(userId, serverName, methodName);

            response.setGUID(handler.getBeanGUIDByQualifiedName(userId,
                                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeGUID,
                                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                qualifiedName,
                                                                parameterName,
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the properties of the software server capability that describes a metadata source.  This could be
     * a user profile manager, user access directory and/or a master data manager.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     *
     * @return unique identifier of the integration daemon's software server capability or
     *  InvalidParameterException  the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException    problem accessing the property server
     */
    public MetadataSourceResponse getMetadataSource(String serverName,
                                                    String userId,
                                                    String metadataSourceGUID)
    {
        final String methodName                       = "getMetadataSource";
        final String metadataSourceGUIDParameterName  = "metadataSourceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        MetadataSourceResponse response = new MetadataSourceResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<MetadataSourceElement> handler = instanceHandler.getMetadataSourceHandler(userId, serverName, methodName);

            response.setElement(handler.getBeanFromRepository(userId,
                                                              metadataSourceGUID,
                                                              metadataSourceGUIDParameterName,
                                                              OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update classification of the metadata source as being capable if managing user profiles.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException    problem accessing the property server
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse addUserProfileManagerClassification(String          serverName,
                                                            String          userId,
                                                            String          metadataSourceGUID,
                                                            NullRequestBody requestBody)
    {
        final String methodName                       = "addUserProfileManagerClassification";
        final String metadataSourceGUIDParameterName  = "metadataSourceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<MetadataSourceElement> handler = instanceHandler.getMetadataSourceHandler(userId, serverName, methodName);

            handler.addSoftwareCapabilityClassification(userId,
                                                        null,
                                                        null,
                                                        metadataSourceGUID,
                                                        metadataSourceGUIDParameterName,
                                                        OpenMetadataType.USER_PROFILE_MANAGER.typeName,
                                                        null,
                                                        null,
                                                        true,
                                                        false,
                                                        false,
                                                        new Date(),
                                                        methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update classification of the metadata source that is providing a user access directory information
     * such as the groups and access rights of a user id.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException    problem accessing the property server
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse addUserAccessDirectoryClassification(String          serverName,
                                                             String          userId,
                                                             String          metadataSourceGUID,
                                                             NullRequestBody requestBody)
    {
        final String methodName                       = "addUserAccessDirectoryClassification";
        final String metadataSourceGUIDParameterName  = "metadataSourceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<MetadataSourceElement> handler = instanceHandler.getMetadataSourceHandler(userId, serverName, methodName);

            handler.addSoftwareCapabilityClassification(userId,
                                                        null,
                                                        null,
                                                        metadataSourceGUID,
                                                        metadataSourceGUIDParameterName,
                                                        OpenMetadataType.USER_ACCESS_DIRECTORY.typeName,
                                                        null,
                                                        null,
                                                        true,
                                                        false,
                                                        false,
                                                        new Date(),
                                                        methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update classification of the metadata source that is a master data manager for user profile information.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param metadataSourceGUID unique identifier of the metadata source
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  the bean properties are invalid
     *  UserNotAuthorizedException user not authorized to issue this request
     *  PropertyServerException    problem accessing the property server
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse addMasterDataManagerClassification(String          serverName,
                                                           String          userId,
                                                           String          metadataSourceGUID,
                                                           NullRequestBody requestBody)
    {
        final String methodName                       = "addMasterDataManagerClassification";
        final String metadataSourceGUIDParameterName  = "metadataSourceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<MetadataSourceElement> handler = instanceHandler.getMetadataSourceHandler(userId, serverName, methodName);

            handler.addSoftwareCapabilityClassification(userId,
                                                        null,
                                                        null,
                                                        metadataSourceGUID,
                                                        metadataSourceGUIDParameterName,
                                                        OpenMetadataType.MASTER_DATA_MANAGER.typeName,
                                                        null,
                                                        null,
                                                        true,
                                                        false,
                                                        false,
                                                        new Date(),
                                                        methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
