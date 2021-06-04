/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Parent process.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParentProcess implements Serializable {
    private String qualifiedName;

    @JsonProperty("containmentType")
    private ProcessContainmentType processContainmentType;

    /**
     * Gets qualified name.
     *
     * @return the qualified name
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * Sets qualified name.
     *
     * @param qualifiedName the qualified name
     */
    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    /**
     * Gets process containment type.
     *
     * @return the process containment type
     */
    public ProcessContainmentType getProcessContainmentType() {
        return processContainmentType;
    }

    /**
     * Sets process containment type.
     *
     * @param processContainmentType the process containment type
     */
    public void setProcessContainmentType(ProcessContainmentType processContainmentType) {
        this.processContainmentType = processContainmentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParentProcess that = (ParentProcess) o;
        return Objects.equals(qualifiedName, that.qualifiedName) &&
                processContainmentType == that.processContainmentType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedName, processContainmentType);
    }

    @Override
    public String toString() {
        return "ParentProcess{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", containmentType=" + processContainmentType +
                '}';
    }
}
