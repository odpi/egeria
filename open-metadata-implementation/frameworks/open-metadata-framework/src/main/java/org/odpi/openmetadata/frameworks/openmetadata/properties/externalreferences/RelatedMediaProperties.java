/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.MediaType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.MediaUsage;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelatedMediaProperties stores information about an link to an external media file that
 * is relevant to open metadata.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedMediaProperties extends ExternalReferenceProperties
{
    /*
     * Attributes of a related media reference
     */
    private MediaType  mediaType                = null;
    private String     mediaTypeOtherId         = null;
    private MediaUsage defaultMediaUsage        = null;
    private String     defaultMediaUsageOtherId = null;


    /**
     * Default constructor
     */
    public RelatedMediaProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.RELATED_MEDIA.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public RelatedMediaProperties(RelatedMediaProperties template)
    {
        super(template);

        if (template != null)
        {
            mediaType         = template.getMediaType();
            defaultMediaUsage = template.getDefaultMediaUsage();
        }
    }


    /**
     * Return the type of media referenced.
     *
     * @return related media type enum
     */
    public MediaType getMediaType() { return mediaType; }


    /**
     * Set up the type of media referenced.
     *
     * @param mediaType related media type enum
     */
    public void setMediaType(MediaType mediaType)
    {
        this.mediaType = mediaType;
    }


    /**
     * Return the other id.
     *
     * @return string
     */
    public String getMediaTypeOtherId()
    {
        return mediaTypeOtherId;
    }


    /**
     * Set up the other id.
     *
     * @param mediaTypeOtherId string
     */
    public void setMediaTypeOtherId(String mediaTypeOtherId)
    {
        this.mediaTypeOtherId = mediaTypeOtherId;
    }


    /**
     * Return the list of recommended uses for the related media.  Null means no usage guidance is available.
     *
     * @return List of MediaUsage
     */
    public MediaUsage getDefaultMediaUsage()
    {
        return defaultMediaUsage;
    }


    /**
     * Set up the list of recommended uses for the related media.  Null means no usage guidance is available.
     *
     * @param defaultMediaUsage List of recommended uses for this media
     */
    public void setDefaultMediaUsage(MediaUsage defaultMediaUsage)
    {
        this.defaultMediaUsage = defaultMediaUsage;
    }


    /**
     * Return the other Id
     *
     * @return string
     */
    public String getDefaultMediaUsageOtherId()
    {
        return defaultMediaUsageOtherId;
    }

    /**
     * Set up the other id
     *
     * @param defaultMediaUsageOtherId string
     */
    public void setDefaultMediaUsageOtherId(String defaultMediaUsageOtherId)
    {
        this.defaultMediaUsageOtherId = defaultMediaUsageOtherId;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelatedMediaProperties{" +
                "mediaType=" + mediaType +
                ", mediaTypeOtherId='" + mediaTypeOtherId + '\'' +
                ", defaultMediaUsage=" + defaultMediaUsage +
                ", defaultMediaUsageOtherId='" + defaultMediaUsageOtherId + '\'' +
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
        RelatedMediaProperties that = (RelatedMediaProperties) objectToCompare;
        return mediaType == that.mediaType &&
                Objects.equals(mediaTypeOtherId, that.mediaTypeOtherId) &&
                defaultMediaUsage == that.defaultMediaUsage &&
                Objects.equals(defaultMediaUsageOtherId, that.defaultMediaUsageOtherId);
    }

    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), mediaType, mediaTypeOtherId, defaultMediaUsage, defaultMediaUsageOtherId);
    }
}