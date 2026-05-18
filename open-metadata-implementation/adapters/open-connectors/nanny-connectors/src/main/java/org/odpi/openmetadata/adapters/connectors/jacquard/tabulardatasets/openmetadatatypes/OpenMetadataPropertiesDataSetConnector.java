/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.openmetadatatypes;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataDataSetConnectorBase;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataAuditCode;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataTypesClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataAttributeTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefGallery;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataPropertiesDataSetConnector enables interaction with the list of open metadata properties as if it is a tabular data set.
 */
public class OpenMetadataPropertiesDataSetConnector extends OpenMetadataDataSetConnectorBase implements AuditLoggingComponent,
                                                                                                        ReadableTabularDataSource
{
    private static final String myConnectorName = OpenMetadataPropertiesDataSetConnector.class.getName();
    private static final Logger log = LoggerFactory.getLogger(OpenMetadataPropertiesDataSetConnector.class);

    private final Map<Long, OpenMetadataProperty> records = new HashMap<>();

    private final ProductDefinitionEnum productDefinition = ProductDefinitionEnum.PROPERTIES_LIST   ;


    /**
     * Default constructor
     */
    public OpenMetadataPropertiesDataSetConnector()
    {
        super(myConnectorName);
    }

    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws UserNotAuthorizedException, ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        super.getColumnDescriptions(productDefinition);
        refreshRecordValues(methodName);
    }


    /**
     * Refresh any cached values.
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    public void refreshCache() throws ConnectorCheckedException
    {
        final String methodName = "refreshCache";

        refreshRecordValues(methodName);
    }


    /**
     * Refresh the details about the open metadata types.
     *
     * @param methodName calling method
     * @throws ConnectorCheckedException problems retrieving the type definitions.
     */
    private void refreshRecordValues(String methodName) throws ConnectorCheckedException
    {
        /*
         * Retrieve the list of open metadata data types and store them in the records map.
         */
        try
        {
            List<String>                      knownTypeNames   = new ArrayList<>();

            /*
             * Add all the known types to the type map - and keep a note of those already allocated a row number.
             */
            for (long recordNumber : records.keySet())
            {
                OpenMetadataProperty property = records.get(recordNumber);

                if (property != null)
                {
                    knownTypeNames.add(property.name);
                }
            }

            /*
             * Now retrieve all the types and look for any new types ...
             */
            for (OpenMetadataProperty property : OpenMetadataProperty.values())
            {
                /*
                 * The record is added if it is not already in the record map.
                 */
                if ((property != null) && (!knownTypeNames.contains(property.name)))
                {
                    records.put((long) records.size(), property);
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
     * Return the canonical table name for this data source.  Each word in the name should be capitalized, with spaces
     * between the words to allow translation between different naming conventions.
     *
     * @return string
     * @throws ConnectorCheckedException data access problem
     */
    @Override
    public String getTableName() throws ConnectorCheckedException
    {
        return productDefinition.getDataSpecTableName();
    }

    
    /**
     * Return the description for this data source.
     *
     * @return string
     */
    @Override
    public String getTableDescription()
    {
        return productDefinition.getDescription();
    }


    /**
     * Return the list of column descriptions associated with this data source.  The information
     * should be sufficient to define the schema in a target data store.  This list is the default
     * column descriptions.  The caller can override them.
     *
     * @return a list of column descriptions or null if not available.
     * @throws ConnectorCheckedException data access problem
     */
    @Override
    public List<TabularColumnDescription> getColumnDescriptions() throws ConnectorCheckedException
    {
        return super.getColumnDescriptions(ProductDefinitionEnum.TYPES_LIST);
    }


    /**
     * Return the requested data record.  The first record is record 0.
     *
     * @param rowNumber long
     * @return list of values (as strings) where each string is the value from a column.  The order is the same as the columns
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    public List<String> readRecord(long rowNumber) throws ConnectorCheckedException
    {
        final String methodName = "readRecord";

        if (records.containsKey(rowNumber))
        {
            OpenMetadataProperty property = records.get(rowNumber);

            return getRecordValues(property);
        }

        throw new ConnectorCheckedException(TabularDataErrorCode.NULL_RECORD.getMessageDefinition(connectorName,
                                                                                                  Long.toString(rowNumber),
                                                                                                  Integer.toString(records.size())),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Convert the open metadata type to a list of column values.
     *
     * @param property valid value that is a member of the valid value set.
     * @return list of column value
     * @throws ConnectorCheckedException an unexpected exception has occurred
     */
    private List<String> getRecordValues(OpenMetadataProperty property) throws ConnectorCheckedException
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
                if (ProductDataFieldDefinition.OPEN_METADATA_PROPERTY_NAME.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(property.name);
                }
                else if (ProductDataFieldDefinition.DATA_TYPE.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(property.type);
                }
                else if (ProductDataFieldDefinition.DESCRIPTION.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(property.description);
                }
                else if (ProductDataFieldDefinition.CATEGORY.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(property.propertyCategory.getDisplayName());
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

        log.debug("Closing Tabular Data Set");
    }
}