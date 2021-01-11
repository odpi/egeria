/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.api.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

// This is the minimal representation of a classification that we use in GE OMAS.
public class GovernanceClassificationBase implements Serializable {

    private static final long serialVersionUID = 1L;

    // Attributes of a Tag Definition
    private String tagId; // An identifier used for cross-referencing within the response
    private String guid;  // The guid for the classification in the repository
    private String name;  // The alphanumeric name for the tag ie EXPIRES_ON


    public String getGuid() {
        return guid;
    }

    /**
     * @param guid - unique identifier
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * @return name - name of classification
     */
    public String getName() {
        return name;
    }

    /**
     * @param name - name of classification
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return tagId - tag id
     */
    public String getTagId() {
        return tagId;
    }

    /**
     * @param tagId - tag id
     */
    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

}

