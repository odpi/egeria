/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SynonymResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * Synonym object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SynonymRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private Synonym synonym = null;

    /**
     * Default constructor
     */
    public SynonymRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.SynonymRelationship);
    }
    public SynonymRelationshipResponse(Synonym synonym)
    {
        this();
        this.synonym =synonym;
    }

    /**
     * Return the Synonym object.
     *
     * @return SynonymRelationshipResponse
     */
    public Synonym getSynonym()
    {
        return synonym;
    }

    public void setSynonym(Synonym synonym)
    {
        this.synonym = synonym;
    }


    @Override
    public String toString()
    {
        return "SynonymResponse{" +
                "synonym=" + synonym +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
