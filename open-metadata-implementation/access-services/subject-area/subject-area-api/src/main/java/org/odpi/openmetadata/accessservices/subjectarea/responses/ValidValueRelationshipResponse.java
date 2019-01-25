/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ValidValue;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ValidValueRelationshipResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * TermValidValueRelationship object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private ValidValue termValidValueRelationship= null;

    /**
     * Default constructor
     */
    public ValidValueRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.ValidValueRelationship);
    }
    public ValidValueRelationshipResponse(ValidValue termValidValueRelationship)
    {
        this();
        this.termValidValueRelationship=termValidValueRelationship;
    }


    /**
     * Return the ValidValue object.
     *
     * @return termValidValueRelationshipResponse
     */
    public ValidValue getValidValue()
    {
        return termValidValueRelationship;
    }

    public void setValidValue(ValidValue termValidValueRelationship)
    {
        this.termValidValueRelationship = termValidValueRelationship;
    }


    @Override
    public String toString()
    {
        return "ValidValueRelationshipResponse{" +
                "termValidValueRelationship=" + termValidValueRelationship +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
