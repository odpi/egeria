/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SubjectAreaDefinitionsResponse is the response structure used on the Subject Area OMAS REST API calls that returns a List of
 * SubjectAreaDefinitions as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SubjectAreaDefinitionsResponse extends SubjectAreaOMASAPIResponse
{
    private List<SubjectAreaDefinition> subjectAreaDefinitions = null;

    /**
     * Default constructor
     */
    public SubjectAreaDefinitionsResponse()
    {
        this.setResponseCategory(ResponseCategory.SubjectAreaDefinitions);
    }
    public SubjectAreaDefinitionsResponse(List<SubjectAreaDefinition> subjectAreaDefinitions)
    {
        this();
        this.subjectAreaDefinitions=subjectAreaDefinitions;
    }


    /**
     * Return the SubjectAreaDefinitions object.
     *
     * @return subjectAreaDefinitions
     */
    public List<SubjectAreaDefinition> getSubjectAreaDefinitions()
    {
        return subjectAreaDefinitions;
    }

    /**
     * Set up the SubjectAreaDefinitions object.
     *
     * @param subjectAreaDefinitions - subjectAreaDefinitions object
     */
    public void setSubjectAreaDefinitions(List<SubjectAreaDefinition> subjectAreaDefinitions)
    {
        this.subjectAreaDefinitions = subjectAreaDefinitions;
    }


    @Override
    public String toString()
    {
        return "SubjectAreaDefinitionResponse{" +
                "subjectAreaDefinitions=" + subjectAreaDefinitions +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
