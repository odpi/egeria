/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.owlcanonicalglossarymodel.properties;

import java.util.*;

/**
 * The Owl RDF Canonical Glossary models are jsonld files. This content is parsed and mapped into
 * ModelElements. These model elements contain identifying information, a description and a version.
 * This ModelElement is subclassed
 */
abstract public class ModelElement
{
    private String id            = null;
    private String displayName   = null;
    private String description   = null;
    private String version       = null;

    /**
     * Get the unique Name for this element
     * @return uniqueName
     */
    public String getId()
    {
        return id;
    }

    /**
     * Set the unique Name for this element
     * @param id unique name to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Get the display name for this element
     * @return the display name
     */
    public String getDisplayName()
    {
        return displayName;
    }
    /**
     * Get the description for this element
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Get the version of this element
     * @return version
     */
    public String getVersion()
    {
        return version;
    }

    @Override
    public String toString()
    {
        return "ModelElement{" +

                       ", id='" + id + '\'' +
                       ", technicalName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", version='" + version + '\'' +
                       '}';
    }


    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        ModelElement that = (ModelElement) objectToCompare;
        return         Objects.equals(getId(), that.getId()) &&
                       Objects.equals(getDisplayName(), that.getDisplayName()) &&
                       Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getVersion(), that.getVersion());
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(getId(), getDisplayName(), getDescription(), getVersion());
    }
}
