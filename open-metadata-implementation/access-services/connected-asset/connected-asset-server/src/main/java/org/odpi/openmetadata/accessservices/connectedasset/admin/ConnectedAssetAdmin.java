/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.admin;

import org.odpi.openmetadata.accessservices.connectedasset.auditlog.ConnectedAssetAuditCode;
import org.odpi.openmetadata.accessservices.connectedasset.listener.ConnectedAssetOMRSTopicListener;
import org.odpi.openmetadata.accessservices.connectedasset.server.ConnectedAssetRESTServices;
import org.odpi.openmetadata.accessservices.connectedasset.server.ConnectedAssetServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * ConnectedAssetAdmin is the class that is called by the OMAG Server to initialize and terminate
 * the Connected Asset OMAS.  The initialization call provides this OMAS with resources from the
 * Open Metadata Repository Services.
 *
 */
public class ConnectedAssetAdmin implements AccessServiceAdmin
{
    private OMRSRepositoryConnector        repositoryConnector = null;
    private OMRSTopicConnector             omrsTopicConnector  = null;
    private AccessServiceConfig            accessServiceConfig = null;
    private OMRSAuditLog                   auditLog            = null;
    private ConnectedAssetServicesInstance instance            = null;
    private String                         serverName          = null;
    private String                         serverUserName      = null;

    private ConnectedAssetOMRSTopicListener omrsTopicListener = null;

    /**
     * Default constructor
     */
    public ConnectedAssetAdmin()
    {
    }

    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector connector for receiving OMRS Events from the cohorts
     * @param enterpriseOMRSRepositoryConnector connector for querying the cohort repositories
     * @param auditLog audit log component for logging messages.
     * @param serverUserName user id to use on OMRS calls where there is no end user - for example when
     *                       processing OMRS messages from the Enterprise OMRS Topic Connector.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    public void initialize(AccessServiceConfig     accessServiceConfigurationProperties,
                           OMRSTopicConnector      enterpriseOMRSTopicConnector,
                           OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                           OMRSAuditLog            auditLog,
                           String                  serverUserName) throws OMAGConfigurationErrorException
    {
        final String            actionDescription = "initialize";
        ConnectedAssetAuditCode auditCode;

        auditCode = ConnectedAssetAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        try
        {
            this.repositoryConnector = enterpriseOMRSRepositoryConnector;
            this.instance = new ConnectedAssetServicesInstance(enterpriseOMRSRepositoryConnector);
            this.serverName = instance.getServerName();

            this.accessServiceConfig = accessServiceConfigurationProperties;
            this.omrsTopicConnector = enterpriseOMRSTopicConnector;

            if (omrsTopicConnector != null)
            {
                auditCode = ConnectedAssetAuditCode.SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC;
                auditLog.logRecord(actionDescription,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(serverName),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());

                omrsTopicListener = new ConnectedAssetOMRSTopicListener(accessServiceConfig.getAccessServiceOutTopic(),
                                                                        repositoryConnector.getRepositoryHelper(),
                                                                        repositoryConnector.getRepositoryValidator(),
                                                                        accessServiceConfig.getAccessServiceName());

                omrsTopicConnector.registerListener(omrsTopicListener);
            }

            this.auditLog = auditLog;
            this.serverUserName = serverUserName;

            auditCode = ConnectedAssetAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(serverName),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
        catch (Throwable error)
        {
            auditCode = ConnectedAssetAuditCode.SERVICE_INSTANCE_FAILURE;
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
     * Shutdown the access service.
     */
    public void shutdown()
    {
        final String             actionDescription = "shutdown";
        ConnectedAssetAuditCode  auditCode;

        this.instance.shutdown();

        auditCode = ConnectedAssetAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(serverName),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }
}
