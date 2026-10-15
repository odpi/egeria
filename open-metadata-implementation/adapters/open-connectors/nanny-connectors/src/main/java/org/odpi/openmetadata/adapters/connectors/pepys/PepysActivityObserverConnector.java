/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.pepys;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adapters.connectors.pepys.ffdc.PepysAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecord;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.UsageActivityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * PepysActivityObserverConnector keeps a diary of the activity around the anchor elements (assets, digital products
 * and glossary terms) in the open metadata ecosystem, in the manner of Samuel Pepys.  Its catalog targets are the
 * topics that the OMAG Servers write their audit logs to (registered by the OMAG Server Platform Cataloguer).  It
 * listens to each of them for the anchor element activity messages (METADATA-OBSERVABILITY-0002 to -0009 and -0011)
 * and maintains the separate counts in the UsageActivity classification of the anchor element named in each message.
 * <p>
 * The activity is gathered in memory as the records arrive and written on each refresh, so an element that is busy
 * costs one read and one update per refresh rather than one per record.  Counts that could not be written are
 * kept and tried again on the next refresh.
 * <p>
 * The connector's own reads and updates of the anchor elements are reported in the audit log too, and they are ignored -
 * otherwise each update of a UsageActivity classification would be counted as more activity, without end.
 */
public class PepysActivityObserverConnector extends DynamicIntegrationConnectorBase implements OpenMetadataTopicListener
{
    private static final Logger log = LoggerFactory.getLogger(PepysActivityObserverConnector.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /*
     * The position of the calling user and the anchor element's unique identifier in the activity messages.
     */
    private static final int userIdParameter    = 0;
    private static final int anchorGUIDParameter = 2;

    /**
     * The activity observed since the last refresh, keyed by the unique identifier of the element it is for.
     * Records arrive on the topic connectors' threads and are written on the refresh thread, so every access is
     * synchronized on the map.
     */
    private final Map<String, PendingUsageActivity> pendingActivity = new HashMap<>();

    /**
     * The user that this connector calls the metadata access server with.  Its activity is not counted.
     */
    private String ignoredUserId = null;


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        ignoredUserId = integrationContext.getMyUserId();

        logRecord(methodName,
                  PepysAuditCode.STARTING_CONNECTOR.getMessageDefinition(connectorName,
                                                                         integrationContext.getMetadataAccessServer(),
                                                                         integrationContext.getMetadataAccessServerPlatformURLRoot(),
                                                                         ignoredUserId));
    }


    /**
     * Refresh the catalog targets (which picks up any new audit log topics) and then write the activity observed
     * since the last refresh to the UsageActivity classifications.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.refresh();

        this.recordPendingActivity();
    }


    /**
     * Create a new catalog target processor for each audit log topic.
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @return new processor based on the catalog target information
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget        retrievedCatalogTarget,
                                                                       CatalogTargetContext catalogTargetContext,
                                                                       Connector            connectorToTarget) throws ConnectorCheckedException,
                                                                                                                      UserNotAuthorizedException
    {
        if (propertyHelper.isTypeOf(retrievedCatalogTarget.getCatalogTargetElement().getElementHeader(), OpenMetadataType.TOPIC.typeName))
        {
            return new PepysTopicCatalogTargetProcessor(retrievedCatalogTarget,
                                                        catalogTargetContext,
                                                        connectorToTarget,
                                                        connectorName,
                                                        auditLog,
                                                        this);
        }

        return new RequestedCatalogTarget(retrievedCatalogTarget, catalogTargetContext, connectorToTarget);
    }


    /**
     * Receive an audit log record from one of the topics.  Records that are not counted anchor element activity messages are
     * ignored, as are those that are not well-formed audit log records.
     *
     * @param event inbound event
     */
    @Override
    public void processEvent(String event)
    {
        final String methodName = "processEvent";

        try
        {
            this.observe(objectMapper.readValue(event, AuditLogRecord.class));
        }
        catch (Exception error)
        {
            /*
             * Anything can be written to a topic.  An event that is not an audit log record is not this
             * connector's business, so it is noted at debug rather than raised as an error.
             */
            log.debug("{} ignored an event in {} that is not an audit log record: {}", connectorName, methodName, error.getMessage());
        }
    }


    /**
     * Count the activity reported by an audit log record, if it is a counted anchor element activity message from a user
     * other than this connector.
     *
     * @param auditLogRecord record from the audit log
     */
    void observe(AuditLogRecord auditLogRecord)
    {
        if (auditLogRecord == null)
        {
            return;
        }

        ObservedActivity activity = ObservedActivity.getActivity(auditLogRecord.getMessageId());
        String[]         parameters = auditLogRecord.getMessageParameters();

        if ((activity == null) || (parameters == null) || (parameters.length <= anchorGUIDParameter))
        {
            return;
        }

        String userId    = parameters[userIdParameter];
        String anchorGUID = parameters[anchorGUIDParameter];

        if ((anchorGUID == null) || ((ignoredUserId != null) && (ignoredUserId.equals(userId))))
        {
            return;
        }

        synchronized (pendingActivity)
        {
            pendingActivity.computeIfAbsent(anchorGUID, guid -> new PendingUsageActivity()).increment(activity.getUsageCount());
        }
    }


    /**
     * Set up the user whose activity is ignored.  This is normally the connector's own user, set in start().
     * This is for testing.
     *
     * @param ignoredUserId userId
     */
    void setIgnoredUserId(String ignoredUserId)
    {
        this.ignoredUserId = ignoredUserId;
    }


    /**
     * Return a copy of the activity waiting to be written.  This is for testing.
     *
     * @return map of element GUID to pending counts
     */
    Map<String, PendingUsageActivity> getPendingActivity()
    {
        synchronized (pendingActivity)
        {
            return new HashMap<>(pendingActivity);
        }
    }


    /**
     * Add the activity observed since the last refresh to the UsageActivity classifications of the elements it
     * is for.  The activity is taken out of the pending map first so that records arriving during the updates
     * are kept for the next refresh rather than lost.
     */
    private void recordPendingActivity()
    {
        final String methodName = "recordPendingActivity";

        Map<String, PendingUsageActivity> activityToRecord;

        synchronized (pendingActivity)
        {
            if (pendingActivity.isEmpty())
            {
                return;
            }

            activityToRecord = new HashMap<>(pendingActivity);
            pendingActivity.clear();
        }

        long elementCount  = 0;
        long activityCount = 0;

        for (String elementGUID : activityToRecord.keySet())
        {
            PendingUsageActivity activity = activityToRecord.get(elementGUID);

            try
            {
                if (this.recordActivity(elementGUID, activity))
                {
                    elementCount  = elementCount + 1;
                    activityCount = activityCount + activity.getTotal();
                }
                else
                {
                    logRecord(methodName,
                              PepysAuditCode.USAGE_ACTIVITY_DISCARDED.getMessageDefinition(connectorName,
                                                                                           Long.toString(activity.getTotal()),
                                                                                           elementGUID,
                                                                                           "null",
                                                                                           "element not found"));
                }
            }
            catch (InvalidParameterException | UserNotAuthorizedException error)
            {
                /*
                 * The element has gone, it can not be classified, or this connector's user is not permitted to
                 * classify it (for example, it is in a governance zone that restricts classification).  Trying
                 * again will not help, so the counts are discarded rather than retried on every refresh.
                 */
                logRecord(methodName,
                          PepysAuditCode.USAGE_ACTIVITY_DISCARDED.getMessageDefinition(connectorName,
                                                                                       Long.toString(activity.getTotal()),
                                                                                       elementGUID,
                                                                                       error.getClass().getName(),
                                                                                       error.getMessage()));
            }
            catch (Exception error)
            {
                /*
                 * Most likely the metadata access server is unavailable.  The counts are put back so that they
                 * are added on the next refresh.
                 */
                synchronized (pendingActivity)
                {
                    pendingActivity.computeIfAbsent(elementGUID, guid -> new PendingUsageActivity()).add(activity);
                }

                logRecord(methodName,
                          PepysAuditCode.USAGE_ACTIVITY_DEFERRED.getMessageDefinition(connectorName,
                                                                                      elementGUID,
                                                                                      error.getClass().getName(),
                                                                                      error.getMessage(),
                                                                                      Long.toString(activity.getTotal())));
            }
        }

        if (elementCount > 0)
        {
            logRecord(methodName,
                      PepysAuditCode.USAGE_ACTIVITY_RECORDED.getMessageDefinition(connectorName,
                                                                                  Long.toString(activityCount),
                                                                                  Long.toString(elementCount)));
        }
    }


    /**
     * Add the pending activity to an element's UsageActivity classification, creating the classification if the
     * element does not have one yet.  The element is read immediately before it is written, so the counts are
     * added to the values that are actually stored.
     *
     * @param elementGUID unique identifier of the element
     * @param activity activity to add
     * @return false if the element could not be found
     * @throws Exception problem reading or updating the element
     */
    private boolean recordActivity(String               elementGUID,
                                   PendingUsageActivity activity) throws Exception
    {
        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        /*
         * Only the element itself is needed - not the elements attached to it.
         */
        GetOptions getOptions = new GetOptions(openMetadataStore.getGetOptions());
        getOptions.setGraphQueryDepth(0);

        OpenMetadataElement element = openMetadataStore.getMetadataElementByGUID(elementGUID, getOptions);

        if (element == null)
        {
            return false;
        }

        ElementHeader elementHeader = propertyHelper.getElementHeader(element);

        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

        /*
         * An element that is not classified yet starts from zero for every count.
         */
        UsageActivityProperties storedProperties = new UsageActivityProperties();
        boolean                 isClassified     = false;

        if ((elementHeader.getUsageActivity() != null) &&
                (elementHeader.getUsageActivity().getClassificationProperties() instanceof UsageActivityProperties existingProperties))
        {
            storedProperties = existingProperties;
            isClassified     = true;
        }

        UsageActivityProperties usageActivityProperties = new UsageActivityProperties();

        usageActivityProperties.setExplicitGetCount(storedProperties.getExplicitGetCount() + activity.getCount(ObservedActivity.UsageCount.EXPLICIT_GET));
        usageActivityProperties.setFindCount(storedProperties.getFindCount() + activity.getCount(ObservedActivity.UsageCount.FIND));
        usageActivityProperties.setUpdatePropertyCount(storedProperties.getUpdatePropertyCount() + activity.getCount(ObservedActivity.UsageCount.UPDATE_PROPERTY));
        usageActivityProperties.setUpdateAttachmentCount(storedProperties.getUpdateAttachmentCount() + activity.getCount(ObservedActivity.UsageCount.UPDATE_ATTACHMENT));
        usageActivityProperties.setUpdateFeedbackCount(storedProperties.getUpdateFeedbackCount() + activity.getCount(ObservedActivity.UsageCount.UPDATE_FEEDBACK));
        usageActivityProperties.setDeleteAttachmentCount(storedProperties.getDeleteAttachmentCount() + activity.getCount(ObservedActivity.UsageCount.DELETE_ATTACHMENT));
        usageActivityProperties.setDeleteFeedbackCount(storedProperties.getDeleteFeedbackCount() + activity.getCount(ObservedActivity.UsageCount.DELETE_FEEDBACK));

        if (isClassified)
        {
            classificationExplorerClient.updateUsageActivityClassification(elementGUID,
                                                                           usageActivityProperties,
                                                                           classificationExplorerClient.getUpdateOptions(false));
        }
        else
        {
            classificationExplorerClient.addUsageActivityClassification(elementGUID,
                                                                        usageActivityProperties,
                                                                        classificationExplorerClient.getMetadataSourceOptions());
        }

        return true;
    }


    /**
     * Write any activity that has not been recorded yet before the connector shuts down.
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
                this.recordPendingActivity();
            }
        }
        catch (Exception error)
        {
            logRecord(methodName,
                      PepysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                               error.getClass().getName(),
                                                                               methodName,
                                                                               error.getMessage()));
        }

        super.disconnect();
    }
}
