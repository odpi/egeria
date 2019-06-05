/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server;

import org.odpi.openmetadata.accessservices.assetowner.rest.NewFileAssetRequestBody;
import org.odpi.openmetadata.adapters.connectors.structuredfile.StructuredFileStoreProvider;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.AssetHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.SchemaTypeHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * AssetOnboardingRESTServices provides the server-side implementation of the Asset Owner OMAS AssetOnboardingInterface
 * for specialized assets.
 */
public class AssetOnboardingRESTServices
{
    private static AssetOwnerInstanceHandler   instanceHandler     = new AssetOwnerInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(AssetOnboardingRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public AssetOnboardingRESTServices()
    {
    }


    /**
     * Add a simple asset description linked to a connection object for a CSV file.
     *
     * @param serverName name of calling server
     * @param userId calling user (assumed to be the owner)
     * @param requestBody parameters for the new asset
     *
     * @return unique identifier (guid) of the asset or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GUIDResponse addCSVFileToCatalog(String                  serverName,
                                            String                  userId,
                                            NewFileAssetRequestBody requestBody)
    {
        final String        methodName = "addCSVFileToCatalog";

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();
        OMRSAuditLog auditLog = null;

        try
        {
            if (requestBody != null)
            {
                AssetHandler  assetHandler  = instanceHandler.getAssetHandler(userId, serverName, methodName);
                Asset         asset         = assetHandler.createEmptyAsset(AssetMapper.CSV_FILE_TYPE_GUID,
                                                                            AssetMapper.CSV_FILE_TYPE_NAME);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                asset.setDisplayName(requestBody.getDisplayName());
                asset.setDescription(requestBody.getDescription());
                asset.setQualifiedName(AssetMapper.CSV_FILE_TYPE_NAME + ":" + requestBody.getFullPath());

                SchemaType            schemaType    = null;
                List<SchemaAttribute> schemaAttributes = null;

                Character    delimiterCharacter = requestBody.getDelimiterCharacter();
                Character    quoteCharacter = requestBody.getQuoteCharacter();


                if (requestBody.getColumnHeaders() != null)
                {
                    SchemaTypeHandler schemaTypeHandler = instanceHandler.getSchemaTypeHandler(userId, serverName, methodName);

                    schemaType  = schemaTypeHandler.getTabularSchemaType(asset.getQualifiedName(),
                                                                         asset.getDisplayName(),
                                                                         userId,
                                                                         "CSVFile",
                                                                         requestBody.getColumnHeaders());

                    schemaAttributes = schemaTypeHandler.getTabularSchemaColumns(schemaType.getQualifiedName(),
                                                                                 requestBody.getColumnHeaders());
                }

                if (delimiterCharacter == null)
                {
                    delimiterCharacter = ',';
                }

                if (quoteCharacter == null)
                {
                    quoteCharacter = '\"';
                }

                Map<String, Object>  extendedProperties = new HashMap<>();
                extendedProperties.put(AssetMapper.DELIMITER_CHARACTER_PROPERTY_NAME, delimiterCharacter);
                extendedProperties.put(AssetMapper.QUOTE_CHARACTER_PROPERTY_NAME, quoteCharacter);
                asset.setExtendedProperties(extendedProperties);

                response.setGUID(assetHandler.saveAsset(userId,
                                                        asset,
                                                        schemaType,
                                                        schemaAttributes,
                                                        getStructuredFileConnection(requestBody.getFullPath(),
                                                                                    requestBody.getColumnHeaders(),
                                                                                    delimiterCharacter,
                                                                                    quoteCharacter)));
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }





    /**
     * This method creates a connection.  The connection object provides the OCF with the properties to create the
     * connector and the properties needed by the connector to access the asset.
     *
     * The Connection object includes a Connector Type object.  This defines the type of connector to create.
     * The Connection object also includes an Endpoint object.  This is used by the connector to locate and connect
     * to the Asset.
     *
     * @param fileName name of the file to connect to
     * @param columnHeaders boolean parameter defining if the column headers are included in the file
     * @return connection object
     */
    private Connection getStructuredFileConnection(String            fileName,
                                                   List<String>      columnHeaders,
                                                   Character         delimiterCharacter,
                                                   Character         quoteCharacter)
    {
        final String endpointGUID      = "8bf8f5fa-b5d8-40e1-a00e-e4a0c59fd6c0";
        final String connectorTypeGUID = "2e1556a3-908f-4303-812d-d81b48b19bab";
        final String connectionGUID    = "b9af734f-f005-4085-9975-bf46c67a099a";

        final String endpointDescription = "File name.";

        String endpointName    = "StructuredFileStore.Endpoint." + fileName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(Endpoint.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(fileName);


        final String connectorTypeDescription   = "StructuredFileStore connector type.";
        final String connectorTypeJavaClassName = StructuredFileStoreProvider.class.getName();

        String connectorTypeName = "StructuredFileStore.ConnectorType.Test";

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "StructuredFileStore connection.";

        String connectionName = fileName + "Structured File Store Connection";

        Connection connection = new Connection();

        connection.setType(Connection.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);

        Map<String, Object>  configurationProperties = new HashMap<>();

        if (delimiterCharacter != null)
        {
            configurationProperties.put(StructuredFileStoreProvider.delimiterCharacterProperty, delimiterCharacter);
        }

        if (quoteCharacter != null)
        {
            configurationProperties.put(StructuredFileStoreProvider.quoteCharacterProperty, quoteCharacter);
        }

        if (columnHeaders != null)
        {
            configurationProperties.put(StructuredFileStoreProvider.columnNamesProperty, columnHeaders);
        }

        if (! configurationProperties.isEmpty())
        {
            connection.setConfigurationProperties(configurationProperties);
        }

        return connection;
    }
}
