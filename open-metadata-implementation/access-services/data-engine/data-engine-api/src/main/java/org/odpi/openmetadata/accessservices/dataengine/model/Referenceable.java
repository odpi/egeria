/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Referenceable implements Serializable {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    /**
     * -- GETTER --
     * Returns the stored qualified name property for the metadata entity.
     * If no qualified name is available then the empty string is returned.
     *
     * @return qualifiedName
     * -- SETTER --
     * Set up the fully qualified name.
     * @param qualifiedName String name
     */
    private String qualifiedName;

    /**
     * -- GETTER --
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     * -- SETTER --
     * Set up additional properties.
     * @param additionalProperties Additional properties object
     */
    private Map<String, String> additionalProperties;

}
