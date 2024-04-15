/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.client;


import org.odpi.openmetadata.accessservices.digitalarchitecture.api.ManageTemplates;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.rest.DigitalArchitectureRESTClient;
import org.odpi.openmetadata.accessservices.digitalarchitecture.rest.TemplateClassificationRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Map;

/**
 * TemplateManager provides specialist methods for managing templates
 */
public class TemplateManager extends DigitalArchitectureClientBase implements ManageTemplates
{

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public TemplateManager(String   serverName,
                           String   serverPlatformURLRoot,
                           int      maxPageSize,
                           AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize, auditLog);
    }

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public TemplateManager(String serverName,
                           String serverPlatformURLRoot,
                           int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);
    }

    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public TemplateManager(String   serverName,
                           String   serverPlatformURLRoot,
                           String   userId,
                           String   password,
                           int      maxPageSize,
                           AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize, auditLog);
    }

    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public TemplateManager(String serverName,
                           String serverPlatformURLRoot,
                           String userId,
                           String password,
                           int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
    }

    /**
     * Create a new client that is going to be used in an OMAG Server (view service or integration service typically).
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient            client that issues the REST API calls
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public TemplateManager(String                        serverName,
                           String                        serverPlatformURLRoot,
                           DigitalArchitectureRESTClient restClient,
                           int                           maxPageSize,
                           AuditLog                      auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /**
     * Classify an element as suitable to be used as a template for cataloguing elements of a similar types.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element to classify
     * @param name name of the template
     * @param versionIdentifier version identifier for the template eg 1.0
     * @param description description of when, where and how to use the template
     * @param additionalProperties any additional properties
     *
     * @throws InvalidParameterException element or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void addTemplateClassification(String              userId,
                                          String              elementGUID,
                                          String              name,
                                          String              versionIdentifier,
                                          String              description,
                                          Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String   methodName = "addTemplateClassification";
        final String   elementGUIDParameter = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/elements/{2}/template-classification";

        TemplateClassificationRequestBody requestBody = new TemplateClassificationRequestBody();

        requestBody.setName(name);
        requestBody.setVersionIdentifier(versionIdentifier);
        requestBody.setDescription(description);
        requestBody.setAdditionalProperties(additionalProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        elementGUID);
    }


    /**
     * Remove the classification that indicates that this element can be used as a template.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element to declassify
     *
     * @throws InvalidParameterException element or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeTemplateClassification(String userId,
                                             String elementGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String   methodName = "removeTemplateClassification";
        final String   elementGUIDParameter = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/elements/{2}/template-classification/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        elementGUID);
    }
}
