/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors.base;

import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.ManageAssetGuard;
import org.odpi.openmetadata.archiveutilities.openconnectors.*;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreConfigurationProperty;
import org.odpi.openmetadata.frameworks.opengovernance.controls.RequestParameterType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.ReplacementAttributeType;
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


/**
 * CorePackArchiveWriter creates an open metadata archive that includes the connector type
 * information for all open connectors supplied by the egeria project.
 */
public abstract class ContentPackBaseArchiveWriter extends EgeriaBaseArchiveWriter
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

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, PlaceholderProperty.FILE_PATH_NAME.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_TYPE.name, PlaceholderProperty.FILE_TYPE.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_EXTENSION.name, PlaceholderProperty.FILE_EXTENSION.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.FILE_NAME.name, PlaceholderProperty.FILE_NAME.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create a data asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        classifications.add(archiveHelper.getDataAssetEncodingClassification(PlaceholderProperty.FILE_ENCODING.getPlaceholder(),
                                                                             encodingLanguage,
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


    protected void addDataSetCatalogTemplates(ContentPackDefinition contentPackDefinition)
    {
        for (DataSetTemplateDefinition templateDefinition : DataSetTemplateDefinition.values())
        {
            if (templateDefinition.getContentPackDefinition() == contentPackDefinition)
            {
                createDataSetCatalogTemplate(templateDefinition.getTemplateGUID(),
                                             templateDefinition.getDeployedImplementationType(),
                                             templateDefinition.getQualifiedName(),
                                             templateDefinition.getConnectorTypeGUID());
            }
        }
    }

    /**
     * Create a template for a dataset and link it to the associated open metadata type.
     * The template consists of a DataFile asset plus an optional connection, linked
     * to the supplied connector type and an endpoint,
     *
     * @param deployedImplementationType values for the template
     * @param connectorTypeGUID          connector type to link to the connection
     */
    protected void createDataSetCatalogTemplate(String                               templateGUID,
                                                DeployedImplementationTypeDefinition deployedImplementationType,
                                                String                               qualifiedName,
                                                String                               connectorTypeGUID)
    {
        final String methodName = "createDataSetCatalogTemplate";

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    "Create an asset of type " + deployedImplementationType.getAssociatedTypeName() + " with an associated Connection.",
                                                                    "V1.0",
                                                                    null, methodName));

        archiveHelper.setGUID(qualifiedName, templateGUID);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  PlaceholderProperty.DISPLAY_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        assert(assetGUID.equals(templateGUID));

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
                                               PlaceholderProperty.getDataSetPlaceholderPropertyTypes());
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
                                                                     null,
                                                                     1);
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
                                                                     null,
                                                                     1);
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
     * @param assetName name for the asset
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
                                                  String                               assetName,
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
                                                  assetName,
                                                  versionIdentifier,
                                                  assetDescription,
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(templateGUID.equals(assetGUID));

        if (connectorTypeGUID != null)
        {
            String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName,
                                                            null,
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
                                                             qualifiedName + ":Connection",
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

                Map<String, Object> secretsStoreConfigurationProperties = new HashMap<>();

                secretsStoreConfigurationProperties.put(SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName(), secretsStoreCollectionName);

                String secretStoreEndpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                                           deployedImplementationType.getAssociatedTypeName(),
                                                                           OpenMetadataType.ASSET.typeName,
                                                                           null,
                                                                           qualifiedName + ":SecretStoreEndpoint",
                                                                           assetName + " secret store endpoint",
                                                                           null,
                                                                           secretsStoreFileName,
                                                                           null,
                                                                           null);

                String secretsStoreConnectionGUID = archiveHelper.addConnection(OpenMetadataType.CONNECTION.typeName,
                                                                                qualifiedName + ":SecretsStoreConnection",
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

                archiveHelper.addEmbeddedConnection(connectionGUID,
                                                    0,
                                                    secretsStorePurpose,
                                                    null,
                                                    secretsStoreConnectionGUID);
            }

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
     * Add a new valid values record for a deployed implementation type.
     *
     * @param deployedImplementationType preferred value
     * @param associatedTypeName         specific type name to tie it to (maybe null)
     * @param qualifiedName              qualifiedName for this value
     * @param namespace                   namespace for this value
     * @param description                description of this value
     * @param wikiLink                   optional URL link to more information
     * @param isATypeOf                  superType
     * @return unique identifier of the deployedImplementationType
     */
    protected String addDeployedImplementationType(String                               deployedImplementationType,
                                                   String                               associatedTypeName,
                                                   String                               qualifiedName,
                                                   String                               namespace,
                                                   String                               description,
                                                   String                               wikiLink,
                                                   DeployedImplementationTypeDefinition isATypeOf)
    {
        String parentSetGUID = this.getParentSet(null,
                                                 associatedTypeName,
                                                 OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                 null);

        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put(OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name, associatedTypeName);

        String validValueGUID = this.archiveHelper.addValidValue(null,
                                                                 parentSetGUID,
                                                                 parentSetGUID,
                                                                 OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                 OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                 null,
                                                                 OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                 qualifiedName,
                                                                 Category.VALID_METADATA_VALUES.getName(),
                                                                 deployedImplementationType,
                                                                 description,
                                                                 namespace,
                                                                 OpenMetadataValidValues.VALID_METADATA_VALUES_USAGE,
                                                                 DataType.STRING.getName(),
                                                                 OpenMetadataValidValues.OPEN_METADATA_ECOSYSTEM_SCOPE,
                                                                 deployedImplementationType,
                                                                 null,
                                                                 false,
                                                                 additionalProperties);

        if (wikiLink != null)
        {
            String externalReferenceGUID = this.archiveHelper.addExternalReference(null,
                                                                                   validValueGUID,
                                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                                   OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                                   null,
                                                                                   qualifiedName + "_wikiLink",
                                                                                   "More information about deployedImplementationType: " + deployedImplementationType,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   originatorName,
                                                                                   null,
                                                                                   wikiLink,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   null);

            this.archiveHelper.addExternalReferenceLink(validValueGUID, externalReferenceGUID, null, null, null);
        }

        if (isATypeOf != null)
        {
            archiveHelper.addValidValueAssociationRelationship(qualifiedName,
                                                               isATypeOf.getQualifiedName(),
                                                               OpenMetadataValidValues.VALID_METADATA_VALUE_IS_TYPE_OF,
                                                               AssociationType.INHERITANCE.getName(),
                                                               null);
        }

        return validValueGUID;
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
                                                                    integrationConnectorDefinition.getConnectorUserId(),
                                                                    integrationConnectorDefinition.getEndpointAddress(),
                                                                    null);
                assert (integrationConnectorDefinition.getGUID().equals(guid));

                archiveHelper.addRegisteredIntegrationConnector(integrationGroupDefinition.getGUID(),
                                                                integrationConnectorDefinition.getConnectorName(),
                                                                integrationConnectorDefinition.getConnectorUserId(),
                                                                integrationConnectorDefinition.getMetadataSourceQualifiedName(),
                                                                integrationConnectorDefinition.getRefreshTimeInterval(),
                                                                true,
                                                                integrationConnectorDefinition.getGUID());

                archiveHelper.addITProfile(integrationConnectorDefinition.getGUID(),
                                           integrationConnectorDefinition.getConnectorUserId(),
                                           integrationConnectorDefinition.getQualifiedName(integrationGroupDefinition.getQualifiedName()) + ":ActorProfile",
                                           integrationConnectorDefinition.getConnectorName(),
                                           integrationConnectorDefinition.getDescription(),
                                           null);

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

        String guid = archiveHelper.addGovernanceActionType(null,
                                                            governanceEngineGUID,
                                                            governanceEngineTypeName,
                                                            OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                            null,
                                                            governanceEngineName + "::" + governanceRequestType,
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
     * @param createTemplate details of the template to use
     * @param surveyRequestType request type to run the survey
     * @param supportedElementQualifiedName qualified name of the element that this should be listed as a resource
     */
    protected void createAndSurveyServerGovernanceActionProcess(String                     assetType,
                                                                String                     technologyType,
                                                                RequestTypeDefinition      createRequestType,
                                                                TemplateDefinition         createTemplate,
                                                                RequestTypeDefinition      surveyRequestType,
                                                                String                     supportedElementQualifiedName)
    {
        String description = "Create a " + technologyType + ", run a survey against it, and print out the resulting report.";

        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                      assetType + ":CreateAndSurveyGovernanceActionProcess",
                                                                      assetType + ":CreateAndSurvey",
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

        String step1GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        assetType + ":CreateAndSurvey:Step1",
                                                                        "Create the " + assetType + " entity",
                                                                        "Create the description of the " + technologyType,
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

        String step2GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
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
            addStepExecutor(step2GUID, surveyRequestType, surveyRequestType.getGovernanceEngine());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, ManageAssetGuard.SET_UP_COMPLETE.getName(), false, step2GUID);
        }

        String step3GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
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

        if (supportedElementQualifiedName != null)
        {
            String supportedElementGUID = archiveHelper.queryGUID(supportedElementQualifiedName);
            archiveHelper.addResourceListRelationshipByGUID(supportedElementGUID,
                                                            processGUID,
                                                            ResourceUse.SURVEY_RESOURCE.getResourceUse(),
                                                            description,
                                                            createRequestType.getRequestParameters(),
                                                            false);
        }
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
     * @param technologyType value for deployed implementation type
     * @param createRequestType request type used to create the server's metadata element
     * @param createTemplate details of the template to use
     * @param catalogRequestType request type to run the survey
     * @param supportedElementQualifiedName qualified name of the element that this should be listed as a resource
     */
    protected void createAndCatalogServerGovernanceActionProcess(String                     serverType,
                                                                 String                     technologyType,
                                                                 RequestTypeDefinition      createRequestType,
                                                                 TemplateDefinition         createTemplate,
                                                                 RequestTypeDefinition      catalogRequestType,
                                                                 String                     supportedElementQualifiedName)
    {
        createAsCatalogTargetGovernanceActionProcess(serverType,
                                                     OpenMetadataType.SOFTWARE_SERVER.typeName,
                                                     technologyType,
                                                     "catalog",
                                                     createRequestType,
                                                     createTemplate,
                                                     catalogRequestType,
                                                     supportedElementQualifiedName);
    }


    /**
     * Create a two-step governance action process that creates a metadata element for a particular type of asset
     * and then adds it as a catalog target for an appropriate integration connector.
     *
     * @param assetType name for the asset type (no spaces)
     * @param openMetadataType open metadata type name for the created asset
     * @param technologyType value for deployed implementation type
     * @param createRequestType request type used to create the server's metadata element
     * @param createTemplate details of the template to use
     * @param catalogRequestType request type to run the survey
     * @param supportedElementQualifiedName qualified name of the element that this should be listed as a resource
     */
    protected void createAndCatalogAssetGovernanceActionProcess(String                     assetType,
                                                                String                     openMetadataType,
                                                                String                     technologyType,
                                                                RequestTypeDefinition      createRequestType,
                                                                TemplateDefinition         createTemplate,
                                                                RequestTypeDefinition      catalogRequestType,
                                                                String                     supportedElementQualifiedName)
    {
        createAsCatalogTargetGovernanceActionProcess(assetType,
                                                     openMetadataType,
                                                     technologyType,
                                                     "catalog",
                                                     createRequestType,
                                                     createTemplate,
                                                     catalogRequestType,
                                                     supportedElementQualifiedName);
    }


    /**
     * Create a two-step governance action process that creates a metadata element for a particular type of asset
     * and then adds it as a catalog target for an appropriate integration connector.
     *
     * @param assetType name for the asset type (no spaces)
     * @param openMetadataType open metadata type name for the created asset
     * @param technologyType value for deployed implementation type
     * @param createRequestType request type used to create the server's metadata element
     * @param createTemplate details of the template to use
     * @param catalogRequestType request type to run the survey
     * @param supportedElementQualifiedName qualified name of the element that this should be listed as a resource
     */
    protected void createAndHarvestToAssetGovernanceActionProcess(String                     assetType,
                                                                  String                     openMetadataType,
                                                                  String                     technologyType,
                                                                  RequestTypeDefinition      createRequestType,
                                                                  TemplateDefinition         createTemplate,
                                                                  RequestTypeDefinition      catalogRequestType,
                                                                  String                     supportedElementQualifiedName)
    {
        createAsCatalogTargetGovernanceActionProcess(assetType,
                                                     openMetadataType,
                                                     technologyType,
                                                     "harvest",
                                                     createRequestType,
                                                     createTemplate,
                                                     catalogRequestType,
                                                     supportedElementQualifiedName);
    }



    /**
     * Create a two-step governance action process that creates a metadata element for a particular type of asset
     * and then adds it as a catalog target for an appropriate integration connector.
     *
     * @param assetType name for the server type (no spaces)
     * @param openMetadataType type of the element to create
     * @param technologyType value for deployed implementation type
     * @param actionName nme to use for the action
     * @param createRequestType request type used to create the server's metadata element
     * @param createTemplate details of the template to use
     * @param catalogRequestType request type to run the survey
     * @param supportedElementQualifiedName qualified name of the element that this should be listed as a resource
     */
    protected void createAsCatalogTargetGovernanceActionProcess(String                     assetType,
                                                                String                     openMetadataType,
                                                                String                     technologyType,
                                                                String                     actionName,
                                                                RequestTypeDefinition      createRequestType,
                                                                TemplateDefinition         createTemplate,
                                                                RequestTypeDefinition      catalogRequestType,
                                                                String                     supportedElementQualifiedName)
    {
        String description = "Create a " + technologyType + " and configure an integration connector to " + actionName + " its contents.";

        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                      assetType + ":CreateAsCatalogTargetGovernanceActionProcess",
                                                                      assetType + ":CreateAsCatalogTarget",
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

        archiveHelper.addSupportedRequestParameters(processGUID,
                                                    OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                    processGUID,
                                                    OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                    OpenMetadataType.ASSET.typeName,
                                                    null,
                                                    supportedRequestParameters);

        String step1GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        assetType + ":CreateAsCatalogTarget:Step1",
                                                                        "Create the " + openMetadataType + " entity",
                                                                        "Create the description of the " + technologyType,
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

        String step2GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        assetType + ":CreateAsCatalogTarget:Step2",
                                                                        "Connect new asset to integration connector",
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
            addStepExecutor(step2GUID, catalogRequestType, catalogRequestType.getGovernanceEngine());

            archiveHelper.addNextGovernanceActionProcessStep(step1GUID, ManageAssetGuard.SET_UP_COMPLETE.getName(), false, step2GUID);
        }

        if (supportedElementQualifiedName != null)
        {
            String supportedElementGUID = archiveHelper.queryGUID(supportedElementQualifiedName);
            archiveHelper.addResourceListRelationshipByGUID(supportedElementGUID,
                                                            processGUID,
                                                            ResourceUse.CATALOG_RESOURCE.getResourceUse(),
                                                            description,
                                                            createRequestType.getRequestParameters(),
                                                            false);
        }
    }




    /**
     * Create a one-step governance action process that deletes a metadata element for a particular type of asset
     * which then removes it, any anchored content and relationships - like the catalog target for an appropriate integration connector.
     *
     * @param assetType name for the server type (no spaces)
     * @param technologyType value for deployed implementation type
     * @param deleteRequestType request type used to delete the server's metadata element
     * @param supportedElementQualifiedName qualified name of the element that this should be listed as a resource
     */
    protected void deleteAsCatalogTargetGovernanceActionProcess(String                     assetType,
                                                                String                     openMetadataType,
                                                                String                     technologyType,
                                                                RequestTypeDefinition      deleteRequestType,
                                                                String                     supportedElementQualifiedName)
    {
        String description = "Delete the asset for " + technologyType + " using the same template properties that were used to create it.  This will delete all of the metadata anchored to the asset and relationships to other entities such as the catalog target relationships.";

        String processGUID = archiveHelper.addGovernanceActionProcess(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                      assetType + ":DeleteAssetWithTemplateGovernanceActionProcess",
                                                                      assetType + ":DeleteAsset",
                                                                      null,
                                                                      description,
                                                                      null,
                                                                      0,
                                                                      null,
                                                                      null,
                                                                      null);

        String step1GUID = archiveHelper.addGovernanceActionProcessStep(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                        processGUID,
                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                        OpenMetadataType.ASSET.typeName,
                                                                        null,
                                                                        assetType + ":Delete Asset:Step1",
                                                                        "Delete the " + openMetadataType + " entity",
                                                                        "Delete asset for " + technologyType + " using the same template properties as was used to create it.",
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

        if (supportedElementQualifiedName != null)
        {
            String supportedElementGUID = archiveHelper.queryGUID(supportedElementQualifiedName);
            archiveHelper.addResourceListRelationshipByGUID(supportedElementGUID,
                                                            processGUID,
                                                            ResourceUse.SURVEY_RESOURCE.getResourceUse(),
                                                            description,
                                                            deleteRequestType.getRequestParameters(),
                                                            false);
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