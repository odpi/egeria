/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogBean;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetCatalogResponse is the response structure used on the Asset Catalog OMAS REST API calls that returns a
 * list of asset catalog beans object as a response.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class AssetCatalogListResponse extends AssetCatalogOMASAPIResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The list of assets
     * -- GETTER --
     * Returns the list of assets
     * @return the list of assets
     * -- SETTER --
     * Setup the list of assets
     * @param assetCatalogBeanList the list of assets
     */
    private List<AssetCatalogBean> assetCatalogBeanList;
}
