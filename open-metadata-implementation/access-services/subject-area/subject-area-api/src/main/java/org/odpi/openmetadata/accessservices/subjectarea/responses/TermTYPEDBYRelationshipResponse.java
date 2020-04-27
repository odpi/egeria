/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TypedBy;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TermTYPEDBYRelationshipResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * TypedBy object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermTYPEDBYRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private TypedBy termTYPEDBYRelationship= null;

    /**
     * Default constructor
     */
    public TermTYPEDBYRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.TermTYPEDBYRelationship);
    }
    public TermTYPEDBYRelationshipResponse(TypedBy termTYPEDBYRelationship)
    {
        this();
        this.termTYPEDBYRelationship=termTYPEDBYRelationship;
    }


    /**
     * Return the TypedBy object.
     *
     * @return termTYPEDBYRelationshipResponse
     */
    public TypedBy getTermTYPEDBYRelationship()
    {
        return termTYPEDBYRelationship;
    }

    public void setTermTYPEDBYRelationship(TypedBy termTYPEDBYRelationship)
    {
        this.termTYPEDBYRelationship = termTYPEDBYRelationship;
    }


    @Override
    public String toString()
    {
        return "TermTYPEDBYRelationshipResponse{" +
                "termTYPEDBYRelationship=" + termTYPEDBYRelationship +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
