/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;


/**
 * DigitalServiceInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DigitalServiceAdmin class.
 */
class DigitalServiceInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    DigitalServiceInstanceHandler()
    {
        super(AccessServiceDescription.DIGITAL_SERVICE_OMAS.getAccessServiceFullName());

        DigitalServiceRegistration.registerAccessService();
    }


}
