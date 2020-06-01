/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.SemanticAssignment;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SemanticAssignmentResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * SemanticAssignment object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SemanticAssignementRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private SemanticAssignment semanticAssignment = null;

    /**
     * Default constructor
     */
    public SemanticAssignementRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.SemanticAssignmentRelationship);
    }
    public SemanticAssignementRelationshipResponse(SemanticAssignment semanticAssignment)
    {
        this();
        this.semanticAssignment =semanticAssignment;
    }

    /**
     * Return the SemanticAssignment object.
     *
     * @return SemanticAssignmentRelationshipResponse
     */
    public SemanticAssignment getSemanticAssignment()
    {
        return semanticAssignment;
    }

    public void setSemanticAssignment(SemanticAssignment semanticAssignment)
    {
        this.semanticAssignment = semanticAssignment;
    }


    @Override
    public String toString()
    {
        return "SemanticAssignmentResponse{" +
                "semanticAssignment=" + semanticAssignment +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
