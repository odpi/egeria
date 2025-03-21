/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.RegisteredModelInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.SchemaInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.integration.iterator.IntegrationIterator;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberAction;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberElement;
import org.odpi.openmetadata.frameworks.integration.iterator.MetadataCollectionIterator;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the specialist methods for working with Unity Catalog (UC) Registered Models.
 */
public class OSSUnityCatalogInsideCatalogSyncRegisteredModels extends OSSUnityCatalogInsideCatalogSyncBase
{
    private final String entityTypeName = OpenMetadataType.DEPLOYED_ANALYTICS_MODEL.typeName;

    private String templateGUID = null;


    /**
     * Set up the registered model synchronizer.
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
    public OSSUnityCatalogInsideCatalogSyncRegisteredModels(String                           connectorName,
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
              UnityCatalogDeployedImplementationType.OSS_UC_REGISTERED_MODEL,
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
     * Review all the registered models stored in Egeria.
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
        final String methodName = "refreshEgeriaRegisteredModels";

        MetadataCollectionIterator modelIterator = new MetadataCollectionIterator(catalogGUID,
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

        while (modelIterator.moreToReceive())
        {
            MemberElement nextElement = modelIterator.getNextMember();

            if (nextElement != null)
            {
                /*
                 * Check that this is a model.
                 */
                String deployedImplementationType = propertyHelper.getStringProperty(catalogName,
                                                                                     OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                                     nextElement.getElement().getElementProperties(),
                                                                                     methodName);

                if (UnityCatalogDeployedImplementationType.OSS_UC_REGISTERED_MODEL.getDeployedImplementationType().equals(deployedImplementationType))
                {
                    RegisteredModelInfo registeredModelInfo = null;

                    String modelName = propertyHelper.getStringProperty(catalogName,
                                                                        OpenMetadataProperty.RESOURCE_NAME.name,
                                                                        nextElement.getElement().getElementProperties(),
                                                                        methodName);

                    if (context.elementShouldBeCatalogued(modelName, excludeNames, includeNames))
                    {
                        try
                        {
                            registeredModelInfo = ucConnector.getRegisteredModel(modelName);
                        }
                        catch (Exception missing)
                        {
                            // this is not necessarily an error
                        }

                        MemberAction memberAction = MemberAction.NO_ACTION;
                        if (registeredModelInfo == null)
                        {
                            memberAction = nextElement.getMemberAction(null, null);
                        }
                        else if (noMismatchInExternalIdentifier(registeredModelInfo.getId(), nextElement))
                        {
                            memberAction = nextElement.getMemberAction(this.getDateFromLong(registeredModelInfo.getCreated_at()),
                                                                       this.getDateFromLong(registeredModelInfo.getUpdated_at()));
                        }

                        this.takeAction(context.getAnchorGUID(nextElement.getElement()),
                                        super.getUCSchemaFromMember(nextElement),
                                        memberAction,
                                        nextElement,
                                        registeredModelInfo);
                    }
                }
            }
        }

        return modelIterator;
    }



    /**
     * Review all the models stored in UC.
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
                        List<RegisteredModelInfo> infoList = ucConnector.listRegisteredModels(catalogName, schemaInfo.getName());

                        if (infoList != null)
                        {
                            for (RegisteredModelInfo modelInfo : infoList)
                            {
                                if (modelInfo != null)
                                {
                                    if (ucFullNameToEgeriaGUID.get(modelInfo.getFull_name()) == null)
                                    {
                                        String ucModelQualifiedName = this.getQualifiedName(modelInfo.getFull_name());

                                        MemberElement memberElement = iterator.getMemberByQualifiedName(ucModelQualifiedName);
                                        MemberAction memberAction = memberElement.getMemberAction(this.getDateFromLong(modelInfo.getCreated_at()),
                                                                                                  this.getDateFromLong(modelInfo.getUpdated_at()));
                                        if (noMismatchInExternalIdentifier(modelInfo.getId(), memberElement))
                                        {
                                            this.takeAction(schemaGUID, modelInfo.getSchema_name(), memberAction, memberElement, modelInfo);
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
     * @param modelInfo element from UC
     */
    private void takeAction(String              schemaGUID,
                            String              schemaName,
                            MemberAction        memberAction,
                            MemberElement       memberElement,
                            RegisteredModelInfo modelInfo) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        switch (memberAction)
        {
            case CREATE_INSTANCE_IN_OPEN_METADATA -> this.createElementInEgeria(schemaGUID, modelInfo);
            case UPDATE_INSTANCE_IN_OPEN_METADATA -> this.updateElementInEgeria(modelInfo, memberElement);
            case DELETE_INSTANCE_IN_OPEN_METADATA -> this.deleteElementInEgeria(memberElement);
            case CREATE_INSTANCE_IN_THIRD_PARTY   -> this.createElementInThirdParty(schemaName, memberElement);
            case UPDATE_INSTANCE_IN_THIRD_PARTY   -> this.updateElementInThirdParty(modelInfo, memberElement);
            case DELETE_INSTANCE_IN_THIRD_PARTY   -> this.deleteElementInThirdParty(modelInfo);
        }
    }


    /**
     * Create a model element in open metadata.
     *
     * @param modelInfo object from UC
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void createElementInEgeria(String     schemaGUID,
                                       RegisteredModelInfo modelInfo) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String parentLinkTypeName = OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName;
        final boolean parentAtEnd1 = true;

        String ucModelGUID;

        if (templateGUID != null)
        {
            ucModelGUID = openMetadataAccess.getMetadataElementFromTemplate(catalogGUID,
                                                                            catalogQualifiedName,
                                                                            deployedImplementationType.getAssociatedTypeName(),
                                                                            schemaGUID,
                                                                            false,
                                                                            null,
                                                                            null,
                                                                            templateGUID,
                                                                            null,
                                                                            this.getPlaceholderProperties(modelInfo),
                                                                            schemaGUID,
                                                                            parentLinkTypeName,
                                                                            null,
                                                                            parentAtEnd1);
        }
        else
        {
            String qualifiedName = super.getQualifiedName(modelInfo.getFull_name());

            ucModelGUID = openMetadataAccess.createMetadataElementInStore(catalogGUID,
                                                                          catalogQualifiedName,
                                                                          deployedImplementationType.getAssociatedTypeName(),
                                                                          ElementStatus.ACTIVE,
                                                                          null,
                                                                          schemaGUID,
                                                                          false,
                                                                          null,
                                                                          null,
                                                                          this.getElementProperties(qualifiedName, modelInfo),
                                                                          schemaGUID,
                                                                          parentLinkTypeName,
                                                                          null,
                                                                          parentAtEnd1);

            Map<String, String> facetProperties = new HashMap<>();

            facetProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), modelInfo.getStorage_location());

            super.addPropertyFacet(ucModelGUID, qualifiedName, modelInfo, facetProperties);
        }

        context.addExternalIdentifier(catalogGUID,
                                      catalogQualifiedName,
                                      catalogTypeName,
                                      ucModelGUID,
                                      deployedImplementationType.getAssociatedTypeName(),
                                      this.getExternalIdentifierProperties(modelInfo,
                                                                           modelInfo.getSchema_name(),
                                                                           UnityCatalogPlaceholderProperty.MODEL_NAME.getName(),
                                                                           "model",
                                                                           modelInfo.getId(),
                                                                           PermittedSynchronization.FROM_THIRD_PARTY));

        ucFullNameToEgeriaGUID.put(modelInfo.getFull_name(), ucModelGUID);
    }


    /**
     * Using the information from UC, update the elements Egeria.
     *
     * @param modelInfo info from UC
     * @param memberElement elements from Egeria
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void updateElementInEgeria(RegisteredModelInfo modelInfo,
                                       MemberElement       memberElement) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        String egeriaModelGUID = memberElement.getElement().getElementGUID();

        openMetadataAccess.updateMetadataElementInStore(catalogGUID,
                                                        catalogQualifiedName,
                                                        egeriaModelGUID,
                                                        false,
                                                        this.getElementProperties(modelInfo));

        context.confirmSynchronization(catalogGUID,
                                       catalogQualifiedName,
                                       egeriaModelGUID,
                                       entityTypeName,
                                       modelInfo.getId());
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
        RegisteredModelInfo modelInfo = ucConnector.createRegisteredModel(super.getUCNameFromMember(memberElement),
                                                                          catalogName,
                                                                          schemaName,
                                                                          super.getUCCommentFomMember(memberElement),
                                                                          super.getUCStorageLocationFromMember(memberElement));

        if (memberElement.getExternalIdentifier() == null)
        {
            context.addExternalIdentifier(catalogGUID,
                                          catalogQualifiedName,
                                          catalogTypeName,
                                          memberElement.getElement().getElementGUID(),
                                          deployedImplementationType.getAssociatedTypeName(),
                                          this.getExternalIdentifierProperties(modelInfo,
                                                                               modelInfo.getSchema_name(),
                                                                               UnityCatalogPlaceholderProperty.MODEL_NAME.getName(),
                                                                               "model",
                                                                               modelInfo.getId(),
                                                                               PermittedSynchronization.TO_THIRD_PARTY));
        }
        else
        {
            context.confirmSynchronization(catalogGUID,
                                           catalogQualifiedName,
                                           memberElement.getElement().getElementGUID(),
                                           deployedImplementationType.getAssociatedTypeName(),
                                           modelInfo.getId());
        }
    }


    /**
     * Update the element in Unity Catalog using the values from Egeria.
     *
     * @param modelInfo info
     * @param memberElement elements from Egeria
     *
     * @throws InvalidParameterException bad call to Egeria
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException problem communicating with UC
     */
    private void updateElementInThirdParty(RegisteredModelInfo    modelInfo,
                                           MemberElement memberElement) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException
    {
        final String methodName = "updateElementInThirdParty";

        auditLog.logMessage(methodName,
                            UCAuditCode.VOLUME_UPDATE.getMessageDefinition(connectorName,
                                                                           memberElement.getElement().getElementGUID(),
                                                                           modelInfo.getFull_name(),
                                                                           ucServerEndpoint));

        context.confirmSynchronization(catalogGUID,
                                       catalogName,
                                       memberElement.getElement().getElementGUID(),
                                       deployedImplementationType.getAssociatedTypeName(),
                                       modelInfo.getId());
    }


    /**
     * Delete the model from Unity Catalog.
     *
     * @param modelInfo info object describing the element to delete.
     *
     * @throws PropertyServerException problem connecting to UC
     */
    private void deleteElementInThirdParty(RegisteredModelInfo  modelInfo) throws PropertyServerException
    {
        final String methodName = "deleteElementInThirdParty";

        auditLog.logMessage(methodName,
                            UCAuditCode.UC_ELEMENT_DELETE.getMessageDefinition(connectorName,
                                                                               modelInfo.getFull_name(),
                                                                               ucServerEndpoint));

        ucConnector.deleteRegisteredModel(modelInfo.getFull_name());
    }


    /**
     * Return the template's placeholder properties populated with the info object's values.
     *
     * @param info object from UC
     * @return map of placeholder values
     */
    private Map<String, String> getPlaceholderProperties(RegisteredModelInfo info)
    {
        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), ucServerEndpoint);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), info.getCatalog_name());
        placeholderProperties.put(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), info.getSchema_name());
        placeholderProperties.put(UnityCatalogPlaceholderProperty.MODEL_NAME.getName(), info.getName());
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), info.getComment());
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), null);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), info.getStorage_location());

        return placeholderProperties;
    }


    /**
     * Set up the element properties for an asset from the info object.
     *
     * @param info  information extracted from UC
     *
     * @return element properties suitable for create or update
     */
    private ElementProperties getElementProperties(RegisteredModelInfo info)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.NAME.name,
                                                                               info.getName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.RESOURCE_NAME.name,
                                                             info.getFull_name());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             info.getComment());


        // elementProperties = propertyHelper.addStringMapProperty(elementProperties,
        //                                                         OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
        //                                                         info.getProperties());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                             UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getDeployedImplementationType());

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
    private ElementProperties getElementProperties(String              qualifiedName,
                                                   RegisteredModelInfo info)
    {
        ElementProperties elementProperties = this.getElementProperties(info);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                             qualifiedName);

        return elementProperties;
    }
}
