/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Classification object holds properties that are used for displaying details about the classification.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Classification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The name of the classification
     * -- GETTER --
     * Return the classification's name
     * @return the classification's name
     * -- SETTER --
     * Set up the name of the classification
     * @param name the classification's name
     */
    private String name;

    /**
     * The origin of the classification
     * -- GETTER --
     * Return the classification's origin
     * @return the classification's origin
     * -- SETTER --
     * Set up the origin of the classification
     * @param origin the classification's origin
     */
    private String origin;

    /**
     * The origin unique identifier
     * -- GETTER --
     * Return the origin unique identifier
     * @return the origin unique identifier
     * -- SETTER --
     * Set up the origin unique identifier
     * @param originGUID the origin unique identifier
     */
    private String originGUID;

    /**
     * The author of the classification
     * -- GETTER --
     * Return the author of the classification
     * @return the author of the classification
     * -- SETTER --
     * Set up the author of the classification
     * @param createdBy the author of the classification
     */
    private String createdBy;

    /**
     * The creation date of the classification
     * -- GETTER --
     * Return the creation date of the classification
     * @return the creation date of the classification
     * -- SETTER --
     * Set up the creation date of the classification
     * @param createTime the creation date of the classification
     */
    private Date createTime;

    /**
     * The author of the last update of the classification
     * -- GETTER --
     * Return the author of the last update of the classification
     * @return the author of the last update of the classification
     * -- SETTER --
     * Set up the author of the last update of the classification
     * @param updatedBy the author of the last update of the classification
     */
    private String updatedBy;

    /**
     * The update date of the classification
     * -- GETTER --
     * Return the update date of the classification
     * @return the update date of the classification
     * -- SETTER --
     * Set up the update date of the classification
     * @param updateTime the update date of the classification
     */
    private Date updateTime;

    /**
     * The version of the classification
     * -- GETTER --
     * Return the classification's version
     * @return the classification's version
     * -- SETTER --
     * Set up the version of the classification
     * @param version the classification's version
     */
    private Long version;

    /**
     * The status of the classification
     * -- GETTER --
     * Return the classification's status
     * @return the classification's status
     * -- SETTER --
     * Set up the status of the classification
     * @param status the classification's status
     */
    private String status;

    /**
     * The type of the classification
     * -- GETTER --
     * Return the classification's type
     * @return the classification's type
     * -- SETTER --
     * Set up the type of the classification
     * @param type the classification's type
     */
    private Type type;

    /**
     * The properties of the classification
     * -- GETTER --
     * Return the classification's properties
     * @return the classification's properties
     * -- SETTER --
     * Set up the properties of the classification
     * @param properties the classification's properties
     */
    private Map<String, String> properties;
}
