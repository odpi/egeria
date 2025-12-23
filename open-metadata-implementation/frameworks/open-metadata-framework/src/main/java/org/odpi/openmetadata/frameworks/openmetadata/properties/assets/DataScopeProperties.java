/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataAssetEncodingProperties provides the JavaBean for describing a data asset's encoding.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class DataScopeProperties extends ClassificationBeanProperties
{
    private float               minLongitude            = 0F;
    private float               minLatitude             = 0F;
    private float               maxLongitude            = 0F;
    private float               maxLatitude             = 0F;
    private float               minHeight               = 0F;
    private float               maxHeight               = 0F;
    private Date                dataCollectionStartTime = null;
    private Date                dataCollectionEndTime   = null;
    private Map<String, String> additionalProperties    = null;


    /**
     * Default constructor
     */
    public DataScopeProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_SCOPE_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataScopeProperties(DataScopeProperties template)
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
            additionalProperties    = template.getAdditionalProperties();
        }
    }


    /**
     * Return the minimum longitude in a bounded box.
     *
     * @return float
     */
    public float getMinLongitude()
    {
        return minLongitude;
    }


    /**
     * Set up the minimum longitude in a bounded box.
     *
     * @param minLongitude float
     */
    public void setMinLongitude(float minLongitude)
    {
        this.minLongitude = minLongitude;
    }


    /**
     * Return the minimum latitude in a bounded box.
     *
     * @return float
     */
    public float getMinLatitude()
    {
        return minLatitude;
    }


    /**
     * Set up the minimum latitude in a bounded box.
     *
     * @param minLatitude float
     */
    public void setMinLatitude(float minLatitude)
    {
        this.minLatitude = minLatitude;
    }


    /**
     * Return the max longitude in a bounded box.
     *
     * @return float
     */
    public float getMaxLongitude()
    {
        return maxLongitude;
    }


    /**
     * Set up the max longitude in a bounded box.
     *
     * @param maxLongitude float
     */
    public void setMaxLongitude(float maxLongitude)
    {
        this.maxLongitude = maxLongitude;
    }


    /**
     * Return the maximum latitude in a bounded box.
     *
     * @return float
     */
    public float getMaxLatitude()
    {
        return maxLatitude;
    }


    /**
     * Set up the maximum latitude in a bounded box.
     *
     * @param maxLatitude float
     */
    public void setMaxLatitude(float maxLatitude)
    {
        this.maxLatitude = maxLatitude;
    }


    /**
     * Return the minimum height that data is collected from.
     *
     * @return float
     */
    public float getMinHeight()
    {
        return minHeight;
    }


    /**
     * Set up the minimum height that data is collected from.
     *
     * @param minHeight float
     */
    public void setMinHeight(float minHeight)
    {
        this.minHeight = minHeight;
    }


    /**
     * Return the maximum height that data is collected from.
     *
     * @return float
     */
    public float getMaxHeight()
    {
        return maxHeight;
    }


    /**
     * Set up the maximum height that data is collected from.
     *
     * @param maxHeight float
     */
    public void setMaxHeight(float maxHeight)
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
     * Return the additional properties associated with the encoding process.
     *
     * @return map of name-value pairs
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up the additional properties associated with the encoding process.
     *
     * @param additionalProperties map of name-value pairs
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataScopeProperties{" +
                "minLongitude=" + minLongitude +
                ", minLatitude=" + minLatitude +
                ", maxLongitude=" + maxLongitude +
                ", maxLatitude=" + maxLatitude +
                ", minHeight=" + minHeight +
                ", maxHeight=" + maxHeight +
                ", dataCollectionStartTime=" + dataCollectionStartTime +
                ", dataCollectionEndTime=" + dataCollectionEndTime +
                ", additionalProperties=" + additionalProperties +
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
        DataScopeProperties that = (DataScopeProperties) objectToCompare;
        return Float.compare(minLongitude, that.minLongitude) == 0 &&
                Float.compare(minLatitude, that.minLatitude) == 0 &&
                Float.compare(maxLongitude, that.maxLongitude) == 0 &&
                Float.compare(maxLatitude, that.maxLatitude) == 0 &&
                Float.compare(minHeight, that.minHeight) == 0 &&
                Float.compare(maxHeight, that.maxHeight) == 0 &&
                Objects.equals(dataCollectionStartTime, that.dataCollectionStartTime) &&
                Objects.equals(dataCollectionEndTime, that.dataCollectionEndTime) &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), minLongitude, minLatitude, maxLongitude, maxLatitude, minHeight, maxHeight, dataCollectionStartTime, dataCollectionEndTime, additionalProperties);
    }
}
