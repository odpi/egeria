/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.requests;


import java.util.Objects;

/**
 * Describes a node in the lineage graph
 */
public class Node
{
    private String type;
    private String name;


    /**
     * Default constructor
     */
    public Node()
    {
    }


    /**
     * Set up constructor.
     *
     * @param type type of node
     * @param name name of node
     */
    public Node(String type, String name)
    {
        this.type = type;
        this.name = name;
    }


    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }



    @Override
    public String toString()
    {
        return "Node{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        Node node = (Node) objectToCompare;
        return Objects.equals(type, node.type) && Objects.equals(name, node.name);
    }



    @Override
    public int hashCode()
    {
        return Objects.hash(type, name);
    }
}
