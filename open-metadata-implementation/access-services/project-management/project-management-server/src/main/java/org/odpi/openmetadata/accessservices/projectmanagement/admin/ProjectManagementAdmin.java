/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.admin;


import org.odpi.openmetadata.accessservices.projectmanagement.ffdc.ProjectManagementAuditCode;
import org.odpi.openmetadata.accessservices.projectmanagement.listener.ProjectManagementOMRSTopicListener;
import org.odpi.openmetadata.accessservices.projectmanagement.server.ProjectManagementServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * ProjectManagementAdmin manages the start up and shutdown of the Stewardship Action OMAS.   During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class ProjectManagementAdmin extends AccessServiceAdmin
{
    private AuditLog                          auditLog   = null;
    private ProjectManagementServicesInstance instance   = null;
    private String                            serverName = null;

    /**
     * Default constructor
     */
    public ProjectManagementAdmin()
    {
    }


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig  specific configuration properties for this access service.
     * @param omrsTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector  connector for querying the cohort repositories
     * @param auditLog  audit log component for logging messages.
     * @param serverUserName  user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(AccessServiceConfig     accessServiceConfig,
                           OMRSTopicConnector      omrsTopicConnector,
                           OMRSRepositoryConnector repositoryConnector,
                           AuditLog                auditLog,
                           String                  serverUserName) throws OMAGConfigurationErrorException
    {
        final String               actionDescription = "initialize";

        auditLog.logMessage(actionDescription, ProjectManagementAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;

        try
        {
            List<String>  supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                                                                      accessServiceConfig.getAccessServiceName(),
                                                                      auditLog);

            this.instance = new ProjectManagementServicesInstance(repositoryConnector,
                                                                  supportedZones,
                                                                  auditLog,
                                                                  serverUserName,
                                                                  repositoryConnector.getMaxPageSize());
            this.serverName = instance.getServerName();

            /*
             * Only set up the listening and event publishing if requested in the config.
             */
            if (accessServiceConfig.getAccessServiceOutTopic() != null)
            {
                ProjectManagementOMRSTopicListener omrsTopicListener;

                omrsTopicListener = new ProjectManagementOMRSTopicListener(accessServiceConfig.getAccessServiceOutTopic(),
                                                                           repositoryConnector.getRepositoryHelper(),
                                                                           repositoryConnector.getRepositoryValidator(),
                                                                           accessServiceConfig.getAccessServiceName(),
                                                                           supportedZones,
                                                                           auditLog);
                super.registerWithEnterpriseTopic(accessServiceConfig.getAccessServiceName(),
                                                  serverName,
                                                  omrsTopicConnector,
                                                  omrsTopicListener,
                                                  auditLog);
            }

            auditLog.logMessage(actionDescription,
                                ProjectManagementAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                accessServiceConfig.toString());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  ProjectManagementAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceFullName(),
                                                         error);
        }
    }


    /**
     * Shutdown the access service.
     */
    @Override
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditLog.logMessage(actionDescription, ProjectManagementAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}
