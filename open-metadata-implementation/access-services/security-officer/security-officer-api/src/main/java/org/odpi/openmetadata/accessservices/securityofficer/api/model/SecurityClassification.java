/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.accessservices.securityofficer.api.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityClassification implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<String> securityLabels;
    private Map<String, Object> securityProperties;

    public List<String> getSecurityLabels() {
        return securityLabels;
    }

    public void setSecurityLabels(List<String> securityLabels) {
        this.securityLabels = securityLabels;
    }

    public Map<String, Object> getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(Map<String, Object> securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public String toString() {
        return "SecurityClassification{" +
                "securityLabels=" + securityLabels +
                ", securityProperties=" + securityProperties +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityClassification that = (SecurityClassification) o;
        return securityLabels.equals(that.securityLabels) &&
                Objects.equals(securityProperties, that.securityProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(securityLabels, securityProperties);
    }
}