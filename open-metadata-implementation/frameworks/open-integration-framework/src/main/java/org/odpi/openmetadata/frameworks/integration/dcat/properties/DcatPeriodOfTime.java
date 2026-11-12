/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DcatPeriodOfTime describes a temporal period covered by a dataset (dct:PeriodOfTime).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DcatPeriodOfTime
{
    private String startDate = null;
    private String endDate = null;


    /**
     * Default constructor
     */
    public DcatPeriodOfTime()
    {
    }


    /**
     * Return the start of the period (dcat:startDate).
     *
     * @return string date
     */
    public String getStartDate()
    {
        return startDate;
    }


    /**
     * Set up the start of the period (dcat:startDate).
     *
     * @param startDate string date
     */
    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Return the end of the period (dcat:endDate).
     *
     * @return string date
     */
    public String getEndDate()
    {
        return endDate;
    }


    /**
     * Set up the end of the period (dcat:endDate).
     *
     * @param endDate string date
     */
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DcatPeriodOfTime{" +
                       "startDate='" + startDate + '\'' +
                       ", endDate=" + endDate +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
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
        DcatPeriodOfTime that = (DcatPeriodOfTime) objectToCompare;
        return Objects.equals(startDate, that.startDate) &&
                       Objects.equals(endDate, that.endDate);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(startDate, endDate);
    }
}
