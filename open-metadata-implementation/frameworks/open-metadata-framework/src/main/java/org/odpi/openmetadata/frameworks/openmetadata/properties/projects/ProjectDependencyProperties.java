/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.projects;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProjectDependencyProperties identifies dependencies between projects.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectDependencyProperties extends RelationshipBeanProperties
{
    String projectDependency = null;

    /**
     * Default constructor
     */
    public ProjectDependencyProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProjectDependencyProperties(ProjectDependencyProperties template)
    {
        super(template);

        if (template != null)
        {
            this.projectDependency = template.getProjectDependency();
        }
    }


    /**
     * Return the membership type.
     *
     * @return membership type
     */
    public String getProjectDependency()
    {
        return projectDependency;
    }


    /**
     * Set up the membership type.
     *
     * @param projectDependency membership type
     */
    public void setProjectDependency(String projectDependency)
    {
        this.projectDependency = projectDependency;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProjectDependencyProperties{" +
                "projectDependency='" + projectDependency + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ProjectDependencyProperties that = (ProjectDependencyProperties) objectToCompare;
        return Objects.equals(projectDependency, that.projectDependency);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), projectDependency);
    }
}
