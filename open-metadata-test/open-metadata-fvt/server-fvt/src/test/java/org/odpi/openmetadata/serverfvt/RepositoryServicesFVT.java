/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.clients.AuditLogServicesClient;
import org.odpi.openmetadata.repositoryservices.clients.MetadataHighwayServicesClient;
import org.odpi.openmetadata.repositoryservices.properties.CohortDescription;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogReport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RepositoryServicesFVT covers the two operational surfaces of a running server's repository services: its
 * <b>audit log</b> and its <b>metadata highway</b>.
 * <br><br>
 * It does not touch the metadata collection - creating, finding and updating instances is what query-fvt and
 * type-fvt are for, and both need a repository with real content behind them.  What is left is the part that
 * an operator uses rather than a developer, and it is the part with no coverage: "what is this server
 * logging, and who is it federating with?"
 * <br><br>
 * The metadata highway tests run against a server with <b>no cohorts configured</b>, and that is the
 * interesting case rather than a limitation.  A server that has joined no cohort is the normal state of most
 * deployments, and "no cohorts" and "something went wrong" have to be distinguishable - an operator looking
 * at an empty list needs to know which of the two they are seeing.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class RepositoryServicesFVT
{
    /**
     * A running server should report the audit log severities it understands.
     * <br><br>
     * This failed when it was first written, and it is the one defect this suite found that a REST-level test
     * would <em>not</em> have caught: the HTTP call returned 200 with a perfectly well-formed body, and the
     * client could not read it.  That is a fair illustration of why these clients are worth testing as
     * clients rather than as a thin layer over an endpoint.
     * <br><br>
     * Two things were wrong, and the second was hidden by the first.
     * {@code AuditLogSeveritiesResponse.severities} was declared as
     * {@code List&lt;AuditLogRecordSeverity&gt;} - an <em>interface</em> Jackson cannot construct - so every
     * response failed with {@code InvalidDefinitionException: Cannot construct instance of
     * AuditLogRecordSeverity}.  And the severities the server supplies are enum constants, which Jackson
     * serializes by name, so the body was {@code ["UNKNOWN","INFO",...]}: the ordinal and the description -
     * the two things an endpoint called {@code severity-definitions} exists to provide - were never sent at
     * all, to any client in any language.
     * <br><br>
     * The response now holds the concrete {@code OMRSAuditLogReportSeverity}, which was already in the
     * repository for exactly this purpose and unused, so the body carries the whole definition and can be
     * read back.  The method also carried the same copy-paste as the JDBC audit log destination - its
     * {@code methodName} was {@code "getAuditLogReport"} - so its failures named the wrong operation.
     * <br><br>
     * What the test asks for is what a caller needs: this list is how they find out what to pass to the
     * severity filters on the audit log destination setters, so it has to be readable and it has to contain
     * the severities those filters are matched against.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aRunningServerReportsItsAuditLogSeverities() throws Exception
    {
        AuditLogServicesClient client = ServerFvtTestSupport.getAuditLogServicesClient(OMAGPlatformExtension.METADATA_STORE_NAME);

        List<AuditLogRecordSeverity> severities = client.getSeverityList();

        assertNotNull(severities, "A running server should report the audit log severities it understands");
        assertFalse(severities.isEmpty(), "There should be at least one audit log severity");

        boolean foundError = false;

        for (AuditLogRecordSeverity severity : severities)
        {
            assertNotNull(severity.getName(), "Every severity reported should have a name");

            if ("Error".equals(severity.getName()))
            {
                foundError = true;
            }
        }

        assertTrue(foundError,
                   "The severity list should include Error, which is the one every audit log destination filter" +
                           " is expected to be able to name.  It reported: " + severities);
    }


    /**
     * A running server should report on its audit log destinations.
     * <br><br>
     * The report is the only way to see what a server is actually logging to, as opposed to what its
     * configuration document says it should be - and those differ whenever a destination failed to start.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aRunningServerReportsItsAuditLog() throws Exception
    {
        AuditLogServicesClient client = ServerFvtTestSupport.getAuditLogServicesClient(OMAGPlatformExtension.METADATA_STORE_NAME);

        OMRSAuditLogReport report = client.getAuditLogReport();

        assertNotNull(report, "A running server should report on its audit log");
        assertNotNull(report.getOriginatorProperties(),
                      "An audit log report should say which server it came from");
        assertNotNull(report.getDestinationsReport(),
                      "An audit log report should describe the destinations the server is logging to");
    }


    /**
     * A server that has joined no cohorts should say so, rather than failing.
     * <br><br>
     * This failed when it was first written: a server with no cohorts configured has no metadata highway, and
     * every call to this API was answered with {@code OMRS-REST-API-503-003 There is no metadata highway to
     * support REST API call getCohortList} - carried, as Egeria's convention requires, in the body of an
     * HTTP 200 response with {@code relatedHTTPCode} 503 rather than as an HTTP 503.
     * <br><br>
     * The distinction that collapsed is between an empty answer and no answer, and it is the distinction an
     * operator is at this API to make.  A server that has joined no cohort is the normal state of most
     * deployments rather than a fault, so it now reports no cohorts - which is also what the metadata highway
     * manager already did for a cohort it did not hold.  An unknown or unstarted server still fails, because
     * that is a failure the caller needs.
     * <br><br>
     * What makes this test worth keeping is the difference between an answer and a refusal, not the exact
     * shape of the answer - see the comment on the assertion.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aServerWithNoCohortsReportsAnEmptyList() throws Exception
    {
        MetadataHighwayServicesClient client =
                ServerFvtTestSupport.getMetadataHighwayServicesClient(OMAGPlatformExtension.METADATA_STORE_NAME);

        /*
         * The assertion is that this call *answers*, and that the answer is "no cohorts".
         *
         * It is deliberately not an assertion that the answer is an empty list rather than null.  These
         * response beans collapse an empty list to null in their getters - eleven of the twelve in this
         * package do - so "no cohorts" is reported as null by the house convention, and a test demanding an
         * empty list here would be asserting against Egeria's own style rather than against a defect.
         */
        List<CohortDescription> cohorts = client.getCohortDescriptions();

        assertTrue((cohorts == null) || cohorts.isEmpty(),
                   "This suite's metadata access store is configured with no cohorts, so none should be reported." +
                           "  It reported: " + cohorts);
    }


    /**
     * Asking about a cohort the server is not a member of should report no registration, rather than failing.
     * <br><br>
     * This follows the convention the metadata highway manager already uses for a cohort it does not hold:
     * {@code getLocalRegistration(cohortName)} returns null, {@code getRemoteMembers} an empty list, and the
     * connect and disconnect operations {@code false} - "the cohort name was not recognized" - and only a
     * <em>null</em> cohort name is treated as an error.  Not being in a named cohort is an answer, not a
     * failure.
     * <br><br>
     * This test failed when it was first written, but for a reason that had nothing to do with the cohort
     * name: with no cohorts configured the server had no metadata highway at all, so every call to this API
     * was answered with {@code OMRS-REST-API-503-003} regardless of what was asked. See
     * {@link #aServerWithNoCohortsReportsAnEmptyList}.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void askingAboutAnUnknownCohortReportsNoRegistration() throws Exception
    {
        MetadataHighwayServicesClient client =
                ServerFvtTestSupport.getMetadataHighwayServicesClient(OMAGPlatformExtension.METADATA_STORE_NAME);

        assertNull(client.getLocalRegistration("serverFvtCohortThatDoesNotExist"),
                   "A cohort this server is not a member of should report no local registration");
    }


    /**
     * A null cohort name should be refused by the client rather than sent.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aNullCohortNameIsRefusedByTheClient() throws Exception
    {
        MetadataHighwayServicesClient client =
                ServerFvtTestSupport.getMetadataHighwayServicesClient(OMAGPlatformExtension.METADATA_STORE_NAME);

        Exception error = assertThrows(Exception.class,
                                       () -> client.getLocalRegistration(null),
                                       "A null cohort name should be refused");

        assertNotNull(error.getMessage(), "Refusing a null cohort name should say what was wrong");
    }


    /**
     * Asking the audit log of a server that is not running should be reported clearly.
     * <br><br>
     * Worth checking separately from the same question asked of server-operations, because these clients
     * address the server through a different URL shape - the server name is in the client's root URL rather
     * than supplied per call - so the failure arrives by a different route.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void askingTheAuditLogOfAServerThatIsNotRunningIsReportedClearly() throws Exception
    {
        final String unknownServer = "serverFvtServerThatIsNotRunning";

        AuditLogServicesClient client = ServerFvtTestSupport.getAuditLogServicesClient(unknownServer);

        Exception error = assertThrows(Exception.class,
                                       client::getSeverityList,
                                       "Asking a server that is not running for its audit log should fail");

        String message = String.valueOf(error.getMessage());

        assertFalse(message.isBlank(), "A failure at an operational API should say something");
        assertTrue(message.contains(unknownServer),
                   "The message should name the server that was asked about.  It said: " + message);
    }
}
