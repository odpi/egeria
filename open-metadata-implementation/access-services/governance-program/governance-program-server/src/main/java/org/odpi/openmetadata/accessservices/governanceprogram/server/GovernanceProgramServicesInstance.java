/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;


import org.odpi.openmetadata.accessservices.governanceprogram.converters.*;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramErrorCode;
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
    private static AccessServiceDescription myDescription = AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS;

    private ElementStubConverter<ElementStub>                        elementStubConverter;
    private GovernanceZoneHandler<GovernanceZoneElement>             governanceZoneHandler;
    private PersonRoleHandler<GovernanceRoleElement>                 governanceRoleHandler;
    private ExternalReferenceHandler<ExternalReferenceElement>       externalReferenceHandler;
    private GovernanceDefinitionHandler<GovernanceDefinitionElement> governanceDefinitionHandler;
    private SubjectAreaHandler<SubjectAreaElement>                   subjectAreaHandler;


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
     * Return the handler for governance definition requests.
     *
     * @return handler object
     */
    SubjectAreaHandler<SubjectAreaElement> getSubjectAreaHandler()
    {
        return subjectAreaHandler;
    }
}
