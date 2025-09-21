/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager;


import org.odpi.openmetadata.adapters.connectors.productmanager.ffdc.ProductManagerAuditCode;
import org.odpi.openmetadata.adapters.connectors.productmanager.ffdc.ProductManagerErrorCode;
import org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.ReferenceDataSetConnectorBase;
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
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceMeasurementsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Calculates the last time an update was made to the tabular data set that is the target and if it has changes since
 * the last refresh (or this is the first refresh), the GovernanceMeasurements classification is updated with the latest update time.
 * This will be detected as a change to the catalog target by any monitoring process.
 */
public class OpenMetadataProductsHarvesterCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    private ReferenceDataSetConnectorBase tabularDataSource = null;


    /**
     * Constructor
     *
     * @param catalogTarget catalog target information
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     * @throws ConnectorCheckedException error
     * @throws UserNotAuthorizedException connector is disconnected
     */
    public OpenMetadataProductsHarvesterCatalogTargetProcessor(CatalogTarget            catalogTarget,
                                                               CatalogTargetContext     catalogTargetContext,
                                                               Connector                connectorToTarget,
                                                               String                   connectorName,
                                                               AuditLog                 auditLog) throws ConnectorCheckedException,
                                                                                                         UserNotAuthorizedException
    {
        super(catalogTarget, catalogTargetContext, connectorToTarget, connectorName, auditLog);

        if (super.getConnectorToTarget() instanceof ReferenceDataSetConnectorBase readableTabularDataSource)
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
     * Check whether the data set has changed since the las refresh.  If it has then update the asset's
     * GovernanceMeasurement classification.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        try
        {
            /*
             * Make sure the data source has the latest information.
             */
            tabularDataSource.refreshCache();

            /*
             * Locate the lastUpdate column, no action is taken if the last update column is not present in the
             * data set.
             */
            int lastUpdateColumnNumber = tabularDataSource.getColumnNumber(OpenMetadataProperty.UPDATE_TIME.name);

            if (lastUpdateColumnNumber < 0)
            {
                auditLog.logMessage(methodName,
                                    ProductManagerAuditCode.NO_LAST_UPDATE_DATE.getMessageDefinition(connectorName, getCatalogTargetName()));
            }
            else
            {
                /*
                 * Extract the last update time for all records in the data set saved the last time the data set was scanned.
                 */
                Date createTime     = null;
                Date lastUpdateTime = null;

                AssetClient             dataSetClient = integrationContext.getAssetClient(OpenMetadataType.DATA_SET.typeName);
                OpenMetadataRootElement dataSet       = dataSetClient.getAssetByGUID(getCatalogTargetElement().getGUID(), null);

                if (dataSet.getElementHeader().getGovernanceMeasurements() != null)
                {
                    if (dataSet.getElementHeader().getGovernanceMeasurements().getClassificationProperties() instanceof GovernanceMeasurementsProperties governanceMeasurementsProperties)
                    {
                        createTime = governanceMeasurementsProperties.getDates().get(OpenMetadataProperty.CREATE_TIME.name);

                        if (governanceMeasurementsProperties.getDates() != null)
                        {
                            lastUpdateTime = governanceMeasurementsProperties.getDates().get(OpenMetadataProperty.UPDATE_TIME.name);

                            if (lastUpdateTime == null)
                            {
                                lastUpdateTime = governanceMeasurementsProperties.getDates().get(OpenMetadataProperty.CREATE_TIME.name);
                            }
                        }
                    }
                }

                Date dataSetCreateTime     = dataSet.getElementHeader().getVersions().getCreateTime();
                Date dataSetLastUpdateTime = getDataSetLastUpdateTime(lastUpdateColumnNumber);

                if ((createTime == null) || (lastUpdateTime == null) ||
                        ((dataSetLastUpdateTime != null) && (dataSetLastUpdateTime.after(lastUpdateTime))))
                {
                    /*
                     * The data set has changed (or this is the first time it has been monitored).  Details of
                     * the creation and last update times are saved in the data set's GovernanceMeasurements
                     * classification.
                     */
                    ClassificationManagerClient classificationManagerClient = integrationContext.getClassificationManagerClient(OpenMetadataType.ASSET.typeName);

                    if (dataSet.getElementHeader().getGovernanceMeasurements() == null)
                    {
                        /*
                         * First time thorough - no GovernanceMeasurements classification set up.
                         */
                        GovernanceMeasurementsProperties governanceMeasurementsProperties = new GovernanceMeasurementsProperties();
                        Map<String, Date> dates = new HashMap<>();

                        dates.put(OpenMetadataProperty.CREATE_TIME.name, dataSetCreateTime);
                        dates.put(OpenMetadataProperty.UPDATE_TIME.name, dataSetLastUpdateTime);

                        governanceMeasurementsProperties.setDataCollectionStartTime(new Date());
                        governanceMeasurementsProperties.setDates(dates);
                        classificationManagerClient.addGovernanceMeasurements(dataSet.getElementHeader().getGUID(),
                                                                              governanceMeasurementsProperties,
                                                                              integrationContext.getOpenMetadataStore().getMetadataSourceOptions());
                    }
                    else
                    {
                        /*
                         * Update the existing governance measurements classification.
                         */
                        GovernanceMeasurementsProperties governanceMeasurementsProperties;

                        if (dataSet.getElementHeader().getGovernanceMeasurements().getClassificationProperties() instanceof GovernanceMeasurementsProperties properties)
                        {
                            /*
                             * The copy/clone constructor is used to preserve any measurements from other processes.
                             */
                            governanceMeasurementsProperties = new GovernanceMeasurementsProperties(properties);

                            if (governanceMeasurementsProperties.getDataCollectionStartTime() == null)
                            {
                                /*
                                 * The start time might not be set if the classification was created by another process.
                                 */
                                governanceMeasurementsProperties.setDataCollectionStartTime(new Date());
                            }
                        }
                        else
                        {
                            /*
                             * This is unexpected - suggests that another process created the classification,
                             * but without any properties.
                             */
                            governanceMeasurementsProperties = new GovernanceMeasurementsProperties();

                            governanceMeasurementsProperties.setDataCollectionStartTime(new Date());
                        }

                        /*
                         * Preserve any other dates saved by different processes.
                         */
                        Map<String, Date> dates = governanceMeasurementsProperties.getDates();

                        if (dates == null)
                        {
                            /*
                             * Nothing else saved.
                             */
                            dates = new HashMap<>();
                        }

                        dates.put(OpenMetadataProperty.CREATE_TIME.name, dataSetCreateTime);
                        dates.put(OpenMetadataProperty.UPDATE_TIME.name, dataSetLastUpdateTime);

                        governanceMeasurementsProperties.setDates(dates);

                        classificationManagerClient.updateGovernanceMeasurements(dataSet.getElementHeader().getGUID(),
                                                                                 governanceMeasurementsProperties,
                                                                                 integrationContext.getOpenMetadataStore().getUpdateOptions(true));
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  ProductManagerAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                    error.getClass().getName(),
                                                                                                    methodName,
                                                                                                    error.getMessage()),
                                  error);


            throw new ConnectorCheckedException(ProductManagerErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }



    /**
     * Extract the date of the last update to the data set.
     *
     * @param lastUpdateColumnNumber column to check
     * @return date (or null if no date has been detected - typically because the data set is empty)
     * @throws ConnectorCheckedException problem accessing the data set
     * @throws ParseException problem parsing the value stored in the data set
     */
    private Date getDataSetLastUpdateTime(int lastUpdateColumnNumber) throws ConnectorCheckedException,
                                                                             ParseException
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
