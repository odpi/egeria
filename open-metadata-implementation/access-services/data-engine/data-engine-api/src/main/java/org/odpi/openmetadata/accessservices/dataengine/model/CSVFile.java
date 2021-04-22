/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OM type CSVFile
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSVFile extends DataFile{

    protected String delimiterCharacter;
    protected String quoteCharacter;

    /**
     * Get delimited character
     *
     * @return  delimiter character
     */
    public String getDelimiterCharacter() {
        return delimiterCharacter;
    }

    /**
     * Set delimited character
     *
     * @param delimiterCharacter
     */
    public void setDelimiterCharacter(String delimiterCharacter) {
        this.delimiterCharacter = delimiterCharacter;
    }

    /**
     * Get quote character
     *
     * @return quote character
     */
    public String getQuoteCharacter() {
        return quoteCharacter;
    }

    /**
     * Set quote character
     *
     * @param quoteCharacter
     */
    public void setQuoteCharacter(String quoteCharacter) {
        this.quoteCharacter = quoteCharacter;
    }

    @Override
    public String toString() {
        return "CSVFile{" +
                "delimiterCharacter='" + delimiterCharacter + "'" +
                "quoteCharacter='" + quoteCharacter + "'" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CSVFile csvFile = (CSVFile) o;
        return Objects.equals(delimiterCharacter, csvFile.delimiterCharacter) &&
                Objects.equals(quoteCharacter, csvFile.quoteCharacter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), delimiterCharacter, quoteCharacter);
    }

}
