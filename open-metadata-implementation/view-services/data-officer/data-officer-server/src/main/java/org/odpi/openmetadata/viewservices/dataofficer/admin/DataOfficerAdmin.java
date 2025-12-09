/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dataofficer.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.registration.ViewServiceAdmin;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.dataofficer.ffdc.DataOfficerAuditCode;
import org.odpi.openmetadata.viewservices.dataofficer.server.DataOfficerInstance;

/**
 * DataOfficerAdmin is the class that is called by the View Server to initialize and terminate
 * the Data Officer OMVS.  The initialization call provides this OMVS with the Audit log and configuration.
 */
public class DataOfficerAdmin extends ViewServiceAdmin
{
    private AuditLog              auditLog   = null;
    private DataOfficerInstance instance   = null;
    private String                serverName = null;

    /**
     * Default constructor
     */
    public DataOfficerAdmin()
    {
    }


    /**
     * Initialize the view service.
     *
     * @param serverName                         name of the local server
     * @param viewServiceConfig                  specific configuration properties for this view service.
     * @param auditLog                           audit log component for logging messages.
     * @param serverUserName                     user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param maxPageSize                        maximum page size. 0 means unlimited
     * @throws OMAGConfigurationErrorException   invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(String                       serverName,
                           ViewServiceConfig            viewServiceConfig,
                           AuditLog                     auditLog,
                           String                       serverUserName,
                           int                          maxPageSize) throws OMAGConfigurationErrorException
    {

        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, DataOfficerAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;
        this.serverName = serverName;

        try
        {
            /*
             * The name and rootURL of a repository server are not passed at this stage - they are not known at this stage
             * because they are set at runtime by the user and potentially changed between operations.
             */
            this.instance = new DataOfficerInstance(serverName,
                                                    auditLog,
                                                    serverUserName,
                                                    viewServiceConfig.getSecretsStoreProvider(),
                                                    viewServiceConfig.getSecretsStoreLocation(),
                                                    viewServiceConfig.getSecretsStoreCollection(),
                                                    maxPageSize,
                                                    viewServiceConfig.getOMAGServerName(),
                                                    viewServiceConfig.getOMAGServerPlatformRootURL());

            auditLog.logMessage(actionDescription,
                                DataOfficerAuditCode.SERVICE_INITIALIZED.getMessageDefinition(),
                                viewServiceConfig.toString());
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  DataOfficerAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  viewServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         ViewServiceDescription.DATA_OFFICER.getViewServiceFullName(),
                                                         error);
        }
    }

    /**
     * Shutdown the Data Officer service.
     */
    @Override
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        auditLog.logMessage(actionDescription, DataOfficerAuditCode.SERVICE_TERMINATING.getMessageDefinition(serverName));

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditLog.logMessage(actionDescription, DataOfficerAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}