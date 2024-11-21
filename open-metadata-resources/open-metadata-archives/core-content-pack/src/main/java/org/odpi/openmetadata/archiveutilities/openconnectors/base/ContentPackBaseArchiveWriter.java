/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors.base;

import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.ManageAssetGuard;
import org.odpi.openmetadata.archiveutilities.openconnectors.*;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyActionGuard;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.samples.archiveutilities.EgeriaBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.GovernanceActionDescription;

import java.util.*;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * CorePackArchiveWriter creates an open metadata archive that includes the connector type
 * information for all open connectors supplied by the egeria project.
 */
public abstract class ContentPackBaseArchiveWriter extends EgeriaBaseArchiveWriter
{
    private static final Date creationDate = new Date(1639984840038L);
    protected final Map<String, String> deployedImplementationTypeQNAMEs = new HashMap<>();
    private final Map<String, String> parentValidValueQNameToGUIDMap  = new HashMap<>();


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
     * Create a template for a data file and link it to the associated open metadata type.
     * The template consists of a DataFile asset plus an optional connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param connectorTypeGUID          connector type to link to the connection
     * @param deployedImplementationType deployed implementation type to link the template to
     * @param configurationProperties    configuration properties
     */
    protected void createDataFileCatalogTemplate(DeployedImplementationType deployedImplementationType,
                                                 String                     connectorTypeGUID,
                                                 Map<String, Object>        configurationProperties)
    {
        final String methodName = "createDataFileCatalogTemplate";

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType()  + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + ":" + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder();
        String               versionIdentifier  = "V1.0";
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_TYPE.name, PlaceholderProperty.FILE_TYPE.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_EXTENSION.name, PlaceholderProperty.FILE_EXTENSION.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_NAME.name, PlaceholderProperty.FILE_NAME.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create an asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        classifications.add(archiveHelper.getDataAssetEncodingClassification(PlaceholderProperty.FILE_ENCODING.getPlaceholder(), null, null, null));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  PlaceholderProperty.FILE_NAME.getPlaceholder(),
                                                  versionIdentifier,
                                                  null,
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
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
                                                            OpenMetadataType.ASSET.typeName);

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               PlaceholderProperty.getDataFilesPlaceholderPropertyTypes());
    }


    /**
     * Create a template for a file directory and link it to the associated open metadata type.
     * The template consists of a DataFile asset plus an optional connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param deployedImplementationType info for the template
     * @param connectorTypeGUID          connector type to link to the connection
     */
    protected void createFolderCatalogTemplate(DeployedImplementationType deployedImplementationType,
                                               String                     connectorTypeGUID)
    {
        final String methodName = "createFolderCatalogTemplate";

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + ":" + PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder();
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create an asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  PlaceholderProperty.DIRECTORY_NAME.getPlaceholder(),
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
                                                            qualifiedName + ":Endpoint",
                                                            PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder() + " endpoint",
                                                            null,
                                                            PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder(),
                                                            null,
                                                            null);

            String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                                PlaceholderProperty.DIRECTORY_PATH_NAME.getPlaceholder() + " connection",
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
                                                                OpenMetadataType.ASSET.typeName);

            archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               PlaceholderProperty.getFolderPlaceholderPropertyTypes());
    }


    /**
     * Create a template for a dataset and link it to the associated open metadata type.
     * The template consists of a DataFile asset plus an optional connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param deployedImplementationType values for the template
     * @param connectorTypeGUID          connector type to link to the connection
     */
    protected void createDataSetCatalogTemplate(DeployedImplementationType deployedImplementationType,
                                                String                     qualifiedName,
                                                String                     connectorTypeGUID)
    {
        final String methodName = "createDataSetCatalogTemplate";

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create an asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        if (connectorTypeGUID != null)
        {
            String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                                PlaceholderProperty.DISPLAY_NAME.getPlaceholder() + " connection",
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                connectorTypeGUID,
                                                                null,
                                                                assetGUID,
                                                                deployedImplementationType.getAssociatedTypeName(),
                                                                OpenMetadataType.ASSET.typeName);

            archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               PlaceholderProperty.getDataSetPlaceholderPropertyTypes());
    }


    /**
     * Create a template for a software file and link it to the associated open metadata type.
     * The template consists of a DataFile asset plus an optional connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param deployedImplementationType description for the template
     * @param connectorTypeGUID          connector type to link to the connection
     */
    protected void createSoftwareFileCatalogTemplate(DeployedImplementationType deployedImplementationType,
                                                     String                     connectorTypeGUID)
    {
        final String methodName = "createSoftwareFileCatalogTemplate";

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + ":" + PlaceholderProperty.FILE_SYSTEM_NAME.getPlaceholder() + ":" + PlaceholderProperty.FILE_PATH_NAME.getPlaceholder();
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_TYPE.name, PlaceholderProperty.FILE_TYPE.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_EXTENSION.name, PlaceholderProperty.FILE_EXTENSION.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_NAME.name, PlaceholderProperty.FILE_NAME.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create an asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        classifications.add(archiveHelper.getDataAssetEncodingClassification(PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                                                                             PlaceholderProperty.PROGRAMMING_LANGUAGE.getPlaceholder(),
                                                                             null,
                                                                             null));

        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  PlaceholderProperty.FILE_NAME.getPlaceholder(),
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
                                                                null,
                                                                null,
                                                                connectorTypeGUID,
                                                                endpointGUID,
                                                                assetGUID,
                                                                deployedImplementationType.getAssociatedTypeName(),
                                                                OpenMetadataType.ASSET.typeName);

            archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               PlaceholderProperty.getSoftwareFilesPlaceholderPropertyTypes());
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
                                                    templateDefinition.getServerDescription(),
                                                    templateDefinition.getUserId(),
                                                    templateDefinition.getConnectorTypeGUID(),
                                                    templateDefinition.getNetworkAddress(),
                                                    templateDefinition.getConfigurationProperties(),
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
                                               templateDefinition.getDeployedImplementationType(),
                                               templateDefinition.getAssetName(),
                                               templateDefinition.getAssetDescription(),
                                               templateDefinition.getServerName(),
                                               templateDefinition.getUserId(),
                                               templateDefinition.getPassword(),
                                               templateDefinition.getConnectorTypeGUID(),
                                               templateDefinition.getNetworkAddress(),
                                               templateDefinition.getConfigurationProperties(),
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
     * @param description                      description for the server
     * @param userId                           userId for the connection
     * @param connectorTypeGUID                connector type to link to the connection
     * @param networkAddress                   network address for the endpoint
     * @param configurationProperties          additional properties for the connection
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
                                                       String                               description,
                                                       String                               userId,
                                                       String                               connectorTypeGUID,
                                                       String                               networkAddress,
                                                       Map<String, Object>                  configurationProperties,
                                                       String                               secretsStorePurpose,
                                                       String                               secretsStoreConnectorTypeGUID,
                                                       String                               secretsStoreFileName,
                                                       List<ReplacementAttributeType>       replacementAttributeTypes,
                                                       List<PlaceholderPropertyType>        placeholderPropertyTypes)
    {
        final String methodName = "createSoftwareServerCatalogTemplate";

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name,
                               serverName);

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
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(), 
                                                  description,
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        if (softwareCapabilityType != null)
        {
            archiveHelper.addSoftwareCapability(softwareCapabilityType.getAssociatedTypeName(),
                                                qualifiedName + ":" + softwareCapabilityName,
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
                                                OpenMetadataType.ASSET.typeName);

            archiveHelper.addSupportedSoftwareCapabilityRelationship(qualifiedName + ":" + softwareCapabilityName,
                                                                     qualifiedName,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     1);
        }

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
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
                                                         OpenMetadataType.ASSET.typeName);
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
                                                         OpenMetadataType.ASSET.typeName);

            Map<String, Object> secretsStoreConfigurationProperties = new HashMap<>();

            secretsStoreConfigurationProperties.put(SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName(), qualifiedName);

            String secretStoreEndpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                                       deployedImplementationType.getAssociatedTypeName(),
                                                                       OpenMetadataType.ASSET.typeName,
                                                                       qualifiedName + ":SecretStoreEndpoint",
                                                                       serverName + " secret store endpoint",
                                                                       null,
                                                                       secretsStoreFileName,
                                                                       null,
                                                                       null);

            String secretsStoreConnectionGUID = archiveHelper.addConnection(OpenMetadataType.CONNECTION.typeName,
                                                                            qualifiedName + ":SecretsStoreConnection",
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
                                                                            OpenMetadataType.ASSET.typeName);

            archiveHelper.addEmbeddedConnection(connectionGUID,
                                                0,
                                                secretsStorePurpose,
                                                null,
                                                secretsStoreConnectionGUID);
        }

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addReplacementAttributes(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               replacementAttributeTypes);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
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

        String         qualifiedName            = deployedImplementationType.getDeployedImplementationType() + ":" + serverName;
        Classification templateClassification   = archiveHelper.getTemplateClassification(templateName,
                                                                                          templateDescription,
                                                                                          templateVersion,
                                                                                          null,
                                                                                          methodName);

        archiveHelper.setGUID(qualifiedName, guid);

        String endpointGUID = archiveHelper.addEndpoint(null,
                                                        OpenMetadataType.ENDPOINT.typeName,
                                                        OpenMetadataType.ENDPOINT.typeName,
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
                                               OpenMetadataType.ENDPOINT.typeName,
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

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + ":" + PlaceholderProperty.HOST_URL.getPlaceholder();
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name,
                               PlaceholderProperty.HOST_URL.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create a " + deployedImplementationType.getDeployedImplementationType() + " Host with an associated SoftwareCapability.",
                                                                    "V1.0",
                                                                    null, methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        if (softwareCapabilityType != null)
        {
            archiveHelper.addSoftwareCapability(softwareCapabilityType.getAssociatedTypeName(),
                                                qualifiedName + ":" + softwareCapabilityName,
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
                                                OpenMetadataType.ASSET.typeName);

            archiveHelper.addSupportedSoftwareCapabilityRelationship(qualifiedName + ":" + softwareCapabilityName,
                                                                     qualifiedName,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     1);
        }

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
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
    protected  void createSoftwareCapabilityCatalogTemplate(String                         guid,
                                                           DeployedImplementationType     deployedImplementationType,
                                                           String                         serverQualifiedName,
                                                           String                         softwareCapabilityName,
                                                           String                         softwareCapabilityDescription,
                                                           Classification                 softwareCapabilityClassification,
                                                           List<ReplacementAttributeType> replacementAttributeTypes,
                                                           List<PlaceholderPropertyType>  placeholderPropertyTypes)
    {
        final String methodName = "createSoftwareCapabilityCatalogTemplate";

        String               qualifiedName      = deployedImplementationType.getAssociatedTypeName() + ":" +deployedImplementationType.getDeployedImplementationType() + ":" + serverQualifiedName + ":" + softwareCapabilityName;
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create a " + deployedImplementationType.getDeployedImplementationType() + " SoftwareCapability.",
                                                                    "V1.0",
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
                                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeName);
        assert(guid.equals(capabilityGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, capabilityGUID);

        archiveHelper.addReplacementAttributes(capabilityGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                               replacementAttributeTypes);

        archiveHelper.addPlaceholderProperties(capabilityGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                               placeholderPropertyTypes);
    }


    /**
     * Create a template for a type of asset and link it to the associated deployed implementation type.
     * The template consists of an asset linked to a connection, that is in turn linked
     * to the supplied connector type and an endpoint, along with a nested secrets store
     *
     * @param guid fixed unique identifier
     * @param deployedImplementationType deployed implementation type for the technology
     * @param assetName name for the asset
     * @param assetDescription description
     * @param serverName optional server name
     * @param userId userId for the connection
     * @param password password for the connection
     * @param connectorTypeGUID connector type to link to the connection
     * @param networkAddress network address for the endpoint
     * @param configurationProperties  additional properties for the connection
     * @param secretsStorePurpose              purpose for the secrets store
     * @param secretsStoreConnectorTypeGUID    optional name for the secrets store connector provider to include in the template
     * @param secretsStoreFileName             location of the secrets store
     * @param replacementAttributeTypes attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes placeholder variables used in the supplied parameters
     */
    protected void createDataAssetCatalogTemplate(String                               guid,
                                                  DeployedImplementationTypeDefinition deployedImplementationType,
                                                  String                               assetName,
                                                  String                               assetDescription,
                                                  String                               serverName,
                                                  String                               userId,
                                                  String                               password,
                                                  String                               connectorTypeGUID,
                                                  String                               networkAddress,
                                                  Map<String, Object>                  configurationProperties,
                                                  String                               secretsStorePurpose,
                                                  String                               secretsStoreConnectorTypeGUID,
                                                  String                               secretsStoreFileName,
                                                  List<ReplacementAttributeType>       replacementAttributeTypes,
                                                  List<PlaceholderPropertyType>        placeholderPropertyTypes)
    {
        final String methodName = "createDataAssetCatalogTemplate";

        String               qualifiedName;

        if (serverName == null)
        {
            qualifiedName = deployedImplementationType.getDeployedImplementationType() + ":" + assetName;
        }
        else
        {
            qualifiedName = deployedImplementationType.getDeployedImplementationType() + ":" + serverName + ":" + assetName;
        }

        String               versionIdentifier = "V1.0";
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    null, "V1.0", null, methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  assetName,
                                                  versionIdentifier,
                                                  assetDescription,
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
                                                        qualifiedName + ":Endpoint",
                                                        assetName + " endpoint",
                                                        null,
                                                        networkAddress,
                                                        null,
                                                        null);

        String connectionGUID;
        if (secretsStoreConnectorTypeGUID == null)
        {
            connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                         serverName + " connection",
                                                         null,
                                                         userId,
                                                         password,
                                                         null,
                                                         null,
                                                         configurationProperties,
                                                         null,
                                                         connectorTypeGUID,
                                                         endpointGUID,
                                                         assetGUID,
                                                         deployedImplementationType.getAssociatedTypeName(),
                                                         OpenMetadataType.ASSET.typeName);
        }
        else
        {
            connectionGUID = archiveHelper.addConnection(OpenMetadataType.VIRTUAL_CONNECTION.typeName,
                                                         qualifiedName + ":Connection",
                                                         serverName + " connection",
                                                         null,
                                                         userId,
                                                         password,
                                                         null,
                                                         null,
                                                         configurationProperties,
                                                         null,
                                                         connectorTypeGUID,
                                                         endpointGUID,
                                                         assetGUID,
                                                         deployedImplementationType.getAssociatedTypeName(),
                                                         OpenMetadataType.ASSET.typeName);

            Map<String, Object> secretsStoreConfigurationProperties = new HashMap<>();

            secretsStoreConfigurationProperties.put(SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName(), qualifiedName);

            String secretStoreEndpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                                       deployedImplementationType.getAssociatedTypeName(),
                                                                       OpenMetadataType.ASSET.typeName,
                                                                       qualifiedName + ":SecretStoreEndpoint",
                                                                       serverName + " secret store endpoint",
                                                                       null,
                                                                       secretsStoreFileName,
                                                                       null,
                                                                       null);

            String secretsStoreConnectionGUID = archiveHelper.addConnection(OpenMetadataType.CONNECTION.typeName,
                                                                            qualifiedName + ":SecretsStoreConnection",
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
                                                                            OpenMetadataType.ASSET.typeName);

            archiveHelper.addEmbeddedConnection(connectionGUID,
                                                0,
                                                secretsStorePurpose,
                                                null,
                                                secretsStoreConnectionGUID);
        }

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addReplacementAttributes(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               replacementAttributeTypes);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               placeholderPropertyTypes);
    }



    /**
     * Create a template for a type of asset and link it to the associated deployed implementation type.
     * The template consists of an asset linked to a connection, that is in turn linked
     * to the supplied connector type and an endpoint,
     *
     * @param guid fixed unique identifier
     * @param deployedImplementationType deployed implementation type for the technology
     * @param assetName name for the asset
     * @param assetDescription description
     * @param serverName optional server name
     * @param userId userId for the connection
     * @param password password for the connection
     * @param connectorTypeGUID connector type to link to the connection
     * @param networkAddress network address for the endpoint
     * @param configurationProperties  additional properties for the connection
     * @param replacementAttributeTypes attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes placeholder variables used in the supplied parameters
     */
    protected void createDataAssetCatalogTemplate(String                         guid,
                                                  DeployedImplementationTypeDefinition  deployedImplementationType,
                                                  String                         assetName,
                                                  String                         assetDescription,
                                                  String                         serverName,
                                                  String                         userId,
                                                  String                         password,
                                                  String                         connectorTypeGUID,
                                                  String                         networkAddress,
                                                  Map<String, Object>            configurationProperties,
                                                  List<ReplacementAttributeType> replacementAttributeTypes,
                                                  List<PlaceholderPropertyType>  placeholderPropertyTypes)
    {
        final String methodName = "createDataAssetCatalogTemplate";

        String               qualifiedName;

        if (serverName == null)
        {
            qualifiedName = deployedImplementationType.getDeployedImplementationType() + ":" + assetName;
        }
        else
        {
            qualifiedName = deployedImplementationType.getDeployedImplementationType() + ":" + serverName + ":" + assetName;
        }

        String               versionIdentifier = "V1.0";
        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                               deployedImplementationType.getDeployedImplementationType());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    null, "V1.0", null, methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  assetName,
                                                  versionIdentifier,
                                                  assetDescription,
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
                                                        qualifiedName + ":Endpoint",
                                                        assetName + " endpoint",
                                                        null,
                                                        networkAddress,
                                                        null,
                                                        null);

        String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                            assetName + " connection",
                                                            null,
                                                            userId,
                                                            password,
                                                            null,
                                                            null,
                                                            configurationProperties,
                                                            null,
                                                            connectorTypeGUID,
                                                            endpointGUID,
                                                            assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName);

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addReplacementAttributes(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               replacementAttributeTypes);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               placeholderPropertyTypes);
    }



    /**
     * Add a new valid values record for a deployed implementation type.
     *
     * @param deployedImplementationType preferred value
     * @param associatedTypeName         specific type name to tie it to (maybe null)
     * @param qualifiedName              qualifiedName for this value
     * @param category                   category for this value
     * @param description                description of this value
     * @param wikiLink                   optional URL link to more information
     * @return unique identifier of the deployedImplementationType
     */
    protected String addDeployedImplementationType(String deployedImplementationType,
                                                   String associatedTypeName,
                                                   String qualifiedName,
                                                   String category,
                                                   String description,
                                                   String wikiLink)
    {
        String parentSetGUID = this.getParentSet(null,
                                                 associatedTypeName,
                                                 OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                 null);

        String typeQualifiedName = constructValidValueQualifiedName(null,
                                                                    OpenMetadataProperty.TYPE_NAME.name,
                                                                    null,
                                                                    associatedTypeName);

        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put(OpenMetadataProperty.TYPE_NAME.name, associatedTypeName);

        String validValueGUID = this.archiveHelper.addValidValue(null,
                                                                 parentSetGUID,
                                                                 parentSetGUID,
                                                                 OpenMetadataType.VALID_VALUE_SET.typeName,
                                                                 OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                 OpenMetadataType.VALID_VALUE_SET.typeName,
                                                                 qualifiedName,
                                                                 deployedImplementationType,
                                                                 description,
                                                                 category,
                                                                 OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                                                 "string",
                                                                 OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                                 deployedImplementationType,
                                                                 false,
                                                                 false,
                                                                 additionalProperties);

        if (wikiLink != null)
        {
            String externalReferenceGUID = this.archiveHelper.addExternalReference(null,
                                                                                   validValueGUID,
                                                                                   OpenMetadataType.VALID_VALUE_SET.typeName,
                                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                                   qualifiedName + "_wikiLink",
                                                                                   "More information about deployedImplementationType: " + deployedImplementationType,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   0,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   wikiLink,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null);

            this.archiveHelper.addExternalReferenceLink(validValueGUID, externalReferenceGUID, null, null, null);
        }

        return validValueGUID;
    }


    /**
     * Find or create the parent set for a valid value.
     *
     * @param requestedGUID optional guid for the valid value
     * @param typeName name of the type (can be null)
     * @param propertyName name of the property (can be null)
     * @param mapName name of the mapName (can be null)
     * @return unique identifier (guid) of the parent set
     */
    protected String getParentSet(String requestedGUID,
                                  String typeName,
                                  String propertyName,
                                  String mapName)
    {
        final String parentDescription = "Organizing set for valid metadata values";

        String parentQualifiedName = constructValidValueQualifiedName(typeName, propertyName, mapName, null);
        String parentSetGUID = parentValidValueQNameToGUIDMap.get(parentQualifiedName);

        if (parentSetGUID == null)
        {
            String grandParentSetGUID = null;
            String parentDisplayName = parentQualifiedName.substring(26);

            if (mapName != null)
            {
                grandParentSetGUID = getParentSet(null, typeName, propertyName, null);
            }
            else if (propertyName != null)
            {
                grandParentSetGUID = getParentSet(null, typeName, null, null);
            }
            else if (typeName != null)
            {
                grandParentSetGUID = getParentSet(null, null, null, null);
            }

            parentSetGUID =  archiveHelper.addValidValue(requestedGUID,
                                                         grandParentSetGUID,
                                                         grandParentSetGUID,
                                                         OpenMetadataType.VALID_VALUE_SET.typeName,
                                                         OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                         OpenMetadataType.VALID_VALUE_SET.typeName,
                                                         parentQualifiedName,
                                                         parentDisplayName,
                                                         parentDescription,
                                                         constructValidValueCategory(typeName, propertyName, mapName),
                                                         OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                                         null,
                                                         OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                         null,
                                                         false,
                                                         false,
                                                         null);

            parentValidValueQNameToGUIDMap.put(parentQualifiedName, parentSetGUID);

            return parentSetGUID;
        }
        else
        {
            return parentSetGUID;
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
                                                                                null,
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
                archiveHelper.setGUID(integrationConnectorDefinition.getQualifiedName(integrationGroupDefinition.getQualifiedName()), integrationConnectorDefinition.getGUID());
                String guid = archiveHelper.addIntegrationConnector(integrationConnectorDefinition.getConnectorProviderClassName(),
                                                                    integrationConnectorDefinition.getConfigurationProperties(),
                                                                    integrationConnectorDefinition.getQualifiedName(integrationGroupDefinition.getQualifiedName()),
                                                                    integrationConnectorDefinition.getDisplayName(),
                                                                    integrationConnectorDefinition.getDescription(),
                                                                    integrationConnectorDefinition.getEndpointAddress(),
                                                                    null);
                assert (integrationConnectorDefinition.getGUID().equals(guid));

                archiveHelper.addRegisteredIntegrationConnector(integrationGroupDefinition.getGUID(),
                                                                integrationConnectorDefinition.getConnectorName(),
                                                                integrationConnectorDefinition.getConnectorUserId(),
                                                                integrationConnectorDefinition.getMetadataSourceQualifiedName(),
                                                                integrationConnectorDefinition.getRefreshTimeInterval(),
                                                                integrationConnectorDefinition.getGUID());

                if (integrationConnectorDefinition.getDeployedImplementationTypes() != null)
                {
                    for (String deployedImplementationType : integrationConnectorDefinition.getDeployedImplementationTypes())
                    {
                        String deployedImplementationTypeGUID = archiveHelper.queryGUID(deployedImplementationType);

                        archiveHelper.addResourceListRelationshipByGUID(deployedImplementationTypeGUID,
                                                                        integrationConnectorDefinition.getGUID(),
                                                                        integrationConnectorDefinition.getResourceUse().getResourceUse(),
                                                                        integrationConnectorDefinition.getResourceUse().getDescription());
                    }
                }
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
                                    requestTypeDefinition.getSupportedElementQualifiedName());
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
     * @param governanceActionDescription description of the governance action if and
     * @param governanceActionTypeGUID unique identifier of the associated governance action type
     * @param supportedElementQualifiedName element to link the governance action type to
     */
    protected void addRequestType(String                      governanceEngineGUID,
                                  String                      governanceEngineName,
                                  String                      governanceEngineTypeName,
                                  String                      governanceRequestType,
                                  String                      serviceRequestType,
                                  Map<String, String>         requestParameters,
                                  List<NewActionTarget>       actionTargets,
                                  GovernanceActionDescription governanceActionDescription,
                                  String                      governanceActionTypeGUID,
                                  String                      supportedElementQualifiedName)
    {
        archiveHelper.addSupportedGovernanceService(governanceEngineGUID,
                                                    governanceRequestType,
                                                    serviceRequestType,
                                                    requestParameters,
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
     * @param governanceActionDescription description of the governance action if and
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
        String governanceActionTypeQualifiedName = governanceEngineName + ":" + governanceRequestType;

        archiveHelper.setGUID(governanceActionTypeQualifiedName, governanceActionTypeGUID);

        String guid = archiveHelper.addGovernanceActionType(null,
                                                            governanceEngineGUID,
                                                            governanceEngineTypeName,
                                                            OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                            governanceEngineName + ":" + governanceRequestType,
                                                            governanceRequestType + " (" + governanceEngineName + ")",
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

        if (governanceActionDescription.supportedTechnologies != null)
        {
            for (SupportedTechnologyType supportedTechnology : governanceActionDescription.supportedTechnologies)
            {
                if (supportedTechnology != null)
                {
                    if (supportedTechnology.getDataType() != null)
                    {
                        String openMetadataTypeGUID = OpenMetadataType.getDescriptionGUIDForType(supportedTechnology.getDataType());

                        if (openMetadataTypeGUID != null)
                        {
                            archiveHelper.addResourceListRelationshipByGUID(openMetadataTypeGUID,
                                                                            governanceActionTypeGUID,
                                                                            governanceActionDescription.resourceUse.getResourceUse(),
                                                                            governanceActionDescription.governanceServiceDescription,
                                                                            requestParameters,
                                                                            false);
                        }
                    }

                    if (supportedTechnology.getName() != null)
                    {
                        String deployedImplementationTypeQNAME = deployedImplementationTypeQNAMEs.get(supportedTechnology.getName());

                        if (deployedImplementationTypeQNAME != null)
                        {
                            archiveHelper.addResourceListRelationshipByGUID(archiveHelper.queryGUID(deployedImplementationTypeQNAME),
                                                                            governanceActionTypeGUID,
                                                                            governanceActionDescription.resourceUse.getResourceUse(),
                                                                            governanceActionDescription.governanceServiceDescription,
                                                                            requestParameters,
                                                                            false);
                        }
                    }
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
                                                            requestParameters,
                                                            false);
        }
    }


    /**
     * Create a three-step governance action process that creates a metadata element for a particular type of asset
     * and then runs a survey against the asset's resource and creates a report.
     *
     * @param assetType name for the asset type (no spaces)
     * @param technologyType value for deployed implementation type
     * @param createRequestType request type used to create the server's metadata element
     * @param createEngineDefinition engine to call for the create operation
     * @param surveyRequestType request type to run the survey
     * @param surveyEngineDefinition survey engine
     */
    protected void createAndSurveyServerGovernanceActionProcess(String                     assetType,
                                                                String                     technologyType,
                                                                RequestTypeDefinition      createRequestType,
                                                                GovernanceEngineDefinition createEngineDefinition,
                                                                RequestTypeDefinition      surveyRequestType,
                                                                GovernanceEngineDefinition surveyEngineDefinition)
    {
        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                      assetType + ":CreateAndSurveyGovernanceActionProcess",
                                                                      assetType + ":CreateAndSurvey",
                                                                      null,
                                                                      "Create a " + technologyType + ", run a survey against it, and print out the resulting report.",
                                                                      null,
                                                                      0,
                                                                      null,
                                                                      null,
                                                                      null);

        String step1GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        assetType + ":CreateAndSurvey:Step1",
                                                                        "Create the asset entity",
                                                                        "Create the description of the " + technologyType,
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
            addStepExecutor(step1GUID, createRequestType, createEngineDefinition);

            archiveHelper.addGovernanceActionProcessFlow(processGUID, null, null, step1GUID);
        }

        String step2GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        assetType + ":CreateAndSurvey:Step2",
                                                                        "Run the survey.",
                                                                        "Create a survey report detailing the contents of the "+ technologyType + ".",
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
            addStepExecutor(step2GUID, surveyRequestType, surveyEngineDefinition);

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, ManageAssetGuard.SET_UP_COMPLETE.getName(), false, step2GUID);
        }

        String step3GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        assetType + ":CreateAndSurvey:Step3",
                                                                        "Print the survey report.",
                                                                        "Print a survey report detailing the contents of the " + technologyType + ".",
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
    }


    /**
     * Create a two-step governance action process that creates a metadata element for a particular type of server
     * and then adds it as a catalog target for an appropriate integration connector.
     *
     * @param serverType name for the server type (no spaces)
     * @param technologyType value for deployed implementation type
     * @param createRequestType request type used to create the server's metadata element
     * @param createEngineDefinition governance action engine
     * @param catalogRequestType request type to run the survey
     * @param catalogEngineDefinition governance action engine
     */
    protected void createAndCatalogServerGovernanceActionProcess(String                     serverType,
                                                                 String                     technologyType,
                                                                 RequestTypeDefinition      createRequestType,
                                                                 GovernanceEngineDefinition createEngineDefinition,
                                                                 RequestTypeDefinition      catalogRequestType,
                                                                 GovernanceEngineDefinition catalogEngineDefinition)
    {
        createAsCatalogTargetGovernanceActionProcess(serverType,
                                                     OpenMetadataType.SOFTWARE_SERVER.typeName,
                                                     technologyType,
                                                     "catalog",
                                                     createRequestType,
                                                     createEngineDefinition,
                                                     catalogRequestType,
                                                     catalogEngineDefinition);
    }


    /**
     * Create a two-step governance action process that creates a metadata element for a particular type of asset
     * and then adds it as a catalog target for an appropriate integration connector.
     *
     * @param assetType name for the asset type (no spaces)
     * @param openMetadataType open metadata type name for the created asset
     * @param technologyType value for deployed implementation type
     * @param createRequestType request type used to create the server's metadata element
     * @param createEngineDefinition governance action engine
     * @param catalogRequestType request type to run the survey
     * @param catalogEngineDefinition governance action engine
     */
    protected void createAndCatalogAssetGovernanceActionProcess(String                     assetType,
                                                                String                     openMetadataType,
                                                                String                     technologyType,
                                                                RequestTypeDefinition      createRequestType,
                                                                GovernanceEngineDefinition createEngineDefinition,
                                                                RequestTypeDefinition      catalogRequestType,
                                                                GovernanceEngineDefinition catalogEngineDefinition)
    {
        createAsCatalogTargetGovernanceActionProcess(assetType,
                                                     openMetadataType,
                                                     technologyType,
                                                     "catalog",
                                                     createRequestType,
                                                     createEngineDefinition,
                                                     catalogRequestType,
                                                     catalogEngineDefinition);
    }


    /**
     * Create a two-step governance action process that creates a metadata element for a particular type of asset
     * and then adds it as a catalog target for an appropriate integration connector.
     *
     * @param assetType name for the asset type (no spaces)
     * @param openMetadataType open metadata type name for the created asset
     * @param technologyType value for deployed implementation type
     * @param createRequestType request type used to create the server's metadata element
     * @param createEngineDefinition governance action engine
     * @param catalogRequestType request type to run the survey
     * @param catalogEngineDefinition governance action engine
     */
    protected void createAndHarvestToAssetGovernanceActionProcess(String                     assetType,
                                                                  String                     openMetadataType,
                                                                  String                     technologyType,
                                                                  RequestTypeDefinition      createRequestType,
                                                                  GovernanceEngineDefinition createEngineDefinition,
                                                                  RequestTypeDefinition      catalogRequestType,
                                                                  GovernanceEngineDefinition catalogEngineDefinition)
    {
        createAsCatalogTargetGovernanceActionProcess(assetType,
                                                     openMetadataType,
                                                     technologyType,
                                                     "harvest",
                                                     createRequestType,
                                                     createEngineDefinition,
                                                     catalogRequestType,
                                                     catalogEngineDefinition);
    }



    /**
     * Create a two-step governance action process that creates a metadata element for a particular type of server
     * and then adds it as a catalog target for an appropriate integration connector.
     *
     * @param assetType name for the server type (no spaces)
     * @param technologyType value for deployed implementation type
     * @param createRequestType request type used to create the server's metadata element
     * @param createEngineDefinition governance action engine
     * @param catalogRequestType request type to run the survey
     * @param catalogEngineDefinition governance action engine
     */
    protected void createAsCatalogTargetGovernanceActionProcess(String                     assetType,
                                                                String                     openMetadataType,
                                                                String                     technologyType,
                                                                String                     actionName,
                                                                RequestTypeDefinition      createRequestType,
                                                                GovernanceEngineDefinition createEngineDefinition,
                                                                RequestTypeDefinition      catalogRequestType,
                                                                GovernanceEngineDefinition catalogEngineDefinition)
    {
        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                      assetType + ":CreateAsCatalogTargetGovernanceActionProcess",
                                                                      assetType + ":CreateAsCatalogTarget",
                                                                      null,
                                                                      "Create a " + technologyType + " and configure an integration connector to " + actionName + " its contents.",
                                                                      null,
                                                                      0,
                                                                      null,
                                                                      null,
                                                                      null);

        String step1GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        assetType + ":CreateAsCatalogTarget:Step1",
                                                                        "Create the " + openMetadataType + " entity",
                                                                        "Create the description of the " + technologyType,
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
            addStepExecutor(step1GUID, createRequestType, createEngineDefinition);

            archiveHelper.addGovernanceActionProcessFlow(processGUID, null, null, step1GUID);
        }

        String step2GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        assetType + ":CreateAsCatalogTarget:Step2",
                                                                        "Connect new asset to integration connector.",
                                                                        "Connect the asset entity for the " + technologyType + " to the appropriate integration connector.",
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
            addStepExecutor(step2GUID, catalogRequestType, catalogEngineDefinition);

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, ManageAssetGuard.SET_UP_COMPLETE.getName(), false, step2GUID);
        }
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