/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservice.assetcatalog.model.ColumnContext;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetDescriptionResponse is the response structure used on the Asset Catalog OMAS REST API calls that returns a
 * asset description object as a response.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColumnContextResponse extends AssetCatalogOMASAPIResponse implements Serializable {

    private List<ColumnContext> assetDescriptionList;

    public List<ColumnContext> getAssetDescriptionList() {
        return assetDescriptionList;
    }

    public void setAssetDescriptionList(List<ColumnContext> assetDescriptionList) {
        this.assetDescriptionList = assetDescriptionList;
    }
}