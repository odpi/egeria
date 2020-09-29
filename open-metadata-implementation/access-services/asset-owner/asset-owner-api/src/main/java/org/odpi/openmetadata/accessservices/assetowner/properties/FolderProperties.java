/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;


/**
 * FolderProperties defines an asset that is a folder.  The qualified name is the fully qualified path name of the folder.
 */
public class FolderProperties extends DataStoreProperties
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public FolderProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FolderProperties(FolderProperties template)
    {
        super(template);
    }


    /**
     * Subtyping constructor.
     *
     * @param template object to copy
     */
    public FolderProperties(DataStoreProperties template)
    {
        super(template);
    }


    /**
     * Subtyping constructor.
     *
     * @param template object to copy
     */
    public FolderProperties(AssetProperties template)
    {
        super(template);
    }
}
