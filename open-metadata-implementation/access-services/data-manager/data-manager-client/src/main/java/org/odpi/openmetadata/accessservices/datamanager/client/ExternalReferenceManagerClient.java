/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.client;

import org.odpi.openmetadata.accessservices.datamanager.api.ExternalReferenceManagerInterface;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ExternalReferenceElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;

import java.util.List;

/**
 * ExternalReferenceManagerClient supports the APIs to maintain links to external documentation and
 * other resources.
 */
public class ExternalReferenceManagerClient implements ExternalReferenceManagerInterface
{
    private static final String urlTemplatePrefix  = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/external-references";

    String   serverName;               /* Initialized in constructor */
    String   serverPlatformURLRoot;    /* Initialized in constructor */
    AuditLog auditLog = null;          /* Initialized in constructor */

    InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    DataManagerRESTClient   restClient;               /* Initialized in constructor */


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalReferenceManagerClient(String   serverName,
                                          String   serverPlatformURLRoot,
                                          AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalReferenceManagerClient(String serverName,
                                          String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot);
    }



    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalReferenceManagerClient(String serverName,
                                          String serverPlatformURLRoot,
                                          String userId,
                                          String password) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }



    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalReferenceManagerClient(String   serverName,
                                          String   serverPlatformURLRoot,
                                          String   userId,
                                          String   password,
                                          AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalReferenceManagerClient(String                serverName,
                                          String                serverPlatformURLRoot,
                                          DataManagerRESTClient restClient,
                                          int                   maxPageSize,
                                          AuditLog              auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;

        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = restClient;
    }

    /**
     * Create a definition of a external reference.
     *
     * @param userId calling user
     * @param anchorGUID optional element to link the external reference to that will act as an anchor - that is, this external reference
     *                   will be deleted when the element is deleted
     * @param linkId identifier for the reference from the perspective of the anchor object that the reference is being attached to.
     * @param linkDescription description for the reference from the perspective of the anchor object that the reference is being attached to.
     * @param properties properties for a external reference
     *
     * @return unique identifier of the external reference
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createExternalReference(String                      userId,
                                          String                      anchorGUID,
                                          String                      linkId,
                                          String                      linkDescription,
                                          ExternalReferenceProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                     = "createExternalReference";
        final String parentElementGUIDParameterName = "anchorGUID";
        final String propertiesParameterName        = "properties";
        final String qualifiedNameParameterName     = "properties.resourceId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix;

        LinkedExternalReferenceRequestBody requestBody = new LinkedExternalReferenceRequestBody();

        requestBody.setAnchorGUID(anchorGUID);
        requestBody.setLinkId(linkId);
        requestBody.setLinkDescription(linkDescription);
        requestBody.setProperties(properties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update the definition of a external reference.
     *
     * @param userId calling user
     * @param externalReferenceGUID unique identifier of external reference
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateExternalReference(String                      userId,
                                        String                      externalReferenceGUID,
                                        boolean                     isMergeUpdate,
                                        ExternalReferenceProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName               = "updateExternalReference";
        final String elementGUIDParameterName = "externalReferenceGUID";
        final String propertiesParameterName  = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}?isMergeUpdate={3}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        externalReferenceGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the definition of a external reference.
     *
     * @param userId calling user
     * @param externalReferenceGUID unique identifier of external reference
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deleteExternalReference(String userId,
                                        String externalReferenceGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName               = "deleteExternalReference";
        final String elementGUIDParameterName = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
                                        userId,
                                        externalReferenceGUID);
    }


    /**
     * Link an external reference to an object.
     *
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external references.
     * @param linkId identifier for the reference from the perspective of the object that the reference is being attached to.
     * @param linkDescription description for the reference from the perspective of the object that the reference is being attached to.
     * @param externalReferenceGUID unique identifier (guid) of the external reference details.
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void linkExternalReferenceToElement(String userId,
                                               String attachedToGUID,
                                               String linkId,
                                               String linkDescription,
                                               String externalReferenceGUID) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName                     = "linkExternalReferenceToElement";
        final String attachedToGUIDParameterName    = "attachedToGUID";
        final String externalReferenceGUIDParameterName = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(attachedToGUID, attachedToGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, externalReferenceGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/link-to/{3}";

        ExternalReferenceLinkRequestBody requestBody = new ExternalReferenceLinkRequestBody();

        requestBody.setLinkId(linkId);
        requestBody.setLinkDescription(linkDescription);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        externalReferenceGUID,
                                        attachedToGUID);
    }


    /**
     * Remove the link between a external reference and an element.  If the element is its anchor, the external reference is removed.
     *
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external references.
     * @param externalReferenceGUID identifier of the external reference.
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void unlinkExternalReferenceFromElement(String userId,
                                                   String attachedToGUID,
                                                   String externalReferenceGUID) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName                     = "unlinkExternalReferenceFromElement";
        final String attachedToGUIDParameterName    = "attachedToGUID";
        final String externalReferenceGUIDParameterName = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(attachedToGUID, attachedToGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, externalReferenceGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}/unlink-from/{3}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
                                        userId,
                                        externalReferenceGUID,
                                        attachedToGUID);
    }


    /**
     * Return information about a specific external reference.
     *
     * @param userId calling user
     * @param externalReferenceGUID unique identifier for the external reference
     *
     * @return properties of the external reference
     *
     * @throws InvalidParameterException externalReferenceGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public ExternalReferenceElement getExternalReferenceByGUID(String userId,
                                                               String externalReferenceGUID) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName        = "getExternalReferenceByGUID";
        final String guidParameterName = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/{2}";

        ExternalReferenceResponse restResult = restClient.callExternalReferenceGetRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           externalReferenceGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the list of external references for this resourceId (qualified name).
     *
     * @param userId the name of the calling user.
     * @param resourceId unique reference id assigned by the resource owner (supports wildcards). This is the qualified name of the entity
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ExternalReferenceElement> findExternalReferencesById(String userId,
                                                                     String resourceId,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName        = "findExternalReferencesById";
        final String nameParameterName = "resourceId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(resourceId, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);


        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/by-resource-id?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(resourceId);
        requestBody.setNamePropertyName(nameParameterName);

        ExternalReferencesResponse restResult = restClient.callExternalReferencesPostRESTCall(methodName,
                                                                                              urlTemplate,
                                                                                              requestBody,
                                                                                              serverName,
                                                                                              userId,
                                                                                              startFrom,
                                                                                              validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of external references for this URL.
     *
     * @param userId the name of the calling user.
     * @param url URL of the external resource.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ExternalReferenceElement> findExternalReferencesByURL(String userId,
                                                                      String url,
                                                                      int    startFrom,
                                                                      int    pageSize) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName        = "findExternalReferencesByURL";
        final String nameParameterName = "url";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(url, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/by-url?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(url);
        requestBody.setNamePropertyName(nameParameterName);

        ExternalReferencesResponse restResult = restClient.callExternalReferencesPostRESTCall(methodName,
                                                                                              urlTemplate,
                                                                                              requestBody,
                                                                                              serverName,
                                                                                              userId,
                                                                                              startFrom,
                                                                                              validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of external references attached to the supplied object.
     *
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external reference.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ExternalReferenceElement> retrieveAttachedExternalReferences(String userId,
                                                                             String attachedToGUID,
                                                                             int    startFrom,
                                                                             int    pageSize) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName        = "retrieveAttachedExternalReferences";
        final String guidParameterName = "attachedToGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(attachedToGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);


        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/attached-to/{2}?startFrom={3}&pageSize={4}";

        ExternalReferencesResponse restResult = restClient.callExternalReferencesGetRESTCall(methodName,
                                                                                             urlTemplate,
                                                                                             serverName,
                                                                                             userId,
                                                                                             attachedToGUID,
                                                                                             startFrom,
                                                                                             validatedPageSize);

        return restResult.getElements();
    }

}
