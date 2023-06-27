/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

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
 * The Type object holds properties that are used for displaying the Open Metadata Types
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Type implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The type name
     * -- GETTER --
     * Returns the type name
     * @return the type name
     * -- SETTER --
     * Set up the type name
     * @param name the type name
     */
    private String name;

    /**
     * The description of the type
     * -- GETTER --
     * Returns the description of the type
     * @return the description of the type
     * -- SETTER --
     * Set up the description of the type
     * @param description the description of the type
     */
    private String description;

    /**
     * The version of the type
     * -- GETTER --
     * Returns the version of the type
     * @return the version of the type
     * -- SETTER --
     * Set up the version of the type
     * @param version the version of the type
     */
    private Long version;

    /**
     * The super type of the current type
     * -- GETTER --
     * Returns the super type of the current type
     * @return the super type of the current type
     * -- SETTER --
     * Set up the super type of the current type
     * @param superType the super type of the current type
     */
    private String superType;
}
