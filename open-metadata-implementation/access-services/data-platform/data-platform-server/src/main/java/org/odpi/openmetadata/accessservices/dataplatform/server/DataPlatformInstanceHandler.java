/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;


public class DataPlatformInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    DataPlatformInstanceHandler()
    {
        super(AccessServiceDescription.DATA_PLATFORM_OMAS.getAccessServiceFullName());

        DataPlatformOMASRegistration.registerAccessService();
    }
}
