/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.infrastructure.connector;

import org.odpi.openmetadata.accessservices.itinfrastructure.api.ITInfrastructureEventListener;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.*;
import org.odpi.openmetadata.frameworks.governanceaction.client.ActionControlInterface;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.ControlFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.DataFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.LineageMappingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.ProcessCallProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.CapabilityDeploymentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * InfrastructureIntegratorContext provides a wrapper around the IT Infrastructure OMAS client.
 * It provides the simplified interface to open metadata needed by the InfrastructureIntegratorConnector.
 */
public class InfrastructureIntegratorContext extends IntegrationContext
{
    private final CapabilityManagerClient     capabilityManagerClient;
    private final ConnectionManagerClient     connectionManagerClient;
    private final ConnectorTypeManagerClient  connectorTypeManagerClient;
    private final DataAssetManagerClient      dataAssetManagerClient;
    private final EndpointManagerClient       endpointManagerClient;
    private final HostManagerClient           hostManagerClient;
    private final ActorProfileManagement      actorProfileManagement;
    private final ContactMethodManagement      contactMethodManagement;
    private final UserIdentityManagement      userIdentityClient;
    private final PlatformManagerClient       platformManagerClient;
    private final ProcessManagerClient        processManagerClient;
    private final ServerManagerClient         serverManagerClient;
    private final ITInfrastructureEventClient eventClient;

    private boolean     externalSourceIsHome = true;

    static final String assetTypeName         = "Asset";


    /**
     * Create a new context for a connector.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param serverName name of the integration daemon
     * @param openIntegrationClient client for calling the metadata server
     * @param governanceConfiguration client for managing catalog targets
     * @param openMetadataStoreClient client for calling the metadata server
     * @param actionControlInterface client for initiating governance actions
     * @param capabilityManagerClient client for software capabilities
     * @param connectionManagerClient client for connections
     * @param connectorTypeManagerClient client for connector types
     * @param dataAssetManagerClient clients for data stores, data sets and data feeds
     * @param endpointManagerClient client for endpoints
     * @param hostManagerClient client for hosts
     * @param actorProfileManagement client for IT profiles
     * @param contactMethodManagement client for IT profiles
     * @param userIdentityClient client form managing user identities
     * @param platformManagerClient client for software platforms
     * @param processManagerClient client for processes
     * @param serverManagerClient client for software servers
     * @param eventClient client for receiving events
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization the direction of integration permitted by the integration connector
     * @param integrationConnectorGUID unique identifier for the integration connector if it is started via an integration group (otherwise it is
     *                                 null).
     * @param externalSourceGUID unique identifier of the software server capability for the asset manager
     * @param externalSourceName unique name of the software server capability for the asset manager
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public InfrastructureIntegratorContext(String                       connectorId,
                                           String                       connectorName,
                                           String                       connectorUserId,
                                           String                       serverName,
                                           OpenIntegrationClient        openIntegrationClient,
                                           GovernanceConfiguration      governanceConfiguration,
                                           OpenMetadataClient           openMetadataStoreClient,
                                           ActionControlInterface       actionControlInterface,
                                           CapabilityManagerClient      capabilityManagerClient,
                                           ConnectionManagerClient      connectionManagerClient,
                                           ConnectorTypeManagerClient   connectorTypeManagerClient,
                                           DataAssetManagerClient       dataAssetManagerClient,
                                           EndpointManagerClient        endpointManagerClient,
                                           HostManagerClient            hostManagerClient,
                                           ActorProfileManagement       actorProfileManagement,
                                           ContactMethodManagement      contactMethodManagement,
                                           UserIdentityManagement       userIdentityClient,
                                           PlatformManagerClient        platformManagerClient,
                                           ProcessManagerClient         processManagerClient,
                                           ServerManagerClient          serverManagerClient,
                                           ITInfrastructureEventClient  eventClient,
                                           boolean                      generateIntegrationReport,
                                           PermittedSynchronization     permittedSynchronization,
                                           String                       integrationConnectorGUID,
                                           String                       externalSourceGUID,
                                           String                       externalSourceName,
                                           AuditLog                     auditLog,
                                           int                          maxPageSize)
    {
        super(connectorId,
              connectorName,
              connectorUserId,
              serverName,
              openIntegrationClient,
              governanceConfiguration,
              openMetadataStoreClient,
              actionControlInterface,
              generateIntegrationReport,
              permittedSynchronization,
              externalSourceGUID,
              externalSourceName,
              integrationConnectorGUID,
              auditLog,
              maxPageSize);

        this.capabilityManagerClient    = capabilityManagerClient;
        this.connectionManagerClient    = connectionManagerClient;
        this.connectorTypeManagerClient = connectorTypeManagerClient;
        this.dataAssetManagerClient     = dataAssetManagerClient;
        this.endpointManagerClient      = endpointManagerClient;
        this.hostManagerClient          = hostManagerClient;
        this.actorProfileManagement     = actorProfileManagement;
        this.contactMethodManagement    = contactMethodManagement;
        this.userIdentityClient         = userIdentityClient;
        this.platformManagerClient      = platformManagerClient;
        this.processManagerClient       = processManagerClient;
        this.serverManagerClient        = serverManagerClient;
        this.eventClient                = eventClient;
    }


    /* ========================================================
     * Set up whether metadata is owned by the infrastructure manager
     */


    /**
     * Set up the flag that controls the ownership of metadata created for this infrastructure manager. Default is true.
     *
     * @param externalSourceIsHome should the metadata be marked as owned by the infrastructure manager so others can not update?
     */
    public void setInfrastructureManagerIsHome(boolean externalSourceIsHome)
    {
        this.externalSourceIsHome = externalSourceIsHome;
    }


    /* ========================================================
     * Register for inbound events from the IT Infrastructure OMAS OutTopic
     */


    /**
     * Register a listener object that will be passed each of the events published by
     * the IT Infrastructure OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(ITInfrastructureEventListener listener) throws InvalidParameterException,
                                                                                ConnectionCheckedException,
                                                                                ConnectorCheckedException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }


    /* =====================================================================================================================
     * The host describes the computer or container that provides the operating system for the platforms.
     */


    /**
     * Create a new metadata element to represent a host.
     *
     * @param hostProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createHost(HostProperties hostProperties) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        String hostGUID = hostManagerClient.createHost(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, hostProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(hostGUID);
        }

        return hostGUID;
    }


    /**
     * Create a new metadata element to represent a host using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createHostFromTemplate(String             templateGUID,
                                         TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        String hostGUID = hostManagerClient.createHostFromTemplate(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, templateGUID, templateProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(hostGUID);
        }

        return hostGUID;
    }


    /**
     * Update the metadata element representing a host.
     *
     * @param hostGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param hostProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateHost(String         hostGUID,
                           boolean        isMergeUpdate,
                           HostProperties hostProperties) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        hostManagerClient.updateHost(userId, externalSourceGUID, externalSourceName, hostGUID, isMergeUpdate, hostProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(hostGUID);
        }
    }


    /**
     * Create a relationship between a host and a cluster member host.
     *
     * @param hostGUID unique identifier of the host
     * @param clusterMemberGUID unique identifier of the cluster member host
     * @param effectiveFrom time when this hosting is effective - null means immediately
     * @param effectiveTo time when this hosting is no longer effective - null means forever
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupClusterMember(String  hostGUID,
                                   String  clusterMemberGUID,
                                   Date    effectiveFrom,
                                   Date    effectiveTo) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        hostManagerClient.setupClusterMember(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, hostGUID, clusterMemberGUID, effectiveFrom, effectiveTo);
    }


    /**
     * Remove a relationship between a host and a cluster member host.
     *
     * @param hostGUID unique identifier of the host
     * @param clusterMemberGUID unique identifier of the cluster member host
     * @param effectiveTime time when the hosting is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearClusterMember(String hostGUID,
                                   String clusterMemberGUID,
                                   Date   effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        hostManagerClient.clearClusterMember(userId, externalSourceGUID, externalSourceName, hostGUID, clusterMemberGUID, effectiveTime);
    }



    /**
     * Update the zones for the host asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param hostGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishHost(String hostGUID) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException
    {
        hostManagerClient.publishHost(userId, hostGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(hostGUID);
        }
    }


    /**
     * Update the zones for the host asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the host is first created).
     *
     * @param hostGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawHost(String hostGUID) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException
    {
        hostManagerClient.withdrawHost(userId, hostGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(hostGUID);
        }
    }


    /**
     * Remove the metadata element representing a host.
     *
     * @param hostGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeHost(String hostGUID) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        hostManagerClient.removeHost(userId, externalSourceGUID, externalSourceName, hostGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(hostGUID);
        }
    }



    /**
     * Retrieve the list of host metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<HostElement> findHosts(String searchString,
                                       Date   effectiveTime,
                                       int    startFrom,
                                       int    pageSize) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        return hostManagerClient.findHosts(userId, searchString, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the list of host metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<HostElement> getHostsByName(String name,
                                            Date   effectiveTime,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return hostManagerClient.getHostsByName(userId, name, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the list of hosts created by this caller.
     *
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<HostElement> getHostsForInfrastructureManager(Date   effectiveTime,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return hostManagerClient.getHostsForInfrastructureManager(userId, externalSourceGUID, externalSourceName, effectiveTime, startFrom,
                                                                  pageSize);
    }


    /**
     * Return the list of cluster members associated with a host.
     *
     * @param hostGUID unique identifier of the host to query
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<HostElement> getClusterMembersForHost(String hostGUID,
                                                      Date   effectiveTime,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return hostManagerClient.getClusterMembersForHost(userId, hostGUID, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the host metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public HostElement getHostByGUID(String guid) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        return hostManagerClient.getHostByGUID(userId, guid);
    }

    

    /* =====================================================================================================================
     * The platform runs on the host.
     */


    /**
     * Create a new metadata element to represent a platform.
     *
     * @param platformProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSoftwareServerPlatform(SoftwareServerPlatformProperties platformProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        String platformGUID = platformManagerClient.createSoftwareServerPlatform(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, platformProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(platformGUID);
        }

        return platformGUID;
    }


    /**
     * Create a new metadata element to represent a platform using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSoftwareServerPlatformFromTemplate(String             templateGUID,
                                                           TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        String platformGUID = platformManagerClient.createSoftwareServerPlatformFromTemplate(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, templateGUID, templateProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(platformGUID);
        }

        return platformGUID;
    }


    /**
     * Update the metadata element representing a platform.
     *
     * @param platformGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param platformProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSoftwareServerPlatform(String                           platformGUID,
                                             boolean                          isMergeUpdate,
                                             SoftwareServerPlatformProperties platformProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException

    {
        platformManagerClient.updateSoftwareServerPlatform(userId, externalSourceGUID, externalSourceName, platformGUID, isMergeUpdate, platformProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(platformGUID);
        }
    }


    /**
     * Update the zones for the platform asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param platformGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishSoftwareServerPlatform(String platformGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        platformManagerClient.publishSoftwareServerPlatform(userId, platformGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(platformGUID);
        }
    }


    /**
     * Update the zones for the platform asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the platform is first created).
     *
     * @param platformGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawSoftwareServerPlatform(String platformGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException

    {
        platformManagerClient.withdrawSoftwareServerPlatform(userId, platformGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(platformGUID);
        }
    }


    /**
     * Remove the metadata element representing a platform.
     *
     * @param platformGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSoftwareServerPlatform(String platformGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException

    {
        platformManagerClient.removeSoftwareServerPlatform(userId, externalSourceGUID, externalSourceName, platformGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(platformGUID);
        }
    }



    /**
     * Retrieve the list of platform metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SoftwareServerPlatformElement> findSoftwareServerPlatforms(String searchString,
                                                                           Date   effectiveTime,
                                                                           int    startFrom,
                                                                           int    pageSize) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return platformManagerClient.findSoftwareServerPlatforms(userId, searchString, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the list of platform metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SoftwareServerPlatformElement> getSoftwareServerPlatformsByName(String name,
                                                                                Date   effectiveTime,
                                                                                int    startFrom,
                                                                                int    pageSize) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException

    {
        return platformManagerClient.getSoftwareServerPlatformsByName(userId, name, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the list of platforms created by this caller.
     *
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SoftwareServerPlatformElement> getSoftwareServerPlatformsForInfrastructureManager(Date   effectiveTime,
                                                                                                  int    startFrom,
                                                                                                  int    pageSize) throws InvalidParameterException,
                                                                                                                          UserNotAuthorizedException,
                                                                                                                          PropertyServerException

    {
        return platformManagerClient.getSoftwareServerPlatformsForInfrastructureManager(userId, externalSourceGUID, externalSourceName, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the platform metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SoftwareServerPlatformElement getSoftwareServerPlatformByGUID(String guid) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return platformManagerClient.getSoftwareServerPlatformByGUID(userId, guid);
    }




    /* =====================================================================================================================
     * The software server is an IT Infrastructure asset
     */

    /**
     * Create a new metadata element to represent a software server.
     *
     * @param softwareServerProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSoftwareServer(SoftwareServerProperties softwareServerProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        String softwareServerGUID = serverManagerClient.createSoftwareServer(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, softwareServerProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(softwareServerGUID);
        }

        return softwareServerGUID;
    }


    /**
     * Create a new metadata element to represent a software server using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSoftwareServerFromTemplate(String             templateGUID,
                                                   TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException

    {
        String softwareServerGUID = serverManagerClient.createSoftwareServerFromTemplate(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, templateGUID, templateProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(softwareServerGUID);
        }

        return softwareServerGUID;
    }


    /**
     * Update the metadata element representing a software server.
     *
     * @param softwareServerGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param softwareServerProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSoftwareServer(String                   softwareServerGUID,
                                     boolean                  isMergeUpdate,
                                     SoftwareServerProperties softwareServerProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        serverManagerClient.updateSoftwareServer(userId, externalSourceGUID, externalSourceName, softwareServerGUID, isMergeUpdate, softwareServerProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(softwareServerGUID);
        }
    }


    /**
     * Update the zones for the software server asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param softwareServerGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishSoftwareServer(String softwareServerGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        serverManagerClient.publishSoftwareServer(userId, softwareServerGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(softwareServerGUID);
        }
    }


    /**
     * Update the zones for the software server asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the softwareServer is first created).
     *
     * @param softwareServerGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawSoftwareServer(String softwareServerGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        serverManagerClient.withdrawSoftwareServer(userId, softwareServerGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(softwareServerGUID);
        }
    }


    /**
     * Remove the metadata element representing a software server.
     *
     * @param softwareServerGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSoftwareServer(String softwareServerGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        serverManagerClient.removeSoftwareServer(userId, externalSourceGUID, externalSourceName, softwareServerGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(softwareServerGUID);
        }
    }


    /**
     * Retrieve the list of software server metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param effectiveTime time that the element is effective
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SoftwareServerElement> findSoftwareServers(String searchString,
                                                           Date   effectiveTime,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return serverManagerClient.findSoftwareServers(userId, searchString, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the list of softwareServer metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param effectiveTime time that the element is effective
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SoftwareServerElement> getSoftwareServersByName(String name,
                                                                Date   effectiveTime,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException

    {
        return serverManagerClient.getSoftwareServersByName(userId, name, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the list of software servers created by this caller.
     *
     * @param effectiveTime time that the element is effective
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SoftwareServerElement> getSoftwareServersForInfrastructureManager(Date   effectiveTime,
                                                                                  int    startFrom,
                                                                                  int    pageSize) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException

    {
        return serverManagerClient.getSoftwareServersForInfrastructureManager(userId, externalSourceGUID, externalSourceName, effectiveTime,
                                                                              startFrom, pageSize);
    }


    /**
     * Retrieve the softwareServer metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SoftwareServerElement getSoftwareServerByGUID(String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException

    {
        return serverManagerClient.getSoftwareServerByGUID(userId, guid);
    }

    /*
     * Server purposes
     */


    /**
     * Add a Server Purpose classification to an IT asset.
     *
     * @param itAssetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param effectiveFrom when should relationship be effective - null means immediately
     * @param effectiveTo when should relationship no longer be effective - null means never
     * @param classificationProperties properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addServerPurpose(String              itAssetGUID,
                                 String              classificationName,
                                 Date                effectiveFrom,
                                 Date                effectiveTo,
                                 Map<String, Object> classificationProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        serverManagerClient.addServerPurpose(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, itAssetGUID, classificationName, effectiveFrom, effectiveTo, classificationProperties);
    }


    /**
     * Update the properties of a classification for an asset.
     *
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param effectiveFrom when should relationship be effective - null means immediately
     * @param effectiveTo when should relationship no longer be effective - null means never
     * @param isMergeUpdate   should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param classificationProperties properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateServerPurpose(String              assetGUID,
                                    String              classificationName,
                                    Date                effectiveFrom,
                                    Date                effectiveTo,
                                    boolean             isMergeUpdate,
                                    Map<String, Object> classificationProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        serverManagerClient.updateServerPurpose(userId, externalSourceGUID, externalSourceName, assetTypeName, assetGUID, classificationName, effectiveFrom, effectiveTo, isMergeUpdate, classificationProperties);
    }


    /**
     * Remove a server purpose classification.
     *
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param effectiveTime effective time of the classification to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearServerPurpose(String assetGUID,
                                   String classificationName,
                                   Date   effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        serverManagerClient.clearServerPurpose(userId, externalSourceGUID, externalSourceName, assetTypeName, assetGUID, classificationName, effectiveTime);
    }


    /**
     * Create a relationship that represents the deployment of an IT infrastructure asset to a specific deployment destination (another asset).
     *
     * @param itAssetGUID unique identifier of the IT infrastructure asset
     * @param destinationGUID unique identifier of the destination where the asset is being deployed to
     * @param deploymentProperties relationship properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void deployITAsset(String               itAssetGUID,
                              String               destinationGUID,
                              DeploymentProperties deploymentProperties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        serverManagerClient.deployITAsset(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, itAssetGUID, destinationGUID, deploymentProperties);
    }


    /**
     * Update a deployment relationship.
     *
     * @param deploymentGUID unique identifier of the relationship
     * @param isMergeUpdate             should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param deploymentProperties properties for the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateITAssetDeployment(String               deploymentGUID,
                                        boolean              isMergeUpdate,
                                        DeploymentProperties deploymentProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        serverManagerClient.updateITAssetDeployment(userId, externalSourceGUID, externalSourceName, deploymentGUID, isMergeUpdate, deploymentProperties);
    }


    /**
     * Remove a deployment relationship.
     *
     * @param itAssetGUID unique identifier of the IT infrastructure asset
     * @param destinationGUID unique identifier of the destination where the asset is being deployed to
     * @param effectiveTime time when the deployment is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearDeployment(String itAssetGUID,
                                String destinationGUID,
                                Date   effectiveTime) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        serverManagerClient.clearDeployment(userId, externalSourceGUID, externalSourceName, itAssetGUID, destinationGUID, effectiveTime);
    }


    /**
     * Return the list of assets deployed on a particular destination.
     *
     * @param destinationGUID unique identifier of the destination asset to query
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DeploymentElement> getDeployedITAssets(String destinationGUID,
                                                       Date   effectiveTime,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return serverManagerClient.getDeployedITAssets(userId, destinationGUID, effectiveTime, startFrom, pageSize);
    }


    /**
     * Return the list of destinations that a particular IT infrastructure asset is deployed to.
     *
     * @param itAssetGUID unique identifier of the IT infrastructure asset to query
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DeploymentElement> getDeploymentDestinations(String itAssetGUID,
                                                             Date   effectiveTime,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return serverManagerClient.getDeploymentDestinations(userId, itAssetGUID, effectiveTime, startFrom, pageSize);
    }


    /* =====================================================================================================================
     * The software capability describes functions of the hosting server.
     */

    /**
     * Create a new metadata element to represent a software server capability.
     *
     * @param classificationName optional classification name that refines the type of the software server capability.
     * @param capabilityProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSoftwareCapability(String                       classificationName,
                                           SoftwareCapabilityProperties capabilityProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        String capabilityGUID = capabilityManagerClient.createSoftwareCapability(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, classificationName, capabilityProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(capabilityGUID);
        }

        return capabilityGUID;
    }


    /**
     * Create a new metadata element to represent a software capability using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSoftwareCapabilityFromTemplate(String             templateGUID,
                                                       TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        String capabilityGUID = capabilityManagerClient.createSoftwareCapabilityFromTemplate(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, templateGUID, templateProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(capabilityGUID);
        }

        return capabilityGUID;
    }


    /**
     * Update the metadata element representing a software capability.
     *
     * @param capabilityGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param capabilityProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSoftwareCapability(String                       capabilityGUID,
                                         boolean                      isMergeUpdate,
                                         SoftwareCapabilityProperties capabilityProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        capabilityManagerClient.updateSoftwareCapability(userId, externalSourceGUID, externalSourceName, capabilityGUID, isMergeUpdate, capabilityProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(capabilityGUID);
        }
    }


    /**
     * Link a software capability to a software server.
     *
     * @param capabilityGUID unique identifier of the software server capability
     * @param infrastructureAssetGUID unique identifier of the software server
     * @param deploymentProperties describes the deployment of the capability onto the server
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void deployCapability(String                         capabilityGUID,
                                 String                         infrastructureAssetGUID,
                                 CapabilityDeploymentProperties deploymentProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        capabilityManagerClient.deployCapability(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, capabilityGUID, infrastructureAssetGUID, deploymentProperties);
    }


    /**
     * Update the properties of a server capability's deployment.
     *
     * @param deploymentGUID unique identifier of the relationship
     * @param deploymentProperties describes the deployment of the capability onto the server
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateCapabilityDeployment(String                         deploymentGUID,
                                           boolean                        isMergeUpdate,
                                           CapabilityDeploymentProperties deploymentProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        capabilityManagerClient.updateCapabilityDeployment(userId, externalSourceGUID, externalSourceName, deploymentGUID, isMergeUpdate, deploymentProperties);
    }


    /**
     * Remove the link between a software server capability and a software server.
     *
     * @param itAssetGUID unique identifier of the software server/platform/host
     * @param capabilityGUID unique identifier of the software server capability
     * @param effectiveTime time that the relationship is effective
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeCapabilityDeployment(String itAssetGUID,
                                           String capabilityGUID,
                                           Date   effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        capabilityManagerClient.removeCapabilityDeployment(userId, externalSourceGUID, externalSourceName, itAssetGUID, capabilityGUID, effectiveTime);
    }



    /**
     * Remove the metadata element representing a software capability.
     *
     * @param capabilityGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSoftwareCapability(String capabilityGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        capabilityManagerClient.removeSoftwareCapability(userId, externalSourceGUID, externalSourceName, capabilityGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(capabilityGUID);
        }
    }


    /**
     * Retrieve the list of software capability metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SoftwareCapabilityElement> findSoftwareCapabilities(String searchString,
                                                                    Date   effectiveTime,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return capabilityManagerClient.findSoftwareCapabilities(userId, searchString, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the list of software capability metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SoftwareCapabilityElement> getSoftwareCapabilitiesByName(String name,
                                                                         Date   effectiveTime,
                                                                         int    startFrom,
                                                                         int    pageSize) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return capabilityManagerClient.getSoftwareCapabilitiesByName(userId, name, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the IT asset metadata elements where the software with the supplied unique identifier is deployed.
     *
     * @param guid unique identifier of the requested metadata element
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related IT Assets
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RelatedAssetElement> getSoftwareCapabilityDeployments(String guid,
                                                                      Date   effectiveTime,
                                                                      int    startFrom,
                                                                      int    pageSize) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return capabilityManagerClient.getSoftwareCapabilityDeployments(userId, guid, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the software capabilities that are deployed to an IT asset.
     *
     * @param itAssetGUID unique identifier of the hosting metadata element
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related IT Assets
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SoftwareCapabilityElement> getDeployedSoftwareCapabilities(String itAssetGUID,
                                                                           Date   effectiveTime,
                                                                           int    startFrom,
                                                                           int    pageSize) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return capabilityManagerClient.getDeployedSoftwareCapabilities(userId, itAssetGUID, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the list of capabilities created by this caller.
     *
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SoftwareCapabilityElement> getSoftwareCapabilitiesForInfrastructureManager(Date   effectiveTime,
                                                                                           int    startFrom,
                                                                                           int    pageSize) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        return capabilityManagerClient.getSoftwareCapabilitiesForInfrastructureManager(userId, externalSourceGUID, externalSourceName,
                                                                                       effectiveTime, startFrom, pageSize);
    }



    /**
     * Retrieve the software capability metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SoftwareCapabilityElement getSoftwareCapabilityByGUID(String guid) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return capabilityManagerClient.getSoftwareCapabilityByGUID(userId, guid);
    }


    /*
     * ======================================================================================
     * A software server capability works with assets
     */

    /**
     * Create a new metadata relationship to represent the use of an asset by a software server capability.
     *
     * @param capabilityGUID unique identifier of a software server capability
     * @param assetGUID unique identifier of an asset
     * @param properties properties about the ServerAssetUse relationship
     *
     * @return unique identifier of the new ServerAssetUse relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createServerAssetUse(String                   capabilityGUID,
                                       String                   assetGUID,
                                       ServerAssetUseProperties properties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return capabilityManagerClient.createServerAssetUse(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, capabilityGUID, assetGUID, properties);
    }


    /**
     * Update the metadata relationship to represent the use of an asset by a software server capability.
     *
     * @param serverAssetUseGUID unique identifier of the relationship between a software server capability and an asset
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the ServerAssetUse relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateServerAssetUse(String                   serverAssetUseGUID,
                                     boolean                  isMergeUpdate,
                                     ServerAssetUseProperties properties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        capabilityManagerClient.updateServerAssetUse(userId, externalSourceGUID, externalSourceName, serverAssetUseGUID, isMergeUpdate, properties);
    }


    /**
     * Remove the metadata relationship to represent the use of an asset by a software server capability.
     *
     * @param serverAssetUseGUID unique identifier of the relationship between a software server capability and an asset
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeServerAssetUse(String serverAssetUseGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        capabilityManagerClient.removeServerAssetUse(userId, externalSourceGUID, externalSourceName, serverAssetUseGUID);
    }


    /**
     * Return the list of server asset use relationships associated with a software server capability.
     *
     * @param capabilityGUID unique identifier of the software server capability to query
     * @param useType value to search for.  Null means all use types.
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ServerAssetUseElement> getServerAssetUsesForCapability(String             capabilityGUID,
                                                                       ServerAssetUseType useType,
                                                                       Date               effectiveTime,
                                                                       int                startFrom,
                                                                       int                pageSize) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return capabilityManagerClient.getServerAssetUsesForCapability(userId, capabilityGUID, useType, effectiveTime, startFrom, pageSize);
    }


    /**
     * Return the list of software server capabilities that make use of a specific asset.
     *
     * @param assetGUID unique identifier of the asset to query
     * @param useType Optionally restrict the search to a specific user type.  Null means all use types.
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ServerAssetUseElement> getCapabilityUsesForAsset(String             assetGUID,
                                                                 ServerAssetUseType useType,
                                                                 Date               effectiveTime,
                                                                 int                startFrom,
                                                                 int                pageSize) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        return capabilityManagerClient.getCapabilityUsesForAsset(userId, assetGUID, useType, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the list of relationships between a specific software server capability and a specific asset.
     *
     * @param capabilityGUID unique identifier of a software server capability
     * @param assetGUID unique identifier of an asset
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ServerAssetUseElement> getServerAssetUsesForElements(String capabilityGUID,
                                                                     String assetGUID,
                                                                     Date   effectiveTime,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return capabilityManagerClient.getServerAssetUsesForElements(userId, capabilityGUID, assetGUID, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the server asset use type relationship with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ServerAssetUseElement getServerAssetUseByGUID(String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return capabilityManagerClient.getServerAssetUseByGUID(userId, guid);
    }





    /*
     * ==========================================================
     * The Data Asset entity is the top level element to describe a data source such as a data store or data set.
     */

    /**
     * Create a new metadata element to represent the root of an asset.
     *
     * @param dataAssetProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataAsset(DataAssetProperties dataAssetProperties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        String assetGUID = dataAssetManagerClient.createDataAsset(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, dataAssetProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(assetGUID);
        }

        return assetGUID;
    }


    /**
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataAssetFromTemplate(String             templateGUID,
                                              TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException

    {
        String assetGUID = dataAssetManagerClient.createDataAssetFromTemplate(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, templateGUID, templateProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(assetGUID);
        }

        return assetGUID;
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param assetGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param dataAssetProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDataAsset(String              assetGUID,
                                boolean             isMergeUpdate,
                                DataAssetProperties dataAssetProperties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        dataAssetManagerClient.updateDataAsset(userId, externalSourceGUID, externalSourceName, assetGUID, isMergeUpdate, dataAssetProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(assetGUID);
        }
    }



    /**
     * Classify the asset to indicate that it can be used as reference data.
     *
     * @param assetGUID unique identifier of the metadata element to update
     * @param effectiveFrom when should classification be effective - null means immediately
     * @param effectiveTo when should classification no longer be effective - null means never
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setDataAssetAsReferenceData(String assetGUID,
                                            Date   effectiveFrom,
                                            Date   effectiveTo) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        dataAssetManagerClient.setDataAssetAsReferenceData(userId, externalSourceGUID, externalSourceName, assetGUID, effectiveFrom, effectiveTo);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(assetGUID);
        }
    }


    /**
     * Remove the reference data designation from the asset.
     *
     * @param assetGUID unique identifier of the metadata element to update
     * @param effectiveTime time when the classification is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearDataAssetAsReferenceData(String assetGUID,
                                              Date   effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        dataAssetManagerClient.clearDataAssetAsReferenceData(userId, externalSourceGUID, externalSourceName, assetGUID, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(assetGUID);
        }
    }


    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param assetGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDataAsset(String assetGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException

    {
        dataAssetManagerClient.publishDataAsset(userId, assetGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(assetGUID);
        }
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param assetGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDataAsset(String assetGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException

    {
        dataAssetManagerClient.withdrawDataAsset(userId, assetGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(assetGUID);
        }
    }


    /**
     * Remove the metadata element representing an asset.  This will delete the asset and all anchored
     * elements such as schema and comments.
     *
     * @param assetGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDataAsset(String assetGUID) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        dataAssetManagerClient.removeDataAsset(userId, externalSourceGUID, externalSourceName, assetGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(assetGUID);
        }
    }


    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> findDataAssets(String searchString,
                                                 Date   effectiveTime,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException

    {
        return dataAssetManagerClient.findDataAssets(userId, searchString, effectiveTime, startFrom, pageSize);
    }



    /**
     * Retrieve the list of asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> getDataAssetsByName(String name,
                                                      Date   effectiveTime,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException

    {
        return dataAssetManagerClient.getDataAssetsByName(userId, name, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the list of assets created on behalf of the named infrastructure manager.
     *
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> getDataAssetsForInfrastructureManager(Date   effectiveTime,
                                                                        int    startFrom,
                                                                        int    pageSize) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException

    {
        return dataAssetManagerClient.getDataAssetsForInfrastructureManager(userId, externalSourceGUID, externalSourceName, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the asset metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataAssetElement getDataAssetByGUID(String guid) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        return dataAssetManagerClient.getDataAssetByGUID(userId, guid);
    }




    /* =====================================================================================================================
     * A process describes a well-defined series of steps that gets something done.
     */

    /**
     * Create a new metadata element to represent a process.
     *
     * @param processStatus initial status of the process
     * @param processProperties properties about the process to store
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createProcess(ProcessStatus     processStatus,
                                ProcessProperties processProperties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        String processGUID = processManagerClient.createProcess(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, processStatus, processProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(processGUID);
        }

        return processGUID;
    }


    /**
     * Create a new metadata element to represent a process using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createProcessFromTemplate(String             templateGUID,
                                            TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException

    {
        String processGUID = processManagerClient.createProcessFromTemplate(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, templateGUID, templateProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(processGUID);
        }

        return processGUID;
    }


    /**
     * Update the metadata element representing a process.
     *
     * @param processGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param processProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcess(String            processGUID,
                              boolean           isMergeUpdate,
                              ProcessProperties processProperties) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException

    {
        processManagerClient.updateProcess(userId, externalSourceGUID, externalSourceName, processGUID, isMergeUpdate, processProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(processGUID);
        }
    }


    /**
     * Update the status of the metadata element representing a process.
     *
     * @param processGUID unique identifier of the process to update
     * @param processStatus new status for the process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcessStatus(String        processGUID,
                                    ProcessStatus processStatus) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        processManagerClient.updateProcessStatus(userId, externalSourceGUID, externalSourceName, processGUID, processStatus);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(processGUID);
        }
    }


    /**
     * Create a parent-child relationship between two processes.
     *
     * @param parentProcessGUID unique identifier of the process in the external infrastructure manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external infrastructure manager that is to be the nested sub-process
     * @param containmentType describes the ownership of the sub-process
     * @param effectiveFrom time when this relationship is effective - null means immediately
     * @param effectiveTo time when this relationship is no longer effective - null means forever
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProcessParent(String                 parentProcessGUID,
                                   String                 childProcessGUID,
                                   ProcessContainmentType containmentType,
                                   Date                   effectiveFrom,
                                   Date                   effectiveTo) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        processManagerClient.setupProcessParent(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, parentProcessGUID, childProcessGUID, containmentType, effectiveFrom, effectiveTo);
    }


    /**
     * Remove a parent-child relationship between two processes.
     *
     * @param parentProcessGUID unique identifier of the process in the external infrastructure manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external infrastructure manager that is to be the nested sub-process
     * @param effectiveTime time when the relationship is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessParent(String parentProcessGUID,
                                   String childProcessGUID,
                                   Date   effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        processManagerClient.clearProcessParent(userId, externalSourceGUID, externalSourceName, parentProcessGUID, childProcessGUID, effectiveTime);
    }


    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param processGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishProcess(String processGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        processManagerClient.publishProcess(userId, processGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(processGUID);
        }
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the host is first created).
     *
     * @param processGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawProcess(String processGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException

    {
        processManagerClient.withdrawProcess(userId, processGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(processGUID);
        }
    }


    /**
     * Remove the metadata element representing a process.
     *
     * @param processGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeProcess(String processGUID) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        processManagerClient.removeProcess(userId, externalSourceGUID, externalSourceName, processGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(processGUID);
        }
    }


    /**
     * Retrieve the list of process metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement> findProcesses(String searchString,
                                              Date   effectiveTime,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return processManagerClient.findProcesses(userId, searchString, effectiveTime, startFrom, pageSize);
    }



    /**
     * Retrieve the list of process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement> getProcessesByName(String name,
                                                   Date   effectiveTime,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return processManagerClient.getProcessesByName(userId, name, effectiveTime, startFrom, pageSize);
    }


    /**
     * Return the list of processes associated with the infrastructure manager.
     *
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the processes associated with the requested infrastructure manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement> getProcessesForInfrastructureManager(Date   effectiveTime,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException

    {
        return processManagerClient.getProcessesForInfrastructureManager(userId, externalSourceGUID, externalSourceName, effectiveTime, startFrom, pageSize);
    }



    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElement getProcessByGUID(String processGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return processManagerClient.getProcessByGUID(userId, processGUID);
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param processGUID unique identifier of the requested metadata element
     * @param effectiveTime effective time for the query
     *
     * @return parent process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElement getProcessParent(String processGUID,
                                           Date   effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return processManagerClient.getProcessParent(userId, processGUID, effectiveTime);
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param processGUID unique identifier of the requested metadata element
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement> getSubProcesses(String processGUID,
                                                Date   effectiveTime,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return processManagerClient.getSubProcesses(userId, processGUID, effectiveTime, startFrom, pageSize);
    }


    /* ===============================================================================
     * General linkage and classifications
     */


    /**
     * Classify a port, process or asset as "BusinessSignificant" (this may affect the way that lineage is displayed).
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param effectiveFrom time when this hosting is effective - null means immediately
     * @param effectiveTo time when this hosting is no longer effective - null means forever
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setBusinessSignificant(String elementGUID,
                                       Date   effectiveFrom,
                                       Date   effectiveTo) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        processManagerClient.setBusinessSignificant(userId, externalSourceGUID, externalSourceName, elementGUID, effectiveFrom, effectiveTo);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove the "BusinessSignificant" designation from the element.
     *
     * @param effectiveTime effective time for the query
     * @param elementGUID unique identifier of the metadata element to update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearBusinessSignificant(String elementGUID,
                                         Date   effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        processManagerClient.clearBusinessSignificant(userId, externalSourceGUID, externalSourceName, elementGUID, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Link two elements together to show that data flows from one to the other.
     *
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupDataFlow(String             dataSupplierGUID,
                                String             dataConsumerGUID,
                                DataFlowProperties properties,
                                Date               effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException

    {
        return processManagerClient.setupDataFlow(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, dataSupplierGUID, dataConsumerGUID, properties, effectiveTime);
    }


    /**
     * Retrieve the data flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one data flow relationships between these two elements since it is used to disambiguate
     * the request. This is often used in conjunction with update.
     *
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFlowElement getDataFlow(String dataSupplierGUID,
                                       String dataConsumerGUID,
                                       String qualifiedName,
                                       Date   effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException

    {
        return processManagerClient.getDataFlow(userId, dataSupplierGUID, dataConsumerGUID, qualifiedName, effectiveTime);
    }


    /**
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDataFlow(String             dataFlowGUID,
                               DataFlowProperties properties,
                               Date               effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException

    {
        processManagerClient.updateDataFlow(userId, externalSourceGUID, externalSourceName, dataFlowGUID, properties, effectiveTime);
    }


    /**
     * Remove the data flow relationship between two elements.
     *
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param effectiveTime time when the relationship is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearDataFlow(String dataFlowGUID,
                              Date   effectiveTime) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException

    {
        processManagerClient.clearDataFlow(userId, externalSourceGUID, externalSourceName, dataFlowGUID, effectiveTime);
    }


    /**
     * Retrieve the data flow relationships linked from a specific element to the downstream consumers.
     *
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param effectiveTime time when the hosting is effective
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFlowElement> getDataFlowConsumers(String dataSupplierGUID,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException

    {
        return processManagerClient.getDataFlowConsumers(userId, dataSupplierGUID, startFrom, pageSize, effectiveTime);
    }


    /**
     * Retrieve the data flow relationships linked from a specific element to the upstream suppliers.
     *
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFlowElement> getDataFlowSuppliers(String dataConsumerGUID,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException

    {
        return processManagerClient.getDataFlowSuppliers(userId, dataConsumerGUID, startFrom, pageSize, effectiveTime);
    }


    /**
     * Link two elements to show that when one completes the next is started.
     *
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier for the control flow relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupControlFlow(String                 currentStepGUID,
                                   String                 nextStepGUID,
                                   ControlFlowProperties properties,
                                   Date                   effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException

    {
        return processManagerClient.setupControlFlow(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, currentStepGUID, nextStepGUID, properties, effectiveTime);
    }


    /**
     * Retrieve the control flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one control flow relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ControlFlowElement getControlFlow(String currentStepGUID,
                                             String nextStepGUID,
                                             String qualifiedName,
                                             Date   effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException

    {
        return processManagerClient.getControlFlow(userId, currentStepGUID, nextStepGUID, qualifiedName, effectiveTime);
    }


    /**
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateControlFlow(String                controlFlowGUID,
                                  ControlFlowProperties properties,
                                  Date                  effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException

    {
        processManagerClient.updateControlFlow(userId, externalSourceGUID, externalSourceName, controlFlowGUID, properties, effectiveTime);
    }


    /**
     * Remove the control flow relationship between two elements.
     *
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param effectiveTime time when the relationship is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearControlFlow(String controlFlowGUID,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException

    {
        processManagerClient.clearControlFlow(userId, externalSourceGUID, externalSourceName, controlFlowGUID, effectiveTime);
    }


    /**
     * Retrieve the control relationships linked from a specific element to the possible next elements in the process.
     *
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ControlFlowElement> getControlFlowNextSteps(String currentStepGUID,
                                                            int    startFrom,
                                                            int    pageSize,
                                                            Date   effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException

    {
        return processManagerClient.getControlFlowNextSteps(userId, currentStepGUID, startFrom, pageSize, effectiveTime);
    }


    /**
     * Retrieve the control relationships linked from a specific element to the possible previous elements in the process.
     *
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ControlFlowElement> getControlFlowPreviousSteps(String currentStepGUID,
                                                                int    startFrom,
                                                                int    pageSize,
                                                                Date   effectiveTime) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException

    {
        return processManagerClient.getControlFlowPreviousSteps(userId, currentStepGUID, startFrom, pageSize, effectiveTime);
    }


    /**
     * Link two elements together to show a request-response call between them.
     *
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupProcessCall(String                callerGUID,
                                   String                calledGUID,
                                   ProcessCallProperties properties,
                                   Date                  effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException

    {
        return processManagerClient.setupProcessCall(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, callerGUID, calledGUID, properties, effectiveTime);
    }


    /**
     * Retrieve the process call relationship between two elements.  The qualifiedName is optional unless there
     * is more than one process call relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessCallElement getProcessCall(String callerGUID,
                                             String calledGUID,
                                             String qualifiedName,
                                             Date   effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException

    {
        return processManagerClient.getProcessCall(userId, callerGUID, calledGUID, qualifiedName, effectiveTime);
    }


    /**
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param processCallGUID unique identifier of the process call relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcessCall(String                processCallGUID,
                                  ProcessCallProperties properties,
                                  Date                  effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        processManagerClient.updateProcessCall(userId, externalSourceGUID, externalSourceName, processCallGUID, properties, effectiveTime);
    }


    /**
     * Remove the process call relationship.
     *
     * @param processCallGUID unique identifier of the process call relationship
     * @param effectiveTime time when the relationship is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessCall(String processCallGUID,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException

    {
        processManagerClient.clearProcessCall(userId, externalSourceGUID, externalSourceName, processCallGUID, effectiveTime);
    }


    /**
     * Retrieve the process call relationships linked from a specific element to the elements it calls.
     *
     * @param callerGUID unique identifier of the element that is making the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessCallElement> getProcessCalled(String callerGUID,
                                                     int    startFrom,
                                                     int    pageSize,
                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return processManagerClient.getProcessCalled(userId, callerGUID, startFrom, pageSize, effectiveTime);
    }


    /**
     * Retrieve the process call relationships linked from a specific element to its callers.
     *
     * @param calledGUID unique identifier of the element that is processing the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessCallElement> getProcessCallers(String calledGUID,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return processManagerClient.getProcessCallers(userId, calledGUID, startFrom, pageSize, effectiveTime);
    }


    /**
     * Link two elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically, the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupLineageMapping(String                   sourceElementGUID,
                                    String                   destinationElementGUID,
                                    LineageMappingProperties properties,
                                    Date                     effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        processManagerClient.setupLineageMapping(userId, sourceElementGUID, destinationElementGUID, properties, effectiveTime);
    }


    /**
     * Retrieve the lineage mapping relationship between two elements.  The qualifiedName is optional unless there
     * is more than one relationship between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public LineageMappingElement getLineageMapping(String sourceElementGUID,
                                                   String destinationElementGUID,
                                                   String qualifiedName,
                                                   Date   effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException

    {
        return processManagerClient.getLineageMapping(userId, sourceElementGUID, destinationElementGUID, qualifiedName, effectiveTime);
    }


    /**
     * Update the lineage mapping relationship between two elements.
     *
     * @param lineageMappingGUID unique identifier of the relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateLineageMapping(String                   lineageMappingGUID,
                                     LineageMappingProperties properties,
                                     Date                     effectiveTime) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        processManagerClient.updateLineageMapping(userId, externalSourceGUID, externalSourceName, lineageMappingGUID, properties, effectiveTime);
    }


    /**
     * Remove the lineage mapping between two elements.
     *
     * @param lineageMappingGUID unique identifier of the source
     * @param effectiveTime time when the relationship is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearLineageMapping(String lineageMappingGUID,
                                    Date   effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        processManagerClient.clearLineageMapping(userId, lineageMappingGUID, effectiveTime);
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific source element to its destinations.
     *
     * @param sourceElementGUID unique identifier of the source
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return list of lineage mapping relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<LineageMappingElement> getDestinationLineageMappings(String sourceElementGUID,
                                                                     int    startFrom,
                                                                     int    pageSize,
                                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return processManagerClient.getDestinationLineageMappings(userId, sourceElementGUID, startFrom, pageSize, effectiveTime);
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific destination element to its sources.
     *
     * @param destinationElementGUID unique identifier of the destination
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return list of lineage mapping relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<LineageMappingElement> getSourceLineageMappings(String destinationElementGUID,
                                                                int    startFrom,
                                                                int    pageSize,
                                                                Date   effectiveTime) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return processManagerClient.getSourceLineageMappings(userId, destinationElementGUID, startFrom, pageSize, effectiveTime);
    }



    /* =====================================================================================================================
     * A Connection is the top level object for working with connectors
     */

    /**
     * Create a new metadata element to represent a connection.
     *
     * @param connectionProperties properties about the connection to store
     *
     * @return unique identifier of the new connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnection(ConnectionProperties connectionProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        String connectionGUID = connectionManagerClient.createConnection(userId, externalSourceGUID, externalSourceName, connectionProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(connectionGUID);
        }

        return connectionGUID;
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectionFromTemplate(String             templateGUID,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        String connectionGUID = connectionManagerClient.createConnectionFromTemplate(userId, externalSourceGUID, externalSourceName, templateGUID, templateProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(connectionGUID);
        }

        return connectionGUID;
    }


    /**
     * Update the metadata element representing a connection.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param connectionGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectionProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateConnection(String               connectionGUID,
                                 boolean              isMergeUpdate,
                                 ConnectionProperties connectionProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        connectionManagerClient.updateConnection(userId, externalSourceGUID, externalSourceName, connectionGUID, isMergeUpdate, connectionProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectionGUID);
        }
    }


    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupConnectorType(String  connectionGUID,
                                   String  connectorTypeGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        connectionManagerClient.setupConnectorType(userId, externalSourceGUID, externalSourceName, connectionGUID, connectorTypeGUID);
    }


    /**
     * Remove a relationship between a connection and a connector type.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearConnectorType(String connectionGUID,
                                   String connectorTypeGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        connectionManagerClient.clearConnectorType(userId, externalSourceGUID, externalSourceName, connectionGUID, connectorTypeGUID);
    }


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupEndpoint(String  connectionGUID,
                              String  endpointGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        connectionManagerClient.setupEndpoint(userId, externalSourceGUID, externalSourceName, connectionGUID, endpointGUID);
    }


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearEndpoint(String connectionGUID,
                              String endpointGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        connectionManagerClient.clearEndpoint(userId, externalSourceGUID, externalSourceName, connectionGUID, endpointGUID);
    }


    /**
     * Create a relationship between a virtual connection and an embedded connection.
     *
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param position which order should this connection be processed
     * @param arguments What additional properties should be passed to the embedded connector via the configuration properties
     * @param displayName what does this connector signify?
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupEmbeddedConnection(String              connectionGUID,
                                        int                 position,
                                        String              displayName,
                                        Map<String, Object> arguments,
                                        String              embeddedConnectionGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        connectionManagerClient.setupEmbeddedConnection(userId, externalSourceGUID, externalSourceName, connectionGUID, position, displayName, arguments, embeddedConnectionGUID);
    }


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearEmbeddedConnection(String connectionGUID,
                                        String embeddedConnectionGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        connectionManagerClient.clearEmbeddedConnection(userId, externalSourceGUID, externalSourceName, connectionGUID, embeddedConnectionGUID);
    }


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the  connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAssetConnection(String  assetGUID,
                                     String  connectionGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        connectionManagerClient.setupAssetConnection(userId, externalSourceGUID, externalSourceName, assetGUID, connectionGUID);
    }


    /**
     * Remove a relationship between an asset and its connection.
     *
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAssetConnection(String assetGUID,
                                     String connectionGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        connectionManagerClient.clearAssetConnection(userId, externalSourceGUID, externalSourceName, assetGUID, connectionGUID);
    }


    /**
     * Remove the metadata element representing a connection.
     *
     * @param connectionGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnection(String connectionGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        connectionManagerClient.removeConnection(userId, externalSourceGUID, externalSourceName, connectionGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(connectionGUID);
        }
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> findConnections(String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return connectionManagerClient.findConnections(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectionElement> getConnectionsByName(String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return connectionManagerClient.getConnectionsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param connectionGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionElement getConnectionByGUID(String connectionGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return connectionManagerClient.getConnectionByGUID(userId, connectionGUID);
    }

    /*
     * ========================================================
     * Infrastructure Assets are connected to an endpoint
     */


    /**
     * Create a new metadata element to represent an endpoint
     *
     * @param infrastructureGUID unique identifier of the infrastructure to connect it to (optional)
     * @param endpointProperties properties about the endpoint to store
     *
     * @return unique identifier of the new endpoint
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpoint(String             infrastructureGUID,
                                 EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        String endpointGUID = endpointManagerClient.createEndpoint(userId, null, null, infrastructureGUID, endpointProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(endpointGUID);
        }

        return endpointGUID;
    }


    /**
     * Create a new metadata element to represent a endpoint using an existing metadata element as a template.
     *
     * @param infrastructureGUID unique identifier of the infrastructure to connect it to (optional)
     * @param networkAddress location of the endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties descriptive properties that override the template
     *
     * @return unique identifier of the new endpoint
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpointFromTemplate(String             infrastructureGUID,
                                             String             networkAddress,
                                             String             templateGUID,
                                             TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String endpointGUID = endpointManagerClient.createEndpointFromTemplate(userId, null, null, infrastructureGUID, networkAddress, templateGUID, templateProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(endpointGUID);
        }

        return endpointGUID;
    }


    /**
     * Update the metadata element representing an endpoint.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param endpointGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateEndpoint(boolean            isMergeUpdate,
                               String             endpointGUID,
                               EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        endpointManagerClient.updateEndpoint(userId, externalSourceGUID, externalSourceName, isMergeUpdate, endpointGUID, endpointProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(endpointGUID);
        }
    }


    /**
     * Remove the metadata element representing a endpoint.
     *
     * @param endpointGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeEndpoint(String endpointGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        endpointManagerClient.removeEndpoint(userId, externalSourceGUID, externalSourceName, endpointGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(endpointGUID);
        }
    }


    /**
     * Retrieve the list of endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> findEndpoints(String searchString,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return endpointManagerClient.findEndpoints(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> getEndpointsByName(String name,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return endpointManagerClient.getEndpointsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching networkAddress.
     * There are no wildcards supported on this request.
     *
     * @param networkAddress networkAddress to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> getEndpointsByNetworkAddress(String networkAddress,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return endpointManagerClient.getEndpointsByNetworkAddress(userId, networkAddress, startFrom, pageSize);
    }


    /**
     * Retrieve the list of endpoint metadata elements that are attached to a specific infrastructure element.
     *
     * @param infrastructureGUID element to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<EndpointElement> getEndpointsForInfrastructure(String infrastructureGUID,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return endpointManagerClient.getEndpointsForInfrastructure(userId, infrastructureGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the endpoint metadata element with the supplied unique identifier.
     *
     * @param endpointGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointElement getEndpointByGUID(String endpointGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return endpointManagerClient.getEndpointByGUID(userId, endpointGUID);
    }


    /*
     * ========================================================
     * Connector types describe the implementation of a connector
     */


    /**
     * Create a new metadata element to represent an connectorType
     *
     * @param connectorTypeProperties properties about the connector type to store
     *
     * @return unique identifier of the new connectorType
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectorType(ConnectorTypeProperties connectorTypeProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        String connectorTypeGUID = connectorTypeManagerClient.createConnectorType(userId, externalSourceGUID, externalSourceName, connectorTypeProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectorTypeGUID);
        }

        return connectorTypeGUID;
    }


    /**
     * Create a new metadata element to represent an connectorType using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties descriptive properties that override the template
     *
     * @return unique identifier of the new connectorType
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectorTypeFromTemplate(String             templateGUID,
                                                  TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        String connectorTypeGUID = connectorTypeManagerClient.createConnectorTypeFromTemplate(userId, externalSourceGUID, externalSourceName, templateGUID, templateProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectorTypeGUID);
        }

        return connectorTypeGUID;
    }


    /**
     * Update the metadata element representing an connectorType.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param connectorTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectorTypeProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateConnectorType(boolean                 isMergeUpdate,
                                    String                  connectorTypeGUID,
                                    ConnectorTypeProperties connectorTypeProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        connectorTypeManagerClient.updateConnectorType(userId, externalSourceGUID, externalSourceName, isMergeUpdate, connectorTypeGUID, connectorTypeProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(connectorTypeGUID);
        }
    }


    /**
     * Remove the metadata element representing an connectorType.
     *
     * @param connectorTypeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnectorType(String connectorTypeGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        connectorTypeManagerClient.removeConnectorType(userId, externalSourceGUID, externalSourceName, connectorTypeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(connectorTypeGUID);
        }
    }


    /**
     * Retrieve the list of connector type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> findConnectorTypes(String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return connectorTypeManagerClient.findConnectorTypes(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of connector type metadata elements with a matching qualified name, display name or
     * connector provider class name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ConnectorTypeElement> getConnectorTypesByName(String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return connectorTypeManagerClient.getConnectorTypesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the connector type metadata element with the supplied unique identifier.
     *
     * @param connectorTypeGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypeElement getConnectorTypeByGUID(String connectorTypeGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return connectorTypeManagerClient.getConnectorTypeByGUID(userId, connectorTypeGUID);
    }


    /*
     * IT profiles for infrastructure
     */


    /**
     * Create a new actor profile.
     *
     * @param anchorGUID                   unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                                     or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor                  boolean flag to day that the element should be classified as its own anchor once its element
     *                                     is created in the repository.
     * @param anchorScopeGUID              unique identifier of any anchor scope to use for searching
     * @param properties                   properties for the new element.
     * @param parentGUID                   unique identifier of optional parent entity
     * @param parentRelationshipTypeName   type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1                 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createActorProfile(String                 anchorGUID,
                                     boolean                isOwnAnchor,
                                     String                 anchorScopeGUID,
                                     ActorProfileProperties properties,
                                     String                 parentGUID,
                                     String                 parentRelationshipTypeName,
                                     ElementProperties      parentRelationshipProperties,
                                     boolean                parentAtEnd1,
                                     boolean                forLineage,
                                     boolean                forDuplicateProcessing,
                                     Date                   effectiveTime) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        String actorProfileGUID = actorProfileManagement.createActorProfile(userId, externalSourceGUID, externalSourceName, anchorGUID, isOwnAnchor, anchorScopeGUID, properties, parentGUID, parentRelationshipTypeName, parentRelationshipProperties, parentAtEnd1, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(actorProfileGUID);
        }

        return actorProfileGUID;
    }


    /**
     * Update the properties of an actor profile.
     *
     * @param actorProfileGUID       unique identifier of the actor profile (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the element.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateActorProfile(String                 actorProfileGUID,
                                   boolean                replaceAllProperties,
                                   ActorProfileProperties properties,
                                   boolean                forLineage,
                                   boolean                forDuplicateProcessing,
                                   Date                   effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        actorProfileManagement.updateActorProfile(userId, externalSourceGUID, externalSourceName, actorProfileGUID, replaceAllProperties, properties, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(actorProfileGUID);
        }
    }

    /**
     * Attach a profile to a location.
     *
     * @param locationGUID           unique identifier of the location
     * @param actorProfileGUID       unique identifier of the actor profile
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkLocationToProfile(String                    actorProfileGUID,
                                      String                    locationGUID,
                                      ProfileLocationProperties relationshipProperties,
                                      boolean                   forLineage,
                                      boolean                   forDuplicateProcessing,
                                      Date                      effectiveTime) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        actorProfileManagement.linkLocationToProfile(userId, externalSourceGUID, externalSourceName, actorProfileGUID, locationGUID, relationshipProperties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Detach an actor profile from a location.
     *
     * @param locationGUID           unique identifier of the parent actor profile.
     * @param actorProfileGUID            unique identifier of the nested actor profile.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachLocationFromProfile(String  actorProfileGUID,
                                          String  locationGUID,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        actorProfileManagement.detachLocationFromProfile(userId, externalSourceGUID, externalSourceName, actorProfileGUID, locationGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Attach an asset to an IT profile.
     *
     * @param assetGUID       unique identifier of the asset
     * @param itProfileGUID            unique identifier of the IT profile
     * @param relationshipProperties description of the relationship.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAssetToProfile(String                            assetGUID,
                                   String                            itProfileGUID,
                                   ITInfrastructureProfileProperties relationshipProperties,
                                   boolean                           forLineage,
                                   boolean                           forDuplicateProcessing,
                                   Date                              effectiveTime) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        actorProfileManagement.linkAssetToProfile(userId, externalSourceGUID, externalSourceName, assetGUID, itProfileGUID, relationshipProperties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Detach an asset from an IT profile.
     *
     * @param assetGUID              unique identifier of the asset
     * @param itProfileGUID          unique identifier of the IT profile
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAssetFromProfile(String  assetGUID,
                                       String  itProfileGUID,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        actorProfileManagement.detachAssetFromProfile(userId, externalSourceGUID, externalSourceName, assetGUID, itProfileGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Delete a actor profile.
     *
     * @param actorProfileGUID       unique identifier of the element
     * @param cascadedDelete         can the actor profile be deleted if it has nested components linked to it?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteActorProfile(String  actorProfileGUID,
                                   boolean cascadedDelete,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        actorProfileManagement.deleteActorProfile(userId, externalSourceGUID, externalSourceName, actorProfileGUID, cascadedDelete, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(actorProfileGUID);
        }
    }


    /**
     * Create a new contact method.
     *
     * @param actorProfileGUID             unique identifier of the actor profile that should be the anchor for the new element.
     * @param properties                   properties for the new element.
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createContactMethod(String                  actorProfileGUID,
                                      ContactMethodProperties properties,
                                      boolean                 forLineage,
                                      boolean                 forDuplicateProcessing,
                                      Date                    effectiveTime) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        String contactMethodGUID = contactMethodManagement.createContactMethod(userId, externalSourceGUID, externalSourceName, actorProfileGUID, properties, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(contactMethodGUID);
        }

        return contactMethodGUID;
    }


    /**
     * Create a new metadata element to represent a contact method using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new contact method.
     *
     * @param actorProfileGUID             unique identifier of the actor profile that should be the anchor for the new element.
     * @param effectiveFrom                the date when this element is active - null for active on creation
     * @param effectiveTo                  the date when this element becomes inactive - null for active until deleted
     * @param templateGUID                 the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                                     connection etc)
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createContactMethodFromTemplate(String              actorProfileGUID,
                                                  Date                effectiveFrom,
                                                  Date                effectiveTo,
                                                  String              templateGUID,
                                                  ElementProperties   replacementProperties,
                                                  Map<String, String> placeholderProperties,
                                                  boolean             forLineage,
                                                  boolean             forDuplicateProcessing,
                                                  Date                effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        String contactMethodGUID = contactMethodManagement.createContactMethodFromTemplate(userId, externalSourceGUID, externalSourceName, actorProfileGUID, effectiveFrom, effectiveTo, templateGUID, replacementProperties, placeholderProperties, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(contactMethodGUID);
        }

        return contactMethodGUID;
    }


    /**
     * Update the properties of a contact method.
     *
     * @param contactMethodGUID      unique identifier of the contact method (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the element.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateContactMethod(String                  contactMethodGUID,
                                    boolean                 replaceAllProperties,
                                    ContactMethodProperties properties,
                                    boolean                 forLineage,
                                    boolean                 forDuplicateProcessing,
                                    Date                    effectiveTime) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        contactMethodManagement.updateContactMethod(userId, externalSourceGUID, externalSourceName, contactMethodGUID, replaceAllProperties, properties, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(contactMethodGUID);
        }
    }


    /**
     * Delete a contact method.
     *
     * @param contactMethodGUID       unique identifier of the element
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteContactMethod(String  contactMethodGUID,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        contactMethodManagement.deleteContactMethod(userId, externalSourceGUID, externalSourceName, contactMethodGUID, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(contactMethodGUID);
        }
    }


    /**
     * Returns the list of actor profiles with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param templateFilter         should templates be returned?
     * @param limitResultsByStatus   control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime               repository time to use
     * @param sequencingOrder        order to retrieve results
     * @param sequencingProperty     property to use for sequencing order
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<ActorProfileElement> getActorProfilesByName(String              name,
                                                            TemplateFilter      templateFilter,
                                                            List<ElementStatus> limitResultsByStatus,
                                                            Date                asOfTime,
                                                            SequencingOrder     sequencingOrder,
                                                            String              sequencingProperty,
                                                            int                 startFrom,
                                                            int                 pageSize,
                                                            boolean             forLineage,
                                                            boolean             forDuplicateProcessing,
                                                            Date                effectiveTime) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        return actorProfileManagement.getActorProfilesByName(userId, name, templateFilter, limitResultsByStatus, asOfTime, sequencingOrder, sequencingProperty, startFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime);
    }

    /**
     * Return the properties of a specific actor profile.
     *
     * @param actorProfileGUID       unique identifier of the required element
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ActorProfileElement getActorProfileByGUID(String  actorProfileGUID,
                                                     Date    asOfTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        return actorProfileManagement.getActorProfileByGUID(userId, actorProfileGUID, asOfTime, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Return the properties of a specific actor profile retrieved using an associated userId.
     *
     * @param requiredUserId         identifier of user
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ActorProfileElement getActorProfileByUserId(String  requiredUserId,
                                                       Date    asOfTime,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        return actorProfileManagement.getActorProfileByUserId(userId, requiredUserId, asOfTime, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Retrieve the list of actor profiles metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned actor profiles include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param templateFilter         should templates be returned?
     * @param limitResultsByStatus   control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime               repository time to use
     * @param sequencingOrder        order to retrieve results
     * @param sequencingProperty     property to use for sequencing order
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ActorProfileElement> findActorProfiles(String              searchString,
                                                       TemplateFilter      templateFilter,
                                                       List<ElementStatus> limitResultsByStatus,
                                                       Date                asOfTime,
                                                       SequencingOrder     sequencingOrder,
                                                       String              sequencingProperty,
                                                       int                 startFrom,
                                                       int                 pageSize,
                                                       boolean             forLineage,
                                                       boolean             forDuplicateProcessing,
                                                       Date                effectiveTime) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return actorProfileManagement.findActorProfiles(userId, searchString, templateFilter, limitResultsByStatus, asOfTime, sequencingOrder, sequencingProperty, startFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /* ========================================================
     * Manage user identities
     */


    /**
     * Create a new user identity.
     *
     * @param anchorGUID                   unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                                     or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor                  boolean flag to day that the element should be classified as its own anchor once its element
     *                                     is created in the repository.
     * @param anchorScopeGUID              unique identifier of any anchor scope to use for searching
     * @param properties                   properties for the new element.
     * @param parentGUID                   unique identifier of optional parent entity
     * @param parentRelationshipTypeName   type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1                 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createUserIdentity(String                 anchorGUID,
                                     boolean                isOwnAnchor,
                                     String                 anchorScopeGUID,
                                     UserIdentityProperties properties,
                                     String                 parentGUID,
                                     String                 parentRelationshipTypeName,
                                     ElementProperties      parentRelationshipProperties,
                                     boolean                parentAtEnd1,
                                     boolean                forLineage,
                                     boolean                forDuplicateProcessing,
                                     Date                   effectiveTime) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        String userIdentityGUID = userIdentityClient.createUserIdentity(userId, externalSourceGUID, externalSourceName, anchorGUID, isOwnAnchor, anchorScopeGUID, properties, parentGUID, parentRelationshipTypeName, parentRelationshipProperties, parentAtEnd1, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(userIdentityGUID);
        }

        return userIdentityGUID;
    }


    /**
     * Create a new metadata element to represent a user identity using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new user identity.
     *
     * @param anchorGUID                   unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                                     or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor                  boolean flag to day that the element should be classified as its own anchor once its element
     *                                     is created in the repository.
     * @param anchorScopeGUID              unique identifier of any anchor scope to use for searching
     * @param effectiveFrom                the date when this element is active - null for active on creation
     * @param effectiveTo                  the date when this element becomes inactive - null for active until deleted
     * @param templateGUID                 the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                                     connection etc)
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID                   unique identifier of optional parent entity
     * @param parentRelationshipTypeName   type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1                 which end should the parent GUID go in the relationship
     * @param forLineage                   the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing       the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime                only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createUserIdentityFromTemplate(String              anchorGUID,
                                                 boolean             isOwnAnchor,
                                                 String              anchorScopeGUID,
                                                 Date                effectiveFrom,
                                                 Date                effectiveTo,
                                                 String              templateGUID,
                                                 ElementProperties   replacementProperties,
                                                 Map<String, String> placeholderProperties,
                                                 String              parentGUID,
                                                 String              parentRelationshipTypeName,
                                                 ElementProperties   parentRelationshipProperties,
                                                 boolean             parentAtEnd1,
                                                 boolean             forLineage,
                                                 boolean             forDuplicateProcessing,
                                                 Date                effectiveTime) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String userIdentityGUID = userIdentityClient.createUserIdentityFromTemplate(userId, externalSourceGUID, externalSourceName, anchorGUID, isOwnAnchor, anchorScopeGUID, effectiveFrom, effectiveTo, templateGUID, replacementProperties, placeholderProperties, parentGUID, parentRelationshipTypeName, parentRelationshipProperties, parentAtEnd1, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(userIdentityGUID);
        }

        return userIdentityGUID;
    }


    /**
     * Update the properties of a user identity.
     *
     * @param userIdentityGUID      unique identifier of the user identity (returned from create)
     * @param replaceAllProperties   flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             properties for the element.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateUserIdentity(String                 userIdentityGUID,
                                   boolean                replaceAllProperties,
                                   UserIdentityProperties properties,
                                   boolean                forLineage,
                                   boolean                forDuplicateProcessing,
                                   Date                   effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        userIdentityClient.updateUserIdentity(userId, externalSourceGUID, externalSourceName, userIdentityGUID, replaceAllProperties, properties, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(userIdentityGUID);
        }
    }


    /**
     * Attach a profile to a user identity.
     *
     * @param userIdentityGUID unique identifier of the parent
     * @param profileGUID     unique identifier of the actor profile
     * @param relationshipProperties  description of the relationship.
     * @param forLineage              the query is to support lineage retrieval
     * @param forDuplicateProcessing  the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime           the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkIdentityToProfile(String                    userIdentityGUID,
                                      String                    profileGUID,
                                      ProfileIdentityProperties relationshipProperties,
                                      boolean                   forLineage,
                                      boolean                   forDuplicateProcessing,
                                      Date                      effectiveTime) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        userIdentityClient.linkIdentityToProfile(userId, externalSourceGUID, externalSourceName, userIdentityGUID, profileGUID, relationshipProperties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Detach an actor profile from a user identity.
     *
     * @param userIdentityGUID    unique identifier of the parent actor profile.
     * @param profileGUID    unique identifier of the nested actor profile.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachProfileIdentity(String  userIdentityGUID,
                                      String  profileGUID,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        userIdentityClient.detachProfileIdentity(userId, externalSourceGUID, externalSourceName, userIdentityGUID, profileGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Add the SecurityGroupMembership classification to the user identity.
     *
     * @param userIdentityGUID    unique identifier of the user identity.
     * @param properties            properties for the classification
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addSecurityGroupMembership(String                            userIdentityGUID,
                                           SecurityGroupMembershipProperties properties,
                                           boolean                           forLineage,
                                           boolean                           forDuplicateProcessing,
                                           Date                              effectiveTime) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        userIdentityClient.addSecurityGroupMembership(userId, externalSourceGUID, externalSourceName, userIdentityGUID, properties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Update the SecurityGroupMembership classification for the user identity.
     *
     * @param userIdentityGUID    unique identifier of the user identity.
     * @param properties            properties for the classification
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateSecurityGroupMembership(String                            userIdentityGUID,
                                              SecurityGroupMembershipProperties properties,
                                              boolean                           forLineage,
                                              boolean                           forDuplicateProcessing,
                                              Date                              effectiveTime) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        userIdentityClient.updateSecurityGroupMembership(userId, externalSourceGUID, externalSourceName, userIdentityGUID, properties, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Remove the SecurityGroupMembership classification from the user identity.
     *
     * @param userIdentityGUID    unique identifier of the user identity.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeAllSecurityGroupMembership(String                            userIdentityGUID,
                                                 boolean                           forLineage,
                                                 boolean                           forDuplicateProcessing,
                                                 Date                              effectiveTime) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        userIdentityClient.removeAllSecurityGroupMembership(userId, externalSourceGUID, externalSourceName, userIdentityGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }



    /**
     * Delete a user identity.
     *
     * @param userIdentityGUID      unique identifier of the element
     * @param cascadedDelete         can the user identity be deleted if it has actor profiles linked to it?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteUserIdentity(String  userIdentityGUID,
                                   boolean cascadedDelete,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        userIdentityClient.deleteUserIdentity(userId, externalSourceGUID, externalSourceName, userIdentityGUID, cascadedDelete, forLineage, forDuplicateProcessing, effectiveTime);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(userIdentityGUID);
        }
    }


    /**
     * Returns the list of user identities with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param templateFilter         should templates be returned?
     * @param limitResultsByStatus   control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime               repository time to use
     * @param sequencingOrder        order to retrieve results
     * @param sequencingProperty     property to use for sequencing order
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<UserIdentityElement> getUserIdentitiesByName(String              name,
                                                             TemplateFilter templateFilter,
                                                             List<ElementStatus> limitResultsByStatus,
                                                             Date                asOfTime,
                                                             SequencingOrder sequencingOrder,
                                                             String              sequencingProperty,
                                                             int                 startFrom,
                                                             int                 pageSize,
                                                             boolean             forLineage,
                                                             boolean             forDuplicateProcessing,
                                                             Date                effectiveTime) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        return userIdentityClient.getUserIdentitiesByName(userId, name, templateFilter, limitResultsByStatus, asOfTime, sequencingOrder, sequencingProperty, startFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Return the properties of a specific user identity.
     *
     * @param userIdentityGUID      unique identifier of the required element
     * @param asOfTime               repository time to use
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public UserIdentityElement getUserIdentityByGUID(String  userIdentityGUID,
                                                     Date    asOfTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        return userIdentityClient.getUserIdentityByGUID(userId, userIdentityGUID, asOfTime, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Retrieve the list of user identities metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param templateFilter         should templates be returned?
     * @param limitResultsByStatus   control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime               repository time to use
     * @param sequencingOrder        order to retrieve results
     * @param sequencingProperty     property to use for sequencing order
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<UserIdentityElement> findUserIdentities(String              searchString,
                                                        TemplateFilter      templateFilter,
                                                        List<ElementStatus> limitResultsByStatus,
                                                        Date                asOfTime,
                                                        SequencingOrder     sequencingOrder,
                                                        String              sequencingProperty,
                                                        int                 startFrom,
                                                        int                 pageSize,
                                                        boolean             forLineage,
                                                        boolean             forDuplicateProcessing,
                                                        Date                effectiveTime) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return userIdentityClient.findUserIdentities(userId, searchString, templateFilter, limitResultsByStatus, asOfTime, sequencingOrder, sequencingProperty, startFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime);
    }
}
