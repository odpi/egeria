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
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The relational table event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class RelationalTableEvent extends DataEngineEventHeader {
    /**
     * The relational table to be created
     * -- GETTER --
     * Return the relational table bean
     *
     * @return the relational table
     * -- SETTER --
     * Set up the relational table bean
     * @param relationalTable the relational table
     */
    @JsonProperty("table")
    private RelationalTable relationalTable;
    /**
     * The qualified name of the database
     * -- GETTER --
     * Return the database qualified name
     *
     * @return String - qualified name of the database
     * -- SETTER --
     * Set up the qualified name of the database
     * @param databaseQualifiedName of the database
     */
    private String databaseQualifiedName;
}
