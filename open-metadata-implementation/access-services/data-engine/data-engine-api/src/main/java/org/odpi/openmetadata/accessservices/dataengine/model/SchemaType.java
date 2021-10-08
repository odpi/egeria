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
 * The type Schema type.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
public class SchemaType extends Referenceable {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    /**
     * The display name
     * -- GETTER --
     * Gets display name.
     * @return the display name
     * -- SETTER --
     * Sets display name.
     * @param displayName the display name
     */
    private String displayName;

    /**
     * The author
     * -- GETTER --
     * Gets author.
     * @return the author
     * -- SETTER --
     * Sets author.
     * @param author the author
     */
    private String author;

    /**
     * The usage
     * -- GETTER --
     * Gets usage.
     * @return the usage
     * -- SETTER --
     * Sets usage.
     * @param usage the usage
     */
    private String usage;

    /**
     * The encoding standard
     * -- GETTER --
     * Gets encoding standard.
     * @return the encoding standard
     * -- SETTER --
     * Sets encoding standard.
     * @param encodingStandard the encoding standard
     */
    private String encodingStandard;

    /**
     * The version number
     * -- GETTER --
     * Gets version number.
     * @return the version number
     * -- SETTER --
     * Sets version number.
     * @param versionNumber the version number
     */
    private String versionNumber;

    /**
     * The type
     * -- GETTER --
     * Gets type.
     * @return the type
     * -- SETTER --
     * Sets type.
     * @param type the type
     */
    private String type;

    /**
     * The attribute list
     * -- GETTER --
     * Gets attribute list.
     * @return the attribute list
     * -- SETTER --
     * Sets attribute list.
     * @param attributeList the attribute list
     */
    @JsonProperty("columns")
    private List<Attribute> attributeList;

}
