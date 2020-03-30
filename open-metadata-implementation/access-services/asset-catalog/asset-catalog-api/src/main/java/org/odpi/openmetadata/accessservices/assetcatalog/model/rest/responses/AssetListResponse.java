/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetListResponse is the response structure used on the Asset Catalog OMAS REST API calls that returns an
 * assets object as a response.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetListResponse extends AssetCatalogOMASAPIResponse {

    private List<AssetElements> assetElementsList = new ArrayList<>();

    public List<AssetElements> getAssetElementsList() {
        return assetElementsList;
    }

    public void setAssetElementsList(List<AssetElements> assetElementsList) {
        this.assetElementsList = assetElementsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AssetListResponse that = (AssetListResponse) o;
        return Objects.equals(assetElementsList, that.assetElementsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), assetElementsList);
    }
}
