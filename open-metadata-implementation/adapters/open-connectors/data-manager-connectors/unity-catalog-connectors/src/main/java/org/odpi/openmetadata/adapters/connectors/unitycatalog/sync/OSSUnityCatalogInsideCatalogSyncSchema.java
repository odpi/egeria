/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.SchemaInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.integration.iterator.IntegrationIterator;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberAction;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberElement;
import org.odpi.openmetadata.frameworks.integration.iterator.MetadataCollectionIterator;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ServerAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the specialist methods for working with Unity Catalog (UC) Schema.
 */
public class OSSUnityCatalogInsideCatalogSyncSchema extends OSSUnityCatalogInsideCatalogSyncBase
{
    private String templateGUID = null;


    /**
     * Set up the schema synchronizer.
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
    public OSSUnityCatalogInsideCatalogSyncSchema(String                           connectorName,
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
              UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA,
              templates,
              configurationProperties,
              excludeNames,
              includeNames,
              auditLog);

        if (templates != null)
        {
            this.templateGUID = templates.get(UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType());
        }
    }



    /**
     * Review all the schemas stored in Egeria.
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
        final String methodName = "refreshEgeria";

        MetadataCollectionIterator iterator = new MetadataCollectionIterator(catalogGUID,
                                                                             catalogQualifiedName,
                                                                             catalogGUID,
                                                                             catalogQualifiedName,
                                                                             catalogName,
                                                                             connectorName,
                                                                             deployedImplementationType.getAssociatedTypeName(),
                                                                             openMetadataAccess,
                                                                             targetPermittedSynchronization,
                                                                             context.getMaxPageSize(),
                                                                             auditLog);

        while (iterator.moreToReceive())
        {
            MemberElement nextElement = iterator.getNextMember();

            if (nextElement != null)
            {
                /*
                 * Check that this is a UC Schema.
                 */
                String deployedImplementationType = propertyHelper.getStringProperty(catalogName,
                                                                                     OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                                     nextElement.getElement().getElementProperties(),
                                                                                     methodName);

                if (UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType().equals(deployedImplementationType))
                {
                    SchemaInfo schemaInfo = null;

                    String schemaName = propertyHelper.getStringProperty(catalogName,
                                                                         OpenMetadataProperty.RESOURCE_NAME.name,
                                                                         nextElement.getElement().getElementProperties(),
                                                                         methodName);

                    try
                    {
                        schemaInfo = ucConnector.getSchema(schemaName);
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

                    this.takeAction(memberAction, nextElement, schemaInfo);
                }
            }
        }

        return iterator;
    }


    /**
     * Review all the schemas stored in UC.
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
                    if (ucFullNameToEgeriaGUID.get(schemaInfo.getFull_name()) == null)
                    {
                        String ucSchemaQualifiedName = this.getQualifiedName(schemaInfo.getFull_name());

                        MemberElement memberElement = iterator.getMemberByQualifiedName(ucSchemaQualifiedName);
                        MemberAction memberAction   = memberElement.getMemberAction(this.getDateFromLong(schemaInfo.getCreated_at()),
                                                                                    this.getDateFromLong(schemaInfo.getUpdated_at()));
                        if (noMismatchInExternalIdentifier(schemaInfo.getSchema_id(), memberElement))
                        {
                            this.takeAction(memberAction, memberElement, schemaInfo);
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
     */
    private void takeAction(MemberAction  memberAction,
                            MemberElement memberElement,
                            SchemaInfo    schemaInfo) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        switch (memberAction)
        {
            case CREATE_INSTANCE_IN_OPEN_METADATA -> this.createElementInEgeria(schemaInfo);
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
    private void createElementInEgeria(SchemaInfo schemaInfo) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String parentLinkTypeName = OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName;
        final boolean parentAtEnd1 = true;

        String ucSchemaGUID;
        String qualifiedName = super.getQualifiedName(schemaInfo.getFull_name());

        if (templateGUID != null)
        {
            ucSchemaGUID = openMetadataAccess.createMetadataElementFromTemplate(catalogGUID,
                                                                                catalogQualifiedName,
                                                                                deployedImplementationType.getAssociatedTypeName(),
                                                                                catalogGUID,
                                                                                false,
                                                                                null,
                                                                                null,
                                                                                templateGUID,
                                                                                null,
                                                                                this.getPlaceholderProperties(schemaInfo),
                                                                                catalogGUID,
                                                                                parentLinkTypeName,
                                                                                propertyHelper.addEnumProperty(null,
                                                                                                               OpenMetadataProperty.USE_TYPE.name,
                                                                                                               ServerAssetUseType.getOpenTypeName(),
                                                                                                               ServerAssetUseType.OWNS.getName()),
                                                                                parentAtEnd1);
        }
        else
        {
            ucSchemaGUID = openMetadataAccess.createMetadataElementInStore(catalogGUID,
                                                                           catalogQualifiedName,
                                                                           deployedImplementationType.getAssociatedTypeName(),
                                                                           ElementStatus.ACTIVE,
                                                                           null,
                                                                           catalogGUID,
                                                                           false,
                                                                           null,
                                                                           null,
                                                                           this.getElementProperties(qualifiedName, schemaInfo),
                                                                           catalogGUID,
                                                                           parentLinkTypeName,
                                                                           propertyHelper.addEnumProperty(null,
                                                                                                          OpenMetadataProperty.USE_TYPE.name,
                                                                                                          ServerAssetUseType.getOpenTypeName(),
                                                                                                          ServerAssetUseType.OWNS.getName()),
                                                                           parentAtEnd1);
        }

        super.addPropertyFacet(ucSchemaGUID, qualifiedName, schemaInfo, null);

        context.addExternalIdentifier(catalogGUID,
                                      catalogQualifiedName,
                                      catalogTypeName,
                                      ucSchemaGUID,
                                      deployedImplementationType.getAssociatedTypeName(),
                                      this.getExternalIdentifierProperties(schemaInfo,
                                                                           schemaInfo.getName(),
                                                                           PlaceholderProperty.SCHEMA_NAME.getName(),
                                                                           "schema",
                                                                           schemaInfo.getSchema_id(),
                                                                           PermittedSynchronization.FROM_THIRD_PARTY));

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
        String egeriaSchemaGUID = memberElement.getElement().getElementGUID();

        openMetadataAccess.updateMetadataElementInStore(catalogGUID,
                                                        catalogQualifiedName,
                                                        egeriaSchemaGUID,
                                                        false,
                                                        getElementProperties(schemaInfo));

        context.confirmSynchronization(catalogGUID,
                                       catalogQualifiedName,
                                       egeriaSchemaGUID,
                                       deployedImplementationType.getAssociatedTypeName(),
                                       schemaInfo.getSchema_id());
    }


    /**
     * Create a schema in UC.
     *
     * @param memberElement elements from Egeria
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     */
    private void createElementInThirdParty(MemberElement memberElement) throws PropertyServerException,
                                                                               InvalidParameterException,
                                                                               UserNotAuthorizedException
    {
        SchemaInfo schemaInfo = ucConnector.createSchema(super.getUCNameFromMember(memberElement),
                                                         catalogName,
                                                         super.getUCCommentFomMember(memberElement),
                                                         super.getUCPropertiesFromMember(memberElement));

        if (memberElement.getExternalIdentifier() == null)
        {
            context.addExternalIdentifier(catalogGUID,
                                          catalogQualifiedName,
                                          catalogTypeName,
                                          memberElement.getElement().getElementGUID(),
                                          deployedImplementationType.getAssociatedTypeName(),
                                          this.getExternalIdentifierProperties(schemaInfo,
                                                                               schemaInfo.getName(),
                                                                               PlaceholderProperty.SCHEMA_NAME.getName(),
                                                                               "schema",
                                                                               schemaInfo.getSchema_id(),
                                                                               PermittedSynchronization.TO_THIRD_PARTY));
        }
        else
        {
            context.confirmSynchronization(catalogGUID,
                                           catalogQualifiedName,
                                           memberElement.getElement().getElementGUID(),
                                           deployedImplementationType.getAssociatedTypeName(),
                                           schemaInfo.getSchema_id());
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
                                                                             memberElement.getElement().getElementGUID(),
                                                                             schemaInfo.getCatalog_name() + "." + schemaInfo.getName(),
                                                                             ucServerEndpoint));

        context.confirmSynchronization(catalogGUID,
                                       catalogQualifiedName,
                                       memberElement.getElement().getElementGUID(),
                                       deployedImplementationType.getAssociatedTypeName(),
                                       schemaInfo.getSchema_id());
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
        placeholderProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), catalogName);
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
                                                                               OpenMetadataProperty.NAME.name,
                                                                               info.getName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.RESOURCE_NAME.name,
                                                             info.getFull_name());

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
