/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.model;

import java.io.Serializable;

/**
 * The DataType enum facilitates the conversion between the properties type from a metadata collection to Java types.
 */
public enum DataType implements Serializable {
    BOOLEAN(1, "boolean", "java.lang.Boolean"),
    BYTE(2, "byte", "java.lang.Byte"),
    CHAR(3, "char", "java.Lang.Char"),
    SHORT(4, "short", "java.lang.Short"),
    INT(5, "int", "java.lang.Integer"),
    LONG(6, "long", "java.lang.Long"),
    FLOAT(7, "float", "java.lang.Float"),
    DOUBLE(8, "double", "java.lang.Double"),
    BIG_INTEGER(9, "biginteger", "java.math.BigInteger"),
    BIG_DECIMAL(10, "bigdecimal", "java.math.BigDecimal"),
    STRING(11, "string", "java.lang.String"),
    DATE(12, "date", "java.util.Date");


    private static final long serialVersionUID = 1L;

    private int code;
    private String name;
    private String javaClassName;

    DataType(int code, String name, String javaClassName) {
        this.code = code;
        this.name = name;
        this.javaClassName = javaClassName;
    }
}
