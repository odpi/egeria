/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;


import org.odpi.openmetadata.accessservices.governanceprogram.converters.GovernanceZoneConverter;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramErrorCode;
import org.odpi.openmetadata.accessservices.governanceprogram.handlers.ExternalReferencesHandler;
import org.odpi.openmetadata.accessservices.governanceprogram.handlers.GovernanceOfficerHandler;
import org.odpi.openmetadata.accessservices.governanceprogram.handlers.PersonalProfileHandler;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceZoneElement;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceZoneHandler;
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

    private GovernanceZoneHandler<GovernanceZoneElement> governanceZoneHandler;
    private GovernanceOfficerHandler                     governanceOfficerHandler;
    private ExternalReferencesHandler                    externalReferencesHandler;
    private PersonalProfileHandler                       personalProfileHandler;


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

            this.externalReferencesHandler = new ExternalReferencesHandler(serviceName,
                                                                           serverName,
                                                                           invalidParameterHandler,
                                                                           repositoryHelper,
                                                                           repositoryHandler);

            this.personalProfileHandler = new PersonalProfileHandler(serviceName,
                                                                           serverName,
                                                                           invalidParameterHandler,
                                                                           repositoryHelper,
                                                                           repositoryHandler);

            this.governanceOfficerHandler = new GovernanceOfficerHandler(serviceName,
                                                                         serverName,
                                                                         invalidParameterHandler,
                                                                         repositoryHelper,
                                                                         repositoryHandler,
                                                                         personalProfileHandler,
                                                                         externalReferencesHandler);
        }
        else
        {
            throw new NewInstanceException(GovernanceProgramErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }
    }


    /**
     * Return the governance officer handler
     *
     * @return handler
     */
    GovernanceOfficerHandler getGovernanceOfficerHandler()
    {
        return governanceOfficerHandler;
    }


    /**
     * Return the external references handler
     *
     * @return handler
     */
    ExternalReferencesHandler getExternalReferencesHandler()
    {
        return externalReferencesHandler;
    }


    /**
     * Return the persona profile handler.
     *
     * @return handler
     */
    PersonalProfileHandler getPersonalProfileHandler()
    {
        return personalProfileHandler;
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

}
