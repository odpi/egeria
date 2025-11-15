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
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.iterator.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.analytics.DeployedAnalyticsModelProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the specialist methods for working with Unity Catalog (UC) Registered Models.
 */
public class OSSUnityCatalogInsideCatalogSyncRegisteredModels extends OSSUnityCatalogInsideCatalogSyncBase
{
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
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     * @throws InvalidParameterException the template is missing
     */
    public OSSUnityCatalogInsideCatalogSyncRegisteredModels(String                           connectorName,
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
              OpenMetadataType.DEPLOYED_ANALYTICS_MODEL.typeName,
              UnityCatalogDeployedImplementationType.OSS_UC_REGISTERED_MODEL,
              templates,
              configurationProperties,
              excludeNames,
              includeNames,
              auditLog);
    }



    /**
     * Review all the registered models stored in Egeria.
     *
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through
     * @param relationshipProperties optional properties for the relationship
     * @return iterator
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     * @throws ConnectorCheckedException problem extracting properties
     */
    protected IntegrationIterator refreshEgeria(String                     parentGUID,
                                                String                     parentRelationshipTypeName,
                                                RelationshipBeanProperties relationshipProperties) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          ConnectorCheckedException
    {
        RelatedElementsIterator modelIterator = new RelatedElementsIterator(context.getMetadataSourceGUID(),
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

        while (modelIterator.moreToReceive())
        {
            MemberElement nextElement = modelIterator.getNextMember();

            if ((nextElement != null) && (nextElement.getElement() != null) && (nextElement.getElement().getProperties() instanceof DeployedAnalyticsModelProperties deployedAnalyticsModelProperties))
            {
                /*
                 * Check that this is a model.
                 */
                if (UnityCatalogDeployedImplementationType.OSS_UC_REGISTERED_MODEL.getDeployedImplementationType().equals(deployedAnalyticsModelProperties.getDeployedImplementationType()))
                {
                    RegisteredModelInfo registeredModelInfo = null;

                    String modelName = deployedAnalyticsModelProperties.getResourceName();

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

                        this.takeAction(parentGUID,
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
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through
     * @param relationshipProperties optional properties for the relationship
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
                    String schemaGUID = ucFullNameToEgeriaGUID.get(schemaInfo.getFull_name());

                    if (schemaGUID != null)
                    {
                        List<RegisteredModelInfo> infoList = ucConnector.listRegisteredModels(catalogTargetName, schemaInfo.getName());

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
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     * @throws ConnectorCheckedException logic error in properties
     */
    private void takeAction(String              schemaGUID,
                            String              schemaName,
                            MemberAction        memberAction,
                            MemberElement       memberElement,
                            RegisteredModelInfo modelInfo) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException,
                                                                  ConnectorCheckedException
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

        TemplateOptions templateOptions = new TemplateOptions(assetClient.getMetadataSourceOptions());

        templateOptions.setAnchorGUID(schemaGUID);
        templateOptions.setIsOwnAnchor(false);
        templateOptions.setAnchorScopeGUID(UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getGUID());

        templateOptions.setParentGUID(schemaGUID);
        templateOptions.setParentAtEnd1(parentAtEnd1);
        templateOptions.setParentRelationshipTypeName(parentLinkTypeName);

        String ucModelGUID = openMetadataStore.createMetadataElementFromTemplate(deployedImplementationType.getAssociatedTypeName(),
                                                                          templateOptions,
                                                                          templateGUID,
                                                                          null,
                                                                          this.getPlaceholderProperties(modelInfo),
                                                                          null);

        super.addExternalIdentifier(ucModelGUID,
                                    modelInfo,
                                    modelInfo.getSchema_name(),
                                    UnityCatalogPlaceholderProperty.MODEL_NAME.getName(),
                                    "model",
                                    modelInfo.getId(),
                                    PermittedSynchronization.FROM_THIRD_PARTY);

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
        String egeriaModelGUID = memberElement.getElement().getElementHeader().getGUID();

        UpdateOptions updateOptions = new UpdateOptions(assetClient.getUpdateOptions(true));

        openMetadataStore.updateMetadataElementInStore(egeriaModelGUID,
                                                       updateOptions,
                                                       this.getElementProperties(modelInfo));

        externalIdClient.confirmSynchronization(memberElement.getElement(), modelInfo.getId());
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
                                                                               UserNotAuthorizedException,
                                                                               ConnectorCheckedException
    {
        RegisteredModelInfo modelInfo = ucConnector.createRegisteredModel(super.getUCNameFromMember(memberElement),
                                                                          catalogTargetName,
                                                                          schemaName,
                                                                          super.getUCCommentFomMember(memberElement),
                                                                          super.getUCStorageLocationFromMember(memberElement));

        if (memberElement.getExternalIdentifier() == null)
        {
            super.addExternalIdentifier(memberElement.getElement().getElementHeader().getGUID(),
                                        modelInfo,
                                        modelInfo.getSchema_name(),
                                        UnityCatalogPlaceholderProperty.MODEL_NAME.getName(),
                                        "model",
                                        modelInfo.getId(),
                                        PermittedSynchronization.TO_THIRD_PARTY);
        }
        else
        {
            externalIdClient.confirmSynchronization(memberElement.getElement(), modelInfo.getId());
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
                                                                           memberElement.getElement().getElementHeader().getGUID(),
                                                                           modelInfo.getFull_name(),
                                                                           ucServerEndpoint));

        externalIdClient.confirmSynchronization(memberElement.getElement(), modelInfo.getId());
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
                                                                               OpenMetadataProperty.DISPLAY_NAME.name,
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
