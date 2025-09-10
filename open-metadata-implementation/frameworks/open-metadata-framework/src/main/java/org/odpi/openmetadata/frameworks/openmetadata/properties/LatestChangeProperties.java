/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.LatestChangeAction;
import org.odpi.openmetadata.frameworks.openmetadata.enums.LatestChangeTarget;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Defines the properties for the LatestChange classification
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LatestChangeProperties extends ClassificationBeanProperties
{
    private LatestChangeTarget latestChangeTarget = null;
    private LatestChangeAction latestChangeAction = null;
    private String             classificationName = null;
    private String             attachmentGUID     = null;
    private String             attachmentTypeName = null;
    private String             relationshipTypeName = null;
    private String             userId             = null;
    private String             actionDescription  = null;


    /**
     * Default constructor
     */
    public LatestChangeProperties()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LatestChangeProperties(LatestChangeProperties template)
    {
        super(template);

        if (template != null)
        {
            latestChangeTarget   = template.getLatestChangeTarget();
            latestChangeAction   = template.getLatestChangeAction();
            classificationName   = template.getClassificationName();
            attachmentGUID       = template.getAttachmentGUID();
            attachmentTypeName   = template.getAttachmentTypeName();
            relationshipTypeName = template.getRelationshipTypeName();
            userId               = template.getUserId();
            actionDescription    = template.getActionDescription();
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
     * Return the name of any changed relationship.
     *
     * @return string name
     */
    public String getRelationshipTypeName()
    {
        return relationshipTypeName;
    }


    /**
     * Set up the name of any changed relationship.
     *
     * @param relationshipTypeName string name
     */
    public void setRelationshipTypeName(String relationshipTypeName)
    {
        this.relationshipTypeName = relationshipTypeName;
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "LatestChangeProperties{" +
                "latestChangeTarget=" + latestChangeTarget +
                ", latestChangeAction=" + latestChangeAction +
                ", classificationName='" + classificationName + '\'' +
                ", attachmentGUID='" + attachmentGUID + '\'' +
                ", attachmentTypeName='" + attachmentTypeName + '\'' +
                ", relationshipTypeName='" + relationshipTypeName + '\'' +
                ", userId='" + userId + '\'' +
                ", actionDescription='" + actionDescription + '\'' +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        LatestChangeProperties that = (LatestChangeProperties) objectToCompare;
        return latestChangeTarget == that.latestChangeTarget &&
                latestChangeAction == that.latestChangeAction &&
                Objects.equals(classificationName, that.classificationName) &&
                Objects.equals(attachmentGUID, that.attachmentGUID) &&
                Objects.equals(attachmentTypeName, that.attachmentTypeName) &&
                Objects.equals(relationshipTypeName, that.relationshipTypeName) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(actionDescription, that.actionDescription);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), latestChangeTarget, latestChangeAction, classificationName, attachmentGUID, attachmentTypeName, relationshipTypeName, userId, actionDescription);
    }
}
