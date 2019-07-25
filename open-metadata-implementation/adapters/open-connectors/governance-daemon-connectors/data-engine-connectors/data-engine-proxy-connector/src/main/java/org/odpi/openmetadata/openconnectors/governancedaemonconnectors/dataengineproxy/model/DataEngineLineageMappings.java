/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.model;

import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;

import java.util.Set;

public class DataEngineLineageMappings {

    private Set<LineageMapping> lineageMappings;
    private String userId;

    public DataEngineLineageMappings(Set<LineageMapping> lineageMappings, String userId) {
        this.lineageMappings = lineageMappings;
        this.userId = userId;
    }

    public Set<LineageMapping> getLineageMappings() { return lineageMappings; }
    public void setLineageMappings(Set<LineageMapping> lineageMappings) { this.lineageMappings = lineageMappings; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

}
