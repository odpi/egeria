/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram;

import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceOfficer;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceResponsibility;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.PersonalProfile;

import java.util.List;
import java.util.Map;

/**
 * The GovernanceTeamInterface is used to manage the appointment of the governance officers.
 * There are methods for maintaining the governance responsibilities and linking them to
 * governance policies.  Then there are methods for maintaining the profiles of the governance
 * officers if they are not already set up
 */
public interface GovernanceTeamInterface
{
    String createGovernanceResponsibility(String       roleId,
                                          String       roleTitle,
                                          String       roleDescription,
                                          String       scope,
                                          String       priority,
                                          List<String> implications,
                                          List<String> outcomes) throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        PropertyServerException;

    void   linkGovernanceResponsiblityToPolicy(String              responsibilityGUID,
                                               String              policyGUID);

    void   updateGovernanceResponsibility(String              responsibilityGUID);


    void   setGovernanceResponsibilityStatus(String              responsibilityGUID);
    void   retireGovernanceResponsibility(String              responsibilityGUID);
    void   deleteGovernanceResponsibility(String              responsibilityGUID);


    GovernanceResponsibility  getGovernanceResponsibilityByGUID(String  responsibilityGUID);
    GovernanceResponsibility  getGovernanceResponsibilityByDocumentId(String  documentId);
    GovernanceResponsibility  getGovernanceResponsibilityByTitle(String  title);

    List<GovernanceResponsibility>  getGovernanceResponsibilities(String  title);


    String createGovernanceOfficerProfile(String              employeeNumber,
                                          String              fullName,
                                          String              knownName,
                                          String              jobTitle,
                                          String              jobRoleDescription,
                                          Map<String, Object> additionalProperties);

    void   updateGovernanceOfficerProfile(String              profileGUID,
                                          String              employeeNumber,
                                          String              fullName,
                                          String              knownName,
                                          String              jobTitle,
                                          String              jobRoleDescription,
                                          Map<String, Object> additionalProperties);

    void   deleteGovernanceOfficerProfile(String              profileGUID,
                                          String              employeeNumber);

    PersonalProfile getPersonalProfileByGUID(String             profileGUID);

    PersonalProfile getPersonalProfileByEmployeeNumber(String         employeeNumber);


    List<PersonalProfile>  getPersonalProfilesByName(String   name);


    void appointGovernanceOfficer(String  responsibilityGUID,
                                  String  profileGUID,
                                  String  context);

    void relieveGovernanceOfficer(String  responsibilityGUID,
                                  String  profileGUID);


    List<GovernanceOfficer>  getGovernanceOfficers();

    List<GovernanceOfficer>  getGovernanceOfficersByResponsibity(String   governanceResponsibilityGUID);

    List<GovernanceResponsibility>  getGovernanceResponsibitiesForOfficer(String   governanceOfficerGUID);

}
