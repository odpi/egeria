/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.footer;

import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetColumnChunk;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetColumnProfile;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetFileMetadata;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetRowGroup;
import org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.properties.ParquetSchemaElement;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ParquetFooterReader extracts the description of a Parquet file from its footer.
 * <br><br>
 * A Parquet file begins and ends with the four characters PAR1.  Immediately before the final marker is the length
 * of the footer, and before that is the footer itself.  The footer is a structure called FileMetaData, defined in
 * <a href="https://github.com/apache/parquet-format/blob/master/src/main/thrift/parquet.thrift">parquet.thrift</a>,
 * that is serialized using the Thrift compact protocol.  It describes the schema, the number of rows, where the
 * values for each column are located, how they are encoded and compressed, and statistics about them.
 * <br><br>
 * This layout has not changed since the first release of Parquet.  Each new version of the format has added
 * optional fields to the footer, and new ways to lay out, encode and compress the pages of values that it
 * points to.  Since this reader only decodes the footer, it works with files written to any version of the
 * format, using any encoding or compression algorithm.  It copes with the differences between the versions
 * that do show up in the footer as follows:
 * <ul>
 *     <li>Both the original converted type annotations and the newer logical type annotations are understood.</li>
 *     <li>The minimum and maximum values are taken from the newer statistics fields where they are present.
 *     The original fields are used only for the types of column where they are known to be correct.</li>
 *     <li>Both the standard layouts for lists and maps, and the legacy ones, are recognized.</li>
 *     <li>The version of the data page layout is taken from the footer where it is recorded.  If it is not, the
 *     header of the first data page of each column is read to discover it.</li>
 *     <li>Fields that are not recognized are ignored.</li>
 * </ul>
 */
public class ParquetFooterReader
{
    private static final String PARQUET_MARKER           = "PAR1";
    private static final String ENCRYPTED_FOOTER_MARKER  = "PARE";
    private static final int    MARKER_LENGTH            = 4;
    private static final int    FOOTER_LENGTH_LENGTH     = 4;
    private static final int    MINIMUM_FILE_LENGTH      = MARKER_LENGTH + FOOTER_LENGTH_LENGTH + MARKER_LENGTH;

    /*
     * The amount of a page to read in order to decode its header.  A header is typically less than 100 bytes,
     * but it can include the minimum and maximum values of the page.
     */
    private static final int    PAGE_HEADER_READ_LENGTH  = 16 * 1024;

    /*
     * The number of pages at the start of a column chunk to look through to find the first data page.
     */
    private static final int    MAX_PAGES_TO_INSPECT     = 3;

    private static final int    PAGE_TYPE_DATA_PAGE      = 0;
    private static final int    PAGE_TYPE_DATA_PAGE_V2   = 3;

    /**
     * Name used for the original data page layout.
     */
    public static final String  DATA_PAGE_V1             = "V1";

    /**
     * Name used for the data page layout introduced in version 2 of the format.
     */
    public static final String  DATA_PAGE_V2             = "V2";

    /*
     * The names of the values in the enums defined in parquet.thrift.  The position in the array is the enum's value.
     */
    private static final String[] physicalTypes     = {"BOOLEAN", "INT32", "INT64", "INT96", "FLOAT", "DOUBLE", "BYTE_ARRAY", "FIXED_LEN_BYTE_ARRAY"};
    private static final String[] repetitionTypes   = {"REQUIRED", "OPTIONAL", "REPEATED"};
    private static final String[] convertedTypes    = {"UTF8", "MAP", "MAP_KEY_VALUE", "LIST", "ENUM", "DECIMAL", "DATE", "TIME_MILLIS", "TIME_MICROS",
                                                       "TIMESTAMP_MILLIS", "TIMESTAMP_MICROS", "UINT_8", "UINT_16", "UINT_32", "UINT_64", "INT_8",
                                                       "INT_16", "INT_32", "INT_64", "JSON", "BSON", "INTERVAL"};
    private static final String[] logicalTypes      = {null, "STRING", "MAP", "LIST", "ENUM", "DECIMAL", "DATE", "TIME", "TIMESTAMP", null, "INTEGER",
                                                       "UNKNOWN", "JSON", "BSON", "UUID", "FLOAT16", "VARIANT", "GEOMETRY", "GEOGRAPHY"};
    private static final String[] timeUnits         = {null, "MILLIS", "MICROS", "NANOS"};
    private static final String[] encodings         = {"PLAIN", null, "PLAIN_DICTIONARY", "RLE", "BIT_PACKED", "DELTA_BINARY_PACKED",
                                                       "DELTA_LENGTH_BYTE_ARRAY", "DELTA_BYTE_ARRAY", "RLE_DICTIONARY", "BYTE_STREAM_SPLIT"};
    private static final String[] compressionCodecs = {"UNCOMPRESSED", "SNAPPY", "GZIP", "LZO", "BROTLI", "LZ4", "ZSTD", "LZ4_RAW"};
    private static final String[] encryptionAlgorithms = {null, "AES_GCM_V1", "AES_GCM_CTR_V1"};


    /**
     * Default constructor.
     */
    public ParquetFooterReader()
    {
    }


    /**
     * Read the footer of a Parquet file.
     *
     * @param file file to read
     * @return description of the file
     * @throws ParquetFormatException the file is not a Parquet file that can be described
     * @throws IOException the file could not be read
     */
    public ParquetFileMetadata readFooter(File file) throws IOException
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r"))
        {
            long fileLength = randomAccessFile.length();

            if (fileLength < MINIMUM_FILE_LENGTH)
            {
                throw new ParquetFormatException(ParquetFormatException.Reason.NOT_PARQUET,
                                                 "the file is only " + fileLength + " bytes long, which is too short to be a Parquet file");
            }

            byte[] header  = new byte[MARKER_LENGTH];
            byte[] trailer = new byte[FOOTER_LENGTH_LENGTH + MARKER_LENGTH];

            randomAccessFile.readFully(header);
            randomAccessFile.seek(fileLength - trailer.length);
            randomAccessFile.readFully(trailer);

            String startMarker = new String(header, StandardCharsets.US_ASCII);
            String endMarker   = new String(trailer, FOOTER_LENGTH_LENGTH, MARKER_LENGTH, StandardCharsets.US_ASCII);

            if (ENCRYPTED_FOOTER_MARKER.equals(endMarker))
            {
                throw new ParquetFormatException(ParquetFormatException.Reason.ENCRYPTED_FOOTER,
                                                 "the file ends with the " + ENCRYPTED_FOOTER_MARKER + " marker");
            }

            if ((! PARQUET_MARKER.equals(startMarker)) || (! PARQUET_MARKER.equals(endMarker)))
            {
                throw new ParquetFormatException(ParquetFormatException.Reason.NOT_PARQUET,
                                                 "the file does not begin and end with the " + PARQUET_MARKER + " marker");
            }

            long footerLength = (trailer[0] & 0xffL) |
                                ((trailer[1] & 0xffL) << 8) |
                                ((trailer[2] & 0xffL) << 16) |
                                ((trailer[3] & 0xffL) << 24);

            if ((footerLength == 0) || (footerLength > Integer.MAX_VALUE) || (footerLength > (fileLength - MINIMUM_FILE_LENGTH)))
            {
                throw new ParquetFormatException(ParquetFormatException.Reason.CORRUPT_FOOTER,
                                                 "the footer length of " + footerLength + " bytes is not valid for a file of " + fileLength + " bytes");
            }

            byte[] footer = new byte[(int) footerLength];

            randomAccessFile.seek(fileLength - trailer.length - footerLength);
            randomAccessFile.readFully(footer);

            ParquetFileMetadata fileMetadata;

            try
            {
                fileMetadata = this.parseFooter(footer);
            }
            catch (ParquetFormatException error)
            {
                throw error;
            }
            catch (IOException | RuntimeException error)
            {
                /*
                 * The RuntimeException is typically a ClassCastException because a field has an unexpected type.
                 */
                throw new ParquetFormatException(ParquetFormatException.Reason.CORRUPT_FOOTER,
                                                 "the footer could not be decoded (" + error.getClass().getSimpleName() + ": " + error.getMessage() + ")");
            }

            fileMetadata.setFileLength(fileLength);
            fileMetadata.setFooterLength((int) footerLength);

            if (! fileMetadata.getRowGroups().isEmpty())
            {
                this.inspectDataPages(randomAccessFile, fileLength, fileMetadata.getRowGroups().get(0));
            }

            this.buildColumnProfiles(fileMetadata);

            return fileMetadata;
        }
    }


    /**
     * Convert the content of the footer into the description of the file.
     *
     * @param footer bytes from the file
     * @return description of the file (without the column profiles)
     * @throws IOException the footer is not valid
     */
    private ParquetFileMetadata parseFooter(byte[] footer) throws IOException
    {
        Map<Integer, Object> footerFields = new ThriftCompactReader(footer).readStruct();

        ParquetFileMetadata fileMetadata = new ParquetFileMetadata();

        fileMetadata.setFormatVersion(getInt(footerFields, 1, 0));
        fileMetadata.setRowCount(getLong(footerFields, 3, 0L));
        fileMetadata.setCreatedBy(getString(footerFields, 6));

        Map<Integer, Object> encryptionAlgorithm = getStruct(footerFields, 8);

        if (encryptionAlgorithm != null)
        {
            fileMetadata.setEncryptionAlgorithm(getUnionName(encryptionAlgorithm, encryptionAlgorithms));
        }

        /*
         * The schema is a tree that is stored as a list, in depth first order, with each group recording
         * how many children it has.
         */
        List<Object> schemaList = getList(footerFields, 2);

        if (schemaList.isEmpty())
        {
            throw new ParquetFormatException(ParquetFormatException.Reason.CORRUPT_FOOTER, "the footer does not include a schema");
        }

        int[] nextSchemaIndex = {0};

        fileMetadata.setRootSchemaElement(this.getSchemaElement(schemaList, nextSchemaIndex, null, 0));

        Map<String, ParquetSchemaElement> leafColumns = new HashMap<>();

        this.addLeafColumns(fileMetadata.getRootSchemaElement(), leafColumns);

        for (Object keyValueObject : getList(footerFields, 5))
        {
            Map<Integer, Object> keyValue = asStruct(keyValueObject);
            String               key      = getString(keyValue, 1);

            if (key != null)
            {
                fileMetadata.getKeyValueMetadata().put(key, getString(keyValue, 2));
            }
        }

        for (Object rowGroupObject : getList(footerFields, 4))
        {
            Map<Integer, Object> rowGroupFields = asStruct(rowGroupObject);
            ParquetRowGroup      rowGroup       = new ParquetRowGroup();

            rowGroup.setUncompressedSize(getLong(rowGroupFields, 2, 0L));
            rowGroup.setRowCount(getLong(rowGroupFields, 3, 0L));

            long compressedSize = 0L;

            for (Object columnChunkObject : getList(rowGroupFields, 1))
            {
                ParquetColumnChunk columnChunk = this.getColumnChunk(asStruct(columnChunkObject), leafColumns);

                compressedSize = compressedSize + columnChunk.getCompressedSize();
                rowGroup.getColumnChunks().add(columnChunk);
            }

            /*
             * The compressed size of the row group was added in version 2.  Using the sum from the column chunks
             * works for all versions.
             */
            rowGroup.setCompressedSize(compressedSize);

            fileMetadata.getRowGroups().add(rowGroup);
        }

        return fileMetadata;
    }


    /**
     * Build the description of a field in the schema, and the fields nested within it.
     *
     * @param schemaList the flattened schema from the footer
     * @param nextSchemaIndex the position in the list of the field to build - updated to the position after the field
     *                        and all its nested fields
     * @param parentPathName the path name of the group that this field belongs to (null for the root and top-level fields)
     * @param depth number of groups that this field is nested in
     * @return description of the field
     * @throws IOException the schema is not valid
     */
    private ParquetSchemaElement getSchemaElement(List<Object> schemaList,
                                                  int[]        nextSchemaIndex,
                                                  String       parentPathName,
                                                  int          depth) throws IOException
    {
        if (nextSchemaIndex[0] >= schemaList.size())
        {
            throw new ParquetFormatException(ParquetFormatException.Reason.CORRUPT_FOOTER,
                                             "the schema has fewer fields than its groups say they contain");
        }

        Map<Integer, Object> fields        = asStruct(schemaList.get(nextSchemaIndex[0]));
        ParquetSchemaElement schemaElement = new ParquetSchemaElement();

        nextSchemaIndex[0]++;

        schemaElement.setName(getString(fields, 4));
        schemaElement.setPhysicalType(getEnumName(fields, 1, physicalTypes));
        schemaElement.setTypeLength(getInteger(fields, 2));
        schemaElement.setRepetition(getEnumName(fields, 3, repetitionTypes));
        schemaElement.setConvertedType(getEnumName(fields, 6, convertedTypes));
        schemaElement.setScale(getInteger(fields, 7));
        schemaElement.setPrecision(getInteger(fields, 8));
        schemaElement.setFieldId(getInteger(fields, 9));

        this.setLogicalType(schemaElement, getStruct(fields, 10));

        if (depth == 0)
        {
            /*
             * The root is not part of the path name of the columns.
             */
            schemaElement.setPathName(null);
        }
        else if (parentPathName == null)
        {
            schemaElement.setPathName(schemaElement.getName());
        }
        else
        {
            schemaElement.setPathName(parentPathName + "." + schemaElement.getName());
        }

        int childCount = getInt(fields, 5, 0);

        if ((childCount > 0) && (schemaElement.getPhysicalType() == null))
        {
            for (int i = 0; i < childCount; i++)
            {
                schemaElement.getChildren().add(this.getSchemaElement(schemaList, nextSchemaIndex, schemaElement.getPathName(), depth + 1));
            }
        }

        return schemaElement;
    }


    /**
     * Add the details from the logical type annotation to the description of a field.  The logical type
     * is a union which means it is a structure with a single field.  The identifier of the field says which logical type
     * it is, and the value of the field is a structure holding any parameters for the logical type.
     *
     * @param schemaElement description of the field
     * @param logicalType union from the footer (may be null)
     */
    private void setLogicalType(ParquetSchemaElement schemaElement,
                                Map<Integer, Object> logicalType)
    {
        if ((logicalType == null) || (logicalType.isEmpty()))
        {
            return;
        }

        int                  logicalTypeId   = logicalType.keySet().iterator().next();
        String               logicalTypeName = getName(logicalTypeId, logicalTypes);
        Map<Integer, Object> parameters      = getStruct(logicalType, logicalTypeId);
        String               description     = logicalTypeName;

        if (parameters != null)
        {
            switch (logicalTypeName)
            {
                case "DECIMAL" ->
                {
                    Integer scale     = getInteger(parameters, 1);
                    Integer precision = getInteger(parameters, 2);

                    if (scale != null)
                    {
                        schemaElement.setScale(scale);
                    }
                    if (precision != null)
                    {
                        schemaElement.setPrecision(precision);
                    }

                    description = logicalTypeName + "(" + precision + "," + scale + ")";
                }
                case "TIME", "TIMESTAMP" ->
                {
                    boolean              adjustedToUTC = Boolean.TRUE.equals(parameters.get(1));
                    Map<Integer, Object> unit          = getStruct(parameters, 2);
                    String               unitName      = (unit == null) ? null : getUnionName(unit, timeUnits);

                    schemaElement.setAdjustedToUTC(adjustedToUTC);
                    schemaElement.setTimeUnit(unitName);

                    description = logicalTypeName + "(" + unitName + ", " + (adjustedToUTC ? "UTC" : "local") + ")";
                }
                case "INTEGER" ->
                {
                    boolean signed = Boolean.TRUE.equals(parameters.get(2));

                    description = logicalTypeName + "(" + getInt(parameters, 1, 0) + ", " + (signed ? "signed" : "unsigned") + ")";
                }
                default -> { }
            }
        }

        schemaElement.setLogicalTypeName(logicalTypeName);
        schemaElement.setLogicalType(description);
    }


    /**
     * Build a map of the leaf columns, keyed by their path name.
     *
     * @param schemaElement field in the schema
     * @param leafColumns map to add to
     */
    private void addLeafColumns(ParquetSchemaElement              schemaElement,
                                Map<String, ParquetSchemaElement> leafColumns)
    {
        if (schemaElement.isLeaf())
        {
            leafColumns.put(schemaElement.getPathName(), schemaElement);
        }

        for (ParquetSchemaElement child : schemaElement.getChildren())
        {
            this.addLeafColumns(child, leafColumns);
        }
    }


    /**
     * Build the description of the values stored for a column in a row group.
     *
     * @param columnChunkFields the ColumnChunk structure from the footer
     * @param leafColumns the columns from the schema
     * @return description of the column chunk
     */
    private ParquetColumnChunk getColumnChunk(Map<Integer, Object>              columnChunkFields,
                                              Map<String, ParquetSchemaElement> leafColumns)
    {
        ParquetColumnChunk   columnChunk = new ParquetColumnChunk();
        Map<Integer, Object> metadata    = getStruct(columnChunkFields, 3);

        if (metadata == null)
        {
            /*
             * The description of the column chunk is missing because it is encrypted.  The path name is
             * available from the details of the encryption key.
             */
            columnChunk.setEncrypted(true);

            Map<Integer, Object> cryptoMetadata = getStruct(columnChunkFields, 8);

            if (cryptoMetadata != null)
            {
                Map<Integer, Object> columnKey = getStruct(cryptoMetadata, 2);

                if (columnKey != null)
                {
                    columnChunk.setPathName(getPathName(getList(columnKey, 1)));
                }
            }

            return columnChunk;
        }

        columnChunk.setPathName(getPathName(getList(metadata, 3)));
        columnChunk.setCompressionCodec(getEnumName(metadata, 4, compressionCodecs));
        columnChunk.setValueCount(getLong(metadata, 5, 0L));
        columnChunk.setUncompressedSize(getLong(metadata, 6, 0L));
        columnChunk.setCompressedSize(getLong(metadata, 7, 0L));
        columnChunk.setDataPageOffset(getLong(metadata, 9, 0L));
        columnChunk.setDictionaryPagePresent(metadata.get(11) != null);
        columnChunk.setBloomFilterPresent(metadata.get(14) != null);

        for (Object encoding : getList(metadata, 2))
        {
            addMissing(columnChunk.getEncodings(), getName(((Long) encoding).intValue(), encodings));
        }

        /*
         * The encoding statistics list each combination of page type and encoding used.  This is where the
         * version of the data page layout can be found.  The field was added in 2015 and not all writers supply it.
         */
        for (Object encodingStatsObject : getList(metadata, 13))
        {
            Map<Integer, Object> encodingStats = asStruct(encodingStatsObject);
            int                  pageType      = getInt(encodingStats, 1, -1);

            if (pageType == PAGE_TYPE_DATA_PAGE)
            {
                addMissing(columnChunk.getDataPageVersions(), DATA_PAGE_V1);
            }
            else if (pageType == PAGE_TYPE_DATA_PAGE_V2)
            {
                addMissing(columnChunk.getDataPageVersions(), DATA_PAGE_V2);
            }
        }

        Map<Integer, Object> statistics = getStruct(metadata, 12);
        ParquetSchemaElement column     = leafColumns.get(columnChunk.getPathName());

        if (statistics != null)
        {
            columnChunk.setNullCount(getLongObject(statistics, 3));
            columnChunk.setDistinctCount(getLongObject(statistics, 4));

            if (column != null)
            {
                /*
                 * Fields 5 and 6 are the minimum and maximum values calculated using the correct ordering for
                 * the type of the column.  Fields 1 and 2 are the original (now deprecated) fields that were
                 * calculated using a signed comparison whatever the type of the column.
                 */
                byte[] maxValue = getBytes(statistics, 5);
                byte[] minValue = getBytes(statistics, 6);

                if ((maxValue == null) && (minValue == null) && (ParquetStatisticsDecoder.usesSignedOrder(column)))
                {
                    maxValue = getBytes(statistics, 1);
                    minValue = getBytes(statistics, 2);
                }

                columnChunk.setMaxValue(ParquetStatisticsDecoder.decode(maxValue, column));
                columnChunk.setMinValue(ParquetStatisticsDecoder.decode(minValue, column));
            }
        }

        return columnChunk;
    }


    /**
     * Discover which version of the data page layout is used for each column in a row group, where this
     * is not recorded in the footer, by reading the header of the first data page.  Any problem reading
     * a page header is ignored - it just means that the data page version is not known for that column.
     *
     * @param randomAccessFile open file
     * @param fileLength length of the file
     * @param rowGroup row group to inspect
     */
    private void inspectDataPages(RandomAccessFile randomAccessFile,
                                  long             fileLength,
                                  ParquetRowGroup  rowGroup)
    {
        for (ParquetColumnChunk columnChunk : rowGroup.getColumnChunks())
        {
            if ((columnChunk.isEncrypted()) || (! columnChunk.getDataPageVersions().isEmpty()))
            {
                continue;
            }

            try
            {
                long pageOffset = columnChunk.getDataPageOffset();

                /*
                 * The offset of the first data page is the offset of the dictionary page in files from some
                 * older writers, so it may be necessary to step over a page to reach a data page.
                 */
                for (int i = 0; i < MAX_PAGES_TO_INSPECT; i++)
                {
                    if ((pageOffset < MARKER_LENGTH) || (pageOffset >= fileLength))
                    {
                        break;
                    }

                    byte[] pageHeaderBytes = new byte[(int) Math.min(PAGE_HEADER_READ_LENGTH, fileLength - pageOffset)];

                    randomAccessFile.seek(pageOffset);
                    randomAccessFile.readFully(pageHeaderBytes);

                    ThriftCompactReader  pageHeaderReader = new ThriftCompactReader(pageHeaderBytes);
                    Map<Integer, Object> pageHeader       = pageHeaderReader.readStruct();
                    int                  pageType         = getInt(pageHeader, 1, -1);

                    if (pageType == PAGE_TYPE_DATA_PAGE)
                    {
                        columnChunk.getDataPageVersions().add(DATA_PAGE_V1);
                        break;
                    }
                    else if (pageType == PAGE_TYPE_DATA_PAGE_V2)
                    {
                        columnChunk.getDataPageVersions().add(DATA_PAGE_V2);
                        break;
                    }

                    pageOffset = pageOffset + pageHeaderReader.getPosition() + getLong(pageHeader, 3, 0L);
                }
            }
            catch (IOException | RuntimeException error)
            {
                /*
                 * The data page version for this column is unknown.
                 */
            }
        }
    }


    /**
     * Combine the details of each column from all the row groups.
     *
     * @param fileMetadata description of the file to update
     */
    private void buildColumnProfiles(ParquetFileMetadata fileMetadata)
    {
        Map<String, ParquetColumnProfile> columnProfiles = new HashMap<>();

        this.addColumnProfiles(fileMetadata.getRootSchemaElement(), fileMetadata.getColumnProfiles(), columnProfiles);

        /*
         * These are the columns where a value is missing from one of the row groups and so there is no value
         * for the file as a whole.
         */
        List<String> unknownNullCounts = new ArrayList<>();
        List<String> unknownRanges     = new ArrayList<>();

        Map<String, Comparable<?>> minValues = new HashMap<>();
        Map<String, Comparable<?>> maxValues = new HashMap<>();

        for (ParquetRowGroup rowGroup : fileMetadata.getRowGroups())
        {
            for (ParquetColumnChunk columnChunk : rowGroup.getColumnChunks())
            {
                String               pathName      = columnChunk.getPathName();
                ParquetColumnProfile columnProfile = columnProfiles.get(pathName);

                if (columnProfile == null)
                {
                    continue;
                }

                if (columnChunk.isEncrypted())
                {
                    columnProfile.setEncrypted(true);
                    unknownNullCounts.add(pathName);
                    unknownRanges.add(pathName);
                    continue;
                }

                columnProfile.setValueCount(columnProfile.getValueCount() + columnChunk.getValueCount());
                columnProfile.setCompressedSize(columnProfile.getCompressedSize() + columnChunk.getCompressedSize());
                columnProfile.setUncompressedSize(columnProfile.getUncompressedSize() + columnChunk.getUncompressedSize());

                addMissing(columnProfile.getCompressionCodecs(), columnChunk.getCompressionCodec());

                for (String encoding : columnChunk.getEncodings())
                {
                    addMissing(columnProfile.getEncodings(), encoding);
                }

                for (String dataPageVersion : columnChunk.getDataPageVersions())
                {
                    addMissing(columnProfile.getDataPageVersions(), dataPageVersion);
                }

                if (columnChunk.getNullCount() == null)
                {
                    unknownNullCounts.add(pathName);
                }
                else if (columnProfile.getNullCount() == null)
                {
                    columnProfile.setNullCount(columnChunk.getNullCount());
                }
                else
                {
                    columnProfile.setNullCount(columnProfile.getNullCount() + columnChunk.getNullCount());
                }

                if (fileMetadata.getRowGroups().size() == 1)
                {
                    columnProfile.setDistinctCount(columnChunk.getDistinctCount());
                }

                Comparable<?> minValue = (Comparable<?>) columnChunk.getMinValue();
                Comparable<?> maxValue = (Comparable<?>) columnChunk.getMaxValue();

                if ((minValue == null) || (maxValue == null))
                {
                    /*
                     * There is no minimum and maximum when all the values are null.  Otherwise, the missing
                     * values mean that the range of the values in the file is not known.
                     */
                    if ((columnChunk.getNullCount() == null) || (columnChunk.getNullCount() != columnChunk.getValueCount()))
                    {
                        unknownRanges.add(pathName);
                    }
                }
                else
                {
                    minValues.put(pathName, ParquetStatisticsDecoder.min(minValues.get(pathName), minValue));
                    maxValues.put(pathName, ParquetStatisticsDecoder.max(maxValues.get(pathName), maxValue));
                }
            }
        }

        for (ParquetColumnProfile columnProfile : fileMetadata.getColumnProfiles())
        {
            String pathName = columnProfile.getColumn().getPathName();

            if (unknownNullCounts.contains(pathName))
            {
                columnProfile.setNullCount(null);
            }

            if ((! unknownRanges.contains(pathName)) && (minValues.get(pathName) != null) && (maxValues.get(pathName) != null))
            {
                columnProfile.setMinValue(minValues.get(pathName).toString());
                columnProfile.setMaxValue(maxValues.get(pathName).toString());
            }
        }
    }


    /**
     * Create an empty profile for each leaf column in the schema.
     *
     * @param schemaElement field in the schema
     * @param columnProfileList list to add the profile to - this is in the order of the schema
     * @param columnProfileMap map to add the profile to
     */
    private void addColumnProfiles(ParquetSchemaElement              schemaElement,
                                   List<ParquetColumnProfile>        columnProfileList,
                                   Map<String, ParquetColumnProfile> columnProfileMap)
    {
        if (schemaElement.isLeaf())
        {
            ParquetColumnProfile columnProfile = new ParquetColumnProfile(schemaElement);

            columnProfileList.add(columnProfile);
            columnProfileMap.put(schemaElement.getPathName(), columnProfile);
        }

        for (ParquetSchemaElement child : schemaElement.getChildren())
        {
            this.addColumnProfiles(child, columnProfileList, columnProfileMap);
        }
    }


    /*
     * ===========================
     * Methods to extract values from the decoded structures.
     */

    /**
     * Add a value to a list if it is not null and not already present.
     *
     * @param values list to update
     * @param value value to add
     */
    private void addMissing(List<String> values,
                            String       value)
    {
        if ((value != null) && (! values.contains(value)))
        {
            values.add(value);
        }
    }


    /**
     * Convert the list of names in a column's path into a single dot-separated name.
     *
     * @param pathInSchema list of names
     * @return path name
     */
    private String getPathName(List<Object> pathInSchema)
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object name : pathInSchema)
        {
            if (! stringBuilder.isEmpty())
            {
                stringBuilder.append(".");
            }

            stringBuilder.append(new String((byte[]) name, StandardCharsets.UTF_8));
        }

        return stringBuilder.toString();
    }


    /**
     * Return the name for an enum value.
     *
     * @param value value from the footer
     * @param names names of the enum's values
     * @return name
     */
    private String getName(int      value,
                           String[] names)
    {
        if ((value >= 0) && (value < names.length) && (names[value] != null))
        {
            return names[value];
        }

        /*
         * This value has been added to the format since this reader was written.
         */
        return "UNRECOGNIZED(" + value + ")";
    }


    /**
     * Return the name of an optional enum field.
     *
     * @param fields structure
     * @param fieldId identifier of the field
     * @param names names of the enum's values
     * @return name or null if the field is not present
     */
    private String getEnumName(Map<Integer, Object> fields,
                               int                  fieldId,
                               String[]             names)
    {
        Integer value = getInteger(fields, fieldId);

        if (value == null)
        {
            return null;
        }

        return getName(value, names);
    }


    /**
     * Return the name of the field that is set in a union.
     *
     * @param union structure with one field
     * @param names names of the fields in the union
     * @return name
     */
    private String getUnionName(Map<Integer, Object> union,
                                String[]             names)
    {
        if (union.isEmpty())
        {
            return null;
        }

        return getName(union.keySet().iterator().next(), names);
    }


    /**
     * Return an optional integer field.
     *
     * @param fields structure
     * @param fieldId identifier of the field
     * @return value or null
     */
    private Long getLongObject(Map<Integer, Object> fields,
                               int                  fieldId)
    {
        return (Long) fields.get(fieldId);
    }


    /**
     * Return an integer field.
     *
     * @param fields structure
     * @param fieldId identifier of the field
     * @param defaultValue value to use if the field is not present
     * @return value
     */
    private long getLong(Map<Integer, Object> fields,
                         int                  fieldId,
                         long                 defaultValue)
    {
        Long value = getLongObject(fields, fieldId);

        return (value == null) ? defaultValue : value;
    }


    /**
     * Return an optional integer field.
     *
     * @param fields structure
     * @param fieldId identifier of the field
     * @return value or null
     */
    private Integer getInteger(Map<Integer, Object> fields,
                               int                  fieldId)
    {
        Long value = getLongObject(fields, fieldId);

        return (value == null) ? null : value.intValue();
    }


    /**
     * Return an integer field.
     *
     * @param fields structure
     * @param fieldId identifier of the field
     * @param defaultValue value to use if the field is not present
     * @return value
     */
    private int getInt(Map<Integer, Object> fields,
                       int                  fieldId,
                       int                  defaultValue)
    {
        Integer value = getInteger(fields, fieldId);

        return (value == null) ? defaultValue : value;
    }


    /**
     * Return an optional binary field.
     *
     * @param fields structure
     * @param fieldId identifier of the field
     * @return value or null
     */
    private byte[] getBytes(Map<Integer, Object> fields,
                            int                  fieldId)
    {
        return (byte[]) fields.get(fieldId);
    }


    /**
     * Return an optional string field.
     *
     * @param fields structure
     * @param fieldId identifier of the field
     * @return value or null
     */
    private String getString(Map<Integer, Object> fields,
                             int                  fieldId)
    {
        byte[] value = getBytes(fields, fieldId);

        return (value == null) ? null : new String(value, StandardCharsets.UTF_8);
    }


    /**
     * Return a list field.
     *
     * @param fields structure
     * @param fieldId identifier of the field
     * @return list - which is empty if the field is not present
     */
    @SuppressWarnings("unchecked")
    private List<Object> getList(Map<Integer, Object> fields,
                                 int                  fieldId)
    {
        List<Object> value = (List<Object>) fields.get(fieldId);

        return (value == null) ? new ArrayList<>() : value;
    }


    /**
     * Return an optional nested structure.
     *
     * @param fields structure
     * @param fieldId identifier of the field
     * @return structure or null
     */
    private Map<Integer, Object> getStruct(Map<Integer, Object> fields,
                                           int                  fieldId)
    {
        return asStruct(fields.get(fieldId));
    }


    /**
     * Return a decoded value as a structure.
     *
     * @param value decoded value
     * @return structure
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, Object> asStruct(Object value)
    {
        return (Map<Integer, Object>) value;
    }
}
