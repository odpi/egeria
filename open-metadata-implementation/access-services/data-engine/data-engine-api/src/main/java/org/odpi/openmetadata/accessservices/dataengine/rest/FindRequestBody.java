/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.odpi.openmetadata.accessservices.dataengine.model.Identifiers;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FindRequestBody describes the request body used to search for an entity
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class FindRequestBody implements Serializable {

    /**
     * The identifiers name of the entity
     * -- GETTER --
     * Return the entity identifiers
     *
     * @return String - identifiers of the entity
     * -- SETTER --
     * Set up the identifiers of the entity
     * @param identifiers of the entity
     */
    private Identifiers identifiers;

    /**
     * The externalSourceName of the entity
     * -- GETTER --
     * Return the entity externalSourceName
     *
     * @return String - externalSourceName of the entity
     * -- SETTER --
     * Set up the externalSourceName of the entity
     * @param externalSourceName of the entity
     */
    private String externalSourceName;

    /**
     * The type of the entity
     * -- GETTER --
     * Return the entity type
     *
     * @return String - type of the entity
     * -- SETTER --
     * Set up the type of the entity
     * @param type of the entity
     */
    private String type;

}

