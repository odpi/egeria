/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.productfamily;

import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataDataSetConnectorBase;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.TabularDataSetConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataAuditCode;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataCollection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDuplicateLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.StatusIdentifier;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.EgeriaConnectedAssetClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * DigitalProductFamilyDataSetCollectionConnector presents the products of a digital product family as one
 * collection of tabular data sets - a table per product.  It is the connector behind the asset Jacquard attaches
 * to each product family, and it is what a subscription to the family is provisioned from: a provisioning
 * service asks it which tables there are, brings each into focus in turn, and copies it.
 * <br><br>
 * The family is named in the connection's configuration properties.  On start the connector walks the family's
 * members - and the members of any family nested in it - and for each product finds the asset that carries the
 * product's data and builds that asset's own connector.  Nothing is started at that point: a family may have
 * many members, and each member's connector loads its data when started, so members are started one at a time
 * as they are brought into focus and disconnected when the focus moves on.
 * <br><br>
 * A product that appears twice - two writers created it at the same instant - presents the same table name twice.
 * The first copy is presented and the two assets are linked as peer duplicates in DISCOVERED status, which is
 * what the Mendel Automated Duplicate Manager works from to confirm and consolidate duplicates; the connector
 * itself does not choose between them.
 * <br><br>
 * The list of members is taken when the connector starts and again on {@link #refreshCache()}.  A product added
 * to the family after that is seen the next time the connector is started, which for a subscription is the
 * next time it is provisioned.
 */
public class DigitalProductFamilyDataSetCollectionConnector extends OpenMetadataDataSetConnectorBase implements ReadableTabularDataCollection
{
    private static final String myConnectorName = "DigitalProductFamilyDataSetCollectionConnector";

    private String               familyGUID           = null;
    private String               familyName           = null;
    private ConnectedAssetClient connectedAssetClient = null;

    /*
     * The members in the order they were found, keyed by the table name each presents.
     */
    private final Map<String, MemberDataSet> membersByTableName = new LinkedHashMap<>();

    private MemberDataSet memberInFocus = null;


    /**
     * One product's data set: where it comes from and, while it is in focus, the connector that reads it.
     */
    private static class MemberDataSet
    {
        private final String    assetGUID;
        private final String    productGUID;
        private final String    productQualifiedName;
        private final String    productName;
        private final String    tableName;
        private       Connector connector;
        private       boolean   started = false;

        MemberDataSet(String assetGUID, String productGUID, String productQualifiedName, String productName, String tableName, Connector connector)
        {
            this.assetGUID            = assetGUID;
            this.productGUID          = productGUID;
            this.productQualifiedName = productQualifiedName;
            this.productName          = productName;
            this.tableName            = tableName;
            this.connector            = connector;
        }
    }


    /**
     * Default constructor.
     */
    public DigitalProductFamilyDataSetCollectionConnector()
    {
        super(myConnectorName);
    }


    /**
     * Connect to the metadata access server named in the connection, find the family, and list its members.
     *
     * @throws ConnectorCheckedException the connection does not name a family, or the family cannot be read
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws UserNotAuthorizedException, ConnectorCheckedException
    {
        final String methodName = "start";

        super.start();

        familyGUID = super.getStringConfigurationProperty(TabularDataSetConfigurationProperty.STARTING_ELEMENT_GUID.getName(),
                                                          connectionBean.getConfigurationProperties());

        if (familyGUID == null)
        {
            throw new ConnectorCheckedException(TabularDataErrorCode.NULL_STARTING_ELEMENT.getMessageDefinition(connectorName,
                                                                                                                 TabularDataSetConfigurationProperty.STARTING_ELEMENT_GUID.getName()),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * The members' connectors are built the same way a governance service builds a connector for an asset:
         * from the asset's connection, through the metadata server's connected-asset service.  This client
         * talks to the same server as the metadata client the base class set up, with the same secrets.
         */
        try
        {
            Endpoint endpoint      = connectionBean.getEndpoint();
            String   serverName    = super.getStringConfigurationProperty(TabularDataSetConfigurationProperty.SERVER_NAME.getName(), connectionBean.getConfigurationProperties());
            int      maxPageSize   = super.getIntConfigurationProperty(TabularDataSetConfigurationProperty.MAX_PAGE_SIZE.getName(), connectionBean.getConfigurationProperties());

            connectedAssetClient = new EgeriaConnectedAssetClient(serverName,
                                                                  endpoint.getNetworkAddress(),
                                                                  secretsStoreConnectorMap,
                                                                  maxPageSize,
                                                                  auditLog);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(TabularDataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }

        this.refreshCache();
    }


    /**
     * Walk the family again and rebuild the list of members.  Any member in focus is released.
     *
     * @throws ConnectorCheckedException the family cannot be read
     */
    @Override
    public void refreshCache() throws ConnectorCheckedException
    {
        final String methodName = "refreshCache";

        this.releaseFocus();
        membersByTableName.clear();

        try
        {
            CollectionClient collectionClient = connectorContext.getCollectionClient();
            GetOptions       headerAndProperties = super.getHeaderAndPropertiesOptions(collectionClient);

            OpenMetadataRootElement family = collectionClient.getCollectionByGUID(familyGUID, headerAndProperties);

            if ((family == null) || (! propertyHelper.isTypeOf(family.getElementHeader(), OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName)))
            {
                throw new ConnectorCheckedException(TabularDataErrorCode.NOT_A_PRODUCT_FAMILY.getMessageDefinition(connectorName,
                                                                                                                   familyGUID,
                                                                                                                   (family == null) ? "missing element" : family.getElementHeader().getType().getTypeName()),
                                                    this.getClass().getName(),
                                                    methodName);
            }

            familyName = this.getProductName(family);

            this.addMembersOfFamily(collectionClient, family, new HashSet<>());

            super.logRecord(methodName, TabularDataAuditCode.FAMILY_MEMBERS_LOADED.getMessageDefinition(connectorName,
                                                                                                        Integer.toString(membersByTableName.size()),
                                                                                                        familyName,
                                                                                                        familyGUID));
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
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
     * Add the data sets of every product in a family, walking into any family nested in it.
     *
     * @param collectionClient client for reading collection members
     * @param family the family to walk
     * @param familiesWalked the families already walked, so a family that contains itself through a cycle is
     *                       walked once
     * @throws Exception problem reading the family's members
     */
    private void addMembersOfFamily(CollectionClient        collectionClient,
                                    OpenMetadataRootElement family,
                                    Set<String>             familiesWalked) throws Exception
    {
        if (! familiesWalked.add(family.getElementHeader().getGUID()))
        {
            return;
        }

        for (OpenMetadataRootElement member : this.getCollectionMembers(collectionClient, family.getElementHeader().getGUID()))
        {
            /*
             * A family's members are products and families.  An asset directly under a family is the family's
             * own collection asset - the one this connector is behind - and is not a member's data.
             */
            if (propertyHelper.isTypeOf(member.getElementHeader(), OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName))
            {
                this.addMembersOfFamily(collectionClient, member, familiesWalked);
            }
            else if (propertyHelper.isTypeOf(member.getElementHeader(), OpenMetadataType.DIGITAL_PRODUCT.typeName))
            {
                this.addProductDataSets(collectionClient, member);
            }
        }
    }


    /**
     * Add the data sets of one product: each asset that is a member of the product.
     *
     * @param collectionClient client for reading collection members
     * @param product the product
     * @throws Exception problem reading the product's members
     */
    private void addProductDataSets(CollectionClient        collectionClient,
                                    OpenMetadataRootElement product) throws Exception
    {
        final String methodName = "addProductDataSets";

        String productGUID          = product.getElementHeader().getGUID();
        String productName          = this.getProductName(product);
        String productQualifiedName = this.getProductQualifiedName(product);

        for (OpenMetadataRootElement member : this.getCollectionMembers(collectionClient, productGUID))
        {
            if (propertyHelper.isTypeOf(member.getElementHeader(), OpenMetadataType.ASSET.typeName))
            {
                String assetGUID = member.getElementHeader().getGUID();

                try
                {
                    Connector connector = connectedAssetClient.getConnectorForAsset(super.getClientUserId(), assetGUID, auditLog);

                    if (connector instanceof ReadableTabularDataSource readableTabularDataSource)
                    {
                        String tableName = this.getTableName(readableTabularDataSource);

                        MemberDataSet existingMember = membersByTableName.get(tableName);

                        if (existingMember == null)
                        {
                            membersByTableName.put(tableName, new MemberDataSet(assetGUID, productGUID, productQualifiedName, productName, tableName, connector));
                        }
                        else if ((productQualifiedName != null) && (productQualifiedName.equals(existingMember.productQualifiedName)))
                        {
                            /*
                             * Two products with the same qualified name are two copies of one product - two
                             * writers created it at the same time, which a federated environment cannot rule
                             * out.  Duplicates are the platform's business: the two assets are linked as peer
                             * duplicates, which is what the duplicate manager works from, and the first copy is
                             * presented so that the family's data is still delivered.
                             */
                            this.linkAsPeerDuplicates(existingMember.assetGUID, assetGUID, productQualifiedName);

                            super.logRecord(methodName, TabularDataAuditCode.FAMILY_MEMBER_DUPLICATE_TABLE.getMessageDefinition(connectorName,
                                                                                                                                assetGUID,
                                                                                                                                productName,
                                                                                                                                familyName,
                                                                                                                                existingMember.assetGUID,
                                                                                                                                productQualifiedName));
                            this.disconnectQuietly(connector);
                        }
                        else
                        {
                            super.logRecord(methodName, TabularDataAuditCode.FAMILY_MEMBER_TABLE_NAME_CLASH.getMessageDefinition(connectorName,
                                                                                                                                 assetGUID,
                                                                                                                                 productName,
                                                                                                                                 familyName,
                                                                                                                                 tableName,
                                                                                                                                 existingMember.assetGUID));
                            this.disconnectQuietly(connector);
                        }
                    }
                    else
                    {
                        super.logRecord(methodName, TabularDataAuditCode.FAMILY_MEMBER_NOT_TABULAR.getMessageDefinition(connectorName,
                                                                                                                        assetGUID,
                                                                                                                        productName,
                                                                                                                        familyName,
                                                                                                                        (connector == null) ? "null" : connector.getClass().getName()));
                        this.disconnectQuietly(connector);
                    }
                }
                catch (Exception error)
                {
                    /*
                     * One product whose asset cannot be read is left out, and the rest of the family is still
                     * presented; the problem is logged against the product so it can be put right.
                     */
                    super.logExceptionRecord(methodName,
                                             TabularDataAuditCode.FAMILY_MEMBER_UNREADABLE.getMessageDefinition(connectorName,
                                                                                                                assetGUID,
                                                                                                                productName,
                                                                                                                familyName,
                                                                                                                error.getClass().getName(),
                                                                                                                error.getMessage()),
                                             error);
                }
            }
        }
    }


    /**
     * Return the table name a member's connector presents.  Jacquard's data set connectors know their table name
     * from their product definition before they are started; a connector that has to be started to answer is
     * started, asked, and stopped again, so that no member is left running until it is in focus.
     *
     * @param readableTabularDataSource the member's connector
     * @return table name
     * @throws ConnectorCheckedException the connector cannot say
     */
    private String getTableName(ReadableTabularDataSource readableTabularDataSource) throws ConnectorCheckedException
    {
        try
        {
            return readableTabularDataSource.getTableName();
        }
        catch (ConnectorCheckedException error)
        {
            if (readableTabularDataSource instanceof Connector connector)
            {
                try
                {
                    connector.start();
                    return readableTabularDataSource.getTableName();
                }
                catch (UserNotAuthorizedException startError)
                {
                    throw error;
                }
                finally
                {
                    this.disconnectQuietly(connector);
                }
            }

            throw error;
        }
    }


    /**
     * Read every member of a collection, page by page.
     *
     * @param collectionClient client
     * @param collectionGUID collection to read
     * @return members - header and properties only
     * @throws Exception problem reading the collection
     */
    private List<OpenMetadataRootElement> getCollectionMembers(CollectionClient collectionClient,
                                                               String           collectionGUID) throws Exception
    {
        List<OpenMetadataRootElement> members   = new ArrayList<>();
        int                           pageSize  = connectorContext.getMaxPageSize();
        int                           startFrom = 0;

        while (true)
        {
            QueryOptions queryOptions = super.getHeaderAndPropertiesQueryOptions(collectionClient, startFrom, pageSize);

            List<OpenMetadataRootElement> page = collectionClient.getCollectionMembers(collectionGUID, queryOptions);

            if ((page == null) || (page.isEmpty()))
            {
                break;
            }

            for (OpenMetadataRootElement member : page)
            {
                if (member != null)
                {
                    members.add(member);
                }
            }

            if ((pageSize <= 0) || (page.size() < pageSize))
            {
                break;
            }

            startFrom = startFrom + page.size();
        }

        return members;
    }


    /**
     * Return a product's qualified name, or null if it has none.
     *
     * @param product the product's element
     * @return qualified name
     */
    private String getProductQualifiedName(OpenMetadataRootElement product)
    {
        if (product.getProperties() instanceof DigitalProductProperties digitalProductProperties)
        {
            return digitalProductProperties.getQualifiedName();
        }

        return null;
    }


    /**
     * Link two assets that are copies of one product's data set as peer duplicates in DISCOVERED status, unless
     * they are already linked, so that the duplicate manager can confirm and consolidate them.  A failure to
     * link is logged and does not stop the family being presented.
     *
     * @param assetGUID the asset presented
     * @param duplicateAssetGUID the copy that is not
     * @param productQualifiedName the qualified name both products carry
     */
    private void linkAsPeerDuplicates(String assetGUID,
                                      String duplicateAssetGUID,
                                      String productQualifiedName)
    {
        final String methodName = "linkAsPeerDuplicates";

        try
        {
            ClassificationExplorerClient classificationExplorerClient = connectorContext.getClassificationExplorerClient();

            List<OpenMetadataRootElement> existingPeers = classificationExplorerClient.getRelatedRootElements(assetGUID,
                                                                                                             0,
                                                                                                             OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
                                                                                                             super.getHeaderAndPropertiesQueryOptions(classificationExplorerClient, 0, 0));

            if (existingPeers != null)
            {
                for (OpenMetadataRootElement existingPeer : existingPeers)
                {
                    if ((existingPeer != null) && (duplicateAssetGUID.equals(existingPeer.getElementHeader().getGUID())))
                    {
                        return;
                    }
                }
            }

            PeerDuplicateLinkProperties properties = new PeerDuplicateLinkProperties();

            properties.setStatusIdentifier(StatusIdentifier.DISCOVERED.getOrdinal());
            properties.setSource(connectorName);
            properties.setNotes("Both assets belong to a product with qualified name " + productQualifiedName + " in digital product family " + familyName + ".");

            classificationExplorerClient.linkElementsAsPeerDuplicates(assetGUID,
                                                                      duplicateAssetGUID,
                                                                      properties,
                                                                      new MakeAnchorOptions(classificationExplorerClient.getMetadataSourceOptions()));
        }
        catch (Exception error)
        {
            super.logExceptionRecord(methodName,
                                     TabularDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                    error.getClass().getName(),
                                                                                                    methodName,
                                                                                                    error.getMessage()),
                                     error);
        }
    }


    /**
     * Return the name to call a product by in messages.
     *
     * @param product the product's element
     * @return its display name, falling back to its qualified name and then its GUID
     */
    private String getProductName(OpenMetadataRootElement product)
    {
        if (product.getProperties() instanceof DigitalProductProperties digitalProductProperties)
        {
            if (digitalProductProperties.getDisplayName() != null)
            {
                return digitalProductProperties.getDisplayName();
            }
            else if (digitalProductProperties.getQualifiedName() != null)
            {
                return digitalProductProperties.getQualifiedName();
            }
        }

        return product.getElementHeader().getGUID();
    }


    /**
     * Return the names of the tables in this collection: one per product in the family that has a readable
     * data set, in the order the products were found.
     *
     * @return table names in canonical word format
     */
    @Override
    public List<String> getTableNames()
    {
        return new ArrayList<>(membersByTableName.keySet());
    }


    /**
     * Bring one of the family's data sets into focus, starting its connector and stopping the connector of
     * whichever data set was in focus before.  The description is the destination's concern and is ignored here.
     *
     * @param tableName name of the table, as returned by getTableNames()
     * @param tableDescription ignored
     * @throws ConnectorCheckedException the name is not one of this family's tables, or the member's connector
     *                                   could not be started
     */
    @Override
    public void setTableName(String tableName,
                             String tableDescription) throws ConnectorCheckedException
    {
        final String methodName = "setTableName";

        MemberDataSet member = membersByTableName.get(tableName);

        if (member == null)
        {
            throw new ConnectorCheckedException(TabularDataErrorCode.UNKNOWN_TABLE_NAME.getMessageDefinition(connectorName,
                                                                                                             tableName,
                                                                                                             familyName,
                                                                                                             this.getTableNames().toString()),
                                                this.getClass().getName(),
                                                methodName);
        }

        if (member == memberInFocus)
        {
            return;
        }

        this.releaseFocus();

        try
        {
            if (member.connector == null)
            {
                member.connector = connectedAssetClient.getConnectorForAsset(super.getClientUserId(), member.assetGUID, auditLog);
            }

            member.connector.start();
            member.started = true;
            memberInFocus  = member;
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(TabularDataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName + "(" + tableName + ")",
                                                                                                               error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Stop the connector of the member in focus, if any.  It is built again if the member comes back into focus.
     */
    private void releaseFocus()
    {
        if (memberInFocus != null)
        {
            if (memberInFocus.started)
            {
                this.disconnectQuietly(memberInFocus.connector);
                memberInFocus.connector = null;
                memberInFocus.started   = false;
            }

            memberInFocus = null;
        }
    }


    /**
     * Disconnect a connector, ignoring any complaint it makes on the way out.
     *
     * @param connector connector to disconnect - may be null
     */
    private void disconnectQuietly(Connector connector)
    {
        if (connector != null)
        {
            try
            {
                connector.disconnect();
            }
            catch (Exception error)
            {
                // the connector is being discarded; its complaint changes nothing
            }
        }
    }


    /**
     * Return the connector of the data set in focus.
     *
     * @param methodName calling method, for the error
     * @return readable data source
     * @throws ConnectorCheckedException nothing is in focus
     */
    private ReadableTabularDataSource getDataSetInFocus(String methodName) throws ConnectorCheckedException
    {
        if ((memberInFocus != null) && (memberInFocus.connector instanceof ReadableTabularDataSource readableTabularDataSource))
        {
            return readableTabularDataSource;
        }

        throw new ConnectorCheckedException(TabularDataErrorCode.NO_TABLE_IN_FOCUS.getMessageDefinition(connectorName, familyName),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Return the record count of the data set in focus.
     *
     * @return count
     * @throws ConnectorCheckedException nothing is in focus, or data access problem
     */
    @Override
    public long getRecordCount() throws ConnectorCheckedException
    {
        return this.getDataSetInFocus("getRecordCount").getRecordCount();
    }


    /**
     * Return the table name of the data set in focus.
     *
     * @return string
     * @throws ConnectorCheckedException nothing is in focus, or data access problem
     */
    @Override
    public String getTableName() throws ConnectorCheckedException
    {
        return this.getDataSetInFocus("getTableName").getTableName();
    }


    /**
     * Return the description of the data set in focus.
     *
     * @return string
     * @throws ConnectorCheckedException nothing is in focus, or data access problem
     */
    @Override
    public String getTableDescription() throws ConnectorCheckedException
    {
        return this.getDataSetInFocus("getTableDescription").getTableDescription();
    }


    /**
     * Return the columns of the data set in focus.
     *
     * @return column descriptions
     * @throws ConnectorCheckedException nothing is in focus, or data access problem
     */
    @Override
    public List<TabularColumnDescription> getColumnDescriptions() throws ConnectorCheckedException
    {
        return this.getDataSetInFocus("getColumnDescriptions").getColumnDescriptions();
    }


    /**
     * Return the position of a column in the data set in focus.
     *
     * @param columnName name of the column
     * @return column number, or -1 if there is no such column
     * @throws ConnectorCheckedException nothing is in focus, or data access problem
     */
    @Override
    public int getColumnNumber(String columnName) throws ConnectorCheckedException
    {
        return this.getDataSetInFocus("getColumnNumber").getColumnNumber(columnName);
    }


    /**
     * Read a record of the data set in focus.
     *
     * @param rowNumber row to read, starting at 0
     * @return the record's values, in column order
     * @throws ConnectorCheckedException nothing is in focus, or data access problem
     */
    @Override
    public List<String> readRecord(long rowNumber) throws ConnectorCheckedException
    {
        return this.getDataSetInFocus("readRecord").readRecord(rowNumber);
    }


    /**
     * Stop whichever member is in focus, then close the connector.
     */
    @Override
    public void disconnect()
    {
        this.releaseFocus();

        for (MemberDataSet member : membersByTableName.values())
        {
            this.disconnectQuietly(member.connector);
            member.connector = null;
        }

        membersByTableName.clear();

        super.disconnect();
    }
}
