/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.simon;

import org.odpi.openmetadata.adapters.connectors.simon.controls.SimonConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.simon.ffdc.SimonAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ActorProfileClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ContributionRecordClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementVersions;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContributionRecordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * SimonContributionRecorderConnector recognizes the contributions that people make to the open metadata ecosystem,
 * in recognition of Herbert A. Simon (1916-2001), whose study of bounded rationality revolutionized how we
 * understand human decision-making.  It listens to the open metadata events and awards a karma point to the user
 * responsible for each change - the user that last updated the element, relationship or classification, or the user
 * that created it if it has not been updated.  The total is kept in the karmaPoints property of the ContributionRecord
 * attached to the user's actor profile.  Only users with an actor profile are awarded karma points.
 * <p>
 * The karma points are gathered in memory as the events arrive and written on each refresh, so a busy user costs
 * one read and one update per refresh rather than one per change.  Karma points that could not be written are
 * kept and tried again on the next refresh.  The unique identifier of each user's contribution record is cached
 * so that the actor profile is only looked up the first time.
 * <p>
 * The users whose userIds match one of the patterns in the ignoredUserIdPatterns configuration property are not awarded
 * karma points.  The core content pack sets them to match the non-personal accounts (ending in npa) and the governance
 * engines (ending in engine), so only the contributions of people are recognized.  The patterns are used rather than
 * a list of userIds because the servers' userIds differ in each environment.  The connector's own userId is always
 * ignored, whether a pattern matches it or not - each update it makes to a contribution record produces an event of
 * its own, and otherwise it would award itself a karma point for every one, without end.
 */
public class SimonContributionRecorderConnector extends IntegrationConnectorBase implements OpenMetadataEventListener
{
    /**
     * The karma points awarded since the last refresh, keyed by the userId they are awarded to.  Events arrive on
     * the listener's thread and are written on the refresh thread, so every access is synchronized on the map.
     */
    private final Map<String, Long> pendingKarmaPoints = new HashMap<>();

    /**
     * The unique identifier of each user's contribution record, once it is known.  This is only used on the
     * refresh thread.
     */
    private final Map<String, String> contributionRecordGUIDs = new HashMap<>();

    /**
     * The patterns matching the users that are not awarded karma points.  The list is replaced rather than changed,
     * so the listener's thread always sees a complete list.
     */
    private volatile List<Pattern> ignoredUserIdPatterns = new ArrayList<>();


    /**
     * Indicates that the connector is completely configured and can begin processing.  The listener is registered
     * here rather than after the first refresh, since there is no backlog for a refresh to work through - the
     * contributions are only seen as they happen.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        List<String> configuredPatterns = super.getArrayConfigurationProperty(SimonConfigurationProperty.IGNORED_USER_ID_PATTERNS.getName(),
                                                                              connectionBean.getConfigurationProperties());

        this.setIgnoredUserIdPatterns(configuredPatterns, integrationContext.getMyUserId());

        logRecord(methodName,
                  SimonAuditCode.STARTING_CONNECTOR.getMessageDefinition(connectorName,
                                                                         integrationContext.getMetadataAccessServer(),
                                                                         integrationContext.getMetadataAccessServerPlatformURLRoot(),
                                                                         ignoredUserIdPatterns.toString()));

        try
        {
            integrationContext.registerListener(this);
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               OIFAuditCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(connectorName,
                                                                                             error.getClass().getName(),
                                                                                             error.getMessage()),
                               error);

            throw new ConnectorCheckedException(OIFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName,
                                                                                                       error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Write the karma points awarded since the last refresh to the users' contribution records.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        this.recordPendingKarmaPoints();
    }


    /**
     * Award a karma point to the user responsible for the change reported by an open metadata event.  The refresh
     * events do not report a change - they are sent when an element is distributed around the cohort.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        if ((event == null) ||
                (event.getEventType() == null) ||
                (event.getEventType() == OpenMetadataEventType.REFRESH_ELEMENT_EVENT) ||
                (event.getEventType() == OpenMetadataEventType.UNKNOWN_EVENT))
        {
            return;
        }

        this.awardKarmaPoint(getResponsibleUserId(event.getElementHeader()));
    }


    /**
     * Return the user responsible for the latest change to an element, relationship or classification.  This is
     * the user that last updated it, or the user that created it if it has not been updated.  Adding or removing a
     * classification updates the element it is attached to, so the element's versions name the user that changed
     * the classification.
     *
     * @param elementHeader header from the event
     * @return userId or null if not known
     */
    static String getResponsibleUserId(ElementHeader elementHeader)
    {
        if (elementHeader == null)
        {
            return null;
        }

        ElementVersions versions = elementHeader.getVersions();

        if (versions == null)
        {
            return null;
        }

        if (versions.getUpdatedBy() != null)
        {
            return versions.getUpdatedBy();
        }

        return versions.getCreatedBy();
    }


    /**
     * Award a karma point to a user, unless they are one of the ignored users.
     *
     * @param userId user responsible for a change
     */
    void awardKarmaPoint(String userId)
    {
        if ((userId == null) || (this.isIgnoredUser(userId)))
        {
            return;
        }

        synchronized (pendingKarmaPoints)
        {
            pendingKarmaPoints.merge(userId, 1L, Long::sum);
        }
    }


    /**
     * Set up the patterns matching the users that are not awarded karma points.  This is called from start() with
     * the ignoredUserIdPatterns configuration property.  If no pattern matches the connector's own userId, an exact
     * match for it is added - otherwise each update of a contribution record would earn another karma point,
     * without end.  A pattern that is not a valid regular expression is reported and skipped.
     *
     * @param patterns list of regular expressions - may be null
     * @param connectorUserId the connector's own userId - may be null
     */
    void setIgnoredUserIdPatterns(List<String> patterns,
                                  String       connectorUserId)
    {
        final String methodName = "setIgnoredUserIdPatterns";

        List<Pattern> compiledPatterns = new ArrayList<>();

        if (patterns != null)
        {
            for (String pattern : patterns)
            {
                try
                {
                    compiledPatterns.add(Pattern.compile(pattern));
                }
                catch (PatternSyntaxException error)
                {
                    logRecord(methodName,
                              SimonAuditCode.INVALID_USER_ID_PATTERN.getMessageDefinition(connectorName,
                                                                                          pattern,
                                                                                          error.getMessage()));
                }
            }
        }

        if ((connectorUserId != null) && (! isMatch(compiledPatterns, connectorUserId)))
        {
            compiledPatterns.add(Pattern.compile(Pattern.quote(connectorUserId)));
        }

        ignoredUserIdPatterns = compiledPatterns;
    }


    /**
     * Return whether a user is ignored.
     *
     * @param userId calling user
     * @return true if one of the patterns matches the whole userId
     */
    private boolean isIgnoredUser(String userId)
    {
        return isMatch(ignoredUserIdPatterns, userId);
    }


    /**
     * Return whether any of the patterns matches the whole userId.
     *
     * @param patterns patterns to test
     * @param userId userId to test
     * @return boolean
     */
    private static boolean isMatch(List<Pattern> patterns,
                                   String        userId)
    {
        for (Pattern pattern : patterns)
        {
            if (pattern.matcher(userId).matches())
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Return a copy of the karma points waiting to be written.  This is for testing.
     *
     * @return map of userId to karma points
     */
    Map<String, Long> getPendingKarmaPoints()
    {
        synchronized (pendingKarmaPoints)
        {
            return new HashMap<>(pendingKarmaPoints);
        }
    }


    /**
     * Add the karma points awarded since the last refresh to the users' contribution records.  The karma points
     * are taken out of the pending map first so that the points awarded during the updates are kept for the next
     * refresh rather than lost.
     */
    private void recordPendingKarmaPoints()
    {
        final String methodName = "recordPendingKarmaPoints";

        Map<String, Long> karmaPointsToRecord;

        synchronized (pendingKarmaPoints)
        {
            if (pendingKarmaPoints.isEmpty())
            {
                return;
            }

            karmaPointsToRecord = new HashMap<>(pendingKarmaPoints);
            pendingKarmaPoints.clear();
        }

        long         userCount           = 0;
        long         karmaPointCount     = 0;
        List<String> usersWithoutProfile = new ArrayList<>();
        long         unrecordedPoints    = 0;

        for (String userId : karmaPointsToRecord.keySet())
        {
            long karmaPoints = karmaPointsToRecord.get(userId);

            try
            {
                if (this.recordKarmaPoints(userId, karmaPoints))
                {
                    userCount       = userCount + 1;
                    karmaPointCount = karmaPointCount + karmaPoints;
                }
                else
                {
                    usersWithoutProfile.add(userId);
                    unrecordedPoints = unrecordedPoints + karmaPoints;
                }
            }
            catch (InvalidParameterException | UserNotAuthorizedException error)
            {
                /*
                 * The connector's user is not permitted to read the profile or change the contribution record (for
                 * example, it is in a governance zone that restricts updates).  Trying again will not help, so the
                 * karma points are discarded rather than retried on every refresh.
                 */
                logRecord(methodName,
                          SimonAuditCode.KARMA_POINTS_DISCARDED.getMessageDefinition(connectorName,
                                                                                     Long.toString(karmaPoints),
                                                                                     userId,
                                                                                     error.getClass().getName(),
                                                                                     error.getMessage()));
            }
            catch (Exception error)
            {
                /*
                 * Most likely the metadata access server is unavailable.  The karma points are put back so that
                 * they are added on the next refresh.
                 */
                synchronized (pendingKarmaPoints)
                {
                    pendingKarmaPoints.merge(userId, karmaPoints, Long::sum);
                }

                logRecord(methodName,
                          SimonAuditCode.KARMA_POINTS_DEFERRED.getMessageDefinition(connectorName,
                                                                                    userId,
                                                                                    error.getClass().getName(),
                                                                                    error.getMessage(),
                                                                                    Long.toString(karmaPoints)));
            }
        }

        if (! usersWithoutProfile.isEmpty())
        {
            logRecord(methodName,
                      SimonAuditCode.NO_ACTOR_PROFILE.getMessageDefinition(connectorName,
                                                                           Long.toString(unrecordedPoints),
                                                                           usersWithoutProfile.toString()));
        }

        if (userCount > 0)
        {
            logRecord(methodName,
                      SimonAuditCode.KARMA_POINTS_RECORDED.getMessageDefinition(connectorName,
                                                                                Long.toString(karmaPointCount),
                                                                                Long.toString(userCount)));
        }
    }


    /**
     * Add karma points to a user's contribution record, creating the contribution record if the user's actor
     * profile does not have one yet.  The contribution record is read immediately before it is written, so the
     * karma points are added to the value that is actually stored.
     *
     * @param userId user to reward
     * @param karmaPoints karma points to add
     * @return false if the user has no actor profile
     * @throws Exception problem reading or updating the contribution record
     */
    private boolean recordKarmaPoints(String userId,
                                      long   karmaPoints) throws Exception
    {
        final String methodName = "recordKarmaPoints";

        String contributionRecordGUID = contributionRecordGUIDs.get(userId);

        if (contributionRecordGUID != null)
        {
            OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

            /*
             * Only the contribution record itself is needed - not the elements attached to it.
             */
            GetOptions getOptions = new GetOptions(openMetadataStore.getGetOptions());
            getOptions.setGraphQueryDepth(0);

            OpenMetadataElement contributionRecord = openMetadataStore.getMetadataElementByGUID(contributionRecordGUID, getOptions);

            if (contributionRecord != null)
            {
                long storedKarmaPoints = propertyHelper.getLongProperty(connectorName,
                                                                        OpenMetadataProperty.KARMA_POINTS.name,
                                                                        contributionRecord.getElementProperties(),
                                                                        methodName);

                this.updateContributionRecord(contributionRecordGUID, storedKarmaPoints + karmaPoints);

                return true;
            }

            /*
             * The contribution record has been deleted, so the actor profile is looked up again.
             */
            contributionRecordGUIDs.remove(userId);
        }

        ActorProfileClient actorProfileClient = integrationContext.getActorProfileClient();

        /*
         * Only the profile's properties and its contribution record are used.
         */
        GetOptions getOptions = actorProfileClient.getGetOptions();

        getOptions.setGraphQueryDepth(1);
        getOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.CONTRIBUTION_RELATIONSHIP.typeName));

        OpenMetadataRootElement actorProfile = actorProfileClient.getActorProfileByUserId(userId, getOptions);

        if (actorProfile == null)
        {
            return false;
        }

        RelatedMetadataElementSummary existingContributionRecord = actorProfile.getContributionRecord();

        if (existingContributionRecord != null)
        {
            contributionRecordGUID = existingContributionRecord.getRelatedElement().getElementHeader().getGUID();

            long storedKarmaPoints = 0L;

            if (existingContributionRecord.getRelatedElement().getProperties() instanceof ContributionRecordProperties contributionRecordProperties)
            {
                storedKarmaPoints = OpenMetadataProperty.KARMA_POINTS.longValueOrDefault(contributionRecordProperties.getKarmaPoints());
            }

            this.updateContributionRecord(contributionRecordGUID, storedKarmaPoints + karmaPoints);
        }
        else
        {
            ContributionRecordClient     contributionRecordClient     = integrationContext.getContributionRecordClient();
            ContributionRecordProperties contributionRecordProperties = new ContributionRecordProperties();

            String profileQualifiedName = actorProfile.getElementHeader().getGUID();
            String profileDisplayName   = userId;

            if (actorProfile.getProperties() instanceof ActorProfileProperties actorProfileProperties)
            {
                profileQualifiedName = actorProfileProperties.getQualifiedName();

                if (actorProfileProperties.getDisplayName() != null)
                {
                    profileDisplayName = actorProfileProperties.getDisplayName();
                }
            }

            contributionRecordProperties.setQualifiedName(profileQualifiedName + "_contributionRecord");
            contributionRecordProperties.setDisplayName("Contribution record for " + profileDisplayName);
            contributionRecordProperties.setKarmaPoints(karmaPoints);

            contributionRecordGUID = contributionRecordClient.addContributionRecordToElement(actorProfile.getElementHeader().getGUID(),
                                                                                             contributionRecordClient.getMetadataSourceOptions(),
                                                                                             null,
                                                                                             contributionRecordProperties,
                                                                                             null);
        }

        contributionRecordGUIDs.put(userId, contributionRecordGUID);

        return true;
    }


    /**
     * Set the total karma points in a contribution record.
     *
     * @param contributionRecordGUID unique identifier of the contribution record
     * @param karmaPoints new total
     * @throws Exception problem updating the contribution record
     */
    private void updateContributionRecord(String contributionRecordGUID,
                                          long   karmaPoints) throws Exception
    {
        ContributionRecordClient     contributionRecordClient     = integrationContext.getContributionRecordClient();
        ContributionRecordProperties contributionRecordProperties = new ContributionRecordProperties();

        contributionRecordProperties.setKarmaPoints(karmaPoints);

        contributionRecordClient.updateContributionRecord(contributionRecordGUID,
                                                          contributionRecordClient.getUpdateOptions(true),
                                                          contributionRecordProperties);
    }


    /**
     * Write any karma points that have not been recorded yet before the connector shuts down.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        try
        {
            if (integrationContext != null)
            {
                this.recordPendingKarmaPoints();
            }
        }
        catch (Exception error)
        {
            logRecord(methodName,
                      SimonAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                               error.getClass().getName(),
                                                                               methodName,
                                                                               error.getMessage()));
        }

        super.disconnect();
    }
}
