/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RequestSourceElement;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The engine host sweeps for missed engine actions every few seconds.  A sweep that failed used to log the same
 * error on every pass - thousands of identical audit log records while the cause persisted.  The error is now
 * logged when it first happens, again if it changes, and again if it comes back after a sweep has succeeded.
 */
public class MissedEngineActionSweepTest
{
    private GovernanceContextClient engineActionClient;
    private AuditLog                auditLog;
    private GovernanceEngineHandler governanceEngineHandler;


    @BeforeMethod
    public void setUp()
    {
        EngineConfig engineConfig = new EngineConfig();

        engineConfig.setEngineQualifiedName("TestEngine");
        engineConfig.setEngineUserId("testengineuser");

        engineActionClient = mock(GovernanceContextClient.class);
        auditLog           = mock(AuditLog.class);

        governanceEngineHandler = new GovernanceEngineHandler(engineConfig,
                                                              "testServer",
                                                              "testServerUser",
                                                              "TestEngineService",
                                                              null,
                                                              engineActionClient,
                                                              auditLog,
                                                              100)
        {
            @Override
            public void runGovernanceService(String                     engineActionGUID,
                                             String                     governanceRequestType,
                                             String                     requesterUserId,
                                             Date                       requestedStartDate,
                                             Map<String, String>        requestParameters,
                                             List<RequestSourceElement> requestSourceElements,
                                             List<ActionTargetElement>  actionTargetElements)
            {
            }
        };
    }


    /**
     * A sweep that keeps failing with the same error logs it once.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testRepeatedErrorIsLoggedOnce() throws Exception
    {
        when(engineActionClient.getActiveEngineActions(anyString(), anyInt(), anyInt()))
                .thenThrow(new IllegalStateException("metadata store unavailable"));

        for (int sweep = 0; sweep < 5; sweep++)
        {
            governanceEngineHandler.startMissedEngineActions();
        }

        verify(auditLog, times(1)).logException(anyString(), any(AuditLogMessageDefinition.class), any(Throwable.class));
    }


    /**
     * A different error is logged, and so is the same error coming back after a sweep has succeeded.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testChangedOrReturningErrorIsLoggedAgain() throws Exception
    {
        IllegalStateException unavailable = new IllegalStateException("metadata store unavailable");

        when(engineActionClient.getActiveEngineActions(anyString(), anyInt(), anyInt()))
                .thenThrow(unavailable)                                          /* logged */
                .thenThrow(unavailable)                                          /* not logged */
                .thenThrow(new IllegalArgumentException("something different"))  /* logged */
                .thenReturn(null)                                                /* sweep succeeds */
                .thenThrow(unavailable);                                         /* logged */

        for (int sweep = 0; sweep < 5; sweep++)
        {
            governanceEngineHandler.startMissedEngineActions();
        }

        verify(auditLog, times(3)).logException(anyString(), any(AuditLogMessageDefinition.class), any(Throwable.class));
    }
}
