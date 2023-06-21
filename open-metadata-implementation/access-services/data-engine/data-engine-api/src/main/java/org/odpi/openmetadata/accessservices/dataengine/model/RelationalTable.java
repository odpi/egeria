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

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationalTable is a java bean used to create RelationalTable associated with the external data engine.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
public class RelationalTable extends Referenceable {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    /**
     * The display name of the relational table
     * -- GETTER --
     * Returns the display name of the relational table.
     * @return display name
     * -- SETTER --
     * Sets up the display name of the relational table.
     * @param displayName display name
     */
    private String displayName;

    /**
     * The type of the relational table
     * -- GETTER --
     * Returns the type of the relational table.
     * @return type
     * -- SETTER --
     * Sets up the type of the relational table.
     * @param type type
     */
    private String type;

    /**
     * The list of aliases of the relational table
     * -- GETTER --
     * Returns list of aliases of the relational table.
     * @return aliases
     * -- SETTER --
     * Sets up the list of aliases of the relational table.
     * @param aliases aliases
     */
    private List<String> aliases;

    /**
     * Determines if the relational table if deprecated
     * -- GETTER --
     * Returns if the relational table if deprecated.
     * @return if the relational table if deprecated
     * -- SETTER --
     * Sets up the value that determines if the relational table if deprecated.
     * @param isDeprecated value saying if the relational table if deprecated
     */
    private boolean isDeprecated;

    /**
     * The description of the relational table
     * -- GETTER --
     * Returns the description of the relational table.
     * @return description
     * -- SETTER --
     * Sets up the description of the relational table.
     * @param description description
     */
    private String description;

    /**
     * The list of columns of the relational table
     * -- GETTER --
     * Returns list of columns of the relational table.
     * @return columns
     * -- SETTER --
     * Sets up the list of columns of the relational table.
     * @param columns columns
     */
    private List<RelationalColumn> columns;

    /**
     * Determines if the table is incomplete
     * -- GETTER --
     * Return if the table is incomplete
     *
     * @return if the table is incomplete
     * -- SETTER --
     * Sets up if the table is incomplete
     * @param incomplete if the table is incomplete
     */
    @JsonProperty("incomplete")
    private boolean incomplete;
}
