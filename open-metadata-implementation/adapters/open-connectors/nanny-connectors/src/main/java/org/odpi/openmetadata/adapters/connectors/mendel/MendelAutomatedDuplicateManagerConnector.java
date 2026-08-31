/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.mendel;

import org.odpi.openmetadata.adapters.connectors.mendel.controls.MendelConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.mendel.ffdc.MendelAuditCode;
import org.odpi.openmetadata.adapters.connectors.mendel.ffdc.MendelErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ActorRoleClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ToDoProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ActionType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.StatusIdentifier;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MendelAutomatedDuplicateManagerConnector manages the duplicate links and classifications that drive the
 * deduplication of open metadata elements on retrieval.  Each refresh makes four passes over the
 * PeerDuplicateLink relationships in the open metadata ecosystem.
 * <ul>
 *     <li>The links that no steward has ruled on yet - the DISCOVERED, PROPOSED and IMPORTED ones.  Where the
 *     linked elements are a close enough match, the link is moved to VALIDATED and the KnownDuplicate
 *     classification is added to both elements, which is the combination that causes the retrieval processing to
 *     combine them.  The rest are passed to a person appointed to the DuplicateMetadataSteward role via a to do.</li>
 *     <li>The validated links that this connector decided itself, told apart from a steward's by the userId in
 *     their updatedBy.  A close match can stop being one - a qualified name is corrected, say - and nothing else
 *     revisits a validated link, so a link whose grounds have gone is retired.  A steward's decision is never
 *     reconsidered.</li>
 *     <li>The links that have been retired - the DEPRECATED and OBSOLETE ones.  The KnownDuplicate
 *     classification is removed from an element once it is no longer deduplicated by any route - no live peer
 *     link and no consolidated cluster to be reached through - so that the element stops being combined with
 *     anything.</li>
 *     <li>The clusters of validated peer duplicates.  Once a cluster reaches the configured size, its members are
 *     consolidated into a single element using the survivorship rules in
 *     {@link MendelDuplicateConsolidator}.</li>
 * </ul>
 * Each pass works from one snapshot of the duplicate links, so the links that this refresh validates are
 * consolidated by the next refresh rather than this one.
 * <p>
 * Once the first refresh has worked through the backlog, the connector also listens for open metadata events so
 * that a new or updated duplicate link is reviewed as soon as it appears, rather than waiting for the next refresh.
 * The update that this connector makes to a duplicate link produces another event for the same relationship, which
 * is ignored because the status is VALIDATED by then, so there is no loop.
 * <p>
 * This connector works across the whole open metadata ecosystem rather than through catalog targets, which is why
 * it extends IntegrationConnectorBase rather than DynamicIntegrationConnectorBase.  It registers its own listener
 * as a result, and - like the dynamic base class - it ignores events while a refresh is in progress so that a
 * duplicate link is not worked on by both threads at once.
 */
public class MendelAutomatedDuplicateManagerConnector extends IntegrationConnectorBase implements OpenMetadataEventListener
{
    /**
     * The role that receives the to dos for the duplicates that this connector can not resolve on its own.
     */
    /*
     * Recorded in the notes of every link this connector validates itself, so that the grounds for the decision
     * are on the link rather than only in this code.
     */
    private static final String closeMatchExplanation =
            "Validated automatically: the two elements are of exactly the same type, that type is a Referenceable, " +
                    "and they have the same qualifiedName - so they describe the same thing.  A pairing that does not " +
                    "meet all three tests is referred to a steward instead.";

    /*
     * Recorded in the notes of every link this connector withdraws, replacing the grounds it recorded when it
     * validated the link.
     */
    private static final String withdrawnExplanation =
            "Withdrawn automatically: this connector validated the link because the two elements were of exactly " +
                    "the same type, that type is a Referenceable, and they had the same qualifiedName.  That is no " +
                    "longer true, so the grounds for the decision have gone.  A steward who believes the elements are " +
                    "duplicates can validate the link again; this connector reconsiders only its own decisions.";

    private static final String stewardRoleName          = "DuplicateMetadataSteward";
    private static final String stewardRoleQualifiedName = OpenMetadataType.PERSON_ROLE.typeName + "::" + stewardRoleName;
    private static final String stewardRoleDescription   = "Decides whether elements that have been detected as potential duplicates " +
                                                                   "represent the same thing, and resolves the ones that do.";

    /**
     * These are the statuses that mean a steward has not yet ruled on the duplicate link.
     */
    private static final List<Integer> undecidedStatuses = List.of(StatusIdentifier.DISCOVERED.getOrdinal(),
                                                                   StatusIdentifier.PROPOSED.getOrdinal(),
                                                                   StatusIdentifier.IMPORTED.getOrdinal());

    /**
     * These are the statuses that mean a steward has retired the duplicate link.  The elements it connects are no
     * longer combined because of it.
     */
    private static final List<Integer> retiredStatuses = List.of(StatusIdentifier.DEPRECATED.getOrdinal(),
                                                                 StatusIdentifier.OBSOLETE.getOrdinal());

    /**
     * The duplicate links that have already been passed to a steward.  This stops a second to do being raised on
     * each refresh.  It is rebuilt if this connector restarts - the to do's qualified name is derived from the
     * relationship's unique identifier so that the repeats are recognizable.  It is thread-safe because the
     * refresh thread and the event listener thread both add to it.
     */
    private final Set<String> toDosRaised = ConcurrentHashMap.newKeySet();

    private int    clusterSize     = MendelConfigurationProperty.DEFAULT_DUPLICATE_CLUSTER_SIZE;
    private String stewardRoleGUID = null;

    private static final Logger log = LoggerFactory.getLogger(MendelAutomatedDuplicateManagerConnector.class);


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected during start up
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        int configuredClusterSize = super.getIntConfigurationProperty(MendelConfigurationProperty.DUPLICATE_CLUSTER_SIZE.getName(),
                                                                      connectionBean.getConfigurationProperties());

        /*
         * A cluster of one is a single element, and a cluster of zero is the value returned when the property is
         * not configured, so both fall back to the default.
         */
        if (configuredClusterSize > 1)
        {
            clusterSize = configuredClusterSize;
        }

        auditLog.logMessage(methodName,
                            MendelAuditCode.STARTING_CONNECTOR.getMessageDefinition(connectorName,
                                                                                    integrationContext.getMetadataAccessServer(),
                                                                                    integrationContext.getMetadataAccessServerPlatformURLRoot(),
                                                                                    Integer.toString(clusterSize)));
    }


    /**
     * Review the duplicate links in the open metadata ecosystem.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is unable to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected so stop refresh processing
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        try
        {
            List<OpenMetadataRelationship> duplicateLinks = this.getDuplicateLinks();

            if (! duplicateLinks.isEmpty())
            {
                this.reviewUndecidedLinks(duplicateLinks);
                this.reconsiderOwnValidations(duplicateLinks);
                this.removeRetiredClassifications(duplicateLinks);
                this.consolidateValidatedClusters(duplicateLinks);
            }

            /*
             * The listener is registered once the first refresh has worked through the backlog of duplicate links,
             * so that the events for the links this connector has just processed are not delivered as well.
             */
            this.registerListener();
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  MendelAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                            error.getClass().getName(),
                                                                                            methodName,
                                                                                            error.getMessage()),
                                  error);

            throw new ConnectorCheckedException(MendelErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Retrieve all of the peer duplicate links in the open metadata ecosystem.
     *
     * @return list of relationships - empty if there are none
     *
     * @throws Exception the retrieval failed - reported by the caller
     */
    private List<OpenMetadataRelationship> getDuplicateLinks() throws Exception
    {
        List<OpenMetadataRelationship> duplicateLinks    = new ArrayList<>();
        OpenMetadataStore              openMetadataStore = integrationContext.getOpenMetadataStore();

        int startFrom = 0;

        /*
         * Every read this connector makes asks for the duplicates as they are, not as the retrieval
         * processing would combine them.  A connector that manages duplicates has to see them: reading a
         * combined view means seeing one element where there are three, and the relationships of all three
         * merged onto it, which is neither what is stored nor what needs changing.
         */
        QueryOptions queryOptions = this.getRawQueryOptions(startFrom);

        OpenMetadataRelationshipList retrievedLinks = openMetadataStore.findRelationshipsBetweenMetadataElements(OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
                                                                                                                 null,
                                                                                                                 null,
                                                                                                                 null,
                                                                                                                 null,
                                                                                                                 null,
                                                                                                                 queryOptions);

        while ((retrievedLinks != null) && (retrievedLinks.getRelationships() != null) && (! retrievedLinks.getRelationships().isEmpty()))
        {
            duplicateLinks.addAll(retrievedLinks.getRelationships());

            startFrom = startFrom + integrationContext.getMaxPageSize();

            queryOptions = this.getRawQueryOptions(startFrom);

            retrievedLinks = openMetadataStore.findRelationshipsBetweenMetadataElements(OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        queryOptions);
        }

        return duplicateLinks;
    }


    /**
     * Register this connector to receive the open metadata events for new and updated duplicate links.  Nothing
     * happens if a listener is already registered.
     */
    private void registerListener()
    {
        final String methodName = "registerListener";

        if (integrationContext.noListenerRegistered())
        {
            try
            {
                integrationContext.registerListener(this);

                auditLog.logMessage(methodName,
                                    MendelAuditCode.LISTENER_REGISTERED.getMessageDefinition(connectorName));
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      MendelAuditCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(connectorName,
                                                                                                        error.getClass().getName(),
                                                                                                        error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Process an event that was published by the Open Metadata Framework Manager.  Only the creation and update of
     * a duplicate link is of interest: a deleted link means the duplicates have been separated again, which needs
     * no action, and the retirement of a link and the consolidation of a cluster are handled by the refresh
     * processing because they depend on the other duplicate links attached to the same elements.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        final String methodName = "processEvent";

        if ((event == null) || (event.getElementHeader() == null) || (event.getElementHeader().getType() == null))
        {
            return;
        }

        /*
         * A refresh works from its own snapshot of the duplicate links and makes updates that generate events of
         * their own.  Processing events at the same time would mean the same duplicate link is worked on twice, so
         * the events are left for the refresh to pick up.
         */
        if (! integrationContext.noRefreshInProgress())
        {
            return;
        }

        if ((event.getEventType() != OpenMetadataEventType.NEW_ELEMENT_CREATED) &&
                (event.getEventType() != OpenMetadataEventType.ELEMENT_UPDATED))
        {
            return;
        }

        if (! OpenMetadataType.PEER_DUPLICATE_LINK.typeName.equals(event.getElementHeader().getType().getTypeName()))
        {
            return;
        }

        ElementHeader endOne = event.getEndOneElementHeader();
        ElementHeader endTwo = event.getEndTwoElementHeader();

        if ((endOne == null) || (endTwo == null))
        {
            return;
        }

        try
        {
            int statusIdentifier = propertyHelper.getIntProperty(connectorName,
                                                                 OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                 event.getElementProperties(),
                                                                 methodName);

            if (! undecidedStatuses.contains(statusIdentifier))
            {
                /*
                 * A steward (or a previous pass of this connector) has already ruled on this link.  This is also
                 * the event that results from this connector's own update, which is why there is no loop.
                 */
                if (log.isDebugEnabled())
                {
                    log.debug("Ignoring duplicate link " + event.getElementHeader().getGUID() + " with status " + statusIdentifier);
                }

                return;
            }

            String endOneQualifiedName = propertyHelper.getStringProperty(connectorName,
                                                                          OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                          event.getEndOneElementProperties(),
                                                                          methodName);

            String endTwoQualifiedName = propertyHelper.getStringProperty(connectorName,
                                                                          OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                          event.getEndTwoElementProperties(),
                                                                          methodName);

            if (this.isCloseMatch(endOne, endOneQualifiedName, endTwo, endTwoQualifiedName))
            {
                this.validateDuplicates(event.getElementHeader().getGUID(),
                                        event.getElementHeader(),
                                        endOne.getGUID(),
                                        endOne,
                                        endOne.getKnownDuplicate() != null,
                                        endTwo.getGUID(),
                                        endTwo,
                                        endTwo.getKnownDuplicate() != null);
            }
            else
            {
                this.requestStewardDecision(event.getElementHeader().getGUID(), endOne.getGUID(), endTwo.getGUID());
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  MendelAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                            error.getClass().getName(),
                                                                                            methodName,
                                                                                            error.getMessage()),
                                  error);
        }
    }


    /**
     * Return query options that ask for the duplicates as they are stored, rather than combined.
     *
     * @param startFrom where to start the page
     * @return query options
     */
    private QueryOptions getRawQueryOptions(int startFrom)
    {
        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setForDuplicateProcessing(true);
        queryOptions.setStartFrom(startFrom);
        queryOptions.setPageSize(integrationContext.getMaxPageSize());

        return queryOptions;
    }


    /* ==============================================================================
     * Pass one - the duplicate links that no steward has ruled on yet.
     */


    /**
     * Work through the duplicate links that are still waiting for a decision.  The close matches are validated;
     * the rest are passed to a steward.
     *
     * @param duplicateLinks all of the duplicate links in the open metadata ecosystem
     */
    private void reviewUndecidedLinks(List<OpenMetadataRelationship> duplicateLinks)
    {
        final String methodName = "reviewUndecidedLinks";

        for (OpenMetadataRelationship duplicateLink : duplicateLinks)
        {
            if (undecidedStatuses.contains(this.getStatusIdentifier(duplicateLink)))
            {
                OpenMetadataElementStub endOne = duplicateLink.getElementAtEnd1();
                OpenMetadataElementStub endTwo = duplicateLink.getElementAtEnd2();

                if ((endOne == null) || (endTwo == null))
                {
                    continue;
                }

                try
                {
                    if (this.isCloseMatch(endOne, endOne.getUniqueName(), endTwo, endTwo.getUniqueName()))
                    {
                        this.validateDuplicates(duplicateLink.getRelationshipGUID(),
                                                duplicateLink,
                                                endOne.getGUID(),
                                                endOne,
                                                this.isKnownDuplicate(endOne),
                                                endTwo.getGUID(),
                                                endTwo,
                                                this.isKnownDuplicate(endTwo));
                    }
                    else
                    {
                        this.requestStewardDecision(duplicateLink.getRelationshipGUID(), endOne.getGUID(), endTwo.getGUID());
                    }
                }
                catch (Exception error)
                {
                    /*
                     * One duplicate link that can not be processed must not stop the rest being reviewed.
                     */
                    auditLog.logException(methodName,
                                          MendelAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                    error.getClass().getName(),
                                                                                                    methodName,
                                                                                                    error.getMessage()),
                                          error);
                }
            }
        }
    }


    /**
     * Determine whether the two linked elements are a close enough match for this connector to confirm that they
     * are duplicates without a steward's involvement.
     * <p>
     * The elements are a close match when they are of exactly the same type, that type is a Referenceable, and they
     * have the same qualified name.  A qualified name is intended to be unique for a type of element, so two
     * Referenceables of the same type that share one are describing the same thing.  Any other combination - a
     * different type at each end, a match on some other property, or a subtype relationship between the two types -
     * is a judgement call that belongs to a steward.
     *
     * @param endOne element at end one of the duplicate link
     * @param endOneQualifiedName qualified name of the element at end one
     * @param endTwo element at end two of the duplicate link
     * @param endTwoQualifiedName qualified name of the element at end two
     * @return flag - true means the elements can be combined automatically
     */
    private boolean isCloseMatch(ElementControlHeader endOne,
                                 String               endOneQualifiedName,
                                 ElementControlHeader endTwo,
                                 String               endTwoQualifiedName)
    {
        if ((endOne.getType() == null) || (endTwo.getType() == null))
        {
            return false;
        }

        /*
         * The elements must be of exactly the same type ...
         */
        if (! endOne.getType().getTypeName().equals(endTwo.getType().getTypeName()))
        {
            return false;
        }

        /*
         * ... and that type must be a Referenceable, since only a Referenceable has a qualified name.
         */
        if (! propertyHelper.isTypeOf(endOne, OpenMetadataType.REFERENCEABLE.typeName))
        {
            return false;
        }

        return (endOneQualifiedName != null) && (endOneQualifiedName.equals(endTwoQualifiedName));
    }


    /**
     * Confirm that the two linked elements are duplicates.  The status of the duplicate link is moved to VALIDATED
     * and the KnownDuplicate classification is added to both elements.  Both are needed before the retrieval
     * processing combines the elements.
     *
     * @param duplicateLinkGUID unique identifier of the duplicate link
     * @param endOneGUID unique identifier of the element at end one of the duplicate link
     * @param endOneKnownDuplicate is the element at end one already classified?
     * @param endTwoGUID unique identifier of the element at end two of the duplicate link
     * @param endTwoKnownDuplicate is the element at end two already classified?
     *
     * @throws Exception the update failed - reported by the caller
     */
    private void validateDuplicates(String               duplicateLinkGUID,
                                    ElementControlHeader duplicateLinkHeader,
                                    String               endOneGUID,
                                    ElementControlHeader endOneHeader,
                                    boolean              endOneKnownDuplicate,
                                    String               endTwoGUID,
                                    ElementControlHeader endTwoHeader,
                                    boolean              endTwoKnownDuplicate) throws Exception
    {
        final String methodName = "validateDuplicates";

        OpenMetadataStore            openMetadataStore            = integrationContext.getOpenMetadataStore();
        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

        /*
         * The status is what the retrieval processing acts on.  The rest is for whoever reads the link later:
         * a steward looking at a pair of combined elements can see that the decision was this connector's
         * rather than a person's, and on what grounds - which is the difference between a decision that can be
         * revisited mechanically and one that cannot.  The steward is recorded as this connector's own userId,
         * which is also what lands in the link's updatedBy.
         */
        ElementProperties properties = propertyHelper.addIntProperty(null,
                                                                     OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                     StatusIdentifier.VALIDATED.getOrdinal());

        properties = propertyHelper.addStringProperty(properties,
                                                       OpenMetadataProperty.STEWARD.name,
                                                       integrationContext.getMyUserId());

        properties = propertyHelper.addStringProperty(properties,
                                                       OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                       OpenMetadataType.USER_IDENTITY.typeName);

        properties = propertyHelper.addStringProperty(properties,
                                                       OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                       OpenMetadataProperty.USER_ID.name);

        properties = propertyHelper.addStringProperty(properties,
                                                       OpenMetadataProperty.SOURCE.name,
                                                       connectorName);

        properties = propertyHelper.addStringProperty(properties,
                                                       OpenMetadataProperty.NOTES.name,
                                                       closeMatchExplanation);

        UpdateOptions updateOptions = openMetadataStore.getUpdateOptions(true);

        updateOptions.setForDuplicateProcessing(true);

        this.setOwningMetadataCollection(updateOptions, duplicateLinkHeader);

        openMetadataStore.updateRelatedElementsInStore(duplicateLinkGUID, updateOptions, properties);

        if (! endOneKnownDuplicate)
        {
            classificationExplorerClient.setKnownDuplicateClassification(endOneGUID,
                                                                          null,
                                                                          this.getMakeAnchorOptions(classificationExplorerClient, endOneHeader));
        }

        if (! endTwoKnownDuplicate)
        {
            classificationExplorerClient.setKnownDuplicateClassification(endTwoGUID,
                                                                          null,
                                                                          this.getMakeAnchorOptions(classificationExplorerClient, endTwoHeader));
        }

        auditLog.logMessage(methodName,
                            MendelAuditCode.DUPLICATES_VALIDATED.getMessageDefinition(connectorName,
                                                                                      duplicateLinkGUID,
                                                                                      endOneGUID,
                                                                                      endTwoGUID));
    }


    /**
     * Ask a steward to decide whether the two linked elements represent the same thing.  Both elements are attached
     * to the to do as action targets so that the steward can work on them directly.
     *
     * @param duplicateLinkGUID unique identifier of the duplicate link
     * @param endOneGUID unique identifier of the element at end one of the duplicate link
     * @param endTwoGUID unique identifier of the element at end two of the duplicate link
     *
     * @throws Exception the to do could not be created - reported by the caller
     */
    private void requestStewardDecision(String duplicateLinkGUID,
                                        String endOneGUID,
                                        String endTwoGUID) throws Exception
    {
        final String methodName = "requestStewardDecision";

        /*
         * There is one to do per duplicate link.  The in-memory record answers immediately for the links this
         * connector instance has already dealt with; the repository is asked about the rest, because a
         * restarted connector - or a second one - has no memory of what was raised before, and a steward with
         * two to dos for the same decision has to work out for themselves that they are the same decision.
         */
        String toDoQualifiedName = connectorName + "::" + ActionType.DUPLICATE_REVIEW.getActionTargetName() + "::" + duplicateLinkGUID;

        if (! toDosRaised.add(duplicateLinkGUID))
        {
            if (log.isDebugEnabled())
            {
                log.debug("To do already raised by this connector for duplicate link " + duplicateLinkGUID);
            }

            return;
        }

        if (this.toDoAlreadyExists(toDoQualifiedName))
        {
            if (log.isDebugEnabled())
            {
                log.debug("To do " + toDoQualifiedName + " already exists for duplicate link " + duplicateLinkGUID);
            }

            return;
        }

        ToDoProperties toDoProperties = new ToDoProperties();

        toDoProperties.setQualifiedName(toDoQualifiedName);
        toDoProperties.setDisplayName(ActionType.DUPLICATE_REVIEW.getDescription());
        toDoProperties.setDescription("Elements " + endOneGUID + " and " + endTwoGUID + " have been detected as potential " +
                                              "duplicates and are linked with duplicate link " + duplicateLinkGUID + ".  Decide " +
                                              "whether they represent the same thing.  If they do, move the status of the duplicate link to " +
                                              "VALIDATED and add the KnownDuplicate classification to both elements so that they are combined " +
                                              "when they are retrieved.  If they do not, move the status of the duplicate link to DEPRECATED.");
        toDoProperties.setCategory(ActionType.DUPLICATE_REVIEW.getDisplayName());
        toDoProperties.setPriority(0);

        List<NewActionTarget> actionTargets = new ArrayList<>();

        actionTargets.add(this.getActionTarget(endOneGUID));
        actionTargets.add(this.getActionTarget(endTwoGUID));

        String toDoGUID = integrationContext.openToDo(null,
                                                       toDoProperties,
                                                       this.getStewardRoleGUID(),
                                                       null,
                                                       actionTargets);

        auditLog.logMessage(methodName,
                            MendelAuditCode.STEWARD_ACTION_REQUESTED.getMessageDefinition(connectorName,
                                                                                          toDoGUID,
                                                                                          duplicateLinkGUID,
                                                                                          endOneGUID,
                                                                                          endTwoGUID));
    }


    /* ==============================================================================
     * Pass two - the links this connector validated itself, whose grounds may since have gone.
     */


    /**
     * Withdraw the validations this connector made itself where the grounds for them no longer hold.
     * <p>
     * A close match is validated automatically, and from then on the two elements are combined on retrieval.  The
     * grounds can disappear afterwards - the usual way is a qualified name being corrected, which is what a pair
     * that only ever shared a name by mistake looks like once the mistake is fixed.  Nothing else revisits a
     * validated link, so without this the two elements stay combined for ever on the strength of a match that no
     * longer exists.
     * <p>
     * Only this connector's own validations are reconsidered.  A steward's decision is a judgement this connector
     * is not entitled to overturn - a steward may well validate a pair that was never a close match, and that is
     * the normal case rather than an exception.  The two are told apart by the link's updatedBy: this connector
     * writes the link under its own userId, so a link updated by anyone else was ruled on by somebody else.
     * <p>
     * The link is moved to DEPRECATED rather than deleted, which leaves the decision visible and reversible: a
     * steward can see what was withdrawn and why, and validate it again if the pair really are duplicates.  The
     * KnownDuplicate classifications are not touched here - removeRetiredClassifications() takes them off once
     * the element is no longer deduplicated by any route, which is the same rule that applies when a steward
     * retires a link.
     * <p>
     * An element that has been consolidated is not a special case here.  Retiring the link records that the
     * pairwise evidence has gone; it does not break up the cluster, because the members go on being reached
     * through the element that replaced them until a steward removes the consolidation.  A message is raised so
     * that the steward knows the cluster now rests on less than it did.
     *
     * @param duplicateLinks all of the duplicate links in the open metadata ecosystem
     */
    private void reconsiderOwnValidations(List<OpenMetadataRelationship> duplicateLinks)
    {
        final String methodName = "reconsiderOwnValidations";

        for (OpenMetadataRelationship duplicateLink : duplicateLinks)
        {
            if (this.getStatusIdentifier(duplicateLink) != StatusIdentifier.VALIDATED.getOrdinal())
            {
                continue;
            }

            if (! this.isOwnValidation(duplicateLink))
            {
                continue;
            }

            OpenMetadataElementStub endOne = duplicateLink.getElementAtEnd1();
            OpenMetadataElementStub endTwo = duplicateLink.getElementAtEnd2();

            if ((endOne == null) || (endTwo == null))
            {
                continue;
            }

            if (this.isCloseMatch(endOne, endOne.getUniqueName(), endTwo, endTwo.getUniqueName()))
            {
                /*
                 * The grounds still hold.
                 */
                continue;
            }

            try
            {
                this.retireDuplicateLink(duplicateLink);

                auditLog.logMessage(methodName,
                                    MendelAuditCode.OWN_VALIDATION_WITHDRAWN.getMessageDefinition(connectorName,
                                                                                                   duplicateLink.getRelationshipGUID(),
                                                                                                   endOne.getGUID(),
                                                                                                   endTwo.getGUID()));

                /*
                 * Retiring the link does not break up a consolidated cluster - the members go on being reached
                 * through the element that replaced them - but the evidence that justified the cluster has just
                 * become weaker, and only a steward can decide what that means for it.
                 */
                if (this.isConsolidated(endOne) || this.isConsolidated(endTwo))
                {
                    auditLog.logMessage(methodName,
                                        MendelAuditCode.CONSOLIDATED_CLUSTER_WEAKENED.getMessageDefinition(connectorName,
                                                                                                            duplicateLink.getRelationshipGUID(),
                                                                                                            endOne.getGUID(),
                                                                                                            endTwo.getGUID()));
                }
            }
            catch (Exception error)
            {
                /*
                 * One duplicate link that can not be processed must not stop the rest being reconsidered.
                 */
                auditLog.logException(methodName,
                                      MendelAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                error.getClass().getName(),
                                                                                                methodName,
                                                                                                error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Determine whether this connector was the one that last ruled on a duplicate link.  The connector writes its
     * decisions under its own userId, so that is what updatedBy holds for a link it validated - and anything else
     * means a steward, or another service, has had the last word on it.
     * <p>
     * A link that has never been updated is not this connector's: it is a detected link that nothing has ruled on,
     * and this pass only looks at validated ones anyway.
     *
     * @param duplicateLink the duplicate link
     * @return flag - true means this connector made the decision that stands on this link
     */
    private boolean isOwnValidation(OpenMetadataRelationship duplicateLink)
    {
        if ((duplicateLink.getVersions() == null) || (duplicateLink.getVersions().getUpdatedBy() == null))
        {
            return false;
        }

        return duplicateLink.getVersions().getUpdatedBy().equals(integrationContext.getMyUserId());
    }


    /**
     * Determine whether an element has been consolidated - that is, whether a consolidated element has been created
     * to stand for the cluster this element belongs to.  The members of a cluster do not carry a classification
     * saying so; what they carry is a ConsolidatedDuplicateLink to the element that replaced them.
     *
     * @param element element to test
     * @return flag - true means the element belongs to a consolidated cluster
     * @throws Exception the retrieval failed - reported by the caller
     */
    private boolean isConsolidated(OpenMetadataElementStub element) throws Exception
    {
        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        RelatedMetadataElementList consolidatedLinks = openMetadataStore.getRelatedMetadataElements(element.getGUID(),
                                                                                                    0,
                                                                                                    OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK.typeName,
                                                                                                    this.getRawQueryOptions(0));

        return (consolidatedLinks != null) &&
                       (consolidatedLinks.getElementList() != null) &&
                       (! consolidatedLinks.getElementList().isEmpty());
    }


    /**
     * Move a duplicate link to DEPRECATED, and record the reason on the link.
     * <p>
     * The status is also written back onto the copy of the link that this refresh is working from, so that the
     * passes that follow - the one that strips the classifications from elements whose links are all retired, and
     * the one that builds clusters out of the validated links - see the decision that has just been made rather
     * than the state the link was retrieved in.
     *
     * @param duplicateLink the duplicate link to retire
     * @throws Exception the update failed - reported by the caller
     */
    private void retireDuplicateLink(OpenMetadataRelationship duplicateLink) throws Exception
    {
        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        ElementProperties properties = propertyHelper.addIntProperty(null,
                                                                     OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                     StatusIdentifier.DEPRECATED.getOrdinal());

        properties = propertyHelper.addStringProperty(properties,
                                                       OpenMetadataProperty.NOTES.name,
                                                       withdrawnExplanation);

        UpdateOptions updateOptions = openMetadataStore.getUpdateOptions(true);

        updateOptions.setForDuplicateProcessing(true);

        this.setOwningMetadataCollection(updateOptions, duplicateLink);

        openMetadataStore.updateRelatedElementsInStore(duplicateLink.getRelationshipGUID(), updateOptions, properties);

        duplicateLink.setRelationshipProperties(propertyHelper.addIntProperty(duplicateLink.getRelationshipProperties(),
                                                                              OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                              StatusIdentifier.DEPRECATED.getOrdinal()));
    }


    /* ==============================================================================
     * Pass three - the duplicate links that a steward has retired.
     */


    /**
     * Remove the KnownDuplicate classification from the elements that are no longer combined with anything.
     * <p>
     * The classification is what the retrieval processing gates all of its deduplication on - both the peer
     * duplicates and, for an element that has been consolidated, the redirect to the element that replaced its
     * cluster.  So it may only come off an element that is not deduplicated by either route: no live
     * PeerDuplicateLink, and no ConsolidatedDuplicateLink.
     * <p>
     * Taking it off an element that still has a ConsolidatedDuplicateLink would silently detach that element from
     * its consolidated cluster - it would start returning itself while the rest of the cluster still returned the
     * consolidated element, which continues to carry the content merged from it.  Whether a cluster should be
     * broken up is a steward's decision, made by removing the consolidation, not a side effect of retiring the
     * pairwise evidence that first justified it.
     *
     * @param duplicateLinks all of the duplicate links in the open metadata ecosystem
     */
    private void removeRetiredClassifications(List<OpenMetadataRelationship> duplicateLinks)
    {
        final String methodName = "removeRetiredClassifications";

        /*
         * Build the set of elements whose duplicate links are all retired.  An element is a candidate when it is at
         * the end of a retired link, and is disqualified as soon as it is found at the end of a link that is not.
         */
        Map<String, OpenMetadataElementStub> candidateElements   = new HashMap<>();
        Set<String>                          disqualifiedGUIDs   = new HashSet<>();

        for (OpenMetadataRelationship duplicateLink : duplicateLinks)
        {
            boolean retiredLink = retiredStatuses.contains(this.getStatusIdentifier(duplicateLink));

            for (OpenMetadataElementStub element : this.getEnds(duplicateLink))
            {
                if (retiredLink)
                {
                    candidateElements.put(element.getGUID(), element);
                }
                else
                {
                    disqualifiedGUIDs.add(element.getGUID());
                }
            }
        }

        for (OpenMetadataElementStub candidateElement : candidateElements.values())
        {
            if ((! disqualifiedGUIDs.contains(candidateElement.getGUID())) && (this.isKnownDuplicate(candidateElement)))
            {
                try
                {
                    if (this.isConsolidated(candidateElement))
                    {
                        /*
                         * The element is still reached through its consolidated cluster, so it is still a duplicate
                         * of something even though none of its peer links are live.
                         */
                        continue;
                    }

                    ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

                    MetadataSourceOptions metadataSourceOptions = classificationExplorerClient.getMetadataSourceOptions();

                    metadataSourceOptions.setForDuplicateProcessing(true);

                    this.setOwningMetadataCollection(metadataSourceOptions, candidateElement);

                    classificationExplorerClient.clearKnownDuplicateClassification(candidateElement.getGUID(), metadataSourceOptions);

                    auditLog.logMessage(methodName,
                                        MendelAuditCode.RETIRED_DUPLICATE.getMessageDefinition(connectorName,
                                                                                               candidateElement.getGUID()));
                }
                catch (Exception error)
                {
                    auditLog.logException(methodName,
                                          MendelAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                    error.getClass().getName(),
                                                                                                    methodName,
                                                                                                    error.getMessage()),
                                          error);
                }
            }
        }
    }


    /* ==============================================================================
     * Pass four - the clusters of validated peer duplicates.
     */


    /**
     * Group the validated duplicate links into clusters of elements and consolidate the clusters that have reached
     * the configured size.
     *
     * @param duplicateLinks all of the duplicate links in the open metadata ecosystem
     */
    private void consolidateValidatedClusters(List<OpenMetadataRelationship> duplicateLinks)
    {
        final String methodName = "consolidateValidatedClusters";

        /*
         * Two elements are in the same cluster if there is a chain of validated duplicate links between them, so
         * the clusters are the connected components of the graph that the validated links form.
         */
        Map<String, Set<String>>             clusterMembers = new HashMap<>();
        Map<String, OpenMetadataElementStub> elementLookup  = new HashMap<>();

        for (OpenMetadataRelationship duplicateLink : duplicateLinks)
        {
            if (this.getStatusIdentifier(duplicateLink) == StatusIdentifier.VALIDATED.getOrdinal())
            {
                List<OpenMetadataElementStub> ends = this.getEnds(duplicateLink);

                if (ends.size() == 2)
                {
                    for (OpenMetadataElementStub element : ends)
                    {
                        elementLookup.put(element.getGUID(), element);
                    }

                    this.mergeClusters(clusterMembers, ends.get(0).getGUID(), ends.get(1).getGUID());
                }
            }
        }

        MendelDuplicateConsolidator consolidator = new MendelDuplicateConsolidator(integrationContext,
                                                                                   propertyHelper,
                                                                                   connectorName,
                                                                                   auditLog);

        for (Set<String> cluster : new HashSet<>(clusterMembers.values()))
        {
            if (cluster.size() >= clusterSize)
            {
                try
                {
                    List<OpenMetadataElementStub> members = new ArrayList<>();

                    for (String memberGUID : cluster)
                    {
                        members.add(elementLookup.get(memberGUID));
                    }

                    consolidator.consolidateCluster(members);
                }
                catch (Exception error)
                {
                    auditLog.logException(methodName,
                                          MendelAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                    error.getClass().getName(),
                                                                                                    methodName,
                                                                                                    error.getMessage()),
                                          error);
                }
            }
        }
    }


    /**
     * Add two linked elements to the same cluster, combining the clusters that each of them is already in.
     * Every member of the resulting cluster maps to the same set, so a cluster is complete once all of the
     * validated links have been added.
     *
     * @param clusterMembers map from element to the cluster it is in
     * @param endOneGUID unique identifier of the element at end one of a validated duplicate link
     * @param endTwoGUID unique identifier of the element at end two of a validated duplicate link
     */
    private void mergeClusters(Map<String, Set<String>> clusterMembers,
                               String                   endOneGUID,
                               String                   endTwoGUID)
    {
        Set<String> endOneCluster = clusterMembers.get(endOneGUID);
        Set<String> endTwoCluster = clusterMembers.get(endTwoGUID);

        if ((endOneCluster != null) && (endOneCluster == endTwoCluster))
        {
            return;
        }

        Set<String> combinedCluster = new HashSet<>();

        if (endOneCluster != null)
        {
            combinedCluster.addAll(endOneCluster);
        }

        if (endTwoCluster != null)
        {
            combinedCluster.addAll(endTwoCluster);
        }

        combinedCluster.add(endOneGUID);
        combinedCluster.add(endTwoGUID);

        for (String memberGUID : combinedCluster)
        {
            clusterMembers.put(memberGUID, combinedCluster);
        }
    }


    /* ==============================================================================
     * Shared helpers.
     */


    /**
     * Name the metadata collection that owns an instance as the external source of an update.
     * <p>
     * An instance that this repository does not own - one loaded from a content pack, or mastered by another
     * repository in the cohort - can only be changed on behalf of whoever does own it.  Its home metadata
     * collection is recorded in its own header, so that is what is passed.  This matters for the duplicates
     * that arrive from successive releases of a content pack: without it, every attempt to validate a link
     * or classify an element is refused, and the duplicates that most need managing are the ones that cannot
     * be touched.
     * <p>
     * Nothing is set for an instance this repository owns - naming an external source for a local instance
     * would wrongly record it as belonging to somebody else.
     *
     * @param metadataSourceOptions options to fill in
     * @param elementHeader header of the instance being changed
     */
    private void setOwningMetadataCollection(MetadataSourceOptions metadataSourceOptions,
                                             ElementControlHeader  elementHeader)
    {
        if ((elementHeader != null) && (elementHeader.getOrigin() != null))
        {
            ElementOriginCategory originCategory = elementHeader.getOrigin().getOriginCategory();

            if ((originCategory != null) && (originCategory != ElementOriginCategory.LOCAL_COHORT))
            {
                metadataSourceOptions.setExternalSourceGUID(elementHeader.getOrigin().getHomeMetadataCollectionId());
                metadataSourceOptions.setExternalSourceName(elementHeader.getOrigin().getHomeMetadataCollectionName());
            }
        }
    }


    /**
     * Return the options to use when classifying an element, naming the metadata collection that owns it if
     * this repository does not.
     *
     * @param classificationExplorerClient client that the classification is added through
     * @param elementHeader header of the element being classified
     * @return options
     */
    private MakeAnchorOptions getMakeAnchorOptions(ClassificationExplorerClient classificationExplorerClient,
                                                   ElementControlHeader         elementHeader)
    {
        MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions(classificationExplorerClient.getMetadataSourceOptions());

        makeAnchorOptions.setForDuplicateProcessing(true);

        this.setOwningMetadataCollection(makeAnchorOptions, elementHeader);

        return makeAnchorOptions;
    }


    /**
     * Extract the status identifier from a duplicate link.
     *
     * @param duplicateLink the duplicate link
     * @return status identifier - zero (DISCOVERED) if the property is not set
     */
    private int getStatusIdentifier(OpenMetadataRelationship duplicateLink)
    {
        final String methodName = "getStatusIdentifier";

        return propertyHelper.getIntProperty(connectorName,
                                             OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                             duplicateLink.getRelationshipProperties(),
                                             methodName);
    }


    /**
     * Return the elements at the two ends of a duplicate link.
     *
     * @param duplicateLink the duplicate link
     * @return list of up to two elements
     */
    private List<OpenMetadataElementStub> getEnds(OpenMetadataRelationship duplicateLink)
    {
        List<OpenMetadataElementStub> ends = new ArrayList<>();

        if (duplicateLink.getElementAtEnd1() != null)
        {
            ends.add(duplicateLink.getElementAtEnd1());
        }

        if (duplicateLink.getElementAtEnd2() != null)
        {
            ends.add(duplicateLink.getElementAtEnd2());
        }

        return ends;
    }


    /**
     * Determine whether an element already has the KnownDuplicate classification.
     *
     * @param element element to test
     * @return boolean flag
     */
    private boolean isKnownDuplicate(OpenMetadataElementStub element)
    {
        if (element.getClassifications() != null)
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                if ((classification != null) &&
                        (OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName.equals(classification.getClassificationName())))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Determine whether a to do has already been raised for a duplicate link.  The to do's qualified name is
     * derived from the link's unique identifier, so the same link always produces the same name and the
     * question can be answered by looking that name up.
     *
     * @param toDoQualifiedName qualified name that the to do for this link would have
     * @return true if the to do is already there
     */
    private boolean toDoAlreadyExists(String toDoQualifiedName)
    {
        final String methodName = "toDoAlreadyExists";

        try
        {
            OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

            GetOptions getOptions = openMetadataStore.getGetOptions();

            getOptions.setForDuplicateProcessing(true);

            return openMetadataStore.getMetadataElementByUniqueName(toDoQualifiedName,
                                                                     OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                     getOptions) != null;
        }
        catch (Exception notFound)
        {
            /*
             * A to do that can not be retrieved is treated as absent, which risks a duplicate to do rather
             * than a missing one - the request to create it fails harmlessly if it turns out to be there.
             */
            if (log.isDebugEnabled())
            {
                log.debug("No existing to do found for " + toDoQualifiedName + " during " + methodName + ": " + notFound.getMessage());
            }

            return false;
        }
    }


    /**
     * Create an action target for one of the potential duplicates.
     *
     * @param elementGUID unique identifier of the element that the steward works on
     * @return action target
     */
    private NewActionTarget getActionTarget(String elementGUID)
    {
        NewActionTarget actionTarget = new NewActionTarget();

        actionTarget.setActionTargetName(ActionType.DUPLICATE_REVIEW.getActionTargetName());
        actionTarget.setActionTargetGUID(elementGUID);

        return actionTarget;
    }


    /**
     * Return the unique identifier of the person role that the to dos are assigned to, creating it if this is the
     * first time it is needed.  The value is cached because every to do is assigned to the same role.
     *
     * @return unique identifier of the DuplicateMetadataSteward person role
     *
     * @throws Exception the role could not be retrieved or created - reported by the caller
     */
    private synchronized String getStewardRoleGUID() throws Exception
    {
        final String methodName = "getStewardRoleGUID";

        if (stewardRoleGUID != null)
        {
            return stewardRoleGUID;
        }

        ActorRoleClient actorRoleClient = integrationContext.getActorRoleClient(OpenMetadataType.PERSON_ROLE.typeName);

        QueryOptions roleQueryOptions = actorRoleClient.getQueryOptions();

        roleQueryOptions.setForDuplicateProcessing(true);

        List<OpenMetadataRootElement> existingRoles = actorRoleClient.getActorRolesByName(stewardRoleQualifiedName, roleQueryOptions);

        if ((existingRoles != null) && (! existingRoles.isEmpty()))
        {
            stewardRoleGUID = existingRoles.get(0).getElementHeader().getGUID();

            return stewardRoleGUID;
        }

        PersonRoleProperties personRoleProperties = new PersonRoleProperties();

        personRoleProperties.setQualifiedName(stewardRoleQualifiedName);
        personRoleProperties.setIdentifier(stewardRoleName);
        personRoleProperties.setDisplayName(stewardRoleName);
        personRoleProperties.setDescription(stewardRoleDescription);

        NewElementOptions newElementOptions = new NewElementOptions(actorRoleClient.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);

        stewardRoleGUID = actorRoleClient.createActorRole(newElementOptions,
                                                          null,
                                                          personRoleProperties,
                                                          null);

        auditLog.logMessage(methodName,
                            MendelAuditCode.NEW_STEWARD_ROLE.getMessageDefinition(connectorName,
                                                                                  stewardRoleName,
                                                                                  stewardRoleGUID));

        return stewardRoleGUID;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        auditLog.logMessage(methodName,
                            MendelAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName,
                                                                                    integrationContext.getMetadataAccessServer(),
                                                                                    integrationContext.getMetadataAccessServerPlatformURLRoot()));

        super.disconnect();
    }
}
