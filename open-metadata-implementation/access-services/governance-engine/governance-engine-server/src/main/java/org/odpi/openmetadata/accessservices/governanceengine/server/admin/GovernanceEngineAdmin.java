/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server.admin;

import org.odpi.openmetadata.accessservices.governanceengine.common.auditlog.GovernanceEngineAuditCode;
import org.odpi.openmetadata.accessservices.governanceengine.server.GovernanceEngineRESTServices;
import org.odpi.openmetadata.accessservices.governanceengine.server.listeners.GovernanceEngineOMRSTopicListener;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GovernanceEngineAdmin implements AccessServiceAdmin {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineAdmin.class);

    private AccessServiceConfig accessServiceConfig = null;
    private OMRSAuditLog auditLog = null;
    private String serverUserName = null;

    private OMRSRepositoryConnector repositoryConnector = null;
    private OMRSTopicConnector omrsTopicConnector = null;
    private GovernanceEngineOMRSTopicListener omrsTopicListener = null;

    //TODO Prevent multiple initialization/termination


    /**
     * Default constructor
     */
    public GovernanceEngineAdmin() {
    }

    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties - specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector         - connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector    - connector for querying the cohort repositories
     * @param auditLog                             - audit log component for logging messages.
     * @param serverUserName                       - user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException - invalid parameters in the configuration properties.
     */
    public synchronized void initialize(AccessServiceConfig accessServiceConfigurationProperties,
                           OMRSTopicConnector enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                           OMRSAuditLog auditLog,
                           String serverUserName) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";

        log.debug(">>" + actionDescription);

        GovernanceEngineAuditCode auditCode;

        auditCode = GovernanceEngineAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        this.repositoryConnector = enterpriseOMRSRepositoryConnector;
        GovernanceEngineRESTServices.setRepositoryConnector(accessServiceConfigurationProperties.getAccessServiceName(),
                repositoryConnector);

        this.accessServiceConfig = accessServiceConfigurationProperties;
        this.omrsTopicConnector = enterpriseOMRSTopicConnector;


        if (omrsTopicConnector != null) {
            auditCode = GovernanceEngineAuditCode.SERVICE_REGISTERED_WITH_TOPIC;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());

            omrsTopicListener = new GovernanceEngineOMRSTopicListener(accessServiceConfig.getAccessServiceOutTopic(),
                    repositoryConnector.getRepositoryHelper(),
                    repositoryConnector.getRepositoryValidator(),
                    accessServiceConfig.getAccessServiceName());
            omrsTopicConnector.registerListener(omrsTopicListener);
        }

        this.auditLog = auditLog;
        this.serverUserName = serverUserName;


        auditCode = GovernanceEngineAuditCode.SERVICE_INITIALIZED;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        log.debug("<<" + actionDescription);


    }


    /**
     * Shutdown the access service.
     */
    public synchronized void shutdown() {
        final String actionDescription = "shutdown";

        log.debug(">>" + actionDescription);

        GovernanceEngineAuditCode auditCode;

        auditCode = GovernanceEngineAuditCode.SERVICE_TERMINATING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());


        // TODO Look into what we need to do for termination
        this.repositoryConnector = null;
        //TODO Clear repository connector?
        // GovernanceEngineRESTServices.setRepositoryConnector
        // (accessServiceConfigurationProperties
        // .getAccessServiceName(),
        //        null);

        this.accessServiceConfig =null;
        this.omrsTopicConnector = null;



        auditCode = GovernanceEngineAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        log.debug("<<" + actionDescription);


    }

    //TODO Add String representation
    //@Override
    //public String toString() {
    //    return "ServerURL"+OMASServer;
    //}
}


