/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.MediaUsage;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MediaReferenceProperties provides a structure for the properties that link a media reference to an object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MediaReferenceProperties extends RelationshipBeanProperties
{
    private String mediaId     = null;
    private String description = null;
    private MediaUsage mediaUsage        = null;
    private String     mediaUsageOtherId = null;


    /**
     * Default constructor
     */
    public MediaReferenceProperties()
    {
        super();
        super.typeName = OpenMetadataType.MEDIA_REFERENCE_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MediaReferenceProperties(MediaReferenceProperties template)
    {
        super (template);

        if (template != null)
        {
            this.mediaId     = template.getMediaId();
            this.description = template.getDescription();
            this.mediaUsage        = template.getMediaUsage();
            this.mediaUsageOtherId = template.getMediaUsageOtherId();
        }
    }


    /**
     * Return the identifier that this reference is to be known as with respect to the linked object.
     *
     * @return String identifier
     */
    public String getMediaId()
    {
        return mediaId;
    }


    /**
     * Set up the identifier that this reference is to be known as with respect to the linked object.
     *
     * @param mediaId String identifier
     */
    public void setMediaId(String mediaId)
    {
        this.mediaId = mediaId;
    }


    /**
     * Return the description of the external reference with respect to the linked object.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the external reference with respect to the linked object.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the expected use of the media.
     *
     * @return enum
     */
    public MediaUsage getMediaUsage()
    {
        return mediaUsage;
    }


    /**
     * Set up the expected use of the media.
     *
     * @param mediaUsage enum
     */
    public void setMediaUsage(MediaUsage mediaUsage)
    {
        this.mediaUsage = mediaUsage;
    }


    /**
     * Return the other usage type for the reference.
     *
     * @return string
     */
    public String getMediaUsageOtherId()
    {
        return mediaUsageOtherId;
    }


    /**
     * Set up the other usage type for the reference.
     *
     * @param mediaUsageOtherId string
     */
    public void setMediaUsageOtherId(String mediaUsageOtherId)
    {
        this.mediaUsageOtherId = mediaUsageOtherId;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "MediaReferenceProperties{" +
                "referenceId='" + mediaId + '\'' +
                ", description='" + description + '\'' +
                ", mediaUsage=" + mediaUsage +
                ", mediaUsageOtherId='" + mediaUsageOtherId + '\'' +
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
        if (! (objectToCompare instanceof MediaReferenceProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return mediaUsage == that.mediaUsage &&
                Objects.equals(mediaId, that.mediaId) &&
                Objects.equals(description, that.description) &&
                Objects.equals(mediaUsageOtherId, that.mediaUsageOtherId);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), mediaId, description, mediaUsage, mediaUsageOtherId);
    }
}
