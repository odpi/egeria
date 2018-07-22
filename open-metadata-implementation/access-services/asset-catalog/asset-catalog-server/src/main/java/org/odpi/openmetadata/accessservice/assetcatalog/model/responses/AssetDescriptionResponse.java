/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.model.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.odpi.openmetadata.accessservice.assetcatalog.model.AssetDescription;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetDescriptionResponse is the response structure used on the Asset Catalog OMAS REST API calls that returns a
 * asset description object as a response.
 */
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AssetDescriptionResponse extends AssetCatalogOMASAPIResponse implements Serializable {

    private List<AssetDescription> assetDescriptionList;
}