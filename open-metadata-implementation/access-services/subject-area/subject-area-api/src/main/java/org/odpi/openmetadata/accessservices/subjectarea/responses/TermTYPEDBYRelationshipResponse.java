/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermTYPEDBYRelationship;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TermTYPEDBYRelationshipResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * TermTYPEDBYRelationship object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermTYPEDBYRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private TermTYPEDBYRelationship termTYPEDBYRelationship= null;

    /**
     * Default constructor
     */
    public TermTYPEDBYRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.TermTYPEDBYRelationship);
    }
    public TermTYPEDBYRelationshipResponse(TermTYPEDBYRelationship termTYPEDBYRelationship)
    {
        this.termTYPEDBYRelationship=termTYPEDBYRelationship;
        this.setResponseCategory(ResponseCategory.TermTYPEDBYRelationship);
    }


    /**
     * Return the TermTYPEDBYRelationship object.
     *
     * @return termTYPEDBYRelationshipResponse
     */
    public TermTYPEDBYRelationship getTermTYPEDBYRelationship()
    {
        return termTYPEDBYRelationship;
    }

    public void setTermTYPEDBYRelationship(TermTYPEDBYRelationship termTYPEDBYRelationship)
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
