/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UsageActivityProperties describes the UsageActivity classification.  It is attached to an anchor element
 * (typically an Asset, DigitalProduct or GlossaryTerm) and counts the activity around the element and its
 * anchored elements: how often it is explicitly retrieved, how often it is returned by a search, and how often its
 * properties, attachments and feedback are updated or deleted.  Each kind of activity is counted separately.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UsageActivityProperties extends ClassificationBeanProperties
{
    private Long explicitGetCount = null;
    private Long findCount = null;
    private Long updatePropertyCount = null;
    private Long updateAttachmentCount = null;
    private Long updateFeedbackCount = null;
    private Long deleteAttachmentCount = null;
    private Long deleteFeedbackCount = null;


    /**
     * Default constructor
     */
    public UsageActivityProperties()
    {
        super();
        super.typeName = OpenMetadataType.USAGE_ACTIVITY_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public UsageActivityProperties(UsageActivityProperties template)
    {
        super(template);

        if (template != null)
        {
            explicitGetCount = template.getExplicitGetCount();
            findCount = template.getFindCount();
            updatePropertyCount = template.getUpdatePropertyCount();
            updateAttachmentCount = template.getUpdateAttachmentCount();
            updateFeedbackCount = template.getUpdateFeedbackCount();
            deleteAttachmentCount = template.getDeleteAttachmentCount();
            deleteFeedbackCount = template.getDeleteFeedbackCount();
        }
    }


    /**
     * Return the number of times the element, or one of its anchored elements, has been explicitly retrieved from the repository by an identifier or name.
     *
     * @return long
     */
    public Long getExplicitGetCount()
    {
        return explicitGetCount;
    }


    /**
     * Set up the number of times the element, or one of its anchored elements, has been explicitly retrieved from the repository by an identifier or name.
     *
     * @param explicitGetCount long
     */
    public void setExplicitGetCount(Long explicitGetCount)
    {
        this.explicitGetCount = explicitGetCount;
    }


    /**
     * Return the number of times the element, or one of its anchored elements, has been retrieved from the repository as part of a search request.
     *
     * @return long
     */
    public Long getFindCount()
    {
        return findCount;
    }


    /**
     * Set up the number of times the element, or one of its anchored elements, has been retrieved from the repository as part of a search request.
     *
     * @param findCount long
     */
    public void setFindCount(Long findCount)
    {
        this.findCount = findCount;
    }


    /**
     * Return the number of times the properties of the element have been updated.
     *
     * @return long
     */
    public Long getUpdatePropertyCount()
    {
        return updatePropertyCount;
    }


    /**
     * Set up the number of times the properties of the element have been updated.
     *
     * @param updatePropertyCount long
     */
    public void setUpdatePropertyCount(Long updatePropertyCount)
    {
        this.updatePropertyCount = updatePropertyCount;
    }


    /**
     * Return the number of times an element anchored to the element has been created, linked, unlinked or updated.
     *
     * @return long
     */
    public Long getUpdateAttachmentCount()
    {
        return updateAttachmentCount;
    }


    /**
     * Set up the number of times an element anchored to the element has been created, linked, unlinked or updated.
     *
     * @param updateAttachmentCount long
     */
    public void setUpdateAttachmentCount(Long updateAttachmentCount)
    {
        this.updateAttachmentCount = updateAttachmentCount;
    }


    /**
     * Return the number of times feedback (comments, likes, ratings and informal tags) attached to the element has been added or updated.
     *
     * @return long
     */
    public Long getUpdateFeedbackCount()
    {
        return updateFeedbackCount;
    }


    /**
     * Set up the number of times feedback (comments, likes, ratings and informal tags) attached to the element has been added or updated.
     *
     * @param updateFeedbackCount long
     */
    public void setUpdateFeedbackCount(Long updateFeedbackCount)
    {
        this.updateFeedbackCount = updateFeedbackCount;
    }


    /**
     * Return the number of times an element anchored to the element has been deleted.
     *
     * @return long
     */
    public Long getDeleteAttachmentCount()
    {
        return deleteAttachmentCount;
    }


    /**
     * Set up the number of times an element anchored to the element has been deleted.
     *
     * @param deleteAttachmentCount long
     */
    public void setDeleteAttachmentCount(Long deleteAttachmentCount)
    {
        this.deleteAttachmentCount = deleteAttachmentCount;
    }


    /**
     * Return the number of times feedback (comments, likes, ratings and informal tags) attached to the element has been deleted.
     *
     * @return long
     */
    public Long getDeleteFeedbackCount()
    {
        return deleteFeedbackCount;
    }


    /**
     * Set up the number of times feedback (comments, likes, ratings and informal tags) attached to the element has been deleted.
     *
     * @param deleteFeedbackCount long
     */
    public void setDeleteFeedbackCount(Long deleteFeedbackCount)
    {
        this.deleteFeedbackCount = deleteFeedbackCount;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "UsageActivityProperties{" +
                "explicitGetCount=" + explicitGetCount +
                ", findCount=" + findCount +
                ", updatePropertyCount=" + updatePropertyCount +
                ", updateAttachmentCount=" + updateAttachmentCount +
                ", updateFeedbackCount=" + updateFeedbackCount +
                ", deleteAttachmentCount=" + deleteAttachmentCount +
                ", deleteFeedbackCount=" + deleteFeedbackCount +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        UsageActivityProperties that = (UsageActivityProperties) objectToCompare;
        return Objects.equals(explicitGetCount, that.explicitGetCount) &&
               Objects.equals(findCount, that.findCount) &&
               Objects.equals(updatePropertyCount, that.updatePropertyCount) &&
               Objects.equals(updateAttachmentCount, that.updateAttachmentCount) &&
               Objects.equals(updateFeedbackCount, that.updateFeedbackCount) &&
               Objects.equals(deleteAttachmentCount, that.deleteAttachmentCount) &&
               Objects.equals(deleteFeedbackCount, that.deleteFeedbackCount);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), explicitGetCount, findCount, updatePropertyCount, updateAttachmentCount, updateFeedbackCount, deleteAttachmentCount, deleteFeedbackCount);
    }
}
