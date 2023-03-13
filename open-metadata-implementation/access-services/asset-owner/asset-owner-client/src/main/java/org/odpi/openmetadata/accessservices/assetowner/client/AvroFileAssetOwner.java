/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.client;

import org.odpi.openmetadata.accessservices.assetowner.api.AssetOnboardingAvroFileInterface;
import org.odpi.openmetadata.accessservices.assetowner.client.rest.AssetOwnerRESTClient;
import org.odpi.openmetadata.accessservices.assetowner.rest.NewFileAssetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;


/**
 * AvroFileAssetOwner provides specialist methods for working with Avro files.
 * This includes accessing the file and attempting to read the schema to catalog
 * its internal structure.  The file is opened client-side so that the metadata server does not
 * need access to the file contents.
 *
 */
public class AvroFileAssetOwner extends AssetOwner implements AssetOnboardingAvroFileInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request and an audit log.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AvroFileAssetOwner(String   serverName,
                              String   serverPlatformURLRoot,
                              AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AvroFileAssetOwner(String serverName,
                              String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     * There is also an audit log destination.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AvroFileAssetOwner(String   serverName,
                              String   serverPlatformURLRoot,
                              String   userId,
                              String   password,
                              AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AvroFileAssetOwner(String serverName,
                              String serverPlatformURLRoot,
                              String userId,
                              String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server (view service or integration service typically).
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AvroFileAssetOwner(String               serverName,
                              String               serverPlatformURLRoot,
                              AssetOwnerRESTClient restClient,
                              int                  maxPageSize,
                              AuditLog             auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);

        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = restClient;
    }



    /*
     * ==============================================
     * AssetOnboardingFileSystem
     * ==============================================
     */


    /**
     * Add a simple asset description linked to a connection object for a Avro file.
     *
     * @param userId calling user (assumed to be the owner)
     * @param displayName display name for the file in the catalog
     * @param description description of the file in the catalog
     * @param fullPath full path of the file - used to access the file through the connector
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<String> addAvroFileToCatalog(String    userId,
                                             String    displayName,
                                             String    description,
                                             String    fullPath) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   methodName = "addAvroFileToCatalog";
        final String   pathParameter = "fullPath";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/assets/data-files/avro";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(fullPath, pathParameter, methodName);

        NewFileAssetRequestBody requestBody = new NewFileAssetRequestBody();
        requestBody.setName(displayName);
        requestBody.setDescription(description);
        requestBody.setFullPath(fullPath);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId);

        return restResult.getGUIDs();
    }
}
