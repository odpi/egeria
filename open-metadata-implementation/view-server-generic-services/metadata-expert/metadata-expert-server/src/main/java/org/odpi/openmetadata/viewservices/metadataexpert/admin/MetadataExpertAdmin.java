/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.metadataexpert.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.registration.ViewServerGenericServiceAdmin;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.metadataexpert.ffdc.MetadataExpertAuditCode;
import org.odpi.openmetadata.viewservices.metadataexpert.server.MetadataExpertInstance;

import java.util.List;

/**
 * MetadataExpertAdmin is the class that is called by the View Server to initialize and terminate
 * the Metadata Expert OMVS.  The initialization call provides this OMVS with the Audit log and configuration.
 */
public class MetadataExpertAdmin extends ViewServerGenericServiceAdmin
{
    private AuditLog                 auditLog   = null;
    private MetadataExpertInstance instance   = null;
    private String                   serverName = null;

    /**
     * Default constructor
     */
    public MetadataExpertAdmin()
    {
    }


    /**
     * Initialize the view service.
     *
     * @param serverName                         name of the local server
     * @param viewServiceConfig                  specific configuration properties for this view service.
     * @param auditLog                           audit log component for logging messages.
     * @param serverUserName                     user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param serverUserPassword                 password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize                        maximum page size. 0 means unlimited
     * @param activeViewServices list of view services active in this server
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(String                  serverName,
                           ViewServiceConfig       viewServiceConfig,
                           AuditLog                auditLog,
                           String                  serverUserName,
                           String                  serverUserPassword,
                           int                     maxPageSize,
                           List<ViewServiceConfig> activeViewServices) throws OMAGConfigurationErrorException
    {

        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, MetadataExpertAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;
        this.serverName = serverName;

        try
        {
            /*
             * The name and rootURL of a repository server are not passed at this stage - they are not known at this stage
             * because they are set at runtime by the user and potentially changed between operations.
             */
            this.instance = new MetadataExpertInstance(serverName,
                                                         auditLog,
                                                         serverUserName,
                                                         serverUserPassword,
                                                         maxPageSize,
                                                         viewServiceConfig.getOMAGServerName(),
                                                         viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                         activeViewServices);

            auditLog.logMessage(actionDescription,
                                MetadataExpertAuditCode.SERVICE_INITIALIZED.getMessageDefinition(),
                                viewServiceConfig.toString());

        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  MetadataExpertAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  viewServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         ViewServiceDescription.METADATA_EXPERT.getViewServiceFullName(),
                                                         error);
        }
    }

    /**
     * Shutdown the Metadata Expert service.
     */
    @Override
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        auditLog.logMessage(actionDescription, MetadataExpertAuditCode.SERVICE_TERMINATING.getMessageDefinition(serverName));

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditLog.logMessage(actionDescription, MetadataExpertAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}