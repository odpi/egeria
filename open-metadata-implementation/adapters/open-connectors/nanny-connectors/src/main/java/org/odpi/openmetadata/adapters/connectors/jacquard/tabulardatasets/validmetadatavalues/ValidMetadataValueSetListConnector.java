/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.validmetadatavalues;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataDataSetConnectorBase;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataAuditCode;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * ValidValueSetListConnector maintains a tabular data set that lists the valid value sets in open metadata.
 * Valid value sets are collections of valid metadata value elements that have the same identifier that denotes the
 * property that the list is for.
 */
public class ValidMetadataValueSetListConnector extends OpenMetadataDataSetConnectorBase implements AuditLoggingComponent,
                                                                                                    ReadableTabularDataSource
{
    private static final String myConnectorName = ValidMetadataValueSetListConnector.class.getName();

    private final Map<Long, PropertyDetails> records = new HashMap<>();


    /**
     * Describes the values of a row in the data set
     */
    static class PropertyDetails
    {
        private final String propertyName;
        private final String description;
        private final String dataType;
        private final String example;
        private       Date   createTime;
        private       Date   updateTime;

        /**
         * Create details of a new property found in the valid metadata valid values.
         *
         * @param propertyName name of the property - this is the key
         * @param description description of the property
         * @param dataType data type of the property
         * @param example one of the valid values - first one found
         * @param createTime earliest create time for a valid value for this property
         * @param updateTime latest update time for this property
         */
        public PropertyDetails(String propertyName, String description, String dataType, String example, Date createTime, Date updateTime)
        {
            this.propertyName = propertyName;
            this.description  = description;
            this.dataType     = dataType;
            this.example      = example;
            this.createTime   = createTime;

            if (updateTime == null)
            {
                this.updateTime = createTime;
            }
            else
            {
                this.updateTime = updateTime;
            }
        }


        /**
         * Return the name of the property.
         *
         * @return string
         */
        public String getPropertyName()
        {
            return propertyName;
        }


        /**
         * Return the description of the property.
         *
         * @return string
         */
        public String getDescription()
        {
            return description;
        }


        /**
         * Return the data type of the values in the set (matches the property definition).
         *
         * @return string
         */
        public String getDataType()
        {
            return dataType;
        }


        /**
         * Return an example value from the valid value set.
         *
         * @return string
         */
        public String getExample()
        {
            return example;
        }


        /**
         * Return the earliest change to this set.
         *
         * @return date
         */
        public Date getCreateTime()
        {
            return createTime;
        }

        /**
         * Return the latest change to this set.
         *
         * @return date
         */
        public Date getUpdateTime()
        {
            return updateTime;
        }

        /**
         * Check that the create and update times reflect the values for the whole valid metadata value set.
         *
         * @param createTime create time from valid metadata value element
         * @param updateTime update time from valid metadata value element (may be null)
         */
        public void setCreateUpdateTime(Date createTime,
                                        Date updateTime)
        {
            if (createTime != null)
            {
                if (createTime.before(this.createTime))
                {
                    this.createTime = createTime;
                }

                if (updateTime == null)
                {
                    if (createTime.after(this.updateTime))
                    {
                        this.updateTime = createTime;
                    }
                }
                else
                {
                    if (updateTime.after(this.updateTime))
                    {
                        this.updateTime = updateTime;
                    }
                }
            }
        }
    }

    /**
     * Default constructor
     */
    public ValidMetadataValueSetListConnector()
    {
        super(myConnectorName,
              ProductDefinitionEnum.VALID_METADATA_VALUE_SET_LIST);
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

        refreshCache();
    }


    /**
     * Refresh any cached values.
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    public void refreshCache() throws ConnectorCheckedException
    {
        final String methodName = "refreshCache";

        /*
         * Retrieve all valid values definitions and evaluate whether each definition is a top-level valid value
         * set or not.  All top-level valid value sets are stored in the validValueSets list.
         */
        try
        {
            OpenMetadataStore openMetadataStore = connectorContext.getOpenMetadataStore();

            int startFrom = 0;
            int pageSize = openMetadataStore.getMaxPagingSize();

            if (pageSize == 0)
            {
                pageSize = 100;
            }

            /*
             * Builds the data values for the data set based on values extracted from open metadata.
             */
            Map<String, PropertyDetails> properties = new HashMap<>();

            /*
             * Returns all the valid metadata value elements stored in Egeria - this can be a very large set of queries.
             */
            List<OpenMetadataElement> validValueDefinitions = openMetadataStore.findMetadataElementsWithString(null,
                                                                                                               openMetadataStore.getSearchOptions(OpenMetadataType.VALID_METADATA_VALUE.typeName,
                                                                                                                                                  startFrom,
                                                                                                                                                  pageSize));
            while (validValueDefinitions != null)
            {
                for (OpenMetadataElement validValueDefinition : validValueDefinitions)
                {
                    if (validValueDefinition != null)
                    {
                        String preferredValue = propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.PREFERRED_VALUE.name , validValueDefinition.getElementProperties(), methodName);

                        /*
                         * Skip the parent set.
                         */
                        if (preferredValue != null)
                        {
                            String identifier = propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.IDENTIFIER.name, validValueDefinition.getElementProperties(), methodName);
                            String dataType   = propertyHelper.getStringProperty(connectorName, OpenMetadataProperty.DATA_TYPE.name, validValueDefinition.getElementProperties(), methodName);

                            /*
                             * Element is valid - have we seen this property before - this is found in the identifier attribute.
                             */
                            PropertyDetails propertyDetails = properties.get(identifier);

                            if (propertyDetails == null)
                            {
                                propertyDetails = new PropertyDetails(identifier,
                                                                      this.getPropertyDescription(identifier),
                                                                      dataType,
                                                                      preferredValue,
                                                                      validValueDefinition.getVersions().getCreateTime(),
                                                                      validValueDefinition.getVersions().getUpdateTime());

                                properties.put(identifier, propertyDetails);
                            }
                            else
                            {
                                propertyDetails.setCreateUpdateTime(validValueDefinition.getVersions().getCreateTime(),
                                                                    validValueDefinition.getVersions().getUpdateTime());
                            }
                        }
                    }
                }

                startFrom += pageSize;
                validValueDefinitions = openMetadataStore.findMetadataElementsWithString(null,
                                                                                         openMetadataStore.getSearchOptions(OpenMetadataType.VALID_METADATA_VALUE.typeName,
                                                                                                                            startFrom,
                                                                                                                            pageSize));
            }

            for (String propertyName : properties.keySet())
            {
                records.put((long) records.size(), properties.get(propertyName));
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
     * Return the property description for this valid values set.  All predefined property names will be found.
     * Any added by new types will return unknown property.  If this becomes a problem, it would be possible
     * to look up the property description through the type definition.
     *
     * @param propertyName name of an open metadata property
     * @return string
     */
    private String getPropertyDescription(String propertyName)
    {
        for (OpenMetadataProperty openMetadataProperty : OpenMetadataProperty.values())
        {
            if (openMetadataProperty.name.equals(propertyName))
            {
                return openMetadataProperty.description;
            }
        }

        return "Unknown property: " + propertyName;
    }


    /**
     * Return the records in the data source.
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
     * @return list of  values (as strings) where each string is the value from a column.  The order is the same as the columns
     * @throws ConnectorCheckedException a problem occurred accessing the data.
     */
    public List<String> readRecord(long  rowNumber) throws ConnectorCheckedException
    {
        final String methodName = "readRecord";

        if (records.containsKey(rowNumber))
        {
            PropertyDetails propertyDetails = records.get(rowNumber);

            return getRecordValues(propertyDetails);
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
     * @param propertyDetails data values for a row in the data set
     * @return list of column values
     * @throws ConnectorCheckedException an unexpected exception has occurred
     */
    private List<String> getRecordValues(PropertyDetails propertyDetails) throws ConnectorCheckedException
    {
        final String methodName = "getRecordValues";

        try
        {
            List<String> recordValues = new ArrayList<>();

            recordValues.add(propertyDetails.getPropertyName());
            recordValues.add(propertyDetails.getDescription());
            recordValues.add(Long.toString(propertyDetails.getCreateTime().getTime()));
            recordValues.add(Long.toString(propertyDetails.getUpdateTime().getTime()));
            recordValues.add(propertyDetails.getDataType());
            recordValues.add(propertyDetails.getExample());

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
}