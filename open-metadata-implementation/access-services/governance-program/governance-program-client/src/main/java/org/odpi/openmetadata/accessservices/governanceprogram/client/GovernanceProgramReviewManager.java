/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceProgramReviewInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;

import java.util.List;


/**
 * The GovernanceProgramReviewInterface supports the periodic review of the governance program.
 * This includes looking at the metrics and the governance zones.
 */
public class GovernanceProgramReviewManager extends GovernanceProgramBaseClient implements GovernanceProgramReviewInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceProgramReviewManager(String serverName,
                                          String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
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
        super(serverName, serverPlatformURLRoot, userId, password);
    }



    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
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
        super(serverName, serverPlatformURLRoot, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
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
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize, auditLog);
    }


    /**
     * Create a new client that uses the supplied rest client.  This is typically used when called from another OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
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
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /**
     * Retrieve the governance definition by the unique identifier assigned by this service when it was created.
     *
     * @param userId calling user
     * @param definitionGUID identifier of the governance definition to retrieve
     *
     * @return properties of the governance definition
     *
     * @throws InvalidParameterException guid or userId is null; guid is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public GovernanceDefinitionElement getGovernanceDefinitionByGUID(String userId,
                                                                     String definitionGUID) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String   methodName = "getGovernanceDefinitionByGUID";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-definitions/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceDefinitionResponse restResult = restClient.callGovernanceDefinitionGetRESTCall(methodName,
                                                                                                 urlTemplate,
                                                                                                 serverName,
                                                                                                 userId,
                                                                                                 definitionGUID);

        return restResult.getElement();
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
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-definitions/{2}/for-domain?domainIdentifier={3}&startFrom={4}&pageSize={5}";

        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        GovernanceDefinitionsResponse restResult = restClient.callGovernanceDefinitionsGetRESTCall(methodName,
                                                                                                   urlTemplate,
                                                                                                   serverName,
                                                                                                   userId,
                                                                                                   getTypeName(typeName),
                                                                                                   domainIdentifier,
                                                                                                   typeName,
                                                                                                   startFrom,
                                                                                                   queryPageSize);

        return restResult.getElements();
    }


    /**
     * Return the list of governance definitions associated with a unique docId.  In an ideal world, there should be only one.
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
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-definitions/{2}/for-document-id/{3}?startFrom={4}&pageSize={5}";
        final String   docIdParameterName = "docId";


        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(docIdParameterName, docIdParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        GovernanceDefinitionsResponse restResult = restClient.callGovernanceDefinitionsGetRESTCall(methodName,
                                                                                                   urlTemplate,
                                                                                                   serverName,
                                                                                                   userId,
                                                                                                   getTypeName(typeName),
                                                                                                   docId,
                                                                                                   startFrom,
                                                                                                   queryPageSize);

        return restResult.getElements();
    }


    /**
     * Fill in a default type name if none supplied.
     *
     * @param suppliedTypeName supplied on the parameters
     * @return type name to send
     */
    private String getTypeName(String suppliedTypeName)
    {
        final String defaultTypeName = "GovernanceDefinition";

        if (suppliedTypeName != null)
        {
            return  suppliedTypeName;
        }

        return defaultTypeName;
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-definitions/{2}/in-context";
        final String guidParameterName = "governanceDefinitionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionGUID, guidParameterName, methodName);

        GovernanceDefinitionGraphResponse restResult = restClient.callGovernanceDefinitionGraphGetRESTCall(methodName,
                                                                                                           urlTemplate,
                                                                                                           serverName,
                                                                                                           userId,
                                                                                                           governanceDefinitionGUID);

        return restResult.getElement();
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-definitions/{2}/by-search-string?startFrom={3}&pageSize={4}";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchString, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceDefinitionsResponse restResult = restClient.callGovernanceDefinitionsPostRESTCall(methodName,
                                                                                                    urlTemplate,
                                                                                                    requestBody,
                                                                                                    serverName,
                                                                                                    userId,
                                                                                                    getTypeName(typeName),
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-definitions/{2}/metrics-implementation?startFrom={3}&pageSize={4}";
        final String guidParameterName = "governanceDefinitionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionGUID, guidParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        GovernanceMetricImplementationsResponse    restResult = restClient.callGovernanceMetricsImplementationListGetRESTCall(methodName,
                                                                                                                              urlTemplate,
                                                                                                                              serverName,
                                                                                                                              userId,
                                                                                                                              governanceDefinitionGUID,
                                                                                                                              startFrom,
                                                                                                                              queryPageSize);

        return restResult.getElements();
    }


    /**
     * Return the list of assets that are members of a particular zone.
     *
     * @param userId calling user
     * @param zoneName unique name of the zone to search for
     * @param subTypeName optional asset subtypeName to limit the results
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
                                                      String zoneName,
                                                      String subTypeName,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "getElementsGovernedByDefinition";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/review/governance-zones/{2}/members/{3}?startFrom={4}&pageSize={5}";
        final String nameParameterName = "zoneName";
        final String assetTypeName = "Asset";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(zoneName, nameParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        String typeName = assetTypeName;

        if (subTypeName != null)
        {
            typeName = subTypeName;
        }

        ElementStubsResponse restResult = restClient.callElementStubsGetRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 serverName,
                                                                                 userId,
                                                                                 zoneName,
                                                                                 typeName,
                                                                                 startFrom,
                                                                                 queryPageSize);

        return restResult.getElements();
    }
}
