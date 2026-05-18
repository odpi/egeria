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
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * OpenMetadataTypesDataSetConnector enables interaction with the list of open metadata types as if it is a tabular data set.
 */
public class OpenMetadataAttributesForTypesDataSetConnector extends OpenMetadataDataSetConnectorBase implements AuditLoggingComponent,
                                                                                                                ReadableTabularDataSource
{
    private static final String myConnectorName = OpenMetadataAttributesForTypesDataSetConnector.class.getName();
    private static final Logger log = LoggerFactory.getLogger(OpenMetadataAttributesForTypesDataSetConnector.class);

    private final Map<Long, OpenMetadataAttributeDetails> records = new HashMap<>();

    private final ProductDefinitionEnum productDefinition = ProductDefinitionEnum.ATTRIBUTES_FOR_TYPES_LIST;


    /**
     * Details of an open metadata attribute for a type.
     */
    static class OpenMetadataAttributeDetails
    {
        private final OpenMetadataTypeDefAttribute openMetadataTypeDefAttribute;
        private final String                       typeName;
        private final Date                         createTime;
        private final Date                         updateTime;

        /**
         * Constructor for an open metadata attribute details.
         *
         * @param openMetadataTypeDefAttribute attribute definition
         * @param typeName type name
         * @param createTime creation time
         * @param updateTime update time
         */
        OpenMetadataAttributeDetails(OpenMetadataTypeDefAttribute openMetadataTypeDefAttribute,
                                     String                       typeName,
                                     Date                         createTime,
                                     Date                         updateTime)
        {
            this.openMetadataTypeDefAttribute = openMetadataTypeDefAttribute;
            this.typeName                     = typeName;
            this.createTime                   = createTime;
            this.updateTime                   = updateTime;
        }


        /**
         * Return the type name of this attribute.
         *
         * @return String type name
         */
        public String getTypeName()
        {
            return typeName;
        }


        /**
         * Return the name of this attribute.
         *
         * @return String name
         */
        public String getAttributeName()
        {
            return openMetadataTypeDefAttribute.getAttributeName();
        }


        /**
         * Return the type of this attribute.
         *
         * @return String name
         */
        public String getAttributeType()
        {
            return openMetadataTypeDefAttribute.getAttributeType().getName();
        }


        /**
         * Return the status of this attribute.
         *
         * @return status (null means ACTIVE)
         */
        public OpenMetadataTypeDefAttributeStatus getAttributeStatus()
        {
            return openMetadataTypeDefAttribute.getAttributeStatus();
        }


        /**
         * Return the short description of the attribute.
         *
         * @return String description
         */
        public String getAttributeDescription()
        {
            return openMetadataTypeDefAttribute.getAttributeDescription();
        }


        /**
         * Return the nullable status of the attribute.
         *
         * @return boolean nullable status
         */
        public boolean isNullable()
        {
            return openMetadataTypeDefAttribute.getAttributeCardinality() != OpenMetadataAttributeCardinality.ONE_ONLY;
        }


        /**
         * Return the date/time that this OpenMetadataTypeDef was created.
         *
         * @return Date
         */
        public Date getCreateTime()
        {
            return createTime;
        }


        /**
         * Return the date/time that this OpenMetadataTypeDef was last updated.
         *
         * @return Date
         */
        public Date getUpdateTime()
        {
            return updateTime;
        }
    }


    /**
     * Default constructor
     */
    public OpenMetadataAttributesForTypesDataSetConnector()
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
            Map<String, OpenMetadataAttributeDetails> attributeMap   = new HashMap<>();
            List<String>                              knownAttributeNames = new ArrayList<>();

            /*
             * Add all the known types to the type map - and keep a note of those already allocated a row number.
             */
            for (long recordNumber : records.keySet())
            {
                OpenMetadataAttributeDetails attributeDetails = records.get(recordNumber);

                if (attributeDetails != null)
                {
                    attributeMap.put(attributeDetails.getTypeName() + "::" + attributeDetails.getAttributeName(), attributeDetails);
                    knownAttributeNames.add(attributeDetails.getTypeName() + "::" + attributeDetails.getAttributeName());
                }
            }

            /*
             * Now retrieve all the types and look for any new types ...
             */
            OpenMetadataTypesClient openMetadataTypesClient = connectorContext.getOpenMetadataTypesClient();

            OpenMetadataTypeDefGallery openMetadataTypeDefGallery = openMetadataTypesClient.getAllTypes(true, true);

            if (openMetadataTypeDefGallery != null)
            {
                /*
                 * First-pass through the types to establish the record.
                 */
                for (OpenMetadataTypeDef member : openMetadataTypeDefGallery.getTypeDefs())
                {
                    if ((member != null) && (member.getAttributeDefinitions() != null))
                    {
                        for (OpenMetadataTypeDefAttribute attributeTypeDef : member.getAttributeDefinitions())
                        {
                            /*
                             * The record is added if it is not already in the record map.
                             */
                            if ((!knownAttributeNames.contains(member.getName() + "::" + attributeTypeDef.getAttributeName())))
                            {
                                OpenMetadataAttributeDetails attributeDetails = new OpenMetadataAttributeDetails(attributeTypeDef, member.getName(), member.getCreateTime(), member.getUpdateTime());
                                attributeMap.put(member.getName(), attributeDetails);
                            }
                        }
                    }
                }

                /*
                 * Third-pass through the types to add new types to the record.
                 */
                for (String newType : attributeMap.keySet())
                {
                    OpenMetadataAttributeDetails attributeDetails = attributeMap.get(newType);

                    if (! knownAttributeNames.contains(attributeDetails.getTypeName() + "::" + attributeDetails.getAttributeName()))
                    {
                        records.put((long) records.size(), attributeDetails);
                    }
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
            OpenMetadataAttributeDetails validValue = records.get(rowNumber);

            return getRecordValues(validValue);
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
     * @param attributeDetails valid value that is a member of the valid value set.
     * @return list of column value
     * @throws ConnectorCheckedException an unexpected exception has occurred
     */
    private List<String> getRecordValues(OpenMetadataAttributeDetails attributeDetails) throws ConnectorCheckedException
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
                if (ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(attributeDetails.getTypeName());
                }
                else if (ProductDataFieldDefinition.OPEN_METADATA_PROPERTY_NAME.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(attributeDetails.getAttributeName());
                }
                else if (ProductDataFieldDefinition.IS_NULLABLE.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(Boolean.toString(attributeDetails.isNullable()));
                }
                else if (ProductDataFieldDefinition.DATA_TYPE.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(attributeDetails.getAttributeType());
                }
                else if (ProductDataFieldDefinition.DESCRIPTION.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(attributeDetails.getAttributeDescription());
                }
                else if (ProductDataFieldDefinition.OPEN_METADATA_ATTRIBUTE_STATUS.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    if (attributeDetails.getAttributeStatus() != null)
                    {
                        recordValues.add(attributeDetails.getAttributeStatus().getName());
                    }
                    else
                    {
                        recordValues.add(null);
                    }
                }
                else if (ProductDataFieldDefinition.CREATE_TIME.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    Date createTime = attributeDetails.getCreateTime();
                    recordValues.add(Long.toString(createTime.getTime()));
                }
                else if (ProductDataFieldDefinition.UPDATE_TIME.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    Date updateTime = attributeDetails.getUpdateTime();

                    if (updateTime == null)
                    {
                        updateTime = attributeDetails.getCreateTime();
                    }
                    recordValues.add(Long.toString(updateTime.getTime()));
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