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
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Schema type event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SchemaTypeEvent extends DataEngineEventHeader {

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
     * The port qualified name
     * -- GETTER --
     * Returns the port qualified name
     * @return the port qualified name
     * -- SETTER --
     * Sets up the port qualified name
     * @param portQualifiedName the port qualified name
     */
    private String portQualifiedName;

    /**
     * The schema type
     * -- GETTER --
     * Returns the schema type
     * @return the schema type
     * -- SETTER --
     * Sets up the schema type
     * @param schemaType the schema type
     */
    @JsonProperty("schema")
    private SchemaType schemaType;

}
