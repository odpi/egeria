/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets;

import org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.controls.ReferenceDataConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.ffdc.ReferenceDataAuditCode;
import org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.ffdc.ReferenceDataErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * ValidValueDataSetConnector enables interaction with a valid value set as if it is a tabular data set.
 */
public abstract class ReferenceDataSetConnectorBase extends ConnectorBase implements AuditLoggingComponent,
                                                                                     ReadableTabularDataSource
{
    private String   localServerName  = null;
    private String   localServiceName = null;
    private String   clientUserId     = null;
    private String   targetRootURL    = null;

    protected AuditLog auditLog         = null;
    protected final String   connectorName;
    protected ConnectorContextBase connectorContext  = null;
    protected String               validValueSetGUID = null;

    /*
     * This definition can be supplied by the caller.  The subclasses can also provide a default list.
     */
    protected List<TabularColumnDescription> columnDescriptions = null;

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(ReferenceDataSetConnectorBase.class);


    /**
     * Constructor used to set up the name of this connector (supplied by the subclasses).
     *
     * @param connectorName name of the connector
     */
    public ReferenceDataSetConnectorBase(String connectorName)
    {
        this.connectorName = connectorName;
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
     * Return the component description that is used by this connector in the audit log.
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
     * @throws ConnectorCheckedException there is a problem within the connector.
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
            targetRootURL = endpoint.getAddress();
        }

        if (targetRootURL == null)
        {
            throw new ConnectorCheckedException(ReferenceDataErrorCode.NULL_URL.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        String serverName = super.getStringConfigurationProperty(ReferenceDataConfigurationProperty.SERVER_NAME.getName(), connectionBean.getConfigurationProperties());

        if (serverName == null)
        {
            throw new ConnectorCheckedException(ReferenceDataErrorCode.NULL_SERVER_NAME.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        validValueSetGUID = super.getStringConfigurationProperty(ReferenceDataConfigurationProperty.VALID_VALUE_SET_GUID.getName(), connectionBean.getConfigurationProperties());

        if (validValueSetGUID == null)
        {
            super.throwMissingConfigurationProperty(connectorName,
                                                    this.getClass().getName(),
                                                    ReferenceDataConfigurationProperty.VALID_VALUE_SET_GUID.getName(),
                                                    methodName);
        }

        int maxPageSize = super.getIntConfigurationProperty(ReferenceDataConfigurationProperty.MAX_PAGE_SIZE.getName(), connectionBean.getConfigurationProperties());

        /*
         * Set up the extractor client.
         */
        try
        {
            OpenMetadataClient openMetadataClient;

            if (clientUserId != null)
            {
                openMetadataClient = new EgeriaOpenMetadataStoreClient(serverName, targetRootURL, maxPageSize);
            }
            else
            {
                clientUserId = connectionBean.getUserId();

                if (connectionBean.getClearPassword() != null)
                {
                    openMetadataClient = new EgeriaOpenMetadataStoreClient(serverName,
                                                                           targetRootURL,
                                                                           connectionBean.getUserId(),
                                                                           connectionBean.getClearPassword(),
                                                                           maxPageSize);
                }
                else
                {
                    openMetadataClient = new EgeriaOpenMetadataStoreClient(serverName, targetRootURL, maxPageSize);
                }
            }

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
     * Refresh any cached values,
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    public abstract void refreshCache() throws ConnectorCheckedException;


    /**
     * Convert an open metadata property enum into a tabular column description.
     *
     * @param openMetadataProperty property enum
     * @return tabular column description
     */
    protected TabularColumnDescription getTabularColumnDescription(OpenMetadataProperty openMetadataProperty,
                                                                   boolean              isNullable)
    {
        return new TabularColumnDescription(openMetadataProperty.name,
                                            openMetadataProperty.dataType,
                                            openMetadataProperty.description,
                                            isNullable);
    }

    /**
     * Return the list of column descriptions associated with this data source.  The information
     * should be sufficient to define the schema in a target data store.
     *
     * @return a list of column descriptions or null if not available.
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    @Override
    public List<TabularColumnDescription> getColumnDescriptions() throws ConnectorCheckedException
    {
        return columnDescriptions;
    }


    /**
     * Locate the named column. A negative number means the column is not present.
     *
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
     * @throws ConnectorCheckedException there is a problem accessing the data
     */
    public void setColumnDescriptions(List<TabularColumnDescription> columnDescriptions) throws ConnectorCheckedException
    {
        this.columnDescriptions = columnDescriptions;
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

        log.debug("Closing Structured File Store");
    }
}