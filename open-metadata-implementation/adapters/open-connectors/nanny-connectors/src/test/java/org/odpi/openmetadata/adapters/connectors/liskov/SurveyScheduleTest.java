/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.liskov;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the rules that decide when Liskov surveys a member of a data sharing hub: straight away if it has
 * never been surveyed, then at an interval that starts at the minimum, grows by a day for each survey in a row
 * that finds no change, stops at the maximum, and drops back to the minimum when a survey finds a change.
 */
public class SurveyScheduleTest
{
    private static final long ONE_HOUR = 60L * 60L * 1000L;
    private static final long ONE_DAY  = SurveySchedule.ONE_DAY;
    private static final long NOW      = 1_800_000_000_000L;


    /**
     * A survey history for the tests: whether each completed survey, most recent first, found a change since
     * the one before it.  It records which comparisons were asked for, so a test can check that the schedule
     * only looks as far back as it needs to.
     */
    private static class History implements SurveySchedule.ChangeDetector
    {
        private final boolean[]     foundChange;
        private final List<Integer> comparisons = new ArrayList<>();

        /**
         * Constructor.
         *
         * @param foundChange whether each completed survey found a change, most recent first.  The oldest survey
         *                    has nothing before it to compare with, so the history holds one more survey than
         *                    there are entries.
         */
        History(boolean... foundChange)
        {
            this.foundChange = foundChange;
        }

        int completedSurveyCount()
        {
            return foundChange.length + 1;
        }

        @Override
        public boolean surveyFoundChange(int surveyIndex)
        {
            comparisons.add(surveyIndex);

            return foundChange[surveyIndex];
        }
    }


    /**
     * Return the time of a survey that started the supplied time before now.
     *
     * @param age time since the survey started
     * @return survey time
     */
    private Date surveyedAgo(long age)
    {
        return new Date(NOW - age);
    }


    private final SurveySchedule defaultSchedule = new SurveySchedule(SurveySchedule.DEFAULT_MINIMUM_INTERVAL_DAYS,
                                                                      SurveySchedule.DEFAULT_MAXIMUM_INTERVAL_DAYS);


    /**
     * A member that has never been surveyed is surveyed straight away.
     */
    @Test public void testNeverSurveyedIsDue() throws Exception
    {
        assertTrue(defaultSchedule.isSurveyDue(null, 0, new History(), NOW));
    }


    /**
     * Within the minimum interval nothing is due, and the survey history is not even looked at.
     */
    @Test public void testNothingIsDueWithinTheMinimumInterval() throws Exception
    {
        History history = new History(true, true);

        assertFalse(defaultSchedule.isSurveyDue(surveyedAgo(ONE_HOUR), history.completedSurveyCount(), history, NOW));
        assertFalse(defaultSchedule.isSurveyDue(surveyedAgo(20 * ONE_HOUR), history.completedSurveyCount(), history, NOW));
        assertTrue(history.comparisons.isEmpty());
    }


    /**
     * A survey that falls due within the tolerance counts as due.  The surveys start a little after the daily
     * refresh that requested them, so without this the next day's refresh would always be a little early.
     */
    @Test public void testToleranceAllowsForTheRefreshCycle() throws Exception
    {
        History history = new History(true);

        assertFalse(defaultSchedule.isSurveyDue(surveyedAgo(ONE_DAY - SurveySchedule.TIME_TOLERANCE - ONE_HOUR),
                                                history.completedSurveyCount(), history, NOW));
        assertTrue(defaultSchedule.isSurveyDue(surveyedAgo(ONE_DAY - SurveySchedule.TIME_TOLERANCE + 1),
                                               history.completedSurveyCount(), history, NOW));
    }


    /**
     * After the first survey there is nothing to compare it with, so the interval is the minimum.
     */
    @Test public void testFirstSurveyGivesTheMinimumInterval() throws Exception
    {
        assertTrue(defaultSchedule.isSurveyDue(surveyedAgo(ONE_DAY), 1, new History(), NOW));
    }


    /**
     * A member whose surveys keep finding changes is surveyed every day.
     */
    @Test public void testChangingMemberIsSurveyedDaily() throws Exception
    {
        History history = new History(true, true, true);

        assertTrue(defaultSchedule.isSurveyDue(surveyedAgo(ONE_DAY), history.completedSurveyCount(), history, NOW));
        assertEquals(history.comparisons, List.of(0));
    }


    /**
     * Each survey in a row that finds no change adds a day to the interval: one unchanged survey makes it two
     * days, two make it three, and so on.
     */
    @Test public void testIntervalGrowsByADayForEachUnchangedSurvey() throws Exception
    {
        for (int unchangedSurveys = 1; unchangedSurveys < 6; unchangedSurveys++)
        {
            boolean[] foundChange = new boolean[unchangedSurveys + 1];

            foundChange[unchangedSurveys] = true;   // the survey before the unchanged run found a change

            History history = new History(foundChange);

            int intervalDays = 1 + unchangedSurveys;

            assertFalse(defaultSchedule.isSurveyDue(surveyedAgo((intervalDays - 1) * ONE_DAY), history.completedSurveyCount(), history, NOW),
                        "Due a day early after " + unchangedSurveys + " unchanged survey(s)");
            assertTrue(defaultSchedule.isSurveyDue(surveyedAgo(intervalDays * ONE_DAY), history.completedSurveyCount(), history, NOW),
                       "Not due after " + intervalDays + " days with " + unchangedSurveys + " unchanged survey(s)");
        }
    }


    /**
     * The interval stops growing at the maximum, and once the maximum has passed the history is not looked at.
     */
    @Test public void testIntervalStopsAtTheMaximum() throws Exception
    {
        History history = new History(false, false, false, false, false, false, false, false, false);

        assertFalse(defaultSchedule.isSurveyDue(surveyedAgo(6 * ONE_DAY), history.completedSurveyCount(), history, NOW));

        history.comparisons.clear();

        assertTrue(defaultSchedule.isSurveyDue(surveyedAgo(7 * ONE_DAY), history.completedSurveyCount(), history, NOW));
        assertTrue(history.comparisons.isEmpty());
    }


    /**
     * A survey that finds a change brings the interval straight back to the minimum, however long the run of
     * unchanged surveys before it.  The comparisons stop at that change.
     */
    @Test public void testChangeResetsTheInterval() throws Exception
    {
        History history = new History(true, false, false, false, false);

        assertTrue(defaultSchedule.isSurveyDue(surveyedAgo(ONE_DAY), history.completedSurveyCount(), history, NOW));
        assertEquals(history.comparisons, List.of(0));
    }


    /**
     * Only as many comparisons are made as the time since the latest survey requires.
     */
    @Test public void testOnlyTheNeededComparisonsAreMade() throws Exception
    {
        History history = new History(false, false, false, false, false, false);

        assertFalse(defaultSchedule.isSurveyDue(surveyedAgo(3 * ONE_DAY), history.completedSurveyCount(), history, NOW));
        assertEquals(history.comparisons, List.of(0, 1, 2));
    }


    /**
     * A run of unchanged surveys that is shorter than the time since the latest survey requires means the
     * survey is due, even though no comparison found a change.
     */
    @Test public void testShortHistoryMeansDue() throws Exception
    {
        History history = new History(false);

        assertFalse(defaultSchedule.isSurveyDue(surveyedAgo(ONE_DAY), history.completedSurveyCount(), history, NOW));
        assertTrue(defaultSchedule.isSurveyDue(surveyedAgo(2 * ONE_DAY), history.completedSurveyCount(), history, NOW));
    }


    /**
     * The time is measured from the latest survey even if it did not complete, so a failing survey is retried at
     * the minimum interval rather than on every refresh.
     */
    @Test public void testFailedSurveyIsRetriedAtTheMinimumInterval() throws Exception
    {
        History history = new History(true);

        assertFalse(defaultSchedule.isSurveyDue(surveyedAgo(ONE_HOUR), history.completedSurveyCount(), history, NOW));
    }


    /**
     * The minimum and maximum can be configured.
     */
    @Test public void testConfiguredLimits() throws Exception
    {
        SurveySchedule schedule = new SurveySchedule(2, 4);

        History changing = new History(true, true);

        assertFalse(schedule.isSurveyDue(surveyedAgo(ONE_DAY), changing.completedSurveyCount(), changing, NOW));
        assertTrue(schedule.isSurveyDue(surveyedAgo(2 * ONE_DAY), changing.completedSurveyCount(), changing, NOW));

        History stable = new History(false, false, false, false);

        assertFalse(schedule.isSurveyDue(surveyedAgo(3 * ONE_DAY), stable.completedSurveyCount(), stable, NOW));
        assertTrue(schedule.isSurveyDue(surveyedAgo(4 * ONE_DAY), stable.completedSurveyCount(), stable, NOW));

        assertEquals(schedule.getCompletedSurveysNeeded(), 3);
        assertEquals(defaultSchedule.getCompletedSurveysNeeded(), 7);
    }


    /**
     * A negative minimum is treated as zero, and a maximum below the minimum as the minimum, which gives a
     * fixed interval.
     */
    @Test public void testInvalidLimitsAreCorrected() throws Exception
    {
        SurveySchedule negative = new SurveySchedule(-3, 2);

        assertEquals(negative.getMinimumIntervalDays(), 0);
        assertEquals(negative.getMaximumIntervalDays(), 2);

        SurveySchedule inverted = new SurveySchedule(3, 1);

        assertEquals(inverted.getMinimumIntervalDays(), 3);
        assertEquals(inverted.getMaximumIntervalDays(), 3);

        History stable = new History(false, false, false);

        assertFalse(inverted.isSurveyDue(surveyedAgo(2 * ONE_DAY), stable.completedSurveyCount(), stable, NOW));
        assertTrue(inverted.isSurveyDue(surveyedAgo(3 * ONE_DAY), stable.completedSurveyCount(), stable, NOW));
    }
}
