/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The AssetLineageRESTServices provides the server-side implementation of the Asset Lineage Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class AssetLineageRestServices {

    private static AssetLineageInstanceHandler instanceHandler = new AssetLineageInstanceHandler();
    private static final Logger log = LoggerFactory.getLogger(AssetLineageRestServices.class);


    /**
     * Default constructor
     */
    public AssetLineageRestServices() {
        instanceHandler.registerAccessService();
    }


}
