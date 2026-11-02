/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.usageinsight.popularity;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MemberPopularityProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.UsageActivityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * MemberActivity holds the seven usage activity counts for an element: the counts from its own UsageActivity
 * classification, plus - for a collection - the totals of the activity of its members.  It is the value that an
 * element contributes to the MemberPopularityProfile of each collection it is a member of.
 */
public class MemberActivity
{
    private long explicitGetCount      = 0L;
    private long findCount             = 0L;
    private long updatePropertyCount   = 0L;
    private long updateAttachmentCount = 0L;
    private long updateFeedbackCount   = 0L;
    private long deleteAttachmentCount = 0L;
    private long deleteFeedbackCount   = 0L;


    /**
     * Default constructor - all counts are zero.
     */
    public MemberActivity()
    {
    }


    /**
     * Return the activity recorded in an element's UsageActivity classification.
     *
     * @param usageActivity classification from the element's header (may be null)
     * @return activity - all zero if the element is not classified
     */
    public static MemberActivity fromUsageActivity(ElementClassification usageActivity)
    {
        MemberActivity activity = new MemberActivity();

        if ((usageActivity != null) && (usageActivity.getClassificationProperties() instanceof UsageActivityProperties properties))
        {
            activity.explicitGetCount      = OpenMetadataProperty.EXPLICIT_GET_COUNT.longValueOrDefault(properties.getExplicitGetCount());
            activity.findCount             = OpenMetadataProperty.FIND_COUNT.longValueOrDefault(properties.getFindCount());
            activity.updatePropertyCount   = OpenMetadataProperty.UPDATE_PROPERTY_COUNT.longValueOrDefault(properties.getUpdatePropertyCount());
            activity.updateAttachmentCount = OpenMetadataProperty.UPDATE_ATTACHMENT_COUNT.longValueOrDefault(properties.getUpdateAttachmentCount());
            activity.updateFeedbackCount   = OpenMetadataProperty.UPDATE_FEEDBACK_COUNT.longValueOrDefault(properties.getUpdateFeedbackCount());
            activity.deleteAttachmentCount = OpenMetadataProperty.DELETE_ATTACHMENT_COUNT.longValueOrDefault(properties.getDeleteAttachmentCount());
            activity.deleteFeedbackCount   = OpenMetadataProperty.DELETE_FEEDBACK_COUNT.longValueOrDefault(properties.getDeleteFeedbackCount());
        }

        return activity;
    }


    /**
     * Return the totals recorded in an element's MemberPopularityProfile classification.
     *
     * @param memberPopularityProfile classification from the element's header (may be null)
     * @return activity - all zero if the element is not classified
     */
    public static MemberActivity fromMemberPopularityProfile(ElementClassification memberPopularityProfile)
    {
        MemberActivity activity = new MemberActivity();

        if ((memberPopularityProfile != null) && (memberPopularityProfile.getClassificationProperties() instanceof MemberPopularityProfileProperties properties))
        {
            activity.explicitGetCount      = OpenMetadataProperty.EXPLICIT_GET_COUNT_TOTAL.longValueOrDefault(properties.getExplicitGetCountTotal());
            activity.findCount             = OpenMetadataProperty.FIND_COUNT_TOTAL.longValueOrDefault(properties.getFindCountTotal());
            activity.updatePropertyCount   = OpenMetadataProperty.UPDATE_PROPERTY_COUNT_TOTAL.longValueOrDefault(properties.getUpdatePropertyCountTotal());
            activity.updateAttachmentCount = OpenMetadataProperty.UPDATE_ATTACHMENT_COUNT_TOTAL.longValueOrDefault(properties.getUpdateAttachmentCountTotal());
            activity.updateFeedbackCount   = OpenMetadataProperty.UPDATE_FEEDBACK_COUNT_TOTAL.longValueOrDefault(properties.getUpdateFeedbackCountTotal());
            activity.deleteAttachmentCount = OpenMetadataProperty.DELETE_ATTACHMENT_COUNT_TOTAL.longValueOrDefault(properties.getDeleteAttachmentCountTotal());
            activity.deleteFeedbackCount   = OpenMetadataProperty.DELETE_FEEDBACK_COUNT_TOTAL.longValueOrDefault(properties.getDeleteFeedbackCountTotal());
        }

        return activity;
    }


    /**
     * Add another element's activity to this one.
     *
     * @param other activity to add (may be null)
     */
    public void add(MemberActivity other)
    {
        if (other != null)
        {
            explicitGetCount      += other.explicitGetCount;
            findCount             += other.findCount;
            updatePropertyCount   += other.updatePropertyCount;
            updateAttachmentCount += other.updateAttachmentCount;
            updateFeedbackCount   += other.updateFeedbackCount;
            deleteAttachmentCount += other.deleteAttachmentCount;
            deleteFeedbackCount   += other.deleteFeedbackCount;
        }
    }


    /**
     * Return whether there has been no activity at all.
     *
     * @return boolean
     */
    public boolean isEmpty()
    {
        return (explicitGetCount == 0L) && (findCount == 0L) && (updatePropertyCount == 0L) && (updateAttachmentCount == 0L) &&
               (updateFeedbackCount == 0L) && (deleteAttachmentCount == 0L) && (deleteFeedbackCount == 0L);
    }


    /**
     * Build the MemberPopularityProfile for a collection from the activity of each of its members.  The totals are the
     * sum of the members' activity, and each profile maps a member's GUID to that member's count.  Members with a zero
     * count are left out of a profile so that the maps only describe the members that have been used.
     *
     * @param membersActivity map of member GUID to that member's activity
     * @return classification properties
     */
    public static MemberPopularityProfileProperties getMemberPopularityProfile(Map<String, MemberActivity> membersActivity)
    {
        MemberActivity    totals                  = new MemberActivity();
        Map<String, Long> explicitGetCountProfile = new HashMap<>();
        Map<String, Long> findCountProfile        = new HashMap<>();
        Map<String, Long> updatePropertyProfile   = new HashMap<>();
        Map<String, Long> updateAttachmentProfile = new HashMap<>();
        Map<String, Long> updateFeedbackProfile   = new HashMap<>();
        Map<String, Long> deleteAttachmentProfile = new HashMap<>();
        Map<String, Long> deleteFeedbackProfile   = new HashMap<>();

        if (membersActivity != null)
        {
            for (String memberGUID : membersActivity.keySet())
            {
                MemberActivity activity = membersActivity.get(memberGUID);

                if (activity != null)
                {
                    totals.add(activity);

                    addToProfile(explicitGetCountProfile, memberGUID, activity.explicitGetCount);
                    addToProfile(findCountProfile, memberGUID, activity.findCount);
                    addToProfile(updatePropertyProfile, memberGUID, activity.updatePropertyCount);
                    addToProfile(updateAttachmentProfile, memberGUID, activity.updateAttachmentCount);
                    addToProfile(updateFeedbackProfile, memberGUID, activity.updateFeedbackCount);
                    addToProfile(deleteAttachmentProfile, memberGUID, activity.deleteAttachmentCount);
                    addToProfile(deleteFeedbackProfile, memberGUID, activity.deleteFeedbackCount);
                }
            }
        }

        MemberPopularityProfileProperties properties = new MemberPopularityProfileProperties();

        properties.setExplicitGetCountTotal(totals.explicitGetCount);
        properties.setExplicitGetCountProfile(explicitGetCountProfile);
        properties.setFindCountTotal(totals.findCount);
        properties.setFindCountProfile(findCountProfile);
        properties.setUpdatePropertyCountTotal(totals.updatePropertyCount);
        properties.setUpdatePropertyCountProfile(updatePropertyProfile);
        properties.setUpdateAttachmentCountTotal(totals.updateAttachmentCount);
        properties.setUpdateAttachmentCountProfile(updateAttachmentProfile);
        properties.setUpdateFeedbackCountTotal(totals.updateFeedbackCount);
        properties.setUpdateFeedbackCountProfile(updateFeedbackProfile);
        properties.setDeleteAttachmentCountTotal(totals.deleteAttachmentCount);
        properties.setDeleteAttachmentCountProfile(deleteAttachmentProfile);
        properties.setDeleteFeedbackCountTotal(totals.deleteFeedbackCount);
        properties.setDeleteFeedbackCountProfile(deleteFeedbackProfile);

        return properties;
    }


    /**
     * Return whether two profiles hold the same counts.  Only the fourteen count attributes are compared - the
     * profile read back from the repository carries other values (such as its type name) that a newly built one
     * does not, and they must not make an unchanged profile look changed.
     *
     * @param existing profile currently stored (may be null)
     * @param latest newly built profile
     * @return boolean
     */
    public static boolean sameCounts(MemberPopularityProfileProperties existing,
                                     MemberPopularityProfileProperties latest)
    {
        if ((existing == null) || (latest == null))
        {
            return false;
        }

        return Objects.equals(existing.getExplicitGetCountTotal(), latest.getExplicitGetCountTotal()) &&
               Objects.equals(existing.getFindCountTotal(), latest.getFindCountTotal()) &&
               Objects.equals(existing.getUpdatePropertyCountTotal(), latest.getUpdatePropertyCountTotal()) &&
               Objects.equals(existing.getUpdateAttachmentCountTotal(), latest.getUpdateAttachmentCountTotal()) &&
               Objects.equals(existing.getUpdateFeedbackCountTotal(), latest.getUpdateFeedbackCountTotal()) &&
               Objects.equals(existing.getDeleteAttachmentCountTotal(), latest.getDeleteAttachmentCountTotal()) &&
               Objects.equals(existing.getDeleteFeedbackCountTotal(), latest.getDeleteFeedbackCountTotal()) &&
               sameProfile(existing.getExplicitGetCountProfile(), latest.getExplicitGetCountProfile()) &&
               sameProfile(existing.getFindCountProfile(), latest.getFindCountProfile()) &&
               sameProfile(existing.getUpdatePropertyCountProfile(), latest.getUpdatePropertyCountProfile()) &&
               sameProfile(existing.getUpdateAttachmentCountProfile(), latest.getUpdateAttachmentCountProfile()) &&
               sameProfile(existing.getUpdateFeedbackCountProfile(), latest.getUpdateFeedbackCountProfile()) &&
               sameProfile(existing.getDeleteAttachmentCountProfile(), latest.getDeleteAttachmentCountProfile()) &&
               sameProfile(existing.getDeleteFeedbackCountProfile(), latest.getDeleteFeedbackCountProfile());
    }


    /**
     * Compare two profile maps, treating null and empty as the same - an empty map is not stored.
     *
     * @param existing stored map
     * @param latest new map
     * @return boolean
     */
    private static boolean sameProfile(Map<String, Long> existing,
                                       Map<String, Long> latest)
    {
        Map<String, Long> existingMap = (existing == null) ? Map.of() : existing;
        Map<String, Long> latestMap   = (latest == null) ? Map.of() : latest;

        return existingMap.equals(latestMap);
    }


    /**
     * Add a member's count to a profile map if it is not zero.
     *
     * @param profile map of member GUID to count
     * @param memberGUID member
     * @param count count for this member
     */
    private static void addToProfile(Map<String, Long> profile,
                                     String            memberGUID,
                                     long              count)
    {
        if (count != 0L)
        {
            profile.put(memberGUID, count);
        }
    }


    /**
     * Return the explicit get count.
     *
     * @return long
     */
    public long getExplicitGetCount()
    {
        return explicitGetCount;
    }


    /**
     * Return the find count.
     *
     * @return long
     */
    public long getFindCount()
    {
        return findCount;
    }


    /**
     * Return the update property count.
     *
     * @return long
     */
    public long getUpdatePropertyCount()
    {
        return updatePropertyCount;
    }


    /**
     * Return the update attachment count.
     *
     * @return long
     */
    public long getUpdateAttachmentCount()
    {
        return updateAttachmentCount;
    }


    /**
     * Return the update feedback count.
     *
     * @return long
     */
    public long getUpdateFeedbackCount()
    {
        return updateFeedbackCount;
    }


    /**
     * Return the delete attachment count.
     *
     * @return long
     */
    public long getDeleteAttachmentCount()
    {
        return deleteAttachmentCount;
    }


    /**
     * Return the delete feedback count.
     *
     * @return long
     */
    public long getDeleteFeedbackCount()
    {
        return deleteFeedbackCount;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values
     */
    @Override
    public String toString()
    {
        return "MemberActivity{" +
                "explicitGetCount=" + explicitGetCount +
                ", findCount=" + findCount +
                ", updatePropertyCount=" + updatePropertyCount +
                ", updateAttachmentCount=" + updateAttachmentCount +
                ", updateFeedbackCount=" + updateFeedbackCount +
                ", deleteAttachmentCount=" + deleteAttachmentCount +
                ", deleteFeedbackCount=" + deleteFeedbackCount +
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
        MemberActivity that = (MemberActivity) objectToCompare;
        return explicitGetCount == that.explicitGetCount &&
               findCount == that.findCount &&
               updatePropertyCount == that.updatePropertyCount &&
               updateAttachmentCount == that.updateAttachmentCount &&
               updateFeedbackCount == that.updateFeedbackCount &&
               deleteAttachmentCount == that.deleteAttachmentCount &&
               deleteFeedbackCount == that.deleteFeedbackCount;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(explicitGetCount, findCount, updatePropertyCount, updateAttachmentCount,
                            updateFeedbackCount, deleteAttachmentCount, deleteFeedbackCount);
    }
}
