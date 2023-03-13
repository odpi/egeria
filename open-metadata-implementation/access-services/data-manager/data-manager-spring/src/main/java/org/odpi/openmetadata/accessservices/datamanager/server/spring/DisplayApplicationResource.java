/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.accessservices.datamanager.server.DisplayApplicationRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;


/**
 * DisplayApplicationResource is the server-side implementation of the Data Manager OMAS's
 * support for forms, reports and queries.  It matches the DisplayApplicationClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-manager/users/{userId}")

@Tag(name="Data Manager OMAS",
     description="The Data Manager OMAS provides APIs for tools and applications wishing to manage metadata relating to data managers " +
                         "such as database servers, event brokers, content managers and file systems.",
     externalDocs=@ExternalDocumentation(description="Data Manager Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/data-manager/overview/"))

public class DisplayApplicationResource
{
    private final DisplayApplicationRESTServices restAPI = new DisplayApplicationRESTServices();

    /**
     * Default constructor
     */
    public DisplayApplicationResource()
    {
    }


    /* ========================================================
     * The form, report and query are the top level assets for a application
     */

    /**
     * Create a new metadata element to represent a form.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param applicationIsHome should the form be marked as owned by the application so others can not update?
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/forms")

    public GUIDResponse createForm(@PathVariable String          serverName,
                                   @PathVariable String          userId,
                                   @RequestParam boolean         applicationIsHome,
                                   @RequestBody  FormRequestBody requestBody)
    {
        return restAPI.createForm(serverName, userId, applicationIsHome, requestBody);
    }


    /**
     * Create a new metadata element to represent a form using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param applicationIsHome should the form be marked as owned by the application so others can not update?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/forms/by-template/{templateGUID}")
    
    public GUIDResponse createFormFromTemplate(@PathVariable String              serverName,
                                               @PathVariable String              userId,
                                               @PathVariable String              templateGUID,
                                               @RequestParam boolean             applicationIsHome,
                                               @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createFormFromTemplate(serverName, userId, templateGUID, applicationIsHome, requestBody);
    }


    /**
     * Update the metadata element representing a form.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param formGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/forms/{formGUID}")

    public VoidResponse updateForm(@PathVariable String          serverName,
                                   @PathVariable String          userId,
                                   @PathVariable String          formGUID,
                                   @RequestParam boolean         isMergeUpdate,
                                   @RequestBody  FormRequestBody requestBody)
    {
        return restAPI.updateForm(serverName, userId, formGUID, isMergeUpdate, requestBody);
    }


    /**
     * Update the zones for the form asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param formGUID unique identifier of the metadata element to publish
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/forms/{formGUID}/publish")

    public VoidResponse publishForm(@PathVariable                  String          serverName,
                                    @PathVariable                  String          userId,
                                    @PathVariable                  String          formGUID,
                                    @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.publishForm(serverName, userId, formGUID, nullRequestBody);
    }


    /**
     * Update the zones for the form asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the form is first created).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param formGUID unique identifier of the metadata element to withdraw
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/forms/{formGUID}/withdraw")

    public VoidResponse withdrawForm(@PathVariable                  String          serverName,
                                     @PathVariable                  String          userId,
                                     @PathVariable                  String          formGUID,
                                     @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.withdrawForm(serverName, userId, formGUID, nullRequestBody);
    }


    /**
     * Remove the metadata element representing a form.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param formGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/forms/{formGUID}/{qualifiedName}/delete")

    public VoidResponse removeForm(@PathVariable String                    serverName,
                                   @PathVariable String                    userId,
                                   @PathVariable String                    formGUID,
                                   @PathVariable String                    qualifiedName,
                                   @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.removeForm(serverName, userId, formGUID, qualifiedName, requestBody);
    }


    /**
     * Retrieve the list of form metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody  string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/forms/by-search-string")

    public FormsResponse findForms(@PathVariable String                  serverName,
                                   @PathVariable String                  userId,
                                   @RequestBody  SearchStringRequestBody requestBody,
                                   @RequestParam int                     startFrom,
                                   @RequestParam int                     pageSize)
    {
        return restAPI.findForms(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of form metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/forms/by-name")

    public FormsResponse   getFormsByName(@PathVariable String          serverName,
                                          @PathVariable String          userId,
                                          @RequestBody  NameRequestBody requestBody,
                                          @RequestParam int             startFrom,
                                          @RequestParam int             pageSize)
    {
        return restAPI.getFormsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of forms created by this caller.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the application
     * @param applicationName unique name of software server capability representing the application
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/forms/applications/{applicationGUID}/{applicationName}")

    public FormsResponse getFormsForApplication(@PathVariable String serverName,
                                                @PathVariable String userId,
                                                @PathVariable String applicationGUID,
                                                @PathVariable String applicationName,
                                                @RequestParam int    startFrom,
                                                @RequestParam int    pageSize)
    {
        return restAPI.getFormsForApplication(serverName, userId, applicationGUID, applicationName, startFrom, pageSize);
    }


    /**
     * Retrieve the form metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param formGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/forms/{formGUID}")

    public FormResponse getFormByGUID(@PathVariable String serverName,
                                      @PathVariable String userId,
                                      @PathVariable String formGUID)
    {
        return restAPI.getFormByGUID(serverName, userId, formGUID);
    }


    /**
     * Create a new metadata element to represent a report.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param applicationIsHome should the report be marked as owned by the application so others can not update?
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/reports")

    public GUIDResponse createReport(@PathVariable String            serverName,
                                     @PathVariable String            userId,
                                     @RequestParam boolean           applicationIsHome,
                                     @RequestBody  ReportRequestBody requestBody)
    {
        return restAPI.createReport(serverName, userId, applicationIsHome, requestBody);
    }


    /**
     * Create a new metadata element to represent a report using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param applicationIsHome should the report be marked as owned by the application so others can not update?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/reports/from-template/{templateGUID}")

    public GUIDResponse createReportFromTemplate(@PathVariable String              serverName,
                                                 @PathVariable String              userId,
                                                 @PathVariable String              templateGUID,
                                                 @RequestParam boolean             applicationIsHome,
                                                 @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createReportFromTemplate(serverName, userId, templateGUID, applicationIsHome, requestBody);
    }


    /**
     * Update the metadata element representing a report.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param reportGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/reports/{reportGUID}")

    public VoidResponse updateReport(@PathVariable String            serverName,
                                     @PathVariable String            userId,
                                     @PathVariable String            reportGUID,
                                     @RequestParam boolean           isMergeUpdate,
                                     @RequestBody  ReportRequestBody requestBody)
    {
        return restAPI.updateReport(serverName, userId, reportGUID, isMergeUpdate, requestBody);
    }


    /**
     * Update the zones for the report asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param reportGUID unique identifier of the metadata element to publish
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/reports/{reportGUID}/publish")

    public VoidResponse publishReport(@PathVariable                  String          serverName,
                                      @PathVariable                  String          userId,
                                      @PathVariable                  String          reportGUID,
                                      @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.publishReport(serverName, userId, reportGUID, nullRequestBody);
    }


    /**
     * Update the zones for the report asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the report is first created).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param reportGUID unique identifier of the metadata element to withdraw
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/reports/{reportGUID}/withdraw")

    public VoidResponse withdrawReport(@PathVariable                  String          serverName,
                                       @PathVariable                  String          userId,
                                       @PathVariable                  String          reportGUID,
                                       @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.withdrawReport(serverName, userId, reportGUID, nullRequestBody);
    }


    /**
     * Remove the metadata element representing a report.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param reportGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/reports/{reportGUID}/{qualifiedName}/delete")

    public VoidResponse removeReport(@PathVariable String                    serverName,
                                     @PathVariable String                    userId,
                                     @PathVariable String                    reportGUID,
                                     @PathVariable String                    qualifiedName,
                                     @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.removeReport(serverName, userId, reportGUID, qualifiedName, requestBody);
    }


    /**
     * Retrieve the list of report metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody  string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/reports/by-search-string")

    public ReportsResponse findReports(@PathVariable String                  serverName,
                                       @PathVariable String                  userId,
                                       @RequestBody  SearchStringRequestBody requestBody,
                                       @RequestParam int                     startFrom,
                                       @RequestParam int                     pageSize)
    {
        return restAPI.findReports(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of report metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/reports/by-name")

    public ReportsResponse   getReportsByName(@PathVariable String          serverName,
                                              @PathVariable String          userId,
                                              @RequestBody  NameRequestBody requestBody,
                                              @RequestParam int             startFrom,
                                              @RequestParam int             pageSize)
    {
        return restAPI.getReportsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of reports created by this caller.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the application
     * @param applicationName unique name of software server capability representing the application
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/reports/applications/{applicationGUID}/{applicationName}")

    public ReportsResponse   getReportsForApplication(@PathVariable String serverName,
                                                      @PathVariable String userId,
                                                      @PathVariable String applicationGUID,
                                                      @PathVariable String applicationName,
                                                      @RequestParam int    startFrom,
                                                      @RequestParam int    pageSize)
    {
        return restAPI.getReportsForApplication(serverName, userId, applicationGUID, applicationName, startFrom, pageSize);
    }


    /**
     * Retrieve the report metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param reportGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/reports/{reportGUID}")

    public ReportResponse getReportByGUID(@PathVariable String serverName,
                                          @PathVariable String userId,
                                          @PathVariable String reportGUID)
    {
        return restAPI.getReportByGUID(serverName, userId, reportGUID);
    }


    /**
     * Create a new metadata element to represent a query.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param applicationIsHome should the query be marked as owned by the application so others can not update?
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/queries")

    public GUIDResponse createQuery(@PathVariable String           serverName,
                                    @PathVariable String           userId,
                                    @RequestParam boolean          applicationIsHome,
                                    @RequestBody  QueryRequestBody requestBody)
    {
        return restAPI.createQuery(serverName, userId, applicationIsHome, requestBody);
    }


    /**
     * Create a new metadata element to represent a query using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param applicationIsHome should the query be marked as owned by the application so others can not update?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/queries/from-template/{templateGUID}")

    public GUIDResponse createQueryFromTemplate(@PathVariable String              serverName,
                                                @PathVariable String              userId,
                                                @PathVariable String              templateGUID,
                                                @RequestParam boolean             applicationIsHome,
                                                @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createQueryFromTemplate(serverName, userId, templateGUID, applicationIsHome, requestBody);
    }


    /**
     * Update the metadata element representing a query.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param queryGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/queries/{queryGUID}")

    public VoidResponse updateQuery(@PathVariable String           serverName,
                                    @PathVariable String           userId,
                                    @PathVariable String           queryGUID,
                                    @RequestParam boolean          isMergeUpdate,
                                    @RequestBody  QueryRequestBody requestBody)
    {
        return restAPI.updateQuery(serverName, userId, queryGUID, isMergeUpdate, requestBody);
    }


    /**
     * Update the zones for the query asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param queryGUID unique identifier of the metadata element to publish
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/queries/{queryGUID}/publish")

    public VoidResponse publishQuery(@PathVariable                  String          serverName,
                                     @PathVariable                  String          userId,
                                     @PathVariable                  String          queryGUID,
                                     @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.publishQuery(serverName, userId, queryGUID, nullRequestBody);
    }


    /**
     * Update the zones for the query asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the query is first created).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param queryGUID unique identifier of the metadata element to withdraw
     * @param nullRequestBody empty request body
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/queries/{queryGUID}/withdraw")

    public VoidResponse withdrawQuery(@PathVariable                  String          serverName,
                                      @PathVariable                  String          userId,
                                      @PathVariable                  String          queryGUID,
                                      @RequestBody(required = false) NullRequestBody nullRequestBody)
    {
        return restAPI.withdrawQuery(serverName, userId, queryGUID, nullRequestBody);
    }


    /**
     * Remove the metadata element representing a query.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param queryGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/queries/{queryGUID}/{qualifiedName}/delete")

    public VoidResponse removeQuery(@PathVariable String                    serverName,
                                    @PathVariable String                    userId,
                                    @PathVariable String                    queryGUID,
                                    @PathVariable String                    qualifiedName,
                                    @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.removeQuery(serverName, userId, queryGUID, qualifiedName, requestBody);
    }


    /**
     * Retrieve the list of query metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody  string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/queries/by-search-string")

    public QueriesResponse findQueries(@PathVariable String                  serverName,
                                       @PathVariable String                  userId,
                                       @RequestBody  SearchStringRequestBody requestBody,
                                       @RequestParam int                     startFrom,
                                       @RequestParam int                     pageSize)
    {
        return restAPI.findQueries(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of query metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/queries/by-name")

    public QueriesResponse   getQueriesByName(@PathVariable String          serverName,
                                              @PathVariable String          userId,
                                              @RequestBody  NameRequestBody requestBody,
                                              @RequestParam int             startFrom,
                                              @RequestParam int             pageSize)
    {
        return restAPI.getQueriesByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of queries created by this caller.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the application
     * @param applicationName unique name of software server capability representing the application
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/queries/applications/{applicationGUID}/{applicationName}")

    public QueriesResponse   getQueriesForApplication(@PathVariable String serverName,
                                                      @PathVariable String userId,
                                                      @PathVariable String applicationGUID,
                                                      @PathVariable String applicationName,
                                                      @RequestParam int    startFrom,
                                                      @RequestParam int    pageSize)
    {
        return restAPI.getQueriesForApplication(serverName, userId, applicationGUID, applicationName, startFrom, pageSize);
    }


    /**
     * Retrieve the query metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param queryGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/queries/{queryGUID}")

    public QueryResponse getQueryByGUID(@PathVariable String serverName,
                                        @PathVariable String userId,
                                        @PathVariable String queryGUID)
    {
        return restAPI.getQueryByGUID(serverName, userId, queryGUID);
    }


    /* ============================================================================
     * A form, report or query has a structure described in terms of data fields grouped by data containers
     */

    /**
     * Create a new metadata element to represent a data field.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param applicationIsHome should the data field be marked as owned by the event broker so others can not update?
     * @param parentGUID unique identifier of the parent element  where the schema is located
     * @param requestBody properties about the data field
     *
     * @return unique identifier of the new data field or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schemas/elements/{parentGUID}/data-containers")

    public GUIDResponse createDataContainer(@PathVariable String                   serverName,
                                            @PathVariable String                   userId,
                                            @PathVariable String                   parentGUID,
                                            @RequestParam boolean                  applicationIsHome,
                                            @RequestBody  DataContainerRequestBody requestBody)
    {
        return restAPI.createDataContainer(serverName, userId, parentGUID, applicationIsHome, requestBody);
    }


    /**
     * Create a new metadata element to represent a data field using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param parentGUID unique identifier of the parent where the schema is located
     * @param applicationIsHome should the data field be marked as owned by the event broker so others can not update?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new data field or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schemas/elements/{parentGUID}/data-containers/from-template/{templateGUID}")

    public GUIDResponse createDataContainerFromTemplate(@PathVariable String              serverName,
                                                        @PathVariable String              userId,
                                                        @PathVariable String              templateGUID,
                                                        @PathVariable String              parentGUID,
                                                        @RequestParam boolean             applicationIsHome,
                                                        @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createDataContainerFromTemplate(serverName, userId, templateGUID, parentGUID, applicationIsHome, requestBody);
    }


    /**
     * Update the metadata element representing a data field.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param dataContainerGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schemas/data-containers/{dataContainerGUID}")

    public VoidResponse updateDataContainer(@PathVariable String                   serverName,
                                            @PathVariable String                   userId,
                                            @PathVariable String                   dataContainerGUID,
                                            @RequestParam boolean                  isMergeUpdate,
                                            @RequestBody  DataContainerRequestBody requestBody)
    {
        return restAPI.updateDataContainer(serverName, userId, dataContainerGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the metadata element representing a data container.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param dataContainerGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schemas/data-containers/{dataContainerGUID}/delete")

    public VoidResponse removeDataContainer(@PathVariable String                    serverName,
                                            @PathVariable String                    userId,
                                            @PathVariable String                    dataContainerGUID,
                                            @PathVariable String                    qualifiedName,
                                            @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.removeDataContainer(serverName, userId, dataContainerGUID, qualifiedName, requestBody);
    }


    /**
     * Retrieve the list of data field metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody  string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schemas/data-containers/by-search-string")

    public DataContainersResponse findDataContainers(@PathVariable String                  serverName,
                                                     @PathVariable String                  userId,
                                                     @RequestBody  SearchStringRequestBody requestBody,
                                                     @RequestParam int                     startFrom,
                                                     @RequestParam int                     pageSize)
    {
        return restAPI.findDataContainers(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Return the list of schemas associated with a parent element .
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param parentGUID unique identifier of the parent element  to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the schemas associated with the requested parent element  or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/schemas/data-containers/by-parent-element/{parentGUID}")

    public DataContainersResponse getDataContainersForParent(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String parentGUID,
                                                             @RequestParam int    startFrom,
                                                             @RequestParam int    pageSize)
    {
        return restAPI.getDataContainersForParent(serverName, userId, parentGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of data field metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schemas/data-containers/by-name")

    public DataContainersResponse getDataContainersByName(@PathVariable String          serverName,
                                                          @PathVariable String          userId,
                                                          @RequestBody  NameRequestBody requestBody,
                                                          @RequestParam int             startFrom,
                                                          @RequestParam int             pageSize)
    {
        return restAPI.getDataContainersByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the data field metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param dataContainerGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/schemas/data-containers/{dataContainerGUID}")

    public DataContainerResponse getDataContainerByGUID(@PathVariable String serverName,
                                                        @PathVariable String userId,
                                                        @PathVariable String dataContainerGUID)
    {
        return restAPI.getDataContainerByGUID(serverName, userId, dataContainerGUID);
    }
}
