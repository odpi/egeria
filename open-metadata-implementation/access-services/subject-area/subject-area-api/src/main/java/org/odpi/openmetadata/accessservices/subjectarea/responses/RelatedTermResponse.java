/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.RelatedTerm;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * RelatedTermResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * RelatedTerm object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedTermResponse extends SubjectAreaOMASAPIResponse
{
    private RelatedTerm relatedTermRelationship = null;

    /**
     * Default constructor
     */
    public RelatedTermResponse()
    {
        this.setResponseCategory(ResponseCategory.RelatedTerm);
    }
    public RelatedTermResponse(RelatedTerm relatedTermRelationship)
    {
        this.relatedTermRelationship = relatedTermRelationship;
        this.setResponseCategory(ResponseCategory.RelatedTerm);
    }


    /**
     * Return the RelatedTerm object.
     *
     * @return relatedTermResponse
     */
    public RelatedTerm getRelatedTerm()
    {
        return relatedTermRelationship;
    }

    public void setRelatedTerm(RelatedTerm relatedTermRelationship)
    {
        this.relatedTermRelationship = relatedTermRelationship;
    }


    @Override
    public String toString()
    {
        return "RelatedTermResponse{" +
                "relatedTermRelationship=" + relatedTermRelationship +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
