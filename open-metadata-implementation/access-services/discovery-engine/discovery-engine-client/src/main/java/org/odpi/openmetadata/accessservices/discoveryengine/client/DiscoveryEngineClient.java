/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.client;

import org.odpi.openmetadata.accessservices.discoveryengine.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.ConnectedAssetClientBase;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.discovery.properties.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DiscoveryEngineClient provides the client-side operational REST APIs for a running Discovery Engine.
 * The discovery engine embeds this client in each of the special store clients defined by the
 * Open Discovery Framework (ODF).
 */
public class DiscoveryEngineClient extends ConnectedAssetClientBase
{
    private ODFRESTClient                          restClient;               /* Initialized in constructor */

    private static final String  serviceURLName = "discovery-engine";


    /**
     * Create a client-side object for calling a discovery engine within a discovery server.
     *
     * @param serverPlatformRootURL the root url of the platform where the discovery engine is running.
     * @param serverName the name of the discovery server where the discovery engine is running
     * @param restClient pre-built client
     * @param auditLog logging destination
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public DiscoveryEngineClient(String        serverName,
                                 String        serverPlatformRootURL,
                                 ODFRESTClient restClient,
                                 AuditLog      auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, auditLog);

        this.restClient = restClient;
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException null URL or server name
     */
    public DiscoveryEngineClient(String   serverName,
                                 String   serverPlatformRootURL,
                                 AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, auditLog);

        this.restClient = new ODFRESTClient(serverName, serverPlatformRootURL, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException null URL or server name
     */
    public DiscoveryEngineClient(String serverName,
                                 String serverPlatformRootURL) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL);

        this.restClient = new ODFRESTClient(serverName, serverPlatformRootURL);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     * @throws InvalidParameterException null URL or server name
     */
    public DiscoveryEngineClient(String     serverName,
                                 String     serverPlatformRootURL,
                                 String     userId,
                                 String     password,
                                 AuditLog   auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, auditLog);

        this.restClient = new ODFRESTClient(serverName, serverPlatformRootURL, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException null URL or server name
     */
    public DiscoveryEngineClient(String     serverName,
                                 String     serverPlatformRootURL,
                                 String     userId,
                                 String     password) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL);

        this.restClient = new ODFRESTClient(serverName, serverPlatformRootURL, userId, password);
    }


    /**
     * Return the next set of assets to process.
     *
     * @param userId calling user
     * @param startFrom starting point of the query
     * @param pageSize maximum number of results to return
     * @return list of unique identifiers for located assets
     * @throws InvalidParameterException one of the parameters is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    public List<String> getAssets(String  userId,
                                  int     startFrom,
                                  int     pageSize) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String   urlTemplate
                = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/assets?startFrom={2}&pageSize={3}";
        final String   methodName = "getAssets";

        invalidParameterHandler.validateUserId(userId, methodName);

        GUIDListResponse restResult = restClient.callGUIDListGetRESTCall(methodName,
                                                                          serverPlatformRootURL + urlTemplate,
                                                                          serverName,
                                                                          userId,
                                                                          startFrom,
                                                                          pageSize);

        return restResult.getGUIDs();
    }


    /**
     * Return the assets with the same qualified name.  If all is well there should be only one
     * returned.
     *
     * @param userId calling user
     * @param searchParameter the value to query on
     * @param searchParameterName the name of the parameter that provides the search parameter
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @param methodName calling method
     * @return list of unique identifiers for matching assets
     * @throws InvalidParameterException one of the parameters is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    private List<String>  retrieveAssetGUIDList(String userId,
                                                String urlTemplate,
                                                String searchParameter,
                                                String searchParameterName,
                                                int    startFrom,
                                                int    pageSize,
                                                String methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchParameter, searchParameterName, methodName);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          serverPlatformRootURL + urlTemplate,
                                                                          searchParameter,
                                                                          serverName,
                                                                          userId,
                                                                          startFrom,
                                                                          pageSize);

        return restResult.getGUIDs();
    }


    /**
     * Return the assets with the same qualified name.  If all is well there should be only one
     * returned.
     *
     * @param userId calling user
     * @param name the qualified name to query on
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @return list of unique identifiers for matching assets
     * @throws InvalidParameterException one of the parameters is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    public List<String>  getAssetsByQualifiedName(String   userId,
                                                  String   name,
                                                  int      startFrom,
                                                  int      pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String   urlTemplate
                = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/assets/by-qualified-name?startFrom={2}&pageSize={3}";
        final String   methodName = "getAssetsByQualifiedName";
        final String   searchParameterName = "name";

        return retrieveAssetGUIDList(userId, urlTemplate, name, searchParameterName, startFrom, pageSize, methodName);
    }


    /**
     * Return the list of matching assets that have the supplied name as either the
     * qualified name or display name.  This is an exact match retrieval.
     *
     * @param userId calling user
     * @param name name to query for
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @return list of unique identifiers for matching assets
     * @throws InvalidParameterException one of the parameters is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    public List<String>  getAssetsByName(String   userId,
                                         String   name,
                                         int      startFrom,
                                         int      pageSize) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String   urlTemplate
                = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/assets/by-name?startFrom={2}&pageSize={3}";
        final String   methodName = "getAssetsByName";
        final String   searchParameterName = "name";

        return retrieveAssetGUIDList(userId, urlTemplate, name, searchParameterName, startFrom, pageSize, methodName);
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.  The search string is interpreted as a regular expression (RegEx).
     *
     * @param userId calling user
     * @param searchString string to search for in text
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of assets that match the search string.
     *
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<String>  findAssets(String   userId,
                                    String   searchString,
                                    int      startFrom,
                                    int      pageSize) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String   urlTemplate
                = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/assets/by-search-string?startFrom={2}&pageSize={3}";
        final String   methodName = "findAssets";
        final String   searchParameterName = "searchString";

        return retrieveAssetGUIDList(userId, urlTemplate, searchString, searchParameterName, startFrom, pageSize, methodName);
    }


    /**
     * Return the list of assets that have the same endpoint address.
     *
     * @param userId calling user
     * @param networkAddress address to query on
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @return list of unique identifiers for matching assets
     * @throws InvalidParameterException one of the parameters is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    public  List<String>  findAssetsByEndpoint(String   userId,
                                               String   networkAddress,
                                               int      startFrom,
                                               int      pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String   urlTemplate
                = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/assets/by-endpoint-address?startFrom={2}&pageSize={3}";
        final String   methodName = "findAssetsByEndpoint";
        final String   searchParameterName = "networkAddress";

        return retrieveAssetGUIDList(userId, urlTemplate, networkAddress, searchParameterName, startFrom, pageSize, methodName);
    }


    /**
     * Return the connection information for the asset.  This is used to create the connector.  The connector
     * is an Open Connector Framework (OCF) connector that provides access to the asset's data and metadata properties.
     *
     * @param userId calling user
     * @param assetGUID unique identifier (guid) for the asset
     * @return Connection bean
     * @throws InvalidParameterException the asset guid is not recognized
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    Connection getConnectionForAsset(String    userId,
                                     String    assetGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String   methodName = "getConnectionForAsset";
        final String   guidParameterName = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameterName, methodName);

        return super.getConnectionForAsset(restClient, serviceURLName, userId, assetGUID);
    }


    /**
     * Returns the connector corresponding to the supplied connection.
     *
     * @param userId       userId of user making request.
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return Connector   connector instance
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     */
    public Connector getConnectorForConnection(String     userId,
                                               Connection connection) throws InvalidParameterException,
                                                                             ConnectionCheckedException,
                                                                             ConnectorCheckedException
    {
        final  String  methodName = "getConnectorForConnection";

        invalidParameterHandler.validateUserId(userId, methodName);

        return super.getConnectorForConnection(restClient, serviceURLName, userId, connection, methodName);
    }



    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId         userId of user making request.
     * @param assetGUID      unique identifier for asset.
     *
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    AssetUniverse getAssetProperties(String userId,
                                     String assetGUID) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return super.getAssetProperties(serviceURLName, userId, assetGUID);
    }


    /**
     * Log an audit message about this asset.
     *
     * @param userId         userId of user making request.
     * @param assetGUID      unique identifier for asset.
     * @param discoveryService      unique name for discoveryService.
     * @param message        message to log
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void logAssetAuditMessage(String    userId,
                                     String    assetGUID,
                                     String    discoveryService,
                                     String    message) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String   methodName = "logAssetAuditMessage";
        final String   guidParameter = "assetGUID";
        final String   discoveryServiceParameter = "discoveryService";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/assets/{2}/log-records/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);
        invalidParameterHandler.validateName(discoveryService, discoveryServiceParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        message,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        discoveryService);
    }


    /**
     * Create a new discovery analysis report and chain it to its asset, discovery engine and discovery service.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the report
     * @param displayName short name for the report
     * @param description description of the report
     * @param creationDate date of the report
     * @param analysisParameters analysis parameters passed to the discovery service
     * @param discoveryRequestStatus current status of the discovery processing
     * @param assetGUID unique identifier of the asset being analysed
     * @param discoveryEngineGUID unique identifier of the discovery engine that is running the discovery service
     * @param discoveryServiceGUID unique identifier of the discovery service creating the report
     * @param additionalProperties additional properties for the report
     *
     * @return unique identifier of new discovery report.
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or report
     * @throws PropertyServerException there was a problem in the store whether the asset/report properties are kept.
     */
    public  String  createDiscoveryAnalysisReport(String                  userId,
                                                  String                  qualifiedName,
                                                  String                  displayName,
                                                  String                  description,
                                                  Date                    creationDate,
                                                  Map<String, String>     analysisParameters,
                                                  DiscoveryRequestStatus  discoveryRequestStatus,
                                                  String                  assetGUID,
                                                  String                  discoveryEngineGUID,
                                                  String                  discoveryServiceGUID,
                                                  Map<String, String>     additionalProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String   methodName = "createDiscoveryAnalysisReport";
        final String   nameParameterName = "qualifiedName";
        final String   assetGUIDParameterName = "assetGUID";
        final String   discoveryEngineGUIDParameterName = "discoveryEngineGUID";
        final String   discoveryServiceGUIDParameterName = "discoveryServiceGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/assets/{2}/discovery-analysis-reports";

        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, discoveryServiceGUIDParameterName, methodName);

        DiscoveryAnalysisReportRequestBody requestBody = new DiscoveryAnalysisReportRequestBody();

        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setCreationDate(creationDate);
        requestBody.setAnalysisParameters(analysisParameters);
        requestBody.setDiscoveryRequestStatus(discoveryRequestStatus);
        requestBody.setDiscoveryEngineGUID(discoveryEngineGUID);
        requestBody.setDiscoveryServiceGUID(discoveryServiceGUID);
        requestBody.setAdditionalProperties(additionalProperties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID);

        return restResult.getGUID();
    }


    /**
     * Update the properties of the discovery analysis report.
     *
     * @param userId calling user.
     * @param updatedReport updated report - this will replace what was previous stored.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public void  updateDiscoveryAnalysisReport(String                  userId,
                                               DiscoveryAnalysisReport updatedReport) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String   methodName = "updateDiscoveryAnalysisReport";
        final String   reportParameterName = "updatedReport";
        final String   reportHeaderParameterName = "updatedReport.getElementHeader";
        final String   reportGUIDParameterName = "updatedReport.getGUID()";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(updatedReport, reportParameterName, methodName);
        invalidParameterHandler.validateObject(updatedReport.getElementHeader(), reportHeaderParameterName, methodName);
        invalidParameterHandler.validateGUID(updatedReport.getElementHeader().getGUID(), reportGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        updatedReport,
                                        serverName,
                                        userId,
                                        updatedReport.getElementHeader().getGUID());

    }


    /**
     * Request the status of an executing discovery request.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     *
     * @return status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public DiscoveryRequestStatus getDiscoveryStatus(String   userId,
                                                     String   discoveryReportGUID) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        DiscoveryAnalysisReport report = this.getDiscoveryAnalysisReport(userId, discoveryReportGUID);

        if (report != null)
        {
            return report.getDiscoveryRequestStatus();
        }

        return null;
    }


    /**
     * Request the status of an executing discovery request.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param newStatus  status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public void setDiscoveryStatus(String                 userId,
                                   String                 discoveryReportGUID,
                                   DiscoveryRequestStatus newStatus) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        DiscoveryAnalysisReport report = this.getDiscoveryAnalysisReport(userId, discoveryReportGUID);

        if (report != null)
        {
            report.setDiscoveryRequestStatus(newStatus);

            this.updateDiscoveryAnalysisReport(userId, report);
        }
    }


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery report.
     *
     * @return discovery report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public DiscoveryAnalysisReport getDiscoveryAnalysisReport(String   userId,
                                                              String   discoveryReportGUID) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String   methodName = "getDiscoveryAnalysisReport";
        final String   reportGUIDParameterName = "discoveryReportGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, reportGUIDParameterName, methodName);

        DiscoveryAnalysisReportResponse restResult = restClient.callDiscoveryAnalysisReportGetRESTCall(methodName,
                                                                                                       serverPlatformRootURL + urlTemplate,
                                                                                                       serverName,
                                                                                                       userId,
                                                                                                       discoveryReportGUID);

        return restResult.getAnalysisReport();
    }


    /**
     * Return the annotation subtype names.
     *
     * @param userId calling user
     * @return list of type names that are subtypes of annotation
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<String>  getTypesOfAnnotation(String userId) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        final String   methodName = "getTypesOfAnnotation";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/sub-types";

        invalidParameterHandler.validateUserId(userId, methodName);

        NameListResponse restResult = restClient.callNameListGetRESTCall(methodName,
                                                                         serverPlatformRootURL + urlTemplate,
                                                                         serverName,
                                                                         userId);

        return restResult.getNames();
    }


    /**
     * Return the annotation subtype names mapped to their descriptions.
     *
     * @param userId calling user
     * @return map of type names that are subtypes of annotation to their descriptions
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    Map<String, String>  getTypesOfAnnotationWithDescriptions(String userId) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String   methodName = "getTypesOfAnnotationWithDescriptions";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/sub-types/descriptions";

        invalidParameterHandler.validateUserId(userId, methodName);

        StringMapResponse restResult = restClient.callStringMapGetRESTCall(methodName,
                                                                          serverPlatformRootURL + urlTemplate,
                                                                           serverName,
                                                                           userId);

        return restResult.getStringMap();
    }


    /**
     * Return the list of annotations from previous runs of the discovery service that are set to a specific status.
     * If status is null then annotations that have been reviewed, approved and/or actioned are returned from
     * discovery reports that are not waiting or in progress.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param status status value to use on the query
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     * @return list of annotation (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    List<Annotation> getAnnotationsForAssetByStatus(String           userId,
                                                    String           assetGUID,
                                                    AnnotationStatus status,
                                                    int              startingFrom,
                                                    int              maximumResults) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String   methodName = "getAnnotationsForAssetByStatus";
        final String   assetGUIDParameterName = "assetGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/assets/{2}/annotations?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        StatusRequestBody requestBody = new StatusRequestBody();

        requestBody.setAnnotationStatus(status);

        AnnotationListResponse restResult = restClient.callAnnotationListPostRESTCall(methodName,
                                                                                      serverPlatformRootURL + urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      assetGUID,
                                                                                      Integer.toString(startingFrom),
                                                                                      Integer.toString(maximumResults));

        return restResult.getAnnotations();
    }


    /**
     * Return the annotations linked directly to the report.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public List<Annotation> getDiscoveryReportAnnotations(String   userId,
                                                          String   discoveryReportGUID,
                                                          int      startingFrom,
                                                          int      maximumResults) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String   methodName = "getDiscoveryReportAnnotations";

        return this.getDiscoveryReportAnnotations(userId, discoveryReportGUID, startingFrom, maximumResults, methodName);
    }


    /**
     * Return the annotations linked directly to the report.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    List<Annotation> getDiscoveryReportAnnotations(String   userId,
                                                   String   discoveryReportGUID,
                                                   int      startingFrom,
                                                   int      maximumResults,
                                                   String   methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String   reportGUIDParameterName = "discoveryReportGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}/annotations?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, reportGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        AnnotationListResponse restResult = restClient.callAnnotationListGetRESTCall(methodName,
                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     discoveryReportGUID,
                                                                                     Integer.toString(startingFrom),
                                                                                     Integer.toString(maximumResults));

        return restResult.getAnnotations();
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID parent annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public  List<Annotation>  getExtendedAnnotations(String   userId,
                                                     String   annotationGUID,
                                                     int      startingFrom,
                                                     int      maximumResults) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   methodName = "getExtendedAnnotations";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}/extended-annotations?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        AnnotationListResponse restResult = restClient.callAnnotationListGetRESTCall(methodName,
                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     annotationGUID,
                                                                                     Integer.toString(startingFrom),
                                                                                     Integer.toString(maximumResults));

        return restResult.getAnnotations();
    }


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public Annotation getAnnotation(String userId,
                                    String annotationGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String   methodName = "getAnnotation";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        AnnotationResponse restResult = restClient.callAnnotationGetRESTCall(methodName,
                                                                             serverPlatformRootURL + urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             annotationGUID);

        return restResult.getAnnotation();
    }


    /**
     * Add a new annotation to the annotation store as a top level annotation linked directly off of the report.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID unique identifier of the discovery analysis report
     * @param annotation annotation object
     * @return unique identifier of new annotation
     * @throws InvalidParameterException the annotation is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem adding the annotation to the annotation store.
     */
    String  addAnnotationToDiscoveryReport(String     userId,
                                           String     discoveryReportGUID,
                                           Annotation annotation) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String   methodName = "addAnnotationToDiscoveryReport";
        final String   annotationParameterName = "annotation";
        final String   reportGUIDParameterName = "discoveryReportGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}/annotations";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, reportGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  annotation,
                                                                  serverName,
                                                                  userId,
                                                                  discoveryReportGUID);

        return restResult.getGUID();
    }


    /**
     * Add a new annotation and link it to an existing annotation.
     *
     * @param userId identifier of calling user
     * @param parentAnnotationGUID unique identifier of the annotation that this new one is to be attached to
     * @param annotation annotation object
     * @return unique identifier of new annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving annotations in the annotation store.
     */
    String  addAnnotationToAnnotation(String     userId,
                                      String     parentAnnotationGUID,
                                      Annotation annotation) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String   methodName = "addAnnotationToAnnotation";
        final String   annotationGUIDParameterName = "parentAnnotationGUID";
        final String   annotationParameterName = "annotation";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}/extended-annotations";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentAnnotationGUID, annotationGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  annotation,
                                                                  serverName,
                                                                  userId,
                                                                  parentAnnotationGUID);

        return restResult.getGUID();
    }


    /**
     * Replace the current properties of an annotation.
     *
     * @param userId identifier of calling user
     * @param annotation new properties
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the annotation in the annotation store.
     */
    void  updateAnnotation(String     userId,
                           Annotation annotation) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String   methodName = "updateAnnotation";
        final String   annotationParameterName = "annotation";
        final String   headerParameterName = "annotation.getElementHeader()";
        final String   annotationGUIDParameterName = "annotation.getElementHeader().getGUID()";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);
        invalidParameterHandler.validateObject(annotation.getElementHeader(), headerParameterName, methodName);

        ElementHeader elementHeader = annotation.getElementHeader();

        invalidParameterHandler.validateGUID(elementHeader.getGUID(), annotationGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        annotation,
                                        serverName,
                                        userId,
                                        elementHeader.getGUID());
    }


    /**
     * Remove an annotation from the annotation store.
     *
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem deleting the annotation from the annotation store.
     */
    void  deleteAnnotation(String   userId,
                           String   annotationGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String   methodName = "deleteAnnotation";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        annotationGUID);
    }


    /**
     * Return the list of data fields from previous runs of the discovery service.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID unique identifier of the discovery analysis report
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     * @return list of data fields (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving data fields from the annotation store.
     */
    List<DataField>  getPreviousDataFieldsForAsset(String   userId,
                                                   String   discoveryReportGUID,
                                                   int      startingFrom,
                                                   int      maximumResults) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String   methodName = "getPreviousDataFieldsForAsset";
        final String   discoveryReportGUIDParameterName = "discoveryReportGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}/data-fields/previous?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, discoveryReportGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        DataFieldListResponse restResult = restClient.callDataFieldListGetRESTCall(methodName,
                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     discoveryReportGUID,
                                                                                     Integer.toString(startingFrom),
                                                                                     Integer.toString(maximumResults));

        return restResult.getDataFields();
    }


    /**
     * Return the current list of data fields for this discovery run.
     *
     * @param startingFrom starting position in the list.
     * @param maximumResults maximum number of elements that can be returned
     * @return list of data fields (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving data fields from the annotation store.
     */
    List<DataField> getNewDataFieldsForAsset(String    userId,
                                             String    discoveryReportGUID,
                                             int       startingFrom,
                                             int       maximumResults) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String   methodName = "getNewDataFieldsForAsset";
        final String   discoveryReportGUIDParameterName = "discoveryReportGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}/data-fields?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, discoveryReportGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        DataFieldListResponse restResult = restClient.callDataFieldListGetRESTCall(methodName,
                                                                                   serverPlatformRootURL + urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   discoveryReportGUID,
                                                                                   Integer.toString(startingFrom),
                                                                                   Integer.toString(maximumResults));

        return restResult.getDataFields();
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param parentDataFieldGUID parent data field identifier
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of DataField objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    List<DataField>  getNestedDataFields(String   userId,
                                         String   parentDataFieldGUID,
                                         int      startingFrom,
                                         int      maximumResults) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String   methodName = "getNestedDataFields";
        final String   dataFieldGUIDParameterName = "parentDataFieldGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/data-fields/{2}/nested-data-fields?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentDataFieldGUID, dataFieldGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        DataFieldListResponse restResult = restClient.callDataFieldListGetRESTCall(methodName,
                                                                                   serverPlatformRootURL + urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   parentDataFieldGUID,
                                                                                   Integer.toString(startingFrom),
                                                                                   Integer.toString(maximumResults));

        return restResult.getDataFields();
    }


    /**
     * Return a specific data field stored in the annotation store (previous or new).
     *
     * @param dataFieldGUID unique identifier of the data field
     * @return data field object
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving the data field from the annotation store.
     */
    DataField  getDataField(String   userId,
                            String   dataFieldGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String   methodName = "getDataField";
        final String   dataFieldGUIDParameterName = "dataFieldGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/data-fields/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFieldGUID, dataFieldGUIDParameterName, methodName);

        DataFieldResponse restResult = restClient.callDataFieldGetRESTCall(methodName,
                                                                                   serverPlatformRootURL + urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   dataFieldGUID);

        return restResult.getDataField();
    }


    /**
     * Add a new data field to the Annotation store linked off of an annotation (typically SchemaAnalysisAnnotation).
     *
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation that the data field is to be linked to
     * @param dataField dataField object
     * @return unique identifier of new data field
     * @throws InvalidParameterException the dataField is invalid or the annotation GUID points to an annotation
     *                                   that can not be associated with a data field.
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem  adding the data field to the Annotation store.
     */
    String  addDataFieldToDiscoveryReport(String    userId,
                                          String    annotationGUID,
                                          DataField dataField) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String   methodName = "addDataFieldToDiscoveryReport";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   dataFieldParameterName = "dataField";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}/data-fields";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(dataField, dataFieldParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  dataField,
                                                                  serverName,
                                                                  userId,
                                                                  annotationGUID);

        return restResult.getGUID();
    }


    /**
     * Add a new data field and link it to an existing data field.
     *
     * @param userId identifier of calling user
     * @param parentDataFieldGUID unique identifier of the data field that this new one is to be attached to
     * @param dataField data field object
     * @return unique identifier of new data field
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving data fields in the annotation store.
     */
    String  addDataFieldToDataField(String    userId,
                                    String    parentDataFieldGUID,
                                    DataField dataField) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String   methodName = "addDataFieldToDataField";
        final String   dataFieldGUIDParameterName = "parentDataFieldGUID";
        final String   dataFieldParameterName = "dataField";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/data-fields/{2}/nested-data-fields";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentDataFieldGUID, dataFieldGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(dataField, dataFieldParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  dataField,
                                                                  serverName,
                                                                  userId,
                                                                  parentDataFieldGUID);

        return restResult.getGUID();
    }


    /**
     * Add a new annotation and link it to an existing data field.
     *
     * @param userId identifier of calling user
     * @param parentDataFieldGUID unique identifier of the data field that this new one is to be attached to
     * @param annotation data field object
     * @return unique identifier of new annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving data fields in the annotation store.
     */
    String  addAnnotationToDataField(String     userId,
                                     String     parentDataFieldGUID,
                                     Annotation annotation) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String   methodName = "addAnnotationToDataField";
        final String   dataFieldGUIDParameterName = "parentDataFieldGUID";
        final String   annotationParameterName = "annotation";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/data-fields/{2}/annotations";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentDataFieldGUID, dataFieldGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  annotation,
                                                                  serverName,
                                                                  userId,
                                                                  parentDataFieldGUID);

        return restResult.getGUID();
    }


    /**
     * Replace the current properties of a data field.
     *
     * @param userId identifier of calling user
     * @param dataField new properties
     *
     * @return fully filled out data field
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the data field in the annotation store.
     */
    void  updateDataField(String    userId,
                          DataField dataField) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException
    {
        final String   methodName = "updateDataField";
        final String   dataFieldParameterName = "dataField";
        final String   headerParameterName = "dataField.getElementHeader()";
        final String   annotationGUIDParameterName = "dataField.getElementHeader().getGUID()";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/data-fields/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(dataField, dataFieldParameterName, methodName);
        invalidParameterHandler.validateObject(dataField.getElementHeader(), headerParameterName, methodName);

        ElementHeader elementHeader = dataField.getElementHeader();

        invalidParameterHandler.validateGUID(elementHeader.getGUID(), annotationGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        dataField,
                                        serverName,
                                        userId,
                                        elementHeader.getGUID());
    }


    /**
     * Remove a data field from the annotation store.
     *
     * @param userId identifier of calling user
     * @param dataFieldGUID unique identifier of the data field
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem deleting the data field from the annotation store.
     */
    void  deleteDataField(String   userId,
                          String   dataFieldGUID) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String   methodName = "deleteDataField";
        final String   dataFieldGUIDParameterName = "dataFieldGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/data-fields/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFieldGUID, dataFieldGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        dataFieldGUID);
    }
}
