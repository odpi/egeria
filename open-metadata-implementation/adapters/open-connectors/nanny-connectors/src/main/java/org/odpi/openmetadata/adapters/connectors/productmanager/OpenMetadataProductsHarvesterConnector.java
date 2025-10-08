/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager;


import org.odpi.openmetadata.adapters.connectors.governanceactions.subscriptions.ManageDigitalSubscriptionActionTarget;
import org.odpi.openmetadata.adapters.connectors.governanceactions.subscriptions.ManageDigitalSubscriptionRequestParameter;
import org.odpi.openmetadata.adapters.connectors.productmanager.ffdc.ProductManagerAuditCode;
import org.odpi.openmetadata.adapters.connectors.productmanager.ffdc.ProductManagerErrorCode;
import org.odpi.openmetadata.adapters.connectors.productmanager.productcatalog.*;
import org.odpi.openmetadata.adapters.connectors.productmanager.solutionblueprint.*;
import org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.ValidValueSetListConnector;
import org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.ValidValueSetListProvider;
import org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.controls.ReferenceDataConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.connectorcontext.ConnectorConfigClient;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceActionTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CoverageCategory;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.SolutionActorRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.referencedata.ReferenceCodeTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.NoteLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentActorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * OpenMetadataProductsHarvesterConnector converts metadata from the open metadata ecosystem into useful digital
 * products.  The first phase (in the start() method) is to create all of the contextual metadata that surrounds the
 * product catalog.  The remaining phases happen in the refresh() method.  It first surveys open metadata looking for
 * metadata that could be a product.  (For example, a valid value set).  It creates an entry in the product catalog
 * for that product and registers the tabular data set asset for the product as a catalog target.
 * <br><br>
 * Once the possible products are in place, it processes the catalog targets.  For each, the appropriate metadata
 * is scanned for changes.  These changes are recorded in the asset's GovernanceMeasurement classification.
 * This triggers the notification watchdog to send the new data to the subscribers via the provisioning pipelines.
 */
public class OpenMetadataProductsHarvesterConnector extends IntegrationConnectorBase implements CatalogTargetIntegrator
{
    private static final String validValueSetListCatalogTargetName = "ValidValueSetList";

    /*
     * Set everything to null to catch issue where refresh() is called without start() since
     * It will result in NPEs.  This is not expected.
     */
    private String              solutionBlueprintGUID = null;
    private String              anchorScopeGUID       = null;
    private Map<String, String> productFolders        = null;
    private Map<String, String> productRoles          = null;
    private Map<String, String> governanceDefinitions = null;
    private Map<String, String> glossaryTerms         = null;
    private Map<String, String> communities           = null;
    private Map<String, String> communityNoteLogs     = null;
    private Map<String, String> dataFields            = null;
    private Map<String, String> products              = null;


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * It sets up the contextual metadata used to fill out the product catalog.
     * This includes the solution blueprint that covers the components involved in managing
     * the open metadata product catalog.  Then there is the product catalog itself with its
     * internal folders, glossary and data dictionary.  The glossary is then populated
     * with glossary terms, and the data dictionary is populated with data fields.
     * The guids for these elements are managed in instance variables to allow the products
     * to link to them.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        try
        {
            auditLog.logMessage(methodName, ProductManagerAuditCode.STARTING_CONNECTOR.getMessageDefinition(connectorName,
                                                                                                            integrationContext.getMetadataAccessServer(),
                                                                                                            integrationContext.getMetadataAccessServerPlatformURLRoot()));

            /*
             * These governance definitions support the product catalog initiative.
             * They are linked to the product catalog.
             */
            governanceDefinitions = this.getGovernanceDefinitions();

            /*
             * The product folders are set up first so that the top level folder for the product catalog can be
             * the anchor scope for every thing else.
             */
            productFolders        = this.getProductCatalogFolders();
            productRoles          = this.getProductRoles();
            solutionBlueprintGUID = this.getSolutionBlueprint();
            glossaryTerms         = this.getGlossaryTerms();
            communities           = this.getCommunities();
            communityNoteLogs     = this.getCommunityNoteLogs();
            dataFields            = this.getDataFields();
            products              = this.getProducts();
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                ProductManagerAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                  error.getClass().getName(),
                                                                                                  methodName,
                                                                                                  error.getMessage()));

            throw new ConnectorCheckedException(ProductManagerErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
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
        final String methodName = "harvestValidValues";

        RequestedCatalogTarget validValueSetList = existingDataSources.get(validValueSetListCatalogTargetName);

        if (validValueSetList != null)
        {
            Connector connectorToTarget = validValueSetList.getConnectorToTarget();

            if (connectorToTarget instanceof ValidValueSetListConnector validValueSetListConnector)
            {
                long recordCount  = validValueSetListConnector.getRecordCount();
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
                                try
                                {
                                    String productGUID = createValidValueDataSet(qualifiedName);

                                    if (productGUID != null)
                                    {
                                        auditLog.logMessage(methodName,
                                                            ProductManagerAuditCode.NEW_VALID_VALUE_PRODUCT.getMessageDefinition(connectorName, productGUID, qualifiedName));

                                    }
                                }
                                catch (Exception error)
                                {
                                    auditLog.logException(methodName,
                                                          ProductManagerAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                            error.getClass().getName(),
                                                                                                                            methodName,
                                                                                                                            error.getMessage()),
                                                          error);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Create a product that represents a single valid value set.
     *
     * @param qualifiedName unique name of the valid value set
     * @return unique identifier of the product
     */
    private String createValidValueDataSet(String qualifiedName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        ValidValueDefinitionClient validValueDefinitionClient = integrationContext.getValidValueDefinitionClient();

        List<OpenMetadataRootElement> validValueSets = validValueDefinitionClient.getValidValueDefinitionsByName(qualifiedName,
                                                                                                                 validValueDefinitionClient.getQueryOptions());

        /*
         * There should be just one valid value set.
         */
        if (validValueSets != null)
        {
            for (OpenMetadataRootElement validValueSet : validValueSets)
            {
                if ((validValueSet != null) && (validValueSet.getProperties() instanceof ValidValueDefinitionProperties validValueDefinitionProperties))
                {
                    ProductDefinition productDefinition = new ProductDefinitionBean(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                                                                                    new ProductDefinition[]{ ProductDefinitionEnum.VALID_VALUE_SETS },
                                                                                    "Valid Value Set: " + validValueDefinitionProperties.getDisplayName(),
                                                                                    "OPEN-METADATA-" + OpenMetadataType.VALID_VALUE_DEFINITION.typeName + "-" + validValueDefinitionProperties.getQualifiedName(),
                                                                                    null,
                                                                                    validValueDefinitionProperties.getDisplayName(),
                                                                                    validValueDefinitionProperties.getDescription(),
                                                                                    ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
                                                                                    ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                                                                                    ProductCommunityDefinition.REFERENCE_DATA_SIG,
                                                                                    new ProductSubscriptionDefinition[]{
                                                                                            ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                                                                            ProductSubscriptionDefinition.ONGOING_UPDATE},
                                                                                    "Valid Value Set " + validValueDefinitionProperties.getDisplayName(),
                                                                                    new ProductDataFieldDefinition[]{
                                                                                            ProductDataFieldDefinition.GUID},
                                                                                    new ProductDataFieldDefinition[]{
                                                                                            ProductDataFieldDefinition.QUALIFIED_NAME,
                                                                                            ProductDataFieldDefinition.DISPLAY_NAME,
                                                                                            ProductDataFieldDefinition.DESCRIPTION,
                                                                                            ProductDataFieldDefinition.CATEGORY,
                                                                                            ProductDataFieldDefinition.NAMESPACE,
                                                                                            ProductDataFieldDefinition.PREFERRED_VALUE,
                                                                                            ProductDataFieldDefinition.IS_CASE_SENSITIVE,
                                                                                            ProductDataFieldDefinition.DATA_TYPE,
                                                                                            ProductDataFieldDefinition.SCOPE,
                                                                                            ProductDataFieldDefinition.USAGE},
                                                                                    new ValidValueSetListProvider(),
                                                                                    "ValidValueSet:" + qualifiedName);

                    return this.getProduct(productDefinition);
                }
            }
        }

        return null;
    }



    /**
     * Return the map of qualifiedNames-to-guids for the pre-defined products that make up the
     * fixed part of the product catalog.
     *
     * @return map
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getProducts() throws InvalidParameterException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException
    {
        Map<String, String> productMap = new HashMap<>();

        for (ProductDefinition productDefinition : ProductDefinitionEnum.values())
        {
            String productGUID = this.getProduct(productDefinition);

            productMap.put(productDefinition.getQualifiedName(), productGUID);
        }

        return productMap;
    }


    /**
     * Return the unique identifier of a product, either by retrieving it form the open metadata
     * repository or by creating the product.
     *
     * @param productDefinition description of the product
     * @return unique identifier
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getProduct(ProductDefinition productDefinition) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        CollectionClient            collectionClient            = integrationContext.getCollectionClient(OpenMetadataType.DIGITAL_PRODUCT.typeName);
        ClassificationManagerClient classificationManagerClient = integrationContext.getClassificationManagerClient();

        final String methodName = "getProduct";

        /*
         * If the product is already present then return its GUID.  This works for product s and product families,
         */
        List<OpenMetadataRootElement> existingProducts = collectionClient.getCollectionsByName(productDefinition.getQualifiedName(), null);

        if (existingProducts != null)
        {
            for (OpenMetadataRootElement existingProduct : existingProducts)
            {
                if (existingProduct != null)
                {
                    auditLog.logMessage(methodName,
                                        ProductManagerAuditCode.RETRIEVING_OPEN_METADATA_PRODUCT.getMessageDefinition(connectorName,
                                                                                                                      existingProduct.getElementHeader().getGUID(),
                                                                                                                      productDefinition.getProductName()));

                    return existingProduct.getElementHeader().getGUID();
                }
            }
        }

        /*
         * First time, so the product needs to be created, along with its subscription options,
         * solution blueprint, data spec, notification type and asset.  The asset needs to be
         * registered as a catalog target.
         */
        DigitalProductProperties digitalProductProperties = new DigitalProductProperties();

        digitalProductProperties.setTypeName(productDefinition.getTypeName()); // may be family or product
        digitalProductProperties.setQualifiedName(productDefinition.getQualifiedName());
        digitalProductProperties.setDisplayName(productDefinition.getDisplayName());
        digitalProductProperties.setDescription(productDefinition.getDescription());
        digitalProductProperties.setVersionIdentifier(productDefinition.getVersionIdentifier());
        digitalProductProperties.setCategory(productDefinition.getCategory());

        NewElementOptions newElementOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);
        newElementOptions.setAnchorScopeGUID(this.anchorScopeGUID);

        if (productDefinition.getFolder() != null)
        {
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentGUID(productFolders.get(productDefinition.getFolder().getQualifiedName()));
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
        }

        String productGUID = collectionClient.createCollection(newElementOptions,
                                                               null,
                                                               digitalProductProperties,
                                                               null);

        auditLog.logMessage(methodName,
                            ProductManagerAuditCode.NEW_OPEN_METADATA_PRODUCT.getMessageDefinition(connectorName,
                                                                                                   productGUID,
                                                                                                   productDefinition.getProductName()));

        /*
         * Link the product manager
         */
        String productManagerGUID = null;
        if (productDefinition.getProductManager() != null)
        {
            productManagerGUID = productRoles.get(productDefinition.getProductManager().getQualifiedName());

            collectionClient.linkProductManager(productGUID,
                                                productManagerGUID,
                                                collectionClient.getMetadataSourceOptions(),
                                                null);
        }


        /*
         * Link in the license type to the product to show what type of license is granted to the subscriber.
         */
        String licenseTypeGUID = null;
        if (productDefinition.getLicense() != null)
        {
            licenseTypeGUID = governanceDefinitions.get(productDefinition.getLicense().getQualifiedName());

            GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();

            GovernedByProperties governedByProperties = new GovernedByProperties();
            governedByProperties.setLabel("subscriber's license");
            governedByProperties.setDescription("This is the license that a subscriber's asset will be given to access the product data.");
            governanceDefinitionClient.addGovernanceDefinitionToElement(productGUID, licenseTypeGUID, governanceDefinitionClient.getMetadataSourceOptions(), governedByProperties);
        }

        /*
         * Link the community to the product family if defined
         */
        String communityNoteLogGUID = null;
        if ((OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName.equals(productDefinition.getTypeName())) && (productDefinition.getCommunity() != null))
        {
            communityNoteLogGUID = communityNoteLogs.get(productDefinition.getCommunity().getQualifiedName());

            classificationManagerClient.addScopeToElement(communityNoteLogGUID,
                                                          productGUID,
                                                          classificationManagerClient.getMetadataSourceOptions(),
                                                          null);
        }

        /*
         * The data specification lists all of the data fields for this product.
         */
        this.addDataSpec(productDefinition, productGUID);

        /*
         * This asset has a connector to a connector that is able to mine open metadata to create a particular product.
         */
        String productAssetGUID = this.addProductAsset(productDefinition, productGUID);

        /*
         * The subscription options show up as governance action processes that are configured with the appropriate
         * information.
         */
        this.addSubscriptionTypes(productDefinition, productGUID, productAssetGUID, communityNoteLogGUID, productManagerGUID);

        /*
         * Register each product as a catalog target, so it is refreshed.
         */
        if (OpenMetadataType.DIGITAL_PRODUCT.typeName.equals(productDefinition.getTypeName()))
        {
            ConnectorConfigClient connectorConfigClient = integrationContext.getConnectorConfigClient();

            CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

            catalogTargetProperties.setCatalogTargetName(productDefinition.getCatalogTargetName());
            catalogTargetProperties.setConnectionName(productDefinition.getProductName());
            catalogTargetProperties.setPermittedSynchronization(PermittedSynchronization.BOTH_DIRECTIONS);
            catalogTargetProperties.setDeleteMethod(DeleteMethod.LOOK_FOR_LINEAGE);

            connectorConfigClient.addCatalogTarget(integrationContext.getIntegrationConnectorGUID(),
                                                   productAssetGUID,
                                                   catalogTargetProperties);
        }

        if (productDefinition.getProductFamilies() != null)
        {
            for (ProductDefinition productGroup : productDefinition.getProductFamilies())
            {
                CollectionMembershipProperties collectionMembershipProperties = new CollectionMembershipProperties();

                collectionMembershipProperties.setMembershipType("includes product");

                collectionClient.addToCollection(products.get(productGroup.getQualifiedName()),
                                                 productGUID,
                                                 collectionClient.getMetadataSourceOptions(),
                                                 collectionMembershipProperties);
            }
        }

        return productGUID;
    }


    /**
     * Set up a product's data spec.
     *
     * @param productDefinition description of product
     * @param productGUID unique identifier of the product
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private void addDataSpec(ProductDefinition productDefinition,
                             String            productGUID) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        List<ProductDataFieldDefinition> dataFieldIdentifiers = productDefinition.getDataSpecIdentifiers();
        List<ProductDataFieldDefinition> dataFieldDefinitions = productDefinition.getDataSpecFields();

        if ((dataFieldIdentifiers != null) || (dataFieldDefinitions != null))
        {
            DataSpecProperties dataSpecProperties = new DataSpecProperties();

            dataSpecProperties.setQualifiedName(productDefinition.getQualifiedName() + "_dataSpec");
            dataSpecProperties.setDisplayName("Data Specification for " + productDefinition.getDisplayName());
            dataSpecProperties.setDescription("The data specification lists the fields in the " + productDefinition.getProductName() + " product.");
            dataSpecProperties.setVersionIdentifier(productDefinition.getVersionIdentifier());

            CollectionClient    collectionClient    = integrationContext.getCollectionClient(OpenMetadataType.DATA_SPEC_COLLECTION.typeName);
            DataStructureClient dataStructureClient = integrationContext.getDataStructureClient();

            NewElementOptions newElementOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorScopeGUID(this.anchorScopeGUID);
            newElementOptions.setAnchorGUID(productGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentGUID(productGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.DATA_DESCRIPTION_RELATIONSHIP.typeName);

            DataDescriptionProperties dataDescriptionProperties = new DataDescriptionProperties();

            dataDescriptionProperties.setLabel("data-specification");
            dataDescriptionProperties.setDescription("Description of the data structure(s) used in this product.  Each data structure is a member of the data specification.");

            String dataSpecGUID = collectionClient.createCollection(newElementOptions,
                                                                    null,
                                                                    dataSpecProperties,
                                                                    dataDescriptionProperties);

            DataStructureProperties dataStructureProperties = new DataStructureProperties();

            dataStructureProperties.setQualifiedName(productDefinition.getQualifiedName() + "_dataSpec.dataStructure");
            dataStructureProperties.setDisplayName("Data Structure for " + productDefinition.getDisplayName());
            dataStructureProperties.setDescription("The data structure lists the fields in the " + productDefinition.getProductName() + " product.");
            dataStructureProperties.setVersionIdentifier(productDefinition.getVersionIdentifier());
            dataStructureProperties.setNamePatterns(Collections.singletonList(productDefinition.getDataSpecTableName()));

            newElementOptions.setParentGUID(dataSpecGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

            String dataStructureGUID = dataStructureClient.createDataStructure(newElementOptions,
                                                                               null,
                                                                               dataStructureProperties,
                                                                               null);

            int fieldPosition = 1;

            if (dataFieldIdentifiers != null)
            {
                for (ProductDataFieldDefinition dataField : dataFieldIdentifiers)
                {
                    String dataFieldGUID = dataFields.get(dataField.getQualifiedName());

                    MemberDataFieldProperties memberDataFieldProperties = new MemberDataFieldProperties();

                    memberDataFieldProperties.setCoverageCategory(CoverageCategory.IDENTIFIER);
                    memberDataFieldProperties.setPosition(fieldPosition);
                    fieldPosition ++;

                    dataStructureClient.linkMemberDataField(dataStructureGUID,
                                                            dataFieldGUID,
                                                            dataStructureClient.getMetadataSourceOptions(),
                                                            memberDataFieldProperties);
                }
            }

            if (dataFieldDefinitions != null)
            {
                for (ProductDataFieldDefinition dataField : dataFieldDefinitions)
                {
                    String dataFieldGUID = dataFields.get(dataField.getQualifiedName());

                    MemberDataFieldProperties memberDataFieldProperties = new MemberDataFieldProperties();

                    memberDataFieldProperties.setCoverageCategory(CoverageCategory.CORE_DETAIL);
                    memberDataFieldProperties.setPosition(fieldPosition);
                    fieldPosition ++;

                    dataStructureClient.linkMemberDataField(dataStructureGUID,
                                                            dataFieldGUID,
                                                            dataStructureClient.getMetadataSourceOptions(),
                                                            memberDataFieldProperties);
                }
            }
        }
    }


    /**
     * Set up a product's subscription types.  Each are governance action processes configured with an appropriate
     * subscription template.  When the governance action process runs, it creates the subscription for the requesting
     * actor.
     *
     * @param productDefinition description of product
     * @param productGUID unique identifier of the product
     * @param productAssetGUID unique identifier for the asset that represents the product
     * @param communityNoteLogGUID unique identifier of the community's note log
     * @param productManagerGUID unique identifier for the product manager
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private void addSubscriptionTypes(ProductDefinition productDefinition,
                                      String            productGUID,
                                      String            productAssetGUID,
                                      String            communityNoteLogGUID,
                                      String            productManagerGUID) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        if (productDefinition.getSubscriptionTypes() != null)
        {
            for (ProductSubscriptionDefinition productSubscriptionDefinition : productDefinition.getSubscriptionTypes())
            {
                if (productAssetGUID != null)
                {
                    addNotificationType(productSubscriptionDefinition,
                                        productGUID,
                                        productDefinition.getProductName(),
                                        productAssetGUID,
                                        communityNoteLogGUID,
                                        productManagerGUID);
                }

                addSubscriptionGovernanceActionProcess(productDefinition.getProductName(),
                                                       productGUID,
                                                       productSubscriptionDefinition,
                                                       productManagerGUID);
            }
        }
    }


    /**
     * Set up a digital product's notification type.  Each are governance action processes configured with an appropriate
     * subscription template.  When the governance action process runs, it creates the subscription for the requesting
     * actor.
     *
     * @param productSubscriptionDefinition description of the subscription type that is supported by the product
     * @param productGUID unique identifier of the product
     * @param productName name of the product
     * @param productAssetGUID unique identifier for the asset that represents the product
     * @param communityNoteLogGUID unique identifier of the community's note log
     * @param productManagerGUID unique identifier for the product manager
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private void addNotificationType(ProductSubscriptionDefinition productSubscriptionDefinition,
                                     String                        productGUID,
                                     String                        productName,
                                     String                        productAssetGUID,
                                     String                        communityNoteLogGUID,
                                     String                        productManagerGUID) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        /*
         * The notification of changes to a subscription is managed via a notification type.
         */
        GovernanceDefinitionClient notificationTypeClient = integrationContext.getGovernanceDefinitionClient(OpenMetadataType.NOTIFICATION_TYPE.typeName);

        NotificationTypeProperties notificationTypeProperties = new NotificationTypeProperties();

        notificationTypeProperties.setQualifiedName(OpenMetadataType.NOTIFICATION_TYPE.typeName + "::" + productGUID + "::" + productSubscriptionDefinition.getIdentifier());
        notificationTypeProperties.setIdentifier(productSubscriptionDefinition.getIdentifier());
        notificationTypeProperties.setDisplayName("Notification type for " + productSubscriptionDefinition.getDisplayName());
        notificationTypeProperties.setDescription(productSubscriptionDefinition.getDescription());

        NewElementOptions newElementOptions = new NewElementOptions(notificationTypeClient.getMetadataSourceOptions());

        newElementOptions.setAnchorGUID(productGUID);
        newElementOptions.setAnchorScopeGUID(productGUID);
        newElementOptions.setIsOwnAnchor(false);

        String notificationTypeGUID = notificationTypeClient.createGovernanceDefinition(newElementOptions,
                                                                                        null,
                                                                                        notificationTypeProperties,
                                                                                        null);

        /*
         * The engine action for the watchdog notification serve is started before the monitored resource
         * and subscribers are attached to ensure the notification watchdog sees their attachment events and
         * sends out the welcome messages.
         */
        String engineActionGUID = this.activateNotificationWatchdog(productGUID, productManagerGUID, productSubscriptionDefinition, notificationTypeGUID);

        MonitoredResourceProperties monitoredResourceProperties = new MonitoredResourceProperties();

        monitoredResourceProperties.setLabel("product asset");
        monitoredResourceProperties.setDescription("This is the product asset that represents the data for the " + productName + " product.");

        notificationTypeClient.linkMonitoredResource(notificationTypeGUID, productAssetGUID, notificationTypeClient.getMetadataSourceOptions(), monitoredResourceProperties);

        NotificationSubscriberProperties notificationSubscriberProperties = new NotificationSubscriberProperties();

        if (communityNoteLogGUID != null)
        {
            notificationSubscriberProperties.setLabel("community notifications");
            notificationSubscriberProperties.setDescription("A note log collects the notifications from the notification watchdog manager: " + engineActionGUID);

            notificationTypeClient.linkNotificationSubscriber(notificationTypeGUID, communityNoteLogGUID, notificationTypeClient.getMetadataSourceOptions(), notificationSubscriberProperties);
        }

        if (productManagerGUID != null)
        {
            notificationSubscriberProperties.setLabel("product manager notifications");
            notificationSubscriberProperties.setDescription("Notifications from the notification watchdog manager: " + engineActionGUID + " are sent to the product manager.");

            notificationTypeClient.linkNotificationSubscriber(notificationTypeGUID, productManagerGUID, notificationTypeClient.getMetadataSourceOptions(), notificationSubscriberProperties);
        }
    }


    /**
     * Set up a product's subscription types.  Each are governance action processes configured with an appropriate
     * subscription template.  When the governance action process runs, it creates the subscription for the requesting
     * actor.
     *
     * @param productName name of product
     * @param productGUID unique identifier of the product
     * @param productSubscriptionDefinition details of the subscription type
     * @param productManagerGUID unique identifier for the product manager
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private void addSubscriptionGovernanceActionProcess(String                        productName,
                                                        String                        productGUID,
                                                        ProductSubscriptionDefinition productSubscriptionDefinition,
                                                        String                        productManagerGUID) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        /*
         * The subscription notification watchdog manager is running.  Now build the governance action process that is
         * used to add a new subscription of this type.
         */
        Map<String, String> additionalRequestParameters = new HashMap<>();

        additionalRequestParameters.put(ManageDigitalSubscriptionRequestParameter.SUBSCRIPTION_NAME.getName(), productSubscriptionDefinition.getDisplayName());
        additionalRequestParameters.put(ManageDigitalSubscriptionRequestParameter.SUBSCRIPTION_IDENTIFIER.getName(), productSubscriptionDefinition.getIdentifier());
        additionalRequestParameters.put(ManageDigitalSubscriptionRequestParameter.SUBSCRIPTION_DESCRIPTION.getName(), productSubscriptionDefinition.getDescription());

        String governanceActionProcessGUID = integrationContext.createProcessFromGovernanceActionType(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName + "::" + productName + "::" + ResourceUse.CREATE_SUBSCRIPTION.getResourceUse() + "::" + productSubscriptionDefinition.getIdentifier(),
                                                                                                      "Create " + productSubscriptionDefinition.getDisplayName() + " for " + productName,
                                                                                                      productSubscriptionDefinition.getDescription() + "  Supply the requester (actor entity) as an action target called digitalSubscriptionRequester and the asset where the data is to be sent to as action target named destinationDataSet.",
                                                                                                      GovernanceActionTypeDefinition.CREATE_SUBSCRIPTION.getGovernanceActionTypeGUID(),
                                                                                                      additionalRequestParameters,
                                                                                                      productGUID,
                                                                                                      productGUID);

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                       governanceActionProcessGUID,
                                                       productGUID,
                                                       null,
                                                       null,
                                                       propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_ITEM.getName()));

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                       governanceActionProcessGUID,
                                                       productManagerGUID,
                                                       null,
                                                       null,
                                                       propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.DIGITAL_PRODUCT_OWNER.getName()));

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                       governanceActionProcessGUID,
                                                       governanceDefinitions.get(productSubscriptionDefinition.getServiceLevelObjective().getQualifiedName()),
                                                       null,
                                                       null,
                                                       propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.SERVICE_LEVEL_OBJECTIVE.getName()));

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                       governanceActionProcessGUID,
                                                       GovernanceActionTypeDefinition.PROVISION_TABULAR.getGovernanceActionTypeGUID(),
                                                       null,
                                                       null,
                                                       propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.getName()));

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                       governanceActionProcessGUID,
                                                       GovernanceActionTypeDefinition.CANCEL_SUBSCRIPTION.getGovernanceActionTypeGUID(),
                                                       null,
                                                       null,
                                                       propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.getName()));

        /*
         * Remove the specification properties for the action targets that have already been supplied.  This means
         * that the remaining specification properties cover the ones that the caller needs to supply.
         */
        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName);

        OpenMetadataRootElement governanceActionProcess = governanceDefinitionClient.getGovernanceDefinitionByGUID(governanceActionProcessGUID, governanceDefinitionClient.getGetOptions());

        if (governanceActionProcess.getSpecificationProperties() != null)
        {
            // todo
        }

        /*
         * Link the new governance action process to the product.
         */
        ClassificationManagerClient classificationManagerClient = integrationContext.getClassificationManagerClient();

        ResourceListProperties resourceListProperties = new ResourceListProperties();

        resourceListProperties.setResourceUse(ResourceUse.CREATE_SUBSCRIPTION.getResourceUse());
        resourceListProperties.setDescription(ResourceUse.CREATE_SUBSCRIPTION.getDescription());

        classificationManagerClient.addResourceListToElement(productGUID,
                                                             governanceActionProcessGUID,
                                                             classificationManagerClient.getMetadataSourceOptions(),
                                                             resourceListProperties);
    }


    /**
     * Set up a product's data spec.
     *
     * @param productDefinition description of product
     * @param productGUID unique identifier of the product
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String addProductAsset(ProductDefinition productDefinition,
                                   String            productGUID) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "addProductAsset";

        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.REFERENCE_CODE_TABLE.typeName);

        ReferenceCodeTableProperties dataSetProperties = new ReferenceCodeTableProperties();

        dataSetProperties.setQualifiedName(productDefinition.getQualifiedName() + "_referenceCodeTable");
        dataSetProperties.setDisplayName("Reference data set for " + productDefinition.getDisplayName());
        dataSetProperties.setDescription("This asset represents the source of data for the digital product.");
        dataSetProperties.setVersionIdentifier(productDefinition.getVersionIdentifier());

        NewElementOptions newElementOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorGUID(productGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentGUID(productGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

        Map<String, ClassificationProperties> initialClassifications = new HashMap<>();
        initialClassifications.put(OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION.typeName, null);

        String assetGUID = assetClient.createAsset(newElementOptions,
                                                   initialClassifications,
                                                   dataSetProperties,
                                                   null);

        auditLog.logMessage(methodName,
                            ProductManagerAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                       dataSetProperties.getTypeName(),
                                                                                                       dataSetProperties.getDisplayName(),
                                                                                                       assetGUID));

        String connectorTypeGUID = this.getConnectorTypeGUID(productDefinition.getConnectorProvider());

        if (connectorTypeGUID != null)
        {
            /*
             * Set up the connection for the asset.
             */
            ConnectionClient connectionClient = integrationContext.getConnectionClient();

            ConnectionProperties connectionProperties = new ConnectionProperties();

            connectionProperties.setQualifiedName(productDefinition.getQualifiedName() + "_referenceDataSet_connection");
            connectionProperties.setDisplayName("Asset Connection for " + productDefinition.getDisplayName());
            connectionProperties.setDescription("This connection provides access to the metadata access server that supplied the data for this digital product.");
            connectionProperties.setVersionIdentifier(productDefinition.getVersionIdentifier());
            connectionProperties.setUserId(integrationContext.getMyUserId());

            Map<String, Object> connectionConfigurationProperties = new HashMap<>();
            connectionConfigurationProperties.put(ReferenceDataConfigurationProperty.SERVER_NAME.getName(), integrationContext.getMetadataAccessServer());
            connectionConfigurationProperties.put(ReferenceDataConfigurationProperty.MAX_PAGE_SIZE.getName(), integrationContext.getMaxPageSize());
            connectionProperties.setConfigurationProperties(connectionConfigurationProperties);

            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentGUID(assetGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName);

            String connectionGUID = connectionClient.createConnection(newElementOptions,
                                                                      null,
                                                                      connectionProperties,
                                                                      null);

            auditLog.logMessage(methodName,
                                ProductManagerAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                           connectionProperties.getTypeName(),
                                                                                                           connectionProperties.getDisplayName(),
                                                                                                           connectionGUID));
            /*
             * Connect the connection to the connectorType
             */
            connectionClient.linkConnectionConnectorType(connectionGUID,
                                                         connectorTypeGUID,
                                                         connectionClient.getMetadataSourceOptions(),
                                                         null);

            auditLog.logMessage(methodName,
                                ProductManagerAuditCode.LINKING_ELEMENTS.getMessageDefinition(connectorName,
                                                                                              dataSetProperties.getTypeName(),
                                                                                              assetGUID,
                                                                                              connectionProperties.getTypeName(),
                                                                                              connectionGUID,
                                                                                              OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName));

            /*
             * Create an endpoint to carry the URL of the platform needed to connect to the metadata store.
             */
            EndpointProperties endpointProperties = new EndpointProperties();

            endpointProperties.setQualifiedName(productDefinition.getQualifiedName() + "_referenceDataSet_endpoint");
            endpointProperties.setDisplayName("Reference data set for " + productDefinition.getDisplayName());
            endpointProperties.setDescription("This endpoint represents the URL of the OMAG Server Platform that hosts the metadata access store.");
            endpointProperties.setVersionIdentifier(productDefinition.getVersionIdentifier());
            endpointProperties.setNetworkAddress(integrationContext.getMetadataAccessServerPlatformURLRoot());

            newElementOptions.setParentGUID(connectionGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName);

            EndpointClient endpointClient = integrationContext.getEndpointClient();

            String endpointGUID = endpointClient.createEndpoint(newElementOptions, null, endpointProperties, null);

            auditLog.logMessage(methodName,
                                ProductManagerAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                           endpointProperties.getTypeName(),
                                                                                                           endpointProperties.getDisplayName(),
                                                                                                           endpointGUID));

            auditLog.logMessage(methodName,
                                ProductManagerAuditCode.LINKING_ELEMENTS.getMessageDefinition(connectorName,
                                                                                              dataSetProperties.getTypeName(),
                                                                                              assetGUID,
                                                                                              endpointProperties.getTypeName(),
                                                                                              endpointGUID,
                                                                                              OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName));

        }


        return assetGUID;
    }


    /**
     * Return the unique identifier for an asset's connector type.
     *
     * @param connectorProvider name of the connector provider's class
     * @return unique identifier of the connector type
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getConnectorTypeGUID(ConnectorProvider connectorProvider) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {

        ConnectorTypeClient connectorTypeClient = integrationContext.getConnectorTypeClient();

        String connectorTypeGUID = null;

        if (connectorProvider != null)
        {
            /*
             * This is not strictly necessary - but is present to demonstrate how to search for and
             * create Connector Types
             */
            List<OpenMetadataRootElement> existingConnectorTypes = connectorTypeClient.getConnectorTypesByConnectorProvider(connectorProvider.getClass().getName(), null);

            if (existingConnectorTypes != null)
            {
                for (OpenMetadataRootElement connectorType : existingConnectorTypes)
                {
                    if (connectorType != null)
                    {
                        connectorTypeGUID = connectorType.getElementHeader().getGUID();
                        break;
                    }
                }
            }

            /*
             * This connector type is not currently defined.
             */
            if (connectorTypeGUID == null)
            {
                /*
                 * This is the simplified connector type defined by the Open Connector Framework (OCF).
                 * The connector developer fills out the ideal definition for this connector's connector type.
                 * Typically, connector types are defined in an archive and so the GUID is specified to support
                 * that process.
                 * In this method we are creating a new connector type through the API which means the repository
                 * that stores the entry gets to choose the GUID.
                 */
                ConnectorType ocfConnectorType = connectorProvider.getConnectorType();

                ConnectorTypeProperties connectorTypeProperties = new ConnectorTypeProperties();

                connectorTypeProperties.setQualifiedName(ocfConnectorType.getQualifiedName());
                connectorTypeProperties.setDisplayName(ocfConnectorType.getDisplayName());
                connectorTypeProperties.setDescription(ocfConnectorType.getDescription());
                connectorTypeProperties.setConnectorProviderClassName(connectorProvider.getClass().getName());

                /*
                 * Connector types are reusable, so they are not anchored to anything else.
                 */
                NewElementOptions newElementOptions = new NewElementOptions(connectorTypeClient.getMetadataSourceOptions());

                newElementOptions.setAnchorGUID(null);
                newElementOptions.setIsOwnAnchor(true);

                connectorTypeGUID = connectorTypeClient.createConnectorType(newElementOptions,
                                                                            null,
                                                                            connectorTypeProperties,
                                                                            null);
            }
        }

        return connectorTypeGUID;
    }


    /**
     * Set up the notification watchdog.
     *
     * @param productGUID unique identifier of product
     * @param productManagerGUID unique identifier of product manager
     * @param productSubscriptionDefinition details of the specific subscription type
     * @param notificationTypeGUID notification type that is to be monitored
     *
     * @return engine action guid
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String activateNotificationWatchdog(String                        productGUID,
                                                String                        productManagerGUID,
                                                ProductSubscriptionDefinition productSubscriptionDefinition,
                                                String                        notificationTypeGUID) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        List<NewActionTarget> actionTargets = new ArrayList<>();
        NewActionTarget notificationTarget = new NewActionTarget();

        notificationTarget.setActionTargetGUID(notificationTypeGUID);
        notificationTarget.setActionTargetName(ActionTarget.NOTIFICATION_TYPE.name);

        actionTargets.add(notificationTarget);

        GovernanceDefinitionClient governanceActionClient = integrationContext.getGovernanceDefinitionClient(OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName);

        OpenMetadataRootElement governanceActionType = governanceActionClient.getGovernanceDefinitionByGUID(productSubscriptionDefinition.getGovernanceActionTypeGUID(), null);

        if (governanceActionType.getProperties() instanceof GovernanceActionTypeProperties governanceActionTypeProperties)
        {
            return integrationContext.getOpenGovernanceClient().initiateGovernanceActionType(integrationContext.getMyUserId(),
                                                                                             governanceActionTypeProperties.getQualifiedName(),
                                                                                             Arrays.asList(integrationContext.getIntegrationConnectorGUID(), productManagerGUID),
                                                                                             Collections.singletonList(productGUID),
                                                                                             actionTargets,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null);
        }

        return null;
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


    /**
     * Return the map of qualifiedNames-to-guids for the nested collections that make up the
     * Structure of the product catalog.
     *
     * @return map
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getProductCatalogFolders() throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        Map<String, String> productFolderMap = new HashMap<>();

        /*
         * The top level folder needs to be created first so that its guid can be the
         * anchor scope for everything else.
         */
        String topLevelGUID = getProductFolder(ProductFolderDefinition.TOP_LEVEL, productFolderMap);

        productFolderMap.put(ProductFolderDefinition.TOP_LEVEL.getQualifiedName(), topLevelGUID);
        this.anchorScopeGUID = topLevelGUID;


        /*
         * All of the digital products use the same solution design, so it is attached at the top.
         */
        SolutionBlueprintClient solutionBlueprintClient = integrationContext.getSolutionBlueprintClient();
        solutionBlueprintClient.linkSolutionDesign(topLevelGUID,
                                                   solutionBlueprintGUID,
                                                   solutionBlueprintClient.getMetadataSourceOptions(),
                                                   null);

        /*
         * This root collection gathers all of the product catalogs together.  It is defined in the core content pack.
         */
        final String rootCollectionGUID = "dcec6ddb-317e-4c64-907e-be508ceba6d9";
        CollectionClient collectionClient = integrationContext.getCollectionClient();

        collectionClient.addToCollection(rootCollectionGUID, topLevelGUID, collectionClient.getMetadataSourceOptions(), null);

        /*
         * Link the governance definitions to the product catalog ...
         */
        ClassificationManagerClient classificationManagerClient = integrationContext.getClassificationManagerClient();

        classificationManagerClient.addMoreInformationToElement(topLevelGUID,
                                                                governanceDefinitions.get(ProductGovernanceDefinition.DIGITAL_PRODUCT_CATALOG.getQualifiedName()),
                                                                classificationManagerClient.getMetadataSourceOptions(),
                                                                null);

        /*
         * Now set up all of the other folders.
         */
        for (ProductFolderDefinition productFolderDefinition : ProductFolderDefinition.values())
        {
            if (productFolderDefinition != ProductFolderDefinition.TOP_LEVEL)
            {
                String productFolderGUID = this.getProductFolder(productFolderDefinition, productFolderMap);

                productFolderMap.put(productFolderDefinition.getQualifiedName(), productFolderGUID);
            }
        }

        return productFolderMap;
    }


    /**
     * Return the guid of a product folder.  If it exists, it is retrieved from the metadata repository.
     * Otherwise, a new collection is created of the appropriate type and with the right classification
     * attached.  If the product folder has a parent then it is linked to the parent using the CollectionMembership
     * relationship.
     *
     * @param productFolderDefinition description of the folder to create
     * @return guid of the folder
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getProductFolder(ProductFolderDefinition productFolderDefinition,
                                    Map<String, String>     currentProductFolders) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName = "getProductFolder";

        CollectionClient collectionClient = integrationContext.getCollectionClient();

        /*
         * If the product folder is already present then return its GUID
         */
        List<OpenMetadataRootElement> collections = collectionClient.getCollectionsByName(productFolderDefinition.getQualifiedName(), null);

        if (collections != null)
        {
            for (OpenMetadataRootElement collection : collections)
            {
                if (collection != null)
                {
                    auditLog.logMessage(methodName,
                                        ProductManagerAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                                      productFolderDefinition.getTypeName(),
                                                                                                                      productFolderDefinition.getDisplayName(),
                                                                                                                      collection.getElementHeader().getGUID()));

                    return collection.getElementHeader().getGUID();
                }
            }
        }

        /*
         * This is the first time...
         */
        CollectionProperties collectionProperties = new CollectionProperties();

        collectionProperties.setTypeName(productFolderDefinition.getTypeName());
        collectionProperties.setQualifiedName(productFolderDefinition.getQualifiedName());
        collectionProperties.setDisplayName(productFolderDefinition.getDisplayName());
        collectionProperties.setDescription(productFolderDefinition.getDescription());
        collectionProperties.setCategory(productFolderDefinition.getCategory());

        Map<String, ClassificationProperties> initialClassifications = null;

        if (productFolderDefinition.getClassificationName() != null)
        {
            initialClassifications = new HashMap<>();

            initialClassifications.put(productFolderDefinition.getClassificationName(), null);
        }

        NewElementOptions newElementOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());
        newElementOptions.setAnchorScopeGUID(this.anchorScopeGUID);

        if (productFolderDefinition.getParent() != null)
        {
            String parentGUID = currentProductFolders.get(productFolderDefinition.getParent().getQualifiedName());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(parentGUID);
            newElementOptions.setParentGUID(parentGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
            newElementOptions.setParentAtEnd1(true);
        }
        else
        {
            newElementOptions.setIsOwnAnchor(true);
        }

        String collectionGUID = collectionClient.createCollection(newElementOptions,
                                                                  initialClassifications,
                                                                  collectionProperties,
                                                                  null);
        auditLog.logMessage(methodName,
                            ProductManagerAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                       productFolderDefinition.getTypeName(),
                                                                                                       productFolderDefinition.getDisplayName(),
                                                                                                       collectionGUID));

        return collectionGUID;
    }


    /**
     * Add all of the defined terms to the glossary at the requested folder.
     *
     * @return map of glossary term qualified names to GUIDs
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getGlossaryTerms() throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        Map<String, String> glossaryTermMap = new HashMap<>();

        for (ProductGlossaryTermDefinition glossaryTermDefinition : ProductGlossaryTermDefinition.values())
        {
            String glossaryTermGUID = this.getGlossaryTerm(glossaryTermDefinition);

            glossaryTermMap.put(glossaryTermDefinition.getQualifiedName(), glossaryTermGUID);
        }

        return glossaryTermMap;
    }


    /**
     * Return the unique identifier of the glossary term either by retrieving an existing glossary term or,
     * when that fails, creating a new one.
     *
     * @param glossaryTermDefinition description of the glossary term
     * @return unique identifier of the glossary term
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getGlossaryTerm(ProductGlossaryTermDefinition glossaryTermDefinition) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "getGlossaryTerm";

        GlossaryTermClient glossaryTermClient = integrationContext.getGlossaryTermClient();

        /*
         * If the glossary term is already present then return its GUID
         */
        List<OpenMetadataRootElement> terms = glossaryTermClient.getGlossaryTermsByName(glossaryTermDefinition.getQualifiedName(), null);

        if (terms != null)
        {
            for (OpenMetadataRootElement term : terms)
            {
                if (term != null)
                {
                    auditLog.logMessage(methodName,
                                        ProductManagerAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                                      OpenMetadataType.GLOSSARY_TERM.typeName,
                                                                                                                      glossaryTermDefinition.getDisplayName(),
                                                                                                                      term.getElementHeader().getGUID()));

                    return term.getElementHeader().getGUID();
                }
            }
        }

        /*
         * This is the first time...
         */
        GlossaryTermProperties glossaryTermProperties = new GlossaryTermProperties();

        glossaryTermProperties.setQualifiedName(glossaryTermDefinition.getQualifiedName());
        glossaryTermProperties.setDisplayName(glossaryTermDefinition.getDisplayName());
        glossaryTermProperties.setDescription(glossaryTermDefinition.getDescription());
        glossaryTermProperties.setSummary(glossaryTermDefinition.getSummary());
        glossaryTermProperties.setAbbreviation(glossaryTermDefinition.getAbbreviation());

        NewElementOptions newElementOptions = new NewElementOptions(glossaryTermClient.getMetadataSourceOptions());
        newElementOptions.setAnchorScopeGUID(this.anchorScopeGUID);

        if (glossaryTermDefinition.getFolder() != null)
        {
            String parentGUID = productFolders.get(glossaryTermDefinition.getFolder().getQualifiedName());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(parentGUID);
            newElementOptions.setParentGUID(parentGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
            newElementOptions.setParentAtEnd1(true);
        }
        else
        {
            newElementOptions.setIsOwnAnchor(true);
        }

        String glossaryTermGUID = glossaryTermClient.createGlossaryTerm(newElementOptions,
                                                                        null,
                                                                        glossaryTermProperties,
                                                                        null);

        auditLog.logMessage(methodName,
                            ProductManagerAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                       OpenMetadataType.GLOSSARY_TERM.typeName,
                                                                                                       glossaryTermDefinition.getDisplayName(),
                                                                                                       glossaryTermGUID));

        return glossaryTermGUID;
    }


    /**
     * Add all of the defined communities.
     *
     * @return map of qualified names to GUIDs
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getCommunities() throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        Map<String, String> communityMap = new HashMap<>();

        for (ProductCommunityDefinition productCommunityDefinition : ProductCommunityDefinition.values())
        {
            String communityGUID = this.getCommunity(productCommunityDefinition);

            communityMap.put(productCommunityDefinition.getQualifiedName(), communityGUID);
        }

        return communityMap;
    }


    /**
     * Return the unique identifier of the community either by retrieving an existing community or,
     * when that fails, creating a new one.
     *
     * @param productCommunityDefinition description of the community
     * @return unique identifier of the community
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getCommunity(ProductCommunityDefinition productCommunityDefinition) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getCommunity";

        CommunityClient communityClient = integrationContext.getCommunityClient();

        /*
         * If the community is already present then return its GUID
         */
        List<OpenMetadataRootElement> existingCommunities = communityClient.getCommunitiesByName(productCommunityDefinition.getQualifiedName(), null);

        if (existingCommunities != null)
        {
            for (OpenMetadataRootElement existingCommunity : existingCommunities)
            {
                if (existingCommunity != null)
                {
                    auditLog.logMessage(methodName,
                                        ProductManagerAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                                      OpenMetadataType.COMMUNITY.typeName,
                                                                                                                      productCommunityDefinition.getDisplayName(),
                                                                                                                      existingCommunity.getElementHeader().getGUID()));

                    return existingCommunity.getElementHeader().getGUID();
                }
            }
        }

        /*
         * This is the first time...
         */
        CommunityProperties communityProperties = new CommunityProperties();

        communityProperties.setQualifiedName(productCommunityDefinition.getQualifiedName());
        communityProperties.setDisplayName(productCommunityDefinition.getDisplayName());
        communityProperties.setDescription(productCommunityDefinition.getDescription());

        NewElementOptions newElementOptions = new NewElementOptions(communityClient.getMetadataSourceOptions());
        newElementOptions.setAnchorScopeGUID(this.anchorScopeGUID);
        newElementOptions.setIsOwnAnchor(true);

        String communityGUID = communityClient.createCommunity(newElementOptions,
                                                               null,
                                                               communityProperties,
                                                               null);

        auditLog.logMessage(methodName,
                            ProductManagerAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                       OpenMetadataType.COMMUNITY.typeName,
                                                                                                       productCommunityDefinition.getDisplayName(),
                                                                                                       communityGUID));

        return communityGUID;
    }


    /**
     * Add all of the defined communities.
     *
     * @return map of qualified names to GUIDs
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getCommunityNoteLogs() throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        Map<String, String> noteLogMap = new HashMap<>();

        for (ProductCommunityDefinition productCommunityDefinition : ProductCommunityDefinition.values())
        {
            String communityNoteLogGUID = this.getCommunityNoteLog(productCommunityDefinition);

            noteLogMap.put(productCommunityDefinition.getQualifiedName(), communityNoteLogGUID);
        }

        return noteLogMap;
    }


    /**
     * Return the unique identifier of the community either by retrieving an existing community or,
     * when that fails, creating a new one.
     *
     * @param productCommunityDefinition description of the community
     * @return unique identifier of the community
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getCommunityNoteLog(ProductCommunityDefinition productCommunityDefinition) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getCommunityNoteLog";

        CommunityClient communityClient = integrationContext.getCommunityClient();
        String          communityGUID   = communities.get(productCommunityDefinition.getQualifiedName());

        /*
         * If the community is already present then return its GUID
         */
        OpenMetadataRootElement community = communityClient.getCommunityByGUID(communityGUID, null);

        if ((community != null) && (community.getNoteLogs() != null))
        {
            for (RelatedMetadataElementSummary relatedNoteLog : community.getNoteLogs())
            {
                if (relatedNoteLog != null)
                {
                    auditLog.logMessage(methodName,
                                        ProductManagerAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                                   OpenMetadataType.NOTE_LOG.typeName,
                                                                                                                   "Notifications for " + productCommunityDefinition.getDisplayName(),
                                                                                                                   relatedNoteLog.getRelatedElement().getElementHeader().getGUID()));

                    return relatedNoteLog.getRelatedElement().getElementHeader().getGUID();
                }
            }
        }

        NoteLogClient noteLogClient = integrationContext.getNoteLogClient();

        /*
         * This is the first time...
         */
        NoteLogProperties noteLogProperties = new NoteLogProperties();

        noteLogProperties.setQualifiedName(productCommunityDefinition.getQualifiedName() + "_noteLog");
        noteLogProperties.setDisplayName("Notifications for " + productCommunityDefinition.getDisplayName());
        noteLogProperties.setDescription("Notifications received for products associated with this community.");

        NewElementOptions newElementOptions = new NewElementOptions(noteLogClient.getMetadataSourceOptions());
        newElementOptions.setAnchorScopeGUID(this.anchorScopeGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorGUID(communityGUID);

        String noteLogGUID = noteLogClient.createNoteLog(communityGUID,
                                                         noteLogClient.getMetadataSourceOptions(),
                                                         null,
                                                         noteLogProperties);

        auditLog.logMessage(methodName,
                            ProductManagerAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                       OpenMetadataType.NOTE_LOG.typeName,
                                                                                                       noteLogProperties.getDisplayName(),
                                                                                                       noteLogGUID));
        return noteLogGUID;
    }


    /**
     * Set up the map of data field qualified names to guids.
     *
     * @return map of data field qualified names to guids
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getDataFields() throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        Map<String, String> dataFieldMap = new HashMap<>();

        for (ProductDataFieldDefinition dataFieldDefinition : ProductDataFieldDefinition.values())
        {
            String dataFieldGUID = this.getDataField(dataFieldDefinition);

            dataFieldMap.put(dataFieldDefinition.getQualifiedName(), dataFieldGUID);
        }

        return dataFieldMap;
    }


    /**
     * Return the unique identifier of the data field either by retrieving it from the metadata
     * repository or, if that fails, creating a new one.
     *
     * @param dataFieldDefinition description of the data field
     * @return unique identifier (guid)
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getDataField(ProductDataFieldDefinition dataFieldDefinition) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getDataField";

        DataFieldClient dataFieldClient = integrationContext.getDataFieldClient();

        /*
         * If the data field is already present then return its GUID
         */
        List<OpenMetadataRootElement> existingDataFields = dataFieldClient.getDataFieldsByName(dataFieldDefinition.getQualifiedName(), null);

        if (existingDataFields != null)
        {
            for (OpenMetadataRootElement dataField : existingDataFields)
            {
                if (dataField != null)
                {
                    auditLog.logMessage(methodName,
                                        ProductManagerAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                                      OpenMetadataType.DATA_FIELD.typeName,
                                                                                                                      dataFieldDefinition.getDisplayName(),
                                                                                                                      dataField.getElementHeader().getGUID()));

                    return dataField.getElementHeader().getGUID();
                }
            }
        }

        /*
         * This is the first time...
         */
        DataFieldProperties dataFieldProperties = new DataFieldProperties();

        dataFieldProperties.setQualifiedName(dataFieldDefinition.getQualifiedName());
        dataFieldProperties.setDisplayName(dataFieldDefinition.getDisplayName());
        dataFieldProperties.setDescription(dataFieldDefinition.getDescription());
        dataFieldProperties.setDataType(dataFieldDefinition.getDataType().getName());
        dataFieldProperties.setDefaultValue(dataFieldDefinition.getDefaultValue());
        dataFieldProperties.setNamePatterns(List.of(dataFieldDefinition.getNamePattern()));

        NewElementOptions newElementOptions = new NewElementOptions(dataFieldClient.getMetadataSourceOptions());

        String parentGUID = productFolders.get(ProductFolderDefinition.DATA_DICTIONARY.getQualifiedName());

        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorScopeGUID(this.anchorScopeGUID);
        newElementOptions.setAnchorGUID(parentGUID);
        newElementOptions.setParentGUID(parentGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
        newElementOptions.setParentAtEnd1(true);


        Map<String, ClassificationProperties> initialClassifications = null;

        if (dataFieldDefinition.isIdentifier())
        {
            initialClassifications = new HashMap<>();

            initialClassifications.put(OpenMetadataType.OBJECT_IDENTIFIER_CLASSIFICATION.typeName, null);
        }

        String dataFieldGUID = dataFieldClient.createDataField(newElementOptions,
                                               initialClassifications,
                                               dataFieldProperties,
                                               null);

        auditLog.logMessage(methodName,
                            ProductManagerAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                       OpenMetadataType.DATA_FIELD.typeName,
                                                                                                       dataFieldDefinition.getDisplayName(),
                                                                                                       dataFieldGUID));

        return dataFieldGUID;
    }


    /**
     * Add all of the defined terms to the glossary at the requested folder.
     *
     * @return map of governance definition qualified names to GUIDs
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getGovernanceDefinitions() throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        Map<String, String> governanceDefinitionMap = new HashMap<>();

        for (ProductGovernanceDefinition governanceDefinition : ProductGovernanceDefinition.values())
        {
            String governanceDefinitionGUID = this.getGovernanceDefinition(governanceDefinition);

            governanceDefinitionMap.put(governanceDefinition.getQualifiedName(), governanceDefinitionGUID);
        }

        /*
         * Link the governance driver and policies together.  Typically, these definitions would be created
         * by the governance team.
         */
        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();

        PeerDefinitionProperties peerDefinitionProperties = new PeerDefinitionProperties();

        peerDefinitionProperties.setLabel("enables");
        peerDefinitionProperties.setDescription("The digital product catalog(s) create a platform for managing data sharing opportunities.");

        governanceDefinitionClient.linkPeerDefinitions(governanceDefinitionMap.get(ProductGovernanceDefinition.DIGITAL_PRODUCT_CATALOG.getQualifiedName()),
                                                       governanceDefinitionMap.get(ProductGovernanceDefinition.ENABLE_DATA_SHARING.getQualifiedName()),
                                                       OpenMetadataType.GOVERNANCE_POLICY_LINK_RELATIONSHIP.typeName,
                                                       governanceDefinitionClient.getMetadataSourceOptions(),
                                                       peerDefinitionProperties);

        SupportingDefinitionProperties supportingDefinitionProperties = new SupportingDefinitionProperties();

        supportingDefinitionProperties.setRationale("Data sharing helps to ensure that key data generated in one part of the company is available for other teams.");

        governanceDefinitionClient.attachSupportingDefinition(governanceDefinitionMap.get(ProductGovernanceDefinition.DATA_DRIVEN.getQualifiedName()),
                                                              governanceDefinitionMap.get(ProductGovernanceDefinition.ENABLE_DATA_SHARING.getQualifiedName()),
                                                              OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName,
                                                              governanceDefinitionClient.getMetadataSourceOptions(),
                                                              supportingDefinitionProperties);

        supportingDefinitionProperties.setRationale("Digital product catalogs create a platform for exchange or requirements, ideas, skills and, of course, data.  They demonstrate the focus that senior management is placing on data sharing.");

        governanceDefinitionClient.attachSupportingDefinition(governanceDefinitionMap.get(ProductGovernanceDefinition.DATA_DRIVEN.getQualifiedName()),
                                                              governanceDefinitionMap.get(ProductGovernanceDefinition.DIGITAL_PRODUCT_CATALOG.getQualifiedName()),
                                                              OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName,
                                                              governanceDefinitionClient.getMetadataSourceOptions(),
                                                              supportingDefinitionProperties);

        /*
         * Return the map of governance definitions to allow the product definitions to show which governance
         * definitions are relevant to their governance.
         */
        return governanceDefinitionMap;
    }


    /**
     * Return the unique identifier of the governance definition either by retrieving an existing definition or,
     * when that fails, creating a new one.
     *
     * @param governanceDefinition description of the governance definition
     * @return unique identifier of the glossary term
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getGovernanceDefinition(ProductGovernanceDefinition governanceDefinition) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "getGovernanceDefinition";

        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();

        /*
         * If the GovernanceDefinition is already present then return its GUID
         */
        List<OpenMetadataRootElement> existingGovernanceDefinitions = governanceDefinitionClient.getGovernanceDefinitionsByName(governanceDefinition.getQualifiedName(), null);

        if (existingGovernanceDefinitions != null)
        {
            for (OpenMetadataRootElement existingGovernanceDefinition : existingGovernanceDefinitions)
            {
                if (existingGovernanceDefinition != null)
                {
                    auditLog.logMessage(methodName,
                                        ProductManagerAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                                      governanceDefinition.getType(),
                                                                                                                      governanceDefinition.getDisplayName(),
                                                                                                                      existingGovernanceDefinition.getElementHeader().getGUID()));
                    return existingGovernanceDefinition.getElementHeader().getGUID();
                }
            }
        }

        /*
         * This is the first time...
         */
        GovernanceDefinitionProperties governanceDefinitionProperties = new GovernanceDefinitionProperties();

        governanceDefinitionProperties.setTypeName(governanceDefinition.getType());
        governanceDefinitionProperties.setQualifiedName(governanceDefinition.getQualifiedName());
        governanceDefinitionProperties.setDisplayName(governanceDefinition.getDisplayName());
        governanceDefinitionProperties.setDescription(governanceDefinition.getDescription());
        governanceDefinitionProperties.setSummary(governanceDefinition.getSummary());
        governanceDefinitionProperties.setIdentifier(governanceDefinition.getIdentifier());
        governanceDefinitionProperties.setDomainIdentifier(governanceDefinition.getDomain());
        governanceDefinitionProperties.setScope(governanceDefinition.getImportance());
        governanceDefinitionProperties.setUsage(governanceDefinition.getImportance());
        governanceDefinitionProperties.setImportance(governanceDefinition.getImportance());
        governanceDefinitionProperties.setImplications(governanceDefinition.getImplications());
        governanceDefinitionProperties.setOutcomes(governanceDefinition.getOutcomes());
        governanceDefinitionProperties.setResults(governanceDefinition.getResults());

        Map<String, Object> extendedProperties = new HashMap<>();
        if (governanceDefinition.getObligations() != null)
        {
            extendedProperties.put(OpenMetadataProperty.OBLIGATIONS.name, governanceDefinition.getObligations());
        }
        if (governanceDefinition.getEntitlements() != null)
        {
            extendedProperties.put(OpenMetadataProperty.ENTITLEMENTS.name, governanceDefinition.getEntitlements());
        }
        if (governanceDefinition.getRestrictions() != null)
        {
            extendedProperties.put(OpenMetadataProperty.RESTRICTIONS.name, governanceDefinition.getRestrictions());
        }

        if (! extendedProperties.isEmpty())
        {
            governanceDefinitionProperties.setExtendedProperties(extendedProperties);
        }

        /*
         * Each governance definition is created as independent elements, and they are not linked together (yet)
         */
        NewElementOptions newElementOptions = new NewElementOptions(governanceDefinitionClient.getMetadataSourceOptions());
        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
        newElementOptions.setIsOwnAnchor(true);

        String governanceDefinitionGUID =  governanceDefinitionClient.createGovernanceDefinition(newElementOptions,
                                                                     null,
                                                                     governanceDefinitionProperties,
                                                                     null);

        auditLog.logMessage(methodName,
                            ProductManagerAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                       governanceDefinition.getType(),
                                                                                                       governanceDefinition.getDisplayName(),
                                                                                                       governanceDefinitionGUID));

        return governanceDefinitionGUID;
    }


    /**
     * Locates/creates the solution blueprint for the open metadata digital products.
     *
     * @return guid of the blueprint or null if no blueprint can be created
     */
    private String getSolutionBlueprint() throws InvalidParameterException,
                                                 PropertyServerException,
                                                 UserNotAuthorizedException
    {
        final String methodName = "getSolutionBlueprint";

        String blueprintQualifiedName = ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER.getQualifiedName();

        SolutionBlueprintClient solutionBlueprintClient = integrationContext.getSolutionBlueprintClient();

        /*
         * If the solution blueprint is already present then return its GUID,
         */
        List<OpenMetadataRootElement> solutionBlueprints = solutionBlueprintClient.getSolutionBlueprintsByName(blueprintQualifiedName, null);

        if (solutionBlueprints != null)
        {
            for (OpenMetadataRootElement solutionBlueprint : solutionBlueprints)
            {
                if (solutionBlueprint != null)
                {
                    auditLog.logMessage(methodName,
                                        ProductManagerAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                                      OpenMetadataType.SOLUTION_BLUEPRINT.typeName,
                                                                                                                      ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER.getDisplayName(),
                                                                                                                      solutionBlueprint.getElementHeader().getGUID()));
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
        newElementOptions.setAnchorScopeGUID(this.anchorScopeGUID);
        newElementOptions.setIsOwnAnchor(true);

        String blueprintGUID = solutionBlueprintClient.createSolutionBlueprint(newElementOptions,
                                                                               null,
                                                                               solutionBlueprintProperties,
                                                                               null);

        auditLog.logMessage(methodName,
                            ProductManagerAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                       OpenMetadataType.SOLUTION_BLUEPRINT.typeName,
                                                                                                       ProductSolutionBlueprint.AUTO_PRODUCT_MANAGER.getDisplayName(),
                                                                                                       blueprintGUID));

        newElementOptions.setAnchorGUID(blueprintGUID);
        newElementOptions.setParentGUID(blueprintGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
        newElementOptions.setParentAtEnd1(true);

        SolutionComponentClient solutionComponentClient = integrationContext.getSolutionComponentClient();
        Map<String, String>     qualifiedNameToGUIDMap  = new HashMap<>();

        /*
         * Add the solution components to the blueprints.  A map of qualifiedNames to GUIDs is maintained to
         * enable the components to be linked together - and to their solution roles.
         */
        for (ProductSolutionComponent solutionComponentDefinition : ProductSolutionComponent.values())
        {
            String componentQualifiedName = solutionComponentDefinition.getQualifiedName();
            String componentGUID          = null;

            List<OpenMetadataRootElement> solutionComponents = solutionComponentClient.getSolutionComponentsByName(componentQualifiedName, null);

            if (solutionComponents != null)
            {
                for (OpenMetadataRootElement solutionComponent : solutionComponents)
                {
                    if (solutionComponent != null)
                    {
                        /*
                         * Component already exists
                         */
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

                auditLog.logMessage(methodName,
                                    ProductManagerAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                               OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                                                               solutionComponentDefinition.getDisplayName(),
                                                                                                               componentGUID));
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
                                                            qualifiedNameToGUIDMap.get(solutionComponentWire.getComponent2().getQualifiedName()),
                                                            solutionComponentClient.getMetadataSourceOptions(),
                                                            solutionLinkingWireProperties);

            auditLog.logMessage(methodName,
                                ProductManagerAuditCode.LINKING_ELEMENTS.getMessageDefinition(connectorName,
                                                                                              OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                                              solutionComponentWire.getComponent1().getQualifiedName(),
                                                                                              OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                                              solutionComponentWire.getComponent2().getQualifiedName(),
                                                                                              OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName));
        }

        /*
         * Connect Actor Roles to Solution Components
         */
        for (SolutionComponentActor solutionComponentActor : SolutionComponentActor.values())
        {
            SolutionComponentActorProperties solutionComponentActorProperties = new SolutionComponentActorProperties();

            solutionComponentActorProperties.setRole(solutionComponentActor.getRole());
            solutionComponentActorProperties.setDescription(solutionComponentActor.getDescription());

            solutionComponentClient.linkSolutionComponentActor(productRoles.get(solutionComponentActor.getSolutionRole().getQualifiedName()),
                                                               qualifiedNameToGUIDMap.get(solutionComponentActor.getSolutionComponent().getQualifiedName()),
                                                               solutionComponentClient.getMetadataSourceOptions(),
                                                               solutionComponentActorProperties);

            auditLog.logMessage(methodName,
                                ProductManagerAuditCode.LINKING_ELEMENTS.getMessageDefinition(connectorName,
                                                                                              solutionComponentActor.getSolutionRole().getTypeName(),
                                                                                              solutionComponentActor.getSolutionRole().getQualifiedName(),
                                                                                              OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                                              solutionComponentActor.getSolutionComponent().getQualifiedName(),
                                                                                              OpenMetadataType.SOLUTION_COMPONENT_ACTOR_RELATIONSHIP.typeName));
        }

        return blueprintGUID;
    }


    /**
     * Add all of the defined roles for this solution.
     *
     * @return map of qualified names to GUIDs
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getProductRoles() throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        Map<String, String> roleMap = new HashMap<>();

        for (ProductRoleDefinition productRoleDefinition : ProductRoleDefinition.values())
        {
            String roleGUID = this.getProductRole(productRoleDefinition);

            roleMap.put(productRoleDefinition.getQualifiedName(), roleGUID);
        }

        return roleMap;
    }


    /**
     * Set up an individual product role.
     *
     * @param productRoleDefinition role definition
     * @return guid of the role
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getProductRole(ProductRoleDefinition productRoleDefinition) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        final String methodName = "getProductRole";

        ActorRoleClient actorRoleClient = integrationContext.getActorRoleClient();
        NewElementOptions newElementOptions = new NewElementOptions(actorRoleClient.getMetadataSourceOptions());
        newElementOptions.setIsOwnAnchor(true);

        String roleQualifiedName = productRoleDefinition.getQualifiedName();
        String roleGUID          = null;

        List<OpenMetadataRootElement> solutionRoles = actorRoleClient.getActorRolesByName(roleQualifiedName, null);

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

            ActorRoleProperties actorRoleProperties = new SolutionActorRoleProperties();

            actorRoleProperties.setTypeName(productRoleDefinition.getTypeName());
            actorRoleProperties.setQualifiedName(roleQualifiedName);
            actorRoleProperties.setDisplayName(productRoleDefinition.getDisplayName());
            actorRoleProperties.setDescription(productRoleDefinition.getDescription());
            actorRoleProperties.setIdentifier(productRoleDefinition.getIdentifier());

            roleGUID = actorRoleClient.createActorRole(newElementOptions,
                                                       null,
                                                       actorRoleProperties,
                                                       null);

            auditLog.logMessage(methodName,
                                ProductManagerAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                           productRoleDefinition.getTypeName(),
                                                                                                           productRoleDefinition.getDisplayName(),
                                                                                                           roleGUID));
        }
        else
        {
            auditLog.logMessage(methodName,
                                ProductManagerAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                           productRoleDefinition.getTypeName(),
                                                                                                           productRoleDefinition.getDisplayName(),
                                                                                                           roleGUID));
        }

        return roleGUID;
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


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        auditLog.logMessage(methodName, ProductManagerAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName,
                                                                                                        integrationContext.getMetadataAccessServer(),
                                                                                                        integrationContext.getMetadataAccessServerPlatformURLRoot()));

        super.disconnect();
    }
}
