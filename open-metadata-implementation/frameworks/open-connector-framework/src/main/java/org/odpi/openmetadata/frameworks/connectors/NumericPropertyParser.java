/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFRuntimeException;

import java.math.BigDecimal;

/**
 * Turns a configuration property or request parameter into a whole number.  Configuration properties arrive from
 * JSON, so a value set as 30 may reach the connector as an Integer, a Long, a Double (30.0) or a String, and request
 * parameters are always Strings that may carry surrounding spaces.  Parsing the value's string form with
 * Integer.parseInt rejected 30.0 and " 30" with a bare NumberFormatException that named neither the property nor
 * its value.  Any representation of a whole number in range is now accepted, and anything else is reported with
 * the property's name.
 */
public class NumericPropertyParser
{
    private static final String PROPERTY_KIND_CONFIGURATION = "configuration";
    private static final String PROPERTY_KIND_REQUEST       = "request parameter";


    /**
     * Return the value of a configuration property as an int.
     *
     * @param propertyName name of the configuration property
     * @param propertyValue value of the configuration property (not null)
     * @return int value
     * @throws OMFRuntimeException the value is not a whole number in the range of an int
     */
    public static int getIntConfigurationProperty(String propertyName,
                                                  Object propertyValue)
    {
        return getInt(PROPERTY_KIND_CONFIGURATION, propertyName, propertyValue);
    }


    /**
     * Return the value of a configuration property as a long.
     *
     * @param propertyName name of the configuration property
     * @param propertyValue value of the configuration property (not null)
     * @return long value
     * @throws OMFRuntimeException the value is not a whole number in the range of a long
     */
    public static long getLongConfigurationProperty(String propertyName,
                                                    Object propertyValue)
    {
        return getLong(PROPERTY_KIND_CONFIGURATION, propertyName, propertyValue);
    }


    /**
     * Return the value of a request parameter as an int.
     *
     * @param parameterName name of the request parameter
     * @param parameterValue value of the request parameter (not null)
     * @return int value
     * @throws OMFRuntimeException the value is not a whole number in the range of an int
     */
    public static int getIntRequestParameter(String parameterName,
                                             String parameterValue)
    {
        return getInt(PROPERTY_KIND_REQUEST, parameterName, parameterValue);
    }


    /**
     * Return the value of a request parameter as a long.
     *
     * @param parameterName name of the request parameter
     * @param parameterValue value of the request parameter (not null)
     * @return long value
     * @throws OMFRuntimeException the value is not a whole number in the range of a long
     */
    public static long getLongRequestParameter(String parameterName,
                                               String parameterValue)
    {
        return getLong(PROPERTY_KIND_REQUEST, parameterName, parameterValue);
    }


    /**
     * Convert the value to an int.
     *
     * @param propertyKind configuration property or request parameter
     * @param propertyName name of the property
     * @param propertyValue value to convert
     * @return int value
     */
    private static int getInt(String propertyKind,
                              String propertyName,
                              Object propertyValue)
    {
        final String methodName = "getInt";

        try
        {
            return getBigDecimal(propertyValue).intValueExact();
        }
        catch (NumberFormatException | ArithmeticException error)
        {
            throw getInvalidValueException(propertyKind, propertyName, propertyValue, "int", methodName, error);
        }
    }


    /**
     * Convert the value to a long.
     *
     * @param propertyKind configuration property or request parameter
     * @param propertyName name of the property
     * @param propertyValue value to convert
     * @return long value
     */
    private static long getLong(String propertyKind,
                                String propertyName,
                                Object propertyValue)
    {
        final String methodName = "getLong";

        try
        {
            return getBigDecimal(propertyValue).longValueExact();
        }
        catch (NumberFormatException | ArithmeticException error)
        {
            throw getInvalidValueException(propertyKind, propertyName, propertyValue, "long", methodName, error);
        }
    }


    /**
     * Read the value as an exact decimal.  The exact conversions that follow reject a fractional part or a
     * value out of range, rather than truncating it.
     *
     * @param propertyValue value to read
     * @return decimal value
     * @throws NumberFormatException the value is not a number
     */
    private static BigDecimal getBigDecimal(Object propertyValue)
    {
        if (propertyValue instanceof BigDecimal bigDecimal)
        {
            return bigDecimal;
        }

        return new BigDecimal(propertyValue.toString().trim());
    }


    /**
     * Build the exception that reports the invalid value.
     *
     * @param propertyKind configuration property or request parameter
     * @param propertyName name of the property
     * @param propertyValue invalid value
     * @param expectedType name of the type the value should convert to
     * @param methodName calling method
     * @param error parsing error
     * @return exception to throw
     */
    private static OMFRuntimeException getInvalidValueException(String           propertyKind,
                                                                String           propertyName,
                                                                Object           propertyValue,
                                                                String           expectedType,
                                                                String           methodName,
                                                                RuntimeException error)
    {
        return new OMFRuntimeException(OCFErrorCode.INVALID_NUMERIC_PROPERTY.getMessageDefinition(propertyKind,
                                                                                                  propertyName,
                                                                                                  String.valueOf(propertyValue),
                                                                                                  expectedType),
                                       NumericPropertyParser.class.getName(),
                                       methodName,
                                       error);
    }
}
