/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.devopspipeline.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.NetworkHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.OperatingPlatformHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.StorageVolumeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.HostClusterMemberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.NetworkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.OperatingPlatformManifestProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.OperatingPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.OperatingPlatformUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwarePackageDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwarePackageManifestProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.NetworkGatewayLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.NetworkGatewayProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.VisibleEndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.AttachedStorageProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.StorageVolumeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.StoredOnProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms.CloudPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms.CloudProviderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms.CloudTenantProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.operatingplatforms.CloudServiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SoftwareCapabilityHandler;



/**
 * The DevopsPipelineRESTServices provides the server-side implementation of the Devops Pipeline Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class DevopsPipelineRESTServices extends TokenController
{
    private static final DevopsPipelineInstanceHandler instanceHandler = new DevopsPipelineInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(DevopsPipelineRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public DevopsPipelineRESTServices()
    {
    }


    /**
     * Create a storage volume.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return unique identifier of the newly created element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createStorageVolume(String                serverName,
                                            NewElementRequestBody requestBody)
    {
        final String methodName = "createStorageVolume";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            StorageVolumeHandler handler = instanceHandler.getStorageVolumeHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof StorageVolumeProperties storageVolumeProperties)
                {
                    response.setGUID(handler.createStorageVolume(userId,
                                                                 requestBody,
                                                                 requestBody.getInitialClassifications(),
                                                                 storageVolumeProperties,
                                                                 requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(StorageVolumeProperties.class.getName(), methodName);
                }
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a new metadata element to represent a storage volume using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return unique identifier of the newly created element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createStorageVolumeFromTemplate(String              serverName,
                                                        TemplateRequestBody requestBody)
    {
        final String methodName = "createStorageVolumeFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            StorageVolumeHandler handler = instanceHandler.getStorageVolumeHandler(userId, serverName, methodName);

                response.setGUID(handler.createStorageVolumeFromTemplate(userId,
                                                                         requestBody,
                                                                         requestBody.getTemplateGUID(),
                                                                         requestBody.getReplacementProperties(),
                                                                         requestBody.getReplacementClassifications(),
                                                                         requestBody.getPlaceholderPropertyValues(),
                                                                         requestBody.getParentRelationshipProperties()));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a storage volume.
     *
     * @param serverName name of the server to route the request to
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody properties for the request
     *
     * @return boolean - true if an update occurred or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public BooleanResponse updateStorageVolume(String                   serverName,
                                               String                   storageVolumeGUID,
                                               UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateStorageVolume";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            StorageVolumeHandler handler = instanceHandler.getStorageVolumeHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof StorageVolumeProperties storageVolumeProperties)
                {
                    response.setFlag(handler.updateStorageVolume(userId, storageVolumeGUID, requestBody, storageVolumeProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(StorageVolumeProperties.class.getName(), methodName);
                }
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Delete a storage volume.
     *
     * @param serverName name of the server to route the request to
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse deleteStorageVolume(String                   serverName,
                                            String                   storageVolumeGUID,
                                            DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteStorageVolume";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StorageVolumeHandler handler = instanceHandler.getStorageVolumeHandler(userId, serverName, methodName);

            handler.deleteStorageVolume(userId, storageVolumeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Returns the list of storage volumes with a particular name.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return a list of elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getStorageVolumesByName(String            serverName,
                                                                    FilterRequestBody requestBody)
    {
        final String methodName = "getStorageVolumesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StorageVolumeHandler handler = instanceHandler.getStorageVolumeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getStorageVolumesByName(userId, requestBody.getFilter(), requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of storage volume metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return a list of elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findStorageVolumes(String                  serverName,
                                                               SearchStringRequestBody requestBody)
    {
        final String methodName = "findStorageVolumes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StorageVolumeHandler handler = instanceHandler.getStorageVolumeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findStorageVolumes(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findStorageVolumes(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Return the properties of a specific storage volume.
     *
     * @param serverName name of the server to route the request to
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody properties for the request
     *
     * @return matching element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getStorageVolumeByGUID(String         serverName,
                                                                  String         storageVolumeGUID,
                                                                  GetRequestBody requestBody)
    {
        final String methodName = "getStorageVolumeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StorageVolumeHandler handler = instanceHandler.getStorageVolumeHandler(userId, serverName, methodName);

            response.setElement(handler.getStorageVolumeByGUID(userId, storageVolumeGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach a storage volume to the IT infrastructure that it provides storage for.
     *
     * @param serverName name of the server to route the request to
     * @param itInfrastructureGUID unique identifier of the element that the storage volume is attached to
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkAttachedStorage(String                     serverName,
                                            String                     itInfrastructureGUID,
                                            String                     storageVolumeGUID,
                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAttachedStorage";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StorageVolumeHandler handler = instanceHandler.getStorageVolumeHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.linkAttachedStorage(userId, itInfrastructureGUID, storageVolumeGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof AttachedStorageProperties properties)
            {
                handler.linkAttachedStorage(userId, itInfrastructureGUID, storageVolumeGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkAttachedStorage(userId, itInfrastructureGUID, storageVolumeGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(AttachedStorageProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach a storage volume from the IT infrastructure that it provided storage for.
     *
     * @param serverName name of the server to route the request to
     * @param itInfrastructureGUID unique identifier of the element that the storage volume is attached to
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachAttachedStorage(String                        serverName,
                                              String                        itInfrastructureGUID,
                                              String                        storageVolumeGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachAttachedStorage";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StorageVolumeHandler handler = instanceHandler.getStorageVolumeHandler(userId, serverName, methodName);

            handler.detachAttachedStorage(userId, itInfrastructureGUID, storageVolumeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach a data store to the storage volume that its data is stored on.
     *
     * @param serverName name of the server to route the request to
     * @param dataStoreGUID unique identifier of the data store
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkStoredOn(String                     serverName,
                                     String                     dataStoreGUID,
                                     String                     storageVolumeGUID,
                                     NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkStoredOn";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StorageVolumeHandler handler = instanceHandler.getStorageVolumeHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.linkStoredOn(userId, dataStoreGUID, storageVolumeGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof StoredOnProperties properties)
            {
                handler.linkStoredOn(userId, dataStoreGUID, storageVolumeGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkStoredOn(userId, dataStoreGUID, storageVolumeGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(StoredOnProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach a data store from the storage volume that its data was stored on.
     *
     * @param serverName name of the server to route the request to
     * @param dataStoreGUID unique identifier of the data store
     * @param storageVolumeGUID unique identifier of the storage volume
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachStoredOn(String                        serverName,
                                       String                        dataStoreGUID,
                                       String                        storageVolumeGUID,
                                       DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachStoredOn";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StorageVolumeHandler handler = instanceHandler.getStorageVolumeHandler(userId, serverName, methodName);

            handler.detachStoredOn(userId, dataStoreGUID, storageVolumeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Networks
     */
    /**
     * Create a network.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return unique identifier of the newly created element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createNetwork(String                serverName,
                                      NewElementRequestBody requestBody)
    {
        final String methodName = "createNetwork";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof NetworkProperties networkProperties)
                {
                    response.setGUID(handler.createNetwork(userId,
                                                         requestBody,
                                                         requestBody.getInitialClassifications(),
                                                         networkProperties,
                                                         requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NetworkProperties.class.getName(), methodName);
                }
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Create a new metadata element to represent a network using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return unique identifier of the newly created element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createNetworkFromTemplate(String              serverName,
                                                  TemplateRequestBody requestBody)
    {
        final String methodName = "createNetworkFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

                response.setGUID(handler.createNetworkFromTemplate(userId,
                                                                 requestBody,
                                                                 requestBody.getTemplateGUID(),
                                                                 requestBody.getReplacementProperties(),
                                                                 requestBody.getReplacementClassifications(),
                                                                 requestBody.getPlaceholderPropertyValues(),
                                                                 requestBody.getParentRelationshipProperties()));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Update the properties of a network.
     *
     * @param serverName name of the server to route the request to
     * @param networkGUID unique identifier of the network
     * @param requestBody properties for the request
     *
     * @return boolean - true if an update occurred or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public BooleanResponse updateNetwork(String                   serverName,
                                         String                   networkGUID,
                                         UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateNetwork";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof NetworkProperties networkProperties)
                {
                    response.setFlag(handler.updateNetwork(userId, networkGUID, requestBody, networkProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NetworkProperties.class.getName(), methodName);
                }
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Delete a network.
     *
     * @param serverName name of the server to route the request to
     * @param networkGUID unique identifier of the network
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse deleteNetwork(String                   serverName,
                                      String                   networkGUID,
                                      DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteNetwork";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

            handler.deleteNetwork(userId, networkGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Returns the list of networks with a particular name.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return a list of elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getNetworksByName(String            serverName,
                                                              FilterRequestBody requestBody)
    {
        final String methodName = "getNetworksByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getNetworksByName(userId, requestBody.getFilter(), requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Retrieve the list of network metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return a list of elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findNetworks(String                  serverName,
                                                         SearchStringRequestBody requestBody)
    {
        final String methodName = "findNetworks";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findNetworks(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findNetworks(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Return the properties of a specific network.
     *
     * @param serverName name of the server to route the request to
     * @param networkGUID unique identifier of the network
     * @param requestBody properties for the request
     *
     * @return matching element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getNetworkByGUID(String         serverName,
                                                            String         networkGUID,
                                                            GetRequestBody requestBody)
    {
        final String methodName = "getNetworkByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

            response.setElement(handler.getNetworkByGUID(userId, networkGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Network gateways
     */
    /**
     * Create a network gateway.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return unique identifier of the newly created element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createNetworkGateway(String                serverName,
                                             NewElementRequestBody requestBody)
    {
        final String methodName = "createNetworkGateway";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof NetworkGatewayProperties networkGatewayProperties)
                {
                    response.setGUID(handler.createNetworkGateway(userId,
                                                         requestBody,
                                                         requestBody.getInitialClassifications(),
                                                         networkGatewayProperties,
                                                         requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NetworkGatewayProperties.class.getName(), methodName);
                }
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Create a new metadata element to represent a network gateway using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return unique identifier of the newly created element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createNetworkGatewayFromTemplate(String              serverName,
                                                         TemplateRequestBody requestBody)
    {
        final String methodName = "createNetworkGatewayFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

                response.setGUID(handler.createNetworkGatewayFromTemplate(userId,
                                                                 requestBody,
                                                                 requestBody.getTemplateGUID(),
                                                                 requestBody.getReplacementProperties(),
                                                                 requestBody.getReplacementClassifications(),
                                                                 requestBody.getPlaceholderPropertyValues(),
                                                                 requestBody.getParentRelationshipProperties()));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Update the properties of a network gateway.
     *
     * @param serverName name of the server to route the request to
     * @param networkGatewayGUID unique identifier of the network gateway
     * @param requestBody properties for the request
     *
     * @return boolean - true if an update occurred or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public BooleanResponse updateNetworkGateway(String                   serverName,
                                                String                   networkGatewayGUID,
                                                UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateNetworkGateway";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof NetworkGatewayProperties networkGatewayProperties)
                {
                    response.setFlag(handler.updateNetworkGateway(userId, networkGatewayGUID, requestBody, networkGatewayProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NetworkGatewayProperties.class.getName(), methodName);
                }
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Delete a network gateway.
     *
     * @param serverName name of the server to route the request to
     * @param networkGatewayGUID unique identifier of the network gateway
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse deleteNetworkGateway(String                   serverName,
                                             String                   networkGatewayGUID,
                                             DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteNetworkGateway";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

            handler.deleteNetworkGateway(userId, networkGatewayGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Returns the list of network gateways with a particular name.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return a list of elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getNetworkGatewaysByName(String            serverName,
                                                                     FilterRequestBody requestBody)
    {
        final String methodName = "getNetworkGatewaysByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getNetworkGatewaysByName(userId, requestBody.getFilter(), requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Retrieve the list of network gateway metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return a list of elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findNetworkGateways(String                  serverName,
                                                                SearchStringRequestBody requestBody)
    {
        final String methodName = "findNetworkGateways";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findNetworkGateways(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findNetworkGateways(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Return the properties of a specific network gateway.
     *
     * @param serverName name of the server to route the request to
     * @param networkGatewayGUID unique identifier of the network gateway
     * @param requestBody properties for the request
     *
     * @return matching element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getNetworkGatewayByGUID(String         serverName,
                                                                   String         networkGatewayGUID,
                                                                   GetRequestBody requestBody)
    {
        final String methodName = "getNetworkGatewayByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

            response.setElement(handler.getNetworkGatewayByGUID(userId, networkGatewayGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Network relationships
     */

    /**
     * Attach an endpoint to the network that it is visible in.
     *
     * @param serverName name of the server to route the request to
     * @param endpointGUID unique identifier of the endpoint
     * @param networkGUID unique identifier of the network
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkVisibleEndpoint(String                     serverName,
                                            String                     endpointGUID,
                                            String                     networkGUID,
                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkVisibleEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.linkVisibleEndpoint(userId, endpointGUID, networkGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof VisibleEndpointProperties properties)
            {
                handler.linkVisibleEndpoint(userId, endpointGUID, networkGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkVisibleEndpoint(userId, endpointGUID, networkGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(VisibleEndpointProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach an endpoint from the network that it was visible in.
     *
     * @param serverName name of the server to route the request to
     * @param endpointGUID unique identifier of the endpoint
     * @param networkGUID unique identifier of the network
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachVisibleEndpoint(String                        serverName,
                                              String                        endpointGUID,
                                              String                        networkGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachVisibleEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

            handler.detachVisibleEndpoint(userId, endpointGUID, networkGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach a network gateway to a network that it connects to.  NetworkGatewayLink is a multi-link relationship
     * so this always creates a new relationship and returns its unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param networkGatewayGUID unique identifier of the network gateway
     * @param networkGUID unique identifier of the network
     * @param requestBody properties for the relationship
     *
     * @return unique identifier of the new relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse linkNetworkGateway(String                     serverName,
                                           String                     networkGatewayGUID,
                                           String                     networkGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkNetworkGateway";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof NetworkGatewayLinkProperties properties)
                {
                    response.setGUID(handler.linkNetworkGateway(userId, networkGatewayGUID, networkGUID, requestBody, properties));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.linkNetworkGateway(userId, networkGatewayGUID, networkGUID, requestBody, null));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NetworkGatewayLinkProperties.class.getName(), methodName);
                }
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a network gateway link.
     *
     * @param serverName name of the server to route the request to
     * @param networkGatewayLinkGUID unique identifier of the network gateway link relationship
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse updateNetworkGatewayLink(String                        serverName,
                                                 String                        networkGatewayLinkGUID,
                                                 UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateNetworkGatewayLink";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof NetworkGatewayLinkProperties properties)
                {
                    handler.updateNetworkGatewayLink(userId, networkGatewayLinkGUID, requestBody, properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NetworkGatewayLinkProperties.class.getName(), methodName);
                }
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach a network gateway from a network that it connected to.
     *
     * @param serverName name of the server to route the request to
     * @param networkGatewayLinkGUID unique identifier of the network gateway link relationship
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachNetworkGateway(String                        serverName,
                                             String                        networkGatewayLinkGUID,
                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachNetworkGateway";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NetworkHandler handler = instanceHandler.getNetworkHandler(userId, serverName, methodName);

            handler.detachNetworkGateway(userId, networkGatewayLinkGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Host clusters
     */

    /**
     * Attach a host to the host cluster that manages it.
     *
     * @param serverName name of the server to route the request to
     * @param hostClusterGUID unique identifier of the host cluster
     * @param hostGUID unique identifier of the host that is managed by the cluster
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkHostClusterMember(String                     serverName,
                                              String                     hostClusterGUID,
                                              String                     hostGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkHostClusterMember";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.linkHostClusterMember(userId, hostClusterGUID, hostGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof HostClusterMemberProperties properties)
            {
                handler.linkHostClusterMember(userId, hostClusterGUID, hostGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkHostClusterMember(userId, hostClusterGUID, hostGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(HostClusterMemberProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach a host from the host cluster that managed it.
     *
     * @param serverName name of the server to route the request to
     * @param hostClusterGUID unique identifier of the host cluster
     * @param hostGUID unique identifier of the host that was managed by the cluster
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachHostClusterMember(String                        serverName,
                                                String                        hostClusterGUID,
                                                String                        hostGUID,
                                                DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachHostClusterMember";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.detachHostClusterMember(userId, hostClusterGUID, hostGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Operating platforms
     */
    /**
     * Create a operating platform.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return unique identifier of the newly created element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createOperatingPlatform(String                serverName,
                                                NewElementRequestBody requestBody)
    {
        final String methodName = "createOperatingPlatform";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof OperatingPlatformProperties operatingPlatformProperties)
                {
                    response.setGUID(handler.createOperatingPlatform(userId,
                                                         requestBody,
                                                         requestBody.getInitialClassifications(),
                                                         operatingPlatformProperties,
                                                         requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(OperatingPlatformProperties.class.getName(), methodName);
                }
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Create a new metadata element to represent a operating platform using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return unique identifier of the newly created element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createOperatingPlatformFromTemplate(String              serverName,
                                                            TemplateRequestBody requestBody)
    {
        final String methodName = "createOperatingPlatformFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

                response.setGUID(handler.createOperatingPlatformFromTemplate(userId,
                                                                 requestBody,
                                                                 requestBody.getTemplateGUID(),
                                                                 requestBody.getReplacementProperties(),
                                                                 requestBody.getReplacementClassifications(),
                                                                 requestBody.getPlaceholderPropertyValues(),
                                                                 requestBody.getParentRelationshipProperties()));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Update the properties of a operating platform.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier of the operating platform
     * @param requestBody properties for the request
     *
     * @return boolean - true if an update occurred or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public BooleanResponse updateOperatingPlatform(String                   serverName,
                                                   String                   operatingPlatformGUID,
                                                   UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateOperatingPlatform";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof OperatingPlatformProperties operatingPlatformProperties)
                {
                    response.setFlag(handler.updateOperatingPlatform(userId, operatingPlatformGUID, requestBody, operatingPlatformProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(OperatingPlatformProperties.class.getName(), methodName);
                }
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Delete a operating platform.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier of the operating platform
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse deleteOperatingPlatform(String                   serverName,
                                                String                   operatingPlatformGUID,
                                                DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteOperatingPlatform";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

            handler.deleteOperatingPlatform(userId, operatingPlatformGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Returns the list of operating platforms with a particular name.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return a list of elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getOperatingPlatformsByName(String            serverName,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getOperatingPlatformsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getOperatingPlatformsByName(userId, requestBody.getFilter(), requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Retrieve the list of operating platform metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param requestBody properties for the request
     *
     * @return a list of elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findOperatingPlatforms(String                  serverName,
                                                                   SearchStringRequestBody requestBody)
    {
        final String methodName = "findOperatingPlatforms";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findOperatingPlatforms(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findOperatingPlatforms(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }

    /**
     * Return the properties of a specific operating platform.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier of the operating platform
     * @param requestBody properties for the request
     *
     * @return matching element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getOperatingPlatformByGUID(String         serverName,
                                                                      String         operatingPlatformGUID,
                                                                      GetRequestBody requestBody)
    {
        final String methodName = "getOperatingPlatformByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

            response.setElement(handler.getOperatingPlatformByGUID(userId, operatingPlatformGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Operating platform relationships
     */

    /**
     * Attach an operating platform to the IT infrastructure that it is installed on.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier of the operating platform
     * @param itInfrastructureGUID unique identifier of the IT infrastructure that the operating platform is installed on
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkOperatingPlatformUse(String                     serverName,
                                                 String                     operatingPlatformGUID,
                                                 String                     itInfrastructureGUID,
                                                 NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkOperatingPlatformUse";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.linkOperatingPlatformUse(userId, operatingPlatformGUID, itInfrastructureGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof OperatingPlatformUseProperties properties)
            {
                handler.linkOperatingPlatformUse(userId, operatingPlatformGUID, itInfrastructureGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkOperatingPlatformUse(userId, operatingPlatformGUID, itInfrastructureGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(OperatingPlatformUseProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach an operating platform from the IT infrastructure that it was installed on.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier of the operating platform
     * @param itInfrastructureGUID unique identifier of the IT infrastructure that the operating platform is installed on
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachOperatingPlatformUse(String                        serverName,
                                                   String                        operatingPlatformGUID,
                                                   String                        itInfrastructureGUID,
                                                   DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachOperatingPlatformUse";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

            handler.detachOperatingPlatformUse(userId, operatingPlatformGUID, itInfrastructureGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach an operating platform to the collection of software packages that it is packaged with.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier of the operating platform
     * @param collectionGUID unique identifier of the collection of software packages
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkOperatingPlatformManifest(String                     serverName,
                                                      String                     operatingPlatformGUID,
                                                      String                     collectionGUID,
                                                      NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkOperatingPlatformManifest";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.linkOperatingPlatformManifest(userId, operatingPlatformGUID, collectionGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof OperatingPlatformManifestProperties properties)
            {
                handler.linkOperatingPlatformManifest(userId, operatingPlatformGUID, collectionGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkOperatingPlatformManifest(userId, operatingPlatformGUID, collectionGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(OperatingPlatformManifestProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach an operating platform from a collection of software packages that it was packaged with.
     *
     * @param serverName name of the server to route the request to
     * @param operatingPlatformGUID unique identifier of the operating platform
     * @param collectionGUID unique identifier of the collection of software packages
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachOperatingPlatformManifest(String                        serverName,
                                                        String                        operatingPlatformGUID,
                                                        String                        collectionGUID,
                                                        DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachOperatingPlatformManifest";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

            handler.detachOperatingPlatformManifest(userId, operatingPlatformGUID, collectionGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach an asset to the collection of software packages that it depends on when it is running.
     *
     * @param serverName name of the server to route the request to
     * @param assetGUID unique identifier of the asset
     * @param collectionGUID unique identifier of the collection of software packages
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkSoftwarePackageDependency(String                     serverName,
                                                      String                     assetGUID,
                                                      String                     collectionGUID,
                                                      NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSoftwarePackageDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.linkSoftwarePackageDependency(userId, assetGUID, collectionGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof SoftwarePackageDependencyProperties properties)
            {
                handler.linkSoftwarePackageDependency(userId, assetGUID, collectionGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkSoftwarePackageDependency(userId, assetGUID, collectionGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(SoftwarePackageDependencyProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach an asset from a collection of software packages that it no longer depends on.
     *
     * @param serverName name of the server to route the request to
     * @param assetGUID unique identifier of the asset
     * @param collectionGUID unique identifier of the collection of software packages
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachSoftwarePackageDependency(String                        serverName,
                                                        String                        assetGUID,
                                                        String                        collectionGUID,
                                                        DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSoftwarePackageDependency";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

            handler.detachSoftwarePackageDependency(userId, assetGUID, collectionGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify an element to indicate that it describes a list of software packages.
     *
     * @param serverName name of the server to route the request to
     * @param elementGUID unique identifier of the element
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setSoftwarePackageManifest(String                       serverName,
                                                   String                       elementGUID,
                                                   NewClassificationRequestBody requestBody)
    {
        final String methodName = "setSoftwarePackageManifest";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setSoftwarePackageManifest(userId, elementGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof SoftwarePackageManifestProperties properties)
            {
                handler.setSoftwarePackageManifest(userId, elementGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setSoftwarePackageManifest(userId, elementGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(SoftwarePackageManifestProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the software package manifest designation from the element.
     *
     * @param serverName name of the server to route the request to
     * @param elementGUID unique identifier of the element
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearSoftwarePackageManifest(String                          serverName,
                                                     String                          elementGUID,
                                                     DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearSoftwarePackageManifest";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OperatingPlatformHandler handler = instanceHandler.getOperatingPlatformHandler(userId, serverName, methodName);

            handler.clearSoftwarePackageManifest(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Cloud platform classification
     */

    /**
     * Classify a software server platform to say that it is a cloud platform.
     *
     * @param serverName name of the server to route the request to
     * @param softwareServerPlatformGUID unique identifier of the software server platform
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setAsCloudPlatform(String                       serverName,
                                           String                       softwareServerPlatformGUID,
                                           NewClassificationRequestBody requestBody)
    {
        final String methodName = "setAsCloudPlatform";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setAsCloudPlatform(userId, softwareServerPlatformGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof CloudPlatformProperties properties)
            {
                handler.setAsCloudPlatform(userId, softwareServerPlatformGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setAsCloudPlatform(userId, softwareServerPlatformGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(CloudPlatformProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the cloud platform designation from a software server platform.
     *
     * @param serverName name of the server to route the request to
     * @param softwareServerPlatformGUID unique identifier of the software server platform
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAsCloudPlatform(String                          serverName,
                                             String                          softwareServerPlatformGUID,
                                             DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearAsCloudPlatform";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.clearAsCloudPlatform(userId, softwareServerPlatformGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Cloud provider classification
     */

    /**
     * Classify a host to say that it is a cloud provider.
     *
     * @param serverName name of the server to route the request to
     * @param hostGUID unique identifier of the host
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setHostAsCloudProvider(String                       serverName,
                                               String                       hostGUID,
                                               NewClassificationRequestBody requestBody)
    {
        final String methodName = "setHostAsCloudProvider";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setHostAsCloudProvider(userId, hostGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof CloudProviderProperties properties)
            {
                handler.setHostAsCloudProvider(userId, hostGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setHostAsCloudProvider(userId, hostGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(CloudProviderProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the cloud provider designation from a host.
     *
     * @param serverName name of the server to route the request to
     * @param hostGUID unique identifier of the host
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearHostAsCloudProvider(String                          serverName,
                                                 String                          hostGUID,
                                                 DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearHostAsCloudProvider";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.clearHostAsCloudProvider(userId, hostGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Cloud tenant classification
     */

    /**
     * Classify a software server to say that it is hosting a cloud tenant.
     *
     * @param serverName name of the server to route the request to
     * @param softwareServerGUID unique identifier of the software server
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setServerAsCloudTenant(String                       serverName,
                                               String                       softwareServerGUID,
                                               NewClassificationRequestBody requestBody)
    {
        final String methodName = "setServerAsCloudTenant";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setServerAsCloudTenant(userId, softwareServerGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof CloudTenantProperties properties)
            {
                handler.setServerAsCloudTenant(userId, softwareServerGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setServerAsCloudTenant(userId, softwareServerGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(CloudTenantProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the cloud tenant designation from a software server.
     *
     * @param serverName name of the server to route the request to
     * @param softwareServerGUID unique identifier of the software server
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearServerAsCloudTenant(String                          serverName,
                                                 String                          softwareServerGUID,
                                                 DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearServerAsCloudTenant";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            handler.clearServerAsCloudTenant(userId, softwareServerGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Cloud service classification
     */

    /**
     * Classify a software capability to say that it is a cloud service.
     *
     * @param serverName name of the server to route the request to
     * @param softwareCapabilityGUID unique identifier of the software capability
     * @param requestBody properties for the classification
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse setCapabilityAsCloudService(String                       serverName,
                                                    String                       softwareCapabilityGUID,
                                                    NewClassificationRequestBody requestBody)
    {
        final String methodName = "setCapabilityAsCloudService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.setCapabilityAsCloudService(userId, softwareCapabilityGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof CloudServiceProperties properties)
            {
                handler.setCapabilityAsCloudService(userId, softwareCapabilityGUID, properties, requestBody);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.setCapabilityAsCloudService(userId, softwareCapabilityGUID, null, requestBody);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(CloudServiceProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the cloud service designation from a software capability.
     *
     * @param serverName name of the server to route the request to
     * @param softwareCapabilityGUID unique identifier of the software capability
     * @param requestBody options for the request
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse clearCapabilityAsCloudService(String                          serverName,
                                                      String                          softwareCapabilityGUID,
                                                      DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearCapabilityAsCloudService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);

            handler.clearCapabilityAsCloudService(userId, softwareCapabilityGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}
