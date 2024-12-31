/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SurveyReportHandler manages the storage and retrieval of metadata relating to survey
 * reports as defined in the Survey Analysis Framework (SAF).
 */
public class SurveyReportHandler<B> extends OpenMetadataAPIGenericHandler<B>
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
    public SurveyReportHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create a new survey analysis report and chain it to its asset, survey engine and survey service.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the report
     * @param displayName short name for the report
     * @param description description of the report
     * @param creationDate data of the report
     * @param analysisParameters analysis parameters passed to the survey service
     * @param assetGUID unique identifier of the asset being analysed
     * @param governanceEngineGUID unique identifier of the survey engine that is running the survey service
     * @param governanceServiceGUID unique identifier of the survey service creating the report
     * @param additionalProperties additional properties for the report
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return The new survey report.
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or report
     * @throws PropertyServerException there was a problem in the store whether the asset/report properties are kept.
     */
    public String createSurveyReport(String                 userId,
                                     String                 qualifiedName,
                                     String                 displayName,
                                     String                 description,
                                     Date                   creationDate,
                                     Map<String, String>    analysisParameters,
                                     String                 assetGUID,
                                     String                 governanceEngineGUID,
                                     String                 governanceServiceGUID,
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
        final String   governanceEngineGUIDParameterName = "governanceEngineGUID";
        final String   governanceServiceGUIDParameterName = "governanceServiceGUID";

        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameterName, methodName);


        repositoryHandler.getEntityByGUID(userId,
                                          assetGUID,
                                          assetGUIDParameterName,
                                          OpenMetadataType.ASSET.typeName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);

        repositoryHandler.getEntityByGUID(userId,
                                          governanceEngineGUIDParameterName,
                                          assetGUID,
                                          OpenMetadataType.OPEN_DISCOVERY_ENGINE.typeName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);

        SurveyReportBuilder builder = new SurveyReportBuilder(qualifiedName,
                                                              displayName,
                                                              description,
                                                              creationDate,
                                                              analysisParameters,
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
                                                            OpenMetadataType.DISCOVERY_ANALYSIS_REPORT_TYPE_GUID,
                                                            OpenMetadataType.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME,
                                                            null,
                                                            null,
                                                            builder.getInstanceProperties(methodName),
                                                            null,
                                                            InstanceStatus.ACTIVE,
                                                            methodName);

        if (reportGUID != null)
        {
            repositoryHandler.createRelationship(userId,
                                                 OpenMetadataType.REPORT_TO_ASSET_TYPE_GUID,
                                                 null,
                                                 null,
                                                 assetGUID,
                                                 reportGUID,
                                                 null,
                                                 methodName);

            repositoryHandler.createRelationship(userId,
                                                 OpenMetadataType.REPORT_TO_ENGINE_TYPE_GUID,
                                                 null,
                                                 null,
                                                 governanceEngineGUID,
                                                 reportGUID,
                                                 null,
                                                 methodName);

            repositoryHandler.createRelationship(userId,
                                                 OpenMetadataType.REPORT_TO_SERVICE_TYPE_GUID,
                                                 null,
                                                 null,
                                                 governanceServiceGUID,
                                                 reportGUID,
                                                 null,
                                                 methodName);
        }

        return reportGUID;
    }


    /**
     * Update the properties of the survey analysis report.
     *
     * @param userId calling user.
     * @param surveyReportGUID identifier of the survey report
     * @param qualifiedName unique name for the report
     * @param displayName short name for the report
     * @param description description of the report
     * @param creationDate data of the report
     * @param analysisParameters analysis parameters passed to the survey service
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
    public void  updateSurveyReport(String                 userId,
                                    String                 surveyReportGUID,
                                    String                 qualifiedName,
                                    String                 displayName,
                                    String                 description,
                                    Date                   creationDate,
                                    Map<String, String>    analysisParameters,
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
        final String   surveyReportGUIDParameterName = "surveyReportGUID";


        SurveyReportBuilder builder = new SurveyReportBuilder(qualifiedName,
                                                              displayName,
                                                              description,
                                                              creationDate,
                                                              analysisParameters,
                                                              additionalProperties,
                                                              null,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    null,
                                    null,
                                    surveyReportGUID,
                                    surveyReportGUIDParameterName,
                                    OpenMetadataType.DISCOVERY_ANALYSIS_REPORT_TYPE_GUID,
                                    OpenMetadataType.DISCOVERY_ANALYSIS_REPORT_TYPE_NAME,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Request the survey report for a survey request that has completed.
     *
     * @param userId identifier of calling user
     * @param surveyReportGUID identifier of the survey request.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return survey report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public B getSurveyReport(String   userId,
                             String   surveyReportGUID,
                             boolean  forLineage,
                             boolean  forDuplicateProcessing,
                             Date     effectiveTime,
                             String   methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String   reportGUIDParameterName = "surveyReportGUID";

        return getBeanFromRepository(userId,
                                     surveyReportGUID,
                                     reportGUIDParameterName,
                                     OpenMetadataType.SURVEY_REPORT.typeName,
                                     forLineage,
                                     forDuplicateProcessing,
                                     supportedZones,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Return the survey reports about the asset.
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
     * @return list of survey analysis reports
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public List<B> getSurveyReports(String  userId,
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
                                        OpenMetadataType.ASSET.typeName,
                                        OpenMetadataType.REPORT_TO_ASSET_TYPE_GUID,
                                        OpenMetadataType.REPORT_TO_ASSET_TYPE_NAME,
                                        OpenMetadataType.SURVEY_REPORT.typeName,
                                        null,
                                        null,
                                        0,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        startingFrom,
                                        maximumResults,
                                        effectiveTime,
                                        methodName);
    }
}
