/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermAnchorRelationship;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TermAnchorResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * TermAnchorRelationship object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermAnchorRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private TermAnchorRelationship termAnchorRelationship = null;

    /**
     * Default constructor
     */
    public TermAnchorRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.TermAnchorRelationship);
    }
    public TermAnchorRelationshipResponse(TermAnchorRelationship termAnchorRelationship)
    {
        this.termAnchorRelationship = termAnchorRelationship;
        this.setResponseCategory(ResponseCategory.TermAnchorRelationship);
    }

    /**
     * Return the TermAnchorRelationship object.
     *
     * @return TermAnchorRelationship
     */
    public TermAnchorRelationship getTermAnchorRelationship()
    {
        return termAnchorRelationship;
    }

    public void setTermAnchorRelationship(TermAnchorRelationship termHASARelationship)
    {
        this.termAnchorRelationship = termHASARelationship;
    }


    @Override
    public String toString()
    {
        return "TermAnchorResponse{" +
                "termAnchorRelationship=" + termAnchorRelationship +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
