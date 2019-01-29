/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermCategorization;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermCategorization;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TermCategorizationResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * TermCategorization object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermCategorizationRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private TermCategorization termCategorization = null;

    /**
     * Default constructor
     */
    public TermCategorizationRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.TermCategorizationRelationship);
    }
    public TermCategorizationRelationshipResponse(TermCategorization termCategorization)
    {
        this();
        this.termCategorization =termCategorization;
    }

    /**
     * Return the TermCategorization object.
     *
     * @return TermCategorizationRelationshipResponse
     */
    public TermCategorization getTermCategorization()
    {
        return termCategorization;
    }

    public void setTermCategorization(TermCategorization termCategorization)
    {
        this.termCategorization = termCategorization;
    }


    @Override
    public String toString()
    {
        return "TermCategorizationResponse{" +
                "termCategorization=" + termCategorization +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
