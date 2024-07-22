/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.CatalogInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.integration.iterator.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.OperationalStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the specialist methods for working with Unity Catalog (UC) Catalog.
 */
public class OSSUnityCatalogServerSyncCatalog extends OSSUnityCatalogInsideCatalogSyncBase
{
    private final String friendshipConnectorGUID;

    private String templateGUID = null;
    private final String ucServerGUID;

    private final String parentLinkTypeName = OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.typeName;


    /**
     * Set up the catalog synchronizer.
     *
     * @param connectorName name of this connector
     * @param context context for the connector
     * @param catalogTargetName the catalog target name
     * @param ucServerGUID unique identifier of the UC Server
     * @param friendshipConnectorGUID optional unique name of inside catalog integration connector
     * @param targetPermittedSynchronization the policy that controls the direction of metadata exchange
     * @param ucConnector connector for accessing UC
     * @param ucServerEndpoint the server endpoint used to constructing the qualified names
     * @param templates templates supplied through the catalog target
     * @param configurationProperties configuration properties supplied through the catalog target
     * @param excludeCatalogs list of catalogs to ignore (and include all others)
     * @param includeCatalogs list of catalogs to include (and ignore all others) - overrides excludeCatalogs
     * @param auditLog logging destination
     */
    public OSSUnityCatalogServerSyncCatalog(String                           connectorName,
                                            CatalogIntegratorContext         context,
                                            String                           catalogTargetName,
                                            String                           ucServerGUID,
                                            String                           friendshipConnectorGUID,
                                            PermittedSynchronization         targetPermittedSynchronization,
                                            OSSUnityCatalogResourceConnector ucConnector,
                                            String                           ucServerEndpoint,
                                            Map<String, String>              templates,
                                            Map<String, Object>              configurationProperties,
                                            List<String>                     excludeCatalogs,
                                            List<String>                     includeCatalogs,
                                            AuditLog                         auditLog)
    {
        super(connectorName,
              context,
              catalogTargetName,
              null,
              new HashMap<>(),
              targetPermittedSynchronization,
              ucConnector,
              ucServerEndpoint,
              DeployedImplementationType.OSS_UC_CATALOG,
              templates,
              configurationProperties,
              excludeCatalogs,
              includeCatalogs,
              auditLog);

        this.ucServerGUID = ucServerGUID;
        this.friendshipConnectorGUID = friendshipConnectorGUID;

        if (templates != null)
        {
            this.templateGUID = templates.get(DeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType());
        }
    }


    /**
     * Review all the catalogs stored in Egeria.
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

        int parentAtEnd = 1;
        RelatedElementsIterator iterator = new RelatedElementsIterator(this.context.getMetadataSourceGUID(),
                                                                       this.context.getMetadataSourceQualifiedName(),
                                                                       catalogTargetName,
                                                                       connectorName,
                                                                       ucServerGUID,
                                                                       parentLinkTypeName,
                                                                       parentAtEnd,
                                                                       deployedImplementationType.getAssociatedTypeName(),
                                                                       openMetadataAccess,
                                                                       targetPermittedSynchronization,
                                                                       context.getMaxPageSize(),
                                                                       auditLog);

        while (iterator.moreToReceive())
        {
            MemberElement nextElement = iterator.getNextMember();

            if ((nextElement != null) &&
                    (nextElement.getElement() != null) &&
                    (propertyHelper.isTypeOf(nextElement.getElement(), OpenMetadataType.CATALOG.typeName)))
            {
                /*
                 * Check that this is a UC Table.
                 */
                String deployedImplementationType = propertyHelper.getStringProperty(catalogTargetName,
                                                                                     OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                                     nextElement.getElement().getElementProperties(),
                                                                                     methodName);

                if (DeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType().equals(deployedImplementationType))
                {
                    CatalogInfo info = null;

                    String name = propertyHelper.getStringProperty(catalogTargetName,
                                                                   OpenMetadataProperty.NAME.name,
                                                                   nextElement.getElement().getElementProperties(),
                                                                   methodName);

                    if (context.elementShouldBeCatalogued(name, excludeNames, includeNames))
                    {
                        try
                        {
                            info = ucConnector.getCatalog(name);
                        }
                        catch (Exception missing)
                        {
                            // this is not necessarily an error
                        }

                        MemberAction memberAction = MemberAction.NO_ACTION;
                        if (info == null)
                        {
                            memberAction = nextElement.getMemberAction(null, null);
                        }
                        else if (noMismatchInExternalIdentifier(info.getId(), nextElement))
                        {
                            memberAction = nextElement.getMemberAction(this.getDateFromLong(info.getCreated_at()), this.getDateFromLong(info.getUpdated_at()));
                        }

                        this.takeAction(memberAction, nextElement, info);
                    }
                    else if (friendshipConnectorGUID != null)
                    {
                        /*
                         * Check that there is no catalog target set up for this catalog since it should not be catalogued.
                         */
                        int startingFrom = 0;
                        List<CatalogTarget> catalogTargets = context.getCatalogTargets(friendshipConnectorGUID,
                                                                                       startingFrom,
                                                                                       context.getMaxPageSize());
                        while (catalogTargets != null)
                        {
                            for (CatalogTarget catalogTarget : catalogTargets)
                            {
                                if (catalogTarget != null)
                                {
                                    if (name.equals(catalogTarget.getCatalogTargetName()))
                                    {
                                        context.removeCatalogTarget(catalogTarget.getRelationshipGUID());
                                    }
                                }
                            }

                            startingFrom = startingFrom + context.getMaxPageSize();

                            catalogTargets = context.getCatalogTargets(friendshipConnectorGUID,
                                                                       startingFrom,
                                                                       context.getMaxPageSize());
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
                            this.takeAction(memberAction, memberElement, catalogInfo);
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
     */
    private void takeAction(MemberAction  memberAction,
                            MemberElement memberElement,
                            CatalogInfo    catalogInfo) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        switch (memberAction)
        {
            case CREATE_INSTANCE_IN_OPEN_METADATA -> this.createElementInEgeria(catalogInfo);
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
    private void createElementInEgeria(CatalogInfo catalogInfo) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        String ucCatalogGUID;
        String qualifiedName = super.getQualifiedName(catalogInfo.getName());

        boolean parentAtEnd1 = true;
        if (templateGUID != null)
        {
            ucCatalogGUID = openMetadataAccess.createMetadataElementFromTemplate(deployedImplementationType.getAssociatedTypeName(),
                                                                                 ucServerGUID,
                                                                                 false,
                                                                                 null,
                                                                                 null,
                                                                                 templateGUID,
                                                                                 this.getElementProperties(qualifiedName, catalogInfo),
                                                                                 this.getPlaceholderProperties(catalogInfo),
                                                                                 ucServerGUID,
                                                                                 parentLinkTypeName,
                                                                                 propertyHelper.addEnumProperty(null,
                                                                                                                OpenMetadataProperty.OPERATIONAL_STATUS.name,
                                                                                                                OperationalStatus.getOpenTypeName(),
                                                                                                                OperationalStatus.ENABLED.getName()),
                                                                                 parentAtEnd1);
        }
        else
        {
            ucCatalogGUID = openMetadataAccess.createMetadataElementInStore(deployedImplementationType.getAssociatedTypeName(),
                                                                            ElementStatus.ACTIVE,
                                                                            null,
                                                                            ucServerGUID,
                                                                            false,
                                                                            null,
                                                                            null,
                                                                            this.getElementProperties(qualifiedName, catalogInfo),
                                                                            ucServerGUID,
                                                                            parentLinkTypeName,
                                                                            propertyHelper.addEnumProperty(null,
                                                                                                           OpenMetadataProperty.OPERATIONAL_STATUS.name,
                                                                                                           OperationalStatus.getOpenTypeName(),
                                                                                                           OperationalStatus.ENABLED.getName()),
                                                                            parentAtEnd1);
        }

        ucFullNameToEgeriaGUID.put(catalogInfo.getName(), ucCatalogGUID);

        context.setMetadataSourceQualifiedName(ucCatalogGUID, qualifiedName);
        context.addExternalIdentifier(ucCatalogGUID,
                                      OpenMetadataType.CATALOG.typeName,
                                      this.getExternalIdentifierProperties(catalogInfo,
                                                                           null,
                                                                           UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(),
                                                                           catalogInfo.getId(),
                                                                           PermittedSynchronization.FROM_THIRD_PARTY));
        addCatalogTarget(ucServerGUID, qualifiedName, catalogInfo.getName(), templates, configurationProperties);
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
        String egeriaCatalogGUID = memberElement.getElement().getElementGUID();

        openMetadataAccess.updateMetadataElementInStore(egeriaCatalogGUID,
                                                        false,
                                                        getElementProperties(catalogInfo));

        context.confirmSynchronization(egeriaCatalogGUID,
                                       deployedImplementationType.getAssociatedTypeName(),
                                       catalogInfo.getId());
    }


    /**
     * Create a catalog in UC.
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
        CatalogInfo catalogInfo = ucConnector.createCatalog(this.getUCNameFromMember(memberElement),
                                                            this.getUCCommentFomMember(memberElement),
                                                            this.getUCPropertiesFomMember(memberElement));

        context.addExternalIdentifier(memberElement.getElement().getElementGUID(),
                                      OpenMetadataType.CATALOG.typeName,
                                      this.getExternalIdentifierProperties(catalogInfo,
                                                                           null,
                                                                           UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(),
                                                                           catalogInfo.getId(),
                                                                           PermittedSynchronization.TO_THIRD_PARTY));
    }


    /**
     * Update the catalog in UC.
     *
     * @param catalogInfo existing catalog in UC
     * @param memberElement elements from Egeria
     *
     * @throws PropertyServerException  problem communicating with UC
     */
    private void updateElementInThirdParty(CatalogInfo    catalogInfo,
                                           MemberElement memberElement) throws PropertyServerException
    {
        final String methodName = "updateElementInThirdParty";

        auditLog.logMessage(methodName,
                            UCAuditCode.CATALOG_UPDATE.getMessageDefinition(connectorName,
                                                                             memberElement.getElement().getElementGUID(),
                                                                             catalogInfo.getName(),
                                                                             ucServerEndpoint));
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
    private ElementProperties getElementProperties(CatalogInfo info)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.NAME.name,
                                                                               info.getName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             info.getComment());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                             DeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType());

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
    private ElementProperties getElementProperties(String      qualifiedName,
                                                   CatalogInfo info)
    {
        ElementProperties elementProperties = this.getElementProperties(info);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                             qualifiedName);

        return elementProperties;
    }


    /**
     * Add a catalog target relationship between the UC server's asset and the connector that is able to
     * catalog inside a UC catalog.  This will start the cataloging of the datasets within this UC catalog.
     *
     * @param ucServerGUID unique identifier of the server asset - this is null if the UC Server was passed as an endpoint nor a catalog target
     * @param ucCatalogQualifiedName qualified name of the UC Catalog's software capability - becomes metadataSourceQualifiedName
     * @param ucCatalogName name of the catalog - may be used as a placeholder property
     * @param templates list of templates
     * @param configurationProperties configuration properties for this server
     *
     * @throws InvalidParameterException error from call to the metadata store
     * @throws PropertyServerException error from call to the metadata store
     * @throws UserNotAuthorizedException error from call to the metadata store
     */
    private void addCatalogTarget(String              ucServerGUID,
                                  String              ucCatalogQualifiedName,
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
                catalogTargetProperties.setMetadataSourceQualifiedName(ucCatalogQualifiedName);
                catalogTargetProperties.setTemplateProperties(templates);

                Map<String, Object> targetConfigurationProperties = new HashMap<>();

                if (configurationProperties != null)
                {
                    targetConfigurationProperties.putAll(configurationProperties);
                }

                targetConfigurationProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), ucCatalogName);

                catalogTargetProperties.setConfigurationProperties(targetConfigurationProperties);

                String relationshipGUID = context.addCatalogTarget(friendshipConnectorGUID, ucServerGUID, catalogTargetProperties);

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
