/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.ReferenceDataConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataAuditCode;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * ReferenceDataSetConnectorBase enables interaction with a valid value set as if it is a tabular data set.
 */
public abstract class OpenMetadataDataSetConnectorBase extends ConnectorBase implements AuditLoggingComponent,
                                                                                        ReadableTabularDataSource
{
    private String   localServerName  = null;
    private String   localServiceName = null;
    private String   clientUserId     = null;
    private String   targetRootURL    = null;

    protected AuditLog auditLog         = null;
    protected final String   connectorName;
    protected       ConnectorContextBase connectorContext = null;
    protected final PropertyHelper       propertyHelper   = new PropertyHelper();

    protected ProductDefinition productDefinition = null;

    /*
     * The caller can supply this definition.  The subclasses can also provide a default list.
     */
    private List<TabularColumnDescription> columnDescriptions = null;

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(OpenMetadataDataSetConnectorBase.class);


    /**
     * Constructor used to set up the name of this connector (supplied by the subclasses).
     *
     * @param connectorName name of the connector
     */
    public OpenMetadataDataSetConnectorBase(String connectorName)
    {
        this.connectorName = connectorName;
    }


    /**
     * Constructor used to set up the name of this connector (supplied by the subclasses).
     *
     * @param connectorName name of the connector
     */
    public OpenMetadataDataSetConnectorBase(String            connectorName,
                                            ProductDefinition productDefinition)
    {
        this.connectorName = connectorName;
        this.productDefinition = productDefinition;
    }


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description  used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }


    /**
     * Set up caller's environment details.  Needs to be done before start is called.
     *
     * @param clientUserId caller's userId
     * @param localServerName caller's server
     * @param localServiceName caller's service
     */
    public void setLocalEnvironment(String clientUserId,
                                    String localServerName,
                                    String localServiceName)
    {
        this.clientUserId = clientUserId;
        this.localServerName = localServerName;
        this.localServiceName = localServiceName;
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

        /*
         * Retrieve the configuration
         */
        Endpoint endpoint = connectionBean.getEndpoint();

        if (endpoint != null)
        {
            targetRootURL = endpoint.getNetworkAddress();
        }

        if (targetRootURL == null)
        {
            throw new ConnectorCheckedException(TabularDataErrorCode.NULL_URL.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        String serverName = super.getStringConfigurationProperty(ReferenceDataConfigurationProperty.SERVER_NAME.getName(), connectionBean.getConfigurationProperties());

        if (serverName == null)
        {
            throw new ConnectorCheckedException(TabularDataErrorCode.NULL_SERVER_NAME.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        int maxPageSize = super.getIntConfigurationProperty(ReferenceDataConfigurationProperty.MAX_PAGE_SIZE.getName(), connectionBean.getConfigurationProperties());

        /*
         * Set up the extractor client.
         */
        try
        {
            OpenMetadataClient openMetadataClient = new EgeriaOpenMetadataStoreClient(serverName, targetRootURL, secretsStoreConnectorMap, maxPageSize, auditLog);

            connectorContext = new ConnectorContextBase(localServerName,
                                                        localServiceName,
                                                        null,
                                                        null,
                                                        connectorInstanceId,
                                                        connectorName,
                                                        clientUserId,
                                                        null,
                                                        false,
                                                        openMetadataClient,
                                                        auditLog,
                                                        maxPageSize,
                                                        DeleteMethod.LOOK_FOR_LINEAGE);
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
     * @throws ConnectorCheckedException unable to refresh
     */
    public abstract void refreshCache() throws ConnectorCheckedException;


    /**
     * Convert an open metadata property enum into a tabular column description.
     *
     * @param productDataFieldDefinition property enum
     * @param isNullable is the field nullable?
     * @param isIdentifier is the filed all or part of the unique identifier for a row/record
     * @return tabular column description
     */
    protected TabularColumnDescription getTabularColumnDescription(ProductDataFieldDefinition productDataFieldDefinition,
                                                                   boolean                    isNullable,
                                                                   boolean                    isIdentifier)
    {
        return new TabularColumnDescription(productDataFieldDefinition.getDisplayName(),
                                            productDataFieldDefinition.getDataType(),
                                            productDataFieldDefinition.getDescription(),
                                            isNullable,
                                            isIdentifier);
    }


    /**
     * Return the list of column descriptions associated with this data source.  The information
     * should be sufficient to define the schema in a target data store.
     *
     * @return a list of column descriptions or null if not available.
     * @throws ConnectorCheckedException data access problem
     */
    protected List<TabularColumnDescription> getColumnDescriptions(ProductDefinition productDefinition) throws ConnectorCheckedException
    {
        if (columnDescriptions == null)
        {
            columnDescriptions = new ArrayList<>();

            if (productDefinition.getDataSpecIdentifiers() != null)
            {
                for (ProductDataFieldDefinition identifier : productDefinition.getDataSpecIdentifiers())
                {
                    columnDescriptions.add(getTabularColumnDescription(identifier, false, true));
                }
            }

            if (productDefinition.getDataSpecFields() != null)
            {
                for (ProductDataFieldDefinition dataField : productDefinition.getDataSpecFields())
                {
                    columnDescriptions.add(getTabularColumnDescription(dataField, true, false));
                }
            }
        }

        return columnDescriptions;
    }



    /**
     * Return the table name for this data source.  This is in canonical name format where each word in the name
     * should be capitalized, with spaces between the words.
     * This format allows easy translation between different naming conventions.
     *
     * @return string
     * @throws ConnectorCheckedException no product definition
     */
    @Override
    public String getTableName() throws ConnectorCheckedException
    {
        final String methodName = "getTableName";

        if (productDefinition != null)
        {
            return productDefinition.getDataSpecTableName();
        }

        throw new ConnectorCheckedException(TabularDataErrorCode.NULL_PRODUCT_DEFINITION.getMessageDefinition(connectorName, methodName),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Return the description for this data source.
     *
     * @return string
     * @throws ConnectorCheckedException no product definition
     */
    @Override
    public String getTableDescription() throws ConnectorCheckedException
    {
        final String methodName = "getTableDescription";

        if (productDefinition != null)
        {
            return productDefinition.getDescription();
        }

        throw new ConnectorCheckedException(TabularDataErrorCode.NULL_PRODUCT_DEFINITION.getMessageDefinition(connectorName, methodName),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Return the list of column descriptions associated with this data source.  The information
     * should be sufficient to define the schema in a target data store.
     *
     * @return a list of column descriptions or null if not available.
     * @throws ConnectorCheckedException no product definition
     */
    @Override
    public List<TabularColumnDescription> getColumnDescriptions() throws ConnectorCheckedException
    {
        final String methodName = "getColumnDescriptions";

        if (productDefinition != null)
        {
            return getColumnDescriptions(productDefinition);
        }

        throw new ConnectorCheckedException(TabularDataErrorCode.NULL_PRODUCT_DEFINITION.getMessageDefinition(connectorName, methodName),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Locate the named column. A negative number means the column is not present.
     *
     * @param columnName name of the column to return
     * @return column
     * @throws ConnectorCheckedException problem extracting the column descriptions
     */
    @Override
    public int getColumnNumber(String columnName) throws ConnectorCheckedException
    {
        int columnNumber = -1; // means not present

        List<TabularColumnDescription> columnDescriptions = this.getColumnDescriptions();
        if (columnDescriptions != null)
        {
            int columnCount = 0;
            for (TabularColumnDescription tabularColumnDescription : columnDescriptions)
            {
                if ((tabularColumnDescription != null) && (columnName.equals(tabularColumnDescription.columnName())))
                {
                    columnNumber = columnCount;
                    break;
                }

                columnCount ++;
            }
        }

        return columnNumber;
    }


    /**
     * Set up the columns associated with this tabular data source.  These are stored in the first record of the file.
     * The rest of the file is cleared.
     *
     * @param columnDescriptions a list of column descriptions
     * @throws ConnectorCheckedException data access problem
     */
    public void setColumnDescriptions(List<TabularColumnDescription> columnDescriptions) throws ConnectorCheckedException
    {
        this.columnDescriptions = columnDescriptions;
    }


    /**
     * Extracts the record values from the element header based on the specified column name.
     *
     * @param elementHeader element header containing the record values
     * @param columnName name of the column to extract the value for
     * @param recordValues list to store the extracted record values
     * @return true if the value was successfully extracted, false otherwise
     */
    protected boolean getElementHeaderRecordValue(ElementHeader elementHeader,
                                                  String        columnName,
                                                  List<String>  recordValues)
    {
        if ((ProductDataFieldDefinition.GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.LOCATION_GUID.getDisplayName().equals(columnName)))
        {
            recordValues.add(elementHeader.getGUID());
            return true;
        }
        else if (ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME.getDisplayName().equals(columnName))
        {
            recordValues.add(elementHeader.getType().getTypeName());
            return true;
        }
        else if (ProductDataFieldDefinition.ELEMENT_STATUS.getDisplayName().equals(columnName))
        {
            recordValues.add(elementHeader.getStatus().getName());
            return true;
        }
        else if (ProductDataFieldDefinition.CREATE_TIME.getDisplayName().equals(columnName))
        {
            long time = elementHeader.getVersions().getCreateTime().getTime();

            recordValues.add(Long.toString(time));
            return true;
        }
        else if (ProductDataFieldDefinition.UPDATE_TIME.getDisplayName().equals(columnName))
        {
            long time;

            if (elementHeader.getVersions().getUpdateTime() == null)
            {
                time = elementHeader.getVersions().getCreateTime().getTime();
            }
            else
            {
                time = elementHeader.getVersions().getUpdateTime().getTime();
            }

            recordValues.add(Long.toString(time));
            return true;
        }
        else if (ProductDataFieldDefinition.LOCATION_ROLE.getDisplayName().equals(columnName))
        {
            if (elementHeader.getLocationRoles() != null)
            {
                for (ElementClassification locationRole : elementHeader.getLocationRoles())
                {
                    recordValues.add(locationRole.getClassificationName());
                    return true;
                }
            }
        }


        return false;
    }


    /**
     * Extracts the record values from the element properties based on the specified column name.
     *
     * @param openMetadataRootProperties properties object
     * @param columnName name of column to extract
     * @param recordValues array of values to append to
     * @return true if the value was successfully extracted, false otherwise
     */
    protected boolean getElementRecordValue(OpenMetadataRootProperties openMetadataRootProperties,
                                            String                     columnName,
                                            List<String>               recordValues)
    {
        if (openMetadataRootProperties instanceof ReferenceableProperties referenceableProperties)
        {
            if (ProductDataFieldDefinition.QUALIFIED_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(referenceableProperties.getQualifiedName());
                return true;
            }
            else if (ProductDataFieldDefinition.DISPLAY_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(referenceableProperties.getDisplayName());
                return true;
            }
            else if (ProductDataFieldDefinition.DESCRIPTION.getDisplayName().equals(columnName))
            {
                recordValues.add(referenceableProperties.getDescription());
                return true;
            }
            else if (ProductDataFieldDefinition.CATEGORY.getDisplayName().equals(columnName))
            {
                recordValues.add(referenceableProperties.getCategory());
                return true;
            }
            else if (ProductDataFieldDefinition.IDENTIFIER.getDisplayName().equals(columnName))
            {
                recordValues.add(referenceableProperties.getIdentifier());
                return true;
            }
            else if (referenceableProperties instanceof AuthoredReferenceableProperties authoredReferenceableProperties)
            {
                if (ProductDataFieldDefinition.AUTHORS.getDisplayName().equals(columnName))
                {
                    if (authoredReferenceableProperties.getAuthors() != null)
                    {
                        recordValues.add(authoredReferenceableProperties.getAuthors().toString());
                    }
                    else
                    {
                        recordValues.add(null);
                    }
                    return true;
                }
                else if (ProductDataFieldDefinition.CONTENT_STATUS.getDisplayName().equals(columnName))
                {
                    if (authoredReferenceableProperties.getUserDefinedContentStatus() != null)
                    {
                        recordValues.add(authoredReferenceableProperties.getUserDefinedContentStatus());
                    }
                    else if (authoredReferenceableProperties.getContentStatus() != null)
                    {
                        recordValues.add(authoredReferenceableProperties.getContentStatus().getName());
                    }
                    else
                    {
                        recordValues.add(null);
                    }
                    return true;
                }
                else if (authoredReferenceableProperties instanceof ValidValueDefinitionProperties validValueDefinitionProperties)
                {
                    if (ProductDataFieldDefinition.DATA_TYPE.getDisplayName().equals(columnName))
                    {
                        recordValues.add(validValueDefinitionProperties.getDataType());
                        return true;
                    }
                    else if (ProductDataFieldDefinition.PREFERRED_VALUE.getDisplayName().equals(columnName))
                    {
                        recordValues.add(validValueDefinitionProperties.getPreferredValue());
                        return true;
                    }
                    else if (ProductDataFieldDefinition.NAMESPACE.getDisplayName().equals(columnName))
                    {
                        recordValues.add(validValueDefinitionProperties.getNamespace());
                        return true;
                    }
                    else if (ProductDataFieldDefinition.SCOPE.getDisplayName().equals(columnName))
                    {
                        recordValues.add(validValueDefinitionProperties.getScope());
                        return true;
                    }
                    else if (ProductDataFieldDefinition.USAGE.getDisplayName().equals(columnName))
                    {
                        recordValues.add(validValueDefinitionProperties.getUsage());
                        return true;
                    }
                    else if (ProductDataFieldDefinition.IS_CASE_SENSITIVE.getDisplayName().equals(columnName))
                    {
                        if (validValueDefinitionProperties.getIsCaseSensitive())
                        {
                            recordValues.add("true");
                        }
                        else
                        {
                            recordValues.add("false");
                        }
                        return true;
                    }
                }
            }
            else if (referenceableProperties instanceof AssetProperties assetProperties)
            {
                if (ProductDataFieldDefinition.NAMESPACE.getDisplayName().equals(columnName))
                {
                    recordValues.add(assetProperties.getNamespace());
                    return true;
                }
                else if (ProductDataFieldDefinition.RESOURCE_NAME.getDisplayName().equals(columnName))
                {
                    recordValues.add(assetProperties.getResourceName());
                    return true;
                }
                else if (assetProperties instanceof DataAssetProperties dataAssetProperties)
                {
                    if (ProductDataFieldDefinition.AUTHORS.getDisplayName().equals(columnName))
                    {
                        if (dataAssetProperties.getAuthors() != null)
                        {
                            recordValues.add(dataAssetProperties.getAuthors().toString());
                        }
                        else
                        {
                            recordValues.add(null);
                        }
                        return true;
                    }
                    else if (ProductDataFieldDefinition.CONTENT_STATUS.getDisplayName().equals(columnName))
                    {
                        if (dataAssetProperties.getUserDefinedContentStatus() != null)
                        {
                            recordValues.add(dataAssetProperties.getUserDefinedContentStatus());
                        }
                        else if (dataAssetProperties.getContentStatus() != null)
                        {
                            recordValues.add(dataAssetProperties.getContentStatus().getName());
                        }
                        else
                        {
                            recordValues.add(null);
                        }
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /**
     * Convert the root element to a list of column values based on the data spec.
     *
     * @param rootElement valid value that is a member of the valid value set.
     * @return list of column value
     * @throws ConnectorCheckedException an unexpected exception has occurred
     */
    protected List<String> getRecordValues(OpenMetadataRootElement rootElement) throws ConnectorCheckedException
    {
        final String methodName = "getRecordValues";

        try
        {
            List<TabularColumnDescription> tabularColumnDescriptions = this.getColumnDescriptions();
            List<String> recordValues = new ArrayList<>();

            for (TabularColumnDescription tabularColumnDescription : tabularColumnDescriptions)
            {
                /*
                 * If the property is not in the header, look in the properties.  If neither place throw and exception.
                 */
                if (! getElementHeaderRecordValue(rootElement.getElementHeader(), tabularColumnDescription.columnName(), recordValues))
                {
                    if (! getElementRecordValue(rootElement.getProperties(), tabularColumnDescription.columnName(), recordValues))
                    {
                        throw new ConnectorCheckedException(TabularDataErrorCode.UNMAPPED_COLUMN.getMessageDefinition(connectorName,
                                                                                                                      tabularColumnDescription.columnName()),
                                                            this.getClass().getName(),
                                                            methodName);
                    }
                }
            }

            return recordValues;
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
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
     * Close the connector
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

        log.debug("Closing Tabular data set.");
    }
}