/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.connectors.outtopic;

import org.odpi.openmetadata.accessservices.assetcatalog.api.AssetCatalogEventListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

public class AssetCatalogOutTopicEventListener implements AssetCatalogEventListener {


    @Override
    public void processEvent(EntityDetail event) {
//        todo not surre this is needed might delete later
    }
}
