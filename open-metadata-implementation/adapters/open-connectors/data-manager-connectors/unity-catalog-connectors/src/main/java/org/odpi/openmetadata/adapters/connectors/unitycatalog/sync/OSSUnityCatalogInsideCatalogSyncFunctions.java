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
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.integration.iterator.IntegrationIterator;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberAction;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberElement;
import org.odpi.openmetadata.frameworks.integration.iterator.MetadataCollectionIterator;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the specialist methods for working with Unity Catalog (UC) Functions.
 */
public class OSSUnityCatalogInsideCatalogSyncFunctions extends OSSUnityCatalogInsideCatalogSyncBase
{
    private final String entityTypeName = OpenMetadataType.DEPLOYED_API.typeName;

    private String templateGUID = null;


    /**
     * Set up the function synchronizer.
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
    public OSSUnityCatalogInsideCatalogSyncFunctions(String                           connectorName,
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
              UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION,
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
     * Review all the functions stored in Egeria.
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
        final String methodName = "refreshEgeriaFunctions";

        MetadataCollectionIterator functionIterator = new MetadataCollectionIterator(catalogGUID,
                                                                                     catalogName,
                                                                                     catalogTargetName,
                                                                                     connectorName,
                                                                                     entityTypeName,
                                                                                     openMetadataAccess,
                                                                                     targetPermittedSynchronization,
                                                                                     context.getMaxPageSize(),
                                                                                     auditLog);

        while (functionIterator.moreToReceive())
        {
            MemberElement nextElement = functionIterator.getNextMember();

            if (nextElement != null)
            {
                /*
                 * Check that this is a UC Function.
                 */
                String deployedImplementationType = propertyHelper.getStringProperty(catalogTargetName,
                                                                                     OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                                     nextElement.getElement().getElementProperties(),
                                                                                     methodName);

                if (UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getDeployedImplementationType().equals(deployedImplementationType))
                {
                    FunctionInfo functionInfo = null;

                    String functionName = propertyHelper.getStringProperty(catalogTargetName,
                                                                           OpenMetadataProperty.RESOURCE_NAME.name,
                                                                           nextElement.getElement().getElementProperties(),
                                                                           methodName);

                    if (context.elementShouldBeCatalogued(functionName, excludeNames, includeNames))
                    {
                        try
                        {
                            functionInfo = ucConnector.getFunction(functionName);
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

                        this.takeAction(context.getAnchorGUID(nextElement.getElement()),
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
                        List<FunctionInfo> ucFunctionList = ucConnector.listFunctions(catalogName, schemaInfo.getName());

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
                                                               UserNotAuthorizedException
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
        final String parentLinkTypeName = OpenMetadataType.DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP.typeName;
        final boolean parentAtEnd1 = false;

        String ucFunctionGUID;

        if (templateGUID != null)
        {
            ucFunctionGUID = openMetadataAccess.createMetadataElementFromTemplate(catalogGUID,
                                                                                  catalogName,
                                                                                  deployedImplementationType.getAssociatedTypeName(),
                                                                                  schemaGUID,
                                                                                  false,
                                                                                  null,
                                                                                  null,
                                                                                  templateGUID,
                                                                                  null,
                                                                                  this.getPlaceholderProperties(functionInfo),
                                                                                  schemaGUID,
                                                                                  parentLinkTypeName,
                                                                                  null,
                                                                                  parentAtEnd1);
        }
        else
        {
            String qualifiedName = super.getQualifiedName(functionInfo.getFull_name());

            ucFunctionGUID = openMetadataAccess.createMetadataElementInStore(catalogGUID,
                                                                             catalogName,
                                                                             deployedImplementationType.getAssociatedTypeName(),
                                                                             ElementStatus.ACTIVE,
                                                                             null,
                                                                             schemaGUID,
                                                                             false,
                                                                             null,
                                                                             null,
                                                                             this.getElementProperties(qualifiedName, functionInfo),
                                                                             schemaGUID,
                                                                             parentLinkTypeName,
                                                                             null,
                                                                             parentAtEnd1);

            Map<String, String> facetProperties = new HashMap<>();

            facetProperties.put("parameterStyle", functionInfo.getParameter_style());

            super.addPropertyFacet(ucFunctionGUID, qualifiedName, functionInfo, facetProperties);
        }

        context.addExternalIdentifier(catalogGUID,
                                      catalogName,
                                      ucFunctionGUID,
                                      deployedImplementationType.getAssociatedTypeName(),
                                      this.getExternalIdentifierProperties(functionInfo,
                                                                           functionInfo.getSchema_name(),
                                                                           UnityCatalogPlaceholderProperty.FUNCTION_NAME.getName(),
                                                                           functionInfo.getFunction_id(),
                                                                           PermittedSynchronization.FROM_THIRD_PARTY));

        this.createSchemaAttributesForUCFunction(ucFunctionGUID, functionInfo);

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
        String egeriaFunctionGUID = memberElement.getElement().getElementGUID();

        openMetadataAccess.updateMetadataElementInStore(egeriaFunctionGUID,
                                                        false,
                                                        this.getElementProperties(functionInfo));

        this.updateSchemaAttributesForUCFunction(memberElement, functionInfo);

        context.confirmSynchronization(egeriaFunctionGUID, entityTypeName, functionInfo.getFunction_id());
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
        FunctionProperties functionProperties = new FunctionProperties();

        functionProperties.setCatalog_name(catalogName);
        functionProperties.setSchema_name(schemaName);
        functionProperties.setMetastore_id(super.getUCNameFromMember(memberElement));

        // todo - complete parameter mapping
        FunctionInfo functionInfo = ucConnector.createFunction(functionProperties);

        context.addExternalIdentifier(catalogGUID,
                                      catalogName,
                                      memberElement.getElement().getElementGUID(),
                                      deployedImplementationType.getAssociatedTypeName(),
                                      this.getExternalIdentifierProperties(functionInfo,
                                                                           functionInfo.getSchema_name(),
                                                                           UnityCatalogPlaceholderProperty.FUNCTION_NAME.getName(),
                                                                           functionInfo.getFunction_id(),
                                                                           PermittedSynchronization.TO_THIRD_PARTY));
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
                                                                          memberElement.getElement().getElementGUID(),
                                                                          functionInfo.getCatalog_name() + "." + functionInfo.getSchema_name() + "." + functionInfo.getName(),
                                                                          ucServerEndpoint));
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
                                                                               OpenMetadataProperty.NAME.name,
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
     * Create the schema attributes in open metadata reflect the columns from UC.
     *
     * @param functionGUID unique identifier of the newly created function in open metadata
     * @param functionInfo details about the function to add to the schema attributes
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException open metadata repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     */
    private void createSchemaAttributesForUCFunction(String           functionGUID,
                                                     FunctionInfo     functionInfo) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "createSchemaAttributesForUCFunction";

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        super.getQualifiedName(functionInfo.getFull_name()) + "_rootSchemaType");

        /*
         * Create the root schema type.
         */
        openMetadataAccess.createMetadataElementInStore(catalogGUID,
                                                        catalogName,
                                                        OpenMetadataType.API_SCHEMA_TYPE_TYPE_NAME,
                                                        ElementStatus.ACTIVE,
                                                        null,
                                                        functionGUID,
                                                        false,
                                                        null,
                                                        null,
                                                        properties,
                                                        functionGUID,
                                                        OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName,
                                                        null,
                                                        true);

        auditLog.logMessage(methodName,
                            UCAuditCode.MISSING_METHOD.getMessageDefinition(connectorName,
                                                                            methodName,
                                                                            functionInfo.getCatalog_name() + "." + functionInfo.getSchema_name() + "." + functionInfo.getName(),
                                                                            ucServerEndpoint));
    }


    /**
     * Create the schema attributes in open metadata reflect the columns from UC.
     *
     * @param memberElement details of the function in open metadata
     * @param functionInfo details about the function to add to the schema attributes
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException open metadata repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     */
    private void updateSchemaAttributesForUCFunction(MemberElement memberElement,
                                                  FunctionInfo     functionInfo) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "updateSchemaAttributesForUCFunction";

        auditLog.logMessage(methodName,
                            UCAuditCode.MISSING_METHOD.getMessageDefinition(connectorName,
                                                                            methodName,
                                                                            functionInfo.getCatalog_name() + "." + functionInfo.getSchema_name() + "." + functionInfo.getName(),
                                                                            ucServerEndpoint));
    }
}
