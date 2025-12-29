/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.productmanager.tabulardatasets.validmetadatavalues;

import org.odpi.openmetadata.adapters.connectors.productmanager.tabulardatasets.OpenMetadataDataSetConnectorBase;
import org.odpi.openmetadata.adapters.connectors.productmanager.tabulardatasets.ffdc.TabularDataAuditCode;
import org.odpi.openmetadata.adapters.connectors.productmanager.tabulardatasets.ffdc.TabularDataErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ValidValueDefinitionClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidMetadataValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * ValidValueSetListConnector maintains a tabular data set that lists the valid value sets in open metadata.
 * Valid value sets are valid value definitions with no valid value parent and one or more members.
 */
public class ValidMetadataValueSetListConnector extends OpenMetadataDataSetConnectorBase implements AuditLoggingComponent,
                                                                                                    ReadableTabularDataSource
{
    private static final String myConnectorName = "ValidMetadataValueSetListConnector";
    private static final Logger log = LoggerFactory.getLogger(ValidMetadataValueSetListConnector.class);

    private final Map<Long, String> records = new HashMap<>();

    /**
     * Default constructor
     */
    public ValidMetadataValueSetListConnector()
    {
        super(myConnectorName);
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws UserNotAuthorizedException, ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        refreshValidMetadataPropertyNameSet(methodName);
    }


    /**
     * Refresh any cached values,
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    public void refreshCache() throws ConnectorCheckedException
    {
        final String methodName = "refreshCache";

        refreshValidMetadataPropertyNameSet(methodName);
    }


    /**
     * Refresh the list of open metadata properties that have valid value lists associated with them.
     *
     * @param methodName calling method
     * @throws ConnectorCheckedException problems retrieving the valid value set.
     */
    private void refreshValidMetadataPropertyNameSet(String methodName) throws ConnectorCheckedException
    {
        /*
         * Retrieve all valid values definitions and evaluate whether each definition is a top-level valid value
         * set or not.  All top-level valid value sets are stored in the validValueSets list.
         */
        try
        {
            ValidValueDefinitionClient validValueDefinitionClient = connectorContext.getValidValueDefinitionClient(OpenMetadataType.VALID_METADATA_VALUE.typeName);

            int startFrom = 0;
            int pageSize = validValueDefinitionClient.getMaxPagingSize();

            if (pageSize == 0)
            {
                pageSize = 100;
            }

            Set<String> propertyNames = new HashSet<>();

            List<OpenMetadataRootElement> validValueDefinitions = validValueDefinitionClient.findValidValueDefinitions(null,
                                                                                                                       validValueDefinitionClient.getSearchOptions(startFrom, pageSize));

            while (validValueDefinitions != null)
            {
                for (OpenMetadataRootElement validValueDefinition : validValueDefinitions)
                {
                    if ((validValueDefinition != null) && (validValueDefinition.getProperties() instanceof ValidMetadataValueProperties validMetadataValueProperties))
                    {
                        propertyNames.add(validMetadataValueProperties.getIdentifier());
                    }
                }

                startFrom += pageSize;
                validValueDefinitions = validValueDefinitionClient.findValidValueDefinitions(null,
                                                                                             validValueDefinitionClient.getSearchOptions(startFrom, pageSize));
            }

            for (String propertyName : propertyNames)
            {
                records.put((long) records.size(), propertyName);
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
     * Return the number of records in the data source.
     *
     * @return count
     */
    @Override
    public long getRecordCount()
    {
        return records.size();
    }


    /**
     * Return the table name for this data source.  This is in canonical name format where each word in the name
     * should be capitalized, with spaces between the words.
     * This format allows easy translation between different naming conventions.
     *
     * @return string
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    @Override
    public String getTableName() throws ConnectorCheckedException
    {
        return "Valid Metadata Value Set List";
    }


    /**
     * Return the description for this data source.
     *
     * @return string
     */
    @Override
    public String getTableDescription()
    {
        return "A list of the open metadata properties that have valid values defined.";
    }


    /**
     * Return the list of column descriptions associated with this data source.  The information
     * should be sufficient to define the schema in a target data store.  This list is the default
     * column descriptions.  The caller can override them.
     *
     * @return a list of column descriptions or null if not available.
     */
    @Override
    public List<TabularColumnDescription> getColumnDescriptions()
    {
        if (columnDescriptions == null)
        {
            columnDescriptions = new ArrayList<>();

            columnDescriptions.add(new TabularColumnDescription("Row Number",
                                                                DataType.LONG,
                                                                "Identifier of the row.",
                                                                false,
                                                                true));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.PROPERTY_NAME, false, false));
        }

        return columnDescriptions;
    }


    /**
     * Return the requested data record.  The first record is record 0.
     *
     * @param rowNumber long
     * @return list of  values (as strings) where each string is the value from a column.  The order is the same as the columns
     * @throws ConnectorCheckedException there is a problem accessing the data.
     */
    public List<String> readRecord(long  rowNumber) throws ConnectorCheckedException
    {
        final String methodName = "readRecord";

        if (records.containsKey(rowNumber))
        {
            String propertyName = records.get(rowNumber);

            return getRecordValues(rowNumber, propertyName);
        }

        throw new ConnectorCheckedException(TabularDataErrorCode.NULL_RECORD.getMessageDefinition(connectorName,
                                                                                                  Long.toString(rowNumber),
                                                                                                  Integer.toString(records.size())),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Convert the valid value to a list of column values.
     *
     * @param rowNumber number of row
     * @param propertyName name of property
     * @return list of column value
     * @throws ConnectorCheckedException an unexpected exception has occurred
     */
    private List<String> getRecordValues(long   rowNumber,
                                         String propertyName) throws ConnectorCheckedException
    {
        final String methodName = "getRecordValues";

        try
        {
            List<String> recordValues = new ArrayList<>();

            recordValues.add(Long.toString(rowNumber));
            recordValues.add(propertyName);

            return recordValues;
        }
        catch (Exception error)
        {
            /*
             * Probably a null pointer exception - no other exception is expected.
             */
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
     * Close the file
     */
    public void disconnect()
    {
        try
        {
            super.disconnect();
        }
        catch (Exception  exec)
        {
            log.debug("Ignoring unexpected exception " + exec.getClass().getSimpleName() + " with message " + exec.getMessage());
        }

        log.debug("Closing Valid Value Store");
    }
}