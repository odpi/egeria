/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class DatabaseSchemaRequestBody extends DataEngineOMASAPIRequestBody {

    /**
     * The database schema to be created
     * -- GETTER --
     * Return the database schema bean
     *
     * @return the database schema
     * -- SETTER --
     * Set up the database schema bean
     * @param databaseSchema the database schema
     */
    @JsonProperty("databaseSchema")
    private DatabaseSchema databaseSchema;

    /**
     * The database qualified name to which the database schema will be linked, if it exists
     * -- GETTER --
     * Return the database qualified name to which the database schema will be linked, if it exists
     *
     * @return the database qualified name
     * -- SETTER --
     * Set up the database qualified name to which the database schema will be linked, if it exists
     * @param databaseQualifiedName the database qualified name
     */
    @JsonProperty("databaseQualifiedName")
    private String databaseQualifiedName;

}


