/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;


import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FolderProperties defines an asset that is a folder.  The qualified name is the fully qualified path name of the folder.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FolderProperties extends DataStoreProperties
{
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
