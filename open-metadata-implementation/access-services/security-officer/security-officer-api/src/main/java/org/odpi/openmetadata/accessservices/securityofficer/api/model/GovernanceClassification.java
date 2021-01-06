/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.api.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GovernanceClassification extends GovernanceClassificationBase implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<String> securityLabels;
    private Map<String, String> securityProperties;

    /**
     * @return list of the security labels assigned with a Security Tag
     */
    public List<String> getSecurityLabels() {
        return securityLabels;
    }

    /**
     * Set up the security labels
     *
     * @param securityLabels security labels for Security Tag classification
     */
    public void setSecurityLabels(List<String> securityLabels) {
        this.securityLabels = securityLabels;
    }

    /**
     * @return attributeValues - key/value pairs for security properties of Classification
     */
    public Map<String, String> getSecurityProperties() {
        return securityProperties;
    }

    /**
     * @param securityProperties - key/value pairs for security properties of Classification
     */
    public void setSecurityProperties(Map<String, String> securityProperties) {
        this.securityProperties = securityProperties;
    }

    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "GovernanceClassification{" +
                       "securityLabels=" + securityLabels +
                       ", securityProperties=" + securityProperties +
                       '}';
    }
}

