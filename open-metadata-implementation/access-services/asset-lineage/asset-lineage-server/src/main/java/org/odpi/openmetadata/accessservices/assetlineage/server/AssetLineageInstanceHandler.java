/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;

/**
 * AssetLineageInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetLineageAdmin class.
 */
class AssetLineageInstanceHandler {
    private static AssetLineageServicesInstanceMap instanceMap = new AssetLineageServicesInstanceMap();

    /**
     * Default constructor registers the access service
     */
    AssetLineageInstanceHandler() {
        new AssetLineageOMASRegistration();
    }


}
