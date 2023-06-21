/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetElement object is used to describe the elements returned by the search method
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class AssetCatalogItemElement extends Element {

    private static final long serialVersionUID = 1L;

    /**
     * The context for an entity
     * -- GETTER --
     * Returns the context of the entity. It includes the entities that are connected to the element.
     * For example, for a column, this context contains the table, schema and the asset.
     * @return the context for the given element
     * -- SETTER --
     * Setup the context for an entity
     * @param context the describes the elements
     */
    private List<Element> context;

    /**
     * The connection for the asset
     * -- GETTER --
     * Returns the connections to the asset
     * @return a list of available connections
     * -- SETTER --
     * Set up the connection for the asset
     * @param connections the connections for the asset
     */
    private List<Connection> connections;

}
