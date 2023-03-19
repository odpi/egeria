/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LatestChange is a bean that describes the latest change to an asset universe.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LatestChange extends ElementClassificationHeader
{
    private static final long     serialVersionUID = 1L;

    private LatestChangeTarget latestChangeTarget = null;
    private LatestChangeAction latestChangeAction = null;
    private String             classificationName = null;
    private String             attachmentGUID     = null;
    private String             attachmentTypeName = null;
    private String             userId             = null;
    private String             actionDescription  = null;


    /**
     * Default Constructor
     */
    public LatestChange()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LatestChange(LatestChange template)
    {
        super(template);

        if (template != null)
        {
            latestChangeTarget = template.getLatestChangeTarget();
            latestChangeAction = template.getLatestChangeAction();
            classificationName = template.getClassificationName();
            attachmentGUID     = template.getAttachmentGUID();
            attachmentTypeName = template.getAttachmentTypeName();
            userId             = template.getUserId();
            actionDescription  = template.getActionDescription();
        }
    }


    /**
     * Return the relationship of element that has been changed to the anchor.
     *
     * @return enum
     */
    public LatestChangeTarget getLatestChangeTarget()
    {
        return latestChangeTarget;
    }


    /**
     * Set up  the relationship of element that has been changed to the anchor.
     *
     * @param latestChangeTarget enum
     */
    public void setLatestChangeTarget(LatestChangeTarget latestChangeTarget)
    {
        this.latestChangeTarget = latestChangeTarget;
    }


    /**
     * Return the type of change.
     *
     * @return enum
     */
    public LatestChangeAction getLatestChangeAction()
    {
        return latestChangeAction;
    }


    /**
     * Set up the type of change.
     *
     * @param latestChangeAction enum
     */
    public void setLatestChangeAction(LatestChangeAction latestChangeAction)
    {
        this.latestChangeAction = latestChangeAction;
    }


    /**
     * Return a classification name.  If a classification name changed, this is its name or null.
     *
     * @return string name
     */
    public String getClassificationName()
    {
        return classificationName;
    }


    /**
     * Set up a classification name.  If a classification name changed, this is its name or null.
     *
     * @param classificationName string name
     */
    public void setClassificationName(String classificationName)
    {
        this.classificationName = classificationName;
    }


    /**
     * Return the unique identifier of an element.  If an attached entity or relationship changed, this is its unique identifier.
     *
     * @return string guid
     */
    public String getAttachmentGUID()
    {
        return attachmentGUID;
    }


    /**
     * Set up the unique identifier of an element.  If an attached entity or relationship changed, this is its unique identifier.
     *
     * @param attachmentGUID string guid
     */
    public void setAttachmentGUID(String attachmentGUID)
    {
        this.attachmentGUID = attachmentGUID;
    }


    /**
     * Return the type name. If an attached entity or relationship changed, this is its unique type name.
     *
     * @return string name
     */
    public String getAttachmentTypeName()
    {
        return attachmentTypeName;
    }


    /**
     * Set up the type name. If an attached entity or relationship changed, this is its unique type name.
     *
     * @param attachmentTypeName string name
     */
    public void setAttachmentTypeName(String attachmentTypeName)
    {
        this.attachmentTypeName = attachmentTypeName;
    }


    /**
     * Return the user identifier for the person/system making the change.
     *
     * @return string user id
     */
    public String getUserId()
    {
        return userId;
    }


    /**
     * Set up the user identifier for the person/system making the change.
     *
     * @param userId string user id
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    /**
     * Return the description of the change.
     *
     * @return text
     */
    public String getActionDescription()
    {
        return actionDescription;
    }


    /**
     * Set up the description of the change.
     *
     * @param actionDescription text
     */
    public void setActionDescription(String actionDescription)
    {
        this.actionDescription = actionDescription;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LatestChange{" +
                       "classificationOrigin=" + getClassificationOrigin() +
                       ", classificationOriginGUID='" + getClassificationOriginGUID() + '\'' +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", latestChangeTarget=" + latestChangeTarget +
                       ", latestChangeAction=" + latestChangeAction +
                       ", classificationName='" + classificationName + '\'' +
                       ", attachmentGUID='" + attachmentGUID + '\'' +
                       ", attachmentTypeName='" + attachmentTypeName + '\'' +
                       ", userId='" + userId + '\'' +
                       ", actionDescription='" + actionDescription + '\'' +
                       ", headerVersion=" + getHeaderVersion() +
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
        LatestChange that = (LatestChange) objectToCompare;
        return latestChangeTarget == that.latestChangeTarget &&
                latestChangeAction == that.latestChangeAction &&
                Objects.equals(classificationName, that.classificationName) &&
                Objects.equals(attachmentGUID, that.attachmentGUID) &&
                Objects.equals(attachmentTypeName, that.attachmentTypeName) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(actionDescription, that.actionDescription);
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), latestChangeTarget, latestChangeAction, classificationName, attachmentGUID, attachmentTypeName, userId, actionDescription);
    }
}
