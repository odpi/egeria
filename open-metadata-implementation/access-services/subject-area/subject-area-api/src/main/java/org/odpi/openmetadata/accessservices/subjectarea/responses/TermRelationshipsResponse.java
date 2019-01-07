/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TermRelationshipsResponse is the response structure used on the Subject Area OMAS REST API calls that returns a list of
 * relationships associated with a Term in a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermRelationshipsResponse extends SubjectAreaOMASAPIResponse
{
    private List<Line> relationships = null;

    /**
     * Default constructor
     */
    public TermRelationshipsResponse()
    {
        this.setResponseCategory(ResponseCategory.TermRelationships);
    }
    public TermRelationshipsResponse(List<Line> relationships)
    {
        this.relationships=relationships;
        this.setResponseCategory(ResponseCategory.TermRelationships);
    }


    /**
     * Return the term relationships
     *
     * @return term relationships
     */
    public List<Line>  getTermRelationships()
    {
        return relationships;
    }

    /**
     * Set up the term relationships
     *
     * @param relationships - term relationships
     */
    public void setTermRelationships(List<Line>  relationships)
    {
        this.relationships = relationships;
    }


    @Override
    public String toString()
    {
        return "TermRelationship Response{" +
                "termRelationship=" + relationships +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
