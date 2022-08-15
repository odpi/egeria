/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceMetricsInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceMetricElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceMetricProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceMetricListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceMetricResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * GovernanceMetricsManager is the java client for managing governance metrics and their links to all types of governance definitions.
 */
public class GovernanceMetricsManager implements GovernanceMetricsInterface
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
    public GovernanceMetricsManager(String serverName,
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
    public GovernanceMetricsManager(String     serverName,
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
    public GovernanceMetricsManager(String   serverName,
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
    public GovernanceMetricsManager(String     serverName,
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
    public GovernanceMetricsManager(String                      serverName,
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
     * Create a new governance metric.
     *
     * @param userId calling user
     * @param metricProperties properties of the metric
     *
     * @return unique identifier of the metric
     *
     * @throws InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createGovernanceMetric(String                     userId,
                                         GovernanceMetricProperties metricProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "createGovernanceMetric";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics";

        final String qualifiedNameParameterName = "qualifiedName";
        final String propertiesParameterName = "metricProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(metricProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(metricProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformURLRoot + urlTemplate,
                                                                  metricProperties,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update an existing governance metric.
     *
     * @param userId calling user
     * @param metricGUID unique identifier of the metric to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param metricProperties properties to update
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  updateGovernanceMetric(String                     userId,
                                        String                     metricGUID,
                                        boolean                    isMergeUpdate,
                                        GovernanceMetricProperties metricProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "updateGovernanceMetric";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/{2}/update?isMergeUpdate={4}";

        final String qualifiedNameParameterName = "qualifiedName";
        final String guidParameterName = "metricGUID";
        final String propertiesParameterName = "metricProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metricGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(metricProperties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(metricProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        metricProperties,
                                        serverName,
                                        userId,
                                        metricGUID,
                                        isMergeUpdate);
    }


    /**
     * Delete a specific governance metric.
     *
     * @param userId calling user
     * @param metricGUID unique identifier of the metric to remove
     *
     * @throws InvalidParameterException guid is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  deleteGovernanceMetric(String userId,
                                        String metricGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "deleteExternalReference";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/{2}/delete";
        final String guidParameterName = "metricGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metricGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        metricGUID);
    }


    /**
     * Create a link to show that a governance metric supports the requirements of one of the governance policies.
     * If the link already exists the rationale is updated.
     *
     * @param userId calling user
     * @param metricGUID unique identifier of the governance metric
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param rationale description of how the metric supports the driver
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void setupGovernanceDefinitionMetric(String userId,
                                                String metricGUID,
                                                String governanceDefinitionGUID,
                                                String rationale) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "setupGovernanceDefinitionMetric";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/{2}/governance-definitions/{3}/link";

        final String metricGUIDParameterName = "metricGUID";
        final String governanceDefinitionGUIDParameterName = "governanceDefinitionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metricGUID, metricGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionGUID, governanceDefinitionGUIDParameterName, methodName);

        StringRequestBody requestBody = new StringRequestBody();

        requestBody.setString(rationale);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        metricGUID,
                                        governanceDefinitionGUID);
    }


    /**
     * Remove the link between a governance metric and a governance definition.
     *
     * @param userId calling user
     * @param metricGUID unique identifier of the governance metric
     * @param governanceDefinitionGUID unique identifier of the governance definition
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void clearGovernanceDefinitionMetric(String userId,
                                                String metricGUID,
                                                String governanceDefinitionGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "setupGovernanceDefinitionMetric";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/{2}/governance-definitions/{3}/unlink";

        final String metricGUIDParameterName = "metricGUID";
        final String governanceDefinitionGUIDParameterName = "governanceDefinitionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metricGUID, metricGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(governanceDefinitionGUID, governanceDefinitionGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        metricGUID,
                                        governanceDefinitionGUID);
    }


    /**
     * Return information about a specific governance metric.
     *
     * @param userId calling user
     * @param metricGUID unique identifier for the governance metric
     *
     * @return properties of the governance metric
     *
     * @throws InvalidParameterException metricGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public GovernanceMetricElement getGovernanceMetricByGUID(String userId,
                                                             String metricGUID) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "getGovernanceMetricByGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/{2}";

        final String guidParameterName = "metricGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metricGUID, guidParameterName, methodName);

        GovernanceMetricResponse restResult = restClient.callGovernanceMetricGetRESTCall(methodName,
                                                                                          serverPlatformURLRoot + urlTemplate,
                                                                                          serverName,
                                                                                          userId,
                                                                                          metricGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the list of governance metrics for this search string.
     *
     * @param userId the name of the calling user.
     * @param searchString value to search for (supports wildcards).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of metrics
     *
     * @throws InvalidParameterException guid invalid or the search parameter is not correctly specified, or is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<GovernanceMetricElement> findGovernanceMetrics(String userId,
                                                               String searchString,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "findGovernanceMetrics";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/by-search-string?startFrom={2}&pageSize={3}";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceMetricListResponse restResult = restClient.callGovernanceMetricListPostRESTCall(methodName,
                                                                                                  serverPlatformURLRoot + urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  startFrom,
                                                                                                  queryPageSize);

        return restResult.getElements();
    }
}
