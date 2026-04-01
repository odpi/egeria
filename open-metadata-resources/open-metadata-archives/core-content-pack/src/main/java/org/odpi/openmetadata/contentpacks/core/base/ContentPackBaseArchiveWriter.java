/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.base;

import org.odpi.openmetadata.adapters.connectors.EgeriaInformationSupplyChainDefinition;
import org.odpi.openmetadata.adapters.connectors.EgeriaRoleDefinition;
import org.odpi.openmetadata.contentpacks.core.*;
import org.odpi.openmetadata.adapters.connectors.controls.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.ManageAssetGuard;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionComponentDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnnotationTypeType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyActionGuard;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.samples.archiveutilities.EgeriaBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.GovernanceActionDescription;

import java.util.*;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;


/**
 * CorePackArchiveWriter creates an open metadata archive that includes the connector type
 * information for all open connectors supplied by the egeria project.
 */
public abstract class  ContentPackBaseArchiveWriter extends EgeriaBaseArchiveWriter
{
    private static final Date creationDate = new Date();
    protected final Map<String, String> deployedImplementationTypeQNAMEs = new HashMap<>();


    /**
     * Constructor for an archive.
     *
     * @param archiveGUID unique identifier of the archive
     * @param archiveName name of the archive
     * @param archiveDescription description of archive
     * @param archiveFileName name of file to write archive to
     * @param additionalDependencies archive that this archive is dependent on
     */
    public ContentPackBaseArchiveWriter(String                archiveGUID,
                                        String                archiveName,
                                        String                archiveDescription,
                                        String                archiveFileName,
                                        OpenMetadataArchive[] additionalDependencies)
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              creationDate,
              archiveFileName,
              additionalDependencies);

        for (DeployedImplementationType deployedImplementationType : DeployedImplementationType.values())
        {
            deployedImplementationTypeQNAMEs.put(deployedImplementationType.getDeployedImplementationType(),
                                                 deployedImplementationType.getQualifiedName());
        }
        for (EgeriaDeployedImplementationType deployedImplementationType : EgeriaDeployedImplementationType.values())
        {
            deployedImplementationTypeQNAMEs.put(deployedImplementationType.getDeployedImplementationType(),
                                                 deployedImplementationType.getQualifiedName());
        }
    }


    /**
     * Add an analysis step as a valid metadata value to the archive.
     *
     * @param name name to add
     * @param description description to add
     */
    protected void addAnalysisStep(String name,
                                   String description)
    {
        this.addValidMetadataValue(name,
                                   description,
                                   OpenMetadataProperty.ANALYSIS_STEP.name,
                                   null,
                                   null,
                                   name);
    }


    /**
     * Add a catalog definition for this content pack (if applicable).
     *
     * @param contentPackDefinition which content pack is this for?
     */
    protected void addDigitalProductCatalogDefinition(ContentPackDefinition contentPackDefinition)
    {
        for (DigitalProductCatalogDefinition catalogDefinition : DigitalProductCatalogDefinition.values())
        {
            if (catalogDefinition.getContentPackDefinition() == contentPackDefinition)
            {
                archiveHelper.setGUID(catalogDefinition.getQualifiedName(), catalogDefinition.getGUID());

                if (catalogDefinition.getParent() != null)
                {
                    String collectionGUID = archiveHelper.addCollection(catalogDefinition.getTypeName(),
                                                                        catalogDefinition.getParent().getGUID(),
                                                                        catalogDefinition.getParent().getTypeName(),
                                                                        OpenMetadataType.COLLECTION.typeName,
                                                                        catalogDefinition.getAnchorScopeGUID(),
                                                                        null,
                                                                        catalogDefinition.getQualifiedName(),
                                                                        catalogDefinition.getName(),
                                                                        catalogDefinition.getDescription(),
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null);

                    archiveHelper.addMemberToCollection(catalogDefinition.getParent().getGUID(),
                                                        collectionGUID,
                                                        null);
                }
                else
                {
                    archiveHelper.addCollection(catalogDefinition.getTypeName(),
                                                catalogDefinition.getGUID(),
                                                catalogDefinition.getTypeName(),
                                                OpenMetadataType.COLLECTION.typeName,
                                                catalogDefinition.getAnchorScopeGUID(),
                                                null,
                                                catalogDefinition.getQualifiedName(),
                                                catalogDefinition.getName(),
                                                catalogDefinition.getDescription(),
                                                null,
                                                null,
                                                null,
                                                null);
                }
            }
        }
    }


    /**
     * Create supply chains
     */
    protected void addInformationSupplyChains()
    {
        final String methodName = "addInformationSupplyChains";

        for (org.odpi.openmetadata.adapters.connectors.EgeriaInformationSupplyChainDefinition informationSupplyChain : EgeriaInformationSupplyChainDefinition.values())
        {
            archiveHelper.setGUID(informationSupplyChain.getQualifiedName(), informationSupplyChain.getGUID());

            String iscGUID = archiveHelper.addInformationSupplyChain(informationSupplyChain.getOwningSupplyChain(),
                                                                     informationSupplyChain.isOwningInformationSupplyChainAnchor(),
                                                                     informationSupplyChain.getAnchorScopeGUID(),
                                                                     OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName,
                                                                     informationSupplyChain.getQualifiedName(),
                                                                     informationSupplyChain.getDisplayName(),
                                                                     informationSupplyChain.getDescription(),
                                                                     informationSupplyChain.getScope().getPreferredValue(),
                                                                     informationSupplyChain.getDataProcessingPurposes(),
                                                                     null,
                                                                     informationSupplyChain.getOwner(),
                                                                     informationSupplyChain.getOwnerTypeName(),
                                                                     informationSupplyChain.getOwnerPropertyName(),
                                                                     null,
                                                                     null,
                                                                     null);
            assert(iscGUID.equals(informationSupplyChain.getGUID()));

            if (informationSupplyChain.isTemplate())
            {
                archiveHelper.addTemplateClassification(iscGUID,
                                                        informationSupplyChain.getTemplateName(),
                                                        informationSupplyChain.getTemplateDescription(),
                                                        versionName,
                                                        null,
                                                        methodName);
            }
        }
    }



    /**
     * Create a template for a data file and link it to the associated open metadata type.
     * The template consists of a DataFile asset plus an optional connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param connectorTypeGUID          connector type to link to the connection
     * @param deployedImplementationType deployed implementation type to link the template to
     * @param encodingLanguage           language used to encode the contents of the file
     * @param configurationProperties    configuration properties
     * @param placeholderPropertyTypes   placeholders used in template
     */
    protected void createDataFileCatalogTemplate(DeployedImplementationType    deployedImplementationType,
                                                 String                        connectorTypeGUID,
                                                 String                        encodingLanguage,
                                                 Map<String, Object>           configurationProperties,
                                                 List<PlaceholderPropertyType> placeholderPropertyTypes)
    {
        final String methodName = "createDataFileCatalogTemplate";

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType()  + "::" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + "::" + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder();
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_TYPE.name, PlaceholderProperty.FILE_TYPE.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_EXTENSION.name, PlaceholderProperty.FILE_EXTENSION.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_NAME.name, PlaceholderProperty.FILE_NAME.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create a data asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V6.1-SNAPSHOT",
                                                                    null, methodName));

        classifications.add(archiveHelper.getDataAssetEncodingClassification(PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                                                                             encodingLanguage,
                                                                             null,
                                                                             null));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  PlaceholderProperty.FILE_NAME.getPlaceholder(),
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        if (connectorTypeGUID != null)
        {
            String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName,
                                                            null,
                                                            qualifiedName + ":Endpoint",
                                                            PlaceholderProperty.FILE_PATH_NAME.getPlaceholder() + " endpoint",
                                                            null,
                                                            PlaceholderProperty.FILE_PATH_NAME.getPlaceholder(),
                                                            null,
                                                            null);

            String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                                PlaceholderProperty.FILE_PATH_NAME.getPlaceholder() + " connection",
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                configurationProperties,
                                                                null,
                                                                connectorTypeGUID,
                                                                endpointGUID,
                                                                assetGUID,
                                                                deployedImplementationType.getAssociatedTypeName(),
                                                                OpenMetadataType.ASSET.typeName,
                                                                null);

            archiveHelper.addConnectionForAsset(assetGUID, connectionGUID);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               placeholderPropertyTypes);
    }


    /**
     * Loop through the server template definitions creating the specified templates.
     *
     * @param contentPackDefinition which content pack are these templates for?
     */
    protected void addSoftwareServerCatalogTemplates(ContentPackDefinition contentPackDefinition)
    {
        for (SoftwareServerTemplateDefinition templateDefinition : SoftwareServerTemplateDefinition.values())
        {
            if (templateDefinition.getContentPackDefinition() == contentPackDefinition)
            {
                createSoftwareServerCatalogTemplate(templateDefinition.getTemplateGUID(),
                                                    templateDefinition.getQualifiedName(),
                                                    templateDefinition.getTemplateName(),
                                                    templateDefinition.getTemplateDescription(),
                                                    templateDefinition.getTemplateVersionIdentifier(),
                                                    templateDefinition.getDeployedImplementationType(),
                                                    templateDefinition.getSoftwareCapabilityType(),
                                                    templateDefinition.getSoftwareCapabilityName(),
                                                    templateDefinition.getServerName(),
                                                    null,
                                                    templateDefinition.getElementVersionIdentifier(),
                                                    templateDefinition.getServerDescription(),
                                                    templateDefinition.getUserId(),
                                                    templateDefinition.getConnectorTypeGUID(),
                                                    templateDefinition.getNetworkAddress(),
                                                    templateDefinition.getConfigurationProperties(),
                                                    templateDefinition.getSecretsCollectionName(),
                                                    templateDefinition.getSecretsStorePurpose(),
                                                    templateDefinition.getSecretsStoreConnectorTypeGUID(),
                                                    templateDefinition.getSecretsStoreFileName(),
                                                    templateDefinition.getReplacementAttributes(),
                                                    templateDefinition.getPlaceholders());
            }
        }
    }


    /**
     * Loop through the server template definitions creating the specified templates.
     *
     * @param contentPackDefinition which content pack are these templates for?
     */
    protected void addDataAssetCatalogTemplates(ContentPackDefinition contentPackDefinition)
    {
        for (DataAssetTemplateDefinition templateDefinition : DataAssetTemplateDefinition.values())
        {
            if (templateDefinition.getContentPackDefinition() == contentPackDefinition)
            {
                createDataAssetCatalogTemplate(templateDefinition.getTemplateGUID(),
                                               templateDefinition.getTemplateVersionIdentifier(),
                                               templateDefinition.getDeployedImplementationType(),
                                               templateDefinition.getAssetName(),
                                               templateDefinition.getAssetDescription(),
                                               templateDefinition.getQualifiedName(),
                                               templateDefinition.getElementVersionIdentifier(),
                                               templateDefinition.getExtendedProperties(),
                                               templateDefinition.getEncoding(),
                                               templateDefinition.getEncodingLanguage(),
                                               templateDefinition.getConnectorTypeGUID(),
                                               templateDefinition.getNetworkAddress(),
                                               templateDefinition.getConfigurationProperties(),
                                               templateDefinition.getSecretsCollectionName(),
                                               templateDefinition.getSecretsStorePurpose(),
                                               templateDefinition.getSecretsStoreConnectorTypeGUID(),
                                               templateDefinition.getSecretsStoreFileName(),
                                               templateDefinition.getReplacementAttributes(),
                                               templateDefinition.getPlaceholders());
            }
        }
    }


    /**
     * Loop through the server template definitions creating the specified templates.
     *
     * @param contentPackDefinition which content pack are these templates for?
     */
    protected void addTabularDataSetCatalogTemplates(ContentPackDefinition contentPackDefinition)
    {
        for (TabularDataSetTemplateDefinition templateDefinition : TabularDataSetTemplateDefinition.values())
        {
            if (templateDefinition.getContentPackDefinition() == contentPackDefinition)
            {
                createTabularDataSetCatalogTemplate(templateDefinition.getTemplateGUID(),
                                               templateDefinition.getTemplateVersionIdentifier(),
                                               templateDefinition.getDeployedImplementationType(),
                                               templateDefinition.getAssetName(),
                                               templateDefinition.getAssetDescription(),
                                               templateDefinition.getQualifiedName(),
                                               templateDefinition.getElementVersionIdentifier(),
                                               templateDefinition.getExtendedProperties(),
                                               templateDefinition.getEncoding(),
                                               templateDefinition.getEncodingLanguage(),
                                               templateDefinition.getDataSetConnectorTypeGUID(),
                                               templateDefinition.getDataSetConfigurationProperties(),
                                               templateDefinition.getTechnologyConnectorTypeGUID(),
                                               templateDefinition.getTechnologyNetworkAddress(),
                                               templateDefinition.getTechnologyConfigurationProperties(),
                                               templateDefinition.getSecretsCollectionName(),
                                               templateDefinition.getSecretsStorePurpose(),
                                               templateDefinition.getSecretsStoreConnectorTypeGUID(),
                                               templateDefinition.getSecretsStoreFileName(),
                                               templateDefinition.getReplacementAttributes(),
                                               templateDefinition.getPlaceholders());
            }
        }
    }


    /**
     * Create a template for a software server and link it to the associated deployed implementation type.
     * The template consists of a SoftwareServer asset linked to a software capability, plus a connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param guid                             fixed unique identifier
     * @param qualifiedName                    unique name for the template
     * @param templateName                     name of template in Template classification
     * @param templateDescription              description of the template in the Template classification
     * @param templateVersion                  version of the template in the Template classification
     * @param deployedImplementationType       deployed implementation type for the technology
     * @param softwareCapabilityType           type of the associated capability
     * @param softwareCapabilityName           name for the associated capability
     * @param serverName                       name for the server
     * @param namespacePath                    qualifying path name of the server
     * @param serverVersionIdentifier          server version identifier
     * @param description                      description for the server
     * @param userId                           userId for the connection
     * @param connectorTypeGUID                connector type to link to the connection
     * @param networkAddress                   network address for the endpoint
     * @param configurationProperties          additional properties for the connection
     * @param secretsStoreCollectionName       name of the collection to use within the secrets store
     * @param secretsStorePurpose              purpose for the secrets store
     * @param secretsStoreConnectorTypeGUID    optional name for the secrets store connector provider to include in the template
     * @param secretsStoreFileName             location of the secrets store
     * @param replacementAttributeTypes        attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes         placeholder variables used in the supplied parameters
     */
    protected void createSoftwareServerCatalogTemplate(String                               guid,
                                                       String                               qualifiedName,
                                                       String                               templateName,
                                                       String                               templateDescription,
                                                       String                               templateVersion,
                                                       DeployedImplementationTypeDefinition deployedImplementationType,
                                                       DeployedImplementationTypeDefinition softwareCapabilityType,
                                                       String                               softwareCapabilityName,
                                                       String                               serverName,
                                                       String                               namespacePath,
                                                       String                               serverVersionIdentifier,
                                                       String                               description,
                                                       String                               userId,
                                                       String                               connectorTypeGUID,
                                                       String                               networkAddress,
                                                       Map<String, Object>                  configurationProperties,
                                                       String                               secretsStoreCollectionName,
                                                       String                               secretsStorePurpose,
                                                       String                               secretsStoreConnectorTypeGUID,
                                                       String                               secretsStoreFileName,
                                                       List<ReplacementAttributeType>       replacementAttributeTypes,
                                                       List<PlaceholderPropertyType>        placeholderPropertyTypes)
    {
        final String methodName = "createSoftwareServerCatalogTemplate";

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, serverName);
        extendedProperties.put(OpenMetadataProperty.NAMESPACE_PATH.name, namespacePath);

        if (deployedImplementationType.getAssociatedClassification() != null)
        {
            classifications.add(archiveHelper.getServerPurposeClassification(deployedImplementationType.getAssociatedClassification(), null));
        }

        classifications.add(archiveHelper.getTemplateClassification(templateName,
                                                                    templateDescription,
                                                                    templateVersion,
                                                                    null,
                                                                    methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  serverName,
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  serverVersionIdentifier,
                                                  description,
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        if (softwareCapabilityType != null)
        {
            archiveHelper.addSoftwareCapability(softwareCapabilityType.getAssociatedTypeName(),
                                                qualifiedName + "::" + softwareCapabilityName,
                                                softwareCapabilityName,
                                                null,
                                                softwareCapabilityType.getDeployedImplementationType(),
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                (Classification) null,
                                                assetGUID,
                                                deployedImplementationType.getAssociatedTypeName(),
                                                OpenMetadataType.ASSET.typeName,
                                                null);

            archiveHelper.addSupportedSoftwareCapabilityRelationship(qualifiedName + "::" + softwareCapabilityName,
                                                                     qualifiedName,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     null);
        }

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
                                                        null,
                                                        qualifiedName + ":Endpoint",
                                                        serverName + " endpoint",
                                                        null,
                                                        networkAddress,
                                                        null,
                                                        null);

        archiveHelper.addServerEndpointRelationship(assetGUID, endpointGUID);

        String connectionGUID;

        if (secretsStoreConnectorTypeGUID == null)
        {
            connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                         serverName + " connection",
                                                         null,
                                                         userId,
                                                         null,
                                                         null,
                                                         null,
                                                         configurationProperties,
                                                         null,
                                                         connectorTypeGUID,
                                                         endpointGUID,
                                                         assetGUID,
                                                         deployedImplementationType.getAssociatedTypeName(),
                                                         OpenMetadataType.ASSET.typeName,
                                                         null);
        }
        else
        {
            connectionGUID = archiveHelper.addConnection(OpenMetadataType.VIRTUAL_CONNECTION.typeName,
                                                         qualifiedName + ":Connection",
                                                         serverName + " connection",
                                                         null,
                                                         userId,
                                                         null,
                                                         null,
                                                         null,
                                                         configurationProperties,
                                                         null,
                                                         connectorTypeGUID,
                                                         endpointGUID,
                                                         assetGUID,
                                                         deployedImplementationType.getAssociatedTypeName(),
                                                         OpenMetadataType.ASSET.typeName,
                                                         null);

            Map<String, Object> secretsStoreConfigurationProperties = new HashMap<>();

            secretsStoreConfigurationProperties.put(SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName(), secretsStoreCollectionName);

            String secretStoreEndpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                                       deployedImplementationType.getAssociatedTypeName(),
                                                                       OpenMetadataType.ASSET.typeName,
                                                                       null,
                                                                       qualifiedName + "::SecretStoreEndpoint",
                                                                       serverName + " secret store endpoint",
                                                                       null,
                                                                       secretsStoreFileName,
                                                                       null,
                                                                       null);

            String secretsStoreConnectionGUID = archiveHelper.addConnection(OpenMetadataType.CONNECTION.typeName,
                                                                            qualifiedName + "::SecretsStoreConnection",
                                                                            serverName + " secrets store connection",
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            secretsStoreConfigurationProperties,
                                                                            null,
                                                                            secretsStoreConnectorTypeGUID,
                                                                            secretStoreEndpointGUID,
                                                                            assetGUID,
                                                                            deployedImplementationType.getAssociatedTypeName(),
                                                                            OpenMetadataType.ASSET.typeName,
                                                                            null);

            archiveHelper.addEmbeddedConnection(connectionGUID,
                                                0,
                                                secretsStorePurpose,
                                                null,
                                                secretsStoreConnectionGUID);
        }

        archiveHelper.addConnectionForAsset(assetGUID, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addReplacementAttributes(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               replacementAttributeTypes);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               placeholderPropertyTypes);
    }


    /**
     * Loop through the server template definitions creating the specified templates.
     */
    private void addEndpointCatalogTemplates()
    {
        for (EndpointTemplateDefinition templateDefinition : EndpointTemplateDefinition.values())
        {
            createEndpointCatalogTemplate(templateDefinition.getTemplateGUID(),
                                          templateDefinition.getTemplateName(),
                                          templateDefinition.getTemplateDescription(),
                                          templateDefinition.getTemplateVersionIdentifier(),
                                          templateDefinition.getDeployedImplementationType(),
                                          templateDefinition.getServerName(),
                                          templateDefinition.getEndpointDescription(),
                                          templateDefinition.getNetworkAddress(),
                                          templateDefinition.getProtocol(),
                                          templateDefinition.getPlaceholders());
        }
    }


    /**
     * Create a template for a software server and link it to the associated deployed implementation type.
     * The template consists of a SoftwareServer asset linked to a software capability, plus a connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param guid                             fixed unique identifier
     * @param templateName                     name of template in Template classification
     * @param templateDescription              description of the template in the Template classification
     * @param templateVersion                  version of the template in the Template classification
     * @param serverName                       name for the server
     * @param description                      description for the server
     * @param networkAddress                   network address for the endpoint
     * @param protocol                         communication protocol for the endpoint
     * @param placeholderPropertyTypes         placeholder variables used in the supplied parameters
     */
    protected void createEndpointCatalogTemplate(String                               guid,
                                                 String                               templateName,
                                                 String                               templateDescription,
                                                 String                               templateVersion,
                                                 DeployedImplementationTypeDefinition deployedImplementationType,
                                                 String                               serverName,
                                                 String                               description,
                                                 String                               networkAddress,
                                                 String                               protocol,
                                                 List<PlaceholderPropertyType>        placeholderPropertyTypes)
    {
        final String methodName = "createEndpointCatalogTemplate";

        String         qualifiedName            = deployedImplementationType.getDeployedImplementationType() + "::" + serverName;
        Classification templateClassification   = archiveHelper.getTemplateClassification(templateName,
                                                                                          templateDescription,
                                                                                          templateVersion,
                                                                                          null,
                                                                                          methodName);

        archiveHelper.setGUID(qualifiedName, guid);

        String endpointGUID = archiveHelper.addEndpoint(null,
                                                        OpenMetadataType.ENDPOINT.typeName,
                                                        OpenMetadataType.ENDPOINT.typeName,
                                                        null,
                                                        qualifiedName,
                                                        serverName + " endpoint",
                                                        description,
                                                        networkAddress,
                                                        protocol,
                                                        null,
                                                        templateClassification);

        assert(guid.equals(endpointGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, endpointGUID);

        archiveHelper.addPlaceholderProperties(endpointGUID,
                                               OpenMetadataType.ENDPOINT.typeName,
                                               endpointGUID,
                                               OpenMetadataType.ENDPOINT.typeName,
                                               OpenMetadataType.ENDPOINT.typeName,
                                               null,
                                               placeholderPropertyTypes);
    }


    /**
     * Create a template for a host and link it to the associated deployed implementation type.
     * The template consists of a SoftwareServer asset linked to a software capability, plus a connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param guid                             fixed unique identifier
     * @param deployedImplementationType       deployed implementation type for the technology
     * @param softwareCapabilityType           type of the associated capability
     * @param softwareCapabilityName           name for the associated capability
     * @param softwareCapabilityClassification classification for the software capability (or null)
     */
    protected void createHostCatalogTemplate(String                         guid,
                                             DeployedImplementationType     deployedImplementationType,
                                             DeployedImplementationType     softwareCapabilityType,
                                             String                         softwareCapabilityName,
                                             Classification                 softwareCapabilityClassification)
    {
        final String methodName = "createHostCatalogTemplate";

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + "::" + PlaceholderProperty.HOST_URL.getPlaceholder();
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, PlaceholderProperty.HOST_URL.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create a " + deployedImplementationType.getDeployedImplementationType() + " Host with an associated SoftwareCapability.",
                                                                    "6.1-SNAPSHOT",
                                                                    null, methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        if (softwareCapabilityType != null)
        {
            archiveHelper.addSoftwareCapability(softwareCapabilityType.getAssociatedTypeName(),
                                                qualifiedName + "::" + softwareCapabilityName,
                                                softwareCapabilityName,
                                                null,
                                                softwareCapabilityType.getDeployedImplementationType(),
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                softwareCapabilityClassification,
                                                assetGUID,
                                                deployedImplementationType.getAssociatedTypeName(),
                                                OpenMetadataType.ASSET.typeName,
                                                null);

            archiveHelper.addSupportedSoftwareCapabilityRelationship(qualifiedName + "::" + softwareCapabilityName,
                                                                     qualifiedName,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     null);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               PlaceholderProperty.getHostPlaceholderPropertyTypes());
    }


    /**
     * Create a template for a software server and link it to the associated deployed implementation type.
     * The template consists of a SoftwareServer asset linked to a software capability, plus a connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param guid fixed guid for this template
     * @param deployedImplementationType deployed implementation type for the technology
     * @param serverQualifiedName qualified name of the owning server
     * @param softwareCapabilityName name for the associated capability
     * @param softwareCapabilityDescription description for the software capability
     * @param softwareCapabilityClassification optional classification for the associated capability
     * @param replacementAttributeTypes attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes placeholder variables used in the supplied parameters
     */
    protected void createSoftwareCapabilityCatalogTemplate(String                         guid,
                                                           DeployedImplementationType     deployedImplementationType,
                                                           String                         serverQualifiedName,
                                                           String                         softwareCapabilityName,
                                                           String                         softwareCapabilityDescription,
                                                           Classification                 softwareCapabilityClassification,
                                                           List<ReplacementAttributeType> replacementAttributeTypes,
                                                           List<PlaceholderPropertyType>  placeholderPropertyTypes)
    {
        final String methodName = "createSoftwareCapabilityCatalogTemplate";

        String               qualifiedName      = deployedImplementationType.getAssociatedTypeName() + "::" + deployedImplementationType.getDeployedImplementationType() + "::" + serverQualifiedName + "::" + softwareCapabilityName;
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create a " + deployedImplementationType.getDeployedImplementationType() + " SoftwareCapability.",
                                                                    "6.1-SNAPSHOT",
                                                                    null, methodName));

        if (softwareCapabilityClassification != null)
        {
            classifications.add(softwareCapabilityClassification);
        }

        archiveHelper.setGUID(qualifiedName, guid);
        String capabilityGUID = archiveHelper.addSoftwareCapability(deployedImplementationType.getAssociatedTypeName(),
                                                                    qualifiedName,
                                                                    softwareCapabilityName,
                                                                    softwareCapabilityDescription,
                                                                    deployedImplementationType.getDeployedImplementationType(),
                                                                    PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(), 
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    extendedProperties,
                                                                    classifications,
                                                                    null,
                                                                    deployedImplementationType.getAssociatedTypeName(),
                                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                    null);
        assert(guid.equals(capabilityGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, capabilityGUID);

        archiveHelper.addReplacementAttributes(capabilityGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               capabilityGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                               null,
                                               replacementAttributeTypes);

        archiveHelper.addPlaceholderProperties(capabilityGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               capabilityGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                               null,
                                               placeholderPropertyTypes);
    }


    /**
     * Create a template for a type of asset and link it to the associated deployed implementation type.
     * The template consists of an asset linked to a connection, that is in turn linked
     * to the supplied connector type and an endpoint, along with a nested secrets store
     *
     * @param templateGUID fixed unique identifier
     * @param templateVersion version of the template
     * @param deployedImplementationType deployed implementation type for the technology
     * @param assetDisplayName name for the asset
     * @param assetDescription description
     * @param qualifiedName optional server name
     * @param versionIdentifier version identifier
     * @param suppliedExtendedProperties extended properties for the asset
     * @param encoding           what encoding is needed?
     * @param encodingLanguage           language used to encode the contents of the file
     * @param connectorTypeGUID connector type to link to the connection
     * @param networkAddress network address for the endpoint
     * @param configurationProperties  additional properties for the connection
     * @param secretsStoreCollectionName name of the collection to use in the secrets store
     * @param secretsStorePurpose              purpose for the secrets store
     * @param secretsStoreConnectorTypeGUID    optional name for the secrets store connector provider to include in the template
     * @param secretsStoreFileName             location of the secrets store
     * @param replacementAttributeTypes attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes placeholder variables used in the supplied parameters
     */
    protected void createDataAssetCatalogTemplate(String                               templateGUID,
                                                  String                               templateVersion,
                                                  DeployedImplementationTypeDefinition deployedImplementationType,
                                                  String                               assetDisplayName,
                                                  String                               assetDescription,
                                                  String                               qualifiedName,
                                                  String                               versionIdentifier,
                                                  Map<String, Object>                  suppliedExtendedProperties,
                                                  String                               encoding,
                                                  String                               encodingLanguage,
                                                  String                               connectorTypeGUID,
                                                  String                               networkAddress,
                                                  Map<String, Object>                  configurationProperties,
                                                  String                               secretsStoreCollectionName,
                                                  String                               secretsStorePurpose,
                                                  String                               secretsStoreConnectorTypeGUID,
                                                  String                               secretsStoreFileName,
                                                  List<ReplacementAttributeType>       replacementAttributeTypes,
                                                  List<PlaceholderPropertyType>        placeholderPropertyTypes)
    {
        final String methodName = "createDataAssetCatalogTemplate";

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());

        if (suppliedExtendedProperties != null)
        {
            extendedProperties.putAll(suppliedExtendedProperties);
        }

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create a data asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    templateVersion,
                                                                    null,
                                                                    methodName));

        if (encoding != null)
        {
            classifications.add(archiveHelper.getDataAssetEncodingClassification(encoding,
                                                                                 encodingLanguage,
                                                                                 null,
                                                                                 null));
        }

        archiveHelper.setGUID(qualifiedName, templateGUID);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  assetDisplayName,
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  versionIdentifier,
                                                  assetDescription,
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(templateGUID.equals(assetGUID));

        String connectionGUID = addTechnologyConnection(assetGUID,
                                                        deployedImplementationType,
                                                        qualifiedName,
                                                        assetDisplayName,
                                                        connectorTypeGUID,
                                                        networkAddress,
                                                        configurationProperties,
                                                        secretsStoreConnectorTypeGUID,
                                                        secretsStoreCollectionName,
                                                        secretsStoreFileName,
                                                        secretsStorePurpose);
        if (connectionGUID != null)
        {
            archiveHelper.addConnectionForAsset(assetGUID, connectionGUID);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addReplacementAttributes(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               replacementAttributeTypes);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               placeholderPropertyTypes);
    }



    /**
     * Create a template for an asset type and link it to the associated deployed implementation type.
     * The template consists of an asset linked to a connection, that is in turn linked
     * to the supplied connector type and an endpoint, along with a nested secrets store
     *
     * @param templateGUID fixed unique identifier
     * @param templateVersion version of the template
     * @param deployedImplementationType deployed implementation type for the technology
     * @param assetName name for the asset
     * @param assetDescription description
     * @param qualifiedName optional server name
     * @param versionIdentifier version identifier
     * @param suppliedExtendedProperties extended properties for the asset
     * @param encoding           what encoding is needed?
     * @param encodingLanguage           language used to encode the contents of the file
     * @param dataSetConnectorTypeGUID connectorGUID for the wrapper connector - this is optional if the technology
     *                                 connector supports tabular data set directly.
     * @param dataSetConfigurationProperties  configuration properties for the data set connection eg tableName
     * @param technologyConnectorTypeGUID connector type to link to the technology
     * @param networkAddress network address for the technology endpoint
     * @param technologyConfigurationProperties  additional properties for the technology connection
     * @param secretsStoreCollectionName name of the collection to use in the secrets store
     * @param secretsStorePurpose              purpose for the secrets store
     * @param secretsStoreConnectorTypeGUID    optional name for the secrets store connector provider to include in the template
     * @param secretsStoreFileName             location of the secrets store
     * @param replacementAttributeTypes attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes placeholder variables used in the supplied parameters
     */
    protected void createTabularDataSetCatalogTemplate(String                               templateGUID,
                                                       String                               templateVersion,
                                                       DeployedImplementationTypeDefinition deployedImplementationType,
                                                       String                               assetName,
                                                       String                               assetDescription,
                                                       String                               qualifiedName,
                                                       String                               versionIdentifier,
                                                       Map<String, Object>                  suppliedExtendedProperties,
                                                       String                               encoding,
                                                       String                               encodingLanguage,
                                                       String                               dataSetConnectorTypeGUID,
                                                       Map<String, Object>                  dataSetConfigurationProperties,
                                                       String                               technologyConnectorTypeGUID,
                                                       String                               networkAddress,
                                                       Map<String, Object>                  technologyConfigurationProperties,
                                                       String                               secretsStoreCollectionName,
                                                       String                               secretsStorePurpose,
                                                       String                               secretsStoreConnectorTypeGUID,
                                                       String                               secretsStoreFileName,
                                                       List<ReplacementAttributeType>       replacementAttributeTypes,
                                                       List<PlaceholderPropertyType>        placeholderPropertyTypes)
    {
        final String methodName = "createTabularDataSetCatalogTemplate";

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());

        if (suppliedExtendedProperties != null)
        {
            extendedProperties.putAll(suppliedExtendedProperties);
        }

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create a tabular data set of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    templateVersion,
                                                                    null,
                                                                    methodName));

        if (encoding != null)
        {
            classifications.add(archiveHelper.getDataAssetEncodingClassification(encoding,
                                                                                 encodingLanguage,
                                                                                 null,
                                                                                 null));
        }

        archiveHelper.setGUID(qualifiedName, templateGUID);

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  assetName,
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  versionIdentifier,
                                                  assetDescription,
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(templateGUID.equals(assetGUID));

        String connectionGUID;
        if (dataSetConnectorTypeGUID != null)
        {
            connectionGUID = archiveHelper.addConnection(OpenMetadataType.VIRTUAL_CONNECTION.typeName,
                                                                qualifiedName + "::DataSetConnection",
                                                                assetName + " data set connection",
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                dataSetConfigurationProperties,
                                                                null,
                                                                dataSetConnectorTypeGUID,
                                                                null,
                                                                assetGUID,
                                                                deployedImplementationType.getAssociatedTypeName(),
                                                                OpenMetadataType.ASSET.typeName,
                                                                null);



            String technologyConnectionGUID = addTechnologyConnection(assetGUID,
                                                                      deployedImplementationType,
                                                                      qualifiedName,
                                                                      assetName,
                                                                      technologyConnectorTypeGUID,
                                                                      networkAddress,
                                                                      technologyConfigurationProperties,
                                                                      secretsStoreConnectorTypeGUID,
                                                                      secretsStoreCollectionName,
                                                                      secretsStoreFileName,
                                                                      secretsStorePurpose);
            if (technologyConnectionGUID != null)
            {
                archiveHelper.addEmbeddedConnection(connectionGUID,
                                                    0,
                                                    "Connect to technology",
                                                    null,
                                                    technologyConnectionGUID);
            }
        }
        else
        {
            connectionGUID = addTechnologyConnection(assetGUID,
                                                     deployedImplementationType,
                                                     qualifiedName,
                                                     assetName,
                                                     technologyConnectorTypeGUID,
                                                     networkAddress,
                                                     technologyConfigurationProperties,
                                                     secretsStoreConnectorTypeGUID,
                                                     secretsStoreCollectionName,
                                                     secretsStoreFileName,
                                                     secretsStorePurpose);
        }

        if (connectionGUID != null)
        {
            archiveHelper.addConnectionForAsset(assetGUID, connectionGUID);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addReplacementAttributes(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               replacementAttributeTypes);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               null,
                                               placeholderPropertyTypes);
    }


    /**
     * Add the connection for the connector that interacts with a specific technology.
     *
     * @param assetGUID unique id of asset
     * @param deployedImplementationType asset's deployed implementation type
     * @param qualifiedName asset's qualified name
     * @param assetName asset's display name
     * @param connectorTypeGUID optional unique identifier of the connector type for the connection
     * @param networkAddress address to access technology
     * @param configurationProperties configuration properties for the technology connector
     * @param secretsStoreConnectorTypeGUID connector type for the optional embedded secrets store connector
     * @param secretsStoreCollectionName collection name for the optional embedded secrets store connector
     * @param secretsStoreFileName file name for the optional embedded secrets store connector
     * @param secretsStorePurpose purpose of the optional embedded secrets store connector
     * @return unique identifier of the connection - or null if connectorTypeGUID is null
     */
    protected String addTechnologyConnection(String                               assetGUID,
                                             DeployedImplementationTypeDefinition deployedImplementationType,
                                             String                               qualifiedName,
                                             String                               assetName,
                                             String                               connectorTypeGUID,
                                             String                               networkAddress,
                                             Map<String,Object>                   configurationProperties,
                                             String                               secretsStoreConnectorTypeGUID,
                                             String                               secretsStoreCollectionName,
                                             String                               secretsStoreFileName,
                                             String                               secretsStorePurpose)
    {
        if (connectorTypeGUID != null)
        {
            String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName,
                                                            null,
                                                            qualifiedName + "::Endpoint",
                                                            assetName + " endpoint",
                                                            null,
                                                            networkAddress,
                                                            null,
                                                            null);

            String connectionGUID;
            if (secretsStoreConnectorTypeGUID == null)
            {
                connectionGUID = archiveHelper.addConnection(qualifiedName + "::Connection",
                                                             assetName + " connection",
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             configurationProperties,
                                                             null,
                                                             connectorTypeGUID,
                                                             endpointGUID,
                                                             assetGUID,
                                                             deployedImplementationType.getAssociatedTypeName(),
                                                             OpenMetadataType.ASSET.typeName,
                                                             null);
            }
            else
            {
                connectionGUID = archiveHelper.addConnection(OpenMetadataType.VIRTUAL_CONNECTION.typeName,
                                                             qualifiedName + "::Connection",
                                                             assetName + " connection",
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             configurationProperties,
                                                             null,
                                                             connectorTypeGUID,
                                                             endpointGUID,
                                                             assetGUID,
                                                             deployedImplementationType.getAssociatedTypeName(),
                                                             OpenMetadataType.ASSET.typeName,
                                                             null);

                addSecretStoreConnection(assetGUID,
                                         deployedImplementationType,
                                         qualifiedName,
                                         assetName,
                                         secretsStoreConnectorTypeGUID,
                                         secretsStoreCollectionName,
                                         secretsStoreFileName,
                                         secretsStorePurpose,
                                         connectionGUID);
            }

            return connectionGUID;
        }

        return null;
    }


    /**
     * Add the connection for a nested secrets store connector.
     *
     * @param assetGUID unique id of asset
     * @param deployedImplementationType asset's deployed implementation type
     * @param qualifiedName asset's qualified name
     * @param assetName asset's display name
     * @param secretsStoreConnectorTypeGUID connector type for the optional embedded secrets store connector
     * @param secretsStoreCollectionName collection name for the optional embedded secrets store connector
     * @param secretsStoreFileName file name for the optional embedded secrets store connector
     * @param secretsStorePurpose purpose of the optional embedded secrets store connector
     * @param parentConnectionGUID connection GUID to connect new connection to
     */
    protected void addSecretStoreConnection(String                               assetGUID,
                                            DeployedImplementationTypeDefinition deployedImplementationType,
                                            String                               qualifiedName,
                                            String                               assetName,
                                            String                               secretsStoreConnectorTypeGUID,
                                            String                               secretsStoreCollectionName,
                                            String                               secretsStoreFileName,
                                            String                               secretsStorePurpose,
                                            String                               parentConnectionGUID)
    {
        Map<String, Object> secretsStoreConfigurationProperties = new HashMap<>();

        secretsStoreConfigurationProperties.put(SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName(), secretsStoreCollectionName);

        String secretStoreEndpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                                   deployedImplementationType.getAssociatedTypeName(),
                                                                   OpenMetadataType.ASSET.typeName,
                                                                   null,
                                                                   qualifiedName + "::SecretStoreEndpoint",
                                                                   assetName + " secret store endpoint",
                                                                   null,
                                                                   secretsStoreFileName,
                                                                   null,
                                                                   null);

        String secretsStoreConnectionGUID = archiveHelper.addConnection(OpenMetadataType.CONNECTION.typeName,
                                                                        qualifiedName + "::SecretsStoreConnection",
                                                                        assetName + " secrets store connection",
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        secretsStoreConfigurationProperties,
                                                                        null,
                                                                        secretsStoreConnectorTypeGUID,
                                                                        secretStoreEndpointGUID,
                                                                        assetGUID,
                                                                        deployedImplementationType.getAssociatedTypeName(),
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null);

        archiveHelper.addEmbeddedConnection(parentConnectionGUID,
                                            0,
                                            secretsStorePurpose,
                                            null,
                                            secretsStoreConnectionGUID);
    }


    /**
     * Add a new valid values record for a deployed implementation type.
     *
     * @param deployedImplementationType Description of the technology type

     * @return unique identifier of the deployedImplementationType
     */
    protected String addDeployedImplementationType(DeployedImplementationTypeDefinition deployedImplementationType)
    {
        String parentSetGUID = this.getParentSet(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);
        String qualifiedName = deployedImplementationType.getQualifiedName();

        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put(OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name, deployedImplementationType.getAssociatedTypeName());

        /*
         * Not all deployed implementation types have fixed guids. The GUID is typically fixed to use it, say,
         *  for anchorScopeGUID.
         */
        if (deployedImplementationType.getGUID() != null)
        {
            archiveHelper.setGUID(qualifiedName, deployedImplementationType.getGUID());
        }

        String validValueGUID = this.archiveHelper.addValidValue(null,
                                                                 parentSetGUID,
                                                                 parentSetGUID,
                                                                 OpenMetadataType.VALID_METADATA_VALUE.typeName,
                                                                 OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                 null,
                                                                 OpenMetadataType.TECHNOLOGY_TYPE.typeName,
                                                                 qualifiedName,
                                                                 Category.VALID_METADATA_VALUES.getName(),
                                                                 OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                 deployedImplementationType.getDeployedImplementationType(),
                                                                 deployedImplementationType.getDescription(),
                                                                 null,
                                                                 null,
                                                                 DataType.STRING.getDisplayName(),
                                                                 OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                                 deployedImplementationType.getDeployedImplementationType(),
                                                                 deployedImplementationType.getWikiLink(),
                                                                 false,
                                                                 0,
                                                                 false,
                                                                 additionalProperties);

        /*
         * DeployedImplementationTypes may have a parent type.
         */
        if (deployedImplementationType.getIsATypeOf() != null)
        {
            archiveHelper.addValidValueAssociationRelationship(qualifiedName,
                                                               deployedImplementationType.getIsATypeOf().getQualifiedName(),
                                                               OpenMetadataValidValues.VALID_METADATA_VALUE_IS_TYPE_OF,
                                                               AssociationType.INHERITANCE.getName(),
                                                               null);
        }

        /*
         * DeployedImplementationTypes may be part of a solution.  They make useful components to link to for
         * connectors.
         */
        SolutionComponentDefinition solutionComponentDefinition = deployedImplementationType.getSolutionComponent();

        if ((solutionComponentDefinition != null) && (solutionComponentDefinition.getGUID() != null))
        {
            String solutionComponentQualifiedName = solutionComponentDefinition.getQualifiedName();

            archiveHelper.setGUID(solutionComponentQualifiedName, deployedImplementationType.getSolutionComponentGUID());
            archiveHelper.addSolutionComponent(solutionComponentDefinition.getTypeName(),
                                               solutionComponentQualifiedName,
                                               solutionComponentDefinition.getIdentifier(),
                                               solutionComponentDefinition.getDisplayName(),
                                               solutionComponentDefinition.getDescription(),
                                               versionName,
                                               solutionComponentDefinition.getComponentType(),
                                               solutionComponentDefinition.getImplementationType(),
                                               solutionComponentDefinition.getURL(),
                                               null,
                                               null);

            archiveHelper.addMoreInformationLink(deployedImplementationType.getGUID(),
                                                 deployedImplementationType.getSolutionComponentGUID());

            if (solutionComponentDefinition.getSubComponents() != null)
            {
                for (SolutionComponentDefinition subComponent : solutionComponentDefinition.getSubComponents())
                {
                    if (subComponent != null)
                    {
                        archiveHelper.addSolutionCompositionRelationship(deployedImplementationType.getSolutionComponentGUID(),
                                                                         subComponent.getGUID());
                    }
                }
            }
        }

        return validValueGUID;
    }


    /**
     * Add the description of the solutions for each content pack.  There should be
     * at least one solution blueprint in each content pack, used to describe its content.
     *
     * @param contentPackDefinition which content pack?
     * @param additionalComponentGUIDs additional components to add to the solution blueprint
     */
    protected void addSolutionBlueprints(ContentPackDefinition contentPackDefinition,
                                         List<String>          additionalComponentGUIDs)
    {
        for (SolutionBlueprint solutionBlueprint : SolutionBlueprint.values())
        {
            if (solutionBlueprint.getContentPackDefinition() == contentPackDefinition)
            {
                archiveHelper.setGUID(solutionBlueprint.getSolutionBlueprintQualifiedName(), solutionBlueprint.getSolutionBlueprintGUID());

                String solutionBlueprintGUID = archiveHelper.addSolutionBlueprint(OpenMetadataType.SOLUTION_BLUEPRINT.typeName,
                                                                                  solutionBlueprint.getSolutionBlueprintQualifiedName(),
                                                                                  solutionBlueprint.getSolutionBlueprintDisplayName(),
                                                                                  solutionBlueprint.getSolutionBlueprintIdentifier(),
                                                                                  solutionBlueprint.getDescription(),
                                                                                  versionName,
                                                                                  null,
                                                                                  null);

                assert(solutionBlueprint.getSolutionBlueprintGUID().equals(solutionBlueprintGUID));

                if (additionalComponentGUIDs != null)
                {
                    for (String solutionComponentGUID : additionalComponentGUIDs)
                    {
                        archiveHelper.addMemberToCollection(solutionBlueprint.getSolutionBlueprintGUID(),
                                                            solutionComponentGUID,
                                                            "additional component");
                    }
                }

                if (solutionBlueprint.getExtraSolutionComponentGUIDs() != null)
                {
                    for (String solutionComponentGUID : solutionBlueprint.getExtraSolutionComponentGUIDs())
                    {
                        archiveHelper.addMemberToCollection(solutionBlueprint.getSolutionBlueprintGUID(),
                                                            solutionComponentGUID,
                                                            "related component");
                    }
                }

                /*
                 * The solution may include the deployed implementation types (technology types) that it supports.
                 */
                if (solutionBlueprint.getDeployedImplementationTypes() != null)
                {
                    for (DeployedImplementationTypeDefinition deployedImplementationType : solutionBlueprint.getDeployedImplementationTypes())
                    {
                        if (deployedImplementationType.getSolutionComponentGUID() == null)
                        {
                            System.out.println("WARNING: No solution component GUID for " + deployedImplementationType.getDeployedImplementationType());
                            continue;
                        }

                        archiveHelper.addMemberToCollection(solutionBlueprint.getSolutionBlueprintGUID(),
                                                            deployedImplementationType.getSolutionComponentGUID(),
                                                            "interacting with");
                    }
                }

                /*
                 * Add the integration connectors included in the content pack
                 */
                for (IntegrationConnectorDefinition integrationConnectorDefinition : IntegrationConnectorDefinition.values())
                {
                    if (contentPackDefinition.equals(integrationConnectorDefinition.getContentPackDefinition()))
                    {
                        archiveHelper.addMemberToCollection(solutionBlueprint.getSolutionBlueprintGUID(),
                                                            integrationConnectorDefinition.getSolutionComponentGUID(),
                                                            "member of");
                    }
                }

                /*
                 * Add the governance action types included in the content pack
                 */
                for (RequestTypeDefinition requestTypeDefinition : RequestTypeDefinition.values())
                {
                    if (contentPackDefinition.equals(requestTypeDefinition.getContentPackDefinition()))
                    {
                        archiveHelper.addMemberToCollection(solutionBlueprint.getSolutionBlueprintGUID(),
                                                            requestTypeDefinition.getSolutionComponentGUID(),
                                                            "member of");
                    }
                }
            }
        }
    }


    /**
     * Add the additional wires to the solution components.
     * The solution components are added with the implementing components.
     *
     * @param contentPackDefinition which content pack?
     */
    protected void addSolutionLinkingWires(ContentPackDefinition contentPackDefinition)
    {
        for (SolutionLinkingWire solutionLinkingWire : SolutionLinkingWire.values())
        {
            if (solutionLinkingWire.getContentPackDefinition().equals(contentPackDefinition))
            {
                archiveHelper.addSolutionLinkingWireRelationship(solutionLinkingWire.getEnd1GUID(),
                                                                 solutionLinkingWire.getEnd2GUID(),
                                                                 solutionLinkingWire.getLabel(),
                                                                 solutionLinkingWire.getDescription(),
                                                                 null);
            }
        }
    }


    /**
     * Loop through the integration group definitions creating the specified definitions for the names content pack.
     *
     * @param contentPackDefinition which content pack are these templates for?
     */
    protected void addIntegrationGroups(ContentPackDefinition contentPackDefinition)
    {
        for (IntegrationGroupDefinition integrationGroupDefinition : IntegrationGroupDefinition.values())
        {
            if (integrationGroupDefinition.getContentPackDefinition() == contentPackDefinition)
            {
                archiveHelper.setGUID(integrationGroupDefinition.getQualifiedName(), integrationGroupDefinition.getGUID());

                String integrationGroupGUID = archiveHelper.addIntegrationGroup(integrationGroupDefinition.getQualifiedName(),
                                                                                integrationGroupDefinition.getName(),
                                                                                integrationGroupDefinition.getDescription(),
                                                                                versionName,
                                                                                null,
                                                                                contentPackDefinition.getArchiveFileName(),
                                                                                null,
                                                                                null);

                assert(integrationGroupDefinition.getGUID().equals(integrationGroupGUID));
            }
        }
    }


    /**
     * Add the integration connectors for the content pack.
     *
     * @param contentPackDefinition content pack being processed
     * @param integrationGroupDefinition integration group
     */
    protected void addIntegrationConnectors(ContentPackDefinition      contentPackDefinition,
                                            IntegrationGroupDefinition integrationGroupDefinition)
    {
        for (IntegrationConnectorDefinition integrationConnectorDefinition : IntegrationConnectorDefinition.values())
        {
            if (contentPackDefinition.equals(integrationConnectorDefinition.getContentPackDefinition()))
            {
                String connectorQualifiedName = integrationConnectorDefinition.getQualifiedName(integrationGroupDefinition.getQualifiedName());
                archiveHelper.setGUID(connectorQualifiedName, integrationConnectorDefinition.getGUID());

                String guid = archiveHelper.addIntegrationConnector(integrationConnectorDefinition.getConnectorProviderClassName(),
                                                                    integrationConnectorDefinition.getConfigurationProperties(),
                                                                    integrationConnectorDefinition.getQualifiedName(integrationGroupDefinition.getQualifiedName()),
                                                                    integrationConnectorDefinition.getDisplayName(),
                                                                    integrationConnectorDefinition.getDescription(),
                                                                    integrationConnectorDefinition.getConnectorUserId(),
                                                                    integrationConnectorDefinition.getEndpointAddress(),
                                                                    integrationConnectorDefinition.getSecretsCollectionName(),
                                                                    integrationConnectorDefinition.getSecretsStorePurpose(),
                                                                    integrationConnectorDefinition.getSecretsStoreConnectorTypeGUID(),
                                                                    integrationConnectorDefinition.getSecretsStoreFileName(),
                                                                    null);

                assert (integrationConnectorDefinition.getGUID().equals(guid));

                archiveHelper.addRegisteredIntegrationConnector(integrationGroupDefinition.getGUID(),
                                                                integrationConnectorDefinition.getConnectorName(),
                                                                integrationConnectorDefinition.getConnectorUserId(),
                                                                integrationConnectorDefinition.getMetadataSourceQualifiedName(),
                                                                integrationConnectorDefinition.getMetadataCollectionQualifiedName(),
                                                                integrationConnectorDefinition.getRefreshTimeInterval(),
                                                                true,
                                                                integrationConnectorDefinition.getGUID());

                /*
                 * This IT profile allows us to collect karma points for the connector.  This is in addition to their
                 * connector activity reports that are created automatically.
                 */
                archiveHelper.addITProfile(integrationConnectorDefinition.getGUID(),
                                           integrationConnectorDefinition.getConnectorUserId(),
                                           integrationConnectorDefinition.getQualifiedName(integrationGroupDefinition.getQualifiedName()) + ":ActorProfile",
                                           integrationConnectorDefinition.getConnectorName(),
                                           integrationConnectorDefinition.getDescription(),
                                           null);

                /*
                 * The deployed connector is linked as a resource to each of its supported technology types.
                 */
                if (integrationConnectorDefinition.getDeployedImplementationTypes() != null)
                {
                    for (DeployedImplementationTypeDefinition deployedImplementationType : integrationConnectorDefinition.getDeployedImplementationTypes())
                    {
                        archiveHelper.addResourceListRelationshipByGUID(deployedImplementationType.getGUID(),
                                                                        integrationConnectorDefinition.getGUID(),
                                                                        integrationConnectorDefinition.getResourceUse().getResourceUse(),
                                                                        integrationConnectorDefinition.getResourceUse().getDescription());
                    }
                }

                if (integrationConnectorDefinition.getSolutionComponentGUID() != null)
                {
                    /*
                     * The solution component for the connector links to the deployed connector - as its implementation
                     * - and to each of the valid values for its supported technologies.
                     */
                    String qualifiedName = OpenMetadataType.SOLUTION_COMPONENT.typeName + "::" + connectorQualifiedName;

                    archiveHelper.setGUID(qualifiedName, integrationConnectorDefinition.getSolutionComponentGUID());

                    archiveHelper.addSolutionComponent(OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                       qualifiedName,
                                                       integrationConnectorDefinition.getConnectorName(),
                                                       integrationConnectorDefinition.getSolutionComponentName(),
                                                       integrationConnectorDefinition.getSolutionComponentDescription(),
                                                       versionName,
                                                       SolutionComponentType.LONG_RUNNING_DAEMON.getSolutionComponentType(),
                                                       DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                                                       "https://egeria-project.org/concepts/integration-connector/",
                                                       null,
                                                       null);

                    archiveHelper.addImplementedByRelationship(integrationConnectorDefinition.getSolutionComponentGUID(),
                                                               integrationConnectorDefinition.getGUID(),
                                                               null,
                                                               null,
                                                               null,
                                                               null);

                    if (integrationConnectorDefinition.linkToMetadataServerSolutionComponent())
                    {
                        archiveHelper.addSolutionLinkingWireRelationship(integrationConnectorDefinition.getSolutionComponentGUID(),
                                                                         EgeriaDeployedImplementationType.METADATA_ACCESS_STORE.getSolutionComponentGUID(),
                                                                         "metadata",
                                                                         "Querying and maintaining open metadata.",
                                                                         null);
                    }

                    if (integrationConnectorDefinition.getDeployedImplementationTypes() != null)
                    {
                        for (DeployedImplementationTypeDefinition deployedImplementationType : integrationConnectorDefinition.getDeployedImplementationTypes())
                        {
                            if (deployedImplementationType.getSolutionComponentGUID() != null)
                            {
                                archiveHelper.addSolutionLinkingWireRelationship(integrationConnectorDefinition.getSolutionComponentGUID(),
                                                                                 deployedImplementationType.getSolutionComponentGUID(),
                                                                                 integrationConnectorDefinition.getResourceUse().getResourceUse(),
                                                                                 integrationConnectorDefinition.getResourceUse().getDescription(),
                                                                                 null);
                            }
                            else
                            {
                                System.out.println("WARNING: No solution component GUID for " + deployedImplementationType.getDeployedImplementationType());
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Loop through the engine action definitions creating the specified engine action.
     */
    protected void addEngineActions(ContentPackDefinition contentPackDefinition)
    {
        for (EngineActionDefinition actionDefinition : EngineActionDefinition.values())
        {
            if (contentPackDefinition.equals(actionDefinition.getContentPackDefinition()))
            {
                archiveHelper.setGUID(actionDefinition.getQualifiedName(),
                                      actionDefinition.getGUID());

                archiveHelper.addEngineAction(actionDefinition.getQualifiedName(),
                                              actionDefinition.getDomainIdentifier(),
                                              actionDefinition.getDisplayName(),
                                              actionDefinition.getDescription(),
                                              new Date(),
                                              actionDefinition.getActivityStatus(),
                                              actionDefinition.getGovernanceEngineGUID(),
                                              actionDefinition.getGovernanceEngineName(),
                                              actionDefinition.getRequesterUserId(),
                                              actionDefinition.getRequestType(),
                                              actionDefinition.getRequestParameters());
            }
        }
    }



    /**
     * Loop through the lovelave service definitions linking them as catalog targets to the
     * Babbage Analytical Engine.
     */
    protected void addLovelaceCatalogTargets(ContentPackDefinition contentPackDefinition)
    {
        for (LovelaceServiceDefinition lovelaceServiceDefinition : LovelaceServiceDefinition.values())
        {
            if (contentPackDefinition.equals(lovelaceServiceDefinition.getContentPackDefinition()))
            {
                archiveHelper.addCatalogTargetRelationship(IntegrationConnectorDefinition.BABBAGE_ANALYTICAL_ENGINE.getGUID(),
                                                           lovelaceServiceDefinition.getCatalogTargetGUID(),
                                                           lovelaceServiceDefinition.getCatalogTargetName(),
                                                           lovelaceServiceDefinition.getCatalogTargetName(),
                                                           null,
                                                           null,
                                                           null,
                                                           null);
            }
        }
    }



    /**
     * Create the default governance engines
     *
     * @param contentPackDefinition content pack being processed
     */
    protected void createGovernanceEngines(ContentPackDefinition contentPackDefinition)
    {
        for (GovernanceEngineDefinition governanceEngineDefinition : GovernanceEngineDefinition.values())
        {
            this.createGovernanceEngine(governanceEngineDefinition, contentPackDefinition);
        }
    }


    /**
     * Create an entity that represents a governance engine.
     *
     * @param governanceEngineDefinition details of the governance engine
     * @param contentPackDefinition content pack being processed
     */
    private void createGovernanceEngine(GovernanceEngineDefinition governanceEngineDefinition,
                                        ContentPackDefinition      contentPackDefinition)
    {
        if (contentPackDefinition.equals(governanceEngineDefinition.getContentPackDefinition()))
        {
            archiveHelper.setGUID(governanceEngineDefinition.getName(),
                                  governanceEngineDefinition.getGUID());

            archiveHelper.addGovernanceEngine(governanceEngineDefinition.getType(),
                                              governanceEngineDefinition.getName(),
                                              governanceEngineDefinition.getDisplayName(),
                                              governanceEngineDefinition.getDescription(),
                                              null,
                                              null,
                                              null,
                                              null,
                                              governanceEngineDefinition.getUserId(),
                                              null,
                                              null);
        }
    }


    /**
     * Register the governance services that are going to be in the default governance engines.
     *
     * @param contentPackDefinition content pack being processed
     */
    protected void createGovernanceServices(ContentPackDefinition contentPackDefinition)
    {
        for (GovernanceServiceDefinition governanceServiceDefinition : GovernanceServiceDefinition.values())
        {
            this.addGovernanceServiceDefinition(governanceServiceDefinition, contentPackDefinition);
        }
    }


    /**
     * Add entities for each governance action service.
     *
     * @param governanceServiceDefinition details of governance service
     */
    private void addGovernanceServiceDefinition(GovernanceServiceDefinition governanceServiceDefinition,
                                                ContentPackDefinition       contentPackDefinition)
    {
        if (contentPackDefinition.equals(governanceServiceDefinition.getContentPackDefinition()))
        {
            archiveHelper.setGUID(governanceServiceDefinition.getName(), governanceServiceDefinition.getGUID());

            archiveHelper.addGovernanceService(governanceServiceDefinition.getOpenMetadataTypeName(),
                                               governanceServiceDefinition.getDeployedImplementationType(),
                                               governanceServiceDefinition.getConnectorProviderClassName(),
                                               null,
                                               governanceServiceDefinition.getName(),
                                               governanceServiceDefinition.getDisplayName(),
                                               governanceServiceDefinition.getDescription(),
                                               null);
        }
    }


    /**
     * Add the request types.
     *
     * @param contentPackDefinition content pack to fill
     */
    protected void createRequestTypes(ContentPackDefinition contentPackDefinition)
    {
        for (RequestTypeDefinition requestTypeDefinition : RequestTypeDefinition.values())
        {
            if (contentPackDefinition.equals(requestTypeDefinition.getContentPackDefinition()))
            {
                this.addRequestType(requestTypeDefinition.getGovernanceEngine().getGUID(),
                                    requestTypeDefinition.getGovernanceEngine().getName(),
                                    requestTypeDefinition.getGovernanceEngine().getType(),
                                    requestTypeDefinition.getGovernanceRequestType(),
                                    requestTypeDefinition.getServiceRequestType(),
                                    requestTypeDefinition.getRequestParameters(),
                                    requestTypeDefinition.getActionTargets(),
                                    requestTypeDefinition.getGovernanceService().getGovernanceActionDescription(),
                                    requestTypeDefinition.getGovernanceActionTypeGUID(),
                                    requestTypeDefinition.getSupportedElementQualifiedName(),
                                    requestTypeDefinition.getSolutionComponentGUID(),
                                    requestTypeDefinition.getSolutionComponentName(),
                                    requestTypeDefinition.getSolutionComponentDescription(),
                                    requestTypeDefinition.getConfiguresComponentGUID(),
                                    requestTypeDefinition.linkToMetadataServerSolutionComponent(),
                                    requestTypeDefinition.getWorksWithTechnology());
            }
        }
    }


    /**
     * Add details of a request type to the engine.
     *
     * @param governanceEngineGUID unique identifier of the engine
     * @param governanceEngineName name of the governance engine
     * @param governanceEngineTypeName type of engine
     * @param governanceRequestType name of request type
     * @param serviceRequestType internal name of the request type
     * @param requestParameters any request parameters
     * @param actionTargets action targets
     * @param governanceActionDescription description of the governance action, if any
     * @param governanceActionTypeGUID unique identifier of the associated governance action type
     * @param supportedElementQualifiedName technology to link the governance action type to with ResourceList
     * @param solutionComponentGUID unique identifier for the solution component
     * @param solutionComponentName name of the solution component
     * @param solutionComponentDescription description for the solution components
     * @param worksWithTechnology technology type to link the solution component to
     * @param configuresComponentGUID optional component that it configures
     * @param linkToMetadataServerSolutionComponent should this component link to the open metadata server solution component?
     */
    protected void addRequestType(String                               governanceEngineGUID,
                                  String                               governanceEngineName,
                                  String                               governanceEngineTypeName,
                                  String                               governanceRequestType,
                                  String                               serviceRequestType,
                                  Map<String, String>                  requestParameters,
                                  List<NewActionTarget>                actionTargets,
                                  GovernanceActionDescription          governanceActionDescription,
                                  String                               governanceActionTypeGUID,
                                  String                               supportedElementQualifiedName,
                                  String                               solutionComponentGUID,
                                  String                               solutionComponentName,
                                  String                               solutionComponentDescription,
                                  String                               configuresComponentGUID,
                                  boolean                              linkToMetadataServerSolutionComponent,
                                  DeployedImplementationTypeDefinition worksWithTechnology)
    {
        archiveHelper.addSupportedGovernanceService(governanceEngineGUID,
                                                    governanceRequestType,
                                                    serviceRequestType,
                                                    requestParameters,
                                                    true,
                                                    governanceActionDescription.governanceServiceGUID);

        this.addGovernanceActionType(governanceEngineGUID,
                                     governanceEngineName,
                                     governanceEngineTypeName,
                                     governanceRequestType,
                                     requestParameters,
                                     actionTargets,
                                     governanceActionDescription,
                                     governanceActionTypeGUID,
                                     supportedElementQualifiedName);


        /*
         * Create a report type and link it to the governance action type.
         */
        String reportTypeGUID = null;
        if (governanceActionDescription.supportedAnnotationTypes != null)
        {
            String qualifiedName = governanceEngineName + "::" + governanceRequestType;
            String displayName = governanceRequestType + " (" + governanceEngineName + ")";

            reportTypeGUID = archiveHelper.addCollection(OpenMetadataType.REPORT_TYPE.typeName,
                                                         governanceActionTypeGUID,
                                                         OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName,
                                                         OpenMetadataType.AUTHORED_REFERENCEABLE.typeName,
                                                         null,
                                                         null,
                                                         qualifiedName + "::" + OpenMetadataType.REPORT_TYPE.typeName,
                                                         "Survey report produced by " + displayName,
                                                         governanceActionDescription.governanceServiceDescription,
                                                         null,
                                                         null,
                                                         null,
                                                         null);

            archiveHelper.addImplementedByRelationship(reportTypeGUID, governanceActionTypeGUID, null, null, null, null);

            for (AnnotationTypeType annotationTypeType : governanceActionDescription.supportedAnnotationTypes)
            {
                if (annotationTypeType != null)
                {
                    String annotationTypeGUID = archiveHelper.getGUID(constructValidValueQualifiedName(null,
                                                                                                       OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                                                                       null,
                                                                                                       annotationTypeType.getName()));

                    if (annotationTypeGUID != null)
                    {
                        archiveHelper.addMemberToCollection(reportTypeGUID, annotationTypeGUID, null);
                    }
                    else
                    {
                        System.out.println("WARNING: No annotation type GUID for " + annotationTypeType.getName());
                    }
                }
            }
        }

        if (solutionComponentGUID != null)
        {
            String qualifiedName = OpenMetadataType.SOLUTION_COMPONENT.typeName + "::" + OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName + "::" + governanceActionTypeGUID;

            archiveHelper.setGUID(qualifiedName, solutionComponentGUID);

            archiveHelper.addSolutionComponent(OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                               qualifiedName,
                                               governanceRequestType,
                                               solutionComponentName,
                                               solutionComponentDescription,
                                               versionName,
                                               SolutionComponentType.AUTOMATED_ACTION.getSolutionComponentType(),
                                               DeployedImplementationType.GOVERNANCE_ACTION_TYPE.getDeployedImplementationType(),
                                               "https://egeria-project.org/concepts/governance-action-type/",
                                               null,
                                               null);

            if (linkToMetadataServerSolutionComponent)
            {
                archiveHelper.addSolutionLinkingWireRelationship(solutionComponentGUID,
                                                                 EgeriaDeployedImplementationType.METADATA_ACCESS_STORE.getSolutionComponentGUID(),
                                                                 "metadata",
                                                                 "Querying and maintaining open metadata.",
                                                                 null);
            }

            if (configuresComponentGUID != null)
            {
                archiveHelper.addSolutionLinkingWireRelationship(solutionComponentGUID,
                                                                 configuresComponentGUID,
                                                                 "configures",
                                                                 "Creates a link between the supplied element and this component to enable it to work on the element.",
                                                                 null);
            }

            if (worksWithTechnology != null)
            {
                if (worksWithTechnology.getSolutionComponentGUID() != null)
                {
                    archiveHelper.addSolutionLinkingWireRelationship(solutionComponentGUID,
                                                                     worksWithTechnology.getSolutionComponentGUID(),
                                                                     "works with",
                                                                     "interacts with technology",
                                                                     null);
                }
                else
                {
                    System.out.println("WARNING: No solution component GUID for " + worksWithTechnology.getDeployedImplementationType());
                }
            }

            archiveHelper.addImplementedByRelationship(solutionComponentGUID,
                                                       governanceActionTypeGUID,
                                                       null,
                                                       null,
                                                       null,
                                                       null);

            if (reportTypeGUID != null)
            {
                archiveHelper.addSolutionLinkingWireRelationship(solutionComponentGUID,
                                                                 reportTypeGUID,
                                                                 "creates report",
                                                                 "Reports are created that follow the specification laid out by the report type.",
                                                                 null);
            }
        }
    }


    /**
     * Add details of a request type to the engine.
     *
     * @param governanceEngineGUID unique identifier of the engine
     * @param governanceEngineName name of the governance engine
     * @param governanceEngineTypeName type of engine
     * @param governanceRequestType name of request type
     * @param requestParameters any request parameters
     * @param actionTargets action targets
     * @param governanceActionDescription description of the governance action if any
     * @param governanceActionTypeGUID unique identifier of the associated governance action type
     * @param supportedElementQualifiedName element to link the governance action type to
     */
    protected void addGovernanceActionType(String                      governanceEngineGUID,
                                           String                      governanceEngineName,
                                           String                      governanceEngineTypeName,
                                           String                      governanceRequestType,
                                           Map<String, String>         requestParameters,
                                           List<NewActionTarget>       actionTargets,
                                           GovernanceActionDescription governanceActionDescription,
                                           String                      governanceActionTypeGUID,
                                           String                      supportedElementQualifiedName)
    {
        String governanceActionTypeQualifiedName = governanceEngineName + "::" + governanceRequestType;

        archiveHelper.setGUID(governanceActionTypeQualifiedName, governanceActionTypeGUID);

        String qualifiedName = governanceEngineName + "::" + governanceRequestType;
        String displayName = governanceRequestType + " (" + governanceEngineName + ")";

        String guid = archiveHelper.addGovernanceActionType(null,
                                                            governanceEngineGUID,
                                                            governanceEngineTypeName,
                                                            OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                            null,
                                                            qualifiedName,
                                                            displayName,
                                                            governanceActionDescription.governanceServiceDescription,
                                                            0,
                                                            governanceActionDescription.supportedRequestParameters,
                                                            governanceActionDescription.supportedActionTargets,
                                                            governanceActionDescription.supportedAnalysisSteps,
                                                            governanceActionDescription.supportedAnnotationTypes,
                                                            governanceActionDescription.producedRequestParameters,
                                                            governanceActionDescription.producedActionTargets,
                                                            governanceActionDescription.producedGuards,
                                                            0,
                                                            null,
                                                            null,
                                                            null);

        assert(governanceActionTypeGUID.equals(guid));

        archiveHelper.addGovernanceActionExecutor(governanceActionTypeGUID,
                                                  governanceRequestType,
                                                  requestParameters,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  governanceEngineGUID);

        if (actionTargets != null)
        {
            for (NewActionTarget actionTarget : actionTargets)
            {
                if (actionTarget != null)
                {
                    archiveHelper.addTargetForActionType(governanceActionTypeGUID, actionTarget);
                }
            }
        }

        if (supportedElementQualifiedName != null)
        {
            String supportedElementGUID = archiveHelper.queryGUID(supportedElementQualifiedName);
            archiveHelper.addResourceListRelationshipByGUID(supportedElementGUID,
                                                            governanceActionTypeGUID,
                                                            governanceActionDescription.resourceUse.getResourceUse(),
                                                            governanceActionDescription.governanceServiceDescription,
                                                            requestParameters);

        }
    }


    /**
     * Create a three-step governance action process that creates a metadata element for a particular type of asset
     * and then runs a survey against the asset's resource and creates a report.
     *
     * @param assetType name for the asset type (no spaces)
     * @param technologyType value for the deployed implementation type
     * @param url link to useful information about the survey process
     * @param createRequestType request type used to create the server's metadata element
     * @param createTemplate details of the template to use
     * @param surveyRequestType request type to run the survey
     * @return unique identifier for the solution component for the new governance action process
     */
    protected String createAndSurveyServerGovernanceActionProcess(String                               assetType,
                                                                  DeployedImplementationTypeDefinition technologyType,
                                                                  String                               url,
                                                                  RequestTypeDefinition                createRequestType,
                                                                  TemplateDefinition                   createTemplate,
                                                                  RequestTypeDefinition                surveyRequestType)
    {
        String description = "Create a " + technologyType.getDeployedImplementationType() + ", run a survey against it, and print out the resulting report.";

        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                      assetType + ":CreateAndSurveyGovernanceActionProcess",
                                                                      assetType + ":CreateAndSurvey",
                                                                      null,
                                                                      description,
                                                                      url,
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

        archiveHelper.addSupportedRequestParameters(processGUID,
                                                    OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                    processGUID,
                                                    OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                    OpenMetadataType.ASSET.typeName,
                                                    null,
                                                    supportedRequestParameters);

        archiveHelper.addSupportedAnalysisSteps(processGUID,
                                                OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                processGUID,
                                                OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                OpenMetadataType.ASSET.typeName,
                                                null,
                                                surveyRequestType.getGovernanceService().getGovernanceActionDescription().supportedAnalysisSteps);

        archiveHelper.addProducedAnnotationTypes(processGUID,
                                                OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                 processGUID,
                                                 OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                 OpenMetadataType.ASSET.typeName,
                                                null,
                                                surveyRequestType.getGovernanceService().getGovernanceActionDescription().supportedAnnotationTypes);

        String processComponentGUID = archiveHelper.addSolutionComponent(OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                         OpenMetadataType.SOLUTION_COMPONENT.typeName + "::" + assetType + "::CreateAndSurvey",
                                                                         "CREATE-AND-SURVEY-" + assetType.toUpperCase(),
                                                                         "Create and Survey Governance Action Process for " + technologyType.getDeployedImplementationType(),
                                                                         description,
                                                                         versionName,
                                                                         SolutionComponentType.MULTI_STEP_PROCESS.getSolutionComponentType(),
                                                                         DeployedImplementationType.GOVERNANCE_ACTION_PROCESS.getDeployedImplementationType(),
                                                                         url,
                                                                         null,
                                                                         null);

        archiveHelper.addSolutionComponentActorRelationship(EgeriaRoleDefinition.OPEN_METADATA_USER.getGUID(),
                                                            processComponentGUID,
                                                            "requests survey",
                                                            "A user wishing to understand more about this technology can request that a survey is run against the server.");

        String step1GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        assetType + "::CreateAndSurvey::Step1",
                                                                        "Create the " + assetType + " entity",
                                                                        "Create the description of the " + technologyType.getDeployedImplementationType(),
                                                                        0,
                                                                        supportedRequestParameters,
                                                                        null,
                                                                        surveyRequestType.getGovernanceService().getGovernanceActionDescription().supportedAnalysisSteps,
                                                                        surveyRequestType.getGovernanceService().getGovernanceActionDescription().supportedAnnotationTypes,
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
            addStepExecutor(step1GUID, createRequestType, createRequestType.getGovernanceEngine());

            archiveHelper.addGovernanceActionProcessFlow(processGUID, null, null, step1GUID);
        }

        archiveHelper.addSolutionLinkingWireRelationship(processComponentGUID,
                                                         createRequestType.getSolutionComponentGUID(),
                                                         "step 1", "Create the asset.", null);

        String step2GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        assetType + "::CreateAndSurvey::Step2",
                                                                        "Run the survey.",
                                                                        "Create a survey report detailing the contents of the "+ technologyType.getDeployedImplementationType() + ".",
                                                                        0,
                                                                        null,
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

        if (step2GUID != null)
        {
            addStepExecutor(step2GUID, surveyRequestType, surveyRequestType.getGovernanceEngine());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, ManageAssetGuard.SET_UP_COMPLETE.getName(), false, step2GUID);
        }

        archiveHelper.addSolutionLinkingWireRelationship(processComponentGUID,
                                                         surveyRequestType.getSolutionComponentGUID(),
                                                         "step 2", "Run the survey.", null);

        String step3GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        assetType + "::CreateAndSurvey::Step3",
                                                                        "Print the survey report.",
                                                                        "Print a survey report detailing the contents of the " + technologyType.getDeployedImplementationType() + ".",
                                                                        0,
                                                                        null,
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

        if (step3GUID != null)
        {
            addStepExecutor(step3GUID, RequestTypeDefinition.PRINT_SURVEY_REPORT, GovernanceEngineDefinition.STEWARDSHIP_ENGINE);

            archiveHelper.addNextGovernanceActionProcessStep(step2GUID, SurveyActionGuard.SURVEY_COMPLETED.getName(), false, step3GUID);
        }

        archiveHelper.addSolutionLinkingWireRelationship(processComponentGUID,
                                                         RequestTypeDefinition.PRINT_SURVEY_REPORT.getSolutionComponentGUID(),
                                                         "step 3", "Print the survey report.", null);


        if (technologyType.getGUID() != null)
        {
            archiveHelper.addResourceListRelationshipByGUID(technologyType.getGUID(),
                                                            processGUID,
                                                            ResourceUse.SURVEY_RESOURCE.getResourceUse(),
                                                            description,
                                                            createRequestType.getRequestParameters());
        }
        else
        {
            System.out.println("WARNING: No GUID found for " + technologyType.getDeployedImplementationType());
        }

        return processComponentGUID;
    }


    /**
     * Convert a list of placeholder variables into a list request parameters - to create the specification for a
     * governance action process or governance action type.
     *
     * @param placeholderPropertyTypes placeholder properties used by the template
     * @return list of request parameter definitions
     */
    protected List<RequestParameterType> getRequestTypeDefinition(List<PlaceholderPropertyType> placeholderPropertyTypes)
    {
        if (placeholderPropertyTypes != null)
        {
            List<RequestParameterType> requestParameterTypes = new ArrayList<>();

            for (PlaceholderPropertyType placeholderPropertyType : placeholderPropertyTypes)
            {
                if (placeholderPropertyType != null)
                {
                    RequestParameterType requestParameterType = new RequestParameterType();

                    requestParameterType.setName(placeholderPropertyType.getName());
                    requestParameterType.setDataType(placeholderPropertyType.getDataType());
                    requestParameterType.setDescription(placeholderPropertyType.getDescription());
                    requestParameterType.setExample(placeholderPropertyType.getExample());
                    requestParameterType.setRequired(placeholderPropertyType.getRequired());
                    requestParameterType.setOtherPropertyValues(placeholderPropertyType.getOtherPropertyValues());

                    requestParameterTypes.add(requestParameterType);
                }
            }

            return requestParameterTypes;
        }

        return null;
    }

    /**
     * Create a two-step governance action process that creates a metadata element for a particular type of server
     * and then adds it as a catalog target for an appropriate integration connector.
     *
     * @param serverType name for the server type (no spaces)
     * @param technologyType value for the deployed implementation type
     * @param url link to useful information about the survey process
     * @param createRequestType request type used to create the server's metadata element
     * @param createTemplate details of the template to use
     * @param catalogRequestType request type to run the survey
     * @return unique identifier for the solution component for the new governance action process
     */
    protected String createAndCatalogServerGovernanceActionProcess(String                               serverType,
                                                                   DeployedImplementationTypeDefinition technologyType,
                                                                   String                               url,
                                                                   RequestTypeDefinition                createRequestType,
                                                                   TemplateDefinition                   createTemplate,
                                                                   RequestTypeDefinition                catalogRequestType)
    {
        return createAsCatalogTargetGovernanceActionProcess(serverType,
                                                            technologyType,
                                                            "catalog",
                                                            url,
                                                            createRequestType,
                                                            createTemplate,
                                                            catalogRequestType);
    }


    /**
     * Create a two-step governance action process that creates a metadata element for a particular type of asset
     * and then adds it as a catalog target for an appropriate integration connector.
     *
     * @param assetType name for the asset type (no spaces)
     * @param technologyType value for the deployed implementation type
     * @param url link to useful information about the survey process
     * @param createRequestType request type used to create the server's metadata element
     * @param createTemplate details of the template to use
     * @param catalogRequestType request type to run the survey
     * @return unique identifier for the solution component for the new governance action process
     */
    protected String createAndCatalogAssetGovernanceActionProcess(String                               assetType,
                                                                  DeployedImplementationTypeDefinition technologyType,
                                                                  String                               url,
                                                                  RequestTypeDefinition                createRequestType,
                                                                  TemplateDefinition                   createTemplate,
                                                                  RequestTypeDefinition                catalogRequestType)
    {
        return createAsCatalogTargetGovernanceActionProcess(assetType,
                                                            technologyType,
                                                            "catalog",
                                                            url,
                                                            createRequestType,
                                                            createTemplate,
                                                            catalogRequestType);
    }


    /**
     * Create a two-step governance action process that creates a metadata element for a particular type of asset
     * and then adds it as a catalog target for an appropriate integration connector.
     *
     * @param assetType name for the asset type (no spaces)
     * @param technologyType value for the deployed implementation type
     * @param url link to useful information about the survey process
     * @param createRequestType request type used to create the server's metadata element
     * @param createTemplate details of the template to use
     * @param catalogRequestType request type to run the survey
     * @return unique identifier for the solution component for the new governance action process
     */
    protected String createAndHarvestToAssetGovernanceActionProcess(String                               assetType,
                                                                    DeployedImplementationTypeDefinition technologyType,
                                                                    String                               url,
                                                                    RequestTypeDefinition                createRequestType,
                                                                    TemplateDefinition                   createTemplate,
                                                                    RequestTypeDefinition                catalogRequestType)
    {
        return createAsCatalogTargetGovernanceActionProcess(assetType,
                                                            technologyType,
                                                            "harvest",
                                                            url,
                                                            createRequestType,
                                                            createTemplate,
                                                            catalogRequestType);
    }



    /**
     * Create a two-step governance action process that creates a metadata element for a particular type of asset
     * and then adds it as a catalog target for an appropriate integration connector.
     *
     * @param assetType name for the server type (no spaces)
     * @param technologyType value for the deployed implementation type
     * @param actionName nme to use for the action
     * @param url link to useful information about the survey process
     * @param createRequestType request type used to create the server's metadata element
     * @param createTemplate details of the template to use
     * @param catalogRequestType request type to run the survey
     * @return unique identifier for the solution component for the new governance action process
     */
    protected String createAsCatalogTargetGovernanceActionProcess(String                               assetType,
                                                                  DeployedImplementationTypeDefinition technologyType,
                                                                  String                               actionName,
                                                                  String                               url,
                                                                  RequestTypeDefinition                createRequestType,
                                                                  TemplateDefinition                   createTemplate,
                                                                  RequestTypeDefinition                catalogRequestType)
    {
        String description = "Create a " + technologyType.getDeployedImplementationType() + " and configure an integration connector to " + actionName + " its contents.";

        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                      assetType + "::CreateAsCatalogTargetGovernanceActionProcess",
                                                                      assetType + "::CreateAsCatalogTarget",
                                                                      null,
                                                                      description,
                                                                      url,
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

        archiveHelper.addSupportedRequestParameters(processGUID,
                                                    OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                    processGUID,
                                                    OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                    OpenMetadataType.ASSET.typeName,
                                                    null,
                                                    supportedRequestParameters);

        String processComponentGUID = archiveHelper.addSolutionComponent(OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                         OpenMetadataType.SOLUTION_COMPONENT.typeName + "::" + assetType + "::CreateAsCatalogTarget",
                                                                         "CREATE-AND-" + actionName.toUpperCase() + "-" + assetType.toUpperCase(),
                                                                         "Create and " + actionName + " Governance Action Process for " + technologyType.getDeployedImplementationType(),
                                                                         description,
                                                                         versionName,
                                                                         SolutionComponentType.MULTI_STEP_PROCESS.getSolutionComponentType(),
                                                                         DeployedImplementationType.GOVERNANCE_ACTION_PROCESS.getDeployedImplementationType(),
                                                                         url,
                                                                         null,
                                                                         null);

        archiveHelper.addSolutionComponentActorRelationship(EgeriaRoleDefinition.OPEN_METADATA_USER.getGUID(),
                                                            processComponentGUID,
                                                            "requests " + actionName,
                                                            "A user wishing to " + actionName + " a technology can request that it be " + actionName + "ed by linking it to the appropriate integration connector.");

        String step1GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        assetType + "::CreateAsCatalogTarget::Step1",
                                                                        "Create the " + technologyType.getAssociatedTypeName() + " entity",
                                                                        "Create the description of the " + technologyType.getDeployedImplementationType(),
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
            addStepExecutor(step1GUID, createRequestType, createRequestType.getGovernanceEngine());

            archiveHelper.addGovernanceActionProcessFlow(processGUID, null, null, step1GUID);
        }

        archiveHelper.addSolutionLinkingWireRelationship(processComponentGUID,
                                                         createRequestType.getSolutionComponentGUID(),
                                                         "step 1", "Create the asset.", null);


        String step2GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        assetType + "::CreateAsCatalogTarget::Step2",
                                                                        "Connect new asset to integration connector",
                                                                        "Connect the asset entity for the " + technologyType.getDeployedImplementationType() + " to the appropriate integration connector.",
                                                                        0,
                                                                        null,
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

        if (step2GUID != null)
        {
            addStepExecutor(step2GUID, catalogRequestType, catalogRequestType.getGovernanceEngine());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, ManageAssetGuard.SET_UP_COMPLETE.getName(), false, step2GUID);
        }

        archiveHelper.addSolutionLinkingWireRelationship(processComponentGUID,
                                                         catalogRequestType.getSolutionComponentGUID(),
                                                         "step 2", "Link as catalog target.", null);


        if (technologyType.getGUID() != null)
        {
            archiveHelper.addResourceListRelationshipByGUID(technologyType.getGUID(),
                                                            processGUID,
                                                            ResourceUse.CATALOG_RESOURCE.getResourceUse(),
                                                            description,
                                                            createRequestType.getRequestParameters());
        }
        else
        {
            System.out.println("WARNING: No GUID found for " + technologyType.getDeployedImplementationType());
        }

        return processComponentGUID;
    }


    /**
     * Create a one-step governance action process that deletes a metadata element for a particular type of asset
     * which then removes it, any anchored content, and relationships - like the catalog target for an appropriate integration connector.
     *
     * @param assetType name for the server type (no spaces)
     * @param technologyType value for the deployed implementation type
     * @param url link to useful information about the survey process
     * @param deleteRequestType request type used to delete the server's metadata element
     * @return unique identifier for the solution component for the new governance action process
     */
    protected String deleteAsCatalogTargetGovernanceActionProcess(String                               assetType,
                                                                  DeployedImplementationTypeDefinition technologyType,
                                                                  String                               url,
                                                                  RequestTypeDefinition                deleteRequestType)
    {
        String description = "Delete the asset for " + technologyType.getDeployedImplementationType() + " using the same template properties that were used to create it.  This will delete all of the metadata anchored to the asset and relationships to other entities such as the catalog target relationships.";

        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                      assetType + ":DeleteAssetWithTemplateGovernanceActionProcess",
                                                                      assetType + ":DeleteAsset",
                                                                      null,
                                                                      description,
                                                                      url,
                                                                      null,
                                                                      0,
                                                                      null,
                                                                      null,
                                                                      null);

        String processComponentGUID = archiveHelper.addSolutionComponent(OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                         OpenMetadataType.SOLUTION_COMPONENT.typeName + "::" + assetType + "::DeleteAsCatalogTarget",
                                                                         "DELETE-WITH-TEMPLATE-FOR-" + assetType.toUpperCase(),
                                                                         "Delete with Template Governance Action Process for " + technologyType.getDeployedImplementationType(),
                                                                         description,
                                                                         versionName,
                                                                         SolutionComponentType.MULTI_STEP_PROCESS.getSolutionComponentType(),
                                                                         DeployedImplementationType.GOVERNANCE_ACTION_PROCESS.getDeployedImplementationType(),
                                                                         url,
                                                                         null,
                                                                         null);

        archiveHelper.addSolutionComponentActorRelationship(EgeriaRoleDefinition.OPEN_METADATA_USER.getGUID(),
                                                            processComponentGUID,
                                                            "delete " + technologyType.getDeployedImplementationType(),
                                                            "A user wishing to delete an asset to this technology can request that a delete action is run.");

        String step1GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        assetType + "::Delete Asset::Step1",
                                                                        "Delete the " + technologyType.getAssociatedTypeName() + " entity",
                                                                        "Delete asset for " + technologyType.getDeployedImplementationType() + " using the same template properties as was used to create it.",
                                                                        0,
                                                                        null,
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
            addStepExecutor(step1GUID, deleteRequestType, deleteRequestType.getGovernanceEngine());

            archiveHelper.addGovernanceActionProcessFlow(processGUID, null, null, step1GUID);
        }

        archiveHelper.addSolutionLinkingWireRelationship(processComponentGUID,
                                                         deleteRequestType.getSolutionComponentGUID(),
                                                         "step 1", "Delete the asset using a template.", null);

        if (technologyType.getGUID() != null)
        {
            archiveHelper.addResourceListRelationshipByGUID(technologyType.getGUID(),
                                                            processGUID,
                                                            ResourceUse.SURVEY_RESOURCE.getResourceUse(),
                                                            description,
                                                            deleteRequestType.getRequestParameters());
        }
        else
        {
            System.out.println("WARNING: No GUID found for " + technologyType.getDeployedImplementationType());
        }

        return processComponentGUID;
    }


    /**
     * Create the executor relationships and attach the action targets to the step.
     *
     * @param stepGUID unique identifier of the governance action process step
     * @param requestTypeDefinition the request type being configured
     * @param governanceEngineDefinition the engine to connect it to
     */
    protected void addStepExecutor(String                     stepGUID,
                                   RequestTypeDefinition      requestTypeDefinition,
                                   GovernanceEngineDefinition governanceEngineDefinition)
    {
        archiveHelper.addGovernanceActionExecutor(stepGUID,
                                                  requestTypeDefinition.getGovernanceRequestType(),
                                                  requestTypeDefinition.getRequestParameters(),
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  governanceEngineDefinition.getGUID());

        if (requestTypeDefinition.getActionTargets() != null)
        {
            for (NewActionTarget actionTarget : requestTypeDefinition.getActionTargets())
            {
                if (actionTarget != null)
                {
                    archiveHelper.addTargetForActionType(stepGUID, actionTarget);
                }
            }
        }
    }


    /**
     * Generates and writes out an open metadata archive containing all the connector types
     * describing the Egeria project open connectors.
     */
    public void writeOpenMetadataArchive()
    {
        try
        {
            System.out.println("Writing to file: " + archiveFileName);

            super.writeOpenMetadataArchive(archiveFileName, this.getOpenMetadataArchive());
        }
        catch (Exception error)
        {
            System.out.println("error is " + error);
        }
    }
}