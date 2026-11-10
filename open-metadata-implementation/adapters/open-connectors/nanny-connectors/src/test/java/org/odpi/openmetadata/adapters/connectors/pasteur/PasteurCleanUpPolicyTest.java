/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.pasteur;

import org.odpi.openmetadata.adapters.connectors.pasteur.controls.PasteurConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Test the rules that decide which finished actions the Pasteur Action History Cleanup deletes.
 */
public class PasteurCleanUpPolicyTest
{
    private static final long millisecondsPerDay = 24L * 60L * 60L * 1000L;

    private static final List<String> engineActionSuperTypes  = List.of("Action", "Process", "Asset", "Referenceable", "OpenMetadataRoot");
    private static final List<String> processInstanceSuperTypes = List.of("TransientEmbeddedProcess", "EmbeddedProcess", "Action", "Process", "Asset", "Referenceable", "OpenMetadataRoot");
    private static final List<String> dataSharingRequestSuperTypes = List.of("ToDo", "Action", "Process", "Asset", "Referenceable", "OpenMetadataRoot");
    private static final List<String> noteSuperTypes = List.of("Notification", "Action", "Process", "Asset", "Referenceable", "OpenMetadataRoot");

    private final Date now = new Date();


    private Date daysAgo(long days)
    {
        return new Date(now.getTime() - (days * millisecondsPerDay));
    }


    private PasteurCleanUpPolicy getDefaultPolicy()
    {
        return new PasteurCleanUpPolicy(PasteurConfigurationProperty.getDefaultDaysToCleanUp(),
                                        PasteurConfigurationProperty.DEFAULT_COMPLETION_STATUSES);
    }


    /**
     * The entry for the action's own type wins over the entry for a supertype.
     */
    @Test
    public void testOwnTypeWinsOverSuperType()
    {
        Map<String, Long> daysToCleanUp = new HashMap<>();

        daysToCleanUp.put("Action", 365L);
        daysToCleanUp.put("EngineAction", 7L);

        PasteurCleanUpPolicy policy = new PasteurCleanUpPolicy(daysToCleanUp, List.of("COMPLETED"));

        assertEquals(policy.getDaysToCleanUp("EngineAction", engineActionSuperTypes), Long.valueOf(7));
        assertEquals(policy.getDaysToCleanUp("ToDo", dataSharingRequestSuperTypes.subList(1, dataSharingRequestSuperTypes.size())), Long.valueOf(365));
    }


    /**
     * A subtype with no entry of its own uses the nearest supertype that has one.
     */
    @Test
    public void testNearestSuperTypeIsUsed()
    {
        Map<String, Long> daysToCleanUp = new HashMap<>();

        daysToCleanUp.put("Action", 365L);
        daysToCleanUp.put("ToDo", 90L);

        PasteurCleanUpPolicy policy = new PasteurCleanUpPolicy(daysToCleanUp, List.of("COMPLETED"));

        assertEquals(policy.getDaysToCleanUp("DataSharingRequest", dataSharingRequestSuperTypes), Long.valueOf(90));
        assertEquals(policy.getDaysToCleanUp("Note", noteSuperTypes), Long.valueOf(365));
    }


    /**
     * The default days do not reach notes, meetings and the other kinds of action.
     */
    @Test
    public void testDefaultsLeaveOtherActionsAlone()
    {
        PasteurCleanUpPolicy policy = this.getDefaultPolicy();

        assertNull(policy.getDaysToCleanUp("Note", noteSuperTypes));
        assertNull(policy.getDaysToCleanUp("Meeting", engineActionSuperTypes));
        assertNull(policy.getDaysToCleanUp("Action", engineActionSuperTypes.subList(1, engineActionSuperTypes.size())));
        assertNull(policy.getDaysToCleanUp(null, null));

        assertFalse(policy.isEligible("Note", noteSuperTypes, "COMPLETED", daysAgo(1000), null, null, now));
        assertTrue(policy.isEligible("DataSharingRequest", dataSharingRequestSuperTypes, "COMPLETED", daysAgo(91), null, null, now));
        assertTrue(policy.isEligible("GovernanceActionProcessInstance", processInstanceSuperTypes, "FAILED", daysAgo(14), null, null, now));
    }


    /**
     * Only the completion statuses make an action eligible.
     */
    @Test
    public void testCompletionStatuses()
    {
        PasteurCleanUpPolicy policy = this.getDefaultPolicy();

        assertTrue(policy.isEligible("EngineAction", engineActionSuperTypes, "COMPLETED", daysAgo(8), null, null, now));
        assertTrue(policy.isEligible("EngineAction", engineActionSuperTypes, "INVALID", daysAgo(8), null, null, now));
        assertTrue(policy.isEligible("EngineAction", engineActionSuperTypes, "FAILED", daysAgo(8), null, null, now));

        assertFalse(policy.isEligible("EngineAction", engineActionSuperTypes, "IN_PROGRESS", daysAgo(8), null, null, now));
        assertFalse(policy.isEligible("EngineAction", engineActionSuperTypes, "WAITING", daysAgo(8), null, null, now));
        assertFalse(policy.isEligible("EngineAction", engineActionSuperTypes, "CANCELLED", daysAgo(8), null, null, now));
        assertFalse(policy.isEligible("EngineAction", engineActionSuperTypes, "IGNORED", daysAgo(8), null, null, now));
        assertFalse(policy.isEligible("EngineAction", engineActionSuperTypes, null, daysAgo(8), null, null, now));
    }


    /**
     * The action must have completed at least the configured number of days ago.
     */
    @Test
    public void testAge()
    {
        PasteurCleanUpPolicy policy = this.getDefaultPolicy();

        assertFalse(policy.isEligible("EngineAction", engineActionSuperTypes, "COMPLETED", daysAgo(6), null, null, now));
        assertTrue(policy.isEligible("EngineAction", engineActionSuperTypes, "COMPLETED", daysAgo(7), null, null, now));

        assertFalse(PasteurCleanUpPolicy.isOldEnough(new Date(now.getTime() - (7 * millisecondsPerDay) + 1), 7, now));
        assertTrue(PasteurCleanUpPolicy.isOldEnough(now, 0, now));
        assertFalse(PasteurCleanUpPolicy.isOldEnough(null, 0, now));
    }


    /**
     * The completion time comes from completionTime, then the update time, then the create time.
     */
    @Test
    public void testCompletionDateFallback()
    {
        Date completionTime = daysAgo(30);
        Date updateTime     = daysAgo(2);
        Date createTime     = daysAgo(40);

        assertEquals(PasteurCleanUpPolicy.getCompletionDate(completionTime, updateTime, createTime), completionTime);
        assertEquals(PasteurCleanUpPolicy.getCompletionDate(null, updateTime, createTime), updateTime);
        assertEquals(PasteurCleanUpPolicy.getCompletionDate(null, null, createTime), createTime);
        assertNull(PasteurCleanUpPolicy.getCompletionDate(null, null, null));

        PasteurCleanUpPolicy policy = this.getDefaultPolicy();

        assertTrue(policy.isEligible("EngineAction", engineActionSuperTypes, "COMPLETED", completionTime, updateTime, createTime, now));
        assertFalse(policy.isEligible("EngineAction", engineActionSuperTypes, "COMPLETED", null, updateTime, createTime, now));
        assertTrue(policy.isEligible("EngineAction", engineActionSuperTypes, "COMPLETED", null, null, createTime, now));
        assertFalse(policy.isEligible("EngineAction", engineActionSuperTypes, "COMPLETED", null, null, null, now));
    }


    /**
     * Policies with nothing to do are reported as inactive.
     */
    @Test
    public void testIsActive()
    {
        assertTrue(this.getDefaultPolicy().isActive());
        assertFalse(new PasteurCleanUpPolicy(null, List.of("COMPLETED")).isActive());
        assertFalse(new PasteurCleanUpPolicy(PasteurConfigurationProperty.getDefaultDaysToCleanUp(), null).isActive());

        Map<String, Long> negative = new HashMap<>();
        negative.put("EngineAction", -1L);
        assertFalse(new PasteurCleanUpPolicy(negative, List.of("COMPLETED")).isActive());
    }


    /**
     * The daysToCleanUp configuration property accepts integers, longs and numeric strings, and reports the rest.
     */
    @Test
    public void testParseDaysToCleanUp()
    {
        Map<Object, Object> configured = new HashMap<>();

        configured.put("EngineAction", 7);
        configured.put("ToDo", 90L);
        configured.put("Meeting", " 30 ");
        configured.put("Review", "soon");
        configured.put("Notification", -5);
        configured.put("Note", null);

        List<String>      invalidEntries = new ArrayList<>();
        Map<String, Long> daysToCleanUp  = PasteurCleanUpPolicy.parseDaysToCleanUp(configured, invalidEntries);

        assertEquals(daysToCleanUp.size(), 3);
        assertEquals(daysToCleanUp.get("EngineAction"), Long.valueOf(7));
        assertEquals(daysToCleanUp.get("ToDo"), Long.valueOf(90));
        assertEquals(daysToCleanUp.get("Meeting"), Long.valueOf(30));
        assertEquals(invalidEntries.size(), 3);

        invalidEntries.clear();
        assertTrue(PasteurCleanUpPolicy.parseDaysToCleanUp(null, invalidEntries).isEmpty());
        assertTrue(invalidEntries.isEmpty());

        assertTrue(PasteurCleanUpPolicy.parseDaysToCleanUp("EngineAction=7", invalidEntries).isEmpty());
        assertEquals(invalidEntries.size(), 1);
    }


    /**
     * The completion statuses are matched against the ActivityStatus enum, ignoring case, and default when absent.
     */
    @Test
    public void testParseCompletionStatuses()
    {
        List<String> invalidStatuses = new ArrayList<>();

        assertEquals(PasteurCleanUpPolicy.parseCompletionStatuses(null, PasteurConfigurationProperty.DEFAULT_COMPLETION_STATUSES, invalidStatuses),
                     List.of("COMPLETED", "INVALID", "FAILED"));
        assertTrue(invalidStatuses.isEmpty());

        assertEquals(PasteurCleanUpPolicy.parseCompletionStatuses(List.of("cancelled", " COMPLETED ", "Finished", "COMPLETED"),
                                                                  PasteurConfigurationProperty.DEFAULT_COMPLETION_STATUSES,
                                                                  invalidStatuses),
                     List.of("CANCELLED", "COMPLETED"));
        assertEquals(invalidStatuses, List.of("Finished"));
    }


    /**
     * The types are cleaned up in a stable order.
     */
    @Test
    public void testCleanUpTypeOrder()
    {
        assertEquals(this.getDefaultPolicy().getCleanUpTypeNames(),
                     List.of("EngineAction", "GovernanceActionProcessInstance", "ToDo"));
    }


    /**
     * The deleteMethod configuration property defaults to LOOK_FOR_LINEAGE, ignores case and rejects unknown names.
     */
    @Test
    public void testParseDeleteMethod()
    {
        assertEquals(PasteurCleanUpPolicy.DEFAULT_DELETE_METHOD, DeleteMethod.LOOK_FOR_LINEAGE);
        assertEquals(PasteurCleanUpPolicy.parseDeleteMethod(null), DeleteMethod.LOOK_FOR_LINEAGE);
        assertEquals(PasteurCleanUpPolicy.parseDeleteMethod("  "), DeleteMethod.LOOK_FOR_LINEAGE);
        assertEquals(PasteurCleanUpPolicy.parseDeleteMethod("PURGE"), DeleteMethod.PURGE);
        assertEquals(PasteurCleanUpPolicy.parseDeleteMethod(" soft_delete "), DeleteMethod.SOFT_DELETE);
        assertEquals(PasteurCleanUpPolicy.parseDeleteMethod("Archive"), DeleteMethod.ARCHIVE);
        assertNull(PasteurCleanUpPolicy.parseDeleteMethod("SHRED"));
    }
}
