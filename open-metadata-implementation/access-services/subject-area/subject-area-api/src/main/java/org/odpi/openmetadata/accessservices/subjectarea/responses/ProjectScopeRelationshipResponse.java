/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ProjectScopeRelationship;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ProjectScopeResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * ProjectScope object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectScopeRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private ProjectScopeRelationship projectScope = null;

    /**
     * Default constructor
     */
    public ProjectScopeRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.ProjectScopeRelationship);
    }
    public ProjectScopeRelationshipResponse(ProjectScopeRelationship projectScope)
    {
        this();
        this.projectScope =projectScope;
    }

    /**
     * Return the ProjectScope object.
     *
     * @return ProjectScopeRelationshipResponse
     */
    public ProjectScopeRelationship getProjectScope()
    {
        return projectScope;
    }

    public void setProjectScope(ProjectScopeRelationship projectScope)
    {
        this.projectScope = projectScope;
    }


    @Override
    public String toString()
    {
        return "ProjectScopeResponse{" +
                "projectScope=" + projectScope +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
