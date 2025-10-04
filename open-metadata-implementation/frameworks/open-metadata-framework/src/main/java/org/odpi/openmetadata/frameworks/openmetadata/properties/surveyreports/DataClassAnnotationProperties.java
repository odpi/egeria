/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataClassAnnotationProperties recommends a data class that potentially matches this data field.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataClassAnnotationProperties extends DataFieldAnnotationProperties
{
    private List<String> candidateDataClassGUIDs = null;
    private long         matchingValues          = 0L;
    private long         nonMatchingValues       = 0L;


    /**
     * Default constructor
     */
    public DataClassAnnotationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DATA_CLASS_ANNOTATION.typeName);
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataClassAnnotationProperties(DataClassAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            candidateDataClassGUIDs = template.getCandidateDataClassGUIDs();
            matchingValues = template.getMatchingValues();
            nonMatchingValues = template.getNonMatchingValues();
        }
    }


    /**
     * Return the identifiers of data classes that seem to match the values in this data field.
     *
     * @return list of unique identifiers for data classes
     */
    public List<String> getCandidateDataClassGUIDs()
    {
        return candidateDataClassGUIDs;
    }


    /**
     * Set up the identifiers of data classes that seem to match the values in this data field.
     *
     * @param candidateDataClassGUIDs list of guids
     */
    public void setCandidateDataClassGUIDs(List<String> candidateDataClassGUIDs)
    {
        this.candidateDataClassGUIDs = candidateDataClassGUIDs;
    }


    /**
     * Return the count of matching values that match the specification of these data classes.
     *
     * @return long
     */
    public long getMatchingValues()
    {
        return matchingValues;
    }


    /**
     * Set up the count of matching values that match the specification of these data classes.
     *
     * @param matchingValues long
     */
    public void setMatchingValues(long matchingValues)
    {
        this.matchingValues = matchingValues;
    }


    /**
     * Return the count of values that do not match the specification of these data classes.
     *
     * @return long
     */
    public long getNonMatchingValues()
    {
        return nonMatchingValues;
    }


    /**
     * Set up the count of values that do not match the specification of these data classes.
     *
     * @param nonMatchingValues long
     */
    public void setNonMatchingValues(long nonMatchingValues)
    {
        this.nonMatchingValues = nonMatchingValues;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataClassAnnotationProperties{" +
                "candidateDataClassGUIDs=" + candidateDataClassGUIDs +
                ", matchingValues=" + matchingValues +
                ", nonMatchingValues=" + nonMatchingValues +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DataClassAnnotationProperties that = (DataClassAnnotationProperties) objectToCompare;
        return matchingValues == that.matchingValues &&
                nonMatchingValues == that.nonMatchingValues &&
                Objects.equals(candidateDataClassGUIDs, that.candidateDataClassGUIDs);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), candidateDataClassGUIDs, matchingValues, nonMatchingValues);
    }
}
