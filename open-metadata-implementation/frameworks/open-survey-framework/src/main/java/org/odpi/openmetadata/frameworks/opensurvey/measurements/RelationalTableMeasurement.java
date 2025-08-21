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
public class RelationalTableMeasurement
{
    private String  tableName            = null;
    private String  qualifiedTableName   = null;
    private long    tableSize            = 0L;
    private String  tableType            = "Table";
    private long    columnCount          = 0L;
    private long    numberOfRowsInserted = 0;
    private long    numberOfRowsUpdated  = 0;
    private long    numberOfRowsDeleted  = 0;
    private String  tableOwner           = null;
    private boolean isPopulated          = true;
    private boolean hasIndexes           = false;
    private boolean hasRules             = false;
    private boolean hasTriggers          = false;
    private boolean hasRowSecurity       = false;
    private String  queryDefinition      = null;


    public RelationalTableMeasurement()
    {
    }


    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getQualifiedTableName()
    {
        return qualifiedTableName;
    }

    public void setQualifiedTableName(String qualifiedTableName)
    {
        this.qualifiedTableName = qualifiedTableName;
    }

    public long getTableSize()
    {
        return tableSize;
    }

    public void setTableSize(long tableSize)
    {
        this.tableSize = tableSize;
    }

    public String getTableType()
    {
        return tableType;
    }

    public void setTableType(String tableType)
    {
        this.tableType = tableType;
    }

    public long getColumnCount()
    {
        return columnCount;
    }

    public void setColumnCount(long columnCount)
    {
        this.columnCount = columnCount;
    }

    public long getNumberOfRowsInserted()
    {
        return numberOfRowsInserted;
    }

    public void setNumberOfRowsInserted(long numberOfRowsInserted)
    {
        this.numberOfRowsInserted = numberOfRowsInserted;
    }

    public long getNumberOfRowsUpdated()
    {
        return numberOfRowsUpdated;
    }

    public void setNumberOfRowsUpdated(long numberOfRowsUpdated)
    {
        this.numberOfRowsUpdated = numberOfRowsUpdated;
    }

    public long getNumberOfRowsDeleted()
    {
        return numberOfRowsDeleted;
    }

    public void setNumberOfRowsDeleted(long numberOfRowsDeleted)
    {
        this.numberOfRowsDeleted = numberOfRowsDeleted;
    }

    public String getTableOwner()
    {
        return tableOwner;
    }

    public void setTableOwner(String tableOwner)
    {
        this.tableOwner = tableOwner;
    }

    /**
     * Return whether the table is populated (typically true).
     *
     * @return boolean
     */
    public boolean getIsPopulated()
    {
        return isPopulated;
    }


    /**
     * Set up whether the table is populated (typically true).
     *
     * @param populated boolean
     */
    public void setIsPopulated(boolean populated)
    {
        isPopulated = populated;
    }

    public boolean getHasIndexes()
    {
        return hasIndexes;
    }

    public void setHasIndexes(boolean hasIndexes)
    {
        this.hasIndexes = hasIndexes;
    }

    public boolean getHasRules()
    {
        return hasRules;
    }

    public void setHasRules(boolean hasRules)
    {
        this.hasRules = hasRules;
    }

    public boolean getHasTriggers()
    {
        return hasTriggers;
    }

    public void setHasTriggers(boolean hasTriggers)
    {
        this.hasTriggers = hasTriggers;
    }

    public boolean getHasRowSecurity()
    {
        return hasRowSecurity;
    }

    public void setHasRowSecurity(boolean hasRowSecurity)
    {
        this.hasRowSecurity = hasRowSecurity;
    }


    public String getQueryDefinition()
    {
        return queryDefinition;
    }

    public void setQueryDefinition(String queryDefinition)
    {
        this.queryDefinition = queryDefinition;
    }

    @Override
    public String toString()
    {
        return "RelationalTableMeasurement{" +
                "tableName='" + tableName + '\'' +
                ", qualifiedTableName='" + qualifiedTableName + '\'' +
                ", tableSize=" + tableSize +
                ", tableType='" + tableType + '\'' +
                ", columnCount=" + columnCount +
                ", numberOfRowsInserted=" + numberOfRowsInserted +
                ", numberOfRowsUpdated=" + numberOfRowsUpdated +
                ", numberOfRowsDeleted=" + numberOfRowsDeleted +
                ", tableOwner='" + tableOwner + '\'' +
                ", hasIndexes=" + hasIndexes +
                ", hasRules=" + hasRules +
                ", hasTriggers=" + hasTriggers +
                ", hasRowSecurity=" + hasRowSecurity +
                ", viewDefinition='" + queryDefinition + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        RelationalTableMeasurement that = (RelationalTableMeasurement) objectToCompare;
        return tableSize == that.tableSize && columnCount == that.columnCount && numberOfRowsInserted == that.numberOfRowsInserted && numberOfRowsUpdated == that.numberOfRowsUpdated && numberOfRowsDeleted == that.numberOfRowsDeleted && hasIndexes == that.hasIndexes && hasRules == that.hasRules && hasTriggers == that.hasTriggers && hasRowSecurity == that.hasRowSecurity && Objects.equals(tableName, that.tableName) && Objects.equals(qualifiedTableName, that.qualifiedTableName) && Objects.equals(tableType, that.tableType) && Objects.equals(tableOwner, that.tableOwner) && Objects.equals(queryDefinition, that.queryDefinition);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(tableName, qualifiedTableName, tableSize, tableType, columnCount, numberOfRowsInserted, numberOfRowsUpdated, numberOfRowsDeleted, tableOwner, hasIndexes, hasRules, hasTriggers, hasRowSecurity, queryDefinition);
    }
}
