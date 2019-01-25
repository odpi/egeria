/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TermResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * Term object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermResponse extends SubjectAreaOMASAPIResponse
{
    private Term term = null;

    /**
     * Default constructor
     */
    public TermResponse()
    {
        this.setResponseCategory(ResponseCategory.Term);
    }
    public TermResponse(Term term)
    {
        this();
        this.term=term;
    }


    /**
     * Return the Term object.
     *
     * @return term
     */
    public Term getTerm()
    {
        return term;
    }

    /**
     * Set up the Term object.
     *
     * @param term - term object
     */
    public void setTerm(Term term)
    {
        this.term = term;
    }


    @Override
    public String toString()
    {
        return "TermResponse{" +
                "term=" + term +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
