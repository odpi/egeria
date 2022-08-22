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
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOriginCategory;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ElementOrigin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The name of the server where the element was retrieved from.
     * -- GETTER --
     * Returns the name of the server
     * @return String - the name of the server
     * -- SETTER --
     * Set up the name of the server
     * @param sourceServer - name of the server
     */
    private String sourceServer;

    /**
     * The id of the metadata collection.
     * -- GETTER --
     * Returns the id of the metadata collection.
     * @return String - the id of the metadata collection.
     * -- SETTER --
     * Set up the id of the metadata collection.
     * @param metadataCollectionId - id of the metadata collection.
     */
    private String metadataCollectionId;

    /**
     * The name of the metadata collection.
     * -- GETTER --
     * Returns the name of the metadata collection.
     * @return String - the name of the metadata collection.
     * -- SETTER --
     * Set up the name of the metadata collection.
     * @param metadataCollectionName - name of the metadata collection.
     */
    private String metadataCollectionName;

    /**
     * The license string for this instance
     * -- GETTER --
     * Returns the license for this instance.
     * @return String - the license string.
     * -- SETTER --
     * Set up the license string for this instance.
     * @param instanceLicense - the license string.
     */
    private String instanceLicense;

    /**
     * The origin category of the element
     * -- GETTER --
     * Returns the origin category
     * @return the value from the enum representing the origin category
     * -- SETTER --
     * Set up the origin category of the element
     * @param originCategory - the origin category value
     */
    private ElementOriginCategory originCategory;
}
