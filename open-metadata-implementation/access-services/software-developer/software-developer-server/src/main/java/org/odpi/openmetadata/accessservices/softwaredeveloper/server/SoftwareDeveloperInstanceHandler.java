/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.softwaredeveloper.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;


/**
 * SoftwareDeveloperInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the SoftwareDeveloperAdmin class.
 */
class SoftwareDeveloperInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    SoftwareDeveloperInstanceHandler()
    {
        super(AccessServiceDescription.SOFTWARE_DEVELOPER_OMAS.getAccessServiceFullName());

        SoftwareDeveloperRegistration.registerAccessService();
    }
}
