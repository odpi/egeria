/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LastAttachment describes the last attachment to be added to a Referenceable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LastAttachment extends ElementHeader
{
    private static final long    serialVersionUID = 1L;

    private String anchorGUID      = null;
    private String anchorType      = null;
    private String attachmentGUID  = null;
    private String attachmentType  = null;
    private String attachmentOwner = null;
    private String description     = null;
    private Date   updateTime      = null;


    /**
     * Default constructor
     */
    public LastAttachment()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LastAttachment(LastAttachment template)
    {
        super(template);

        if (template != null)
        {
            anchorGUID = template.getAnchorGUID();
            anchorType = template.getAnchorType();
            attachmentGUID = template.getAttachmentGUID();
            attachmentType = template.getAttachmentType();
            attachmentOwner = template.getAttachmentOwner();
            description = template.getDescription();
            updateTime = template.getUpdateTime();
        }
    }


    /**
     * Return the unique identifier of the referenceable
     *
     * @return guid
     */
    public String getAnchorGUID()
    {
        return anchorGUID;
    }


    /**
     * Set up the unique identifier of the referenceable
     *
     * @param anchorGUID guid
     */
    public void setAnchorGUID(String anchorGUID)
    {
        this.anchorGUID = anchorGUID;
    }


    /**
     * Return the type name of the referenceable.
     *
     * @return string type name
     */
    public String getAnchorType()
    {
        return anchorType;
    }


    /**
     * Set up the type name of the referenceable.
     *
     * @param anchorType string type name
     */
    public void setAnchorType(String anchorType)
    {
        this.anchorType = anchorType;
    }


    /**
     * Return the unique identifier of the attached entity.
     *
     * @return guid
     */
    public String getAttachmentGUID()
    {
        return attachmentGUID;
    }


    /**
     * Set up the unique identifier of the attached entity.
     *
     * @param attachmentGUID guid
     */
    public void setAttachmentGUID(String attachmentGUID)
    {
        this.attachmentGUID = attachmentGUID;
    }


    /**
     * Return the type of the attached entity.
     *
     * @return string type name
     */
    public String getAttachmentType()
    {
        return attachmentType;
    }


    /**
     * Set up the type of the attached entity.
     *
     * @param attachmentType string type name
     */
    public void setAttachmentType(String attachmentType)
    {
        this.attachmentType = attachmentType;
    }


    /**
     * Return the user id of the person/engine that created the attachment.
     *
     * @return string user id
     */
    public String getAttachmentOwner()
    {
        return attachmentOwner;
    }


    /**
     * Set up the user id of the person/engine that created the attachment.
     *
     * @param attachmentOwner string user id
     */
    public void setAttachmentOwner(String attachmentOwner)
    {
        this.attachmentOwner = attachmentOwner;
    }


    /**
     * Return the human readable description of the attachment.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the human readable description of the attachment.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the time that the attachment was made/changed.
     *
     * @return date/time
     */
    public Date getUpdateTime()
    {
        return updateTime;
    }


    /**
     * Set up the time that the attachment was made/changed.
     *
     * @param updateTime date/time
     */
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "LastAttachment{" +
                       "anchorGUID='" + anchorGUID + '\'' +
                       ", anchorType='" + anchorType + '\'' +
                       ", attachmentGUID='" + attachmentGUID + '\'' +
                       ", attachmentType='" + attachmentType + '\'' +
                       ", attachmentOwner='" + attachmentOwner + '\'' +
                       ", description='" + description + '\'' +
                       ", updateTime=" + updateTime +
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
        LastAttachment that = (LastAttachment) objectToCompare;
        return Objects.equals(getAnchorGUID(), that.getAnchorGUID()) &&
                       Objects.equals(getAnchorType(), that.getAnchorType()) &&
                       Objects.equals(getAttachmentGUID(), that.getAttachmentGUID()) &&
                       Objects.equals(getAttachmentType(), that.getAttachmentType()) &&
                       Objects.equals(getAttachmentOwner(), that.getAttachmentOwner()) &&
                       Objects.equals(getDescription(), that.getDescription()) &&
                       Objects.equals(getUpdateTime(), that.getUpdateTime());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getAnchorGUID(), getAnchorType(), getAttachmentGUID(), getAttachmentType(),
                            getAttachmentOwner(),
                            getDescription(), getUpdateTime());
    }
}
