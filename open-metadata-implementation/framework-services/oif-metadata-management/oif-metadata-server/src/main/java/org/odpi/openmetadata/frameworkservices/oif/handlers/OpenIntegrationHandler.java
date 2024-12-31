/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationReport;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.oif.builder.IntegrationReportBuilder;
import org.odpi.openmetadata.frameworkservices.oif.converters.IntegrationReportConverter;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * OpenIntegrationHandler provides the open metadata server side implementation of
 * OpenIntegrationServer which is part of the Open Integration Framework (OIF).
 */
public class OpenIntegrationHandler
{
    private final ReferenceableHandler<IntegrationReport>  integrationReportHandler;
    private final InvalidParameterHandler                  invalidParameterHandler;


    /**
     * Construct the governance engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
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
     * @param auditLog logging destination
     */
    public OpenIntegrationHandler(String                             serviceName,
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
        this.invalidParameterHandler = invalidParameterHandler;


        this.integrationReportHandler = new ReferenceableHandler<>(new IntegrationReportConverter<>(repositoryHelper, serviceName, serverName),
                                                                   IntegrationReport.class,
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
     * Create a new integration report for an element (identified by elementGUID).
     *
     * @param userId calling user
     * @param elementGUID element to attach the integration report to
         * @param properties properties of the report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem with the metadata server.
     */
    public void publishIntegrationReport(String                      userId,
                                         String                      elementGUID,
                                         List<String>                serviceSupportedZones,
                                         IntegrationReportProperties properties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName              = "publishIntegrationReport";
        final String guidParameterName       = "elementGUID";
        final String reportGUIDParameterName = "reportGUID";
        final String propertiesParameterName = "properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        EntityDetail entity = integrationReportHandler.getEntityFromRepository(userId,
                                                                               elementGUID,
                                                                               null,
                                                                               OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                               null,
                                                                               null,
                                                                               false,
                                                                               false,
                                                                               serviceSupportedZones,
                                                                               new Date(),
                                                                               methodName);

        if (entity != null)
        {
            IntegrationReportBuilder builder = new IntegrationReportBuilder(properties.getServerName(),
                                                                            properties.getConnectorId(),
                                                                            properties.getConnectorName(),
                                                                            properties.getRefreshStartDate(),
                                                                            properties.getRefreshCompletionDate(),
                                                                            properties.getCreatedElements(),
                                                                            properties.getUpdatedElements(),
                                                                            properties.getDeletedElements(),
                                                                            properties.getAdditionalProperties(),
                                                                            OpenMetadataType.INTEGRATION_REPORT_TYPE_GUID,
                                                                            OpenMetadataType.INTEGRATION_REPORT_TYPE_NAME,
                                                                            integrationReportHandler.getRepositoryHelper(),
                                                                            integrationReportHandler.getServiceName(),
                                                                            integrationReportHandler.getServerName());


            builder.setAnchors(userId,
                               entity.getGUID(),
                               entity.getType().getTypeDefName(),
                               integrationReportHandler.getDomainName(entity),
                               methodName);

            String reportGUID = integrationReportHandler.createBeanInRepository(userId,
                                                                                null,
                                                                                null,
                                                                                OpenMetadataType.INTEGRATION_REPORT_TYPE_GUID,
                                                                                OpenMetadataType.INTEGRATION_REPORT_TYPE_NAME,
                                                                                builder,
                                                                                new Date(),
                                                                                methodName);

            if (reportGUID != null)
            {
                integrationReportHandler.linkElementToElement(userId,
                                                              null,
                                                              null,
                                                              entity.getGUID(),
                                                              guidParameterName,
                                                              OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                              reportGUID,
                                                              reportGUIDParameterName,
                                                              OpenMetadataType.INTEGRATION_REPORT_TYPE_NAME,
                                                              true,
                                                              true,
                                                              integrationReportHandler.getSupportedZones(),
                                                              OpenMetadataType.RELATED_INTEGRATION_REPORT_TYPE_GUID,
                                                              OpenMetadataType.RELATED_INTEGRATION_REPORT_TYPE_NAME,
                                                              null,
                                                              null,
                                                              null,
                                                              null,
                                                              methodName);
            }
        }
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
    public IntegrationReport getIntegrationReport(String userId,
                                                  String reportGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName        = "getIntegrationReport";
        final String guidParameterName = "reportGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(reportGUID, guidParameterName, methodName);

        return integrationReportHandler.getBeanFromRepository(userId,
                                                              reportGUID,
                                                              guidParameterName,
                                                              OpenMetadataType.INTEGRATION_REPORT_TYPE_NAME,
                                                              true,
                                                              true,
                                                              integrationReportHandler.getSupportedZones(),
                                                              null,
                                                              methodName);
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

        int pageSize = invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        List<IntegrationReport> integrationReports = integrationReportHandler.getAttachedElements(userId,
                                                                                                  null,
                                                                                                  null,
                                                                                                  elementGUID,
                                                                                                  guidParameterName,
                                                                                                  OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                                                  OpenMetadataType.RELATED_INTEGRATION_REPORT_TYPE_GUID,
                                                                                                  OpenMetadataType.RELATED_INTEGRATION_REPORT_TYPE_NAME,
                                                                                                  OpenMetadataType.INTEGRATION_REPORT_TYPE_NAME,
                                                                                                  null,
                                                                                                  null,
                                                                                                  2,
                                                                                                  null,
                                                                                                  null,
                                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                                  null,
                                                                                                  true,
                                                                                                  true,
                                                                                                  integrationReportHandler.getSupportedZones(),
                                                                                                  startingFrom,
                                                                                                  0,
                                                                                                  null,
                                                                                                  methodName);

        return this.filterIntegrationReports(integrationReports,
                                             afterCompletionDate,
                                             beforeStartDate,
                                             startingFrom,
                                             pageSize);
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
        int pageSize = invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        List<IntegrationReport> integrationReports = integrationReportHandler.getBeansByType(userId,
                                                                                             OpenMetadataType.INTEGRATION_REPORT_TYPE_GUID,
                                                                                             OpenMetadataType.INTEGRATION_REPORT_TYPE_NAME,
                                                                                             null,
                                                                                             null,
                                                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                                                             null,
                                                                                             false,
                                                                                             false,
                                                                                             integrationReportHandler.getSupportedZones(),
                                                                                             startingFrom,
                                                                                             pageSize,
                                                                                             null,
                                                                                             methodName);

        return this.filterIntegrationReports(integrationReports,
                                             afterCompletionDate,
                                             beforeStartDate,
                                             startingFrom,
                                             pageSize);
    }


    /**
     * Filter out the integration reports that are not within the requested dates.
     *
     * @param retrievedIntegrationReports retrieved reports
     * @param afterCompletionDate restrict reports to those that completed after the requested time (null for any completion time).
     * @param beforeStartDate restrict reports to those that started before the requested time (null for any start time).
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return filtered list of reports
     */
    private List<IntegrationReport> filterIntegrationReports(List<IntegrationReport> retrievedIntegrationReports,
                                                             Date                    afterCompletionDate,
                                                             Date                    beforeStartDate,
                                                             int                     startingFrom,
                                                             int                     maximumResults)
    {
        if ((afterCompletionDate == null) && (beforeStartDate == null))
        {
            return retrievedIntegrationReports;
        }

        List<IntegrationReport> results = new ArrayList<>();

        for (IntegrationReport report : retrievedIntegrationReports)
        {
            // todo add filtering
            results.add(report);
        }

        return results;
    }
}
