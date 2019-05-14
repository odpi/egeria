/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;

/**
 * ConnectedAssetInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ConnectedAssetAdmin class.
 */
class ConnectedAssetInstanceHandler extends OCFOMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    ConnectedAssetInstanceHandler()
    {
        super(AccessServiceDescription.CONNECTED_ASSET_OMAS);

        ConnectedAssetRegistration.registerAccessService();
    }
}
