/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Relationship object holds properties that are used for displaying a relationship between two assets
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Relationship extends Element implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The start element for the relationship
     * -- GETTER --
     * Returns the start element for the relationship
     * @return the start element for the relationship
     * -- SETTER --
     * Setup the start element for the relationship
     * @param fromEntity the start element for the relationship
     */
    private Element fromEntity;

    /**
     * The destination element for the relationship
     * -- GETTER --
     * Returns the destination element for the relationship
     * @return the destination element for the relationship
     * -- SETTER --
     * Setup the destination element for the relationship
     * @param toEntity the destination element for the relationship
     */
    private Element toEntity;
}
