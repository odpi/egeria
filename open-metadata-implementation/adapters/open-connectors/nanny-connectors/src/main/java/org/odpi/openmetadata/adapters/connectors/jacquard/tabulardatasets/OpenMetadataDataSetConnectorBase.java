/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.TabularDataSetConfigurationProperty;
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
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.SecretsCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.UserAccountProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.CyberLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.FixedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
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

        String serverName = super.getStringConfigurationProperty(TabularDataSetConfigurationProperty.SERVER_NAME.getName(), connectionBean.getConfigurationProperties());

        if (serverName == null)
        {
            throw new ConnectorCheckedException(TabularDataErrorCode.NULL_SERVER_NAME.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        int maxPageSize = super.getIntConfigurationProperty(TabularDataSetConfigurationProperty.MAX_PAGE_SIZE.getName(), connectionBean.getConfigurationProperties());

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
     * Extract the update time from the element header
     *
     * @param elementHeader header
     * @return date
     */
    private Date getUpdateTime(ElementControlHeader elementHeader)
    {
        if (elementHeader.getVersions().getUpdateTime() == null)
        {
            return elementHeader.getVersions().getCreateTime();
        }

        return elementHeader.getVersions().getUpdateTime();
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
                (ProductDataFieldDefinition.ACTION_REQUEST_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.ACTION_TARGET_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.ACTION_TARGET_RELATIONSHIP_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.ANNOTATION_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.REPORT_SUBJECT_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.CERTIFICATION_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.CERTIFICATION_TYPE_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.ELEMENT_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.REPORT_ORIGINATOR_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.EXCEPTION_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.EXCEPTION_TYPE_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.LICENSE_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.LICENSE_TYPE_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.PROFILE_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.PROJECT_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.SECRETS_COLLECTION_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.SECRETS_STORE_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.SEMANTIC_TERM_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.SURVEY_REPORT_GUID.getDisplayName().equals(columnName)) ||
                (ProductDataFieldDefinition.ANNOTATION_SUBJECT_GUID.getDisplayName().equals(columnName)))
        {
            recordValues.add(elementHeader.getGUID());
            return true;
        }
        else if ((ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME.getDisplayName().equals(columnName)) ||
                 (ProductDataFieldDefinition.ACTION_TARGET_TYPE_NAME.getDisplayName().equals(columnName)) ||
                 (ProductDataFieldDefinition.ANNOTATION_TYPE_NAME.getDisplayName().equals(columnName)) ||
                 (ProductDataFieldDefinition.ANNOTATION_SUBJECT_TYPE_NAME.getDisplayName().equals(columnName)) ||
                 (ProductDataFieldDefinition.PROFILE_TYPE_NAME.getDisplayName().equals(columnName)) ||
                 (ProductDataFieldDefinition.REPORT_SUBJECT_TYPE_NAME.getDisplayName().equals(columnName)))
        {
            recordValues.add(elementHeader.getType().getTypeName());
            return true;
        }
        else if (ProductDataFieldDefinition.ELEMENT_STATUS.getDisplayName().equals(columnName))
        {
            recordValues.add(elementHeader.getStatus().getDisplayName());
            return true;
        }
        else if (ProductDataFieldDefinition.CREATE_TIME.getDisplayName().equals(columnName))
        {
            Date createTime = elementHeader.getVersions().getCreateTime();
            recordValues.add(Long.toString(createTime.getTime()));
            return true;
        }
        else if (ProductDataFieldDefinition.UPDATE_TIME.getDisplayName().equals(columnName))
        {
            Date updateTime = this.getUpdateTime(elementHeader);
            recordValues.add(Long.toString(updateTime.getTime()));
            return true;
        }
        else  if (elementHeader.getLocationKinds() != null)
        {
            for (ElementClassification locationKind : elementHeader.getLocationKinds())
            {
                if (ProductDataFieldDefinition.LOCATION_KIND.getDisplayName().equals(columnName))
                {
                    recordValues.add(locationKind.getClassificationName());
                    return true;
                }
                else if (locationKind.getClassificationProperties() instanceof FixedLocationProperties fixedLocationProperties)
                {
                    if (ProductDataFieldDefinition.LOCATION_COORDINATES.getDisplayName().equals(columnName))
                    {
                        recordValues.add(fixedLocationProperties.getCoordinates());
                        return true;
                    }
                    else if (ProductDataFieldDefinition.LOCATION_MAP_PROJECTION.getDisplayName().equals(columnName))
                    {
                        recordValues.add(fixedLocationProperties.getMapProjection());
                        return true;
                    }
                    else if (ProductDataFieldDefinition.LOCATION_POSTAL_ADDRESS.getDisplayName().equals(columnName))
                    {
                        recordValues.add(fixedLocationProperties.getPostalAddress());
                        return true;
                    }
                }
                else if (locationKind.getClassificationProperties() instanceof CyberLocationProperties cyberLocationProperties)
                {
                    if (ProductDataFieldDefinition.NETWORK_ADDRESS.getDisplayName().equals(columnName))
                    {
                        recordValues.add(cyberLocationProperties.getNetworkAddress());
                        return true;
                    }
                }
            }
        }
        else if (ProductDataFieldDefinition.OWNER_GUID.getDisplayName().equals(columnName))
        {
            if ((elementHeader.getOwnership() != null) && (elementHeader.getOwnership().getClassificationProperties() instanceof OwnershipProperties ownershipProperties))
            {
                recordValues.add(ownershipProperties.getOwner());
                return true;
            }
        }
        else if (ProductDataFieldDefinition.ORIGIN_ORGANIZATION_GUID.getDisplayName().equals(columnName))
        {
            if ((elementHeader.getDigitalResourceOrigin() != null) && (elementHeader.getDigitalResourceOrigin().getClassificationProperties() instanceof DigitalResourceOriginProperties digitalResourceOriginProperties))
            {
                recordValues.add(digitalResourceOriginProperties.getOrganization());
                return true;
            }
        }
        else if (ProductDataFieldDefinition.ORIGIN_BUSINESS_CAPABILITY_GUID.getDisplayName().equals(columnName))
        {
            if ((elementHeader.getDigitalResourceOrigin() != null) && (elementHeader.getDigitalResourceOrigin().getClassificationProperties() instanceof DigitalResourceOriginProperties digitalResourceOriginProperties))
            {
                recordValues.add(digitalResourceOriginProperties.getBusinessCapability());
                return true;
            }
        }
        else if ((elementHeader.getSecurityListMembership() != null) && (elementHeader.getSecurityListMembership().getClassificationProperties() instanceof SecurityListMembershipProperties securityListMembershipProperties))
        {
            if (ProductDataFieldDefinition.SECURITY_ROLES.getDisplayName().equals(columnName))
            {
                if (securityListMembershipProperties.getSecurityRoles() != null)
                {
                    recordValues.add(securityListMembershipProperties.getSecurityRoles().toString());
                }
                else
                {
                    recordValues.add(null);
                }
                return true;
            }
            else if (ProductDataFieldDefinition.SECURITY_GROUPS.getDisplayName().equals(columnName))
            {
                if (securityListMembershipProperties.getSecurityGroups() != null)
                {
                    recordValues.add(securityListMembershipProperties.getSecurityGroups().toString());
                }
                else
                {
                    recordValues.add(null);
                }
            }
        }
        else if ((elementHeader.getUserAccountProfile() != null) && (elementHeader.getUserAccountProfile().getClassificationProperties() instanceof UserAccountProfileProperties userAccountProfileProperties))
        {
            if (ProductDataFieldDefinition.USER_ACCOUNT_COUNT.getDisplayName().equals(columnName))
            {
                recordValues.add(Long.toString(userAccountProfileProperties.getUserAccountCount()));
                return true;
            }
            else if (ProductDataFieldDefinition.EMPLOYEE_ACCOUNT_COUNT.getDisplayName().equals(columnName))
            {
                recordValues.add(Long.toString(userAccountProfileProperties.getEmployeeAccountCount()));
                return true;
            }
            else if (ProductDataFieldDefinition.CONTRACTOR_ACCOUNT_COUNT.getDisplayName().equals(columnName))
            {
                recordValues.add(Long.toString(userAccountProfileProperties.getContractorAccountCount()));
                return true;
            }
            else if (ProductDataFieldDefinition.EXTERNAL_ACCOUNT_COUNT.getDisplayName().equals(columnName))
            {
                recordValues.add(Long.toString(userAccountProfileProperties.getExternalAccountCount()));
                return true;
            }
            else if (ProductDataFieldDefinition.DIGITAL_ACCOUNT_COUNT.getDisplayName().equals(columnName))
            {
                recordValues.add(Long.toString(userAccountProfileProperties.getDigitalAccountCount()));
                return true;
            }
            else if (ProductDataFieldDefinition.ACTIVE_ACCOUNT_COUNT.getDisplayName().equals(columnName))
            {
                recordValues.add(Long.toString(userAccountProfileProperties.getActiveAccountCount()));
                return true;
            }
            else if (ProductDataFieldDefinition.EXPIRED_ACCOUNT_COUNT.getDisplayName().equals(columnName))
            {
                recordValues.add(Long.toString(userAccountProfileProperties.getExpiredAccountCount()));
                return true;
            }
            else if (ProductDataFieldDefinition.LOCKED_ACCOUNT_COUNT.getDisplayName().equals(columnName))
            {
                recordValues.add(Long.toString(userAccountProfileProperties.getLockedAccountCount()));
                return true;
            }
            else if (ProductDataFieldDefinition.DISABLED_ACCOUNT_COUNT.getDisplayName().equals(columnName))
            {
                recordValues.add(Long.toString(userAccountProfileProperties.getDisabledAccountCount()));
                return true;
            }
        }
        else if ((elementHeader.getZoneMembershipProfile() != null) && (elementHeader.getZoneMembershipProfile().getClassificationProperties() instanceof ZoneMembershipProfileProperties zoneMembershipProfileProperties))
        {
            if (ProductDataFieldDefinition.TOTAL_ELEMENT_MEMBERSHIP.getDisplayName().equals(columnName))
            {
                recordValues.add(Long.toString(zoneMembershipProfileProperties.getTotalMembership()));
                return true;
            }
            else if (ProductDataFieldDefinition.ANCHORED_ELEMENT_MEMBERSHIP.getDisplayName().equals(columnName))
            {
                recordValues.add(Long.toString(zoneMembershipProfileProperties.getAnchoredTotalMembership()));
                return true;
            }
            else if (ProductDataFieldDefinition.ALL_ELEMENT_MEMBERSHIP.getDisplayName().equals(columnName))
            {
                recordValues.add(Long.toString(zoneMembershipProfileProperties.getAllTotalMembership()));
                return true;
            }
        }
        else if (ProductDataFieldDefinition.ZONE_MEMBERSHIP.getDisplayName().equals(columnName))
        {
            if ((elementHeader.getZoneMembership() != null) && (elementHeader.getZoneMembership().getClassificationProperties() instanceof ZoneMembershipProperties zoneMembershipProperties))
            {
                if (zoneMembershipProperties.getZoneMembership() != null)
                {
                    recordValues.add(zoneMembershipProperties.getZoneMembership().toString());
                }
                else
                {
                    recordValues.add(null);
                }
            }
            else if ((elementHeader.getAnchor() != null) && (elementHeader.getAnchor().getClassificationProperties() instanceof AnchorsProperties anchorsProperties))
            {
                if (anchorsProperties.getZoneMembership() != null)
                {
                    recordValues.add(anchorsProperties.getZoneMembership().toString());
                }
                else
                {
                    recordValues.add(null);
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
            if (ProductDataFieldDefinition.ADDITIONAL_PROPERTIES.getDisplayName().equals(columnName))
            {
                if (referenceableProperties.getAdditionalProperties() != null)
                {
                    recordValues.add(referenceableProperties.getAdditionalProperties().toString());
                }
                else
                {
                    recordValues.add("");
                }
                return true;
            }
            else if (ProductDataFieldDefinition.QUALIFIED_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(referenceableProperties.getQualifiedName());
                return true;
            }
            else if ((ProductDataFieldDefinition.DISPLAY_NAME.getDisplayName().equals(columnName)) ||
                     (ProductDataFieldDefinition.PROFILE_NAME.getDisplayName().equals(columnName)))
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
            else if (ProductDataFieldDefinition.VERSION_IDENTIFIER.getDisplayName().equals(columnName))
            {
                recordValues.add(referenceableProperties.getVersionIdentifier());
                return true;
            }
            else if (ProductDataFieldDefinition.URL.getDisplayName().equals(columnName))
            {
                recordValues.add(referenceableProperties.getURL());
                return true;
            }
            else if (referenceableProperties instanceof ActorProperties actorProperties)
            {
                if (actorProperties instanceof UserIdentityProperties userIdentityProperties)
                {
                    if (ProductDataFieldDefinition.USER_ID.getDisplayName().equals(columnName))
                    {
                        recordValues.add(userIdentityProperties.getUserId());
                    }
                    else if (ProductDataFieldDefinition.DISTINGUISHED_NAME.getDisplayName().equals(columnName))
                    {
                        recordValues.add(userIdentityProperties.getDistinguishedName());
                    }
                }
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
                        recordValues.add(authoredReferenceableProperties.getContentStatus().name());
                    }
                    else
                    {
                        recordValues.add(null);
                    }
                    return true;
                }
                else if (referenceableProperties instanceof GovernanceDefinitionProperties governanceDefinitionProperties)
                {
                    if (ProductDataFieldDefinition.DOMAIN_IDENTIFIER.getDisplayName().equals(columnName))
                    {
                        recordValues.add(Integer.toString(governanceDefinitionProperties.getDomainIdentifier()));
                        return true;
                    }
                    else if (ProductDataFieldDefinition.SUMMARY.getDisplayName().equals(columnName))
                    {
                        recordValues.add(governanceDefinitionProperties.getSummary());
                        return true;
                    }
                    else if (ProductDataFieldDefinition.SCOPE.getDisplayName().equals(columnName))
                    {
                        recordValues.add(governanceDefinitionProperties.getScope());
                        return true;
                    }
                    else if (ProductDataFieldDefinition.USAGE.getDisplayName().equals(columnName))
                    {
                        recordValues.add(governanceDefinitionProperties.getUsage());
                        return true;
                    }
                    else if (governanceDefinitionProperties instanceof GovernanceControlProperties governanceControlProperties)
                    {
                        if (governanceControlProperties instanceof SecurityAccessControlProperties securityAccessControlProperties)
                        {
                            if (securityAccessControlProperties instanceof GovernanceZoneProperties governanceZoneProperties)
                            {
                                if (ProductDataFieldDefinition.CRITERIA.getDisplayName().equals(columnName))
                                {
                                    recordValues.add(governanceZoneProperties.getCriteria());
                                    return true;
                                }
                            }
                        }
                    }
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
                    else if (ProductDataFieldDefinition.NAMESPACE_PATH.getDisplayName().equals(columnName))
                    {
                        recordValues.add(validValueDefinitionProperties.getNamespacePath());
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
                if (ProductDataFieldDefinition.DEPLOYED_IMPLEMENTATION_TYPE.getDisplayName().equals(columnName))
                {
                    recordValues.add(assetProperties.getDeployedImplementationType());
                    return true;
                }
                else if (ProductDataFieldDefinition.NAMESPACE_PATH.getDisplayName().equals(columnName))
                {
                    recordValues.add(assetProperties.getNamespacePath());
                    return true;
                }
                else if ((ProductDataFieldDefinition.RESOURCE_NAME.getDisplayName().equals(columnName)) ||
                        (ProductDataFieldDefinition.SECRETS_COLLECTION_NAME.getDisplayName().equals(columnName)))
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
                            recordValues.add(dataAssetProperties.getContentStatus().name());
                        }
                        else
                        {
                            recordValues.add(null);
                        }
                        return true;
                    }
                    else if (dataAssetProperties instanceof DataSetProperties dataSetProperties)
                    {
                        if (dataSetProperties instanceof ReportProperties reportProperties)
                        {
                            if (ProductDataFieldDefinition.PURPOSE.getDisplayName().equals(columnName))
                            {
                                recordValues.add(reportProperties.getPurpose());
                            }
                        }
                        else if (dataSetProperties instanceof SecretsCollectionProperties secretsCollectionProperties)
                        {
                            if (ProductDataFieldDefinition.REFRESH_TIME_INTERVAL.getDisplayName().equals(columnName))
                            {
                                recordValues.add(Long.toString(secretsCollectionProperties.getRefreshTimeInterval()));
                            }
                        }
                    }
                }
                else if (assetProperties instanceof ProcessProperties processProperties)
                {
                    if (ProductDataFieldDefinition.EXPECTED_BEHAVIOUR.getDisplayName().equals(columnName))
                    {
                        recordValues.add(processProperties.getExpectedBehaviour());
                    }
                }

            }

        }

        return false;
    }


    /**
     * Extracts the record values from the element properties based on the specified column name.
     *
     * @param relationshipBeanProperties properties object
     * @param columnName name of column to extract
     * @param recordValues array of values to append to
     * @return true if the value was successfully extracted, false otherwise
     */
    protected boolean getElementRecordValue(RelationshipBeanProperties relationshipBeanProperties,
                                            String                     columnName,
                                            List<String>               recordValues)
    {
        if (relationshipBeanProperties instanceof ActionTargetProperties actionTargetProperties)
        {
            if (ProductDataFieldDefinition.ACTION_TARGET_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(actionTargetProperties.getActionTargetName());
                return true;
            }
        }
        else if (relationshipBeanProperties instanceof CertificationProperties certificationProperties)
        {
            if (ProductDataFieldDefinition.COVERAGE_START.getDisplayName().equals(columnName))
            {
                if (certificationProperties.getCoverageStart() != null)
                {
                    recordValues.add(certificationProperties.getCoverageStart().toString());
                }
                else
                {
                    recordValues.add(null);
                }
                return true;
            }
            else if (ProductDataFieldDefinition.COVERAGE_END.getDisplayName().equals(columnName))
            {
                if (certificationProperties.getCoverageEnd() != null)
                {
                    recordValues.add(certificationProperties.getCoverageEnd().toString());
                }
                else
                {
                    recordValues.add(null);
                }
                return true;
            }
            else if (ProductDataFieldDefinition.CERTIFIED_BY.getDisplayName().equals(columnName))
            {
                recordValues.add(certificationProperties.getCertifiedBy());
                return true;
            }
            else if (ProductDataFieldDefinition.CERTIFIED_BY_TYPE_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(certificationProperties.getCertifiedByTypeName());
                return true;
            }
            else if (ProductDataFieldDefinition.CERTIFIED_BY_PROPERTY_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(certificationProperties.getCertifiedByPropertyName());
                return true;
            }
            else if (ProductDataFieldDefinition.CUSTODIAN.getDisplayName().equals(columnName))
            {
                recordValues.add(certificationProperties.getCustodian());
                return true;
            }
            else if (ProductDataFieldDefinition.CUSTODIAN_TYPE_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(certificationProperties.getCustodianTypeName());
                return true;
            }
            else if (ProductDataFieldDefinition.CUSTODIAN_PROPERTY_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(certificationProperties.getCustodianPropertyName());
                return true;
            }
            else if (ProductDataFieldDefinition.RECIPIENT.getDisplayName().equals(columnName))
            {
                recordValues.add(certificationProperties.getRecipient());
                return true;
            }
            else if (ProductDataFieldDefinition.RECIPIENT_TYPE_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(certificationProperties.getRecipientTypeName());
                return true;
            }
            else if (ProductDataFieldDefinition.RECIPIENT_PROPERTY_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(certificationProperties.getRecipientPropertyName());
                return true;
            }
            else if (ProductDataFieldDefinition.NOTES.getDisplayName().equals(columnName))
            {
                recordValues.add(certificationProperties.getNotes());
                return true;
            }
        }
        else if (relationshipBeanProperties instanceof LabeledRelationshipProperties labeledRelationshipProperties)
        {
            if (ProductDataFieldDefinition.LABEL.getDisplayName().equals(columnName))
            {
                recordValues.add(labeledRelationshipProperties.getLabel());
                return true;
            }
            else if (ProductDataFieldDefinition.DESCRIPTION.getDisplayName().equals(columnName))
            {
                recordValues.add(labeledRelationshipProperties.getDescription());
                return true;
            }

            if (labeledRelationshipProperties instanceof ExceptionProperties exceptionProperties)
            {
                if (ProductDataFieldDefinition.LAST_REVIEW_TIME.getDisplayName().equals(columnName))
                {
                    if (exceptionProperties.getLastReviewTime() != null)
                    {
                        recordValues.add(exceptionProperties.getLastReviewTime().toString());
                    }
                    else
                    {
                        recordValues.add(null);
                    }
                    return true;
                }
                else if (ProductDataFieldDefinition.REVIEW_DATE.getDisplayName().equals(columnName))
                {
                    if (exceptionProperties.getReviewDate() != null)
                    {
                        recordValues.add(exceptionProperties.getReviewDate().toString());
                    }
                    else
                    {
                        recordValues.add(null);
                    }
                    return true;
                }
                else if (ProductDataFieldDefinition.NOTES.getDisplayName().equals(columnName))
                {
                    recordValues.add(exceptionProperties.getNotes());
                    return true;
                }
                else if (ProductDataFieldDefinition.STEWARD.getDisplayName().equals(columnName))
                {
                    recordValues.add(exceptionProperties.getSteward());
                    return true;
                }
                else if (ProductDataFieldDefinition.STEWARD_TYPE_NAME.getDisplayName().equals(columnName))
                {
                    recordValues.add(exceptionProperties.getStewardTypeName());
                    return true;
                }
                else if (ProductDataFieldDefinition.STEWARD_PROPERTY_NAME.getDisplayName().equals(columnName))
                {
                    recordValues.add(exceptionProperties.getStewardPropertyName());
                    return true;
                }
            }
        }
        else if (relationshipBeanProperties instanceof LicenseProperties licenseProperties)
        {
            if (ProductDataFieldDefinition.COVERAGE_START.getDisplayName().equals(columnName))
            {
                if (licenseProperties.getCoverageStart() != null)
                {
                    recordValues.add(licenseProperties.getCoverageStart().toString());
                }
                else
                {
                    recordValues.add(null);
                }
                return true;
            }
            else if (ProductDataFieldDefinition.COVERAGE_END.getDisplayName().equals(columnName))
            {
                if (licenseProperties.getCoverageEnd() != null)
                {
                    recordValues.add(licenseProperties.getCoverageEnd().toString());
                }
                else
                {
                    recordValues.add(null);
                }
                return true;
            }
            else if (ProductDataFieldDefinition.LICENSED_BY.getDisplayName().equals(columnName))
            {
                recordValues.add(licenseProperties.getLicensedBy());
                return true;
            }
            else if (ProductDataFieldDefinition.LICENSED_BY_TYPE_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(licenseProperties.getLicensedByTypeName());
                return true;
            }
            else if (ProductDataFieldDefinition.LICENSED_BY_PROPERTY_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(licenseProperties.getLicensedByPropertyName());
                return true;
            }
            else if (ProductDataFieldDefinition.CUSTODIAN.getDisplayName().equals(columnName))
            {
                recordValues.add(licenseProperties.getCustodian());
                return true;
            }
            else if (ProductDataFieldDefinition.CUSTODIAN_TYPE_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(licenseProperties.getCustodianTypeName());
                return true;
            }
            else if (ProductDataFieldDefinition.CUSTODIAN_PROPERTY_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(licenseProperties.getCustodianPropertyName());
                return true;
            }
            else if (ProductDataFieldDefinition.LICENSEE.getDisplayName().equals(columnName))
            {
                recordValues.add(licenseProperties.getLicensee());
                return true;
            }
            else if (ProductDataFieldDefinition.LICENSEE_TYPE_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(licenseProperties.getLicenseeTypeName());
                return true;
            }
            else if (ProductDataFieldDefinition.LICENSEE_PROPERTY_NAME.getDisplayName().equals(columnName))
            {
                recordValues.add(licenseProperties.getLicenseePropertyName());
                return true;
            }
            else if (ProductDataFieldDefinition.NOTES.getDisplayName().equals(columnName))
            {
                recordValues.add(licenseProperties.getNotes());
                return true;
            }
        }


        return false;
    }


    /**
     * Extracts the record values from the element properties based on the specified column name.
     *
     * @param rootElement element to convert.
     * @param columnName name of column to extract
     * @param recordValues array of values to append to
     * @return true if the value was successfully extracted, false otherwise
     */
    protected boolean getRelatedElementRecordValue(OpenMetadataRootElement rootElement,
                                                   String                     columnName,
                                                   List<String>               recordValues)
    {
        if (ProductDataFieldDefinition.REPORT_SUBJECT_GUID.getDisplayName().equals(columnName))
        {
            if (rootElement.getReportSubjects() != null)
            {
                return this.getRelatedElementGUIDRecordValue(rootElement.getReportSubjects(), recordValues);
            }
            else
            {
                recordValues.add(null);
            }
            return true;
        }

        return false;
    }


    /**
     * Extracts the related element GUID from the first, non-null related element.
     *
     * @param relatedElements list of returned related elements
     * @param recordValues    array of values to append to
     * @return was a property added?
     */
    protected boolean getRelatedElementGUIDRecordValue(List<RelatedMetadataElementSummary> relatedElements,
                                                       List<String>                        recordValues)
    {
        if (relatedElements != null)
        {
            for (RelatedMetadataElementSummary relatedElement : relatedElements)
            {
                if (relatedElement != null)
                {
                    recordValues.add(relatedElement.getRelatedElement().getElementHeader().getGUID());
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Convert the root element to a list of column values based on the data spec.
     *
     * @param rootElement element to convert.
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
                 * If the property is not in the header, look into the properties.  If neither place throw and exception.
                 */
                if (! getElementHeaderRecordValue(rootElement.getElementHeader(), tabularColumnDescription.columnName(), recordValues))
                {
                    if (! getElementRecordValue(rootElement.getProperties(), tabularColumnDescription.columnName(), recordValues))
                    {
                        if (! getRelatedElementRecordValue(rootElement, tabularColumnDescription.columnName(), recordValues))
                        {
                            if (tabularColumnDescription.isNullable())
                            {
                                recordValues.add(null);
                            }
                            else
                            {
                                throw new ConnectorCheckedException(TabularDataErrorCode.UNMAPPED_COLUMN.getMessageDefinition(connectorName,
                                                                                                                              tabularColumnDescription.columnName()),
                                                                    this.getClass().getName(),
                                                                    methodName);
                            }
                        }
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
     * Convert the root element to a list of column values based on the data spec.
     *
     * @param relationshipSummary valid value that is a member of the valid value set.
     * @return list of column value
     * @throws ConnectorCheckedException an unexpected exception has occurred
     */
    protected List<String> getRecordValues(MetadataRelationshipSummary relationshipSummary) throws ConnectorCheckedException
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
                if (! getElementHeaderRecordValue(relationshipSummary.getRelationshipHeader(), tabularColumnDescription.columnName(), recordValues))
                {
                    if (! getElementRecordValue(relationshipSummary.getRelationshipProperties(), tabularColumnDescription.columnName(), recordValues))
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