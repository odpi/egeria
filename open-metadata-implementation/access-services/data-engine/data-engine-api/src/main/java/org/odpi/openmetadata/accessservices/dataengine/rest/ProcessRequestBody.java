/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import java.util.List;
import java.util.Objects;

public class ProcessRequestBody extends AssetRequestBody {
    private List<String> inputs;
    private List<String> outputs;
    private String displayName;
    private String parentProcessGuid;

    public List<String> getInputs() {
        return inputs;
    }

    public List<String> getOutputs() {
        return outputs;
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
                ", inputs=" + inputs +
                ", outputs=" + outputs +
                ", displayName='" + displayName + '\'' +
                ", parentProcessId='" + parentProcessGuid + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProcessRequestBody that = (ProcessRequestBody) o;
        return Objects.equals(inputs, that.inputs) &&
                Objects.equals(outputs, that.outputs) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(parentProcessGuid, that.parentProcessGuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), inputs, outputs, displayName, parentProcessGuid);
    }
}

