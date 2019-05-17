/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * projectsResponse is the response structure used on the Subject Area OMAS REST API calls that returns a List of
 * Projects as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectsResponse extends SubjectAreaOMASAPIResponse
{
    private List<Project> Projects = null;

    /**
     * Default constructor
     */
    public ProjectsResponse()
    {
        this.setResponseCategory(ResponseCategory.Projects);
    }
    public ProjectsResponse(List<Project> Projects)
    {
        this();
        this.Projects=Projects;
    }


    /**
     * Return the Projects object.
     *
     * @return Projects
     */
    public List<Project> getProjects()
    {
        return Projects;
    }

    /**
     * Set up the Projects object.
     *
     * @param Projects - Projects object
     */
    public void setProjects(List<Project> Projects)
    {
        this.Projects = Projects;
    }


    @Override
    public String toString()
    {
        return "ProjectResponse{" +
                "Projects=" + Projects +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
