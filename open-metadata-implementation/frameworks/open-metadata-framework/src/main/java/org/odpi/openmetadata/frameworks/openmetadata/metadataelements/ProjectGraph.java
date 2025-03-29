/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProjectGraph documents the project, it resources and relationships with other projects, plus mermaid summaries.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectGraph extends ProjectHierarchy
{
    private String  mermaidGraph = null;
    private String  mermaidTimeline= null;


    /**
     * Default Constructor
     */
    public ProjectGraph()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProjectGraph(ProjectGraph template)
    {
        super(template);

        if (template != null)
        {
            mermaidGraph    = template.getMermaidGraph();
            mermaidTimeline = template.getMermaidTimeline();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProjectGraph(ProjectHierarchy template)
    {
        super(template);
    }


    /**
     *
     * @return mermaid markdown
     */
    public String getMermaidGraph()
    {
        return mermaidGraph;
    }


    /**
     * Set up the graph view of the information supply chain.
     *
     * @param mermaidGraph mermaid markdown
     */
    public void setMermaidGraph(String mermaidGraph)
    {
        this.mermaidGraph = mermaidGraph;
    }


    /**
     * Return the mermaid timeline.
     *
     * @return string
     */
    public String getMermaidTimeline()
    {
        return mermaidTimeline;
    }


    /**
     * Set up the mermaid timeline.
     *
     * @param mermaidTimeline string
     */
    public void setMermaidTimeline(String mermaidTimeline)
    {
        this.mermaidTimeline = mermaidTimeline;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "ProjectGraph{" +
                "mermaidGraph='" + mermaidGraph + '\'' +
                ", mermaidTimeline='" + mermaidTimeline + '\'' +
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
        if (! (objectToCompare instanceof ProjectGraph that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(mermaidTimeline, that.mermaidTimeline) &&
                Objects.equals(mermaidGraph, that.mermaidGraph);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), mermaidTimeline, mermaidGraph);
    }
}
