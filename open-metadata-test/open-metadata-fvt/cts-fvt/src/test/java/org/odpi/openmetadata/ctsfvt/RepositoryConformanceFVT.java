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
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceProfileResults;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceRequirementResults;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceTestEvidence;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceTestLabResults;
import org.odpi.openmetadata.conformance.beans.OpenMetadataConformanceWorkbenchResults;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCaseResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        /*
         * A workbench that has not recorded its first test case is not working slowly - it has not started,
         * and waiting the full budget for it establishes nothing.  The two states have different causes and
         * different fixes, so they are given different deadlines: this one is short, because the only thing
         * that has to happen before the first test case is that the servers find each other in the cohort.
         */
        long startupTimeoutSeconds = OMAGPlatformExtension.getLongProperty("cts.fvt.workbench.startup.timeout.seconds", 300);

        long deadline        = System.currentTimeMillis() + (timeoutSeconds * 1000L);
        long startupDeadline = System.currentTimeMillis() + (startupTimeoutSeconds * 1000L);

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

            if ((testCaseCount == 0) && (System.currentTimeMillis() > startupDeadline))
            {
                fail("The " + ConformanceSuiteClient.REPOSITORY_WORKBENCH_ID + " had recorded no test cases after "
                             + startupTimeoutSeconds + "s, so it never started - it is not running slowly, and waiting"
                             + " the remaining " + ((deadline - System.currentTimeMillis()) / 1000L) + "s would"
                             + " establish nothing.  The workbench waits for " + OMAGPlatformExtension.TUT_SERVER_NAME
                             + " to register in cohort " + OMAGPlatformExtension.COHORT_NAME + " and then has to build"
                             + " a connector to it, so the cause is on that path.  Check " + AUDIT_LOG.getPath()
                             + " for whether the registration was exchanged - OMRS-AUDIT-0114 reports a remote member"
                             + " whose connection could not be used, and Kafka consumer group errors around the"
                             + " registration topic point at the exchange being missed rather than refused."
                             + "  Set cts.fvt.workbench.startup.timeout.seconds to allow longer.");
            }

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

        /*
         * The full report carries the evidence behind every assertion, and each piece of evidence records how
         * long the repository call it made took and which method it called.  Saving it, and reporting what it
         * adds up to, is what turns "the run took three hours" into something that can be acted on.
         */
        try
        {
            OpenMetadataConformanceTestLabResults fullReport = client.getConformanceReport();

            ConformanceSuiteClient.writeJson(new File(REPORT_DIRECTORY, "conformance-report.json"), fullReport);

            reportWhereTheTimeWent(fullReport);
        }
        catch (Exception error)
        {
            System.out.println("cts-fvt: could not save the full report (" + error.getClass().getSimpleName()
                                       + ": " + error.getMessage() + ") - the summary and failures above are unaffected");
        }

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
                             + " named there instead of every type in the model.  Note that this suite ships"
                             + " with that setting already scoped to a small set, so reaching this message means"
                             + " the scope was widened or emptied for this run - check which, before raising the"
                             + " timeout.  What it did manage to record has been written to "
                             + REPORT_DIRECTORY.getPath() + ".");
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
     * The cohort must actually have exchanged events.
     * <br>
     * This one is not a conformance requirement - it is a check on the run itself, and it exists because a
     * run can do a fraction of its work and still look like a pass.  Much of the workbench is driven by
     * events arriving from the technology under test: the type definitions it shares on registration, and
     * the instance events behind the reference copy tests.  When those events do not arrive, the test cases
     * that depend on them are never created rather than failing, so the report comes back smaller and
     * quieter, with a conformance status that reads as success.
     * <br>
     * A measured run in that state recorded 44 incoming events and 1579 test cases; a healthy run of the
     * same scope recorded 4152 events and 3726 test cases.  The cause was a cohort registry left over from
     * a previous run - a server that believes it is already registered has no reason to share its types
     * again - which is why the harness now clears the registries before it starts.  The check stays because
     * the failure is silent by nature, and because a repository that appears to pass while half of the
     * suite never ran is the most expensive kind of wrong answer.
     * <br>
     * The floor is deliberately far below a healthy count rather than close to it.  What is being detected
     * is the difference between events flowing and events not flowing, not a shortfall against an expected
     * number - the healthy count moves with the scope of the run, and a threshold tracking it would need
     * updating every time the scope changed and would fail for reasons that had nothing to do with health.
     */
    @Test
    void theCohortExchangedEvents()
    {
        long minimumEvents = OMAGPlatformExtension.getLongProperty("cts.fvt.cohort.minimum.events", 100);
        long eventCount    = countAuditLogOccurrences("OMRS-AUDIT-8006");

        System.out.println("cts-fvt: the cohort processed " + eventCount + " incoming events");

        assertTrue(eventCount >= minimumEvents,
                   "The cohort processed only " + eventCount + " incoming events, below the " + minimumEvents
                           + " this check expects, so the event-driven part of the workbench did not run and the"
                           + " results above cover less than they appear to.  The usual cause is the servers"
                           + " rejoining a cohort they were already registered with, which skips the type"
                           + " definition exchange - check " + AUDIT_LOG.getPath() + " for whether registration"
                           + " happened, and that the cohort registry stores under data/servers were cleared."
                           + "  Set cts.fvt.cohort.minimum.events to change the floor.");
    }


    /**
     * Count the lines of the audit log carrying a particular message id.  Read line by line: these logs run
     * to tens of megabytes on a full run.
     *
     * @param messageId the audit code to count
     * @return number of occurrences, or zero if the log cannot be read
     */
    private static long countAuditLogOccurrences(String messageId)
    {
        if (! AUDIT_LOG.isFile())
        {
            return 0L;
        }

        long count = 0L;

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(AUDIT_LOG)))
        {
            String line = reader.readLine();

            while (line != null)
            {
                if (line.contains(messageId))
                {
                    count++;
                }

                line = reader.readLine();
            }
        }
        catch (java.io.IOException error)
        {
            System.out.println("cts-fvt: could not read " + AUDIT_LOG.getPath() + " to check the event flow ("
                                       + error.getMessage() + ")");
            return 0L;
        }

        return count;
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

    /**
     * Report what the run spent its time on, from the timings the conformance suite already records against
     * every assertion.
     * <br>
     * Two breakdowns, because they answer different questions.  By test case family - the test case id with
     * its type suffix removed - says which kind of test is expensive, which is what to look at when a run
     * gets slower after new test cases are added.  By repository method says which call is expensive, which
     * is what to look at when a repository gets slower without the tests changing.
     *
     * @param results the full conformance report
     */
    private static void reportWhereTheTimeWent(OpenMetadataConformanceTestLabResults results)
    {
        if ((results == null) || (results.getTestResultsFromWorkbenches() == null))
        {
            return;
        }

        Map<String, long[]> byTestCaseFamily = new HashMap<>();
        Map<String, long[]> byMethod         = new HashMap<>();

        for (OpenMetadataConformanceWorkbenchResults workbench : results.getTestResultsFromWorkbenches())
        {
            if (workbench.getProfileResults() == null)
            {
                continue;
            }

            for (OpenMetadataConformanceProfileResults profile : workbench.getProfileResults())
            {
                if (profile.getRequirementResults() == null)
                {
                    continue;
                }

                for (OpenMetadataConformanceRequirementResults requirement : profile.getRequirementResults())
                {
                    accumulate(requirement.getPositiveTestEvidence(), byTestCaseFamily, byMethod);
                    accumulate(requirement.getNegativeTestEvidence(), byTestCaseFamily, byMethod);
                }
            }
        }

        printTotals("cts-fvt: time by test case family", byTestCaseFamily);
        printTotals("cts-fvt: time by repository method", byMethod);
    }


    /**
     * Add a list of evidence into the two running totals.
     *
     * @param evidenceList evidence to add - may be null
     * @param byTestCaseFamily totals per test case family
     * @param byMethod totals per repository method
     */
    private static void accumulate(List<OpenMetadataConformanceTestEvidence> evidenceList,
                            Map<String, long[]>                       byTestCaseFamily,
                            Map<String, long[]>                       byMethod)
    {
        if (evidenceList == null)
        {
            return;
        }

        for (OpenMetadataConformanceTestEvidence evidence : evidenceList)
        {
            if ((evidence == null) || (evidence.getElapsedTime() == null))
            {
                continue;
            }

            long elapsedTime = evidence.getElapsedTime();

            if (evidence.getTestCaseId() != null)
            {
                add(byTestCaseFamily, testCaseFamily(evidence.getTestCaseId()), elapsedTime);
            }

            if (evidence.getMethodName() != null)
            {
                add(byMethod, evidence.getMethodName(), elapsedTime);
            }
        }
    }


    /**
     * Strip the type name a test case id ends with, so that the hundreds of ids belonging to one kind of test
     * add up together.  The ids are built as "&lt;test case&gt;-&lt;type name&gt;", and type names are the only part
     * that starts with a capital letter, so that is what identifies the suffix.
     *
     * @param testCaseId full test case id
     * @return the family the test case belongs to
     */
    private static String testCaseFamily(String testCaseId)
    {
        int lastSeparator = testCaseId.lastIndexOf('-');

        if ((lastSeparator > 0) && (lastSeparator < testCaseId.length() - 1)
                    && (Character.isUpperCase(testCaseId.charAt(lastSeparator + 1))))
        {
            return testCaseId.substring(0, lastSeparator);
        }

        return testCaseId;
    }


    /**
     * Add one measurement to a total, keeping both the elapsed time and the number of calls.
     *
     * @param totals the totals to add to
     * @param key what the measurement belongs to
     * @param elapsedTime how long it took
     */
    private static void add(Map<String, long[]> totals, String key, long elapsedTime)
    {
        long[] total = totals.computeIfAbsent(key, name -> new long[2]);

        total[0] += elapsedTime;
        total[1]++;
    }


    /**
     * Print a set of totals, heaviest first, so the expensive things are the ones that are read.
     *
     * @param heading what these totals are
     * @param totals the totals
     */
    private static void printTotals(String heading, Map<String, long[]> totals)
    {
        if (totals.isEmpty())
        {
            return;
        }

        System.out.println(heading + " (total time, calls, mean):");

        totals.entrySet()
              .stream()
              .sorted((one, other) -> Long.compare(other.getValue()[0], one.getValue()[0]))
              .limit(15)
              .forEach(entry ->
                       {
                           long elapsedTime = entry.getValue()[0];
                           long calls       = entry.getValue()[1];

                           System.out.printf("    %-56s %8.1fs %8d %8.1fms%n",
                                             entry.getKey(),
                                             elapsedTime / 1000.0,
                                             calls,
                                             (calls == 0) ? 0.0 : (double) elapsedTime / calls);
                       });
    }

}
