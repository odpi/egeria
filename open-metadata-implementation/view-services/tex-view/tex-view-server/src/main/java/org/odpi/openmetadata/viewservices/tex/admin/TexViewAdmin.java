/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.tex.admin;


import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.tex.api.ffdc.TexViewAuditCode;
import org.odpi.openmetadata.viewservices.tex.server.TexViewServicesInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TexViewAdmin is the class that is called by the UI Server to initialize and terminate
 * the Type Explorer OMVS.  The initialization call provides this OMVS with the Audit log and configuration.
 */
public class TexViewAdmin extends ViewServiceAdmin {

    private static final Logger log = LoggerFactory.getLogger(TexViewAdmin.class);

    //private ViewServiceConfig       viewServiceConfig = null;
    private AuditLog                auditLog          = null;
    private String                  serverUserName    = null;
    private TexViewServicesInstance instance          = null;
    private String                  serverName        = null;

    /**
     * Default constructor
     */
    public TexViewAdmin() {
    }

    /**
     * Initialize the TEX view service.
     *
     * @param serverName                         name of the local server
     * @param viewServiceConfig                  specific configuration properties for this view service.
     * @param auditLog                           audit log component for logging messages.
     * @param serverUserName                     user id to use to issue calls to the remote server.
     * @param maxPageSize                        maximum page size. 0 means unlimited
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(String            serverName,
                           ViewServiceConfig viewServiceConfig,
                           AuditLog          auditLog,
                           String            serverUserName,
                           int               maxPageSize)

    throws OMAGConfigurationErrorException {

        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, TexViewAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;

        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + actionDescription + ", userid=" + serverUserName);
        }

        try {

            // TODO check you do not need to save the config.... this.viewServiceConfig = viewServiceConfig;

            // TODO validate the configuration and when invalid, throw OMAGConfigurationErrorException

            /*
             * The name and URLRoot of the repository server are not passed at this stage - they are not known at this stage as in Tex
             * they are runtime variables set by the user and potentially changed between operations.
             */
            this.instance = new TexViewServicesInstance(serverName,     // this is the name of the server running the view-service
                                                        auditLog,
                                                        serverUserName,      // this is the name of the server running the view-service
                                                        maxPageSize);

            this.serverUserName    = serverUserName;
            this.serverName        = serverName;

            auditLog.logMessage(actionDescription,
                                TexViewAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                viewServiceConfig.toString());

            if (log.isDebugEnabled()) {
                log.debug("<== Method: " + actionDescription + ",userid=" + serverUserName);
            }

        }
        // TODO - if you parse config be ready to catch this...
        //catch (OMAGConfigurationErrorException error)
        // {
        //     throw error;
        //}
        catch (Throwable error)
        {
            auditLog.logException(actionDescription,
                                  TexViewAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  viewServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         ViewServiceDescription.TYPE_EXPLORER.getViewServiceFullName(),
                                                         error);
        }

    }

    /**
     * Shutdown the tex view service.
     */
    @Override
    public void shutdown() {
        final String actionDescription = "shutdown";

        log.debug("==> Method: " + actionDescription + ", userid=" + serverUserName);

        auditLog.logMessage(actionDescription, TexViewAuditCode.SERVICE_TERMINATING.getMessageDefinition(serverName));

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditLog.logMessage(actionDescription, TexViewAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));

        log.debug("<== Method: " + actionDescription + ", userid=" + serverUserName);

    }
}