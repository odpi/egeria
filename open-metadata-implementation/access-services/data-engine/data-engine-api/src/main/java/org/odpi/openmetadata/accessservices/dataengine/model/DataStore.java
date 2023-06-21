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

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataStore is a java bean used to create DataStore associated with the external data engine.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class DataStore extends Asset {

    /**
     * The fully qualified physical location of the data store
     * -- GETTER --
     * Return the fully qualified physical location of the data store.  This should be suitable for the
     * network address of the Endpoint.
     * @return string name
     * -- SETTER --
     * Set up the fully qualified physical location of the data store.  This should be suitable for the
     * network address of the Endpoint.
     * @param pathName string name
     */
    private String pathName;

    /**
     * The time that the data store was created
     * -- GETTER --
     * Return the time that the data store was created.
     * @return create time
     * -- SETTER --
     * Set up the time that the data store was created.
     * @param createTime date
     */
    private Date createTime;

    /**
     * The last known time the data store was modified
     * -- GETTER --
     * Return the last known time the data store was modified.
     * @return modified time
     * -- SETTER --
     * Setup the last known time the data store was modified.
     * @param modifiedTime date
     */
    private Date modifiedTime;

    /**
     * The name of the encoding style used in the data store
     * -- GETTER --
     * Return the name of the encoding style used in the data store.
     * @return the encoding style used in the data store
     * -- SETTER --
     * Set up the name of the encoding style used in the data store.
     * @param encodingType string name
     */
    private String encodingType;

    /**
     * The name of the natural language used for text strings within the data store
     * -- GETTER --
     * Return the name of the natural language used for text strings within the data store.
     * @return encodingLanguage string language name
     * -- SETTER --
     * Set up the name of the natural language used for text strings within the data store.
     * @param encodingLanguage string language name
     */
    private String encodingLanguage;

    /**
     * The description of the encoding used in the data store
     * -- GETTER --
     * Return the description of the encoding used in the data store.
     * @return encodingDescription string text
     * -- SETTER --
     * Set up the description of the encoding used in the data store.
     * @param encodingDescription string text
     */
    private String encodingDescription;

    /**
     * The additional properties associated with the encoding process
     * -- GETTER --
     * Return the additional properties associated with the encoding process
     * @return additional properties associated with the encoding process
     * -- SETTER --
     * Set up the additional properties associated with the encoding process.
     * @param encodingProperties map of name-value pairs
     */
    private Map<String, String> encodingProperties;

    /**
     * Return the additional properties associated with the encoding process.
     * @return map of name-value pairs
     */
    public Map<String, String> getEncodingProperties() {
        if (encodingProperties == null || encodingProperties.isEmpty()) {
            return Collections.emptyMap();
        }
        return encodingProperties;
    }

}
