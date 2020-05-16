/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.cognos.admin;


import java.util.List;

import org.odpi.openmetadata.accessservices.cognos.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.cognos.auditlog.CognosAuditCode;
import org.odpi.openmetadata.accessservices.cognos.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.cognos.ffdc.CognosErrorCode;
import org.odpi.openmetadata.accessservices.cognos.server.CognosServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CognosAdmin is the class that is called by the OMAG Server to initialize and terminate
 * the Cognos OMAS.  The initialization call provides this OMAS with resources from the
 * Open Metadata Repository Services.
 * 
 *
 */
public class CognosAdmin extends AccessServiceAdmin
{

    private static final Logger log = LoggerFactory.getLogger(CognosAdmin.class);
    private OpenMetadataTopicConnector cognosOutTopicConnector;
    private OMRSAuditLog auditLog;
    private String serverName = null;
    private CognosServicesInstance instance = null;


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties  specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector          connector for receiving OMRS Events from the cohorts
     * @param enterpriseConnector     connector for querying the cohort repositories
     * @param auditLog                              audit log component for logging messages.
     * @param serverUserName                        user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(AccessServiceConfig accessServiceConfigurationProperties, OMRSTopicConnector enterpriseOMRSTopicConnector, OMRSRepositoryConnector enterpriseConnector, OMRSAuditLog auditLog, String serverUserName) throws OMAGConfigurationErrorException {
        
    	final String actionDescription = "initialize";
        auditLog.logMessage(actionDescription, CognosAuditCode.SERVICE_INITIALIZING.getMessageDefinition());
        this.auditLog = auditLog;

		if (enterpriseConnector != null) {
			serverName = enterpriseConnector.getServerName();
		}

        String outTopicName = getTopicName(accessServiceConfigurationProperties.getAccessServiceOutTopic());
        cognosOutTopicConnector = initializeCognosTopicConnector(accessServiceConfigurationProperties.getAccessServiceOutTopic());
        
        
        List<String> supportedZones = extractSupportedZones(
        				accessServiceConfigurationProperties.getAccessServiceOptions(),
                        accessServiceConfigurationProperties.getAccessServiceName(),
                        auditLog);

        OMEntityDao omEntityDao = new OMEntityDao(enterpriseConnector, supportedZones, auditLog);

        if (cognosOutTopicConnector != null) {
            startConnector(CognosAuditCode.SERVICE_REGISTERED_WITH_COGNOS_OUT_TOPIC, actionDescription, outTopicName, cognosOutTopicConnector);
        }

        DatabaseContextHandler contextBuilders = new DatabaseContextHandler(omEntityDao);
        instance = new CognosServicesInstance(contextBuilders, serverName);
        
        auditLog.logMessage(actionDescription, CognosAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName));
    }

    private void startConnector(CognosAuditCode auditCode, String actionDescription, String topicName, OpenMetadataTopicConnector topicConnector) throws OMAGConfigurationErrorException {

        auditLog.logMessage(actionDescription, auditCode.getMessageDefinition(topicName));

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
     * Returns the topic created based on connection properties
     *
     * @param topicConnection  properties of the topic
     * @return the topic created based on the connection properties
     */
    private OpenMetadataTopicConnector initializeCognosTopicConnector(Connection topicConnection) {
        if (topicConnection != null) {
            try {
                return getTopicConnector(topicConnection);
            } catch (Exception e) {
		        final String actionDescription = "initialize";
                auditLog.logException(actionDescription, 
                		CognosAuditCode.ERROR_INITIALIZING_CONNECTION
                		.getMessageDefinition(topicConnection.toString(), serverName, e.getMessage()), e);

                throw e;
            }

        }
        return null;
    }


    /**
     * Returns the connector created from topic connection properties
     *
     * @param topicConnection  properties of the topic connection
     * @return the connector created based on the topic connection properties
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection topicConnection) {
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector) connectorBroker.getConnector(topicConnection);

            topicConnector.setAuditLog(auditLog.createNewAuditLog(OMRSAuditingComponent.OPEN_METADATA_TOPIC_CONNECTOR));

            return topicConnector;
        } catch (Throwable error) {
            String methodName = "getTopicConnector";

            throw new OMRSConfigErrorException(
            		CognosErrorCode.NULL_TOPIC_CONNECTOR.getMessageDefinition(methodName),
                    this.getClass().getName(),
                    methodName,
                    error);
        }
    }

    /**
     * Shutdown the access service.
     */
    public void shutdown() {

    	final String actionDescription = "shutdown";
        
    	try {
        	cognosOutTopicConnector.disconnect();
        } catch (ConnectorCheckedException e) {
            log.error("Error disconnecting cognos topic connector");
        }

        if (instance != null)
        {
            instance.shutdown();
        }
        
        auditLog.logMessage(actionDescription, CognosAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}
