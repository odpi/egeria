/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.SchemaInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.iterator.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the specialist methods for working with Unity Catalog (UC) Schema.
 */
public class OSSUnityCatalogInsideCatalogSyncSchema extends OSSUnityCatalogInsideCatalogSyncBase
{
    private final List<String> excludeTableNames;
    private final List<String> includeTableNames;
    private final List<String> excludeFunctionNames;
    private final List<String> includeFunctionNames;
    private final List<String> excludeVolumeNames;
    private final List<String> includeVolumeNames;
    private final List<String> excludeModelNames;
    private final List<String> includeModelNames;


    /**
     * Set up the schema synchronizer.
     *
     * @param connectorName name of this connector
     * @param context context for the connector
     * @param catalogTargetName the catalog target name
     * @param catalogGUID guid of the catalog
     * @param metadataSourceQualifiedName name of the metadata collection for this server
     * @param ucFullNameToEgeriaGUID map of full names from UC to the GUID of the entity in Egeria.
     * @param targetPermittedSynchronization the policy that controls the direction of metadata exchange
     * @param ucConnector connector for accessing UC
     * @param ucServerEndpoint the server endpoint used to constructing the qualified names
     * @param templates templates supplied through the catalog target
     * @param configurationProperties configuration properties supplied through the catalog target
     * @param excludeNames list of catalogs to ignore (and include all others)
     * @param includeNames list of catalogs to include (and ignore all others) - overrides excludeCatalogs
     * @param auditLog logging destination
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     * @throws InvalidParameterException no template
     */
    public OSSUnityCatalogInsideCatalogSyncSchema(String                           connectorName,
                                                  IntegrationContext               context,
                                                  String                           catalogTargetName,
                                                  String                           catalogGUID,
                                                  String                           metadataSourceQualifiedName,
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
              metadataSourceQualifiedName,
              ucFullNameToEgeriaGUID,
              targetPermittedSynchronization,
              ucConnector,
              ucServerEndpoint,
              OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
              UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA,
              templates,
              configurationProperties,
              excludeNames,
              includeNames,
              auditLog);

        this.excludeTableNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_TABLE_NAMES.getName(),
                                                                     configurationProperties);
        this.includeTableNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_TABLE_NAMES.getName(),
                                                                     configurationProperties);
        this.excludeFunctionNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_FUNCTION_NAMES.getName(),
                                                                        configurationProperties);
        this.includeFunctionNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_FUNCTION_NAMES.getName(),
                                                                        configurationProperties);
        this.excludeVolumeNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_VOLUME_NAMES.getName(),
                                                                      configurationProperties);
        this.includeVolumeNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_VOLUME_NAMES.getName(),
                                                                      configurationProperties);
        this.excludeModelNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.EXCLUDE_MODEL_NAMES.getName(),
                                                                     configurationProperties);
        this.includeModelNames = super.getArrayConfigurationProperty(UnityCatalogConfigurationProperty.INCLUDE_MODEL_NAMES.getName(),
                                                                     configurationProperties);
    }


    /**
     * Review all the schemas stored in Egeria.
     *
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through *
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
        /*
         * Iterate through the schema attached to the parent catalog.
         */
        RelatedElementsIterator iterator = new RelatedElementsIterator(context.getMetadataSourceGUID(),
                                                                       catalogTargetName,
                                                                       connectorName,
                                                                       parentGUID,
                                                                       parentRelationshipTypeName,
                                                                       1,
                                                                       deployedImplementationType.getAssociatedTypeName(),
                                                                       context,
                                                                       targetPermittedSynchronization,
                                                                       context.getMaxPageSize(),
                                                                       auditLog);

        while (iterator.moreToReceive())
        {
            MemberElement nextElement = iterator.getNextMember();

            if ((nextElement != null) && (nextElement.getElement() != null) && (nextElement.getElement().getProperties() instanceof AssetProperties assetProperties))
            {
                /*
                 * Check that this is a UC Schema.
                 */
                if (UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType().equals(assetProperties.getDeployedImplementationType()))
                {
                    SchemaInfo schemaInfo = null;

                    try
                    {
                        schemaInfo = ucConnector.getSchema(assetProperties.getResourceName());
                    }
                    catch (Exception missing)
                    {
                        // this is not necessarily an error
                    }

                    MemberAction memberAction = MemberAction.NO_ACTION;
                    if (schemaInfo == null)
                    {
                        memberAction = nextElement.getMemberAction(null, null);
                    }
                    else if (noMismatchInExternalIdentifier(schemaInfo.getSchema_id(), nextElement))
                    {
                        memberAction = nextElement.getMemberAction(this.getDateFromLong(schemaInfo.getCreated_at()), this.getDateFromLong(schemaInfo.getUpdated_at()));
                    }

                    this.takeAction(memberAction, nextElement, schemaInfo, parentGUID, parentRelationshipTypeName, relationshipProperties);
                }
            }
        }

        return iterator;
    }


    /**
     * Review all the schemas stored in Egeria.
     *
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through *
     * @param relationshipProperties optional properties for the relationship
     * @return iterator
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     * @throws ConnectorCheckedException logic error in properties
     */
    protected Map<String, String>  refreshChildren(String                     parentGUID,
                                                   String                     parentRelationshipTypeName,
                                                   RelationshipBeanProperties relationshipProperties) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             ConnectorCheckedException
    {
        /*
         * Iterate through the schema attached to the parent catalog.
         */
        RelatedElementsIterator iterator = new RelatedElementsIterator(context.getMetadataSourceGUID(),
                                                                       catalogTargetName,
                                                                       connectorName,
                                                                       parentGUID,
                                                                       parentRelationshipTypeName,
                                                                       1,
                                                                       deployedImplementationType.getAssociatedTypeName(),
                                                                       context,
                                                                       targetPermittedSynchronization,
                                                                       context.getMaxPageSize(),
                                                                       auditLog);

        while (iterator.moreToReceive())
        {
            MemberElement nextElement = iterator.getNextMember();

            if ((nextElement != null) && (nextElement.getElement() != null) && (nextElement.getElement().getProperties() instanceof AssetProperties assetProperties))
            {
                /*
                 * Check that this is a UC Schema.
                 */
                if (UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType().equals(assetProperties.getDeployedImplementationType()))
                {

                    OSSUnityCatalogInsideCatalogSyncVolumes syncVolumes = new OSSUnityCatalogInsideCatalogSyncVolumes(connectorName,
                                                                                                                      context,
                                                                                                                      catalogTargetName,
                                                                                                                      catalogGUID,
                                                                                                                      metadataCollectionQualifiedName,
                                                                                                                      ucFullNameToEgeriaGUID,
                                                                                                                      targetPermittedSynchronization,
                                                                                                                      ucConnector,
                                                                                                                      ucServerEndpoint,
                                                                                                                      templates,
                                                                                                                      configurationProperties,
                                                                                                                      excludeVolumeNames,
                                                                                                                      includeVolumeNames,
                                                                                                                      auditLog);

                    ucFullNameToEgeriaGUID.putAll(syncVolumes.refresh(nextElement.getElement().getElementHeader().getGUID(),
                                                                      OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                      null));

                    OSSUnityCatalogInsideCatalogSyncTables syncTables = new OSSUnityCatalogInsideCatalogSyncTables(connectorName,
                                                                                                                   context,
                                                                                                                   catalogTargetName,
                                                                                                                   catalogGUID,
                                                                                                                   metadataCollectionQualifiedName,
                                                                                                                   ucFullNameToEgeriaGUID,
                                                                                                                   targetPermittedSynchronization,
                                                                                                                   ucConnector,
                                                                                                                   ucServerEndpoint,
                                                                                                                   templates,
                                                                                                                   configurationProperties,
                                                                                                                   excludeTableNames,
                                                                                                                   includeTableNames,
                                                                                                                   auditLog);

                    ucFullNameToEgeriaGUID.putAll(syncTables.refresh(nextElement.getElement().getElementHeader().getGUID(),
                                                                     OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                     null));

                    OSSUnityCatalogInsideCatalogSyncFunctions syncFunctions = new OSSUnityCatalogInsideCatalogSyncFunctions(connectorName,
                                                                                                                            context,
                                                                                                                            catalogTargetName,
                                                                                                                            catalogGUID,
                                                                                                                            metadataCollectionQualifiedName,
                                                                                                                            ucFullNameToEgeriaGUID,
                                                                                                                            targetPermittedSynchronization,
                                                                                                                            ucConnector,
                                                                                                                            ucServerEndpoint,
                                                                                                                            templates,
                                                                                                                            configurationProperties,
                                                                                                                            excludeFunctionNames,
                                                                                                                            includeFunctionNames,
                                                                                                                            auditLog);

                    ucFullNameToEgeriaGUID.putAll(syncFunctions.refresh(nextElement.getElement().getElementHeader().getGUID(),
                                                                        OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                        null));

                    OSSUnityCatalogInsideCatalogSyncRegisteredModels syncRegisteredModels = new OSSUnityCatalogInsideCatalogSyncRegisteredModels(connectorName,
                                                                                                                                                 context,
                                                                                                                                                 catalogTargetName,
                                                                                                                                                 catalogGUID,
                                                                                                                                                 metadataCollectionQualifiedName,
                                                                                                                                                 ucFullNameToEgeriaGUID,
                                                                                                                                                 targetPermittedSynchronization,
                                                                                                                                                 ucConnector,
                                                                                                                                                 ucServerEndpoint,
                                                                                                                                                 templates,
                                                                                                                                                 configurationProperties,
                                                                                                                                                 excludeModelNames,
                                                                                                                                                 includeModelNames,
                                                                                                                                                 auditLog);

                    ucFullNameToEgeriaGUID.putAll(syncRegisteredModels.refresh(nextElement.getElement().getElementHeader().getGUID(),
                                                                               OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                               null));
                }
            }
        }

        /*
         * This map has been filled with the mapped elements.
         */
        return ucFullNameToEgeriaGUID;
    }


    /**
     * Review all the schemas stored in UC.
     *
     * @param iterator  iterator
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
                    if (ucFullNameToEgeriaGUID.get(schemaInfo.getFull_name()) == null)
                    {
                        String ucSchemaQualifiedName = this.getQualifiedName(schemaInfo.getFull_name());

                        MemberElement memberElement = iterator.getMemberByQualifiedName(ucSchemaQualifiedName);
                        MemberAction memberAction   = memberElement.getMemberAction(this.getDateFromLong(schemaInfo.getCreated_at()),
                                                                                    this.getDateFromLong(schemaInfo.getUpdated_at()));
                        if (noMismatchInExternalIdentifier(schemaInfo.getSchema_id(), memberElement))
                        {
                            this.takeAction(memberAction,
                                            memberElement,
                                            schemaInfo,
                                            parentGUID,
                                            parentRelationshipTypeName,
                                            relationshipProperties);
                        }
                    }
                }
            }
        }
    }


    /**
     * Use the member action enum to request that the correct action is taken.
     *
     * @param memberAction enum
     * @param memberElement element from egeria
     * @param schemaInfo element from UC
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     * @throws ConnectorCheckedException logic error in properties
     */
    private void takeAction(MemberAction               memberAction,
                            MemberElement              memberElement,
                            SchemaInfo                 schemaInfo,
                            String                     parentGUID,
                            String                     parentRelationshipTypeName,
                            RelationshipBeanProperties relationshipProperties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException,
                                                                                      ConnectorCheckedException
    {
        switch (memberAction)
        {
            case CREATE_INSTANCE_IN_OPEN_METADATA -> this.createElementInEgeria(schemaInfo, parentGUID, parentRelationshipTypeName, relationshipProperties);
            case UPDATE_INSTANCE_IN_OPEN_METADATA -> this.updateElementInEgeria(schemaInfo, memberElement);
            case DELETE_INSTANCE_IN_OPEN_METADATA -> this.deleteElementInEgeria(memberElement);
            case CREATE_INSTANCE_IN_THIRD_PARTY   -> this.createElementInThirdParty(memberElement);
            case UPDATE_INSTANCE_IN_THIRD_PARTY   -> this.updateElementInThirdParty(schemaInfo, memberElement);
            case DELETE_INSTANCE_IN_THIRD_PARTY   -> this.deleteElementInThirdParty(schemaInfo);
        }
    }


    /**
     * Create an element in open metadata.
     *
     * @param schemaInfo object from UC
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void createElementInEgeria(SchemaInfo                 schemaInfo,
                                       String                     parentGUID,
                                       String                     parentRelationshipTypeName,
                                       RelationshipBeanProperties relationshipProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        String qualifiedName = super.getQualifiedName(schemaInfo.getFull_name());

        TemplateOptions templateOptions = new TemplateOptions(assetClient.getMetadataSourceOptions());

        templateOptions.setAnchorGUID(catalogGUID);
        templateOptions.setIsOwnAnchor(false);
        templateOptions.setAnchorScopeGUID(UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getGUID());

        templateOptions.setParentGUID(catalogGUID);
        templateOptions.setParentAtEnd1(true);
        templateOptions.setParentRelationshipTypeName(parentRelationshipTypeName);

        String ucSchemaGUID = assetClient.createAssetFromTemplate(templateOptions,
                                                                  templateGUID,
                                                                  null,
                                                                  this.getPlaceholderProperties(schemaInfo),
                                                                  relationshipProperties);

        super.addPropertyFacet(ucSchemaGUID, qualifiedName, schemaInfo, null);

        super.addExternalIdentifier(ucSchemaGUID,
                                    schemaInfo,
                                    schemaInfo.getName(),
                                    PlaceholderProperty.SCHEMA_NAME.getName(),
                                    "schema",
                                    schemaInfo.getSchema_id(),
                                    PermittedSynchronization.FROM_THIRD_PARTY);

        ucFullNameToEgeriaGUID.put(schemaInfo.getFull_name(), ucSchemaGUID);
    }


    /**
     * Update an element in open metadata.
     *
     * @param schemaInfo object from UC
     * @param memberElement existing element in egeria
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    protected void updateElementInEgeria(SchemaInfo    schemaInfo,
                                         MemberElement memberElement) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        String egeriaSchemaGUID = memberElement.getElement().getElementHeader().getGUID();

        UpdateOptions updateOptions = new UpdateOptions(assetClient.getUpdateOptions(true));

        openMetadataStore.updateMetadataElementInStore(egeriaSchemaGUID,
                                                       updateOptions,
                                                       getElementProperties(schemaInfo));

        externalIdClient.confirmSynchronization(memberElement.getElement(),
                                                schemaInfo.getSchema_id());
    }


    /**
     * Create a schema in UC.
     *
     * @param memberElement elements from Egeria
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     * @throws ConnectorCheckedException problem extracting properties
     */
    private void createElementInThirdParty(MemberElement memberElement) throws PropertyServerException,
                                                                               InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               ConnectorCheckedException
    {
        SchemaInfo schemaInfo = ucConnector.createSchema(super.getUCNameFromMember(memberElement),
                                                         catalogTargetName,
                                                         super.getUCCommentFomMember(memberElement),
                                                         super.getUCPropertiesFromMember(memberElement));

        if (memberElement.getExternalIdentifier() == null)
        {
            super.addExternalIdentifier(memberElement.getElement().getElementHeader().getGUID(),
                                        schemaInfo,
                                        schemaInfo.getName(),
                                        PlaceholderProperty.SCHEMA_NAME.getName(),
                                        "schema",
                                        schemaInfo.getSchema_id(),
                                        PermittedSynchronization.TO_THIRD_PARTY);
        }
        else
        {
            externalIdClient.confirmSynchronization(memberElement.getElement(), schemaInfo.getSchema_id());
        }
    }


    /**
     * Update the schema in UC.
     *
     * @param schemaInfo existing schema in UC
     * @param memberElement elements from Egeria
     *
     * @throws InvalidParameterException bad call to Egeria
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException  problem communicating with UC or egeria
     */
    private void updateElementInThirdParty(SchemaInfo    schemaInfo,
                                           MemberElement memberElement) throws PropertyServerException,
                                                                               InvalidParameterException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "updateElementInThirdParty";

        auditLog.logMessage(methodName,
                            UCAuditCode.SCHEMA_UPDATE.getMessageDefinition(connectorName,
                                                                             memberElement.getElement().getElementHeader().getGUID(),
                                                                             schemaInfo.getCatalog_name() + "." + schemaInfo.getName(),
                                                                             ucServerEndpoint));

        externalIdClient.confirmSynchronization(memberElement.getElement(), schemaInfo.getSchema_id());
    }


    /**
     * Delete the UC element.
     *
     * @param info info object describing the element to delete.
     *
     * @throws PropertyServerException problem connecting to UC
     */
    private void deleteElementInThirdParty(SchemaInfo    info) throws PropertyServerException
    {
        ucConnector.deleteSchema(info.getFull_name(), false);
    }


    /**
     * Return the template's placeholder properties populated with the info object's values.
     *
     * @param info object from UC
     * @return map of placeholder values
     */
    private Map<String, String> getPlaceholderProperties(SchemaInfo info)
    {
        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), ucServerEndpoint);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), catalogTargetName);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), info.getName());
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.name, null);
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.name, info.getComment());

        return placeholderProperties;
    }


    /**
     * Set up the element properties for an asset from the info object.
     *
     * @param info  information extracted from UC
     *
     * @return element properties suitable for create or update
     */
    private ElementProperties getElementProperties(SchemaInfo info)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.DISPLAY_NAME.name,
                                                                               info.getName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.RESOURCE_NAME.name,
                                                             info.getFull_name());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             info.getComment());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                             UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType());

       elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                               OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                               info.getProperties());

        return elementProperties;
    }



    /**
     * Set up the element properties for an asset from the info object and qualified name.
     *
     * @param qualifiedName calculated qualified name
     * @param info  information extracted from UC
     *
     * @return element properties suitable for create or update
     */
    private ElementProperties getElementProperties(String     qualifiedName,
                                                   SchemaInfo info)
    {
        ElementProperties elementProperties = this.getElementProperties(info);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                             qualifiedName);

        return elementProperties;
    }

}
