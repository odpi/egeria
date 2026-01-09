/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.tabulardatasets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Information about a tabular data set.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TabularDataSetReport
{
    private long                           recordCount        = 0L;
    private String                         tableName          = null;
    private String                         tableDescription   = null;
    private List<TabularColumnDescription> columnDescriptions = null;
    private Map<String,List<String>>       dataRecords        = null;


    public TabularDataSetReport()
    {
    }


    /**
     * Return the record count in the data source.
     *
     * @return count
     */
    public long getRecordCount()
    {
        return recordCount;
    }


    /**
     * Set up the number of records in the data source.
     *
     * @param recordCount count
     */
    public void setRecordCount(long recordCount)
    {
        this.recordCount = recordCount;
    }


    /**
     * Return the table name for this data source.  This is in canonical word format where each word in the name
     * should be capitalized, with spaces between the words.
     * This format allows easy translation between different naming conventions.
     *
     * @return string
     */
    public String getTableName()
    {
        return tableName;
    }


    /**
     * Set up the table name for this data source.
     *
     * @param tableName string
     */
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }


    /**
     * Return the description for this data source.
     *
     * @return string
     */
    public String getTableDescription()
    {
        return tableDescription;
    }


    /**
     * Set the description for the table in the tabular data set report.
     *
     * @param tableDescription the description of the table
     */
    public void setTableDescription(String tableDescription)
    {
        this.tableDescription = tableDescription;
    }


    /**
     * Return the list of column descriptions associated with this data source.  The information
     * should be sufficient to define the schema in a target data store.  The names of the columns should be in
     * a canonical name format where each word in the name is capitalized with a space between each word.
     * This allows simple translation between the naming conventions supported by different technologies.
     *
     * @return a list of column descriptions or null if not available.
     */
    public List<TabularColumnDescription> getColumnDescriptions()
    {
        return columnDescriptions;
    }


    /**
     * Set up the column descriptions.
     *
     * @param columnDescriptions list
     */
    public void setColumnDescriptions(List<TabularColumnDescription> columnDescriptions)
    {
        this.columnDescriptions = columnDescriptions;
    }


    /**
     * Return the requested data records.
     *
     * @return map of row number to data columns.
     */
    public Map<String, List<String>> getDataRecords()
    {
        return dataRecords;
    }

    public void setDataRecords(Map<String, List<String>> dataRecords)
    {
        this.dataRecords = dataRecords;
    }

    /**
     * Standard toString method. Note SecuredProperties and other credential type properties are not displayed.
     * This is deliberate because there is no knowing where the string will be printed.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TabularDataSetReport{" +
                "recordCount=" + recordCount +
                ", tableName='" + tableName + '\'' +
                ", tableDescription='" + tableDescription + '\'' +
                ", columnDescriptions=" + columnDescriptions +
                ", dataRecords=" + dataRecords +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        TabularDataSetReport that = (TabularDataSetReport) objectToCompare;
        return recordCount == that.recordCount && Objects.equals(tableName, that.tableName) && Objects.equals(tableDescription, that.tableDescription) && Objects.equals(columnDescriptions, that.columnDescriptions) && Objects.equals(dataRecords, that.dataRecords);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(recordCount, tableName, tableDescription, columnDescriptions, dataRecords);
    }
}
