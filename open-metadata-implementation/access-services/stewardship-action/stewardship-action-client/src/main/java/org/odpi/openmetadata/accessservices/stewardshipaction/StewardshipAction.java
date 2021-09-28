/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.stewardshipaction;

import org.odpi.openmetadata.accessservices.stewardshipaction.api.DuplicateManagementInterface;
import org.odpi.openmetadata.accessservices.stewardshipaction.metadataelements.DuplicateElement;
import org.odpi.openmetadata.accessservices.stewardshipaction.metadataelements.ElementStub;
import org.odpi.openmetadata.accessservices.stewardshipaction.rest.DuplicatesRequestBody;
import org.odpi.openmetadata.accessservices.stewardshipaction.rest.StewardshipActionRESTClient;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.ConnectedAssetClientBase;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;


/**
 * StewardshipAction provides the generic client-side interface for the Asset Owner Open Metadata Access Service (OMAS).
 * There are other clients that provide specialized methods for specific types of Asset.
 *
 * This client is initialized with the URL and name of the server that is running the Asset Owner OMAS.
 * This server is responsible for locating and managing the asset owner's definitions exchanged with this client.
 */
public class StewardshipAction extends ConnectedAssetClientBase implements DuplicateManagementInterface

{
    protected StewardshipActionRESTClient restClient;               /* Initialized in constructor */

    private static final String  serviceURLName = "stewardship-action";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipAction(String   serverName,
                             String   serverPlatformURLRoot,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);

        this.restClient = new StewardshipActionRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipAction(String serverName,
                             String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);

        this.restClient = new StewardshipActionRESTClient(serverName, serverPlatformURLRoot);
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
    public StewardshipAction(String   serverName,
                             String   serverPlatformURLRoot,
                             String   userId,
                             String   password,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);

        this.restClient = new StewardshipActionRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
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
    public StewardshipAction(String serverName,
                             String serverPlatformURLRoot,
                             String userId,
                             String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);

        this.restClient = new StewardshipActionRESTClient(serverName, serverPlatformURLRoot, userId, password);
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
    public StewardshipAction(String               serverName,
                             String               serverPlatformURLRoot,
                             StewardshipActionRESTClient restClient,
                             int                  maxPageSize,
                             AuditLog             auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);

        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = restClient;
    }



    /*
     * ==============================================
     * DuplicateManagementInterface
     * ==============================================
     */

    /**
     * Create a simple relationship between two elements in an Asset description (typically the asset itself or
     * attributes in their schema).
     *
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  linkElementsAsDuplicates(String userId,
                                          String element1GUID,
                                          String element2GUID,
                                          int    statusIdentifier,
                                          String steward,
                                          String stewardTypeName,
                                          String stewardPropertyName,
                                          String source,
                                          String notes) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName = "linkElementsAsDuplicates";

        final String element1GUIDParameter = "element1GUID";
        final String element2GUIDParameter = "element2GUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/elements/{2}/duplicate-of/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(element1GUID, element1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(element2GUID, element2GUIDParameter, methodName);

        DuplicatesRequestBody requestBody = new DuplicatesRequestBody();

        requestBody.setStatusIdentifier(statusIdentifier);
        requestBody.setSteward(steward);
        requestBody.setStewardTypeName(stewardTypeName);
        requestBody.setStewardPropertyName(stewardPropertyName);
        requestBody.setSource(source);
        requestBody.setNotes(notes);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        element1GUID,
                                        element2GUID);
    }


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  unlinkElementsAsDuplicates(String userId,
                                            String element1GUID,
                                            String element2GUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "unlinkElementsAsDuplicates";

        final String element1GUIDParameter = "element1GUID";
        final String element2GUIDParameter = "element2GUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/asset-owner/users/{1}/elements/{2}/duplicate-of/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(element1GUID, element1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(element2GUID, element2GUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        element1GUID,
                                        element2GUID);
    }


    /**
     * List the elements that are linked as peer duplicates to the requested element.
     *
     * @param userId calling user
     * @param elementGUID element to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of linked duplicates
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<DuplicateElement> getPeerDuplicates(String userId,
                                                    String elementGUID,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return null;
    }


    /**
     * Mark an element as a consolidated duplicate and link it to the contributing elements.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of the element that contains the consolidated information from a collection of elements
     *                                  that are all duplicates of one another.
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param contributingElementGUIDs list of unique identifiers of the elements that this consolidated element represents
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  markAsConsolidatedDuplicate(String       userId,
                                             String       consolidatedDuplicateGUID,
                                             int          statusIdentifier,
                                             String       steward,
                                             String       stewardTypeName,
                                             String       stewardPropertyName,
                                             String       source,
                                             String       notes,
                                             List<String> contributingElementGUIDs) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {

    }


    /**
     * Update the stewardship properties of an element that is marked as a consolidated duplicate.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of the element that contains the consolidated information from a collection of elements
     *                                  that are all duplicates of one another.
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  updateConsolidatedDuplicate(String userId,
                                             String consolidatedDuplicateGUID,
                                             int    statusIdentifier,
                                             String steward,
                                             String stewardTypeName,
                                             String stewardPropertyName,
                                             String source,
                                             String notes) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {

    }


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of consolidated duplicate
     * @param contributingElementGUID unique identifier of duplicate element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  linkElementToConsolidatedDuplicate(String userId,
                                                    String consolidatedDuplicateGUID,
                                                    int    statusIdentifier,
                                                    String steward,
                                                    String stewardTypeName,
                                                    String stewardPropertyName,
                                                    String source,
                                                    String notes,
                                                    String contributingElementGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {

    }

    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of consolidated duplicate
     * @param contributingElementGUID unique identifier of duplicate element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  unlinkElementFromConsolidatedDuplicate(String userId,
                                                        String consolidatedDuplicateGUID,
                                                        String contributingElementGUID) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {

    }


    /**
     * List the elements that are contributing to a consolidating duplicate element.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID element to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of contributing duplicates
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getContributingDuplicates(String userId,
                                                       String consolidatedDuplicateGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return null;
    }


    /**
     * Return details of the consolidated duplicate for a requested element.
     *
     * @param userId calling user
     * @param elementGUID element to query
     *
     * @return header of consolidated duplicated or null if none
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public ElementStub getContributingDuplicates(String userId,
                                                 String elementGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return null;
    }


    /**
     * Remove the consolidated duplicate element and the links to the elements that contributed to its values.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of element to remove
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  removeConsolidatedDuplicate(String userId,
                                             String elementGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {

    }
}
