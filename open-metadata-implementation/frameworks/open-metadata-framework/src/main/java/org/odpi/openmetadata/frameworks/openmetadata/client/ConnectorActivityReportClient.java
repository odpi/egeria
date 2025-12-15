/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.client;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.converters.ConnectorActivityReportConverter;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.reports.ConnectorActivityReport;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ConnectorActivityReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;


/**
 * ConnectorActivityReportClient defines the interface of the client that implements the maintenance of connector activity reports.
 */
public class ConnectorActivityReportClient
{
    private final OpenMetadataClient openMetadataClient;
    private final AuditLog           auditLog;

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final String serviceName;

    private final ConnectorActivityReportConverter<ConnectorActivityReport> converter;



    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public ConnectorActivityReportClient(String             localServerName,
                                         AuditLog           auditLog,
                                         String             serviceName,
                                         OpenMetadataClient openMetadataClient)
    {
        this.openMetadataClient = openMetadataClient;
        this.auditLog           = auditLog;
        this.serviceName        = serviceName;

        this.converter = new ConnectorActivityReportConverter<>(propertyHelper, serviceName, localServerName);
    }


    /**
     * Create a new connector activity report for an element (identified by anchorGUID).
     *
     * @param userId calling user
     * @param elementGUID element to attach the connector activity report to
     * @param properties properties of the report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem with the metadata server.
     */
    public void publishConnectorActivityReport(String                            userId,
                                               String                            elementGUID,
                                               ConnectorActivityReportProperties properties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName              = "publishConnectorActivityReport";
        final String guidParameterName       = "elementGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setAnchorGUID(elementGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorScopeGUID(null);
        newElementOptions.setParentGUID(elementGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.REPORT_SUBJECT.typeName);
        newElementOptions.setForLineage(true);
        newElementOptions.setForDuplicateProcessing(false);

        openMetadataClient.createMetadataElementInStore(userId,
                                                        OpenMetadataType.CONNECTOR_ACTIVITY_REPORT.typeName,
                                                        newElementOptions,
                                                        null,
                                                        this.getElementProperties(properties),
                                                        null);
    }


    /**
     * Retrieve a specific connector activity report by unique identifier.
     *
     * @param userId calling user
     * @param reportGUID unique identifier of the connector activity report
     *
     * @return report or null
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem with the metadata server.
     */
    public ConnectorActivityReport getConnectorActivityReport(String userId,
                                                              String reportGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getConnectorActivityReport";
        final String guidParameterName = "reportGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(reportGUID, guidParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByGUID(userId,
                                                                                              reportGUID,
                                                                                              new GetOptions());

        if ((openMetadataElement != null) && (propertyHelper.isTypeOf(openMetadataElement, OpenMetadataType.CONNECTOR_ACTIVITY_REPORT.typeName)))
        {
            return convertConnectorActivityReport(openMetadataElement, methodName);
        }

        return null;
    }


    /**
     * Retrieve the connector activity reports attached to an element.  The list can be filtered by start and completion date of the report along with the
     * paging options if there are many connector activity reports.
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
    public List<ConnectorActivityReport> getConnectorActivityReportsForElement(String  userId,
                                                                               String  elementGUID,
                                                                               int     startFrom,
                                                                               int     pageSize) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName              = "getConnectorActivityReportsForElement";
        final String guidParameterName       = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setStartFrom(startFrom);
        queryOptions.setPageSize(pageSize);

        RelatedMetadataElementList relatedMetadataElementList = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                              elementGUID,
                                                                                                              1,
                                                                                                              OpenMetadataType.REPORT_SUBJECT.typeName,
                                                                                                              queryOptions);

        return convertConnectorActivityReports(relatedMetadataElementList, methodName);
    }


    /**
     * Retrieve the published connector activity reports.  The list can be filtered by start and completion date of the report along with the
     * paging options if there are many connector activity reports.
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
    public List<ConnectorActivityReport> getConnectorActivityReports(String  userId,
                                                                     int     startFrom,
                                                                     int     pageSize) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "getConnectorActivityReports";

        propertyHelper.validateUserId(userId, methodName);

        SearchOptions searchOptions = new SearchOptions();

        searchOptions.setStartFrom(startFrom);
        searchOptions.setPageSize(pageSize);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId, null, searchOptions);

        return convertConnectorActivityReports(openMetadataElements, methodName);
    }


    /**
     * Convert the open metadata elements retrieve into connector activity report elements.
     *
     * @param openMetadataElements   elements extracted from the repository
     * @param methodName             calling method
     * @return list of actor profiles (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    private List<ConnectorActivityReport> convertConnectorActivityReports(List<OpenMetadataElement> openMetadataElements,
                                                                          String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<ConnectorActivityReport> connectorActivityReports = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    ConnectorActivityReport connectorActivityReport = convertConnectorActivityReport(openMetadataElement, methodName);
                    if (connectorActivityReport != null)
                    {
                        connectorActivityReports.add(connectorActivityReport);
                    }
                }
            }

            return connectorActivityReports;
        }

        return null;
    }



    /**
     * Convert the open metadata elements retrieve into connector activity report elements.
     *
     * @param relatedMetadataElementsList   elements extracted from the repository
     * @param methodName             calling method
     * @return list of actor profiles (or null)
     * @throws PropertyServerException problem with the conversion process
     */
    private List<ConnectorActivityReport> convertConnectorActivityReports(RelatedMetadataElementList relatedMetadataElementsList,
                                                                          String                     methodName) throws PropertyServerException
    {
        if (relatedMetadataElementsList != null)
        {
            if (relatedMetadataElementsList.getElementList() != null)
            {
                List<ConnectorActivityReport> connectorActivityReports = new ArrayList<>();

                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementsList.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        ConnectorActivityReport connectorActivityReport = convertConnectorActivityReport(relatedMetadataElement.getElement(), methodName);
                        if (connectorActivityReport != null)
                        {
                            connectorActivityReports.add(connectorActivityReport);
                        }
                    }
                }

                return connectorActivityReports;
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
    private ConnectorActivityReport convertConnectorActivityReport(OpenMetadataElement openMetadataElement,
                                                                   String              methodName) throws PropertyServerException
    {
        try
        {
            return converter.getNewBean(ConnectorActivityReport.class, openMetadataElement, methodName);
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
     * Convert the connector activity report properties into element properties.
     *
     * @param connectorActivityReportProperties supplied properties
     * @return element properties
     */
    private NewElementProperties getElementProperties(ConnectorActivityReportProperties connectorActivityReportProperties)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               connectorActivityReportProperties.getQualifiedName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.SERVER_NAME.name,
                                                             connectorActivityReportProperties.getServerName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CONNECTOR_ID.name,
                                                             connectorActivityReportProperties.getConnectorId());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CONNECTOR_NAME.name,
                                                             connectorActivityReportProperties.getConnectorName());

        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                           OpenMetadataProperty.START_TIME.name,
                                                           connectorActivityReportProperties.getStartTime());

        elementProperties = propertyHelper.addDateProperty(elementProperties,
                                                           OpenMetadataProperty.COMPLETION_TIME.name,
                                                           connectorActivityReportProperties.getCompletionTime());

        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                  OpenMetadataProperty.CREATED_ELEMENTS.name,
                                                                  connectorActivityReportProperties.getCreatedElements());

        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                  OpenMetadataProperty.UPDATED_ELEMENTS.name,
                                                                  connectorActivityReportProperties.getUpdatedElements());

        elementProperties = propertyHelper.addStringArrayProperty(elementProperties,
                                                                  OpenMetadataProperty.DELETED_ELEMENTS.name,
                                                                  connectorActivityReportProperties.getDeletedElements());

        elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                connectorActivityReportProperties.getAdditionalProperties());

        return new NewElementProperties(elementProperties);
    }
}
