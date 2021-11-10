/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
/**
 * The Endpoint describes the network information necessary for a connector to connect to the server
 * where the Asset is accessible from.
 */
public class Endpoint extends Referenceable {

    /**
     * The description of the endpoint
     * -- GETTER --
     * Return the description of the endpoint.
     * @return description
     * -- SETTER --
     * Set up the description of the endpoint.
     * @param description description
     */
    private String description;

    /**
     * The display name of the endpoint
     * -- GETTER --
     * Return the display name of the endpoint.
     * @return display name
     * -- SETTER --
     * Set up the display name of the endpoint.
     * @param displayName display name
     */
    private String displayName;

    /**
     * The encryption method of the endpoint
     * -- GETTER --
     * Returns the stored encryption method property for the endpoint. This is allowing the information
     * needed to work with a specific encryption mechanism used by the endpoint to be defined.
     * If no encryption method property is available (typically because this is an unencrypted endpoint)
     * then null is returned.
     * @return encryption method
     * -- SETTER --
     * Set up the encryption method of the endpoint.
     * @param encryptionMethod encryption method
     */
    private String encryptionMethod;

    /**
     * The name of the endpoint
     * -- GETTER --
     * Return the name of the endpoint.
     * @return name
     * -- SETTER --
     * Set up the name of the endpoint.
     * @param name name
     */
    private String name;

    /**
     * The network address of the endpoint
     * -- GETTER --
     * Returns the network address property for the endpoint.
     * @return name
     * -- SETTER --
     * Set up the network address of the endpoint.
     * @param networkAddress network address
     */
    private String networkAddress;

    /**
     * The protocol of the endpoint
     * -- GETTER --
     * Returns the protocol for the endpoint.
     * @return name
     * -- SETTER --
     * Set up the protocol of the endpoint.
     * @param protocol protocol
     */
    private String protocol;

}
