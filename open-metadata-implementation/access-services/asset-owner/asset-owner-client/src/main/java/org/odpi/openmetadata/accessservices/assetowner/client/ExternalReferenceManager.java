/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.client;

import org.odpi.openmetadata.accessservices.assetowner.api.ExternalReferencesInterface;
import org.odpi.openmetadata.accessservices.assetowner.client.rest.AssetOwnerRESTClient;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.ExternalReferenceElement;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.assetowner.properties.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.ExternalReferenceProperties;
import org.odpi.openmetadata.accessservices.assetowner.rest.ExternalReferenceListResponse;
import org.odpi.openmetadata.accessservices.assetowner.rest.ExternalReferenceResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * ExternalReferenceManager is the java client for managing external references and their links to all types of governance definitions.
 */
public class ExternalReferenceManager extends AssetOwnerBaseClient implements ExternalReferencesInterface
{
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
        super(serverName, serverPlatformURLRoot);
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
        super(serverName, serverPlatformURLRoot, userId, password);
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
        super(serverName, serverPlatformURLRoot, maxPageSize, auditLog);
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
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize, auditLog);
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
                                    AssetOwnerRESTClient restClient,
                                    int                         maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/external-references";
        final String propertiesParameterName = "properties";

        return super.createReferenceable(userId, properties, propertiesParameterName, urlTemplate, methodName);
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/external-references/{2}/update?isMergeUpdate={4}";
        final String guidParameterName = "externalReferenceGUID";
        final String propertiesParameterName = "properties";

        super.updateReferenceable(userId, externalReferenceGUID, guidParameterName, isMergeUpdate, properties, propertiesParameterName, urlTemplate, methodName);
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/external-references/{2}/delete";
        final String guidParameterName = "externalReferenceGUID";

        super.removeReferenceable(userId, externalReferenceGUID, guidParameterName, urlTemplate, methodName);
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/elements/{2}/external-references/{3}/link";

        final String elementGUIDParameterName = "attachedToGUID";
        final String externalReferenceGUIDParameterName = "externalReferenceGUID";

        super.setupRelationship(userId, attachedToGUID, elementGUIDParameterName, null, linkProperties, externalReferenceGUID, externalReferenceGUIDParameterName, urlTemplate, methodName);
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/elements/{2}/external-references/{3}/unlink";

        final String elementGUIDParameterName = "attachedToGUID";
        final String externalReferenceGUIDParameterName = "externalReferenceGUID";

        super.clearRelationship(userId, attachedToGUID, elementGUIDParameterName, null, externalReferenceGUID, externalReferenceGUIDParameterName, urlTemplate, methodName);
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/external-references/{2}";

        final String guidParameterName = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, guidParameterName, methodName);

        ExternalReferenceResponse restResult = restClient.callExternalReferenceGetRESTCall(methodName,
                                                                                            urlTemplate,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/external-references/by-resource-id?startFrom={2}&pageSize={3}";
        final String resourceIdParameterName = "resourceId";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(resourceId, resourceIdParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setSearchString(resourceId);
        requestBody.setSearchStringParameterName(resourceIdParameterName);

        ExternalReferenceListResponse restResult = restClient.callExternalReferenceListPostRESTCall(methodName,
                                                                                                    urlTemplate,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/external-references/by-url?startFrom={2}&pageSize={3}";
        final String urlParameterName = "resourceId";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(url);
        requestBody.setNameParameterName(urlParameterName);

        ExternalReferenceListResponse restResult = restClient.callExternalReferenceListPostRESTCall(methodName,
                                                                                                   urlTemplate,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/elements/{2}/external-references?startFrom={3}&pageSize={4}";
        final String guidParameterName = "attachedToGUID";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(attachedToGUID, guidParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        ExternalReferenceListResponse restResult = restClient.callExternalReferenceListGetRESTCall(methodName,
                                                                                                   urlTemplate,
                                                                                                   serverName,
                                                                                                   userId,
                                                                                                   attachedToGUID,
                                                                                                   startFrom,
                                                                                                   queryPageSize);

        return restResult.getElements();
    }


    /**
     * Return information about the elements linked to a externalReference.
     *
     * @param userId calling user
     * @param externalReferenceGUID unique identifier for the externalReference
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the related elements
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<RelatedElement> getElementsForExternalReference(String userId,
                                                                String externalReferenceGUID,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getElementsForExternalReference";
        final String guidParameter = "externalReferenceGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/elements/external-references/{2}?&startFrom={3}&pageSize={4}";

        return super.getRelatedElements(userId, externalReferenceGUID, guidParameter, urlTemplate, startFrom, pageSize, methodName);
    }
}
