/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceProgramReviewInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.*;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;


/**
 * The GovernanceProgramReviewInterface supports the periodic review of the governance program.
 * This includes looking at the metrics and the governance zones.
 */
public class GovernanceProgramReviewManager implements GovernanceProgramReviewInterface
{
    private String                      serverName;               /* Initialized in constructor */
    private String                      serverPlatformURLRoot;    /* Initialized in constructor */
    private GovernanceProgramRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceProgramReviewManager(String serverName,
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
    public GovernanceProgramReviewManager(String     serverName,
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
    public GovernanceProgramReviewManager(String   serverName,
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
    public GovernanceProgramReviewManager(String     serverName,
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
     * Create a new client that uses the supplied rest client.  This is typically used when called fro manother OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient internal client for rest calls
     * @param maxPageSize pre-initialized parameter limit
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceProgramReviewManager(String                      serverName,
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
     * Return the list of governance definitions associated with a particular governance domain.
     *
     * @param userId calling user
     * @param typeName option type name to restrict retrieval to a specific type
     * @param domainIdentifier identifier of the governance domain - 0 = all domains
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance definitions
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    @Override
    public List<GovernanceDefinitionElement> getGovernanceDefinitionsForDomain(String userId,
                                                                               String typeName,
                                                                               int    domainIdentifier,
                                                                               int    startFrom,
                                                                               int    pageSize) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String   methodName = "getGovernanceDefinitionsForDomain";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-definitions/for-domain?domainIdentifier={2}&typeName={3}&startFrom={4}&pageSize={5}";

        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        GovernanceDefinitionListResponse restResult = restClient.callGovernanceDefinitionListGetRESTCall(methodName,
                                                                                                         serverPlatformURLRoot + urlTemplate,
                                                                                                         serverName,
                                                                                                         userId,
                                                                                                         domainIdentifier,
                                                                                                         typeName,
                                                                                                         startFrom,
                                                                                                         queryPageSize);

        return restResult.getElements();
    }


    /**
     * Return the list of governance definitions associated with a unique docId.  In an ideal world, the should be only one.
     *
     * @param userId calling user
     * @param typeName option types name to restrict retrieval to a specific type
     * @param docId unique name of the governance definition
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance definitions
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    @Override
    public List<GovernanceDefinitionElement> getGovernanceDefinitionsForDocId(String userId,
                                                                              String typeName,
                                                                              String docId,
                                                                              int    startFrom,
                                                                              int    pageSize) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String   methodName = "getGovernanceDefinitionsForDocId";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-definitions/for-document-id/{2}?startFrom={3}&pageSize={4}";
        final String   docIdParameterName = "docId";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(docIdParameterName, docIdParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        GovernanceDefinitionListResponse restResult = restClient.callGovernanceDefinitionListGetRESTCall(methodName,
                                                                                                         serverPlatformURLRoot + urlTemplate,
                                                                                                         serverName,
                                                                                                         userId,
                                                                                                         docId,
                                                                                                         startFrom,
                                                                                                         queryPageSize);

        return restResult.getElements();
    }


    /**
     * Return the governance definition associated with a unique identifier and the other governance definitions linked to it.
     *
     * @param userId calling user
     * @param governanceDefinitionGUID unique identifier of the governance definition
     *
     * @return governance definition and its linked elements
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    @Override
    public GovernanceDefinitionGraph getGovernanceDefinitionInContext(String userId,
                                                                      String governanceDefinitionGUID) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        final String methodName = "getGovernanceDefinitionInContext";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-definitions/{2}/in-context";
        final String guidParameterName = "governanceDefinitionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionGUID, guidParameterName, methodName);

        GovernanceDefinitionGraphResponse restResult = restClient.callGovernanceDefinitionGraphGetRESTCall(methodName,
                                                                                                           serverPlatformURLRoot + urlTemplate,
                                                                                                           serverName,
                                                                                                           userId,
                                                                                                           governanceDefinitionGUID);

        return restResult.getElement();
    }


    /**
     * Return the elements that are governed by the supplied governance definition.
     *
     * @param userId calling user
     * @param governanceDefinitionGUID unique name of the governance definition
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
    public List<ElementStub> getElementsGovernedByDefinition(String userId,
                                                             String governanceDefinitionGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getElementsGovernedByDefinition";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-definitions/{2}/elements?startFrom={3}&pageSize={4}";
        final String guidParameterName = "governanceDefinitionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionGUID, guidParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        ElementStubListResponse restResult = restClient.callElementStubListGetRESTCall(methodName,
                                                                                       serverPlatformURLRoot + urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       governanceDefinitionGUID,
                                                                                       startFrom,
                                                                                       queryPageSize);

        return restResult.getElements();
    }


    /**
     * Return the list of governance definitions that match the search string - this can be a regular expression.
     *
     * @param userId calling user
     * @param typeName option types name to restrict retrieval to a specific type
     * @param searchString value to search for
     * @param startFrom where to start from in the list of definition results
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance definitions
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    @Override
    public List<GovernanceDefinitionElement> findGovernanceDefinitions(String userId,
                                                                       String typeName,
                                                                       String searchString,
                                                                       int    startFrom,
                                                                       int    pageSize) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "findGovernanceDefinitions";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-definitions/by-search-string?startFrom={2}&pageSize={3}";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchString, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceDefinitionListResponse restResult = restClient.callGovernanceDefinitionListPostRESTCall(methodName,
                                                                                                          serverPlatformURLRoot + urlTemplate,
                                                                                                          requestBody,
                                                                                                          serverName,
                                                                                                          userId,
                                                                                                          startFrom,
                                                                                                          queryPageSize);

        return restResult.getElements();
    }


    /**
     * Return details of the metrics for a governance definition along with details of where the
     * @param userId calling user
     * @param governanceDefinitionGUID unique name of the governance definition
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of associated metrics and links for retrieving the captured measurements
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    @Override
    public List<GovernanceMetricImplementation> getGovernanceDefinitionMetrics(String userId,
                                                                               String governanceDefinitionGUID,
                                                                               int    startFrom,
                                                                               int    pageSize) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "getGovernanceDefinitionMetrics";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-definitions/{2}/metrics-implementation?startFrom={2}&pageSize={3}";
        final String guidParameterName = "governanceDefinitionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionGUID, guidParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        GovernanceMetricImplementationListResponse restResult = restClient.callGovernanceMetricsImplementationListGetRESTCall(methodName,
                                                                                                                              serverPlatformURLRoot + urlTemplate,
                                                                                                                              serverName,
                                                                                                                              userId,
                                                                                                                              governanceDefinitionGUID,
                                                                                                                              startFrom,
                                                                                                                              queryPageSize);

        return restResult.getElements();
    }


    /**
     * Return detailed information about the requested governance zone.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier of the zone to search for
     *
     * @return detailed information about the requested zone
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    @Override
    public GovernanceZoneInAction getGovernanceZoneInAction(String userId,
                                                            String zoneGUID) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "getElementsGovernedByDefinition";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-zones/{2}/in-action";
        final String guidParameterName = "zoneGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(zoneGUID, guidParameterName, methodName);

        GovernanceZoneInActionResponse restResult = restClient.callGovernanceZoneInActionGetRESTCall(methodName,
                                                                                                     serverPlatformURLRoot + urlTemplate,
                                                                                                     serverName,
                                                                                                     userId,
                                                                                                     zoneGUID);

        return restResult.getElement();
    }


    /**
     * Return the list of assets that are members of a particular zone.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier of the zone to search for
     * @param startFrom where to start from in the list of assets
     * @param pageSize max number of results to return in one call
     *
     * @return list of headers for assets in the requested zone
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    @Override
    public List<ElementStub> getGovernanceZoneMembers(String userId,
                                                      String zoneGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "getElementsGovernedByDefinition";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-zones/{2}/members?startFrom={2}&pageSize={3}";
        final String guidParameterName = "zoneGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(zoneGUID, guidParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        ElementStubListResponse restResult = restClient.callElementStubListGetRESTCall(methodName,
                                                                                       serverPlatformURLRoot + urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       zoneGUID,
                                                                                       startFrom,
                                                                                       queryPageSize);

        return restResult.getElements();
    }
}
