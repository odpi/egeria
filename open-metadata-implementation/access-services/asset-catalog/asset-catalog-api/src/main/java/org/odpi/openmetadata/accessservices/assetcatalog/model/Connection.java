/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Connection object contains the properties needed to access a specific data assets.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Connection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The secured properties of the connection
     * -- GETTER --
     * Return the connection's secured properties
     * @return the connection's secured properties
     * -- SETTER --
     * Set up the secured properties of the connection
     * @param securedProperties the connection's secured properties
     */
    private Map<String, String> securedProperties = null;

    /**
     * The unique identifier of the connection
     * -- GETTER --
     * Return the connection unique identifier
     * @return String - unique identifier of the connection
     * -- SETTER --
     * Set up the unique identifier of the connection
     * @param guid of the connection
     */
    private String guid;

    /**
     * The display name of the connection
     * -- GETTER --
     * Return the display name
     * @return the display name of the connection
     * -- SETTER --
     * Set up the display name of the connection
     * @param displayName the display name of the connection
     */
    private String displayName;

    /**
     * The description of the connection
     * -- GETTER --
     * Return the description
     * @return the description of the connection
     * -- SETTER --
     * Set up the description of the connection
     * @param description the description of the connection
     */
    private String description;

    /**
     * The qualified name of the connection
     * -- GETTER --
     * Return the qualified name
     * @return the qualified name of the connection
     * -- SETTER --
     * Set up the qualified name of the connection
     * @param qualifiedName the qualified name of the connection
     */
    private String qualifiedName;

    /**
     * Instantiates a new Connection.
     *
     * @param guid          the unique identifier of the connection
     * @param qualifiedName the qualified name of the connection
     */
    public Connection(String guid, String qualifiedName) {
        this.guid = guid;
        this.qualifiedName = qualifiedName;
    }
}
