/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataGrainProperties is used to provide the characterizations of the granularity of data stored in a data source.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataGrainProperties extends DataValueSpecificationProperties
{
    private String granularityBasis    = null;
    private String grainStatement      = null;
    private Long   interval            = null;


    /**
     * Default constructor
     */
    public DataGrainProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_GRAIN.typeName;
    }


    /**
     * Copy/clone constructor for an editing glossary classification.
     *
     * @param template template object to copy.
     */
    public DataGrainProperties(DataGrainProperties template)
    {
        super(template);

        if (template != null)
        {
            granularityBasis    = template.getGranularityBasis();
            grainStatement      = template.getGrainStatement();
            interval            = template.getInterval();
        }
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataGrainProperties{" +
                "granularityBasis='" + granularityBasis + '\'' +
                ", grainStatement='" + grainStatement + '\'' +
                ", interval=" + interval +
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
        DataGrainProperties that = (DataGrainProperties) objectToCompare;
        return
                Objects.equals(interval, that.interval) &&
                Objects.equals(granularityBasis, that.granularityBasis) &&
                Objects.equals(grainStatement, that.grainStatement);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), granularityBasis, grainStatement, interval);
    }
}
