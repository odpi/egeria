/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.builders.DiscoveryAnalysisReportBuilder;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.converters.DiscoveryAnalysisReportConverter;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers.DiscoveryAnalysisReportMapper;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers.DiscoveryEnginePropertiesMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.AnnotationStatus;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DiscoveryAnalysisReportHandler manages the storage and retrieval of metadata relating to discovery
 * analysis reports as defined in the Open Discovery Framework (ODF).
 */
public class DiscoveryAnalysisReportHandler
{
    private String                  serviceName;
    private String                  serverName;
    private OMRSRepositoryHelper    repositoryHelper;
    private RepositoryHandler       repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;
    private AnnotationHandler       annotationHandler;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param annotationHandler handler for managing annotations
     */
    public DiscoveryAnalysisReportHandler(String                  serviceName,
                                          String                  serverName,
                                          InvalidParameterHandler invalidParameterHandler,
                                          RepositoryHandler       repositoryHandler,
                                          OMRSRepositoryHelper    repositoryHelper,
                                          AnnotationHandler       annotationHandler)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
        this.annotationHandler = annotationHandler;
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
     * @param methodName calling method
     *
     * @return The new discovery report.
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or report
     * @throws PropertyServerException there was a problem in the store whether the asset/report properties are kept.
     */
    public DiscoveryAnalysisReport createDiscoveryAnalysisReport(String                  userId,
                                                                 String                  qualifiedName,
                                                                 String                  displayName,
                                                                 String                  description,
                                                                 Date                    creationDate,
                                                                 Map<String, String>     analysisParameters,
                                                                 DiscoveryRequestStatus discoveryRequestStatus,
                                                                 String                  assetGUID,
                                                                 String                  discoveryEngineGUID,
                                                                 String                  discoveryServiceGUID,
                                                                 Map<String, String>     additionalProperties,
                                                                 List<Classification>    classifications,
                                                                 String                  methodName) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String   nameParameterName = "qualifiedName";
        final String   assetGUIDParameterName = "assetGUID";
        final String   discoveryEngineGUIDParameterName = "discoveryEngineGUID";
        final String   discoveryServiceGUIDParameterName = "discoveryServiceGUID";

        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, discoveryServiceGUIDParameterName, methodName);

        repositoryHandler.validateEntityGUID(userId,
                                             assetGUID,
                                             AssetMapper.ASSET_TYPE_NAME,
                                             methodName,
                                             assetGUIDParameterName);

        repositoryHandler.validateEntityGUID(userId,
                                             assetGUID,
                                             DiscoveryEnginePropertiesMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                             methodName,
                                             discoveryEngineGUIDParameterName);

        DiscoveryAnalysisReportBuilder builder = new DiscoveryAnalysisReportBuilder(qualifiedName,
                                                                                    displayName,
                                                                                    description,
                                                                                    creationDate,
                                                                                    analysisParameters,
                                                                                    discoveryRequestStatus,
                                                                                    assetGUID,
                                                                                    discoveryEngineGUID,
                                                                                    discoveryServiceGUID,
                                                                                    additionalProperties,
                                                                                    null,
                                                                                    repositoryHelper,
                                                                                    serviceName,
                                                                                    serverName);

        String  reportGUID = repositoryHandler.createEntity(userId,
                                                            DiscoveryAnalysisReportMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_GUID,
                                                            DiscoveryAnalysisReportMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME,
                                                            builder.getInstanceProperties(methodName),
                                                            methodName);

        if (reportGUID != null)
        {
            repositoryHandler.createRelationship(userId,
                                                 DiscoveryAnalysisReportMapper.REPORT_TO_ASSET_TYPE_GUID,
                                                 assetGUID,
                                                 reportGUID,
                                                 null,
                                                 methodName);

            repositoryHandler.createRelationship(userId,
                                                 DiscoveryAnalysisReportMapper.REPORT_TO_ENGINE_TYPE_GUID,
                                                 discoveryEngineGUID,
                                                 reportGUID,
                                                 null,
                                                 methodName);

            repositoryHandler.createRelationship(userId,
                                                 DiscoveryAnalysisReportMapper.REPORT_TO_SERVICE_TYPE_GUID,
                                                 discoveryServiceGUID,
                                                 reportGUID,
                                                 null,
                                                 methodName);
        }

        return null;
    }


    /**
     * Update the properties of the discovery analysis report.
     *
     * @param userId calling user.
     * @param updatedReport updated report - this will replace what was previous stored.
     * @param discoveryReportGUID identifier of the discovery report
     * @return the new values stored in the repository
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public DiscoveryAnalysisReport  updateDiscoveryAnalysisReport(String                  userId,
                                                                  String                  discoveryReportGUID,
                                                                  DiscoveryAnalysisReport updatedReport) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        final String   methodName = "updateDiscoveryAnalysisReport";
        final String   reportParameterName = "updatedReport";
        final String   reportGUIDParameterName = "updatedReport.getGUID()";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(updatedReport, reportParameterName, methodName);
        invalidParameterHandler.validateGUID(updatedReport.getGUID(), reportGUIDParameterName, methodName);

        // TODO
        return null;
    }


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param methodName calling method
     *
     * @return discovery report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public DiscoveryAnalysisReport getDiscoveryAnalysisReport(String   userId,
                                                              String   discoveryReportGUID,
                                                              String   methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String   reportGUIDParameterName = "discoveryReportGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, reportGUIDParameterName, methodName);

        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                discoveryReportGUID,
                                                                reportGUIDParameterName,
                                                                DiscoveryAnalysisReportMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME,
                                                                methodName);

        DiscoveryAnalysisReportConverter converter = new DiscoveryAnalysisReportConverter(entity,
                                                                                          repositoryHelper,
                                                                                          serviceName);

        return converter.getBean();
    }


    /**
     * Return the discovery analysis reports about the asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maximumResults maximum number of elements to return an this call
     * @param methodName calling method
     *
     * @return list of discovery analysis reports
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public List<DiscoveryAnalysisReport>   getDiscoveryAnalysisReports(String  userId,
                                                                       String  assetGUID,
                                                                       int     startingFrom,
                                                                       int     maximumResults,
                                                                       String  methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String   assetGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        // todo
        return null;
    }


    /**
     * Return the annotations linked direction to the report.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param annotationStatus status of the desired annotations - null means all statuses.
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
    public List<Annotation> getDiscoveryReportAnnotations(String           userId,
                                                          String           discoveryReportGUID,
                                                          AnnotationStatus annotationStatus,
                                                          int              startingFrom,
                                                          int              maximumResults,
                                                          String           methodName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String   reportGUIDParameterName = "discoveryReportGUID";

        return annotationHandler.getAnnotationsLinkedToAnchor(userId,
                                                              discoveryReportGUID,
                                                              DiscoveryAnalysisReportMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME,
                                                              reportGUIDParameterName,
                                                              DiscoveryAnalysisReportMapper.REPORT_TO_ANNOTATIONS_TYPE_GUID,
                                                              DiscoveryAnalysisReportMapper.REPORT_TO_ANNOTATIONS_TYPE_NAME,
                                                              annotationStatus,
                                                              startingFrom,
                                                              maximumResults,
                                                              methodName);
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
     * @param methodName calling method
     * @return list of annotation (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    public  List<Annotation> getAnnotationsForAssetByStatus(String           userId,
                                                            String           assetGUID,
                                                            AnnotationStatus status,
                                                            int              startingFrom,
                                                            int              maximumResults,
                                                            String           methodName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String   assetGUIDParameterName = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        // todo
        return null;
    }


    /**
     * Add a new annotation to the annotation store as a top level annotation linked directly off of the report.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID unique identifier of the discovery analysis report
     * @param annotation annotation object
     * @param methodName calling method
     * @return unique identifier of new annotation
     * @throws InvalidParameterException the annotation is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving adding the annotation to the annotation store.
     */
    public  String  addAnnotationToDiscoveryReport(String     userId,
                                                   String     discoveryReportGUID,
                                                   Annotation annotation,
                                                   String     methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String   annotationParameterName = "annotation";
        final String   reportGUIDParameterName = "discoveryReportGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/discovery-analysis-reports/{2}/annotations";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, reportGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);

        // todo
        return null;
    }
}
