/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TermsResponse is the response structure used on the Subject Area OMAS REST API calls that returns a List of
 * Terms as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermsResponse extends SubjectAreaOMASAPIResponse
{
    private List<Term> terms = null;

    /**
     * Default constructor
     */
    public TermsResponse()
    {
        this.setResponseCategory(ResponseCategory.Terms);
    }
    public TermsResponse(List<Term> terms)
    {
        this.terms=terms;
        this.setResponseCategory(ResponseCategory.Terms);
    }


    /**
     * Return the Terms object.
     *
     * @return terms
     */
    public List<Term> getTerms()
    {
        return terms;
    }

    /**
     * Set up the Terms object.
     *
     * @param terms - terms object
     */
    public void setTerms(List<Term> terms)
    {
        this.terms = terms;
    }


    @Override
    public String toString()
    {
        return "TermResponse{" +
                "terms=" + terms +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
