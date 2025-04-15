/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.provision;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogTemplateType;
import org.odpi.openmetadata.frameworks.governanceaction.ProvisioningGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.OperationalStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ServerAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

/**
 * ProvisionUnityCatalogGovernanceActionConnector creates a server and attaches it to an appropriate integration
 * connector (passed as an action target).
 */
public class ProvisionUnityCatalogGovernanceActionConnector extends ProvisioningGovernanceActionService
{
    /**
     * Default constructor
     */
    public ProvisionUnityCatalogGovernanceActionConnector()
    {
    }


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String methodName = "start";

        super.start();

        try
        {
            List<String>              outputGuards        = new ArrayList<>();
            List<NewActionTarget>     outputActionTargets = new ArrayList<>();
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition;
            String                    deployedImplementationType;

            deployedImplementationType = super.getProperty(ProvisionUnityCatalogRequestParameter.TECHNOLOGY_TYPE.getName(), null);

            if (deployedImplementationType == null)
            {
                messageDefinition = UCAuditCode.NO_TECHNOLOGY_TYPE.getMessageDefinition(governanceServiceName);
                outputGuards.add(ProvisionUnityCatalogGuard.NO_TECHNOLOGY_TYPE.getName());
                completionStatus = ProvisionUnityCatalogGuard.NO_TECHNOLOGY_TYPE.getCompletionStatus();
            }
            else
            {
                String templateGUID = this.getTemplateGUID(deployedImplementationType);

                if (templateGUID == null)
                {
                    messageDefinition = UCAuditCode.INVALID_TECHNOLOGY_TYPE.getMessageDefinition(governanceServiceName, deployedImplementationType);
                    outputGuards.add(ProvisionUnityCatalogGuard.INVALID_TECHNOLOGY_TYPE.getName());
                    completionStatus = ProvisionUnityCatalogGuard.INVALID_TECHNOLOGY_TYPE.getCompletionStatus();
                }
                else
                {
                    List<String> missingPlaceholderVariables = this.getMissingPlaceholderVariables(deployedImplementationType);

                    if (missingPlaceholderVariables != null)
                    {
                        messageDefinition = UCAuditCode.MISSING_PLACEHOLDER_VALUES.getMessageDefinition(governanceServiceName,
                                                                                                        deployedImplementationType,
                                                                                                        missingPlaceholderVariables.toString());
                        outputGuards.add(ProvisionUnityCatalogGuard.MISSING_PLACEHOLDER_VALUES.getName());
                        completionStatus = ProvisionUnityCatalogGuard.MISSING_PLACEHOLDER_VALUES.getCompletionStatus();
                    }
                    else
                    {
                        String elementGUID = createNewElement(deployedImplementationType, templateGUID);

                        OpenMetadataElement serverElement = governanceContext.getOpenMetadataStore().getMetadataElementByGUID(elementGUID);

                        messageDefinition = UCAuditCode.NEW_ELEMENT_CREATED.getMessageDefinition(governanceServiceName,
                                                                                                 deployedImplementationType,
                                                                                                 propertyHelper.getStringProperty(governanceServiceName,
                                                                                                                                  OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                                  serverElement.getElementProperties(),
                                                                                                                                  methodName),
                                                                                                 elementGUID);
                        NewActionTarget newActionTarget = new NewActionTarget();

                        newActionTarget.setActionTargetGUID(elementGUID);
                        newActionTarget.setActionTargetName(ActionTarget.NEW_ASSET.name);

                        outputActionTargets.add(newActionTarget);

                        completionStatus = ProvisionUnityCatalogGuard.SET_UP_COMPLETE.getCompletionStatus();
                        outputGuards.add(ProvisionUnityCatalogGuard.SET_UP_COMPLETE.getName());
                    }
                }
            }

            auditLog.logMessage(methodName, messageDefinition);

            if (outputActionTargets.isEmpty())
            {
                outputActionTargets = null;
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, outputActionTargets, messageDefinition);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(UCErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                      error.getClass().getName(),
                                                                                                      error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Retrieve the appropriate template for the deployed implementation type.  A null is returned if the
     * deployed implementation type is invalid.
     *
     * @param deployedImplementationType supplied deployed implementation type (aka technology type).
     * @return guid of the template to use, or null if the deployed implementation type is invalid
     */
    private String getTemplateGUID(String deployedImplementationType)
    {
        String templateGUID;

        if (UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType().equals(deployedImplementationType))
        {
            templateGUID = UnityCatalogTemplateType.OSS_UC_CATALOG_TEMPLATE.getTemplateGUID();
        }
        else if (UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType().equals(deployedImplementationType))
        {
            templateGUID = UnityCatalogTemplateType.OSS_UC_SCHEMA_TEMPLATE.getTemplateGUID();
        }
        else if (UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getDeployedImplementationType().equals(deployedImplementationType))
        {
            templateGUID = UnityCatalogTemplateType.OSS_UC_VOLUME_TEMPLATE.getTemplateGUID();
        }
        else if (UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getDeployedImplementationType().equals(deployedImplementationType))
        {
            templateGUID = UnityCatalogTemplateType.OSS_UC_FUNCTION_TEMPLATE.getTemplateGUID();
        }
        else if ((UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType().equals(deployedImplementationType)))
        {
            templateGUID = UnityCatalogTemplateType.OSS_UC_TABLE_TEMPLATE.getTemplateGUID();
        }
        else
        {
            return null;
        }

        return super.getProperty(ProvisionUnityCatalogRequestParameter.TEMPLATE_GUID.getName(), templateGUID);
    }


    /**
     * Return the new element that will drive the provisioning of Unity Catalog.
     *
     * @param deployedImplementationType supplied deployed implementation type (aka technology type).
     * @param templateGUID unique identifier of the template to use
     *
     * @throws InvalidParameterException one of the parameters (probably templateGUID) is invalid - or missing
     * @throws PropertyServerException problem calling the access server
     * @throws UserNotAuthorizedException security problem
     * @return guid of the new element
     */
    private String createNewElement(String deployedImplementationType,
                                    String templateGUID) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        if (UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType().equals(deployedImplementationType))
        {
            return createNewCatalog(templateGUID);
        }
        else if (UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType().equals(deployedImplementationType))
        {
            return createNewSchema(templateGUID);
        }
        else if (UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getDeployedImplementationType().equals(deployedImplementationType))
        {
            return createNewSchemaResource(templateGUID, UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getAssociatedTypeName());
        }
        else if (UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getDeployedImplementationType().equals(deployedImplementationType))
        {
            return createNewSchemaResource(templateGUID, UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getAssociatedTypeName());
        }
        else if ((UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType().equals(deployedImplementationType)))
        {
            return createNewSchemaResource(templateGUID, UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getAssociatedTypeName());
        }

       return null;
    }


    /**
     * Create a new Unity Catalog Catalog element,
     *
     * @param templateGUID unique identifier of the template
     * @return unique identifier of the new element
     * @throws InvalidParameterException one of the parameters (probably templateGUID) is invalid - or missing
     * @throws PropertyServerException problem calling the access server
     * @throws UserNotAuthorizedException security problem
     */
    private String createNewCatalog(String templateGUID) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        String serverGUID = this.getServerAnchorGUID(getProperty(UnityCatalogPlaceholderProperty.SERVER_QUALIFIED_NAME.getName(), null));

        return governanceContext.getOpenMetadataStore().getMetadataElementFromTemplate(UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getAssociatedTypeName(),
                                                                                       serverGUID,
                                                                                       false,
                                                                                       null,
                                                                                       null,
                                                                                       null,
                                                                                       templateGUID,
                                                                                       null,
                                                                                       governanceContext.getRequestParameters(),
                                                                                       serverGUID,
                                                                                       OpenMetadataType.SUPPORTED_CAPABILITY_RELATIONSHIP.typeName,
                                                                                       propertyHelper.addEnumProperty(null,
                                                                                                                      OpenMetadataProperty.OPERATIONAL_STATUS.name,
                                                                                                                      OperationalStatus.getOpenTypeName(),
                                                                                                                      OperationalStatus.ENABLED.getName()),
                                                                                       true);
    }


    /**
     * Create a new Unity Catalog Schema element,
     *
     * @param templateGUID unique identifier of the template
     * @return unique identifier of the new element
     * @throws InvalidParameterException one of the parameters (probably templateGUID) is invalid - or missing
     * @throws PropertyServerException problem calling the access server
     * @throws UserNotAuthorizedException security problem
     */
    private String createNewSchema(String templateGUID) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        String serverNetworkAddress = super.getProperty(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), null);
        String catalogName = super.getProperty(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), null);
        String catalogGUID = this.getCatalogAnchorGUID(serverNetworkAddress, catalogName);
        String catalogQualifiedName = this.getCatalogQualifiedName(catalogGUID);


        return governanceContext.getOpenMetadataStore().getMetadataElementFromTemplate(catalogGUID,
                                                                                       catalogQualifiedName,
                                                                                       UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getAssociatedTypeName(),
                                                                                       catalogGUID,
                                                                                       false,
                                                                                       null,
                                                                                       null,
                                                                                       null,
                                                                                       templateGUID,
                                                                                       null,
                                                                                       governanceContext.getRequestParameters(),
                                                                                       catalogGUID,
                                                                                       OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                                                       propertyHelper.addEnumProperty(null,
                                                                                                                      OpenMetadataProperty.USE_TYPE.name,
                                                                                                                      ServerAssetUseType.getOpenTypeName(),
                                                                                                                      ServerAssetUseType.OWNS.getName()),
                                                                                       true);
    }



    /**
     * Create a new Unity Catalog element for either a volume, table or function,
     *
     * @param templateGUID unique identifier of the template
     * @param openMetadataTypeName type
     * @return unique identifier of the new element
     * @throws InvalidParameterException one of the parameters (probably templateGUID) is invalid - or missing
     * @throws PropertyServerException problem calling the access server
     * @throws UserNotAuthorizedException security problem
     */
    private String createNewSchemaResource(String templateGUID,
                                           String openMetadataTypeName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        String serverNetworkAddress = super.getProperty(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), null);
        String catalogName = super.getProperty(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), null);
        String catalogGUID = this.getCatalogAnchorGUID(serverNetworkAddress, catalogName);
        String catalogQualifiedName = this.getCatalogQualifiedName(catalogGUID);
        String schemaGUID = this.getSchemaParentGUID(serverNetworkAddress,
                                                     catalogName,
                                                     super.getProperty(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), null));

        String elementGUID = governanceContext.getOpenMetadataStore().getMetadataElementFromTemplate(catalogGUID,
                                                                                                     catalogQualifiedName,
                                                                                                     openMetadataTypeName,
                                                                                                     schemaGUID,
                                                                                                     false,
                                                                                                     schemaGUID,
                                                                                                     null,
                                                                                                     null,
                                                                                                     templateGUID,
                                                                                                     null,
                                                                                                     governanceContext.getRequestParameters(),
                                                                                                     schemaGUID,
                                                                                                     OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                                                     null,
                                                                                                     true);

        String rootSchemaTypeQualifiedName = this.getProperty(PlaceholderProperty.ROOT_SCHEMA_TYPE_QUALIFIED_NAME.getName(), null);

        if (rootSchemaTypeQualifiedName != null)
        {
            String rootSchemaTypeGUID = governanceContext.getOpenMetadataStore().getMetadataElementGUIDByUniqueName(rootSchemaTypeQualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);

            if (rootSchemaTypeGUID != null)
            {
                governanceContext.getOpenMetadataStore().createRelatedElementsInStore(catalogGUID,
                                                                                      catalogQualifiedName,
                                                                                      OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName,
                                                                                      elementGUID,
                                                                                      rootSchemaTypeGUID,
                                                                                      null,
                                                                                      null,
                                                                                      null);
            }
        }

        return elementGUID;
    }


    /**
     * Validate that all the necessary placeholder properties have been supplied.
     *
     * @param deployedImplementationType supplied deployed implementation type (aka technology type).
     * @return list of missing placeholder properties or null
     * @throws ConnectorCheckedException unsupported deployedImplementationType
     */
    private List<String> getMissingPlaceholderVariables(String deployedImplementationType) throws ConnectorCheckedException
    {
        final String methodName = "getMissingPlaceholderVariables";

        if (UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType().equals(deployedImplementationType))
        {
            return getMissingPlaceholderVariables(UnityCatalogPlaceholderProperty.getCatalogPlaceholderPropertyTypes());
        }
        else if (UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType().equals(deployedImplementationType))
        {
            return getMissingPlaceholderVariables(UnityCatalogPlaceholderProperty.getSchemaPlaceholderPropertyTypes());
        }
        else if (UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getDeployedImplementationType().equals(deployedImplementationType))
        {
            return getMissingPlaceholderVariables(UnityCatalogPlaceholderProperty.getVolumePlaceholderPropertyTypes());
        }
        else if (UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getDeployedImplementationType().equals(deployedImplementationType))
        {
            return getMissingPlaceholderVariables(UnityCatalogPlaceholderProperty.getFunctionPlaceholderPropertyTypes());
        }
        else if ((UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType().equals(deployedImplementationType)))
        {
            return getMissingPlaceholderVariables(UnityCatalogPlaceholderProperty.getTablePlaceholderPropertyTypes());
        }

        throw new ConnectorCheckedException(UCErrorCode.LOGIC_ERROR.getMessageDefinition(governanceServiceName, methodName, deployedImplementationType),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Validate that all the necessary placeholder properties have been supplied.
     *
     * @param expectedPlaceholders list of expected placeholders
     * @return list of missing placeholders or null
     */
    private List<String> getMissingPlaceholderVariables(List<PlaceholderPropertyType> expectedPlaceholders)
    {
        List<String> missingPlaceholderValues = new ArrayList<>();
        for (PlaceholderPropertyType placeholderPropertyType : expectedPlaceholders)
        {
            if (! governanceContext.getRequestParameters().containsKey(placeholderPropertyType.getName()))
            {
                missingPlaceholderValues.add(placeholderPropertyType.getName());
            }
        }

        if (missingPlaceholderValues.isEmpty())
        {
            return null;
        }

        return missingPlaceholderValues;
    }


    /**
     * Return the GUID of the server.
     *
     * @param serverQualifiedName unique name
     * @return unique identifier of the server
     * @throws InvalidParameterException bad name
     * @throws PropertyServerException access server in trouble
     * @throws UserNotAuthorizedException security problem
     */
    private String getServerAnchorGUID(String serverQualifiedName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        return governanceContext.getOpenMetadataStore().getMetadataElementGUIDByUniqueName(serverQualifiedName,
                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name);
    }


    /**
     * Retrieve the unique identifier of the associated catalog.
     *
     * @param serverNetworkAddress location of the server
     * @param catalogName name of associated catalog
     * @return string guid
     * @throws InvalidParameterException bad name
     * @throws PropertyServerException access server in trouble
     * @throws UserNotAuthorizedException security problem
     */
    private String getCatalogAnchorGUID(String serverNetworkAddress,
                                        String catalogName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        return governanceContext.getOpenMetadataStore().getMetadataElementGUIDByUniqueName(UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType() + ":" + serverNetworkAddress + ":" + catalogName,
                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name);
    }


    /**
     * Retrieve the unique identifier of the associated schema.
     *
     * @param serverNetworkAddress location of the server
     * @param catalogName name of associated catalog
     * @param schemaName nme of associated schema
     * @return string guid
     * @throws InvalidParameterException bad name
     * @throws PropertyServerException access server in trouble
     * @throws UserNotAuthorizedException security problem
     */
    private String getSchemaParentGUID(String serverNetworkAddress,
                                       String catalogName,
                                       String schemaName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        return governanceContext.getOpenMetadataStore().getMetadataElementGUIDByUniqueName(UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType() + ":" + serverNetworkAddress + ":" + catalogName + "." + schemaName,
                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name);
    }


    /**
     * Retrieve the qualified name of the associated catalog.
     *
     * @param catalogGUID unique identifier of the catalog
     * @return string guid
     * @throws InvalidParameterException bad name
     * @throws PropertyServerException access server in trouble
     * @throws UserNotAuthorizedException security problem
     */
    private String getCatalogQualifiedName(String catalogGUID) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        final String methodName = "getCatalogQualifiedName";

        OpenMetadataElement metadataElement = governanceContext.getOpenMetadataStore().getMetadataElementByGUID(catalogGUID);

        return propertyHelper.getStringProperty(governanceServiceName,
                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                metadataElement.getElementProperties(),
                                                methodName);
    }
}
