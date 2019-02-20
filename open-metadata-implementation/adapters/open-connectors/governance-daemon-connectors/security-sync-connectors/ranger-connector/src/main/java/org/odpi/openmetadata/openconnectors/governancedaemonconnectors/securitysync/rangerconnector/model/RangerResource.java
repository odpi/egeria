/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model;

import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants;

import java.util.List;
import java.util.Map;

public class RangerResource {

    private String op = Constants.ADD_OR_UPDATE;
    private String serviceName;
    private Long tagVersion;
    private String tagUpdateTime;
    private Map<Long, RangerTag> tags;
    private List<RangerServiceResource> serviceResources;
    private Map<Long, List<Long>> resourceToTagIds;
    private Map<Long, RangerTagDef> tagDefinitions;

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getTagVersion() {
        return tagVersion;
    }

    public void setTagVersion(Long tagVersion) {
        this.tagVersion = tagVersion;
    }

    public String getTagUpdateTime() {
        return tagUpdateTime;
    }

    public void setTagUpdateTime(String tagUpdateTime) {
        this.tagUpdateTime = tagUpdateTime;
    }

    public Map<Long, RangerTag> getTags() {
        return tags;
    }

    public void setTags(Map<Long, RangerTag> tags) {
        this.tags = tags;
    }

    public List<RangerServiceResource> getServiceResources() {
        return serviceResources;
    }

    public void setServiceResources(List<RangerServiceResource> serviceResources) {
        this.serviceResources = serviceResources;
    }

    public Map<Long, List<Long>> getResourceToTagIds() {
        return resourceToTagIds;
    }

    public void setResourceToTagIds(Map<Long, List<Long>> resourceToTagIds) {
        this.resourceToTagIds = resourceToTagIds;
    }

    public Map<Long, RangerTagDef> getTagDefinitions() {
        return tagDefinitions;
    }

    public void setTagDefinitions(Map<Long, RangerTagDef> tagDefinitions) {
        this.tagDefinitions = tagDefinitions;
    }

    @Override
    public String toString() {
        return "RangerResource{" +
                "op='" + op + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", tagVersion=" + tagVersion +
                ", tagUpdateTime='" + tagUpdateTime + '\'' +
                ", tags=" + tags +
                ", serviceResources=" + serviceResources +
                ", resourceToTagIds=" + resourceToTagIds +
                '}';
    }
}
