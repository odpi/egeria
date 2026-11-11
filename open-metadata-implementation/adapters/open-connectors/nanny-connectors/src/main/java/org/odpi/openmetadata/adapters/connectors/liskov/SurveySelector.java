/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.liskov;

import java.util.List;


/**
 * Chooses which of the surveys that can be run against an element Liskov should run.  The technology type of an
 * element often lists several surveys - for example, a database server may be surveyed by a general survey and by
 * a more detailed one.  They describe the same element, and running them all multiplies the load on the resource
 * and the number of survey reports, so only one is run: the one whose latest survey produced the most types of
 * annotation, since that one describes the element most fully.
 * <br><br>
 * A survey that has never been run against the element is chosen first, one at a time in the order the technology
 * type lists them, so that each is tried once and its results can be compared.  A survey that ran but did not
 * complete counts as producing no annotation types.  Where surveys tie, the first listed is chosen, so the choice
 * is stable from one refresh to the next.
 */
public class SurveySelector
{
    /**
     * A survey that could be run, with what is known of its results for the element.
     *
     * @param survey the survey - opaque to the selector
     * @param surveyed true if the survey has been run against the element before
     * @param annotationTypeCount number of different types of annotation that the latest completed survey produced;
     *                            zero if no survey completed
     * @param <T> type used to identify the survey
     */
    public record Candidate<T>(T       survey,
                               boolean surveyed,
                               int     annotationTypeCount) {}


    /**
     * Return the survey to run.
     *
     * @param candidates surveys that could be run, in the order the technology type lists them
     * @param <T> type used to identify the survey
     * @return chosen survey, or null if there are no candidates
     */
    public static <T> T chooseSurvey(List<Candidate<T>> candidates)
    {
        if (candidates == null)
        {
            return null;
        }

        Candidate<T> chosen = null;

        for (Candidate<T> candidate : candidates)
        {
            if (candidate == null)
            {
                continue;
            }

            if (! candidate.surveyed())
            {
                return candidate.survey();
            }

            if ((chosen == null) || (candidate.annotationTypeCount() > chosen.annotationTypeCount()))
            {
                chosen = candidate;
            }
        }

        return (chosen == null) ? null : chosen.survey();
    }
}
