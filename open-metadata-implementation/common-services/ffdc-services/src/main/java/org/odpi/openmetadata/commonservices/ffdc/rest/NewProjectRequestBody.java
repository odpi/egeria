/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewProjectRequestBody describes the properties to create a new project.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewProjectRequestBody extends NewElementRequestBody
{
    private ProjectProperties projectProperties = null;


    /**
     * Default constructor
     */
    public NewProjectRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewProjectRequestBody(NewProjectRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.projectProperties = template.getProjectProperties();
        }
    }


    /**
     * Return the properties for the project.
     *
     * @return properties
     */
    public ProjectProperties getProjectProperties()
    {
        return projectProperties;
    }


    /**
     * Set up the properties for the project.
     *
     * @param digitalProductProperties properties
     */
    public void setProjectProperties(ProjectProperties digitalProductProperties)
    {
        this.projectProperties = digitalProductProperties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewProjectRequestBody{" +
                "projectProperties=" + projectProperties +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof NewProjectRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(projectProperties, that.projectProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), projectProperties);
    }
}
