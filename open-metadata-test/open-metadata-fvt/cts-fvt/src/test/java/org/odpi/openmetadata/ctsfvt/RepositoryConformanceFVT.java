/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.ctsfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceProfilePriority;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceProfileSummary;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceStatus;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceTestLabSummary;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceWorkbenchStatus;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceWorkbenchSummary;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCaseResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RepositoryConformanceFVT runs the Open Metadata Conformance Suite's repository workbench against the
 * PostgreSQL repository and reports what it found.
 * <br>
 * The work is done by the conformance suite itself; what this class contributes is the waiting and the
 * reading of results.  The workbench starts as soon as the technology under test registers in the cohort
 * and runs to completion on its own schedule, so the first thing here is to wait for it - and a run that
 * never completes is itself the finding, since it means the cohort never formed or the workbench could
 * not reach the repository through it.
 * <br>
 * Once it has finished, two things are asserted, matching what conformance actually means:
 * <ul>
 *     <li>every <b>mandatory</b> profile is conformant - these are the requirements a repository has to
 *     meet to avoid doing harm to the other repositories it shares metadata with;</li>
 *     <li>no test case failed.</li>
 * </ul>
 * Optional profiles are reported but not asserted on: a repository that does not implement an optional
 * capability is still conformant, and failing the run for that would make the harness useless for
 * measuring where the repository actually stands.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class RepositoryConformanceFVT
{
    /*
     * One report directory per kind of repository, so that running the other kind does not overwrite the
     * results you are still reading - and so that the two can be compared side by side.
     */
    private static final File REPORT_DIRECTORY = new File("build/cts-fvt-report/"
                                                                  + OMAGPlatformExtension.REPOSITORY_KIND.getReportDirectoryName());
    private static final File AUDIT_LOG        = new File("build/cts-fvt-data/logs/audit.log");

    private static OpenMetadataConformanceTestLabSummary summary        = null;
    private static List<OpenMetadataTestCaseResult>      failedTestCases = null;


    /**
     * Wait for the repository workbench to finish, then read its results once and share them with the
     * test cases below.
     * <br>
     * The wait is deliberately patient.  A full workbench run works through every open metadata type
     * against a real database and legitimately takes a long time; the timeout exists to stop a run that
     * has genuinely stalled, not to put the workbench on a schedule.  Both the limit and the polling
     * interval come from application.properties.
     *
     * @throws Exception the workbench did not finish, or its results could not be read
     */
    @BeforeAll
    static void awaitWorkbenchCompletion() throws Exception
    {
        ConformanceSuiteClient client = new ConformanceSuiteClient();

        long timeoutSeconds = OMAGPlatformExtension.getLongProperty("cts.fvt.workbench.timeout.seconds", 5400);
        long pollSeconds    = OMAGPlatformExtension.getLongProperty("cts.fvt.workbench.poll.seconds", 15);

        long deadline = System.currentTimeMillis() + (timeoutSeconds * 1000L);

        OpenMetadataConformanceWorkbenchStatus status        = null;
        int                                    testCaseCount = 0;

        System.out.println("cts-fvt: waiting for the repository workbench to complete (up to " + timeoutSeconds + "s)");

        while (System.currentTimeMillis() < deadline)
        {
            status = client.getRepositoryWorkbenchStatus();

            if ((status != null) && (Boolean.TRUE.equals(status.getWorkbenchComplete())))
            {
                break;
            }

            testCaseCount = client.getTestCaseCount();

            System.out.println("cts-fvt: workbench running - " + testCaseCount
                                       + " test case(s) recorded, " + ((deadline - System.currentTimeMillis()) / 1000L)
                                       + "s left");

            Thread.sleep(pollSeconds * 1000L);
        }

        assertNotNull(status,
                      "The conformance test server never reported a status for the "
                              + ConformanceSuiteClient.REPOSITORY_WORKBENCH_ID + ".");

        /*
         * Read and save the results before deciding whether the run passed.  A workbench that ran out of time
         * has still done hours of real work, and the profile results and failures it did record are worth
         * having - discarding them because the run was incomplete would waste the most expensive part of it.
         */
        summary         = client.getSummary();
        failedTestCases = client.getFailedTestCases();

        ConformanceSuiteClient.writeJson(new File(REPORT_DIRECTORY, "conformance-summary.json"), summary);
        ConformanceSuiteClient.writeJson(new File(REPORT_DIRECTORY, "failed-test-cases.json"), failedTestCases);

        System.out.println("cts-fvt: results written to " + REPORT_DIRECTORY.getAbsolutePath());

        /*
         * Distinguish the two ways a run fails to finish, because they have nothing to do with each other.  A
         * workbench that has recorded test cases is working and simply ran out of time.  One that has recorded
         * none never started, and that means it never got hold of the technology under test through the cohort.
         */
        if (! Boolean.TRUE.equals(status.getWorkbenchComplete()))
        {
            if (testCaseCount > 0)
            {
                fail("The " + ConformanceSuiteClient.REPOSITORY_WORKBENCH_ID + " was still running when the "
                             + timeoutSeconds + "s limit was reached, having recorded " + testCaseCount
                             + " test case(s).  It had not stalled - it needs longer than this run allowed."
                             + "  Either raise cts.fvt.workbench.timeout.seconds, or scope the run down with"
                             + " cts.fvt.workbench.entity.types, which limits the workbench to the entity types"
                             + " named there instead of every type in the model.  What it did manage to record"
                             + " has been written to " + REPORT_DIRECTORY.getPath() + ".");
            }

            fail("The " + ConformanceSuiteClient.REPOSITORY_WORKBENCH_ID + " recorded no test cases at all within "
                         + timeoutSeconds + "s, so it never started.  It waits for "
                         + OMAGPlatformExtension.TUT_SERVER_NAME + " to register in cohort "
                         + OMAGPlatformExtension.COHORT_NAME + " and then has to build a connector to it, so the"
                         + " cause is on that path - check " + AUDIT_LOG.getPath() + " for whether the cohort"
                         + " connected, whether the registration arrived, and whether the connector could be"
                         + " built (OMRS-AUDIT-0114 reports a remote member whose connection could not be used).");
        }

        System.out.println("cts-fvt: workbench complete - " + testCaseCount + " test case(s) recorded");
    }


    /**
     * The run must actually have produced a summary for the repository workbench, naming the technology
     * it tested.  Without this, the assertions below would pass vacuously on an empty report.
     */
    @Test
    void theWorkbenchReportedOnTheTechnologyUnderTest()
    {
        OpenMetadataConformanceWorkbenchSummary workbenchSummary = getRepositoryWorkbenchSummary();

        assertNotNull(workbenchSummary,
                      "The conformance report contains no summary for the "
                              + ConformanceSuiteClient.REPOSITORY_WORKBENCH_ID + ".");

        assertNotNull(workbenchSummary.getProfileSummaries(),
                      "The " + ConformanceSuiteClient.REPOSITORY_WORKBENCH_ID
                              + " reported no profiles at all, so nothing was actually evaluated.");

        assertFalse(workbenchSummary.getProfileSummaries().isEmpty(),
                    "The " + ConformanceSuiteClient.REPOSITORY_WORKBENCH_ID
                            + " reported an empty list of profiles, so nothing was actually evaluated.");
    }


    /**
     * Every mandatory profile must be conformant.  These are the requirements that stop a repository doing
     * harm to the others it shares metadata with, so a failure here means the repository is not conformant
     * - not merely that it is missing an optional capability.
     */
    @Test
    void everyMandatoryProfileIsConformant()
    {
        OpenMetadataConformanceWorkbenchSummary workbenchSummary = getRepositoryWorkbenchSummary();
        List<String>                            notConformant    = new ArrayList<>();

        if ((workbenchSummary != null) && (workbenchSummary.getProfileSummaries() != null))
        {
            for (OpenMetadataConformanceProfileSummary profileSummary : workbenchSummary.getProfileSummaries())
            {
                if ((profileSummary != null)
                            && (OpenMetadataConformanceProfilePriority.MANDATORY_PROFILE.equals(profileSummary.getProfilePriority()))
                            && (! isConformant(profileSummary.getConformanceStatus())))
                {
                    notConformant.add(profileSummary.getName() + " (" + profileSummary.getConformanceStatus() + ")");
                }
            }
        }

        assertTrue(notConformant.isEmpty(),
                   "The repository is not conformant: " + notConformant.size()
                           + " mandatory profile(s) were not met - " + notConformant
                           + ".  The full profile-by-profile results are in " + REPORT_DIRECTORY.getPath() + ".");
    }


    /**
     * No test case may fail.  A failed test case is reported with the profile and requirement it belongs
     * to, so naming them here points straight at what to look at in the saved report.
     */
    @Test
    void noTestCaseFailed()
    {
        List<String> failures = new ArrayList<>();

        if (failedTestCases != null)
        {
            for (OpenMetadataTestCaseResult result : failedTestCases)
            {
                if (result != null)
                {
                    failures.add(result.getTestCaseId());
                }
            }
        }

        assertTrue(failures.isEmpty(),
                   failures.size() + " conformance test case(s) failed: " + failures
                           + ".  The full result for each one, including the assertions that failed and the"
                           + " properties involved, is in " + REPORT_DIRECTORY.getPath() + "/failed-test-cases.json.");
    }


    /**
     * Report where the repository stands on the optional profiles.  This never fails the run - a
     * repository that does not implement an optional capability is still conformant - but it is the part
     * of the report that says what the repository can actually do, so it is worth printing.
     */
    @Test
    void reportOptionalProfiles()
    {
        OpenMetadataConformanceWorkbenchSummary workbenchSummary = getRepositoryWorkbenchSummary();

        if ((workbenchSummary != null) && (workbenchSummary.getProfileSummaries() != null))
        {
            System.out.println("cts-fvt: optional profile results for " + workbenchSummary.getTutName() + ":");

            for (OpenMetadataConformanceProfileSummary profileSummary : workbenchSummary.getProfileSummaries())
            {
                if ((profileSummary != null)
                            && (OpenMetadataConformanceProfilePriority.OPTIONAL_PROFILE.equals(profileSummary.getProfilePriority())))
                {
                    System.out.println("    " + profileSummary.getName() + ": " + profileSummary.getConformanceStatus());
                }
            }
        }
    }


    /**
     * Return the repository workbench's entry in the run summary.
     *
     * @return workbench summary, or null if the workbench did not report one
     */
    private static OpenMetadataConformanceWorkbenchSummary getRepositoryWorkbenchSummary()
    {
        if ((summary == null) || (summary.getTestSummariesFromWorkbenches() == null))
        {
            return null;
        }

        for (OpenMetadataConformanceWorkbenchSummary workbenchSummary : summary.getTestSummariesFromWorkbenches())
        {
            if ((workbenchSummary != null)
                        && (ConformanceSuiteClient.REPOSITORY_WORKBENCH_ID.equals(workbenchSummary.getWorkbenchId())))
            {
                return workbenchSummary;
            }
        }

        return null;
    }


    /**
     * A profile counts as conformant unless the suite says it is not.  "Conformant with no support" is
     * still conformant - it means the repository does not offer the capability but does not misbehave
     * when asked about it.
     *
     * @param conformanceStatus status reported by the suite
     * @return whether it counts as conformant
     */
    private static boolean isConformant(OpenMetadataConformanceStatus conformanceStatus)
    {
        return (conformanceStatus != null) && (! OpenMetadataConformanceStatus.NOT_CONFORMANT.equals(conformanceStatus));
    }
}
