/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.admin;


import org.odpi.openmetadata.accessservices.informationview.auditlog.InformationViewAuditCode;
import org.odpi.openmetadata.accessservices.informationview.context.ContextBuilders;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.registration.RegistrationHandler;
import org.odpi.openmetadata.accessservices.informationview.reports.DataViewHandler;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.reports.ReportHandler;
import org.odpi.openmetadata.accessservices.informationview.eventprocessor.EventPublisher;
import org.odpi.openmetadata.accessservices.informationview.listeners.InformationViewEnterpriseOmrsEventListener;
import org.odpi.openmetadata.accessservices.informationview.server.InformationViewServicesInstance;
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

import java.util.List;
import java.util.Map;


public class InformationViewAdmin extends AccessServiceAdmin
{

    private static final Logger log = LoggerFactory.getLogger(InformationViewAdmin.class);
    private OpenMetadataTopicConnector informationViewOutTopicConnector;
    private OMRSAuditLog auditLog;
    private String serverName = null;
    private InformationViewServicesInstance instance = null;


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
        InformationViewAuditCode auditCode;

        auditCode = InformationViewAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        this.auditLog = auditLog;

        if (enterpriseConnector != null)
        {
            serverName = enterpriseConnector.getServerName();
        }

        String outTopicName = getTopicName(accessServiceConfigurationProperties.getAccessServiceOutTopic());
        informationViewOutTopicConnector = initializeInformationViewTopicConnector(accessServiceConfigurationProperties.getAccessServiceOutTopic());
        List<String> supportedZones = this.extractSupportedZones(accessServiceConfigurationProperties.getAccessServiceOptions());
        OMEntityDao omEntityDao = new OMEntityDao(enterpriseConnector, supportedZones, auditLog);

        EventPublisher eventPublisher = null;
        if (enterpriseOMRSTopicConnector != null) {
            auditCode = InformationViewAuditCode.SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());

            eventPublisher = new EventPublisher(informationViewOutTopicConnector, enterpriseConnector, supportedZones, auditLog);
            InformationViewEnterpriseOmrsEventListener informationViewEnterpriseOmrsEventListener = new InformationViewEnterpriseOmrsEventListener(eventPublisher, auditLog);
            enterpriseOMRSTopicConnector.registerListener(informationViewEnterpriseOmrsEventListener, accessServiceConfigurationProperties.getAccessServiceName());
        }

        if (informationViewOutTopicConnector != null) {
            startConnector(InformationViewAuditCode.SERVICE_REGISTERED_WITH_IV_OUT_TOPIC, actionDescription, outTopicName, informationViewOutTopicConnector);
        }

        LookupHelper lookupHelper = new LookupHelper(enterpriseConnector, omEntityDao, auditLog);
        DataViewHandler dataViewHandler = new DataViewHandler(omEntityDao, lookupHelper, enterpriseConnector.getRepositoryHelper(), auditLog);
        ReportHandler reportHandler = new ReportHandler(omEntityDao, lookupHelper, enterpriseConnector.getRepositoryHelper(), auditLog);
        RegistrationHandler registrationHandler = new RegistrationHandler(omEntityDao, enterpriseConnector, auditLog);
        ContextBuilders contextBuilders = new ContextBuilders(enterpriseConnector, omEntityDao, auditLog);
        instance = new InformationViewServicesInstance(reportHandler, dataViewHandler, registrationHandler, contextBuilders, serverName);

        auditCode = InformationViewAuditCode.SERVICE_INITIALIZED;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName),
                accessServiceConfigurationProperties.toString(),
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }

    private void startConnector(InformationViewAuditCode auditCode, String actionDescription, String topicName, OpenMetadataTopicConnector topicConnector) throws OMAGConfigurationErrorException {

        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(topicName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());


        try {
            topicConnector.start();
        } catch (ConnectorCheckedException e) {
            auditCode = InformationViewAuditCode.ERROR_INITIALIZING_INFORMATION_VIEW_TOPIC_CONNECTION;
            auditLog.logException(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(topicName, serverName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
            throw new OMAGConfigurationErrorException(400,
                    InformationViewAdmin.class.getSimpleName(),
                    actionDescription,
                    auditCode.getFormattedLogMessage(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
        }
    }




    /**
     * Returns the topic created based on connection properties
     *
     * @param topicConnection  properties of the topic
     * @return the topic created based on the connection properties
     */
    private OpenMetadataTopicConnector initializeInformationViewTopicConnector(Connection topicConnection) {
        final String actionDescription = "initialize";
        if (topicConnection != null) {
            try {
                return getTopicConnector(topicConnection);
            } catch (Exception e) {
                InformationViewAuditCode auditCode = InformationViewAuditCode.ERROR_INITIALIZING_CONNECTION;
                auditLog.logException(actionDescription,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(topicConnection.toString(), serverName, e.getMessage()),
                        topicConnection.toString(),
                        auditCode.getSystemAction(),
                        auditCode.getUserAction(),
                        e);
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

            if (log.isDebugEnabled()) {
                log.debug("Unable to create topic connector: " + error.toString());
            }

            InformationViewErrorCode errorCode = InformationViewErrorCode.NULL_TOPIC_CONNECTOR;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage("getTopicConnector");

            throw new OMRSConfigErrorException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    error);

        }
    }


    private List<String> extractSupportedZones(Map<String, Object> accessServiceOptions) throws OMAGConfigurationErrorException
    {
        final String           methodName = "extractSupportedZones";
        InformationViewAuditCode auditCode;

        if (accessServiceOptions == null)
        {
            return null;
        }
        else
        {
            Object   zoneListObject = accessServiceOptions.get(supportedZonesPropertyName);

            if (zoneListObject == null)
            {
                auditCode = InformationViewAuditCode.ALL_ZONES;
                auditLog.logRecord(methodName,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(),
                        null,
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());
                return null;
            }
            else
            {
                try
                {
                    List<String>  zoneList =  (List<String>)zoneListObject;

                    auditCode = InformationViewAuditCode.SUPPORTED_ZONES;
                    auditLog.logRecord(methodName,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(zoneList.toString()),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());

                    return zoneList;
                }
                catch (Throwable error)
                {
                    auditCode = InformationViewAuditCode.BAD_CONFIG;
                    auditLog.logRecord(methodName,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(zoneListObject.toString(), supportedZonesPropertyName),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());

                    InformationViewErrorCode errorCode    = InformationViewErrorCode.BAD_CONFIG;
                    String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(zoneListObject.toString(),
                            supportedZonesPropertyName,
                            error.getClass().getName(),
                            error.getMessage());

                    throw new OMAGConfigurationErrorException(errorCode.getHttpErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction(),
                            error);
                }
            }
        }
    }



    /**
     * Shutdown the access service.
     */
    public void shutdown() {
        try {
            informationViewOutTopicConnector.disconnect();
        } catch (ConnectorCheckedException e) {
            log.error("Error disconnecting information view topic connector");
        }

        if (instance != null)
        {
            instance.shutdown();
        }

        final String actionDescription = "shutdown";
        InformationViewAuditCode auditCode;

        auditCode = InformationViewAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }
}
