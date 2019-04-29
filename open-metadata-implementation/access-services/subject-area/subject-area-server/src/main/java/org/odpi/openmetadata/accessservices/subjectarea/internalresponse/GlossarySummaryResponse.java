/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.internalresponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GlossarySummaryResponse is a response structure used internally by the Subject Area OMAS REST API
 * to represent an omrs GlossarySummary
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossarySummaryResponse extends SubjectAreaOMASAPIResponse
{
    private GlossarySummary glossarySummary= null;

    /**
     * Default constructor
     */
    public GlossarySummaryResponse() {
        this.setResponseCategory(ResponseCategory.GlossarySummary);
    }
    public GlossarySummaryResponse(GlossarySummary glossarySummary)
    {
        this();
        this.glossarySummary = glossarySummary;
    }


    /**
     * Return the GlossarySummary object.
     *
     * @return glossarySummary
     */
    public GlossarySummary getGlossarySummary()
    {
        return glossarySummary;
    }

    /**
     * Set up the GlossarySummary object.
     *
     * @param glossarySummary - glossarySummary object
     */
    public void setGlossarySummary(GlossarySummary glossarySummary)
    {
        this.glossarySummary = glossarySummary;
    }


    @Override
    public String toString()
    {
        return "GlossarySummaryResponse{" +
                "glossarySummary=" + glossarySummary +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
