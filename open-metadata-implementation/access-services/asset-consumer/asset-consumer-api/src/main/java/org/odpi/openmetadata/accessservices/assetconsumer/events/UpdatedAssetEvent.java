/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.events;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

import java.util.Date;
import java.util.Objects;

public class UpdatedAssetEvent extends AssetEvent
{
    private static final long    serialVersionUID = 1L;

    private Date  updateTime    = null;
    private Asset originalAsset = null;


    /**
     * Default constructor
     */
    public UpdatedAssetEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UpdatedAssetEvent(UpdatedAssetEvent template)
    {
        super(template);

        if (template != null)
        {
            updateTime = template.getUpdateTime();
            originalAsset = template.getOriginalAsset();
        }
    }


    /**
     * Return the time that the asset was updated.
     *
     * @return date/time
     */
    public Date getUpdateTime()
    {
        return updateTime;
    }


    /**
     * Set up the time that the asset was updated.
     *
     * @param updateTime date/time
     */
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }


    /**
     * Return the original values of the Asset before the update.
     *
     * @return original asset values or null
     */
    public Asset getOriginalAsset()
    {
        return originalAsset;
    }


    /**
     * Set up details of the original asset - if available.
     *
     * @param originalAsset original asset values.
     */
    public void setOriginalAsset(Asset originalAsset)
    {
        this.originalAsset = originalAsset;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "UpdatedAssetEvent{" +
                "updateTime=" + updateTime +
                ", originalAsset=" + originalAsset +
                ", eventType=" + getEventType() +
                ", asset=" + getAsset() +
                ", eventVersionId=" + getEventVersionId() +
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
        UpdatedAssetEvent that = (UpdatedAssetEvent) objectToCompare;
        return Objects.equals(getUpdateTime(), that.getUpdateTime()) &&
                Objects.equals(getOriginalAsset(), that.getOriginalAsset());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getUpdateTime(), getOriginalAsset());
    }
}
