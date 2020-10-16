/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.stewardshipaction.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;


/**
 * StewardshipActionInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the StewardshipActionAdmin class.
 */
class StewardshipActionInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    StewardshipActionInstanceHandler()
    {
        super(AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName());

        StewardshipActionRegistration.registerAccessService();
    }
}
