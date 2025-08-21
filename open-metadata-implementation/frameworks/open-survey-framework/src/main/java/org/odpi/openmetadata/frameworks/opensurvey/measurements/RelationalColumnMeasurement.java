/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opensurvey.measurements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationalColumnMeasurement
{
    private String  qualifiedColumnName       = null;
    private String  columnName                = null;
    private long    columnSize                = 0L;
    private String  columnDataType            = null;
    private boolean columnNotNull             = true;
    private int     averageColumnWidth        = 0;
    private long    numberOfDistinctValues    = 0L;
    private String  mostCommonValues          = null;
    private String  mostCommonValuesFrequency = null;


    public RelationalColumnMeasurement()
    {
    }

    public String getQualifiedColumnName()
    {
        return qualifiedColumnName;
    }

    public void setQualifiedColumnName(String qualifiedColumnName)
    {
        this.qualifiedColumnName = qualifiedColumnName;
    }

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public long getColumnSize()
    {
        return columnSize;
    }

    public void setColumnSize(long columnSize)
    {
        this.columnSize = columnSize;
    }

    public String getColumnDataType()
    {
        return columnDataType;
    }

    public void setColumnDataType(String columnDataType)
    {
        this.columnDataType = columnDataType;
    }

    public boolean getColumnNotNull()
    {
        return columnNotNull;
    }

    public void setColumnNotNull(boolean columnNotNull)
    {
        this.columnNotNull = columnNotNull;
    }

    public int getAverageColumnWidth()
    {
        return averageColumnWidth;
    }

    public void setAverageColumnWidth(int averageColumnWidth)
    {
        this.averageColumnWidth = averageColumnWidth;
    }

    public long getNumberOfDistinctValues()
    {
        return numberOfDistinctValues;
    }

    public void setNumberOfDistinctValues(long numberOfDistinctValues)
    {
        this.numberOfDistinctValues = numberOfDistinctValues;
    }

    public String getMostCommonValues()
    {
        return mostCommonValues;
    }

    public void setMostCommonValues(String mostCommonValues)
    {
        this.mostCommonValues = mostCommonValues;
    }

    public String getMostCommonValuesFrequency()
    {
        return mostCommonValuesFrequency;
    }

    public void setMostCommonValuesFrequency(String mostCommonValuesFrequency)
    {
        this.mostCommonValuesFrequency = mostCommonValuesFrequency;
    }

    @Override
    public String toString()
    {
        return "RelationalColumnMeasurement{" +
                "qualifiedColumnName='" + qualifiedColumnName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", columnSize=" + columnSize +
                ", columnDataType='" + columnDataType + '\'' +
                ", averageColumnWidth=" + averageColumnWidth +
                ", numberOfDistinctValues=" + numberOfDistinctValues +
                ", mostCommonValues='" + mostCommonValues + '\'' +
                ", mostCommonValuesFrequency='" + mostCommonValuesFrequency + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        RelationalColumnMeasurement that = (RelationalColumnMeasurement) objectToCompare;
        return columnSize == that.columnSize && averageColumnWidth == that.averageColumnWidth && numberOfDistinctValues == that.numberOfDistinctValues && Objects.equals(qualifiedColumnName, that.qualifiedColumnName) && Objects.equals(columnName, that.columnName) && Objects.equals(columnDataType, that.columnDataType) && Objects.equals(mostCommonValues, that.mostCommonValues) && Objects.equals(mostCommonValuesFrequency, that.mostCommonValuesFrequency);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(qualifiedColumnName, columnName, columnSize, columnDataType, averageColumnWidth, numberOfDistinctValues, mostCommonValues, mostCommonValuesFrequency);
    }
}
