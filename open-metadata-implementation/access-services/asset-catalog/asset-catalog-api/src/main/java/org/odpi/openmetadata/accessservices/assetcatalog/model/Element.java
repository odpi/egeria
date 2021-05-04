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
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Element object holds properties that are used for displaying details of an entity.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Element implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier of the asset
     * -- GETTER --
     * Return the asset unique identifier
     * @return String - unique identifier of the asset
     * -- SETTER --
     * Set up the unique identifier of the asset
     * @param guid of the asset
     */
    private String guid;

    /**
     * The type definition of the asset
     * -- GETTER --
     * Returns the type definition of the asset
     * @return the type definition of the asset
     * -- SETTER --
     * Set up the type definition of the asset
     * @param type - the type definition of the asset
     */
    private Type type;

    private String name;

    /**
     * The name of the users that created the asset
     * -- GETTER --
     * Return the name of the user that created the asset.
     * @return the name of the users that created the asset
     * -- SETTER --
     * Set up the name of the user that created the asset
     * @param createdBy - the name of the user that created the asset
     */
    private String createdBy;

    /**
     * The date when the asset has been created
     * -- GETTER --
     * Returns the date when the asset has been created
     * @return date when for the asset creation
     * -- SETTER --
     * Set up the date when the asset has been created
     * @param createTime - creation date of the asset
     */
    private Date createTime;

    /**
     * The name of the user that updated the asset last time
     * -- GETTER --
     * Return the name of the user that updated the asset last time
     * @return string - the name of the user that updated the asset last time
     * -- SETTER --
     * Set up the name of the user that updated the asset last time
     * @param updatedBy the name of the user that updated the asset last time
     */
    private String updatedBy;

    /**
     * The date when the asset has been created
     * -- GETTER --
     * Returns the date when the asset has been created
     * @return date - the date when the asset has been created
     * -- SETTER --
     * Set up the date when the asset has been created
     * @param updateTime - the date when the asset has been created
     */
    private Date updateTime;

    /**
     * The version of the asset
     * -- GETTER --
     * Returns the version of the asset
     * @return long - the version of the asset
     * -- SETTER --
     * Set up the version of the asset
     * @param version -  the version of the asset
     */
    private Long version;

    /**
     * The status of the asset
     * -- GETTER --
     * Returns the status of the asset
     * @return status
     * -- SETTER --
     * Set up the status of the asset
     * @param status - enum that describes the asset's status
     */
    private String status;

    private String url;
    private Map<String, String> properties;
    private Map<String, String> additionalProperties;
    private List<Classification> classifications;
    private Element parentElement;

}
