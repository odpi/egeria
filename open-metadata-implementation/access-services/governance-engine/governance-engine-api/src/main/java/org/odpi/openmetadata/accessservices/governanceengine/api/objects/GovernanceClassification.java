/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.api.objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GovernanceClassification extends GovernanceClassificationBase implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, String> attributes;

    /**
     * @return attributeValues - key/value pairs for additional objects of Classification
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes - key/value pairs for additional objects of Classification
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

}

