/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * RelationshipsResponse is the response structure used on the Subject Area OMAS REST API calls that returns a list of
 * relationships in a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipsResponse extends SubjectAreaOMASAPIResponse
{
    private List<Line> relationships = null;

    /**
     * Default constructor
     */
    public RelationshipsResponse()
    {
        this.setResponseCategory(ResponseCategory.Relationships);
    }
    public RelationshipsResponse(List<Line> relationships)
    {
        this();
        this.relationships=relationships;
    }


    /**
     * Return the relationships
     *
     * @return relationships
     */
    public List<Line> getRelationships()
    {
        return relationships;
    }

    /**
     * Set up the relationships
     *
     * @param relationships relationships associated with this response
     */
    public void setRelationships(List<Line>  relationships)
    {
        this.relationships = relationships;
    }


    @Override
    public String toString()
    {
        return "Relationship Response{" +
                "relationship=" + relationships +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
