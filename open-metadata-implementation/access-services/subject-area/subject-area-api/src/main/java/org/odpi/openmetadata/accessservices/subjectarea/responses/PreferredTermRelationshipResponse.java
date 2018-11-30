/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.PreferredTerm;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * PreferredTermRelationshipResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * PreferredTermRelationship object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PreferredTermRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private PreferredTerm preferredTerm = null;

    /**
     * Default constructor
     */
    public PreferredTermRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.PreferredTermRelationship);
    }
    public PreferredTermRelationshipResponse(PreferredTerm preferredTermRelationship)
    {
        this.preferredTerm =preferredTermRelationship;
        this.setResponseCategory(ResponseCategory.PreferredTermRelationship);
    }


    /**
     * Return the PreferredTermRelationship object.
     *
     * @return preferredTermRelationshipResponse
     */
    public PreferredTerm getPreferredTerm()
    {
        return preferredTerm;
    }

    public void setPreferredTerm(PreferredTerm preferredTerm)
    {
        this.preferredTerm = preferredTerm;
    }


    @Override
    public String toString()
    {
        return "PreferredTermRelationshipResponse{" +
                "preferredTerm=" + preferredTerm +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
