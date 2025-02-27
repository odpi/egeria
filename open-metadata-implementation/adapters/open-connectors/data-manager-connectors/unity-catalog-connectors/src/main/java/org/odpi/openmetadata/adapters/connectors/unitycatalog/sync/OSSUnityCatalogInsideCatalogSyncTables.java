/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.*;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.integration.iterator.IntegrationIterator;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberAction;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberElement;
import org.odpi.openmetadata.frameworks.integration.iterator.MetadataCollectionIterator;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the specialist methods for working with Unity Catalog (UC) Tables.
 */
public class OSSUnityCatalogInsideCatalogSyncTables extends OSSUnityCatalogInsideCatalogSyncBase
{
    private final String entityTypeName = OpenMetadataType.VIRTUAL_RELATIONAL_TABLE.typeName;

    private String templateGUID = null;


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
     */
    public OSSUnityCatalogInsideCatalogSyncTables(String                           connectorName,
                                                  CatalogIntegratorContext         context,
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
                                                  AuditLog                         auditLog)
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
              UnityCatalogDeployedImplementationType.OSS_UC_TABLE,
              templates,
              configurationProperties,
              excludeNames,
              includeNames,
              auditLog);

        if (templates != null)
        {
            this.templateGUID = templates.get(deployedImplementationType.getDeployedImplementationType());
        }
    }



    /**
     * Review all the tables stored in Egeria.
     *
     * @return MetadataCollectionIterator
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    protected IntegrationIterator refreshEgeria() throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        final String methodName = "refreshEgeriaTables";

        MetadataCollectionIterator tableIterator = new MetadataCollectionIterator(catalogGUID,
                                                                                  catalogQualifiedName,
                                                                                  catalogGUID,
                                                                                  catalogQualifiedName,
                                                                                  catalogName,
                                                                                  connectorName,
                                                                                  entityTypeName,
                                                                                  openMetadataAccess,
                                                                                  targetPermittedSynchronization,
                                                                                  context.getMaxPageSize(),
                                                                                  auditLog);

        while (tableIterator.moreToReceive())
        {
            MemberElement nextElement = tableIterator.getNextMember();

            if (nextElement != null)
            {
                /*
                 * Check that this is a UC Table.
                 */
                String deployedImplementationType = propertyHelper.getStringProperty(catalogName,
                                                                                     OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                                     nextElement.getElement().getElementProperties(),
                                                                                     methodName);

                if (UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType().equals(deployedImplementationType))
                {
                    TableInfo tableInfo = null;

                    String tableName = propertyHelper.getStringProperty(catalogName,
                                                                        OpenMetadataProperty.RESOURCE_NAME.name,
                                                                        nextElement.getElement().getElementProperties(),
                                                                        methodName);

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

                        this.takeAction(context.getAnchorGUID(nextElement.getElement()),
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
     * @param iterator  Metadata collection iterator
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    protected void refreshUnityCatalog(IntegrationIterator iterator) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        List<SchemaInfo> ucSchemaList = ucConnector.listSchemas(catalogName);

        if (ucSchemaList != null)
        {
            for (SchemaInfo schemaInfo : ucSchemaList)
            {
                if (schemaInfo != null)
                {
                    String schemaGUID = ucFullNameToEgeriaGUID.get(schemaInfo.getFull_name());

                    if (schemaGUID != null)
                    {
                        List<TableInfo> ucTableList = ucConnector.listTables(catalogName, schemaInfo.getName());

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
     */
    private void takeAction(String        schemaGUID,
                            String        schemaName,
                            MemberAction  memberAction,
                            MemberElement memberElement,
                            TableInfo    tableInfo) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
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
    private void createElementInEgeria(String     schemaGUID,
                                       TableInfo tableInfo) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String parentLinkTypeName = OpenMetadataType.DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP.typeName;
        final boolean parentAtEnd1 = false;

        String ucTableGUID;

        if (templateGUID != null)
        {
            ucTableGUID = openMetadataAccess.getMetadataElementFromTemplate(catalogGUID,
                                                                            catalogQualifiedName,
                                                                            deployedImplementationType.getAssociatedTypeName(),
                                                                            schemaGUID,
                                                                            false,
                                                                            null,
                                                                            null,
                                                                            templateGUID,
                                                                            null,
                                                                            this.getPlaceholderProperties(tableInfo),
                                                                            schemaGUID,
                                                                            parentLinkTypeName,
                                                                            null,
                                                                            parentAtEnd1);
        }
        else
        {
            String qualifiedName = super.getQualifiedName(tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName());

            ucTableGUID = openMetadataAccess.createMetadataElementInStore(catalogGUID,
                                                                          catalogQualifiedName,
                                                                          deployedImplementationType.getAssociatedTypeName(),
                                                                           ElementStatus.ACTIVE,
                                                                           null,
                                                                           schemaGUID,
                                                                           false,
                                                                           null,
                                                                           null,
                                                                           this.getElementProperties(qualifiedName, tableInfo),
                                                                           schemaGUID,
                                                                           parentLinkTypeName,
                                                                           null,
                                                                           parentAtEnd1);

            Map<String, String> facetProperties = new HashMap<>();

            facetProperties.put(UnityCatalogPlaceholderProperty.TABLE_TYPE.getName(), tableInfo.getTable_type());
            facetProperties.put(UnityCatalogPlaceholderProperty.DATA_SOURCE_FORMAT.getName(), tableInfo.getData_source_format());
            facetProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), tableInfo.getStorage_location());


            super.addPropertyFacet(ucTableGUID, qualifiedName, tableInfo, facetProperties);
        }

        context.addExternalIdentifier(catalogGUID,
                                      catalogQualifiedName,
                                      catalogTypeName,
                                      ucTableGUID,
                                      deployedImplementationType.getAssociatedTypeName(),
                                      this.getExternalIdentifierProperties(tableInfo,
                                                                           tableInfo.getSchema_name(),
                                                                           UnityCatalogPlaceholderProperty.TABLE_NAME.getName(),
                                                                           "table",
                                                                           tableInfo.getTable_id(),
                                                                           PermittedSynchronization.FROM_THIRD_PARTY));

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
        String egeriaTableGUID = memberElement.getElement().getElementGUID();

        openMetadataAccess.updateMetadataElementInStore(catalogGUID,
                                                        catalogQualifiedName,
                                                        egeriaTableGUID,
                                                        false,
                                                        this.getElementProperties(tableInfo));

        this.updateSchemaAttributesForUCTable(memberElement, tableInfo);

        context.confirmSynchronization(catalogGUID,
                                       catalogQualifiedName,
                                       egeriaTableGUID,
                                       entityTypeName,
                                       tableInfo.getTable_id());
    }


    /**
     * Create an equivalent element in UC.
     *
     * @param memberElement elements from Egeria
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     */
    private void createElementInThirdParty(String        schemaName,
                                           MemberElement memberElement) throws PropertyServerException,
                                                                               InvalidParameterException,
                                                                               UserNotAuthorizedException
    {
        TableInfo tableInfo = ucConnector.createTable(super.getUCNameFromMember(memberElement),
                                                      catalogName,
                                                      schemaName,
                                                      super.getUCCommentFomMember(memberElement),
                                                      this.getUCTableTypeFromMember(memberElement),
                                                      this.getUCDataSourceFormatFromMember(memberElement),
                                                      this.getUCColumnsForTable(memberElement),
                                                      super.getUCStorageLocationFromMember(memberElement));

        if (memberElement.getExternalIdentifier() == null)
        {
            context.addExternalIdentifier(catalogGUID,
                                          catalogQualifiedName,
                                          catalogTypeName,
                                          memberElement.getElement().getElementGUID(),
                                          deployedImplementationType.getAssociatedTypeName(),
                                          this.getExternalIdentifierProperties(tableInfo,
                                                                               tableInfo.getSchema_name(),
                                                                               UnityCatalogPlaceholderProperty.TABLE_NAME.getName(),
                                                                               "table",
                                                                               tableInfo.getTable_id(),
                                                                               PermittedSynchronization.TO_THIRD_PARTY));
        }
        else
        {
            context.confirmSynchronization(catalogGUID,
                                           catalogQualifiedName,
                                           memberElement.getElement().getElementGUID(),
                                           deployedImplementationType.getAssociatedTypeName(),
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
        final String methodName = "getUCDataSourceFormatFromMember";

        return propertyHelper.getStringPropertyFromClassification(memberElement.getElement(),
                                                                  OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.typeName,
                                                                  OpenMetadataProperty.ENCODING.name,
                                                                  methodName);
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
                                                                          memberElement.getElement().getElementGUID(),
                                                                          tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName(),
                                                                          ucServerEndpoint));

        context.confirmSynchronization(catalogGUID,
                                       catalogQualifiedName,
                                       memberElement.getElement().getElementGUID(),
                                       deployedImplementationType.getAssociatedTypeName(),
                                       tableInfo.getTable_id());
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
    private ElementProperties getElementProperties(TableInfo tableInfo)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.NAME.name,
                                                                               tableInfo.getName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.RESOURCE_NAME.name,
                                                             tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             tableInfo.getComment());

        //elementProperties = propertyHelper.addStringMapProperty(elementProperties,
        //                                                        OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
        //                                                        tableInfo.getProperties());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                             UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType());
        return elementProperties;
    }


    /**
     * Set up the element properties from the info object and qualified name.
     *
     * @param qualifiedName calculated qualified name
     * @param info  information extracted from UC
     *
     * @return element properties suitable for create or update
     */
    private ElementProperties getElementProperties(String    qualifiedName,
                                                   TableInfo info)
    {
        ElementProperties elementProperties = this.getElementProperties(info);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                             qualifiedName);

        return elementProperties;
    }


    private List<ColumnInfo> getUCColumnsForTable(MemberElement memberElement) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        if (memberElement.getRootSchemaType() != null)
        {
            List<ColumnInfo> columnInfoList = new ArrayList<>();

            int startFrom = 0;

            RelatedMetadataElementList schemaAttributes = openMetadataAccess.getRelatedMetadataElements(memberElement.getRootSchemaType().getElement().getElementGUID(),
                                                                                                        1,
                                                                                                        OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName,
                                                                                                        startFrom,
                                                                                                        context.getMaxPageSize());

            while ((schemaAttributes != null) && (schemaAttributes.getElementList() != null))
            {
                for (RelatedMetadataElement schemaAttribute : schemaAttributes.getElementList())
                {
                    if (schemaAttribute != null)
                    {
                        columnInfoList.add(this.getUCColumnFromSchemaAttribute(schemaAttribute));
                    }
                }

                startFrom = startFrom + context.getMaxPageSize();
                schemaAttributes = openMetadataAccess.getRelatedMetadataElements(memberElement.getRootSchemaType().getElement().getElementGUID(),
                                                                                 1,
                                                                                 OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName,
                                                                                 startFrom,
                                                                                 context.getMaxPageSize());
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
    private ColumnInfo getUCColumnFromSchemaAttribute(RelatedMetadataElement schemaAttribute)
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
        /*
         * Create the root schema type.
         */
        openMetadataAccess.createMetadataElementInStore(catalogGUID,
                                                        catalogQualifiedName,
                                                        OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName,
                                                        ElementStatus.ACTIVE,
                                                        null,
                                                        tableGUID,
                                                        false,
                                                        null,
                                                        null,
                                                        properties,
                                                        tableGUID,
                                                        OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName,
                                                        null,
                                                        true);

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
