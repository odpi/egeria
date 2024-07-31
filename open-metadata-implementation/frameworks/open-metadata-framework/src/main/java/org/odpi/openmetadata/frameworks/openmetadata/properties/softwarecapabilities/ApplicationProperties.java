/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;


/**
 * ApplicationProperties describes an collection of processes (application) that implements support for the business.
 */
public class ApplicationProperties extends SoftwareCapabilityProperties
{
    /**
     * Default constructor
     */
    public ApplicationProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ApplicationProperties(ApplicationProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor from OCF bean.
     *
     * @param template object to copy
     */
    public ApplicationProperties(SoftwareCapabilityProperties template)
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
        return "ApplicationProperties{} " + super.toString();
    }
}
