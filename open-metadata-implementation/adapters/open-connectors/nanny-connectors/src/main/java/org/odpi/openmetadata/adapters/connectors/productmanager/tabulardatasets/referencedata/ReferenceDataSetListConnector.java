/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.productmanager.tabulardatasets.referencedata;

import org.odpi.openmetadata.adapters.connectors.productmanager.tabulardatasets.OpenMetadataDataSetConnectorBase;
import org.odpi.openmetadata.adapters.connectors.productmanager.tabulardatasets.ffdc.TabularDataAuditCode;
import org.odpi.openmetadata.adapters.connectors.productmanager.tabulardatasets.ffdc.TabularDataErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ValidValueSetListConnector maintains a tabular data set that lists the valid value sets in open metadata.
 * Valid value sets are valid value definitions with no valid value parent and one or more members.
 */
public class ReferenceDataSetListConnector extends OpenMetadataDataSetConnectorBase implements AuditLoggingComponent,
                                                                                               ReadableTabularDataSource
{
    private static final String myConnectorName = "ValidValueSetListConnector";
    private static final Logger log = LoggerFactory.getLogger(ReferenceDataSetListConnector.class);

    private final Map<Long, OpenMetadataRootElement> records = new HashMap<>();

    /**
     * Default constructor
     */
    public ReferenceDataSetListConnector()
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

        refreshValidValueSet(methodName);
    }


    /**
     * Refresh any cached values,
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    public void refreshCache() throws ConnectorCheckedException
    {
        final String methodName = "refreshCache";

        refreshValidValueSet(methodName);
    }


    /**
     * Refresh the details about the valid value set.
     *
     * @param methodName calling method
     * @throws ConnectorCheckedException problems retrieving the valid value set.
     */
    private void refreshValidValueSet(String methodName) throws ConnectorCheckedException
    {
        /*
         * Retrieve all valid values definitions and evaluate whether each definition is a top-level valid value
         * set or not.  All top-level valid value sets are stored in the validValueSets list.
         */
        try
        {
            int pageSize = connectorContext.getMaxPageSize();

            if (pageSize == 0)
            {
                pageSize = 100;
            }

            Map<String, OpenMetadataElementStub> validValueSetStubs = new HashMap<>();
            QueryOptions  queryOptions = new SearchOptions();

            queryOptions.setPageSize(pageSize);
            queryOptions.setStartFrom(0);
            queryOptions.setGraphQueryDepth(2);

            /*
             * Retrieve all of the valid value member relationships.  The valid value sets will be the valid value
             * definitions at end 1.  Using the map will collect one instance of each valid value set.
             */
            OpenMetadataRelationshipList openMetadataRelationshipList = connectorContext.getOpenMetadataStore().findRelationshipsBetweenMetadataElements(OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                                                                                                                         null,
                                                                                                                                                         queryOptions);

            while ((openMetadataRelationshipList != null) && (openMetadataRelationshipList.getElementList() != null))
            {
                for (OpenMetadataRelationship openMetadataRelationship : openMetadataRelationshipList.getElementList())
                {
                    if (openMetadataRelationship != null)
                    {
                        validValueSetStubs.put(openMetadataRelationship.getElementGUIDAtEnd1(), openMetadataRelationship.getElementAtEnd1());
                    }
                }

                queryOptions.setStartFrom(queryOptions.getStartFrom() + pageSize);
                openMetadataRelationshipList = connectorContext.getOpenMetadataStore().findRelationshipsBetweenMetadataElements(OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                                                                                                null,
                                                                                                                                queryOptions);
            }

            List<OpenMetadataRootElement> validValueSets = new ArrayList<>();

            for (String validValueSetGUID : validValueSetStubs.keySet())
            {
                OpenMetadataRootElement validValueSet = connectorContext.getValidValueDefinitionClient().getValidValueDefinitionByGUID(validValueSetGUID, null);

                if (validValueSet != null)
                {
                    validValueSets.add(validValueSet);
                }
            }

            List<String> knownGUIDs = new ArrayList<>();

            for (long recordNumber : records.keySet())
            {
                OpenMetadataRootElement validValue = records.get(recordNumber);

                if (validValue != null)
                {
                    knownGUIDs.add(validValue.getElementHeader().getGUID());
                }
            }

            for (OpenMetadataRootElement validValueSet : validValueSets)
            {
                /*
                 * The record is added if it is not already in the record map.
                 */
                if ((validValueSet != null) && (! knownGUIDs.contains(validValueSet.getElementHeader().getGUID())))
                {
                    records.put((long) records.size(), validValueSet);
                }
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
        return "Valid Value Set List";
    }



    /**
     * Return the description for this data source.
     *
     * @return string
     */
    @Override
    public String getTableDescription()
    {
        return "A list of the valid value data sets stored in open metadata";
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

            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.GUID, false, true));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.CREATE_TIME, false, true));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.UPDATE_TIME, false, true));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.QUALIFIED_NAME, false, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.DISPLAY_NAME, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.DESCRIPTION, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.CATEGORY, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.NAMESPACE, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.PREFERRED_VALUE, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.IS_CASE_SENSITIVE, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.DATA_TYPE, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.SCOPE, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.USAGE, true, false));
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
            OpenMetadataRootElement validValue = records.get(rowNumber);

            return getRecordValues(validValue);
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
     * @param validValue valid value that is a member of the valid value set.
     * @return list of column value
     * @throws ConnectorCheckedException an unexpected exception has occurred
     */
    private List<String> getRecordValues(OpenMetadataRootElement validValue) throws ConnectorCheckedException
    {
        final String methodName = "getRecordValues";

        try
        {
            List<TabularColumnDescription> tabularColumnDescriptions = this.getColumnDescriptions();
            List<String> recordValues = new ArrayList<>();

            for (TabularColumnDescription tabularColumnDescription : tabularColumnDescriptions)
            {
                /*
                 * The GUID is in the header rather than the properties.  Other header properties
                 * could be supported by expanding the logic below.
                 */
                if (OpenMetadataProperty.GUID.name.equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(validValue.getElementHeader().getGUID());
                }
                else if (OpenMetadataProperty.CREATE_TIME.name.equals(tabularColumnDescription.columnName()))
                {
                    long time = validValue.getElementHeader().getVersions().getCreateTime().getTime();

                    recordValues.add(Long.toString(time));
                }
                else if (OpenMetadataProperty.UPDATE_TIME.name.equals(tabularColumnDescription.columnName()))
                {
                    long time;

                    if (validValue.getElementHeader().getVersions().getUpdateTime() == null)
                    {
                        time = validValue.getElementHeader().getVersions().getCreateTime().getTime();
                    }
                    else
                    {
                        time = validValue.getElementHeader().getVersions().getUpdateTime().getTime();
                    }

                    recordValues.add(Long.toString(time));
                }
                else if (validValue.getProperties() instanceof ValidValueDefinitionProperties validValueDefinitionProperties)
                {
                    if (OpenMetadataProperty.QUALIFIED_NAME.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getQualifiedName());
                    }
                    else if (OpenMetadataProperty.DISPLAY_NAME.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getDisplayName());
                    }
                    else if (OpenMetadataProperty.DESCRIPTION.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getDescription());
                    }
                    else if (OpenMetadataProperty.CATEGORY.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getCategory());
                    }
                    else if (OpenMetadataProperty.PREFERRED_VALUE.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getPreferredValue());
                    }
                    else if (OpenMetadataProperty.DATA_TYPE.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getDataType());
                    }
                    else if (OpenMetadataProperty.SCOPE.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getScope());
                    }
                    else if (OpenMetadataProperty.USAGE.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getUsage());
                    }
                    else if (OpenMetadataProperty.NAMESPACE.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getNamespace());
                    }
                    else if (OpenMetadataProperty.IS_CASE_SENSITIVE.name.equals(tabularColumnDescription.columnName()))
                    {
                        if (validValueDefinitionProperties.getIsCaseSensitive())
                        {
                            recordValues.add("true");
                        }
                        else
                        {
                            recordValues.add("false");
                        }
                    }
                }
                else
                {
                    recordValues.add(null);
                }
            }

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