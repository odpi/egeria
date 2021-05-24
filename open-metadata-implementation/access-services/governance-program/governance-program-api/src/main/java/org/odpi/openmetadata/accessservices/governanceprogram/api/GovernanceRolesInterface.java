/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.api;


import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceRoleAppointee;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceRoleHistory;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceRoleElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceRoleProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * GovernanceRolesInterface covers the definition of governance roles and their appointments.
 *
 */
public interface GovernanceRolesInterface
{
    /**
     * Create a definition of a new governance role.
     *
     * @param userId calling user
     * @param properties role properties
     *
     * @return unique identifier of new role
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String createGovernanceRole(String                   userId,
                                GovernanceRoleProperties properties) throws UserNotAuthorizedException,
                                                                            InvalidParameterException,
                                                                            PropertyServerException;


    /**
     *
     * @param userId calling user
     * @param roleGUID identifier of the governance role to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateGovernanceRole(String                   userId,
                              String                   roleGUID,
                              boolean                  isMergeUpdate,
                              GovernanceRoleProperties properties) throws UserNotAuthorizedException,
                                                                          InvalidParameterException,
                                                                          PropertyServerException;


    /**
     * Link a governance role to a governance control that defines a governance responsibility that a person fulfils.
     *
     * @param userId calling user
     * @param governanceRoleGUID unique identifier of the governance role
     * @param responsibilityGUID unique identifier of the governance responsibility control
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void linkRoleToResponsibility(String userId,
                                  String governanceRoleGUID,
                                  String responsibilityGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Remove the link between a governance role and a governance responsibility.
     *
     * @param userId calling user
     * @param governanceRoleGUID unique identifier of the governance role
     * @param responsibilityGUID unique identifier of the governance responsibility control
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void unlinkRoleFromResponsibility(String userId,
                                      String governanceRoleGUID,
                                      String responsibilityGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Link a governance role to the description of a resource that the role is responsible for.
     *
     * @param userId calling user
     * @param governanceRoleGUID unique identifier of the governance role
     * @param resourceGUID unique identifier of the resource description
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void linkRoleToResource(String userId,
                            String governanceRoleGUID,
                            String resourceGUID) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;


    /**
     * Remove the link between a governance role and a resource.
     *
     * @param userId calling user
     * @param governanceRoleGUID unique identifier of the governance role
     * @param resourceGUID unique identifier of the resource description
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void unlinkRoleFromResource(String userId,
                                String governanceRoleGUID,
                                String resourceGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


    /**
     * Delete the properties of the governance role.
     *
     * @param userId calling user
     * @param governanceRoleGUID identifier of the governance role to delete
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deleteGovernanceRole(String userId,
                              String governanceRoleGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Retrieve the properties of a governance role using its unique identifier.
     *
     * @param userId calling user
     * @param governanceRoleGUID unique identifier
     *
     * @return properties of the role and any current appointees
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    GovernanceRoleElement getGovernanceRoleByGUID(String userId,
                                                  String governanceRoleGUID) throws UserNotAuthorizedException,
                                                                                    InvalidParameterException,
                                                                                    PropertyServerException;

    /**
     * Retrieve the appointment history for a governance role by unique guid.
     *
     * @param userId the name of the calling user.
     * @param governanceRoleGUID unique identifier (guid) of the governance role.
     * @return governance role object
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    GovernanceRoleHistory getGovernanceRoleHistoryByGUID(String userId,
                                                         String governanceRoleGUID) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException;


    /**
     * Retrieve the properties of a governance role using its unique name.  The results are returned as a list
     * since it is possible that two roles have the same identifier due to the distributed nature of the
     * open metadata ecosystem.  By returning all of the search results here it is possible to manage the
     * duplicates through this interface.
     *
     * @param userId calling user
     * @param roleId unique name
     *
     * @return list of roles retrieved
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<GovernanceRoleElement> getGovernanceRoleByRoleId(String userId,
                                                          String roleId) throws UserNotAuthorizedException,
                                                                                InvalidParameterException,
                                                                                PropertyServerException;


    /**
     * Retrieve all of the governance roles for a particular governance domain.
     *
     * @param userId calling user
     * @param domainIdentifier identifier of domain - 0 means all
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of matching roles
     *
     * @throws InvalidParameterException domain identifier is undefined or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<GovernanceRoleElement> getGovernanceRolesByDomainId(String userId,
                                                             int    domainIdentifier,
                                                             int    startFrom,
                                                             int    pageSize) throws UserNotAuthorizedException,
                                                                                     InvalidParameterException,
                                                                                     PropertyServerException;


    /**
     * Retrieve all of the governance roles for a particular title.  The title can include regEx wildcards.
     *
     * @param userId calling user
     * @param title identifier of role
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of matching roles (null if no matching elements)
     *
     * @throws InvalidParameterException title or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<GovernanceRoleElement> getGovernanceRolesByTitle(String userId,
                                                          String title,
                                                          int    startFrom,
                                                          int    pageSize) throws UserNotAuthorizedException,
                                                                                  InvalidParameterException,
                                                                                  PropertyServerException;

    /**
     * Return all of the governance roles and their incumbents (if any).
     *
     * @param userId the name of the calling user.
     * @param domainIdentifier identifier of domain - 0 means all
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *                 
     * @return list of governance role objects
     * @throws InvalidParameterException the userId is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    List<GovernanceRoleAppointee> getCurrentGovernanceRoleAppointments(String userId,
                                                                       int    domainIdentifier,
                                                                       int    startFrom,
                                                                       int    pageSize) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException;


    /**
     * Link a person to a governance role.  Only one person may be appointed at any one time.
     *
     * @param userId the name of the calling user
     * @param roleGUID unique identifier (guid) of the governance role
     * @param profileGUID unique identifier for the profile
     * @param startDate the official start date of the appointment - null means effective immediately
     *
     * @return unique identifier (guid) of the appointment relationship
     * @throws InvalidParameterException the unique identifier of the governance role or profile is either null or invalid
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    String appointGovernanceRole(String userId,
                                 String roleGUID,
                                 String profileGUID,
                                 Date   startDate) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;


    /**
     * Unlink a person from a governance role appointment.
     *
     * @param userId the name of the calling user
     * @param roleGUID unique identifier (guid) of the governance role
     * @param profileGUID unique identifier for the profile
     * @param appointmentGUID unique identifier (guid) of the appointment relationship
     * @param endDate the official end of the appointment - null means effective immediately
     *
     * @throws InvalidParameterException the profile is not linked to this governance role
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void relieveGovernanceRole(String userId,
                               String roleGUID,
                               String profileGUID,
                               String appointmentGUID,
                               Date   endDate) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;


}
