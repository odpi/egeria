/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.feedback;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The LikeProperties object records a single user's "like" of an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LikeProperties extends OpenMetadataRootProperties
{
    private String emoji = null;


    /**
     * Default constructor
     */
    public LikeProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.LIKE.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template   element to copy
     */
    public LikeProperties(LikeProperties template)
    {
        super(template);

        if (template != null)
        {
            emoji = template.getEmoji();
        }
    }


    /**
     * Return an emotion or additional reaction.
     *
     * @return String
     */
    public String getEmoji() {
        return emoji;
    }


    /**
     * Set up an emotion or additional reaction.
     *
     * @param emoji String
     */
    public void setEmoji(String emoji)
    {
        this.emoji = emoji;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LikeProperties{" +
                "emoji='" + emoji + '\'' +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare)) return false;
        LikeProperties like = (LikeProperties) objectToCompare;
        return Objects.equals(getEmoji(), like.getEmoji());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), emoji);
    }
}