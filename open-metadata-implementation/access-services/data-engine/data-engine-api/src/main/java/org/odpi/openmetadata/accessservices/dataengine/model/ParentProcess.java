/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataFlow is a java bean used to create process hierarchies relationships.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ParentProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The qualified name
     * -- GETTER --
     * Gets qualified name.
     * @return the qualified name
     * -- SETTER --
     * Sets qualified name.
     * @param qualifiedName the qualified name
     */
    private String qualifiedName;

    /**
     * The process containment type
     * -- GETTER --
     * Gets process containment type.
     * @return the process containment type
     * -- SETTER --
     * Sets process containment type.
     * @param processContainmentType the process containment type
     */
    @JsonProperty("containmentType")
    private ProcessContainmentType processContainmentType;

}
