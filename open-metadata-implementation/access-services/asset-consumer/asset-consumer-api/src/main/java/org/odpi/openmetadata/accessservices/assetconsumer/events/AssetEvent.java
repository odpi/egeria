/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.events;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetEvent describes the structure of the events emitted by the Asset Consumer OMAS that are about assets.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NewAssetEvent.class, name = "NewAssetEvent"),
        @JsonSubTypes.Type(value = UpdatedAssetEvent.class, name = "UpdatedAssetEvent")
})
public class AssetEvent extends AssetConsumerEvent
{
    private static final long    serialVersionUID = 1L;

    private Asset asset = null;


    /**
     * Default constructor
     */
    public AssetEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetEvent(AssetEvent template)
    {
        super(template);

        if (template != null)
        {
            this.asset = template.getAsset();
        }
    }


    /**
     * Return the asset description.
     *
     * @return properties about the asset
     */
    public Asset getAsset()
    {
        return asset;
    }


    /**
     * Set up the asset description.
     *
     * @param asset - properties about the asset.
     */
    public void setAsset(Asset asset)
    {
        this.asset = asset;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetEvent{" +
                "asset=" + asset +
                ", eventVersionId=" + getEventVersionId() +
                ", eventType=" + getEventType() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetEvent that = (AssetEvent) objectToCompare;
        return Objects.equals(getAsset(), that.getAsset());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getAsset());
    }
}
