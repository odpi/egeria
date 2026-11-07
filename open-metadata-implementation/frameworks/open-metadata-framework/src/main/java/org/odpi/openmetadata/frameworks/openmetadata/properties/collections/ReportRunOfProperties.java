/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.collections;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReportRunOfProperties describes the relationship between a results set and a report that was generated from it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReportRunOfProperties extends LabeledRelationshipProperties
{
    private String resultState       = null;
    private String resultStateReason = null;
    private String resultLocation    = null;


    /**
     * Default constructor
     */
    public ReportRunOfProperties()
    {
        super();
        super.typeName = OpenMetadataType.REPORT_RUN_OF_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ReportRunOfProperties(ReportRunOfProperties template)
    {
        super(template);

        if (template != null)
        {
            resultState       = template.getResultState();
            resultStateReason = template.getResultStateReason();
            resultLocation    = template.getResultLocation();
        }
    }


    /**
     * Return the status of the results.
     *
     * @return string
     */
    public String getResultState()
    {
        return resultState;
    }


    /**
     * Set up the status of the results.
     *
     * @param resultState string
     */
    public void setResultState(String resultState)
    {
        this.resultState = resultState;
    }


    /**
     * Return the reason for the result state.
     *
     * @return string
     */
    public String getResultStateReason()
    {
        return resultStateReason;
    }


    /**
     * Set up the reason for the result state.
     *
     * @param resultStateReason string
     */
    public void setResultStateReason(String resultStateReason)
    {
        this.resultStateReason = resultStateReason;
    }


    /**
     * Return where the results are stored.
     *
     * @return string
     */
    public String getResultLocation()
    {
        return resultLocation;
    }


    /**
     * Set up where the results are stored.
     *
     * @param resultLocation string
     */
    public void setResultLocation(String resultLocation)
    {
        this.resultLocation = resultLocation;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ReportRunOfProperties{" +
                "resultState='" + resultState + '\'' +
                ", resultStateReason='" + resultStateReason + '\'' +
                ", resultLocation='" + resultLocation + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ReportRunOfProperties that = (ReportRunOfProperties) objectToCompare;
        return Objects.equals(resultState, that.resultState) &&
                Objects.equals(resultStateReason, that.resultStateReason) &&
                Objects.equals(resultLocation, that.resultLocation);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), resultState, resultStateReason, resultLocation);
    }
}
