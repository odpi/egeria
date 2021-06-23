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

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Connection extends Referenceable {

    /**
     * -- GETTER --
     * Get clear password
     * @return clear password
     * -- SETTER --
     * Set clear password
     * @param clearPassword
     */
    private String clearPassword;

    /**
     * -- GETTER --
     * Get configuration properties
     * @return configuration properties
     * -- SETTER --
     * Set configuration properties
     * @param configurationProperties
     */
    private Map<String, String> configurationProperties;

    /**
     * -- GETTER --
     * Get description
     * @return description
     * -- SETTER --
     * Set description
     * @param description
     */
    private String description;

    /**
     * -- GETTER --
     * Get display name
     * @return display name
     * -- SETTER --
     * Set display name
     * @param displayName
     */
    private String displayName;

    /**
     * -- GETTER --
     * Get encrypted password
     * @return encrypted password
     * -- SETTER --
     * Set encrypted password
     * @param encryptedPassword
     */
    private String encryptedPassword;

    /**
     * -- GETTER --
     * Get secured properties
     * @return secured properties
     * -- SETTER --
     * Set secured properties
     * @param securedProperties
     */
    private Map<String, String> securedProperties;

    /**
     * -- GETTER --
     * Get user id
     * @return user id
     * -- SETTER --
     * Set user id
     * @param userId
     */
    private String userId;

}
