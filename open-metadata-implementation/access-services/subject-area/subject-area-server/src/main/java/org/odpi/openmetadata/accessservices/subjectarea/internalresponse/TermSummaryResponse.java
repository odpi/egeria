/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.internalresponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.TermSummary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TermSummaryResponse is a response structure used internally by the Subject Area OMAS REST API
 * to represent an omrs TermSummary
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermSummaryResponse extends SubjectAreaOMASAPIResponse
{
    private TermSummary termSummary= null;

    /**
     * Default constructor
     */
    public TermSummaryResponse() {
        this.setResponseCategory(ResponseCategory.TermSummary);
    }
    public TermSummaryResponse(TermSummary termSummary)
    {
        this();
        this.termSummary = termSummary;
    }


    /**
     * Return the TermSummary object.
     *
     * @return termSummary
     */
    public TermSummary getTermSummary()
    {
        return termSummary;
    }

    /**
     * Set up the TermSummary object.
     *
     * @param termSummary - termSummary object
     */
    public void setTermSummary(TermSummary termSummary)
    {
        this.termSummary = termSummary;
    }


    @Override
    public String toString()
    {
        return "TermSummaryResponse{" +
                "termSummary=" + termSummary +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
