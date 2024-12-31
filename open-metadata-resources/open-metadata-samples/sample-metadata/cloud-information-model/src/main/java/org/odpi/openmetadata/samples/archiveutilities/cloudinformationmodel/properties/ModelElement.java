/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties;

/**
 * ModelElement describes the common attributes for each element in the model.
 * Each element has 2 names - the GUID is always unique but not always known.
 * The technical name should also be unique within a specific type.
 */
public class ModelElement
{
    private String guid          = null;
    private String technicalName = null;
    private String displayName   = null;
    private String description   = null;
    private String version       = null;

    public ModelElement()
    {
    }

    public String getGUID()
    {
        return guid;
    }

    public void setGUID(String guid)
    {
        this.guid = guid;
    }

    public String getTechnicalName()
    {
        return technicalName;
    }

    public void setTechnicalName(String technicalName)
    {
        this.technicalName = technicalName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }


    @Override
    public String toString()
    {
        return "ModelElement{" +
                       "guid='" + guid + '\'' +
                       ", technicalName='" + technicalName + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", version='" + version + '\'' +
                       ", GUID='" + getGUID() + '\'' +
                       '}';
    }
}
