/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.serverauthor.admin;


import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.adminservices.registration.ViewServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminAuditCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.viewservices.serverauthor.api.ffdc.ServerAuthorViewAuditCode;
import org.odpi.openmetadata.viewservices.serverauthor.api.ffdc.ServerAuthorViewErrorCode;
import org.odpi.openmetadata.viewservices.serverauthor.initialization.ServerAuthorViewServicesInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * ServerAuthorViewAdmin is the class that is called by the UI Server to initialize and terminate
 * the Server Author OMVS.  The initialization call provides this OMVS with the Audit log and configuration.
 */
public class ServerAuthorViewAdmin extends ViewServiceAdmin {

    private static final Logger log = LoggerFactory.getLogger(ServerAuthorViewAdmin.class);

    /**
     * resource endpoints property name
     */
    protected String   resourceEndpointsPropertyName       = "resourceEndpoints";      /* Common */


    private AuditLog                  auditLog             = null;
    private String                    serverUserName       = null;
    private ServerAuthorViewServicesInstance instance      = null;
    private String                    serverName           = null;

    /**
     * Default constructor
     */
    public ServerAuthorViewAdmin() {
    }

    /**
     * Initialize the Server Author view service.
     *
     * @param serverName                         name of the local server
     * @param viewServiceConfig                  specific configuration properties for this view service.
     * @param auditLog                           audit log component for logging messages.
     * @param serverUserName                     user id to use to issue calls to the remote server.
     * @param maxPageSize                        maximum page size. 0 means unlimited
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(String                       serverName,
                           ViewServiceConfig            viewServiceConfig,
                           AuditLog                     auditLog,
                           String                       serverUserName,
                           int                          maxPageSize)     throws OMAGConfigurationErrorException

    {

        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, ServerAuthorViewAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + actionDescription);
        }

        /*
         * This method will be called (by Operational Services) with the view service config passed as a ViewServiceConfig.
         * This is the super type of IntegrationViewServiceConfig which is what this service actually requires.
         */

        IntegrationViewServiceConfig integrationViewServiceConfig = null;
        if (viewServiceConfig instanceof IntegrationViewServiceConfig) {
            integrationViewServiceConfig = (IntegrationViewServiceConfig) viewServiceConfig;
        }
        else {
            logBadConfiguration(viewServiceConfig.getViewServiceName(),
                                "viewServiceConfig",
                                viewServiceConfig.toString(),
                                auditLog,
                                actionDescription);

            // unreachable
            return;
        }

        final String viewServiceFullName = viewServiceConfig.getViewServiceName();

        try {

            List<ResourceEndpointConfig> resourceEndpoints = this.extractResourceEndpoints(integrationViewServiceConfig.getResourceEndpoints(),
                                                                                           viewServiceFullName,
                                                                                           auditLog);






            /*
             * Pick up the remote omag platform url from the config
             */
            this.instance = new ServerAuthorViewServicesInstance(serverName,
                                                         auditLog,
                                                         serverUserName,
                                                         maxPageSize,
                                                         viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                         resourceEndpoints
                                                                 );

            this.serverUserName    = serverUserName;
            this.serverName        = serverName;

            auditLog.logMessage(actionDescription,
                                ServerAuthorViewAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                viewServiceConfig.toString());

            if (log.isDebugEnabled()) {
                log.debug("<== Method: " + actionDescription);
            }

        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Throwable error)
        {
            auditLog.logException(actionDescription,
                                  ServerAuthorViewAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  viewServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         ViewServiceDescription.SERVER_AUTHOR.getViewServiceFullName(),
                                                         error);
        }

    }

    /**
     * Shutdown the Server Author view service.
     */
    @Override
    public void shutdown() {
        final String actionDescription = "shutdown";

        log.debug("==> Method: " + actionDescription);

        auditLog.logMessage(actionDescription, ServerAuthorViewAuditCode.SERVICE_TERMINATING.getMessageDefinition(serverName));

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditLog.logMessage(actionDescription, ServerAuthorViewAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));

        log.debug("<== Method: " + actionDescription);

    }



    /**
     * Extract the resource endpoints property from the view services option.
     *
     * @param resourceEndpoints options passed to the access service.
     * @param viewServiceFullName name of calling service
     * @param auditLog audit log for error messages
     * @return null or list of resource endpoints
     * @throws OMAGConfigurationErrorException the supported zones property is not a list of zone names.
     */
    protected List<ResourceEndpointConfig> extractResourceEndpoints(List<ResourceEndpointConfig> resourceEndpoints,
                                                                    String                       viewServiceFullName,
                                                                    AuditLog                     auditLog)             throws OMAGConfigurationErrorException
    {
        final String methodName = "extractResourceEndpoints";

        final String parameterName = "resourceEndpoints";

        try {

            /*
             * Server Author cannot operate without any endpoints.
             * Check if resourceEndpoints is null and if so throw am exception, which will be caught and logged by logBadConfigProperties, which will throw OMAGConfigurationErrorException...
             */
            if (resourceEndpoints == null) {

                throw new InvalidParameterException(ServerAuthorViewErrorCode.INVALID_CONFIG_PROPERTY.getMessageDefinition(parameterName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    parameterName);

            } else {

                @SuppressWarnings("unchecked")
                List<ResourceEndpointConfig> endpointList = (List<ResourceEndpointConfig>) resourceEndpoints;
                auditLog.logMessage(methodName, OMAGAdminAuditCode.RESOURCE_ENDPOINTS.getMessageDefinition(viewServiceFullName, endpointList.toString()));
                return endpointList;
            }

        } catch (Throwable error) {

            logBadConfigProperties(viewServiceFullName,
                                   resourceEndpointsPropertyName,
                                   resourceEndpoints.toString(),
                                   auditLog,
                                   methodName,
                                   error);

            // unreachable
            return null;
        }
    }

}