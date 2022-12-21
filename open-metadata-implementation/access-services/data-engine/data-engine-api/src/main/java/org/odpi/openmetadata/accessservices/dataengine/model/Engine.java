/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Engine.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Engine implements Serializable {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
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
     * The display name
     * -- GETTER --
     * Gets display name.
     * @return the display name
     * -- SETTER --
     * Sets display name.
     * @param name the display name
     */
    @JsonProperty("displayName")
    private String name;

    /**
     * The description
     * -- GETTER --
     * Gets description.
     * @return the description
     * -- SETTER --
     * Sets description.
     * @param description the description
     */
    private String description;

    /**
     * The engine type
     * -- GETTER --
     * Gets engine type.
     * @return the engine type
     * -- SETTER --
     * Sets engine type.
     * @param engineType the engine type
     */
    private String engineType;

    /**
     * The engine version
     * -- GETTER --
     * Gets engine version.
     * @return the engine version
     * -- SETTER --
     * Sets engine version.
     * @param engineVersion the engine version
     */
    private String engineVersion;

    /**
     * The patch level
     * -- GETTER --
     * Gets patch level.
     * @return the patch level
     * -- SETTER --
     * Sets patch level.
     * @param patchLevel the patch level
     */
    private String patchLevel;

    /**
     * The source
     * -- GETTER --
     * Gets source.
     * @return the source
     * -- SETTER --
     * Sets source.
     * @param source the source
     */
    private String source;

    /**
     * Additional properties
     * -- GETTER --
     * Gets the additional properties
     * @return the additional properties
     * -- SETTER --
     * Sets the additional properties.
     * @param additionalProperties the additional properties
     */
    private Map<String, String> additionalProperties;

}
