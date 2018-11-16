/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GlossaryResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * Glossary object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryResponse extends SubjectAreaOMASAPIResponse
{
    private Glossary glossary = null;

    /**
     * Default constructor
     */
    public GlossaryResponse() {
        this.setResponseCategory(ResponseCategory.Glossary);
    }
    public GlossaryResponse(Glossary glossary)
    {
        this();
        this.glossary=glossary;
    }


    /**
     * Return the Glossary object.
     *
     * @return glossary
     */
    public Glossary getGlossary()
    {
        return glossary;
    }

    /**
     * Set up the Glossary object.
     *
     * @param glossary - glossary object
     */
    public void setGlossary(Glossary glossary)
    {
        this.glossary = glossary;
    }


    @Override
    public String toString()
    {
        return "GlossaryResponse{" +
                "glossary=" + glossary +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
