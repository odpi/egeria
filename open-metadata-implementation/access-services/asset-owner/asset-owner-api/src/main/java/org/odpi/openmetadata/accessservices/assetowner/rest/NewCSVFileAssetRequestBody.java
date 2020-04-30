/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewCSVFileAssetRequestBody carries the parameters for creating a new CSV file asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewCSVFileAssetRequestBody extends NewFileAssetRequestBody
{
    private static final long    serialVersionUID = 1L;

    private List<String> columnHeaders      = null;
    private Character    delimiterCharacter = null;
    private Character    quoteCharacter     = null;


    /**
     * Default constructor
     */
    public NewCSVFileAssetRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewCSVFileAssetRequestBody(NewCSVFileAssetRequestBody template)
    {
        super(template);

        if (template != null)
        {
            columnHeaders = template.getColumnHeaders();
            delimiterCharacter = template.getDelimiterCharacter();
            quoteCharacter = template.getQuoteCharacter();
        }
    }


    /**
     * Return the list of column headers for the data set - this is used if the columns are not
     * listed in the first line of the file.
     *
     * @return list of column names
     */
    public List<String> getColumnHeaders()
    {
        return columnHeaders;
    }


    /**
     * Set up he list of column headers for the data set - this is used if the columns are not
     * listed in the first line of the file.
     *
     * @param columnHeaders list of column names
     */
    public void setColumnHeaders(List<String> columnHeaders)
    {
        this.columnHeaders = columnHeaders;
    }


    /**
     * Return the delimiter character used between the columns.  Null means used the default of comma.
     *
     * @return character
     */
    public Character getDelimiterCharacter()
    {
        return delimiterCharacter;
    }


    /**
     * Set up the delimiter character used between the columns.  Null means used the default of comma.
     *
     * @param delimiterCharacter character
     */
    public void setDelimiterCharacter(Character delimiterCharacter)
    {
        this.delimiterCharacter = delimiterCharacter;
    }


    /**
     * Return the character used to group the content of a column that contains one or more delimiter characters.
     * Null means the quote character is a double quote.
     *
     * @return character
     */
    public Character getQuoteCharacter()
    {
        return quoteCharacter;
    }


    /**
     * Set up he character used to group the content of a column that contains one or more delimiter characters.
     * Null means the quote character is a double quote.
     *
     * @param quoteCharacter character
     */
    public void setQuoteCharacter(Character quoteCharacter)
    {
        this.quoteCharacter = quoteCharacter;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NewCSVFileAssetRequestBody{" +
                "displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", fullPath='" + getFullPath() + '\'' +
                ", columnHeaders=" + columnHeaders +
                ", delimiterCharacter=" + delimiterCharacter +
                ", quoteCharacter=" + quoteCharacter +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        NewCSVFileAssetRequestBody that = (NewCSVFileAssetRequestBody) objectToCompare;
        return  Objects.equals(getColumnHeaders(), that.getColumnHeaders()) &&
                Objects.equals(getDelimiterCharacter(), that.getDelimiterCharacter()) &&
                Objects.equals(getQuoteCharacter(), that.getQuoteCharacter());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getDisplayName(), getDescription(), getFullPath(), getColumnHeaders(),
                            getDelimiterCharacter(), getQuoteCharacter());
    }
}