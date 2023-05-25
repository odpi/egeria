/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Port.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PortImplementation.class, name = "PortImplementation")
})
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Port extends Referenceable {
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
     * The port type
     * --GETTER --
     * Gets port type.
     * @return the port type
     * -- SETTER --
     * Sets port type.
     * @param portType the port type
     */
    @JsonProperty("type")
    private PortType portType;

    /**
     * The update semantic
     * -- SETTER --
     * Sets update semantic.
     * @param updateSemantic the update semantic
     */
    private UpdateSemantic updateSemantic;

    /**
     * Gets update semantic.
     * @return the update semantic
     */
    public UpdateSemantic getUpdateSemantic() {
        if (updateSemantic == null) {
            return UpdateSemantic.REPLACE;
        }

        return updateSemantic;
    }

}