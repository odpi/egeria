/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.usageinsight.popularity;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MemberPopularityProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.UsageActivityProperties;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.testng.Assert.*;


/**
 * Verify how member activity is read from the classifications, added up and turned into a MemberPopularityProfile,
 * including the rippling up of activity through nested collections.
 */
public class MemberActivityTest
{
    /**
     * Build a UsageActivity classification.
     *
     * @param get explicit get count
     * @param find find count
     * @param updateAttachment update attachment count
     * @param deleteFeedback delete feedback count
     * @return classification
     */
    private ElementClassification usageActivity(long get, long find, long updateAttachment, long deleteFeedback)
    {
        UsageActivityProperties properties = new UsageActivityProperties();

        properties.setExplicitGetCount(get);
        properties.setFindCount(find);
        properties.setUpdateAttachmentCount(updateAttachment);
        properties.setDeleteFeedbackCount(deleteFeedback);

        ElementClassification classification = new ElementClassification();
        classification.setClassificationProperties(properties);

        return classification;
    }


    /**
     * Wrap a profile as a classification.
     *
     * @param properties profile
     * @return classification
     */
    private ElementClassification asClassification(MemberPopularityProfileProperties properties)
    {
        ElementClassification classification = new ElementClassification();
        classification.setClassificationProperties(properties);

        return classification;
    }


    /**
     * An element without a classification contributes nothing.
     */
    @Test public void testMissingClassifications()
    {
        assertTrue(MemberActivity.fromUsageActivity(null).isEmpty());
        assertTrue(MemberActivity.fromMemberPopularityProfile(null).isEmpty());
        assertTrue(MemberActivity.fromUsageActivity(new ElementClassification()).isEmpty());
    }


    /**
     * The profile totals the members and maps each used member to its own count.
     */
    @Test public void testProfileOfLeafMembers()
    {
        Map<String, MemberActivity> members = new LinkedHashMap<>();

        members.put("term-1", MemberActivity.fromUsageActivity(usageActivity(3, 5, 0, 0)));
        members.put("term-2", MemberActivity.fromUsageActivity(usageActivity(1, 0, 2, 1)));
        members.put("term-3", MemberActivity.fromUsageActivity(null));

        MemberPopularityProfileProperties profile = MemberActivity.getMemberPopularityProfile(members);

        assertEquals(profile.getExplicitGetCountTotal(), 4L);
        assertEquals(profile.getFindCountTotal(), 5L);
        assertEquals(profile.getUpdateAttachmentCountTotal(), 2L);
        assertEquals(profile.getDeleteFeedbackCountTotal(), 1L);
        assertEquals(profile.getUpdatePropertyCountTotal(), 0L);

        assertEquals(profile.getExplicitGetCountProfile(), Map.of("term-1", 3L, "term-2", 1L));
        assertEquals(profile.getFindCountProfile(), Map.of("term-1", 5L));
        assertEquals(profile.getUpdateAttachmentCountProfile(), Map.of("term-2", 2L));
        assertTrue(profile.getUpdatePropertyCountProfile().isEmpty(), "unused members are left out of the profiles");
    }


    /**
     * A folder's totals, plus its own usage activity, become what it contributes to the glossary that contains it,
     * and the glossary's totals include the leaf members of the folder.
     */
    @Test public void testRippleUpThroughNestedCollections()
    {
        Map<String, MemberActivity> folderMembers = new LinkedHashMap<>();
        folderMembers.put("term-1", MemberActivity.fromUsageActivity(usageActivity(3, 5, 0, 0)));
        folderMembers.put("term-2", MemberActivity.fromUsageActivity(usageActivity(1, 0, 2, 1)));

        MemberPopularityProfileProperties folderProfile = MemberActivity.getMemberPopularityProfile(folderMembers);

        /*
         * What the folder contributes to the glossary - its own activity plus the totals of its members.
         */
        MemberActivity folderContribution = MemberActivity.fromUsageActivity(usageActivity(2, 0, 0, 0));
        folderContribution.add(MemberActivity.fromMemberPopularityProfile(asClassification(folderProfile)));

        Map<String, MemberActivity> glossaryMembers = new LinkedHashMap<>();
        glossaryMembers.put("folder-1", folderContribution);
        glossaryMembers.put("term-9", MemberActivity.fromUsageActivity(usageActivity(10, 0, 0, 0)));

        MemberPopularityProfileProperties glossaryProfile = MemberActivity.getMemberPopularityProfile(glossaryMembers);

        assertEquals(glossaryProfile.getExplicitGetCountTotal(), 16L, "2 (folder) + 3 + 1 (folder's terms) + 10");
        assertEquals(glossaryProfile.getFindCountTotal(), 5L);
        assertEquals(glossaryProfile.getExplicitGetCountProfile(), Map.of("folder-1", 6L, "term-9", 10L));
        assertEquals(glossaryProfile.getDeleteFeedbackCountProfile(), Map.of("folder-1", 1L));
    }


    /**
     * A profile read back from the repository carries its type name, and empty maps are not stored - neither may
     * make an unchanged profile look changed.
     */
    @Test public void testSameCounts()
    {
        Map<String, MemberActivity> members = new LinkedHashMap<>();
        members.put("term-1", MemberActivity.fromUsageActivity(usageActivity(3, 5, 0, 0)));

        MemberPopularityProfileProperties latest = MemberActivity.getMemberPopularityProfile(members);
        MemberPopularityProfileProperties stored = MemberActivity.getMemberPopularityProfile(members);

        stored.setUpdatePropertyCountProfile(null);
        stored.setDeleteFeedbackCountProfile(null);

        assertTrue(MemberActivity.sameCounts(stored, latest));

        members.put("term-2", MemberActivity.fromUsageActivity(usageActivity(0, 1, 0, 0)));
        assertFalse(MemberActivity.sameCounts(stored, MemberActivity.getMemberPopularityProfile(members)));
        assertFalse(MemberActivity.sameCounts(null, latest));
    }
}
