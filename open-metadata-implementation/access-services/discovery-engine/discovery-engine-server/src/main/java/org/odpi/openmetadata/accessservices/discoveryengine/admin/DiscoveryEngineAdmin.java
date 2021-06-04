/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.admin;

import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineAuditCode;
import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineErrorCode;
import org.odpi.openmetadata.accessservices.discoveryengine.server.DiscoveryEngineServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DiscoveryEngineAdmin manages the start up and shutdown of the Discovery Engine OMAS.   During start up,
 * it validates the parameters and options it receives and sets up the service as requested.
 */
public class DiscoveryEngineAdmin extends AccessServiceAdmin
{
    private AuditLog                        auditLog       = null;
    private DiscoveryEngineServicesInstance instance       = null;
    private String                          serverName     = null;

    /**
     * Default constructor
     */
    public DiscoveryEngineAdmin()
    {
    }


    /**
     * Initialize the Discovery Engine access service.
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
        final String             actionDescription = "initialize";

        auditLog.logMessage(actionDescription, DiscoveryEngineAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;

        try
        {
            /*
             * The supported zones determines which assets can be queried/analysed by any discovery engine instance
             * connected to this instance of the Discovery Engine OMAS.  The default zones determines the zone that
             * any discovery service defined through this Discovery Engine OMAS's configuration interface is
             * set to.  Putting the discovery services in a different zone to the data assets means that they are
             * can be masked from view from users of other OMASs such as Asset Consumer OMAS.
             */
            List<String>   supportedZones = super.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
                                                                        accessServiceConfig.getAccessServiceName(),
                                                                        auditLog);

            List<String>  defaultZones = super.extractDefaultZones(accessServiceConfig.getAccessServiceOptions(),
                                                                   accessServiceConfig.getAccessServiceName(),
                                                                   auditLog);

            List<String>  publishZones = super.extractPublishZones(accessServiceConfig.getAccessServiceOptions(),
                                                                   accessServiceConfig.getAccessServiceName(),
                                                                   auditLog);

            /*
             * The instance is used to support REST API calls to this server instance.  It is given the
             * OutTopic connection for the client so that the client can query it to connect to the right
             * out topic.
             */
            this.instance = new DiscoveryEngineServicesInstance(repositoryConnector,
                                                                supportedZones,
                                                                defaultZones,
                                                                publishZones,
                                                                auditLog,
                                                                serverUserName,
                                                                repositoryConnector.getMaxPageSize());
            this.serverName = instance.getServerName();


            /*
             * Initialization is complete.  The service is now waiting for REST API calls (typically from the Discovery Server) and events
             * from OMRS to indicate that there are metadata changes.
             */
            auditLog.logMessage(actionDescription, DiscoveryEngineAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName));
        }
        catch (OMAGConfigurationErrorException error)
        {
            auditLog.logException(actionDescription,
                                  DiscoveryEngineAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  accessServiceConfig.toString(),
                                  error);
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  DiscoveryEngineAuditCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                    error.getMessage()),
                                  error);

            throw new OMAGConfigurationErrorException(
                    DiscoveryEngineErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                      AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceFullName(),
                                                                                                      serverName,
                                                                                                      error.getMessage()),
                                                      this.getClass().getName(),
                                                      actionDescription,
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

        if (this.instance != null)
        {
            this.instance.shutdown();
        }

        auditLog.logMessage(actionDescription, DiscoveryEngineAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}
