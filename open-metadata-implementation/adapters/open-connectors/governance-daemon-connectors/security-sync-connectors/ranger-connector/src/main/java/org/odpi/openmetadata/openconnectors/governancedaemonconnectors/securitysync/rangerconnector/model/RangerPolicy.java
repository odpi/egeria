/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model;

import java.util.List;
import java.util.Map;

public class RangerPolicy {

    private String service;
    private String name;
    private Integer policyType;
    private Integer policyPriority;
    private String description;
    private String resourceSignature;
    private Boolean isAuditEnabled;
    private Map<String, RangerPolicyResource> resources;
    private List<RangerPolicyItem> policyItems;
    private List<RangerPolicyItem> denyPolicyItems;
    private List<RangerPolicyItem> allowExceptions;
    private List<RangerPolicyItem> denyExceptions;
    private List<RangerDataMaskPolicyItem> dataMaskPolicyItems;
    private List<RangerRowFilterPolicyItem> rowFilterPolicyItems;
    private String serviceType;
    private Map<String, Object> options;
    private List<String> policyLabels;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPolicyType() {
        return policyType;
    }

    public void setPolicyType(Integer policyType) {
        this.policyType = policyType;
    }

    public Integer getPolicyPriority() {
        return policyPriority;
    }

    public void setPolicyPriority(Integer policyPriority) {
        this.policyPriority = policyPriority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceSignature() {
        return resourceSignature;
    }

    public void setResourceSignature(String resourceSignature) {
        this.resourceSignature = resourceSignature;
    }

    public Boolean getAuditEnabled() {
        return isAuditEnabled;
    }

    public void setAuditEnabled(Boolean auditEnabled) {
        isAuditEnabled = auditEnabled;
    }

    public Map<String, RangerPolicyResource> getResources() {
        return resources;
    }

    public void setResources(Map<String, RangerPolicyResource> resources) {
        this.resources = resources;
    }

    public List<RangerPolicyItem> getPolicyItems() {
        return policyItems;
    }

    public void setPolicyItems(List<RangerPolicyItem> policyItems) {
        this.policyItems = policyItems;
    }

    public List<RangerPolicyItem> getDenyPolicyItems() {
        return denyPolicyItems;
    }

    public void setDenyPolicyItems(List<RangerPolicyItem> denyPolicyItems) {
        this.denyPolicyItems = denyPolicyItems;
    }

    public List<RangerPolicyItem> getAllowExceptions() {
        return allowExceptions;
    }

    public void setAllowExceptions(List<RangerPolicyItem> allowExceptions) {
        this.allowExceptions = allowExceptions;
    }

    public List<RangerPolicyItem> getDenyExceptions() {
        return denyExceptions;
    }

    public void setDenyExceptions(List<RangerPolicyItem> denyExceptions) {
        this.denyExceptions = denyExceptions;
    }

    public List<RangerDataMaskPolicyItem> getDataMaskPolicyItems() {
        return dataMaskPolicyItems;
    }

    public void setDataMaskPolicyItems(List<RangerDataMaskPolicyItem> dataMaskPolicyItems) {
        this.dataMaskPolicyItems = dataMaskPolicyItems;
    }

    public List<RangerRowFilterPolicyItem> getRowFilterPolicyItems() {
        return rowFilterPolicyItems;
    }

    public void setRowFilterPolicyItems(List<RangerRowFilterPolicyItem> rowFilterPolicyItems) {
        this.rowFilterPolicyItems = rowFilterPolicyItems;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

    public List<String> getPolicyLabels() {
        return policyLabels;
    }

    public void setPolicyLabels(List<String> policyLabels) {
        this.policyLabels = policyLabels;
    }
}