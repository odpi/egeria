/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;


import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.governanceaction.WatchdogGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogEventType;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogGovernanceEvent;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogMetadataElementEvent;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;

import java.util.ArrayList;
import java.util.List;


/**
 * MonitorForNewAssetGovernanceActionConnector is a Watchdog Governance Action Service that listens for new or refreshed Assets
 * and kicks off a governance action process to validate that this Asset is correctly set up.
 * It is designed to run continuously and so does not set up completion status or guards.  This means its
 * Governance Action entity is never completed and this service is restarted each time the hosting engine is restarted.
 */
public class MonitorForNewAssetGovernanceActionConnector extends WatchdogGovernanceActionService
{

    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     *
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        MonitorForNewAssetListener listener = new MonitorForNewAssetListener(this);

        String assetTypeName = "Asset";

        try
        {
            /*
             *
             */
            List<WatchdogEventType> interestingEventTypes = new ArrayList<>();

            interestingEventTypes.add(WatchdogEventType.NEW_ELEMENT);
            interestingEventTypes.add(WatchdogEventType.REFRESHED_ELEMENT);

            List<String> interestingMetadataTypes = new ArrayList<>();

            // todo generalize
            interestingMetadataTypes.add("Asset");

            interestingMetadataTypes.add(assetTypeName);
            governanceContext.registerListener(listener,
                                               interestingEventTypes,
                                               interestingMetadataTypes,
                                               null);
        }
        catch (Exception error)
        {
            completed = false;

            try
            {
                List<String> outputGuards = new ArrayList<>();

                outputGuards.add(MonitorForNewAssetGovernanceActionProvider.MONITORING_FAILED);
                governanceContext.recordCompletionStatus(CompletionStatus.FAILED, outputGuards, null);
            }
            catch (Exception nestedError)
            {
                // todo log audit log message
            }

            // todo log audit log message
        }
    }


    /**
     * This method is called each time a potentially new asset is received.  It triggers a governance action process to validate and
     * enrich the asset as required.
     *
     * @param event event containing details of a change to an open metadata element.
     *
     * @throws GovernanceServiceException reports that the event can not be processed (this is logged but
     *                                    no other action is taken).  The listener will continue to be
     *                                    called until the watchdog governance action service declares it is complete
     *                                    or administrator action shuts down the service.
     */
    void processEvent(WatchdogGovernanceEvent event) throws GovernanceServiceException
    {
        if (event instanceof WatchdogMetadataElementEvent)
        {
            WatchdogMetadataElementEvent watchdogMetadataElementEvent = (WatchdogMetadataElementEvent)event;

            try
            {
                String assetGUID = watchdogMetadataElementEvent.getMetadataElement().getElementGUID();

                List<String> actionTargetGUIDs = new ArrayList<>();

                actionTargetGUIDs.add(assetGUID);

                // todo generalize the process qualified name
                governanceContext.initiateGovernanceActionProcess("CurateNewAsset",
                                                                  null,
                                                                  actionTargetGUIDs,
                                                                  null);
            }
            catch (OCFCheckedExceptionBase nestedError)
            {
                // todo log error

                throw new GovernanceServiceException(nestedError.getMessage(), nestedError);
            }
        }
        else
        {
            // todo log error
        }
    }


    /**
     * Disconnect is called either because this governance action service called governanceContext.recordCompletionStatus()
     * or the administer requested this governance action service stop running or the hosting server is shutting down.
     *
     * If disconnect completes before the governance action service records
     * its completion status then the governance action service is restarted either at the administrator's request or the next time the server starts.
     * If you do not want this governance action service restarted, be sure to record the completion status in disconnect().
     *
     * The disconnect() method is a standard method from the Open Connector Framework (OCF).  If you need to override this method
     * be sure to call super.disconnect() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {

        super.disconnect();
    }





    protected volatile boolean           completed         = false;

    /**
     * Declare that all of the processing for the governance action service is finished and the status of the work.
     * This method handles exceptions from the governance context by logging them to the audit log.
     *
     * @param completionStatus completion status enum value
     * @param completionGuard optional guard string for triggering subsequent action(s)
     */
    protected synchronized void completeGovernanceActionService(CompletionStatus completionStatus,
                                                                String           completionGuard)
    {
        List<String> completionGuards = new ArrayList<>();

        completionGuards.add(completionGuard);

        this.completeGovernanceActionService(completionStatus, completionGuards, null);
    }


    /**
     * Declare that all of the processing for the governance action service is finished and the status of the work.
     * This method handles exceptions from the governance context by logging them to the audit log.
     *
     * @param completionStatus completion status enum value
     * @param completionGuards optional guard strings for triggering subsequent action(s)
     */
    protected synchronized void completeGovernanceActionService(CompletionStatus completionStatus,
                                                                List<String>     completionGuards)
    {
        completeGovernanceActionService(completionStatus, completionGuards, null);
    }


    /**
     * Declare that all of the processing for the governance action service is finished and the status of the work.
     * This method handles exceptions from the governance context by logging them to the audit log.
     *
     * @param completionStatus completion status enum value
     * @param completionGuards optional guard strings for triggering subsequent action(s)
     * @param nextActionTargetGUIDs list of additional elements to add to the action targets for the next phase
     */
    protected synchronized void completeGovernanceActionService(CompletionStatus completionStatus,
                                                                List<String>     completionGuards,
                                                                List<String>     nextActionTargetGUIDs)
    {
        if ((! completed) && (governanceContext != null))
        {
            try
            {
                governanceContext.recordCompletionStatus(completionStatus, completionGuards, nextActionTargetGUIDs);
            }
            catch (Exception nestedError)
            {
                // todo log audit log message
            }
        }
        else
        {
            // todo
        }
    }
}
