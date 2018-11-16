/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.RelatedTermRelationship;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * RelatedTermRelationshipResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * RelatedTermRelationship object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedTermRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private RelatedTermRelationship relatedTermRelationship = null;

    /**
     * Default constructor
     */
    public RelatedTermRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.RelatedTerm);
    }
    public RelatedTermRelationshipResponse(RelatedTermRelationship relatedTermRelationship)
    {
        this.relatedTermRelationship = relatedTermRelationship;
        this.setResponseCategory(ResponseCategory.RelatedTerm);
    }


    /**
     * Return the RelatedTermRelationship object.
     *
     * @return relatedTermResponse
     */
    public RelatedTermRelationship getRelatedTermRelationship()
    {
        return relatedTermRelationship;
    }

    public void setRelatedTermRelationship(RelatedTermRelationship relatedTermRelationship)
    {
        this.relatedTermRelationship = relatedTermRelationship;
    }


    @Override
    public String toString()
    {
        return "RelatedTermRelationshipResponse{" +
                "relatedTermRelationship=" + relatedTermRelationship +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
