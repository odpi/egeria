/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.ODFOMASServiceInstanceHandler;


/**
 * AssetConsumerInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetConsumerAdmin class.
 */
class AssetOwnerInstanceHandler extends ODFOMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    AssetOwnerInstanceHandler()
    {
        super(AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceFullName());

        AssetOwnerRegistration.registerAccessService();
    }
}
