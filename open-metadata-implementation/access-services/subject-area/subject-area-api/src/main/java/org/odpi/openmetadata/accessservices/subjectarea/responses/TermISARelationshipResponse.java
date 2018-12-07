/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ISARelationship;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ISARelationshipResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * ISARelationship object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermISARelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private ISARelationship termISARelationship = null;

    /**
     * Default constructor
     */
    public TermISARelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.TermISARelationship);
    }
    public TermISARelationshipResponse(ISARelationship termISARelationship)
    {
        this.termISARelationship =termISARelationship;
        this.setResponseCategory(ResponseCategory.TermISARelationship);
    }


    /**
     * Return the ISARelationship object.
     *
     * @return iSARelationshipResponse
     */
    public ISARelationship getTermISARelationship()
    {
        return termISARelationship;
    }

    public void setTermISARelationship(ISARelationship termISARelationship)
    {
        this.termISARelationship = termISARelationship;
    }


    @Override
    public String toString()
    {
        return "ISARelationshipResponse{" +
                "termISARelationship=" + termISARelationship +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
