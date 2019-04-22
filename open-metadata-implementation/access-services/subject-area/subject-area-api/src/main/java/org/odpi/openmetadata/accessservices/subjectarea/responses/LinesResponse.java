/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * LinesResponse is the response structure used on the Subject Area OMAS REST API calls that returns a list of
 * Lines in a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LinesResponse extends SubjectAreaOMASAPIResponse
{
    private List<Line> lines = null;

    /**
     * Default constructor
     */
    public LinesResponse()
    {
        this.setResponseCategory(ResponseCategory.Lines);
    }
    public LinesResponse(List<Line> lines)
    {
        this();
        this.lines=lines;
    }


    /**
     * Return the Lines
     *
     * @return Lines
     */
    public List<Line> getLines()
    {
        return lines;
    }

    /**
     * Set up the Lines
     *
     * @param lines Lines associated with this response
     */
    public void setLines(List<Line>  lines)
    {
        this.lines = lines;
    }


    @Override
    public String toString()
    {
        return "Line Response{" +
                "relationship=" + lines +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
