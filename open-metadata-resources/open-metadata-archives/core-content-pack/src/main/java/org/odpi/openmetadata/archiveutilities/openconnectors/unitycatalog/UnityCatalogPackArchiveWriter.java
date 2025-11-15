/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors.unitycatalog;

import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogTemplateType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.provision.ProvisionUnityCatalogRequestParameter;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogInsideCatalogSyncProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogServerSyncProvider;
import org.odpi.openmetadata.archiveutilities.openconnectors.*;
import org.odpi.openmetadata.archiveutilities.openconnectors.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;

import java.util.*;

/**
 * UnityCatalogPackArchiveWriter creates an open metadata archive that includes the connector type
 * information for all Unity Catalog connectors supplied by the egeria project.
 */
public class UnityCatalogPackArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public UnityCatalogPackArchiveWriter()
    {
        super(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK.getArchiveFileName(),
              new OpenMetadataArchive[]{new CorePackArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Implemented by subclass to add the content.
     */
    @Override
    public void getArchiveContent()
    {
        /*
         * Add valid metadata values for deployedImplementationType.  The GUIDs are saved in a look-up map
         * to make it easy to link other elements to these valid values later.
         */
        for (UnityCatalogDeployedImplementationType deployedImplementationType : UnityCatalogDeployedImplementationType.values())
        {
            this.addDeployedImplementationType(deployedImplementationType.getGUID(),
                                               deployedImplementationType.getDeployedImplementationType(),
                                               deployedImplementationType.getAssociatedTypeName(),
                                               deployedImplementationType.getQualifiedName(),
                                               deployedImplementationType.getDescription(),
                                               deployedImplementationType.getWikiLink(),
                                               deployedImplementationType.getIsATypeOf());
        }

        /*
         * Integration Connector Types may need to link to deployedImplementationType valid value element.
         * This information is in the connector provider.
         */
        archiveHelper.addConnectorType(new OSSUnityCatalogResourceProvider());
        archiveHelper.addConnectorType(new OSSUnityCatalogServerSyncProvider());
        archiveHelper.addConnectorType(new OSSUnityCatalogInsideCatalogSyncProvider());

        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK);
        this.addOSSUCCatalogCatalogTemplate();
        this.addUCSchemaCatalogTemplate();
        this.addUCVolumeCatalogTemplate();
        this.addUCTableCatalogTemplate();
        this.addUCFunctionCatalogTemplate();
        this.addUCRegisteredModelCatalogTemplate();
        this.addUCModelVersionCatalogTemplate();

        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK, IntegrationGroupDefinition.UNITY_CATALOG);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK);

        /*
         * Create Processes for working with Unity Catalog
         */
        this.createAndSurveyServerGovernanceActionProcess("UnityCatalogServer",
                                                          UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.CREATE_UC_SERVER,
                                                          SoftwareServerTemplateDefinition.UNITY_CATALOG_SERVER_TEMPLATE,
                                                          RequestTypeDefinition.SURVEY_UC_SERVER,
                                                          UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName());
        this.createAndCatalogServerGovernanceActionProcess("UnityCatalogServer",
                                                           UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getDeployedImplementationType(),
                                                           RequestTypeDefinition.CREATE_UC_SERVER,
                                                           SoftwareServerTemplateDefinition.UNITY_CATALOG_SERVER_TEMPLATE,
                                                           RequestTypeDefinition.CATALOG_UC_SERVER,
                                                           UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName());
        this.deleteAsCatalogTargetGovernanceActionProcess("UnityCatalogServer",
                                                          UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
                                                          UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.DELETE_UC_SERVER,
                                                          UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getQualifiedName());

        this.createAndSurveyServerGovernanceActionProcess("DatabricksUnityCatalogServer",
                                                          UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.CREATE_DB_UC_SERVER,
                                                          SoftwareServerTemplateDefinition.DATABRICKS_UC_SERVER_TEMPLATE,
                                                          RequestTypeDefinition.SURVEY_UC_SERVER,
                                                          UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getQualifiedName());
        this.createAndCatalogServerGovernanceActionProcess("DatabricksUnityCatalogServer",
                                                           UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getDeployedImplementationType(),
                                                           RequestTypeDefinition.CREATE_DB_UC_SERVER,
                                                           SoftwareServerTemplateDefinition.DATABRICKS_UC_SERVER_TEMPLATE,
                                                           RequestTypeDefinition.CATALOG_UC_SERVER,
                                                           UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getQualifiedName());
        this.deleteAsCatalogTargetGovernanceActionProcess("DatabricksUnityCatalogServer",
                                                          UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
                                                          UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.DELETE_UC_SERVER,
                                                          UnityCatalogDeployedImplementationType.DB_UNITY_CATALOG_SERVER.getQualifiedName());

        this.createProvisionUnityCatalogGovernanceActionProcess("UnityCatalogCatalog",
                                                                UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType(),
                                                                RequestTypeDefinition.PROVISION_UC,
                                                                UnityCatalogTemplateType.OSS_UC_CATALOG_TEMPLATE,
                                                                UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getQualifiedName());
        this.createProvisionUnityCatalogGovernanceActionProcess("UnityCatalogSchema",
                                                                UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType(),
                                                                RequestTypeDefinition.PROVISION_UC,
                                                                UnityCatalogTemplateType.OSS_UC_SCHEMA_TEMPLATE,
                                                                UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getQualifiedName());
        this.createProvisionUnityCatalogGovernanceActionProcess("UnityCatalogVolume",
                                                                UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getDeployedImplementationType(),
                                                                RequestTypeDefinition.PROVISION_UC,
                                                                UnityCatalogTemplateType.OSS_UC_VOLUME_TEMPLATE,
                                                                UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getQualifiedName());
        this.createProvisionUnityCatalogGovernanceActionProcess("UnityCatalogTable",
                                                                UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType(),
                                                                RequestTypeDefinition.PROVISION_UC,
                                                                UnityCatalogTemplateType.OSS_UC_TABLE_TEMPLATE,
                                                                UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getQualifiedName());
        this.createProvisionUnityCatalogGovernanceActionProcess("UnityCatalogFunction",
                                                                UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getDeployedImplementationType(),
                                                                RequestTypeDefinition.PROVISION_UC,
                                                                UnityCatalogTemplateType.OSS_UC_FUNCTION_TEMPLATE,
                                                                UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getQualifiedName());

        /*
         * Saving the GUIDs means tha the guids in the archive are stable between runs of the archive writer.
         */
        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();
    }

    /**
     * Create a two-step governance action process that creates a metadata element for a particular type of server
     * and then adds it as a catalog target for an appropriate integration connector.
     *
     * @param technologyName name of technology
     * @param technologyType value for deployed implementation type
     * @param provisionRequestType request type used to create the server's metadata element
     * @param createTemplate template to use in the creation step
     * @param supportedElementQualifiedName qualified name of the element that this should be listed as a resource
     */
    protected void createProvisionUnityCatalogGovernanceActionProcess(String                     technologyName,
                                                                      String                     technologyType,
                                                                      RequestTypeDefinition      provisionRequestType,
                                                                      TemplateDefinition         createTemplate,
                                                                      String                     supportedElementQualifiedName)
    {
        String description = "Create a " + technologyType + " element in the correct metadata collection so that it is provisioned into unity catalog.";

        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                      "Provision:" + technologyName + ":GovernanceActionProcess",
                                                                      "Provision " + technologyType,
                                                                      null,
                                                                      description,
                                                                      null,
                                                                      0,
                                                                      null,
                                                                      null,
                                                                      null);


        List<RequestParameterType> supportedRequestParameters = null;

        if (createTemplate != null)
        {
            supportedRequestParameters = this.getRequestTypeDefinition(createTemplate.getPlaceholders());
        }

        String step1GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        "Provision:" + technologyName + ":Step1",
                                                                        "Create the new element",
                                                                        "Create a " + technologyType + " element in the correct metadata collection so that it is provisioned into unity catalog.",
                                                                        0,
                                                                        supportedRequestParameters,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        0,
                                                                        true,
                                                                        null,
                                                                        null,
                                                                        null);

        if (step1GUID != null)
        {
            super.addStepExecutor(step1GUID, provisionRequestType, provisionRequestType.getGovernanceEngine());

            Map<String, String> requestParameters = new HashMap<>();

            requestParameters.put(ProvisionUnityCatalogRequestParameter.TECHNOLOGY_TYPE.getName(), technologyType);

            archiveHelper.addGovernanceActionProcessFlow(processGUID, null, requestParameters, step1GUID);
        }

        if (supportedElementQualifiedName != null)
        {
            String supportedElementGUID = archiveHelper.queryGUID(supportedElementQualifiedName);
            archiveHelper.addResourceListRelationshipByGUID(supportedElementGUID,
                                                            processGUID,
                                                            ResourceUse.SURVEY_RESOURCE.getResourceUse(),
                                                            description,
                                                            provisionRequestType.getRequestParameters());
        }
    }


    private void addOSSUCCatalogCatalogTemplate()
    {
        final String methodName = "addOSSUCCatalogCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.OSS_UC_CATALOG_TEMPLATE.getTemplateGUID();

        DeployedImplementationTypeDefinition deployedImplementationType = UnityCatalogDeployedImplementationType.OSS_UC_CATALOG;

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + "::" + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + "::" + UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder();
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.OSS_UC_CATALOG_TEMPLATE.getTemplateDescription(),
                                                                    "6.0-SNAPSHOT",
                                                                    null, methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder(),
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                               null,
                                               UnityCatalogPlaceholderProperty.getCatalogPlaceholderPropertyTypes());
    }


    private void addUCSchemaCatalogTemplate()
    {
        final String methodName = "addOSSUCSchemaCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.OSS_UC_SCHEMA_TEMPLATE.getTemplateGUID();

        DeployedImplementationTypeDefinition deployedImplementationType = UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA;
        String                               fullName                   = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder();
        String                               qualifiedName              = deployedImplementationType.getDeployedImplementationType() + "::"
                                                                        + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + "::"
                                                                        + fullName;

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.OSS_UC_SCHEMA_TEMPLATE.getTemplateDescription(),
                                                                    "6.0-SNAPSHOT",
                                                                    null,
                                                                    methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               UnityCatalogPlaceholderProperty.getSchemaPlaceholderPropertyTypes());
    }


    private void addUCVolumeCatalogTemplate()
    {
        final String methodName = "addUCVolumeCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.OSS_UC_VOLUME_TEMPLATE.getTemplateGUID();

        DeployedImplementationTypeDefinition deployedImplementationType = UnityCatalogDeployedImplementationType.OSS_UC_VOLUME;
        String                     fullName                             = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.VOLUME_NAME.getPlaceholder();
        String                     qualifiedName                        = deployedImplementationType.getDeployedImplementationType() + "::"
                                                                        + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + "::"
                                                                        + fullName;
        String connectorTypeGUID = new DataFolderProvider().getConnectorType().getGUID();

        Map<String, Object>  extendedProperties   = new HashMap<>();
        Map<String, String>  facetProperties      = new HashMap<>();
        List<Classification> classifications      = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        facetProperties.put(UnityCatalogPlaceholderProperty.VOLUME_TYPE.getName(), UnityCatalogPlaceholderProperty.VOLUME_TYPE.getPlaceholder());
        facetProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.OSS_UC_VOLUME_TEMPLATE.getTemplateDescription(),
                                                                    "6.0-SNAPSHOT",
                                                                    null,
                                                                    methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.VOLUME_NAME.getPlaceholder(),
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        archiveHelper.addPropertyFacet(assetGUID,
                                       deployedImplementationType.getAssociatedTypeName(),
                                       OpenMetadataType.ASSET.typeName,
                                       null,
                                       qualifiedName,
                                       PropertyFacetValidValues.UNITY_CATALOG_SOURCE_VALUE,
                                       PropertyFacetValidValues.UNITY_CATALOG_SCHEMA_VERSION_VALUE,
                                       PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE,
                                       facetProperties);

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
                                                        null,
                                                        qualifiedName + "::Endpoint",
                                                        fullName + " endpoint",
                                                        null,
                                                        UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder(),
                                                        null,
                                                        null);

        String connectionGUID = archiveHelper.addConnection(qualifiedName + "::Connection",
                                                            fullName + " connection",
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            connectorTypeGUID,
                                                            endpointGUID,
                                                            assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName,
                                                            null);

        archiveHelper.addConnectionForAsset(assetGUID, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               UnityCatalogPlaceholderProperty.getVolumePlaceholderPropertyTypes());
    }


    private void addUCTableCatalogTemplate()
    {
        final String methodName = "addUCTableCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.OSS_UC_TABLE_TEMPLATE.getTemplateGUID();

        DeployedImplementationTypeDefinition deployedImplementationType = UnityCatalogDeployedImplementationType.OSS_UC_TABLE;
        String                               fullName                   = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.TABLE_NAME.getPlaceholder();
        String                               qualifiedName              = deployedImplementationType.getDeployedImplementationType() + "::"
                                                                        + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + "::"
                                                                        + fullName;

        Map<String, Object>  extendedProperties   = new HashMap<>();
        Map<String, Object>  folderProperties     = new HashMap<>();
        Map<String, String>  facetProperties      = new HashMap<>();
        List<Classification> classifications      = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        folderProperties.put(OpenMetadataProperty.PATH_NAME.name, UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());

        facetProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());
        facetProperties.put(UnityCatalogPlaceholderProperty.TABLE_TYPE.getName(), UnityCatalogPlaceholderProperty.TABLE_TYPE.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.OSS_UC_TABLE_TEMPLATE.getTemplateDescription(),
                                                                    "6.0-SNAPSHOT",
                                                                    null,
                                                                    methodName));

        classifications.add(archiveHelper.getDataAssetEncodingClassification(UnityCatalogPlaceholderProperty.DATA_SOURCE_FORMAT.getPlaceholder(),
                                                                             null,
                                                                             null,
                                                                             null));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.TABLE_NAME.getPlaceholder(),
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        extendedProperties   = new HashMap<>();
        extendedProperties.put("file:///" + OpenMetadataProperty.RESOURCE_NAME.name, UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());
        String folderGUID = archiveHelper.addAnchoredAsset(OpenMetadataType.DATA_FOLDER.typeName,
                                                           assetGUID,
                                                           deployedImplementationType.getAssociatedTypeName(),
                                                           OpenMetadataType.ASSET.typeName,
                                                           null,
                                                           qualifiedName + "_storageLocation",
                                                           fullName + "_storageLocation",
                                                           PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                           "Location of files for table " + fullName,
                                                           null,
                                                           folderProperties,
                                                           classifications);

        archiveHelper.addDataContentForDataSet(folderGUID, assetGUID);

        archiveHelper.addPropertyFacet(assetGUID,
                                       deployedImplementationType.getAssociatedTypeName(),
                                       OpenMetadataType.ASSET.typeName,
                                       null,
                                       qualifiedName,
                                       PropertyFacetValidValues.UNITY_CATALOG_SOURCE_VALUE,
                                       PropertyFacetValidValues.UNITY_CATALOG_SCHEMA_VERSION_VALUE,
                                       PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE,
                                       facetProperties);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               UnityCatalogPlaceholderProperty.getTablePlaceholderPropertyTypes());
    }


    private void addUCFunctionCatalogTemplate()
    {
        final String methodName = "addUCFunctionCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.OSS_UC_FUNCTION_TEMPLATE.getTemplateGUID();

        DeployedImplementationTypeDefinition deployedImplementationType = UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION;
        String                     fullName                             = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.FUNCTION_NAME.getPlaceholder();
        String                     qualifiedName                        = deployedImplementationType.getDeployedImplementationType() + "::"
                                                                        + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + "::"
                                                                        + fullName;

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.OSS_UC_FUNCTION_TEMPLATE.getTemplateDescription(),
                                                                    "6.0-SNAPSHOT",
                                                                    null,
                                                                    methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.FUNCTION_NAME.getPlaceholder(),
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               UnityCatalogPlaceholderProperty.getFunctionPlaceholderPropertyTypes());
    }


    private void addUCRegisteredModelCatalogTemplate()
    {
        final String methodName = "addUCRegisteredModelCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.OSS_UC_REGISTERED_MODEL_TEMPLATE.getTemplateGUID();

        DeployedImplementationTypeDefinition deployedImplementationType = UnityCatalogDeployedImplementationType.OSS_UC_REGISTERED_MODEL;
        String                     fullName                             = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder() + "."
                + UnityCatalogPlaceholderProperty.MODEL_NAME.getPlaceholder();
        String                     qualifiedName                        = deployedImplementationType.getDeployedImplementationType() + "::"
                + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + "::"
                + fullName;

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.OSS_UC_REGISTERED_MODEL_TEMPLATE.getTemplateDescription(),
                                                                    "6.0-SNAPSHOT",
                                                                    null,
                                                                    methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.MODEL_NAME.getPlaceholder(),
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               UnityCatalogPlaceholderProperty.getRegisteredModelPlaceholderPropertyTypes());
    }


    private void addUCModelVersionCatalogTemplate()
    {
        final String methodName = "addUCModelVersionCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.OSS_UC_MODEL_VERSION_TEMPLATE.getTemplateGUID();

        DeployedImplementationTypeDefinition deployedImplementationType = UnityCatalogDeployedImplementationType.OSS_UC_REGISTERED_MODEL_VERSION;
        String                     fullName                             = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder() + "."
                + UnityCatalogPlaceholderProperty.MODEL_NAME.getPlaceholder();
        String                     qualifiedName                        = deployedImplementationType.getDeployedImplementationType() + "::"
                + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + "::"
                + fullName + "::" + UnityCatalogPlaceholderProperty.MODEL_VERSION.getPlaceholder();

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.OSS_UC_MODEL_VERSION_TEMPLATE.getTemplateDescription(),
                                                                    "6.0-SNAPSHOT",
                                                                    null,
                                                                    methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.MODEL_NAME.getPlaceholder() + "::" + UnityCatalogPlaceholderProperty.MODEL_VERSION.getPlaceholder(),
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               UnityCatalogPlaceholderProperty.getModelVersionPlaceholderPropertyTypes());
    }



    /**
     * Main program to initiate the archive writer for the connector types for data store connectors supported by
     * the Egeria project.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            UnityCatalogPackArchiveWriter archiveWriter = new UnityCatalogPackArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}