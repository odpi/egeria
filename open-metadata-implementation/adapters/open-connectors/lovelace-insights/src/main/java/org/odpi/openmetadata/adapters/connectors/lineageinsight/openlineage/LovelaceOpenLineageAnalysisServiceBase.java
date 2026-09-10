/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreConnector;
import org.odpi.openmetadata.adapters.connectors.lovelaceinsight.ffdc.LovelaceInsightAuditCode;
import org.odpi.openmetadata.adapters.connectors.lovelaceinsight.ffdc.LovelaceInsightErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.opengovernance.controls.Guard;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

/**
 * LovelaceOpenLineageAnalysisServiceBase is the common base for the Lovelace services that analyse a historical
 * OpenLineage log store.  It locates the log store (from the openLineageLogStore action target, or the
 * logStoreDirectory request parameter), reads the events in the analysis window into an OpenLineageRunHistory,
 * hands the history to the subclass and records the completion status.  It also provides the lookup of the
 * processes and data assets that the OpenLineage cataloguer created (or matched) for the jobs and datasets,
 * using the same identity rules as the cataloguer - a job is a DeployedSoftwareComponent and a dataset is a
 * DataAsset whose resourceName and namespacePath are the OpenLineage name and namespace.
 */
public abstract class LovelaceOpenLineageAnalysisServiceBase extends GeneralGovernanceActionService
{
    private static final String LEGACY_JOB_QUALIFIED_NAME_PREFIX = "OpenLineageJob:";
    private static final String QUALIFIED_NAME_SEPARATOR         = "::";
    private static final String UNKNOWN_NAMESPACE                = "default";
    private static final int    DEFAULT_ANALYSIS_WINDOW_DAYS     = 30;

    protected static final String ANALYSIS_TIME_PROPERTY         = "analysisTime";
    protected static final String ANALYSIS_WINDOW_START_PROPERTY = "analysisWindowStart";
    protected static final String ANALYSIS_WINDOW_END_PROPERTY   = "analysisWindowEnd";
    protected static final String ANALYSIS_SERVICE_PROPERTY      = "analysisService";

    protected Date windowStart  = null;
    protected Date windowEnd    = null;
    protected Date analysisTime = null;

    private final Map<OpenLineageRunHistory.Key, OpenMetadataRootElement> processCache = new HashMap<>();
    private final Map<OpenLineageRunHistory.Key, OpenMetadataRootElement> assetCache   = new HashMap<>();


    /**
     * Perform the analysis on the history read from the log store.
     *
     * @param history events in the analysis window, grouped by job and dataset
     * @return number of elements updated
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    protected abstract int analyse(OpenLineageRunHistory history) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException;


    /**
     * Return a short description of what the service produces, for the completion message.
     *
     * @return description
     */
    protected abstract String getAnalysisDescription();


    /**
     * Read the log store and run the analysis.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        Connector logStoreConnector = null;

        try
        {
            analysisTime = new Date();

            /*
             * Work out the analysis window.
             */
            int windowDays = DEFAULT_ANALYSIS_WINDOW_DAYS;

            Map<String, String> requestParameters = governanceContext.getRequestParameters();

            if ((requestParameters != null) && (requestParameters.get(OpenLineageAnalysisRequestParameter.ANALYSIS_WINDOW_DAYS.getName()) != null))
            {
                try
                {
                    windowDays = Integer.parseInt(requestParameters.get(OpenLineageAnalysisRequestParameter.ANALYSIS_WINDOW_DAYS.getName()).trim());
                }
                catch (NumberFormatException error)
                {
                    windowDays = DEFAULT_ANALYSIS_WINDOW_DAYS;
                }
            }

            windowEnd   = analysisTime;
            windowStart = (windowDays > 0) ? new Date(analysisTime.getTime() - (windowDays * 24L * 60L * 60L * 1000L)) : null;

            /*
             * Locate the log store: the action target folder first, then the request parameter.
             */
            File   logStoreDirectory = null;
            String logStoreName      = null;

            ActionTargetElement logStoreTarget = getActionTarget(OpenLineageAnalysisActionTarget.OPEN_LINEAGE_LOG_STORE.getName());

            if ((logStoreTarget != null) && (logStoreTarget.getTargetElement() != null))
            {
                logStoreConnector = governanceContext.getConnectorForAsset(logStoreTarget.getTargetElement().getElementGUID());

                if (logStoreConnector instanceof BasicFileStoreConnector fileStoreConnector)
                {
                    fileStoreConnector.start();

                    logStoreDirectory = fileStoreConnector.getFile();
                    logStoreName      = logStoreTarget.getTargetElement().getElementGUID();
                }
            }

            if ((logStoreDirectory == null) && (requestParameters != null) && (requestParameters.get(OpenLineageAnalysisRequestParameter.LOG_STORE_DIRECTORY.getName()) != null))
            {
                logStoreDirectory = new File(requestParameters.get(OpenLineageAnalysisRequestParameter.LOG_STORE_DIRECTORY.getName()));
                logStoreName      = logStoreDirectory.getAbsolutePath();
            }

            if ((logStoreDirectory == null) || (! logStoreDirectory.isDirectory()))
            {
                AuditLogMessageDefinition messageDefinition = LovelaceInsightAuditCode.OPEN_LINEAGE_NO_LOG_STORE.getMessageDefinition(governanceServiceName,
                                                                                                                                       (logStoreDirectory == null) ? "<null>" : logStoreDirectory.getAbsolutePath());
                logRecord(methodName, messageDefinition);

                List<String> outputGuards = new ArrayList<>();
                outputGuards.add(Guard.SERVICE_FAILED.getName());

                governanceContext.recordCompletionStatus(CompletionStatus.FAILED, outputGuards, null, null, messageDefinition);
                return;
            }

            logRecord(methodName, LovelaceInsightAuditCode.OPEN_LINEAGE_ANALYSIS_STARTED.getMessageDefinition(governanceServiceName,
                                                                                                             logStoreDirectory.getAbsolutePath(),
                                                                                                             formatDate(windowStart),
                                                                                                             formatDate(windowEnd)));

            /*
             * Read and analyse.
             */
            OpenLineageRunHistory history = new OpenLineageLogStoreReader().read(logStoreDirectory, windowStart, windowEnd);

            logRecord(methodName, LovelaceInsightAuditCode.OPEN_LINEAGE_EVENTS_READ.getMessageDefinition(governanceServiceName,
                                                                                                        Integer.toString(history.getEventCount()),
                                                                                                        Integer.toString(history.getUnreadableFiles()),
                                                                                                        Integer.toString(history.getJobs().size()),
                                                                                                        Integer.toString(history.getDataSets().size())));

            int elementsUpdated = analyse(history);

            AuditLogMessageDefinition messageDefinition = LovelaceInsightAuditCode.OPEN_LINEAGE_ANALYSIS_COMPLETED.getMessageDefinition(governanceServiceName,
                                                                                                                                         Integer.toString(elementsUpdated),
                                                                                                                                         getAnalysisDescription(),
                                                                                                                                         logStoreName);
            logRecord(methodName, messageDefinition);

            List<String> outputGuards = new ArrayList<>();
            outputGuards.add(Guard.SERVICE_COMPLETED.getName());

            governanceContext.recordCompletionStatus(CompletionStatus.ACTIONED, outputGuards, null, null, messageDefinition);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(LovelaceInsightErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
        finally
        {
            if (logStoreConnector != null)
            {
                try
                {
                    logStoreConnector.disconnect();
                }
                catch (Exception error)
                {
                    /* ignore */
                }
            }
        }
    }


    /* ==========================================================================
     * Locating the catalogued elements
     */

    /**
     * Find the process that represents an OpenLineage job, using the cataloguer's naming and matching rules.
     *
     * @param job job identity
     * @return process or null if there is no unique process for the job
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    protected OpenMetadataRootElement findProcess(OpenLineageRunHistory.Key job) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "findProcess";

        if (processCache.containsKey(job))
        {
            return processCache.get(job);
        }

        AssetClient processClient = governanceContext.getAssetClient(OpenMetadataType.PROCESS.typeName);

        String qualifiedName = OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName + QUALIFIED_NAME_SEPARATOR +
                ((job.namespace() == null) ? UNKNOWN_NAMESPACE : job.namespace()) + QUALIFIED_NAME_SEPARATOR + job.name();

        OpenMetadataRootElement process = processClient.getAssetByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, processClient.getGetOptions());

        if (process == null)
        {
            process = processClient.getAssetByUniqueName(LEGACY_JOB_QUALIFIED_NAME_PREFIX + job.name(), OpenMetadataProperty.QUALIFIED_NAME.name, processClient.getGetOptions());
        }

        if (process == null)
        {
            process = findUniqueMatch(job, OpenMetadataType.PROCESS.typeName, methodName);
        }

        processCache.put(job, process);

        return process;
    }


    /**
     * Find the data asset that represents an OpenLineage dataset, using the cataloguer's matching rules.
     *
     * @param dataset dataset identity
     * @return asset or null if there is no unique asset for the dataset
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    protected OpenMetadataRootElement findDataAsset(OpenLineageRunHistory.Key dataset) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "findDataAsset";

        if (assetCache.containsKey(dataset))
        {
            return assetCache.get(dataset);
        }

        OpenMetadataRootElement asset = findUniqueMatch(dataset, OpenMetadataType.DATA_ASSET.typeName, methodName);

        assetCache.put(dataset, asset);

        return asset;
    }


    /**
     * Find the single asset of a type whose resourceName and namespacePath match an OpenLineage identity.
     *
     * @param key identity
     * @param typeName expected type (or supertype)
     * @param methodName calling method
     * @return asset or null (ambiguity and absence are both logged)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private OpenMetadataRootElement findUniqueMatch(OpenLineageRunHistory.Key key,
                                                    String                    typeName,
                                                    String                    methodName) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        AssetClient assetClient = governanceContext.getAssetClient();

        List<OpenMetadataRootElement> matches   = new ArrayList<>();
        int                           pageSize  = governanceContext.getMaxPageSize();
        int                           startFrom = 0;

        List<OpenMetadataRootElement> candidates = assetClient.getAssetsByName(key.name(), assetClient.getQueryOptions(startFrom, pageSize));

        while ((candidates != null) && (! candidates.isEmpty()))
        {
            for (OpenMetadataRootElement candidate : candidates)
            {
                if ((candidate != null) && (candidate.getElementHeader() != null) && (candidate.getProperties() instanceof AssetProperties candidateProperties))
                {
                    if ((key.name().equals(candidateProperties.getResourceName())) &&
                        (Objects.equals(key.namespace(), candidateProperties.getNamespacePath())) &&
                        (propertyHelper.isTypeOf(candidate.getElementHeader(), typeName)))
                    {
                        matches.add(candidate);
                    }
                }
            }

            if (candidates.size() < pageSize)
            {
                break;
            }

            startFrom  = startFrom + pageSize;
            candidates = assetClient.getAssetsByName(key.name(), assetClient.getQueryOptions(startFrom, pageSize));
        }

        if (matches.size() == 1)
        {
            return matches.get(0);
        }

        logRecord(methodName, LovelaceInsightAuditCode.OPEN_LINEAGE_ELEMENT_NOT_FOUND.getMessageDefinition(governanceServiceName,
                                                                                                          typeName,
                                                                                                          key.display(),
                                                                                                          Integer.toString(matches.size())));
        return null;
    }


    /* ==========================================================================
     * Helpers
     */

    /**
     * Format a date for messages and properties (ISO-8601, UTC).
     *
     * @param date date (may be null)
     * @return string
     */
    protected String formatDate(Date date)
    {
        if (date == null)
        {
            return "<none>";
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        return format.format(date);
    }


    /**
     * Describe a duration in milliseconds in human terms.
     *
     * @param millis milliseconds
     * @return string such as "2 hours 5 minutes"
     */
    protected String formatDuration(long millis)
    {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours   = minutes / 60;
        long days    = hours / 24;

        if (days > 0)
        {
            return days + " days " + (hours % 24) + " hours";
        }
        if (hours > 0)
        {
            return hours + " hours " + (minutes % 60) + " minutes";
        }
        if (minutes > 0)
        {
            return minutes + " minutes " + (seconds % 60) + " seconds";
        }

        return seconds + " seconds";
    }


    /**
     * Add the standard analysis properties to a map.
     *
     * @param properties map to fill
     */
    protected void addAnalysisProperties(Map<String, String> properties)
    {
        properties.put(ANALYSIS_TIME_PROPERTY, formatDate(analysisTime));
        properties.put(ANALYSIS_WINDOW_START_PROPERTY, formatDate(windowStart));
        properties.put(ANALYSIS_WINDOW_END_PROPERTY, formatDate(windowEnd));
        properties.put(ANALYSIS_SERVICE_PROPERTY, governanceServiceName);
    }


    /**
     * Return the length of the analysis window in days, bounded by the events actually seen.
     *
     * @param history history
     * @return days (at least 1)
     */
    protected double getWindowDays(OpenLineageRunHistory history)
    {
        Date start = windowStart;

        if ((start == null) || ((history.getEarliestEvent() != null) && (history.getEarliestEvent().after(start))))
        {
            start = history.getEarliestEvent();
        }

        if ((start == null) || (windowEnd == null))
        {
            return 1;
        }

        double days = (windowEnd.getTime() - start.getTime()) / (24.0 * 60 * 60 * 1000);

        return Math.max(days, 1);
    }


    /**
     * Add a long value to a string map.
     *
     * @param map map
     * @param key key
     * @param value value
     */
    protected void put(Map<String, String> map, String key, long value)
    {
        map.put(key, Long.toString(value));
    }


    /**
     * Add a double value to a string map, rounded to two decimal places.
     *
     * @param map map
     * @param key key
     * @param value value
     */
    protected void put(Map<String, String> map, String key, double value)
    {
        map.put(key, String.format("%.2f", value));
    }
}
