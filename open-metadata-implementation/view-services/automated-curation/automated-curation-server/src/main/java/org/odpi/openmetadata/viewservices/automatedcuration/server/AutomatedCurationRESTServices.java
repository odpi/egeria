/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.automatedcuration.server;

import org.odpi.openmetadata.accessservices.assetowner.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.accessservices.assetowner.client.OpenGovernanceClient;
import org.odpi.openmetadata.accessservices.assetowner.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.TemplateRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.MetadataCorrelationHeadersResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.UpdateMetadataCorrelatorsRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.automatedcuration.handlers.TechnologyTypeHandler;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeElementListResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeHierarchyResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeReportResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeSummaryListResponse;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The AutomatedCurationRESTServices provides the implementation of the Automated Curation Open Metadata View Service (OMVS).
 */

public class AutomatedCurationRESTServices extends TokenController
{
    private static final AutomatedCurationInstanceHandler instanceHandler = new AutomatedCurationInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(AutomatedCurationRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public AutomatedCurationRESTServices()
    {
    }

    /* =====================================================================================================================
     * The technology types provide the reference data to guide the curator.  They are extracted from the valid
     * values for deployedImplementationType
     */

    /**
     * Retrieve the list of deployed implementation type metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public TechnologyTypeSummaryListResponse findTechnologyTypes(String            serverName,
                                                                 boolean           startsWith,
                                                                 boolean           endsWith,
                                                                 boolean           ignoreCase,
                                                                 int               startFrom,
                                                                 int               pageSize,
                                                                 FilterRequestBody requestBody)
    {
        final String methodName = "findTechnologyTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TechnologyTypeSummaryListResponse response = new TechnologyTypeSummaryListResponse();
        AuditLog                          auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            TechnologyTypeHandler handler = instanceHandler.getTechnologyTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findTechnologyTypes(userId,
                                                                 instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                 startFrom,
                                                                 pageSize,
                                                                 requestBody.getEffectiveTime(),
                                                                 requestBody.getLimitResultsByStatus(),
                                                                 requestBody.getAsOfTime(),
                                                                 requestBody.getSequencingOrder(),
                                                                 requestBody.getSequencingProperty()));
            }
            else
            {
                response.setElements(handler.findTechnologyTypes(userId,
                                                                 instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                                 startFrom,
                                                                 pageSize,
                                                                 new Date(),
                                                                 null,
                                                                 null,
                                                                 SequencingOrder.CREATION_DATE_OLDEST,
                                                                 null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of deployed implementation type metadata elements linked to a particular open metadata type.
     *
     * @param serverName name of the service to route the request to
     * @param typeName the type name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody does the value start with the supplied string?
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */

    public TechnologyTypeSummaryListResponse getTechnologyTypesForOpenMetadataType(String             serverName,
                                                                                   String             typeName,
                                                                                   int                startFrom,
                                                                                   int                pageSize,
                                                                                   ResultsRequestBody requestBody)
    {
        final String methodName = "getTechnologyTypesForOpenMetadataType";
        final String parameterName = "typeName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TechnologyTypeSummaryListResponse response = new TechnologyTypeSummaryListResponse();
        AuditLog                          auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (typeName != null)
            {
                TechnologyTypeHandler handler = instanceHandler.getTechnologyTypeHandler(userId, serverName, methodName);

                if (requestBody != null)
                {
                    response.setElements(handler.getTechnologyTypesForOpenMetadataType(userId,
                                                                                       typeName,
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       requestBody.getEffectiveTime(),
                                                                                       requestBody.getLimitResultsByStatus(),
                                                                                       requestBody.getAsOfTime(),
                                                                                       requestBody.getSequencingOrder(),
                                                                                       requestBody.getSequencingProperty()));
                }
                else
                {
                    response.setElements(handler.getTechnologyTypesForOpenMetadataType(userId,
                                                                                       typeName,
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       new Date(),
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_OLDEST,
                                                                                       null));
                }
            }
            else
            {
                restExceptionHandler.handleMissingValue(parameterName, methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the requested deployed implementation type metadata element.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public TechnologyTypeReportResponse getTechnologyTypeDetail(String            serverName,
                                                                FilterRequestBody requestBody)
    {
        final String methodName = "getTechnologyTypeDetail";
        final String parameterName = "requestBody.filter";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TechnologyTypeReportResponse response = new TechnologyTypeReportResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getFilter() != null)
                {
                    TechnologyTypeHandler    handler = instanceHandler.getTechnologyTypeHandler(userId, serverName, methodName);

                    response.setElement(handler.getTechnologyTypeDetail(userId,
                                                                        requestBody.getFilter(),
                                                                        requestBody.getEffectiveTime(),
                                                                        requestBody.getLimitResultsByStatus(),
                                                                        requestBody.getAsOfTime(),
                                                                        requestBody.getSequencingOrder(),
                                                                        requestBody.getSequencingProperty()));
                }
                else
                {
                    restExceptionHandler.handleMissingValue(parameterName, methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the requested deployed implementation type metadata element and its subtypes.  A mermaid version if the hierarchy is also returned.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public TechnologyTypeHierarchyResponse getTechnologyTypeHierarchy(String            serverName,
                                                                      FilterRequestBody requestBody)
    {
        final String methodName = "getTechnologyTypeHierarchy";
        final String parameterName = "requestBody.filter";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TechnologyTypeHierarchyResponse response = new TechnologyTypeHierarchyResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getFilter() != null)
                {
                    TechnologyTypeHandler    handler = instanceHandler.getTechnologyTypeHandler(userId, serverName, methodName);

                    response.setElement(handler.getTechnologyTypeHierarchy(userId,
                                                                           requestBody.getFilter(),
                                                                           requestBody.getEffectiveTime(),
                                                                           requestBody.getLimitResultsByStatus(),
                                                                           requestBody.getAsOfTime(),
                                                                           requestBody.getSequencingOrder(),
                                                                           requestBody.getSequencingProperty()));

                    response.setMermaidString(handler.getTechnologyTypeHierarchyMermaidString(response.getElement()));
                }
                else
                {
                    restExceptionHandler.handleMissingValue(parameterName, methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the requested deployed implementation type metadata element. There are no wildcards allowed in the name.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param getTemplates boolean indicating whether templates or non-template elements should be returned.
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public TechnologyTypeElementListResponse getTechnologyTypeElements(String            serverName,
                                                                       int               startFrom,
                                                                       int               pageSize,
                                                                       boolean           getTemplates,
                                                                       FilterRequestBody requestBody)
    {
        final String methodName = "getTechnologyTypeElements";
        final String parameterName = "requestBody.filter";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TechnologyTypeElementListResponse response = new TechnologyTypeElementListResponse();
        AuditLog                          auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getFilter() != null)
                {
                    TechnologyTypeHandler technologyTypeHandler = instanceHandler.getTechnologyTypeHandler(userId, serverName, methodName);

                    response.setElements(technologyTypeHandler.getTechnologyTypeElements(userId,
                                                                                         requestBody.getFilter(),
                                                                                         getTemplates,
                                                                                         startFrom,
                                                                                         pageSize,
                                                                                         requestBody.getEffectiveTime(),
                                                                                         requestBody.getLimitResultsByStatus(),
                                                                                         requestBody.getAsOfTime(),
                                                                                         requestBody.getSequencingOrder(),
                                                                                         requestBody.getSequencingProperty()));
                }
                else
                {
                    restExceptionHandler.handleMissingValue(parameterName, methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /* =====================================================================================================================
     * Catalog templates make it easy to create new complex object such as assets.
     */

    /**
     * Create a new element from a template.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody information about the template
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createElementFromTemplate(String              serverName,
                                                  TemplateRequestBody requestBody)
    {
        final String methodName = "createElementFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataStoreClient openHandler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                response.setGUID(openHandler.createMetadataElementFromTemplate(userId,
                                                                               requestBody.getExternalSourceGUID(),
                                                                               requestBody.getExternalSourceName(),
                                                                               requestBody.getTypeName(),
                                                                               requestBody.getAnchorGUID(),
                                                                               requestBody.getIsOwnAnchor(),
                                                                               requestBody.getAnchorScopeGUID(),
                                                                               requestBody.getEffectiveFrom(),
                                                                               requestBody.getEffectiveTo(),
                                                                               requestBody.getTemplateGUID(),
                                                                               requestBody.getReplacementProperties(),
                                                                               requestBody.getPlaceholderPropertyValues(),
                                                                               requestBody.getParentGUID(),
                                                                               requestBody.getParentRelationshipTypeName(),
                                                                               requestBody.getParentRelationshipProperties(),
                                                                               requestBody.getParentAtEnd1(),
                                                                               requestBody.getForLineage(),
                                                                               requestBody.getForDuplicateProcessing(),
                                                                               requestBody.getEffectiveTime()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Create a new element from a template.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody information about the template
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse getElementFromTemplate(String              serverName,
                                               TemplateRequestBody requestBody)
    {
        final String methodName = "getElementFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataStoreClient openHandler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                response.setGUID(openHandler.getMetadataElementFromTemplate(userId,
                                                                            requestBody.getExternalSourceGUID(),
                                                                            requestBody.getExternalSourceName(),
                                                                            requestBody.getTypeName(),
                                                                            requestBody.getAnchorGUID(),
                                                                            requestBody.getIsOwnAnchor(),
                                                                            requestBody.getAnchorScopeGUID(),
                                                                            requestBody.getEffectiveFrom(),
                                                                            requestBody.getEffectiveTo(),
                                                                            requestBody.getTemplateGUID(),
                                                                            requestBody.getReplacementProperties(),
                                                                            requestBody.getPlaceholderPropertyValues(),
                                                                            requestBody.getParentGUID(),
                                                                            requestBody.getParentRelationshipTypeName(),
                                                                            requestBody.getParentRelationshipProperties(),
                                                                            requestBody.getParentAtEnd1(),
                                                                            requestBody.getForLineage(),
                                                                            requestBody.getForDuplicateProcessing(),
                                                                            requestBody.getEffectiveTime()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /* =====================================================================================================================
     * A catalog target links an element (typically an asset, to an integration connector for processing.
     */

    /**
     * Add a catalog target to an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    public GUIDResponse addCatalogTarget(String                  serverName,
                                         String                  integrationConnectorGUID,
                                         String                  metadataElementGUID,
                                         CatalogTargetProperties requestBody)
    {
        final String methodName = "addCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog                    auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GovernanceConfigurationClient handler = instanceHandler.getGovernanceConfigurationClient(userId, serverName, methodName);

                response.setGUID(handler.addCatalogTarget(userId,
                                                          integrationConnectorGUID,
                                                          metadataElementGUID,
                                                          requestBody));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Update a catalog target for an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param relationshipGUID unique identifier of the relationship.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    public VoidResponse updateCatalogTarget(String                  serverName,
                                            String                  relationshipGUID,
                                            CatalogTargetProperties requestBody)
    {
        final String methodName = "updateCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog                    auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GovernanceConfigurationClient handler = instanceHandler.getGovernanceConfigurationClient(userId, serverName, methodName);

                handler.updateCatalogTarget(userId, relationshipGUID, requestBody);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param relationshipGUID unique identifier of the relationship.
     *
     * @return details of the governance service and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public CatalogTargetResponse getCatalogTarget(String serverName,
                                                  String relationshipGUID)
    {
        final String methodName = "getCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CatalogTargetResponse response = new CatalogTargetResponse();
        AuditLog              auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceConfigurationClient handler = instanceHandler.getGovernanceConfigurationClient(userId, serverName, methodName);

            response.setElement(handler.getCatalogTarget(userId, relationshipGUID));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the details of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public CatalogTargetsResponse getCatalogTargets(String serverName,
                                                    String integrationConnectorGUID,
                                                    int    startFrom,
                                                    int    pageSize)
    {
        final String methodName = "getCatalogTargets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CatalogTargetsResponse response = new CatalogTargetsResponse();
        AuditLog               auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceConfigurationClient handler = instanceHandler.getGovernanceConfigurationClient(userId, serverName, methodName);

            response.setElements(handler.getCatalogTargets(userId, integrationConnectorGUID, startFrom, pageSize));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param relationshipGUID unique identifier of the integration connector.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeCatalogTarget(String          serverName,
                                            String          relationshipGUID,
                                            NullRequestBody requestBody)
    {
        final String methodName = "removeCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog                    auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceConfigurationClient handler = instanceHandler.getGovernanceConfigurationClient(userId, serverName, methodName);

            handler.removeCatalogTarget(userId, relationshipGUID);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /* =====================================================================================================================
     * A governance action type describes a template to call a single engine action.
     */

    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypesResponse findGovernanceActionTypes(String                  serverName,
                                                                   boolean                 startsWith,
                                                                   boolean                 endsWith,
                                                                   boolean                 ignoreCase,
                                                                   int                     startFrom,
                                                                   int                     pageSize,
                                                                   FilterRequestBody       requestBody)
    {
        final String methodName = "findGovernanceActionTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionTypesResponse response = new GovernanceActionTypesResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findGovernanceActionTypes(userId,
                                                                       instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                       startFrom,
                                                                       pageSize,
                                                                       requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElements(handler.findGovernanceActionTypes(userId,
                                                                       instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                                       startFrom,
                                                                       pageSize,
                                                                       new Date()));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypesResponse getGovernanceActionTypesByName(String            serverName,
                                                                        int               startFrom,
                                                                        int               pageSize,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionTypesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionTypesResponse response = new GovernanceActionTypesResponse();
        AuditLog                             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setElements(handler.getGovernanceActionTypesByName(userId,
                                                                            requestBody.getFilter(),
                                                                            startFrom,
                                                                            pageSize,
                                                                            requestBody.getEffectiveTime()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param governanceActionTypeGUID unique identifier of the governance action type
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypeResponse getGovernanceActionTypeByGUID(String serverName,
                                                                      String governanceActionTypeGUID)
    {
        final String methodName = "getGovernanceActionTypeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionTypeResponse response = new GovernanceActionTypeResponse();
        AuditLog                            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElement(handler.getGovernanceActionTypeByGUID(userId, governanceActionTypeGUID));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /* =====================================================================================================================
     * A governance action process describes a well-defined series of steps that gets something done.
     * The steps are defined using GovernanceActionProcessSteps.
     */

    /**
     * Retrieve the list of governance action process metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessElementsResponse findGovernanceActionProcesses(String                  serverName,
                                                                                 boolean                 startsWith,
                                                                                 boolean                 endsWith,
                                                                                 boolean                 ignoreCase,
                                                                                 int                     startFrom,
                                                                                 int                     pageSize,
                                                                                 FilterRequestBody       requestBody)
    {
        final String methodName = "findGovernanceActionProcesses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessElementsResponse response = new GovernanceActionProcessElementsResponse();
        AuditLog                                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findGovernanceActionProcesses(userId,
                                                                           instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                           startFrom,
                                                                           pageSize,
                                                                           requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElements(handler.findGovernanceActionProcesses(userId,
                                                                           instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                                           startFrom,
                                                                           pageSize,
                                                                           new Date()));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of governance action process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessElementsResponse getGovernanceActionProcessesByName(String            serverName,
                                                                                      int               startFrom,
                                                                                      int               pageSize,
                                                                                      FilterRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionProcessesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessElementsResponse response = new GovernanceActionProcessElementsResponse();
        AuditLog                                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setElements(handler.getGovernanceActionProcessesByName(userId,
                                                                                requestBody.getFilter(),
                                                                                startFrom,
                                                                                pageSize,
                                                                                requestBody.getEffectiveTime()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessElementResponse getGovernanceActionProcessByGUID(String serverName,
                                                                                   String processGUID)
    {
        final String methodName = "getGovernanceActionProcessByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessElementResponse response = new GovernanceActionProcessElementResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElement(handler.getGovernanceActionProcessByGUID(userId, processGUID));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier
     * along with the flow definition describing its implementation.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the requested metadata element
     * @param requestBody effectiveTime
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessGraphResponse getGovernanceActionProcessGraph(String                   serverName,
                                                                                String                   processGUID,
                                                                                ResultsRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionProcessGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessGraphResponse response = new GovernanceActionProcessGraphResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGovernanceActionProcessGraph(userId, processGUID, requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getGovernanceActionProcessGraph(userId, processGUID, new Date()));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * Engine Actions
     */

    /**
     * Request the status and properties of an executing engine action request.
     *
     * @param serverName     name of server instance to route request to
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return engine action properties and status or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementResponse getEngineAction(String serverName,
                                                       String engineActionGUID)
    {
        final String methodName = "getEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        EngineActionElementResponse response = new EngineActionElementResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElement(handler.getEngineAction(userId, engineActionGUID));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Request that an engine action is stopped.
     *
     * @param serverName     name of server instance to route request to
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return engine action properties and status or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public VoidResponse cancelEngineAction(String serverName,
                                           String engineActionGUID)
    {
        final String methodName = "cancelEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            handler.cancelEngineAction(userId, engineActionGUID);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the engine actions that are known to the server.
     *
     * @param serverName     name of server instance to route request to
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementsResponse getEngineActions(String serverName,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName = "getEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        EngineActionElementsResponse response = new EngineActionElementsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElements(handler.getEngineActions(userId,
                                                          startFrom,
                                                          pageSize));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the engine actions that are still in process.
     *
     * @param serverName     name of server instance to route request to
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementsResponse getActiveEngineActions(String serverName,
                                                               int    startFrom,
                                                               int    pageSize)
    {
        final String methodName = "getActiveEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        EngineActionElementsResponse response = new EngineActionElementsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElements(handler.getActiveEngineActions(userId, startFrom, pageSize));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of engine action metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EngineActionElementsResponse findEngineActions(String            serverName,
                                                          boolean           startsWith,
                                                          boolean           endsWith,
                                                          boolean           ignoreCase,
                                                          int               startFrom,
                                                          int               pageSize,
                                                          FilterRequestBody requestBody)
    {
        final String methodName = "findEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        EngineActionElementsResponse response = new EngineActionElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findEngineActions(userId,
                                                               instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                               startFrom,
                                                               pageSize));
            }
            else
            {
                response.setElements(handler.findEngineActions(userId,
                                                               instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                               startFrom,
                                                               pageSize));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of engine action  metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EngineActionElementsResponse getEngineActionsByName(String            serverName,
                                                               int               startFrom,
                                                               int               pageSize,
                                                               FilterRequestBody requestBody)
    {
        final String methodName = "getEngineActionsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        EngineActionElementsResponse response = new EngineActionElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setElements(handler.getEngineActionsByName(userId,
                                                                    requestBody.getFilter(),
                                                                    startFrom,
                                                                    pageSize));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /* =====================================================================================================================
     * Execute governance actions
     */

    /**
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param serverName     name of server instance to route request to
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestBody properties for the governance action and to pass to the governance action service
     *
     * @return unique identifier of the governance action or
     *  InvalidParameterException null qualified name
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse initiateEngineAction(String                          serverName,
                                             String                          governanceEngineName,
                                             InitiateEngineActionRequestBody requestBody)
    {
        final String methodName = "initiateEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setGUID(handler.initiateEngineAction(userId,
                                                              requestBody.getQualifiedName(),
                                                              requestBody.getDomainIdentifier(),
                                                              requestBody.getDisplayName(),
                                                              requestBody.getDescription(),
                                                              requestBody.getRequestSourceGUIDs(),
                                                              requestBody.getActionTargets(),
                                                              requestBody.getReceivedGuards(),
                                                              requestBody.getStartDate(),
                                                              governanceEngineName,
                                                              requestBody.getRequestType(),
                                                              requestBody.getRequestParameters(),
                                                              requestBody.getProcessName(),
                                                              requestBody.getRequestSourceName(),
                                                              requestBody.getOriginatorServiceName(),
                                                              requestBody.getOriginatorEngineName()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Using the named governance action type as a template, initiate an engine action.
     *
     * @param serverName     name of server instance to route request to
     * @param requestBody properties to initiate the new instance of the engine action
     *
     * @return unique identifier of the first governance action of the process or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse initiateGovernanceActionType(String                          serverName,
                                                     InitiateGovernanceActionTypeRequestBody requestBody)
    {
        final String methodName = "initiateGovernanceActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setGUID(handler.initiateGovernanceActionType(userId,
                                                                      requestBody.getGovernanceActionTypeQualifiedName(),
                                                                      requestBody.getRequestSourceGUIDs(),
                                                                      requestBody.getActionTargets(),
                                                                      requestBody.getStartDate(),
                                                                      requestBody.getRequestParameters(),
                                                                      requestBody.getOriginatorServiceName(),
                                                                      requestBody.getOriginatorEngineName()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param serverName     name of server instance to route request to
     * @param requestBody properties to initiate the new instance of the process
     *
     * @return unique identifier of the governance action process instance or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse initiateGovernanceActionProcess(String                             serverName,
                                                        InitiateGovernanceActionProcessRequestBody requestBody)
    {
        final String methodName = "initiateGovernanceActionProcess";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setGUID(handler.initiateGovernanceActionProcess(userId,
                                                                         requestBody.getProcessQualifiedName(),
                                                                         requestBody.getRequestSourceGUIDs(),
                                                                         requestBody.getActionTargets(),
                                                                         requestBody.getStartDate(),
                                                                         requestBody.getRequestParameters(),
                                                                         requestBody.getOriginatorServiceName(),
                                                                         requestBody.getOriginatorEngineName()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /* =====================================================================================================================
     * Work with external identifiers
     */

    /**
     * Add the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse addExternalIdentifier(String                               serverName,
                                              String                               openMetadataElementGUID,
                                              String                               openMetadataElementTypeName,
                                              boolean                              forLineage,
                                              boolean                              forDuplicateProcessing,
                                              UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName                      = "addExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                handler.addExternalIdentifier(userId,
                                              requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                              requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                              requestBody.getMetadataCorrelationProperties().getExternalScopeTypeName(),
                                              openMetadataElementGUID,
                                              openMetadataElementTypeName,
                                              requestBody.getMetadataCorrelationProperties(),
                                              requestBody.getEffectiveFrom(),
                                              requestBody.getEffectiveTo(),
                                              forLineage,
                                              forDuplicateProcessing,
                                              requestBody.getEffectiveTime());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse updateExternalIdentifier(String                               serverName,
                                                 String                               openMetadataElementGUID,
                                                 String                               openMetadataElementTypeName,
                                                 boolean                              forLineage,
                                                 boolean                              forDuplicateProcessing,
                                                 UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName                      = "updateExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                handler.updateExternalIdentifier(userId,
                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeTypeName(),
                                                 openMetadataElementGUID,
                                                 openMetadataElementTypeName,
                                                 requestBody.getMetadataCorrelationProperties(),
                                                 requestBody.getEffectiveFrom(),
                                                 requestBody.getEffectiveTo(),
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 requestBody.getEffectiveTime());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * That the external identifier matches the open metadata GUID.
     *
     * @param serverName name of the service to route the request to.
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public BooleanResponse validateExternalIdentifier(String                               serverName,
                                                      String                               openMetadataElementGUID,
                                                      String                               openMetadataElementTypeName,
                                                      boolean                              forLineage,
                                                      boolean                              forDuplicateProcessing,
                                                      UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName = "validateExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                if ((requestBody.getMetadataCorrelationProperties().getExternalIdentifier() != null) &&
                        (requestBody.getMetadataCorrelationProperties().getExternalScopeGUID() != null) &&
                        (requestBody.getMetadataCorrelationProperties().getExternalScopeName() != null))
                {
                    response.setFlag(handler.validateExternalIdentifier(userId,
                                                                        requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                                        requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                                        openMetadataElementGUID,
                                                                        openMetadataElementTypeName,
                                                                        requestBody.getMetadataCorrelationProperties().getExternalIdentifier(),
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        requestBody.getEffectiveTime()));
                }
                else
                {
                    response.setFlag(true);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not
     * affected.
     *
     * @param serverName name of the service to route the request to.
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse removeExternalIdentifier(String                               serverName,
                                                 String                               openMetadataElementGUID,
                                                 String                               openMetadataElementTypeName,
                                                 boolean                              forLineage,
                                                 boolean                              forDuplicateProcessing,
                                                 UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName = "removeExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                handler.removeExternalIdentifier(userId,
                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                 openMetadataElementGUID,
                                                 openMetadataElementTypeName,
                                                 requestBody.getMetadataCorrelationProperties().getExternalIdentifier(),
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 requestBody.getEffectiveTime());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the scope associated with a collection of external identifiers.  All associated external identifiers are removed too.
     * The linked open metadata elements are not affected.
     *
     * @param serverName name of the service to route the request to.
     * @param externalScopeGUID unique identifier (GUID) of the scope element in the open metadata ecosystem
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse removeExternalScope(String                   serverName,
                                            String                   externalScopeGUID,
                                            boolean                  forLineage,
                                            boolean                  forDuplicateProcessing,
                                            EffectiveTimeRequestBody requestBody)
    {
        final String methodName                      = "removeExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                handler.removeExternalScope(userId,
                                            externalScopeGUID,
                                            forLineage,
                                            forDuplicateProcessing,
                                            requestBody.getEffectiveTime());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param serverName name of the service to route the request to.
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param requestBody details of the external identifier and its scope
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public VoidResponse confirmSynchronization(String                        serverName,
                                               String                        openMetadataElementGUID,
                                               String                        openMetadataElementTypeName,
                                               MetadataCorrelationProperties requestBody)
    {
        final String methodName = "confirmSynchronization";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                handler.confirmSynchronization(userId,
                                               openMetadataElementGUID,
                                               openMetadataElementTypeName,
                                               requestBody.getExternalIdentifier(),
                                               requestBody.getExternalScopeGUID(),
                                               requestBody.getExternalScopeName());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the external identifiers for a particular metadata element.
     *
     * @param serverName name of the service to route the request to.
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody details of the external identifier and its scope
     *
     * @return list of correlation header elements, null if null or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public MetadataCorrelationHeadersResponse getExternalIdentifiers(String                        serverName,
                                                                     String                        openMetadataElementGUID,
                                                                     String                        openMetadataElementTypeName,
                                                                     int                           startFrom,
                                                                     int                           pageSize,
                                                                     boolean                       forLineage,
                                                                     boolean                       forDuplicateProcessing,
                                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getExternalIdentifiers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataCorrelationHeadersResponse response = new MetadataCorrelationHeadersResponse();
        AuditLog                           auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                response.setElementList(handler.getExternalIdentifiers(userId,
                                                                       requestBody.getExternalScopeGUID(),
                                                                       requestBody.getExternalScopeName(),
                                                                       openMetadataElementGUID,
                                                                       openMetadataElementTypeName,
                                                                       startFrom,
                                                                       pageSize,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       requestBody.getEffectiveTime()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }

        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the unique identifier of the external asset manager from its qualified name.
     * Typically, the qualified name comes from the integration connector configuration.
     *
     * @param serverName name of the service to route the request to.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody details of the external identifier
     *
     * @return list of linked elements, null if null or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    public ElementHeadersResponse getElementsForExternalIdentifier(String                               serverName,
                                                                   int                                  startFrom,
                                                                   int                                  pageSize,
                                                                   boolean                              forLineage,
                                                                   boolean                              forDuplicateProcessing,
                                                                   UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        final String methodName = "getElementsForExternalIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ElementHeadersResponse response = new ElementHeadersResponse();
        AuditLog               auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getMetadataCorrelationProperties() != null))
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                response.setElementHeaders(handler.getElementsForExternalIdentifier(userId,
                                                                                    requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                                                    requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                                                    requestBody.getMetadataCorrelationProperties().getExternalIdentifier(),
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    requestBody.getEffectiveTime()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }

        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
