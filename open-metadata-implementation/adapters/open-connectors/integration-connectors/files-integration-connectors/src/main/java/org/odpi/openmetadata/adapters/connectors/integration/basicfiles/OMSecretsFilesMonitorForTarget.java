/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.adapters.connectors.EgeriaInformationSupplyChainDefinition;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsFileConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.users.SecretsCollection;
import org.odpi.openmetadata.frameworks.connectors.properties.users.SecretsStore;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetEncodingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.SecretsCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.FileExtension;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class OMSecretsFilesMonitorForTarget extends DataFilesMonitorForTarget
{
    private static final Logger log = LoggerFactory.getLogger(OMSecretsFilesMonitorForTarget.class);


    /**
     * Construct the monitor for a specific catalog target.
     *
     * @param connectorName name of associated connector
     * @param sourceName source of the pathname
     * @param pathName pathname to the directory
     * @param catalogTargetGUID optional catalog target GUID
     * @param deleteMethod should the connector use delete or archive?
     * @param templates names and GUIDs of templates
     * @param configurationProperties parameters to further modify the behaviour of the connector
     * @param integrationConnector associated connector
     * @param dataFolderElement Egeria element for this directory
     * @param auditLog logging destination
     */
    public OMSecretsFilesMonitorForTarget(String                                    connectorName,
                                          String                                    sourceName,
                                          String                                    pathName,
                                          String                                    catalogTargetGUID,
                                          DeleteMethod                              deleteMethod,
                                          Map<String,String>                        templates,
                                          Map<String, Object>                       configurationProperties,
                                          BasicFilesMonitorIntegrationConnectorBase integrationConnector,
                                          OpenMetadataRootElement                   dataFolderElement,
                                          AuditLog                                  auditLog)
    {
        super(connectorName,
              sourceName,
              pathName,
              catalogTargetGUID,
              deleteMethod,
              templates,
              configurationProperties,
              integrationConnector,
              dataFolderElement,
              auditLog);
    }


    /**
     * Return the unique identifier of a new metadata element describing the file.
     *
     * @param typeName subtype name for file
     * @param properties basic properties to use
     * @param encodingProperties properties for DataAssetEncoding classification
     * @return unique identifier (guid)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException unable to communicate with the repository
     * @throws UserNotAuthorizedException access problem for userId
     */
    protected String addDataFileToCatalog(String                      typeName,
                                          DataFileProperties          properties,
                                          DataAssetEncodingProperties encodingProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        String fileAssetGUID = super.addDataFileToCatalog(OpenMetadataType.ARCHIVE_FILE.typeName,
                                                          properties,
                                                          null);

        if (FileExtension.OM_SECRETS.getFileExtension().equals(properties.getFileExtension()))
        {
            this.catalogSecretsCollections(fileAssetGUID);
        }
        else
        {
            log.debug("ignoring file " + properties);
        }

        return fileAssetGUID;
    }


    /**
     * Return the unique identifier of a new metadata element describing the file created using the supplied template.
     *
     * @param assetTypeName type of asset to create
     * @param fileTemplateGUID template to use
     * @param replacementProperties properties from the template to replace
     * @param placeholderProperties values to use to replace placeholders in the template
     * @return unique identifier (guid)
     * @throws ConnectorCheckedException connector has been shutdown
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException unable to communicate with the repository
     * @throws UserNotAuthorizedException access problem for userId
     */
    protected String addDataFileViaTemplate(String              assetTypeName,
                                            String              fileTemplateGUID,
                                            ElementProperties   replacementProperties,
                                            Map<String, String> placeholderProperties) throws ConnectorCheckedException,
                                                                                              InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        String fileGUID = super.addDataFileViaTemplate(assetTypeName, fileTemplateGUID, replacementProperties, placeholderProperties);

        if (FileExtension.OM_SECRETS.getFileExtension().equals(placeholderProperties.get(PlaceholderProperty.FILE_EXTENSION.getName())))
        {
            this.catalogSecretsCollections(fileGUID);
        }
        else
        {
            log.debug("ignoring file " + placeholderProperties);
        }

        return fileGUID;
    }


    /**
     * Catalog the secrets collections in the archive file.
     *
     * @param fileAssetGUID unique identifier of the file to open
     * @throws UserNotAuthorizedException security problems
     */
    private void catalogSecretsCollections(String fileAssetGUID) throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        PropertyServerException
    {
        final String methodName = "catalogSecretsCollections";

        try
        {
            Connector connector = integrationConnector.integrationContext.getConnectedAssetContext().getConnectorForAsset(fileAssetGUID, auditLog);

            if (connector instanceof YAMLSecretsFileConnector yamlSecretsFileConnector)
            {
                yamlSecretsFileConnector.start();
                SecretsStore secretsStore = yamlSecretsFileConnector.getSecretsStore();

                if (secretsStore != null)
                {
                    for (String secretsCollectionName : secretsStore.getSecretsCollections().keySet())
                    {
                        yamlSecretsFileConnector.setSecretsCollectionName(secretsCollectionName);

                        SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

                        if (secretsCollection != null)
                        {
                            String secretsCollectionGUID = catalogSecretsCollection(secretsCollectionName, secretsCollection, fileAssetGUID);
                        }
                    }
                }
            }
        }
        catch (ClassCastException | RepositoryErrorException | ConnectionCheckedException | ConnectorCheckedException error)
        {
            auditLog.logMessage(methodName,
                                BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   methodName,
                                                                                                                   error.getMessage()));
        }
    }


    /**
     * Catalog the secrets collection in the metadata repository.
     *
     * @param secretsCollectionName name of the secrets collection
     * @param secretsCollection collection of secrets
     * @param fileAssetGUID unique identifier of the file asset
     * @return unique identifier of the secrets collection
     */
    private String catalogSecretsCollection(String            secretsCollectionName,
                                            SecretsCollection secretsCollection,
                                            String            fileAssetGUID) throws UserNotAuthorizedException,
                                                                                    InvalidParameterException,
                                                                                    PropertyServerException
    {
        String qualifiedName = OpenMetadataType.SECRETS_COLLECTION.typeName + "::" + fileAssetGUID + "::" + secretsCollectionName;

        String secretsCollectionGUID;

        try
        {
            secretsCollectionGUID = integrationConnector.integrationContext.getOpenMetadataStore().getMetadataElementGUIDByUniqueName(qualifiedName,
                                                                                                                                      OpenMetadataProperty.QUALIFIED_NAME.name);
        }
        catch (InvalidParameterException notKnown)
        {
            NewElementOptions options = new NewElementOptions();

            options.setIsOwnAnchor(true);
            options.setParentGUID(fileAssetGUID);
            options.setParentAtEnd1(false);
            options.setParentRelationshipTypeName(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName);

            SecretsCollectionProperties secretsCollectionProperties = new SecretsCollectionProperties();

            secretsCollectionProperties.setQualifiedName(qualifiedName);
            secretsCollectionProperties.setResourceName(secretsCollectionName);
            secretsCollectionProperties.setDisplayName(secretsCollection.getDisplayName());
            secretsCollectionProperties.setDescription(secretsCollection.getDescription());

            Map<String, String> additionalProperties = new HashMap<>();

            additionalProperties.put(OpenMetadataProperty.REFRESH_TIME_INTERVAL.name, Long.toString(secretsCollection.getRefreshTimeInterval()));

            secretsCollectionProperties.setAdditionalProperties(additionalProperties);

            DataSetContentProperties dataSetContentProperties = new DataSetContentProperties();

            dataSetContentProperties.setISCQualifiedName(EgeriaInformationSupplyChainDefinition.SECURITY.getQualifiedName());

            /*
             * The secrets collection is not added to the security zone because it need to be visible to the data
             * engineers configuring cataloguing.
             */
            secretsCollectionGUID = integrationConnector.integrationContext.getAssetClient().createAsset(options,
                                                                                                         null,
                                                                                                         secretsCollectionProperties,
                                                                                                         dataSetContentProperties);
        }

        return secretsCollectionGUID;
    }
}
