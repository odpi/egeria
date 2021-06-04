/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.admin;


import org.odpi.openmetadata.accessservices.dataplatform.ffdc.DataPlatformAuditCode;
import org.odpi.openmetadata.accessservices.dataplatform.server.DataPlatformServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


import java.util.List;

/**
 * DataPlatformAdmin manages the start up and shutdown of the Data Platform OMAS. During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class DataPlatformAdmin extends AccessServiceAdmin
{
    private AuditLog                      auditLog         = null;
    private DataPlatformServicesInstance  instance         = null;
    private OpenMetadataTopicConnector    inTopicConnector = null;
    private String                        serverName       = null;


    /**
     * Default constructor
     */
    public DataPlatformAdmin()
    {
    }

    
    /**
     * Initialize the access service.
     *
     * @param accessServiceConfig specific configuration properties for this access service.
     * @param omrsTopicConnector connector for receiving OMRS Events from the cohorts
     * @param repositoryConnector  connector for querying the cohort repositories
     * @param auditLog   audit log component for logging messages.
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

        final String actionDescription = "Initialize Data Platform OMAS service.";

        auditLog.logMessage(actionDescription, DataPlatformAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;

        try
        {
            List<String> supportedZones = this.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                                                                     accessServiceConfig.getAccessServiceName(),
                                                                     auditLog);

            List<String> defaultZones = this.extractDefaultZones(accessServiceConfig.getAccessServiceOptions(),
                                                                 accessServiceConfig.getAccessServiceName(),
                                                                 auditLog);

            List<String> publishZones = this.extractPublishZones(accessServiceConfig.getAccessServiceOptions(),
                                                                 accessServiceConfig.getAccessServiceName(),
                                                                 auditLog);

            this.instance   = new DataPlatformServicesInstance(repositoryConnector,
                                                               supportedZones,
                                                               defaultZones,
                                                               publishZones,
                                                               auditLog,
                                                               serverUserName,
                                                               repositoryConnector.getMaxPageSize(),
                                                               null);
            this.serverName = instance.getServerName();

            auditLog.logMessage(actionDescription,
                                DataPlatformAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName),
                                accessServiceConfig.toString());
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  DataPlatformAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.DATA_PLATFORM_OMAS.getAccessServiceFullName(),
                                                         error);
        }
    }



    /**
     * Shutdown the access service.
     */
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        try
        {
            if (inTopicConnector != null)
            {
                inTopicConnector.disconnect();
            }
        }
        catch (ConnectorCheckedException error)
        {
            auditLog.logException(actionDescription,
                                  DataPlatformAuditCode.SERVICE_INSTANCE_TERMINATION_FAILURE.getMessageDefinition(serverName),
                                  error);
        }

        if (instance != null)
        {
            instance.shutdown();
        }

        auditLog.logMessage(actionDescription, DataPlatformAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}

