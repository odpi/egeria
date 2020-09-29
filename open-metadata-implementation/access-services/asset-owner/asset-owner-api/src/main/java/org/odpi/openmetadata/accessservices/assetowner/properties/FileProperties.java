/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

/**
 * File describes the property of a single data file.
 */
public class File extends DataStore
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public File()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public File(File template)
    {
        super(template);
    }


    /**
     * Subtyping constructor.
     *
     * @param template object to copy
     */
    public File(DataStore template)
    {
        super(template);
    }


    /**
     * Subtyping constructor.
     *
     * @param template object to copy
     */
    public File(Asset template)
    {
        super(template);
    }
}
