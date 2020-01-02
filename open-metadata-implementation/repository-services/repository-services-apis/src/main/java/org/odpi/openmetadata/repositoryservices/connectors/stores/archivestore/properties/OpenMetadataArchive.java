/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataArchive defines the structure of the properties inside of an open metadata archive.
 * There are 3 sections:
 * <ul>
 *     <li>
 *         ArchiveProperties: provides details of the source and contents of the archive.
 *     </li>
 *     <li>
 *         TypeStore: a list of new AttributeTypeDefs, new TypeDefs and patches to existing TypeDefs.
 *     </li>
 *     <li>
 *         InstanceStore: a list of new metadata instances (Entities, Relationships and Classifications).
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataArchive extends OpenMetadataArchiveElementHeader
{
    private static final long    serialVersionUID = 1L;

    private OpenMetadataArchiveProperties    archiveProperties    = null;
    private OpenMetadataArchiveTypeStore     archiveTypeStore     = null;
    private OpenMetadataArchiveInstanceStore archiveInstanceStore = null;


    /**
     * Default constructor relies on the initialization of variables in their type declaration.
     */
    public OpenMetadataArchive()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OpenMetadataArchive(OpenMetadataArchive   template)
    {
        super(template);

        if (template != null)
        {
            archiveProperties = template.getArchiveProperties();
            archiveTypeStore = template.getArchiveTypeStore();
            archiveInstanceStore = template.getArchiveInstanceStore();
        }
    }


    /**
     * Return details of the archive.
     *
     * @return OpenMetadataArchiveProperties object
     */
    public OpenMetadataArchiveProperties getArchiveProperties()
    {
        if (archiveProperties == null)
        {
            return null;
        }
        else
        {
            return archiveProperties;
        }
    }


    /**
     * Set the archive properties for a new archive.
     *
     * @param archiveProperties  the descriptive properties of the archive
     */
    public void setArchiveProperties(OpenMetadataArchiveProperties archiveProperties)
    {
        this.archiveProperties = archiveProperties;
    }


    /**
     * Return the TypeStore for this archive.  The TypeStore contains TypeDefs and TypeDef patches.
     *
     * @return OpenMetadataArchiveTypeStore object
     */
    public OpenMetadataArchiveTypeStore getArchiveTypeStore()
    {
        if (archiveTypeStore == null)
        {
            return null;
        }
        else
        {
            return new OpenMetadataArchiveTypeStore(archiveTypeStore);
        }
    }


    /**
     * Set up the TypeStore for this archive.  The TypeStore contains TypeDefs and TypeDef patches.
     *
     * @param archiveTypeStore  OpenMetadataArchiveTypeStore object
     */
    public void setArchiveTypeStore(OpenMetadataArchiveTypeStore archiveTypeStore)
    {
        this.archiveTypeStore = archiveTypeStore;
    }


    /**
     * Return the InstanceStore for this archive. The InstanceStore contains entity and relationship metadata
     * instances.
     *
     * @return OpenMetadataArchiveInstanceStore object
     */
    public OpenMetadataArchiveInstanceStore getArchiveInstanceStore()
    {
        if (archiveInstanceStore == null)
        {
            return null;
        }
        else
        {
            return new OpenMetadataArchiveInstanceStore(archiveInstanceStore);
        }
    }


    /**
     * Set up the InstanceStore for this archive. The InstanceStore contains entity and relationship metadata
     * instances.
     *
     * @param archiveInstanceStore  OpenMetadataArchiveInstanceStore object
     */
    public void setArchiveInstanceStore(OpenMetadataArchiveInstanceStore archiveInstanceStore)
    {
        this.archiveInstanceStore = archiveInstanceStore;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataArchive{" +
                "archiveProperties=" + archiveProperties +
                ", archiveTypeStore=" + archiveTypeStore +
                ", archiveInstanceStore=" + archiveInstanceStore +
                '}';
    }



    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        OpenMetadataArchive that = (OpenMetadataArchive) objectToCompare;
        return Objects.equals(getArchiveProperties(), that.getArchiveProperties()) &&
                Objects.equals(getArchiveTypeStore(), that.getArchiveTypeStore()) &&
                Objects.equals(getArchiveInstanceStore(), that.getArchiveInstanceStore());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getArchiveProperties(), getArchiveTypeStore(), getArchiveInstanceStore());
    }
}
