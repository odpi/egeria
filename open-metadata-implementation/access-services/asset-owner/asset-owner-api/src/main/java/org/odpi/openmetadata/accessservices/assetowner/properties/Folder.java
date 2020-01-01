/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

/**
 * Folder defines an asset that is a folder.  The qualified name is the fully qualified path name of the folder.
 */
public class Folder extends DataStore
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public Folder()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public Folder(Folder template)
    {
        super(template);
    }


    /**
     * Subtyping constructor.
     *
     * @param template object to copy
     */
    public Folder(DataStore template)
    {
        super(template);
    }


    /**
     * Subtyping constructor.
     *
     * @param template object to copy
     */
    public Folder(Asset template)
    {
        super(template);
    }
}
