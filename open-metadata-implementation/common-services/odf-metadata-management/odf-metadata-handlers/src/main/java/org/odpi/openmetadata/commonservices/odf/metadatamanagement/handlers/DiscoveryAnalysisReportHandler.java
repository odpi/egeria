/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;
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
        final String   assetGUIDParameterName = "assetGUID";
        final String   discoveryEngineGUIDParameterName = "discoveryEngineGUID";
        final String   discoveryServiceGUIDParameterName = "discoveryServiceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(discoveryEngineGUID, discoveryEngineGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(discoveryServiceGUID, discoveryServiceGUIDParameterName, methodName);

        // TODO
        return null;
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
     *
     * @return discovery report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public DiscoveryAnalysisReport getDiscoveryReport(String   userId,
                                                      String   discoveryReportGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String   methodName = "getDiscoveryReport";
        final String   reportGUIDParameterName = "discoveryReportGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(discoveryReportGUID, reportGUIDParameterName, methodName);

        // TODO
        return null;
    }


    /**
     * Return the annotations linked direction to the report.
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

        return annotationHandler.getAnnotationsLinkedToAnchor(userId,
                                                              discoveryReportGUID,
                                                              reportGUIDParameterName,
                                                              startingFrom,
                                                              maximumResults,
                                                              methodName);
    }


}
