/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamodel.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;


/**
 * DataModelInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DataModelAdmin class.
 */
class DataModelInstanceHandler extends OCFOMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    DataModelInstanceHandler()
    {
        super(AccessServiceDescription.DATA_MODEL_OMAS.getAccessServiceName() + " OMAS");

        DataModelRegistration.registerAccessService();
    }
}
