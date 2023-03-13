/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetCatalogBean object holds properties that are used for displaying details of an asset-catalog bean, plus the properties
 * and classifications ans relationships.
 * Also the connection to asset is available in this object.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AssetCatalogBean extends Element implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The list of relationships
     * -- GETTER --
     * Returns the list of available relationships
     * @return the list of relationships
     * -- SETTER --
     * Set up the list of relationships
     * @param relationships of the element
     */
    private List<Relationship> relationships;
}
