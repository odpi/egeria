/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.client;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.properties.IntegrationReport;
import org.odpi.openmetadata.frameworks.integration.properties.IntegrationReportProperties;
import org.odpi.openmetadata.frameworkservices.oif.client.rest.OpenIntegrationRESTClient;
import org.odpi.openmetadata.frameworkservices.oif.rest.CatalogTargetsResponse;
import org.odpi.openmetadata.frameworkservices.oif.rest.IntegrationReportResponse;
import org.odpi.openmetadata.frameworkservices.oif.rest.IntegrationReportsResponse;
import org.odpi.openmetadata.frameworkservices.oif.rest.MetadataSourceRequestBody;

import java.util.Date;
import java.util.List;


/**
 * OpenIntegrationServiceBase supports the open integration interface.
 */
public class OpenIntegrationServiceBase extends OpenIntegrationClient
{
    private final   OpenIntegrationRESTClient restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private AuditLog auditLog = null;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenIntegrationServiceBase(String serviceURLMarker,
                                      String serverName,
                                      String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot);

        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = new OpenIntegrationRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId          caller's userId embedded in all HTTP requests
     * @param serverPassword        caller's password embedded in all HTTP requests
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenIntegrationServiceBase(String serviceURLMarker,
                                      String serverName,
                                      String serverPlatformURLRoot,
                                      String serverUserId,
                                      String serverPassword) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot);

        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = new OpenIntegrationRESTClient(serverName, serverPlatformURLRoot, serverUserId, serverPassword);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient            pre-initialized REST client
     * @param maxPageSize           pre-initialized parameter limit
     *
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public OpenIntegrationServiceBase(String                    serviceURLMarker,
                                      String                    serverName,
                                      String                    serverPlatformURLRoot,
                                      OpenIntegrationRESTClient restClient,
                                      int                       maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot);

        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = restClient;
    }


    /**
     * Retrieve the unique identifier of the metadata element that represents the metadata source.
     *
     * @param userId calling user
     * @param qualifiedName unique name of the metadata source
     *
     * @return unique identifier of the metadata source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public String getMetadataSourceGUID(String  userId,
                                        String  qualifiedName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName                  = "getMetadataSourceGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-integration/users/{2}/metadata" +
                "-sources/by-name";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(qualifiedName);
        requestBody.setNamePropertyName(qualifiedNameParameterName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a software capability.  This describes the metadata source.
     *
     * @param userId calling user
     * @param softwareCapabilityTypeName name of software capability type to describe the metadata source
     * @param classificationName optional classification name that refines the type of the software capability.
     * @param qualifiedName unique name for the external source
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createMetadataSource(String userId,
                                       String softwareCapabilityTypeName,
                                       String classificationName,
                                       String qualifiedName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                  = "createMetadataSource";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-integration/users/{2}/metadata-sources/new";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setQualifiedName(qualifiedName);
        requestBody.setTypeName(softwareCapabilityTypeName);
        requestBody.setClassificationName(classificationName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Retrieve the identifiers of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of named elements
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    @Override
    public List<CatalogTarget> getCatalogTargets(String  userId,
                                                 String  integrationConnectorGUID,
                                                 int     startingFrom,
                                                 int     maximumResults) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "getCatalogTargets";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-integration/users/{2}/integration-connectors/{2}/catalog-targets?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        CatalogTargetsResponse restResult = restClient.callCatalogTargetsGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     serviceURLMarker,
                                                                                     userId,
                                                                                     integrationConnectorGUID,
                                                                                     startingFrom,
                                                                                     maximumResults);

        return restResult.getElements();
    }


    /**
     * Create a new integration report for an element (identified by anchorGUID).
     *
     * @param userId calling user
     * @param anchorGUID element to attach the integration report to
     * @param properties properties of the report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem with the metadata server.
     */
    @Override
    public void publishIntegrationReport(String                      userId,
                                         String                      anchorGUID,
                                         IntegrationReportProperties properties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName              = "publishIntegrationReport";
        final String guidParameterName       = "anchorGUID";
        final String propertiesParameterName = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-integration/users/{2}" +
                "/integration-reports/{3}/new";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        properties,
                                        serverName,
                                        userId);
    }


    /**
     * Retrieve a specific integration report by unique identifier.
     *
     * @param userId calling user
     * @param reportGUID unique identifier of the integration report
     *
     * @return report or null
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem with the metadata server.
     */
    @Override
    public  IntegrationReport getIntegrationReport(String userId,
                                                   String reportGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "getIntegrationReport";
        final String guidParameterName = "reportGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(reportGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-integration/users/{2}" +
                "/integration-reports/{3}";

        IntegrationReportResponse restResult = restClient.callIntegrationReportGetRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           serverName,
                                                                                           serviceURLMarker,
                                                                                           userId,
                                                                                           reportGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the integration reports attached to an element.  The list can be filtered by start and completion date of the report along with the
     * paging options if there are many integration reports.
     *
     * @param userId calling user
     * @param elementGUID calling user
     * @param afterCompletionDate restrict reports to those that completed after the requested time (null for any completion time).
     * @param beforeStartDate restrict reports to those that started before the requested time (null for any start time).
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of qualifying reports
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem with the metadata server.
     */
    @Override
    public List<IntegrationReport> getIntegrationReportsForElement(String  userId,
                                                                   String  elementGUID,
                                                                   Date    afterCompletionDate,
                                                                   Date    beforeStartDate,
                                                                   int     startingFrom,
                                                                   int     maximumResults) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName        = "getIntegrationReportsForElement";
        final String guidParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-integration/users/{2}" +
                "/integration-reports/for-element/{3}?afterCompletionDate={4}&beforeStartDate={5}&startingFrom={6}&maximumResults={7}";

        IntegrationReportsResponse restResult = restClient.callIntegrationReportsGetRESTCall(methodName,
                                                                                             urlTemplate,
                                                                                             serverName,
                                                                                             serviceURLMarker,
                                                                                             userId,
                                                                                             elementGUID,
                                                                                             afterCompletionDate,
                                                                                             beforeStartDate,
                                                                                             startingFrom,
                                                                                             maximumResults);

        return restResult.getElements();
    }


    /**
     * Retrieve the published integration reports.  The list can be filtered by start and completion date of the report along with the
     * paging options if there are many integration reports.
     *
     * @param userId calling user
     * @param afterCompletionDate restrict reports to those that completed after the requested time (null for any completion time).
     * @param beforeStartDate restrict reports to those that started before the requested time (null for any start time).
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of qualifying reports
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem with the metadata server.
     */
    @Override
    public List<IntegrationReport> getIntegrationReports(String  userId,
                                                         Date    afterCompletionDate,
                                                         Date    beforeStartDate,
                                                         int     startingFrom,
                                                         int     maximumResults) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getIntegrationReports";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-integration/users/{2}" +
                "/integration-reports?afterCompletionDate={3}&beforeStartDate={4}&startingFrom={5}&maximumResults={6}";

        IntegrationReportsResponse restResult = restClient.callIntegrationReportsGetRESTCall(methodName,
                                                                                             urlTemplate,
                                                                                             serverName,
                                                                                             serviceURLMarker,
                                                                                             userId,
                                                                                             afterCompletionDate,
                                                                                             beforeStartDate,
                                                                                             startingFrom,
                                                                                             maximumResults);

        return restResult.getElements();
    }
}
