/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.client;


import org.odpi.openmetadata.accessservices.communityprofile.api.SecurityGroupInterface;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.SecurityGroupElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.SecurityGroupProperties;
import org.odpi.openmetadata.accessservices.communityprofile.rest.ElementStubsResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.SecurityGroupResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.SecurityGroupsResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.util.List;


/**
 * SecurityManagerClient is the client for explicitly managing the security groups.
 */
public class SecurityGroupManagement extends CommunityProfileBaseClient implements SecurityGroupInterface
{
    private final NullRequestBody         nullRequestBody         = new NullRequestBody();

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException bad input parameters
     */
    public SecurityGroupManagement(String serverName,
                                   String serverPlatformURLRoot,
                                   int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);
    }


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
    public SecurityGroupManagement(String   serverName,
                                   String   serverPlatformURLRoot,
                                   AuditLog auditLog,
                                   int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog, maxPageSize);
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
     *
     * @throws InvalidParameterException bad input parameters
     */
    public SecurityGroupManagement(String serverName,
                                   String serverPlatformURLRoot,
                                   String userId,
                                   String password,
                                   int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
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
     * @throws InvalidParameterException bad input parameters
     */
    public SecurityGroupManagement(String   serverName,
                                   String   serverPlatformURLRoot,
                                   String   userId,
                                   String   password,
                                   AuditLog auditLog,
                                   int      maxPageSize) throws  InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public SecurityGroupManagement(String                     serverName,
                                   String                     serverPlatformURLRoot,
                                   CommunityProfileRESTClient restClient,
                                   int                        maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }



    /* ========================================
     * Security Groups
     */

    /**
     * Create a new security group.  The type of the definition is located in the properties.
     *
     * @param userId calling user
     * @param properties properties of the definition
     *
     * @return unique identifier of the definition
     *
     * @throws InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     * @throws PropertyServerException problem accessing the metadata service
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createSecurityGroup(String                  userId,
                                      SecurityGroupProperties properties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String   methodName = "createSecurityGroup";
        final String   urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/security-groups";

        final String   docIdParameterName = "documentIdentifier";
        final String   titleParameterName = "title";
        final String   propertiesParameterName = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getDocumentIdentifier(), docIdParameterName, methodName);
        invalidParameterHandler.validateName(properties.getTitle(), titleParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  properties,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update an existing security group.
     *
     * @param userId calling user
     * @param securityGroupGUID unique identifier of the definition to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties properties to update
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  updateSecurityGroup(String                         userId,
                                     String                         securityGroupGUID,
                                     boolean                        isMergeUpdate,
                                     SecurityGroupProperties properties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "updateSecurityGroup";
        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/security-groups/{2}/update?isMergeUpdate={3}";

        final String guidParameterName = "securityGroupGUID";
        final String docIdParameterName = "documentIdentifier";
        final String titleParameterName = "title";
        final String propertiesParameterName = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(securityGroupGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getDocumentIdentifier(), docIdParameterName, methodName);
            invalidParameterHandler.validateName(properties.getTitle(), titleParameterName, methodName);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        securityGroupGUID,
                                        isMergeUpdate);
    }


    /**
     * Delete a specific security group.
     *
     * @param userId calling user
     * @param securityGroupGUID unique identifier of the definition to remove
     *
     * @throws InvalidParameterException guid is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  deleteSecurityGroup(String userId,
                                     String securityGroupGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "deleteSecurityGroup";
        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/security-groups/{2}/delete";
        final String guidParameterName = "securityGroupGUID";

        invalidParameterHandler.validateGUID(securityGroupGUID, guidParameterName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        securityGroupGUID);
    }


    /**
     * Return the list of security groups associated with a unique distinguishedName.  In an ideal world, there should be only one.
     *
     * @param userId calling user
     * @param distinguishedName unique name of the security group
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of security groups
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    @Override
    public List<SecurityGroupElement> getSecurityGroupsForDistinguishedName(String userId,
                                                                            String distinguishedName,
                                                                            int    startFrom,
                                                                            int    pageSize) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String   methodName = "getSecurityGroupsForDistinguishedName";
        final String   urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/security-groups/for-distinguished-name/{2}?startFrom={3}&pageSize={4}";
        final String   parameterName = "distinguishedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(parameterName, parameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SecurityGroupsResponse restResult = restClient.callSecurityGroupsGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     distinguishedName,
                                                                                     startFrom,
                                                                                     queryPageSize);

        return restResult.getElementList();
    }


    /**
     * Return the elements that are governed by the supplied security group.
     *
     * @param userId calling user
     * @param securityGroupGUID unique name of the security group
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of headers for the associated elements
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    @Override
    public List<ElementStub> getElementsGovernedBySecurityGroup(String userId,
                                                                String securityGroupGUID,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getElementsGovernedBySecurityGroup";
        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/security-groups/{2}/governed-by/elements?startFrom={3}&pageSize={4}";
        final String guidParameterName = "securityGroupGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(securityGroupGUID, guidParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        ElementStubsResponse restResult = restClient.callElementStubsGetRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 serverName,
                                                                                 userId,
                                                                                 securityGroupGUID,
                                                                                 startFrom,
                                                                                 queryPageSize);

        return restResult.getElements();
    }


    /**
     * Return the list of security groups that match the search string - this can be a regular expression.
     *
     * @param userId calling user
     * @param searchString value to search for
     * @param startFrom where to start from in the list of definition results
     * @param pageSize max number of results to return in one call
     *
     * @return list of security groups
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    @Override
    public List<SecurityGroupElement> findSecurityGroups(String userId,
                                                         String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "findSecurityGroups";
        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/security-groups/by-search-string?startFrom={2}&pageSize={3}";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchString, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        SecurityGroupsResponse restResult = restClient.callSecurityGroupsPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      startFrom,
                                                                                      queryPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the userIdentity metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param securityGroupGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public SecurityGroupElement getSecurityGroupByGUID(String userId,
                                                       String securityGroupGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName                     = "getSecurityGroupByGUID";
        final String securityGroupGUIDParameterName = "securityGroupGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(securityGroupGUID, securityGroupGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/{2}";

        SecurityGroupResponse restResult = restClient.callSecurityGroupGetRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  serverName,
                                                                                  userId,
                                                                                  securityGroupGUID);

        return restResult.getElement();
    }
}
