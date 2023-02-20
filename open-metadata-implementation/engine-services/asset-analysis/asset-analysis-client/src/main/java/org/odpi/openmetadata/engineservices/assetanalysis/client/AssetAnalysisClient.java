/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.assetanalysis.client;

import org.odpi.openmetadata.accessservices.discoveryengine.rest.AnnotationListResponse;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.AnnotationResponse;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.DiscoveryAnalysisReportResponse;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.DiscoveryRequestRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.properties.ConnectorReport;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorReportResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.engineservices.assetanalysis.api.AssetAnalysisAPI;
import org.odpi.openmetadata.engineservices.assetanalysis.client.rest.AssetAnalysisRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryEngine;
import org.odpi.openmetadata.frameworks.discovery.ffdc.DiscoveryEngineException;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;

import java.util.List;
import java.util.Map;

/**
 * AssetAnalysisClient is a client-side library for calling a specific discovery engine with an engine host server.
 */
public class AssetAnalysisClient extends DiscoveryEngine implements AssetAnalysisAPI
{
    private final String                  serverName;               /* Initialized in constructor */
    private final String                  serverPlatformRootURL;    /* Initialized in constructor */
    private final String                  discoveryEngineName;      /* Initialized in constructor */
    private final AssetAnalysisRESTClient restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();


    /**
     * Create a client-side object for calling a discovery engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the discovery engine is running.
     * @param serverName the name of the engine host server where the discovery engine is running
     * @param discoveryEngineName the unique name of the discovery engine.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public AssetAnalysisClient(String serverPlatformRootURL,
                               String serverName,
                               String discoveryEngineName) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;
        this.discoveryEngineName   = discoveryEngineName;

        this.restClient = new AssetAnalysisRESTClient(serverName, serverPlatformRootURL);
    }


    /**
     * Create a client-side object for calling a discovery engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the discovery engine is running.
     * @param serverName the name of the engine host server where the discovery engine is running
     * @param discoveryEngineName the unique name of the discovery engine.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public AssetAnalysisClient(String serverPlatformRootURL,
                               String serverName,
                               String discoveryEngineName,
                               String userId,
                               String password) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;
        this.discoveryEngineName   = discoveryEngineName;

        this.restClient = new AssetAnalysisRESTClient(serverName, serverPlatformRootURL, userId, password);
    }


    /**
     * Validate the connector and return its connector type.
     *
     * @param userId calling user
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector report for this connector
     *
     * @throws InvalidParameterException the connector provider class name is not a valid connector fo this service
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration service
     */
    public ConnectorReport validateConnector(String userId,
                                             String connectorProviderClassName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String   methodName = "validateConnector";
        final String   nameParameter = "connectorProviderClassName";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-services/asset-analysis/users/{1}/validate-connector";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(connectorProviderClassName, nameParameter, methodName);

        ConnectorReportResponse restResult = restClient.callOCFConnectorReportGetRESTCall(methodName,
                                                                                          serverPlatformRootURL + urlTemplate,
                                                                                          serverName,
                                                                                          userId,
                                                                                          connectorProviderClassName);

        return restResult.getConnectorReport();
    }


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param userId identifier of calling user
     * @param assetGUID identifier of the asset to analyze.
     * @param discoveryRequestType identifier of the type of analysis - this determines which discovery service to run.
     *
     * @return unique id for the discovery request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  String discoverAsset(String   userId,
                                 String   assetGUID,
                                 String   discoveryRequestType) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       DiscoveryEngineException
    {
        return this.discoverAsset(userId, assetGUID, discoveryRequestType, null, null);
    }


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param userId identifier of calling user
     * @param assetGUID identifier of the asset to analyze.
     * @param discoveryRequestType identifier of the type of analysis - this determines which discovery service to run.
     * @param analysisParameters name value properties to control the analysis
     *
     * @return unique id for the discovery request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  String discoverAsset(String              userId,
                                 String              assetGUID,
                                 String              discoveryRequestType,
                                 Map<String, String> analysisParameters) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                DiscoveryEngineException
    {
        return this.discoverAsset(userId, assetGUID, discoveryRequestType, analysisParameters, null);
    }


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param userId identifier of calling user
     * @param assetGUID identifier of the asset to analyze.
     * @param discoveryRequestType identifier of the type of analysis - this determines which discovery service to run.
     * @param analysisParameters name value properties to control the analysis
     * @param annotationTypes list of the types of annotations to produce (and no others)
     *
     * @return unique id for the discovery request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  String discoverAsset(String              userId,
                                 String              assetGUID,
                                 String              discoveryRequestType,
                                 Map<String, String> analysisParameters,
                                 List<String>        annotationTypes) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             DiscoveryEngineException
    {
        final String methodName = "discoverAsset";
        final String assetGUIDParameterName = "assetGUID";
        final String discoveryRequestTypeParameterName = "discoveryRequestType";
        final String urlTemplate
                = "/servers/{0}/open-metadata/engine-services/asset-analysis/users/{1}/discovery-engines/{2}/discovery-request-types/{3}/assets/{4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateName(discoveryRequestType, discoveryRequestTypeParameterName, methodName);

        DiscoveryRequestRequestBody requestBody = new DiscoveryRequestRequestBody();

        requestBody.setAnalysisParameters(analysisParameters);
        requestBody.setAnnotationTypes(annotationTypes);

        try
        {
            GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                      serverPlatformRootURL + urlTemplate,
                                                                      requestBody,
                                                                      serverName,
                                                                      userId,
                                                                      discoveryEngineName,
                                                                      discoveryRequestType,
                                                                      assetGUID);

            exceptionHandler.detectAndThrowInvalidParameterException(restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            exceptionHandler.detectAndThrowPropertyServerException(restResult);

            return restResult.getGUID();
        }
        catch (PropertyServerException  exception)
        {
            throw new DiscoveryEngineException(exception.getReportedErrorMessage(), exception);
        }
    }


    /**
     * Explore each of the assets in the asset store.  A new instance of the discovery service is started for
     * each retrieved asset.
     *
     * @param userId identifier of calling user
     * @param discoveryRequestType identifier of the type of analysis - this determines which discovery service to run.
     * @param analysisParameters name value properties to control the analysis
     * @param annotationTypes list of the types of annotations to produce (and no others)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public void scanAllAssets(String              userId,
                              String              discoveryRequestType,
                              Map<String, String> analysisParameters,
                              List<String>        annotationTypes) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          DiscoveryEngineException
    {
        final String methodName = "scanAllAssets";
        final String discoveryRequestTypeParameterName = "discoveryRequestType";
        final String urlTemplate = "/servers/{0}/open-metadata/engine-services/asset-analysis/users/{1}/discovery-engines/{2}/discovery-request-types/{3}/assets";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(discoveryRequestType, discoveryRequestTypeParameterName, methodName);

        DiscoveryRequestRequestBody requestBody = new DiscoveryRequestRequestBody();

        requestBody.setAnalysisParameters(analysisParameters);
        requestBody.setAnnotationTypes(annotationTypes);

        try
        {
            VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                      serverPlatformRootURL + urlTemplate,
                                                                      requestBody,
                                                                      serverName,
                                                                      userId,
                                                                      discoveryEngineName,
                                                                      discoveryRequestType);

            exceptionHandler.detectAndThrowInvalidParameterException(restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            exceptionHandler.detectAndThrowPropertyServerException(restResult);
        }
        catch (PropertyServerException  exception)
        {
            throw new DiscoveryEngineException(exception.getReportedErrorMessage(), exception);
        }
    }


    /**
     * Request the status of an executing discovery request.
     *
     * @param userId identifier of calling user
     * @param discoveryRequestGUID identifier of the discovery request.
     *
     * @return status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public DiscoveryRequestStatus getDiscoveryStatus(String   userId,
                                                     String   discoveryRequestGUID) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            DiscoveryEngineException
    {
        DiscoveryAnalysisReport report = this.getDiscoveryReport(userId, discoveryRequestGUID);

        if (report != null)
        {
            return report.getDiscoveryRequestStatus();
        }

        return null;
    }


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param userId identifier of calling user
     * @param discoveryRequestGUID identifier of the discovery request.
     *
     * @return discovery report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public DiscoveryAnalysisReport getDiscoveryReport(String   userId,
                                                      String   discoveryRequestGUID) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            DiscoveryEngineException
    {
        final String   methodName = "getDiscoveryAnalysisReport";
        final String   reportGUIDParameterName = "discoveryRequestGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-services/asset-analysis/users/{1}/discovery-engines/{2}/discovery-analysis-reports/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryRequestGUID, reportGUIDParameterName, methodName);

        try
        {
            DiscoveryAnalysisReportResponse restResult = restClient.callDiscoveryAnalysisReportGetRESTCall(methodName,
                                                                                                           serverPlatformRootURL + urlTemplate,
                                                                                                           serverName,
                                                                                                           userId,
                                                                                                           discoveryEngineName,
                                                                                                           discoveryRequestGUID);

            exceptionHandler.detectAndThrowInvalidParameterException(restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            exceptionHandler.detectAndThrowPropertyServerException(restResult);

            return restResult.getAnalysisReport();

        }
        catch (PropertyServerException  exception)
        {
            throw new DiscoveryEngineException(exception.getReportedErrorMessage(), exception);
        }
    }


    /**
     * Return the annotations linked direction to the report.
     *
     * @param userId identifier of calling user
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  List<Annotation> getDiscoveryReportAnnotations(String   userId,
                                                           String   discoveryRequestGUID,
                                                           int      startingFrom,
                                                           int      maximumResults) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           DiscoveryEngineException
    {
        final String   methodName = "getDiscoveryReportAnnotations";
        final String   reportGUIDParameterName = "discoveryReportGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-services/asset-analysis/users/{1}/discovery-engines/{2}/discovery-analysis-reports/{3" +
                "}/annotations?startingFrom={4}&maximumResults={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryRequestGUID, reportGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        try
        {
            AnnotationListResponse restResult = restClient.callAnnotationListGetRESTCall(methodName,
                                                                                         serverPlatformRootURL + urlTemplate,
                                                                                         serverName,
                                                                                         userId,
                                                                                         discoveryEngineName,
                                                                                         discoveryRequestGUID,
                                                                                         Integer.toString(startingFrom),
                                                                                         Integer.toString(maximumResults));

            exceptionHandler.detectAndThrowInvalidParameterException(restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            exceptionHandler.detectAndThrowPropertyServerException(restResult);

            return restResult.getAnnotations();
        }
        catch (PropertyServerException  exception)
        {
            throw new DiscoveryEngineException(exception.getReportedErrorMessage(), exception);
        }
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  List<Annotation>  getExtendedAnnotations(String   userId,
                                                     String   annotationGUID,
                                                     int      startingFrom,
                                                     int      maximumResults) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     DiscoveryEngineException
    {
        final String methodName                  = "getExtendedAnnotations";
        final String annotationGUIDParameterName = "annotationGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/engine-services/asset-analysis/users/{1}/discovery-engines/{2}/annotations/{4}/extended-annotations?startingFrom={5}&maximumResults={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        try
        {
            AnnotationListResponse restResult = restClient.callAnnotationListGetRESTCall(methodName,
                                                                                         serverPlatformRootURL + urlTemplate,
                                                                                         serverName,
                                                                                         userId,
                                                                                         discoveryEngineName,
                                                                                         annotationGUID,
                                                                                         Integer.toString(startingFrom),
                                                                                         Integer.toString(maximumResults));

            exceptionHandler.detectAndThrowInvalidParameterException(restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            exceptionHandler.detectAndThrowPropertyServerException(restResult);

            return restResult.getAnnotations();
        }
        catch (PropertyServerException exception)
        {
            throw new DiscoveryEngineException(exception.getReportedErrorMessage(), exception);
        }
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
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  Annotation getAnnotation(String   userId,
                                     String   annotationGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     DiscoveryEngineException
    {
        final String   methodName = "getAnnotation";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-services/asset-analysis/users/{1}/discovery-engines/{2}/annotations/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        try
        {
            AnnotationResponse restResult = restClient.callAnnotationGetRESTCall(methodName,
                                                                                 serverPlatformRootURL + urlTemplate,
                                                                                 serverName,
                                                                                 userId,
                                                                                 discoveryEngineName,
                                                                                 annotationGUID);

            exceptionHandler.detectAndThrowInvalidParameterException(restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            exceptionHandler.detectAndThrowPropertyServerException(restResult);

            return restResult.getAnnotation();
        }
        catch (PropertyServerException exception)
        {
            throw new DiscoveryEngineException(exception.getReportedErrorMessage(), exception);
        }
    }
}
