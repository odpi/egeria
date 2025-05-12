/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.handlers;

import org.odpi.openmetadata.accessservices.governanceprogram.converters.GovernanceProgramOMASConverter;

import org.odpi.openmetadata.commonservices.generichandlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.PersonRoleHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * AppointmentHandler is responsible for managing the relationship between a person role and a
 * profile.  It typically returns the combination of the relationship and the profile bean.
 * Specifically it supports the return of GovernanceAppointee in GovernanceRoleAppointee and
 * GovernanceRoleHistory.
 */
public class AppointmentHandler
{
    private final PersonRoleHandler<GovernanceRoleElement> roleHandler;
    private final ActorProfileHandler<ProfileElement>      profileHandler;

    private final GovernanceProgramOMASConverter<GovernanceAppointee> converter;
    private final RepositoryErrorHandler                              errorHandler;


    /**
     * Create an AppointmentHandler.
     *
     * @param roleHandler handler for person roles
     * @param profileHandler handler for actor profiles
     * @param repositoryHelper helper
     * @param serviceName this service
     * @param serverName controlling server name
     * @param auditLog logging destination
     */
    public AppointmentHandler(PersonRoleHandler<GovernanceRoleElement> roleHandler,
                              ActorProfileHandler<ProfileElement>      profileHandler,
                              OMRSRepositoryHelper                     repositoryHelper,
                              String                                   serviceName,
                              String                                   serverName,
                              AuditLog                                 auditLog)
    {
        this.roleHandler = roleHandler;
        this.profileHandler = profileHandler;

        this.converter = new GovernanceProgramOMASConverter<>(repositoryHelper, serviceName, serverName);
        this.errorHandler = new RepositoryErrorHandler(repositoryHelper, serviceName, serverName, auditLog);
    }


    /**
     * Return all the governance roles and their incumbents (if any).
     *
     * @param userId the name of the calling user
     * @param domainIdentifier identifier of domain - 0 means all
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     * @param methodName calling method
     *
     * @return list of governance role objects
     *
     * @throws InvalidParameterException the userId is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<GovernanceRoleAppointee> getCurrentGovernanceRoleAppointments(String userId,
                                                                              int    domainIdentifier,
                                                                              int    startFrom,
                                                                              int    pageSize,
                                                                              String methodName) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        final String governanceRoleGUIDParameterName = "governanceRoleGUID";

        List<GovernanceRoleElement> governanceRoles = roleHandler.getPersonRolesForDomainId(userId,
                                                                                            domainIdentifier,
                                                                                            startFrom,
                                                                                            pageSize,
                                                                                            false,
                                                                                            false,
                                                                                            new Date(),
                                                                                            methodName);

        if (governanceRoles != null)
        {
            List<GovernanceRoleAppointee> results = new ArrayList<>();

            for (GovernanceRoleElement governanceRole : governanceRoles)
            {
                if ((governanceRole != null) && (governanceRole.getElementHeader() != null))
                {
                    GovernanceRoleAppointee governanceRoleAppointee = new GovernanceRoleAppointee(governanceRole);

                    List<Relationship>  appointmentRelationships = roleHandler.getAttachmentLinks(userId,
                                                                                                  governanceRole.getElementHeader().getGUID(),
                                                                                                  governanceRoleGUIDParameterName,
                                                                                                  OpenMetadataType.PERSON_ROLE.typeName,
                                                                                                  OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeGUID,
                                                                                                  OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                                                  null,
                                                                                                  OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                                                  2,
                                                                                                  null,
                                                                                                  null,
                                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                                  null,
                                                                                                  false,
                                                                                                  false,
                                                                                                  0,
                                                                                                  0,
                                                                                                  null,
                                                                                                  methodName);

                    if (appointmentRelationships != null)
                    {
                        List<GovernanceAppointee> currentAppointees = new ArrayList<>();

                        for (Relationship relationship : appointmentRelationships)
                        {
                            if ((relationship != null) && (relationship.getProperties() != null))
                            {
                                InstanceProperties properties = relationship.getProperties();
                                Date now = new Date();

                                /*
                                 * Need to retrieve the appointments that are active
                                 */
                                if (((properties.getEffectiveFromTime() == null) || properties.getEffectiveFromTime().before(now)) &&
                                    ((properties.getEffectiveToTime() == null) || properties.getEffectiveToTime().after(now)))
                                {
                                    GovernanceAppointee appointee = getAppointeeFromRelationship(userId, relationship, methodName);

                                    currentAppointees.add(appointee);
                                }
                            }
                            else
                            {
                                errorHandler.logBadRelationship(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                relationship,
                                                                methodName);
                            }
                        }

                        if (!currentAppointees.isEmpty())
                        {
                            governanceRoleAppointee.setCurrentAppointees(currentAppointees);
                        }
                    }

                    results.add(governanceRoleAppointee);
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Retrieve a governance role description by unique guid along with the history of who has been appointed
     * to the role.
     *
     * @param userId the name of the calling user.
     * @param governanceRoleGUID unique identifier (guid) of the governance role
     * @param methodName calling method
     *
     * @return governance role object
     * @throws InvalidParameterException the unique identifier of the governance role is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceRoleHistory getGovernanceRoleHistoryByGUID(String userId,
                                                                String governanceRoleGUID,
                                                                String methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String governanceRoleGUIDParameterName = "governanceRoleGUID";

        GovernanceRoleElement governanceRole = roleHandler.getBeanFromRepository(userId,
                                                                                 governanceRoleGUID,
                                                                                 governanceRoleGUIDParameterName,
                                                                                 OpenMetadataType.PERSON_ROLE.typeName,
                                                                                 false,
                                                                                 false,
                                                                                 new Date(),
                                                                                 methodName);

        if ((governanceRole != null) && (governanceRole.getElementHeader() != null))
        {
            GovernanceRoleHistory governanceRoleHistory = new GovernanceRoleHistory(governanceRole);

            List<Relationship>  appointmentRelationships = roleHandler.getAttachmentLinks(userId,
                                                                                          governanceRole.getElementHeader().getGUID(),
                                                                                          governanceRoleGUIDParameterName,
                                                                                          OpenMetadataType.PERSON_ROLE.typeName,
                                                                                          OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeGUID,
                                                                                          OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                                          null,
                                                                                          OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                                          2,
                                                                                          null,
                                                                                          null,
                                                                                          SequencingOrder.CREATION_DATE_RECENT,
                                                                                          null,
                                                                                          false,
                                                                                          false,
                                                                                          0,
                                                                                          0,
                                                                                          null,
                                                                                          methodName);

            if (appointmentRelationships != null)
            {
                List<GovernanceAppointee> previousAppointees = new ArrayList<>();
                List<GovernanceAppointee> currentAppointees = new ArrayList<>();
                List<GovernanceAppointee> futureAppointees = new ArrayList<>();

                for (Relationship relationship : appointmentRelationships)
                {
                    if ((relationship != null) && (relationship.getProperties() != null))
                    {
                        InstanceProperties properties = relationship.getProperties();
                        Date now = new Date();

                        GovernanceAppointee appointee = getAppointeeFromRelationship(userId, relationship, methodName);

                        /*
                         * Need to retrieve the appointments that are active
                         */
                        if (((properties.getEffectiveFromTime() == null) || properties.getEffectiveFromTime().before(now)) &&
                            ((properties.getEffectiveToTime() == null) || properties.getEffectiveToTime().after(now)))
                        {
                            currentAppointees.add(appointee);
                        }
                        else if ((properties.getEffectiveToTime() != null) && properties.getEffectiveToTime().before(now))
                        {
                            previousAppointees.add(appointee);
                        }
                        else
                        {
                            futureAppointees.add(appointee);
                        }
                    }
                }

                if (! previousAppointees.isEmpty())
                {
                    governanceRoleHistory.setPredecessors(previousAppointees);
                }
                if (! currentAppointees.isEmpty())
                {
                    governanceRoleHistory.setCurrentAppointees(currentAppointees);
                }
                if (! futureAppointees.isEmpty())
                {
                    governanceRoleHistory.setSuccessors(futureAppointees);
                }
            }

            return governanceRoleHistory;
        }

        return null;
    }


    /**
     * Extract the appointee from the supplied relationship
     *
     * @param userId calling user
     * @param relationship PersonRoleAppointment relationship
     * @param methodName calling method
     *
     * @return populated appointee
     *
     * @throws InvalidParameterException the unique identifier of the governance role is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    private GovernanceAppointee getAppointeeFromRelationship(String       userId,
                                                             Relationship relationship,
                                                             String       methodName) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String profileGUIDParameterName = "profileGUID";

        if ((relationship != null) && (relationship.getProperties() != null) && (relationship.getEntityOneProxy() != null) && (relationship.getEntityTwoProxy() != null))
        {
            GovernanceAppointee appointee = new GovernanceAppointee();

            InstanceProperties properties = relationship.getProperties();

            ElementHeader elementHeader = converter.getMetadataElementHeader(GovernanceAppointee.class,
                                                                             relationship,
                                                                             null,
                                                                             methodName);

            appointee.setElementHeader(elementHeader);
            appointee.setStartDate(properties.getEffectiveFromTime());
            appointee.setEndDate(properties.getEffectiveToTime());

            ProfileElement profile = profileHandler.getActorProfileByGUID(userId,
                                                                          relationship.getEntityTwoProxy().getGUID(),
                                                                          profileGUIDParameterName,
                                                                          OpenMetadataType.PERSON.typeName,
                                                                          false,
                                                                          false,
                                                                          new Date(),
                                                                          methodName);

            appointee.setProfile(profile);

            return appointee;
        }
        else
        {
            errorHandler.logBadRelationship(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                            relationship,
                                            methodName);
        }

        return null;
    }
}
