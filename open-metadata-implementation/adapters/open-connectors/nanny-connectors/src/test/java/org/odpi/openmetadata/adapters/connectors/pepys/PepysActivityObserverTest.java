/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.pepys;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecord;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.metadataobservability.ffdc.OpenMetadataObservabilityAuditCode;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;


/**
 * Verify how the Pepys activity observer turns the audit log records into usage activity counts.  These tests
 * drive the counting directly, so they need no integration context.
 */
public class PepysActivityObserverTest
{
    private static final String assetGUID = "c1a0c3b9-8a0d-4a4c-9a63-1d0c6f3f1f01";
    private static final String otherGUID = "9b3d1e7a-2f4c-4d7e-8b1a-6c5e4d3c2b10";


    /**
     * Build an audit log record from one of the observability messages, as the metadata access server would.
     *
     * @param auditCode message
     * @param userId calling user
     * @param guid asset unique identifier
     * @return record
     */
    private AuditLogRecord getRecord(OpenMetadataObservabilityAuditCode auditCode,
                                     String                             userId,
                                     String                             guid)
    {
        AuditLogMessageDefinition messageDefinition = auditCode.getMessageDefinition(userId,
                                                                                     "Asset",
                                                                                     guid,
                                                                                     "testOperation",
                                                                                     "Test Service");
        AuditLogRecord record = new AuditLogRecord();

        record.setMessageId(messageDefinition.getMessageId());
        record.setMessageParameters(messageDefinition.getMessageParams());

        return record;
    }


    /**
     * Each counted message has a mapping to its own count; creation and the user request message are not counted.
     */
    @Test public void testActivityMapping()
    {
        assertEquals(ObservedActivity.values().length, 9);

        assertNull(ObservedActivity.getActivity("METADATA-OBSERVABILITY-0001"));
        assertEquals(ObservedActivity.getActivity("METADATA-OBSERVABILITY-0002").getUsageCount(), ObservedActivity.UsageCount.EXPLICIT_GET);
        assertEquals(ObservedActivity.getActivity("METADATA-OBSERVABILITY-0003").getUsageCount(), ObservedActivity.UsageCount.EXPLICIT_GET);
        assertEquals(ObservedActivity.getActivity("METADATA-OBSERVABILITY-0004").getUsageCount(), ObservedActivity.UsageCount.UPDATE_ATTACHMENT);
        assertEquals(ObservedActivity.getActivity("METADATA-OBSERVABILITY-0005").getUsageCount(), ObservedActivity.UsageCount.UPDATE_FEEDBACK);
        assertEquals(ObservedActivity.getActivity("METADATA-OBSERVABILITY-0006").getUsageCount(), ObservedActivity.UsageCount.UPDATE_PROPERTY);
        assertEquals(ObservedActivity.getActivity("METADATA-OBSERVABILITY-0007").getUsageCount(), ObservedActivity.UsageCount.DELETE_ATTACHMENT);
        assertEquals(ObservedActivity.getActivity("METADATA-OBSERVABILITY-0008").getUsageCount(), ObservedActivity.UsageCount.FIND);
        assertEquals(ObservedActivity.getActivity("METADATA-OBSERVABILITY-0009").getUsageCount(), ObservedActivity.UsageCount.FIND);
        assertNull(ObservedActivity.getActivity("METADATA-OBSERVABILITY-0010"));
        assertEquals(ObservedActivity.getActivity("METADATA-OBSERVABILITY-0011").getUsageCount(), ObservedActivity.UsageCount.DELETE_FEEDBACK);

        assertNull(ObservedActivity.getActivity(null));
    }


    /**
     * Each kind of activity lands in its own count, against the anchor element named in the record.
     */
    @Test public void testCounting()
    {
        PepysActivityObserverConnector connector = new PepysActivityObserverConnector();

        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_CREATE, "erinoverview", assetGUID));
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_READ, "erinoverview", assetGUID));
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_READ_ATTACHMENT, "erinoverview", assetGUID));
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_SEARCH, "peterprofile", assetGUID));
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_UPDATE, "peterprofile", assetGUID));
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_UPDATE_ATTACHMENT, "peterprofile", assetGUID));
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_UPDATE_FEEDBACK, "peterprofile", assetGUID));
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_DELETE_ATTACHMENT, "peterprofile", assetGUID));
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_DELETE_FEEDBACK, "peterprofile", assetGUID));
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_DELETE_FEEDBACK, "erinoverview", assetGUID));
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_SEARCH_ATTACHMENT, "peterprofile", otherGUID));
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.USER_REQUEST_ACTIVITY, "peterprofile", otherGUID));

        Map<String, PendingUsageActivity> pending = connector.getPendingActivity();

        assertEquals(pending.size(), 2);

        PendingUsageActivity asset = pending.get(assetGUID);

        assertEquals(asset.getCount(ObservedActivity.UsageCount.EXPLICIT_GET), 2L);
        assertEquals(asset.getCount(ObservedActivity.UsageCount.FIND), 1L);
        assertEquals(asset.getCount(ObservedActivity.UsageCount.UPDATE_PROPERTY), 1L);
        assertEquals(asset.getCount(ObservedActivity.UsageCount.UPDATE_ATTACHMENT), 1L);
        assertEquals(asset.getCount(ObservedActivity.UsageCount.UPDATE_FEEDBACK), 1L);
        assertEquals(asset.getCount(ObservedActivity.UsageCount.DELETE_ATTACHMENT), 1L);
        assertEquals(asset.getCount(ObservedActivity.UsageCount.DELETE_FEEDBACK), 2L);
        assertEquals(asset.getTotal(), 9L);

        assertEquals(pending.get(otherGUID).getTotal(), 1L);
        assertEquals(pending.get(otherGUID).getCount(ObservedActivity.UsageCount.FIND), 1L);
    }


    /**
     * The activity of the users in the ignoredUserIds configuration property is not counted - this includes the
     * connector's own user, otherwise each update of a UsageActivity classification would be counted as more activity.
     */
    @Test public void testIgnoredUsersActivityIgnored()
    {
        PepysActivityObserverConnector connector = new PepysActivityObserverConnector();

        connector.setIgnoredUserIds(List.of("pepysnpa", "assetonboardingengine"), "pepysnpa");
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_READ, "pepysnpa", assetGUID));
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_UPDATE, "pepysnpa", assetGUID));
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_UPDATE, "assetonboardingengine", assetGUID));

        assertTrue(connector.getPendingActivity().isEmpty());

        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_READ, "erinoverview", assetGUID));

        assertEquals(connector.getPendingActivity().get(assetGUID).getTotal(), 1L);
    }


    /**
     * The connector's own activity is ignored even when it is not in the ignoredUserIds configuration property,
     * or the property is not set.  Every other user's activity is counted.
     */
    @Test public void testOwnActivityAlwaysIgnored()
    {
        PepysActivityObserverConnector connector = new PepysActivityObserverConnector();

        connector.setIgnoredUserIds(null, "pepysnpa");
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_UPDATE, "pepysnpa", assetGUID));

        assertTrue(connector.getPendingActivity().isEmpty());

        connector.setIgnoredUserIds(List.of("assetonboardingengine"), "pepysnpa");
        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_UPDATE, "pepysnpa", assetGUID));

        assertTrue(connector.getPendingActivity().isEmpty());

        connector.observe(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_READ, "erinoverview", assetGUID));

        assertEquals(connector.getPendingActivity().get(assetGUID).getTotal(), 1L);
    }


    /**
     * An audit log record serialized by the event topic audit log destination is understood when it arrives on
     * the topic, and anything else on the topic is ignored without error.
     *
     * @throws Exception serialization problem
     */
    @Test public void testEventParsing() throws Exception
    {
        PepysActivityObserverConnector connector = new PepysActivityObserverConnector();

        String event = new ObjectMapper().writeValueAsString(getRecord(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_SEARCH,
                                                                       "erinoverview",
                                                                       assetGUID));
        connector.processEvent(event);
        connector.processEvent("this is not an audit log record");
        connector.processEvent("{\"messageId\" : \"METADATA-OBSERVABILITY-0002\"}");

        Map<String, PendingUsageActivity> pending = connector.getPendingActivity();

        assertEquals(pending.size(), 1);
        assertEquals(pending.get(assetGUID).getCount(ObservedActivity.UsageCount.FIND), 1L);
    }


    /**
     * Counts that could not be written are added back to the counts that arrived in the meantime.
     */
    @Test public void testPendingActivityAdd()
    {
        PendingUsageActivity first  = new PendingUsageActivity();
        PendingUsageActivity second = new PendingUsageActivity();

        first.increment(ObservedActivity.UsageCount.EXPLICIT_GET);
        first.increment(null);
        second.increment(ObservedActivity.UsageCount.EXPLICIT_GET);
        second.increment(ObservedActivity.UsageCount.DELETE_FEEDBACK);

        first.add(second);
        first.add(null);

        assertEquals(first.getCount(ObservedActivity.UsageCount.EXPLICIT_GET), 2L);
        assertEquals(first.getCount(ObservedActivity.UsageCount.FIND), 0L);
        assertEquals(first.getCount(ObservedActivity.UsageCount.DELETE_FEEDBACK), 1L);
        assertEquals(first.getTotal(), 3L);
    }
}
