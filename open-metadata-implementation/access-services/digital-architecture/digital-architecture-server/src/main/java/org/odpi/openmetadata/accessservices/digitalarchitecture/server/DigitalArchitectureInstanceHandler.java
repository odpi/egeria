/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;


/**
 * DigitalArchitectureInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DigitalArchitectureAdmin class.
 */
class DigitalArchitectureInstanceHandler extends OCFOMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    DigitalArchitectureInstanceHandler()
    {
        super(AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceFullName());

        DigitalArchitectureRegistration.registerAccessService();
    }
}
