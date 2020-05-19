/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.admin;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaAuditCode;
import org.odpi.openmetadata.accessservices.subjectarea.listener.SubjectAreaOMRSTopicListener;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * SubjectAreaAdmin is the Subject Area Open Metadata Access Service (OMAS) implementation class that controls its lifecycle.
 * It is initialised here receiving the OMAS configuration. It is shutdown here.
 */
public class SubjectAreaAdmin extends AccessServiceAdmin {
    private AuditLog auditLog = null;
    private SubjectAreaServicesInstance instance = null;
    private String serverName = null;

    /**
     * Default constructor
     */
    public SubjectAreaAdmin() {
    }


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig specific configuration properties for this access service.
     * @param omrsTopicConnector  connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector connector for querying the cohort repositories
     * @param auditLog            audit log component for logging messages.
     * @param serverUserName      user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    public void initialize(AccessServiceConfig accessServiceConfig,
                           OMRSTopicConnector omrsTopicConnector,
                           OMRSRepositoryConnector repositoryConnector,
                           AuditLog auditLog,
                           String serverUserName) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, SubjectAreaAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;

        try {
            this.instance = new SubjectAreaServicesInstance(repositoryConnector,
                                                            auditLog,
                                                            serverUserName,
                                                            repositoryConnector.getMaxPageSize());
            this.serverName = instance.getServerName();

            /*
             * Only set up the listening and event publishing if requested in the config.
             */
            if (accessServiceConfig.getAccessServiceOutTopic() != null) {
                SubjectAreaOMRSTopicListener omrsTopicListener;

                omrsTopicListener = new SubjectAreaOMRSTopicListener(accessServiceConfig.getAccessServiceOutTopic(),
                                                                     repositoryConnector.getRepositoryHelper(),
                                                                     repositoryConnector.getRepositoryValidator(),
                                                                     accessServiceConfig.getAccessServiceName(),
                                                                     auditLog);
                super.registerWithEnterpriseTopic(accessServiceConfig.getAccessServiceName(),
                                                  serverName,
                                                  omrsTopicConnector,
                                                  omrsTopicListener,
                                                  auditLog);
            }

            auditLog.logMessage(actionDescription,
                                SubjectAreaAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                accessServiceConfig.toString());
        } catch (OMAGConfigurationErrorException error) {
            throw error;
        } catch (Throwable error) {
            auditLog.logException(actionDescription,
                                  SubjectAreaAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getClass().getName(), error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.SUBJECT_AREA_OMAS.getAccessServiceFullName(),
                                                         error);
        }
    }


    /**
     * Shutdown the access service.
     */
    public void shutdown() {
        final String actionDescription = "shutdown";

        if (instance != null) {
            this.instance.shutdown();
        }

        auditLog.logMessage(actionDescription, SubjectAreaAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}

