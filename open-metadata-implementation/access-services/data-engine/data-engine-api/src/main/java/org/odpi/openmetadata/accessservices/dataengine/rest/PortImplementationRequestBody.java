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
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PortImplementationRequestBody describes the request body used to create/update port implementations
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class PortImplementationRequestBody extends DataEngineOMASAPIRequestBody {

    /**
     * The process qualified name
     * -- GETTER --
     * Returns the process qualified name
     * @return the process qualified name
     * -- SETTER --
     * Sets up the process qualified name
     * @param processQualifiedName the process qualified name
     */
    private String processQualifiedName;

    /**
     * The port implementation
     * -- GETTER --
     * Returns the port implementation
     * @return the port implementation
     * -- SETTER --
     * Sets up the port implementation
     * @param portImplementation the port implementation
     */
    private PortImplementation portImplementation;

}