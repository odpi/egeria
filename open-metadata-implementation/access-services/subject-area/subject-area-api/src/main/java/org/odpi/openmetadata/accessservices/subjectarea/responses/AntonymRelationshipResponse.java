/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Antonym;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AntonymResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * Antonym object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AntonymRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private Antonym antonym = null;

    /**
     * Default constructor
     */
    public AntonymRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.AntonymRelationship);
    }
    public AntonymRelationshipResponse(Antonym antonym)
    {
        this.antonym =antonym;
        this.setResponseCategory(ResponseCategory.AntonymRelationship);
    }

    /**
     * Return the Antonym object.
     *
     * @return AntonymRelationshipResponse
     */
    public Antonym getAntonym()
    {
        return antonym;
    }

    public void setAntonym(Antonym termHASARelationship)
    {
        this.antonym = termHASARelationship;
    }


    @Override
    public String toString()
    {
        return "AntonymResponse{" +
                "antonym=" + antonym +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
