/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CSVFile is a java bean used to create CSVFiles associated with the external data engine.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class CSVFile extends DataFile {

    /**
     * The delimited character
     * -- GETTER --
     * Get delimited character
     * @return delimiter character
     * -- SETTER --
     * Set delimited character
     * @param delimiterCharacter delimiter character
     */
    protected String delimiterCharacter;

    /**
     * The quote character
     * -- GETTER --
     * Get quote character
     * @return quote character
     * -- SETTER --
     * Set quote character
     * @param quoteCharacter quote character
     */
    protected String quoteCharacter;

}
