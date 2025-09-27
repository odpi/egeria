/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.SurveyReportProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SurveyReportsResponse is the response structure used on OMAS REST API calls that return a
 * list of SurveyReportProperties properties objects as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SurveyReportsResponse extends FFDCResponseBase
{
    private List<SurveyReportProperties> surveyReportProperties = null;

    /**
     * Default constructor
     */
    public SurveyReportsResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SurveyReportsResponse(SurveyReportsResponse template)
    {
        super(template);

        if (template != null)
        {
            this.surveyReportProperties = template.getSurveyReports();
        }
    }


    /**
     * Return the properties objects.
     *
     * @return list of properties objects
     */
    public List<SurveyReportProperties> getSurveyReports()
    {
        return surveyReportProperties;
    }


    /**
     * Set up the properties objects.
     *
     * @param surveyReportProperties  list of properties objects
     */
    public void setSurveyReports(List<SurveyReportProperties> surveyReportProperties)
    {
        this.surveyReportProperties = surveyReportProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SurveyReportsResponse{" +
                "surveyReportProperties=" + surveyReportProperties +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SurveyReportsResponse that = (SurveyReportsResponse) objectToCompare;
        return Objects.equals(getSurveyReports(), that.getSurveyReports());
    }

    
    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSurveyReports());
    }
}
