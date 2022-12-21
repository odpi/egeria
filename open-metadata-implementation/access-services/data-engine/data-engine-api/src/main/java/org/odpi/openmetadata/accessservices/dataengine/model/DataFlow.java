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

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type DataFlow
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DataFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The data supplier
     * -- GETTER --
     * Gets data supplier.
     * @return the data supplier
     * -- SETTER --
     * Sets data supplier
     * @param dataSupplier the data supplier
     */
    private String dataSupplier;

    /**
     * The data consumer
     * -- GETTER --
     * Gets data consumer
     * @return the data consumer
     * -- SETTER --
     * Sets data consumer
     * @param data consumer the data consumer
     */
    private String dataConsumer;

    private String formula;

    private String description;

}
