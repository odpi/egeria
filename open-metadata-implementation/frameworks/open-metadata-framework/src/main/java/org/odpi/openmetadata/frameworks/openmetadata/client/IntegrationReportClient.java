/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.client;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.converters.IntegrationReportConverter;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.reports.IntegrationReport;
import org.odpi.openmetadata.frameworks.openmetadata.reports.IntegrationReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * IntegrationReportClient defines the interface of the client that implements the maintenance of integration reports.
 */
public class IntegrationReportClient
{
    private final OpenMetadataClient openMetadataClient;
    private final AuditLog           auditLog;

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final String serviceName;

    private final IntegrationReportConverter<IntegrationReport> converter;



    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public IntegrationReportClient(String             localServerName,
                                   AuditLog           auditLog,
                                   String             serviceName,
                                   OpenMetadataClient openMetadataClient)
    {
        this.openMetadataClient = openMetadataClient;
        this.auditLog           = auditLog;
        this.serviceName        = serviceName;

        this.converter = new IntegrationReportConverter<>(propertyHelper, serviceName, localServerName);
    }


    /**
     * Create a new integration report for an element (identified by anchorGUID).
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
                                         IntegrationReportProperties properties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName              = "publishIntegrationReport";
        final String guidParameterName       = "elementGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
        newElementOptions.setAnchorGUID(elementGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorScopeGUID(null);
        newElementOptions.setParentGUID(elementGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.RELATED_INTEGRATION_REPORT.typeName);
        newElementOptions.setEffectiveTime(new Date());
        newElementOptions.setForLineage(true);
        newElementOptions.setForDuplicateProcessing(false);

        openMetadataClient.createMetadataElementInStore(userId,
                                                        OpenMetadataType.INTEGRATION_REPORT.typeName,
                                                        newElementOptions,
                                                        null,
                                                        this.getElementProperties(properties),
                                                        null);
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
        final String methodName = "getIntegrationReport";
        final String guidParameterName = "reportGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(reportGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByGUID(userId,
                                                                                              reportGUID,
                                                                                              new GetOptions());

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.INTEGRATION_REPORT.typeName)))
        {
            return convertIntegrationReport(openMetadataElement, methodName);
        }

        return null;
    }


    /**
     * Retrieve the integration reports attached to an element.  The list can be filtered by start and completion date of the report along with the
     * paging options if there are many integration reports.
     *
     * @param userId calling user
     * @param elementGUID calling user
     * @param startFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     *
     * @return list of qualifying reports
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem with the metadata server.
     */
    public List<IntegrationReport> getIntegrationReportsForElement(String  userId,
                                                                   String  elementGUID,
                                                                   int     startFrom,
                                                                   int     pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName              = "getIntegrationReportsForElement";
        final String guidParameterName       = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setStartFrom(startFrom);
        queryOptions.setPageSize(pageSize);

        RelatedMetadataElementList relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                              elementGUID,
                                                                                                              1,
                                                                                                               OpenMetadataType.RELATED_INTEGRATION_REPORT.typeName,
                                                                                                               queryOptions);

        return convertIntegrationReports(relatedMetadataElementList, methodName);
    }


    /**
     * Retrieve the published integration reports.  The list can be filtered by start and completion date of the report along with the
     * paging options if there are many integration reports.
     *
     * @param userId calling user
     * @param startFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     *
     * @return list of qualifying reports
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem with the metadata server.
     */
    public List<IntegrationReport> getIntegrationReports(String  userId,
                                                         int     startFrom,
                                                         int     pageSize) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getIntegrationReports";

        propertyHelper.validateUserId(userId, methodName);

        SearchOptions searchOptions = new SearchOptions();

        searchOptions.setStartFrom(startFrom);
        searchOptions.setPageSize(pageSize);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId, null, searchOptions);

        return convertIntegrationReports(openMetadataElements, methodName);
    }


    /**
     * Convert the open metadata elements retrieve into integration report elements.
     *
     * @param openMetadataElements   elements extracted from the repository
     * @param methodName             calling method
     * @return list of actor profiles (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    private List<IntegrationReport> convertIntegrationReports(List<OpenMetadataElement> openMetadataElements,
                                                              String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<IntegrationReport> integrationReports = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    IntegrationReport integrationReport = convertIntegrationReport(openMetadataElement, methodName);
                    if (integrationReport != null)
                    {
                        integrationReports.add(integrationReport);
                    }
                }
            }

            return integrationReports;
        }

        return null;
    }



    /**
     * Convert the open metadata elements retrieve into integration report elements.
     *
     * @param relatedMetadataElementsList   elements extracted from the repository
     * @param methodName             calling method
     * @return list of actor profiles (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    private List<IntegrationReport> convertIntegrationReports(RelatedMetadataElementList relatedMetadataElementsList,
                                                              String                     methodName) throws PropertyServerException
    {
        if (relatedMetadataElementsList != null)
        {
            if (relatedMetadataElementsList.getElementList() != null)
            {
                List<IntegrationReport> integrationReports = new ArrayList<>();

                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementsList.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        IntegrationReport integrationReport = convertIntegrationReport(relatedMetadataElement.getElement(), methodName);
                        if (integrationReport != null)
                        {
                            integrationReports.add(integrationReport);
                        }
                    }
                }

                return integrationReports;
            }
        }

        return null;
    }



    /**
     * Return the actor profile extracted from the open metadata element plus linked solution components.
     * Only convert the roles that either inherit from SolutionActorProfile or are linked to solution components.
     *
     * @param openMetadataElement    element extracted from the repository
     * @param methodName             calling method
     * @return bean or null
     * @throws PropertyServerException problem with the conversion process
     */
    private IntegrationReport convertIntegrationReport(OpenMetadataElement openMetadataElement,
                                                       String              methodName) throws PropertyServerException
    {
        try
        {
            return converter.getNewBean(IntegrationReport.class, openMetadataElement, methodName);
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OMFAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                       methodName,
                                                                                                       serviceName,
                                                                                                       error.getMessage()),
                                      error);
            }

            throw new PropertyServerException(OMFErrorCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               serviceName,
                                                                                                               error.getMessage()),
                                              error.getClass().getName(),
                                              methodName,
                                              error);
        }
    }


    /**
     * Convert the integration report properties into element properties.
     *
     * @param integrationReportProperties supplied properties
     * @return element properties
     */
    private NewElementProperties getElementProperties(IntegrationReportProperties integrationReportProperties)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.SERVER_NAME.name,
                                                                               integrationReportProperties.getServerName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CONNECTOR_ID.name,
                                                             integrationReportProperties.getConnectorId());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CONNECTOR_NAME.name,
                                                             integrationReportProperties.getConnectorName());

        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                           OpenMetadataProperty.CONNECTOR_START_DATE.name,
                                                           integrationReportProperties.getConnectorStartDate());

        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                           OpenMetadataProperty.REFRESH_START_DATE.name,
                                                           integrationReportProperties.getRefreshStartDate());

        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                           OpenMetadataProperty.REFRESH_COMPLETION_DATE.name,
                                                           integrationReportProperties.getRefreshStartDate());

        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                           OpenMetadataProperty.CONNECTOR_DISCONNECT_DATE.name,
                                                           integrationReportProperties.getConnectorDisconnectDate());

        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                  OpenMetadataProperty.CREATED_ELEMENTS.name,
                                                                  integrationReportProperties.getCreatedElements());

        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                  OpenMetadataProperty.UPDATED_ELEMENTS.name,
                                                                  integrationReportProperties.getUpdatedElements());

        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                  OpenMetadataProperty.DELETED_ELEMENTS.name,
                                                                  integrationReportProperties.getDeletedElements());

        elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                integrationReportProperties.getAdditionalProperties());

        return new NewElementProperties(elementProperties);
    }
}
