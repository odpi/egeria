/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager;


import org.odpi.openmetadata.adapters.connectors.productmanager.ffdc.ProductManagerAuditCode;
import org.odpi.openmetadata.adapters.connectors.productmanager.ffdc.ProductManagerErrorCode;
import org.odpi.openmetadata.adapters.connectors.productmanager.solutionblueprint.*;
import org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.ValidValueSetListConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ActorRoleClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SolutionBlueprintClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SolutionComponentClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.SolutionActorRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentActorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataProductsHarvesterConnector converts metadata from the open metadata ecosystem into useful digital
 * products.
 */
public class OpenMetadataProductsHarvesterConnector extends IntegrationConnectorBase implements CatalogTargetIntegrator
{
    private static final String validValueSetListCatalogTargetName = "ValidValueSetList";

    private String solutionBlueprintGUID = null;
    private Map<String, String> productFolders = new HashMap<>();


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        solutionBlueprintGUID = this.getSolutionBlueprint();
        productFolders = this.getProductCatalogFolders();
    }


    /**
     * First make sure that all possible tabular data sets have been harvested.  These are set up as catalog targets.
     * Then process each catalog target.  It will record details of any changes to the catalog target's data.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        /*
         * Determine the existing catalog targets - these are tabular data sources that are set up.
         */
        List<RequestedCatalogTarget> requestedCatalogTargets = catalogTargetsManager.refreshKnownCatalogTargets(integrationContext,
                                                                                                                this);

        Map<String, RequestedCatalogTarget> existingDataSources = new HashMap<>();

        if (requestedCatalogTargets != null)
        {
            for (RequestedCatalogTarget requestedCatalogTarget : requestedCatalogTargets)
            {
                existingDataSources.put(requestedCatalogTarget.getCatalogTargetName(), requestedCatalogTarget);
            }
        }

        /*
         * Call each of the insight harvesters to check they have their catalog targets set up.
         */
        harvestValidValues(existingDataSources);

        /*
         * Refresh all of the harvested tabular data sources, looking for data changes.
         */
        refreshCatalogTargets(this);
    }


    /**
     * Make sure data sources are set up for all valid value sets.
     *
     * @param existingDataSources existing data source map
     * @throws ConnectorCheckedException problem access the valid value set list
     */
    private void harvestValidValues(Map<String, RequestedCatalogTarget>  existingDataSources) throws ConnectorCheckedException
    {
        RequestedCatalogTarget validValueSetList = existingDataSources.get(validValueSetListCatalogTargetName);
        Connector connectorToTarget;
        if (validValueSetList == null)
        {
            connectorToTarget = createValidValueSetList();
        }
        else
        {
            connectorToTarget = validValueSetList.getConnectorToTarget();
        }

        if (connectorToTarget instanceof ValidValueSetListConnector validValueSetListConnector)
        {
            long recordCount = validValueSetListConnector.getRecordCount();
            int  columnNumber = validValueSetListConnector.getColumnNumber(OpenMetadataProperty.QUALIFIED_NAME.name);

            if (recordCount > 0)
            {
                for (long rowNumber = 0; rowNumber < recordCount; rowNumber++)
                {
                    List<String> rowValues = validValueSetListConnector.readRecord(rowNumber);

                    if ((rowValues != null) && (rowValues.size() > columnNumber))
                    {
                        String qualifiedName = rowValues.get(columnNumber);

                        if ((qualifiedName != null) && (! existingDataSources.containsKey(qualifiedName)))
                        {
                            createValidValueDataSet(qualifiedName);
                        }
                    }
                }
            }
        }
    }


    /**
     * This function creates the product catalog entry for the valid value set list, plus registers the tabular
     * data set asset as a catalog target for this integration connector.
     *
     * @return the connector for the tabular data set that
     */
    private Connector createValidValueSetList()
    {
        // todo
        return null;
    }


    private void createValidValueDataSet(String qualifiedName)
    {
        // todo
    }


    /**
     * Perform the required integration logic for the assigned catalog target.
     *
     * @param requestedCatalogTarget the catalog target
     * @throws ConnectorCheckedException there is an unrecoverable error and the connector should stop processing.
     */
    @Override
    public void integrateCatalogTarget(RequestedCatalogTarget requestedCatalogTarget) throws ConnectorCheckedException
    {
        if (requestedCatalogTarget instanceof OpenMetadataProductsHarvesterCatalogTargetProcessor catalogTargetProcessor)
        {
            catalogTargetProcessor.refresh();
        }
    }


    private Map<String, String> getProductCatalogFolders()
    {
        // todo
        return null;
    }


    /**
     * Locates/creates the solution blueprint for the open metadata digital products.
     *
     * @return guid of the blueprint or null if no blueprint can be created
     */
    private String getSolutionBlueprint()
    {
        final String methodName = "getSolutionBlueprint";

        try
        {
            String blueprintQualifiedName = ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER.getQualifiedName();

            SolutionBlueprintClient solutionBlueprintClient = integrationContext.getSolutionBlueprintClient();

            /*
             * If the solution blueprint is already present then return it GUID,
             */
            List<OpenMetadataRootElement> solutionBlueprints = solutionBlueprintClient.findSolutionBlueprints(blueprintQualifiedName, null);

            if (solutionBlueprints != null)
            {
                for (OpenMetadataRootElement solutionBlueprint : solutionBlueprints)
                {
                    if (solutionBlueprint != null)
                    {
                        return solutionBlueprint.getElementHeader().getGUID();
                    }
                }
            }

            /*
             * Create the blueprint as this is the first time through
             */
            SolutionBlueprintProperties solutionBlueprintProperties = new SolutionBlueprintProperties();

            solutionBlueprintProperties.setQualifiedName(blueprintQualifiedName);
            solutionBlueprintProperties.setDisplayName(ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER.getDisplayName());
            solutionBlueprintProperties.setDescription(ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER.getDescription());
            solutionBlueprintProperties.setVersionIdentifier(ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER.getVersionIdentifier());

            NewElementOptions newElementOptions = new NewElementOptions(solutionBlueprintClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(true);

            String blueprintGUID = solutionBlueprintClient.createSolutionBlueprint(newElementOptions,
                                                                                           null,
                                                                                           solutionBlueprintProperties,
                                                                                           null);

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(blueprintGUID);
            newElementOptions.setParentGUID(blueprintGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SOLUTION_BLUEPRINT_COMPOSITION_RELATIONSHIP.typeName);
            newElementOptions.setParentAtEnd1(true);

            SolutionComponentClient solutionComponentClient = integrationContext.getSolutionComponentClient();
            Map<String,String>      qualifiedNameToGUIDMap  = new HashMap<>();

            /*
             * Add the solution components to the blueprints.  A map of qualifiedNames to GUIDs is maintained to
             * enable the components to be linked together - and to their solution roles.
             */
            for (ProductSolutionComponent solutionComponentDefinition : ProductSolutionComponent.values())
            {
                String componentQualifiedName = solutionComponentDefinition.getQualifiedName();
                String componentGUID          = null;

                List<OpenMetadataRootElement> solutionComponents = solutionComponentClient.findSolutionComponents(componentQualifiedName, null);

                if (solutionComponents != null)
                {
                    for (OpenMetadataRootElement solutionComponent : solutionComponents)
                    {
                        if (solutionComponent != null)
                        {
                            componentGUID = solutionComponent.getElementHeader().getGUID();
                            break;
                        }
                    }
                }

                if (componentGUID == null)
                {
                    SolutionComponentProperties solutionComponentProperties = new SolutionComponentProperties();

                    solutionComponentProperties.setQualifiedName(componentQualifiedName);
                    solutionComponentProperties.setDisplayName(solutionComponentDefinition.getDisplayName());
                    solutionComponentProperties.setDescription(solutionComponentDefinition.getDescription());
                    solutionComponentProperties.setVersionIdentifier(solutionComponentDefinition.getVersionIdentifier());
                    solutionComponentProperties.setSolutionComponentType(solutionComponentDefinition.getComponentType());
                    solutionComponentProperties.setPlannedDeployedImplementationType(solutionComponentDefinition.getImplementationType());

                    componentGUID = solutionComponentClient.createSolutionComponent(newElementOptions,
                                                                                    null,
                                                                                    solutionComponentProperties,
                                                                                    null);
                }

                qualifiedNameToGUIDMap.put(componentQualifiedName, componentGUID);
            }

            /*
             * Link the components together
             */

            for (SolutionComponentWire solutionComponentWire : SolutionComponentWire.values())
            {
                SolutionLinkingWireProperties solutionLinkingWireProperties = new SolutionLinkingWireProperties();

                solutionLinkingWireProperties.setLabel(solutionComponentWire.getLabel());
                solutionLinkingWireProperties.setDescription(solutionComponentWire.getDescription());

                solutionComponentClient.linkSolutionLinkingWire(qualifiedNameToGUIDMap.get(solutionComponentWire.getComponent1().getQualifiedName()),
                                                                qualifiedNameToGUIDMap.get(solutionComponentWire.getComponent1().getQualifiedName()),
                                                                solutionComponentClient.getMetadataSourceOptions(),
                                                                solutionLinkingWireProperties);
            }

            /*
             * Create the Solution Roles and capture QualifiedNames to GUIDs in the map
             */
            ActorRoleClient actorRoleClient = integrationContext.getActorRoleClient();
            newElementOptions = new NewElementOptions(actorRoleClient.getMetadataSourceOptions());
            newElementOptions.setIsOwnAnchor(true);

            for (ProductRoleDefinition productRoleDefinition : ProductRoleDefinition.values())
            {
                String roleQualifiedName = productRoleDefinition.getQualifiedName();
                String roleGUID          = null;

                List<OpenMetadataRootElement> solutionRoles = actorRoleClient.findActorRoles(roleQualifiedName, null);

                if (solutionRoles != null)
                {
                    for (OpenMetadataRootElement solutionRole : solutionRoles)
                    {
                        if (solutionRole != null)
                        {
                            roleGUID = solutionRole.getElementHeader().getGUID();
                            break;
                        }
                    }
                }

                if (roleGUID == null)
                {
                    SolutionActorRoleProperties solutionActorRoleProperties = new SolutionActorRoleProperties();

                    solutionActorRoleProperties.setQualifiedName(roleQualifiedName);
                    solutionActorRoleProperties.setDisplayName(productRoleDefinition.getDisplayName());
                    solutionActorRoleProperties.setDescription(productRoleDefinition.getDescription());
                    solutionActorRoleProperties.setIdentifier(productRoleDefinition.getIdentifier());

                    roleGUID = actorRoleClient.createActorRole(newElementOptions,
                                                               null,
                                                               solutionActorRoleProperties,
                                                               null);
                }

                qualifiedNameToGUIDMap.put(roleQualifiedName, roleGUID);
            }

            /*
             * Connect Actor Roles to Solution Components
             */

            for (SolutionComponentActor solutionComponentActor : SolutionComponentActor.values())
            {
                SolutionComponentActorProperties solutionComponentActorProperties = new SolutionComponentActorProperties();

                solutionComponentActorProperties.setRole(solutionComponentActor.getRole());
                solutionComponentActorProperties.setDescription(solutionComponentActor.getDescription());

                solutionComponentClient.linkSolutionComponentActor(qualifiedNameToGUIDMap.get(solutionComponentActor.getSolutionRole().getQualifiedName()),
                                                                   qualifiedNameToGUIDMap.get(solutionComponentActor.getSolutionComponent().getQualifiedName()),
                                                                   solutionComponentClient.getMetadataSourceOptions(),
                                                                   solutionComponentActorProperties);
            }

            return blueprintGUID;
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                ProductManagerAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                  error.getClass().getName(),
                                                                                                  methodName,
                                                                                                  error.getMessage()));
        }

        return null;
    }


    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @return new processor based on the catalog target information
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget        retrievedCatalogTarget,
                                                                       CatalogTargetContext catalogTargetContext,
                                                                       Connector            connectorToTarget) throws ConnectorCheckedException
    {
        final String methodName = "getNewRequestedCatalogTargetSkeleton";

        try
        {
            return new OpenMetadataProductsHarvesterCatalogTargetProcessor(retrievedCatalogTarget,
                                                                 catalogTargetContext,
                                                                 connectorToTarget,
                                                                 connectorName,
                                                                 auditLog);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(ProductManagerErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }
}
