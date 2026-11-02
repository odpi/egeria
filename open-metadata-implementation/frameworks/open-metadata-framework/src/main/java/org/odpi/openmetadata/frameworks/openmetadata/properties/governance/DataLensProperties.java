/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataLensProperties provides the JavaBean for describing the scope of data needed for specific processing.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class DataLensProperties extends GovernanceControlProperties
{
    private Float               minLongitude            = null;
    private Float               minLatitude             = null;
    private Float               maxLongitude            = null;
    private Float               maxLatitude             = null;
    private Float               minHeight               = null;
    private Float               maxHeight               = null;
    private Date                dataCollectionStartTime = null;
    private Date                dataCollectionEndTime   = null;
    private Date                dataValidityStartTime   = null;
    private Date                dataValidityEndTime     = null;
    private Date                dataCoverageStartTime   = null;
    private Date                dataCoverageEndTime     = null;
    private Map<String, String> scopeElements           = null;


    /**
     * Default constructor
     */
    public DataLensProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_LENS.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataLensProperties(DataLensProperties template)
    {
        super(template);

        if (template != null)
        {
            minLongitude            = template.getMinLongitude();
            minLatitude             = template.getMinLatitude();
            maxLongitude            = template.getMaxLongitude();
            maxLatitude             = template.getMaxLatitude();
            maxHeight               = template.getMaxHeight();
            minHeight               = template.getMinHeight();
            dataCollectionStartTime = template.getDataCollectionStartTime();
            dataCollectionEndTime   = template.getDataCollectionEndTime();
            dataValidityStartTime   = template.getDataValidityStartTime();
            dataValidityEndTime     = template.getDataValidityEndTime();
            dataCoverageStartTime   = template.getDataCoverageStartTime();
            dataCoverageEndTime     = template.getDataCoverageEndTime();
            scopeElements           = template.getScopeElements();
        }
    }


    /**
     * Return the minimum longitude in a bounded box.
     *
     * @return float
     */
    public Float getMinLongitude()
    {
        return minLongitude;
    }


    /**
     * Set up the minimum longitude in a bounded box.
     *
     * @param minLongitude float
     */
    public void setMinLongitude(Float minLongitude)
    {
        this.minLongitude = minLongitude;
    }


    /**
     * Return the minimum latitude in a bounded box.
     *
     * @return float
     */
    public Float getMinLatitude()
    {
        return minLatitude;
    }


    /**
     * Set up the minimum latitude in a bounded box.
     *
     * @param minLatitude float
     */
    public void setMinLatitude(Float minLatitude)
    {
        this.minLatitude = minLatitude;
    }


    /**
     * Return the max longitude in a bounded box.
     *
     * @return float
     */
    public Float getMaxLongitude()
    {
        return maxLongitude;
    }


    /**
     * Set up the max longitude in a bounded box.
     *
     * @param maxLongitude float
     */
    public void setMaxLongitude(Float maxLongitude)
    {
        this.maxLongitude = maxLongitude;
    }


    /**
     * Return the maximum latitude in a bounded box.
     *
     * @return float
     */
    public Float getMaxLatitude()
    {
        return maxLatitude;
    }


    /**
     * Set up the maximum latitude in a bounded box.
     *
     * @param maxLatitude float
     */
    public void setMaxLatitude(Float maxLatitude)
    {
        this.maxLatitude = maxLatitude;
    }


    /**
     * Return the minimum height that data is collected from.
     *
     * @return float
     */
    public Float getMinHeight()
    {
        return minHeight;
    }


    /**
     * Set up the minimum height that data is collected from.
     *
     * @param minHeight float
     */
    public void setMinHeight(Float minHeight)
    {
        this.minHeight = minHeight;
    }


    /**
     * Return the maximum height that data is collected from.
     *
     * @return float
     */
    public Float getMaxHeight()
    {
        return maxHeight;
    }


    /**
     * Set up the maximum height that data is collected from.
     *
     * @param maxHeight float
     */
    public void setMaxHeight(Float maxHeight)
    {
        this.maxHeight = maxHeight;
    }


    /**
     * Return the start time that data was collected for this asset.
     *
     * @return date
     */
    public Date getDataCollectionStartTime()
    {
        return dataCollectionStartTime;
    }


    /**
     * Set up the start time that data was collected for this asset.
     *
     * @param dataCollectionStartTime date
     */
    public void setDataCollectionStartTime(Date dataCollectionStartTime)
    {
        this.dataCollectionStartTime = dataCollectionStartTime;
    }


    /**
     * Return the end time that data was collected for this asset.
     *
     * @return date
     */
    public Date getDataCollectionEndTime()
    {
        return dataCollectionEndTime;
    }


    /**
     * Set up the end time that data was collected for this asset..
     *
     * @param dataCollectionEndTime date
     */
    public void setDataCollectionEndTime(Date dataCollectionEndTime)
    {
        this.dataCollectionEndTime = dataCollectionEndTime;
    }



    /**
     * Return the start time that the data is valid.
     *
     * @return date
     */
    public Date getDataValidityStartTime()
    {
        return dataValidityStartTime;
    }


    /**
     * Set up the start time that the data is valid.
     *
     * @param dataValidityStartTime date
     */
    public void setDataValidityStartTime(Date dataValidityStartTime)
    {
        this.dataValidityStartTime = dataValidityStartTime;
    }


    /**
     * Return the end time that the data is valid.
     *
     * @return date
     */
    public Date getDataValidityEndTime()
    {
        return dataValidityEndTime;
    }


    /**
     * Set up the end time that the data is valid.
     *
     * @param dataValidityEndTime date
     */
    public void setDataValidityEndTime(Date dataValidityEndTime)
    {
        this.dataValidityEndTime = dataValidityEndTime;
    }


    /**
     * Return the start time of the period that the data covers.
     *
     * @return date
     */
    public Date getDataCoverageStartTime()
    {
        return dataCoverageStartTime;
    }


    /**
     * Set up the start time of the period that the data covers.
     *
     * @param dataCoverageStartTime date
     */
    public void setDataCoverageStartTime(Date dataCoverageStartTime)
    {
        this.dataCoverageStartTime = dataCoverageStartTime;
    }


    /**
     * Return the end time of the period that the data covers.
     *
     * @return date
     */
    public Date getDataCoverageEndTime()
    {
        return dataCoverageEndTime;
    }


    /**
     * Set up the end time of the period that the data covers.
     *
     * @param dataCoverageEndTime date
     */
    public void setDataCoverageEndTime(Date dataCoverageEndTime)
    {
        this.dataCoverageEndTime = dataCoverageEndTime;
    }


    /**
     * Return the map of scope elements that defines the scope of the data.
     *
     * @return map
     */
    public Map<String, String> getScopeElements()
    {
        return scopeElements;
    }


    /**
     * Set up the map of scope elements that defines the scope of the data.
     *
     * @param scopeElements map
     */
    public void setScopeElements(Map<String, String> scopeElements)
    {
        this.scopeElements = scopeElements;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataLensProperties{" +
                "minLongitude=" + minLongitude +
                ", minLatitude=" + minLatitude +
                ", maxLongitude=" + maxLongitude +
                ", maxLatitude=" + maxLatitude +
                ", minHeight=" + minHeight +
                ", maxHeight=" + maxHeight +
                ", dataCollectionStartTime=" + dataCollectionStartTime +
                ", dataCollectionEndTime=" + dataCollectionEndTime +
                ", dataValidityStartTime=" + dataValidityStartTime +
                ", dataValidityEndTime=" + dataValidityEndTime +
                ", dataCoverageStartTime=" + dataCoverageStartTime +
                ", dataCoverageEndTime=" + dataCoverageEndTime +
                ", scopeElements=" + scopeElements +
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
        DataLensProperties that = (DataLensProperties) objectToCompare;
        return Objects.equals(minLongitude, that.minLongitude) &&
                Objects.equals(minLatitude, that.minLatitude) &&
                Objects.equals(maxLongitude, that.maxLongitude) &&
                Objects.equals(maxLatitude, that.maxLatitude) &&
                Objects.equals(minHeight, that.minHeight) &&
                Objects.equals(maxHeight, that.maxHeight) &&
                Objects.equals(dataCollectionStartTime, that.dataCollectionStartTime) &&
                Objects.equals(dataCollectionEndTime, that.dataCollectionEndTime) &&
                Objects.equals(dataValidityStartTime, that.dataValidityStartTime) &&
                Objects.equals(dataValidityEndTime, that.dataValidityEndTime) &&
                Objects.equals(dataCoverageStartTime, that.dataCoverageStartTime) &&
                Objects.equals(dataCoverageEndTime, that.dataCoverageEndTime) &&
                Objects.equals(scopeElements, that.scopeElements);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), minLongitude, minLatitude, maxLongitude, maxLatitude, minHeight,
                            maxHeight, dataCollectionStartTime, dataCollectionEndTime, dataValidityStartTime,
                            dataValidityEndTime, dataCoverageStartTime, dataCoverageEndTime, scopeElements);
    }
}
