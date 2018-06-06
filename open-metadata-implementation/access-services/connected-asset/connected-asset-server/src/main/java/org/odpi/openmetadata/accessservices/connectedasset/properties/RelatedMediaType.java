/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The RelatedMediaType defines the type of resource referenced in a related media reference.
 * <ul>
 *     <li>Image</li>
 *     <li>Audio</li>
 *     <li>Document</li>
 *     <li>Video</li>
 *     <li>Other</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum RelatedMediaType implements Serializable
{
    IMAGE(0, "Image"),
    AUDIO(1, "Audio"),
    DOCUMENT(2, "Document"),
    VIDEO(3, "Video"),
    OTHER(99, "Other");

    private static final long     serialVersionUID = 1L;

    private int            mediaTypeCode;
    private String         mediaTypeName;


    /**
     * Typical Constructor
     */
    RelatedMediaType(int     mediaTypeCode, String   mediaTypeName)
    {
        /*
         * Save the values supplied
         */
        this.mediaTypeCode = mediaTypeCode;
        this.mediaTypeName = mediaTypeName;

    }


    /**
     * Return the code for this enum instance
     *
     * @return int - media type code
     */
    public int getMediaUsageCode()
    {
        return mediaTypeCode;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String - default name
     */
    public String getMediaUsageName()
    {
        return mediaTypeName;
    }
}