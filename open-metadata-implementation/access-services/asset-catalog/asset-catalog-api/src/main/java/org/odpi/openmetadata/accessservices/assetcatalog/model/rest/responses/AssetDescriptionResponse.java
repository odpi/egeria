/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetDescriptionResponse is the response structure used on the Asset Catalog OMAS REST API calls that returns an
 * asset description object as a response.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetDescriptionResponse extends AssetCatalogOMASAPIResponse {

    private AssetDescription assetDescription;

    public AssetDescription getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(AssetDescription assetDescription) {
        this.assetDescription = assetDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AssetDescriptionResponse that = (AssetDescriptionResponse) o;
        return assetDescription.equals(that.assetDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), assetDescription);
    }
}
