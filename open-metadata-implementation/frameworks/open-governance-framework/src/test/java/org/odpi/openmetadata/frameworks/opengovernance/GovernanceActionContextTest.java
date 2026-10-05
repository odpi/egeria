/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.opengovernance.client.GovernanceCompletionInterface;
import org.odpi.openmetadata.frameworks.opengovernance.ffdc.OGFErrorCode;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;


/**
 * Verify that GovernanceActionContext only holds a completion status once it has been recorded on the engine
 * action.  The engine host asks the context for its completion status to decide whether a service that has
 * failed still needs to be recorded as failed; a status held for an update that never happened makes it skip
 * that step, and the engine action - and any process waiting on it - stays IN_PROGRESS for ever.
 */
public class GovernanceActionContextTest
{
    /**
     * A completion status that is recorded on the engine action is held by the context.
     */
    @Test public void testRecordedStatusIsHeld() throws Exception
    {
        GovernanceActionContext context = newContext(new CompletionClient(false));

        context.recordCompletionStatus(CompletionStatus.ACTIONED, List.of("done"), null, null, (String) null);

        assertEquals(context.getCompletionStatus(), CompletionStatus.ACTIONED);
    }


    /**
     * A completion status that could not be recorded on the engine action is not held, whichever variant of
     * recordCompletionStatus is used.
     */
    @Test public void testUnrecordedStatusIsNotHeld()
    {
        GovernanceActionContext context = newContext(new CompletionClient(true));

        try
        {
            context.recordCompletionStatus(CompletionStatus.ACTIONED, List.of("done"));
            fail("The completion client's failure was not passed on.");
        }
        catch (PropertyServerException expected)
        {
            assertNull(context.getCompletionStatus(), "A status was held although recording it failed.");
        }
        catch (Exception unexpected)
        {
            fail("Unexpected exception " + unexpected);
        }

        try
        {
            context.recordCompletionStatus(CompletionStatus.ACTIONED, List.of("done"), (List<NewActionTarget>) null);
            fail("The completion client's failure was not passed on.");
        }
        catch (PropertyServerException expected)
        {
            assertNull(context.getCompletionStatus(), "A status was held although recording it failed.");
        }
        catch (Exception unexpected)
        {
            fail("Unexpected exception " + unexpected);
        }

        try
        {
            context.recordCompletionStatus(CompletionStatus.ACTIONED, List.of("done"), null, null, (String) null);
            fail("The completion client's failure was not passed on.");
        }
        catch (PropertyServerException expected)
        {
            assertNull(context.getCompletionStatus(), "A status was held although recording it failed.");
        }
        catch (Exception unexpected)
        {
            fail("Unexpected exception " + unexpected);
        }
    }


    /**
     * Build a context whose only working collaborator is the completion client.
     *
     * @param completionClient completion client
     * @return context
     */
    private GovernanceActionContext newContext(GovernanceCompletionInterface completionClient)
    {
        return new GovernanceActionContext("server",
                                           "service",
                                           null,
                                           null,
                                           "connectorId",
                                           "connectorName",
                                           "userId",
                                           "connectorGUID",
                                           false,
                                           null,
                                           null,
                                           100,
                                           null,
                                           "engineActionGUID",
                                           "requestType",
                                           null,
                                           "requesterUserId",
                                           null,
                                           null,
                                           null,
                                           null,
                                           null,
                                           null,
                                           completionClient,
                                           null,
                                           null);
    }


    /**
     * A completion client that either accepts every completion status or rejects them all as if the metadata
     * access store could not be reached.
     */
    private static class CompletionClient implements GovernanceCompletionInterface
    {
        private final boolean failRecording;

        /**
         * Constructor.
         *
         * @param failRecording should recordCompletionStatus fail
         */
        CompletionClient(boolean failRecording)
        {
            this.failRecording = failRecording;
        }

        @Override
        public void updateEngineActionStatus(String userId, String engineActionGUID, ActivityStatus activityStatus)
        {
        }

        @Override
        public List<EngineActionElement> getApprovedEngineActions(String userId, String governanceEngineGUID, int startFrom, int pageSize)
        {
            return null;
        }

        @Override
        public List<EngineActionElement> getActiveClaimedEngineActions(String userId, String governanceEngineGUID, int startFrom, int pageSize)
        {
            return null;
        }

        @Override
        public void claimEngineAction(String userId, String engineActionGUID)
        {
        }

        @Override
        public void updateActionTargetStatus(String userId, String actionTargetGUID, ActivityStatus status, Date startDate, Date completionDate, String completionMessage)
        {
        }

        @Override
        public void recordCompletionStatus(String                userId,
                                           String                engineActionGUID,
                                           Map<String, String>   requestParameters,
                                           CompletionStatus      status,
                                           List<String>          outputGuards,
                                           List<NewActionTarget> newActionTargets,
                                           String                completionMessage) throws PropertyServerException
        {
            if (failRecording)
            {
                throw new PropertyServerException(OGFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition("service",
                                                                                                         "SQLException",
                                                                                                         "recordCompletionStatus",
                                                                                                         "Connection is closed"),
                                                  this.getClass().getName(),
                                                  "recordCompletionStatus");
            }
        }
    }
}
