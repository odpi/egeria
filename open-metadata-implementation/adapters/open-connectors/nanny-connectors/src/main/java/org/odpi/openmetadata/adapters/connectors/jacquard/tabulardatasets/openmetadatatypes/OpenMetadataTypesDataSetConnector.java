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
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefGallery;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefLink;
import org.odpi.openmetadata.frameworks.openmetadata.properties.TypeDefList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataTypesDataSetConnector enables interaction with the list of open metadata types as if it is a tabular data set.
 */
public class OpenMetadataTypesDataSetConnector extends OpenMetadataDataSetConnectorBase implements AuditLoggingComponent,
                                                                                                   ReadableTabularDataSource
{
    private static final String myConnectorName = OpenMetadataTypesDataSetConnector.class.getName();
    private static final Logger log = LoggerFactory.getLogger(OpenMetadataTypesDataSetConnector.class);

    private final Map<Long, OpenMetadataTypeDetails> records = new HashMap<>();

    private final ProductDefinitionEnum productDefinition = ProductDefinitionEnum.TYPES_LIST;

    static class OpenMetadataTypeDetails
    {
        private final OpenMetadataTypeDef openMetadataTypeDef;
        private final List<String>        subtypes   = new ArrayList<>();
        private       List<String>        superTypes = null;

        public OpenMetadataTypeDetails(OpenMetadataTypeDef openMetadataTypeDef,
                                       TypeDefList         subtypes)
        {
            this.openMetadataTypeDef = openMetadataTypeDef;

            if (subtypes != null)
            {
                for (OpenMetadataTypeDefLink subtype : subtypes.getTypeDefs())
                {
                    this.subtypes.add(subtype.getName());
                }
            }
        }

        public void setSuperTypes(List<String> superTypes)
        {
            this.superTypes = superTypes;
        }

        public List<String> getSuperTypes()
        {
            return superTypes;
        }

        public OpenMetadataTypeDef getOpenMetadataTypeDef()
        {
            return openMetadataTypeDef;
        }

        public List<String> getSubtypes()
        {
            return subtypes;
        }

        public String getGUID()
        {
            return openMetadataTypeDef.getGUID();
        }
    }

    /**
     * Default constructor
     */
    public OpenMetadataTypesDataSetConnector()
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

        super.getColumnDescriptions(ProductDefinitionEnum.TYPES_LIST);
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
         * Retrieve the list of open metadata types and store them in the records map.
         */
        try
        {
            Map<String, OpenMetadataTypeDetails> typeMap = new HashMap<>();
            List<String>                         knownTypeNames = new ArrayList<>();

            /*
             * Add all the known types to the type map - and keep a note of those already allocated a row number.
             */
            for (long recordNumber : records.keySet())
            {
                OpenMetadataTypeDetails openMetadataTypeDetails = records.get(recordNumber);

                if (openMetadataTypeDetails != null)
                {
                    typeMap.put(openMetadataTypeDetails.getOpenMetadataTypeDef().getName(), openMetadataTypeDetails);
                    knownTypeNames.add(openMetadataTypeDetails.getOpenMetadataTypeDef().getName());
                }
            }

            /*
             * Now retrieve all the types and look for any new types ...
             */
            OpenMetadataTypesClient openMetadataTypesClient = connectorContext.getOpenMetadataTypesClient();

            OpenMetadataTypeDefGallery openMetadataTypeDefGallery = openMetadataTypesClient.getAllTypes();

            if (openMetadataTypeDefGallery != null)
            {
                /*
                 * First-pass through the types to establish the record.
                 */
                for (OpenMetadataTypeDef member : openMetadataTypeDefGallery.getTypeDefs())
                {
                    /*
                     * The record is added if it is not already in the record map.
                     */
                    if ((member != null) && (!knownTypeNames.contains(member.getGUID())))
                    {
                        OpenMetadataTypeDetails openMetadataTypeDetails = new OpenMetadataTypeDetails(member, openMetadataTypesClient.getSubTypes(member.getName()));

                        typeMap.put(member.getName(), openMetadataTypeDetails);
                    }
                }

                /*
                 * Second-pass through the types to establish the super types.
                 */
                for (String newType : typeMap.keySet())
                {
                    OpenMetadataTypeDetails openMetadataTypeDetails = typeMap.get(newType);
                    openMetadataTypeDetails.setSuperTypes(getSuperTypes(typeMap.get(newType).getOpenMetadataTypeDef().getName(), typeMap));
                }

                /*
                 * Third-pass through the types to add new types to the record.
                 */
                for (String newType : typeMap.keySet())
                {
                    OpenMetadataTypeDetails openMetadataTypeDetails = typeMap.get(newType);

                    if (! knownTypeNames.contains(openMetadataTypeDetails.getOpenMetadataTypeDef().getName()))
                    {
                        records.put((long) records.size(), openMetadataTypeDetails);
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
     * Recurse through the open metadata types to build a list of super types.
     *
     * @param typeName starting type name
     * @param typeMap map of type names to type details
     * @return list of super type names or empty list if no super type
     */
    private List<String> getSuperTypes(String                               typeName,
                                       Map<String, OpenMetadataTypeDetails> typeMap)
    {
        List<String> superTypes = new ArrayList<>();
        OpenMetadataTypeDetails openMetadataTypeDetails = typeMap.get(typeName);

        if ((openMetadataTypeDetails != null) && (openMetadataTypeDetails.getOpenMetadataTypeDef().getSuperType() != null))
        {
            superTypes = this.getSuperTypes(openMetadataTypeDetails.getOpenMetadataTypeDef().getSuperType().getName(), typeMap);
            superTypes.add(openMetadataTypeDetails.getOpenMetadataTypeDef().getSuperType().getName());
        }

        return superTypes;
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
            OpenMetadataTypeDetails validValue = records.get(rowNumber);

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
     * @param openMetadataTypeDetails valid value that is a member of the valid value set.
     * @return list of column value
     * @throws ConnectorCheckedException an unexpected exception has occurred
     */
    private List<String> getRecordValues(OpenMetadataTypeDetails openMetadataTypeDetails) throws ConnectorCheckedException
    {
        final String methodName = "getRecordValues";

        try
        {
            List<TabularColumnDescription> tabularColumnDescriptions = this.getColumnDescriptions();
            List<String> recordValues = new ArrayList<>();

            OpenMetadataTypeDef openMetadataTypeDef = openMetadataTypeDetails.getOpenMetadataTypeDef();

            for (TabularColumnDescription tabularColumnDescription : tabularColumnDescriptions)
            {
                /*
                 * The GUID is in the header rather than the properties.  Other header properties
                 * could be supported by expanding the logic below.
                 */
                if (ProductDataFieldDefinition.GUID.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(openMetadataTypeDef.getGUID());
                }
                else if (ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(openMetadataTypeDef.getName());
                }
                else if (ProductDataFieldDefinition.CREATE_TIME.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(Long.toString(openMetadataTypeDef.getCreateTime().getTime()));
                }
                else if (ProductDataFieldDefinition.UPDATE_TIME.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    if (openMetadataTypeDef.getUpdateTime() != null)
                    {
                        recordValues.add(Long.toString(openMetadataTypeDef.getUpdateTime().getTime()));
                    }
                    else
                    {
                        recordValues.add(Long.toString(openMetadataTypeDef.getCreateTime().getTime()));
                    }
                    recordValues.add(openMetadataTypeDef.getStatus().getName());
                }
                else if (ProductDataFieldDefinition.OPEN_METADATA_TYPE_STATUS.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(openMetadataTypeDef.getStatus().getName());
                }
                else if (ProductDataFieldDefinition.URL.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(openMetadataTypeDef.getDescriptionWiki());
                }
                else if (ProductDataFieldDefinition.BEAN_CLASS_NAME.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(openMetadataTypeDef.getBeanClassName());
                }
                else if (ProductDataFieldDefinition.CATEGORY.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    if (openMetadataTypeDef.getCategory() != null)
                    {
                        recordValues.add(openMetadataTypeDef.getCategory().getName());
                    }
                    else
                    {
                        recordValues.add(null);
                    }
                }
                else if (ProductDataFieldDefinition.DESCRIPTION.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    recordValues.add(openMetadataTypeDef.getDescription());
                }
                else if (ProductDataFieldDefinition.OPEN_METADATA_SUBTYPES.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    if (openMetadataTypeDetails.getSubtypes() != null)
                    {
                        recordValues.add(openMetadataTypeDetails.getSubtypes().toString());
                    }
                    else
                    {
                        recordValues.add(null);
                    }
                }
                else if (ProductDataFieldDefinition.OPEN_METADATA_SUPER_TYPES.getDisplayName().equals(tabularColumnDescription.columnName()))
                {
                    if (openMetadataTypeDetails.getSuperTypes() != null)
                    {
                        recordValues.add(openMetadataTypeDetails.getSuperTypes().toString());
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