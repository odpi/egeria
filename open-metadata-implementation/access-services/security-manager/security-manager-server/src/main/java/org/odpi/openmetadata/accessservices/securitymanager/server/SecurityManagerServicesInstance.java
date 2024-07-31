/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.server;

import org.odpi.openmetadata.accessservices.securitymanager.connectors.outtopic.SecurityManagerOutTopicClientProvider;
import org.odpi.openmetadata.accessservices.securitymanager.ffdc.SecurityManagerErrorCode;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * SecurityManagerServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class SecurityManagerServicesInstance extends OMASServiceInstance
{
    private final static AccessServiceDescription myDescription = AccessServiceDescription.SECURITY_MANAGER_OMAS;

    private final SoftwareCapabilityHandler<SecurityManagerElement> softwareCapabilityHandler;
    private final GovernanceDefinitionHandler<SecurityGroupElement> securityGroupHandler;
    private final UserIdentityHandler<UserIdentityElement>          userIdentityHandler;
    private final ContactDetailsHandler<ContactMethodElement>       contactDetailsHandler;
    private final ActorProfileHandler<ActorProfileElement>          actorProfileHandler;
    private final PersonRoleHandler<PersonRoleElement>              personRoleHandler;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that SecurityManager is allowed to serve Assets from.
     * @param defaultZones list of zones that SecurityManager sets up in new Asset instances.
     * @param publishZones list of zones that SecurityManager sets up in published Asset instances.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @param outTopicEventBusConnection inner event bus connection to use to build topic connection to send to client if they which
     *                                   to listen on the out topic.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public SecurityManagerServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                           List<String>            supportedZones,
                                           List<String>            defaultZones,
                                           List<String>            publishZones,
                                           AuditLog                auditLog,
                                           String                  localServerUserId,
                                           int                     maxPageSize,
                                           Connection              outTopicEventBusConnection) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog,
              localServerUserId,
              maxPageSize,
              null,
              null,
              SecurityManagerOutTopicClientProvider.class.getName(),
              outTopicEventBusConnection);

        if (repositoryHandler == null)
        {
            final String methodName = "new ServiceInstance";

            throw new NewInstanceException(SecurityManagerErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }


        this.softwareCapabilityHandler = new SoftwareCapabilityHandler<>(new SecurityManagerConverter<>(repositoryHelper, serviceName, serverName),
                                                                         SecurityManagerElement.class,
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
    }



    /**
     * Return the handler for managing software server capability objects representing the integrator.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    SoftwareCapabilityHandler<SecurityManagerElement> getSoftwareCapabilityHandler() throws PropertyServerException
    {
        final String methodName = "getSoftwareCapabilityHandler";

        validateActiveRepository(methodName);

        return softwareCapabilityHandler;
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

}
