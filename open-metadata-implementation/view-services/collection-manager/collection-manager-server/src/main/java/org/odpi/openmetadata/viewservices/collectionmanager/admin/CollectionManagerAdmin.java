/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.collectionmanager.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.registration.ViewServiceAdmin;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.collectionmanager.ffdc.CollectionManagerAuditCode;
import org.odpi.openmetadata.viewservices.collectionmanager.server.CollectionManagerInstance;

/**
 * CollectionManagerAdmin is the class that is called by the View Server to initialize and terminate
 * the Collection Manager OMVS.  The initialization call provides this OMVS with the Audit log and configuration.
 */
public class CollectionManagerAdmin extends ViewServiceAdmin
{
    private AuditLog                  auditLog   = null;
    private CollectionManagerInstance instance   = null;
    private String                    serverName = null;

    /**
     * Default constructor
     */
    public CollectionManagerAdmin()
    {
    }


    /**
     * Initialize the Collection Manager view service.
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

        auditLog.logMessage(actionDescription, CollectionManagerAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;
        this.serverName = serverName;

        try
        {
            /*
             * The name and rootURL of a repository server are not passed at this stage - they are not known at this stage
             * because they are set at runtime by the user and potentially changed between operations.
             */
            this.instance = new CollectionManagerInstance(serverName,
                                                          auditLog,
                                                          serverUserName,
                                                          maxPageSize,
                                                          viewServiceConfig.getOMAGServerName(),
                                                          viewServiceConfig.getOMAGServerPlatformRootURL());

            auditLog.logMessage(actionDescription,
                                CollectionManagerAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                viewServiceConfig.toString());

        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  CollectionManagerAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  viewServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         ViewServiceDescription.COLLECTION_MANAGER.getViewServiceFullName(),
                                                         error);
        }
    }

    /**
     * Shutdown the Collection Manager service.
     */
    @Override
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        auditLog.logMessage(actionDescription, CollectionManagerAuditCode.SERVICE_TERMINATING.getMessageDefinition(serverName));

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditLog.logMessage(actionDescription, CollectionManagerAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}