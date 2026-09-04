/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard;


import org.odpi.openmetadata.adapters.connectors.jacquard.ffdc.JacquardAuditCode;
import org.odpi.openmetadata.adapters.connectors.jacquard.ffdc.JacquardErrorCode;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Calculates the last time an update was made to the tabular data set that is the target and if it has changed since
 * the last refresh (or this is the first refresh), the DataScope classification is updated with the latest update time.
 * This will be detected as a change to the catalog target by any monitoring process.
 */
public class JacquardCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    private OpenMetadataDataSetConnectorBase tabularDataSource        = null;
    private boolean                          tabularDataSourceStarted = false;


    /**
     * Constructor
     *
     * @param catalogTarget catalog target information
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     * @throws ConnectorCheckedException error
     * @throws UserNotAuthorizedException the connector is disconnected
     */
    public JacquardCatalogTargetProcessor(CatalogTarget            catalogTarget,
                                          CatalogTargetContext     catalogTargetContext,
                                          Connector                connectorToTarget,
                                          String                   connectorName,
                                          AuditLog                 auditLog) throws ConnectorCheckedException,
                                                                                                         UserNotAuthorizedException
    {
        super(catalogTarget, catalogTargetContext, connectorToTarget, connectorName, auditLog);

        if (super.getConnectorToTarget() instanceof OpenMetadataDataSetConnectorBase readableTabularDataSource)
        {
            this.tabularDataSource = readableTabularDataSource;
            this.tabularDataSource.setLocalEnvironment(catalogTargetContext.getMyUserId(),
                                                       catalogTargetContext.getLocalServerName(),
                                                       connectorName);

            /*
             * The data source is not started here - see start().
             */
        }
    }


    /**
     * Start the processor, and with it the connector to the product's data set.
     * <br><br>
     * The targets manager starts every new processor while it is still retrieving the catalog targets, before
     * the connector's refresh has done anything else.  Starting the data source opens a connection to the
     * platform named in the product's connection, and a product whose connection named a platform that had
     * moved failed here - and the failure, thrown, aborted the whole refresh: no other product was refreshed,
     * and the harvest that repairs such connections was never reached, so the product could never recover.
     * <br><br>
     * A failure to start the data source is therefore logged against this product and kept, not thrown.  The
     * refresh tries again each cycle and, while it keeps failing, skips this product and no other.
     *
     * @throws ConnectorCheckedException a problem in the framework's own start-up
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        integrationContext.validateIsActive(methodName);

        this.startTabularDataSource(methodName);
    }


    /**
     * Start the connector to the product's data set if it is not already started, logging rather than throwing
     * if it cannot be.
     *
     * @param methodName calling method, for the log
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    private void startTabularDataSource(String methodName) throws UserNotAuthorizedException
    {
        if (! tabularDataSourceStarted)
        {
            try
            {
                super.start();
                tabularDataSourceStarted = true;
            }
            catch (UserNotAuthorizedException error)
            {
                throw error;
            }
            catch (Exception error)
            {
                integrationContext.validateIsActive(methodName);

                auditLog.logException(methodName,
                                      JacquardAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                  error.getClass().getName(),
                                                                                                  methodName + "(" + getCatalogTargetName() + ")",
                                                                                                  error.getMessage()),
                                      error);
            }
        }
    }




    /* ==============================================================================
     * Standard methods that trigger activity.
     */


    /**
     * Check whether the data set has changed since the last refresh.  If it has then update the asset's
     * DataScope classification.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is unable to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected so stop refresh processing
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        super.refresh();

        /*
         * A data source that could not be started when the processor was created - see start() - is tried
         * again now, and if it still cannot be, this product is skipped for this cycle.
         */
        this.startTabularDataSource(methodName);

        if (! tabularDataSourceStarted)
        {
            return;
        }

        try
        {
            if (tabularDataSource != null)
            {
                auditLog.logMessage(methodName, JacquardAuditCode.REFRESH_CATALOG_TARGET.getMessageDefinition(connectorName, tabularDataSource.getTableName(), getCatalogTargetName()));

                /*
                 * Make sure the data source has the latest information.
                 */
                tabularDataSource.refreshCache();

                /*
                 * Locate the lastUpdate column, no action is taken if the last update column is not present in the
                 * data set.
                 */
                int createTimeColumnNumber = tabularDataSource.getColumnNumber(ProductDataFieldDefinition.CREATE_TIME.getDisplayName());
                int lastUpdateColumnNumber = tabularDataSource.getColumnNumber(ProductDataFieldDefinition.UPDATE_TIME.getDisplayName());

                if (lastUpdateColumnNumber < 0)
                {
                    auditLog.logMessage(methodName,
                                        JacquardAuditCode.NO_LAST_UPDATE_DATE.getMessageDefinition(connectorName, getCatalogTargetName()));
                }
                else if (createTimeColumnNumber < 0)
                {
                    auditLog.logMessage(methodName,
                                        JacquardAuditCode.NO_CREATION_DATE.getMessageDefinition(connectorName, getCatalogTargetName()));
                }
                else
                {
                    /*
                     * Extract the last update time for all records in the data set saved the last time the data set was scanned.
                     */
                    Date dataScopeCreateTime     = null;
                    Date dataScopeLastUpdateTime = null;

                    AssetClient             dataSetClient = integrationContext.getAssetClient(OpenMetadataType.DATA_SET.typeName);
                    /*
                     * The catalog target is the data set, and only its header is needed - the DataScope
                     * classification is on it - so it is read with nothing attached.  A product data set's graph
                     * includes its schema, field by field.
                     */
                    GetOptions headerOnly = dataSetClient.getGetOptions();

                    headerOnly.setGraphQueryDepth(0);

                    OpenMetadataRootElement dataSet       = this.getCatalogTargetElement(headerOnly);

                    if (dataSet.getElementHeader().getDataScope() != null)
                    {
                        if (dataSet.getElementHeader().getDataScope().getClassificationProperties() instanceof DataScopeProperties dataScopeProperties)
                        {
                            dataScopeCreateTime = dataScopeProperties.getDataCollectionStartTime();
                            dataScopeLastUpdateTime = dataScopeProperties.getDataCollectionEndTime();
                        }
                    }

                    Date dataSetCreateTime     = getDataSetCreateTime(createTimeColumnNumber);
                    Date dataSetLastUpdateTime = getDataSetLastUpdateTime(lastUpdateColumnNumber);

                    if ((dataScopeCreateTime == null) || (dataScopeLastUpdateTime == null) ||
                            ((dataSetLastUpdateTime != null) && (dataSetLastUpdateTime.after(dataScopeLastUpdateTime))))
                    {
                        auditLog.logMessage(methodName, JacquardAuditCode.MAINTAINED_DATA_SCOPE.getMessageDefinition(connectorName, tabularDataSource.getTableName(), getCatalogTargetName()));

                        /*
                         * The data set has changed (or this is the first time it has been monitored).  Details of
                         * the creation and last update times are saved in the data set's DataScope
                         * classification.  This will trigger the Baudot Notification Manager to send a notification.
                         */
                        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient(OpenMetadataType.ASSET.typeName);

                        if (dataSet.getElementHeader().getDataScope() == null)
                        {
                            /*
                             * First time thorough - no DataScope classification set up.
                             */
                            DataScopeProperties dataScopeProperties = new DataScopeProperties();

                            dataScopeProperties.setDataCollectionStartTime(dataSetCreateTime);
                            dataScopeProperties.setDataCollectionEndTime(dataSetLastUpdateTime);

                            classificationExplorerClient.addDataScopeClassification(dataSet.getElementHeader().getGUID(),
                                                                                    dataScopeProperties,
                                                                                    integrationContext.getOpenMetadataStore().getMetadataSourceOptions());
                        }
                        else
                        {
                            /*
                             * Update the existing governance measurements classification.
                             */
                            DataScopeProperties dataScopeProperties;

                            if (dataSet.getElementHeader().getDataScope().getClassificationProperties() instanceof DataScopeProperties properties)
                            {
                                /*
                                 * The copy/clone constructor is used to preserve any measurements from other processes.
                                 */
                                dataScopeProperties = new DataScopeProperties(properties);
                            }
                            else
                            {
                                /*
                                 * This is unexpected - suggests that another process created the classification,
                                 * but without any properties.
                                 */
                                dataScopeProperties = new DataScopeProperties();
                            }

                            dataScopeProperties.setDataCollectionStartTime(dataSetCreateTime);
                            dataScopeProperties.setDataCollectionEndTime(dataSetLastUpdateTime);

                            classificationExplorerClient.updateDataScopeClassification(dataSet.getElementHeader().getGUID(),
                                                                                       dataScopeProperties,
                                                                                       integrationContext.getOpenMetadataStore().getUpdateOptions(true));
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            /*
             * Check that the error is not caused because the server/platform is shutting down.
             */
            integrationContext.validateIsActive(methodName);

            /*
             * OK so this is really unexpected.
             */
            auditLog.logException(methodName,
                                  JacquardAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                              error.getClass().getName(),
                                                                                              methodName,
                                                                                              error.getMessage()),
                                  error);


            throw new ConnectorCheckedException(JacquardErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                            error.getClass().getName(),
                                                                                                            methodName,
                                                                                                            error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }



    /**
     * Extract the creation time of the data set: the earliest create time of any of its records.
     *
     * @param createTimeColumnNumber column to check
     * @return date (or null if no date has been detected - typically because the data set is empty)
     * @throws ConnectorCheckedException problem accessing the data set values
     */
    private Date getDataSetCreateTime(int createTimeColumnNumber) throws ConnectorCheckedException
    {
        Date earliest = null;

        for (Date createTime : this.getColumnDates(createTimeColumnNumber))
        {
            if ((earliest == null) || (createTime.before(earliest)))
            {
                earliest = createTime;
            }
        }

        return earliest;
    }


    /**
     * Extract the time of the last update to the data set: the latest update time of any of its records.
     *
     * @param lastUpdateColumnNumber column to check
     * @return date (or null if no date has been detected - typically because the data set is empty)
     * @throws ConnectorCheckedException problem accessing the data set values
     */
    private Date getDataSetLastUpdateTime(int lastUpdateColumnNumber) throws ConnectorCheckedException
    {
        Date latest = null;

        for (Date updateTime : this.getColumnDates(lastUpdateColumnNumber))
        {
            if ((latest == null) || (updateTime.after(latest)))
            {
                latest = updateTime;
            }
        }

        return latest;
    }


    /**
     * Read one date column of every record in the data set.  A value that is not a date is logged once per
     * refresh and left out, so that one bad value does not stop the product being refreshed.
     *
     * @param columnNumber column to read
     * @return the dates found, in record order; empty if the data set is empty or the column is absent
     * @throws ConnectorCheckedException problem accessing the data set values
     */
    private List<Date> getColumnDates(int columnNumber) throws ConnectorCheckedException
    {
        final String methodName = "getColumnDates";

        List<Date> dates = new ArrayList<>();

        if (columnNumber < 0)
        {
            return dates;
        }

        long    recordCount   = tabularDataSource.getRecordCount();
        boolean badValueSeen  = false;

        for (long rowNumber = 0; rowNumber < recordCount; rowNumber++)
        {
            List<String> recordValues = tabularDataSource.readRecord(rowNumber);

            if ((recordValues != null) && (recordValues.size() > columnNumber) && (recordValues.get(columnNumber) != null))
            {
                Date date = this.parseDate(recordValues.get(columnNumber));

                if (date != null)
                {
                    dates.add(date);
                }
                else if (! badValueSeen)
                {
                    badValueSeen = true;

                    auditLog.logMessage(methodName,
                                        JacquardAuditCode.UNREADABLE_DATE_VALUE.getMessageDefinition(connectorName,
                                                                                                    recordValues.get(columnNumber),
                                                                                                    Long.toString(rowNumber),
                                                                                                    Integer.toString(columnNumber),
                                                                                                    tabularDataSource.getTableName(),
                                                                                                    getCatalogTargetName()));
                }
            }
        }

        return dates;
    }


    /**
     * Parse a date as the data set connectors write it.  They write dates as ISO-8601 instants
     * (Date.toInstant().toString(), for example 2026-04-01T06:45:49.989Z); a plain count of milliseconds since
     * the epoch is accepted too, since that is how the data spec describes the column.
     *
     * @param dateAsString the value in the record
     * @return date, or null if the value is neither form
     */
    private Date parseDate(String dateAsString)
    {
        try
        {
            return Date.from(Instant.parse(dateAsString));
        }
        catch (DateTimeParseException notAnInstant)
        {
            try
            {
                return new Date(Long.parseLong(dateAsString));
            }
            catch (NumberFormatException notMilliseconds)
            {
                return null;
            }
        }
    }
}
