/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.types.DataValueDescriptor;
import org.apache.derby.iapi.types.TypeId;

import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ApplyMasking {

    private static final String DEFAULT_CHAR_MASKING = "####";
    private static final int DEFAULT_INT_MASKING = 1111;
    private static final String DEFAULT_DATA_MASKING = "0001-01-01";
    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final String CHAR_MASKING_PROPERTY = "ranger.plugin.gaian.char.masking";
    private static final String INT_MASKING_PROPERTY = "ranger.plugin.gaian.int.masking";
    private static final String DATE_MASKING_PROPERTY = "ranger.plugin.gaian.date.masking";

    public static void redact(DataValueDescriptor dataValueDescriptor, Boolean isNullMasking, Properties properties) throws StandardException, ParseException {
        if (dataValueDescriptor == null) {
            return;
        }

        if (isNullMasking) {
            maskedToNull(dataValueDescriptor);
        } else {
            customMasking(dataValueDescriptor, properties);
        }
    }

    private static void maskedToNull(DataValueDescriptor dataValueDescriptor) {
        dataValueDescriptor.setToNull();
    }

    private static void customMasking(DataValueDescriptor dataValueDescriptor, Properties properties) throws StandardException, ParseException {
        int jdbcType = TypeId.getBuiltInTypeId(dataValueDescriptor.getTypeName()).getJDBCTypeId();
        switch (jdbcType) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.CLOB:
                dataValueDescriptor.setValue(getCharMaskingValue(properties));
                break;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                dataValueDescriptor.setValue(getDateMaskingValue(properties));
                break;
            case Types.INTEGER:
            case Types.DOUBLE:
            case Types.DECIMAL:
            case Types.FLOAT:
                dataValueDescriptor.setValue(getIntMaskingValue(properties));
                break;
            default:
                dataValueDescriptor.setValue("Masked");
        }
    }

    private static String getCharMaskingValue(Properties properties) {
        String property = getStringProperty(properties, CHAR_MASKING_PROPERTY);
        if (property != null) return property;
        return DEFAULT_CHAR_MASKING;
    }

    private static int getIntMaskingValue(Properties properties){
        String property = getStringProperty(properties, INT_MASKING_PROPERTY);
        if (property != null) return Integer.valueOf(property);
        return DEFAULT_INT_MASKING;
    }

    private static Date getDateMaskingValue(Properties properties) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        return format.parse(getDateMasking(properties));
    }

    private static String getDateMasking(Properties properties){
        String property = getStringProperty(properties, DATE_MASKING_PROPERTY);
        if (property != null) return property;
        return DEFAULT_DATA_MASKING;
    }

    private static String getStringProperty(Properties properties, String s) {
        if (properties != null) {
            String property = properties.getProperty(s);
            if (property != null) {
                return property;
            }
        }
        return null;
    }
}