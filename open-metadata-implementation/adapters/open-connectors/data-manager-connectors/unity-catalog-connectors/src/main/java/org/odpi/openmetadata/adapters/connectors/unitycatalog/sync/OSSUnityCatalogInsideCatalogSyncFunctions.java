/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.*;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.iterator.*;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaTypeClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.AttributeForSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.TypeEmbeddedAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIOperationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIOperationsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIParameterListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIParameterProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIRequestProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIResponseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APISchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.APIParameterType;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the specialist methods for working with Unity Catalog (UC) Functions.
 */
public class OSSUnityCatalogInsideCatalogSyncFunctions extends OSSUnityCatalogInsideCatalogSyncBase
{
    /**
     * Set up the function synchronizer.
     *
     * @param connectorName name of this connector
     * @param context context for the connector
     * @param catalogTargetName the catalog target name
     * @param serverGUID guid of the UC server asset
     * @param catalogGUID guid of the catalog
     * @param metadataCollectionGUID guid of the metadata collection for this server
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
    public OSSUnityCatalogInsideCatalogSyncFunctions(String                           connectorName,
                                                     IntegrationContext               context,
                                                     String                           catalogTargetName,
                                                     String                           serverGUID,
                                                     String                           catalogGUID,
                                                     String                           metadataCollectionGUID,
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
              serverGUID,
              catalogGUID,
              metadataCollectionGUID,
              catalogName,
              ucFullNameToEgeriaGUID,
              targetPermittedSynchronization,
              ucConnector,
              ucServerEndpoint,
              OpenMetadataType.DEPLOYED_API.typeName,
              UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION,
              templates,
              configurationProperties,
              excludeNames,
              includeNames,
              auditLog);
    }



    /**
     * Review all the functions stored in Egeria.
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
        final String methodName = "refreshEgeriaFunctions";

        RelatedElementsIterator functionIterator = new RelatedElementsIterator(context.getMetadataSourceGUID(),
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

        while (functionIterator.moreToReceive())
        {
            MemberElement nextElement = functionIterator.getNextMember();

            if ((nextElement != null) && (nextElement.getElement() != null) && (nextElement.getElement().getProperties() instanceof AssetProperties assetProperties))
            {
                /*
                 * Check that this is a UC Function.
                 */
                if (UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getDeployedImplementationType().equals(assetProperties.getDeployedImplementationType()))
                {
                    FunctionInfo functionInfo = null;

                    if (context.elementShouldBeCatalogued(assetProperties.getResourceName(), excludeNames, includeNames))
                    {
                        try
                        {
                            functionInfo = ucConnector.getFunction(assetProperties.getResourceName());
                        }
                        catch (Exception missing)
                        {
                            // this is not necessarily an error
                        }

                        MemberAction memberAction = MemberAction.NO_ACTION;
                        if (functionInfo == null)
                        {
                            memberAction = nextElement.getMemberAction(null, null);
                        }
                        else if (noMismatchInExternalIdentifier(functionInfo.getFunction_id(), nextElement))
                        {
                            memberAction = nextElement.getMemberAction(this.getDateFromLong(functionInfo.getCreated_at()),
                                                                       this.getDateFromLong(functionInfo.getUpdated_at()));
                        }

                        this.takeAction(parentGUID,
                                        super.getUCSchemaFromMember(nextElement),
                                        memberAction,
                                        nextElement,
                                        functionInfo);
                    }
                }
            }
        }

        return functionIterator;
    }



    /**
     * Review all the functions stored in UC.
     *
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through
     * @param relationshipProperties optional properties for the relationship
     * @param iterator  iterator
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     * @throws ConnectorCheckedException problem with properties
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
                        List<FunctionInfo> ucFunctionList = ucConnector.listFunctions(catalogTargetName, schemaInfo.getName());

                        if (ucFunctionList != null)
                        {
                            for (FunctionInfo functionInfo : ucFunctionList)
                            {
                                if (functionInfo != null)
                                {
                                    if (ucFullNameToEgeriaGUID.get(functionInfo.getFull_name()) == null)
                                    {
                                        String ucFunctionQualifiedName = this.getQualifiedName(functionInfo.getFull_name());

                                        MemberElement memberElement = iterator.getMemberByQualifiedName(ucFunctionQualifiedName);
                                        MemberAction memberAction = memberElement.getMemberAction(this.getDateFromLong(functionInfo.getCreated_at()),
                                                                                                  this.getDateFromLong(functionInfo.getUpdated_at()));

                                        if (noMismatchInExternalIdentifier(functionInfo.getFunction_id(), memberElement))
                                        {
                                            this.takeAction(schemaGUID, functionInfo.getSchema_name(), memberAction, memberElement, functionInfo);
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
     * @param functionInfo element from UC
     */
    private void takeAction(String        schemaGUID,
                            String        schemaName,
                            MemberAction  memberAction,
                            MemberElement memberElement,
                            FunctionInfo  functionInfo) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException,
                                                               ConnectorCheckedException
    {
        switch (memberAction)
        {
            case CREATE_INSTANCE_IN_OPEN_METADATA -> this.createElementInEgeria(schemaGUID, functionInfo);
            case UPDATE_INSTANCE_IN_OPEN_METADATA -> this.updateElementInEgeria(functionInfo, memberElement);
            case DELETE_INSTANCE_IN_OPEN_METADATA -> this.deleteElementInEgeria(memberElement);
            case CREATE_INSTANCE_IN_THIRD_PARTY   -> this.createElementInThirdParty(schemaName, memberElement);
            case UPDATE_INSTANCE_IN_THIRD_PARTY   -> this.updateElementInThirdParty(functionInfo, memberElement);
            case DELETE_INSTANCE_IN_THIRD_PARTY   -> this.deleteElementInThirdParty(functionInfo);
        }
    }


    /**
     * Create a function element in open metadata.
     *
     * @param functionInfo object from UC
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void createElementInEgeria(String       schemaGUID,
                                       FunctionInfo functionInfo) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String parentLinkTypeName = OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName;
        final boolean parentAtEnd1 = true;

        TemplateOptions templateOptions = new TemplateOptions(assetClient.getMetadataSourceOptions());

        templateOptions.setAnchorGUID(schemaGUID);
        templateOptions.setIsOwnAnchor(false);
        templateOptions.setAnchorScopeGUIDs(super.buildAnchorScopeGUIDs(schemaGUID));

        templateOptions.setParentGUID(schemaGUID);
        templateOptions.setParentAtEnd1(parentAtEnd1);
        templateOptions.setParentRelationshipTypeName(parentLinkTypeName);

        String ucFunctionGUID = openMetadataStore.createMetadataElementFromTemplate(deployedImplementationType.getAssociatedTypeName(),
                                                                                    templateOptions,
                                                                                    templateGUID,
                                                                                    null,
                                                                                    null,
                                                                                    this.getPlaceholderProperties(functionInfo),
                                                                                    null);

        super.addExternalIdentifier(ucFunctionGUID,
                                    schemaGUID,
                                    functionInfo,
                                    functionInfo.getSchema_name(),
                                    UnityCatalogPlaceholderProperty.FUNCTION_NAME.getName(),
                                    "function",
                                    functionInfo.getFunction_id(),
                                    PermittedSynchronization.FROM_THIRD_PARTY);

        this.getOrCreateSchemaAttributesForUCFunction(schemaGUID, ucFunctionGUID, functionInfo);

        ucFullNameToEgeriaGUID.put(functionInfo.getFull_name(), ucFunctionGUID);
    }


    /**
     * Using the information from UC, update the elements Egeria.
     *
     * @param functionInfo info from UC
     * @param memberElement elements from Egeria
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void updateElementInEgeria(FunctionInfo    functionInfo,
                                       MemberElement memberElement) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        String egeriaFunctionGUID = memberElement.getElement().getElementHeader().getGUID();

        UpdateOptions updateOptions = new UpdateOptions(assetClient.getUpdateOptions(true));

        openMetadataStore.updateMetadataElementInStore(egeriaFunctionGUID,
                                                       updateOptions,
                                                       this.getElementProperties(functionInfo));

        this.getOrCreateSchemaAttributesForUCFunction(super.getParentGUIDFromMember(memberElement), egeriaFunctionGUID, functionInfo);

        externalIdClient.confirmSynchronization(memberElement.getElement(),
                                                functionInfo.getFunction_id());
    }


    /**
     * Create an equivalent element in UC.
     *
     * @param memberElement elements from Egeria
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     * @throws ConnectorCheckedException missing property
     */
    private void createElementInThirdParty(String        schemaName,
                                           MemberElement memberElement) throws PropertyServerException,
                                                                               InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               ConnectorCheckedException
    {
        FunctionProperties functionProperties = new FunctionProperties();

        functionProperties.setCatalog_name(catalogTargetName);
        functionProperties.setSchema_name(schemaName);
        functionProperties.setName(super.getUCNameFromMember(memberElement));

        // todo - complete parameter mapping
        FunctionInfo functionInfo = ucConnector.createFunction(functionProperties);

        if (memberElement.getExternalIdentifier() == null)
        {
            super.addExternalIdentifier(memberElement.getElement().getElementHeader().getGUID(),
                                        super.getParentGUIDFromMember(memberElement),
                                        functionInfo,
                                        functionInfo.getSchema_name(),
                                        UnityCatalogPlaceholderProperty.FUNCTION_NAME.getName(),
                                        "function",
                                        functionInfo.getFunction_id(),
                                        PermittedSynchronization.TO_THIRD_PARTY);
        }
        else
        {
            externalIdClient.confirmSynchronization(memberElement.getElement(), functionInfo.getFunction_id());
        }
    }



    /**
     * Update the element in Unity Catalog using the values from Egeria.
     *
     * @param functionInfo info
     * @param memberElement elements from Egeria
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException open metadata repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     */
    private void updateElementInThirdParty(FunctionInfo     functionInfo,
                                           MemberElement memberElement) throws PropertyServerException,
                                                                               InvalidParameterException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "updateElementInThirdParty";

        auditLog.logMessage(methodName,
                            UCAuditCode.FUNCTION_UPDATE.getMessageDefinition(connectorName,
                                                                          memberElement.getElement().getElementHeader().getGUID(),
                                                                          functionInfo.getCatalog_name() + "." + functionInfo.getSchema_name() + "." + functionInfo.getName(),
                                                                          ucServerEndpoint));

        externalIdClient.confirmSynchronization(memberElement.getElement(), functionInfo.getFunction_id());
    }


    /**
     * Delete the function from Unity Catalog.
     *
     * @param functionInfo info object describing the element to delete.
     *
     * @throws PropertyServerException problem connecting to UC
     */
    private void deleteElementInThirdParty(FunctionInfo     functionInfo) throws PropertyServerException
    {
        final String methodName = "deleteElementInThirdParty";

        auditLog.logMessage(methodName,
                            UCAuditCode.UC_ELEMENT_DELETE.getMessageDefinition(connectorName,
                                                                               functionInfo.getCatalog_name() + "." + functionInfo.getSchema_name() + "." + functionInfo.getName(),
                                                                               ucServerEndpoint));
        ucConnector.deleteFunction(functionInfo.getFull_name());
    }


    /**
     * Return the template's placeholder properties populated with the info object's values.
     *
     * @param functionInfo object from UC
     * @return map of placeholder values
     */
    private Map<String, String> getPlaceholderProperties(FunctionInfo functionInfo)
    {
        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), ucServerEndpoint);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), functionInfo.getCatalog_name());
        placeholderProperties.put(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), functionInfo.getSchema_name());
        placeholderProperties.put(UnityCatalogPlaceholderProperty.FUNCTION_NAME.getName(), functionInfo.getName());
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), functionInfo.getComment());
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), null);

        return placeholderProperties;
    }


    /**
     * Set up the element properties for an asset from the info object.
     *
     * @param functionInfo  information extracted from UC
     *
     * @return element properties suitable for create or update
     */
    private ElementProperties getElementProperties(FunctionInfo functionInfo)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.DISPLAY_NAME.name,
                                                                               functionInfo.getName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.RESOURCE_NAME.name,
                                                             functionInfo.getFull_name());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             functionInfo.getComment());

        //elementProperties = propertyHelper.addStringMapProperty(elementProperties,
        //                                                        OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
        //                                                        functionInfo.getProperties());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                             UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getDeployedImplementationType());
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
                                                   FunctionInfo info)
    {
        ElementProperties elementProperties = this.getElementProperties(info);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                             qualifiedName);

        return elementProperties;
    }

    /**
     * Create (or, on a later refresh, find and reuse) the API schema for a UC function: an APISchemaType holding
     * a single APIOperation (representing invoking the function), with an APIParameterList of APIParameters for
     * the input parameters and, if the function returns a structured result, a second APIParameterList of
     * APIParameters for the return parameters.  This follows the same schema pattern used by the Open API
     * Cataloguer for DeployedAPI assets.
     *
     * @param schemaGUID unique identifier of the function's schema in open metadata
     * @param functionGUID unique identifier of the function in open metadata
     * @param functionInfo details about the function to add to the schema attributes
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException open metadata repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     */
    private void getOrCreateSchemaAttributesForUCFunction(String       schemaGUID,
                                                          String       functionGUID,
                                                          FunctionInfo functionInfo) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        String functionQualifiedName      = super.getQualifiedName(functionInfo.getFull_name());
        String apiSchemaTypeQualifiedName = functionQualifiedName + "_rootSchemaType";

        SchemaTypeClient schemaTypeClient = context.getSchemaTypeClient(OpenMetadataType.API_SCHEMA_TYPE.typeName);

        String apiSchemaTypeGUID = super.findExistingSchemaType(schemaTypeClient, apiSchemaTypeQualifiedName);

        if (apiSchemaTypeGUID == null)
        {
            APISchemaTypeProperties apiSchemaTypeProperties = new APISchemaTypeProperties();

            apiSchemaTypeProperties.setQualifiedName(apiSchemaTypeQualifiedName);
            apiSchemaTypeProperties.setDisplayName(functionInfo.getName() + " schema");

            NewElementOptions apiSchemaTypeOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

            apiSchemaTypeOptions.setAnchorGUID(functionGUID);
            apiSchemaTypeOptions.setIsOwnAnchor(false);
            apiSchemaTypeOptions.setAnchorScopeGUIDs(super.buildAnchorScopeGUIDs(schemaGUID, functionGUID));
            apiSchemaTypeOptions.setParentGUID(functionGUID);
            apiSchemaTypeOptions.setParentAtEnd1(true);
            apiSchemaTypeOptions.setParentRelationshipTypeName(OpenMetadataType.SCHEMA_RELATIONSHIP.typeName);

            apiSchemaTypeGUID = schemaTypeClient.createSchemaType(apiSchemaTypeOptions, null, apiSchemaTypeProperties, null);
        }

        /*
         * A UC function is represented as a single operation - invoking the function.
         */
        String apiOperationQualifiedName = functionQualifiedName + "_invoke";

        SchemaTypeClient operationClient = context.getSchemaTypeClient(OpenMetadataType.API_OPERATION.typeName);

        String apiOperationGUID = super.findExistingSchemaType(operationClient, apiOperationQualifiedName);

        if (apiOperationGUID == null)
        {
            APIOperationProperties apiOperationProperties = new APIOperationProperties();

            apiOperationProperties.setQualifiedName(apiOperationQualifiedName);
            apiOperationProperties.setDisplayName(functionInfo.getName());
            apiOperationProperties.setDescription(functionInfo.getComment());

            NewElementOptions apiOperationOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

            apiOperationOptions.setAnchorGUID(functionGUID);
            apiOperationOptions.setIsOwnAnchor(false);
            apiOperationOptions.setAnchorScopeGUIDs(super.buildAnchorScopeGUIDs(schemaGUID, functionGUID));
            apiOperationOptions.setParentGUID(apiSchemaTypeGUID);
            apiOperationOptions.setParentAtEnd1(true);
            apiOperationOptions.setParentRelationshipTypeName(OpenMetadataType.API_OPERATIONS_RELATIONSHIP.typeName);

            apiOperationGUID = operationClient.createSchemaType(apiOperationOptions, null, apiOperationProperties, new APIOperationsProperties());
        }

        SchemaAttributeClient parameterClient = context.getSchemaAttributeClient(OpenMetadataType.API_PARAMETER.typeName);

        if ((functionInfo.getInput_params() != null) && (functionInfo.getInput_params().getParameters() != null))
        {
            this.getOrCreateParameterList(schemaGUID,
                                          functionGUID,
                                          apiOperationGUID,
                                          apiOperationQualifiedName + "_request",
                                          OpenMetadataType.API_REQUEST_RELATIONSHIP.typeName,
                                          new APIRequestProperties(),
                                          APIParameterType.REQUEST_BODY,
                                          parameterClient,
                                          functionInfo.getInput_params().getParameters());
        }

        if ((functionInfo.getReturn_params() != null) && (functionInfo.getReturn_params().getParameters() != null))
        {
            this.getOrCreateParameterList(schemaGUID,
                                          functionGUID,
                                          apiOperationGUID,
                                          apiOperationQualifiedName + "_response",
                                          OpenMetadataType.API_RESPONSE_RELATIONSHIP.typeName,
                                          new APIResponseProperties(),
                                          APIParameterType.RESPONSE_BODY,
                                          parameterClient,
                                          functionInfo.getReturn_params().getParameters());
        }
    }


    /**
     * Create (or, on a later refresh, find and reuse) an APIParameterList under a function's APIOperation, and
     * create/update its APIParameter children.
     *
     * @param schemaGUID unique identifier of the function's schema in open metadata
     * @param functionGUID unique identifier of the function in open metadata
     * @param apiOperationGUID unique identifier of the function's APIOperation
     * @param listQualifiedName qualified name for the parameter list
     * @param parentRelationshipTypeName relationship type linking the list to the operation (request/response)
     * @param parentRelationshipProperties relationship properties bean matching parentRelationshipTypeName
     * @param parameterType classification of the parameters in this list (request body/response body)
     * @param parameterClient client for creating/updating APIParameter schema attributes
     * @param parameters parameter details from UC
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException open metadata repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void getOrCreateParameterList(String                      schemaGUID,
                                          String                      functionGUID,
                                          String                      apiOperationGUID,
                                          String                      listQualifiedName,
                                          String                      parentRelationshipTypeName,
                                          RelationshipProperties      parentRelationshipProperties,
                                          APIParameterType            parameterType,
                                          SchemaAttributeClient       parameterClient,
                                          List<FunctionParameterInfo> parameters) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        SchemaTypeClient listClient = context.getSchemaTypeClient(OpenMetadataType.API_PARAMETER_LIST.typeName);

        String listGUID = super.findExistingSchemaType(listClient, listQualifiedName);

        if (listGUID == null)
        {
            APIParameterListProperties listProperties = new APIParameterListProperties();

            listProperties.setQualifiedName(listQualifiedName);
            listProperties.setDisplayName(listQualifiedName);
            listProperties.setRequired(true);

            NewElementOptions listOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

            listOptions.setAnchorGUID(functionGUID);
            listOptions.setIsOwnAnchor(false);
            listOptions.setAnchorScopeGUIDs(super.buildAnchorScopeGUIDs(schemaGUID, functionGUID));
            listOptions.setParentGUID(apiOperationGUID);
            listOptions.setParentAtEnd1(true);
            listOptions.setParentRelationshipTypeName(parentRelationshipTypeName);

            listGUID = listClient.createSchemaType(listOptions, null, listProperties, parentRelationshipProperties);
        }

        for (FunctionParameterInfo parameterInfo : parameters)
        {
            if (parameterInfo != null)
            {
                String parameterQualifiedName = listQualifiedName + "::" + parameterInfo.getName();
                String parameterGUID          = super.findExistingSchemaAttribute(parameterClient, parameterQualifiedName);

                APIParameterProperties parameterProperties = new APIParameterProperties();

                parameterProperties.setQualifiedName(parameterQualifiedName);
                parameterProperties.setDisplayName(parameterInfo.getName());
                parameterProperties.setDescription(parameterInfo.getComment());
                parameterProperties.setParameterType(parameterType.getDisplayName());

                if (parameterGUID == null)
                {
                    Map<String, ClassificationProperties> initialClassifications = new HashMap<>();

                    if (parameterInfo.getType_name() != null)
                    {
                        TypeEmbeddedAttributeProperties typeEmbeddedAttributeProperties = new TypeEmbeddedAttributeProperties();

                        typeEmbeddedAttributeProperties.setSchemaTypeName(OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName);
                        typeEmbeddedAttributeProperties.setDataType(parameterInfo.getType_name());

                        initialClassifications.put(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName, typeEmbeddedAttributeProperties);
                    }

                    NewElementOptions parameterOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

                    parameterOptions.setAnchorGUID(functionGUID);
                    parameterOptions.setIsOwnAnchor(false);
                    parameterOptions.setAnchorScopeGUIDs(super.buildAnchorScopeGUIDs(schemaGUID, functionGUID));
                    parameterOptions.setParentGUID(listGUID);
                    parameterOptions.setParentAtEnd1(true);
                    parameterOptions.setParentRelationshipTypeName(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName);

                    AttributeForSchemaProperties relationshipProperties = new AttributeForSchemaProperties();

                    relationshipProperties.setPosition(parameterInfo.getPosition());

                    parameterClient.createSchemaAttribute(parameterOptions, initialClassifications, parameterProperties, relationshipProperties);
                }
                else
                {
                    parameterClient.updateSchemaAttribute(parameterGUID, parameterClient.getUpdateOptions(true), parameterProperties);
                }
            }
        }
    }
}
