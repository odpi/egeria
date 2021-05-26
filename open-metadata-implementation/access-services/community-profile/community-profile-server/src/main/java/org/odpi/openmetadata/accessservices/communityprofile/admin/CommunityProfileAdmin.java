/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.admin;

import org.odpi.openmetadata.accessservices.communityprofile.connectors.outtopic.CommunityProfileOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.communityprofile.connectors.outtopic.CommunityProfileOutTopicServerProvider;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileAuditCode;
import org.odpi.openmetadata.accessservices.communityprofile.intopic.CommunityProfileInTopicProcessor;
import org.odpi.openmetadata.accessservices.communityprofile.omrstopic.CommunityProfileOMRSTopicProcessor;
import org.odpi.openmetadata.accessservices.communityprofile.server.CommunityProfileServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

public class CommunityProfileAdmin extends AccessServiceAdmin
{
    private AuditLog                           auditLog            = null;
    private CommunityProfileServicesInstance   instance            = null;
    private String                             serverName          = null;
    private CommunityProfileInTopicProcessor   inTopicProcessor    = null;


    /**
     * Default constructor
     */
    public CommunityProfileAdmin()
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
        final String              actionDescription = "initialize";

        auditLog.logMessage(actionDescription, CommunityProfileAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        this.auditLog = auditLog;

        try
        {
            /*
             * The supported zones determines which assets can be queried/analysed by any governance engine instance
             * connected to this instance of the Governance Engine OMAS.  The default zones determines the zone that
             * any governance service defined through this Governance Engine OMAS's configuration interface is
             * set to.  Putting the governance services in a different zone to the data assets means that they are
             * can be masked from view from users of other OMASs such as Asset Consumer OMAS.
             */
            List<String> supportedZones = super.extractSupportedZones(accessServiceConfig.getAccessServiceOptions(),
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
            this.instance = new CommunityProfileServicesInstance(repositoryConnector,
                                                                 supportedZones,
                                                                 defaultZones,
                                                                 publishZones,
                                                                 auditLog,
                                                                 serverUserName,
                                                                 repositoryConnector.getMaxPageSize(),
                                                                 super.extractKarmaPointPlateau(accessServiceConfig.getAccessServiceOptions(),
                                                                                                accessServiceConfig.getAccessServiceName(),
                                                                                                auditLog),
                                                                 accessServiceConfig.getAccessServiceOutTopic());
            this.serverName = instance.getServerName();

            OpenMetadataTopicConnector outTopicConnector = null;

            if (accessServiceConfig.getAccessServiceOutTopic() != null)
            {
                outTopicConnector = super.getOutTopicEventBusConnector(accessServiceConfig.getAccessServiceOutTopic(),
                                                                       AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceFullName(),
                                                                       auditLog);
            }

            OMRSTopicListener omrsTopicProcessor = new CommunityProfileOMRSTopicProcessor(outTopicConnector,
                                                                                          super.extractKarmaPointIncrement(accessServiceConfig.getAccessServiceOptions(),
                                                                                                                           accessServiceConfig.getAccessServiceName(),
                                                                                                                           auditLog),
                                                                                          AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceFullName(),
                                                                                          serverUserName,
                                                                                          auditLog.createNewAuditLog(OMRSAuditingComponent.ENTERPRISE_TOPIC_LISTENER),
                                                                                          repositoryConnector.getRepositoryHelper(),
                                                                                          instance);

            super.registerWithEnterpriseTopic(accessServiceConfig.getAccessServiceName(),
                                              serverName,
                                              omrsTopicConnector,
                                              omrsTopicProcessor,
                                              auditLog);



            if (accessServiceConfig.getAccessServiceInTopic() != null)
            {
                inTopicProcessor = new CommunityProfileInTopicProcessor(super.getInTopicEventBusConnector(accessServiceConfig.getAccessServiceInTopic(),
                                                                                                          AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceFullName(),
                                                                                                          auditLog),
                                                                        instance);
            }

            /*
             * This piece is setting up the server-side mechanism for the out topic.
             */
            Connection outTopicEventBusConnection = accessServiceConfig.getAccessServiceOutTopic();

            if (outTopicEventBusConnection != null)
            {
                Endpoint endpoint = outTopicEventBusConnection.getEndpoint();

                AuditLog outTopicAuditLog = auditLog.createNewAuditLog(OMRSAuditingComponent.OMAS_OUT_TOPIC);
                Connection serverSideOutTopicConnection = this.getOutTopicConnection(accessServiceConfig.getAccessServiceOutTopic(),
                                                                                     AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceFullName(),
                                                                                     CommunityProfileOutTopicServerProvider.class.getName(),
                                                                                     auditLog);
                CommunityProfileOutTopicServerConnector outTopicServerConnector = super.getTopicConnector(serverSideOutTopicConnection,
                                                                                                          CommunityProfileOutTopicServerConnector.class,
                                                                                                          outTopicAuditLog,
                                                                                                          AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceFullName(),
                                                                                                          actionDescription);

                this.registerWithEnterpriseTopic(AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName(),
                                                 serverName,
                                                 omrsTopicConnector,
                                                 omrsTopicProcessor,
                                                 auditLog);
            }

            /*
             * Initialization is complete.  The service is now waiting for REST API calls and events from OMRS to indicate
             * that there are metadata changes.
             */
            auditLog.logMessage(actionDescription, CommunityProfileAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName));
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  CommunityProfileAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  error);

            super.throwUnexpectedInitializationException(actionDescription,
                                                         AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceFullName(),
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

        if (inTopicProcessor != null)
        {
            inTopicProcessor.shutdown();
        }

        if (instance != null)
        {
            this.instance.shutdown();
        }

        auditLog.logMessage(actionDescription, CommunityProfileAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));
    }
}
