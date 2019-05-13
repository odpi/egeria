/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model;

import java.util.Map;
import java.util.Objects;

public class RangerServiceResource extends RangerBaseObject {

    private String serviceName;
    private Map<String, RangerPolicyResource> resourceElements;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, RangerPolicyResource> getResourceElements() {
        return resourceElements;
    }

    public void setResourceElements(Map<String, RangerPolicyResource> resourceElements) {
        this.resourceElements = resourceElements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangerServiceResource resource = (RangerServiceResource) o;
        return Objects.equals(serviceName, resource.serviceName) &&
                Objects.equals(resourceElements, resource.resourceElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, resourceElements);
    }
}
