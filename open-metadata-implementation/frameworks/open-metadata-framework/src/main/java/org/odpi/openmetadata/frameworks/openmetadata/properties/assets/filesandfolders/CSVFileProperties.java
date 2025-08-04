/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CSVFileProperties carries the parameters for maintaining a CSV file asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CSVFileProperties extends DataFileProperties
{
    private Character    delimiterCharacter = null;
    private Character    quoteCharacter     = null;


    /**
     * Default constructor
     */
    public CSVFileProperties()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CSVFileProperties(CSVFileProperties template)
    {
        super(template);

        if (template != null)
        {
            delimiterCharacter = template.getDelimiterCharacter();
            quoteCharacter = template.getQuoteCharacter();
        }
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
        return "CSVFileProperties{" +
                "delimiterCharacter=" + delimiterCharacter +
                ", quoteCharacter=" + quoteCharacter +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        CSVFileProperties that = (CSVFileProperties) objectToCompare;
        return Objects.equals(delimiterCharacter, that.delimiterCharacter) &&
                Objects.equals(quoteCharacter, that.quoteCharacter);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), delimiterCharacter, quoteCharacter);
    }
}
