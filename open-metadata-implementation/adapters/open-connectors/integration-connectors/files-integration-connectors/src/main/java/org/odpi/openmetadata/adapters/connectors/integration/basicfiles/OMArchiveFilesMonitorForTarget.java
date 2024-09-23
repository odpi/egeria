/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.FileBasedOpenMetadataArchiveStoreConnector;
import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.FileBasedOpenMetadataArchiveStoreProvider;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.FileFolderElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.FileExtension;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class OMArchiveFilesMonitorForTarget extends DataFilesMonitorForTarget
{
    private static final Logger log = LoggerFactory.getLogger(OMArchiveFilesMonitorForTarget.class);

    private static final ConnectorBroker connectorBroker = new ConnectorBroker();


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
    public OMArchiveFilesMonitorForTarget(String                                    connectorName,
                                          String                                    sourceName,
                                          String                                    pathName,
                                          String                                    catalogTargetGUID,
                                          DeleteMethod                              deleteMethod,
                                          Map<String,String>                        templates,
                                          Map<String, Object>                       configurationProperties,
                                          BasicFilesMonitorIntegrationConnectorBase integrationConnector,
                                          FileFolderElement                         dataFolderElement,
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
     * @param properties basic properties to use
     * @return unique identifier (guid)
     * @throws ConnectorCheckedException connector has been shut down
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException unable to communicate with the repository
     * @throws UserNotAuthorizedException access problem for userId
     */
    protected String addDataFileToCatalog(DataFileProperties properties) throws ConnectorCheckedException,
                                                                                InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "addDataFileToCatalog";

        if (FileExtension.OM_ARCHIVE_FILE.getFileExtension().equals(properties.getFileExtension()))
        {
            ElementProperties replacementProperties = this.updateReplacementProperties(properties.getPathName(),
                                                                                       null);

            String propertyValue = propertyHelper.getStringProperty(connectorName,
                                                                    OpenMetadataProperty.NAME.name,
                                                                    replacementProperties,
                                                                    methodName);

            if (propertyValue != null)
            {
                properties.setResourceName(propertyValue);
            }

            propertyValue = propertyHelper.getStringProperty(connectorName,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             replacementProperties,
                                                             methodName);

            if (propertyValue != null)
            {
                properties.setResourceDescription(propertyValue);
            }

            Map<String, String> additionalProperties = properties.getAdditionalProperties();
            Map<String, String> propertyMap = propertyHelper.getStringMapFromProperty(connectorName,
                                                                                      OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                                      replacementProperties,
                                                                                      methodName);

            if (additionalProperties == null)
            {
                properties.setAdditionalProperties(propertyMap);
            }
            else if (propertyMap != null)
            {
                additionalProperties.putAll(propertyMap);
                properties.setAdditionalProperties(additionalProperties);
            }

            return super.addDataFileToCatalog(properties);
        }
        else
        {
            log.debug("ignoring file " + properties);
        }

        return null;
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
        if (FileExtension.OM_ARCHIVE_FILE.getFileExtension().equals(placeholderProperties.get(PlaceholderProperty.FILE_EXTENSION.getName())))
        {
            return super.addDataFileViaTemplate(assetTypeName,
                                                fileTemplateGUID,
                                                this.updateReplacementProperties(placeholderProperties.get(PlaceholderProperty.FILE_PATH_NAME.getName()),
                                                                                 replacementProperties),
                                                placeholderProperties);
        }
        else
        {
            log.debug("ignoring file " + placeholderProperties);
        }

        return null;
    }


    private ElementProperties updateReplacementProperties(String            pathName,
                                                          ElementProperties replacementProperties) throws ConnectorCheckedException
    {
        final String methodName = "updateReplacementProperties";

        ElementProperties archiveElementProperties = replacementProperties;

        try
        {
            FileBasedOpenMetadataArchiveStoreProvider provider = new FileBasedOpenMetadataArchiveStoreProvider();

            Connection connection = new Connection();
            connection.setConnectorType(provider.getConnectorType());

            Endpoint endpoint = new Endpoint();
            endpoint.setAddress(pathName);
            connection.setEndpoint(endpoint);

            FileBasedOpenMetadataArchiveStoreConnector connector = (FileBasedOpenMetadataArchiveStoreConnector)connectorBroker.getConnector(connection);

            OpenMetadataArchive openMetadataArchive = connector.getArchiveContents();

            if (openMetadataArchive != null)
            {
                OpenMetadataArchiveProperties properties = openMetadataArchive.getArchiveProperties();

                if (properties != null)
                {
                    archiveElementProperties = propertyHelper.addStringProperty(archiveElementProperties,
                                                                                OpenMetadataProperty.NAME.name,
                                                                                properties.getArchiveName());
                    archiveElementProperties = propertyHelper.addStringProperty(archiveElementProperties,
                                                                                OpenMetadataProperty.DESCRIPTION.name,
                                                                                properties.getArchiveDescription());

                    Map<String, String> additionalProperties = new HashMap<>();

                    additionalProperties.put("archiveGUID", properties.getArchiveGUID());
                    additionalProperties.put("archiveVersion", properties.getArchiveVersion());
                    if (properties.getArchiveType() != null)
                    {
                        additionalProperties.put("archiveType", properties.getArchiveType().getName());
                    }
                    else
                    {
                        additionalProperties.put("archiveType", null);
                    }
                    additionalProperties.put("originatorName", properties.getOriginatorName());
                    additionalProperties.put("originatorOrganization", properties.getOriginatorOrganization());
                    additionalProperties.put("originatorLicense", properties.getOriginatorLicense());
                    if (properties.getDependsOnArchives() != null)
                    {
                        additionalProperties.put("dependsOnArchives", properties.getDependsOnArchives().toString());
                    }
                    else
                    {
                        additionalProperties.put("dependsOnArchives", "[]");
                    }
                    if (properties.getCreationDate() != null)
                    {
                        additionalProperties.put("creationDate", properties.getCreationDate().toString());
                    }
                    else
                    {
                        additionalProperties.put("creationDate", null);
                    }

                    archiveElementProperties = propertyHelper.addStringMapProperty(archiveElementProperties,
                                                                                   OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                                   additionalProperties);
                }
            }
        }
        catch (ClassCastException | RepositoryErrorException | ConnectionCheckedException error)
        {
            auditLog.logMessage(methodName,
                                BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   methodName,
                                                                                                                   error.getMessage()));
        }

        return archiveElementProperties;
    }
}
