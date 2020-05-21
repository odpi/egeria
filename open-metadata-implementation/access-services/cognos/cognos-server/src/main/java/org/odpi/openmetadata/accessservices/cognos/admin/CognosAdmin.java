/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.cognos.admin;


import java.util.List;

import org.odpi.openmetadata.accessservices.cognos.auditlog.CognosAuditCode;
import org.odpi.openmetadata.accessservices.cognos.ffdc.CognosErrorCode;
import org.odpi.openmetadata.accessservices.cognos.server.CognosServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * CognosAdmin is the class that is called by the OMAG Server to initialize and terminate
 * the Cognos OMAS.  The initialization call provides this OMAS with resources from the
 * Open Metadata Repository Services.
 * 
 *
 */
public class CognosAdmin extends AccessServiceAdmin
{

    private OpenMetadataTopicConnector cognosOutTopicConnector;
    private AuditLog auditLog;
    private String serverName = null;
    private CognosServicesInstance instance = null;


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig				specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector		connector for receiving OMRS Events from the cohorts
     * @param enterpriseConnector     			connector for querying the cohort repositories
     * @param auditLog                      	audit log component for logging messages.
     * @param serverUserName                    user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException	invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(	AccessServiceConfig accessServiceConfig, 
    						OMRSTopicConnector enterpriseOMRSTopicConnector, 
    						OMRSRepositoryConnector enterpriseConnector, 
    						AuditLog auditLog,
    						String serverUserName) throws OMAGConfigurationErrorException {
        
        final String actionDescription = "Initialize Cognos OMAS service.";

        auditLog.logMessage(actionDescription, CognosAuditCode.SERVICE_INITIALIZING.getMessageDefinition());
        this.auditLog = auditLog;

		if (enterpriseConnector != null) {
			serverName = enterpriseConnector.getServerName();
		}

        
        try {
            // initialize Topic Connector
        	String outTopicName = getTopicName(accessServiceConfig.getAccessServiceOutTopic());
            cognosOutTopicConnector =  super.getTopicConnector(accessServiceConfig.getAccessServiceOutTopic(),
            		OpenMetadataTopicConnector.class,
            		auditLog,
                    AccessServiceDescription.COGNOS_OMAS.getAccessServiceFullName(),
                    actionDescription);

            if (cognosOutTopicConnector != null) {
                startConnector(actionDescription, outTopicName, cognosOutTopicConnector);
            }

            List<String> supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                    accessServiceConfig.getAccessServiceName(), auditLog);

            instance = new CognosServicesInstance(enterpriseConnector, supportedZones,
                    auditLog, serverUserName,  enterpriseConnector.getMaxPageSize());

       	
        } catch (OMAGConfigurationErrorException error) {
            throw error;
        } catch (Exception error) {
            auditLog.logException(
            		actionDescription,
            		CognosAuditCode.getAuditLogMessageDefinition(
            				OMAGAdminErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(),
            				AccessServiceDescription.COGNOS_OMAS.getAccessServiceFullName(),
            				error.getClass().getName(),
            				error.getMessage()),
            		error);
            
            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.COGNOS_OMAS.getAccessServiceFullName(),
                                                         error);
        }
        
        
        auditLog.logMessage(actionDescription, CognosAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName));
    }

    /** 
     * Helper method to wrap start connector logic and error processing.
     * 
     * @param actionDescription parent method name providing context for error handling.
     * @param topicName to start
     * @param topicConnector to start
     * @throws OMAGConfigurationErrorException
     */
    private void startConnector(String actionDescription, String topicName, OpenMetadataTopicConnector topicConnector) throws OMAGConfigurationErrorException {

        auditLog.logMessage(actionDescription, CognosAuditCode.SERVICE_REGISTERED_WITH_COGNOS_OUT_TOPIC.getMessageDefinition(topicName));

        try {
            topicConnector.start();
        } catch (ConnectorCheckedException e) {
        	
            auditLog.logException(actionDescription, 
            		CognosAuditCode.getAuditLogMessageDefinition(
            			CognosErrorCode.ERROR_INITIALIZING_COGNOS_TOPIC_CONNECTION.getMessageDefinition(), topicName, serverName),
            		e);

            throw new OMAGConfigurationErrorException(
            		CognosErrorCode.ERROR_INITIALIZING_COGNOS_TOPIC_CONNECTION
            		.getMessageDefinition(topicName, serverName),
            		getClass().getSimpleName(),
            		actionDescription,
            		e);
        }
    }

    /**
     * Shutdown the access service.
     */
    @Override
    public void shutdown() {

    	final String actionDescription = "shutdown";
        
    	try {
        	cognosOutTopicConnector.disconnect();
        } catch (ConnectorCheckedException error) {
            auditLog.logException(actionDescription,
                    CognosAuditCode.SERVICE_INSTANCE_TERMINATION_FAILURE.getMessageDefinition(serverName),
                    error);
        }

        if (instance != null)
        {
            instance.shutdown();
        }
        
        auditLog.logMessage(actionDescription, CognosAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}
