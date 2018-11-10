/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.SubjectAreaDefinition;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SubjectAreaDefinitionResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * SubjectAreaDefinition object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SubjectAreaDefinitionResponse extends SubjectAreaOMASAPIResponse
{
    private SubjectAreaDefinition subjectArea = null;

    /**
     * Default constructor
     */
    public SubjectAreaDefinitionResponse() {
        this.setResponseCategory(ResponseCategory.SubjectAreaDefinition);
    }
    public SubjectAreaDefinitionResponse(SubjectAreaDefinition subjectArea)
    {
        this();
        this.subjectArea=subjectArea;
    }


    /**
     * Return the SubjectAreaDefinition object.
     *
     * @return subjectArea
     */
    public SubjectAreaDefinition getSubjectAreaDefinition()
    {
        return subjectArea;
    }

    /**
     * Set up the SubjectAreaDefinition object.
     *
     * @param subjectArea - subjectArea object
     */
    public void setSubjectAreaDefinition(SubjectAreaDefinition subjectArea)
    {
        this.subjectArea = subjectArea;
    }


    @Override
    public String toString()
    {
        return "SubjectAreaDefinitionResponse{" +
                "subjectArea=" + subjectArea +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
