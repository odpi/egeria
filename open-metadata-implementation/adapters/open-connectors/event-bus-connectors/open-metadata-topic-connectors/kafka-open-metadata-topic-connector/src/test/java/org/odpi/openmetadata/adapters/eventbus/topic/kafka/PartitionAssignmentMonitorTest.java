/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.adapters.eventbus.topic.kafka.PartitionAssignmentMonitor.Report;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * A consumer that Apache Kafka never assigns a partition to receives nothing, and used to say nothing: a cohort
 * member in that state ran for a whole conformance test without hearing the other member register.  The monitor
 * decides when the consumer reports it.
 */
public class PartitionAssignmentMonitorTest
{
    private static final long START  = 1_000_000L;
    private static final long FIRST  = 60_000L;
    private static final long REPEAT = 600_000L;


    /**
     * The usual case: a partition arrives within the first poll or two, and nothing is ever reported.
     */
    @Test
    public void testPromptAssignmentIsNeverReported()
    {
        PartitionAssignmentMonitor monitor = new PartitionAssignmentMonitor(START, FIRST, REPEAT);

        assertEquals(monitor.check(false, START + 10), Report.NOTHING);
        assertEquals(monitor.check(true, START + 40), Report.NOTHING);
        assertEquals(monitor.check(true, START + FIRST + 1), Report.NOTHING);
        assertEquals(monitor.check(true, START + REPEAT * 3), Report.NOTHING);
    }


    /**
     * A consumer with no partition is reported once the first interval has passed, and not before.
     */
    @Test
    public void testUnassignedConsumerIsReportedAfterTheFirstInterval()
    {
        PartitionAssignmentMonitor monitor = new PartitionAssignmentMonitor(START, FIRST, REPEAT);

        assertEquals(monitor.check(false, START + FIRST - 1), Report.NOTHING);
        assertEquals(monitor.check(false, START + FIRST), Report.STILL_UNASSIGNED);
        assertEquals(monitor.getSecondsUnassigned(START + FIRST), 60);
    }


    /**
     * The report is not made on every poll - the consumer polls many times a second - but it is repeated,
     * so a server that has been in this state for hours still says so in a recent part of its log.
     */
    @Test
    public void testReportIsRepeatedAtTheRepeatInterval()
    {
        PartitionAssignmentMonitor monitor = new PartitionAssignmentMonitor(START, FIRST, REPEAT);

        long firstReport = START + FIRST;

        assertEquals(monitor.check(false, firstReport), Report.STILL_UNASSIGNED);
        assertEquals(monitor.check(false, firstReport + 10), Report.NOTHING);
        assertEquals(monitor.check(false, firstReport + REPEAT - 1), Report.NOTHING);
        assertEquals(monitor.check(false, firstReport + REPEAT), Report.STILL_UNASSIGNED);
        assertEquals(monitor.getSecondsUnassigned(firstReport + REPEAT), 660);
        assertEquals(monitor.check(false, firstReport + REPEAT + 10), Report.NOTHING);
        assertEquals(monitor.check(false, firstReport + REPEAT * 2), Report.STILL_UNASSIGNED);
    }


    /**
     * A consumer that was reported says when it is given a partition - once.
     */
    @Test
    public void testRecoveryIsReportedOnce()
    {
        PartitionAssignmentMonitor monitor = new PartitionAssignmentMonitor(START, FIRST, REPEAT);

        assertEquals(monitor.check(false, START + FIRST), Report.STILL_UNASSIGNED);
        assertEquals(monitor.check(true, START + FIRST + 500), Report.NOW_ASSIGNED);
        assertEquals(monitor.check(true, START + FIRST + 600), Report.NOTHING);
    }


    /**
     * A rebalance takes a consumer's partition away for a moment.  That is not reported, and the time without
     * a partition is counted from when it was lost, not from when the consumer started.
     */
    @Test
    public void testShortRebalanceIsNotReported()
    {
        PartitionAssignmentMonitor monitor = new PartitionAssignmentMonitor(START, FIRST, REPEAT);

        long later = START + REPEAT * 5;

        assertEquals(monitor.check(true, later), Report.NOTHING);
        assertEquals(monitor.check(false, later + 2_000), Report.NOTHING);
        assertEquals(monitor.check(false, later + FIRST - 1), Report.NOTHING);
        assertEquals(monitor.check(true, later + FIRST), Report.NOTHING);
    }


    /**
     * A partition lost for good, long after the consumer started, is reported a full interval after it was lost.
     */
    @Test
    public void testPartitionLostLaterIsReported()
    {
        PartitionAssignmentMonitor monitor = new PartitionAssignmentMonitor(START, FIRST, REPEAT);

        long lost = START + REPEAT * 5;

        assertEquals(monitor.check(true, lost), Report.NOTHING);
        assertEquals(monitor.check(false, lost + FIRST - 1), Report.NOTHING);
        assertEquals(monitor.check(false, lost + FIRST), Report.STILL_UNASSIGNED);
        assertEquals(monitor.getSecondsUnassigned(lost + FIRST), 60);
        assertEquals(monitor.check(true, lost + FIRST + 1), Report.NOW_ASSIGNED);
    }
}
