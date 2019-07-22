/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;

public class DataPlatformInstanceHandler extends OCFOMASServiceInstanceHandler {

    /**
     * Default constructor registers the access service
     */
    public DataPlatformInstanceHandler() {
        super(AccessServiceDescription.DATA_PLATFORM_OMAS.getAccessServiceName() + " OMAS");
        DataPlatformOMASRegistration.registerAccessService();
    }
}
