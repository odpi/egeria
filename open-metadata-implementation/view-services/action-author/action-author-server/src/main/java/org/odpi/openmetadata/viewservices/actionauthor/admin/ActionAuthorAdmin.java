/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.actionauthor.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.registration.ViewServiceAdmin;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.actionauthor.ffdc.ActionAuthorAuditCode;
import org.odpi.openmetadata.viewservices.actionauthor.server.ActionAuthorInstance;

/**
 * ActionAuthorAdmin is the class that is called by the View Server to initialize and terminate
 * the Action Author OMVS.  The initialization call provides this OMVS with the Audit log and configuration.
 */
public class ActionAuthorAdmin extends ViewServiceAdmin
{
    private AuditLog             auditLog   = null;
    private ActionAuthorInstance instance   = null;
    private String               serverName = null;

    /**
     * Default constructor
     */
    public ActionAuthorAdmin()
    {
    }


    /**
     * Initialize the Action Author view service.
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
                           int                          maxPageSize) throws OMAGConfigurationErrorException

    {

        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, ActionAuthorAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;
        this.serverName = serverName;

        try
        {
            /*
             * The name and rootURL of a repository server are not passed at this stage - they are not known at this stage
             * because they are set at runtime by the user and potentially changed between operations.
             */
            this.instance = new ActionAuthorInstance(serverName,
                                                     auditLog,
                                                     serverUserName,
                                                     maxPageSize,
                                                     viewServiceConfig.getOMAGServerName(),
                                                     viewServiceConfig.getOMAGServerPlatformRootURL());

            auditLog.logMessage(actionDescription,
                                ActionAuthorAuditCode.SERVICE_INITIALIZED.getMessageDefinition(),
                                viewServiceConfig.toString());

        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  ActionAuthorAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  viewServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         ViewServiceDescription.ACTION_AUTHOR.getViewServiceFullName(),
                                                         error);
        }
    }

    /**
     * Shutdown the Action Author service.
     */
    @Override
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        auditLog.logMessage(actionDescription, ActionAuthorAuditCode.SERVICE_TERMINATING.getMessageDefinition(serverName));

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditLog.logMessage(actionDescription, ActionAuthorAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}