/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.footer;

import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetColumnProfile;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetFileMetadata;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetSchemaElement;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Test that the ParquetFooterReader can describe Parquet files written to different versions of the format,
 * and that it reports files that it cannot describe.
 * <br><br>
 * The files in src/test/resources were written by DuckDB 2.0.0.  The values expected in the tests are the values
 * that DuckDB reports for the same files through its parquet_schema(), parquet_metadata() and
 * parquet_file_metadata() functions.  The files that mimic older writers are built by the tests themselves.
 */
public class ParquetFooterReaderTest
{
    private static final String resourcesDirectory = "src/test/resources/";

    private final ParquetFooterReader footerReader = new ParquetFooterReader();


    /**
     * A flat table covering each type of column, written with the original (version 1) encodings and
     * compressed with Snappy.
     *
     * @throws IOException test failed
     */
    @Test public void testFlatFileVersion1() throws IOException
    {
        ParquetFileMetadata fileMetadata = footerReader.readFooter(new File(resourcesDirectory + "flat-v1-snappy.parquet"));

        assertEquals(fileMetadata.getFormatVersion(), 1);
        assertEquals(fileMetadata.getFileLength(), 24855L);
        assertEquals(fileMetadata.getFooterLength(), 2006);
        assertEquals(fileMetadata.getCompressionCodecs(), List.of("SNAPPY"));
        assertEquals(fileMetadata.getEncodings(), List.of("PLAIN", "PLAIN_DICTIONARY"));

        validateFlatFile(fileMetadata);
    }


    /**
     * The same table written with the version 2 encodings and compressed with ZStandard.  Everything
     * about the content of the file is the same.
     *
     * @throws IOException test failed
     */
    @Test public void testFlatFileVersion2() throws IOException
    {
        ParquetFileMetadata fileMetadata = footerReader.readFooter(new File(resourcesDirectory + "flat-v2-zstd.parquet"));

        assertEquals(fileMetadata.getFormatVersion(), 2);
        assertEquals(fileMetadata.getCompressionCodecs(), List.of("ZSTD"));
        assertEquals(fileMetadata.getEncodings(), List.of("PLAIN", "DELTA_BINARY_PACKED", "BYTE_STREAM_SPLIT", "RLE_DICTIONARY"));

        validateFlatFile(fileMetadata);
    }


    /**
     * Check the description of the flat table.
     *
     * @param fileMetadata description from the reader
     */
    private void validateFlatFile(ParquetFileMetadata fileMetadata)
    {
        assertTrue(fileMetadata.getCreatedBy().startsWith("DuckDB"));
        assertEquals(fileMetadata.getRowCount(), 300L);
        assertEquals(fileMetadata.getRowGroups().size(), 1);
        assertNull(fileMetadata.getEncryptionAlgorithm());
        assertEquals(fileMetadata.getDataPageVersions(), List.of(ParquetFooterReader.DATA_PAGE_V1));
        assertEquals(fileMetadata.getTypeAnnotationStyle(), ParquetFileMetadata.LOGICAL_TYPE_ANNOTATIONS);
        assertTrue(fileMetadata.getCompressedSize() < fileMetadata.getUncompressedSize());

        assertEquals(fileMetadata.getColumnNames(),
                     List.of("flag", "tiny", "small", "id", "big", "utiny", "usmall", "uint", "ubig", "ratio", "amount", "price",
                             "name", "day", "at", "created", "updated", "key", "payload", "empty"));

        String[] expectedDataTypes = {"boolean", "byte", "short", "int", "long", "unsigned byte", "unsigned short", "unsigned int",
                                      "unsigned long", "float", "double", "decimal(10,2)", "string", "date", "time", "timestamp",
                                      "timestamp", "uuid", "binary", "int"};

        for (int i = 0; i < expectedDataTypes.length; i++)
        {
            assertEquals(fileMetadata.getColumns().get(i).getDataType(), expectedDataTypes[i], fileMetadata.getColumns().get(i).getName());
        }

        assertFalse(getColumn(fileMetadata, "id").isNullable());
        assertTrue(getColumn(fileMetadata, "name").isNullable());
        assertEquals(getColumn(fileMetadata, "created").getLogicalType(), "TIMESTAMP(MICROS, local)");
        assertEquals(getColumn(fileMetadata, "updated").getLogicalType(), "TIMESTAMP(MICROS, UTC)");
        assertEquals(getColumn(fileMetadata, "price").getPhysicalType(), "INT64");
        assertEquals(getColumn(fileMetadata, "key").getTypeLength(), Integer.valueOf(16));

        assertEquals(fileMetadata.getColumnProfiles().size(), 20);

        validateProfile(fileMetadata, "flag", 300L, 0L, "false", "true");
        validateProfile(fileMetadata, "tiny", 300L, 0L, "-50", "49");
        validateProfile(fileMetadata, "small", 300L, 0L, "-150", "149");
        validateProfile(fileMetadata, "id", 300L, 0L, "-1000", "1990");
        validateProfile(fileMetadata, "big", 300L, 0L, "0", "299000002093");
        validateProfile(fileMetadata, "utiny", 300L, 0L, "0", "199");
        validateProfile(fileMetadata, "usmall", 300L, 0L, "60000", "60299");
        validateProfile(fileMetadata, "uint", 300L, 0L, "4000000000", "4000000299");
        validateProfile(fileMetadata, "ubig", 300L, 0L, "18000000000000000000", "18000000000000000299");
        validateProfile(fileMetadata, "ratio", 300L, 0L, "0.0", "74.75");
        validateProfile(fileMetadata, "amount", 300L, 0L, "-100.0", "348.5");
        validateProfile(fileMetadata, "price", 300L, 0L, "-50.00", "323.75");
        validateProfile(fileMetadata, "name", 300L, 43L, "name-000", "name-049");
        validateProfile(fileMetadata, "day", 300L, 0L, "2024-01-01", "2024-10-26");
        validateProfile(fileMetadata, "at", 300L, 0L, "08:00", "08:04:59");
        validateProfile(fileMetadata, "created", 300L, 0L, "2024-01-01T00:00", "2024-01-01T04:59");
        validateProfile(fileMetadata, "updated", 300L, 0L, "2024-01-01T00:00:00Z", "2024-01-13T11:00:00Z");
        validateProfile(fileMetadata, "key", 300L, 0L, "00000000-0000-0000-0000-000000000000", "00000000-0000-0000-0000-000000000299");
        validateProfile(fileMetadata, "payload", 300L, 0L, "0x626c6f6230", "0x626c6f6234");

        /*
         * Every value in this column is null and so there is no minimum or maximum.
         */
        validateProfile(fileMetadata, "empty", 300L, 300L, null, null);

        assertEquals(getProfile(fileMetadata, "name").getDistinctCount(), Long.valueOf(50L));
        assertNull(getProfile(fileMetadata, "id").getDistinctCount());
    }


    /**
     * A file with lists, structures and maps.  It also has properties added by the writer.
     *
     * @throws IOException test failed
     */
    @Test public void testNestedFile() throws IOException
    {
        ParquetFileMetadata fileMetadata = footerReader.readFooter(new File(resourcesDirectory + "nested-v1-gzip.parquet"));

        assertEquals(fileMetadata.getRowCount(), 20L);
        assertEquals(fileMetadata.getCompressionCodecs(), List.of("GZIP"));
        assertEquals(fileMetadata.getKeyValueMetadata().get("origin"), "egeria-test");
        assertEquals(fileMetadata.getKeyValueMetadata().get("purpose"), "nested types");

        assertEquals(fileMetadata.getColumnNames(), List.of("id", "scores", "address", "counters", "items"));

        assertEquals(getColumn(fileMetadata, "id").getDataType(), "int");
        assertEquals(getColumn(fileMetadata, "scores").getDataType(), "array<int>");
        assertEquals(getColumn(fileMetadata, "address").getDataType(), "struct<street:string,geo:struct<lat:decimal(21,1),lon:decimal(21,1)>>");
        assertEquals(getColumn(fileMetadata, "counters").getDataType(), "map<string,long>");
        assertEquals(getColumn(fileMetadata, "items").getDataType(), "array<struct<sku:string,qty:long>>");

        assertFalse(getColumn(fileMetadata, "address").isLeaf());
        assertEquals(getColumn(fileMetadata, "address").getChildren().get(1).getPathName(), "address.geo");

        /*
         * The profiles are for the columns that hold the values.  There are three values in each row's list and
         * two entries in each row's map.
         */
        assertEquals(fileMetadata.getColumnProfiles().size(), 9);

        validateProfile(fileMetadata, "scores.list.element", 60L, 0L, "0", "21");
        validateProfile(fileMetadata, "address.street", 20L, 0L, "road 0", "road 9");
        validateProfile(fileMetadata, "address.geo.lon", 20L, 0L, "-9.5", "0.0");
        validateProfile(fileMetadata, "counters.key_value.key", 40L, 0L, "a", "b");
        validateProfile(fileMetadata, "counters.key_value.value", 40L, 0L, "0", "38");
        validateProfile(fileMetadata, "items.list.element.qty", 20L, 0L, "0", "19");
    }


    /**
     * A file with three row groups.  The values from each of the row groups are combined.
     *
     * @throws IOException test failed
     */
    @Test public void testMultipleRowGroups() throws IOException
    {
        ParquetFileMetadata fileMetadata = footerReader.readFooter(new File(resourcesDirectory + "rowgroups-v2-zstd.parquet"));

        assertEquals(fileMetadata.getRowCount(), 5000L);
        assertEquals(fileMetadata.getRowGroups().size(), 3);
        assertEquals(fileMetadata.getRowGroups().get(0).getRowCount(), 2048L);
        assertEquals(fileMetadata.getRowGroups().get(2).getRowCount(), 904L);
        assertEquals(fileMetadata.getRowGroups().get(0).getColumnChunks().get(1).getNullCount(), Long.valueOf(205L));
        assertEquals(fileMetadata.getTypeAnnotationStyle(), ParquetFileMetadata.CONVERTED_TYPE_ANNOTATIONS);

        validateProfile(fileMetadata, "id", 5000L, 0L, "0", "4999");
        validateProfile(fileMetadata, "name", 5000L, 500L, "customer-1", "customer-9");
        validateProfile(fileMetadata, "day", 5000L, 0L, "2020-01-01", "2021-05-15");

        /*
         * The number of distinct values in each row group cannot be combined.
         */
        assertNull(getProfile(fileMetadata, "name").getDistinctCount());

        long compressedSize = 0L;

        for (int i = 0; i < 3; i++)
        {
            compressedSize = compressedSize + fileMetadata.getRowGroups().get(i).getCompressedSize();
        }

        assertEquals(fileMetadata.getCompressedSize(), compressedSize);
    }


    /**
     * A file with a schema and no rows.
     *
     * @throws IOException test failed
     */
    @Test public void testEmptyFile() throws IOException
    {
        ParquetFileMetadata fileMetadata = footerReader.readFooter(new File(resourcesDirectory + "empty-uncompressed.parquet"));

        assertEquals(fileMetadata.getRowCount(), 0L);
        assertTrue(fileMetadata.getRowGroups().isEmpty());
        assertEquals(fileMetadata.getColumnNames().size(), 20);
        assertEquals(fileMetadata.getColumnProfiles().size(), 20);
        assertTrue(fileMetadata.getCompressionCodecs().isEmpty());
        assertTrue(fileMetadata.getDataPageVersions().isEmpty());

        validateProfile(fileMetadata, "id", 0L, null, null, null);
    }


    /**
     * A file with the characteristics of those written by the first generation of writers (such as Apache Hive
     * and Impala, and Apache Spark 1.x).  Specifically:
     * <ul>
     *     <li>The columns are annotated with converted types and there are no logical types.</li>
     *     <li>A timestamp is stored as an INT96.</li>
     *     <li>A list uses the two-level layout.</li>
     *     <li>The statistics use the original minimum and maximum fields, which are correct for numbers and
     *     incorrect for strings.</li>
     *     <li>The footer does not say which version of data page is used.  The first column has a data page
     *     with the original layout.  The second column has a dictionary page followed by a data page with the
     *     version 2 layout.</li>
     * </ul>
     * It also has a field in the footer that the reader does not recognize.
     *
     * @throws IOException test failed
     */
    @Test public void testLegacyFile() throws IOException
    {
        ByteArrayOutputStream fileContent = new ByteArrayOutputStream();

        fileContent.writeBytes("PAR1".getBytes(StandardCharsets.US_ASCII));

        long idPageOffset = fileContent.size();

        fileContent.writeBytes(new ThriftCompactWriter().i32(1, 0).i32(2, 8).i32(3, 8)
                                                        .struct(5, new ThriftCompactWriter().i32(1, 2).i32(2, 0).i32(3, 3).i32(4, 3))
                                                        .toBytes());
        fileContent.writeBytes(new byte[8]);

        long namePageOffset = fileContent.size();

        fileContent.writeBytes(new ThriftCompactWriter().i32(1, 2).i32(2, 5).i32(3, 5)
                                                        .struct(7, new ThriftCompactWriter().i32(1, 1).i32(2, 2))
                                                        .toBytes());
        fileContent.writeBytes(new byte[5]);
        fileContent.writeBytes(new ThriftCompactWriter().i32(1, 3).i32(2, 6).i32(3, 6)
                                                        .struct(8, new ThriftCompactWriter().i32(1, 2).i32(2, 0).i32(3, 2).i32(4, 8))
                                                        .toBytes());
        fileContent.writeBytes(new byte[6]);

        List<ThriftCompactWriter> schema = List.of(
                new ThriftCompactWriter().string(4, "hive_schema").i32(5, 4),
                new ThriftCompactWriter().i32(1, 1).i32(3, 0).string(4, "id"),
                new ThriftCompactWriter().i32(1, 6).i32(3, 1).string(4, "name").i32(6, 0),
                new ThriftCompactWriter().i32(1, 3).i32(3, 1).string(4, "ts"),
                new ThriftCompactWriter().i32(3, 1).string(4, "tags").i32(5, 1).i32(6, 3),
                new ThriftCompactWriter().i32(1, 6).i32(3, 2).string(4, "array").i32(6, 0));

        ThriftCompactWriter idStatistics = new ThriftCompactWriter().binary(1, intBytes(100)).binary(2, intBytes(1)).i64(3, 0);
        ThriftCompactWriter nameStatistics = new ThriftCompactWriter().binary(1, "z".getBytes(StandardCharsets.UTF_8))
                                                                      .binary(2, "a".getBytes(StandardCharsets.UTF_8))
                                                                      .i64(3, 3);

        List<ThriftCompactWriter> columnChunks = List.of(
                new ThriftCompactWriter().i64(2, idPageOffset)
                                         .struct(3, new ThriftCompactWriter().i32(1, 1).i32List(2, 0, 3).stringList(3, "id").i32(4, 0)
                                                                             .i64(5, 100).i64(6, 400).i64(7, 400).i64(9, idPageOffset)
                                                                             .struct(12, idStatistics)),
                new ThriftCompactWriter().i64(2, namePageOffset)
                                         .struct(3, new ThriftCompactWriter().i32(1, 6).i32List(2, 2, 3).stringList(3, "name").i32(4, 1)
                                                                             .i64(5, 100).i64(6, 900).i64(7, 300).i64(9, namePageOffset)
                                                                             .struct(12, nameStatistics)),
                new ThriftCompactWriter().i64(2, 0)
                                         .struct(3, new ThriftCompactWriter().i32(1, 3).i32List(2, 0).stringList(3, "ts").i32(4, 1)
                                                                             .i64(5, 100).i64(6, 1200).i64(7, 600).i64(9, 0)),
                new ThriftCompactWriter().i64(2, 0)
                                         .struct(3, new ThriftCompactWriter().i32(1, 6).i32List(2, 0).stringList(3, "tags", "array").i32(4, 1)
                                                                             .i64(5, 250).i64(6, 700).i64(7, 350).i64(9, 0)));

        byte[] footer = new ThriftCompactWriter().i32(1, 1)
                                                 .structList(2, schema)
                                                 .i64(3, 100)
                                                 .structList(4, List.of(new ThriftCompactWriter().structList(1, columnChunks).i64(2, 3200).i64(3, 100)))
                                                 .string(6, "parquet-mr version 1.6.0")
                                                 .i32(99, 7)
                                                 .toBytes();

        fileContent.writeBytes(footer);
        fileContent.writeBytes(intBytes(footer.length));
        fileContent.writeBytes("PAR1".getBytes(StandardCharsets.US_ASCII));

        ParquetFileMetadata fileMetadata = footerReader.readFooter(writeFile(fileContent.toByteArray()));

        assertEquals(fileMetadata.getFormatVersion(), 1);
        assertEquals(fileMetadata.getCreatedBy(), "parquet-mr version 1.6.0");
        assertEquals(fileMetadata.getRowCount(), 100L);
        assertEquals(fileMetadata.getTypeAnnotationStyle(), ParquetFileMetadata.CONVERTED_TYPE_ANNOTATIONS);
        assertEquals(fileMetadata.getCompressionCodecs(), List.of("UNCOMPRESSED", "SNAPPY"));
        assertEquals(fileMetadata.getEncodings(), List.of("PLAIN", "RLE", "PLAIN_DICTIONARY"));
        assertEquals(fileMetadata.getDataPageVersions(), List.of(ParquetFooterReader.DATA_PAGE_V1, ParquetFooterReader.DATA_PAGE_V2));
        assertEquals(fileMetadata.getCompressedSize(), 1650L);
        assertEquals(fileMetadata.getUncompressedSize(), 3200L);

        assertEquals(fileMetadata.getColumnNames(), List.of("id", "name", "ts", "tags"));
        assertEquals(getColumn(fileMetadata, "id").getDataType(), "int");
        assertEquals(getColumn(fileMetadata, "name").getDataType(), "string");
        assertEquals(getColumn(fileMetadata, "ts").getDataType(), "timestamp");
        assertEquals(getColumn(fileMetadata, "tags").getDataType(), "array<string>");
        assertNull(getColumn(fileMetadata, "name").getLogicalType());

        validateProfile(fileMetadata, "id", 100L, 0L, "1", "100");
        validateProfile(fileMetadata, "name", 100L, 3L, null, null);
        validateProfile(fileMetadata, "ts", 100L, null, null, null);
        validateProfile(fileMetadata, "tags.array", 250L, null, null, null);

        assertEquals(getProfile(fileMetadata, "id").getDataPageVersions(), List.of(ParquetFooterReader.DATA_PAGE_V1));
        assertEquals(getProfile(fileMetadata, "name").getDataPageVersions(), List.of(ParquetFooterReader.DATA_PAGE_V2));
        assertTrue(getProfile(fileMetadata, "ts").getDataPageVersions().isEmpty());
    }


    /**
     * A file that is not in the Parquet format.
     */
    @Test public void testNotParquet()
    {
        validateFailure(new File(resourcesDirectory + "not-parquet.parquet"), ParquetFormatException.Reason.NOT_PARQUET);
    }


    /**
     * A file that is too short to hold the Parquet markers.
     *
     * @throws IOException test failed
     */
    @Test public void testShortFile() throws IOException
    {
        validateFailure(writeFile("PAR1PAR1".getBytes(StandardCharsets.US_ASCII)), ParquetFormatException.Reason.NOT_PARQUET);
    }


    /**
     * A Parquet file that has an encrypted footer.
     *
     * @throws IOException test failed
     */
    @Test public void testEncryptedFooter() throws IOException
    {
        validateFailure(writeFile("PARE", new byte[40], 40, "PARE"), ParquetFormatException.Reason.ENCRYPTED_FOOTER);
    }


    /**
     * Parquet files where the footer is not valid - because it is not a thrift structure, it does not contain a
     * schema, or because its length is longer than the file.
     *
     * @throws IOException test failed
     */
    @Test public void testCorruptFooter() throws IOException
    {
        byte[] randomBytes = new byte[40];

        for (int i = 0; i < randomBytes.length; i++)
        {
            randomBytes[i] = (byte) (0xf9 - i);
        }

        validateFailure(writeFile("PAR1", randomBytes, 40, "PAR1"), ParquetFormatException.Reason.CORRUPT_FOOTER);
        validateFailure(writeFile("PAR1", new byte[40], 40, "PAR1"), ParquetFormatException.Reason.CORRUPT_FOOTER);
        validateFailure(writeFile("PAR1", new byte[40], 4000, "PAR1"), ParquetFormatException.Reason.CORRUPT_FOOTER);
        validateFailure(writeFile("PAR1", new byte[40], 0, "PAR1"), ParquetFormatException.Reason.CORRUPT_FOOTER);
    }


    /**
     * A copy of a valid file with the end missing - as happens when a file is still being written or copied.
     *
     * @throws IOException test failed
     */
    @Test public void testTruncatedFile() throws IOException
    {
        byte[] validFile = Files.readAllBytes(new File(resourcesDirectory + "flat-v2-zstd.parquet").toPath());
        byte[] truncated = new byte[validFile.length - 100];

        System.arraycopy(validFile, 0, truncated, 0, truncated.length);

        validateFailure(writeFile(truncated), ParquetFormatException.Reason.NOT_PARQUET);
    }


    /**
     * Check that a file is rejected for the right reason.
     *
     * @param file file to read
     * @param expectedReason why it should fail
     */
    private void validateFailure(File                          file,
                                 ParquetFormatException.Reason expectedReason)
    {
        try
        {
            footerReader.readFooter(file);
            fail("The file should not have been accepted");
        }
        catch (ParquetFormatException error)
        {
            assertEquals(error.getReason(), expectedReason, error.getMessage());
            assertNotNull(error.getMessage());
        }
        catch (IOException error)
        {
            fail("Unexpected exception " + error);
        }
    }


    /**
     * Check the summary of a column's values.
     *
     * @param fileMetadata description from the reader
     * @param pathName which column
     * @param valueCount expected number of values
     * @param nullCount expected number of nulls
     * @param minValue expected minimum value
     * @param maxValue expected maximum value
     */
    private void validateProfile(ParquetFileMetadata fileMetadata,
                                 String              pathName,
                                 long                valueCount,
                                 Long                nullCount,
                                 String              minValue,
                                 String              maxValue)
    {
        ParquetColumnProfile columnProfile = getProfile(fileMetadata, pathName);

        assertEquals(columnProfile.getValueCount(), valueCount, pathName);
        assertEquals(columnProfile.getNullCount(), nullCount, pathName);
        assertEquals(columnProfile.getMinValue(), minValue, pathName);
        assertEquals(columnProfile.getMaxValue(), maxValue, pathName);
        assertFalse(columnProfile.isEncrypted());
    }


    /**
     * Return the profile of a column.
     *
     * @param fileMetadata description from the reader
     * @param pathName which column
     * @return profile
     */
    private ParquetColumnProfile getProfile(ParquetFileMetadata fileMetadata,
                                            String              pathName)
    {
        for (ParquetColumnProfile columnProfile : fileMetadata.getColumnProfiles())
        {
            if (pathName.equals(columnProfile.getColumn().getPathName()))
            {
                return columnProfile;
            }
        }

        fail("No profile for column " + pathName);
        return null;
    }


    /**
     * Return a top-level column.
     *
     * @param fileMetadata description from the reader
     * @param name which column
     * @return schema element
     */
    private ParquetSchemaElement getColumn(ParquetFileMetadata fileMetadata,
                                           String              name)
    {
        for (ParquetSchemaElement column : fileMetadata.getColumns())
        {
            if (name.equals(column.getName()))
            {
                return column;
            }
        }

        fail("No column called " + name);
        return null;
    }


    /**
     * Return the little endian encoding of an integer.
     *
     * @param value integer
     * @return bytes
     */
    private byte[] intBytes(int value)
    {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
    }


    /**
     * Create a file with the layout of a Parquet file.
     *
     * @param startMarker first four characters
     * @param footer content of the footer
     * @param footerLength the length to record for the footer
     * @param endMarker last four characters
     * @return file
     * @throws IOException unable to create file
     */
    private File writeFile(String startMarker,
                           byte[] footer,
                           int    footerLength,
                           String endMarker) throws IOException
    {
        ByteArrayOutputStream fileContent = new ByteArrayOutputStream();

        fileContent.writeBytes(startMarker.getBytes(StandardCharsets.US_ASCII));
        fileContent.writeBytes(footer);
        fileContent.writeBytes(intBytes(footerLength));
        fileContent.writeBytes(endMarker.getBytes(StandardCharsets.US_ASCII));

        return writeFile(fileContent.toByteArray());
    }


    /**
     * Create a temporary file.
     *
     * @param fileContent content of the file
     * @return file
     * @throws IOException unable to create file
     */
    private File writeFile(byte[] fileContent) throws IOException
    {
        File file = Files.createTempFile("ParquetFooterReaderTest", ".parquet").toFile();

        file.deleteOnExit();
        Files.write(file.toPath(), fileContent);

        return file;
    }
}
