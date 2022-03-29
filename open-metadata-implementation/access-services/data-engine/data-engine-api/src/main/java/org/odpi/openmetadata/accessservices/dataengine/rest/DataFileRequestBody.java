/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class DataFileRequestBody extends DataEngineOMASAPIRequestBody {

    /**
     * The data file to be created
     * -- GETTER --
     * Return the data file bean
     *
     * @return the data file
     * -- SETTER --
     * Set up the data file bean
     * @param dataFile the data file
     */
    @JsonProperty("file")
    private DataFile dataFile;

    /**
     * Determines if the data file is incomplete or not
     * -- GETTER --
     * Returns if the data file is incomplete or not
     * @return if the data file is incomplete or not
     *
     * -- SETTER --
     * Set up the value that determines if the data file is incomplete or not
     * @param incomplete the value that determines if the data file is incomplete or not
     */
    @JsonProperty("incomplete")
    private boolean incomplete;
}