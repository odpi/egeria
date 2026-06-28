/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataAuditCode;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationshipSummary;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SequencingOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenMetadataRootDataSetConnectorBase is the base class for the tabular data set built on OpenMetadataRootElements.
 * The constructor allows the provider
 * to pass the product definition to the open metadata repository.
 */
public abstract class OpenMetadataRelationshipDataSetConnectorBase extends OpenMetadataDataSetConnectorBase
{
    protected final Map<Long, MetadataRelationshipSummary> records = new HashMap<>();

    /**
     * Constructor used to set up the name of this connector (supplied by the subclasses).
     *
     * @param connectorName name of the connector
     */
    public OpenMetadataRelationshipDataSetConnectorBase(String connectorName)
    {
        super(connectorName);
    }


    /**
     * Constructor used to set up the name of this connector (supplied by the subclasses).
     *
     * @param connectorName name of the connector
     */
    public OpenMetadataRelationshipDataSetConnectorBase(String            connectorName,
                                                        ProductDefinition productDefinition)
    {
        super(connectorName, productDefinition);
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException  the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws UserNotAuthorizedException,
                               ConnectorCheckedException
    {
        super.start();

        refreshCache();
    }


    /**
     * Refresh any existing records.
     *
     * @return list of GUIDs already processed and stored in the records
     * @throws ConnectorCheckedException problem processing existing records
     */
    protected List<String> refreshExistingRecords() throws ConnectorCheckedException
    {
        final String methodName = "refreshExistingRecords";

        try
        {
            ClassificationExplorerClient classificationExplorerClient = connectorContext.getClassificationExplorerClient();

            List<String> knownGUIDs = new ArrayList<>();

            for (long recordNumber : records.keySet())
            {
                MetadataRelationshipSummary existingValue = records.get(recordNumber);

                try
                {
                    existingValue = classificationExplorerClient.getRelationshipSummaryByGUID(existingValue.getRelationshipHeader().getGUID(),
                                                                                              classificationExplorerClient.getGetOptions());

                }
                catch (Exception error)
                {
                    logRecord(methodName, TabularDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),
                                                                                                         methodName,
                                                                                                         error.getMessage()));
                }

                if (existingValue != null)
                {
                    knownGUIDs.add(existingValue.getRelationshipHeader().getGUID());
                    records.put(recordNumber, existingValue);
                }
                else
                {
                    records.remove(recordNumber);
                }
            }

            return knownGUIDs;
        }
        catch (Exception error)
        {
            super.logRecord(methodName, TabularDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName,
                                                                                                       error.getMessage()));

            throw new ConnectorCheckedException(TabularDataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Refresh any cached values.
     *
     * @param metadataTypeName name of the metadata type to refresh
     * @throws ConnectorCheckedException unable to refresh
     */
    public void refreshCache(String metadataTypeName) throws ConnectorCheckedException
    {
        String methodName = "refreshCache(" + metadataTypeName + ")";

        /*
         * Retrieve all elements of the requested type since they make up the records of the dataset.
         */
        try
        {
            List<String> knownGUIDs = refreshExistingRecords();

            int startFrom = 0;
            int pageSize  = connectorContext.getMaxPageSize();

            if (pageSize == 0)
            {
                pageSize = 100;
            }

            ClassificationExplorerClient classificationExplorerClient = connectorContext.getClassificationExplorerClient();

            SearchOptions searchOptions = classificationExplorerClient.getSearchOptions(startFrom, pageSize);

            /*
             * Retrieving by creation date/time ensure the order is preserved.
             */
            searchOptions.setSequencingOrder(SequencingOrder.CREATION_DATE_OLDEST);

            /*
             * Retrieve all the reference data sets and add them to the records.
             */
            List<MetadataRelationshipSummary> relationships = classificationExplorerClient.getRelationshipsByType(metadataTypeName, searchOptions);

            while (relationships != null)
            {
                for (MetadataRelationshipSummary metadataRelationshipSummary : relationships)
                {
                    if ((metadataRelationshipSummary != null) && (!knownGUIDs.contains(metadataRelationshipSummary.getRelationshipHeader().getGUID())))
                    {
                        records.put((long) records.size(), metadataRelationshipSummary);
                        knownGUIDs.add(metadataRelationshipSummary.getRelationshipHeader().getGUID());
                    }
                }

                searchOptions.setStartFrom(searchOptions.getStartFrom() + pageSize);
                relationships = classificationExplorerClient.getRelationshipsByType(metadataTypeName, searchOptions);
            }
        }
        catch (Exception error)
        {
            super.logRecord(methodName, TabularDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName,
                                                                                                       error.getMessage()));

            throw new ConnectorCheckedException(TabularDataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Return the record count in the data source.
     *
     * @return count
     */
    @Override
    public long getRecordCount()
    {
        return records.size();
    }


    /**
     * Return the requested data record.  The first record is record 0.
     *
     * @param rowNumber long
     * @return list of values (as strings) where each string is the value from a column.  The order is the same as the columns
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    public List<String> readRecord(long  rowNumber) throws ConnectorCheckedException
    {
        final String methodName = "readRecord";

        if (records.containsKey(rowNumber))
        {
            MetadataRelationshipSummary rootElement = records.get(rowNumber);

            return getRecordValues(rootElement);
        }

        throw new ConnectorCheckedException(TabularDataErrorCode.NULL_RECORD.getMessageDefinition(connectorName,
                                                                                                  Long.toString(rowNumber),
                                                                                                  Integer.toString(records.size())),
                                            this.getClass().getName(),
                                            methodName);
    }
}
