/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.connectors.outtopic.CommunityProfileOutTopicClientProvider;
import org.odpi.openmetadata.accessservices.communityprofile.converters.*;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * CommunityProfileServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class CommunityProfileServicesInstance extends OMASServiceInstance
{
    private final static AccessServiceDescription myDescription = AccessServiceDescription.COMMUNITY_PROFILE_OMAS;

    private final ReferenceableHandler<RelatedElement>                   relatedElementHandler;
    private final SoftwareCapabilityHandler<MetadataSourceElement>       metadataSourceHandler;
    private final UserIdentityHandler<UserIdentityElement>               userIdentityHandler;
    private final ActorProfileHandler<ActorProfileElement>               actorProfileHandler;
    private final PersonRoleHandler<PersonRoleElement>                   personRoleHandler;
    private final CollectionHandler<CollectionElement>                   collectionHandler;
    private final CommunityHandler<CommunityElement>                     communityHandler;
    private final ContributionRecordHandler<ContributionRecordElement>   contributionRecordHandler;
    private final ContactDetailsHandler<ContactMethodElement>            contactDetailsHandler;
    private final LocationHandler<LocationElement>                       locationHandler;
    private final GovernanceDefinitionHandler<SecurityGroupElement>      securityGroupHandler;
    private final ValidValuesHandler<ValidValueElement>                  validValuesHandler;

    private final CommentHandler<CommentElement>         commentHandler;
    private final InformalTagHandler<InformalTagElement> informalTagHandler;
    private final LikeHandler<LikeElement>               likeHandler;
    private final RatingHandler<RatingElement>           ratingHandler;


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
            this.relatedElementHandler = new ReferenceableHandler<>(new RelatedElementConverter<>(repositoryHelper, serviceName,serverName),
                                                                    RelatedElement.class,
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
                                                                 ActorProfileElement.class,
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

            this.personRoleHandler = new PersonRoleHandler<>(new PersonRoleConverter<>(repositoryHelper, serviceName,serverName),
                                                             PersonRoleElement.class,
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

            this.collectionHandler = new CollectionHandler<>(new CollectionConverter<>(repositoryHelper, serviceName,serverName),
                                                             CollectionElement.class,
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

            this.communityHandler = new CommunityHandler<>(new CommunityConverter<>(repositoryHelper, serviceName,serverName),
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

            this.userIdentityHandler = new UserIdentityHandler<>(new UserIdentityConverter<>(repositoryHelper, serviceName, serverName),
                                                                 UserIdentityElement.class,
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

            this.contactDetailsHandler = new ContactDetailsHandler<>(new ContactMethodConverter<>(repositoryHelper, serviceName, serverName),
                                                                     ContactMethodElement.class,
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

            this.commentHandler = new CommentHandler<>(new CommentConverter<>(repositoryHelper, serviceName, serverName),
                                                       CommentElement.class,
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

            this.informalTagHandler = new InformalTagHandler<>(new InformalTagConverter<>(repositoryHelper, serviceName, serverName),
                                                               InformalTagElement.class,
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

            this.likeHandler = new LikeHandler<>(new LikeConverter<>(repositoryHelper, serviceName, serverName),
                                                 LikeElement.class,
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

            this.ratingHandler = new RatingHandler<>(new RatingConverter<>(repositoryHelper, serviceName, serverName),
                                                     RatingElement.class,
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
    public ReferenceableHandler<RelatedElement> getRelatedElementHandler() throws PropertyServerException
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
    public ActorProfileHandler<ActorProfileElement> getActorProfileHandler() throws PropertyServerException
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
    public PersonRoleHandler<PersonRoleElement> getPersonRoleHandler() throws PropertyServerException
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
    public CollectionHandler<CollectionElement> getCollectionHandler() throws PropertyServerException
    {
        final String methodName = "getCollectionHandler";

        validateActiveRepository(methodName);

        return collectionHandler;
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
     * Return the handler for contact methods requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public ContactDetailsHandler<ContactMethodElement> getContactDetailsHandler() throws PropertyServerException
    {
        final String methodName = "getContactMethodHandler";

        validateActiveRepository(methodName);

        return contactDetailsHandler;
    }


    /**
     * Return the handler for user identity requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public UserIdentityHandler<UserIdentityElement> getUserIdentityHandler() throws PropertyServerException
    {
        final String methodName = "getUserIdentityHandler";

        validateActiveRepository(methodName);

        return userIdentityHandler;
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
     * Return the handler for managing comment objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    CommentHandler<CommentElement> getCommentHandler() throws PropertyServerException
    {
        final String methodName = "getCommentHandler";

        validateActiveRepository(methodName);

        return commentHandler;
    }


    /**
     * Return the handler for managing informal tag objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    InformalTagHandler<InformalTagElement> getInformalTagHandler() throws PropertyServerException
    {
        final String methodName = "getInformalTagHandler";

        validateActiveRepository(methodName);

        return informalTagHandler;
    }


    /**
     * Return the handler for managing like objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    LikeHandler<LikeElement> getLikeHandler() throws PropertyServerException
    {
        final String methodName = "getLikeHandler";

        validateActiveRepository(methodName);

        return likeHandler;
    }


    /**
     * Return the handler for managing rating objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    RatingHandler<RatingElement> getRatingHandler() throws PropertyServerException
    {
        final String methodName = "getRatingHandler";

        validateActiveRepository(methodName);

        return ratingHandler;
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
