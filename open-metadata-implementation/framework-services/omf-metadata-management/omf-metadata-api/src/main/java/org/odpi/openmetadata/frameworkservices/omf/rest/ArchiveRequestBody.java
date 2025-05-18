/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ArchiveProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ArchiveRequestBody provides a structure for passing the archive information for a metadata element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ArchiveRequestBody extends MetadataSourceRequestBody
{
    private ArchiveProperties archiveProperties = null;


    /**
     * Default constructor
     */
    public ArchiveRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ArchiveRequestBody(ArchiveRequestBody template)
    {
        super(template);

        if (template != null)
        {
            archiveProperties = template.getArchiveProperties();
        }
    }


    /**
     * Return the archive properties.
     *
     * @return properties
     */
    public ArchiveProperties getArchiveProperties()
    {
        return archiveProperties;
    }


    /**
     * Set up the archive properties.
     *
     * @param archiveProperties properties
     */
    public void setArchiveProperties(ArchiveProperties archiveProperties)
    {
        this.archiveProperties = archiveProperties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ArchiveRequestBody{" +
                "archiveProperties=" + archiveProperties +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ArchiveRequestBody that = (ArchiveRequestBody) objectToCompare;
        return archiveProperties == that.archiveProperties;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), archiveProperties);
    }
}
