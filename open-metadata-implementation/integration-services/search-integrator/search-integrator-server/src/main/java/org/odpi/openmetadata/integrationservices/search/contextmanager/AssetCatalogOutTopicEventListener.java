/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.search.contextmanager;

import org.odpi.openmetadata.accessservices.assetcatalog.api.AssetCatalogEventListener;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogEvent;
import org.odpi.openmetadata.integrationservices.search.connector.SearchIntegratorConnector;

public class AssetCatalogOutTopicEventListener implements AssetCatalogEventListener {

    private SearchIntegratorConnector searchIntegratorConnector;

    public SearchIntegratorConnector getSearchIntegratorConnector() {
        return searchIntegratorConnector;
    }

    public void setSearchIntegratorConnector(SearchIntegratorConnector searchIntegratorConnector) {
        this.searchIntegratorConnector = searchIntegratorConnector;
    }

    @Override
    public void processEvent(AssetCatalogEvent event) {
        searchIntegratorConnector.saveAsset(event);
    }
}
