/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.CatalogInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.iterator.IntegrationIterator;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberAction;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberElement;
import org.odpi.openmetadata.frameworks.integration.iterator.RelatedElementsIterator;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DatabaseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the specialist methods for working with a Unity Catalog (UC) server.
 * It synchronizes the server's catalogs and sets them up as catalog targets in the
 * friendship connector that configures the contents of the catalog.
 */
public class OSSUnityCatalogServerSyncCatalog extends OSSUnityCatalogInsideCatalogSyncBase
{
    private final String friendshipConnectorGUID;
    private final String metadataCollectionGUID;
    private final String metadataCollectionName;
    private final String dataAccessManagerGUID;

    private final String ucServerGUID;



    /**
     * Set up the catalog synchronizer.
     *
     * @param connectorName name of this connector
     * @param context context for the connector
     * @param catalogTargetName the catalog target name
     * @param ucServerElement description of the UC Server - including its relationships to known catalogs
     * @param dataAccessManagerGUID the data access manager software capability for this element
     * @param metadataCollectionGUID the metadata collection to use for all open metadata elements relating to this server.
     * @param metadataCollectionName the metadata collection to use for all open metadata elements relating to this server.
     * @param friendshipConnectorGUID optional unique name of inside catalog integration connector
     * @param targetPermittedSynchronization the policy that controls the direction of metadata exchange
     * @param ucConnector connector for accessing UC
     * @param ucServerEndpoint the server endpoint used to constructing the qualified names
     * @param templates templates supplied through the catalog target
     * @param configurationProperties configuration properties supplied through the catalog target
     * @param excludeCatalogs list of catalogs to ignore (and include all others)
     * @param includeCatalogs list of catalogs to include (and ignore all others) - overrides excludeCatalogs
     * @param auditLog logging destination
     * @throws UserNotAuthorizedException connector disconnected
     * @throws InvalidParameterException missing template
     */
    public OSSUnityCatalogServerSyncCatalog(String                           connectorName,
                                            IntegrationContext               context,
                                            String                           catalogTargetName,
                                            OpenMetadataRootElement          ucServerElement,
                                            String                           dataAccessManagerGUID,
                                            String                           metadataCollectionGUID,
                                            String                           metadataCollectionName,
                                            String                           friendshipConnectorGUID,
                                            PermittedSynchronization         targetPermittedSynchronization,
                                            OSSUnityCatalogResourceConnector ucConnector,
                                            String                           ucServerEndpoint,
                                            Map<String, String>              templates,
                                            Map<String, Object>              configurationProperties,
                                            List<String>                     excludeCatalogs,
                                            List<String>                     includeCatalogs,
                                            AuditLog                         auditLog) throws UserNotAuthorizedException,
                                                                                              InvalidParameterException
    {
        super(connectorName,
              context,
              catalogTargetName,
              null,
              metadataCollectionName,
              new HashMap<>(),
              targetPermittedSynchronization,
              ucConnector,
              ucServerEndpoint,
              OpenMetadataType.DATABASE.typeName,
              UnityCatalogDeployedImplementationType.OSS_UC_CATALOG,
              templates,
              configurationProperties,
              excludeCatalogs,
              includeCatalogs,
              auditLog);

        this.ucServerGUID = ucServerElement.getElementHeader().getGUID();
        this.friendshipConnectorGUID = friendshipConnectorGUID;
        this.metadataCollectionGUID = metadataCollectionGUID;
        this.metadataCollectionName = metadataCollectionName;
        this.dataAccessManagerGUID = dataAccessManagerGUID;

        /*
         * The server synchronization connector may not have been set up with the metadata collection identifiers,
         * so they are set up explicitly.
         */
        this.assetClient.setExternalSource(metadataCollectionGUID, metadataCollectionName);
        this.propertyFacetClient.setExternalSource(metadataCollectionGUID, metadataCollectionName);
        this.externalIdClient.setExternalSource(metadataCollectionGUID, metadataCollectionName);
        this.openMetadataStore.setExternalSource(metadataCollectionGUID, metadataCollectionName);
    }


    /**
     * Review all the catalogs stored in Egeria.
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
    @Override
    protected IntegrationIterator refreshEgeria(String                     parentGUID,
                                                String                     parentRelationshipTypeName,
                                                RelationshipBeanProperties relationshipProperties) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          ConnectorCheckedException
    {
        int parentAtEnd = 1;
        RelatedElementsIterator iterator = new RelatedElementsIterator(metadataCollectionGUID,
                                                                       catalogTargetName,
                                                                       connectorName,
                                                                       parentGUID,
                                                                       parentRelationshipTypeName,
                                                                       parentAtEnd,
                                                                       deployedImplementationType.getAssociatedTypeName(),
                                                                       context,
                                                                       targetPermittedSynchronization,
                                                                       context.getMaxPageSize(),
                                                                       auditLog);

        while (iterator.moreToReceive())
        {
            MemberElement nextElement = iterator.getNextMember();

            if ((nextElement != null) &&
                    (nextElement.getElement() != null) &&
                    (propertyHelper.isTypeOf(nextElement.getElement().getElementHeader(), UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getAssociatedTypeName())) &&
                    (nextElement.getElement().getProperties() instanceof DatabaseProperties databaseProperties))
            {
                /*
                 * Check that this is a UC Catalog.
                 */
                if (UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType().equals(databaseProperties.getDeployedImplementationType()))
                {
                    CatalogInfo catalogInfo = null;
                    String      catalogName = databaseProperties.getResourceName();

                    if (context.elementShouldBeCatalogued(catalogName, excludeNames, includeNames))
                    {
                        try
                        {
                            catalogInfo = ucConnector.getCatalog(catalogName);
                        }
                        catch (Exception missing)
                        {
                            // this is not necessarily an error
                        }

                        MemberAction memberAction = MemberAction.NO_ACTION;
                        if (catalogInfo == null)
                        {
                            memberAction = nextElement.getMemberAction(null, null);
                        }
                        else if (noMismatchInExternalIdentifier(catalogInfo.getId(), nextElement))
                        {
                            memberAction = nextElement.getMemberAction(this.getDateFromLong(catalogInfo.getCreated_at()), this.getDateFromLong(catalogInfo.getUpdated_at()));
                        }

                        this.takeAction(memberAction, nextElement, catalogInfo, parentGUID, parentRelationshipTypeName, relationshipProperties);
                    }
                    else if (friendshipConnectorGUID != null)
                    {
                        AssetClient assetClient = context.getAssetClient();

                        /*
                         * Check that there is no catalog target set up for this catalog since it should not be catalogued.
                         */
                        int startingFrom = 0;
                        List<OpenMetadataRootElement> catalogTargets = assetClient.getCatalogTargets(friendshipConnectorGUID,
                                                                                                     assetClient.getQueryOptions(startingFrom, context.getMaxPageSize()));
                        while (catalogTargets != null)
                        {
                            for (OpenMetadataRootElement catalogTarget : catalogTargets)
                            {
                                if ((catalogTarget != null) && (catalogTarget.getRelatedBy().getRelationshipProperties() instanceof CatalogTargetProperties catalogTargetProperties))
                                {
                                    if (catalogName.equals(catalogTargetProperties.getCatalogTargetName()))
                                    {
                                        assetClient.removeCatalogTarget(catalogTarget.getRelatedBy().getRelationshipHeader().getGUID(), assetClient.getDeleteOptions(false));
                                    }
                                }
                            }

                            startingFrom = startingFrom + context.getMaxPageSize();

                            catalogTargets = assetClient.getCatalogTargets(friendshipConnectorGUID,
                                                                           assetClient.getQueryOptions(startingFrom, context.getMaxPageSize()));
                        }
                    }
                }
            }
        }

        return iterator;
    }


    /**
     * Review all the catalogs stored in UC.
     *
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through
     * @param relationshipProperties optional properties for the relationship
     * @param iterator  Metadata collection iterator
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     * @throws ConnectorCheckedException logic error in properties
     */
    @Override
    protected void refreshUnityCatalog(String                     parentGUID,
                                       String                     parentRelationshipTypeName,
                                       RelationshipBeanProperties relationshipProperties,
                                       IntegrationIterator        iterator) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException,
                                                                                   ConnectorCheckedException
    {
        List<CatalogInfo> ucCatalogList = ucConnector.listCatalogs();

        if (ucCatalogList != null)
        {
            for (CatalogInfo catalogInfo : ucCatalogList)
            {
                if (catalogInfo != null)
                {
                    if (ucFullNameToEgeriaGUID.get(catalogInfo.getName()) == null)
                    {
                        String ucCatalogQualifiedName = this.getQualifiedName(catalogInfo.getName());

                        MemberElement memberElement = iterator.getMemberByQualifiedName(ucCatalogQualifiedName);
                        MemberAction memberAction   = memberElement.getMemberAction(this.getDateFromLong(catalogInfo.getCreated_at()),
                                                                                    this.getDateFromLong(catalogInfo.getUpdated_at()));

                        if (noMismatchInExternalIdentifier(catalogInfo.getId(), memberElement))
                        {
                            this.takeAction(memberAction, memberElement, catalogInfo, parentGUID, parentRelationshipTypeName, relationshipProperties);
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
     * @param catalogInfo element from UC
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through
     * @param relationshipProperties optional properties for the relationship
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     * @throws ConnectorCheckedException logic error in properties
     */
    private void takeAction(MemberAction               memberAction,
                            MemberElement              memberElement,
                            CatalogInfo                catalogInfo,
                            String                     parentGUID,
                            String                     parentRelationshipTypeName,
                            RelationshipBeanProperties relationshipProperties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException,
                                                                                      ConnectorCheckedException
    {
        switch (memberAction)
        {
            case CREATE_INSTANCE_IN_OPEN_METADATA -> this.createElementInEgeria(catalogInfo, parentGUID, parentRelationshipTypeName, relationshipProperties);
            case UPDATE_INSTANCE_IN_OPEN_METADATA -> this.updateElementInEgeria(catalogInfo, memberElement);
            case DELETE_INSTANCE_IN_OPEN_METADATA -> this.deleteElementInEgeria(memberElement);
            case CREATE_INSTANCE_IN_THIRD_PARTY   -> this.createElementInThirdParty(memberElement);
            case UPDATE_INSTANCE_IN_THIRD_PARTY   -> this.updateElementInThirdParty(catalogInfo, memberElement);
            case DELETE_INSTANCE_IN_THIRD_PARTY   -> this.deleteElementInThirdParty(catalogInfo);
        }
    }


    /**
     * Create an element in open metadata.
     *
     * @param catalogInfo object from UC
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void createElementInEgeria(CatalogInfo                catalogInfo,
                                       String                     parentGUID,
                                       String                     parentRelationshipTypeName,
                                       RelationshipBeanProperties relationshipProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        String ucCatalogGUID;
        String qualifiedName = super.getQualifiedName(catalogInfo.getName());

        TemplateOptions templateOptions = new TemplateOptions();

        templateOptions.setAnchorGUID(ucServerGUID);
        templateOptions.setIsOwnAnchor(false);
        templateOptions.setAnchorScopeGUID(UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getGUID());

        templateOptions.setParentGUID(parentGUID);
        templateOptions.setParentAtEnd1(true);
        templateOptions.setParentRelationshipTypeName(parentRelationshipTypeName);

        ucCatalogGUID = assetClient.createAssetFromTemplate(templateOptions,
                                                            templateGUID,
                                                            this.getReplacementProperties(qualifiedName, catalogInfo),
                                                            this.getPlaceholderProperties(catalogInfo),
                                                            relationshipProperties);

        ucFullNameToEgeriaGUID.put(catalogInfo.getName(), ucCatalogGUID);

        super.addPropertyFacet(ucCatalogGUID, qualifiedName, catalogInfo, null);

        super.addExternalIdentifier(ucCatalogGUID,
                                    catalogInfo,
                                    null,
                                    UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(),
                                    "catalog",
                                    catalogInfo.getId(),
                                    PermittedSynchronization.FROM_THIRD_PARTY);

        addCatalogTarget(ucServerGUID, ucCatalogGUID, catalogInfo.getName(), templates, configurationProperties);
    }


    /**
     * Update an element in open metadata.
     *
     * @param catalogInfo object from UC
     * @param memberElement existing element in egeria
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    protected void updateElementInEgeria(CatalogInfo   catalogInfo,
                                         MemberElement memberElement) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        String egeriaCatalogGUID = memberElement.getElement().getElementHeader().getGUID();

        UpdateOptions updateOptions = new UpdateOptions(assetClient.getUpdateOptions(true));

        openMetadataStore.updateMetadataElementInStore(egeriaCatalogGUID,
                                                       updateOptions,
                                                       getElementProperties(catalogInfo));

        externalIdClient.confirmSynchronization(memberElement.getElement(), catalogInfo.getId());
    }


    /**
     * Create a catalog in UC.
     *
     * @param memberElement elements from Egeria
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     * @throws ConnectorCheckedException logic error in properties
     */
    private void createElementInThirdParty(MemberElement memberElement) throws PropertyServerException,
                                                                               InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               ConnectorCheckedException
    {
        CatalogInfo catalogInfo = ucConnector.createCatalog(this.getUCNameFromMember(memberElement),
                                                            this.getUCCommentFomMember(memberElement),
                                                            this.getUCPropertiesFromMember(memberElement));

        if (memberElement.getExternalIdentifier() == null)
        {
            super.addExternalIdentifier(memberElement.getElement().getElementHeader().getGUID(),
                                        catalogInfo,
                                        null,
                                        UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(),
                                        "catalog",
                                        catalogInfo.getId(),
                                        PermittedSynchronization.TO_THIRD_PARTY);
        }
        else
        {
            externalIdClient.confirmSynchronization(memberElement.getElement(),
                                                    catalogInfo.getId());
        }

        addCatalogTarget(ucServerGUID, memberElement.getElement().getElementHeader().getGUID(), catalogInfo.getName(), templates, configurationProperties);
    }


    /**
     * Update the catalog in UC.
     *
     * @param catalogInfo existing catalog in UC
     * @param memberElement elements from Egeria
     *
     * @throws InvalidParameterException bad call to Egeria
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException  problem communicating with UC
     */
    private void updateElementInThirdParty(CatalogInfo   catalogInfo,
                                           MemberElement memberElement) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException
    {
        final String methodName = "updateElementInThirdParty";

        auditLog.logMessage(methodName,
                            UCAuditCode.CATALOG_UPDATE.getMessageDefinition(connectorName,
                                                                             memberElement.getElement().getElementHeader().getGUID(),
                                                                             catalogInfo.getName(),
                                                                             ucServerEndpoint));

        externalIdClient.confirmSynchronization(memberElement.getElement(), catalogInfo.getId());
    }


    /**
     * Delete the UC element.
     *
     * @param info info object describing the element to delete.
     *
     * @throws PropertyServerException problem connecting to UC
     */
    private void deleteElementInThirdParty(CatalogInfo    info) throws PropertyServerException
    {
        ucConnector.deleteCatalog(info.getName(), false);
    }


    /**
     * Return the template's placeholder properties populated with the info object's values.
     *
     * @param info object from UC
     * @return map of placeholder values
     */
    private Map<String, String> getPlaceholderProperties(CatalogInfo info)
    {
        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), ucServerEndpoint);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), info.getName());
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
    private ReferenceableProperties getReplacementProperties(CatalogInfo info)
    {
        DatabaseProperties catalogProperties = new DatabaseProperties();

        catalogProperties.setDisplayName(info.getName());
        catalogProperties.setDescription(info.getComment());
        catalogProperties.setDeployedImplementationType(UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType());

        // catalogProperties.setAdditionalProperties(info.getProperties());

        return catalogProperties;
    }



    /**
     * Set up the element properties for an asset from the info object.
     *
     * @param info  information extracted from UC
     *
     * @return element properties suitable for create or update
     */
    private ElementProperties getElementProperties(CatalogInfo info)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.DISPLAY_NAME.name,
                                                                               info.getName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             info.getComment());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                             UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType());

       // elementProperties = propertyHelper.addStringMapProperty(elementProperties,
       //                                                         OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
       //                                                         info.getProperties());

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
    private EntityProperties getReplacementProperties(String      qualifiedName,
                                                      CatalogInfo info)
    {
        ReferenceableProperties elementProperties = this.getReplacementProperties(info);

        elementProperties.setQualifiedName(qualifiedName);

        return elementProperties;
    }


    /**
     * Add a catalog target relationship between the UC server's asset and the connector that is able to
     * catalog inside a UC catalog.  This will start the cataloging of the datasets within this UC catalog.
     *
     * @param ucServerGUID unique identifier of the server asset - this is null if the UC Server was passed as an endpoint nor a catalog target
     * @param ucCatalogGUID unique identifier of the catalog
     * @param ucCatalogName name of the catalog - may be used as a placeholder property
     * @param templates list of templates
     * @param configurationProperties configuration properties for this server
     *
     * @throws InvalidParameterException error from call to the metadata store
     * @throws PropertyServerException error from call to the metadata store
     * @throws UserNotAuthorizedException error from call to the metadata store
     */
    private void addCatalogTarget(String              ucServerGUID,
                                  String              ucCatalogGUID,
                                  String              ucCatalogName,
                                  Map<String, String> templates,
                                  Map<String, Object> configurationProperties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "addCatalogTarget";

        if (ucServerGUID != null)
        {
            if (friendshipConnectorGUID != null)
            {
                CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

                catalogTargetProperties.setCatalogTargetName(ucCatalogName);
                catalogTargetProperties.setMetadataSourceQualifiedName(metadataCollectionName);
                catalogTargetProperties.setTemplates(templates);

                Map<String, Object> targetConfigurationProperties = new HashMap<>();

                if (configurationProperties != null)
                {
                    targetConfigurationProperties.putAll(configurationProperties);
                }

                targetConfigurationProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), ucCatalogName);
                targetConfigurationProperties.put(OpenMetadataProperty.GUID.name, ucCatalogGUID);

                catalogTargetProperties.setConfigurationProperties(targetConfigurationProperties);

                String relationshipGUID = context.getAssetClient().addCatalogTarget(friendshipConnectorGUID,
                                                                                    ucServerGUID,
                                                                                    new MakeAnchorOptions(context.getAssetClient().getMetadataSourceOptions()),
                                                                                    catalogTargetProperties);

                auditLog.logMessage(methodName,
                                    UCAuditCode.NEW_CATALOG_TARGET.getMessageDefinition(connectorName,
                                                                                        relationshipGUID,
                                                                                        friendshipConnectorGUID,
                                                                                        ucServerGUID,
                                                                                        ucCatalogName));
            }
        }
    }


}
