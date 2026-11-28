/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.surveyaction.surveyparquet;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ParquetFileMeasurement captures the measurements taken from the footer of a Parquet file, so that they can be
 * stored as JSON in the measurement annotation.  The properties match the names in ParquetFileMetric.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ParquetFileMeasurement
{
    private int          formatVersion       = 0;
    private String       createdBy           = null;
    private long         rowCount            = 0L;
    private int          rowGroupCount       = 0;
    private int          columnCount         = 0;
    private int          leafColumnCount     = 0;
    private int          footerSize          = 0;
    private long         compressedSize      = 0L;
    private long         uncompressedSize    = 0L;
    private List<String> compressionCodecs   = null;
    private List<String> encodings           = null;
    private List<String> dataPageVersions    = null;
    private String       typeAnnotationStyle = null;
    private String       encryptionAlgorithm = null;
    private List<String> writerPropertyNames = null;


    /**
     * Default constructor.
     */
    public ParquetFileMeasurement()
    {
    }


    /**
     * Return the version of the Parquet format declared by the writer of the file.
     *
     * @return version number
     */
    public int getFormatVersion()
    {
        return formatVersion;
    }


    /**
     * Set up the version of the Parquet format declared by the writer of the file.
     *
     * @param formatVersion version number
     */
    public void setFormatVersion(int formatVersion)
    {
        this.formatVersion = formatVersion;
    }


    /**
     * Return the description of the software that wrote the file.
     *
     * @return string
     */
    public String getCreatedBy()
    {
        return createdBy;
    }


    /**
     * Set up the description of the software that wrote the file.
     *
     * @param createdBy string
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    /**
     * Return the number of rows in the file.
     *
     * @return count
     */
    public long getRowCount()
    {
        return rowCount;
    }


    /**
     * Set up the number of rows in the file.
     *
     * @param rowCount count
     */
    public void setRowCount(long rowCount)
    {
        this.rowCount = rowCount;
    }


    /**
     * Return the number of row groups that the rows are divided into.
     *
     * @return count
     */
    public int getRowGroupCount()
    {
        return rowGroupCount;
    }


    /**
     * Set up the number of row groups that the rows are divided into.
     *
     * @param rowGroupCount count
     */
    public void setRowGroupCount(int rowGroupCount)
    {
        this.rowGroupCount = rowGroupCount;
    }


    /**
     * Return the number of top-level columns.
     *
     * @return count
     */
    public int getColumnCount()
    {
        return columnCount;
    }


    /**
     * Set up the number of top-level columns.
     *
     * @param columnCount count
     */
    public void setColumnCount(int columnCount)
    {
        this.columnCount = columnCount;
    }


    /**
     * Return the number of columns that hold values.
     *
     * @return count
     */
    public int getLeafColumnCount()
    {
        return leafColumnCount;
    }


    /**
     * Set up the number of columns that hold values.
     *
     * @param leafColumnCount count
     */
    public void setLeafColumnCount(int leafColumnCount)
    {
        this.leafColumnCount = leafColumnCount;
    }


    /**
     * Return the number of bytes in the footer.
     *
     * @return number of bytes
     */
    public int getFooterSize()
    {
        return footerSize;
    }


    /**
     * Set up the number of bytes in the footer.
     *
     * @param footerSize number of bytes
     */
    public void setFooterSize(int footerSize)
    {
        this.footerSize = footerSize;
    }


    /**
     * Return the number of bytes that the values occupy in the file.
     *
     * @return number of bytes
     */
    public long getCompressedSize()
    {
        return compressedSize;
    }


    /**
     * Set up the number of bytes that the values occupy in the file.
     *
     * @param compressedSize number of bytes
     */
    public void setCompressedSize(long compressedSize)
    {
        this.compressedSize = compressedSize;
    }


    /**
     * Return the number of bytes that the values occupy once they are decompressed.
     *
     * @return number of bytes
     */
    public long getUncompressedSize()
    {
        return uncompressedSize;
    }


    /**
     * Set up the number of bytes that the values occupy once they are decompressed.
     *
     * @param uncompressedSize number of bytes
     */
    public void setUncompressedSize(long uncompressedSize)
    {
        this.uncompressedSize = uncompressedSize;
    }


    /**
     * Return the compression algorithms used in the file.
     *
     * @return list
     */
    public List<String> getCompressionCodecs()
    {
        return compressionCodecs;
    }


    /**
     * Set up the compression algorithms used in the file.
     *
     * @param compressionCodecs list
     */
    public void setCompressionCodecs(List<String> compressionCodecs)
    {
        this.compressionCodecs = compressionCodecs;
    }


    /**
     * Return the encodings used in the file.
     *
     * @return list
     */
    public List<String> getEncodings()
    {
        return encodings;
    }


    /**
     * Set up the encodings used in the file.
     *
     * @param encodings list
     */
    public void setEncodings(List<String> encodings)
    {
        this.encodings = encodings;
    }


    /**
     * Return the versions of the data page layout used in the file.
     *
     * @return list
     */
    public List<String> getDataPageVersions()
    {
        return dataPageVersions;
    }


    /**
     * Set up the versions of the data page layout used in the file.
     *
     * @param dataPageVersions list
     */
    public void setDataPageVersions(List<String> dataPageVersions)
    {
        this.dataPageVersions = dataPageVersions;
    }


    /**
     * Return the style of annotation used to describe how the stored values are interpreted.
     *
     * @return string
     */
    public String getTypeAnnotationStyle()
    {
        return typeAnnotationStyle;
    }


    /**
     * Set up the style of annotation used to describe how the stored values are interpreted.
     *
     * @param typeAnnotationStyle string
     */
    public void setTypeAnnotationStyle(String typeAnnotationStyle)
    {
        this.typeAnnotationStyle = typeAnnotationStyle;
    }


    /**
     * Return the algorithm used to encrypt columns in the file.
     *
     * @return string or null
     */
    public String getEncryptionAlgorithm()
    {
        return encryptionAlgorithm;
    }


    /**
     * Set up the algorithm used to encrypt columns in the file.
     *
     * @param encryptionAlgorithm string or null
     */
    public void setEncryptionAlgorithm(String encryptionAlgorithm)
    {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }


    /**
     * Return the names of the additional properties that the writer added to the file.
     *
     * @return list
     */
    public List<String> getWriterPropertyNames()
    {
        return writerPropertyNames;
    }


    /**
     * Set up the names of the additional properties that the writer added to the file.
     *
     * @param writerPropertyNames list
     */
    public void setWriterPropertyNames(List<String> writerPropertyNames)
    {
        this.writerPropertyNames = writerPropertyNames;
    }
}
