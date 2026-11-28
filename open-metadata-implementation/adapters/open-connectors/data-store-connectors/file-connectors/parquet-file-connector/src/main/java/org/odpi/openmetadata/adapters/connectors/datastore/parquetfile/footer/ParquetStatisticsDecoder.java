/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.footer;

import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetSchemaElement;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

/**
 * ParquetStatisticsDecoder converts the minimum and maximum values that a writer saved in the footer of
 * a Parquet file into Java objects.  The class of the object depends on the type of the column:
 * <ul>
 *     <li>Boolean - for boolean columns;</li>
 *     <li>Long - for integer columns;</li>
 *     <li>BigInteger - for unsigned 64-bit integer columns;</li>
 *     <li>Double - for floating point columns;</li>
 *     <li>BigDecimal - for decimal columns;</li>
 *     <li>LocalDate - for date columns;</li>
 *     <li>LocalTime - for time columns;</li>
 *     <li>Instant - for timestamps that are on the UTC timeline;</li>
 *     <li>LocalDateTime - for timestamps that have no time zone;</li>
 *     <li>String - for everything else: text is returned as is, a UUID in its standard form, and other binary
 *     values in hexadecimal.</li>
 * </ul>
 * All of these classes are comparable, and each column only ever produces values of one class, so that values
 * from different row groups can be compared with one another.
 */
public class ParquetStatisticsDecoder
{
    private static final String MILLIS = "MILLIS";
    private static final String MICROS = "MICROS";

    /*
     * The number of characters of a text or binary value that is kept.
     */
    private static final int    MAX_VALUE_LENGTH = 256;

    private static final char[] hexDigits = "0123456789abcdef".toCharArray();


    /**
     * This class only has static methods.
     */
    private ParquetStatisticsDecoder()
    {
    }


    /**
     * Return whether the values of a column are ordered using a simple signed comparison.  The minimum and maximum
     * values written to the original statistics fields (by writers from before 2017) were always calculated
     * this way, whatever the type of the column.  This means they are only correct for these columns.
     *
     * @param column description of the column
     * @return boolean
     */
    public static boolean usesSignedOrder(ParquetSchemaElement column)
    {
        if (column.isUnsigned())
        {
            return false;
        }

        String physicalType = column.getPhysicalType();

        return "BOOLEAN".equals(physicalType) ||
               "INT32".equals(physicalType) ||
               "INT64".equals(physicalType) ||
               "FLOAT".equals(physicalType) ||
               "DOUBLE".equals(physicalType);
    }


    /**
     * Convert a value from the statistics.
     *
     * @param value value as stored in the file
     * @param column description of the column that the value is from
     * @return comparable object, or null if the value is missing or not usable
     */
    public static Comparable<?> decode(byte[]               value,
                                       ParquetSchemaElement column)
    {
        if ((value == null) || (column == null) || (column.getPhysicalType() == null))
        {
            return null;
        }

        String annotation = column.getAnnotation();

        if (annotation == null)
        {
            annotation = "";
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN);

        switch (column.getPhysicalType())
        {
            case "BOOLEAN" ->
            {
                if (value.length < 1)
                {
                    return null;
                }

                return value[0] != 0;
            }

            case "INT32" ->
            {
                if (value.length < 4)
                {
                    return null;
                }

                int intValue = byteBuffer.getInt();

                if (column.isUnsigned())
                {
                    return Integer.toUnsignedLong(intValue);
                }

                switch (annotation)
                {
                    case "DECIMAL" -> { return BigDecimal.valueOf(intValue, getScale(column)); }
                    case "DATE"    -> { return LocalDate.ofEpochDay(intValue); }
                    case "TIME", "TIME_MILLIS" ->
                    {
                        if ((intValue < 0) || (intValue >= 86400000))
                        {
                            return null;
                        }

                        return LocalTime.ofNanoOfDay(intValue * 1000000L);
                    }
                    default -> { return (long) intValue; }
                }
            }

            case "INT64" ->
            {
                if (value.length < 8)
                {
                    return null;
                }

                long longValue = byteBuffer.getLong();

                if (column.isUnsigned())
                {
                    return new BigInteger(Long.toUnsignedString(longValue));
                }

                switch (annotation)
                {
                    case "DECIMAL" -> { return BigDecimal.valueOf(longValue, getScale(column)); }
                    case "TIME", "TIME_MICROS" -> { return decodeTime(longValue, getTimeUnit(column, annotation)); }
                    case "TIMESTAMP", "TIMESTAMP_MILLIS", "TIMESTAMP_MICROS" ->
                    {
                        return decodeTimestamp(longValue, getTimeUnit(column, annotation), column.getAdjustedToUTC());
                    }
                    default -> { return longValue; }
                }
            }

            case "FLOAT" ->
            {
                if (value.length < 4)
                {
                    return null;
                }

                return decodeDouble(byteBuffer.getFloat());
            }

            case "DOUBLE" ->
            {
                if (value.length < 8)
                {
                    return null;
                }

                return decodeDouble(byteBuffer.getDouble());
            }

            case "BYTE_ARRAY", "FIXED_LEN_BYTE_ARRAY" ->
            {
                switch (annotation)
                {
                    case "STRING", "UTF8", "ENUM", "JSON" -> { return truncate(new String(value, StandardCharsets.UTF_8)); }
                    case "DECIMAL" ->
                    {
                        if (value.length == 0)
                        {
                            return null;
                        }

                        return new BigDecimal(new BigInteger(value), getScale(column));
                    }
                    case "UUID" ->
                    {
                        if (value.length != 16)
                        {
                            return null;
                        }

                        String hex = toHex(value, 16);

                        return hex.substring(0, 8) + "-" + hex.substring(8, 12) + "-" + hex.substring(12, 16) + "-" + hex.substring(16, 20) + "-" + hex.substring(20);
                    }
                    case "FLOAT16" ->
                    {
                        if (value.length != 2)
                        {
                            return null;
                        }

                        return decodeDouble(Float.float16ToFloat(byteBuffer.getShort()));
                    }
                    default -> { return "0x" + toHex(value, MAX_VALUE_LENGTH / 2); }
                }
            }

            default ->
            {
                /*
                 * This includes INT96, which has no defined ordering and so its statistics are not usable.
                 */
                return null;
            }
        }
    }


    /**
     * Return the smaller of two values from the same column.
     *
     * @param first value (may be null)
     * @param second value (may be null)
     * @return the smaller value, or the value that is not null
     */
    public static Comparable<?> min(Comparable<?> first,
                                    Comparable<?> second)
    {
        if (first == null)
        {
            return second;
        }

        if (second == null)
        {
            return first;
        }

        return (compare(first, second) <= 0) ? first : second;
    }


    /**
     * Return the larger of two values from the same column.
     *
     * @param first value (may be null)
     * @param second value (may be null)
     * @return the larger value, or the value that is not null
     */
    public static Comparable<?> max(Comparable<?> first,
                                    Comparable<?> second)
    {
        if (first == null)
        {
            return second;
        }

        if (second == null)
        {
            return first;
        }

        return (compare(first, second) >= 0) ? first : second;
    }


    /**
     * Compare two values from the same column.
     *
     * @param first value
     * @param second value
     * @return negative, zero or positive
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static int compare(Comparable first,
                               Comparable second)
    {
        if (first.getClass() != second.getClass())
        {
            /*
             * This is not expected since all the values come from the same column.
             */
            return first.toString().compareTo(second.toString());
        }

        return first.compareTo(second);
    }


    /**
     * Return the scale of a decimal column.
     *
     * @param column description of the column
     * @return scale
     */
    private static int getScale(ParquetSchemaElement column)
    {
        return (column.getScale() == null) ? 0 : column.getScale();
    }


    /**
     * Return the unit used to store a time or timestamp.
     *
     * @param column description of the column
     * @param annotation the logical, or converted, type of the column
     * @return MILLIS, MICROS or NANOS
     */
    private static String getTimeUnit(ParquetSchemaElement column,
                                      String               annotation)
    {
        if (column.getTimeUnit() != null)
        {
            return column.getTimeUnit();
        }

        return annotation.endsWith(MILLIS) ? MILLIS : MICROS;
    }


    /**
     * Convert a time of day.
     *
     * @param value value from the file
     * @param timeUnit the units that the value is in
     * @return time, or null if the value is out of range
     */
    private static LocalTime decodeTime(long   value,
                                        String timeUnit)
    {
        long nanoseconds;

        if (MILLIS.equals(timeUnit))
        {
            nanoseconds = value * 1000000L;
        }
        else if (MICROS.equals(timeUnit))
        {
            nanoseconds = value * 1000L;
        }
        else
        {
            nanoseconds = value;
        }

        if ((nanoseconds < 0) || (nanoseconds > LocalTime.MAX.toNanoOfDay()))
        {
            return null;
        }

        return LocalTime.ofNanoOfDay(nanoseconds);
    }


    /**
     * Convert a timestamp.
     *
     * @param value value from the file
     * @param timeUnit the units that the value is in
     * @param adjustedToUTC is this an instant on the UTC timeline (true or null) or a local date and time (false)
     * @return Instant or LocalDateTime, or null if the value is out of range
     */
    private static Comparable<?> decodeTimestamp(long    value,
                                                 String  timeUnit,
                                                 Boolean adjustedToUTC)
    {
        long unitsPerSecond;
        long nanosecondsPerUnit;

        if (MILLIS.equals(timeUnit))
        {
            unitsPerSecond     = 1000L;
            nanosecondsPerUnit = 1000000L;
        }
        else if (MICROS.equals(timeUnit))
        {
            unitsPerSecond     = 1000000L;
            nanosecondsPerUnit = 1000L;
        }
        else
        {
            unitsPerSecond     = 1000000000L;
            nanosecondsPerUnit = 1L;
        }

        try
        {
            Instant instant = Instant.ofEpochSecond(Math.floorDiv(value, unitsPerSecond),
                                                    Math.floorMod(value, unitsPerSecond) * nanosecondsPerUnit);

            if (Boolean.FALSE.equals(adjustedToUTC))
            {
                return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
            }

            return instant;
        }
        catch (RuntimeException outOfRange)
        {
            return null;
        }
    }


    /**
     * Convert a floating point number.  NaN is not a usable minimum or maximum.
     *
     * @param value value from the file
     * @return value or null
     */
    private static Double decodeDouble(double value)
    {
        if (Double.isNaN(value))
        {
            return null;
        }

        return value;
    }


    /**
     * Limit the size of a text value.
     *
     * @param value value from the file
     * @return value, shortened if necessary
     */
    private static String truncate(String value)
    {
        if (value.length() > MAX_VALUE_LENGTH)
        {
            return value.substring(0, MAX_VALUE_LENGTH);
        }

        return value;
    }


    /**
     * Convert bytes to hexadecimal.
     *
     * @param value bytes
     * @param maxBytes the maximum number of bytes to convert
     * @return string
     */
    private static String toHex(byte[] value,
                                int    maxBytes)
    {
        int           length        = Math.min(value.length, maxBytes);
        StringBuilder stringBuilder = new StringBuilder(length * 2);

        for (int i = 0; i < length; i++)
        {
            stringBuilder.append(hexDigits[(value[i] >> 4) & 0x0f]);
            stringBuilder.append(hexDigits[value[i] & 0x0f]);
        }

        return stringBuilder.toString();
    }
}
