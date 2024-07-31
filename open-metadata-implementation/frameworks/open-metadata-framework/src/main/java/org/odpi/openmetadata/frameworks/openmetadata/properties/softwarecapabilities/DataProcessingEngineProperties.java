/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;


/**
 * DataProcessingEngineProperties describes an engine.  Set up the typeName to create common subtypes such as
 * ReportingEngine, WorkflowEngine, AnalyticsEngine, DataMovementEngine or DataVirtualizationEngine.
 */
public class DataProcessingEngineProperties extends SoftwareCapabilityProperties
{
    /**
     * Default constructor
     */
    public DataProcessingEngineProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataProcessingEngineProperties(DataProcessingEngineProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor from OCF bean.
     *
     * @param template object to copy
     */
    public DataProcessingEngineProperties(SoftwareCapabilityProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataProcessingEngineProperties{} " + super.toString();
    }
}
