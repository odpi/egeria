/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.liskov;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;


/**
 * Checks that Liskov runs only the survey that describes an element most fully.
 */
public class SurveySelectorTest
{
    /**
     * Once every survey has been tried, the one producing the most annotation types is chosen.
     */
    @Test
    public void testMostAnnotationTypesWins()
    {
        assertEquals(SurveySelector.chooseSurvey(List.of(new SurveySelector.Candidate<>("basic", true, 3),
                                                         new SurveySelector.Candidate<>("detailed", true, 7),
                                                         new SurveySelector.Candidate<>("failed", true, 0))),
                     "detailed");
    }


    /**
     * A survey never run against the element is tried first, in listed order, even after others have results.
     */
    @Test
    public void testUntriedSurveyFirst()
    {
        assertEquals(SurveySelector.chooseSurvey(List.of(new SurveySelector.Candidate<>("basic", true, 3),
                                                         new SurveySelector.Candidate<>("detailed", false, 0),
                                                         new SurveySelector.Candidate<>("other", false, 0))),
                     "detailed");
    }


    /**
     * Ties go to the first listed so the choice does not flip between refreshes.
     */
    @Test
    public void testTieIsStable()
    {
        assertEquals(SurveySelector.chooseSurvey(List.of(new SurveySelector.Candidate<>("a", true, 4),
                                                         new SurveySelector.Candidate<>("b", true, 4))),
                     "a");
    }


    /**
     * No candidates, no survey.
     */
    @Test
    public void testNoCandidates()
    {
        assertNull(SurveySelector.chooseSurvey(null));
        assertNull(SurveySelector.chooseSurvey(List.of()));
        assertNull(SurveySelector.chooseSurvey(Arrays.asList((SurveySelector.Candidate<String>) null)));
    }
}
