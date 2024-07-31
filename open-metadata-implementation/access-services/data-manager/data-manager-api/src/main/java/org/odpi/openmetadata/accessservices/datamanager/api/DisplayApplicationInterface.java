/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.api;

import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.display.FormProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.display.QueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.display.ReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.display.DataContainerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.display.DataFieldProperties;

import java.util.List;

/**
 * ApplicationInterface defines the client side interface for the Data Manager OMAS that is
 * relevant for form assets that provide data displays and other business functions to an organization.
 * It provides the ability to define and maintain the metadata about forms, reports and queries and the
 * schemas (data containers and data fields) they contain.
 */
public interface DisplayApplicationInterface
{
    /*
     * The form describes a data display that includes input fields where a person can enter new data.
     */

    /**
     * Create a new metadata element to represent a form.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param applicationIsHome should the form be marked as owned by the event broker so others can not update?
     * @param formProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createForm(String         userId,
                      String         applicationGUID,
                      String         applicationName,
                      boolean        applicationIsHome,
                      FormProperties formProperties) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


    /**
     * Create a new metadata element to represent a form using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param applicationIsHome should the form be marked as owned by the event broker so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createFormFromTemplate(String             userId,
                                  String             applicationGUID,
                                  String             applicationName,
                                  boolean            applicationIsHome,
                                  String             templateGUID,
                                  TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Update the metadata element representing a form.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param formGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param formProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateForm(String         userId,
                    String         applicationGUID,
                    String         applicationName,
                    String         formGUID,
                    boolean        isMergeUpdate,
                    FormProperties formProperties) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;


    /**
     * Update the zones for the form asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param userId calling user
     * @param formGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void publishForm(String userId,
                     String formGUID) throws InvalidParameterException,
                                             UserNotAuthorizedException,
                                             PropertyServerException;


    /**
     * Update the zones for the form asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the form is first created).
     *
     * @param userId calling user
     * @param formGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void withdrawForm(String userId,
                      String formGUID) throws InvalidParameterException,
                                              UserNotAuthorizedException,
                                              PropertyServerException;


    /**
     * Remove the metadata element representing a form.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param formGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeForm(String userId,
                    String applicationGUID,
                    String applicationName,
                    String formGUID,
                    String qualifiedName) throws InvalidParameterException,
                                                 UserNotAuthorizedException,
                                                 PropertyServerException;


    /**
     * Retrieve the list of form metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
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
    List<FormElement> findForms(String userId,
                                String searchString,
                                int    startFrom,
                                int    pageSize) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;


    /**
     * Retrieve the list of form metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
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
    List<FormElement> getFormsByName(String userId,
                                     String name,
                                     int    startFrom,
                                     int    pageSize) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Retrieve the list of forms created by this caller.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the form manager (event broker)
     * @param applicationName unique name of software server capability representing the form manager (event broker)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<FormElement> getFormsForApplication(String userId,
                                             String applicationGUID,
                                             String applicationName,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Retrieve the form metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    FormElement getFormByGUID(String userId,
                              String guid) throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException;


    /*
     * The report is the top level asset in an application
     */

    /**
     * Create a new metadata element to represent a report.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param applicationIsHome should the report be marked as owned by the event broker so others can not update?
     * @param reportProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createReport(String           userId,
                        String           applicationGUID,
                        String           applicationName,
                        boolean          applicationIsHome,
                        ReportProperties reportProperties) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Create a new metadata element to represent a report using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param applicationIsHome should the report be marked as owned by the event broker so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createReportFromTemplate(String             userId,
                                    String             applicationGUID,
                                    String             applicationName,
                                    boolean            applicationIsHome,
                                    String             templateGUID,
                                    TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Update the metadata element representing a report.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param reportGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param reportProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateReport(String           userId,
                      String           applicationGUID,
                      String           applicationName,
                      String           reportGUID,
                      boolean          isMergeUpdate,
                      ReportProperties reportProperties) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Update the zones for the report asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param userId calling user
     * @param reportGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void publishReport(String userId,
                       String reportGUID) throws InvalidParameterException,
                                                 UserNotAuthorizedException,
                                                 PropertyServerException;


    /**
     * Update the zones for the report asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the report is first created).
     *
     * @param userId calling user
     * @param reportGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void withdrawReport(String userId,
                        String reportGUID) throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException;


    /**
     * Remove the metadata element representing a report.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param reportGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeReport(String userId,
                      String applicationGUID,
                      String applicationName,
                      String reportGUID,
                      String qualifiedName) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException;


    /**
     * Retrieve the list of report metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
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
    List<ReportElement> findReports(String userId,
                                    String searchString,
                                    int    startFrom,
                                    int    pageSize) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


    /**
     * Retrieve the list of report metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
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
    List<ReportElement> getReportsByName(String userId,
                                         String name,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Retrieve the list of reports created by this caller.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the report manager (event broker)
     * @param applicationName unique name of software server capability representing the report manager (event broker)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<ReportElement> getReportsForApplication(String userId,
                                                 String applicationGUID,
                                                 String applicationName,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Retrieve the report metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    ReportElement getReportByGUID(String userId,
                                  String guid) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException;


    /*
     * The query is a top level asset in an application that assembles data for a report or form (or other data processing work).
     */

    /**
     * Create a new metadata element to represent a query.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param applicationIsHome should the query be marked as owned by the event broker so others can not update?
     * @param queryProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createQuery(String          userId,
                       String          applicationGUID,
                       String          applicationName,
                       boolean         applicationIsHome,
                       QueryProperties queryProperties) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Create a new metadata element to represent a query using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param applicationIsHome should the query be marked as owned by the event broker so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createQueryFromTemplate(String             userId,
                                   String             applicationGUID,
                                   String             applicationName,
                                   boolean            applicationIsHome,
                                   String             templateGUID,
                                   TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Update the metadata element representing a query.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param queryGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param queryProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateQuery(String          userId,
                     String          applicationGUID,
                     String          applicationName,
                     String          queryGUID,
                     boolean         isMergeUpdate,
                     QueryProperties queryProperties) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Update the zones for the query asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param userId calling user
     * @param queryGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void publishQuery(String userId,
                      String queryGUID) throws InvalidParameterException,
                                               UserNotAuthorizedException,
                                               PropertyServerException;


    /**
     * Update the zones for the query asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the query is first created).
     *
     * @param userId calling user
     * @param queryGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void withdrawQuery(String userId,
                       String queryGUID) throws InvalidParameterException,
                                                UserNotAuthorizedException,
                                                PropertyServerException;


    /**
     * Remove the metadata element representing a query.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param queryGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeQuery(String userId,
                     String applicationGUID,
                     String applicationName,
                     String queryGUID,
                     String qualifiedName) throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException;


    /**
     * Retrieve the list of query metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
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
    List<QueryElement> findQueries(String userId,
                                   String searchString,
                                   int    startFrom,
                                   int    pageSize) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Retrieve the list of query metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
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
    List<QueryElement> getQueriesByName(String userId,
                                        String name,
                                        int    startFrom,
                                        int    pageSize) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Retrieve the list of querys created by this caller.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the query manager (event broker)
     * @param applicationName unique name of software server capability representing the query manager (event broker)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<QueryElement> getQueriesForApplication(String userId,
                                                String applicationGUID,
                                                String applicationName,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Retrieve the query metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    QueryElement getQueryByGUID(String userId,
                                String guid) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException;


    /*
     * Application assets have nested data fields grouped into related data containers
     */


    /**
     * Create a new metadata element to represent a data container.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param applicationIsHome should the query be marked as owned by the event broker so others can not update?
     * @param parentElementGUID element to link the data container to
     * @param dataContainerProperties properties about the data container to store
     *
     * @return unique identifier of the new data container
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDataContainer(String                  userId,
                               String                  applicationGUID,
                               String                  applicationName,
                               boolean                 applicationIsHome,
                               String                  parentElementGUID,
                               DataContainerProperties dataContainerProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;

    /**
     * Create a new metadata element to represent a data container using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param applicationIsHome should the query be marked as owned by the event broker so others can not update?
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
    String createDataContainerFromTemplate(String             userId,
                                           String             applicationGUID,
                                           String             applicationName,
                                           boolean            applicationIsHome,
                                           String             parentElementGUID,
                                           String             templateGUID,
                                           TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Update the metadata element representing a data container.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param dataContainerGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param dataContainerProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateDataContainer(String               userId,
                             String               applicationGUID,
                             String               applicationName,
                             String               dataContainerGUID,
                             boolean              isMergeUpdate,
                             DataContainerProperties dataContainerProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;




    /**
     * Remove the metadata element representing a data container.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param dataContainerGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeDataContainer(String userId,
                             String applicationGUID,
                             String applicationName,
                             String dataContainerGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


    /**
     * Retrieve the list of data container metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
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
    List<DataContainerElement> findDataContainers(String userId,
                                                  String searchString,
                                                  String typeName,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Return the data container associated with a specific open metadata element (data asset, process or port).
     *
     * @param userId calling user
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
    List<DataContainerElement> getDataContainersForElement(String userId,
                                                           String parentElementGUID,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;


    /**
     * Retrieve the list of data container metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
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
    List<DataContainerElement> getDataContainerByName(String userId,
                                                      String name,
                                                      String typeName,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Retrieve the data container metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param dataContainerGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    DataContainerElement getDataContainerByGUID(String userId,
                                                String dataContainerGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Retrieve the header of the metadata element connected to a data container.
     *
     * @param userId calling user
     * @param dataContainerGUID unique identifier of the requested metadata element
     *
     * @return header for parent element (data asset, process, port)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    ElementStub getDataContainerParent(String userId,
                                       String dataContainerGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /*
     * A data field describes a label and a data value
     */

    /**
     * Create a new metadata element to represent an data field.  This describes the structure of an event supported by
     * the form. The structure of this data field is added using SchemaAttributes.   These SchemaAttributes can have
     * a simple type or a nested structure.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param applicationIsHome should the data field be marked as owned by the event broker so others can not update?
     * @param parentElementGUID unique identifier of the parent
     * @param properties properties about the form schema
     *
     * @return unique identifier of the new form schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDataField(String              userId,
                           String              applicationGUID,
                           String              applicationName,
                           boolean             applicationIsHome,
                           String              parentElementGUID,
                           DataFieldProperties properties) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Create a new metadata element to represent a an data field using an existing data field as a template.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param applicationIsHome should the data field be marked as owned by the event broker so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param parentElementGUID unique identifier of the form where the data field is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new data field
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDataFieldFromTemplate(String             userId,
                                       String             applicationGUID,
                                       String             applicationName,
                                       boolean            applicationIsHome,
                                       String             templateGUID,
                                       String             parentElementGUID,
                                       TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Connect a schema type to a data field.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param applicationIsHome should the data field be marked as owned by the event broker so others can not update?
     * @param relationshipTypeName name of relationship to create
     * @param apiParameterGUID unique identifier of the API parameter
     * @param schemaTypeGUID unique identifier of the schema type to connect
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupSchemaType(String  userId,
                         String  applicationGUID,
                         String  applicationName,
                         boolean applicationIsHome,
                         String  relationshipTypeName,
                         String  apiParameterGUID,
                         String  schemaTypeGUID) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;

    /**
     * Update the metadata element representing an data field.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param dataFieldGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateDataField(String              userId,
                         String              applicationGUID,
                         String              applicationName,
                         String              dataFieldGUID,
                         boolean             isMergeUpdate,
                         DataFieldProperties properties) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Remove an data field.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the caller
     * @param applicationName unique name of software server capability representing the caller
     * @param dataFieldGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeDataField(String userId,
                         String applicationGUID,
                         String applicationName,
                         String dataFieldGUID) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException;


    /**
     * Retrieve the list of data fields metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
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
    List<DataFieldElement> findDataFields(String userId,
                                          String searchString,
                                          int    startFrom,
                                          int    pageSize) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Return the list of data fields associated with a parent element (application asset or data container or data field with a complex type).
     *
     * @param userId calling user
     * @param parentElementGUID unique identifier of the form to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the data fields associated with the requested form
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<DataFieldElement> getChildDataFields(String userId,
                                              String parentElementGUID,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Retrieve the list of data fields metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
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
    List<DataFieldElement> getDataFieldsByName(String userId,
                                               String name,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Retrieve the data field metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    DataFieldElement getDataFieldByGUID(String userId,
                                        String guid) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;
}
