/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.accessservices.communityprofile.handlers.ContributionRecordHandler;
import org.odpi.openmetadata.accessservices.communityprofile.handlers.PersonalProfileHandler;
import org.odpi.openmetadata.accessservices.communityprofile.handlers.UserIdentityHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * CommunityProfileServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class CommunityProfileServicesInstance extends OMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.COMMUNITY_PROFILE_OMAS;

    private PersonalProfileHandler    personalProfileHandler;
    private UserIdentityHandler       userIdentityHandler;
    private ContributionRecordHandler contributionRecordHandler;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that the community profile is allowed to serve Assets from.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @param karmaPointPlateau number of karma points to reach a plateau
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public CommunityProfileServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                            List<String>            supportedZones,
                                            AuditLog                auditLog,
                                            String                  localServerUserId,
                                            int                     maxPageSize,
                                            int                     karmaPointPlateau) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              auditLog,
              localServerUserId,
              maxPageSize);

        final String methodName = "new ServiceInstance";

        super.supportedZones = supportedZones;

        if (repositoryHandler != null)
        {
            this.personalProfileHandler = new PersonalProfileHandler(serviceName,
                                                                     serverName,
                                                                     invalidParameterHandler,
                                                                     repositoryHelper,
                                                                     repositoryHandler,
                                                                     errorHandler);

            this.userIdentityHandler = new UserIdentityHandler(serviceName,
                                                               invalidParameterHandler,
                                                               repositoryHelper,
                                                               repositoryHandler,
                                                               errorHandler);

            this.contributionRecordHandler = new ContributionRecordHandler(serviceName,
                                                                           serverName,
                                                                           invalidParameterHandler,
                                                                           repositoryHelper,
                                                                           repositoryHandler,
                                                                           karmaPointPlateau);
        }
        else
        {
            throw new NewInstanceException(CommunityProfileErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }
    }


    /**
     * Return the handler for personal profile requests.
     *
     * @return handler object
     */
    public PersonalProfileHandler getPersonalProfileHandler()
    {
        return personalProfileHandler;
    }


    /**
     * Return the handler for user identity requests.
     *
     * @return handler object
     */
    public UserIdentityHandler getUserIdentityHandler()
    {
        return userIdentityHandler;
    }


    /**
     * Return the handler for personal contribution record requests.
     *
     * @return handler object
     */
    public ContributionRecordHandler getContributionRecordHandler()
    {
        return contributionRecordHandler;
    }
}
