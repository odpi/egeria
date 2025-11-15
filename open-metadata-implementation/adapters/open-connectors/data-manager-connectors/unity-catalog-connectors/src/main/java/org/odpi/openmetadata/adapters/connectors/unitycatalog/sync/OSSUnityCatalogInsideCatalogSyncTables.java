/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.*;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.iterator.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataHierarchySummary;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetEncodingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.VirtualRelationalTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the specialist methods for working with Unity Catalog (UC) Tables.
 */
public class OSSUnityCatalogInsideCatalogSyncTables extends OSSUnityCatalogInsideCatalogSyncBase
{
    /**
     * Set up the table synchronizer.
     *
     * @param connectorName name of this connector
     * @param context context for the connector
     * @param catalogTargetName the catalog target name
     * @param catalogGUID guid of the catalog
     * @param catalogName name of the catalog
     * @param ucFullNameToEgeriaGUID map of full names from UC to the GUID of the entity in Egeria.
     * @param targetPermittedSynchronization the policy that controls the direction of metadata exchange
     * @param ucConnector connector for accessing UC
     * @param ucServerEndpoint the server endpoint used to constructing the qualified names
     * @param templates templates supplied through the catalog target
     * @param configurationProperties configuration properties supplied through the catalog target
     * @param excludeNames list of catalogs to ignore (and include all others)
     * @param includeNames list of catalogs to include (and ignore all others) - overrides excludeCatalogs
     * @param auditLog logging destination
     * @throws UserNotAuthorizedException connector disconnected
     * @throws InvalidParameterException missing template
     */
    public OSSUnityCatalogInsideCatalogSyncTables(String                           connectorName,
                                                  IntegrationContext               context,
                                                  String                           catalogTargetName,
                                                  String                           catalogGUID,
                                                  String                           catalogName,
                                                  Map<String, String>              ucFullNameToEgeriaGUID,
                                                  PermittedSynchronization         targetPermittedSynchronization,
                                                  OSSUnityCatalogResourceConnector ucConnector,
                                                  String                           ucServerEndpoint,
                                                  Map<String, String>              templates,
                                                  Map<String, Object>              configurationProperties,
                                                  List<String>                     excludeNames,
                                                  List<String>                     includeNames,
                                                  AuditLog                         auditLog) throws UserNotAuthorizedException,
                                                                                                    InvalidParameterException
    {
        super(connectorName,
              context,
              catalogTargetName,
              catalogGUID,
              catalogName,
              ucFullNameToEgeriaGUID,
              targetPermittedSynchronization,
              ucConnector,
              ucServerEndpoint,
              OpenMetadataType.VIRTUAL_RELATIONAL_TABLE.typeName,
              UnityCatalogDeployedImplementationType.OSS_UC_TABLE,
              templates,
              configurationProperties,
              excludeNames,
              includeNames,
              auditLog);
    }



    /**
     * Review all the tables stored in Egeria.
     *
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through
     * @param relationshipProperties optional properties for the relationship
     * @return iterator
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     * @throws ConnectorCheckedException logic error in properties
     */
    protected IntegrationIterator refreshEgeria(String                     parentGUID,
                                                String                     parentRelationshipTypeName,
                                                RelationshipBeanProperties relationshipProperties) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          ConnectorCheckedException
    {
        RelatedElementsIterator tableIterator = new RelatedElementsIterator(context.getMetadataSourceGUID(),
                                                                            catalogTargetName,
                                                                            connectorName,
                                                                            parentGUID,
                                                                            parentRelationshipTypeName,
                                                                            1,
                                                                            entityTypeName,
                                                                            context,
                                                                            targetPermittedSynchronization,
                                                                            context.getMaxPageSize(),
                                                                            auditLog);

        while (tableIterator.moreToReceive())
        {
            MemberElement nextElement = tableIterator.getNextMember();

            if ((nextElement != null) && (nextElement.getElement() != null) && (nextElement.getElement().getProperties() instanceof VirtualRelationalTableProperties relationalTableProperties))
            {
                /*
                 * Check that this is a UC Table.
                 */
                String deployedImplementationType = relationalTableProperties.getDeployedImplementationType();

                if (UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType().equals(deployedImplementationType))
                {
                    TableInfo tableInfo = null;

                    String tableName = relationalTableProperties.getResourceName();

                    if (context.elementShouldBeCatalogued(tableName, excludeNames, includeNames))
                    {
                        try
                        {
                            tableInfo = ucConnector.getTable(tableName);
                        }
                        catch (Exception missing)
                        {
                            // this is not necessarily an error
                        }

                        MemberAction memberAction = MemberAction.NO_ACTION;
                        if (tableInfo == null)
                        {
                            memberAction = nextElement.getMemberAction(null, null);
                        }
                        else if (noMismatchInExternalIdentifier(tableInfo.getTable_id(), nextElement))
                        {
                            memberAction = nextElement.getMemberAction(this.getDateFromLong(tableInfo.getCreated_at()),
                                                                       this.getDateFromLong(tableInfo.getUpdated_at()));
                        }

                        this.takeAction(parentGUID,
                                        super.getUCSchemaFromMember(nextElement),
                                        memberAction,
                                        nextElement,
                                        tableInfo);
                    }
                }
            }
        }

        return tableIterator;
    }



    /**
     * Review all the tables stored in UC.
     *
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through
     * @param relationshipProperties optional properties for the relationship
     * @param iterator iterator
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     * @throws ConnectorCheckedException logic error in properties
     */
    protected void refreshUnityCatalog(String                     parentGUID,
                                       String                     parentRelationshipTypeName,
                                       RelationshipBeanProperties relationshipProperties,
                                       IntegrationIterator        iterator) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException,
                                                                                   ConnectorCheckedException
    {
        List<SchemaInfo> ucSchemaList = ucConnector.listSchemas(catalogTargetName);

        if (ucSchemaList != null)
        {
            for (SchemaInfo schemaInfo : ucSchemaList)
            {
                if (schemaInfo != null)
                {
                    String schemaGUID = ucFullNameToEgeriaGUID.get(schemaInfo.getFull_name());

                    if (schemaGUID != null)
                    {
                        List<TableInfo> ucTableList = ucConnector.listTables(catalogTargetName, schemaInfo.getName());

                        if (ucTableList != null)
                        {
                            for (TableInfo tableInfo : ucTableList)
                            {
                                if (tableInfo != null)
                                {
                                    if (ucFullNameToEgeriaGUID.get(tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName()) == null)
                                    {
                                        String ucTableQualifiedName = this.getQualifiedName(tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName());

                                        MemberElement memberElement = iterator.getMemberByQualifiedName(ucTableQualifiedName);
                                        MemberAction memberAction = memberElement.getMemberAction(this.getDateFromLong(tableInfo.getCreated_at()),
                                                                                                  this.getDateFromLong(tableInfo.getUpdated_at()));
                                        if (noMismatchInExternalIdentifier(tableInfo.getTable_id(), memberElement))
                                        {
                                            this.takeAction(schemaGUID, tableInfo.getSchema_name(), memberAction, memberElement, tableInfo);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Use the member action enum to request that the correct action is taken.
     *
     * @param schemaGUID unique identifier of the schema in Egeria
     * @param memberAction enum
     * @param memberElement element from egeria
     * @param tableInfo element from UC
     * @throws ConnectorCheckedException logic error in properties
     */
    private void takeAction(String        schemaGUID,
                            String        schemaName,
                            MemberAction  memberAction,
                            MemberElement memberElement,
                            TableInfo    tableInfo) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException,
                                                           ConnectorCheckedException
    {
        switch (memberAction)
        {
            case CREATE_INSTANCE_IN_OPEN_METADATA -> this.createElementInEgeria(schemaGUID, tableInfo);
            case UPDATE_INSTANCE_IN_OPEN_METADATA -> this.updateElementInEgeria(tableInfo, memberElement);
            case DELETE_INSTANCE_IN_OPEN_METADATA -> this.deleteElementInEgeria(memberElement);
            case CREATE_INSTANCE_IN_THIRD_PARTY   -> this.createElementInThirdParty(schemaName, memberElement);
            case UPDATE_INSTANCE_IN_THIRD_PARTY   -> this.updateElementInThirdParty(tableInfo, memberElement);
            case DELETE_INSTANCE_IN_THIRD_PARTY   -> this.deleteElementInThirdParty(tableInfo);
        }
    }


    /**
     * Create a table element in open metadata.
     *
     * @param tableInfo object from UC
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void createElementInEgeria(String    schemaGUID,
                                       TableInfo tableInfo) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String parentLinkTypeName = OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName;

        TemplateOptions templateOptions = new TemplateOptions(assetClient.getMetadataSourceOptions());

        templateOptions.setAnchorGUID(schemaGUID);
        templateOptions.setIsOwnAnchor(false);
        templateOptions.setAnchorScopeGUID(UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getGUID());

        templateOptions.setParentGUID(schemaGUID);
        templateOptions.setParentAtEnd1(true);
        templateOptions.setParentRelationshipTypeName(parentLinkTypeName);

        String ucTableGUID = openMetadataStore.createMetadataElementFromTemplate(deployedImplementationType.getAssociatedTypeName(),
                                                                          templateOptions,
                                                                          templateGUID,
                                                                          null,
                                                                          this.getPlaceholderProperties(tableInfo),
                                                                          null);

        super.addExternalIdentifier(ucTableGUID,
                                    tableInfo,
                                    tableInfo.getSchema_name(),
                                    UnityCatalogPlaceholderProperty.TABLE_NAME.getName(),
                                    "table",
                                    tableInfo.getTable_id(),
                                    PermittedSynchronization.FROM_THIRD_PARTY);

        this.createSchemaAttributesForUCTable(ucTableGUID, tableInfo);

        ucFullNameToEgeriaGUID.put(tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName(), ucTableGUID);
    }


    /**
     * Using the information from UC, update the elements Egeria.
     *
     * @param tableInfo info from UC
     * @param memberElement elements from Egeria
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void updateElementInEgeria(TableInfo    tableInfo,
                                       MemberElement memberElement) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        String egeriaTableGUID = memberElement.getElement().getElementHeader().getGUID();

        UpdateOptions updateOptions = new UpdateOptions(assetClient.getUpdateOptions(true));

        assetClient.updateAsset(egeriaTableGUID,
                                updateOptions,
                                this.getAssetProperties(tableInfo));

        this.updateSchemaAttributesForUCTable(memberElement, tableInfo);

        externalIdClient.confirmSynchronization(memberElement.getElement(),
                                                tableInfo.getTable_id());
    }


    /**
     * Create an equivalent element in UC.
     *
     * @param memberElement elements from Egeria
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     * @throws ConnectorCheckedException logic error in properties
     */
    private void createElementInThirdParty(String        schemaName,
                                           MemberElement memberElement) throws PropertyServerException,
                                                                               InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               ConnectorCheckedException
    {
        TableInfo tableInfo = ucConnector.createTable(super.getUCNameFromMember(memberElement),
                                                      catalogTargetName,
                                                      schemaName,
                                                      super.getUCCommentFomMember(memberElement),
                                                      this.getUCTableTypeFromMember(memberElement),
                                                      this.getUCDataSourceFormatFromMember(memberElement),
                                                      this.getUCColumnsForTable(memberElement),
                                                      super.getUCStorageLocationFromMember(memberElement));

        if (memberElement.getExternalIdentifier() == null)
        {
            super.addExternalIdentifier(memberElement.getElement().getElementHeader().getGUID(),
                                        tableInfo,
                                        tableInfo.getSchema_name(),
                                        UnityCatalogPlaceholderProperty.TABLE_NAME.getName(),
                                        "table",
                                        tableInfo.getTable_id(),
                                        PermittedSynchronization.TO_THIRD_PARTY);
        }
        else
        {
            externalIdClient.confirmSynchronization(memberElement.getElement(),
                                                    tableInfo.getTable_id());
        }
    }


    /**
     * Retrieve the value for table type.  This is stored in the property facet.
     *
     * @param memberElement elements from Egeria
     * @return table type enum
     */
    private String getUCTableTypeFromMember(MemberElement memberElement)
    {
        Map<String, String> vendorProperties = memberElement.getVendorProperties(PropertyFacetValidValues.UNITY_CATALOG_SOURCE_VALUE);

        if (vendorProperties != null)
        {
            return vendorProperties.get(UnityCatalogPlaceholderProperty.TABLE_TYPE.getName());
        }

        return null;
    }


    /**
     * Retrieve the value for table type.  This is stored in the property facet.
     *
     * @param memberElement elements from Egeria
     * @return table type enum
     */
    private String getUCDataSourceFormatFromMember(MemberElement memberElement)
    {
        if ((memberElement.getElement().getElementHeader().getDataAssetEncoding() != null) &&
            (memberElement.getElement().getElementHeader().getDataAssetEncoding().getClassificationProperties() instanceof DataAssetEncodingProperties dataAssetEncodingProperties))
        {
            return dataAssetEncodingProperties.getEncodingType();
        }

        return null;
    }


    /**
     * Update the element in Unity Catalog using the values from Egeria.
     *
     * @param tableInfo info
     * @param memberElement elements from Egeria
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException open metadata repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     */
    private void updateElementInThirdParty(TableInfo     tableInfo,
                                           MemberElement memberElement) throws PropertyServerException,
                                                                               InvalidParameterException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "updateElementInThirdParty";

        auditLog.logMessage(methodName,
                            UCAuditCode.TABLE_UPDATE.getMessageDefinition(connectorName,
                                                                          memberElement.getElement().getElementHeader().getGUID(),
                                                                          tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName(),
                                                                          ucServerEndpoint));

        externalIdClient.confirmSynchronization(memberElement.getElement(), tableInfo.getTable_id());
    }


    /**
     * Delete the table from Unity Catalog.
     *
     * @param tableInfo info object describing the element to delete.
     *
     * @throws PropertyServerException problem connecting to UC
     */
    private void deleteElementInThirdParty(TableInfo     tableInfo) throws PropertyServerException
    {
        final String methodName = "deleteElementInThirdParty";

        auditLog.logMessage(methodName,
                            UCAuditCode.UC_ELEMENT_DELETE.getMessageDefinition(connectorName,
                                                                               tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName(),
                                                                               ucServerEndpoint));
        ucConnector.deleteTable(tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName());
    }


    /**
     * Return the template's placeholder properties populated with the info object's values.
     *
     * @param info object from UC
     * @return map of placeholder values
     */
    private Map<String, String> getPlaceholderProperties(TableInfo info)
    {
        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), ucServerEndpoint);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), info.getCatalog_name());
        placeholderProperties.put(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), info.getSchema_name());
        placeholderProperties.put(UnityCatalogPlaceholderProperty.TABLE_NAME.getName(), info.getName());
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), info.getComment());
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), null);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), info.getStorage_location());
        placeholderProperties.put(UnityCatalogPlaceholderProperty.TABLE_TYPE.getName(), info.getTable_type());
        placeholderProperties.put(UnityCatalogPlaceholderProperty.DATA_SOURCE_FORMAT.getName(), info.getData_source_format());

        return placeholderProperties;
    }


    /**
     * Set up the element properties for an asset from the info object.
     *
     * @param tableInfo  information extracted from UC
     *
     * @return element properties suitable for create or update
     */
    private VirtualRelationalTableProperties getAssetProperties(TableInfo tableInfo)
    {
        VirtualRelationalTableProperties assetProperties = new VirtualRelationalTableProperties();

        assetProperties.setDisplayName(tableInfo.getName());
        assetProperties.setResourceName(tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName());
        assetProperties.setDescription(tableInfo.getComment());

        /*
         * Todo waiting for API to become stable
         */
        //elementProperties = propertyHelper.addStringMapProperty(elementProperties,
        //                                                        OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
        //                                                        tableInfo.getProperties());

        assetProperties.setDeployedImplementationType(UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType());

        return assetProperties;
    }


    /**
     * Set up the element properties from the info object and qualified name.
     *
     * @param qualifiedName calculated qualified name
     * @param info  information extracted from UC
     *
     * @return element properties suitable for create or update
     */
    private VirtualRelationalTableProperties getAssetProperties(String    qualifiedName,
                                                                TableInfo info)
    {
        VirtualRelationalTableProperties assetProperties = this.getAssetProperties(info);

        assetProperties.setQualifiedName(qualifiedName);

        return assetProperties;
    }


    private List<ColumnInfo> getUCColumnsForTable(MemberElement memberElement) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        if (memberElement.getRootSchemaType() != null)
        {
            List<ColumnInfo> columnInfoList = new ArrayList<>();

            RelatedMetadataElementSummary       schemaType        = memberElement.getRootSchemaType();
            if (schemaType instanceof RelatedMetadataHierarchySummary relatedMetadataHierarchySummary)
            {
                for (RelatedMetadataElementSummary schemaAttribute : relatedMetadataHierarchySummary.getNestedElements())
                {
                    if (schemaAttribute != null)
                    {
                        columnInfoList.add(this.getUCColumnFromSchemaAttribute(schemaAttribute));
                    }
                }
            }

            if (! columnInfoList.isEmpty())
            {
                return columnInfoList;
            }
        }
        return null;
    }


    /**
     * Return the column information from a schema element.
     *
     * @param schemaAttribute column information from open metadata
     * @return columnInfo
     */
    private ColumnInfo getUCColumnFromSchemaAttribute(RelatedMetadataElementSummary schemaAttribute)
    {
        return null;
    }


    /**
     * Create the schema attributes in open metadata reflect the columns from UC.
     *
     * @param tableGUID unique identifier of the newly created table in open metadata
     * @param tableInfo details about the table to add to the schema attributes
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException open metadata repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     */
    private void createSchemaAttributesForUCTable(String        tableGUID,
                                                  TableInfo     tableInfo) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "createSchemaAttributesForUCTable";

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        super.getQualifiedName(tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName()) + "_rootSchemaType");

        NewElementOptions newElementOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

        newElementOptions.setExternalSourceGUID(catalogGUID);
        newElementOptions.setExternalSourceName(metadataCollectionQualifiedName);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);

        newElementOptions.setAnchorGUID(tableGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorScopeGUID(UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getGUID());

        newElementOptions.setParentGUID(tableGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName( OpenMetadataType.SCHEMA_RELATIONSHIP.typeName);

        /*
         * Create the root schema type.
         */
        openMetadataStore.createMetadataElementInStore(OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName,
                                                       newElementOptions,
                                                       null,
                                                       new NewElementProperties(properties),
                                                       null);

        auditLog.logMessage(methodName,
                            UCAuditCode.MISSING_METHOD.getMessageDefinition(connectorName,
                                                                            methodName,
                                                                            tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName(),
                                                                            ucServerEndpoint));
    }


    /**
     * Create the schema attributes in open metadata reflect the columns from UC.
     *
     * @param memberElement details of the table in open metadata
     * @param tableInfo details about the table to add to the schema attributes
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException open metadata repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     */
    private void updateSchemaAttributesForUCTable(MemberElement memberElement,
                                                  TableInfo     tableInfo) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "updateSchemaAttributesForUCTable";

        auditLog.logMessage(methodName,
                            UCAuditCode.MISSING_METHOD.getMessageDefinition(connectorName,
                                                                            methodName,
                                                                            tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName(),
                                                                            ucServerEndpoint));
    }
}
