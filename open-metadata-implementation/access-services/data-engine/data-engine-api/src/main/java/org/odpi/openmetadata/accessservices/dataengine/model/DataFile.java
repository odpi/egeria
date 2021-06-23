/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OM type DataFile
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "fileType")
@JsonSubTypes({ @JsonSubTypes.Type(value = DataFile.class, name = "DataFile"),
                @JsonSubTypes.Type(value = CSVFile.class, name = "CSVFile")})
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class DataFile extends DataStore {

    /**
     * -- GETTER --
     * Gets file type
     * @return type
     * -- SETTER --
     * Sets the file type
     * @param fileType type
     */
    private String fileType;

    /**
     * -- GETTER --
     * Gets the file schema
     * @return schema
     * -- SETTER --
     * Sets the file schema
     * @param schema schema
     */
    private SchemaType schema;

    /**
     * -- GETTER --
     * Gets the file columns
     * @return columns
     * -- SETTER --
     * Sets the file columns
     * @param columns columns
     */
    private List<Attribute> columns;

    /**
     * -- GETTER --
     * Gets the network address
     * @return columns
     * -- SETTER --
     * Sets the network address. Needed to create Endpoint, which in turn is internally generated along with Connection,
     * not provided by user
     * @param networkAddress network address
     */
    // Needed to create Endpoint, which in turn is internally generated along with Connection, not provided by user
    private String networkAddress;

    /**
     * -- GETTER --
     * Get an Endpoint protocol
     * @return network address
     * -- SETTER --
     * Sets the protocol. Needed to create Endpoint, which in turn is internally generated along with Connection,
     * not provided by user
     * @param protocol protocol
     */
    private String protocol;

}
