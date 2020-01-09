/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataStore provides the JavaBean for describing a data store.  This is a physical store of data.
 * It is saved in the catalog as an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
                      @JsonSubTypes.Type(value = File.class, name = "File"),
                      @JsonSubTypes.Type(value = Folder.class, name = "Folder")

              })
public class DataStore extends Asset
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public DataStore()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataStore(DataStore template)
    {
        super(template);
    }


    /**
     * Subtyping constructor.
     *
     * @param template object to copy
     */
    public DataStore(Asset template)
    {
        super(template);
    }
}
