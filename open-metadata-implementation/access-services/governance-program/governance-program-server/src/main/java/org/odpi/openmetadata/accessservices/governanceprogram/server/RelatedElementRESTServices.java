/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;

import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceRoleElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.AssignmentScopeProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ResourceListProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.StakeholderProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceDefinitionListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceRoleListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.RelatedElementListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.RelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.PersonRoleHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * RelatedElementRESTServices support requests about common Referenceable relationships.
 * It is the server side for the RelatedElementsManagementInterface.
 */
public class RelatedElementRESTServices
{
    private static final GovernanceProgramInstanceHandler  instanceHandler = new GovernanceProgramInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(RelatedElementRESTServices.class),
                                                                                        instanceHandler.getServiceName());


    /**
     * Create a "MoreInformation" relationship between an element that is descriptive and one that is providing the detail.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param detailGUID         unique identifier of the element that provides the detail
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupMoreInformation(String                  serverName,
                                             String                  userId,
                                             String                  elementGUID,
                                             String                  detailGUID,
                                             RelationshipRequestBody requestBody)
    {
        final String methodName               = "setupMoreInformation";
        final String elementGUIDParameterName = "elementGUID";
        final String detailGUIDParameterName  = "detailGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.addMoreInformation(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               elementGUID,
                                               elementGUIDParameterName,
                                               detailGUID,
                                               detailGUIDParameterName,
                                               requestBody.getProperties().getEffectiveFrom(),
                                               requestBody.getProperties().getEffectiveTo(),
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else
                {
                    handler.addMoreInformation(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               elementGUID,
                                               elementGUIDParameterName,
                                               detailGUID,
                                               detailGUIDParameterName,
                                               null,
                                               null,
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
     * Remove a "MoreInformation" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param detailGUID         unique identifier of the element that provides the detail
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearMoreInformation(String                    serverName,
                                             String                    userId,
                                             String                    elementGUID,
                                             String                    detailGUID,
                                             ExternalSourceRequestBody requestBody)
    {
        final String methodName               = "clearMoreInformation";
        final String elementGUIDParameterName = "elementGUID";
        final String detailGUIDParameterName  = "detailGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeMoreInformation(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              elementGUID,
                                              elementGUIDParameterName,
                                              detailGUID,
                                              detailGUIDParameterName,
                                              false,
                                              false,
                                              new Date(),
                                              methodName);
            }
            else
            {
                handler.removeMoreInformation(userId,
                                              null,
                                              null,
                                              elementGUID,
                                              elementGUIDParameterName,
                                              detailGUID,
                                              detailGUIDParameterName,
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
     * Retrieve the detail elements linked via a "MoreInformation" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is descriptive
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedElementListResponse getMoreInformation(String serverName,
                                                         String userId,
                                                         String elementGUID,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName        = "getMoreInformation";
        final String guidPropertyName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getMoreInformation(userId,
                                                               elementGUID,
                                                               guidPropertyName,
                                                               OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                               OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                               startFrom,
                                                               pageSize,
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the descriptive elements linked via a "MoreInformation" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param detailGUID         unique identifier of the element that provides the detail
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getDescriptiveElements(String serverName,
                                                              String userId,
                                                              String detailGUID,
                                                              int    startFrom,
                                                              int    pageSize)
    {
        final String methodName        = "getDescriptiveElements";
        final String guidPropertyName  = "detailGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getDescriptiveElements(userId,
                                                                   detailGUID,
                                                                   guidPropertyName,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                   startFrom,
                                                                   pageSize,
                                                                   false,
                                                                   false,
                                                                   new Date(),
                                                                   methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a "GovernedBy" relationship between an element and a governance definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID unique identifier of the element that is governed
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupGovernedBy(String                  serverName,
                                        String                  userId,
                                        String                  elementGUID,
                                        String                  governanceDefinitionGUID,
                                        RelationshipRequestBody requestBody)
    {
        final String methodName               = "setupGovernedBy";
        final String elementGUIDParameterName = "elementGUID";
        final String governanceDefinitionGUIDParameterName  = "governanceDefinitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.addGovernedBy(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          governanceDefinitionGUID,
                                          governanceDefinitionGUIDParameterName,
                                          elementGUID,
                                          elementGUIDParameterName,
                                          requestBody.getProperties().getEffectiveFrom(),
                                          requestBody.getProperties().getEffectiveTo(),
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
                }
                else
                {
                    handler.addGovernedBy(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          governanceDefinitionGUID,
                                          governanceDefinitionGUIDParameterName,
                                          elementGUID,
                                          elementGUIDParameterName,
                                          null,
                                          null,
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
     * Remove a "GovernedBy" relationship between two elements.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID unique identifier of the element that is governed
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearGovernedBy(String                    serverName,
                                        String                    userId,
                                        String                    elementGUID,
                                        String                    governanceDefinitionGUID,
                                        ExternalSourceRequestBody requestBody)
    {
        final String methodName               = "clearGovernedBy";
        final String elementGUIDParameterName = "elementGUID";
        final String governanceDefinitionGUIDParameterName  = "governanceDefinitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGovernedBy(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         governanceDefinitionGUID,
                                         governanceDefinitionGUIDParameterName,
                                         elementGUID,
                                         elementGUIDParameterName,
                                         false,
                                         false,
                                         new Date(),
                                         methodName);
            }
            else
            {
                handler.removeGovernedBy(userId,
                                         null,
                                         null,
                                         governanceDefinitionGUID,
                                         governanceDefinitionGUIDParameterName,
                                         elementGUID,
                                         elementGUIDParameterName,
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
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to an element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element that is governed
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceDefinitionListResponse getGovernanceDefinitionsForElement(String serverName,
                                                                               String userId,
                                                                               String elementGUID,
                                                                               int    startFrom,
                                                                               int    pageSize)
    {
        final String methodName        = "getGovernanceDefinitionsForElement";
        final String guidPropertyName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDefinitionListResponse response = new GovernanceDefinitionListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            response.setElements(handler.getGoverningDefinitions(userId,
                                                                 elementGUID,
                                                                 guidPropertyName,
                                                                 OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                 OpenMetadataAPIMapper.GOVERNANCE_DEFINITION_TYPE_NAME,
                                                                 startFrom,
                                                                 pageSize,
                                                                 false,
                                                                 false,
                                                                 new Date(),
                                                                 methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the governed elements linked via a "GovernedBy" relationship to a governance definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param governanceDefinitionGUID unique identifier of the element that provides the detail
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getGovernedElements(String serverName,
                                                           String userId,
                                                           String governanceDefinitionGUID,
                                                           int    startFrom,
                                                           int    pageSize)
    {
        final String methodName        = "getGovernedElements";
        final String guidPropertyName  = "governanceDefinitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getGovernedElements(userId,
                                                                governanceDefinitionGUID,
                                                                guidPropertyName,
                                                                OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                startFrom,
                                                                pageSize,
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Create an "GovernanceDefinitionScope" relationship between a governance definition and its scope element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param scopeGUID unique identifier of the scope
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupGovernanceDefinitionScope(String                  serverName,
                                                       String                  userId,
                                                       String                  governanceDefinitionGUID,
                                                       String                  scopeGUID,
                                                       RelationshipRequestBody requestBody)
    {
        final String methodName               = "setupGovernanceDefinitionScope";
        final String governanceDefinitionGUIDParameterName = "governanceDefinitionGUID";
        final String scopeGUIDParameterName  = "scopeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.addGovernanceDefinitionScope(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         governanceDefinitionGUID,
                                                         governanceDefinitionGUIDParameterName,
                                                         scopeGUID,
                                                         scopeGUIDParameterName,
                                                         requestBody.getProperties().getEffectiveFrom(),
                                                         requestBody.getProperties().getEffectiveTo(),
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName);
                }
                else
                {
                    handler.addGovernanceDefinitionScope(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         governanceDefinitionGUID,
                                                         governanceDefinitionGUIDParameterName,
                                                         scopeGUID,
                                                         scopeGUIDParameterName,
                                                         null,
                                                         null,
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
     * Remove an "GovernanceDefinitionScope" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param scopeGUID unique identifier of the scope
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearGovernanceDefinitionScope(String                    serverName,
                                                       String                    userId,
                                                       String                    governanceDefinitionGUID,
                                                       String                    scopeGUID,
                                                       ExternalSourceRequestBody requestBody)
    {
        final String methodName               = "clearGovernanceDefinitionScope";
        final String governanceDefinitionGUIDParameterName = "governanceDefinitionGUID";
        final String scopeGUIDParameterName  = "scopeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGovernanceDefinitionScope(userId,
                                                        requestBody.getExternalSourceGUID(),
                                                        requestBody.getExternalSourceName(),
                                                        governanceDefinitionGUID,
                                                        governanceDefinitionGUIDParameterName,
                                                        scopeGUID,
                                                        scopeGUIDParameterName,
                                                        false,
                                                        false,
                                                        new Date(),
                                                        methodName);
            }
            else
            {
                handler.removeGovernanceDefinitionScope(userId,
                                                        null,
                                                        null,
                                                        governanceDefinitionGUID,
                                                        governanceDefinitionGUIDParameterName,
                                                        scopeGUID,
                                                        scopeGUIDParameterName,
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
     * Retrieve the assigned scopes linked by the "GovernanceDefinitionScope" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedElementListResponse getGovernanceDefinitionScopes(String serverName,
                                                                    String userId,
                                                                    String governanceDefinitionGUID,
                                                                    int    startFrom,
                                                                    int    pageSize)
    {
        final String methodName        = "getGovernanceDefinitionScopes";
        final String guidPropertyName  = "governanceDefinitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getGovernanceDefinitionScope(userId,
                                                                         governanceDefinitionGUID,
                                                                         guidPropertyName,
                                                                         OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                         startFrom,
                                                                         pageSize,
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the governance definitions linked by the "GovernanceDefinitionScope" relationship to a scope element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param scopeGUID unique identifier of the scope
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  GovernanceDefinitionListResponse getScopedGovernanceDefinitions(String serverName,
                                                                            String userId,
                                                                            String scopeGUID,
                                                                            int    startFrom,
                                                                            int    pageSize)
    {
        final String methodName        = "getScopedGovernanceDefinitions";
        final String guidPropertyName  = "scopeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDefinitionListResponse response = new GovernanceDefinitionListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            response.setElements(handler.getScopedGovernanceDefinitions(userId,
                                                                        scopeGUID,
                                                                        guidPropertyName,
                                                                        OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                        OpenMetadataAPIMapper.GOVERNANCE_DEFINITION_TYPE_NAME,
                                                                        startFrom,
                                                                        pageSize,
                                                                        false,
                                                                        false,
                                                                        new Date(),
                                                                        methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Create a "GovernanceResponsibilityAssignment" relationship between a governance responsibility and a person role.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param governanceResponsibilityGUID unique identifier of the governance responsibility (type of governance definition)
     * @param personRoleGUID unique identifier of the person role element
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupGovernanceResponsibilityAssignment(String                  serverName,
                                                                String                  userId,
                                                                String                  governanceResponsibilityGUID,
                                                                String                  personRoleGUID,
                                                                RelationshipRequestBody requestBody)
    {
        final String methodName               = "setupGovernanceResponsibilityAssignment";
        final String governanceResponsibilityGUIDParameterName = "governanceResponsibilityGUID";
        final String personRoleGUIDParameterName  = "personRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.addGovernanceResponsibility(userId,
                                                        requestBody.getExternalSourceGUID(),
                                                        requestBody.getExternalSourceName(),
                                                        personRoleGUID,
                                                        personRoleGUIDParameterName,
                                                        governanceResponsibilityGUID,
                                                        governanceResponsibilityGUIDParameterName,
                                                        requestBody.getProperties().getEffectiveFrom(),
                                                        requestBody.getProperties().getEffectiveTo(),
                                                        false,
                                                        false,
                                                        new Date(),
                                                        methodName);
                }
                else
                {
                    handler.addGovernanceResponsibility(userId,
                                                        requestBody.getExternalSourceGUID(),
                                                        requestBody.getExternalSourceName(),
                                                        personRoleGUID,
                                                        personRoleGUIDParameterName,
                                                        governanceResponsibilityGUID,
                                                        governanceResponsibilityGUIDParameterName,
                                                        null,
                                                        null,
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
     * Remove a "GovernanceResponsibilityAssignment" relationship between a governance responsibility and a person role.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param governanceResponsibilityGUID unique identifier of the governance responsibility (type of governance definition)
     * @param personRoleGUID unique identifier of the person role element
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearGovernanceResponsibilityAssignment(String                    serverName,
                                                                String                    userId,
                                                                String                    governanceResponsibilityGUID,
                                                                String                    personRoleGUID,
                                                                ExternalSourceRequestBody requestBody)
    {
        final String methodName               = "clearGovernanceResponsibilityAssignment";
        final String governanceResponsibilityGUIDParameterName = "governanceResponsibilityGUID";
        final String personRoleGUIDParameterName  = "personRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGovernanceResponsibility(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       governanceResponsibilityGUID,
                                                       governanceResponsibilityGUIDParameterName,
                                                       personRoleGUID,
                                                       personRoleGUIDParameterName,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName);
            }
            else
            {
                handler.removeGovernanceResponsibility(userId,
                                                       null,
                                                       null,
                                                       governanceResponsibilityGUID,
                                                       governanceResponsibilityGUIDParameterName,
                                                       personRoleGUID,
                                                       personRoleGUIDParameterName,
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
     * Retrieve the person roles linked via a "GovernanceResponsibilityAssignment" relationship to a governance responsibility.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param governanceResponsibilityGUID unique identifier of the governance responsibility (type of governance definition)
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceRoleListResponse getResponsibleRoles(String serverName,
                                                      String userId,
                                                      String governanceResponsibilityGUID,
                                                      int    startFrom,
                                                      int    pageSize)
    {
        final String methodName        = "getResponsibleRoles";
        final String guidPropertyName  = "governanceResponsibilityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceRoleListResponse response = new GovernanceRoleListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

            response.setElements(handler.getRolesWithGovernanceResponsibility(userId,
                                                                              governanceResponsibilityGUID,
                                                                              guidPropertyName,
                                                                              startFrom,
                                                                              pageSize,
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the governance responsibilities linked via a "GovernanceResponsibilityAssignment" relationship to a person role.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param personRoleGUID unique identifier of the person role element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  GovernanceDefinitionListResponse getRoleResponsibilities(String serverName,
                                                                     String userId,
                                                                     String personRoleGUID,
                                                                     int    startFrom,
                                                                     int    pageSize)
    {
        final String methodName        = "getRoleResponsibilities";
        final String guidPropertyName  = "personRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDefinitionListResponse response = new GovernanceDefinitionListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            response.setElements(handler.getGovernanceResponsibilitiesForRole(userId,
                                                                              personRoleGUID,
                                                                              guidPropertyName,
                                                                              startFrom,
                                                                              pageSize,
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a "Stakeholder" relationship between an element and its stakeholder.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param stakeholderGUID    unique identifier of the stakeholder
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupStakeholder(String                  serverName,
                                         String                  userId,
                                         String                  elementGUID,
                                         String                  stakeholderGUID,
                                         RelationshipRequestBody requestBody)
    {
        final String methodName                   = "setupStakeholder";
        final String elementGUIDParameterName     = "elementGUID";
        final String stakeholderGUIDParameterName = "stakeholderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof StakeholderProperties)
                {
                    StakeholderProperties properties = (StakeholderProperties) requestBody.getProperties();

                    handler.addStakeholder(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           elementGUID,
                                           elementGUIDParameterName,
                                           stakeholderGUID,
                                           stakeholderGUIDParameterName,
                                           properties.getStakeholderRole(),
                                           properties.getEffectiveFrom(),
                                           properties.getEffectiveTo(),
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addStakeholder(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           elementGUID,
                                           elementGUIDParameterName,
                                           stakeholderGUID,
                                           stakeholderGUIDParameterName,
                                           null,
                                           null,
                                           null,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(StakeholderProperties.class.getName(), methodName);
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
     * Remove a "Stakeholder" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param stakeholderGUID    unique identifier of the stakeholder
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearStakeholder(String                    serverName,
                                         String                    userId,
                                         String                    elementGUID,
                                         String                    stakeholderGUID,
                                         ExternalSourceRequestBody requestBody)
    {
        final String methodName                   = "clearStakeholder";
        final String elementGUIDParameterName     = "elementGUID";
        final String stakeholderGUIDParameterName = "stakeholderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeStakeholder(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          elementGUID,
                                          elementGUIDParameterName,
                                          stakeholderGUID,
                                          stakeholderGUIDParameterName,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
            }
            else
            {
                handler.removeStakeholder(userId,
                                          null,
                                          null,
                                          elementGUID,
                                          elementGUIDParameterName,
                                          stakeholderGUID,
                                          stakeholderGUIDParameterName,
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
     * Retrieve the stakeholder elements linked via the "Stakeholder"  relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId             calling user
     * @param elementGUID        unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getStakeholders(String serverName,
                                                       String userId,
                                                       String elementGUID,
                                                       int    startFrom,
                                                       int    pageSize)
    {
        final String methodName        = "getStakeholders";
        final String guidPropertyName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getStakeholders(userId,
                                                            elementGUID,
                                                            guidPropertyName,
                                                            OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                            OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                            startFrom,
                                                            pageSize,
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the elements commissioned by a stakeholder, linked via the "Stakeholder"  relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param stakeholderGUID unique identifier of the stakeholder
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getStakeholderCommissionedElements(String serverName,
                                                                          String userId,
                                                                          String stakeholderGUID,
                                                                          int    startFrom,
                                                                          int    pageSize)
    {
        final String methodName        = "getStakeholderCommissionedElements";
        final String guidPropertyName  = "stakeholderGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getCommissionedByStakeholder(userId,
                                                                         stakeholderGUID,
                                                                         guidPropertyName,
                                                                         OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                         OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                         startFrom,
                                                                         pageSize,
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create an "AssignmentScope" relationship between an element and its scope.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param scopeGUID unique identifier of the scope
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupAssignmentScope(String                  serverName,
                                             String                  userId,
                                             String                  elementGUID,
                                             String                  scopeGUID,
                                             RelationshipRequestBody requestBody)
    {
        final String methodName               = "setupAssignmentScope";
        final String elementGUIDParameterName = "elementGUID";
        final String scopeGUIDParameterName   = "scopeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AssignmentScopeProperties)
                {
                    AssignmentScopeProperties properties = (AssignmentScopeProperties) requestBody.getProperties();

                    handler.addAssignmentScope(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               elementGUID,
                                               elementGUIDParameterName,
                                               scopeGUID,
                                               scopeGUIDParameterName,
                                               properties.getAssignmentType(),
                                               properties.getDescription(),
                                               properties.getEffectiveFrom(),
                                               properties.getEffectiveTo(),
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addAssignmentScope(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               elementGUID,
                                               elementGUIDParameterName,
                                               scopeGUID,
                                               scopeGUIDParameterName,
                                               null,
                                               null,
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssignmentScopeProperties.class.getName(), methodName);
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
     * Remove an "AssignmentScope" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param scopeGUID unique identifier of the scope
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAssignmentScope(String                    serverName,
                                             String                    userId,
                                             String                    elementGUID,
                                             String                    scopeGUID,
                                             ExternalSourceRequestBody requestBody)
    {
        final String methodName               = "clearAssignmentScope";
        final String elementGUIDParameterName = "elementGUID";
        final String scopeGUIDParameterName   = "scopeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeAssignmentScope(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              elementGUID,
                                              elementGUIDParameterName,
                                              scopeGUID,
                                              scopeGUIDParameterName,
                                              false,
                                              false,
                                              new Date(),
                                              methodName);
            }
            else
            {
                handler.removeAssignmentScope(userId,
                                              null,
                                              null,
                                              elementGUID,
                                              elementGUIDParameterName,
                                              scopeGUID,
                                              scopeGUIDParameterName,
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
     * Retrieve the assigned scopes linked by the "AssignmentScope" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getAssignedScopes(String serverName,
                                                         String userId,
                                                         String elementGUID,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName        = "getAssignedScopes";
        final String guidPropertyName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getAssignmentScope(userId,
                                                               elementGUID,
                                                               guidPropertyName,
                                                               OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                               OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                               startFrom,
                                                               pageSize,
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the assigned actors linked by the "AssignmentScope" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param scopeGUID unique identifier of the scope
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getAssignedActors(String serverName,
                                                         String userId,
                                                         String scopeGUID,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName        = "getAssignedActors";
        final String guidPropertyName  = "scopeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getAssignedActors(userId,
                                                              scopeGUID,
                                                              guidPropertyName,
                                                              OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                              OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                              startFrom,
                                                              pageSize,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a "ResourceList" relationship between a consuming element and an element that represents resources.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param resourceGUID unique identifier of the resource
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupResource(String                  serverName,
                                      String                  userId,
                                      String                  elementGUID,
                                      String                  resourceGUID,
                                      RelationshipRequestBody requestBody)
    {
        final String methodName                = "setupResource";
        final String elementGUIDParameterName  = "elementGUID";
        final String resourceGUIDParameterName = "resourceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ResourceListProperties)
                {
                    ResourceListProperties properties = (ResourceListProperties) requestBody.getProperties();

                    handler.saveResourceListMember(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   elementGUID,
                                                   elementGUIDParameterName,
                                                   resourceGUID,
                                                   resourceGUIDParameterName,
                                                   properties.getResourceUse(),
                                                   properties.getWatchResource(),
                                                   properties.getEffectiveFrom(),
                                                   properties.getEffectiveTo(),
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.saveResourceListMember(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   elementGUID,
                                                   elementGUIDParameterName,
                                                   resourceGUID,
                                                   resourceGUIDParameterName,
                                                   null,
                                                   false,
                                                   null,
                                                   null,
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ResourceListProperties.class.getName(), methodName);
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
     * Remove a "ResourceList" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param resourceGUID unique identifier of the resource
     * @param requestBody external source identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearResource(String                    serverName,
                                      String                    userId,
                                      String                    elementGUID,
                                      String                    resourceGUID,
                                      ExternalSourceRequestBody requestBody)
    {
        final String methodName                = "clearResource";
        final String elementGUIDParameterName  = "elementGUID";
        final String resourceGUIDParameterName = "resourceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeResourceListMember(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 elementGUID,
                                                 elementGUIDParameterName,
                                                 resourceGUID,
                                                 resourceGUIDParameterName,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
            }
            else
            {
                handler.removeResourceListMember(userId,
                                                 null,
                                                 null,
                                                 elementGUID,
                                                 elementGUIDParameterName,
                                                 resourceGUID,
                                                 resourceGUIDParameterName,
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
     * Retrieve the list of resources assigned to an element via the "ResourceList" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getResourceList(String serverName,
                                                       String userId,
                                                       String elementGUID,
                                                       int    startFrom,
                                                       int    pageSize)
    {
        final String methodName        = "getResourceList";
        final String guidPropertyName  = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getResourceList(userId,
                                                            elementGUID,
                                                            guidPropertyName,
                                                            OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                            startFrom,
                                                            pageSize,
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of elements assigned to a resource via the "ResourceList" relationship between two referenceables.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param resourceGUID unique identifier of the resource
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  RelatedElementListResponse getSupportedByResource(String serverName,
                                                              String userId,
                                                              String resourceGUID,
                                                              int    startFrom,
                                                              int    pageSize)
    {
        final String methodName        = "getSupportedByResource";
        final String guidPropertyName  = "resourceGUID";


        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementListResponse response = new RelatedElementListResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElementList(handler.getSupportedByResource(userId,
                                                                   resourceGUID,
                                                                   guidPropertyName,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                   startFrom,
                                                                   pageSize,
                                                                   false,
                                                                   false,
                                                                   new Date(),
                                                                   methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
