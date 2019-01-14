/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.admin;


import org.odpi.openmetadata.accessservices.subjectarea.auditlog.SubjectAreaAuditCode;


import org.odpi.openmetadata.accessservices.subjectarea.listener.SubjectAreaOMRSTopicListener;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRESTServicesInstance;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SubjectAreaAdmin is the Subject Area access service implementation class that controls its lifecycle.
 * It is initalised here receiving the access service configuration. It is shutdown here.
 */
public class SubjectAreaAdmin implements AccessServiceAdmin
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaAdmin.class);

    private OMRSRepositoryConnector repositoryConnector = null;
    private OMRSTopicConnector omrsTopicConnector  = null;
    private AccessServiceConfig     accessServiceConfig = null;
    private OMRSAuditLog auditLog            = null;
    private String                  serverUserName      = null;

    private SubjectAreaOMRSTopicListener omrsTopicListener = null;
    private SubjectAreaServicesInstance instance =null;
    private String serverName =null;

    /**
     * Default constructor
     */
    public SubjectAreaAdmin()
    {
    }


    /**
     * Initialize the subject area access service.
     *
     * @param accessServiceConfigurationProperties - specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector - connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector - connector for querying the cohort repositories
     * @param auditLog - audit log component for logging messages.
     * @param serverUserName - user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException - invalid parameters in the configuration properties.
     */
    public void initialize(AccessServiceConfig     accessServiceConfigurationProperties,
                           OMRSTopicConnector      enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                           OMRSAuditLog            auditLog,
                           String                  serverUserName) throws OMAGConfigurationErrorException
    {
        final String            actionDescription = "initialize";
        final String methodName = actionDescription;
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userid="+ serverUserName);
        }
        //TODO validate the configuration and when invalid, throw OMAGConfigurationErrorException

        SubjectAreaAuditCode  auditCode;

        auditCode = SubjectAreaAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        this.repositoryConnector = enterpriseOMRSRepositoryConnector;

        try
        {
            this.instance = new SubjectAreaServicesInstance(repositoryConnector);
            this.serverName = instance.getServerName();
            this.accessServiceConfig = accessServiceConfigurationProperties;
            this.omrsTopicConnector = enterpriseOMRSTopicConnector;

            if (this.omrsTopicConnector != null)
            {
                auditCode = SubjectAreaAuditCode.SERVICE_REGISTERED_WITH_TOPIC;
                auditLog.logRecord(actionDescription,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(),
                        null,
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());

                this.omrsTopicListener = new SubjectAreaOMRSTopicListener(this.accessServiceConfig.getAccessServiceOutTopic(),
                        this.repositoryConnector.getRepositoryHelper(),
                        this.repositoryConnector.getRepositoryValidator(),
                        this.accessServiceConfig.getAccessServiceName());
                this.omrsTopicConnector.registerListener(this.omrsTopicListener);
            }

            this.auditLog = auditLog;
            this.serverUserName = serverUserName;

            auditCode = SubjectAreaAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());

            if (log.isDebugEnabled()) {
                log.debug("<== Method: " + methodName + ",userid="+ serverUserName);
            }
        } catch (Throwable error)
        {
            auditCode =SubjectAreaAuditCode.SERVICE_INSTANCE_FAILURE;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(error.getMessage()),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }
    }

    /**
     * Shutdown the subject area access service.
     */
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        log.debug(">>" + actionDescription);

        SubjectAreaAuditCode auditCode;

        auditCode = SubjectAreaAuditCode.SERVICE_TERMINATING;
        auditLog.logRecord(actionDescription, auditCode.getLogMessageId(), auditCode.getSeverity(), auditCode.getFormattedLogMessage(serverName), null, auditCode.getSystemAction(), auditCode.getUserAction());


        // TODO Look into what we need to do for termination
        this.repositoryConnector = null;
        this.accessServiceConfig = null;
        this.omrsTopicConnector = null;

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditCode = SubjectAreaAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription, auditCode.getLogMessageId(), auditCode.getSeverity(), auditCode.getFormattedLogMessage(serverName), null, auditCode.getSystemAction(), auditCode.getUserAction());

        log.debug("<<" + actionDescription);
    }
}

