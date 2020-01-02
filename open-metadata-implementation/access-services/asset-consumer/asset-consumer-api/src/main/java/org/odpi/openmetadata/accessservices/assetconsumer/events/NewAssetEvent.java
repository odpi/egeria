/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.events;


import java.util.Date;

/**
 * NewAssetEvent describes the payload sent when a new Asset is added to the cohort metadata repository.
 */
public class NewAssetEvent extends AssetEvent
{
    private static final long    serialVersionUID = 1L;

    private Date   creationTime = null;

    /**
     * Default constructor
     */
    public NewAssetEvent()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewAssetEvent(NewAssetEvent template)
    {
        super(template);

        if (template != null)
        {
            creationTime = template.getCreationTime();
        }
    }


    /**
     * Return the time that the asset was created.
     *
     * @return date/time
     */
    public Date getCreationTime()
    {
        return creationTime;
    }


    /**
     * Set up the time that the asset was created.
     *
     * @param creationTime date/time
     */
    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NewAssetEvent{" +
                "creationTime=" + creationTime +
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



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */

}
