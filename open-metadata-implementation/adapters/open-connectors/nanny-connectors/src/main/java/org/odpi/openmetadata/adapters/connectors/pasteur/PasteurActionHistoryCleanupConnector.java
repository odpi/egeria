/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.pasteur;

import org.odpi.openmetadata.adapters.connectors.pasteur.controls.PasteurConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.pasteur.ffdc.PasteurAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementVersions;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyCondition;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PasteurActionHistoryCleanupConnector keeps the action history of the open metadata ecosystem in check, in
 * recognition of Louis Pasteur (1822-1895), whose work on germ theory and vaccination defeated infectious diseases.
 * Every engine action, governance action process instance, to do and other kind of action stays in the
 * repository after it finishes, so without housekeeping the action history grows without bound.
 * <p>
 * On each refresh the connector looks, type by type, for the actions of each type listed in its daysToCleanUp
 * configuration property whose activity status is one of its completionStatuses, and deletes those that
 * completed at least the configured number of days ago (see {@link PasteurCleanUpPolicy}).  It uses the
 * LOOK_FOR_LINEAGE delete method by default: an action with lineage relationships is archived, so it is still
 * available to lineage queries, and the others are soft-deleted.  Neither is a purge, so an action removed in error
 * can be restored.  The deleteMethod configuration property can name a different delete method.  The delete is
 * cascaded, so the elements anchored to an action go with it.
 * <p>
 * An action anchored to another action whose type has its own entry in daysToCleanUp - typically the engine actions
 * of a governance action process, which are anchored to the process instance - is left for the delete of its anchor
 * to remove.  That way a process instance and the engine actions it ran are removed together rather than the
 * engine actions disappearing from under a process instance that is still being kept.
 */
public class PasteurActionHistoryCleanupConnector extends IntegrationConnectorBase
{
    private PasteurCleanUpPolicy cleanUpPolicy = null;

    /**
     * The delete method, from the deleteMethod configuration property.
     */
    private DeleteMethod deleteMethod = PasteurCleanUpPolicy.DEFAULT_DELETE_METHOD;


    /**
     * Indicates that the connector is completely configured and can begin processing.  The configuration
     * properties are read here.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

        Object configuredDaysToCleanUp = null;

        if (configurationProperties != null)
        {
            configuredDaysToCleanUp = configurationProperties.get(PasteurConfigurationProperty.DAYS_TO_CLEAN_UP.getName());
        }

        List<String> configuredStatuses = super.getArrayConfigurationProperty(PasteurConfigurationProperty.COMPLETION_STATUSES.getName(),
                                                                              configurationProperties);

        List<String>      invalidEntries  = new ArrayList<>();
        Map<String, Long> daysToCleanUp   = PasteurCleanUpPolicy.parseDaysToCleanUp(configuredDaysToCleanUp, invalidEntries);
        List<String>      invalidStatuses = new ArrayList<>();
        List<String>      statuses        = PasteurCleanUpPolicy.parseCompletionStatuses(configuredStatuses,
                                                                                         PasteurConfigurationProperty.DEFAULT_COMPLETION_STATUSES,
                                                                                         invalidStatuses);

        if (! invalidEntries.isEmpty())
        {
            logRecord(methodName,
                      PasteurAuditCode.INVALID_DAYS_TO_CLEAN_UP.getMessageDefinition(connectorName,
                                                                                    invalidEntries.toString()));
        }

        if (! invalidStatuses.isEmpty())
        {
            logRecord(methodName,
                      PasteurAuditCode.INVALID_COMPLETION_STATUSES.getMessageDefinition(connectorName,
                                                                                       invalidStatuses.toString()));
        }

        String configuredDeleteMethod = super.getStringConfigurationProperty(PasteurConfigurationProperty.DELETE_METHOD.getName(),
                                                                             configurationProperties);

        deleteMethod = PasteurCleanUpPolicy.parseDeleteMethod(configuredDeleteMethod);

        if (deleteMethod == null)
        {
            logRecord(methodName,
                      PasteurAuditCode.INVALID_DELETE_METHOD.getMessageDefinition(connectorName,
                                                                                 configuredDeleteMethod,
                                                                                 PasteurCleanUpPolicy.DEFAULT_DELETE_METHOD.name()));

            deleteMethod = PasteurCleanUpPolicy.DEFAULT_DELETE_METHOD;
        }

        cleanUpPolicy = new PasteurCleanUpPolicy(daysToCleanUp, statuses);

        logRecord(methodName,
                  PasteurAuditCode.STARTING_CONNECTOR.getMessageDefinition(connectorName,
                                                                           integrationContext.getMetadataAccessServer(),
                                                                           integrationContext.getMetadataAccessServerPlatformURLRoot(),
                                                                           cleanUpPolicy.getDaysToCleanUp().toString(),
                                                                           statuses.toString(),
                                                                           deleteMethod.name()));

        if (! cleanUpPolicy.isActive())
        {
            logRecord(methodName,
                      PasteurAuditCode.NOTHING_TO_CLEAN_UP.getMessageDefinition(connectorName));
        }
    }


    /**
     * Delete (or, if they have lineage, archive) the finished actions that are old enough.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        if ((cleanUpPolicy == null) || (! cleanUpPolicy.isActive()))
        {
            return;
        }

        Date now = new Date();

        for (String typeName : cleanUpPolicy.getCleanUpTypeNames())
        {
            try
            {
                long deleteCount = this.cleanUpActions(typeName, now);

                if (deleteCount > 0)
                {
                    logRecord(methodName,
                              PasteurAuditCode.ACTIONS_DELETED.getMessageDefinition(connectorName,
                                                                                    Long.toString(deleteCount),
                                                                                    typeName,
                                                                                    Long.toString(cleanUpPolicy.getDaysToCleanUp(typeName, null)),
                                                                                    deleteMethod.name()));
                }
            }
            catch (Exception error)
            {
                logExceptionRecord(methodName,
                                   PasteurAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                              error.getClass().getName(),
                                                                                              methodName,
                                                                                              error.getMessage()),
                                   error);
            }
        }
    }


    /**
     * Delete the eligible actions of one type (including its subtypes).  The search returns the actions
     * with one of the completion statuses, a page at a time.  The deleted and archived actions drop out of the search results,
     * so the next page starts after the actions that were kept, not after all the actions that were read.
     *
     * @param typeName name of Action or one of its subtypes
     * @param now time to measure the age of the actions from
     * @return number of actions deleted
     * @throws Exception problem reading the actions
     */
    private long cleanUpActions(String typeName,
                                Date   now) throws Exception
    {
        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();
        SearchProperties  searchProperties  = this.getCompletionStatusSearchProperties();
        Set<String>       visitedGUIDs      = new HashSet<>();
        long              deleteCount       = 0;
        int               startFrom         = 0;

        List<OpenMetadataElement> actions = openMetadataStore.findMetadataElements(typeName,
                                                                                   null,
                                                                                   searchProperties,
                                                                                   null,
                                                                                   startFrom,
                                                                                   0);

        while ((actions != null) && (! actions.isEmpty()))
        {
            int keptCount = 0;
            int newCount  = 0;

            for (OpenMetadataElement action : actions)
            {
                if ((action == null) || (action.getElementGUID() == null) || (! visitedGUIDs.add(action.getElementGUID())))
                {
                    /*
                     * Seen before - it was kept, or its delete failed and it is still visible.
                     */
                    keptCount = keptCount + 1;
                }
                else
                {
                    newCount = newCount + 1;

                    if ((this.isEligible(action, now)) && (this.deleteAction(action)))
                    {
                        deleteCount = deleteCount + 1;
                    }
                    else
                    {
                        keptCount = keptCount + 1;
                    }
                }
            }

            if (newCount == 0)
            {
                /*
                 * The page only holds actions that have been dealt with already.
                 */
                break;
            }

            startFrom = startFrom + keptCount;

            actions = openMetadataStore.findMetadataElements(typeName,
                                                             null,
                                                             searchProperties,
                                                             null,
                                                             startFrom,
                                                             0);
        }

        return deleteCount;
    }


    /**
     * Return the search properties that match any of the completion statuses.
     *
     * @return search properties
     */
    private SearchProperties getCompletionStatusSearchProperties()
    {
        List<PropertyCondition> conditions = null;

        for (String completionStatus : cleanUpPolicy.getCompletionStatuses())
        {
            conditions = propertyHelper.addEnumProperty(conditions,
                                                        OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                        ActivityStatus.getOpenTypeName(),
                                                        completionStatus,
                                                        PropertyComparisonOperator.EQ);
        }

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(conditions);
        searchProperties.setMatchCriteria(MatchCriteria.ANY);

        return searchProperties;
    }


    /**
     * Return whether an action should be deleted now.
     *
     * @param action action retrieved from the open metadata store
     * @param now time to measure the age of the action from
     * @return boolean
     */
    private boolean isEligible(OpenMetadataElement action,
                               Date                now)
    {
        final String methodName = "isEligible";

        String       typeName       = null;
        List<String> superTypeNames = null;
        ElementType  elementType    = action.getType();

        if (elementType != null)
        {
            typeName       = elementType.getTypeName();
            superTypeNames = elementType.getSuperTypeNames();
        }

        Date updateTime = null;
        Date createTime = null;

        ElementVersions versions = action.getVersions();

        if (versions != null)
        {
            updateTime = versions.getUpdateTime();
            createTime = versions.getCreateTime();
        }

        String activityStatus = propertyHelper.getEnumPropertySymbolicName(connectorName,
                                                                           OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                           action.getElementProperties(),
                                                                           methodName);

        Date completionTime = propertyHelper.getDateProperty(connectorName,
                                                             OpenMetadataProperty.COMPLETION_TIME.name,
                                                             action.getElementProperties(),
                                                             methodName);

        if (! cleanUpPolicy.isEligible(typeName, superTypeNames, activityStatus, completionTime, updateTime, createTime, now))
        {
            return false;
        }

        return ! this.isLeftForAnchor(action);
    }


    /**
     * Return whether the action is anchored to another action whose type is cleaned up.  Deleting that anchor
     * removes this action too.
     *
     * @param action action retrieved from the open metadata store
     * @return boolean
     */
    private boolean isLeftForAnchor(OpenMetadataElement action)
    {
        final String methodName = "isLeftForAnchor";

        AttachedClassification anchors = propertyHelper.getClassification(action, OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);

        if (anchors == null)
        {
            return false;
        }

        String anchorGUID = propertyHelper.getStringProperty(connectorName,
                                                             OpenMetadataProperty.ANCHOR_GUID.name,
                                                             anchors.getClassificationProperties(),
                                                             methodName);

        if ((anchorGUID == null) || (anchorGUID.equals(action.getElementGUID())))
        {
            return false;
        }

        String anchorTypeName = propertyHelper.getStringProperty(connectorName,
                                                                 OpenMetadataProperty.ANCHOR_TYPE_NAME.name,
                                                                 anchors.getClassificationProperties(),
                                                                 methodName);

        return (cleanUpPolicy.getDaysToCleanUp(anchorTypeName, null) != null);
    }


    /**
     * Delete an action along with the elements anchored to it, using the configured delete method.  The
     * default, LOOK_FOR_LINEAGE, archives an action that has lineage relationships, so that lineage queries can
     * still find it, and soft-deletes the others.
     *
     * @param action action to delete
     * @return true if it was deleted
     */
    private boolean deleteAction(OpenMetadataElement action)
    {
        final String methodName = "deleteAction";

        try
        {
            OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();
            DeleteOptions     deleteOptions     = openMetadataStore.getDeleteOptions(true);

            deleteOptions.setDeleteMethod(deleteMethod);

            openMetadataStore.deleteMetadataElementInStore(action.getElementGUID(), deleteOptions);

            return true;
        }
        catch (Exception error)
        {
            String typeName = null;

            if (action.getType() != null)
            {
                typeName = action.getType().getTypeName();
            }

            logRecord(methodName,
                      PasteurAuditCode.DELETE_FAILED.getMessageDefinition(connectorName,
                                                                          action.getElementGUID(),
                                                                          typeName,
                                                                          error.getClass().getName(),
                                                                          error.getMessage()));

            return false;
        }
    }
}
