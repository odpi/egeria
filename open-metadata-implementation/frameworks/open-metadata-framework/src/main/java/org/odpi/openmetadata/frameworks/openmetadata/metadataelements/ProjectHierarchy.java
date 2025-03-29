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
 * ProjectGraph documents the project, it resources and relationships with other projects.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectHierarchy extends ProjectElement
{
    private List<RelatedMetadataElementSummary> dependentProjects = null;
    private List<RelatedMetadataElementSummary> dependsOnProjects = null;
    private List<ProjectHierarchy>              children          = null;


    /**
     * Default Constructor
     */
    public ProjectHierarchy()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProjectHierarchy(ProjectHierarchy template)
    {
        super (template);

        if (template != null)
        {
            dependsOnProjects = template.getDependsOnProjects();
            dependentProjects = template.getDependentProjects();
            children          = template.getChildren();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProjectHierarchy(ProjectElement template)
    {
        super (template);
    }


    /**
     * Return the list of projects that need this project to complete.
     *
     * @return list of project summaries
     */
    public List<RelatedMetadataElementSummary> getDependentProjects()
    {
        return dependentProjects;
    }


    /**
     * Set up the list of projects that need this project to complete.
     *
     * @param dependentProjects list of project summaries
     */
    public void setDependentProjects(List<RelatedMetadataElementSummary> dependentProjects)
    {
        this.dependentProjects = dependentProjects;
    }


    /**
     * Return the list of projects that this project needs to complete.
     *
     * @return list of project summaries
     */
    public List<RelatedMetadataElementSummary> getDependsOnProjects()
    {
        return dependsOnProjects;
    }


    /**
     * Set up the list of projects that this project needs to complete.
     *
     * @param dependsOnProjects list of project summaries
     */
    public void setDependsOnProjects(List<RelatedMetadataElementSummary> dependsOnProjects)
    {
        this.dependsOnProjects = dependsOnProjects;
    }


    /**
     * Return the governance definitions that support this governance definition.
     *
     * @return list of governance definition stubs
     */
    public List<ProjectHierarchy> getChildren()
    {
        return children;
    }


    /**
     * Set up the governance definitions that support this governance definition.
     *
     * @param children list of governance definition stubs
     */
    public void setChildren(List<ProjectHierarchy> children)
    {
        this.children = children;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "ProjectHierarchy{" +
                "dependentProjects=" + dependentProjects +
                ", dependsOnProjects=" + dependsOnProjects +
                ", children=" + children +
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
        if (! (objectToCompare instanceof ProjectHierarchy that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(dependsOnProjects, that.dependsOnProjects) &&
                Objects.equals(dependentProjects, that.dependentProjects) &&
                Objects.equals(children, that.children);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), dependsOnProjects, dependentProjects, children);
    }
}
