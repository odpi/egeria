/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.client;

import org.odpi.openmetadata.accessservices.datamanager.api.DisplayApplicationInterface;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.datamanager.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.display.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.display.DataContainerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.display.QueryDataFieldProperties;


import java.util.ArrayList;
import java.util.List;

/**
 * DisplayApplicationClient is the client for managing reports from an Application that displays data to users.
 */
public class DisplayApplicationClient extends SchemaManagerClient implements DisplayApplicationInterface
{
    private static final String formURLTemplatePrefix      = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/forms";
    private static final String reportURLTemplatePrefix    = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/reports";
    private static final String queryURLTemplatePrefix     = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/queries";
    private static final String schemaURLTemplatePrefix    = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/schemas";
    private static final String defaultSchemaAttributeName = "DisplayDataField";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DisplayApplicationClient(String   serverName,
                                    String   serverPlatformURLRoot,
                                    AuditLog auditLog) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DisplayApplicationClient(String serverName,
                                    String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DisplayApplicationClient(String   serverName,
                                    String   serverPlatformURLRoot,
                                    String   userId,
                                    String   password,
                                    AuditLog auditLog) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DisplayApplicationClient(String                serverName,
                                    String                serverPlatformURLRoot,
                                    DataManagerRESTClient restClient,
                                    int                   maxPageSize) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }

    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DisplayApplicationClient(String serverName,
                                    String serverPlatformURLRoot,
                                    String userId,
                                    String password) throws InvalidParameterException
    {
        super(defaultSchemaAttributeName, serverName, serverPlatformURLRoot, userId, password);
    }


    /* ========================================================
     * The forms, reports and queries are top level assets on an application or reporting engine.
     */


    /**
     * Create a new metadata element to represent a form.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param applicationIsHome should the form be marked as owned by the event broker so others can not update?
     * @param formProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createForm(String         userId,
                             String         applicationGUID,
                             String         applicationName,
                             boolean        applicationIsHome,
                             FormProperties formProperties) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName                  = "createForm";
        final String propertiesParameterName     = "formProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(formProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(formProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + formURLTemplatePrefix + "?applicationIsHome={2}";

        FormRequestBody requestBody = new FormRequestBody(formProperties);

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  applicationIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a form using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
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
    @Override
    public String createFormFromTemplate(String             userId,
                                         String             applicationGUID,
                                         String             applicationName,
                                         boolean            applicationIsHome,
                                         String             templateGUID,
                                         TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                  = "createFormFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + formURLTemplatePrefix + "/from-template/{2}?applicationIsHome={3}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  applicationIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a form.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param formGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param formProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateForm(String         userId,
                           String         applicationGUID,
                           String         applicationName,
                           String         formGUID,
                           boolean        isMergeUpdate,
                           FormProperties formProperties) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName                  = "updateForm";
        final String elementGUIDParameterName    = "formGUID";
        final String propertiesParameterName     = "formProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(formGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(formProperties, propertiesParameterName, methodName);
        if (!isMergeUpdate)
        {
            invalidParameterHandler.validateName(formProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + formURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        FormRequestBody requestBody = new FormRequestBody(formProperties);

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        formGUID,
                                        isMergeUpdate);
    }


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
    @Override
    public void publishForm(String userId,
                            String formGUID) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException
    {
        final String methodName               = "publishForm";
        final String elementGUIDParameterName = "formGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(formGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + formURLTemplatePrefix + "/{2}/publish";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        formGUID);
    }


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
    @Override
    public void withdrawForm(String userId,
                             String formGUID) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException
    {
        final String methodName               = "withdrawForm";
        final String elementGUIDParameterName = "formGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(formGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + formURLTemplatePrefix + "/{2}/withdraw";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        formGUID);
    }


    /**
     * Remove the metadata element representing a form.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param formGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeForm(String userId,
                           String applicationGUID,
                           String applicationName,
                           String formGUID,
                           String qualifiedName) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        final String methodName = "removeForm";
        final String elementGUIDParameterName    = "formGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(formGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + formURLTemplatePrefix + "/{2}/{3}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        formGUID,
                                        qualifiedName);
    }


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
    @Override
    public List<FormElement> findForms(String userId,
                                       String searchString,
                                       int    startFrom,
                                       int    pageSize) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName                = "findForms";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + formURLTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        FormsResponse restResult = restClient.callFormsPostRESTCall(methodName,
                                                                   urlTemplate,
                                                                   requestBody,
                                                                   serverName,
                                                                   userId,
                                                                   startFrom,
                                                                   validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public List<FormElement>   getFormsByName(String userId,
                                              String name,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName        = "getFormsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + formURLTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        FormsResponse restResult = restClient.callFormsPostRESTCall(methodName,
                                                                    urlTemplate,
                                                                    requestBody,
                                                                    serverName,
                                                                    userId,
                                                                    startFrom,
                                                                    validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public List<FormElement> getFormsForApplication(String userId,
                                                    String applicationGUID,
                                                    String applicationName,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "getFormsForApplication";
        final String applicationGUIDParameterName = "applicationGUID";
        final String applicationNameParameterName = "applicationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(applicationGUID, applicationGUIDParameterName, methodName);
        invalidParameterHandler.validateName(applicationName, applicationNameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + formURLTemplatePrefix + "/applications/{2}/{3}?startFrom={4}&pageSize={5}";

        FormsResponse restResult = restClient.callFormsGetRESTCall(methodName,
                                                                   urlTemplate,
                                                                   serverName,
                                                                   userId,
                                                                   applicationGUID,
                                                                   applicationName,
                                                                   startFrom,
                                                                   validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public FormElement getFormByGUID(String userId,
                                     String guid) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String methodName = "getFormByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + formURLTemplatePrefix + "/{2}";

        FormResponse restResult = restClient.callFormGetRESTCall(methodName,
                                                                 urlTemplate,
                                                                 serverName,
                                                                 userId,
                                                                 guid);

        return restResult.getElement();
    }




    /**
     * Create a new metadata element to represent a report.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param applicationIsHome should the report be marked as owned by the event broker so others can not update?
     * @param reportProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createReport(String          userId,
                               String          applicationGUID,
                               String          applicationName,
                               boolean         applicationIsHome,
                               ReportProperties reportProperties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                  = "createReport";
        final String propertiesParameterName     = "reportProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(reportProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(reportProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + reportURLTemplatePrefix + "?applicationIsHome={2}";

        ReportRequestBody requestBody = new ReportRequestBody(reportProperties);

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  applicationIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a report using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
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
    @Override
    public String createReportFromTemplate(String             userId,
                                           String             applicationGUID,
                                           String             applicationName,
                                           boolean            applicationIsHome,
                                           String             templateGUID,
                                           TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                  = "createReportFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + reportURLTemplatePrefix + "/from-template/{2}?applicationIsHome={3}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  applicationIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a report.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param reportGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param reportProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateReport(String          userId,
                             String          applicationGUID,
                             String          applicationName,
                             String          reportGUID,
                             boolean         isMergeUpdate,
                             ReportProperties reportProperties) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName                  = "updateReport";
        final String elementGUIDParameterName    = "reportGUID";
        final String propertiesParameterName     = "reportProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(reportGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(reportProperties, propertiesParameterName, methodName);
        if (!isMergeUpdate)
        {
            invalidParameterHandler.validateName(reportProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + reportURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        ReportRequestBody requestBody = new ReportRequestBody(reportProperties);

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        reportGUID,
                                        isMergeUpdate);
    }


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
    @Override
    public void publishReport(String userId,
                              String reportGUID) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        final String methodName               = "publishReport";
        final String elementGUIDParameterName = "reportGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(reportGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + reportURLTemplatePrefix + "/{2}/publish";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        reportGUID);
    }


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
    @Override
    public void withdrawReport(String userId,
                               String reportGUID) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String methodName               = "withdrawReport";
        final String elementGUIDParameterName = "reportGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(reportGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + reportURLTemplatePrefix + "/{2}/withdraw";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        reportGUID);
    }


    /**
     * Remove the metadata element representing a report.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param reportGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeReport(String userId,
                             String applicationGUID,
                             String applicationName,
                             String reportGUID,
                             String qualifiedName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String methodName = "removeReport";
        final String elementGUIDParameterName    = "reportGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(reportGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + reportURLTemplatePrefix + "/{2}/{3}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        reportGUID,
                                        qualifiedName);
    }


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
    @Override
    public List<ReportElement> findReports(String userId,
                                           String searchString,
                                           int    startFrom,
                                           int    pageSize) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName                = "findReports";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + reportURLTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        ReportsResponse restResult = restClient.callReportsPostRESTCall(methodName,
                                                                        urlTemplate,
                                                                        requestBody,
                                                                        serverName,
                                                                        userId,
                                                                        startFrom,
                                                                        validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public List<ReportElement>   getReportsByName(String userId,
                                                  String name,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName        = "getReportsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + reportURLTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        ReportsResponse restResult = restClient.callReportsPostRESTCall(methodName,
                                                                        urlTemplate,
                                                                        requestBody,
                                                                        serverName,
                                                                        userId,
                                                                        startFrom,
                                                                        validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public List<ReportElement> getReportsForApplication(String userId,
                                                        String applicationGUID,
                                                        String applicationName,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "getReportsForApplication";
        final String applicationGUIDParameterName = "applicationGUID";
        final String applicationNameParameterName = "applicationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(applicationGUID, applicationGUIDParameterName, methodName);
        invalidParameterHandler.validateName(applicationName, applicationNameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + reportURLTemplatePrefix + "/applications/{2}/{3}?startFrom={4}&pageSize={5}";

        ReportsResponse restResult = restClient.callReportsGetRESTCall(methodName,
                                                                       urlTemplate,
                                                                       serverName,
                                                                       userId,
                                                                       applicationGUID,
                                                                       applicationName,
                                                                       startFrom,
                                                                       validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public ReportElement getReportByGUID(String userId,
                                         String guid) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName = "getReportByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + reportURLTemplatePrefix + "/{2}";

        ReportResponse restResult = restClient.callReportGetRESTCall(methodName,
                                                                     urlTemplate,
                                                                     serverName,
                                                                     userId,
                                                                     guid);

        return restResult.getElement();
    }




    /**
     * Create a new metadata element to represent a query.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param applicationIsHome should the query be marked as owned by the event broker so others can not update?
     * @param queryProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createQuery(String          userId,
                              String          applicationGUID,
                              String          applicationName,
                              boolean         applicationIsHome,
                              QueryProperties queryProperties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName                  = "createQuery";
        final String propertiesParameterName     = "queryProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(queryProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(queryProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + queryURLTemplatePrefix + "?applicationIsHome={2}";

        QueryRequestBody requestBody = new QueryRequestBody(queryProperties);

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  applicationIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a query using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
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
    @Override
    public String createQueryFromTemplate(String             userId,
                                          String             applicationGUID,
                                          String             applicationName,
                                          boolean            applicationIsHome,
                                          String             templateGUID,
                                          TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName                  = "createQueryFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + queryURLTemplatePrefix + "/from-template/{2}?applicationIsHome={3}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  applicationIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a query.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param queryGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param queryProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateQuery(String          userId,
                            String          applicationGUID,
                            String          applicationName,
                            String          queryGUID,
                            boolean         isMergeUpdate,
                            QueryProperties queryProperties) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                  = "updateQuery";
        final String elementGUIDParameterName    = "queryGUID";
        final String propertiesParameterName     = "queryProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(queryGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(queryProperties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(queryProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + queryURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        QueryRequestBody requestBody = new QueryRequestBody(queryProperties);

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        queryGUID,
                                        isMergeUpdate);
    }


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
    @Override
    public void publishQuery(String userId,
                             String queryGUID) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException
    {
        final String methodName               = "publishQuery";
        final String elementGUIDParameterName = "queryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(queryGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + queryURLTemplatePrefix + "/{2}/publish";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        queryGUID);
    }


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
    @Override
    public void withdrawQuery(String userId,
                              String queryGUID) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        final String methodName               = "withdrawQuery";
        final String elementGUIDParameterName = "queryGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(queryGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + queryURLTemplatePrefix + "/{2}/withdraw";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        queryGUID);
    }


    /**
     * Remove the metadata element representing a query.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param queryGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeQuery(String userId,
                            String applicationGUID,
                            String applicationName,
                            String queryGUID,
                            String qualifiedName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String methodName = "removeQuery";
        final String elementGUIDParameterName    = "queryGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(queryGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + queryURLTemplatePrefix + "/{2}/{3}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        queryGUID,
                                        qualifiedName);
    }


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
    @Override
    public List<QueryElement> findQueries(String userId,
                                          String searchString,
                                          int    startFrom,
                                          int    pageSize) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName                = "findQueries";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + queryURLTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        QueriesResponse restResult = restClient.callQueriesPostRESTCall(methodName,
                                                                        urlTemplate,
                                                                        requestBody,
                                                                        serverName,
                                                                        userId,
                                                                        startFrom,
                                                                        validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public List<QueryElement>   getQueriesByName(String userId,
                                                 String name,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName        = "getQueriesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + queryURLTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        QueriesResponse restResult = restClient.callQueriesPostRESTCall(methodName,
                                                                        urlTemplate,
                                                                        requestBody,
                                                                        serverName,
                                                                        userId,
                                                                        startFrom,
                                                                        validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of queries created by this caller.
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
    @Override
    public List<QueryElement> getQueriesForApplication(String userId,
                                                       String applicationGUID,
                                                       String applicationName,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getQueriesForApplication";
        final String applicationGUIDParameterName = "applicationGUID";
        final String applicationNameParameterName = "applicationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(applicationGUID, applicationGUIDParameterName, methodName);
        invalidParameterHandler.validateName(applicationName, applicationNameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + queryURLTemplatePrefix + "/applications/{2}/{3}?startFrom={4}&pageSize={5}";

        QueriesResponse restResult = restClient.callQueriesGetRESTCall(methodName,
                                                                       urlTemplate,
                                                                       serverName,
                                                                       userId,
                                                                       applicationGUID,
                                                                       applicationName,
                                                                       startFrom,
                                                                       validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public QueryElement getQueryByGUID(String userId,
                                       String guid) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName = "getQueryByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + queryURLTemplatePrefix + "/{2}";

        QueryResponse restResult = restClient.callQueryGetRESTCall(methodName,
                                                                   urlTemplate,
                                                                   serverName,
                                                                   userId,
                                                                   guid);

        return restResult.getElement();
    }



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
    @Override
    public String createDataContainer(String                  userId,
                                      String                  applicationGUID,
                                      String                  applicationName,
                                      boolean                 applicationIsHome,
                                      String                  parentElementGUID,
                                      DataContainerProperties dataContainerProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName                  = "createDataContainer";
        final String propertiesParameterName     = "dataContainerProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(dataContainerProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(dataContainerProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaURLTemplatePrefix + "/elements/{2}/data-containers?applicationIsHome={3}";

        DataContainerRequestBody requestBody = new DataContainerRequestBody(dataContainerProperties);

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  parentElementGUID,
                                                                  applicationIsHome);

        return restResult.getGUID();
    }


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
    @Override
    public String createDataContainerFromTemplate(String             userId,
                                                  String             applicationGUID,
                                                  String             applicationName,
                                                  boolean            applicationIsHome,
                                                  String             parentElementGUID,
                                                  String             templateGUID,
                                                  TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName                  = "createDataContainerFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaURLTemplatePrefix + "/elements/{2}/data-containers/from-template/{3}?applicationIsHome={4}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  parentElementGUID,
                                                                  templateGUID,
                                                                  applicationIsHome);

        return restResult.getGUID();
    }


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
    @Override
    public void updateDataContainer(String                  userId,
                                    String                  applicationGUID,
                                    String                  applicationName,
                                    String                  dataContainerGUID,
                                    boolean                 isMergeUpdate,
                                    DataContainerProperties dataContainerProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName                  = "updateDataContainer";
        final String elementGUIDParameterName    = "dataContainerGUID";
        final String propertiesParameterName     = "dataContainerProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataContainerGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(dataContainerProperties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(dataContainerProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + schemaURLTemplatePrefix + "/data-containers/{2}?isMergeUpdate={3}";

        DataContainerRequestBody requestBody = new DataContainerRequestBody(dataContainerProperties);

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        dataContainerGUID,
                                        isMergeUpdate);
    }




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
    @Override
    public void removeDataContainer(String userId,
                                    String applicationGUID,
                                    String applicationName,
                                    String dataContainerGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName               = "removeDataContainer";
        final String elementGUIDParameterName = "dataContainerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataContainerGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaURLTemplatePrefix + "/data-containers/{2}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(applicationGUID);
        requestBody.setExternalSourceName(applicationName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        dataContainerGUID);
    }


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
    @Override
    public List<DataContainerElement> findDataContainers(String userId,
                                                         String typeName,
                                                         String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName                = "findDataContainers";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaURLTemplatePrefix + "/data-containers/types/{2}/by-search-string?startFrom={3}&pageSize={4}";

        String requestTypeName = "DataContainer";

        if (typeName != null)
        {
            requestTypeName = typeName;
        }

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        DataContainersResponse restResult = restClient.callDataContainersPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      requestTypeName,
                                                                                      startFrom,
                                                                                      validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public List<DataContainerElement> getDataContainersForElement(String userId,
                                                                  String parentElementGUID,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName                     = "findDataContainer";
        final String parentElementGUIDParameterName = "parentElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaURLTemplatePrefix + "/data-containers/by-parent-element/{2}?startFrom={4}&pageSize={5}";

        DataContainersResponse restResult = restClient.callDataContainersGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     parentElementGUID,
                                                                                     startFrom,
                                                                                     validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public List<DataContainerElement>   getDataContainerByName(String userId,
                                                               String name,
                                                               String typeName,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName        = "getDataContainerByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaURLTemplatePrefix + "/data-containers/types/{2}/by-name?startFrom={3}&pageSize={4}";

        String requestTypeName = "DataContainer";

        if (typeName != null)
        {
            requestTypeName = typeName;
        }

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        DataContainersResponse restResult = restClient.callDataContainersPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      requestTypeName,
                                                                                      startFrom,
                                                                                      validatedPageSize);

        return restResult.getElements();
    }


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
    @Override
    public DataContainerElement getDataContainerByGUID(String userId,
                                                       String dataContainerGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getDataContainerByGUID";
        final String guidParameterName = "dataContainerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataContainerGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaURLTemplatePrefix + "/data-containers/{2}";

        DataContainerResponse restResult = restClient.callDataContainerGetRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   serverName,
                                                                                   userId,
                                                                                   dataContainerGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the header of the metadata element connected to a data container.
     *
     * @param userId calling user
     * @param dataContainerGUID unique identifier of the requested metadata element
     *
     * @return header for parent element (data asset, process, port) plus qualified name
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ElementStub getDataContainerParent(String userId,
                                              String dataContainerGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getDataContainerByGUID";
        final String guidParameterName = "dataContainerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataContainerGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + schemaURLTemplatePrefix + "/data-containers/{2}/parent";

        ElementStubResponse restResult = restClient.callElementStubGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               dataContainerGUID);

        return restResult.getElement();
    }



    /* ============================================================================
     * A report may host one or more data fields depending on its capability
     */

    /**
     * Create a new metadata element to represent a data field.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param applicationIsHome should the data field be marked as owned by the event broker so others can not update?
     * @param parentElementGUID unique identifier of the element where the data field is located
     * @param properties properties about the data field
     *
     * @return unique identifier of the new data field
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createDataField(String              userId,
                                  String              applicationGUID,
                                  String              applicationName,
                                  boolean             applicationIsHome,
                                  String              parentElementGUID,
                                  QueryDataFieldProperties properties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName                     = "createDataField";
        final String parentElementGUIDParameterName = "parentElementGUID";
        final String propertiesParameterName        = "properties";
        final String qualifiedNameParameterName     = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        if (applicationIsHome)
        {
            return super.createSchemaAttribute(userId, applicationGUID, applicationName, parentElementGUID, properties);
        }
        else
        {
            return super.createSchemaAttribute(userId, null, null, parentElementGUID, properties);
        }
    }


    /**
     * Create a new metadata element to represent a data field using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param applicationIsHome should the data field be marked as owned by the event broker so others can not update?
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
    @Override
    public String createDataFieldFromTemplate(String             userId,
                                              String             applicationGUID,
                                              String             applicationName,
                                              boolean            applicationIsHome,
                                              String             templateGUID,
                                              String             parentElementGUID,
                                              TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName                     = "createDataFieldFromTemplate";
        final String templateGUIDParameterName      = "templateGUID";
        final String parentElementGUIDParameterName = "parentElementGUID";
        final String propertiesParameterName        = "templateProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);

        if (applicationIsHome)
        {
            return super.createSchemaAttributeFromTemplate(userId, applicationGUID, applicationName, parentElementGUID, templateGUID, templateProperties);
        }
        else
        {
            return super.createSchemaAttributeFromTemplate(userId, null, null, parentElementGUID, templateGUID, templateProperties);
        }
    }


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
    public void setupSchemaType(String  userId,
                                String  applicationGUID,
                                String  applicationName,
                                boolean applicationIsHome,
                                String  relationshipTypeName,
                                String  apiParameterGUID,
                                String  schemaTypeGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        if (applicationIsHome)
        {
            super.setupSchemaType(userId, applicationGUID, applicationName, relationshipTypeName, apiParameterGUID, schemaTypeGUID);
        }
        else
        {
            super.setupSchemaType(userId, null, null, relationshipTypeName, apiParameterGUID, schemaTypeGUID);
        }
    }



    /**
     * Update the metadata element representing a data field.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param dataFieldGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateDataField(String              userId,
                                String              applicationGUID,
                                String              applicationName,
                                String              dataFieldGUID,
                                boolean             isMergeUpdate,
                                QueryDataFieldProperties properties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName               = "updateDataField";
        final String elementGUIDParameterName = "dataFieldGUID";
        final String propertiesParameterName  = "reportProperties";
        final String qualifiedNameParameterName     = "properties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFieldGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        super.updateSchemaAttribute(userId, applicationGUID, applicationName, dataFieldGUID, isMergeUpdate, properties);
    }


    /**
     * Remove the metadata element representing a data field.
     *
     * @param userId calling user
     * @param applicationGUID unique identifier of software server capability representing the event broker
     * @param applicationName unique name of software server capability representing the event broker
     * @param dataFieldGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeDataField(String userId,
                                String applicationGUID,
                                String applicationName,
                                String dataFieldGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName                  = "removeDataField";
        final String elementGUIDParameterName    = "dataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFieldGUID, elementGUIDParameterName, methodName);

        super.removeSchemaAttribute(userId, applicationGUID, applicationName, dataFieldGUID);
    }


    /**
     * Convert a list of schema attribute elements into a list of Data Fields.
     *
     * @param schemaAttributeElements returned list
     * @return return reformatted list
     */
    private List<QueryDataFieldElement> getDataFieldFromSchemaAttributes(List<SchemaAttributeElement> schemaAttributeElements)
    {
        if (schemaAttributeElements != null)
        {
            List<QueryDataFieldElement> queryDataFieldElements = new ArrayList<>();

            for (SchemaAttributeElement schemaAttributeElement : schemaAttributeElements)
            {
                if (schemaAttributeElement != null)
                {
                    QueryDataFieldElement queryDataFieldElement = new QueryDataFieldElement(schemaAttributeElement);

                    queryDataFieldElements.add(queryDataFieldElement);
                }
            }

            if (! queryDataFieldElements.isEmpty())
            {
                return queryDataFieldElements;
            }
        }

        return null;
    }


    /**
     * Retrieve the list of data field metadata elements that contain the search string.
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
    @Override
    public List<QueryDataFieldElement> findDataFields(String userId,
                                                      String searchString,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                = "findDataFields";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<SchemaAttributeElement> schemaAttributeElements = super.findSchemaAttributes(userId, searchString, defaultSchemaAttributeName, startFrom, validatedPageSize);

        return getDataFieldFromSchemaAttributes(schemaAttributeElements);
    }


    /**
     * Return the list of data-fields associated with a element.
     *
     * @param userId calling user
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
    @Override
    public List<QueryDataFieldElement> getChildDataFields(String userId,
                                                          String parentElementGUID,
                                                          int    startFrom,
                                                          int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName                     = "getDataFieldsForReport";
        final String parentElementGUIDParameterName = "parentElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, parentElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<SchemaAttributeElement> schemaAttributeElements = super.getNestedAttributes(userId, parentElementGUID, startFrom, validatedPageSize);

        return getDataFieldFromSchemaAttributes(schemaAttributeElements);
    }


    /**
     * Retrieve the list of data field metadata elements with a matching qualified or display name.
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
    @Override
    public List<QueryDataFieldElement>   getDataFieldsByName(String userId,
                                                             String name,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName        = "getDataFieldsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<SchemaAttributeElement> schemaAttributeElements = super.getSchemaAttributesByName(userId, name, defaultSchemaAttributeName, startFrom, validatedPageSize);

        return getDataFieldFromSchemaAttributes(schemaAttributeElements);
    }


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
    @Override
    public QueryDataFieldElement getDataFieldByGUID(String userId,
                                                    String guid) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName        = "getDataFieldByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        SchemaAttributeElement schemaAttributeElement = super.getSchemaAttributeByGUID(userId, guid);

        if (schemaAttributeElement != null)
        {
            return new QueryDataFieldElement(schemaAttributeElement);
        }
        else
        {
            return null;
        }
    }
}
