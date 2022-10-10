/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.api;



import org.odpi.openmetadata.accessservices.digitalservice.metadataelements.AgreementRoleAppointee;
import org.odpi.openmetadata.accessservices.digitalservice.metadataelements.PersonRoleAppointee;
import org.odpi.openmetadata.accessservices.digitalservice.metadataelements.PersonRoleElement;
import org.odpi.openmetadata.accessservices.digitalservice.metadataelements.PersonRoleHistory;
import org.odpi.openmetadata.accessservices.digitalservice.properties.AgreementRoleProperties;
import org.odpi.openmetadata.accessservices.digitalservice.properties.PersonRoleProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * DigitalRolesInterface covers the definition of person roles and their appointments.  This is to define the digital service managers and
 * the roles associated with agreement.
 */
public interface DigitalRolesInterface
{
    /**
     * Create a definition of a new role for the manager of a digital service.  The role is anchored to the digital service and will be deleted
     * automatically when the digital service is deleted.  It can be managed using the methods below before that.
     * A person can be appointed to ths role using the <i>appointPersonRole</i> method.
     *
     * More general support for creating roles is found in Community Profile OMAS and Governance Program OMAS.
     *
     * @param userId calling user
     * @param digitalServiceGUID unique identifier of the digital service to connect the new role to
     * @param properties role properties
     *
     * @return unique identifier of new role
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String createDigitalServiceManagerRole(String               userId,
                                           String               digitalServiceGUID,
                                           PersonRoleProperties properties) throws UserNotAuthorizedException,
                                                                                   InvalidParameterException,
                                                                                   PropertyServerException;


    /**
     * Create a definition of a new role that is part of an agreement such as a digital subscription.
     *
     * These roles are often referred to in the agreement.  For example, the agreement may refer to the subscriber, payer, data consumer.
     * The name of such a role is stored in the <i>agreementRoleProperties</i>.  The properties of the person role that is created to represent
     * the role is stored in <i>personRoleProperties</i>.  A person (or multiple people) can be appointed to the new role using the
     * <i>appointPersonRole</i> method.
     *
     * The new person role created is anchored to the agreement and so it is deleted when the agreement is deleted.
     * It can be managed using the methods below before that.
     *
     * More general support for creating roles is found in Community Profile OMAS and Governance Program OMAS.
     *
     * @param userId calling user
     * @param agreementGUID unique identifier of the agreement
     * @param agreementRoleProperties description of a role found in the agreement text
     * @param personRoleProperties properties to use when creating the role.
     *
     * @return unique identifier of new role
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String createAgreementRole(String                  userId,
                               String                  agreementGUID,
                               AgreementRoleProperties agreementRoleProperties,
                               PersonRoleProperties    personRoleProperties) throws UserNotAuthorizedException,
                                                                                    InvalidParameterException,
                                                                                    PropertyServerException;


    /**
     * Update the properties of a specific person role.
     *
     * @param userId calling user
     * @param roleGUID identifier of the person role to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updatePersonRole(String               userId,
                          String               roleGUID,
                          boolean              isMergeUpdate,
                          PersonRoleProperties properties) throws UserNotAuthorizedException,
                                                                  InvalidParameterException,
                                                                  PropertyServerException;


    /**
     * Update the name of the role for a specific agreement.
     *
     * @param userId calling user
     * @param agreementGUID unique identifier of the agreement
     * @param roleGUID identifier of the person role
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param agreementRoleProperties properties to change
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateAgreementRole(String                  userId,
                             String                  agreementGUID,
                             String                  roleGUID,
                             boolean                 isMergeUpdate,
                             AgreementRoleProperties agreementRoleProperties) throws UserNotAuthorizedException,
                                                                                     InvalidParameterException,
                                                                                     PropertyServerException;


    /**
     * Delete the person role.
     *
     * @param userId calling user
     * @param personRoleGUID identifier of the person role to delete
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void removePersonRole(String userId,
                          String personRoleGUID) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;


    /**
     * Retrieve the properties of a person role using its unique identifier.
     *
     * @param userId calling user
     * @param personRoleGUID unique identifier
     *
     * @return properties of the role and any current appointees
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    PersonRoleElement getPersonRoleByGUID(String userId,
                                          String personRoleGUID) throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        PropertyServerException;


    /**
     * Retrieve the appointment history for a person role by unique guid.
     *
     * @param userId the name of the calling user.
     * @param personRoleGUID unique identifier (guid) of the person role.
     * @return person role object
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    PersonRoleHistory getPersonRoleHistoryByGUID(String userId,
                                                 String personRoleGUID) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException;


    /**
     * Retrieve the properties of a person role using its unique name.  The results are returned as a list
     * since it is possible that two roles have the same identifier due to the distributed nature of the
     * open metadata ecosystem.  By returning all the search results here it is possible to manage the
     * duplicates through this interface.
     *
     * @param userId calling user
     * @param qualifiedName unique name
     *
     * @return list of roles retrieved
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<PersonRoleElement> getPersonRoleByQualifiedName(String userId,
                                                         String qualifiedName) throws UserNotAuthorizedException,
                                                                                      InvalidParameterException,
                                                                                      PropertyServerException;


    /**
     * Retrieve all the person roles for a particular title.  The title can include regEx wildcards.
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
    List<PersonRoleElement> getPersonRolesByTitle(String userId,
                                                  String title,
                                                  int    startFrom,
                                                  int    pageSize) throws UserNotAuthorizedException,
                                                                          InvalidParameterException,
                                                                          PropertyServerException;


    /**
     * Return all the person roles and their incumbents (if any).
     *
     * @param userId the name of the calling user.
     * @param digitalServiceGUID unique identifier of the digital service to connect the new role to
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *                 
     * @return list of person role objects with details of the people appointed to the roles (if any)
     * @throws InvalidParameterException the userId is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    List<PersonRoleAppointee> getDigitalServiceManagers(String userId,
                                                        String digitalServiceGUID,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException;


    /**
     * Return all the person roles and their incumbents (if any).
     *
     * @param userId the name of the calling user.
     * @param agreementGUID unique identifier of the agreement
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of person role objects with details of the people appointed to the roles (if any)
     * @throws InvalidParameterException the userId is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    List<AgreementRoleAppointee> getAgreementRoles(String userId,
                                                   String agreementGUID,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException;


    /**
     * Link a person to a person role.
     *
     * @param userId the name of the calling user
     * @param roleGUID unique identifier (guid) of the person role
     * @param profileGUID unique identifier for the profile
     * @param startDate the official start date of the appointment - null means effective immediately
     *
     * @return unique identifier (guid) of the appointment relationship
     * @throws InvalidParameterException the unique identifier of the person role or profile is either null or invalid
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    String appointPersonRole(String userId,
                             String roleGUID,
                             String profileGUID,
                             Date   startDate) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;


    /**
     * Unlink a person from a person role appointment.
     *
     * @param userId the name of the calling user
     * @param roleGUID unique identifier (guid) of the person role
     * @param profileGUID unique identifier for the profile
     * @param appointmentGUID unique identifier (guid) of the appointment relationship created by <i>appointPersonRole</i> -
     *                        this is returned by <i>appointPersonRole</i> and can be retrieved by the <i>getDigitalServiceManagers</i> or
     *                        <i>getAgreementRoles</i> depending on the type of role.
     * @param endDate the official end of the appointment - null means effective immediately
     *
     * @throws InvalidParameterException the profile is not linked to this person role
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    void relievePersonRole(String userId,
                           String roleGUID,
                           String profileGUID,
                           String appointmentGUID,
                           Date   endDate) throws InvalidParameterException,
                                                  PropertyServerException,
                                                  UserNotAuthorizedException;
}
