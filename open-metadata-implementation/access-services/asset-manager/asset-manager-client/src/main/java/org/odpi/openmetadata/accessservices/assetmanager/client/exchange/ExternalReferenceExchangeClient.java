/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client.exchange;

import org.odpi.openmetadata.accessservices.assetmanager.api.exchange.ExternalReferencesInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalReferenceElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalReferenceLinkElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceProperties;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ExternalReferenceElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ExternalReferenceElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ExternalReferenceLinkElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ExternalReferenceLinkRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ExternalReferenceRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NameRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * ExternalReferenceExchangeClient is the client for managing external references.
 */
public class ExternalReferenceExchangeClient extends ExchangeClientBase implements ExternalReferencesInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalReferenceExchangeClient(String   serverName,
                                           String   serverPlatformURLRoot,
                                           AuditLog auditLog,
                                           int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog, maxPageSize);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalReferenceExchangeClient(String serverName,
                                           String serverPlatformURLRoot,
                                           int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);
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
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalReferenceExchangeClient(String   serverName,
                                           String   serverPlatformURLRoot,
                                           String   userId,
                                           String   password,
                                           AuditLog auditLog,
                                           int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog, maxPageSize);
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
    public ExternalReferenceExchangeClient(String                 serverName,
                                           String                 serverPlatformURLRoot,
                                           AssetManagerRESTClient restClient,
                                           int                    maxPageSize,
                                           AuditLog               auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalReferenceExchangeClient(String serverName,
                                           String serverPlatformURLRoot,
                                           String userId,
                                           String password,
                                           int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
    }


    /**
     * Create a definition of an external reference.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param anchorGUID optional element to link the external reference to that will act as an anchor - that is, this external reference
     *                   will be deleted when the element is deleted (once the external reference is linked to the anchor).
     * @param properties properties for an external reference
     *
     * @return unique identifier of the external reference
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createExternalReference(String                       userId,
                                          String                       assetManagerGUID,
                                          String                       assetManagerName,
                                          boolean                      assetManagerIsHome,
                                          ExternalIdentifierProperties externalIdentifierProperties,
                                          String                       anchorGUID,
                                          ExternalReferenceProperties  properties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName                  = "createExternalReference";
        final String propertiesParameterName     = "properties";
        final String qualifiedNameParameterName  = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        ExternalReferenceRequestBody requestBody = new ExternalReferenceRequestBody();
        requestBody.setElementProperties(properties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references?assetManagerIsHome={2}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the definition of an external reference.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalReferenceGUID unique identifier of external reference
     * @param referenceExternalIdentifier unique identifier of the external reference in the external asset manager
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateExternalReference(String                      userId,
                                        String                      assetManagerGUID,
                                        String                      assetManagerName,
                                        String                      externalReferenceGUID,
                                        String                      referenceExternalIdentifier,
                                        boolean                     isMergeUpdate,
                                        ExternalReferenceProperties properties,
                                        Date                        effectiveTime,
                                        boolean                     forLineage,
                                        boolean                     forDuplicateProcessing) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName                         = "updateExternalReference";
        final String externalReferenceGUIDParameterName = "externalReferenceGUID";
        final String propertiesParameterName            = "properties";
        final String qualifiedNameParameterName         = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, externalReferenceGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        if (!isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        ExternalReferenceRequestBody requestBody = new ExternalReferenceRequestBody();
        requestBody.setElementProperties(properties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   referenceExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references/{2}?isMergeUpdate={3}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        externalReferenceGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the definition of an external reference.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalReferenceGUID unique identifier of external reference
     * @param referenceExternalIdentifier unique identifier of the external reference in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deleteExternalReference(String  userId,
                                        String  assetManagerGUID,
                                        String  assetManagerName,
                                        String  externalReferenceGUID,
                                        String  referenceExternalIdentifier,
                                        Date    effectiveTime,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                         = "deleteExternalReference";
        final String externalReferenceGUIDParameterName = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, externalReferenceGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references/{2}/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, referenceExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        externalReferenceGUID);
    }


    /**
     * Link an external reference to an object.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param attachedToGUID object linked to external references.
     * @param linkProperties description for the reference from the perspective of the object that the reference is being attached to.
     * @param externalReferenceGUID unique identifier (guid) of the external reference details.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return Unique identifier for new relationship
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public String linkExternalReferenceToElement(String                          userId,
                                                 String                          assetManagerGUID,
                                                 String                          assetManagerName,
                                                 boolean                         assetManagerIsHome,
                                                 String                          attachedToGUID,
                                                 String                          externalReferenceGUID,
                                                 ExternalReferenceLinkProperties linkProperties,
                                                 Date                            effectiveTime,
                                                 boolean                         forLineage,
                                                 boolean                         forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        final String methodName                          = "linkExternalReferenceToElement";
        final String attachedToGUIDParameterName         = "attachedToGUID";
        final String externalReferenceGUIDParameterName  = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(attachedToGUID, attachedToGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, externalReferenceGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references/{2}/links/{3}?assetManagerIsHome={4}";

        ExternalReferenceLinkRequestBody requestBody = new ExternalReferenceLinkRequestBody();

        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setElementProperties(linkProperties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  externalReferenceGUID,
                                                                  attachedToGUID);

        return restResult.getGUID();
    }



    /**
     * Update the link between an external reference to an object.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param linkProperties description for the reference from the perspective of the object that the reference is being attached to.
     * @param externalReferenceLinkGUID unique identifier (guid) of the external reference details.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void updateExternalReferenceToElementLink(String                          userId,
                                                     String                          assetManagerGUID,
                                                     String                          assetManagerName,
                                                     String                          externalReferenceLinkGUID,
                                                     ExternalReferenceLinkProperties linkProperties,
                                                     Date                            effectiveTime,
                                                     boolean                         forLineage,
                                                     boolean                         forDuplicateProcessing) throws InvalidParameterException, 
                                                                                                                    PropertyServerException, 
                                                                                                                    UserNotAuthorizedException
    {
        final String methodName                          = "linkExternalReferenceToElement";
        final String externalReferenceGUIDParameterName  = "externalReferenceLinkGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceLinkGUID, externalReferenceGUIDParameterName, methodName);

        ExternalReferenceLinkRequestBody requestBody = new ExternalReferenceLinkRequestBody();

        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setElementProperties(linkProperties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references/links/{2}/update";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        externalReferenceLinkGUID);
    }


    /**
     * Remove the link between an external reference and an element.  If the element is its anchor, the external reference is removed.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalReferenceLinkGUID identifier of the external reference relationship.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void unlinkExternalReferenceFromElement(String  userId,
                                                   String  assetManagerGUID,
                                                   String  assetManagerName,
                                                   String  externalReferenceLinkGUID,
                                                   Date    effectiveTime,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName                          = "linkExternalReferenceToElement";
        final String externalReferenceGUIDParameterName  = "externalReferenceLinkGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceLinkGUID, externalReferenceGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references/links/{2}/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        externalReferenceLinkGUID);
    }


    /**
     * Retrieve the list of external references sorted in open metadata.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ExternalReferenceElement> getExternalReferences(String  userId,
                                                                String  assetManagerGUID,
                                                                String  assetManagerName,
                                                                int     startFrom,
                                                                int     pageSize,
                                                                Date    effectiveTime,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing) throws InvalidParameterException, 
                                                                                                       PropertyServerException, 
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getExternalReferences";

        invalidParameterHandler.validateUserId(userId, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references/by-type?startFrom={2}&pageSize={3}";

        ExternalReferenceElementsResponse restResult = restClient.callExternalReferencesPostRESTCall(methodName,
                                                                                                     urlTemplate,
                                                                                                     getEffectiveTimeQueryRequestBody(assetManagerGUID,
                                                                                                                                      assetManagerName,
                                                                                                                                      effectiveTime),
                                                                                                     serverName,
                                                                                                     userId,
                                                                                                     startFrom,
                                                                                                     validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of external references for this resourceId.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param resourceId unique reference id assigned by the resource owner (supports wildcards). This is the qualified name of the entity
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ExternalReferenceElement> getExternalReferencesById(String  userId,
                                                                    String  assetManagerGUID,
                                                                    String  assetManagerName,
                                                                    String  resourceId,
                                                                    int     startFrom,
                                                                    int     pageSize,
                                                                    Date    effectiveTime,
                                                                    boolean forLineage,
                                                                    boolean forDuplicateProcessing) throws InvalidParameterException, 
                                                                                                           PropertyServerException, 
                                                                                                           UserNotAuthorizedException
    {
        final String methodName        = "getExternalReferencesById";
        final String nameParameterName = "resourceId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(resourceId, nameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setName(resourceId);
        requestBody.setNameParameterName(nameParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references/by-resource-id?startFrom={2}&pageSize={3}";

        ExternalReferenceElementsResponse restResult = restClient.callExternalReferencesPostRESTCall(methodName,
                                                                                                     urlTemplate,
                                                                                                     requestBody,
                                                                                                     serverName,
                                                                                                     userId,
                                                                                                     startFrom,
                                                                                                     validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of external references for this URL.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param url URL of the external resource.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ExternalReferenceElement> getExternalReferencesByURL(String  userId,
                                                                     String  assetManagerGUID,
                                                                     String  assetManagerName,
                                                                     String  url,
                                                                     int     startFrom,
                                                                     int     pageSize,
                                                                     Date    effectiveTime,
                                                                     boolean forLineage,
                                                                     boolean forDuplicateProcessing) throws InvalidParameterException, 
                                                                                                            PropertyServerException, 
                                                                                                            UserNotAuthorizedException
    {
        final String methodName        = "getExternalReferencesByURL";
        final String nameParameterName = "url";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(url, nameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setName(url);
        requestBody.setNameParameterName(nameParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references/by-url?startFrom={2}&pageSize={3}";

        ExternalReferenceElementsResponse restResult = restClient.callExternalReferencesPostRESTCall(methodName,
                                                                                                     urlTemplate,
                                                                                                     requestBody,
                                                                                                     serverName,
                                                                                                     userId,
                                                                                                     startFrom,
                                                                                                     validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of external references for this name.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name qualifiedName or displayName of the external resource.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ExternalReferenceElement> getExternalReferencesByName(String  userId,
                                                                      String  assetManagerGUID,
                                                                      String  assetManagerName,
                                                                      String  name,
                                                                      int     startFrom,
                                                                      int     pageSize,
                                                                      Date    effectiveTime,
                                                                      boolean forLineage,
                                                                      boolean forDuplicateProcessing) throws InvalidParameterException, 
                                                                                                             PropertyServerException, 
                                                                                                             UserNotAuthorizedException
    {
        final String methodName        = "getExternalReferencesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references/by-name?startFrom={2}&pageSize={3}";

        ExternalReferenceElementsResponse restResult = restClient.callExternalReferencesPostRESTCall(methodName,
                                                                                                     urlTemplate,
                                                                                                     requestBody,
                                                                                                     serverName,
                                                                                                     userId,
                                                                                                     startFrom,
                                                                                                     validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of external reference created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ExternalReferenceElement> getExternalReferencesForAssetManager(String  userId,
                                                                               String  assetManagerGUID,
                                                                               String  assetManagerName,
                                                                               int     startFrom,
                                                                               int     pageSize,
                                                                               Date    effectiveTime,
                                                                               boolean forLineage,
                                                                               boolean forDuplicateProcessing) throws InvalidParameterException, 
                                                                                                                      UserNotAuthorizedException, 
                                                                                                                      PropertyServerException
    {
        final String methodName = "getExternalReferencesForAssetManager";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references/by-asset-manager?startFrom={2}&pageSize={3}";

        ExternalReferenceElementsResponse restResult = restClient.callExternalReferencesPostRESTCall(methodName,
                                                                                                     urlTemplate,
                                                                                                     getEffectiveTimeQueryRequestBody(assetManagerGUID,
                                                                                                                                      assetManagerName,
                                                                                                                                      effectiveTime),
                                                                                                     serverName,
                                                                                                     userId,
                                                                                                     startFrom,
                                                                                                     validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Find the external references that contain the search string - which may contain wildcards.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString regular expression (RegEx) to search for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ExternalReferenceElement> findExternalReferences(String  userId,
                                                                 String  assetManagerGUID,
                                                                 String  assetManagerName,
                                                                 String  searchString,
                                                                 int     startFrom,
                                                                 int     pageSize,
                                                                 Date    effectiveTime,
                                                                 boolean forLineage,
                                                                 boolean forDuplicateProcessing) throws InvalidParameterException, 
                                                                                                        PropertyServerException, 
                                                                                                        UserNotAuthorizedException
    {
        final String methodName                = "findExternalReferences";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references/by-search-string?startFrom={2}&pageSize={3}";

        ExternalReferenceElementsResponse restResult = restClient.callExternalReferencesPostRESTCall(methodName,
                                                                                                     urlTemplate,
                                                                                                     requestBody,
                                                                                                     serverName,
                                                                                                     userId,
                                                                                                     startFrom,
                                                                                                     validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of external references attached to the supplied object.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param attachedToGUID object linked to external reference.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ExternalReferenceLinkElement> retrieveAttachedExternalReferences(String  userId,
                                                                                 String  assetManagerGUID,
                                                                                 String  assetManagerName,
                                                                                 String  attachedToGUID,
                                                                                 int     startFrom,
                                                                                 int     pageSize,
                                                                                 Date    effectiveTime,
                                                                                 boolean forLineage,
                                                                                 boolean forDuplicateProcessing) throws InvalidParameterException, 
                                                                                                                        PropertyServerException, 
                                                                                                                        UserNotAuthorizedException
    {
        final String methodName        = "retrieveAttachedExternalReferences";
        final String guidParameterName = "attachedToGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(attachedToGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references/attached-to/{2}?startFrom={3}&pageSize={4}";

        ExternalReferenceLinkElementsResponse restResult = restClient.callExternalReferenceLinksPostRESTCall(methodName,
                                                                                                             urlTemplate,
                                                                                                             getEffectiveTimeQueryRequestBody(assetManagerGUID,
                                                                                                                                              assetManagerName,
                                                                                                                                              effectiveTime),
                                                                                                             serverName,
                                                                                                             userId,
                                                                                                             attachedToGUID,
                                                                                                             startFrom,
                                                                                                             validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Return information about a specific external reference.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalReferenceGUID unique identifier for the external reference
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return properties of the external reference
     *
     * @throws InvalidParameterException externalReferenceGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public ExternalReferenceElement getExternalReferenceByGUID(String  userId,
                                                               String  assetManagerGUID,
                                                               String  assetManagerName,
                                                               String  externalReferenceGUID,
                                                               Date    effectiveTime,
                                                               boolean forLineage,
                                                               boolean forDuplicateProcessing) throws InvalidParameterException, 
                                                                                                      UserNotAuthorizedException, 
                                                                                                      PropertyServerException
    {
        final String methodName = "getExternalReferenceByGUID";
        final String guidParameterName = "externalReferenceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/external-references/{2}/by-guid";

        ExternalReferenceElementResponse restResult = restClient.callExternalReferencePostRESTCall(methodName,
                                                                                                   urlTemplate,
                                                                                                   getEffectiveTimeQueryRequestBody(assetManagerGUID,
                                                                                                                                    assetManagerName,
                                                                                                                                    effectiveTime),
                                                                                                   serverName,
                                                                                                   userId,
                                                                                                   externalReferenceGUID);

        return restResult.getElement();
    }
}
