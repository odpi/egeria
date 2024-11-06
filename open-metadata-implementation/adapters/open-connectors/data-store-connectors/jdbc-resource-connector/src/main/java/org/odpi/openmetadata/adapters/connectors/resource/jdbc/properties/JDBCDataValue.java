/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties;

import java.util.Objects;

/**
 * JDBCDataValue is used when inserting new rows into a table.
 */
public class JDBCDataValue
{
    private Object dataValue     = null;
    private int    targetSQLType = 0;
    private int    scaleOrLength = 0;


    /**
     * Typical constructor does not need the scaleOrLength.
     *
     * @param dataValue data value formatted into an appropriate SQL type
     * @param targetSQLType type of the data value from java.sql.Types
     */
    public JDBCDataValue(Object dataValue, int targetSQLType)
    {
        this.dataValue = dataValue;
        this.targetSQLType = targetSQLType;
    }


    /**
     * Constructor for numbers and streams.
     *
     * @param dataValue data value formatted into an appropriate SQL type
     * @param targetSQLType type of the data value from java.sql.Types
     * @param scaleOrLength for java.sql.Types.DECIMAL or java.sql.Types.NUMERIC types, this is the number of digits after the decimal point.
     * For Java Object types InputStream and Reader, this is the length of the data in the stream or reader.
     * For all other types, this value will be ignored.
     */
    public JDBCDataValue(Object dataValue, int targetSQLType, int scaleOrLength)
    {
        this.dataValue = dataValue;
        this.targetSQLType = targetSQLType;
        this.scaleOrLength = scaleOrLength;
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template object ot copy
     */
    public JDBCDataValue(JDBCDataValue template)
    {
        if (template != null)
        {
            this.dataValue     = template.getDataValue();
            this.targetSQLType = template.getTargetSQLType();
            this.scaleOrLength = template.getScaleOrLength();
        }
    }


    /**
     * The value formatted into the appropriate SQL type.
     *
     * @return object
     */
    public Object getDataValue()
    {
        return dataValue;
    }


    /**
     * The type of the data value using the values from java.sql.Types.
     *
     * @return int
     */
    public int getTargetSQLType()
    {
        return targetSQLType;
    }


    /**
     * Return the scaleOrLength. For java.sql.Types.DECIMAL or java.sql.Types.NUMERIC types, this is the number of digits after the decimal point.
     * For Java Object types InputStream and Reader, this is the length of the data in the stream or reader.
     * For all other types, this value will be ignored.
     *
     * @return int
     */
    public int getScaleOrLength()
    {
        return scaleOrLength;
    }


    @Override
    public String toString()
    {
        return "JDBCDataValue{" +
                       "dataValue=" + dataValue +
                       ", targetSQLType=" + targetSQLType +
                       ", scaleOrLength=" + scaleOrLength +
                       '}';
    }


    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof JDBCDataValue dataValue1))
        {
            return false;
        }
        return targetSQLType == dataValue1.targetSQLType &&
                       scaleOrLength == dataValue1.scaleOrLength &&
                       Objects.equals(dataValue, dataValue1.dataValue);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(dataValue, targetSQLType, scaleOrLength);
    }
}
