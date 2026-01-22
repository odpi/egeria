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
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationManagerClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;


/**
 * Calculates the last time an update was made to the tabular data set that is the target and if it has changes since
 * the last refresh (or this is the first refresh), the DataScope classification is updated with the latest update time.
 * This will be detected as a change to the catalog target by any monitoring process.
 */
public class JacquardCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    private OpenMetadataDataSetConnectorBase tabularDataSource = null;


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
            this.tabularDataSource.start();
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
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        try
        {
            if (tabularDataSource != null)
            {
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
                    OpenMetadataRootElement dataSet       = dataSetClient.getAssetByGUID(getCatalogTargetElement().getElementHeader().getGUID(), null);

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
                        /*
                         * The data set has changed (or this is the first time it has been monitored).  Details of
                         * the creation and last update times are saved in the data set's DataScope
                         * classification.  This will trigger the Baudot Notification Manager to send a notification.
                         */
                        ClassificationManagerClient classificationManagerClient = integrationContext.getClassificationManagerClient(OpenMetadataType.ASSET.typeName);

                        if (dataSet.getElementHeader().getDataScope() == null)
                        {
                            /*
                             * First time thorough - no DataScope classification set up.
                             */
                            DataScopeProperties dataScopeProperties = new DataScopeProperties();

                            dataScopeProperties.setDataCollectionStartTime(dataSetCreateTime);
                            dataScopeProperties.setDataCollectionEndTime(dataSetLastUpdateTime);

                            classificationManagerClient.addDataScopeClassification(dataSet.getElementHeader().getGUID(),
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

                            classificationManagerClient.updateDataScopeClassification(dataSet.getElementHeader().getGUID(),
                                                                                     dataScopeProperties,
                                                                                     integrationContext.getOpenMetadataStore().getUpdateOptions(true));
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
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
     * Extract the date of the creation time of the data set.
     *
     * @param createTimeColumnNumber column to check
     * @return date (or null if no date has been detected - typically because the data set is empty)
     * @throws ConnectorCheckedException problem accessing the data set values
     */
    private Date getDataSetCreateTime(int createTimeColumnNumber) throws ConnectorCheckedException
    {
        long recordCount = tabularDataSource.getRecordCount();

        if (recordCount > 0L)
        {
            for (long rowNumber = 0; rowNumber < recordCount; rowNumber++)
            {
                List<String> recordValues = tabularDataSource.readRecord(rowNumber);

                if (recordValues != null)
                {
                    String createDateAsString = recordValues.get(createTimeColumnNumber);
                    if (createDateAsString != null)
                    {
                        return new Date(Long.parseLong(createDateAsString));
                    }
                }
            }
        }

        return null;
    }


    /**
     * Extract the date of the last update to the data set.
     *
     * @param lastUpdateColumnNumber column to check
     * @return date (or null if no date has been detected - typically because the data set is empty)
     * @throws ConnectorCheckedException problem accessing the data set values
     */
    private Date getDataSetLastUpdateTime(int lastUpdateColumnNumber) throws ConnectorCheckedException
    {
        long recordCount = tabularDataSource.getRecordCount();

        if (recordCount > 0L)
        {
            for (long rowNumber = 0; rowNumber < recordCount; rowNumber++)
            {
                List<String> recordValues = tabularDataSource.readRecord(rowNumber);

                if (recordValues != null)
                {
                    String lastUpdateDateAsString = recordValues.get(lastUpdateColumnNumber);
                    if (lastUpdateDateAsString != null)
                    {
                        return new Date(Long.parseLong(lastUpdateDateAsString));
                    }
                }
            }
        }

        return null;
    }
}
