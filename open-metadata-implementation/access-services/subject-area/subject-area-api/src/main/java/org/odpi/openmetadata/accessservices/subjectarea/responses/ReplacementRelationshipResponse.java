/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ReplacementTerm;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ReplacementRelationshipResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * TermReplacementRelationship object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReplacementRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private ReplacementTerm termReplacementRelationship= null;

    /**
     * Default constructor
     */
    public ReplacementRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.TermReplacementRelationship);
    }
    public ReplacementRelationshipResponse(ReplacementTerm termReplacementRelationship)
    {
        this();
        this.termReplacementRelationship=termReplacementRelationship;
    }


    /**
     * Return the TermReplacementRelationship object.
     *
     * @return termReplacementRelationshipResponse
     */
    public ReplacementTerm getTermReplacementRelationship()
    {
        return termReplacementRelationship;
    }

    public void setTermReplacementRelationship(ReplacementTerm termReplacementRelationship)
    {
        this.termReplacementRelationship = termReplacementRelationship;
    }


    @Override
    public String toString()
    {
        return "ReplacementRelationshipResponse{" +
                "termReplacementRelationship=" + termReplacementRelationship +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
