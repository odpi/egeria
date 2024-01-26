/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import java.util.Objects;


/**
 * Edge describes a relationship in the lineage graph.
 */
public class Edge
{
    private String id;
    private String from;
    private String to;
    private String label;
    private String type;

    /**
     * Construct a simple edge.
     *
     * @param from identifier of the starting element
     * @param to identifier of the destination
     */
    public Edge(String from,
                String to)
    {
        this.from = from;
        this.to = to;
    }

    /**
     * Construct an edge.
     *
     * @param id identifier of the edge
     * @param from identifier of the starting element
     * @param to identifier of the destination
     * @param label label for the edge
     */
    public Edge(String id,
                String from,
                String to,
                String label)
    {
        this.id = id;
        this.from = from;
        this.to = to;
        this.label = label;
    }


    /**
     * Return the identifier of the edge.
     *
     * @return guid
     */
    public String getId() {
        return id;
    }


    /**
     * Set up the identifier of the edge.
     *
     * @param id guid
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * Return the identifier of hte element that the edge starts at.
     *
     * @return guid
     */
    public String getFrom()
    {
        return from;
    }


    /**
     * Set up the identifier of the element that the edge starts at.
     *
     * @param from guid
     */
    public void setFrom(String from)
    {
        this.from = from;
    }


    /**
     * Return the identifier of the element that the end ends at.
     *
     * @return guid
     */
    public String getTo()
    {
        return to;
    }


    /**
     * Set up the identifier of the element that the end ends at.
     *
     * @param to guid
     */
    public void setTo(String to)
    {
        this.to = to;
    }


    /**
     * Set up the label for the edge.
     *
     * @return name
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Return the label for the edge.
     *
     * @param label name
     */
    public void setLabel(String label)
    {
        this.label = label;
    }


    /**
     * Return the type of the edge.
     *
     * @return name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the type of the edge.
     *
     * @param type name
     */
    public void setType(String type)
    {
        this.type = type;
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        Edge edge = (Edge) objectToCompare;
        return Objects.equals(id, edge.id) &&
                Objects.equals(from, edge.from) &&
                Objects.equals(to, edge.to) &&
                Objects.equals(label, edge.label) &&
                Objects.equals(type, edge.type);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, from, to, label, type);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Edge{" +
                "id='" + id + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
