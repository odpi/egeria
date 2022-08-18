/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.ExternalReferencesInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.ExternalReferenceElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReferenceProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * ExternalReferenceManager is the java client for managing external references and their links to all types of governance definitions.
 */
public class ExternalReferenceManager implements ExternalReferencesInterface
{
    private final String                      serverName;               /* Initialized in constructor */
    private final String                      serverPlatformURLRoot;    /* Initialized in constructor */
    private final GovernanceProgramRESTClient restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final NullRequestBody         nullRequestBody         = new NullRequestBody();

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException bad input parameters
     */
    public ExternalReferenceManager(String serverName,
                                    String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    public ExternalReferenceManager(String     serverName,
                                    String     serverPlatformURLRoot,
                                    String     userId,
                                    String     password) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }



    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException bad input parameters
     */
    public ExternalReferenceManager(String   serverName,
                                    String   serverPlatformURLRoot,
                                    int      maxPageSize,
                                    AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException bad input parameters
     */
    public ExternalReferenceManager(String     serverName,
                                    String     serverPlatformURLRoot,
                                    String     userId,
                                    String     password,
                                    int        maxPageSize,
                                    AuditLog   auditLog) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that uses the supplied rest client.  This is typically used when called from another OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient internal client for rest calls
     * @param maxPageSize pre-initialized parameter limit
     *
     * @throws InvalidParameterException bad input parameters
     */
    public ExternalReferenceManager(String                      serverName,
                                    String                      serverPlatformURLRoot,
                                    GovernanceProgramRESTClient restClient,
                                    int                         maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = restClient;
    }


    /**
     * Create a definition of an external reference.
     *
     * @param userId calling user
     * @param anchorGUID optional element to link the external reference to that will act as an anchor - that is, this external reference
     *                   will be deleted when the element is deleted (once the external reference is linked to the anchor).
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
                                          ExternalReferenceProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "createExternalReference";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/external-references";

        final String qualifiedNameParameterName = "qualifiedName";
        final String uriParameterName = "uri";
        final String propertiesParameterName = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateName(properties.getURI(), uriParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  properties,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update the definition of an external reference.
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
        final String methodName = "updateExternalReference";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/external-references/{2}/update?isMergeUpdate={4}";

        final String qualifiedNameParameterName = "qualifiedName";
        final String guidParameterName = "externalReferenceGUID";
        final String uriParameterName = "uri";
        final String propertiesParameterName = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
            invalidParameterHandler.validateName(properties.getURI(), uriParameterName, methodName);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        externalReferenceGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the definition of an external reference.
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
        final String methodName = "deleteExternalReference";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/external-references/{2}/delete";
        final String guidParameterName = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        externalReferenceGUID);
    }


    /**
     * Link an external reference to an object.
     *
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external references.
     * @param externalReferenceGUID unique identifier (guid) of the external reference details.
     * @param linkProperties description for the reference from the perspective of the object that the reference is being attached to.
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void linkExternalReferenceToElement(String                          userId,
                                               String                          attachedToGUID,
                                               String                          externalReferenceGUID,
                                               ExternalReferenceLinkProperties linkProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "linkExternalReferenceToElement";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/external-references/{2}/elements/{3}/link";

        final String elementGUIDParameterName = "attachedToGUID";
        final String externalReferenceGUIDParameterName = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(attachedToGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, externalReferenceGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        linkProperties,
                                        serverName,
                                        userId,
                                        externalReferenceGUID,
                                        attachedToGUID);
    }



    /**
     * Remove the link between an external reference and an element.  If the element is its anchor, the external reference is removed.
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
        final String methodName = "unlinkExternalReferenceToElement";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/external-references/{2}/elements/{3}/unlink";

        final String elementGUIDParameterName = "attachedToGUID";
        final String externalReferenceGUIDParameterName = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(attachedToGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, externalReferenceGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
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
        final String methodName = "getExternalReferenceByGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/external-references/{2}";

        final String guidParameterName = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, guidParameterName, methodName);

        ExternalReferenceResponse restResult = restClient.callExternalReferenceGetRESTCall(methodName,
                                                                                           serverPlatformURLRoot + urlTemplate,
                                                                                           serverName,
                                                                                           userId,
                                                                                           externalReferenceGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the list of external references for this resourceId.
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
        final String methodName = "findExternalReferencesById";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/external-references/by-resource-id?startFrom={2}&pageSize={3}";
        final String resourceIdParameterName = "resourceId";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(resourceId, resourceIdParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setSearchString(resourceId);
        requestBody.setSearchStringParameterName(resourceIdParameterName);

        ExternalReferenceListResponse restResult = restClient.callExternalReferenceListPostRESTCall(methodName,
                                                                                                    serverPlatformURLRoot + urlTemplate,
                                                                                                    requestBody,
                                                                                                    serverName,
                                                                                                    userId,
                                                                                                    startFrom,
                                                                                                    queryPageSize);

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
    public List<ExternalReferenceElement> getExternalReferencesByURL(String userId,
                                                                     String url,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getExternalReferencesByURL";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/external-references/by-url?startFrom={2}&pageSize={3}";
        final String urlParameterName = "resourceId";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(url);
        requestBody.setNameParameterName(urlParameterName);

        ExternalReferenceListResponse restResult = restClient.callExternalReferenceListPostRESTCall(methodName,
                                                                                                   serverPlatformURLRoot + urlTemplate,
                                                                                                   requestBody,
                                                                                                   serverName,
                                                                                                   userId,
                                                                                                   startFrom,
                                                                                                   queryPageSize);

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
        final String methodName = "retrieveAttachedExternalReferences";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/external-references/for-element/{2}?startFrom={3}&pageSize={4}";
        final String guidParameterName = "attachedToGUID";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(attachedToGUID, guidParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        ExternalReferenceListResponse restResult = restClient.callExternalReferenceListGetRESTCall(methodName,
                                                                                                   serverPlatformURLRoot + urlTemplate,
                                                                                                    serverName,
                                                                                                    userId,
                                                                                                    attachedToGUID,
                                                                                                    startFrom,
                                                                                                    queryPageSize);

        return restResult.getElements();
    }
}
