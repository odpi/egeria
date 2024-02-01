/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetCatalogItemElement object is used to describe the elements returned by the search method
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetCatalogItemElement extends Element
{
    /**
     * The context for an entity
     */
    private List<Element> context;

    /**
     * The connections for the asset
     */
    private List<Connection> connections;


    /**
     * Default constructor
     */
    public AssetCatalogItemElement()
    {
    }


    /**
     * Returns the context of the entity. It includes the entities that are connected to the element.
     * For example, for a column, this context contains the table, schema and the asset.
     *
     * @return the context for the given element
     */
    public List<Element> getContext()
    {
        return context;
    }


    /**
     * Set up the context for an entity
     * @param context the describes the elements
     */
    public void setContext(List<Element> context)
    {
        this.context = context;
    }


    /**
     * Returns the connections to the asset.
     *
     * @return a list of available connections
     */
    public List<Connection> getConnections()
    {
        return connections;
    }


    /**
     * Set up the connection for the asset.
     *
     * @param connections the connections for the asset
     */
    public void setConnections(List<Connection> connections)
    {
        this.connections = connections;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetCatalogItemElement{" +
                       "context=" + context +
                       ", connections=" + connections +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof AssetCatalogItemElement that))
        {
            return false;
        }
        return Objects.equals(context, that.context) && Objects.equals(connections, that.connections);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(context, connections);
    }
}
