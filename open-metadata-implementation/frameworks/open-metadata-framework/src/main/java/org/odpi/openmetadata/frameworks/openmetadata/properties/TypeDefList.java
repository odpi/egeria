/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TypeDefList provides a simple bean for returning a list of TypeDefs and an optional mermaid graph.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefList
{
    private List<OpenMetadataTypeDef> typeDefs = null;
    private String                    mermaidGraph = null;

    /**
     * Default Constructor
     */
    public TypeDefList()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TypeDefList(TypeDefList template)
    {
        if (template != null)
        {
            typeDefs     = template.getTypeDefs();
            mermaidGraph = template.getMermaidGraph();
        }
    }


    /**
     * Return the list of typeDefs
     *
     * @return list of typeDefs
     */
    public List<OpenMetadataTypeDef> getTypeDefs()
    {
        return typeDefs;
    }


    /**
     * Set up the list of typeDefs
     *
     * @param typeDefs list of typeDefs
     */
    public void setTypeDefs(List<OpenMetadataTypeDef> typeDefs)
    {
        this.typeDefs = typeDefs;
    }



    /**
     * Return the mermaid representation of these types.
     *
     * @return string markdown
     */
    public String getMermaidGraph()
    {
        return mermaidGraph;
    }


    /**
     * Set up the mermaid representation of these types.
     *
     * @param mermaidGraph markdown string
     */
    public void setMermaidGraph(String mermaidGraph)
    {
        this.mermaidGraph = mermaidGraph;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TypeDefList{" +
                "typeDefs=" + typeDefs +
                ", mermaidGraph='" + mermaidGraph + '\'' +
                "} " + super.toString();
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
        if (!(objectToCompare instanceof TypeDefList that))
        {
            return false;
        }
        return Objects.equals(typeDefs, that.typeDefs) &&
                Objects.equals(mermaidGraph, that.mermaidGraph);

    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(typeDefs, mermaidGraph);
    }
}
