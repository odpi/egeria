/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceDomainInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDomainElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDomainSetElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceLevelIdentifierElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceLevelIdentifierSetElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomainProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomainSetProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceLevelIdentifierProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceLevelIdentifierSetProperties;
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
 * GovernanceDomainManager sets up the governance domains that are part of an organization governance.
 * Each governance domain describes a focus for governance.  The governance domain typically focuses on a particular set of activity
 * within the organization.  There is often overlap in the resources (assets) that each domain governs.  As a result, there is
 * often linkage between the governance definitions from different governance domains.
 */
public class GovernanceDomainManager implements GovernanceDomainInterface
{
    private String                      serverName;               /* Initialized in constructor */
    private String                      serverPlatformURLRoot;    /* Initialized in constructor */
    private GovernanceProgramRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private NullRequestBody         nullRequestBody         = new NullRequestBody();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceDomainManager(String serverName,
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
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceDomainManager(String serverName,
                                   String serverPlatformURLRoot,
                                   String userId,
                                   String password) throws InvalidParameterException
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
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceDomainManager(String   serverName,
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
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceDomainManager(String   serverName,
                                   String   serverPlatformURLRoot,
                                   String   userId,
                                   String   password,
                                   int      maxPageSize,
                                   AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that uses the supplied rest client.  This is typically used when called fro manother OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient internal client for rest calls
     * @param maxPageSize pre-initialized parameter limit
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceDomainManager(String                      serverName,
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


    /* =====================================================================================================================
     * Egeria defines a default set of governance domains for the governance program.  The method below sets them up.
     */

    /**
     * Create a governance domain set called "EgeriaStandardDomains" containing governance domain definitions for the following governance domains.
     *
     * <ul>
     *     <li>Unclassified - The governance domain is not specified - that is the definition applies to all domains - this is the default value for governance definitions within the governance program.</li>
     *     <li>Data - The data (information) governance domain</li>
     *     <li>Privacy - The data privacy governance domain</li>
     *     <li>Security - The security governance domain.</li>
     *     <li>IT Infrastructure - The IT infrastructure management governance domain.</li>
     *     <li>Software Development - The software development lifecycle (SDLC) governance domain.</li>
     *     <li>Corporate - The corporate governance domain.</li>
     *     <li>Asset Management - The physical asset management governance domain.</li>
     *     <li>Other - The governance domain is locally defined.</li>
     * </ul>
     *
     * @param userId calling user
     * @return unique identifier of the governance domain set
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createStandardGovernanceDomains(String userId) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "createStandardGovernanceDomains";

        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/standard-set";

        invalidParameterHandler.validateUserId(userId, methodName);

        GUIDResponse response = restClient.callGUIDPostRESTCall(methodName,
                                                                serverPlatformURLRoot + urlTemplate,
                                                                nullRequestBody,
                                                                serverName,
                                                                userId);

        return response.getGUID();

    }


    /* =====================================================================================================================
     * The GovernanceDomainSet entity is the top level element in a collection of related governance domains.
     */


    /**
     * Create a new metadata element to represent the root of a Governance Domain Set.
     *
     * @param userId calling user
     * @param properties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGovernanceDomainSet(String                        userId,
                                            GovernanceDomainSetProperties properties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "createGovernanceDomainSet";

        final String propertiesParameter = "properties";
        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameter, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameter, methodName);

        GUIDResponse response = restClient.callGUIDPostRESTCall(methodName,
                                                                serverPlatformURLRoot + urlTemplate,
                                                                properties,
                                                                serverName,
                                                                userId);

        return response.getGUID();
    }


    /**
     * Update the metadata element representing a Governance Domain Set.
     *
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the metadata element to remove
     * @param properties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGovernanceDomainSet(String                        userId,
                                          String                        governanceDomainSetGUID,
                                          GovernanceDomainSetProperties properties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "updateGovernanceDomainSet";

        final String guidParameter = "governanceDomainSetGUID";
        final String propertiesParameter = "properties";
        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDomainSetGUID, guidParameter, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameter, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        governanceDomainSetGUID);
    }


    /**
     * Remove the metadata element representing a governanceDomainSet.  The governance domains are not deleted.
     *
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeGovernanceDomainSet(String userId,
                                          String governanceDomainSetGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "removeGovernanceDomainSet";

        final String guidParameter = "governanceDomainSetGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDomainSetGUID, guidParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        governanceDomainSetGUID);

    }


    /**
     * Retrieve the list of governanceDomainSet metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
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
    public List<GovernanceDomainSetElement> findGovernanceDomainSets(String userId,
                                                                     String searchString,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "findGovernanceDomainSets";

        final String searchStringParameterName = "searchString";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/by-search-string?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceDomainSetListResponse restResult = restClient.callGovernanceDomainSetListPostRESTCall(methodName,
                                                                                                        serverPlatformURLRoot + urlTemplate,
                                                                                                        requestBody,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        startFrom,
                                                                                                        queryPageSize);
        return restResult.getElements();
    }


    /**
     * Retrieve the list of governanceDomainSet metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
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
    public List<GovernanceDomainSetElement> getGovernanceDomainSetsByName(String userId,
                                                                          String name,
                                                                          int    startFrom,
                                                                          int    pageSize) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "getGovernanceDomainSetsByName";

        final String nameParameterName = "name";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/by-name?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        GovernanceDomainSetListResponse restResult = restClient.callGovernanceDomainSetListPostRESTCall(methodName,
                                                                                                        serverPlatformURLRoot + urlTemplate,
                                                                                                        requestBody,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        startFrom,
                                                                                                        queryPageSize);
        return restResult.getElements();
    }




    /**
     * Retrieve the governanceDomainSet metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceDomainSetElement getGovernanceDomainSetByGUID(String userId,
                                                                   String governanceDomainSetGUID) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName = "getGovernanceDomainSetByGUID";

        final String guidParameterName = "governanceDomainSetGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(governanceDomainSetGUID, guidParameterName, methodName);

        GovernanceDomainSetResponse restResult = restClient.callGovernanceDomainSetGetRESTCall(methodName,
                                                                                         serverPlatformURLRoot + urlTemplate,
                                                                                         serverName,
                                                                                         userId,
                                                                                         governanceDomainSetGUID);
        return restResult.getElement();
    }


    /* =====================================================================================================================
     * A Governance Domain describes an area of focus in the governance program.
     */

    /**
     * Create a new metadata element to represent a governance domain.
     *
     * @param userId calling user
     * @param setGUID unique identifier of the set that this identifier belongs
     * @param properties properties about the Governance Domain to store
     *
     * @return unique identifier of the new Governance Domain
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGovernanceDomain(String                     userId,
                                         String                     setGUID,
                                         GovernanceDomainProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "createGovernanceDomain";

        final String guidParameter = "setGUID";
        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/{2}/governance-domains";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(setGUID, guidParameter, methodName);
        invalidParameterHandler.validateObject(properties, qualifiedNameParameter, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameter, methodName);

        GUIDResponse response = restClient.callGUIDPostRESTCall(methodName,
                                                                serverPlatformURLRoot + urlTemplate,
                                                                properties,
                                                                serverName,
                                                                userId,
                                                                setGUID);

        return response.getGUID();
    }


    /**
     * Update the metadata element representing a Governance Domain.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the metadata element to update
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGovernanceDomain(String                     userId,
                                       String                     governanceDomainGUID,
                                       GovernanceDomainProperties properties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "updateGovernanceDomain";

        final String guidParameter = "governanceDomainGUID";
        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/governance-domains/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDomainGUID, guidParameter, methodName);
        invalidParameterHandler.validateObject(properties, qualifiedNameParameter, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        governanceDomainGUID);
    }


    /**
     * Remove the metadata element representing a Governance Domain.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void deleteGovernanceDomain(String userId,
                                       String governanceDomainGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "deleteGovernanceDomain";

        final String guidParameter = "governanceDomainGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/governance-domains/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDomainGUID, guidParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        governanceDomainGUID);
    }


    /**
     * Retrieve the list of Governance Domain metadata elements defined for the governance program.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
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
    public List<GovernanceDomainElement> getGovernanceDomains(String userId,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "getGovernanceDomains";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/governance-domains?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        GovernanceDomainListResponse restResult = restClient.callGovernanceDomainListGetRESTCall(methodName,
                                                                                                 serverPlatformURLRoot + urlTemplate,
                                                                                                 serverName,
                                                                                                 userId,
                                                                                                 startFrom,
                                                                                                 queryPageSize);
        return restResult.getElements();
    }


    /**
     * Retrieve the list of Governance Domain metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
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
    public List<GovernanceDomainElement> findGovernanceDomains(String userId,
                                                               String searchString,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "findGovernanceDomains";

        final String searchStringParameterName = "searchString";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/governance-domains/by-search-string?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceDomainListResponse restResult = restClient.callGovernanceDomainListPostRESTCall(methodName,
                                                                                                  serverPlatformURLRoot + urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  startFrom,
                                                                                                  queryPageSize);
        return restResult.getElements();
    }


    /**
     * Return the list of governance domain sets that a governance domain belong.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the governance domain to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the sets associated with the requested governanceDomainSet
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GovernanceDomainSetElement> getSetsForGovernanceDomain(String userId,
                                                                       String governanceDomainGUID,
                                                                       int    startFrom,
                                                                       int    pageSize) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "getSetsForGovernanceDomain";

        final String guidParameterName = "governanceDomainGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/governance-domains/{2}?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDomainGUID, guidParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        GovernanceDomainSetListResponse restResult = restClient.callGovernanceDomainSetListPostRESTCall(methodName,
                                                                                                        serverPlatformURLRoot + urlTemplate,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        governanceDomainGUID,
                                                                                                        startFrom,
                                                                                                        queryPageSize);
        return restResult.getElements();
    }


    /**
     * Retrieve the list of Governance Domain metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
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
    public List<GovernanceDomainElement> getGovernanceDomainsByName(String userId,
                                                                    String name,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "getGovernanceDomainsByName";

        final String nameParameterName = "name";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/governance-domains/by-name?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        GovernanceDomainListResponse restResult = restClient.callGovernanceDomainListPostRESTCall(methodName,
                                                                                                  serverPlatformURLRoot + urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  startFrom,
                                                                                                  queryPageSize);
        return restResult.getElements();
    }


    /**
     * Retrieve the Governance Domain metadata element with the supplied unique identifier assigned when the domain description was stored in
     * the metadata repository.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceDomainElement getGovernanceDomainByGUID(String userId,
                                                             String governanceDomainGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "getGovernanceDomainByGUID";

        final String guidParameterName = "governanceDomainGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domains/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDomainGUID, guidParameterName, methodName);

        GovernanceDomainResponse restResult = restClient.callGovernanceDomainGetRESTCall(methodName,
                                                                                               serverPlatformURLRoot + urlTemplate,
                                                                                               serverName,
                                                                                               userId,
                                                                                               governanceDomainGUID);
        return restResult.getElement();
    }


    /**
     * Retrieve the Governance Domain metadata element with the supplied domain identifier.
     *
     * @param userId calling user
     * @param domainIdentifier identifier used to identify the domain
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceDomainElement getGovernanceDomainByIdentifier(String userId,
                                                                   int    domainIdentifier) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getGovernanceDomainByIdentifier";

        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domains/by-identifier/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceDomainResponse restResult = restClient.callGovernanceDomainGetRESTCall(methodName,
                                                                                         serverPlatformURLRoot + urlTemplate,
                                                                                         serverName,
                                                                                         userId,
                                                                                         domainIdentifier);
        return restResult.getElement();
    }
}
