/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client;

import org.odpi.openmetadata.accessservices.assetmanager.api.ExternalReferencesInterface;
import org.odpi.openmetadata.accessservices.assetmanager.api.GlossaryExchangeInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalGlossaryLinkElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalReferenceElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryCategoryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalGlossaryElementLinkProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalGlossaryLinkProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalReferenceProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryCategoryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermActivityType;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermCategorization;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermContextDefinition;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermRelationship;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermStatus;
import org.odpi.openmetadata.accessservices.assetmanager.properties.KeyPattern;
import org.odpi.openmetadata.accessservices.assetmanager.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * ExternalReferenceExchangeClient is the client for managing external references.
 */
public class ExternalReferenceExchangeClient extends ExchangeClientBase implements ExternalReferencesInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalReferenceExchangeClient(String   serverName,
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
    public ExternalReferenceExchangeClient(String serverName,
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
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalReferenceExchangeClient(String   serverName,
                                           String   serverPlatformURLRoot,
                                           String   userId,
                                           String   password,
                                           AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalReferenceExchangeClient(String serverName,
                                           String serverPlatformURLRoot,
                                           String userId,
                                           String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a definition of a external reference.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param referenceExternalIdentifier unique identifier of the external reference in the external asset manager
     * @param referenceExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param referenceExternalIdentifierUsage optional usage description for the external identifier when calling the external asset manager
     * @param referenceExternalIdentifierSource component that issuing this request.
     * @param referenceExternalIdentifierKeyPattern  pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the external asset manager and open metadata
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
                                          String                      assetManagerGUID,
                                          String                      assetManagerName,
                                          boolean                     assetManagerIsHome,
                                          String                      referenceExternalIdentifier,
                                          String                      referenceExternalIdentifierName,
                                          String                      referenceExternalIdentifierUsage,
                                          String                      referenceExternalIdentifierSource,
                                          KeyPattern                  referenceExternalIdentifierKeyPattern,
                                          Map<String, String>         mappingProperties,
                                          String                      anchorGUID,
                                          ExternalReferenceProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return null;
    }


    /**
     * Update the definition of a external reference.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalReferenceGUID unique identifier of external reference
     * @param referenceExternalIdentifier unique identifier of the external reference in the external asset manager
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
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
                                        ExternalReferenceProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {

    }


    /**
     * Remove the definition of a external reference.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalReferenceGUID unique identifier of external reference
     * @param referenceExternalIdentifier unique identifier of the external reference in the external asset manager
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deleteExternalReference(String userId,
                                        String assetManagerGUID,
                                        String assetManagerName,
                                        String externalReferenceGUID,
                                        String referenceExternalIdentifier) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {

    }


    /**
     * Link an external reference to an object.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param attachedToGUID object linked to external references.
     * @param linkProperties description for the reference from the perspective of the object that the reference is being attached to.
     * @param externalReferenceGUID unique identifier (guid) of the external reference details.
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void linkExternalReferenceToElement(String                          userId,
                                               String                          assetManagerGUID,
                                               String                          assetManagerName,
                                               String                          attachedToGUID,
                                               String                          externalReferenceGUID,
                                               ExternalReferenceLinkProperties linkProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {

    }


    /**
     * Remove the link between a external reference and an element.  If the element is its anchor, the external reference is removed.
     *
     * @param userId the name of the calling user.
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param attachedToGUID object linked to external references.
     * @param externalReferenceGUID identifier of the external reference.
     *
     * @throws InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public void unlinkExternalReferenceFromElement(String userId,
                                                   String assetManagerGUID,
                                                   String assetManagerName,
                                                   String attachedToGUID,
                                                   String externalReferenceGUID) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
    }


    /**
     * Return information about a specific external reference.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
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
                                                               String assetManagerGUID,
                                                               String assetManagerName,
                                                               String externalReferenceGUID) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return null;
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
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ExternalReferenceElement> getExternalReferencesById(String userId,
                                                                    String assetManagerGUID,
                                                                    String assetManagerName,
                                                                    String resourceId,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        return null;
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
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ExternalReferenceElement> getExternalReferencesByURL(String userId,
                                                                     String assetManagerGUID,
                                                                     String assetManagerName,
                                                                     String url,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Retrieve the list of external reference created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ExternalReferenceElement> getExternalReferencesForAssetManager(String userId,
                                                                               String assetManagerGUID,
                                                                               String assetManagerName,
                                                                               int    startFrom,
                                                                               int    pageSize) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        return null;
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
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ExternalReferenceElement> findExternalReferences(String userId,
                                                                 String assetManagerGUID,
                                                                 String assetManagerName,
                                                                 String searchString,
                                                                 int    startFrom,
                                                                 int    pageSize) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        return null;
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
     *
     * @return links to addition information.
     *
     * @throws InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<ExternalReferenceElement> retrieveAttachedExternalReferences(String userId,
                                                                             String assetManagerGUID,
                                                                             String assetManagerName,
                                                                             String attachedToGUID,
                                                                             int    startFrom,
                                                                             int    pageSize) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        return null;
    }
}
