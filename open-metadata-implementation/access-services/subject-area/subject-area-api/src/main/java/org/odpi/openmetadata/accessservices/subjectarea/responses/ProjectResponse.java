/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ProjectResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * Project object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectResponse extends SubjectAreaOMASAPIResponse
{
    private Project project = null;

    /**
     * Default constructor
     */
    public ProjectResponse()
    {
        this.setResponseCategory(ResponseCategory.Project);
    }
    public ProjectResponse(Project project)
    {
        this.project=project;
        this.setResponseCategory(ResponseCategory.Project);
    }


    /**
     * Return the Project object.
     *
     * @return project
     */
    public Project getProject()
    {
        return project;
    }

    /**
     * Set up the Project object.
     *
     * @param project - project object
     */
    public void setProject(Project project)
    {
        this.project = project;
    }


    @Override
    public String toString()
    {
        return "ProjectResponse{" +
                "project=" + project +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
