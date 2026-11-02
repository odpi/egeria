/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataGrainAnnotationProperties recommends a data grain that potentially matches this data source.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataGrainAnnotationProperties extends DataFieldAnnotationProperties
{
    private String granularityBasis    = null;
    private String grainStatement      = null;
    private Long   interval            = null;
    private List<String> candidateDataGrainGUIDs = null;



    /**
     * Default constructor
     */
    public DataGrainAnnotationProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_GRAIN_ANNOTATION.typeName;
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataGrainAnnotationProperties(DataGrainAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            granularityBasis        = template.getGranularityBasis();
            grainStatement          = template.getGrainStatement();
            candidateDataGrainGUIDs = template.getCandidateDataGrainGUIDs();
            interval                = template.getInterval();
        }
    }


    /**
     * Return the granularity basis.
     *
     * @return string name
     */
    public String getGranularityBasis()
    {
        return granularityBasis;
    }


    /**
     * Set up the granularity basis.
     *
     * @param granularityBasis string name
     */
    public void setGranularityBasis(String granularityBasis)
    {
        this.granularityBasis = granularityBasis;
    }


    /**
     * Return the grain statement.
     *
     * @return string
     */
    public String getGrainStatement()
    {
        return grainStatement;
    }


    /**
     * Set up the grain statement.
     *
     * @param grainStatement string
     */
    public void setGrainStatement(String grainStatement)
    {
        this.grainStatement = grainStatement;
    }


    /**
     * Return the interval.
     *
     * @return long
     */
    public Long getInterval()
    {
        return interval;
    }


    /**
     * Set up the interval.
     *
     * @param interval long
     */
    public void setInterval(Long interval)
    {
        this.interval = interval;
    }


    /**
     * Return the identifiers of data classes that seem to match the values in this data field.
     *
     * @return list of unique identifiers for data classes
     */
    public List<String> getCandidateDataGrainGUIDs()
    {
        return candidateDataGrainGUIDs;
    }


    /**
     * Set up the identifiers of data classes that seem to match the values in this data field.
     *
     * @param candidateDataGrainGUIDs list of guids
     */
    public void setCandidateDataGrainGUIDs(List<String> candidateDataGrainGUIDs)
    {
        this.candidateDataGrainGUIDs = candidateDataGrainGUIDs;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataGrainAnnotationProperties{" +
                "granularityBasis='" + granularityBasis + '\'' +
                ", grainStatement='" + grainStatement + '\'' +
                ", interval=" + interval +
                ", candidateDataGrainGUIDs=" + candidateDataGrainGUIDs +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DataGrainAnnotationProperties that = (DataGrainAnnotationProperties) objectToCompare;
        return Objects.equals(interval, that.interval) &&
                Objects.equals(granularityBasis, that.granularityBasis) &&
                Objects.equals(grainStatement, that.grainStatement) &&
                Objects.equals(candidateDataGrainGUIDs, that.candidateDataGrainGUIDs);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), candidateDataGrainGUIDs, granularityBasis, grainStatement, interval);
    }
}
