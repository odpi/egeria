/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram;


import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceResponsibility;

import java.util.List;

public interface GovernanceOrganizationInterface
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


    GovernanceResponsibility getGovernanceResponsibilityByGUID(String  responsibilityGUID);
    GovernanceResponsibility  getGovernanceResponsibilityByDocumentId(String  documentId);
    GovernanceResponsibility  getGovernanceResponsibilityByTitle(String  title);

    List<GovernanceResponsibility>  getGovernanceResponsibilities(String  title);





}
