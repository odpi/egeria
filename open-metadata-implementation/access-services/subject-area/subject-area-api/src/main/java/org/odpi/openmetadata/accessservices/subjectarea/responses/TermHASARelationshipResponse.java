/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Hasa;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.UsedInContext;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TermHASARelationshipResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * Hasa object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermHASARelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private Hasa termHASARelationship= null;

    /**
     * Default constructor
     */
    public TermHASARelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.TermHASARelationship);
    }
    public TermHASARelationshipResponse(Hasa termHASARelationship)
    {
        this();
        this.termHASARelationship=termHASARelationship;
    }


    /**
     * Return the Hasa object.
     *
     * @return termHASARelationshipResponse
     */
    public Hasa getTermHASARelationship()
    {
        return termHASARelationship;
    }

    public void setTermHASARelationship(Hasa termHASARelationship)
    {
        this.termHASARelationship = termHASARelationship;
    }


    @Override
    public String toString()
    {
        return "TermHASARelationshipResponse{" +
                "termHASARelationship=" + termHASARelationship +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }

    /**
     * UsedInContextResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
     * UsedInContext object as a response.
     */
    @JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class UsedInContextResponse extends SubjectAreaOMASAPIResponse
    {
        private UsedInContext termUsedInContextRelationship= null;

        /**
         * Default constructor
         */
        public UsedInContextResponse()
        {

            this.setResponseCategory(ResponseCategory.TermUsedInContextRelationship);
        }
        public UsedInContextResponse(UsedInContext termUsedInContextRelationship)
        {
            this();
            this.termUsedInContextRelationship=termUsedInContextRelationship;
        }


        /**
         * Return the UsedInContext object.
         *
         * @return termUsedInContextRelationshipResponse
         */
        public UsedInContext getUsedInContext()
        {
            return termUsedInContextRelationship;
        }

        public void setUsedInContext(UsedInContext termUsedInContextRelationship)
        {
            this.termUsedInContextRelationship = termUsedInContextRelationship;
        }


        @Override
        public String toString()
        {
            return "UsedInContextResponse{" +
                    "termUsedInContextRelationship=" + termUsedInContextRelationship +
                    ", relatedHTTPCode=" + relatedHTTPCode +
                    '}';
        }
    }
}
