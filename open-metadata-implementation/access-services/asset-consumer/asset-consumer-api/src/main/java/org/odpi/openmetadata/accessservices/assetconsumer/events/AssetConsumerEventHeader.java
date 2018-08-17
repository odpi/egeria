/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.events;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.Asset;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetConsumerEventHeader provides a common base for all events from the access service.
 * It implements Serializable and a version Id.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AssetConsumerEvent.class, name = "AssetConsumerEvent")
})
public abstract class AssetConsumerEventHeader implements Serializable
{
    private static final long serialVersionUID = 1L;


    private long eventVersionId = 1L;


    /**
     * Default Constructor sets the properties to nulls
     */
    AssetConsumerEventHeader()
    {
        /*
         * Nothing to do.
         */
    }


    /**
     * Copy/clone constructor set values from the template
     *
     * @param template object to copy
     */
    AssetConsumerEventHeader(AssetConsumerEventHeader template)
    {
        if (template != null)
        {
            this.eventVersionId = template.getEventVersionId();
        }
    }


    /**
     * Return the event version id for this event structure.
     *
     * @return long
     */
    public long getEventVersionId()
    {
        return eventVersionId;
    }


    /**
     * Set up the event version id for this event structure.
     *
     * @param eventVersionId long
     */
    public void setEventVersionId(long eventVersionId)
    {
        this.eventVersionId = eventVersionId;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetConsumerEventHeader{" +
                "eventVersionId=" + eventVersionId +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof AssetConsumerEventHeader))
        {
            return false;
        }
        AssetConsumerEventHeader that = (AssetConsumerEventHeader) objectToCompare;
        return getEventVersionId() == that.getEventVersionId();
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getEventVersionId());
    }

}
