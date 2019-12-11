/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses;

import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;

import java.util.List;

public class AssetCatalogSupportedTypes extends AssetCatalogOMASAPIResponse {

    private static final long serialVersionUID = 1L;

    private List<Type> types;

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }
}
