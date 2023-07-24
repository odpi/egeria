/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.platform;

import org.odpi.openmetadata.conformance.ffdc.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCase;
import org.odpi.openmetadata.conformance.workbenches.platform.PlatformConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.platform.PlatformConformanceWorkPad;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
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
            AuditLog auditLog = platformConformanceWorkPad.getAuditLog();

            auditLog.logMessage(methodName,
                                ConformanceSuiteAuditCode.TEST_CASE_INITIALIZING.getMessageDefinition(testCaseId,
                                                                                                      testCaseDescriptionURL));
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
            int exceptionCount;

            if (exceptionBean == null)
            {
                exceptionCount = 0;
            }
            else
            {
                exceptionCount = 1;
            }

            AuditLog auditLog = platformConformanceWorkPad.getAuditLog();

            if (successMessage == null)
            {
                auditLog.logMessage(methodName,
                                    ConformanceSuiteAuditCode.TEST_CASE_COMPLETED.getMessageDefinition(testCaseId,
                                                                                                       Integer.toString(successfulAssertions.size()),
                                                                                                       Integer.toString(unsuccessfulAssertions.size()),
                                                                                                       Integer.toString(exceptionCount),
                                                                                                       Integer.toString(discoveredProperties.size())));
            }
            else
            {
                auditLog.logMessage(methodName,
                                    ConformanceSuiteAuditCode.TEST_CASE_COMPLETED_SUCCESSFULLY.getMessageDefinition(testCaseId,
                                                                                                                    Integer.toString(successfulAssertions.size()),
                                                                                                                    Integer.toString(unsuccessfulAssertions.size()),
                                                                                                                    Integer.toString(exceptionCount),
                                                                                                                    Integer.toString(discoveredProperties.size()),
                                                                                                                    successMessage));
            }
        }
    }
}
