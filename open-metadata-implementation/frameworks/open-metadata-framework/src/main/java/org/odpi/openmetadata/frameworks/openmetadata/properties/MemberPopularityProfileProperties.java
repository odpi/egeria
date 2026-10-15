/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MemberPopularityProfileProperties describes the MemberPopularityProfile classification.  It summarizes the usage
 * activity around the members of an element: a total for each kind of activity, and a profile mapping each
 * member's GUID to its own count.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MemberPopularityProfileProperties extends ClassificationBeanProperties
{
    private long              explicitGetCountTotal = 0L;
    private Map<String, Long> explicitGetCountProfile = null;
    private long              findCountTotal = 0L;
    private Map<String, Long> findCountProfile = null;
    private long              updatePropertyCountTotal = 0L;
    private Map<String, Long> updatePropertyCountProfile = null;
    private long              updateAttachmentCountTotal = 0L;
    private Map<String, Long> updateAttachmentCountProfile = null;
    private long              updateFeedbackCountTotal = 0L;
    private Map<String, Long> updateFeedbackCountProfile = null;
    private long              deleteAttachmentCountTotal = 0L;
    private Map<String, Long> deleteAttachmentCountProfile = null;
    private long              deleteFeedbackCountTotal = 0L;
    private Map<String, Long> deleteFeedbackCountProfile = null;


    /**
     * Default constructor
     */
    public MemberPopularityProfileProperties()
    {
        super();
        super.typeName = OpenMetadataType.MEMBER_POPULARITY_PROFILE_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public MemberPopularityProfileProperties(MemberPopularityProfileProperties template)
    {
        super(template);

        if (template != null)
        {
            explicitGetCountTotal = template.getExplicitGetCountTotal();
            explicitGetCountProfile = template.getExplicitGetCountProfile();
            findCountTotal = template.getFindCountTotal();
            findCountProfile = template.getFindCountProfile();
            updatePropertyCountTotal = template.getUpdatePropertyCountTotal();
            updatePropertyCountProfile = template.getUpdatePropertyCountProfile();
            updateAttachmentCountTotal = template.getUpdateAttachmentCountTotal();
            updateAttachmentCountProfile = template.getUpdateAttachmentCountProfile();
            updateFeedbackCountTotal = template.getUpdateFeedbackCountTotal();
            updateFeedbackCountProfile = template.getUpdateFeedbackCountProfile();
            deleteAttachmentCountTotal = template.getDeleteAttachmentCountTotal();
            deleteAttachmentCountProfile = template.getDeleteAttachmentCountProfile();
            deleteFeedbackCountTotal = template.getDeleteFeedbackCountTotal();
            deleteFeedbackCountProfile = template.getDeleteFeedbackCountProfile();
        }
    }


    /**
     * Return the number of times any of the members has been explicitly retrieved from the repository by an identifier or name.
     *
     * @return long
     */
    public long getExplicitGetCountTotal()
    {
        return explicitGetCountTotal;
    }


    /**
     * Set up the number of times any of the members has been explicitly retrieved from the repository by an identifier or name.
     *
     * @param explicitGetCountTotal long
     */
    public void setExplicitGetCountTotal(long explicitGetCountTotal)
    {
        this.explicitGetCountTotal = explicitGetCountTotal;
    }


    /**
     * Return a map of member GUID to the number of times that member has been explicitly retrieved from the repository by an identifier or name.
     *
     * @return map
     */
    public Map<String, Long> getExplicitGetCountProfile()
    {
        return explicitGetCountProfile;
    }


    /**
     * Set up a map of member GUID to the number of times that member has been explicitly retrieved from the repository by an identifier or name.
     *
     * @param explicitGetCountProfile map
     */
    public void setExplicitGetCountProfile(Map<String, Long> explicitGetCountProfile)
    {
        this.explicitGetCountProfile = explicitGetCountProfile;
    }


    /**
     * Return the number of times any of the members has been retrieved from the repository as part of a search request.
     *
     * @return long
     */
    public long getFindCountTotal()
    {
        return findCountTotal;
    }


    /**
     * Set up the number of times any of the members has been retrieved from the repository as part of a search request.
     *
     * @param findCountTotal long
     */
    public void setFindCountTotal(long findCountTotal)
    {
        this.findCountTotal = findCountTotal;
    }


    /**
     * Return a map of member GUID to the number of times that member has been retrieved from the repository as part of a search request.
     *
     * @return map
     */
    public Map<String, Long> getFindCountProfile()
    {
        return findCountProfile;
    }


    /**
     * Set up a map of member GUID to the number of times that member has been retrieved from the repository as part of a search request.
     *
     * @param findCountProfile map
     */
    public void setFindCountProfile(Map<String, Long> findCountProfile)
    {
        this.findCountProfile = findCountProfile;
    }


    /**
     * Return the number of times any of the members has had its properties updated.
     *
     * @return long
     */
    public long getUpdatePropertyCountTotal()
    {
        return updatePropertyCountTotal;
    }


    /**
     * Set up the number of times any of the members has had its properties updated.
     *
     * @param updatePropertyCountTotal long
     */
    public void setUpdatePropertyCountTotal(long updatePropertyCountTotal)
    {
        this.updatePropertyCountTotal = updatePropertyCountTotal;
    }


    /**
     * Return a map of member GUID to the number of times that member has had its properties updated.
     *
     * @return map
     */
    public Map<String, Long> getUpdatePropertyCountProfile()
    {
        return updatePropertyCountProfile;
    }


    /**
     * Set up a map of member GUID to the number of times that member has had its properties updated.
     *
     * @param updatePropertyCountProfile map
     */
    public void setUpdatePropertyCountProfile(Map<String, Long> updatePropertyCountProfile)
    {
        this.updatePropertyCountProfile = updatePropertyCountProfile;
    }


    /**
     * Return the number of times any of the members has had an element anchored to it created, linked, unlinked or updated.
     *
     * @return long
     */
    public long getUpdateAttachmentCountTotal()
    {
        return updateAttachmentCountTotal;
    }


    /**
     * Set up the number of times any of the members has had an element anchored to it created, linked, unlinked or updated.
     *
     * @param updateAttachmentCountTotal long
     */
    public void setUpdateAttachmentCountTotal(long updateAttachmentCountTotal)
    {
        this.updateAttachmentCountTotal = updateAttachmentCountTotal;
    }


    /**
     * Return a map of member GUID to the number of times that member has had an element anchored to it created, linked, unlinked or updated.
     *
     * @return map
     */
    public Map<String, Long> getUpdateAttachmentCountProfile()
    {
        return updateAttachmentCountProfile;
    }


    /**
     * Set up a map of member GUID to the number of times that member has had an element anchored to it created, linked, unlinked or updated.
     *
     * @param updateAttachmentCountProfile map
     */
    public void setUpdateAttachmentCountProfile(Map<String, Long> updateAttachmentCountProfile)
    {
        this.updateAttachmentCountProfile = updateAttachmentCountProfile;
    }


    /**
     * Return the number of times any of the members has had feedback (comments, likes, ratings and informal tags) added or updated.
     *
     * @return long
     */
    public long getUpdateFeedbackCountTotal()
    {
        return updateFeedbackCountTotal;
    }


    /**
     * Set up the number of times any of the members has had feedback (comments, likes, ratings and informal tags) added or updated.
     *
     * @param updateFeedbackCountTotal long
     */
    public void setUpdateFeedbackCountTotal(long updateFeedbackCountTotal)
    {
        this.updateFeedbackCountTotal = updateFeedbackCountTotal;
    }


    /**
     * Return a map of member GUID to the number of times that member has had feedback (comments, likes, ratings and informal tags) added or updated.
     *
     * @return map
     */
    public Map<String, Long> getUpdateFeedbackCountProfile()
    {
        return updateFeedbackCountProfile;
    }


    /**
     * Set up a map of member GUID to the number of times that member has had feedback (comments, likes, ratings and informal tags) added or updated.
     *
     * @param updateFeedbackCountProfile map
     */
    public void setUpdateFeedbackCountProfile(Map<String, Long> updateFeedbackCountProfile)
    {
        this.updateFeedbackCountProfile = updateFeedbackCountProfile;
    }


    /**
     * Return the number of times any of the members has had an element anchored to it deleted.
     *
     * @return long
     */
    public long getDeleteAttachmentCountTotal()
    {
        return deleteAttachmentCountTotal;
    }


    /**
     * Set up the number of times any of the members has had an element anchored to it deleted.
     *
     * @param deleteAttachmentCountTotal long
     */
    public void setDeleteAttachmentCountTotal(long deleteAttachmentCountTotal)
    {
        this.deleteAttachmentCountTotal = deleteAttachmentCountTotal;
    }


    /**
     * Return a map of member GUID to the number of times that member has had an element anchored to it deleted.
     *
     * @return map
     */
    public Map<String, Long> getDeleteAttachmentCountProfile()
    {
        return deleteAttachmentCountProfile;
    }


    /**
     * Set up a map of member GUID to the number of times that member has had an element anchored to it deleted.
     *
     * @param deleteAttachmentCountProfile map
     */
    public void setDeleteAttachmentCountProfile(Map<String, Long> deleteAttachmentCountProfile)
    {
        this.deleteAttachmentCountProfile = deleteAttachmentCountProfile;
    }


    /**
     * Return the number of times any of the members has had feedback (comments, likes, ratings and informal tags) deleted.
     *
     * @return long
     */
    public long getDeleteFeedbackCountTotal()
    {
        return deleteFeedbackCountTotal;
    }


    /**
     * Set up the number of times any of the members has had feedback (comments, likes, ratings and informal tags) deleted.
     *
     * @param deleteFeedbackCountTotal long
     */
    public void setDeleteFeedbackCountTotal(long deleteFeedbackCountTotal)
    {
        this.deleteFeedbackCountTotal = deleteFeedbackCountTotal;
    }


    /**
     * Return a map of member GUID to the number of times that member has had feedback (comments, likes, ratings and informal tags) deleted.
     *
     * @return map
     */
    public Map<String, Long> getDeleteFeedbackCountProfile()
    {
        return deleteFeedbackCountProfile;
    }


    /**
     * Set up a map of member GUID to the number of times that member has had feedback (comments, likes, ratings and informal tags) deleted.
     *
     * @param deleteFeedbackCountProfile map
     */
    public void setDeleteFeedbackCountProfile(Map<String, Long> deleteFeedbackCountProfile)
    {
        this.deleteFeedbackCountProfile = deleteFeedbackCountProfile;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MemberPopularityProfileProperties{" +
                "explicitGetCountTotal=" + explicitGetCountTotal +
                ", explicitGetCountProfile=" + explicitGetCountProfile +
                ", findCountTotal=" + findCountTotal +
                ", findCountProfile=" + findCountProfile +
                ", updatePropertyCountTotal=" + updatePropertyCountTotal +
                ", updatePropertyCountProfile=" + updatePropertyCountProfile +
                ", updateAttachmentCountTotal=" + updateAttachmentCountTotal +
                ", updateAttachmentCountProfile=" + updateAttachmentCountProfile +
                ", updateFeedbackCountTotal=" + updateFeedbackCountTotal +
                ", updateFeedbackCountProfile=" + updateFeedbackCountProfile +
                ", deleteAttachmentCountTotal=" + deleteAttachmentCountTotal +
                ", deleteAttachmentCountProfile=" + deleteAttachmentCountProfile +
                ", deleteFeedbackCountTotal=" + deleteFeedbackCountTotal +
                ", deleteFeedbackCountProfile=" + deleteFeedbackCountProfile +
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
        MemberPopularityProfileProperties that = (MemberPopularityProfileProperties) objectToCompare;
        return explicitGetCountTotal == that.explicitGetCountTotal &&
               Objects.equals(explicitGetCountProfile, that.explicitGetCountProfile) &&
               findCountTotal == that.findCountTotal &&
               Objects.equals(findCountProfile, that.findCountProfile) &&
               updatePropertyCountTotal == that.updatePropertyCountTotal &&
               Objects.equals(updatePropertyCountProfile, that.updatePropertyCountProfile) &&
               updateAttachmentCountTotal == that.updateAttachmentCountTotal &&
               Objects.equals(updateAttachmentCountProfile, that.updateAttachmentCountProfile) &&
               updateFeedbackCountTotal == that.updateFeedbackCountTotal &&
               Objects.equals(updateFeedbackCountProfile, that.updateFeedbackCountProfile) &&
               deleteAttachmentCountTotal == that.deleteAttachmentCountTotal &&
               Objects.equals(deleteAttachmentCountProfile, that.deleteAttachmentCountProfile) &&
               deleteFeedbackCountTotal == that.deleteFeedbackCountTotal &&
               Objects.equals(deleteFeedbackCountProfile, that.deleteFeedbackCountProfile);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), explicitGetCountTotal, explicitGetCountProfile, findCountTotal, findCountProfile, updatePropertyCountTotal, updatePropertyCountProfile, updateAttachmentCountTotal, updateAttachmentCountProfile, updateFeedbackCountTotal, updateFeedbackCountProfile, deleteAttachmentCountTotal, deleteAttachmentCountProfile, deleteFeedbackCountTotal, deleteFeedbackCountProfile);
    }
}
