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
 * The type Lineage mapping.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class LineageMapping implements Serializable {

    /**
     * The source attribute
     * -- GETTER --
     * Gets source attribute.
     * @return the source attribute
     * -- SETTER --
     * Sets source attribute.
     * @param sourceAttribute the source attribute
     */
    private String sourceAttribute;

    /**
     * The target attribute
     * -- GETTER --
     * Gets target attribute.
     * @return the target attribute
     * -- SETTER --
     * Sets target attribute.
     * @param targetAttribute the target attribute
     */
    private String targetAttribute;

}
