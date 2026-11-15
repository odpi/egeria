/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

/**
 * PartitionAssignmentMonitor decides when a consumer that has no partition of its topic should say so.
 * <br><br>
 * A consumer reads nothing until Apache Kafka's group coordinator has assigned it a partition.  That normally
 * takes a fraction of a second, and happens again briefly whenever the consumer group rebalances.  If the broker
 * can not complete the assignment - its group coordinator is unable to write the group's state, say - the
 * consumer's polls simply return nothing, for ever, and look exactly like a quiet topic.  A cohort member in that
 * state never hears another member's registration; an engine never hears a request.
 * <br><br>
 * The consumer tells this monitor, after each poll, whether it has a partition.  The monitor answers with what, if
 * anything, should be reported: that the consumer has been without a partition for too long (the first time after
 * {@code firstReportAfterMs}, and then every {@code repeatReportAfterMs}), or that a consumer which was reported
 * has since been given one.  A consumer that gets its partition promptly is never reported at all.
 */
class PartitionAssignmentMonitor
{
    /**
     * What the consumer should report after a poll.
     */
    enum Report
    {
        /** Nothing to say. */
        NOTHING,

        /** The consumer has been without a partition for long enough to be worth reporting (again). */
        STILL_UNASSIGNED,

        /** The consumer was reported as having no partition and now has one. */
        NOW_ASSIGNED
    }

    private final long firstReportAfterMs;
    private final long repeatReportAfterMs;

    private long    unassignedSince;
    private long    nextReportTime;
    private boolean reported = false;


    /**
     * Constructor.
     *
     * @param startTime time the consumer started, in milliseconds - it has no partition from this moment
     * @param firstReportAfterMs how long the consumer may be without a partition before it is first reported
     * @param repeatReportAfterMs how long to leave between later reports
     */
    PartitionAssignmentMonitor(long startTime,
                               long firstReportAfterMs,
                               long repeatReportAfterMs)
    {
        this.firstReportAfterMs  = firstReportAfterMs;
        this.repeatReportAfterMs = repeatReportAfterMs;
        this.unassignedSince     = startTime;
        this.nextReportTime      = startTime + firstReportAfterMs;
    }


    /**
     * Record whether the consumer has a partition, and return what should be reported.
     *
     * @param partitionAssigned does the consumer have at least one partition of its topic?
     * @param now the current time in milliseconds
     * @return what to report
     */
    Report check(boolean partitionAssigned,
                 long    now)
    {
        if (partitionAssigned)
        {
            /*
             * The clock for the next spell without a partition starts when the partition is lost, so it is kept
             * level with now while the consumer has one.
             */
            unassignedSince = now;
            nextReportTime  = now + firstReportAfterMs;

            if (reported)
            {
                reported = false;

                return Report.NOW_ASSIGNED;
            }

            return Report.NOTHING;
        }

        if (now >= nextReportTime)
        {
            reported       = true;
            nextReportTime = now + repeatReportAfterMs;

            return Report.STILL_UNASSIGNED;
        }

        return Report.NOTHING;
    }


    /**
     * Return how long the consumer has been without a partition.
     *
     * @param now the current time in milliseconds
     * @return whole seconds
     */
    long getSecondsUnassigned(long now)
    {
        return Math.max(0, (now - unassignedSince) / 1000);
    }
}
