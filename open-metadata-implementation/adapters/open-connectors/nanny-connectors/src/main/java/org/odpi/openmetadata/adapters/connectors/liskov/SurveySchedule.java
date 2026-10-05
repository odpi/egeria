/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.liskov;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Date;

/**
 * Decides whether a survey of a data store is due.
 * <br><br>
 * A data store that has never been surveyed is surveyed straight away.  After that, the interval between surveys
 * starts at the minimum interval and grows by a day each time a survey finds no change since the survey before
 * it, up to the maximum interval.  As soon as a survey finds a change, the interval drops back to the minimum and
 * the cycle repeats.  With the default limits, a data store that keeps changing is surveyed daily, and one that
 * has stopped changing is surveyed after 1 day, then 2, then 3 and so on up to once a week.
 * <br><br>
 * Nothing about the schedule is stored.  It is worked out from the survey history each time it is needed, so it
 * survives a restart of the integration daemon.  The history is only consulted when the answer depends on it -
 * never before the minimum interval has passed, never once the maximum has, and otherwise only as far back as
 * the time since the latest survey requires.
 */
public class SurveySchedule
{
    /**
     * The default minimum number of days between surveys.
     */
    public static final int DEFAULT_MINIMUM_INTERVAL_DAYS = 1;

    /**
     * The default maximum number of days between surveys.
     */
    public static final int DEFAULT_MAXIMUM_INTERVAL_DAYS = 7;

    /**
     * Number of milliseconds in a day.
     */
    static final long ONE_DAY = 24L * 60L * 60L * 1000L;

    /**
     * A survey is treated as due if it falls due within this time.  The surveys are requested during the
     * connector's refresh, which by default runs once a day, and each survey starts a little after the refresh
     * that requested it - however long its engine action waited to be picked up.  Without this allowance the
     * next day's refresh would find a little less than a day had passed, and every one-day interval would
     * become two.
     */
    static final long TIME_TOLERANCE = 2L * 60L * 60L * 1000L;


    /**
     * Compares one survey with the survey before it.
     */
    @FunctionalInterface
    public interface ChangeDetector
    {
        /**
         * Determine whether a survey found a change since the survey before it.
         *
         * @param surveyIndex position of the survey in the completed survey history, most recent first - so
         *                    0 compares the latest completed survey with the one before it
         * @return true if the survey found a change
         * @throws InvalidParameterException  the parameters are invalid
         * @throws PropertyServerException    problem accessing the property server
         * @throws UserNotAuthorizedException user is not authorized to issue this request
         */
        boolean surveyFoundChange(int surveyIndex) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;
    }


    /**
     * The outcome of {@link #decide}: whether a survey is due, and why.
     *
     * @param due true if a survey should be requested
     * @param reason why, in words suitable for an audit log message
     */
    public record Decision(boolean due, String reason) { }


    private final int minimumIntervalDays;
    private final int maximumIntervalDays;


    /**
     * Constructor.  A negative minimum is treated as zero, and a maximum below the minimum as the minimum.
     *
     * @param minimumIntervalDays fewest days between surveys
     * @param maximumIntervalDays most days between surveys
     */
    public SurveySchedule(int minimumIntervalDays,
                          int maximumIntervalDays)
    {
        this.minimumIntervalDays = Math.max(0, minimumIntervalDays);
        this.maximumIntervalDays = Math.max(this.minimumIntervalDays, maximumIntervalDays);
    }


    /**
     * Return the fewest days between surveys.
     *
     * @return number of days
     */
    public int getMinimumIntervalDays()
    {
        return minimumIntervalDays;
    }


    /**
     * Return the most days between surveys.
     *
     * @return number of days
     */
    public int getMaximumIntervalDays()
    {
        return maximumIntervalDays;
    }


    /**
     * Return the largest number of completed surveys, most recent first, that {@link #isSurveyDue} can need
     * to look at.  A caller retrieving the survey history need not retrieve any more than this.
     *
     * @return number of completed surveys
     */
    public int getCompletedSurveysNeeded()
    {
        return maximumIntervalDays - minimumIntervalDays + 1;
    }


    /**
     * Determine whether a survey is due.
     * <br><br>
     * The interval is the minimum plus a day for each of the most recent surveys in a row that found no change,
     * up to the maximum.  So with d whole days since the latest survey, the survey is due unless each of the
     * latest (d - minimum + 1) completed surveys found no change since the one before it.  The comparisons stop
     * at the first change.  A completed survey with no completed survey before it to compare with counts as a
     * change, because there is no evidence yet that the data store is stable.
     *
     * @param latestSurveyTime when the latest survey started - completed or not - or null if there has never
     *                         been one.  Measuring from the latest attempt means a survey that keeps failing is
     *                         retried at the minimum interval rather than on every refresh.
     * @param completedSurveyCount number of completed surveys available to the change detector
     * @param changeDetector compares a completed survey with the one before it
     * @param now the current time in milliseconds
     * @return true if a survey should be requested
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    public boolean isSurveyDue(Date           latestSurveyTime,
                               int            completedSurveyCount,
                               ChangeDetector changeDetector,
                               long           now) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        return this.decide(latestSurveyTime, completedSurveyCount, changeDetector, now).due();
    }


    /**
     * Determine whether a survey is due, and why - see {@link #isSurveyDue}.
     *
     * @param latestSurveyTime when the latest survey started - completed or not - or null if there has never
     *                         been one
     * @param completedSurveyCount number of completed surveys available to the change detector
     * @param changeDetector compares a completed survey with the one before it
     * @param now the current time in milliseconds
     * @return decision and the reason for it
     * @throws InvalidParameterException  the parameters are invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to issue this request
     */
    public Decision decide(Date           latestSurveyTime,
                           int            completedSurveyCount,
                           ChangeDetector changeDetector,
                           long           now) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException
    {
        if (latestSurveyTime == null)
        {
            return new Decision(true, "it has never been surveyed");
        }

        long daysSinceLatestSurvey = (now - latestSurveyTime.getTime() + TIME_TOLERANCE) / ONE_DAY;

        if (daysSinceLatestSurvey < minimumIntervalDays)
        {
            return new Decision(false, daysSinceLatestSurvey + " day(s) since the latest survey is less than the minimum interval of "
                    + minimumIntervalDays + " day(s)");
        }

        if (daysSinceLatestSurvey >= maximumIntervalDays)
        {
            return new Decision(true, daysSinceLatestSurvey + " day(s) since the latest survey reaches the maximum interval of "
                    + maximumIntervalDays + " day(s)");
        }

        long comparisonsNeeded = daysSinceLatestSurvey - minimumIntervalDays + 1;

        for (int surveyIndex = 0; surveyIndex < comparisonsNeeded; surveyIndex++)
        {
            int intervalDays = minimumIntervalDays + surveyIndex;

            if (surveyIndex + 1 >= completedSurveyCount)
            {
                return new Decision(true, daysSinceLatestSurvey + " day(s) since the latest survey reaches the interval of "
                        + intervalDays + " day(s), because there are only " + completedSurveyCount
                        + " completed survey(s) to compare");
            }

            if (changeDetector.surveyFoundChange(surveyIndex))
            {
                return new Decision(true, daysSinceLatestSurvey + " day(s) since the latest survey reaches the interval of "
                        + intervalDays + " day(s), because completed survey " + (surveyIndex + 1)
                        + " (most recent first) found a change since the survey before it");
            }
        }

        return new Decision(false, "the latest " + comparisonsNeeded + " completed survey(s) found no change, so "
                + daysSinceLatestSurvey + " day(s) since the latest survey is within the interval");
    }
}
