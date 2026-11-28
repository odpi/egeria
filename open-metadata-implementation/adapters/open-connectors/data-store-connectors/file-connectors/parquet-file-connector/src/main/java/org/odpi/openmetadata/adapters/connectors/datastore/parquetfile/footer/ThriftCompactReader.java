/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.footer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ThriftCompactReader decodes a structure that has been serialized using the Apache Thrift compact protocol.
 * This is the encoding used for all the metadata structures in an Apache Parquet file.
 * <br><br>
 * The reader is deliberately generic.  It does not know the definition of the structure it is decoding, and so it
 * returns each structure as a map from the field's identifier to its value.  The value is one of:
 * <ul>
 *     <li>Boolean - for a boolean field;</li>
 *     <li>Long - for any of the integer fields (i8, i16, i32, i64);</li>
 *     <li>Double - for a double field;</li>
 *     <li>byte[] - for a binary or string field;</li>
 *     <li>List - for a list or set field;</li>
 *     <li>Map - for either a nested structure (keyed by field identifier) or a map field.</li>
 * </ul>
 * Working this way means that fields added by newer versions of the Parquet format are read, and then ignored by
 * the caller, rather than causing a failure.  It also means that there is no need for the Thrift libraries, or
 * code generated from the Parquet thrift definition.
 */
public class ThriftCompactReader
{
    private static final int TYPE_STOP          = 0;
    private static final int TYPE_BOOLEAN_TRUE  = 1;
    private static final int TYPE_BOOLEAN_FALSE = 2;
    private static final int TYPE_BYTE          = 3;
    private static final int TYPE_I16           = 4;
    private static final int TYPE_I32           = 5;
    private static final int TYPE_I64           = 6;
    private static final int TYPE_DOUBLE        = 7;
    private static final int TYPE_BINARY        = 8;
    private static final int TYPE_LIST          = 9;
    private static final int TYPE_SET           = 10;
    private static final int TYPE_MAP           = 11;
    private static final int TYPE_STRUCT        = 12;
    private static final int TYPE_UUID          = 13;

    /*
     * Structures in a Parquet footer nest only a few levels deep (the logical type of a column in a row group
     * is about seven).  The limit stops a corrupt, or malicious, file from exhausting the stack.
     */
    private static final int MAX_NESTING_DEPTH = 64;

    private final byte[] buffer;
    private final int    limit;
    private       int    position;


    /**
     * Create a reader over a buffer of bytes.
     *
     * @param buffer bytes to decode
     */
    public ThriftCompactReader(byte[] buffer)
    {
        this(buffer, 0, buffer.length);
    }


    /**
     * Create a reader over part of a buffer of bytes.
     *
     * @param buffer bytes to decode
     * @param offset position of the first byte to decode
     * @param length number of bytes available to decode
     */
    public ThriftCompactReader(byte[] buffer,
                               int    offset,
                               int    length)
    {
        this.buffer   = buffer;
        this.position = offset;
        this.limit    = offset + length;
    }


    /**
     * Return the position of the next byte to read.  After a call to readStruct, this is the first byte
     * after the structure.
     *
     * @return offset into the buffer
     */
    public int getPosition()
    {
        return position;
    }


    /**
     * Decode a structure.
     *
     * @return map of field identifier to field value
     * @throws IOException the buffer does not contain a valid structure
     */
    public Map<Integer, Object> readStruct() throws IOException
    {
        return readStruct(0);
    }


    /**
     * Decode a structure.
     *
     * @param depth number of structures and collections that this structure is nested inside
     * @return map of field identifier to field value
     * @throws IOException the buffer does not contain a valid structure
     */
    private Map<Integer, Object> readStruct(int depth) throws IOException
    {
        checkDepth(depth);

        Map<Integer, Object> fields = new HashMap<>();

        int lastFieldId = 0;

        while (true)
        {
            int header = readByte();

            if (header == TYPE_STOP)
            {
                return fields;
            }

            int type    = header & 0x0f;
            int idDelta = (header >> 4) & 0x0f;
            int fieldId;

            if (idDelta == 0)
            {
                /*
                 * The field identifier is too far from the previous one to be expressed as a delta.
                 */
                fieldId = (int) readZigZag();
            }
            else
            {
                fieldId = lastFieldId + idDelta;
            }

            lastFieldId = fieldId;

            if (type == TYPE_BOOLEAN_TRUE)
            {
                fields.put(fieldId, Boolean.TRUE);
            }
            else if (type == TYPE_BOOLEAN_FALSE)
            {
                fields.put(fieldId, Boolean.FALSE);
            }
            else
            {
                fields.put(fieldId, readValue(type, depth + 1));
            }
        }
    }


    /**
     * Decode a single value.
     *
     * @param type compact protocol type of the value
     * @param depth number of structures and collections that this value is nested inside
     * @return decoded value
     * @throws IOException the buffer does not contain a valid value
     */
    private Object readValue(int type,
                             int depth) throws IOException
    {
        switch (type)
        {
            case TYPE_BOOLEAN_TRUE:
            case TYPE_BOOLEAN_FALSE:
                /*
                 * This is a boolean in a collection, which has its own byte.  (A boolean field in a
                 * structure is encoded in the field's header.)
                 */
                return readByte() == TYPE_BOOLEAN_TRUE;

            case TYPE_BYTE:
                return (long) (byte) readByte();

            case TYPE_I16:
            case TYPE_I32:
            case TYPE_I64:
                return readZigZag();

            case TYPE_DOUBLE:
                return Double.longBitsToDouble(readFixedLong());

            case TYPE_BINARY:
                return readBytes(readLength());

            case TYPE_UUID:
                return readBytes(16);

            case TYPE_LIST:
            case TYPE_SET:
                return readList(depth);

            case TYPE_MAP:
                return readMap(depth);

            case TYPE_STRUCT:
                return readStruct(depth);

            default:
                throw new IOException("Unrecognized thrift compact protocol type " + type + " at offset " + (position - 1));
        }
    }


    /**
     * Decode a list (or set).
     *
     * @param depth number of structures and collections that this list is nested inside
     * @return list of decoded values
     * @throws IOException the buffer does not contain a valid list
     */
    private List<Object> readList(int depth) throws IOException
    {
        checkDepth(depth);

        int header      = readByte();
        int elementType = header & 0x0f;
        int size        = (header >> 4) & 0x0f;

        if (size == 15)
        {
            size = readLength();
        }

        /*
         * Every element, of every type, takes at least one byte.
         */
        if (size > (limit - position))
        {
            throw new IOException("List of " + size + " elements at offset " + position + " is longer than the remaining data");
        }

        List<Object> values = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
        {
            values.add(readValue(elementType, depth + 1));
        }

        return values;
    }


    /**
     * Decode a map.
     *
     * @param depth number of structures and collections that this map is nested inside
     * @return map of decoded values
     * @throws IOException the buffer does not contain a valid map
     */
    private Map<Object, Object> readMap(int depth) throws IOException
    {
        checkDepth(depth);

        int size = readLength();

        Map<Object, Object> values = new HashMap<>();

        if (size > 0)
        {
            int types     = readByte();
            int keyType   = (types >> 4) & 0x0f;
            int valueType = types & 0x0f;

            for (int i = 0; i < size; i++)
            {
                Object key = readValue(keyType, depth + 1);

                values.put(key, readValue(valueType, depth + 1));
            }
        }

        return values;
    }


    /**
     * Check that the structure is not nested implausibly deeply.
     *
     * @param depth current depth
     * @throws IOException too deep
     */
    private void checkDepth(int depth) throws IOException
    {
        if (depth > MAX_NESTING_DEPTH)
        {
            throw new IOException("Structure is nested more than " + MAX_NESTING_DEPTH + " levels deep at offset " + position);
        }
    }


    /**
     * Read the next byte as an unsigned value.
     *
     * @return value between 0 and 255
     * @throws IOException no more data
     */
    private int readByte() throws IOException
    {
        if (position >= limit)
        {
            throw new IOException("Unexpected end of data at offset " + position);
        }

        return buffer[position++] & 0xff;
    }


    /**
     * Read a block of bytes.
     *
     * @param length number of bytes
     * @return bytes
     * @throws IOException not enough data
     */
    private byte[] readBytes(int length) throws IOException
    {
        if (length > (limit - position))
        {
            throw new IOException("Value of " + length + " bytes at offset " + position + " is longer than the remaining data");
        }

        byte[] bytes = new byte[length];

        System.arraycopy(buffer, position, bytes, 0, length);
        position = position + length;

        return bytes;
    }


    /**
     * Read an unsigned variable length integer.
     *
     * @return value
     * @throws IOException invalid data
     */
    private long readVarInt() throws IOException
    {
        long result = 0;
        int  shift  = 0;

        while (true)
        {
            int nextByte = readByte();

            result = result | ((long) (nextByte & 0x7f) << shift);

            if ((nextByte & 0x80) == 0)
            {
                return result;
            }

            shift = shift + 7;

            if (shift > 63)
            {
                throw new IOException("Variable length integer is too long at offset " + position);
            }
        }
    }


    /**
     * Read a signed integer that is zigzag encoded in a variable length integer.
     *
     * @return value
     * @throws IOException invalid data
     */
    private long readZigZag() throws IOException
    {
        long value = readVarInt();

        return (value >>> 1) ^ -(value & 1);
    }


    /**
     * Read the length of a value or collection.
     *
     * @return length
     * @throws IOException invalid data, or implausible length
     */
    private int readLength() throws IOException
    {
        long length = readVarInt();

        if ((length < 0) || (length > Integer.MAX_VALUE))
        {
            throw new IOException("Invalid length " + length + " at offset " + position);
        }

        return (int) length;
    }


    /**
     * Read a fixed length, little endian, 8 byte value.
     *
     * @return value
     * @throws IOException not enough data
     */
    private long readFixedLong() throws IOException
    {
        long result = 0;

        for (int i = 0; i < 8; i++)
        {
            result = result | ((long) readByte() << (8 * i));
        }

        return result;
    }
}
