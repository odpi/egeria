/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets;

import org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.controls.ReferenceDataConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.ffdc.ReferenceDataAuditCode;
import org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.ffdc.ReferenceDataErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataCollection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ValidValueDataSetConnector enables interaction with a valid value set as if it is a tabular data set.
 */
public class ValidValueDataSetConnector extends ReferenceDataSetConnectorBase implements AuditLoggingComponent,
                                                                                         ReadableTabularDataSource,
                                                                                         WritableTabularDataSource,
                                                                                         TabularDataCollection
{
    private static final String myConnectorName = "ValidValueDataSetConnector";
    private static final Logger log = LoggerFactory.getLogger(ValidValueDataSetConnector.class);

    private final Map<Long, RelatedMetadataElementSummary> records                    = new HashMap<>();
    private String                                         validValueSetQualifiedName = null;
    private String                                         tableName = null;
    private String                                         tableDescription = null;
    protected String                                       validValueSetGUID = null;


    /**
     * Default constructor
     */
    public ValidValueDataSetConnector()
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

        validValueSetGUID = super.getStringConfigurationProperty(ReferenceDataConfigurationProperty.VALID_VALUE_SET_GUID.getName(), connectionBean.getConfigurationProperties());

        if (validValueSetGUID == null)
        {
            super.throwMissingConfigurationProperty(connectorName,
                                                    this.getClass().getName(),
                                                    ReferenceDataConfigurationProperty.VALID_VALUE_SET_GUID.getName(),
                                                    methodName);
        }

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
         * Retrieve the valid values for the valid value set and store them in the records map.
         */
        try
        {
            OpenMetadataRootElement validValueSet = connectorContext.getValidValueDefinitionClient().getValidValueDefinitionByGUID(validValueSetGUID, null);

            if (validValueSet.getProperties() instanceof ValidValueDefinitionProperties validValueDefinitionProperties)
            {
                validValueSetQualifiedName = validValueDefinitionProperties.getQualifiedName();
                tableDescription = validValueDefinitionProperties.getDescription();

                if (tableName == null)
                {
                    tableName = validValueDefinitionProperties.getPreferredValue();

                    if (tableName == null)
                    {
                        tableName = validValueDefinitionProperties.getDisplayName();
                    }

                    if (tableName == null)
                    {
                        tableName = validValueDefinitionProperties.getQualifiedName().replaceAll("::", " ").replaceAll("-", " ");
                    }
                }
            }
            else
            {
                throw new ConnectorCheckedException(ReferenceDataErrorCode.INVALID_ELEMENT.getMessageDefinition(connectorName, validValueSet.toString()),
                                                    this.getClass().getName(),
                                                    methodName);
            }

            List<String> knownGUIDs = new ArrayList<>();

            for (long recordNumber : records.keySet())
            {
                RelatedMetadataElementSummary validValue = records.get(recordNumber);

                if (validValue != null)
                {
                    knownGUIDs.add(validValue.getRelatedElement().getElementHeader().getGUID());
                }
            }

            if (validValueSet.getValidValueMembers() != null)
            {
                for (RelatedMetadataElementSummary member : validValueSet.getValidValueMembers())
                {
                    /*
                     * The record is added if it is not already in the record map.
                     */
                    if ((member != null) && (! knownGUIDs.contains(member.getRelatedElement().getElementHeader().getGUID())))
                    {
                        records.put((long) records.size(), member);
                    }
                }
            }
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            super.logRecord(methodName, ReferenceDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),
                                                                                                         methodName,
                                                                                                         error.getMessage()));

            throw new ConnectorCheckedException(ReferenceDataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * Return the canonical table name for this data source.  Each word in the name should be capitalized, with spaces
     * between the words to allow translation between different naming conventions.
     *
     * @return string
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    @Override
    public String getTableName() throws ConnectorCheckedException
    {
        return tableName;
    }



    /**
     * Return the description for this data source.
     *
     * @return string
     */
    @Override
    public String getTableDescription()
    {
        return tableDescription;
    }


    /**
     * Set up the canonical table name for this data source.  Each word in the name should be capitalized, with spaces
     * between the words to allow translation between different naming conventions.
     *
     * @param tableName  string
     * @param tableDescription optional description
     */
    @Override
    public void setTableName(String tableName,
                             String tableDescription)
    {
        this.tableName = tableName;
        this.tableDescription = tableDescription;
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
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.CREATE_TIME, false, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.UPDATE_TIME, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.QUALIFIED_NAME, false, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.DISPLAY_NAME, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.DESCRIPTION, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.CATEGORY, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.NAMESPACE, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.PREFERRED_VALUE, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.IS_CASE_SENSITIVE, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.DATA_TYPE, false, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.SCOPE, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.USAGE, true, false));
            columnDescriptions.add(getTabularColumnDescription(OpenMetadataProperty.IS_DEFAULT_VALUE, false, false));
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
            RelatedMetadataElementSummary validValue = records.get(rowNumber);

            return getRecordValues(validValue);
        }

        throw new ConnectorCheckedException(ReferenceDataErrorCode.NULL_RECORD.getMessageDefinition(connectorName,
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
    private List<String> getRecordValues(RelatedMetadataElementSummary validValue) throws ConnectorCheckedException
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
                    recordValues.add(validValue.getRelatedElement().getElementHeader().getGUID());
                }
                else if (OpenMetadataProperty.CURRENT_STATUS.name.equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(validValue.getRelatedElement().getElementHeader().getStatus().toString());
                }
                else if (OpenMetadataProperty.CREATE_TIME.name.equals(tabularColumnDescription.columnName()))
                {
                    long time = validValue.getRelatedElement().getElementHeader().getVersions().getCreateTime().getTime();

                    recordValues.add(Long.toString(time));
                }
                else if (OpenMetadataProperty.UPDATE_TIME.name.equals(tabularColumnDescription.columnName()))
                {
                    long time;
                    if (validValue.getRelatedElement().getElementHeader().getVersions().getUpdateTime() == null)
                    {
                        time = validValue.getRelatedElement().getElementHeader().getVersions().getCreateTime().getTime();
                    }
                    else
                    {
                        time = validValue.getRelatedElement().getElementHeader().getVersions().getUpdateTime().getTime();
                    }

                    recordValues.add(Long.toString(time));
                }
                else if (validValue.getRelatedElement().getProperties() instanceof ValidValueDefinitionProperties validValueDefinitionProperties)
                {
                    if (OpenMetadataProperty.QUALIFIED_NAME.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getQualifiedName());
                    }
                    else if (OpenMetadataProperty.IDENTIFIER.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getIdentifier());
                    }
                    else if (OpenMetadataProperty.DISPLAY_NAME.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getDisplayName());
                    }
                    else if (OpenMetadataProperty.CATEGORY.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getCategory());
                    }
                    else if (OpenMetadataProperty.DESCRIPTION.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getCategory());
                    }
                    else if (OpenMetadataProperty.VERSION_IDENTIFIER.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getVersionIdentifier());
                    }
                    else if (OpenMetadataProperty.PREFERRED_VALUE.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getPreferredValue());
                    }
                    else if (OpenMetadataProperty.NAMESPACE.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getNamespace());
                    }
                    else if (OpenMetadataProperty.USER_DEFINED_CONTENT_STATUS.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getUserDefinedContentStatus());
                    }
                    else if (OpenMetadataProperty.DATA_TYPE.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getDataType());
                    }
                    else if (OpenMetadataProperty.USAGE.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getUsage());
                    }
                    else if (OpenMetadataProperty.SCOPE.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(validValueDefinitionProperties.getScope());
                    }
                    else if (OpenMetadataProperty.IS_CASE_SENSITIVE.name.equals(tabularColumnDescription.columnName()))
                    {
                        recordValues.add(Boolean.toString(validValueDefinitionProperties.getIsCaseSensitive()));
                    }
                    else if (validValueDefinitionProperties.getAdditionalProperties() != null)
                    {
                        if (validValueDefinitionProperties.getAdditionalProperties().containsKey(tabularColumnDescription.columnName()))
                        {
                            recordValues.add(validValueDefinitionProperties.getAdditionalProperties().get(tabularColumnDescription.columnName()));
                        }
                        else
                        {
                            recordValues.add(null);
                        }
                    }
                    else
                    {
                        recordValues.add(null);
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
            super.logRecord(methodName, ReferenceDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),
                                                                                                         methodName,
                                                                                                         error.getMessage()));

            throw new ConnectorCheckedException(ReferenceDataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                 error.getClass().getName(),
                                                                                                                 methodName,
                                                                                                                 error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Convert the properties from the valid value record into valid value definition properties.
     * Notice that header properties (eg GUID) and relationship properties (eg isDefaultValue) are
     * handled elsewhere
     *
     * @param requestedProperties map of properties
     * @return valid value properties object
     */
    private ValidValueDefinitionProperties getValidValueProperties(ValidValueDefinitionProperties existingProperties,
                                                                   Map<String, String>            requestedProperties)
    {
        ValidValueDefinitionProperties validValueDefinitionProperties = new ValidValueDefinitionProperties(existingProperties);

        if (requestedProperties != null)
        {
            for (String propertyName : requestedProperties.keySet())
            {
                if (propertyName != null)
                {
                    if (propertyName.equals(OpenMetadataProperty.QUALIFIED_NAME.name))
                    {
                        validValueDefinitionProperties.setQualifiedName(requestedProperties.get(propertyName));
                    }
                    else if (propertyName.equals(OpenMetadataProperty.IDENTIFIER.name))
                    {
                        validValueDefinitionProperties.setIdentifier(requestedProperties.get(propertyName));
                    }
                    else if (propertyName.equals(OpenMetadataProperty.DISPLAY_NAME.name))
                    {
                        validValueDefinitionProperties.setDisplayName(requestedProperties.get(propertyName));
                    }
                    else if (propertyName.equals(OpenMetadataProperty.DESCRIPTION.name))
                    {
                        validValueDefinitionProperties.setDescription(requestedProperties.get(propertyName));
                    }
                    else if (propertyName.equals(OpenMetadataProperty.CATEGORY.name))
                    {
                        validValueDefinitionProperties.setCategory(requestedProperties.get(propertyName));
                    }
                    else if (propertyName.equals(OpenMetadataProperty.PREFERRED_VALUE.name))
                    {
                        validValueDefinitionProperties.setPreferredValue(requestedProperties.get(propertyName));
                    }
                    else if (propertyName.equals(OpenMetadataProperty.DATA_TYPE.name))
                    {
                        validValueDefinitionProperties.setDataType(requestedProperties.get(propertyName));
                    }
                    else if (propertyName.equals(OpenMetadataProperty.SCOPE.name))
                    {
                        validValueDefinitionProperties.setScope(requestedProperties.get(propertyName));
                    }
                    else if (propertyName.equals(OpenMetadataProperty.USAGE.name))
                    {
                        validValueDefinitionProperties.setUsage(requestedProperties.get(propertyName));
                    }
                    else if (propertyName.equals(OpenMetadataProperty.NAMESPACE.name))
                    {
                        validValueDefinitionProperties.setNamespace(requestedProperties.get(propertyName));
                    }
                    else if (propertyName.equals(OpenMetadataProperty.IS_CASE_SENSITIVE.name))
                    {
                        String propertyValue = requestedProperties.get(propertyName);

                        validValueDefinitionProperties.setIsCaseSensitive("true".equals(propertyValue));
                    }
                }
            }
        }

        return validValueDefinitionProperties;
    }


    /**
     * Write the requested data record.  The first data record is record 0.
     * This process reads the entire file, inserts the record in the right place and writes it out again.
     *
     * @param requestedRowNumber  long
     * @param dataValues Map of column descriptions to strings, each string is the value for the column.
     * @throws ConnectorCheckedException there is a problem accessing the data.
     */
    @Override
    public void writeRecord(long requestedRowNumber, List<String> dataValues) throws ConnectorCheckedException
    {
        final String methodName = "writeRecord";

        /*
         * Check that the right number of columns have been passed.
         */
        List<TabularColumnDescription> tabularColumnDescriptions = this.getColumnDescriptions();
        if ((dataValues == null) || (dataValues.size() != tabularColumnDescriptions.size()))
        {
            int numberOfColumns = 0;

            if (dataValues != null)
            {
                numberOfColumns = dataValues.size();
            }

            throw new ConnectorCheckedException(ReferenceDataErrorCode.NULL_RECORD.getMessageDefinition(connectorName,
                                                                                                        Integer.toString(numberOfColumns),
                                                                                                        methodName,
                                                                                                        Long.toString(requestedRowNumber),
                                                                                                        Integer.toString(tabularColumnDescriptions.size())),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * Check that the row number matches an existing record
         */
        RelatedMetadataElementSummary existingValidValue = records.get(requestedRowNumber);

        if (existingValidValue == null)
        {
            throw new ConnectorCheckedException(ReferenceDataErrorCode.NULL_RECORD.getMessageDefinition(connectorName,
                                                                                                        Long.toString(requestedRowNumber),
                                                                                                        Integer.toString(records.size())),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * Existing records will have at least one property.
         */
        if (existingValidValue.getRelatedElement().getProperties() instanceof ValidValueDefinitionProperties existingProperties)
        {
            /*
             * Convert the properties to a ValidValuesDefinitionProperties object (unrecognized properties become additionalProperties).
             */
            ValidValueDefinitionProperties properties = getValidValueProperties(existingProperties, this.getNewProperties(dataValues, methodName));

            /*
             * Perform a merge update.  Notice that isDefaultValue is not updated (yet) and GUID can not be updated.
             */
            UpdateOptions updateOptions = new UpdateOptions();

            updateOptions.setMergeUpdate(true);

            try
            {
                connectorContext.getValidValueDefinitionClient().updateValidValueDefinition(existingValidValue.getRelatedElement().getElementHeader().getGUID(),
                                                                                            updateOptions,
                                                                                            properties);
            }
            catch (Exception error)
            {
                super.logRecord(methodName, ReferenceDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                             error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()));

                throw new ConnectorCheckedException(ReferenceDataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                     error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
            }
        }
    }


    /**
     * Write the requested data record to the end of the data source.
     *
     * @param dataValues Map of column descriptions to strings, each string is the value for the column.
     * @throws ConnectorCheckedException there is a problem accessing the data.
     */
    @Override
    public void appendRecord(List<String> dataValues) throws ConnectorCheckedException
    {
        final String methodName = "appendRecord";

        /*
         * Check that the right number of columns have been passed.
         */
        List<TabularColumnDescription> tabularColumnDescriptions = this.getColumnDescriptions();
        if ((dataValues == null) || (dataValues.size() != tabularColumnDescriptions.size()))
        {
            int numberOfColumns = 0;

            if (dataValues != null)
            {
                numberOfColumns = dataValues.size();
            }

            throw new ConnectorCheckedException(ReferenceDataErrorCode.NULL_RECORD.getMessageDefinition(connectorName,
                                                                                                        Integer.toString(numberOfColumns),
                                                                                                        methodName,
                                                                                                        Long.toString(records.size()),
                                                                                                        Integer.toString(tabularColumnDescriptions.size())),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * Set up the properties for the new valid value
         */
        Map<String, String> newProperties = new HashMap<>();

        for (int i=0 ; i < dataValues.size() ; i++)
        {
            newProperties.put(tabularColumnDescriptions.get(i).columnName(), dataValues.get(i));
        }

        /*
         * Make sure there is a qualified name since this is not required in the resulting data record.
         */
        if (! newProperties.containsKey(OpenMetadataProperty.QUALIFIED_NAME.name))
        {
            newProperties.put(OpenMetadataProperty.QUALIFIED_NAME.name, validValueSetQualifiedName + "::" + records.size());
        }

        /*
         * Convert the properties to a ValidValuesDefinitionProperties object (unrecognized properties become additionalProperties).
         */
        ValidValueDefinitionProperties properties = getValidValueProperties(null, newProperties);
        boolean isDefaultValue = false;

        if (newProperties.containsKey(OpenMetadataProperty.IS_DEFAULT_VALUE.name))
        {
            if ("true".equals(newProperties.get(OpenMetadataProperty.IS_DEFAULT_VALUE.name)))
            {
                isDefaultValue = true;
            }
        }

        /*
         * Create a new valid value,
         */
        try
        {
            connectorContext.getValidValueDefinitionClient().createValidValueDefinition(validValueSetGUID,
                                                                                        properties,
                                                                                        isDefaultValue);
        }
        catch (Exception error)
        {
            super.logRecord(methodName, ReferenceDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),
                                                                                                         methodName,
                                                                                                         error.getMessage()));

            throw new ConnectorCheckedException(ReferenceDataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                 error.getClass().getName(),
                                                                                                                 methodName,
                                                                                                                 error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }

        /*
         * Refresh the valid value records to include the new one.
         */
        refreshValidValueSet(methodName);
    }


    /**
     *
     * @param dataValues list of values to store
     * @param methodName calling method
     *
     * @return map of column names to column values
     * @throws ConnectorCheckedException wrong data values
     */
    private Map<String, String> getNewProperties(List<String>                   dataValues,
                                                 String                         methodName) throws ConnectorCheckedException
    {
        /*
         * Check that the right number of columns have been passed.
         */
        List<TabularColumnDescription> tabularColumnDescriptions = this.getColumnDescriptions();
        if ((dataValues == null) || (dataValues.size() != tabularColumnDescriptions.size()))
        {
            int numberOfColumns = 0;

            if (dataValues != null)
            {
                numberOfColumns = dataValues.size();
            }

            throw new ConnectorCheckedException(ReferenceDataErrorCode.NULL_RECORD.getMessageDefinition(connectorName,
                                                                                                        Integer.toString(numberOfColumns),
                                                                                                        methodName,
                                                                                                        Long.toString(records.size()),
                                                                                                        Integer.toString(tabularColumnDescriptions.size())),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * Set up the properties for the new valid value
         */
        Map<String, String> newProperties = new HashMap<>();

        for (int i=0 ; i < dataValues.size() ; i++)
        {
            newProperties.put(tabularColumnDescriptions.get(i).columnName(), dataValues.get(i));
        }

        return newProperties;
    }


    /**
     * Remove the requested data record.  The first data record is record 0.
     *
     * @param rowNumber long
     * @throws ConnectorCheckedException there is a problem accessing the data.
     */
    @Override
    public void deleteRecord(long rowNumber) throws ConnectorCheckedException
    {
        // todo
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