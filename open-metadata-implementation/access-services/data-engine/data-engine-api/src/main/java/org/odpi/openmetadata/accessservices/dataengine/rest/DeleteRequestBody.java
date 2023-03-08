/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class DeleteRequestBody extends DataEngineOMASAPIRequestBody {

    /**
     * The unique identifier of the entity
     * -- GETTER --
     * Return the entity unique identifier
     *
     * @return String - unique identifier of the entity
     * -- SETTER --
     * Set up the unique identifier of the entity
     * @param guid of the entity
     */
    private String guid;

    /**
     * The qualified name of the entity
     * -- GETTER --
     * Return the entity qualified name
     *
     * @return String - qualified name of the entity
     * -- SETTER --
     * Set up the qualified name of the entity
     * @param qualifiedName of the entity
     */
    private String qualifiedName;

    /**
     * The delete semantic
     * -- GETTER --
     * Return the delete semantic
     *
     * @return String - unique identifier of the entity
     * -- SETTER --
     * Set up the delete semantic
     * @param deleteSemantic of the entity
     */
    private DeleteSemantic deleteSemantic = DeleteSemantic.SOFT;
}

