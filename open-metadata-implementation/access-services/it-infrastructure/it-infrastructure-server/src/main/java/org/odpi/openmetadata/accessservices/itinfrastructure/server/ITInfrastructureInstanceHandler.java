/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;


/**
 * ITInfrastructureInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ITInfrastructureAdmin class.
 */
class ITInfrastructureInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    ITInfrastructureInstanceHandler()
    {
        super(AccessServiceDescription.IT_INFRASTRUCTURE_OMAS.getAccessServiceFullName());

        ITInfrastructureRegistration.registerAccessService();
    }
}
