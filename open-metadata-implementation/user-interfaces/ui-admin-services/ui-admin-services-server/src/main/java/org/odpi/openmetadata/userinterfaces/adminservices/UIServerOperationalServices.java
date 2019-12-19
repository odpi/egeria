/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices;


import org.odpi.openmetadata.adminservices.OMAGServerExceptionHandler;
import org.odpi.openmetadata.adminservices.OMAGServerOperationalInstanceHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.SuccessMessageResponse;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerPlatformInstanceMap;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogDestination;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStore;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.UIAdminErrorCode;
import org.odpi.openmetadata.userinterface.adminservices.rest.UIServerConfigResponse;
import org.odpi.userinterface.adminservices.configuration.registration.ViewServiceAdmin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * UIServerOperationalServices will provide support to start, manage and stop services in the UI Server.
 */
public class UIServerOperationalServices
{
    private OMAGServerOperationalInstanceHandler instanceHandler = new OMAGServerOperationalInstanceHandler(CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName());

    private OMAGServerPlatformInstanceMap platformInstanceMap = new OMAGServerPlatformInstanceMap();

    private UIServerAdminStoreServices configStore  = new UIServerAdminStoreServices();
    private UIServerErrorHandler errorHandler = new UIServerErrorHandler();
    private OMAGServerExceptionHandler   exceptionHandler = new OMAGServerExceptionHandler();
    /*
     * =============================================================
     * Initialization and shutdown
     */

    /**
     * Activate the open metadata and governance services using the stored configuration information.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    public SuccessMessageResponse activateWithStoredConfig(String userId,
                                                           String serverName)
    {
        final String methodName = "activateWithStoredConfig";

        SuccessMessageResponse response = new SuccessMessageResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            response = activateWithSuppliedConfig(userId, serverName, configStore.getServerConfig(userId, serverName, methodName));
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }


    /**
     * Activate the open metadata and governance services using the supplied configuration
     * document.
     *
     * @param userId  user that is issuing the request
     * @param configuration  properties used to initialize the services
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    public SuccessMessageResponse activateWithSuppliedConfig(String           userId,
                                                             String           serverName,
                                                             UIServerConfig   configuration)
    {
        final String methodName                = "activateWithSuppliedConfig";

        List<String> activatedServiceList = new ArrayList<>();

        SuccessMessageResponse response = new SuccessMessageResponse();

        try
        {
            /*
             * Check that a serverName and userId is supplied
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            /*
             * Validate there is a configuration document supplied.
             */
            if (configuration == null)
            {
                UIAdminErrorCode errorCode    = UIAdminErrorCode.NULL_SERVER_CONFIG;
                String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        "UIServerConfig");
            }

            /*
             * Next verify that there are services configured.
             */

            int maxPageSize = configuration.getMaxPageSize();

            /*
             * Next verify that there are views configured.
             */
            List<ViewServiceConfig> viewServiceConfigList   = configuration.getViewServicesConfig();

            if ((viewServiceConfigList == null) || (viewServiceConfigList.isEmpty()))
            {
                UIAdminErrorCode errorCode    = UIAdminErrorCode.EMPTY_CONFIGURATION;
                String             errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

                throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }


            /*
             * Validate the server name from the configuration document matches the server name passed in the request
             * and if all is well, save the configuration document to the config store and the server operationalServicesInstance.
             */
            errorHandler.validateConfigServerName(serverName, configuration.getLocalServerName(), methodName);
            // validate there is metadata server information, so we have a metadata server to talk to.
            errorHandler.validateMetadataServerName(serverName,methodName,configuration.getMetadataServerName());
            errorHandler.validateMetadataServerURL(serverName,methodName,configuration.getMetadataServerURL());

            /*
             * Validate that the server is not running already.  If it is running it should be shutdown.
             */
            if (instanceHandler.isServerActive(userId, serverName))
            {
                this.deactivateTemporarily(userId, serverName);
            }

            /*
             * The operationalServicesInstance saves the operational services objects for this server operationalServicesInstance so they can be retrieved
             * in response to subsequent REST calls for the server
             */
            UIOperationalServicesInstance operationalServicesInstance = new UIOperationalServicesInstance(serverName,
                    CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName(), maxPageSize);

            /*
             * Save the configuration that is going to be used to start the server.
             */
            operationalServicesInstance.setOperationalConfiguration(configuration);

            /*
             * Initialize the audit log
             */

            OMRSAuditLogDestination auditLogDestination = new OMRSAuditLogDestination(configuration.getLocalServerName(),
                    configuration.getLocalServerType(),
                    configuration.getOrganizationName(),
                    getAuditLogStores(configuration.getAuditLogConnections(),
                            configuration.getLocalServerName()));

            OMRSAuditLog auditLog = new OMRSAuditLog(auditLogDestination, OMRSAuditingComponent.OPERATIONAL_SERVICES);
            /*
             * Ready to start services - unlike the omag server, the ui does not require the repository services.
             */

            List<ViewServiceAdmin>        operationalViewServiceAdminList = operationalServicesInstance.getOperationalViewServiceAdminList();
            if (viewServiceConfigList != null)
            {
                List<ViewServiceConfig> updatedViewServiceConfigList   = new ArrayList<>();
                for (ViewServiceConfig  viewServiceConfig : viewServiceConfigList)
                {
                    // ignore null in the list
                    if (viewServiceConfig != null)
                    {

                        String viewServiceAdminClassName = viewServiceConfig.getViewServiceAdminClass();

                        if (viewServiceAdminClassName != null)
                        {
                            ViewServiceAdmin viewServiceAdmin = null;

                            try {
                                viewServiceAdmin = (ViewServiceAdmin) Class.forName(viewServiceAdminClassName).newInstance();
                            } catch (InstantiationException exception) {
                                throwBadViewAdminClassException(serverName, viewServiceAdminClassName, methodName, viewServiceConfig);
                            } catch (IllegalAccessException exception){
                                throwBadViewAdminClassException(serverName, viewServiceAdminClassName, methodName, viewServiceConfig);
                            }

                            ViewServiceConfig updatedViewServiceConfig = viewServiceAdmin.initialize(viewServiceConfig,
                                    auditLog,
                                    configuration.getLocalServerUserId(),
                                    maxPageSize,
                                    serverName,
                                    configuration.getMetadataServerName(),
                                    configuration.getMetadataServerURL());

                            updatedViewServiceConfigList.add(updatedViewServiceConfig);
                            operationalViewServiceAdminList.add(viewServiceAdmin);
                            activatedServiceList.add(updatedViewServiceConfig.getViewServiceName());
                            // update the config with the updated (expanded) view service content.
                            configuration.setViewServicesConfig(updatedViewServiceConfigList);
                        }
                    }
                    else
                    {
                        OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.NULL_VIEW_SERVICE_ADMIN_CLASS;
                        String        errorMessage = errorCode.getErrorMessageId()
                                + errorCode.getFormattedErrorMessage(serverName,
                                viewServiceConfig.getViewServiceName());

                        throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                this.getClass().getName(),
                                methodName,
                                errorMessage,
                                errorCode.getSystemAction(),
                                errorCode.getUserAction());
                    }
                }
            }
            // store the config
            configStore.saveServerConfig(serverName, methodName, configuration);
            /*
             * Save the list of running access services to the operationalServicesInstance and then add the operationalServicesInstance to the operationalServicesInstance map.
             * The operationalServicesInstance information can then be retrieved for shutdown or other management requests.
             */
            operationalServicesInstance.setOperationalViewServiceAdminList(operationalViewServiceAdminList);

            response.setSuccessMessage(new Date().toString() + " " + serverName + " is running the following services: " + activatedServiceList.toString());
        }
        catch (OMAGConfigurationErrorException  error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }

    /**
     * An instance of the view service admin class could not be created.
     * @param serverName serverName
     * @param viewServiceAdminClassName the requested admin class that a new instance could not be created from
     * @param methodName method name for diagnotics
     * @param viewServiceConfig view service config - this should be non null
     * @throws OMAGConfigurationErrorException throw a bad omag configuration exception. The used should correct the supplied admin class.
     */
    void throwBadViewAdminClassException(String serverName,String viewServiceAdminClassName, String methodName, ViewServiceConfig viewServiceConfig) throws OMAGConfigurationErrorException {
        OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.BAD_VIEW_SERVICE_ADMIN_CLASS;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(serverName,
                viewServiceAdminClassName,
                viewServiceConfig.getViewServiceName());

        throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }
    /**
     * Return the connectors to the AuditLog store using the connection information supplied.  If there is a
     * problem with the connection information that means a connector can not be created, an exception is thrown.
     *
     * @param auditLogStoreConnections properties for the audit log stores
     * @param serverName serverName
     * @return audit log store connector
     */
    private List<OMRSAuditLogStore>  getAuditLogStores(List<Connection> auditLogStoreConnections, String serverName)
    {
        List<OMRSAuditLogStore>   auditLogStores = new ArrayList<>();

        for (Connection auditLogStoreConnection : auditLogStoreConnections)
        {
            auditLogStores.add(getAuditLogStore(auditLogStoreConnection,serverName));
        }

        if (auditLogStores.isEmpty())
        {
            return null;
        }
        else
        {
            return auditLogStores;
        }
    }

    /**
     * Return a connector to an audit log store.
     *
     * @param auditLogStoreConnection connection with the parameters of the audit log store
     * @return connector for audit log store.
     */
    private OMRSAuditLogStore getAuditLogStore(Connection   auditLogStoreConnection, String serverName)
    {
        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker();
            Connector connector       = connectorBroker.getConnector(auditLogStoreConnection);

            return (OMRSAuditLogStore)connector;
        }
        catch (Throwable   error)
        {
            String methodName = "getAuditLogStore";

            //log.debug("Unable to create audit log store connector: " + error.toString());

            /*
             * Throw runtime exception to indicate that the audit log is not available.
             */
            OMRSErrorCode errorCode = OMRSErrorCode.NULL_AUDIT_LOG_STORE;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(serverName);
            throw new OMRSConfigErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    error);
        }
    }


    /**
     * Shutdown any running services for a specific server instance.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param methodName calling method
     * @param instance a list of the running services
     * @param permanentDeactivation should the server be permanently disconnected
     * @throws InvalidParameterException one of the services detected an invalid parameter
     * @throws PropertyServerException one of the services had problems shutting down
     */
    private void deactivateRunningServiceInstances(String                          userId,
                                                   String                          serverName,
                                                   String                          methodName,
                                                   UIOperationalServicesInstance   instance,
                                                   boolean                         permanentDeactivation) throws InvalidParameterException,
            PropertyServerException
    {
        instanceHandler.removeServerServiceInstance(serverName);
        platformInstanceMap.shutdownServerInstance(userId, serverName, methodName);
    }


    /**
     * Temporarily deactivate any open metadata and governance services.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException the serverName is invalid.
     */
    public VoidResponse deactivateTemporarily(String  userId,
                                              String  serverName)
    {
        final String methodName = "deactivateTemporarily";

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            deactivateRunningServiceInstances(userId,
                    serverName,
                    methodName,
                    (UIOperationalServicesInstance)instanceHandler.getServerServiceInstance(userId, serverName, methodName),
                    false);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }


    /**
     * Terminate any running open metadata and governance services, remove the server from any open metadata cohorts
     * and delete the server's configuration.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException the serverName is invalid.
     */
    public VoidResponse deactivatePermanently(String  userId,
                                              String  serverName)
    {
        final String methodName = "deactivatePermanently";

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            deactivateRunningServiceInstances(userId,
                    serverName,
                    methodName,
                    (UIOperationalServicesInstance)instanceHandler.getServerServiceInstance(userId, serverName, methodName),
                    true);

            /*
             * Delete the configuration for this server
             */
            configStore.saveServerConfig(serverName, methodName, null);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }


    /*
     * =============================================================
     * Services on running instances
     */

    /*
     * Query current configuration
     */


    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return UIServerConfig properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName parameter.
     */
    public UIServerConfigResponse getActiveConfiguration(String userId,
                                                         String serverName)
    {
        final String methodName = "getActiveConfiguration";

        UIServerConfigResponse response = new UIServerConfigResponse();

        try
        {
            errorHandler.validateUserId(userId, serverName, methodName);

            UIOperationalServicesInstance instance = (UIOperationalServicesInstance)instanceHandler.getServerServiceInstance(userId, serverName, methodName);

            response.setUIServerConfig(instance.getOperationalConfiguration());
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }

}