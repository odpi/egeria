/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetElement object is used to describe the elements returned by the search method
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetElement extends Element {

    private List<Element> context;
    private List<Connection> connections;

    /**
     * Returns the context of the entity. It includes the entities that are connected to the element.
     * For example, for a column, this context contains the table, schema and the asset.
     *
     * @return the context for the given element
     */
    public List<Element> getContext() {
        return context;
    }

    /**
     * Setup the context for an entity
     *
     * @param context the describes the elements
     */
    public void setContext(List<Element> context) {
        this.context = context;
    }

    /**
     * Returns the connections to the asset
     *
     * @return a list of available connections
     */
    public List<Connection> getConnections() {
        return connections;
    }

    /**
     * Set up the connection for the asset
     *
     * @param connections
     */
    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AssetElement that = (AssetElement) o;
        return Objects.equals(context, that.context) &&
                Objects.equals(connections, that.connections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), context, connections);
    }
}
