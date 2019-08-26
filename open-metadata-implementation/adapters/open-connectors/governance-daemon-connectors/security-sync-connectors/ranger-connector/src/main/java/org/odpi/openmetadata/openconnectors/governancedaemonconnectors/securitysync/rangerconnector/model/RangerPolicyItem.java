/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model;

import java.util.List;

public class RangerPolicyItem {

    private List<RangerPolicyItemAccess> accesses;
    private List<String> users;
    private List<String> groups;
    private List<RangerPolicyItemCondition> conditions;
    private Boolean delegateAdmin;

    public List<RangerPolicyItemAccess> getAccesses() {
        return accesses;
    }

    public void setAccesses(List<RangerPolicyItemAccess> accesses) {
        this.accesses = accesses;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<RangerPolicyItemCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<RangerPolicyItemCondition> conditions) {
        this.conditions = conditions;
    }

    public Boolean getDelegateAdmin() {
        return delegateAdmin;
    }

    public void setDelegateAdmin(Boolean delegateAdmin) {
        this.delegateAdmin = delegateAdmin;
    }
}