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

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
/**
 * Connection is a java bean used to create connections associated with the external data engine.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Connection extends Referenceable {

    /**
     * The clear password
     * -- GETTER --
     * Get clear password
     * @return clear password
     * -- SETTER --
     * Set clear password
     * @param clearPassword clear password
     */
    private String clearPassword;

    /**
     * The configuration properties
     * -- GETTER --
     * Get configuration properties
     * @return configuration properties
     * -- SETTER --
     * Set configuration properties
     * @param configurationProperties configuration properties
     */
    private Map<String, String> configurationProperties;

    /**
     * The description
     * -- GETTER --
     * Get description
     * @return description
     * -- SETTER --
     * Set description
     * @param description description
     */
    private String description;

    /**
     * The display name
     * -- GETTER --
     * Get display name
     * @return display name
     * -- SETTER --
     * Set display name
     * @param displayName display name
     */
    private String displayName;

    /**
     * The encrypted password
     * -- GETTER --
     * Get encrypted password
     * @return encrypted password
     * -- SETTER --
     * Set encrypted password
     * @param encryptedPassword encrypted password
     */
    private String encryptedPassword;

    /**
     * The secured properties
     * -- GETTER --
     * Get secured properties
     * @return secured properties
     * -- SETTER --
     * Set secured properties
     * @param securedProperties secured properties
     */
    private Map<String, String> securedProperties;

    /**
     * The user id
     * -- GETTER --
     * Get user id
     * @return user id
     * -- SETTER --
     * Set user id
     * @param userId user ID
     */
    private String userId;

    /**
     * The connector type
     * -- GETTER --
     * Get connector type
     * @return connector type
     * -- SETTER --
     * Set connector type
     * @param connectorType connector type
     */
    private ConnectorType connectorType;

    /**
     * The endpoint
     * -- GETTER --
     * Get endpoint
     * @return endpoint
     * -- SETTER --
     * Set endpoint
     * @param endpoint the endpoint
     */
    private Endpoint endpoint;

    /**
     * The asset summary
     * -- GETTER --
     * Get asset summary
     * @return asset summary
     * -- SETTER --
     * Set asset summary
     * @param assetSummary the asset summary
     */
    private String assetSummary;

}
