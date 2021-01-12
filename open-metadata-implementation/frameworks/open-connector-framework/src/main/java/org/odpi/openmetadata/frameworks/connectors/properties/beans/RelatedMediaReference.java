/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelatedMediaReference stores information about an link to an external media file that
 * is relevant to this asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedMediaReference extends ExternalReference
{
    private static final long     serialVersionUID = 1L;

    /*
     * Attributes of a related media reference
     */
    protected RelatedMediaType        mediaType           = null;
    protected List<RelatedMediaUsage> mediaUsageList      = null;


    /**
     * Default constructor
     */
    public RelatedMediaReference()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateRelatedMediaReference element to copy
     */
    public RelatedMediaReference(RelatedMediaReference templateRelatedMediaReference)
    {
        super(templateRelatedMediaReference);

        if (templateRelatedMediaReference != null)
        {
            mediaType = templateRelatedMediaReference.getMediaType();
            mediaUsageList = templateRelatedMediaReference.getMediaUsageList();
        }
    }


    /**
     * Return the type of media referenced.
     *
     * @return related media type enum
     */
    public RelatedMediaType getMediaType() { return mediaType; }


    /**
     * Set up the type of media referenced.
     *
     * @param mediaType related media type enum
     */
    public void setMediaType(RelatedMediaType mediaType)
    {
        this.mediaType = mediaType;
    }


    /**
     * Return the list of recommended uses for the related media.  Null means no usage guidance is available.
     *
     * @return List of RelatedMediaUsage
     */
    public List<RelatedMediaUsage> getMediaUsageList()
    {
        if (mediaUsageList == null)
        {
            return null;
        }
        else
        {
            return new ArrayList<>(mediaUsageList);
        }
    }


    /**
     * Set up the list of recommended uses for the related media.  Null means no usage guidance is available.
     *
     * @param mediaUsageList List of recommended uses for this media
     */
    public void setMediaUsageList(List<RelatedMediaUsage> mediaUsageList)
    {
        this.mediaUsageList = mediaUsageList;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelatedMediaReference{" +
                "mediaId='" + referenceId + '\'' +
                ", linkDescription='" + linkDescription + '\'' +
                ", displayName='" + displayName + '\'' +
                ", uri='" + uri + '\'' +
                ", resourceDescription='" + resourceDescription + '\'' +
                ", version='" + version + '\'' +
                ", organization='" + organization + '\'' +
                ", mediaType=" + mediaType +
                ", mediaUsageList=" + mediaUsageList +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        RelatedMediaReference that = (RelatedMediaReference) objectToCompare;
        return getMediaType() == that.getMediaType() &&
                       Objects.equals(getMediaUsageList(), that.getMediaUsageList());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMediaType(), getMediaUsageList());
    }
}