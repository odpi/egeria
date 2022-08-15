/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;


import org.odpi.openmetadata.accessservices.governanceprogram.converters.*;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramErrorCode;
import org.odpi.openmetadata.accessservices.governanceprogram.handlers.AppointmentHandler;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * GovernanceProgramServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class GovernanceProgramServicesInstance extends OMASServiceInstance
{
    private final static AccessServiceDescription myDescription = AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS;

    private final ElementStubConverter<ElementStub>                        elementStubConverter;
    private final GovernanceZoneHandler<GovernanceZoneElement>             governanceZoneHandler;
    private final PersonRoleHandler<GovernanceRoleElement>                 governanceRoleHandler;
    private final ActorProfileHandler<ProfileElement>                      profileHandler;
    private final ExternalReferenceHandler<ExternalReferenceElement>       externalReferenceHandler;
    private final GovernanceDefinitionHandler<GovernanceDefinitionElement> governanceDefinitionHandler;
    private final CollectionHandler<GovernanceDomainSetElement>            governanceDomainSetHandler;
    private final GovernanceDomainHandler<GovernanceDomainElement>         governanceDomainHandler;
    private final SubjectAreaHandler<SubjectAreaElement>                   subjectAreaHandler;

    private AppointmentHandler                                       appointmentHandler;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param auditLog logging destination
     * @param localServerUserId userId to use for server initiated requests
     * @param maxPageSize maximum number of results on a single request
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public GovernanceProgramServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                             AuditLog                auditLog,
                                             String                  localServerUserId,
                                             int                     maxPageSize) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              auditLog,
              localServerUserId,
              maxPageSize);

        final String methodName = "new ServiceInstance";

        if (repositoryHandler != null)
        {
            this.elementStubConverter = new ElementStubConverter<>(repositoryHelper, serviceName, serverName);

            this.governanceZoneHandler = new GovernanceZoneHandler<>(new GovernanceZoneConverter<>(repositoryHelper, serviceName, serverName),
                                                                     GovernanceZoneElement.class,
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

            this.externalReferenceHandler = new ExternalReferenceHandler<>(new ExternalReferenceConverter<>(repositoryHelper, serviceName, serverName),
                                                                           ExternalReferenceElement.class,
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

            this.governanceRoleHandler = new PersonRoleHandler<>(new GovernanceRoleConverter<>(repositoryHelper, serviceName, serverName),
                                                                 GovernanceRoleElement.class,
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

            this.governanceDefinitionHandler = new GovernanceDefinitionHandler<>(new GovernanceDefinitionConverter<>(repositoryHelper, serviceName, serverName),
                                                                 GovernanceDefinitionElement.class,
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

            this.governanceDomainHandler = new GovernanceDomainHandler<>(new GovernanceDomainConverter<>(repositoryHelper, serviceName, serverName),
                                                                     GovernanceDomainElement.class,
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

            this.governanceDomainSetHandler = new CollectionHandler<>(new GovernanceDomainSetConverter<>(repositoryHelper, serviceName, serverName),
                                                                     GovernanceDomainSetElement.class,
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

            this.subjectAreaHandler = new SubjectAreaHandler<>(new SubjectAreaConverter<>(repositoryHelper, serviceName, serverName),
                                                               SubjectAreaElement.class,
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

            this.profileHandler = new ActorProfileHandler<>(new ProfileConverter<>(repositoryHelper, serviceName, serverName),
                                                               ProfileElement.class,
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

            this.appointmentHandler = new AppointmentHandler(governanceRoleHandler,
                                                             profileHandler,
                                                             repositoryHelper,
                                                             serviceName,
                                                             serverName,
                                                             auditLog);
        }
        else
        {
            throw new NewInstanceException(GovernanceProgramErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }
    }


    /**
     * Return the element stub converter
     *
     * @return converter
     */
    ElementStubConverter<ElementStub> getElementStubConverter()
    {
        return elementStubConverter;
    }


    /**
     * Return the governance role handler
     *
     * @return handler
     */
    PersonRoleHandler<GovernanceRoleElement> getGovernanceRoleHandler()
    {
        return governanceRoleHandler;
    }


    /**
     * Return the appointment handler
     *
     * @return handler
     */
    AppointmentHandler getAppointmentHandler()
    {
        return appointmentHandler;
    }


    /**
     * Return the external references handler
     *
     * @return handler
     */
    ExternalReferenceHandler<ExternalReferenceElement> getExternalReferencesHandler()
    {
        return externalReferenceHandler;
    }


    /**
     * Return the handler for governance zone requests.
     *
     * @return handler object
     */
    GovernanceZoneHandler<GovernanceZoneElement> getGovernanceZoneHandler()
    {
        return governanceZoneHandler;
    }


    /**
     * Return the handler for governance definition requests.
     *
     * @return handler object
     */
    GovernanceDefinitionHandler<GovernanceDefinitionElement> getGovernanceDefinitionHandler()
    {
        return governanceDefinitionHandler;
    }


    /**
     * Return the handler for governance domains requests.
     *
     * @return handler object
     */
    GovernanceDomainHandler<GovernanceDomainElement> getGovernanceDomainHandler()
    {
        return governanceDomainHandler;
    }


    /**
     * Return the handler for governance domain sets requests.
     *
     * @return handler object
     */
    CollectionHandler<GovernanceDomainSetElement> getGovernanceDomainSetHandler()
    {
        return governanceDomainSetHandler;
    }


    /**
     * Return the handler for governance definition requests.
     *
     * @return handler object
     */
    SubjectAreaHandler<SubjectAreaElement> getSubjectAreaHandler()
    {
        return subjectAreaHandler;
    }


    /**
     * Return the handler for profile requests.
     *
     * @return handler object
     */
    ActorProfileHandler<ProfileElement> getProfileHandler()
    {
        return profileHandler;
    }
}
