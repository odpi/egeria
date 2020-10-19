/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;


/**
 * ProjectManagementInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ProjectManagementAdmin class.
 */
class ProjectManagementInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    ProjectManagementInstanceHandler()
    {
        super(AccessServiceDescription.PROJECT_MANAGEMENT_OMAS.getAccessServiceFullName());

        ProjectManagementRegistration.registerAccessService();
    }
}
