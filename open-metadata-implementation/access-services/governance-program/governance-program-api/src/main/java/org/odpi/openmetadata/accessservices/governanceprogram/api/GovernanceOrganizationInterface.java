/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.api;


import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceResponsibilityProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

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

    void   linkGovernanceResponsibilityToPolicy(String              responsibilityGUID,
                                               String              policyGUID);

    void   updateGovernanceResponsibility(String              responsibilityGUID);


    void   setGovernanceResponsibilityStatus(String              responsibilityGUID);
    void   retireGovernanceResponsibility(String              responsibilityGUID);
    void   deleteGovernanceResponsibility(String              responsibilityGUID);


    GovernanceResponsibilityProperties getGovernanceResponsibilityByGUID(String  responsibilityGUID);
    GovernanceResponsibilityProperties getGovernanceResponsibilityByDocumentId(String  documentId);
    GovernanceResponsibilityProperties getGovernanceResponsibilityByTitle(String  title);

    List<GovernanceResponsibilityProperties>  getGovernanceResponsibilities(String  title);





}
