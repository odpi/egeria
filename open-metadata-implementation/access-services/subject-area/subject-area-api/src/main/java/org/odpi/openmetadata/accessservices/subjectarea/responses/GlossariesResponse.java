/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GlossariesResponse is the response structure used on the Subject Area OMAS REST API calls that returns a List of
 * Glossaries as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossariesResponse extends SubjectAreaOMASAPIResponse
{
    private List<Glossary> glossaries = null;

    /**
     * Default constructor
     */
    public GlossariesResponse()
    {
        this.setResponseCategory(ResponseCategory.Glossaries);
    }
    public GlossariesResponse(List<Glossary> glossaries)
    {
        this();
        this.glossaries=glossaries;
    }


    /**
     * Return the Glossaries object.
     *
     * @return glossaries
     */
    public List<Glossary> getGlossaries()
    {
        return glossaries;
    }

    /**
     * Set up the Glossaries object.
     *
     * @param glossaries - glossaries object
     */
    public void setGlossaries(List<Glossary> glossaries)
    {
        this.glossaries = glossaries;
    }


    @Override
    public String toString()
    {
        return "GlossaryResponse{" +
                "glossaries=" + glossaries +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
