/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.wedgwood;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.TabularDataSetProvisioningLineage;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataCollection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataCollection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * WedgwoodProvisionSubscriptionGovernanceActionConnector copies data from one tabular data set to another.
 * <br><br>
 * The source may be a single tabular data set or a collection of them.  A digital product family's asset is a
 * collection - one table per product in the family - and delivering it means delivering each of its tables into
 * the destination, which for a family is a collection too: a schema that receives one table per product.  The
 * service asks the source collection for its table names, brings each into focus and copies it.  A table that
 * cannot be copied is logged and skipped so that the rest of the family is still delivered; the service then
 * completes with a failed status and says which tables were missed.
 * <br><br>
 * The lineage of the tables delivered is recorded once the copying is done - see
 * {@link TabularDataSetProvisioningLineage} for the pattern.  When the service is run by the provisioning pipeline
 * that a digital subscription sets up, the lineage passes through that pipeline's process.
 */
public class WedgwoodProvisionSubscriptionGovernanceActionConnector extends GeneralGovernanceActionService
{
    /*
     * The top-level process name is null unless the caller supplies one, so that the lineage can pass through
     * the provisioning pipeline that started this service when there is one.
     */
    private String  topLevelProcessName                  = null;
    private String  informationSupplyChainQualifiedName  = null;
    private String  topLevelProcessTemplateQualifiedName = null;

    /*
     * The default lineage pattern: a child process per run beneath the top-level process (not used when the
     * lineage passes through a provisioning pipeline), with column-level lineage wherever the schemas of both
     * data sets are catalogued.
     */
    private boolean createLineage = true;
    private boolean childProcessLineage = true;
    private boolean columnLevelLineage = true;


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        List<String>              outputGuards = new ArrayList<>();
        CompletionStatus          completionStatus;
        AuditLogMessageDefinition completionMessage = null;


        /*
         * Retrieve the source file and destination folder from either the request parameters or the action targets.  If both
         * are specified, then the action target elements take priority.
         */
        if (governanceContext.getRequestParameters() != null)
        {
            Map<String, String> requestParameters = governanceContext.getRequestParameters();

            for (String requestParameterName : requestParameters.keySet())
            {

                if (WedgwoodProvisionSubscriptionRequestParameter.INFORMATION_SUPPLY_CHAIN_QUALIFIED_NAME.getName().equals(requestParameterName))
                {
                    informationSupplyChainQualifiedName = requestParameters.get(requestParameterName);
                }
                else if (WedgwoodProvisionSubscriptionRequestParameter.NO_LINEAGE.getName().equals(requestParameterName))
                {
                    createLineage = false;
                }
                else if (WedgwoodProvisionSubscriptionRequestParameter.TOP_LEVEL_PROCESS_NAME.getName().equals(requestParameterName))
                {
                    topLevelProcessName = requestParameters.get(requestParameterName);
                }
                else if (WedgwoodProvisionSubscriptionRequestParameter.TOP_LEVEL_PROCESS_TEMPLATE_NAME.getName().equals(requestParameterName))
                {
                    topLevelProcessTemplateQualifiedName = requestParameters.get(requestParameterName);
                }
                else if (WedgwoodProvisionSubscriptionRequestParameter.TOP_LEVEL_PROCESS_ONLY_LINEAGE.getName().equals(requestParameterName))
                {
                    childProcessLineage = false;
                }
                else if (WedgwoodProvisionSubscriptionRequestParameter.IGNORE_COLUMN_LEVEL_LINEAGE.getName().equals(requestParameterName))
                {
                    columnLevelLineage = false;
                }
            }
        }

        OpenMetadataElement sourceMetadataElement = null;
        OpenMetadataElement destinationMetadataElement = null;

        if (governanceContext.getActionTargetElements() != null)
        {
            for (ActionTargetElement actionTargetElement : governanceContext.getActionTargetElements())
            {
                if (actionTargetElement != null)
                {
                    if (WedgwoodProvisionSubscriptionActionTarget.SOURCE_DATA_SET.getName().equals(actionTargetElement.getActionTargetName()))
                    {
                        sourceMetadataElement = actionTargetElement.getTargetElement();
                    }
                    else if (WedgwoodProvisionSubscriptionActionTarget.DESTINATION_DATA_SET.getName().equals(actionTargetElement.getActionTargetName()))
                    {
                        destinationMetadataElement = actionTargetElement.getTargetElement();
                    }
                }
            }
        }

        if ((sourceMetadataElement == null) || (destinationMetadataElement == null))
        {
            outputGuards.add(WedgwoodProvisionSubscriptionGuard.PROVISIONING_NO_TARGETS.getName());
            completionStatus = WedgwoodProvisionSubscriptionGuard.PROVISIONING_NO_TARGETS.getCompletionStatus();
            completionMessage = GovernanceActionConnectorsAuditCode.NO_TARGETS.getMessageDefinition(governanceServiceName);
        }
        else
        {
            String sourceName      = this.getElementName(sourceMetadataElement, methodName);
            String destinationName = this.getElementName(destinationMetadataElement, methodName);

            Connector sourceAssetConnector      = null;
            Connector destinationAssetConnector = null;

            /*
             * The tables delivered, for the lineage: source table name to destination table name.
             */
            Map<String, String> deliveredTables = new LinkedHashMap<>();

            try
            {
                sourceAssetConnector      = governanceContext.getConnectorForAsset(sourceMetadataElement.getElementGUID());
                destinationAssetConnector = governanceContext.getConnectorForAsset(destinationMetadataElement.getElementGUID());

                /*
                 * A connector is built by the connector broker and handed over unstarted - starting it is the
                 * caller's job, and it is where the connector reads its own configuration.  Using one without
                 * starting it means every value it should have read is still unset: the table name is null, and
                 * the first request that needs it fails complaining about a null parameter rather than about a
                 * connector that was never started.
                 */
                sourceAssetConnector.start();
                destinationAssetConnector.start();

                ReadableTabularDataSource sourceConnector      = (ReadableTabularDataSource) sourceAssetConnector;
                WritableTabularDataSource destinationConnector = (WritableTabularDataSource) destinationAssetConnector;

                if (sourceConnector instanceof ReadableTabularDataCollection sourceCollection)
                {
                    List<String> tableNames   = sourceCollection.getTableNames();
                    List<String> failedTables = new ArrayList<>();

                    for (String tableName : tableNames)
                    {
                        try
                        {
                            sourceCollection.setTableName(tableName, null);

                            deliveredTables.put(tableName, this.copyTable(sourceConnector, destinationConnector, sourceName, destinationName));
                        }
                        catch (Exception error)
                        {
                            /*
                             * One table that cannot be delivered does not stop the others: a family's products
                             * are independent of each other, and the subscriber is better served by most of
                             * them than by none.  The miss is logged here and reported in the completion status.
                             */
                            super.logExceptionRecord(methodName,
                                                     GovernanceActionConnectorsAuditCode.TABLE_PROVISIONING_FAILED.getMessageDefinition(governanceServiceName,
                                                                                                                                        tableName,
                                                                                                                                        sourceName,
                                                                                                                                        destinationName,
                                                                                                                                        error.getClass().getName(),
                                                                                                                                        error.getMessage()),
                                                     error);
                            failedTables.add(tableName);
                        }
                    }

                    if (failedTables.isEmpty())
                    {
                        completionMessage = GovernanceActionConnectorsAuditCode.COLLECTION_PROVISIONED.getMessageDefinition(governanceServiceName,
                                                                                                                             Integer.toString(tableNames.size()),
                                                                                                                             sourceName,
                                                                                                                             destinationName);
                        outputGuards.add(WedgwoodProvisionSubscriptionGuard.PROVISIONING_COMPLETE.getName());
                        completionStatus = WedgwoodProvisionSubscriptionGuard.PROVISIONING_COMPLETE.getCompletionStatus();
                    }
                    else
                    {
                        completionMessage = GovernanceActionConnectorsAuditCode.COLLECTION_PARTIALLY_PROVISIONED.getMessageDefinition(governanceServiceName,
                                                                                                                                       Integer.toString(tableNames.size() - failedTables.size()),
                                                                                                                                       Integer.toString(tableNames.size()),
                                                                                                                                       sourceName,
                                                                                                                                       destinationName,
                                                                                                                                       failedTables.toString());
                        super.logRecord(methodName, completionMessage);

                        outputGuards.add(WedgwoodProvisionSubscriptionGuard.PROVISIONING_FAILED_EXCEPTION.getName());
                        completionStatus = WedgwoodProvisionSubscriptionGuard.PROVISIONING_FAILED_EXCEPTION.getCompletionStatus();
                    }
                }
                else
                {
                    deliveredTables.put(sourceConnector.getTableName(),
                                        this.copyTable(sourceConnector, destinationConnector, sourceName, destinationName));

                    outputGuards.add(WedgwoodProvisionSubscriptionGuard.PROVISIONING_COMPLETE.getName());
                    completionStatus = WedgwoodProvisionSubscriptionGuard.PROVISIONING_COMPLETE.getCompletionStatus();
                }
            }
            catch (Exception error)
            {
                completionMessage = GovernanceActionConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage());
                super.logExceptionRecord(methodName, completionMessage, error);

                outputGuards.add(WedgwoodProvisionSubscriptionGuard.PROVISIONING_FAILED_EXCEPTION.getName());
                completionStatus = WedgwoodProvisionSubscriptionGuard.PROVISIONING_FAILED_EXCEPTION.getCompletionStatus();
            }
            finally
            {
                /*
                 * Both connectors hold resources - a database connection, a metadata client - that are only
                 * released by disconnecting them.  The engine action is done with them either way.
                 */
                this.disconnectQuietly(sourceAssetConnector);
                this.disconnectQuietly(destinationAssetConnector);
            }

            if ((createLineage) && (! deliveredTables.isEmpty()))
            {
                this.recordLineage(sourceMetadataElement.getElementGUID(),
                                   destinationMetadataElement.getElementGUID(),
                                   deliveredTables);
            }
        }

        try
        {
            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, completionMessage);
        }
        catch (OMFCheckedExceptionBase error)
        {
            throw new ConnectorCheckedException(error.getReportedErrorMessage(), error);
        }
    }


    /**
     * Copy the table the source is presenting into the destination, so that the destination holds exactly the
     * source's records afterwards.
     *
     * @param sourceConnector where the records come from
     * @param destinationConnector where they go
     * @param sourceName name of the source, for the log
     * @param destinationName name of the destination, for the log
     * @return canonical name of the destination table the records were written to
     * @throws ConnectorCheckedException problem reading or writing
     */
    private String copyTable(ReadableTabularDataSource sourceConnector,
                           WritableTabularDataSource destinationConnector,
                           String                    sourceName,
                           String                    destinationName) throws ConnectorCheckedException
    {
        final String methodName = "copyTable";

        String tableName         = sourceConnector.getTableName();
        long   sourceRecordCount = sourceConnector.getRecordCount();

        /*
         * A destination that holds many tables has to be told which one this data belongs in before it
         * can be asked anything about it.  Counting its records first asks a collection for the size of
         * a table it has not been given the name of, which fails rather than answering zero.
         */
        if (destinationConnector instanceof TabularDataCollection tabularDataCollection)
        {
            tabularDataCollection.setTableName(tableName, sourceConnector.getTableDescription());
        }

        /*
         * The destination is given the source's shape before it is asked what it holds.  Describing it
         * is what creates the table where there is not one yet, so counting first asks about a table
         * that need not exist - and a destination that has never been delivered to is exactly the
         * normal case for a new subscription.
         */
        List<TabularColumnDescription> columnDescriptions = sourceConnector.getColumnDescriptions();

        destinationConnector.setColumnDescriptions(columnDescriptions);

        long destinationRecordCount = destinationConnector.getRecordCount();

        long changedRecordCount = copyRecords(sourceConnector,
                                              sourceRecordCount,
                                              destinationConnector,
                                              destinationRecordCount,
                                              columnDescriptions);

        super.logRecord(methodName, GovernanceActionConnectorsAuditCode.TABLE_PROVISIONED.getMessageDefinition(governanceServiceName,
                                                                                                                Long.toString(sourceRecordCount),
                                                                                                                tableName,
                                                                                                                sourceName,
                                                                                                                destinationName,
                                                                                                                Long.toString(changedRecordCount)));

        /*
         * A collection destination was given the source's table name above; any other destination holds the
         * one table it was configured with.
         */
        if ((! (destinationConnector instanceof TabularDataCollection)) && (destinationConnector instanceof ReadableTabularDataSource readableDestination))
        {
            return readableDestination.getTableName();
        }

        return tableName;
    }


    /**
     * Return the positions of the columns that identify a record.
     *
     * @param columnDescriptions the source's columns - may be null
     * @return positions, empty when no column identifies a record
     */
    static List<Integer> getIdentifierColumnPositions(List<TabularColumnDescription> columnDescriptions)
    {
        List<Integer> identifierColumnPositions = new ArrayList<>();

        if (columnDescriptions != null)
        {
            for (int columnNumber = 0; columnNumber < columnDescriptions.size(); columnNumber++)
            {
                TabularColumnDescription columnDescription = columnDescriptions.get(columnNumber);

                if ((columnDescription != null) && (columnDescription.isIdentifier()))
                {
                    identifierColumnPositions.add(columnNumber);
                }
            }
        }

        return identifierColumnPositions;
    }


    /**
     * Make the destination's records match the source's.
     * <br>
     * Where the destination can be read back, only the differences are written.  A subscription is delivered
     * again each time its product changes, so most deliveries find the destination almost current.
     * <br>
     * Records are matched by identity, not position.  Comparing the record at position n on each side meant a
     * single record added or removed early in the table shifted every later record, so the whole tail looked
     * changed.  Each mismatch was a write, and each write threw away the destination's read-ahead, so the next
     * read re-ran an {@code OFFSET n} query.  A 43,600-row table cost tens of thousands of statements on every
     * delivery, quadratic in its size.  It also gave the wrong answer: a keyed destination upserts rather than
     * overwriting by position, so a record removed from the middle of the source was never removed, and the
     * last record was deleted in its place.
     * <br>
     * A record's identity is the values of its identifying columns.  Where no column identifies a record, the
     * whole record is its identity: a record either matches one in the destination or it does not, and a
     * changed one is a deletion plus an addition.
     * <br>
     * The two sides are read in step, a record from each at a time, and each record is matched against the
     * records of the other side that have not found a partner yet.  Both sides are normally in the same order -
     * by their identifying columns - so a record's partner is read at about the same time as it is, and what is
     * held in memory is little more than the differences.  Indexing the whole destination first held every one
     * of its records.  Nothing depends on the order, though: two sides that are ordered differently (two
     * databases with different collations, say) still match correctly, they just hold more while they do.
     * <br>
     * The destination is read once, from start to finish, before anything is written, so its reads are never
     * interrupted by a write.  The changes are then made in an order that keeps every row number valid for
     * a destination that works by position: deletions, at their positions before any is deleted, then
     * overwrites at their positions adjusted for those deletions, then additions on the end in source order.
     * A destination that works by identity ignores the row numbers and gets the same result.  Each kind of change
     * is passed to the destination together, so one that can write many records at once does.
     * <br>
     * Records are matched and compared in their comparable form - see {@link #getComparableRecord} - so that a
     * number the destination writes differently, such as {@code 1.50} for {@code 1.5}, is not taken for a change.
     * The values written are always the source's own.
     *
     * @param sourceConnector where the records come from
     * @param sourceRecordCount number of records in the source
     * @param destinationConnector where they go - its table has already been selected and described
     * @param destinationRecordCount number of records already in the destination
     * @param columnDescriptions the source's columns - may be null when they are not known
     * @return number of records written, appended or deleted
     * @throws ConnectorCheckedException problem reading or writing
     */
    static long copyRecords(ReadableTabularDataSource      sourceConnector,
                            long                           sourceRecordCount,
                            WritableTabularDataSource      destinationConnector,
                            long                           destinationRecordCount,
                            List<TabularColumnDescription> columnDescriptions) throws ConnectorCheckedException
    {
        if (! (destinationConnector instanceof ReadableTabularDataSource readableDestination))
        {
            return overwriteRecords(sourceConnector, sourceRecordCount, destinationConnector, destinationRecordCount);
        }

        List<Integer> identifierColumnPositions = getIdentifierColumnPositions(columnDescriptions);

        /*
         * The records of each side still waiting for a partner, by identity.  More than one record can share an
         * identity when the whole record is the identity, so each identity keeps a queue of them, in the order
         * they were read.
         */
        Map<List<String>, ArrayDeque<DestinationRecord>> unmatchedDestinationRecords = new HashMap<>();
        Map<List<String>, ArrayDeque<SourceRecord>>      unmatchedSourceRecords      = new HashMap<>();

        /*
         * Only the records that need writing are kept beyond the unmatched ones.
         */
        Map<Long, List<String>> changedRecords = new TreeMap<>();

        long sourceRowNumber      = 0;
        long destinationRowNumber = 0;

        while ((sourceRowNumber < sourceRecordCount) || (destinationRowNumber < destinationRecordCount))
        {
            SourceRecord      sourceRecord        = null;
            List<String>      sourceIdentity      = null;
            DestinationRecord destinationRecord   = null;
            List<String>      destinationIdentity = null;

            if (sourceRowNumber < sourceRecordCount)
            {
                List<String> dataValues = sourceConnector.readRecord(sourceRowNumber);

                sourceRecord   = new SourceRecord(sourceRowNumber, dataValues, getComparableRecord(dataValues, columnDescriptions));
                sourceIdentity = getRecordIdentity(sourceRecord.comparableValues(), identifierColumnPositions, columnDescriptions);
                sourceRowNumber++;
            }

            if (destinationRowNumber < destinationRecordCount)
            {
                destinationRecord   = new DestinationRecord(destinationRowNumber,
                                                            getComparableRecord(readableDestination.readRecord(destinationRowNumber), columnDescriptions));
                destinationIdentity = getRecordIdentity(destinationRecord.dataValues(), identifierColumnPositions, columnDescriptions);
                destinationRowNumber++;
            }

            if ((sourceRecord != null) && (destinationRecord != null) && (Objects.equals(sourceIdentity, destinationIdentity))
                        && (! unmatchedDestinationRecords.containsKey(destinationIdentity))
                        && (! unmatchedSourceRecords.containsKey(sourceIdentity)))
            {
                /*
                 * The usual case: the two sides are level with each other.
                 */
                matchRecords(sourceRecord, destinationRecord, columnDescriptions, changedRecords);
                continue;
            }

            if (sourceRecord != null)
            {
                DestinationRecord partner = takeFirst(unmatchedDestinationRecords, sourceIdentity);

                if (partner == null)
                {
                    unmatchedSourceRecords.computeIfAbsent(sourceIdentity, identity -> new ArrayDeque<>()).add(sourceRecord);
                }
                else
                {
                    matchRecords(sourceRecord, partner, columnDescriptions, changedRecords);
                }
            }

            if (destinationRecord != null)
            {
                SourceRecord partner = takeFirst(unmatchedSourceRecords, destinationIdentity);

                if (partner == null)
                {
                    unmatchedDestinationRecords.computeIfAbsent(destinationIdentity, identity -> new ArrayDeque<>()).add(destinationRecord);
                }
                else
                {
                    matchRecords(partner, destinationRecord, columnDescriptions, changedRecords);
                }
            }
        }

        /*
         * Every destination record left unmatched is not in the source any more, and every source record left
         * unmatched is new.
         */
        List<Long> deletedRowNumbers = new ArrayList<>();

        for (ArrayDeque<DestinationRecord> unmatched : unmatchedDestinationRecords.values())
        {
            for (DestinationRecord unmatchedRecord : unmatched)
            {
                deletedRowNumbers.add(unmatchedRecord.rowNumber());
            }
        }

        Collections.sort(deletedRowNumbers);

        List<SourceRecord> newSourceRecords = new ArrayList<>();

        for (ArrayDeque<SourceRecord> unmatched : unmatchedSourceRecords.values())
        {
            newSourceRecords.addAll(unmatched);
        }

        newSourceRecords.sort(Comparator.comparingLong(SourceRecord::rowNumber));

        List<List<String>> newRecords = new ArrayList<>(newSourceRecords.size());

        for (SourceRecord newSourceRecord : newSourceRecords)
        {
            newRecords.add(newSourceRecord.dataValues());
        }

        /*
         * Each changed record has moved up by one for each deleted record that was in front of it.
         */
        Map<Long, List<String>> overwrittenRecords = new TreeMap<>();

        for (Map.Entry<Long, List<String>> changedRecord : changedRecords.entrySet())
        {
            int deletedInFront = Collections.binarySearch(deletedRowNumbers, changedRecord.getKey());

            overwrittenRecords.put(changedRecord.getKey() - (-deletedInFront - 1), changedRecord.getValue());
        }

        if (! deletedRowNumbers.isEmpty())
        {
            destinationConnector.deleteRecords(deletedRowNumbers);
        }

        if (! overwrittenRecords.isEmpty())
        {
            destinationConnector.writeRecords(overwrittenRecords);
        }

        if (! newRecords.isEmpty())
        {
            destinationConnector.appendRecords(newRecords);
        }

        return deletedRowNumbers.size() + changedRecords.size() + newRecords.size();
    }


    /**
     * Make a destination that cannot be read back hold the source's records, by writing every one of them.
     *
     * @param sourceConnector where the records come from
     * @param sourceRecordCount number of records in the source
     * @param destinationConnector where they go
     * @param destinationRecordCount number of records already in the destination
     * @return number of records written, appended or deleted
     * @throws ConnectorCheckedException problem reading or writing
     */
    private static long overwriteRecords(ReadableTabularDataSource sourceConnector,
                                         long                      sourceRecordCount,
                                         WritableTabularDataSource destinationConnector,
                                         long                      destinationRecordCount) throws ConnectorCheckedException
    {
        long changedRecordCount = 0;
        long overlappingRecords = Math.min(sourceRecordCount, destinationRecordCount);

        for (long rowNumber = 0; rowNumber < overlappingRecords; rowNumber++)
        {
            destinationConnector.writeRecord(rowNumber, sourceConnector.readRecord(rowNumber));
            changedRecordCount++;
        }

        for (long rowNumber = destinationRecordCount; rowNumber < sourceRecordCount; rowNumber++)
        {
            destinationConnector.appendRecord(sourceConnector.readRecord(rowNumber));
            changedRecordCount++;
        }

        /*
         * Records beyond the end of the source are removed from the end backwards, so the row numbers still to
         * be deleted are not moved by each deletion.
         */
        for (long rowNumber = destinationRecordCount - 1; rowNumber >= sourceRecordCount; rowNumber--)
        {
            destinationConnector.deleteRecord(rowNumber);
            changedRecordCount++;
        }

        return changedRecordCount;
    }


    /**
     * Return the values that identify a record: those of its identifying columns, or the whole record when
     * there are none.  A point in time with an offset is identified by the instant it names, written in UTC, so
     * that the same instant identifies the same record whichever offset each side writes it with.
     *
     * @param dataValues the record, in its comparable form
     * @param identifierColumnPositions positions of the identifying columns
     * @param columnDescriptions the source's columns - may be null
     * @return identity
     */
    private static List<String> getRecordIdentity(List<String>                   dataValues,
                                                  List<Integer>                  identifierColumnPositions,
                                                  List<TabularColumnDescription> columnDescriptions)
    {
        if (dataValues == null)
        {
            return null;
        }

        List<String> identity = new ArrayList<>();

        if (identifierColumnPositions.isEmpty())
        {
            for (int columnPosition = 0; columnPosition < dataValues.size(); columnPosition++)
            {
                identity.add(getIdentityValue(dataValues, columnPosition, columnDescriptions));
            }
        }
        else
        {
            for (int columnPosition : identifierColumnPositions)
            {
                identity.add(getIdentityValue(dataValues, columnPosition, columnDescriptions));
            }
        }

        return identity;
    }


    /**
     * Return one value of a record's identity.
     *
     * @param dataValues the record, in its comparable form
     * @param columnPosition position of the value
     * @param columnDescriptions the source's columns - may be null
     * @return value
     */
    private static String getIdentityValue(List<String>                   dataValues,
                                           int                            columnPosition,
                                           List<TabularColumnDescription> columnDescriptions)
    {
        String dataValue = (columnPosition < dataValues.size()) ? dataValues.get(columnPosition) : null;

        if ((dataValue != null) && (isDate(columnDescriptions, columnPosition)))
        {
            OffsetDateTime instant = parseOffsetDateTime(dataValue);

            if (instant != null)
            {
                return instant.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime().toString();
            }
        }

        return dataValue;
    }


    /**
     * Return whether a source record, and the destination record with the same identity, hold the same values.
     * Both are in their comparable form, so numbers already read alike; points in time are compared as
     * described in {@link #isSameTime}.
     *
     * @param sourceRecord the source's record
     * @param destinationRecord the destination's record
     * @param columnDescriptions the source's columns - may be null
     * @return true if nothing needs writing
     */
    static boolean recordsMatch(List<String>                   sourceRecord,
                                List<String>                   destinationRecord,
                                List<TabularColumnDescription> columnDescriptions)
    {
        if (Objects.equals(sourceRecord, destinationRecord))
        {
            return true;
        }

        if ((sourceRecord == null) || (destinationRecord == null) || (sourceRecord.size() != destinationRecord.size()))
        {
            return false;
        }

        for (int columnPosition = 0; columnPosition < sourceRecord.size(); columnPosition++)
        {
            String sourceValue      = sourceRecord.get(columnPosition);
            String destinationValue = destinationRecord.get(columnPosition);

            if (! Objects.equals(sourceValue, destinationValue))
            {
                if ((sourceValue == null) || (destinationValue == null) || (! isDate(columnDescriptions, columnPosition))
                            || (! isSameTime(sourceValue, destinationValue)))
                {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Return whether two points in time, in their comparable form, are the same.
     * <ul>
     *     <li>When both carry an offset they are the same if they name the same instant: a destination writes
     *         an instant with its own session's offset, so {@code 11:20+05:30} comes back as
     *         {@code 05:50+00}.</li>
     *     <li>When only one carries an offset they are the same if they show the same date and time.  A value
     *         without an offset that is written into a column with a time zone is taken to be in the session's
     *         time zone and comes back in it, with that offset added, so its date and time are kept.  Going the
     *         other way, a column without a time zone keeps the date and time of a value and drops its
     *         offset.</li>
     *     <li>When neither carries an offset they are the same if they show the same date and time.  They
     *         would already have compared equal unless one of them was written differently - a date that came
     *         back as midnight on that date, for example.</li>
     * </ul>
     *
     * @param sourceValue the source's value
     * @param destinationValue the destination's value
     * @return true if they are the same point in time
     */
    private static boolean isSameTime(String sourceValue,
                                      String destinationValue)
    {
        OffsetDateTime sourceInstant      = parseOffsetDateTime(sourceValue);
        OffsetDateTime destinationInstant = parseOffsetDateTime(destinationValue);

        if ((sourceInstant != null) && (destinationInstant != null))
        {
            return sourceInstant.isEqual(destinationInstant);
        }

        LocalDateTime sourceDateTime      = (sourceInstant != null) ? sourceInstant.toLocalDateTime() : parseLocalDateTime(sourceValue);
        LocalDateTime destinationDateTime = (destinationInstant != null) ? destinationInstant.toLocalDateTime() : parseLocalDateTime(destinationValue);

        return (sourceDateTime != null) && (sourceDateTime.equals(destinationDateTime));
    }


    /**
     * Return whether a column holds dates or times.
     *
     * @param columnDescriptions the source's columns - may be null
     * @param columnPosition position of the column
     * @return true for a date or time column
     */
    private static boolean isDate(List<TabularColumnDescription> columnDescriptions,
                                  int                            columnPosition)
    {
        return (columnDescriptions != null) && (columnPosition < columnDescriptions.size())
                       && (columnDescriptions.get(columnPosition) != null)
                       && (isDate(columnDescriptions.get(columnPosition).columnDataType()));
    }


    /**
     * Return whether a data type holds dates or times: a point in time, a date and time without a time zone,
     * or a calendar date.  Their values are compared the same way.  A destination created from the source's
     * columns holds the same kind, so most values compare equal as written; the rules in {@link #isSameTime}
     * cover a destination that holds a different kind - one created before the kinds were told apart, say.
     *
     * @param dataType the column's type - may be null
     * @return true for the date and time types
     */
    private static boolean isDate(DataType dataType)
    {
        return (dataType == DataType.DATE) || (dataType == DataType.LOCAL_DATE_TIME) || (dataType == DataType.LOCAL_DATE);
    }


    /**
     * Return a record in the form it is matched and compared in.  The value of a numeric column is written as
     * its number in plain notation with no trailing zeros, so the same number reads the same whichever side it
     * came from: a destination column with a fixed scale returns {@code 1.50} for a source's {@code 1.5}, and
     * one holding text keeps whatever form the value arrived in.  Without this, every such record looked
     * changed on every delivery, and the destination was rewritten to no effect.
     * <br>
     * The value of a date column is written in ISO form - a date and time, with its offset when it has one - so
     * that the many ways of writing a point in time ({@code 2026-06-15 11:20:00+00}, {@code 2026-06-15T11:20Z})
     * read alike, and a date on its own reads as midnight on that date.  Whether two of them are the same point
     * in time is then decided by {@link #isSameTime}, since a destination may write the same instant with a
     * different offset.
     * <br>
     * A value that does not parse as a number or a point in time is left as it is, as is every value of a column
     * of another type, or of any column when the columns are not known.
     *
     * @param dataValues the record - may be null
     * @param columnDescriptions the source's columns - may be null
     * @return the record to compare; the original when nothing needs changing
     */
    static List<String> getComparableRecord(List<String>                   dataValues,
                                            List<TabularColumnDescription> columnDescriptions)
    {
        if ((dataValues == null) || (columnDescriptions == null))
        {
            return dataValues;
        }

        List<String> comparableRecord = null;

        for (int columnNumber = 0; (columnNumber < dataValues.size()) && (columnNumber < columnDescriptions.size()); columnNumber++)
        {
            TabularColumnDescription columnDescription = columnDescriptions.get(columnNumber);
            String                   dataValue         = dataValues.get(columnNumber);
            String                   comparableValue   = null;

            if ((dataValue != null) && (columnDescription != null))
            {
                if (isNumeric(columnDescription.columnDataType()))
                {
                    comparableValue = getComparableNumber(dataValue);
                }
                else if (isDate(columnDescription.columnDataType()))
                {
                    comparableValue = getComparableTime(dataValue);
                }
            }

            if (comparableValue != null)
            {
                if (! comparableValue.equals(dataValue))
                {
                    if (comparableRecord == null)
                    {
                        comparableRecord = new ArrayList<>(dataValues);
                    }

                    comparableRecord.set(columnNumber, comparableValue);
                }
            }
        }

        return (comparableRecord == null) ? dataValues : comparableRecord;
    }


    /**
     * Return whether a column holds numbers.
     *
     * @param dataType the column's type - may be null
     * @return true for the numeric types
     */
    private static boolean isNumeric(DataType dataType)
    {
        return (dataType == DataType.INT)        || (dataType == DataType.LONG)
            || (dataType == DataType.SHORT)      || (dataType == DataType.BYTE)
            || (dataType == DataType.BIGINTEGER) || (dataType == DataType.BIGDECIMAL)
            || (dataType == DataType.FLOAT)      || (dataType == DataType.DOUBLE);
    }


    /**
     * Return a number in plain notation with no trailing zeros, or the value as it is when it is not a number
     * that can be written that way - {@code NaN} and {@code Infinity}, for example.
     *
     * @param dataValue the value
     * @return comparable form
     */
    private static String getComparableNumber(String dataValue)
    {
        try
        {
            return new BigDecimal(dataValue.trim()).stripTrailingZeros().toPlainString();
        }
        catch (NumberFormatException notANumber)
        {
            return dataValue;
        }
    }


    /*
     * Reads a point in time as databases write it: a date, optionally followed - after a space or a T - by a
     * time with any fraction of a second, optionally followed by an offset such as +00, +05:30 or Z.
     */
    private static final DateTimeFormatter LENIENT_TIME_FORMAT = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .optionalStart()
                .optionalStart().appendLiteral(' ').optionalEnd()
                .optionalStart().appendLiteral('T').optionalEnd()
                .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .optionalEnd()
            .optionalStart().appendOffset("+HH:mm", "Z").optionalEnd()
            .toFormatter();


    /**
     * Return a point in time in ISO form - with its offset when it has one - or the value as it is when it
     * cannot be read as a point in time ({@code infinity}, for example).
     *
     * @param dataValue the value
     * @return comparable form
     */
    private static String getComparableTime(String dataValue)
    {
        try
        {
            TemporalAccessor parsed   = LENIENT_TIME_FORMAT.parse(dataValue.trim());
            LocalDate        date     = LocalDate.from(parsed);
            LocalTime        time     = parsed.isSupported(ChronoField.HOUR_OF_DAY) ? LocalTime.from(parsed) : LocalTime.MIDNIGHT;
            LocalDateTime    dateTime = LocalDateTime.of(date, time);

            if (parsed.isSupported(ChronoField.OFFSET_SECONDS))
            {
                return OffsetDateTime.of(dateTime, ZoneOffset.from(parsed)).toString();
            }

            return dateTime.toString();
        }
        catch (DateTimeException notATime)
        {
            return dataValue;
        }
    }


    /**
     * Return a point in time, in its comparable form, as an instant - or null if it has no offset or is not a
     * point in time.
     *
     * @param comparableValue value from {@link #getComparableTime}
     * @return instant or null
     */
    private static OffsetDateTime parseOffsetDateTime(String comparableValue)
    {
        try
        {
            return OffsetDateTime.parse(comparableValue);
        }
        catch (DateTimeException noOffset)
        {
            return null;
        }
    }


    /**
     * Return a point in time without an offset, in its comparable form, as a date and time - or null if it is
     * not one.
     *
     * @param comparableValue value from {@link #getComparableTime}
     * @return date and time or null
     */
    private static LocalDateTime parseLocalDateTime(String comparableValue)
    {
        try
        {
            return LocalDateTime.parse(comparableValue);
        }
        catch (DateTimeException notATime)
        {
            return null;
        }
    }


    /**
     * A source record and the row it is at.
     *
     * @param rowNumber position of the record in the source
     * @param dataValues the record's values, as they are written
     * @param comparableValues the record's values in comparable form, as they are matched
     */
    private record SourceRecord(long rowNumber, List<String> dataValues, List<String> comparableValues)
    {
    }


    /**
     * Note that a source record and the destination record with the same identity are partners, and keep the
     * source's values for writing at the destination's position if the two differ.
     *
     * @param sourceRecord the source's record
     * @param destinationRecord its partner in the destination, in comparable form
     * @param columnDescriptions the source's columns - may be null
     * @param changedRecords destination row number to the source values to write there
     */
    private static void matchRecords(SourceRecord                   sourceRecord,
                                     DestinationRecord              destinationRecord,
                                     List<TabularColumnDescription> columnDescriptions,
                                     Map<Long, List<String>>        changedRecords)
    {
        if (! recordsMatch(sourceRecord.comparableValues(), destinationRecord.dataValues(), columnDescriptions))
        {
            changedRecords.put(destinationRecord.rowNumber(), sourceRecord.dataValues());
        }
    }


    /**
     * Remove and return the first record waiting with the requested identity, dropping the identity once no
     * record is waiting with it so that the map holds only what is unmatched.
     *
     * @param unmatchedRecords records waiting for a partner, by identity
     * @param identity identity wanted
     * @return record, or null if none is waiting
     * @param <T> kind of record
     */
    private static <T> T takeFirst(Map<List<String>, ArrayDeque<T>> unmatchedRecords,
                                   List<String>                     identity)
    {
        ArrayDeque<T> candidates = unmatchedRecords.get(identity);

        if (candidates == null)
        {
            return null;
        }

        T candidate = candidates.poll();

        if (candidates.isEmpty())
        {
            unmatchedRecords.remove(identity);
        }

        return candidate;
    }


    /**
     * A record in the destination and the row it is at.
     *
     * @param rowNumber position of the record when the delivery began
     * @param dataValues the record's values, in comparable form
     */
    private record DestinationRecord(long rowNumber, List<String> dataValues)
    {
    }


    /**
     * Record the lineage of the tables delivered.  The data has been delivered by the time this is called, so a
     * failure here is logged and does not change the outcome of the delivery.
     *
     * @param sourceAssetGUID source data set
     * @param destinationAssetGUID destination data set
     * @param deliveredTables source table name mapped to destination table name
     */
    private void recordLineage(String              sourceAssetGUID,
                               String              destinationAssetGUID,
                               Map<String, String> deliveredTables)
    {
        final String methodName = "recordLineage";

        try
        {
            TabularDataSetProvisioningLineage lineage = new TabularDataSetProvisioningLineage(governanceContext,
                                                                                              this.getClass().getName());

            lineage.setTopLevelProcessName(topLevelProcessName);
            lineage.setTopLevelProcessTemplateQualifiedName(topLevelProcessTemplateQualifiedName);
            lineage.setInformationSupplyChainQualifiedName(informationSupplyChainQualifiedName);
            lineage.setChildProcessLineage(childProcessLineage);
            lineage.setColumnLevelLineage(columnLevelLineage);

            TabularDataSetProvisioningLineage.LineageSummary summary = lineage.createLineage(sourceAssetGUID,
                                                                                             destinationAssetGUID,
                                                                                             connectorInstanceId,
                                                                                             deliveredTables);

            super.logRecord(methodName,
                            GovernanceActionConnectorsAuditCode.PROVISIONING_LINEAGE_CREATED.getMessageDefinition(governanceServiceName,
                                                                                                                   sourceAssetGUID,
                                                                                                                   summary.processGUID(),
                                                                                                                   destinationAssetGUID,
                                                                                                                   Integer.toString(summary.columnMappingCount())));
        }
        catch (Exception error)
        {
            super.logExceptionRecord(methodName,
                                     GovernanceActionConnectorsAuditCode.PROVISIONING_LINEAGE_FAILED.getMessageDefinition(governanceServiceName,
                                                                                                                           sourceAssetGUID,
                                                                                                                           destinationAssetGUID,
                                                                                                                           error.getClass().getName(),
                                                                                                                           error.getMessage()),
                                     error);
        }
    }


    /**
     * Return a name to call an element by in the log: its display name if it has one, otherwise its GUID.
     *
     * @param element the element
     * @param methodName calling method
     * @return name
     */
    private String getElementName(OpenMetadataElement element,
                                  String              methodName)
    {
        String displayName = propertyHelper.getStringProperty(governanceServiceName,
                                                              OpenMetadataProperty.DISPLAY_NAME.name,
                                                              element.getElementProperties(),
                                                              methodName);

        if (displayName != null)
        {
            return displayName + " (" + element.getElementGUID() + ")";
        }

        return element.getElementGUID();
    }


    /**
     * Disconnect a connector, ignoring any complaint it makes on the way out.
     *
     * @param connector connector to disconnect - may be null
     */
    private void disconnectQuietly(Connector connector)
    {
        if (connector != null)
        {
            try
            {
                connector.disconnect();
            }
            catch (Exception error)
            {
                // the work is done; the connector's complaint on closing changes nothing
            }
        }
    }
}
