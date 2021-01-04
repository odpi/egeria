/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.api.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GovernedAsset implements Serializable {

    private static final long serialVersionUID = 1L;

    private String guid;
    private String fullQualifiedName;
    private String name;
    private String type;
    private String owner;
    private Context context;
    private GovernanceClassification assignedGovernanceClassification;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getFullQualifiedName() {
        return fullQualifiedName;
    }

    public void setFullQualifiedName(String fullQualifiedName) {
        this.fullQualifiedName = fullQualifiedName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public GovernanceClassification getAssignedGovernanceClassification() {
        return assignedGovernanceClassification;
    }

    public void setAssignedGovernanceClassification(GovernanceClassification assignedGovernanceClassification) {
        this.assignedGovernanceClassification = assignedGovernanceClassification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GovernedAsset that = (GovernedAsset) o;
        return Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(context);
    }

    @Override
    public String toString() {
        return "GovernedAsset{" +
                "guid='" + guid + '\'' +
                ", fullQualifiedName='" + fullQualifiedName + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", owner='" + owner + '\'' +
                ", context=" + context +
                ", assignedGovernanceClassification=" + assignedGovernanceClassification +
                '}';
    }
}
