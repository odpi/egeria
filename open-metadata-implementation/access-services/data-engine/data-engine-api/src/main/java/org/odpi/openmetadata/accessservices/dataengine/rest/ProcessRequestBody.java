/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import java.util.List;
import java.util.Objects;

public class ProcessRequestBody extends DataEngineOMASAPIRequestBody {
    private String name;
    private List<String> inputs;
    private List<String> outputs;
    private String description;
    private String latestChange;
    private List<String> zoneMembership;
    private String displayName;
    private String parentProcessGuid;

    public String getName() {
        return name;
    }

    public List<String> getInputs() {
        return inputs;
    }

    public List<String> getOutputs() {
        return outputs;
    }

    public String getDescription() {
        return description;
    }

    public String getLatestChange() {
        return latestChange;
    }

    public List<String> getZoneMembership() {
        return zoneMembership;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getParentProcessGuid() {
        return parentProcessGuid;
    }

    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString() {
        return "ProcessRequestBody{" +
                "name='" + name + '\'' +
                ", inputs=" + inputs +
                ", outputs=" + outputs +
                ", description='" + description + '\'' +
                ", latestChange='" + latestChange + '\'' +
                ", zoneMembership='" + zoneMembership + '\'' +
                ", displayName='" + displayName + '\'' +
                ", parentProcessId='" + parentProcessGuid + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessRequestBody that = (ProcessRequestBody) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(inputs, that.inputs) &&
                Objects.equals(outputs, that.outputs) &&
                Objects.equals(description, that.description) &&
                Objects.equals(latestChange, that.latestChange) &&
                Objects.equals(zoneMembership, that.zoneMembership) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(parentProcessGuid, that.parentProcessGuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, inputs, outputs, description, latestChange, zoneMembership, displayName,
                parentProcessGuid);
    }
}

