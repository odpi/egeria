/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.mappers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ffdc.JDBCErrorCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.ColumnType;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Common routines used by the mappers
 */
public class BaseMapper
{
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();
    private static final ObjectReader OBJECT_READER = new ObjectMapper().reader();

    private final String connectorName;

    /**
     * Constructor ensures there is a connector name for error messages.
     *
     * @param connectorName connector name
     */
    public BaseMapper(String connectorName)
    {
        this.connectorName = connectorName;
    }


    /**
     * Create a class to wrap the map to simplify the use of Jackson
     */
    static class SerializableMap
    {
        private Map<String, Serializable>  mappingProperties = null;

        public Map<String, Serializable> getMappingProperties()
        {
            return mappingProperties;
        }

        public void setMappingProperties(Map<String, Serializable> mappingProperties)
        {
            this.mappingProperties = mappingProperties;
        }
    }


    /**
     * Retrieve a string value from a specific column.
     *
     * @param columnName name of column to interrogate
     * @param instanceTableRow row from the database
     * @param isRequired is this value required
     * @return string value
     * @throws RepositoryErrorException missing value
     */
    public String getStringPropertyFromColumn(String                     columnName,
                                              Map<String, JDBCDataValue> instanceTableRow,
                                              boolean                    isRequired) throws RepositoryErrorException
    {
        final String methodName = "getStringPropertyFromColumn";

        if (instanceTableRow == null)
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        JDBCDataValue jdbcDataValue = instanceTableRow.get(columnName);
        String        newValue = null;

        if ((jdbcDataValue != null) && (jdbcDataValue.getDataValue() != null))
        {
            newValue = jdbcDataValue.getDataValue().toString();
        }

        if ((isRequired) && (newValue == null))
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        return newValue;
    }


    /**
     * Retrieve a string value from a specific column.
     *
     * @param columnName name of column to interrogate
     * @param instanceTableRow row from the database
     * @param isRequired is this value required
     * @return string value
     * @throws RepositoryErrorException missing value
     */
    public List<String> getStringArrayPropertyFromColumn(String                     columnName,
                                                         Map<String, JDBCDataValue> instanceTableRow,
                                                         boolean                    isRequired) throws RepositoryErrorException
    {
        final String methodName = "getStringArrayPropertyFromColumn";

        if (instanceTableRow == null)
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        JDBCDataValue jdbcDataValue = instanceTableRow.get(columnName);
        String        newValue = null;

        if ((jdbcDataValue != null) && (jdbcDataValue.getDataValue() != null))
        {
            newValue = jdbcDataValue.getDataValue().toString();
        }

        if ((isRequired) && (newValue == null))
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        if (newValue == null)
        {
            return null;
        }

        return Arrays.asList(newValue.split(","));
    }


    /**
     * Retrieve a string value from a specific column.
     *
     * @param columnName name of column to interrogate
     * @param instanceTableRow row from the database
     * @param isRequired is this value required
     * @return string value
     * @throws RepositoryErrorException missing value
     */
    public Map<String, Serializable> getSerializableMapPropertyFromColumn(String                     columnName,
                                                                          Map<String, JDBCDataValue> instanceTableRow,
                                                                          boolean                    isRequired) throws RepositoryErrorException
    {
        final String methodName = "getSerializableMapPropertyFromColumn";

        if (instanceTableRow == null)
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        JDBCDataValue jdbcDataValue = instanceTableRow.get(columnName);
        String        newValue = null;

        if ((jdbcDataValue != null) && (jdbcDataValue.getDataValue() != null))
        {
            newValue = jdbcDataValue.getDataValue().toString();
        }

        if ((isRequired) && (newValue == null))
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        if (newValue != null)
        {
            try
            {
                SerializableMap serializableMap = OBJECT_READER.readValue(newValue, SerializableMap.class);

                if (serializableMap != null)
                {
                    return serializableMap.getMappingProperties();
                }
            }
            catch (Exception error)
            {
                throw new RepositoryErrorException(JDBCErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                           error.getClass().getName(),
                                                                                                           methodName,
                                                                                                           error.getMessage()),
                                                   error.getClass().getName(),
                                                   methodName,
                                                   error);
            }
        }

        return null;
    }


    /**
     * Retrieve a date value from a specific column.
     *
     * @param columnName name of column to interrogate
     * @param instanceTableRow row from the database
     * @param isRequired is this value required
     * @return string value
     * @throws RepositoryErrorException missing value
     */
    public Date getDatePropertyFromColumn(String                     columnName,
                                          Map<String, JDBCDataValue> instanceTableRow,
                                          boolean                    isRequired) throws RepositoryErrorException
    {
        final String methodName = "getDatePropertyFromColumn";

        if (instanceTableRow == null)
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        JDBCDataValue jdbcDataValue = instanceTableRow.get(columnName);
        Date          newValue = null;

        if ((jdbcDataValue != null) && (jdbcDataValue.getDataValue() instanceof Long longValue))
        {
            newValue = new Date(longValue);
        }

        if ((jdbcDataValue != null) && (jdbcDataValue.getDataValue() instanceof Date dateValue))
        {
            newValue = dateValue;
        }

        if ((isRequired) && (newValue == null))
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        return newValue;
    }


    /**
     * Retrieve a long value from a specific column.
     *
     * @param columnName name of column to interrogate
     * @param instanceTableRow row from the database
     * @param isRequired is this value required
     * @return string value
     * @throws RepositoryErrorException missing value
     */
    public long getLongPropertyFromColumn(String                     columnName,
                                          Map<String, JDBCDataValue> instanceTableRow,
                                          boolean                    isRequired) throws RepositoryErrorException
    {
        final String methodName = "getLongPropertyFromColumn";

        if (instanceTableRow == null)
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        JDBCDataValue jdbcDataValue = instanceTableRow.get(columnName);

        if ((jdbcDataValue != null) && (jdbcDataValue.getDataValue() instanceof Long longValue))
        {
            return longValue;
        }

        if ((jdbcDataValue != null) && (jdbcDataValue.getDataValue() instanceof BigDecimal bigDecimalValue))
        {
            return bigDecimalValue.longValue();
        }

        if (isRequired)
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        return 0L;
    }


    /**
     * Retrieve a date value from a specific column.
     *
     * @param columnName name of column to interrogate
     * @param instanceTableRow row from the database
     * @param isRequired is this value required
     * @return string value
     * @throws RepositoryErrorException missing value
     */
    public boolean getBooleanPropertyFromColumn(String                     columnName,
                                                Map<String, JDBCDataValue> instanceTableRow,
                                                boolean                    isRequired) throws RepositoryErrorException
    {
        final String methodName = "getBooleanPropertyFromColumn";

        if (instanceTableRow == null)
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        JDBCDataValue jdbcDataValue = instanceTableRow.get(columnName);

        if ((jdbcDataValue != null) && (jdbcDataValue.getDataValue() instanceof Boolean booleanValue))
        {
            return booleanValue;
        }

        if (isRequired)
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        return false;
    }


    /**
     * Ensure any single quote in a property value is escaped.
     *
     * @param propertyValue supplied property value
     * @return escaped property value
     */
    private String escapePropertyValue(Object propertyValue)
    {
        if (propertyValue != null)
        {
            return propertyValue.toString().replaceAll("'", "''");
        }

        return null;
    }


    /**
     * Set up the string value in the supplied instance table row.
     *
     * @param instanceTableRow table row
     * @param propertyValue value to set up
     * @param columnName column to place the value in
     * @param isRequired is this value allowed to be null
     * @throws RepositoryErrorException missing property value
     */
    protected void setUpStringValueInRow(Map<String, JDBCDataValue> instanceTableRow,
                                         String                     propertyValue,
                                         String                     columnName,
                                         boolean                    isRequired) throws RepositoryErrorException
    {
        final String methodName = "setUpStringValueInRow";

        if ((propertyValue == null) && (isRequired))
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        if (propertyValue != null)
        {
            instanceTableRow.put(columnName, new JDBCDataValue(escapePropertyValue(propertyValue), ColumnType.STRING.getJdbcType()));
        }
    }


    /**
     * Set up the list of string values as a comma-separated list in a single string column in the supplied instance table row.
     *
     * @param instanceTableRow table row
     * @param propertyValues value to set up
     * @param columnName column to place the value in
     * @param isRequired is this value allowed to be null
     * @throws RepositoryErrorException missing property value
     */
    protected void setUpStringArrayValueInRow(Map<String, JDBCDataValue> instanceTableRow,
                                              List<String>               propertyValues,
                                              String                     columnName,
                                              boolean                    isRequired) throws RepositoryErrorException
    {
        final String methodName = "setUpStringArrayValueInRow";

        if ((propertyValues == null) && (isRequired))
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        StringBuilder propertyValueAsString = new StringBuilder();
        boolean       firstProperty         = true;

        if (propertyValues != null)
        {
            for (String propertyValue : propertyValues)
            {
                if (propertyValue != null)
                {
                    if (firstProperty)
                    {
                        firstProperty = false;
                    }
                    else
                    {
                        propertyValueAsString.append(",");
                    }

                    propertyValueAsString.append(escapePropertyValue(propertyValue));
                }
            }

            instanceTableRow.put(columnName, new JDBCDataValue(propertyValueAsString.toString(), ColumnType.STRING.getJdbcType()));
        }
    }


    /**
     * Set up the list of string values as a comma-separated list in a single string column in the supplied instance table row.
     *
     * @param instanceTableRow table row
     * @param propertyValues value to set up
     * @param columnName column to place the value in
     * @param isRequired is this value allowed to be null
     * @throws RepositoryErrorException missing property value
     */
    protected void setUpSerializableMapValueInRow(Map<String, JDBCDataValue> instanceTableRow,
                                                  Map<String, Serializable>  propertyValues,
                                                  String                     columnName,
                                                  boolean                    isRequired) throws RepositoryErrorException
    {
        final String methodName = "setUpStringArrayValueInRow";

        if ((propertyValues == null) && (isRequired))
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        if (propertyValues != null)
        {
            try
            {
                SerializableMap serializableMap = new SerializableMap();

                serializableMap.setMappingProperties(propertyValues);

                instanceTableRow.put(columnName, new JDBCDataValue(OBJECT_WRITER.writeValueAsString(serializableMap),
                                                                   ColumnType.STRING.getJdbcType()));
            }
            catch (Exception error)
            {
                throw new RepositoryErrorException(JDBCErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                           error.getClass().getName(),
                                                                                                           methodName,
                                                                                                           error.getMessage()),
                                                   error.getClass().getName(),
                                                   methodName,
                                                   error);
            }
        }
    }


    /**
     * Set up the string value in the supplied instance table row.
     *
     * @param instanceTableRow table row
     * @param propertyValue value to set up
     * @param columnName column to place the value in
     * @param isRequired is this value allowed to be null
     * @throws RepositoryErrorException missing property value
     */
    protected void setUpDateValueInRow(Map<String, JDBCDataValue> instanceTableRow,
                                       Date                       propertyValue,
                                       String                     columnName,
                                       boolean                    isRequired) throws RepositoryErrorException
    {
        final String methodName = "setUpDateValueInRow";

        if ((propertyValue == null) && (isRequired))
        {
            throw new RepositoryErrorException(JDBCErrorCode.MISSING_DATABASE_VALUE.getMessageDefinition(columnName,
                                                                                                         methodName,
                                                                                                         this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        if (propertyValue != null)
        {
            instanceTableRow.put(columnName, new JDBCDataValue(propertyValue, ColumnType.DATE.getJdbcType()));
        }
    }


    /**
     * Set up the string value in the supplied instance table row.
     *
     * @param instanceTableRow table row
     * @param propertyValue value to set up
     * @param columnName column to place the value in
     */
    protected void setUpBooleanValueInRow(Map<String, JDBCDataValue> instanceTableRow,
                                          boolean                    propertyValue,
                                          String                     columnName)
    {
        instanceTableRow.put(columnName, new JDBCDataValue(propertyValue, ColumnType.BOOLEAN.getJdbcType()));
    }


    /**
     * Set up the string value in the supplied instance table row.
     *
     * @param instanceTableRow table row
     * @param propertyValue value to set up
     * @param columnName column to place the value in
     */
    protected void setUpLongValueInRow(Map<String, JDBCDataValue> instanceTableRow,
                                       long                       propertyValue,
                                       String                     columnName)
    {
        instanceTableRow.put(columnName, new JDBCDataValue(propertyValue, ColumnType.LONG.getJdbcType()));
    }
}
