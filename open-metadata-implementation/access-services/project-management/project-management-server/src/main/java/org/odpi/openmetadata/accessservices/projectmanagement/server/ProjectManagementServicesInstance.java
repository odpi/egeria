/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.server;

import org.odpi.openmetadata.accessservices.projectmanagement.ffdc.ProjectManagementErrorCode;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * ProjectManagementServicesInstance caches references to objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class ProjectManagementServicesInstance extends OMASServiceInstance
{
    private final static AccessServiceDescription myDescription = AccessServiceDescription.PROJECT_MANAGEMENT_OMAS;

    private final ReferenceableHandler<RelatedElementStub> relatedElementHandler;
    private final ActorProfileHandler<ActorProfileElement> actorProfileHandler;
    private final PersonRoleHandler<ActorRoleElement>      personRoleHandler;
    private final ProjectHandler<ProjectElement>           projectHandler;

    /**
     * Set up the handlers for this server.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that ProjectManagement is allowed to serve Assets from.
     * @param auditLog destination for audit log events.
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum number of results that can be returned on a single call
     * @throws NewInstanceException a problem occurred during initialization
     */
    public ProjectManagementServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                             List<String>            supportedZones,
                                             AuditLog                auditLog,
                                             String                  localServerUserId,
                                             int                     maxPageSize) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              null,
              null,
              auditLog,
              localServerUserId,
              maxPageSize);

        final String methodName = "new ServiceInstance";

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


            this.actorProfileHandler = new ActorProfileHandler<>(new ActorProfileConverter<>(repositoryHelper, serviceName, serverName),
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

            this.projectHandler = new ProjectHandler<>(new ProjectConverter<>(repositoryHelper, serviceName, serverName),
                                                       ProjectElement.class,
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
            throw new NewInstanceException(ProjectManagementErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
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
    public ProjectHandler<ProjectElement> getProjectHandler() throws PropertyServerException
    {
        final String methodName = "getCommunityHandler";

        validateActiveRepository(methodName);

        return projectHandler;
    }
}
