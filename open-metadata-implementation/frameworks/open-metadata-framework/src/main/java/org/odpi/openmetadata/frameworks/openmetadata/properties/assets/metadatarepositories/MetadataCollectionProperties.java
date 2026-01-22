/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.metadatarepositories;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MetadataCollectionProperties is a class for representing a topic for an event broker or streaming service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MetadataCollectionProperties extends DataSetProperties
{
    private String managedMetadataCollectionId = null;

    /**
     * Default constructor
     */
    public MetadataCollectionProperties()
    {
        super();
        super.typeName = OpenMetadataType.METADATA_COLLECTION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public MetadataCollectionProperties(MetadataCollectionProperties template)
    {
        super(template);

        if (template != null)
        {
            managedMetadataCollectionId = template.getManagedMetadataCollectionId();
        }
    }


    /**
     * Return the unique identifier of the metadata collection.
     *
     * @return string guid
     */
    public String getManagedMetadataCollectionId()
    {
        return managedMetadataCollectionId;
    }


    /**
     * Set up the unique identifier of the metadata collection.
     *
     * @param managedMetadataCollectionId string guid
     */
    public void setManagedMetadataCollectionId(String managedMetadataCollectionId)
    {
        this.managedMetadataCollectionId = managedMetadataCollectionId;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MetadataCollectionProperties{" +
                "managedMetadataCollectionId='" + managedMetadataCollectionId + '\'' +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        MetadataCollectionProperties that = (MetadataCollectionProperties) objectToCompare;
        return Objects.equals(managedMetadataCollectionId, that.managedMetadataCollectionId);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), managedMetadataCollectionId);
    }
}
