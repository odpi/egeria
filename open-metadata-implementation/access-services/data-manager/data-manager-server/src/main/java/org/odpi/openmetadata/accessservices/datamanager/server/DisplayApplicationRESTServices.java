/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.DataContainerProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.QueryProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.ReportProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.FormProperties;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.DisplayDataContainerHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DisplayApplicationRESTServices is the server-side implementation of the Data Manager OMAS's
 * support for forms.  It matches the DisplayApplicationClient.
 */
public class DisplayApplicationRESTServices
{
    private static final DataManagerInstanceHandler instanceHandler = new DataManagerInstanceHandler();
    private static final RESTCallLogger             restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(DisplayApplicationRESTServices.class),
                                                                                         instanceHandler.getServiceName());

    private final RESTExceptionHandler  restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DisplayApplicationRESTServices()
    {
    }


    /* ========================================================
     * The form, report and query are the top level assets for an application
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
    public GUIDResponse createForm(String          serverName,
                                   String          userId,
                                   boolean         applicationIsHome,
                                   FormRequestBody requestBody)
    {
        final String methodName                   = "createForm";
        final String applicationGUIDParameterName = "applicationGUID";
        final String formGUIDParameterName        = "formGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<FormElement> handler = instanceHandler.getFormHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String typeName = OpenMetadataAPIMapper.FORM_TYPE_NAME;

                if (requestBody.getTypeName() != null)
                {
                    typeName = requestBody.getTypeName();
                }

                String formGUID = handler.createAssetInRepository(userId,
                                                                  handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceGUID()),
                                                                  handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceName()),
                                                                  requestBody.getQualifiedName(),
                                                                  requestBody.getDisplayName(),
                                                                  requestBody.getVersionIdentifier(),
                                                                  requestBody.getDescription(),
                                                                  requestBody.getAdditionalProperties(),
                                                                  typeName,
                                                                  requestBody.getExtendedProperties(),
                                                                  InstanceStatus.ACTIVE,
                                                                  requestBody.getEffectiveFrom(),
                                                                  requestBody.getEffectiveTo(),
                                                                  new Date(),
                                                                  methodName);

                if (formGUID != null)
                {
                    handler.attachAssetToSoftwareServerCapability(userId,
                                                                  handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceGUID()),
                                                                  handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceName()),
                                                                  formGUID,
                                                                  formGUIDParameterName,
                                                                  requestBody.getExternalSourceGUID(),
                                                                  applicationGUIDParameterName,
                                                                  null,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName);

                    handler.setVendorProperties(userId,
                                                formGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }

                response.setGUID(formGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDResponse createFormFromTemplate(String              serverName,
                                               String              userId,
                                               String              templateGUID,
                                               boolean             applicationIsHome,
                                               TemplateRequestBody requestBody)
    {
        final String methodName                   = "createFormFromTemplate";
        final String applicationGUIDParameterName = "applicationGUID";
        final String formGUIDParameterName       = "formGUID";
        final String templateGUIDParameterName    = "templateGUID";
        final String qualifiedNameParameterName   = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<FormElement> handler = instanceHandler.getFormHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String formGUID = handler.addAssetFromTemplate(userId,
                                                               handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceGUID()),
                                                               handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceName()),
                                                               templateGUID,
                                                               templateGUIDParameterName,
                                                               OpenMetadataAPIMapper.FORM_TYPE_GUID,
                                                               OpenMetadataAPIMapper.FORM_TYPE_NAME,
                                                               requestBody.getQualifiedName(),
                                                               qualifiedNameParameterName,
                                                               requestBody.getDisplayName(),
                                                               requestBody.getVersionIdentifier(),
                                                               requestBody.getDescription(),
                                                               null,
                                                               requestBody.getNetworkAddress(),
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName);

                if (formGUID != null)
                {
                    handler.attachAssetToSoftwareServerCapability(userId,
                                                                  handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceGUID()),
                                                                  handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceName()),
                                                                  formGUID,
                                                                  formGUIDParameterName,
                                                                  requestBody.getExternalSourceGUID(),
                                                                  applicationGUIDParameterName,
                                                                  null,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName);
                }

                response.setGUID(formGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse updateForm(String          serverName,
                                   String          userId,
                                   String          formGUID,
                                   boolean         isMergeUpdate,
                                   FormRequestBody requestBody)
    {
        final String methodName = "updateForm";
        final String formGUIDParameterName = "formGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<FormElement> handler = instanceHandler.getFormHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String typeName = OpenMetadataAPIMapper.FORM_TYPE_NAME;

                if (requestBody.getTypeName() != null)
                {
                    typeName = requestBody.getTypeName();
                }

                handler.updateAsset(userId,
                                    requestBody.getExternalSourceGUID(),
                                    requestBody.getExternalSourceName(),
                                    formGUID,
                                    formGUIDParameterName,
                                    requestBody.getQualifiedName(),
                                    requestBody.getDisplayName(),
                                    requestBody.getVersionIdentifier(),
                                    requestBody.getDescription(),
                                    requestBody.getAdditionalProperties(),
                                    typeName,
                                    requestBody.getExtendedProperties(),
                                    requestBody.getEffectiveFrom(),
                                    requestBody.getEffectiveTo(),
                                    isMergeUpdate,
                                    false,
                                    false,
                                    new Date(),
                                    methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                formGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse publishForm(String          serverName,
                                    String          userId,
                                    String          formGUID,
                                    NullRequestBody nullRequestBody)
    {
        final String methodName = "publishForm";
        final String formGUIDParameterName = "formGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<FormElement> handler = instanceHandler.getFormHandler(userId, serverName, methodName);

            handler.publishAsset(userId, formGUID, formGUIDParameterName, false, false, new Date(),methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawForm(String          serverName,
                                     String          userId,
                                     String          formGUID,
                                     NullRequestBody nullRequestBody)
    {
        final String methodName = "withdrawForm";
        final String formGUIDParameterName = "formGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<FormElement> handler = instanceHandler.getFormHandler(userId, serverName, methodName);

            handler.withdrawAsset(userId, formGUID, formGUIDParameterName, false, false, new Date(), methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse removeForm(String                    serverName,
                                   String                    userId,
                                   String                    formGUID,
                                   String                    qualifiedName,
                                   MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeForm";
        final String formGUIDParameterName = "formGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<FormElement> handler = instanceHandler.getFormHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteBeanInRepository(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               formGUID,
                                               formGUIDParameterName,
                                               OpenMetadataAPIMapper.FORM_TYPE_GUID,
                                               OpenMetadataAPIMapper.FORM_TYPE_NAME,
                                               OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                               qualifiedName,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of form metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public FormsResponse findForms(String                  serverName,
                                   String                  userId,
                                   SearchStringRequestBody requestBody,
                                   int                     startFrom,
                                   int                     pageSize)
    {
        final String methodName = "findForms";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        FormsResponse response = new FormsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler<FormElement> handler = instanceHandler.getFormHandler(userId, serverName, methodName);

                List<FormElement> formAssets = handler.findAssets(userId,
                                                                  OpenMetadataAPIMapper.FORM_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.FORM_TYPE_NAME,
                                                                  requestBody.getSearchString(),
                                                                  searchStringParameterName,
                                                                  startFrom,
                                                                  pageSize,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName);

                response.setElementList(setUpFormVendorProperties(userId, formAssets, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public FormsResponse   getFormsByName(String          serverName,
                                          String          userId,
                                          NameRequestBody requestBody,
                                          int             startFrom,
                                          int             pageSize)
    {
        final String methodName = "getFormsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        FormsResponse response = new FormsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler<FormElement> handler = instanceHandler.getFormHandler(userId, serverName, methodName);

                List<FormElement> formAssets = handler.getAssetsByName(userId,
                                                                       OpenMetadataAPIMapper.FORM_TYPE_GUID,
                                                                       OpenMetadataAPIMapper.FORM_TYPE_NAME,
                                                                       requestBody.getName(),
                                                                       nameParameterName,
                                                                       startFrom,
                                                                       pageSize,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName);

                response.setElementList(setUpFormVendorProperties(userId, formAssets, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public FormsResponse getFormsForApplication(String serverName,
                                                String userId,
                                                String applicationGUID,
                                                String applicationName,
                                                int    startFrom,
                                                int    pageSize)
    {
        final String methodName = "getFormsForApplication";
        final String applicationGUIDParameterName = "applicationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        FormsResponse response = new FormsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<FormElement> handler = instanceHandler.getFormHandler(userId, serverName, methodName);

            List<FormElement> formAssets = handler.getAttachedElements(userId,
                                                                       applicationGUID,
                                                                       applicationGUIDParameterName,
                                                                       OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                                       OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                                       OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                                       OpenMetadataAPIMapper.FORM_TYPE_NAME,
                                                                       null,
                                                                       null,
                                                                       0,
                                                                       false,
                                                                       false,
                                                                       startFrom,
                                                                       pageSize,
                                                                       new Date(),
                                                                       methodName);

            response.setElementList(setUpFormVendorProperties(userId, formAssets, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the form metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public FormResponse getFormByGUID(String serverName,
                                      String userId,
                                      String guid)
    {
        final String methodName = "getFormByGUID";
        final String guidParameterName = "formGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        FormResponse response = new FormResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<FormElement> handler = instanceHandler.getFormHandler(userId, serverName, methodName);

            FormElement formAsset = handler.getBeanFromRepository(userId,
                                                                  guid,
                                                                  guidParameterName,
                                                                  OpenMetadataAPIMapper.FORM_TYPE_NAME,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName);

            response.setElement(setUpVendorProperties(userId, formAsset, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDResponse createReport(String            serverName,
                                     String            userId,
                                     boolean           applicationIsHome,
                                     ReportRequestBody requestBody)
    {
        final String methodName                   = "createReport";
        final String applicationGUIDParameterName = "applicationGUID";
        final String reportGUIDParameterName       = "reportGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<ReportElement> handler = instanceHandler.getReportHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String typeName = OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_NAME;

                if (requestBody.getTypeName() != null)
                {
                    typeName = requestBody.getTypeName();
                }

                Map<String, Object> extendedProperties = requestBody.getExtendedProperties();

                if (extendedProperties == null)
                {
                    extendedProperties = new HashMap<>();
                }

                if (requestBody.getId() != null)
                {
                    extendedProperties.put(OpenMetadataAPIMapper.ID_PROPERTY_NAME, requestBody.getId());
                }

                if (requestBody.getAuthor() != null)
                {
                    extendedProperties.put(OpenMetadataAPIMapper.AUTHOR_PROPERTY_NAME, requestBody.getAuthor());
                }

                if (requestBody.getUrl() != null)
                {
                    extendedProperties.put(OpenMetadataAPIMapper.URL_PROPERTY_NAME, requestBody.getUrl());
                }

                if (requestBody.getCreateTime() != null)
                {
                    extendedProperties.put(OpenMetadataAPIMapper.CREATED_TIME_PROPERTY_NAME, requestBody.getCreateTime());
                }

                if (requestBody.getLastModifiedTime() != null)
                {
                    extendedProperties.put(OpenMetadataAPIMapper.LAST_MODIFIED_TIME_PROPERTY_NAME, requestBody.getLastModifiedTime());
                }

                if (requestBody.getLastModifier() != null)
                {
                    extendedProperties.put(OpenMetadataAPIMapper.LAST_MODIFIER_PROPERTY_NAME, requestBody.getLastModifier());
                }

                if (extendedProperties.isEmpty())
                {
                    extendedProperties = null;
                }

                String reportGUID = handler.createAssetInRepository(userId,
                                                                    handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceGUID()),
                                                                    handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceName()),
                                                                    requestBody.getQualifiedName(),
                                                                    requestBody.getDisplayName(),
                                                                    requestBody.getVersionIdentifier(),
                                                                    requestBody.getDescription(),
                                                                    requestBody.getAdditionalProperties(),
                                                                    typeName,
                                                                    extendedProperties,
                                                                    InstanceStatus.ACTIVE,
                                                                    requestBody.getEffectiveFrom(),
                                                                    requestBody.getEffectiveTo(),
                                                                    new Date(),
                                                                    methodName);

                if ((reportGUID != null) && (requestBody.getExternalSourceGUID() != null))
                {
                    handler.linkElementToElement(userId,
                                                 handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceGUID()),
                                                 handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceName()),
                                                 requestBody.getExternalSourceGUID(),
                                                 applicationGUIDParameterName,
                                                 OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                 reportGUID,
                                                 reportGUIDParameterName,
                                                 OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_NAME,
                                                 false,
                                                 false,
                                                 OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                 OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                 (InstanceProperties) null,
                                                 null,
                                                 null,
                                                 new Date(),
                                                 methodName);
                }

                if (reportGUID != null)
                {
                    if (requestBody.getVendorProperties() != null)
                    {
                        handler.setVendorProperties(userId,
                                                    reportGUID,
                                                    requestBody.getVendorProperties(),
                                                    false,
                                                    false,
                                                    new Date(),
                                                    methodName);
                    }
                }

                response.setGUID(reportGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDResponse createReportFromTemplate(String              serverName,
                                                 String              userId,
                                                 String              templateGUID,
                                                 boolean             applicationIsHome,
                                                 TemplateRequestBody requestBody)
    {
        final String methodName                   = "createReportFromTemplate";
        final String applicationGUIDParameterName = "applicationGUID";
        final String reportGUIDParameterName       = "reportGUID";
        final String templateGUIDParameterName    = "templateGUID";
        final String qualifiedNameParameterName   = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<ReportElement> handler = instanceHandler.getReportHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String reportGUID = handler.addAssetFromTemplate(userId,
                                                                 handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceGUID()),
                                                                 handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceName()),
                                                                 templateGUID,
                                                                 templateGUIDParameterName,
                                                                 OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_GUID,
                                                                 OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_NAME,
                                                                 requestBody.getQualifiedName(),
                                                                 qualifiedNameParameterName,
                                                                 requestBody.getDisplayName(),
                                                                 requestBody.getVersionIdentifier(),
                                                                 requestBody.getDescription(),
                                                                 null,
                                                                 requestBody.getNetworkAddress(),
                                                                 false,
                                                                 false,
                                                                 new Date(),
                                                                 methodName);

                if ((reportGUID != null) && (requestBody.getExternalSourceGUID() != null))
                {
                    handler.linkElementToElement(userId,
                                                 handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceGUID()),
                                                 handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceName()),
                                                 requestBody.getExternalSourceGUID(),
                                                 applicationGUIDParameterName,
                                                 OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                 reportGUID,
                                                 reportGUIDParameterName,
                                                 OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_NAME,
                                                 false,
                                                 false,
                                                 OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                 OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                 (InstanceProperties) null,
                                                 null,
                                                 null,
                                                 new Date(),
                                                 methodName);
                }

                response.setGUID(reportGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse updateReport(String           serverName,
                                     String           userId,
                                     String           reportGUID,
                                     boolean          isMergeUpdate,
                                     ReportRequestBody requestBody)
    {
        final String methodName = "updateReport";
        final String reportGUIDParameterName = "reportGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<ReportElement> handler = instanceHandler.getReportHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String typeName = OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_NAME;

                if (requestBody.getTypeName() != null)
                {
                    typeName = requestBody.getTypeName();
                }

                Map<String, Object> extendedProperties = requestBody.getExtendedProperties();

                if (extendedProperties == null)
                {
                    extendedProperties = new HashMap<>();
                }

                if (requestBody.getId() != null)
                {
                    extendedProperties.put(OpenMetadataAPIMapper.ID_PROPERTY_NAME, requestBody.getId());
                }

                if (requestBody.getAuthor() != null)
                {
                    extendedProperties.put(OpenMetadataAPIMapper.AUTHOR_PROPERTY_NAME, requestBody.getAuthor());
                }

                if (requestBody.getUrl() != null)
                {
                    extendedProperties.put(OpenMetadataAPIMapper.URL_PROPERTY_NAME, requestBody.getUrl());
                }

                if (requestBody.getCreateTime() != null)
                {
                    extendedProperties.put(OpenMetadataAPIMapper.CREATED_TIME_PROPERTY_NAME, requestBody.getCreateTime());
                }

                if (requestBody.getLastModifiedTime() != null)
                {
                    extendedProperties.put(OpenMetadataAPIMapper.LAST_MODIFIED_TIME_PROPERTY_NAME, requestBody.getLastModifiedTime());
                }

                if (requestBody.getLastModifier() != null)
                {
                    extendedProperties.put(OpenMetadataAPIMapper.LAST_MODIFIER_PROPERTY_NAME, requestBody.getLastModifier());
                }

                if (extendedProperties.isEmpty())
                {
                    extendedProperties = null;
                }

                handler.updateAsset(userId,
                                    requestBody.getExternalSourceGUID(),
                                    requestBody.getExternalSourceName(),
                                    reportGUID,
                                    reportGUIDParameterName,
                                    requestBody.getQualifiedName(),
                                    requestBody.getDisplayName(),
                                    requestBody.getVersionIdentifier(),
                                    requestBody.getDescription(),
                                    requestBody.getAdditionalProperties(),
                                    typeName,
                                    extendedProperties,
                                    requestBody.getEffectiveFrom(),
                                    requestBody.getEffectiveTo(),
                                    isMergeUpdate,
                                    false,
                                    false,
                                    new Date(),
                                    methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                reportGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse publishReport(String          serverName,
                                      String          userId,
                                      String          reportGUID,
                                      NullRequestBody nullRequestBody)
    {
        final String methodName = "publishReport";
        final String reportGUIDParameterName = "reportGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<ReportElement> handler = instanceHandler.getReportHandler(userId, serverName, methodName);

            handler.publishAsset(userId, reportGUID, reportGUIDParameterName, false, false, new Date(), methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawReport(String          serverName,
                                       String          userId,
                                       String          reportGUID,
                                       NullRequestBody nullRequestBody)
    {
        final String methodName = "withdrawReport";
        final String reportGUIDParameterName = "reportGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<ReportElement> handler = instanceHandler.getReportHandler(userId, serverName, methodName);

            handler.withdrawAsset(userId, reportGUID, reportGUIDParameterName, false, false, new Date(), methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse removeReport(String                    serverName,
                                     String                    userId,
                                     String                    reportGUID,
                                     String                    qualifiedName,
                                     MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeReport";
        final String reportGUIDParameterName = "reportGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<ReportElement> handler = instanceHandler.getReportHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteBeanInRepository(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               reportGUID,
                                               reportGUIDParameterName,
                                               OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_GUID,
                                               OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_NAME,
                                               OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                               qualifiedName,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of report metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ReportsResponse findReports(String                  serverName,
                                       String                  userId,
                                       SearchStringRequestBody requestBody,
                                       int                     startFrom,
                                       int                     pageSize)
    {
        final String methodName = "findReports";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ReportsResponse response = new ReportsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler<ReportElement> handler = instanceHandler.getReportHandler(userId, serverName, methodName);

                List<ReportElement> reportAssets = handler.findAssets(userId,
                                                                      OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_GUID,
                                                                      OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_NAME,
                                                                      requestBody.getSearchString(),
                                                                      searchStringParameterName,
                                                                      startFrom,
                                                                      pageSize,
                                                                      false,
                                                                      false,
                                                                      new Date(),
                                                                      methodName);

                response.setElementList(setUpReportVendorProperties(userId, reportAssets, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public ReportsResponse   getReportsByName(String          serverName,
                                              String          userId,
                                              NameRequestBody requestBody,
                                              int             startFrom,
                                              int             pageSize)
    {
        final String methodName = "getReportsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ReportsResponse response = new ReportsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler<ReportElement> handler = instanceHandler.getReportHandler(userId, serverName, methodName);

                List<ReportElement> reportAssets = handler.getAssetsByName(userId,
                                                                           OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_GUID,
                                                                           OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_NAME,
                                                                           requestBody.getName(),
                                                                           nameParameterName,
                                                                           startFrom,
                                                                           pageSize,
                                                                           false,
                                                                           false,
                                                                           new Date(),
                                                                           methodName);

                response.setElementList(setUpReportVendorProperties(userId, reportAssets, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public ReportsResponse   getReportsForApplication(String serverName,
                                                      String userId,
                                                      String applicationGUID,
                                                      String applicationName,
                                                      int    startFrom,
                                                      int    pageSize)
    {
        final String methodName = "getReportsForApplication";
        final String applicationGUIDParameterName = "applicationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ReportsResponse response = new ReportsResponse();
        AuditLog        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<ReportElement> handler = instanceHandler.getReportHandler(userId, serverName, methodName);

            List<ReportElement> reportAssets = handler.getAttachedElements(userId,
                                                                           applicationGUID,
                                                                           applicationGUIDParameterName,
                                                                           OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                                           OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                                           OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                                           OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_NAME,
                                                                           null,
                                                                           null,
                                                                           0,
                                                                           false,
                                                                           false,
                                                                           startFrom,
                                                                           pageSize,
                                                                           new Date(),
                                                                           methodName);

            response.setElementList(setUpReportVendorProperties(userId, reportAssets, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the report metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ReportResponse getReportByGUID(String serverName,
                                          String userId,
                                          String guid)
    {
        final String methodName = "getReportByGUID";
        final String guidParameterName = "reportGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ReportResponse response = new ReportResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<ReportElement> handler = instanceHandler.getReportHandler(userId, serverName, methodName);

            ReportElement reportAsset = handler.getBeanFromRepository(userId,
                                                                      guid,
                                                                      guidParameterName,
                                                                      OpenMetadataAPIMapper.DEPLOYED_REPORT_TYPE_NAME,
                                                                      false,
                                                                      false,
                                                                      new Date(),
                                                                      methodName);

            response.setElement(setUpVendorProperties(userId, reportAsset, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDResponse createQuery(String           serverName,
                                    String           userId,
                                    boolean          applicationIsHome,
                                    QueryRequestBody requestBody)
    {
        final String methodName                   = "createQuery";
        final String applicationGUIDParameterName = "applicationGUID";
        final String queryGUIDParameterName       = "queryGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<QueryElement> handler = instanceHandler.getQueryHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String typeName = OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_NAME;

                if (requestBody.getTypeName() != null)
                {
                    typeName = requestBody.getTypeName();
                }

                String queryGUID = handler.createAssetInRepository(userId,
                                                                   handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceGUID()),
                                                                   handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceName()),
                                                                   requestBody.getQualifiedName(),
                                                                   requestBody.getDisplayName(),
                                                                   requestBody.getVersionIdentifier(),
                                                                   requestBody.getDescription(),
                                                                   requestBody.getAdditionalProperties(),
                                                                   typeName,
                                                                   requestBody.getExtendedProperties(),
                                                                   InstanceStatus.ACTIVE,
                                                                   requestBody.getEffectiveFrom(),
                                                                   requestBody.getEffectiveTo(),
                                                                   new Date(),
                                                                   methodName);

                if ((queryGUID != null) && (requestBody.getExternalSourceGUID() != null))
                {
                    handler.linkElementToElement(userId,
                                                 handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceGUID()),
                                                 handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceName()),
                                                 requestBody.getExternalSourceGUID(),
                                                 applicationGUIDParameterName,
                                                 OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                 queryGUID,
                                                 queryGUIDParameterName,
                                                 OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_NAME,
                                                 false,
                                                 false,
                                                 OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                 OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                 (InstanceProperties) null,
                                                 null,
                                                 null,
                                                 new Date(),
                                                 methodName);
                }

                if (queryGUID != null)
                {
                    if (requestBody.getVendorProperties() != null)
                    {
                        handler.setVendorProperties(userId,
                                                    queryGUID,
                                                    requestBody.getVendorProperties(),
                                                    false,
                                                    false,
                                                    new Date(),
                                                    methodName);
                    }
                }

                response.setGUID(queryGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDResponse createQueryFromTemplate(String              serverName,
                                                String              userId,
                                                String              templateGUID,
                                                boolean             applicationIsHome,
                                                TemplateRequestBody requestBody)
    {
        final String methodName                   = "createQueryFromTemplate";
        final String applicationGUIDParameterName = "applicationGUID";
        final String queryGUIDParameterName       = "queryGUID";
        final String templateGUIDParameterName    = "templateGUID";
        final String qualifiedNameParameterName   = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<QueryElement> handler = instanceHandler.getQueryHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String queryGUID = handler.addAssetFromTemplate(userId,
                                                                handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceGUID()),
                                                                handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceName()),
                                                                templateGUID,
                                                                templateGUIDParameterName,
                                                                OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_GUID,
                                                                OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_NAME,
                                                                requestBody.getQualifiedName(),
                                                                qualifiedNameParameterName,
                                                                requestBody.getDisplayName(),
                                                                requestBody.getVersionIdentifier(),
                                                                requestBody.getDescription(),
                                                                null,
                                                                requestBody.getNetworkAddress(),
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName);

                if ((queryGUID != null) && (requestBody.getExternalSourceGUID() != null))
                {
                    handler.linkElementToElement(userId,
                                                 handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceGUID()),
                                                 handler.getExternalSourceID(applicationIsHome, requestBody.getExternalSourceName()),
                                                 requestBody.getExternalSourceGUID(),
                                                 applicationGUIDParameterName,
                                                 OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                 queryGUID,
                                                 queryGUIDParameterName,
                                                 OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_NAME,
                                                 false,
                                                 false,
                                                 OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                 OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                 (InstanceProperties) null,
                                                 null,
                                                 null,
                                                 new Date(),
                                                 methodName);
                }

                response.setGUID(queryGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse updateQuery(String           serverName,
                                    String           userId,
                                    String           queryGUID,
                                    boolean          isMergeUpdate,
                                    QueryRequestBody requestBody)
    {
        final String methodName = "updateQuery";
        final String queryGUIDParameterName = "queryGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<QueryElement> handler = instanceHandler.getQueryHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String typeName = OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_NAME;

                if (requestBody.getTypeName() != null)
                {
                    typeName = requestBody.getTypeName();
                }

                handler.updateAsset(userId,
                                    requestBody.getExternalSourceGUID(),
                                    requestBody.getExternalSourceName(),
                                    queryGUID,
                                    queryGUIDParameterName,
                                    requestBody.getQualifiedName(),
                                    requestBody.getDisplayName(),
                                    requestBody.getVersionIdentifier(),
                                    requestBody.getDescription(),
                                    requestBody.getAdditionalProperties(),
                                    typeName,
                                    requestBody.getExtendedProperties(),
                                    requestBody.getEffectiveFrom(),
                                    requestBody.getEffectiveTo(),
                                    isMergeUpdate,
                                    false,
                                    false,
                                    new Date(),
                                    methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                queryGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse publishQuery(String          serverName,
                                     String          userId,
                                     String          queryGUID,
                                     NullRequestBody nullRequestBody)
    {
        final String methodName = "publishQuery";
        final String queryGUIDParameterName = "queryGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<QueryElement> handler = instanceHandler.getQueryHandler(userId, serverName, methodName);

            handler.publishAsset(userId, queryGUID, queryGUIDParameterName, false, false, new Date(), methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawQuery(String          serverName,
                                      String          userId,
                                      String          queryGUID,
                                      NullRequestBody nullRequestBody)
    {
        final String methodName = "withdrawQuery";
        final String queryGUIDParameterName = "queryGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<QueryElement> handler = instanceHandler.getQueryHandler(userId, serverName, methodName);

            handler.withdrawAsset(userId, queryGUID, queryGUIDParameterName, false, false, new Date(), methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse removeQuery(String                    serverName,
                                    String                    userId,
                                    String                    queryGUID,
                                    String                    qualifiedName,
                                    MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeQuery";
        final String queryGUIDParameterName = "queryGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<QueryElement> handler = instanceHandler.getQueryHandler(userId, serverName, methodName);

            handler.deleteBeanInRepository(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           queryGUID,
                                           queryGUIDParameterName,
                                           OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_GUID,
                                           OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_NAME,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           qualifiedName,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of query metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public QueriesResponse findQueries(String                  serverName,
                                       String                  userId,
                                       SearchStringRequestBody requestBody,
                                       int                     startFrom,
                                       int                     pageSize)
    {
        final String methodName = "findQueries";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        QueriesResponse response = new QueriesResponse();
        AuditLog        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler<QueryElement> handler = instanceHandler.getQueryHandler(userId, serverName, methodName);

                List<QueryElement> queryAssets = handler.findAssets(userId,
                                                                    OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_NAME,
                                                                    requestBody.getSearchString(),
                                                                    searchStringParameterName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName);

                response.setElementList(setUpQueryVendorProperties(userId, queryAssets, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public QueriesResponse   getQueriesByName(String          serverName,
                                              String          userId,
                                              NameRequestBody requestBody,
                                              int             startFrom,
                                              int             pageSize)
    {
        final String methodName = "getQueriesByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        QueriesResponse response = new QueriesResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                AssetHandler<QueryElement> handler = instanceHandler.getQueryHandler(userId, serverName, methodName);

                List<QueryElement> queryAssets = handler.getAssetsByName(userId,
                                                                         OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_GUID,
                                                                         OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_NAME,
                                                                         requestBody.getName(),
                                                                         nameParameterName,
                                                                         startFrom,
                                                                         pageSize,
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName);

                response.setElementList(setUpQueryVendorProperties(userId, queryAssets, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    @SuppressWarnings(value = "unused")
    public QueriesResponse   getQueriesForApplication(String serverName,
                                                      String userId,
                                                      String applicationGUID,
                                                      String applicationName,
                                                      int    startFrom,
                                                      int    pageSize)
    {
        final String methodName = "getQueriesForApplication";
        final String applicationGUIDParameterName = "applicationGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        QueriesResponse response = new QueriesResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<QueryElement> handler = instanceHandler.getQueryHandler(userId, serverName, methodName);

            List<QueryElement> queryAssets = handler.getAttachedElements(userId,
                                                                         applicationGUID,
                                                                         applicationGUIDParameterName,
                                                                         OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                                         OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                                         OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                                         OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_NAME,
                                                                         null,
                                                                         null,
                                                                         0,
                                                                         false,
                                                                         false,
                                                                         startFrom,
                                                                         pageSize,
                                                                         new Date(),
                                                                         methodName);

            response.setElementList(setUpQueryVendorProperties(userId, queryAssets, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the query metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public QueryResponse getQueryByGUID(String serverName,
                                        String userId,
                                        String guid)
    {
        final String methodName = "getQueryByGUID";
        final String guidParameterName = "queryGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        QueryResponse response = new QueryResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            AssetHandler<QueryElement> handler = instanceHandler.getQueryHandler(userId, serverName, methodName);

            QueryElement queryAsset = handler.getBeanFromRepository(userId,
                                                                    guid,
                                                                    guidParameterName,
                                                                    OpenMetadataAPIMapper.INFORMATION_VIEW_TYPE_NAME,
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName);

            response.setElement(setUpVendorProperties(userId, queryAsset, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public GUIDResponse createDataContainer(String                   serverName,
                                            String                   userId,
                                            String                   parentGUID,
                                            boolean                  applicationIsHome,
                                            DataContainerRequestBody requestBody)
    {
        final String methodName = "createDataContainer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DisplayDataContainerHandler<DataContainerElement, SchemaTypeElement> handler = instanceHandler.getDisplayDataContainerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String typeName = OpenMetadataAPIMapper.DISPLAY_DATA_CONTAINER_TYPE_NAME;

                if (requestBody.getTypeName() != null)
                {
                    typeName = requestBody.getTypeName();
                }

                String dataFieldGUID;

                if (applicationIsHome)
                {
                    dataFieldGUID = handler.createDataContainer(userId,
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                parentGUID,
                                                                requestBody.getQualifiedName(),
                                                                requestBody.getDisplayName(),
                                                                requestBody.getDescription(),
                                                                requestBody.getIsDeprecated(),
                                                                requestBody.getElementPosition(),
                                                                requestBody.getMinCardinality(),
                                                                requestBody.getMaxCardinality(),
                                                                requestBody.getAdditionalProperties(),
                                                                typeName,
                                                                requestBody.getExtendedProperties(),
                                                                requestBody.getVendorProperties(),
                                                                null,
                                                                null,
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName);
                }
                else
                {
                    dataFieldGUID = handler.createDataContainer(userId,
                                                                null,
                                                                null,
                                                                parentGUID,
                                                                requestBody.getQualifiedName(),
                                                                requestBody.getDisplayName(),
                                                                requestBody.getDescription(),
                                                                requestBody.getIsDeprecated(),
                                                                requestBody.getElementPosition(),
                                                                requestBody.getMinCardinality(),
                                                                requestBody.getMaxCardinality(),
                                                                requestBody.getAdditionalProperties(),
                                                                typeName,
                                                                requestBody.getExtendedProperties(),
                                                                requestBody.getVendorProperties(),
                                                                null,
                                                                null,
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName);
                }

                if (requestBody.getVendorProperties() != null)
                {
                    handler.setVendorProperties(userId,
                                                parentGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
                }

                response.setGUID(dataFieldGUID);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a data field using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param parentGUID unique identifier of the parent element  where the schema is located
     * @param applicationIsHome should the data field be marked as owned by the event broker so others can not update?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new data field or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDataContainerFromTemplate(String              serverName,
                                                        String              userId,
                                                        String              templateGUID,
                                                        String              parentGUID,
                                                        boolean             applicationIsHome,
                                                        TemplateRequestBody requestBody)
    {
        final String methodName = "createDataContainerFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DisplayDataContainerHandler<DataContainerElement, SchemaTypeElement> handler = instanceHandler.getDisplayDataContainerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (applicationIsHome)
                {
                    response.setGUID(handler.createDataContainerFromTemplate(userId,
                                                                             requestBody.getExternalSourceGUID(),
                                                                             requestBody.getExternalSourceName(),
                                                                             parentGUID,
                                                                             templateGUID,
                                                                             requestBody.getQualifiedName(),
                                                                             requestBody.getDisplayName(),
                                                                             requestBody.getDescription(),
                                                                             null,
                                                                             null,
                                                                             false,
                                                                             false,
                                                                             new Date(),
                                                                             methodName));
                }
                else
                {
                    response.setGUID(handler.createDataContainerFromTemplate(userId,
                                                                             null,
                                                                             null,
                                                                             parentGUID,
                                                                             templateGUID,
                                                                             requestBody.getQualifiedName(),
                                                                             requestBody.getDisplayName(),
                                                                             requestBody.getDescription(),
                                                                             null,
                                                                             null,
                                                                             false,
                                                                             false,
                                                                             new Date(),
                                                                             methodName));
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse updateDataContainer(String                   serverName,
                                            String                   userId,
                                            String                   dataContainerGUID,
                                            boolean                  isMergeUpdate,
                                            DataContainerRequestBody requestBody)
    {
        final String methodName = "updateDataContainer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DisplayDataContainerHandler<DataContainerElement, SchemaTypeElement> handler = instanceHandler.getDisplayDataContainerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateDataContainer(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            dataContainerGUID,
                                            requestBody.getQualifiedName(),
                                            requestBody.getDisplayName(),
                                            requestBody.getDescription(),
                                            requestBody.getIsDeprecated(),
                                            requestBody.getAdditionalProperties(),
                                            requestBody.getTypeName(),
                                            requestBody.getExtendedProperties(),
                                            requestBody.getVendorProperties(),
                                            null,
                                            null,
                                            isMergeUpdate,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public VoidResponse removeDataContainer(String                    serverName,
                                            String                    userId,
                                            String                    dataContainerGUID,
                                            String                    qualifiedName,
                                            MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeDataContainer";
        final String dataFieldGUIDParameterName = "dataContainerGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DisplayDataContainerHandler<DataContainerElement, SchemaTypeElement> handler = instanceHandler.getDisplayDataContainerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeDataContainer(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            dataContainerGUID,
                                            dataFieldGUIDParameterName,
                                            qualifiedName,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of data field metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataContainersResponse findDataContainers(String                  serverName,
                                                     String                  userId,
                                                     SearchStringRequestBody requestBody,
                                                     int                     startFrom,
                                                     int                     pageSize)
    {
        final String methodName = "findDataContainers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataContainersResponse response = new DataContainersResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DisplayDataContainerHandler<DataContainerElement, SchemaTypeElement> handler = instanceHandler.getDisplayDataContainerHandler(userId,
                                                                                                                                              serverName,
                                                                                                                                              methodName);

                List<DataContainerElement> elements = handler.findDataContainers(userId,
                                                                                 requestBody.getSearchString(),
                                                                                 startFrom,
                                                                                 pageSize,
                                                                                 false,
                                                                                 false,
                                                                                 new Date(),
                                                                                 methodName);

                response.setElementList(setUpVendorProperties(userId, elements, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DataContainersResponse getDataContainersForParent(String serverName,
                                                             String userId,
                                                             String parentGUID,
                                                             int    startFrom,
                                                             int    pageSize)
    {
        final String methodName = "getDataContainersForParent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataContainersResponse response = new DataContainersResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DisplayDataContainerHandler<DataContainerElement, SchemaTypeElement> handler = instanceHandler.getDisplayDataContainerHandler(userId, serverName, methodName);

            List<DataContainerElement> elements = handler.getContainersForParent(userId,
                                                                                 null,
                                                                                 null,
                                                                                 parentGUID,
                                                                                 startFrom,
                                                                                 pageSize,
                                                                                 null,
                                                                                 null,
                                                                                 false,
                                                                                 false,
                                                                                 new Date(),
                                                                                 methodName);

            response.setElementList(setUpVendorProperties(userId, elements, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
    public DataContainersResponse getDataContainersByName(String          serverName,
                                                          String          userId,
                                                          NameRequestBody requestBody,
                                                          int             startFrom,
                                                          int             pageSize)
    {
        final String methodName = "getDataContainersByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataContainersResponse response = new DataContainersResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                DisplayDataContainerHandler<DataContainerElement, SchemaTypeElement> handler = instanceHandler.getDisplayDataContainerHandler(userId,
                                                                                                                                              serverName,
                                                                                                                                              methodName);

                List<DataContainerElement> elements = handler.getDataContainersByName(userId,
                                                                                      requestBody.getName(),
                                                                                      startFrom,
                                                                                      pageSize,
                                                                                      false,
                                                                                      false,
                                                                                      new Date(),
                                                                                      methodName);

                response.setElementList(setUpVendorProperties(userId, elements, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the data field metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataContainerResponse getDataContainerByGUID(String serverName,
                                                        String userId,
                                                        String guid)
    {
        final String methodName = "getDataContainerByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataContainerResponse response = new DataContainerResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DisplayDataContainerHandler<DataContainerElement, SchemaTypeElement> handler = instanceHandler.getDisplayDataContainerHandler(userId, serverName, methodName);

            DataContainerElement element = handler.getDataContainerByGUID(userId,
                                                                          guid,
                                                                          false,
                                                                          false,
                                                                          new Date(),
                                                                          methodName);

            response.setElement(setUpVendorProperties(userId, element, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<FormElement> setUpFormVendorProperties(String                    userId,
                                                        List<FormElement>         retrievedResults,
                                                        AssetHandler<FormElement> handler,
                                                        String                    methodName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (FormElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private FormElement setUpVendorProperties(String                    userId,
                                              FormElement               element,
                                              AssetHandler<FormElement> handler,
                                              String                    methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            FormProperties properties = element.getProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }

        return element;
    }



    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<ReportElement> setUpReportVendorProperties(String                      userId,
                                                            List<ReportElement>         retrievedResults,
                                                            AssetHandler<ReportElement> handler,
                                                            String                      methodName) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (ReportElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private ReportElement setUpVendorProperties(String                      userId,
                                                ReportElement               element,
                                                AssetHandler<ReportElement> handler,
                                                String                      methodName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            ReportProperties properties = element.getProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }

        return element;
    }



    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<QueryElement> setUpQueryVendorProperties(String                     userId,
                                                          List<QueryElement>         retrievedResults,
                                                          AssetHandler<QueryElement> handler,
                                                          String                     methodName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (QueryElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private QueryElement setUpVendorProperties(String                     userId,
                                               QueryElement               element,
                                               AssetHandler<QueryElement> handler,
                                               String                     methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            QueryProperties properties = element.getProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }

        return element;
    }


    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<DataContainerElement> setUpVendorProperties(String                                           userId,
                                                             List<DataContainerElement>                    retrievedResults,
                                                             DisplayDataContainerHandler<DataContainerElement, SchemaTypeElement> handler,
                                                             String                                           methodName) throws InvalidParameterException,
                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                    PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (DataContainerElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private DataContainerElement setUpVendorProperties(String                                                               userId,
                                                       DataContainerElement                                                 element,
                                                       DisplayDataContainerHandler<DataContainerElement, SchemaTypeElement> handler,
                                                       String                                                               methodName) throws InvalidParameterException,
                                                                                                                                               UserNotAuthorizedException,
                                                                                                                                               PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            DataContainerProperties properties = element.getProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }

        return element;
    }
}
