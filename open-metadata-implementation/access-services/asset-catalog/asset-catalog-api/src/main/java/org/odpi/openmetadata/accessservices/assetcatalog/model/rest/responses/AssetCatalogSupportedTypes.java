/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses;

import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;

import java.util.List;
import java.util.Objects;

public class AssetCatalogSupportedTypes extends AssetCatalogOMASAPIResponse {

    private static final long serialVersionUID = 1L;

    private List<Type> types;

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AssetCatalogSupportedTypes that = (AssetCatalogSupportedTypes) o;
        return Objects.equals(types, that.types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), types);
    }
}
