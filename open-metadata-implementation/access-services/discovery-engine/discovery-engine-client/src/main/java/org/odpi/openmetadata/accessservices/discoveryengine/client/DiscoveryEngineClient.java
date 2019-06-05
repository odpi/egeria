/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.client;

import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.ConnectedAssetClientBase;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.client.ODFRESTClient;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.AnnotationStatus;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DiscoveryEngineClient provides the client-side operational REST APIs for a running Discovery Engine
 */
public class DiscoveryEngineClient extends ConnectedAssetClientBase
{
    private ODFRESTClient restClient;               /* Initialized in constructor */

    /**
     * Constructor sets up the key parameters for accessing the asset store.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param restClient client for calling REST APIs
     */
    public DiscoveryEngineClient(String        serverName,
                                 String        serverPlatformRootURL,
                                 ODFRESTClient restClient) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL);

        this.restClient = restClient;
    }


    /**
     * Return the connection information for the asset.  This is used to create the connector.  The connector
     * is an Open Connector Framework (OCF) connector that provides access to the asset's data and metadata properties.
     *
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

        return super.getConnectionForAsset(restClient, userId, assetGUID);
    }


    /**
     * Create a new discovery analysis report and chain it to its asset, discovery engine and discovery service.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the report
     * @param displayName short name for the report
     * @param description description of the report
     * @param creationDate data of the report
     * @param analysisParameters analysis parameters passed to the discovery service
     * @param discoveryRequestStatus current status of the discovery processing
     * @param assetGUID unique identifier of the asset being analysed
     * @param discoveryEngineGUID unique identifier of the discovery engine that is running the discovery service
     * @param discoveryServiceGUID unique identifier of the discovery service creating the report
     * @param additionalProperties additional properties for the report
     * @param classifications classifications to attach to the report
     *
     * @return The new discovery report.
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or report
     * @throws PropertyServerException there was a problem in the store whether the asset/report properties are kept.
     */
    public  DiscoveryAnalysisReport  createDiscoveryAnalysisReport(String                  userId,
                                                                   String                  qualifiedName,
                                                                   String                  displayName,
                                                                   String                  description,
                                                                   Date                    creationDate,
                                                                   Map<String, String>     analysisParameters,
                                                                   DiscoveryRequestStatus  discoveryRequestStatus,
                                                                   String                  assetGUID,
                                                                   String                  discoveryEngineGUID,
                                                                   String                  discoveryServiceGUID,
                                                                   Map<String, String>     additionalProperties,
                                                                   List<Classification>    classifications) throws InvalidParameterException,
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
        requestBody.setClassifications(classifications);

        DiscoveryAnalysisReportResponse restResult = restClient.callDiscoveryAnalysisReportPostRESTCall(methodName,
                                                                                                        serverPlatformRootURL + urlTemplate,
                                                                                                        requestBody,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        assetGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getAnalysisReport();
    }


    /**
     * Update the properties of the discovery analysis report.
     *
     * @param userId calling user.
     * @param updatedReport updated report - this will replace what was previous stored.
     * @return the new values stored in the repository
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public DiscoveryAnalysisReport  updateDiscoveryAnalysisReport(String                  userId,
                                                                  DiscoveryAnalysisReport updatedReport) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        final String   methodName = "updateDiscoveryAnalysisReport";
        final String   reportParameterName = "updatedReport";
        final String   reportGUIDParameterName = "updatedReport.getGUID()";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(updatedReport, reportParameterName, methodName);
        invalidParameterHandler.validateGUID(updatedReport.getGUID(), reportGUIDParameterName, methodName);

        DiscoveryAnalysisReportResponse restResult = restClient.callDiscoveryAnalysisReportPostRESTCall(methodName,
                                                                                                        serverPlatformRootURL + urlTemplate,
                                                                                                        updatedReport,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        updatedReport.getGUID());

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getAnalysisReport();
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
     * Request the discovery report for a discovery request that has completed.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
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

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getAnalysisReport();
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
    public  List<Annotation> getAnnotationsForAssetByStatus(String           userId,
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

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

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

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getAnnotations();
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

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

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
    public  Annotation        getAnnotation(String   userId,
                                            String   annotationGUID) throws InvalidParameterException,
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

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

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
     * @throws PropertyServerException there was a problem retrieving adding the annotation to the annotation store.
     */
    public  String  addAnnotationToDiscoveryReport(String     userId,
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

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }


    /**
     * Add a new annotation and link it to an existing annotation.
     *
     * @param userId identifier of calling user
     * @param anchorAnnotationGUID unique identifier of the annotation that this new one os to be attached to
     * @param annotation annotation object
     * @return fully filled out annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving annotations in the annotation store.
     */
    public  Annotation  addAnnotationToAnnotation(String     userId,
                                                  String     anchorAnnotationGUID,
                                                  Annotation annotation) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String   methodName = "addAnnotationToAnnotation";
        final String   annotationGUIDParameterName = "anchorAnnotationGUID";
        final String   annotationParameterName = "annotation";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}/extended-annotations";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorAnnotationGUID, annotationGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);

        AnnotationResponse restResult = restClient.callAnnotationPostRESTCall(methodName,
                                                                              serverPlatformRootURL + urlTemplate,
                                                                              annotation,
                                                                              serverName,
                                                                              userId,
                                                                              anchorAnnotationGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getAnnotation();
    }


    /**
     * Link an existing annotation to another object.  The anchor object must be a Referenceable.
     *
     * @param userId identifier of calling user
     * @param anchorGUID unique identifier that the annotation is to be linked to
     * @param annotationGUID unique identifier of the annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating annotations in the annotation store.
     */
    public  void    linkAnnotation(String userId,
                                   String anchorGUID,
                                   String annotationGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String   methodName = "linkAnnotation";
        final String   anchorGUIDParameterName = "anchorGUID";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}/related-instances/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, anchorGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  nullRequestBody,
                                                                  serverName,
                                                                  userId,
                                                                  annotationGUID,
                                                                  anchorGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Remove the relationship between an annotation and another object.
     *
     * @param userId identifier of calling user
     * @param anchorGUID unique identifier that the annotation is to be unlinked from
     * @param annotationGUID unique identifier of the annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating annotations in the annotation store.
     */
    public  void    unlinkAnnotation(String userId,
                                     String anchorGUID,
                                     String annotationGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String   methodName = "unlinkAnnotation";
        final String   anchorGUIDParameterName = "anchorGUID";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}/related-instances{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, anchorGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  nullRequestBody,
                                                                  serverName,
                                                                  userId,
                                                                  annotationGUID,
                                                                  anchorGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }


    /**
     * Replace the current properties of an annotation.
     *
     * @param userId identifier of calling user
     * @param annotation new properties
     *
     * @return fully filled out annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the annotation in the annotation store.
     */
    public  Annotation  updateAnnotation(String     userId,
                                         Annotation annotation) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String   methodName = "updateAnnotation";
        final String   annotationParameterName = "annotation";
        final String   annotationGUIDParameterName = "annotation.getGUID()";

        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);
        invalidParameterHandler.validateGUID(annotation.getGUID(), annotationGUIDParameterName, methodName);

        AnnotationResponse restResult = restClient.callAnnotationPostRESTCall(methodName,
                                                                              serverPlatformRootURL + urlTemplate,
                                                                              annotation,
                                                                              serverName,
                                                                              userId,
                                                                              annotation.getGUID());

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getAnnotation();
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
    public  void  deleteAnnotation(String   userId,
                                   String   annotationGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String   methodName = "deleteAnnotation";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  serverPlatformRootURL + urlTemplate,
                                                                  nullRequestBody,
                                                                  serverName,
                                                                  userId,
                                                                  annotationGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }
}
