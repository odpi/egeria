/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.footer;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * ThriftCompactWriter builds a structure in the Thrift compact protocol, so that the tests can create the
 * footers of Parquet files that today's writers no longer produce.  Fields must be added in ascending order
 * of their identifier.
 */
class ThriftCompactWriter
{
    private static final int TYPE_BOOLEAN_TRUE  = 1;
    private static final int TYPE_BOOLEAN_FALSE = 2;
    private static final int TYPE_I32           = 5;
    private static final int TYPE_I64           = 6;
    private static final int TYPE_BINARY        = 8;
    private static final int TYPE_LIST          = 9;
    private static final int TYPE_STRUCT        = 12;

    private final ByteArrayOutputStream bytes       = new ByteArrayOutputStream();
    private       int                   lastFieldId = 0;


    ThriftCompactWriter bool(int fieldId, boolean value)
    {
        fieldHeader(fieldId, value ? TYPE_BOOLEAN_TRUE : TYPE_BOOLEAN_FALSE);
        return this;
    }


    ThriftCompactWriter i32(int fieldId, int value)
    {
        fieldHeader(fieldId, TYPE_I32);
        zigZag(value);
        return this;
    }


    ThriftCompactWriter i64(int fieldId, long value)
    {
        fieldHeader(fieldId, TYPE_I64);
        zigZag(value);
        return this;
    }


    ThriftCompactWriter binary(int fieldId, byte[] value)
    {
        fieldHeader(fieldId, TYPE_BINARY);
        varInt(value.length);
        bytes.writeBytes(value);
        return this;
    }


    ThriftCompactWriter string(int fieldId, String value)
    {
        return binary(fieldId, value.getBytes(StandardCharsets.UTF_8));
    }


    ThriftCompactWriter struct(int fieldId, ThriftCompactWriter value)
    {
        fieldHeader(fieldId, TYPE_STRUCT);
        bytes.writeBytes(value.toBytes());
        return this;
    }


    ThriftCompactWriter structList(int fieldId, List<ThriftCompactWriter> values)
    {
        fieldHeader(fieldId, TYPE_LIST);
        listHeader(values.size(), TYPE_STRUCT);
        for (ThriftCompactWriter value : values)
        {
            bytes.writeBytes(value.toBytes());
        }
        return this;
    }


    ThriftCompactWriter i32List(int fieldId, int... values)
    {
        fieldHeader(fieldId, TYPE_LIST);
        listHeader(values.length, TYPE_I32);
        for (int value : values)
        {
            zigZag(value);
        }
        return this;
    }


    ThriftCompactWriter stringList(int fieldId, String... values)
    {
        fieldHeader(fieldId, TYPE_LIST);
        listHeader(values.length, TYPE_BINARY);
        for (String value : values)
        {
            byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
            varInt(valueBytes.length);
            bytes.writeBytes(valueBytes);
        }
        return this;
    }


    /**
     * Return the completed structure, including its stop marker.
     *
     * @return bytes
     */
    byte[] toBytes()
    {
        byte[] content = bytes.toByteArray();
        byte[] result  = new byte[content.length + 1];

        System.arraycopy(content, 0, result, 0, content.length);

        return result;
    }


    private void fieldHeader(int fieldId, int type)
    {
        int delta = fieldId - lastFieldId;

        if ((delta > 0) && (delta <= 15))
        {
            bytes.write((delta << 4) | type);
        }
        else
        {
            bytes.write(type);
            zigZag(fieldId);
        }

        lastFieldId = fieldId;
    }


    private void listHeader(int size, int elementType)
    {
        if (size < 15)
        {
            bytes.write((size << 4) | elementType);
        }
        else
        {
            bytes.write(0xf0 | elementType);
            varInt(size);
        }
    }


    private void zigZag(long value)
    {
        varInt((value << 1) ^ (value >> 63));
    }


    private void varInt(long value)
    {
        while ((value & ~0x7fL) != 0)
        {
            bytes.write((int) ((value & 0x7f) | 0x80));
            value = value >>> 7;
        }

        bytes.write((int) value);
    }
}
