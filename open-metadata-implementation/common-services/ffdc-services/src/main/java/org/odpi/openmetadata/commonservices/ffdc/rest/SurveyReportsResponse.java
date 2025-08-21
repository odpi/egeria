/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.opensurvey.properties.SurveyReport;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SurveyReportsResponse is the response structure used on OMAS REST API calls that return a
 * list of SurveyReport properties objects as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SurveyReportsResponse extends FFDCResponseBase
{
    private List<SurveyReport> surveyReports = null;

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
            this.surveyReports = template.getSurveyReports();
        }
    }


    /**
     * Return the properties objects.
     *
     * @return list of properties objects
     */
    public List<SurveyReport> getSurveyReports()
    {
        return surveyReports;
    }


    /**
     * Set up the properties objects.
     *
     * @param surveyReports  list of properties objects
     */
    public void setSurveyReports(List<SurveyReport> surveyReports)
    {
        this.surveyReports = surveyReports;
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
                "surveyReports=" + surveyReports +
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
