/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DiscoveryAnalysisReportHandler manages the storage and retrieval of metadata relating to discovery
 * analysis reports as defined in the Open Discovery Framework (ODF).
 */
public class DiscoveryAnalysisReportHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    /**
     * Construct the handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from.
     * @param defaultZones list of zones that the access service should set in all new B instances.
     * @param publishZones list of zones that the access service sets up in published B instances.
     * @param auditLog destination for audit log events.
     */
    public DiscoveryAnalysisReportHandler(OpenMetadataAPIGenericConverter<B> converter,
                                          Class<B>                           beanClass,
                                          String                             serviceName,
                                          String                             serverName,
                                          InvalidParameterHandler            invalidParameterHandler,
                                          RepositoryHandler                  repositoryHandler,
                                          OMRSRepositoryHelper               repositoryHelper,
                                          String                             localServerUserId,
                                          OpenMetadataServerSecurityVerifier securityVerifier,
                                          List<String>                       supportedZones,
                                          List<String>                       defaultZones,
                                          List<String>                       publishZones,
                                          AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);
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
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return The new discovery report.
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or report
     * @throws PropertyServerException there was a problem in the store whether the asset/report properties are kept.
     */
    public String createDiscoveryAnalysisReport(String                 userId,
                                                String                 qualifiedName,
                                                String                 displayName,
                                                String                 description,
                                                Date                   creationDate,
                                                Map<String, String>    analysisParameters,
                                                int                    discoveryRequestStatus,
                                                String                 assetGUID,
                                                String                 discoveryEngineGUID,
                                                String                 discoveryServiceGUID,
                                                Map<String, String>    additionalProperties,
                                                Date                   effectiveFrom,
                                                Date                   effectiveTo,
                                                boolean                forLineage,
                                                boolean                forDuplicateProcessing,
                                                Date                   effectiveTime,
                                                String                 methodName) throws InvalidParameterException,
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


        repositoryHandler.getEntityByGUID(userId,
                                          assetGUID,
                                          assetGUIDParameterName,
                                          OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);

        repositoryHandler.getEntityByGUID(userId,
                                          discoveryEngineGUIDParameterName,
                                          assetGUID,
                                          OpenMetadataAPIMapper.DISCOVERY_ENGINE_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);

        DiscoveryAnalysisReportBuilder builder = new DiscoveryAnalysisReportBuilder(qualifiedName,
                                                                                    displayName,
                                                                                    description,
                                                                                    creationDate,
                                                                                    analysisParameters,
                                                                                    discoveryRequestStatus,
                                                                                    additionalProperties,
                                                                                    null,
                                                                                    repositoryHelper,
                                                                                    serviceName,
                                                                                    serverName);

        if (assetGUID != null)
        {
            this.addAnchorGUIDToBuilder(userId,
                                        assetGUID,
                                        assetGUIDParameterName,
                                        false,
                                        false,
                                        effectiveTime,
                                        supportedZones,
                                        builder,
                                        methodName);
        }

        builder.setEffectivityDates(effectiveFrom,effectiveTo);

        String  reportGUID = repositoryHandler.createEntity(userId,
                                                            OpenMetadataAPIMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_GUID,
                                                            OpenMetadataAPIMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME,
                                                            null,
                                                            null,
                                                            builder.getInstanceProperties(methodName),
                                                            null,
                                                            InstanceStatus.ACTIVE,
                                                            methodName);

        if (reportGUID != null)
        {
            repositoryHandler.createRelationship(userId,
                                                 OpenMetadataAPIMapper.REPORT_TO_ASSET_TYPE_GUID,
                                                 null,
                                                 null,
                                                 assetGUID,
                                                 reportGUID,
                                                 null,
                                                 methodName);

            repositoryHandler.createRelationship(userId,
                                                 OpenMetadataAPIMapper.REPORT_TO_ENGINE_TYPE_GUID,
                                                 null,
                                                 null,
                                                 discoveryEngineGUID,
                                                 reportGUID,
                                                 null,
                                                 methodName);

            repositoryHandler.createRelationship(userId,
                                                 OpenMetadataAPIMapper.REPORT_TO_SERVICE_TYPE_GUID,
                                                 null,
                                                 null,
                                                 discoveryServiceGUID,
                                                 reportGUID,
                                                 null,
                                                 methodName);
        }

        return reportGUID;
    }


    /**
     * Update the properties of the discovery analysis report.
     *
     * @param userId calling user.
     * @param discoveryReportGUID identifier of the discovery report
     * @param qualifiedName unique name for the report
     * @param displayName short name for the report
     * @param description description of the report
     * @param creationDate data of the report
     * @param analysisParameters analysis parameters passed to the discovery service
     * @param discoveryRequestStatus current status of the discovery processing
     * @param additionalProperties additional properties for the report
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public void  updateDiscoveryAnalysisReport(String                 userId,
                                               String                 discoveryReportGUID,
                                               String                 qualifiedName,
                                               String                 displayName,
                                               String                 description,
                                               Date                   creationDate,
                                               Map<String, String>    analysisParameters,
                                               int                    discoveryRequestStatus,
                                               Map<String, String>    additionalProperties,
                                               Date                   effectiveFrom,
                                               Date                   effectiveTo,
                                               boolean                isMergeUpdate,
                                               boolean                forLineage,
                                               boolean                forDuplicateProcessing,
                                               Date                   effectiveTime,
                                               String                 methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String   discoveryReportGUIDParameterName = "discoveryReportGUID";


        DiscoveryAnalysisReportBuilder builder = new DiscoveryAnalysisReportBuilder(qualifiedName,
                                                                                    displayName,
                                                                                    description,
                                                                                    creationDate,
                                                                                    analysisParameters,
                                                                                    discoveryRequestStatus,
                                                                                    additionalProperties,
                                                                                    null,
                                                                                    repositoryHelper,
                                                                                    serviceName,
                                                                                    serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    null,
                                    null,
                                    discoveryReportGUID,
                                    discoveryReportGUIDParameterName,
                                    OpenMetadataAPIMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_GUID,
                                    OpenMetadataAPIMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return discovery report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public B getDiscoveryAnalysisReport(String   userId,
                                        String   discoveryReportGUID,
                                        boolean  forLineage,
                                        boolean  forDuplicateProcessing,
                                        Date     effectiveTime,
                                        String   methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String   reportGUIDParameterName = "discoveryReportGUID";

        return getBeanFromRepository(userId,
                                     discoveryReportGUID,
                                     reportGUIDParameterName,
                                     OpenMetadataAPIMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME,
                                     forLineage,
                                     forDuplicateProcessing,
                                     supportedZones,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Return the discovery analysis reports about the asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maximumResults maximum number of elements to return on this call
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of discovery analysis reports
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public List<B>   getDiscoveryAnalysisReports(String  userId,
                                                 String  assetGUID,
                                                 int     startingFrom,
                                                 int     maximumResults,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String assetGUIDParameter = "assetGUID";

        return this.getAttachedElements(userId,
                                        assetGUID,
                                        assetGUIDParameter,
                                        OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                        OpenMetadataAPIMapper.REPORT_TO_ASSET_TYPE_GUID,
                                        OpenMetadataAPIMapper.REPORT_TO_ASSET_TYPE_NAME,
                                        OpenMetadataAPIMapper.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME,
                                        null,
                                        null,
                                        0,
                                        forLineage,
                                        forDuplicateProcessing,
                                        startingFrom,
                                        maximumResults,
                                        effectiveTime,
                                        methodName);
    }
}
