/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.platform;

import org.odpi.openmetadata.conformance.auditlog.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.tests.platform.OpenMetadataPlatformTestCase;
import org.odpi.openmetadata.conformance.tests.platform.origin.TestOpenMetadataOrigin;
import org.odpi.openmetadata.conformance.workbenches.OpenMetadataConformanceWorkbench;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * PlatformConformanceWorkbench provides the thread that drives the platform workbench from the Open Metadata
 * Conformance Suite.  The properties used to initialise the workbench are passed on the constructor.
 * The run() method contains the workbench logic.  It executes until the tests are complete, or it is signaled to
 * shutdown.
 */
public class PlatformConformanceWorkbench extends OpenMetadataConformanceWorkbench
{
    private static final Logger     log         = LoggerFactory.getLogger(PlatformConformanceWorkbench.class);

    private PlatformConformanceWorkPad workPad;

    /**
     * Constructor receives the work pad with the configuration that provides the location to record the results.
     *
     * @param workPad platform workbench's work pad
     */
    public PlatformConformanceWorkbench(PlatformConformanceWorkPad workPad)
    {
        super(workPad.getWorkbenchId(),
              workPad.getWorkbenchName(),
              workPad.getWorkbenchVersionNumber(),
              workPad.getWorkbenchDocURL());

        final String  methodName = "platformWorkbenchThread.constructor";

        this.workPad = workPad;

        OMRSAuditLog auditLog   = workPad.getAuditLog();

        ConformanceSuiteAuditCode auditCode = ConformanceSuiteAuditCode.WORKBENCH_INITIALIZING;
        auditLog.logRecord(methodName,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(workbenchId, workbenchDocumentationURL),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());
    }

    /**
     * Initialize the list of repository test cases to run.
     *
     * @return list of test cases
     */
    private List<OpenMetadataPlatformTestCase> getTestCases()
    {
        List<OpenMetadataPlatformTestCase> testCases = new ArrayList<>();

        testCases.add(new TestOpenMetadataOrigin(workPad));

        return testCases;
    }


    /**
     * This is the method that provides the behaviour of the thread.
     */
    @Override
    public void run()
    {
        final String   methodName = "platformWorkbenchThread.run";

        if (workPad !=  null)
        {
            OMRSAuditLog              auditLog   = workPad.getAuditLog();
            ConformanceSuiteAuditCode auditCode;

            auditCode = ConformanceSuiteAuditCode.WORKBENCH_INITIALIZED;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(workPad.getWorkbenchId()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());

            try
            {
                List<OpenMetadataPlatformTestCase>   testCases   = this.getTestCases();

                if (testCases != null)
                {
                    for (OpenMetadataPlatformTestCase testCase : testCases)
                    {
                        if (isRunning())
                        {
                            testCase.executeTest();
                        }
                    }
                }
            }
            catch (Exception error)
            {
                log.error(String.format("Unexpected error: %s", error.getMessage()), error);

                auditCode = ConformanceSuiteAuditCode.WORKBENCH_FAILURE;
                auditLog.logRecord(methodName,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(workPad.getWorkbenchId(),
                                                                    error.getMessage()),
                                   error.toString(),
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
            }

            auditCode = ConformanceSuiteAuditCode.WORKBENCH_COMPLETED;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(workPad.getWorkbenchId()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }
}
