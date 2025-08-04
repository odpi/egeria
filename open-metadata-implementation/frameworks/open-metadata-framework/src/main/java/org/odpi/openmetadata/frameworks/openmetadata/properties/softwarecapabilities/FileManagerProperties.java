/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;


/**
 * FileSystemProperties describes an application that manages a collection of files.
 */
public class FileManagerProperties extends SoftwareCapabilityProperties
{
    /**
     * Default constructor
     */
    public FileManagerProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public FileManagerProperties(FileManagerProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor from OCF bean.
     *
     * @param template object to copy
     */
    public FileManagerProperties(SoftwareCapabilityProperties template)
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
        return "FileManagerProperties{} " + super.toString();
    }
}
