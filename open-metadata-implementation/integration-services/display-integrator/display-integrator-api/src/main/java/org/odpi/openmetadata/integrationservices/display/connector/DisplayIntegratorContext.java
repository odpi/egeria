/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.display.connector;

import org.odpi.openmetadata.accessservices.datamanager.api.DataManagerEventListener;
import org.odpi.openmetadata.accessservices.datamanager.client.DisplayApplicationClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;

import java.util.List;


/**
 * DisplayIntegratorContext is the context for cataloging topics from a display application server.
 */
public class DisplayIntegratorContext extends IntegrationContext
{
    private final DisplayApplicationClient client;
    private final DataManagerEventClient   eventClient;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param serverName name of the integration daemon
     * @param openIntegrationClient client for calling the metadata server
     * @param openMetadataStoreClient client for calling the metadata server
     * @param displayApplicationClient client to map request to
     * @param eventClient client to register for events
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization the direction of integration permitted by the integration connector
     * @param integrationConnectorGUID unique identifier for the integration connector if it is started via an integration group (otherwise it is
     *                                 null).
     * @param externalSourceGUID unique identifier of the software server capability for the asset manager
     * @param externalSourceName unique name of the software server capability for the asset manager
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public DisplayIntegratorContext(String                       connectorId,
                                    String                       connectorName,
                                    String                       connectorUserId,
                                    String                       serverName,
                                    OpenIntegrationClient        openIntegrationClient,
                                    OpenMetadataClient           openMetadataStoreClient,
                                    DisplayApplicationClient     displayApplicationClient,
                                    DataManagerEventClient       eventClient,
                                    boolean                      generateIntegrationReport,
                                    PermittedSynchronization     permittedSynchronization,
                                    String                       integrationConnectorGUID,
                                    String                       externalSourceGUID,
                                    String                       externalSourceName,
                                    AuditLog                     auditLog,
                                    int                          maxPageSize)
    {
        super(connectorId,
              connectorName,
              connectorUserId,
              serverName,
              openIntegrationClient,
              openMetadataStoreClient,
              generateIntegrationReport,
              permittedSynchronization,
              externalSourceGUID,
              externalSourceName,
              integrationConnectorGUID,
              auditLog,
              maxPageSize);

        this.client          = displayApplicationClient;
        this.eventClient     = eventClient;
    }


    /* ========================================================
     * Register for inbound events from the Data Manager OMAS OutTopic
     */

    /**
     * Register a listener object that will be passed each of the events published by the Data Manager OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(DataManagerEventListener listener) throws InvalidParameterException,
                                                                           ConnectionCheckedException,
                                                                           ConnectorCheckedException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }


    /* ========================================================
     * The forms, reports and queries are top level assets on an application or reporting engine.
     */


    /**
     * Create a new metadata element to represent a form.
     *
     * @param formProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createForm(FormProperties formProperties) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        String formGUID = client.createForm(userId, externalSourceGUID, externalSourceName, externalSourceIsHome, formProperties);

        if ((formGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(formGUID, formGUID);
            integrationReportWriter.reportElementCreation(formGUID);
        }

        return formGUID;
    }


    /**
     * Create a new metadata element to represent a form using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createFormFromTemplate(String             templateGUID,
                                         TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        String formGUID = client.createFormFromTemplate(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        externalSourceIsHome,
                                                        templateGUID,
                                                        templateProperties);

        if ((formGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(formGUID, formGUID);
            integrationReportWriter.reportElementCreation(formGUID);
        }

        return formGUID;
    }


    /**
     * Update the metadata element representing a form.
     *
     * @param formGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param formProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateForm(String          formGUID,
                           boolean         isMergeUpdate,
                           FormProperties formProperties) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        client.updateForm(userId, externalSourceGUID, externalSourceName, formGUID, isMergeUpdate, formProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(formGUID, formGUID);
            integrationReportWriter.reportElementUpdate(formGUID);
        }
    }


    /**
     * Update the zones for the form asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param formGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishForm(String formGUID) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException
    {
        client.publishForm(userId, formGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(formGUID, formGUID);
            integrationReportWriter.reportElementUpdate(formGUID);
        }
    }


    /**
     * Update the zones for the form asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the form is first created).
     *
     * @param formGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawForm(String formGUID) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException
    {
        client.withdrawForm(userId, formGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(formGUID, formGUID);
            integrationReportWriter.reportElementUpdate(formGUID);
        }
    }


    /**
     * Remove the metadata element representing a form.
     *
     * @param formGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeForm(String formGUID,
                           String qualifiedName) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        client.removeForm(userId, externalSourceGUID, externalSourceName, formGUID, qualifiedName);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(formGUID, formGUID);
            integrationReportWriter.reportElementDelete(formGUID);
        }
    }


    /**
     * Retrieve the list of form metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<FormElement> findForms(String searchString,
                                       int    startFrom,
                                       int    pageSize) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        return client.findForms(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of form metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<FormElement>   getFormsByName(String name,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return client.getFormsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of forms created by this caller.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<FormElement> getFormsForApplication(int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return client.getFormsForApplication(userId, externalSourceGUID, externalSourceName, startFrom, pageSize);
    }


    /**
     * Retrieve the form metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public FormElement getFormByGUID(String guid) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        return client.getFormByGUID(userId, guid);
    }
    

    /**
     * Create a new metadata element to represent a report.
     *
     * @param reportProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createReport(ReportProperties reportProperties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        String reportGUID = client.createReport(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                externalSourceIsHome,
                                                reportProperties);

        if ((reportGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(reportGUID, reportGUID);
            integrationReportWriter.reportElementCreation(reportGUID);
        }

        return reportGUID;
    }


    /**
     * Create a new metadata element to represent a report using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createReportFromTemplate(String             templateGUID,
                                           TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        String reportGUID = client.createReportFromTemplate(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            externalSourceIsHome,
                                                            templateGUID,
                                                            templateProperties);

        if ((reportGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(reportGUID, reportGUID);
            integrationReportWriter.reportElementCreation(reportGUID);
        }

        return reportGUID;
    }


    /**
     * Update the metadata element representing a report.
     *
     * @param reportGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param reportProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateReport(String           reportGUID,
                             boolean          isMergeUpdate,
                             ReportProperties reportProperties) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        client.updateReport(userId, externalSourceGUID, externalSourceName, reportGUID, isMergeUpdate, reportProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(reportGUID, reportGUID);
            integrationReportWriter.reportElementUpdate(reportGUID);
        }
    }


    /**
     * Update the zones for the report asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param reportGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishReport(String reportGUID) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        client.publishReport(userId, reportGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(reportGUID, reportGUID);
            integrationReportWriter.reportElementUpdate(reportGUID);
        }
    }


    /**
     * Update the zones for the report asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the report is first created).
     *
     * @param reportGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawReport(String reportGUID) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        client.withdrawReport(userId, reportGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(reportGUID, reportGUID);
            integrationReportWriter.reportElementUpdate(reportGUID);
        }
    }


    /**
     * Remove the metadata element representing a report.
     *
     * @param reportGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeReport(String reportGUID,
                             String qualifiedName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        client.removeReport(userId, externalSourceGUID, externalSourceName, reportGUID, qualifiedName);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(reportGUID, reportGUID);
            integrationReportWriter.reportElementDelete(reportGUID);
        }
    }


    /**
     * Retrieve the list of report metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ReportElement> findReports(String searchString,
                                           int    startFrom,
                                           int    pageSize) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        return client.findReports(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of report metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ReportElement>   getReportsByName(String name,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return client.getReportsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of reports created by this caller.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ReportElement> getReportsForApplication(int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return client.getReportsForApplication(userId, externalSourceGUID, externalSourceName, startFrom, pageSize);
    }


    /**
     * Retrieve the report metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ReportElement getReportByGUID(String guid) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        return client.getReportByGUID(userId, guid);
    }

    
    /**
     * Create a new metadata element to represent a query.
     *
      * @param queryProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createQuery(QueryProperties queryProperties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        String queryGUID = client.createQuery(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              externalSourceIsHome,
                                              queryProperties);

        if ((queryGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(queryGUID, queryGUID);
            integrationReportWriter.reportElementCreation(queryGUID);
        }

        return queryGUID;
    }


    /**
     * Create a new metadata element to represent a query using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createQueryFromTemplate(String             templateGUID,
                                          TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        String queryGUID = client.createQueryFromTemplate(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          externalSourceIsHome,
                                                          templateGUID,
                                                          templateProperties);

        if ((queryGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setAnchor(queryGUID, queryGUID);
            integrationReportWriter.reportElementCreation(queryGUID);
        }

        return queryGUID;
    }


    /**
     * Update the metadata element representing a query.
     *
     * @param queryGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param queryProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateQuery(String          queryGUID,
                            boolean         isMergeUpdate,
                            QueryProperties queryProperties) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        client.updateQuery(userId, externalSourceGUID, externalSourceName, queryGUID, isMergeUpdate, queryProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(queryGUID, queryGUID);
            integrationReportWriter.reportElementUpdate(queryGUID);
        }
    }


    /**
     * Update the zones for the query asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param queryGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishQuery(String queryGUID) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException
    {
        client.publishQuery(userId, queryGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(queryGUID, queryGUID);
            integrationReportWriter.reportElementUpdate(queryGUID);
        }
    }


    /**
     * Update the zones for the query asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the query is first created).
     *
     * @param queryGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawQuery(String queryGUID) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        client.withdrawQuery(userId, queryGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(queryGUID, queryGUID);
            integrationReportWriter.reportElementUpdate(queryGUID);
        }
    }


    /**
     * Remove the metadata element representing a query.
     *
     * @param queryGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeQuery(String queryGUID,
                            String qualifiedName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        client.removeQuery(userId, externalSourceGUID, externalSourceName, queryGUID, qualifiedName);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(queryGUID, queryGUID);
            integrationReportWriter.reportElementDelete(queryGUID);
        }
    }


    /**
     * Retrieve the list of query metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<QueryElement> findQueries(String searchString,
                                          int    startFrom,
                                          int    pageSize) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return client.findQueries(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of query metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<QueryElement>   getQueriesByName(String name,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return client.getQueriesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of queries created by this caller.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<QueryElement> getQueriesForApplication(int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return client.getQueriesForApplication(userId, externalSourceGUID, externalSourceName, startFrom, pageSize);
    }


    /**
     * Retrieve the query metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public QueryElement getQueryByGUID(String guid) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        return client.getQueryByGUID(userId, guid);
    }



    /*
     * Application assets have nested data fields grouped into related data containers
     */


    /**
     * Create a new metadata element to represent a data container.
     *
     * @param parentElementGUID element to link the data container to
     * @param dataContainerProperties properties about the data container to store
     *
     * @return unique identifier of the new data container
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataContainer(String                  parentElementGUID,
                                      DataContainerProperties dataContainerProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        String dataContainerGUID = client.createDataContainer(userId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              externalSourceIsHome,
                                                              parentElementGUID,
                                                              dataContainerProperties);

        if ((dataContainerGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setParent(dataContainerGUID, parentElementGUID);
            integrationReportWriter.reportElementCreation(dataContainerGUID);
        }

        return dataContainerGUID;
    }


    /**
     * Create a new metadata element to represent a data container using an existing metadata element as a template.
     *
     * @param parentElementGUID element to link the data container to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new data container
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataContainerFromTemplate(String             parentElementGUID,
                                                  String             templateGUID,
                                                  TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        String dataContainerGUID = client.createDataContainerFromTemplate(userId,
                                                                          externalSourceGUID,
                                                                          externalSourceName,
                                                                          externalSourceIsHome,
                                                                          parentElementGUID,
                                                                          templateGUID,
                                                                          templateProperties);

        if ((dataContainerGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setParent(dataContainerGUID, parentElementGUID);
            integrationReportWriter.reportElementCreation(dataContainerGUID);
        }

        return dataContainerGUID;
    }


    /**
     * Update the metadata element representing a data container.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param dataContainerGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param dataContainerProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDataContainer(String                  dataContainerGUID,
                                    boolean                 isMergeUpdate,
                                    DataContainerProperties dataContainerProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        client.updateDataContainer(userId, externalSourceGUID, externalSourceName, dataContainerGUID, isMergeUpdate, dataContainerProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(dataContainerGUID);
        }
    }




    /**
     * Remove the metadata element representing a data container.
     *
     * @param dataContainerGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDataContainer(String dataContainerGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        client.removeDataContainer(userId, externalSourceGUID, externalSourceName, dataContainerGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(dataContainerGUID);
        }
    }


    /**
     * Retrieve the list of data container metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param typeName optional type name for the data container - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataContainerElement> findDataContainers(String typeName,
                                                         String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return client.findDataContainers(userId, typeName, searchString, startFrom, pageSize);
    }


    /**
     * Return the data container associated with a specific open metadata element (data asset, process or port).
     *
     * @param parentElementGUID unique identifier of the open metadata element that this data container is connected to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return metadata element describing the data container associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataContainerElement> getDataContainersForElement(String parentElementGUID,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return client.getDataContainersForElement(userId, parentElementGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of data container metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param typeName optional type name for the data container - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataContainerElement>   getDataContainerByName(String name,
                                                               String typeName,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return client.getDataContainerByName(userId, name, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the data container metadata element with the supplied unique identifier.
     *
     * @param dataContainerGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataContainerElement getDataContainerByGUID(String dataContainerGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return client.getDataContainerByGUID(userId, dataContainerGUID);
    }


    /**
     * Retrieve the header of the metadata element connected to a data container.
     *
     * @param dataContainerGUID unique identifier of the requested metadata element
     *
     * @return header for parent element (data asset, process, port) plus qualified name
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementStub getDataContainerParent(String dataContainerGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return client.getDataContainerParent(userId, dataContainerGUID);
    }
    

    /**
     * Create a new metadata element to represent a data field.
     *
     * @param parentElementGUID unique identifier of the element where the data field is located
     * @param properties properties about the data field
     *
     * @return unique identifier of the new data field
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataField(String              parentElementGUID,
                                  DataFieldProperties properties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        String dataFieldGUID = client.createDataField(userId,
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      externalSourceIsHome,
                                                      parentElementGUID,
                                                      properties);

        if ((dataFieldGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setParent(dataFieldGUID, parentElementGUID);
            integrationReportWriter.reportElementCreation(dataFieldGUID);
        }

        return dataFieldGUID;
    }


    /**
     * Create a new metadata element to represent a data field using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param parentElementGUID unique identifier of the report where the data field is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new data field
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataFieldFromTemplate(String             templateGUID,
                                              String             parentElementGUID,
                                              TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        String dataFieldGUID = client.createDataFieldFromTemplate(userId,
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  externalSourceIsHome,
                                                                  templateGUID,
                                                                  parentElementGUID,
                                                                  templateProperties);

        if ((dataFieldGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.setParent(dataFieldGUID, parentElementGUID);
            integrationReportWriter.reportElementCreation(dataFieldGUID);
        }

        return dataFieldGUID;
    }


    /**
     * Connect a schema type to a data field.
     *
     * @param relationshipTypeName name of relationship to create
     * @param dataFieldGUID unique identifier of the data field
     * @param schemaTypeGUID unique identifier of the schema type to connect
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaType(String  relationshipTypeName,
                                String  dataFieldGUID,
                                String  schemaTypeGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        client.setupSchemaType(userId,
                               externalSourceGUID,
                               externalSourceName,
                               externalSourceIsHome,
                               relationshipTypeName,
                               dataFieldGUID,
                               schemaTypeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.setParent(schemaTypeGUID, dataFieldGUID);
            integrationReportWriter.reportElementUpdate(dataFieldGUID);
        }
    }


    /**
     * Update the metadata element representing a data field.
     *
     * @param dataFieldGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDataField(String              dataFieldGUID,
                                boolean             isMergeUpdate,
                                DataFieldProperties properties) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        client.updateDataField(userId, externalSourceGUID, externalSourceName, dataFieldGUID, isMergeUpdate, properties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(dataFieldGUID);
        }
    }


    /**
     * Remove the metadata element representing a data field.
     *
     * @param dataFieldGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDataField(String dataFieldGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        client.removeDataField(userId, externalSourceGUID, externalSourceName, dataFieldGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(dataFieldGUID);
        }
    }


    /**
     * Retrieve the list of data field metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFieldElement> findDataFields(String searchString,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return client.findDataFields(userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of data-fields associated with an element.
     *
     * @param parentElementGUID unique identifier of the element to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the data fields associated with the requested report
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFieldElement> getChildDataFields(String parentElementGUID,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return client.getChildDataFields(userId, parentElementGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of data field metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFieldElement>   getDataFieldsByName(String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return client.getDataFieldsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the data field metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFieldElement getDataFieldByGUID(String guid) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        return client.getDataFieldByGUID(userId, guid);
    }
}
