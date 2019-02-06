/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model;

import java.util.List;
import java.util.Objects;

public class RangerPolicyResource {

    private List<String> values;
    private Boolean isExcludes;
    private Boolean isRecursive;

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public Boolean getExcludes() {
        return isExcludes;
    }

    public void setExcludes(Boolean excludes) {
        isExcludes = excludes;
    }

    public Boolean getRecursive() {
        return isRecursive;
    }

    public void setRecursive(Boolean recursive) {
        isRecursive = recursive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangerPolicyResource that = (RangerPolicyResource) o;
        return values.equals(that.values) &&
                isExcludes.equals(that.isExcludes) &&
                isRecursive.equals(that.isRecursive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values, isExcludes, isRecursive);
    }
}