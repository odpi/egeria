/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.organizationinsight.karmapoints;

import org.odpi.openmetadata.adapters.connectors.organizationinsight.ffdc.OrgInsightAuditCode;
import org.odpi.openmetadata.adapters.connectors.organizationinsight.ffdc.OrgInsightErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ActorProfileClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ContributionRecordClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContributionRecordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openwatchdog.GenericWatchdogActionListener;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceConnector;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionGuard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * KarmaPointAwardsService fills in the contribution record for an actor profile.  The aim is to
 * perform this update as cheaply as possible, and so some values are cached.
 * Any strangeness in the metadata elements is ignored in the interest of speed.
 * The updates are synchronized to reduce the chance of updating the same contribution record simultaneously
 * in multiple threads.
 */
public class KarmaPointAwardsService extends WatchdogActionServiceConnector
{
    private final GenericWatchdogActionListener listener = new GenericWatchdogActionListener(this);

    private final UserToContributionRecordMap userToContributionRecordMap = new UserToContributionRecordMap();

    private static final Logger log = LoggerFactory.getLogger(KarmaPointAwardsService.class);

    /**
     * Indicates that the watchdog action service is completely configured and can begin processing.
     * This is where the function of the watchdog action service is implemented.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException a problem within the watchdog action service.
     * @throws UserNotAuthorizedException the service was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        try
        {
            watchdogContext.registerListener(listener,
                                             null,
                                             null,
                                             null);
        }
        catch (Exception error)
        {
            try
            {
                List<String> outputGuards = new ArrayList<>();

                outputGuards.add(WatchdogActionGuard.MONITORING_FAILED.getName());

                AuditLogMessageDefinition completionMessage = OrgInsightAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                                                            error.getClass().getName(),
                                                                                                                            methodName,
                                                                                                                            error.getMessage());
                auditLog.logException(methodName, completionMessage, error);

                watchdogContext.recordCompletionStatus(WatchdogActionGuard.MONITORING_FAILED.getCompletionStatus(),
                                                       outputGuards,
                                                       null,
                                                       null,
                                                       completionMessage);
            }
            catch (Exception nestedError)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          OrgInsightAuditCode.UNABLE_TO_SET_COMPLETION_STATUS.getMessageDefinition(watchdogActionServiceName,
                                                                                                                   nestedError.getClass().getName(),
                                                                                                                   nestedError.getMessage()),
                                          nestedError);
                }
            }

            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OrgInsightAuditCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(watchdogActionServiceName,
                                                                                                           error.getClass().getName(),
                                                                                                           error.getMessage()),
                                      error);
            }

            throw new GovernanceServiceException(OrgInsightErrorCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(watchdogActionServiceName,
                                                                                                                      error.getClass().getName(),
                                                                                                                      error.getMessage()),
                                                 error.getClass().getName(),
                                                 methodName,
                                                 error);
        }
    }


    /**
     * This method is called each time a requested event is received from the open metadata repositories.
     * It is called for events received after this listener is registered until the watchdog governance
     * service sets its status in the context as ACTIONED, INVALID, IGNORED or FAILED,
     * or it is stopped by an administrator shutting down
     * the hosting server or this service explicitly.
     *
     * @param event event containing details of a change to an open metadata element.
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        if ((event != null) && (event.getElementHeader() != null))
        {
            if (event.getEventType() != OpenMetadataEventType.REFRESH_ELEMENT_EVENT)
            {
                if (event.getElementHeader().getVersions().getUpdatedBy() != null)
                {
                    if (log.isDebugEnabled())
                    {
                        log.debug("Event: " + event.getEventType() + "; last update user: " + event.getElementHeader().getVersions().getUpdatedBy());
                    }

                    this.incrementKarmaPointForUser(event.getElementHeader().getVersions().getUpdatedBy());
                }
                else
                {
                    if (log.isDebugEnabled())
                    {
                        log.debug("Event: " + event.getEventType() + "; creation user: " + event.getElementHeader().getVersions().getCreatedBy());
                    }
                    this.incrementKarmaPointForUser(event.getElementHeader().getVersions().getCreatedBy());
                }
            }
        }
    }


    /**
     * Karma points are managed in a contribution record that is linked off of a user's profile.  We need to
     * retrieve the userIdentity entity for the user, then locate the profile, then see if
     * the contribution record exists.  If it does not then a new contribution record should be
     * created.
     *
     * @param userId user that should be rewarded with a karma point.
     */
    private synchronized void incrementKarmaPointForUser(String userId)
    {
        final String methodName = "incrementKarmaPointForUser";
        try
        {
            String contributionRecordGUID = userToContributionRecordMap.getContributionRecordGUID(userId);

            if (contributionRecordGUID == null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Unknown contribution record for user: " + userId);
                }

                ActorProfileClient actorProfileClient = watchdogContext.getActorProfileClient();

                OpenMetadataRootElement actorProfile = actorProfileClient.getActorProfileByUserId(userId, actorProfileClient.getGetOptions());

                if (actorProfile != null)
                {
                    /*
                     * Only award karma points to users with a profile.
                     */
                    if (actorProfile.getContributionRecord() != null)
                    {
                        if (log.isDebugEnabled())
                        {
                            log.debug("Updating existing contribution record " + actorProfile.getContributionRecord().getRelatedElement().getElementHeader().getGUID() + " for user: " + userId);
                        }

                        /*
                         * Save the contribution record GUID, so we don't have t look it up again.
                         */
                        userToContributionRecordMap.addUserContributionRecordMapping(userId, actorProfile.getContributionRecord().getRelatedElement().getElementHeader().getGUID());

                        /*
                         * Extract the current value for the karma points and increment.
                         */
                        if (actorProfile.getContributionRecord().getRelatedElement().getProperties() instanceof ContributionRecordProperties contributionRecordProperties)
                        {
                            this.incrementKarmaPointTotalInContributionRecord(actorProfile.getContributionRecord().getRelatedElement().getElementHeader().getGUID(), contributionRecordProperties.getKarmaPoints(), userId);
                        }
                    }
                    else if (actorProfile.getProperties() instanceof ActorProfileProperties actorProfileProperties)
                    {
                        /*
                         * Create a new contribution record.
                         */
                        ContributionRecordClient contributionRecordClient = watchdogContext.getContributionRecordClient();

                        ContributionRecordProperties contributionRecordProperties = new ContributionRecordProperties();

                        contributionRecordProperties.setQualifiedName(actorProfileProperties.getQualifiedName() + "_contributionRecord");
                        contributionRecordProperties.setDisplayName("Contribution record for " + actorProfileProperties.getDisplayName());
                        contributionRecordProperties.setKarmaPoints(1L); // their reward is added here

                        contributionRecordGUID = contributionRecordClient.addContributionRecordToElement(actorProfile.getElementHeader().getGUID(),
                                                                                                         contributionRecordClient.getMetadataSourceOptions(),
                                                                                                         null,
                                                                                                         contributionRecordProperties,
                                                                                                         null);

                        userToContributionRecordMap.addUserContributionRecordMapping(userId, contributionRecordGUID);
                    }
                }
            }
            else
            {
                /*
                 * A known user so update the contribution record.
                 */
                if (log.isDebugEnabled())
                {
                    log.debug("Updating known contribution record " + contributionRecordGUID + " for user: " + userId);
                }

                this.incrementKarmaPointTotalInContributionRecord(contributionRecordGUID, userId);
            }
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                OrgInsightAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                              error.getClass().getName(),
                                                                                              methodName,
                                                                                              error.getMessage()));
        }
    }


    /**
     * Retrieve the contribution record and increment the karma points in it.
     *
     * @param contributionRecordGUID unique identifier of the contribution record
     * @param userId user to reward
     */
    private synchronized void incrementKarmaPointTotalInContributionRecord(String contributionRecordGUID,
                                                                           String userId)
    {
        final String methodName = "incrementKarmaPointTotalInContributionRecord";
        try
        {
            OpenMetadataStore openMetadataStore = watchdogContext.getOpenMetadataStore();

            /*
             * Use the open metadata store because do not need the full OpenMetadataRootElement.
             */
            OpenMetadataElement contributionRecord = openMetadataStore.getMetadataElementByGUID(contributionRecordGUID);

            if (contributionRecord != null)
            {
                long currentValue = propertyHelper.getLongProperty(watchdogActionServiceName,
                                                                   OpenMetadataProperty.KARMA_POINTS.name,
                                                                   contributionRecord.getElementProperties(),
                                                                   methodName);
                incrementKarmaPointTotalInContributionRecord(contributionRecordGUID, currentValue, userId);
            }
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                OrgInsightAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                              error.getClass().getName(),
                                                                                              methodName,
                                                                                              error.getMessage()));
        }
    }


    /**
     * Make the update to the contribution record.
     *
     * @param contributionRecordGUID unique identifier of the contribution record.
     * @param currentValue current value of the karma points
     * @param userId user to reward
     */
    private synchronized void incrementKarmaPointTotalInContributionRecord(String contributionRecordGUID,
                                                                           long   currentValue,
                                                                           String userId)
    {
        final String methodName = "incrementKarmaPointTotalInContributionRecord";

        try
        {
            if (log.isDebugEnabled())
            {
                log.debug("Incrementing karma points from " + currentValue + " for user: " + userId);
            }

            ContributionRecordClient contributionRecordClient = watchdogContext.getContributionRecordClient();

            ContributionRecordProperties contributionRecordProperties = new ContributionRecordProperties();

            contributionRecordProperties.setKarmaPoints(currentValue+1);

            contributionRecordClient.updateContributionRecord(contributionRecordGUID,
                                                              contributionRecordClient.getUpdateOptions(true),
                                                              contributionRecordProperties);
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                OrgInsightAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                              error.getClass().getName(),
                                                                                              methodName,
                                                                                              error.getMessage()));
        }
    }


    /**
     * Manage the map
     */
    static class UserToContributionRecordMap
    {
        private final Map<String, String> userIdToContributionRecordGUID = new HashMap<>();


        /**
         * Set up a map entry.
         *
         * @param userId userId
         * @param contributionRecordGUID unique identifier for the contribution record.
         */
        public synchronized void addUserContributionRecordMapping(String userId, String contributionRecordGUID)
        {
            userIdToContributionRecordGUID.put(userId, contributionRecordGUID);
        }


        /**
         * Retrieve the unique identifier for the contribution record.
         *
         * @param userId userId to award
         * @return unique identifier of the contribution record.
         */
        public synchronized String getContributionRecordGUID(String userId)
        {
            return userIdToContributionRecordGUID.get(userId);
        }
    }
}
