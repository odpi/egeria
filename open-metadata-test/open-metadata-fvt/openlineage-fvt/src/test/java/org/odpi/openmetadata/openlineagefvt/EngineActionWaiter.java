/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openlineagefvt;

import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworkservices.gaf.client.EgeriaOpenGovernanceClient;

import java.util.Set;

/**
 * EngineActionWaiter starts governance action types and waits for the resulting engine actions to finish.
 * A Lovelace service runs as an engine action, so this is how the tests run one on demand rather than waiting
 * for the Babbage Analytical Engine's refresh.
 */
class EngineActionWaiter
{
    private static final Set<ActivityStatus> TERMINAL_STATUSES = Set.of(ActivityStatus.COMPLETED,
                                                                        ActivityStatus.INVALID,
                                                                        ActivityStatus.IGNORED,
                                                                        ActivityStatus.FAILED,
                                                                        ActivityStatus.CANCELLED,
                                                                        ActivityStatus.ABANDONED);

    private final EgeriaOpenGovernanceClient openGovernanceClient;
    private final long                       timeoutMilliseconds;
    private final long                       pollMilliseconds;


    /**
     * Constructor.
     *
     * @throws Exception client could not be created
     */
    EngineActionWaiter() throws Exception
    {
        this.openGovernanceClient = new EgeriaOpenGovernanceClient(OMAGPlatformExtension.METADATA_STORE_NAME,
                                                                    OMAGPlatformExtension.getPlatformURLRoot(),
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    OpenLineageFvtTestSupport.MAX_PAGE_SIZE,
                                                                    null);
        this.timeoutMilliseconds = OMAGPlatformExtension.getLongProperty("openlineage.fvt.engine.action.timeout.seconds", 300) * 1000;
        this.pollMilliseconds    = OMAGPlatformExtension.getLongProperty("openlineage.fvt.engine.action.poll.seconds", 2) * 1000;
    }


    /**
     * Return the governance client.
     *
     * @return client
     */
    EgeriaOpenGovernanceClient getOpenGovernanceClient()
    {
        return openGovernanceClient;
    }


    /**
     * Retrieve an engine action.
     *
     * @param engineActionGUID engine action
     * @return element
     * @throws Exception problem calling the metadata store
     */
    EngineActionElement getEngineAction(String engineActionGUID) throws Exception
    {
        return openGovernanceClient.getEngineAction(OMAGPlatformExtension.USER_ID, engineActionGUID);
    }


    /**
     * Wait for an engine action to complete successfully.
     *
     * @param engineActionGUID engine action
     * @param description what the action is, for the failure message
     * @return the completed engine action
     * @throws Exception it did not complete, or completed with a status other than COMPLETED
     */
    EngineActionElement waitForCompletion(String engineActionGUID,
                                          String description) throws Exception
    {
        long                giveUpTime   = System.currentTimeMillis() + timeoutMilliseconds;
        EngineActionElement engineAction = null;

        while (System.currentTimeMillis() < giveUpTime)
        {
            engineAction = getEngineAction(engineActionGUID);

            if ((engineAction != null) && (engineAction.getActionStatus() != null) && (TERMINAL_STATUSES.contains(engineAction.getActionStatus())))
            {
                if (engineAction.getActionStatus() != ActivityStatus.COMPLETED)
                {
                    throw new AssertionError(description + " ended as " + engineAction.getActionStatus()
                                                     + " rather than COMPLETED.  Request type '" + engineAction.getRequestType()
                                                     + "' on engine '" + engineAction.getGovernanceEngineName() + "' said: "
                                                     + engineAction.getCompletionMessage());
                }

                return engineAction;
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new AssertionError(description + " did not finish within " + (timeoutMilliseconds / 1000) + " seconds (last status: "
                                         + ((engineAction == null) ? "not found" : engineAction.getActionStatus())
                                         + ").  The audit log at build/openlineage-fvt-data/logs/audit.log says what the engine host was doing.");
    }
}
