/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model;

import java.util.Date;
import java.util.List;

public class RangerSecurityServicePolicies {

    private String serviceName;
    private Long serviceId;
    private Long policyVersion;
    private Date policyUpdateTime;
    private List<RangerPolicy> policies;
    private RangerServiceDef serviceDef;
    private String auditMode;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getPolicyVersion() {
        return policyVersion;
    }

    public void setPolicyVersion(Long policyVersion) {
        this.policyVersion = policyVersion;
    }

    public Date getPolicyUpdateTime() {
        return policyUpdateTime;
    }

    public void setPolicyUpdateTime(Date policyUpdateTime) {
        this.policyUpdateTime = policyUpdateTime;
    }

    public List<RangerPolicy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<RangerPolicy> policies) {
        this.policies = policies;
    }

    public RangerServiceDef getServiceDef() {
        return serviceDef;
    }

    public void setServiceDef(RangerServiceDef serviceDef) {
        this.serviceDef = serviceDef;
    }

    public String getAuditMode() {
        return auditMode;
    }

    public void setAuditMode(String auditMode) {
        this.auditMode = auditMode;
    }
}