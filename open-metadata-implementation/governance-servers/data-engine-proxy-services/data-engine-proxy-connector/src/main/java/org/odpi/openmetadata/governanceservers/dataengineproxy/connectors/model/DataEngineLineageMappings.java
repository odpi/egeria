/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.connectors.model;

import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;

import java.util.Set;

/**
 * Wrapper for the Data Engine OMAS's LineageMapping object, to also be able to include the userId information.
 */
public class DataEngineLineageMappings {

    private Set<LineageMapping> lineageMappings;
    private String userId;

    /**
     * Default constructor
     *
     * @param lineageMappings the LineageMappings being maintained
     * @param userId          the user maintaining the LineageMappings
     */
    public DataEngineLineageMappings(Set<LineageMapping> lineageMappings, String userId) {
        this.lineageMappings = lineageMappings;
        this.userId = userId;
    }

    /**
     * Retrieve the LineageMappings being maintained.
     *
     * @return {@code Set<LineageMapping>}
     */
    public Set<LineageMapping> getLineageMappings() { return lineageMappings; }

    /**
     * Set the LineageMappings to be maintained.
     *
     * @param lineageMappings the LineageMappings to be maintained
     */
    public void setLineageMappings(Set<LineageMapping> lineageMappings) { this.lineageMappings = lineageMappings; }

    /**
     * Retrieve the user maintaining the LineageMappings.
     *
     * @return String
     */
    public String getUserId() { return userId; }

    /**
     * Set the user maintaining the LineageMappings.
     *
     * @param userId the user maintaining the LineageMappings
     */
    public void setUserId(String userId) { this.userId = userId; }

}
