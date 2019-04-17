/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.platform;

import org.odpi.openmetadata.conformance.auditlog.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCase;
import org.odpi.openmetadata.conformance.workbenches.platform.PlatformConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.platform.PlatformConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

/**
 * OpenMetadataTestCase is the superclass for an open metadata conformance test.  It manages the
 * test environment and reporting.
 */
public abstract class OpenMetadataPlatformTestCase extends OpenMetadataTestCase
{
    protected PlatformConformanceWorkPad  platformConformanceWorkPad;

    /**
     * Constructor passes the standard descriptive information to the superclass.
     *
     * @param workPad  workspace.
     * @param testCaseId  id of the test case
     * @param testCaseName  name of the test case
     */
    protected OpenMetadataPlatformTestCase(PlatformConformanceWorkPad workPad,
                                           String                     testCaseId,
                                           String                     testCaseName)
    {
        super(workPad,
              testCaseId,
              testCaseName,
              PlatformConformanceProfileRequirement.ORIGIN_IDENTIFIER.getProfileId(),
              PlatformConformanceProfileRequirement.ORIGIN_IDENTIFIER.getRequirementId());

        this.platformConformanceWorkPad = workPad;
    }


    /**
     * Log that the test case is starting.
     *
     * @param methodName calling method name
     */
    protected void logTestStart(String methodName)
    {
        if (workPad != null)
        {
            OMRSAuditLog auditLog = platformConformanceWorkPad.getAuditLog();

            ConformanceSuiteAuditCode auditCode = ConformanceSuiteAuditCode.TEST_CASE_INITIALIZING;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(testCaseId,
                                                                testCaseDescriptionURL),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * Log that the test case is ending.
     *
     * @param methodName calling method name
     */
    protected void logTestEnd(String methodName)
    {
        if (workPad != null)
        {
            Integer    exceptionCount;

            if (exceptionBean == null)
            {
                exceptionCount = 0;
            }
            else
            {
                exceptionCount = 1;
            }

            OMRSAuditLog auditLog = platformConformanceWorkPad.getAuditLog();

            if (successMessage == null)
            {
                ConformanceSuiteAuditCode auditCode = ConformanceSuiteAuditCode.TEST_CASE_COMPLETED;
                auditLog.logRecord(methodName,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(testCaseId,
                                                                    Integer.toString(successfulAssertions.size()),
                                                                    Integer.toString(unsuccessfulAssertions.size()),
                                                                    Integer.toString(exceptionCount),
                                                                    Integer.toString(discoveredProperties.size())),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
            }
            else
            {
                ConformanceSuiteAuditCode auditCode = ConformanceSuiteAuditCode.TEST_CASE_COMPLETED_SUCCESSFULLY;
                auditLog.logRecord(methodName,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(testCaseId,
                                                                    Integer.toString(successfulAssertions.size()),
                                                                    Integer.toString(unsuccessfulAssertions.size()),
                                                                    Integer.toString(exceptionCount),
                                                                    Integer.toString(discoveredProperties.size()),
                                                                    successMessage),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
            }
        }
    }
}
