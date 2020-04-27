/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.IsaTypeOf;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TermISATYPEOFRelationshipResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * TermISATYPEOFRelationship object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermISATYPEOFRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private IsaTypeOf termISATYPEOFRelationship= null;

    /**
     * Default constructor
     */
    public TermISATYPEOFRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.TermISATYPEOFRelationship);
    }
    public TermISATYPEOFRelationshipResponse(IsaTypeOf termISATYPEOFRelationship)
    {
        this();
        this.termISATYPEOFRelationship=termISATYPEOFRelationship;
    }


    /**
     * Return the TermISATYPEOFRelationship object.
     *
     * @return termISATYPEOFRelationshipResponse
     */
    public IsaTypeOf getTermISATYPEOFRelationship()
    {
        return termISATYPEOFRelationship;
    }

    public void setTermISATYPEOFRelationship(IsaTypeOf termISATYPEOFRelationship)
    {
        this.termISATYPEOFRelationship = termISATYPEOFRelationship;
    }


    @Override
    public String toString()
    {
        return "TermISATYPEOFRelationshipResponse{" +
                "termISATYPEOFRelationship=" + termISATYPEOFRelationship +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
