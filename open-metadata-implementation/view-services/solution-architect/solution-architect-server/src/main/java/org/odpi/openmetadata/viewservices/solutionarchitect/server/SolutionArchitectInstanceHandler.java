/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;



/**
 * SolutionArchitectInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the SolutionArchitectAdmin class.
 */
public class SolutionArchitectInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public SolutionArchitectInstanceHandler()
    {
        super(ViewServiceDescription.SOLUTION_ARCHITECT.getViewServiceName());

        SolutionArchitectRegistration.registerViewService();
    }


}
