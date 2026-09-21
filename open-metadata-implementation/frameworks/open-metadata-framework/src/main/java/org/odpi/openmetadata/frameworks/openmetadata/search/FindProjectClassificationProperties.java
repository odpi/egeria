/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProjectClassificationProperties is used to classify a project to indicate how it is governed and the expectations
 * of how the results will be used.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindProjectClassificationProperties extends QueryOptions
{
    private String approach = null;
    private String managementStyle = null;
    private String resultsUsage = null;


    /**
     * Default constructor
     */
    public FindProjectClassificationProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public FindProjectClassificationProperties(FindProjectClassificationProperties template)
    {
        super(template);

        if (template != null)
        {
            approach = template.getApproach();
            managementStyle = template.getManagementStyle();
            resultsUsage = template.getResultsUsage();
        }
    }


    /**
     * Return the approach/method used to manage the project.
     *
     * @return string
     */
    public String getApproach()
    {
        return approach;
    }


    /**
     * Set up the approach/method used to manage the project.
     *
     * @param approach string
     */
    public void setApproach(String approach)
    {
        this.approach = approach;
    }


    /**
     * Return the management style used to manage the project.
     *
     * @return string
     */
    public String getManagementStyle()
    {
        return managementStyle;
    }


    /**
     * Set up the management style used to manage the project.
     *
     * @param managementStyle string
     */
    public void setManagementStyle(String managementStyle)
    {
        this.managementStyle = managementStyle;
    }


    /**
     * Return the usage of the results of the project.
     *
     * @return string
     */
    public String getResultsUsage()
    {
        return resultsUsage;
    }


    /**
     * Set up the usage of the results of the project.
     *
     * @param resultsUsage string
     */
    public void setResultsUsage(String resultsUsage)
    {
        this.resultsUsage = resultsUsage;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FindProjectClassificationProperties{" +
                "approach='" + approach + '\'' +
                ", managementStyle='" + managementStyle + '\'' +
                ", resultsUsage='" + resultsUsage + '\'' +
                "} " + super.toString();
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
        if (! (objectToCompare instanceof FindProjectClassificationProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(approach, that.approach) &&
                Objects.equals(managementStyle, that.managementStyle) &&
                Objects.equals(resultsUsage, that.resultsUsage);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), approach, managementStyle, resultsUsage);
    }
}
