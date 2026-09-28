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
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataCollection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        destinationConnector.setColumnDescriptions(sourceConnector.getColumnDescriptions());

        long destinationRecordCount = destinationConnector.getRecordCount();

        if (sourceRecordCount >= destinationRecordCount)
        {
            for (long rowNumber=0; rowNumber < destinationRecordCount ; rowNumber++)
            {
                destinationConnector.writeRecord(rowNumber, sourceConnector.readRecord(rowNumber));
            }

            for (long rowNumber = destinationRecordCount; rowNumber < sourceRecordCount ; rowNumber ++)
            {
                destinationConnector.appendRecord(sourceConnector.readRecord(rowNumber));
            }
        }
        else
        {
            for (long rowNumber=0; rowNumber < sourceRecordCount ; rowNumber++)
            {
                destinationConnector.writeRecord(rowNumber, sourceConnector.readRecord(rowNumber));
            }

            for (long rowNumber = sourceRecordCount; rowNumber < destinationRecordCount ; rowNumber ++)
            {
                destinationConnector.deleteRecord(rowNumber);
            }
        }

        super.logRecord(methodName, GovernanceActionConnectorsAuditCode.TABLE_PROVISIONED.getMessageDefinition(governanceServiceName,
                                                                                                                Long.toString(sourceRecordCount),
                                                                                                                tableName,
                                                                                                                sourceName,
                                                                                                                destinationName));

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
