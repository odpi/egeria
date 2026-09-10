/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;

import org.odpi.openmetadata.adapters.connectors.lovelaceinsight.ffdc.LovelaceInsightAuditCode;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataScopeProperties;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * LovelaceOpenLineageDataScopeService analyses the series of writes to each dataset in an OpenLineage log store
 * and refines the DataScope classification of the dataset's asset.  The OpenLineage cataloguer extends the
 * data collection window as each write arrives; this service looks at the whole series to decide whether the
 * store is replaced or accumulates (from lifecycleStateChange and subset facets), sets the collection start time
 * accordingly, and records the write and read pattern in the classification's additional properties.
 */
public class LovelaceOpenLineageDataScopeService extends LovelaceOpenLineageAnalysisServiceBase
{
    /**
     * Default constructor
     */
    public LovelaceOpenLineageDataScopeService()
    {
    }


    /**
     * Describe what the service produces.
     *
     * @return description
     */
    @Override
    protected String getAnalysisDescription()
    {
        return "data scope classifications";
    }


    /**
     * Refine the data scope of each dataset.
     *
     * @param history events in the analysis window
     * @return number of assets updated
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    @Override
    protected int analyse(OpenLineageRunHistory history) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String methodName = "analyse";

        ClassificationExplorerClient classificationClient = governanceContext.getClassificationExplorerClient();

        int    updated    = 0;
        double windowDays = getWindowDays(history);

        for (OpenLineageRunHistory.DataSetHistory datasetHistory : history.getDataSets().values())
        {
            if ((datasetHistory.writes.isEmpty()) && (datasetHistory.reads.isEmpty()))
            {
                continue;
            }

            OpenMetadataRootElement asset = findDataAsset(datasetHistory.dataset);

            if (asset == null)
            {
                continue;
            }

            boolean             classified = false;
            DataScopeProperties dataScope  = null;

            if ((asset.getElementHeader() != null) && (asset.getElementHeader().getDataScope() != null))
            {
                classified = true;

                if (asset.getElementHeader().getDataScope().getClassificationProperties() instanceof DataScopeProperties existing)
                {
                    dataScope = new DataScopeProperties(existing);
                }
            }

            if (dataScope == null)
            {
                dataScope = new DataScopeProperties();
            }

            Map<String, String> additionalProperties = new HashMap<>();

            if (dataScope.getAdditionalProperties() != null)
            {
                additionalProperties.putAll(dataScope.getAdditionalProperties());
            }

            addAnalysisProperties(additionalProperties);

            String writePattern = null;

            if (! datasetHistory.writes.isEmpty())
            {
                List<OpenLineageRunHistory.WriteRecord> writes = new ArrayList<>(datasetHistory.writes);

                writes.sort(Comparator.comparing(OpenLineageRunHistory.WriteRecord::time));

                /*
                 * Work out how the store is written: replaced (the whole store is rewritten), partitioned
                 * (subsets are written, so data accumulates by partition) or appended.
                 */
                Date    latestReplacement = null;
                boolean partitioned       = false;
                long    totalRows         = 0;
                long    maxRows           = 0;
                int     writesWithRows    = 0;
                Set<OpenLineageRunHistory.Key> writers = new HashSet<>();
                Set<String>                    versions = new HashSet<>();

                for (OpenLineageRunHistory.WriteRecord write : writes)
                {
                    writers.add(write.job());

                    if (("OVERWRITE".equalsIgnoreCase(write.lifecycleStateChange())) ||
                        ("TRUNCATE".equalsIgnoreCase(write.lifecycleStateChange())) ||
                        ("CREATE".equalsIgnoreCase(write.lifecycleStateChange())))
                    {
                        latestReplacement = write.time();
                    }

                    if (write.subsetType() != null)
                    {
                        partitioned = true;
                    }

                    if (write.rowCount() != null)
                    {
                        totalRows += write.rowCount();
                        maxRows = Math.max(maxRows, write.rowCount());
                        writesWithRows++;
                    }

                    if (write.datasetVersion() != null)
                    {
                        versions.add(write.datasetVersion());
                    }
                }

                if (latestReplacement != null)
                {
                    writePattern = "REPLACED";
                }
                else if (partitioned)
                {
                    writePattern = "PARTITIONED";
                }
                else
                {
                    writePattern = "APPENDED";
                }

                Date firstWrite = writes.get(0).time();
                Date lastWrite  = writes.get(writes.size() - 1).time();

                /*
                 * The collection window starts when the first record still in the store was written.  For a replaced
                 * store that is the latest replacement; otherwise it is the earliest write known - which may be
                 * before this analysis window, so an earlier existing start time is kept.
                 */
                if (latestReplacement != null)
                {
                    dataScope.setDataCollectionStartTime(latestReplacement);
                }
                else if ((dataScope.getDataCollectionStartTime() == null) || (firstWrite.before(dataScope.getDataCollectionStartTime())))
                {
                    dataScope.setDataCollectionStartTime(firstWrite);
                }

                if ((dataScope.getDataCollectionEndTime() == null) || (lastWrite.after(dataScope.getDataCollectionEndTime())))
                {
                    dataScope.setDataCollectionEndTime(lastWrite);
                }

                additionalProperties.put("writePattern", writePattern);
                put(additionalProperties, "writesInWindow", writes.size());
                put(additionalProperties, "writersInWindow", writers.size());
                put(additionalProperties, "writesPerDay", writes.size() / windowDays);
                additionalProperties.put("firstWriteInWindow", formatDate(firstWrite));
                additionalProperties.put("lastWriteInWindow", formatDate(lastWrite));

                if (writesWithRows > 0)
                {
                    put(additionalProperties, "meanRowsPerWrite", (double) totalRows / writesWithRows);
                    put(additionalProperties, "maxRowsPerWrite", maxRows);
                    put(additionalProperties, "rowsWrittenInWindow", totalRows);
                }

                if (! versions.isEmpty())
                {
                    put(additionalProperties, "datasetVersionsInWindow", versions.size());
                }
            }

            if (! datasetHistory.reads.isEmpty())
            {
                Set<OpenLineageRunHistory.Key> readers = new HashSet<>();
                Date lastRead = null;

                for (OpenLineageRunHistory.ReadRecord read : datasetHistory.reads)
                {
                    readers.add(read.job());

                    if ((lastRead == null) || (read.time().after(lastRead)))
                    {
                        lastRead = read.time();
                    }
                }

                put(additionalProperties, "readsInWindow", datasetHistory.reads.size());
                put(additionalProperties, "readersInWindow", readers.size());
                put(additionalProperties, "readsPerDay", datasetHistory.reads.size() / windowDays);
                additionalProperties.put("lastReadInWindow", formatDate(lastRead));
            }

            dataScope.setAdditionalProperties(additionalProperties);

            if (classified)
            {
                classificationClient.updateDataScopeClassification(asset.getElementHeader().getGUID(), dataScope, classificationClient.getUpdateOptions(false));
            }
            else
            {
                classificationClient.addDataScopeClassification(asset.getElementHeader().getGUID(), dataScope, classificationClient.getMetadataSourceOptions());
            }

            logRecord(methodName, LovelaceInsightAuditCode.OPEN_LINEAGE_DATA_SCOPE_UPDATED.getMessageDefinition(governanceServiceName,
                                                                                                               datasetHistory.dataset.display(),
                                                                                                               asset.getElementHeader().getGUID(),
                                                                                                               (writePattern == null) ? "READ_ONLY" : writePattern,
                                                                                                               formatDate(dataScope.getDataCollectionStartTime()),
                                                                                                               formatDate(dataScope.getDataCollectionEndTime())));
            updated++;
        }

        return updated;
    }
}
