/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets;


import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataAuditCode;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationManagerClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DynamicOpenMetadataDataSetConnector is the base class for the dynamic tabular data set to allow the provider
 * to pass the product definition to the open metadata repository.
 */
public abstract class OpenMetadataRootDataSetConnectorBase extends OpenMetadataDataSetConnectorBase
{
    protected final Map<Long, OpenMetadataRootElement> records = new HashMap<>();

    /**
     * Constructor used to set up the name of this connector (supplied by the subclasses).
     *
     * @param connectorName name of the connector
     */
    public OpenMetadataRootDataSetConnectorBase(String connectorName)
    {
        super(connectorName);
    }


    /**
     * Constructor used to set up the name of this connector (supplied by the subclasses).
     *
     * @param connectorName name of the connector
     */
    public OpenMetadataRootDataSetConnectorBase(String            connectorName,
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
            ClassificationManagerClient classificationManagerClient = connectorContext.getClassificationManagerClient();

            List<String> knownGUIDs = new ArrayList<>();

            for (long recordNumber : records.keySet())
            {
                OpenMetadataRootElement existingValue = records.get(recordNumber);

                try
                {
                    existingValue = classificationManagerClient.getRootElementByGUID(existingValue.getElementHeader().getGUID(),
                                                                                     classificationManagerClient.getGetOptions());

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
                    knownGUIDs.add(existingValue.getElementHeader().getGUID());
                    records.put(recordNumber, existingValue);

                    processNestedValues(existingValue, knownGUIDs);
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
     * Process any nested records.
     *
     * @param element retrieved element
     * @param knownGUIDs list of records already processed
     */
    protected void processNestedValues(OpenMetadataRootElement element,
                                       List<String>            knownGUIDs)
    {
        // the default behaviour is to ignore nested records
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

            ClassificationManagerClient classificationManagerClient = connectorContext.getClassificationManagerClient(metadataTypeName);

            SearchOptions searchOptions = classificationManagerClient.getSearchOptions(startFrom, pageSize);

            /*
             * Retrieving by creation date/time ensure the order is preserved.
             */
            searchOptions.setSequencingOrder(SequencingOrder.CREATION_DATE_OLDEST);

            /*
             * Retrieve all the reference data sets and add them to the records.
             */
            List<OpenMetadataRootElement> rootElements = classificationManagerClient.findRootElements(null, searchOptions);

            while (rootElements != null)
            {
                for (OpenMetadataRootElement rootElement : rootElements)
                {
                    if ((rootElement != null) && (!knownGUIDs.contains(rootElement.getElementHeader().getGUID())))
                    {
                        records.put((long) records.size(), rootElement);
                        knownGUIDs.add(rootElement.getElementHeader().getGUID());
                        processNestedValues(rootElement, knownGUIDs);
                    }
                }

                searchOptions.setStartFrom(searchOptions.getStartFrom() + pageSize);
                rootElements = classificationManagerClient.findRootElements(null, searchOptions);
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
            OpenMetadataRootElement validValue = records.get(rowNumber);

            return getRecordValues(validValue);
        }

        throw new ConnectorCheckedException(TabularDataErrorCode.NULL_RECORD.getMessageDefinition(connectorName,
                                                                                                  Long.toString(rowNumber),
                                                                                                  Integer.toString(records.size())),
                                            this.getClass().getName(),
                                            methodName);
    }
}
