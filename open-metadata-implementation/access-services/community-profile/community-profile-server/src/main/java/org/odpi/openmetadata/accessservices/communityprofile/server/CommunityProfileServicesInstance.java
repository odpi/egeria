/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.connectors.outtopic.CommunityProfileOutTopicClientProvider;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * CommunityProfileServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class CommunityProfileServicesInstance extends OMASServiceInstance
{
    private final static AccessServiceDescription myDescription = AccessServiceDescription.COMMUNITY_PROFILE_OMAS;

    private final ReferenceableHandler<RelatedElementStub>         relatedElementHandler;
    private final SoftwareCapabilityHandler<MetadataSourceElement> metadataSourceHandler;
    private final ActorProfileHandler<ActorProfileGraphElement> actorProfileHandler;
    private final PersonRoleHandler<ActorRoleElement>      personRoleHandler;
    private final CommunityHandler<CommunityElement>                     communityHandler;
    private final ContributionRecordHandler<ContributionRecordElement>   contributionRecordHandler;
    private final LocationHandler<LocationElement>                       locationHandler;
    private final GovernanceDefinitionHandler<SecurityGroupElement>      securityGroupHandler;
    private final ValidValuesHandler<ValidValueElement>                  validValuesHandler;



    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that the community profile is allowed to serve Assets from.
     * @param defaultZones list of zones that GovernanceEngine should set in all new Assets.
     * @param publishedZones list of zones that governance engine can use to make a governance service visible.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @param karmaPointPlateau number of karma points to reach a plateau
     * @param outTopicEventBusConnection inner event bus connection to use to build topic connection to send to client if they which
     *                                   to listen on the out topic.
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public CommunityProfileServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                            List<String>            supportedZones,
                                            List<String>            defaultZones,
                                            List<String>            publishedZones,
                                            AuditLog                auditLog,
                                            String                  localServerUserId,
                                            int                     maxPageSize,
                                            int                     karmaPointPlateau,
                                            Connection              outTopicEventBusConnection) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              defaultZones,
              publishedZones,
              auditLog,
              localServerUserId,
              maxPageSize,
              null,
              null,
              CommunityProfileOutTopicClientProvider.class.getName(),
              outTopicEventBusConnection);

        final String methodName = "new ServiceInstance";

        super.supportedZones = supportedZones;

        if (repositoryHandler != null)
        {
            this.relatedElementHandler = new ReferenceableHandler<>(new RelatedElementConverter<>(repositoryHelper, serviceName, serverName),
                                                                    RelatedElementStub.class,
                                                                    serviceName,
                                                                    serverName,
                                                                    invalidParameterHandler,
                                                                    repositoryHandler,
                                                                    repositoryHelper,
                                                                    localServerUserId,
                                                                    securityVerifier,
                                                                    supportedZones,
                                                                    defaultZones,
                                                                    publishZones,
                                                                    auditLog);

            this.metadataSourceHandler = new SoftwareCapabilityHandler<>(new MetadataSourceConverter<>(repositoryHelper, serviceName,serverName),
                                                                         MetadataSourceElement.class,
                                                                         serviceName,
                                                                         serverName,
                                                                         invalidParameterHandler,
                                                                         repositoryHandler,
                                                                         repositoryHelper,
                                                                         localServerUserId,
                                                                         securityVerifier,
                                                                         supportedZones,
                                                                         defaultZones,
                                                                         publishZones,
                                                                         auditLog);


            this.actorProfileHandler = new ActorProfileHandler<>(new ActorProfileConverter<>(repositoryHelper, serviceName,serverName),
                                                                 ActorProfileGraphElement.class,
                                                                 serviceName,
                                                                 serverName,
                                                                 invalidParameterHandler,
                                                                 repositoryHandler,
                                                                 repositoryHelper,
                                                                 localServerUserId,
                                                                 securityVerifier,
                                                                 supportedZones,
                                                                 defaultZones,
                                                                 publishZones,
                                                                 auditLog);

            this.personRoleHandler = new PersonRoleHandler<>(new PersonRoleConverter<>(repositoryHelper, serviceName, serverName),
                                                             ActorRoleElement.class,
                                                             serviceName,
                                                             serverName,
                                                             invalidParameterHandler,
                                                             repositoryHandler,
                                                             repositoryHelper,
                                                             localServerUserId,
                                                             securityVerifier,
                                                             supportedZones,
                                                             defaultZones,
                                                             publishZones,
                                                             auditLog);

            this.communityHandler = new CommunityHandler<>(new CommunityConverter<>(repositoryHelper, serviceName, serverName),
                                                           CommunityElement.class,
                                                           serviceName,
                                                           serverName,
                                                           invalidParameterHandler,
                                                           repositoryHandler,
                                                           repositoryHelper,
                                                           localServerUserId,
                                                           securityVerifier,
                                                           supportedZones,
                                                           defaultZones,
                                                           publishZones,
                                                           auditLog);

            this.locationHandler = new LocationHandler<>(new LocationConverter<>(repositoryHelper, serviceName, serverName),
                                                         LocationElement.class,
                                                         serviceName,
                                                         serverName,
                                                         invalidParameterHandler,
                                                         repositoryHandler,
                                                         repositoryHelper,
                                                         localServerUserId,
                                                         securityVerifier,
                                                         supportedZones,
                                                         defaultZones,
                                                         publishZones,
                                                         auditLog);

            this.securityGroupHandler = new GovernanceDefinitionHandler<>(new SecurityGroupConverter<>(repositoryHelper, serviceName, serverName),
                                                                        SecurityGroupElement.class,
                                                                        serviceName,
                                                                        serverName,
                                                                        invalidParameterHandler,
                                                                        repositoryHandler,
                                                                        repositoryHelper,
                                                                        localServerUserId,
                                                                        securityVerifier,
                                                                        supportedZones,
                                                                        defaultZones,
                                                                        publishZones,
                                                                        auditLog);


            this.contributionRecordHandler = new ContributionRecordHandler<>(new ContributionRecordConverter<>(repositoryHelper, serviceName, serverName, karmaPointPlateau),
                                                                             ContributionRecordElement.class,
                                                                             serviceName,
                                                                             serverName,
                                                                             invalidParameterHandler,
                                                                             repositoryHandler,
                                                                             repositoryHelper,
                                                                             localServerUserId,
                                                                             securityVerifier,
                                                                             supportedZones,
                                                                             defaultZones,
                                                                             publishZones,
                                                                             karmaPointPlateau,
                                                                             auditLog);

            this.validValuesHandler = new ValidValuesHandler<>(new ValidValueConverter<>(repositoryHelper, serviceName, serverName),
                                                               ValidValueElement.class,
                                                               serviceName,
                                                               serverName,
                                                               invalidParameterHandler,
                                                               repositoryHandler,
                                                               repositoryHelper,
                                                               localServerUserId,
                                                               securityVerifier,
                                                               supportedZones,
                                                               defaultZones,
                                                               publishZones,
                                                               auditLog);
        }
        else
        {
            throw new NewInstanceException(CommunityProfileErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }
    }


    /**
     * Return the handler for related referenceables.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public ReferenceableHandler<RelatedElementStub> getRelatedElementHandler() throws PropertyServerException
    {
        final String methodName = "getRelatedElementHandler";

        validateActiveRepository(methodName);

        return relatedElementHandler;
    }


    /**
     * Return the handler for metadata source requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public SoftwareCapabilityHandler<MetadataSourceElement> getMetadataSourceHandler() throws PropertyServerException
    {
        final String methodName = "getMetadataSourceHandler";

        validateActiveRepository(methodName);

        return metadataSourceHandler;
    }


    /**
     * Return the handler for organization requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public ActorProfileHandler<ActorProfileGraphElement> getActorProfileHandler() throws PropertyServerException
    {
        final String methodName = "getActorProfileHandler";

        validateActiveRepository(methodName);

        return actorProfileHandler;
    }


    /**
     * Return the handler for role requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public PersonRoleHandler<ActorRoleElement> getPersonRoleHandler() throws PropertyServerException
    {
        final String methodName = "getPersonRoleHandler";

        validateActiveRepository(methodName);

        return personRoleHandler;
    }


    /**
     * Return the handler for community requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public CommunityHandler<CommunityElement> getCommunityHandler() throws PropertyServerException
    {
        final String methodName = "getCommunityHandler";

        validateActiveRepository(methodName);

        return communityHandler;
    }


    /**
     * Return the handler for location requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public LocationHandler<LocationElement> getLocationHandler() throws PropertyServerException
    {
        final String methodName = "getLocationHandler";

        validateActiveRepository(methodName);

        return locationHandler;
    }


    /**
     * Return the handler for security group requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public GovernanceDefinitionHandler<SecurityGroupElement> getSecurityGroupHandler() throws PropertyServerException
    {
        final String methodName = "getSecurityGroupHandler";

        validateActiveRepository(methodName);

        return securityGroupHandler;
    }


    /**
     * Return the handler for personal contribution record requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public ContributionRecordHandler<ContributionRecordElement> getContributionRecordHandler() throws PropertyServerException
    {
        final String methodName = "getContributionRecordHandler";

        validateActiveRepository(methodName);

        return contributionRecordHandler;
    }


    /**
     * Return the handler for valid value requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public ValidValuesHandler<ValidValueElement> getValidValuesHandler() throws PropertyServerException
    {
        final String methodName = "getValidValuesHandler";

        validateActiveRepository(methodName);

        return validValuesHandler;
    }

}
