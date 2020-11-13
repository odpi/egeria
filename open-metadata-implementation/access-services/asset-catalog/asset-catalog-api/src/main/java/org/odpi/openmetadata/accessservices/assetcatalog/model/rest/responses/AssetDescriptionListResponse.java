/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetDescriptionResponse is the response structure used on the Asset Catalog OMAS REST API calls that returns a
 * list of asset description object as a response.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetDescriptionListResponse extends AssetCatalogOMASAPIResponse implements Serializable {

    private List<AssetDescription> assetDescriptionList;

    public List<AssetDescription> getAssetDescriptionList() {
        return assetDescriptionList;
    }

    public void setAssetDescriptionList(List<AssetDescription> assetDescriptionList) {
        this.assetDescriptionList = assetDescriptionList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetDescriptionListResponse that = (AssetDescriptionListResponse) o;
        return Objects.equals(assetDescriptionList, that.assetDescriptionList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assetDescriptionList);
    }
}