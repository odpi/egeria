/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.governanceofficer.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;



/**
 * GovernanceOfficerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceOfficerAdmin class.
 */
public class GovernanceOfficerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public GovernanceOfficerInstanceHandler()
    {
        super(ViewServiceDescription.GOVERNANCE_OFFICER.getViewServiceName());

        GovernanceOfficerRegistration.registerViewService();
    }


}
