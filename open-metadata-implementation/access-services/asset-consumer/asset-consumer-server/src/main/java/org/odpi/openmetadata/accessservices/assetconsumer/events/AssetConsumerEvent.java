/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.Asset;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetConsumerEvent describes the structure of the events emitted by the Asset Consumer OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetConsumerEvent implements Serializable
{
    AssetConsumerEventType eventType     = AssetConsumerEventType.UNKNOWN_ASSET_CONSUMER_EVENT;
    Asset                  originalAsset = null;
    Asset                  asset         = null;

    private static final long     serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public AssetConsumerEvent()
    {
    }


    /**
     * Return the type of event.
     *
     * @return event type enum
     */
    public AssetConsumerEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the type of event.
     *
     * @param eventType - event type enum
     */
    public void setEventType(AssetConsumerEventType eventType)
    {
        this.eventType = eventType;
    }


    /**
     * Return the original asset description.
     *
     * @return properties about the asset
     */
    public Asset getOriginalAsset()
    {
        return originalAsset;
    }


    /**
     * Set up the original asset description.
     *
     * @param originalAsset - properties about the asset.
     */
    public void setOriginalAsset(Asset originalAsset)
    {
        this.originalAsset = originalAsset;
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
}
