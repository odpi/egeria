/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.odpi.openmetadata.accessservices.dataengine.rest.FindRequestBody;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The data file event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class FindEvent extends DataEngineEventHeader {

    /**
     * Serial version UID
     * -- GETTER --
     * Gets the serial version UID
     * @return the serial version UID
     * -- SETTER --
     * Sets the serial version UID
     * @param serialVersionUID the serial version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * The find request body
     * -- GETTER --
     * Return the find request body
     *
     * @return the find request body
     * -- SETTER --
     * Set up the find request body
     * @param findRequestBody the find request body
     */
    @JsonProperty("findRequestBody")
    private FindRequestBody findRequestBody;
}
