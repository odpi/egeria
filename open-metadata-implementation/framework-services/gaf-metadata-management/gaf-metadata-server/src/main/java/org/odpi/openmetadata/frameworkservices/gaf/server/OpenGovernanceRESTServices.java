/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.EngineActionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ProcessStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworkservices.gaf.handlers.MetadataElementHandler;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The OpenGovernanceRESTServices provides the server-side implementation of the services used by the governance
 * engine as it is managing requests to execute open governance services in the governance server.
 * These services align with the interface definitions from the Governance Action Framework (GAF).
 */
public class OpenGovernanceRESTServices
{
    private final static GAFMetadataManagementInstanceHandler instanceHandler = new GAFMetadataManagementInstanceHandler();

    private final        RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private final static RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(OpenGovernanceRESTServices.class),
                                                                                        instanceHandler.getServiceName());

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Default constructor
     */
    public OpenGovernanceRESTServices()
    {
    }


    /* =====================================================================================================================
     * A governance action type describes a template to call a single engine action.
     */

    /**
     * Create a new metadata element to represent a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody properties about the process to store
     *
     * @return unique identifier of the new governance action type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGovernanceActionType(String                         serverName,
                                                   String                         serviceURLMarker,
                                                   String                         userId,
                                                   GovernanceActionTypeProperties requestBody)
    {
        final String methodName = "createGovernanceActionType";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceActionTypeHandler<GovernanceActionTypeElement>
                        handler = instanceHandler.getGovernanceActionTypeHandler(userId,
                                                                                 serverName,
                                                                                 methodName);

                response.setGUID(handler.createGovernanceActionType(userId,
                                                                    requestBody.getQualifiedName(),
                                                                    requestBody.getDomainIdentifier(),
                                                                    requestBody.getDisplayName(),
                                                                    requestBody.getDescription(),
                                                                    requestBody.getAdditionalProperties(),
                                                                    requestBody.getGovernanceEngineGUID(),
                                                                    requestBody.getRequestType(),
                                                                    requestBody.getFixedRequestParameters(),
                                                                    requestBody.getWaitTime(),
                                                                    null,
                                                                    null,
                                                                    false,
                                                                    false,
                                                                    instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                    new Date(),
                                                                    methodName));
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
     * Update the metadata element representing a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGovernanceActionType(String                                serverName,
                                                   String                                serviceURLMarker,
                                                   String                                userId,
                                                   String                                governanceActionTypeGUID,
                                                   UpdateGovernanceActionTypeRequestBody requestBody)
    {
        final String methodName = "updateGovernanceActionType";
        final String propertiesParameterName = "requestBody.getProperties";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceActionTypeHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionTypeHandler(userId,
                                                                                                                                  serverName,
                                                                                                                                  methodName);

                GovernanceActionTypeProperties properties = requestBody.getProperties();

                invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

                handler.updateGovernanceActionType(userId,
                                                   governanceActionTypeGUID,
                                                   requestBody.getMergeUpdate(),
                                                   properties.getQualifiedName(),
                                                   properties.getDomainIdentifier(),
                                                   properties.getDisplayName(),
                                                   properties.getDescription(),
                                                   properties.getAdditionalProperties(),
                                                   properties.getGovernanceEngineGUID(),
                                                   properties.getRequestType(),
                                                   properties.getFixedRequestParameters(),
                                                   properties.getWaitTime(),
                                                   null,
                                                   null,
                                                   false,
                                                   false,
                                                   instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Remove the metadata element representing a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeGovernanceActionType(String          serverName,
                                                   String          serviceURLMarker,
                                                   String          userId,
                                                   String          governanceActionTypeGUID,
                                                   NullRequestBody requestBody)
    {
        final String methodName = "removeGovernanceActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceActionTypeHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionTypeHandler(userId,
                                                                                                                              serverName,
                                                                                                                              methodName);

            handler.removeGovernanceActionType(userId,
                                                      governanceActionTypeGUID,
                                                      false,
                                                      false,
                                                      instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Retrieve the list of governance action type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
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
                                                                   String                  serviceURLMarker,
                                                                   String                  userId,
                                                                   int                     startFrom,
                                                                   int                     pageSize,
                                                                   SearchStringRequestBody requestBody)
    {
        final String methodName = "findGovernanceActionTypes";

        String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionTypesResponse response = new GovernanceActionTypesResponse();
        AuditLog                             auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                List<String> supportedZones = instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName);

                GovernanceActionTypeHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionTypeHandler(userId,
                                                                                                                                  serverName,
                                                                                                                                  methodName);

                if (requestBody.getSearchStringParameterName() != null)
                {
                    searchStringParameterName = requestBody.getSearchStringParameterName();
                }

                List<GovernanceActionTypeElement> governanceActionTypeElements = handler.findGovernanceActionTypes(userId,
                                                                                                                   requestBody.getSearchString(),
                                                                                                                   searchStringParameterName,
                                                                                                                   startFrom,
                                                                                                                   pageSize,
                                                                                                                   false,
                                                                                                                   false,
                                                                                                                   supportedZones,
                                                                                                                   new Date(),
                                                                                                                   methodName);

                response.setElements(addGovernanceActionTypeSpecification(userId, handler, supportedZones, governanceActionTypeElements));
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
     * Add the specification to each of the elements returned.
     *
     * @param userId calling user
     * @param handler generic handler
     * @param supportedZones supported zones
     * @param elements retrieved elements.
     * @return elements plus specifications
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    private List<GovernanceActionTypeElement> addGovernanceActionTypeSpecification
                                                               (String                                                     userId,
                                                               OpenMetadataAPIGenericHandler<GovernanceActionTypeElement> handler,
                                                               List<String>                                               supportedZones,
                                                               List<GovernanceActionTypeElement>                          elements) throws InvalidParameterException,
                                                                                                                                           PropertyServerException,
                                                                                                                                           UserNotAuthorizedException
    {
        if (elements != null)
        {
            final String elementGUIDParameterName = "governanceActionTypeGUID";

            List<GovernanceActionTypeElement> results = new ArrayList<>();

            for (GovernanceActionTypeElement element : elements)
            {
                if (element != null)
                {
                    element.setSpecification(handler.getSpecification(userId,
                                                                      element.getElementHeader().getGUID(),
                                                                      elementGUIDParameterName,
                                                                      OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                                      supportedZones));

                    results.add(element);
                }
                else
                {
                    results.add(null);
                }
            }

            return results;
        }

        return null;
    }



    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypesResponse getGovernanceActionTypesByName(String          serverName,
                                                                        String          serviceURLMarker,
                                                                        String          userId,
                                                                        int             startFrom,
                                                                        int             pageSize,
                                                                        NameRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionTypesByName";

        String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionTypesResponse response = new GovernanceActionTypesResponse();
        AuditLog                             auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                List<String> supportedZones = instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName);
                GovernanceActionTypeHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionTypeHandler(userId,
                                                                                                                                  serverName,
                                                                                                                                  methodName);

                if (requestBody.getNameParameterName() != null)
                {
                    nameParameterName = requestBody.getNameParameterName();
                }

                List<GovernanceActionTypeElement> governanceActionTypeElements = handler.getGovernanceActionTypesByName(userId,
                                                                                                                        requestBody.getName(),
                                                                                                                        nameParameterName,
                                                                                                                        startFrom,
                                                                                                                        pageSize,
                                                                                                                        supportedZones,
                                                                                                                        null,
                                                                                                                        methodName);

                response.setElements(addGovernanceActionTypeSpecification(userId, handler, supportedZones, governanceActionTypeElements));
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
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the governance action type
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypeResponse getGovernanceActionTypeByGUID(String serverName,
                                                                      String serviceURLMarker,
                                                                      String userId,
                                                                      String governanceActionTypeGUID)
    {
        final String methodName = "getGovernanceActionTypeByGUID";
        final String elementGUIDParameterName = "governanceActionTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionTypeResponse response = new GovernanceActionTypeResponse();
        AuditLog                            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            List<String> supportedZones = instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName);
            GovernanceActionTypeHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionTypeHandler(userId,
                                                                                                                              serverName,
                                                                                                                              methodName);

            GovernanceActionTypeElement element = handler.getGovernanceActionTypeByGUID(userId,
                                                                                        governanceActionTypeGUID,
                                                                                        supportedZones,
                                                                                        null,
                                                                                        methodName);

            element.setSpecification(handler.getSpecification(userId,
                                                              element.getElementHeader().getGUID(),
                                                              elementGUIDParameterName,
                                                              OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                                              supportedZones));

            response.setElement(element);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /* =====================================================================================================================
     * A governance action process describes a well-defined series of steps that gets something done.
     * The steps are defined using GovernanceActionProcessSteps.
     */

    /**
     * Create a new metadata element to represent a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody properties about the process to store and status value for the new process (default = ACTIVE)
     *
     * @return unique identifier of the new process or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public GUIDResponse createGovernanceActionProcess(String                                serverName,
                                                      String                                serviceURLMarker,
                                                      String                                userId,
                                                      NewGovernanceActionProcessRequestBody requestBody)
    {
        final String  methodName = "createGovernanceActionProcess";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);

                GovernanceActionProcessProperties processProperties = requestBody.getProperties();

                Map<String, Object> extendedProperties = new HashMap<>();
                extendedProperties.put(OpenMetadataProperty.FORMULA.name, processProperties.getFormula());
                extendedProperties.put(OpenMetadataProperty.FORMULA_TYPE.name, processProperties.getFormulaType());
                extendedProperties.put(OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME, processProperties.getDomainIdentifier());

                Date effectiveTime = new Date();

                response.setGUID(handler.createAssetInRepository(userId,
                                                                 null,
                                                                 null,
                                                                 processProperties.getQualifiedName(),
                                                                 processProperties.getDisplayName(),
                                                                 processProperties.getVersionIdentifier(),
                                                                 processProperties.getDescription(),
                                                                 null,
                                                                 processProperties.getAdditionalProperties(),
                                                                 OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                 extendedProperties,
                                                                 this.getProcessStatus(requestBody.getProcessStatus()),
                                                                 null,
                                                                 null,
                                                                 effectiveTime,
                                                                 methodName));
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
     * Translate the Process Status into a Repository Services' InstanceStatus.
     *
     * @param processStatus value from the caller
     * @return InstanceStatus enum
     */
    private InstanceStatus getProcessStatus(ProcessStatus processStatus)
    {
        if (processStatus != null)
        {
            return switch (processStatus)
                           {
                               case UNKNOWN -> InstanceStatus.UNKNOWN;
                               case DRAFT -> InstanceStatus.DRAFT;
                               case PROPOSED -> InstanceStatus.PROPOSED;
                               case APPROVED -> InstanceStatus.APPROVED;
                               case ACTIVE -> InstanceStatus.ACTIVE;
                           };
        }

        return InstanceStatus.ACTIVE;
    }


    /**
     * Update the metadata element representing a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGovernanceActionProcess(String                                   serverName,
                                                      String                                   serviceURLMarker,
                                                      String                                   userId,
                                                      String                                   processGUID,
                                                      UpdateGovernanceActionProcessRequestBody requestBody)
    {
        final String methodName = "updateGovernanceActionProcess";
        final String processGUIDParameterName = "processGUID";
        final String newStatusParameterName = "requestBody.getProcessStatus";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);

                GovernanceActionProcessProperties processProperties = requestBody.getProperties();

                Map<String, Object> extendedProperties = new HashMap<>();
                extendedProperties.put(OpenMetadataProperty.FORMULA.name, processProperties.getFormula());
                extendedProperties.put(OpenMetadataProperty.FORMULA_TYPE.name, processProperties.getFormulaType());
                extendedProperties.put(OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME, processProperties.getDomainIdentifier());

                Date effectiveTime = new Date();

                handler.updateAsset(userId,
                                    null,
                                    null,
                                    processGUID,
                                    processGUIDParameterName,
                                    processProperties.getQualifiedName(),
                                    processProperties.getVersionIdentifier(),
                                    processProperties.getDisplayName(),
                                    processProperties.getDisplayName(),
                                    processProperties.getDescription(),
                                    null,
                                    processProperties.getAdditionalProperties(),
                                    OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_GUID,
                                    OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                    instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                    extendedProperties,
                                    null,
                                    null,
                                    requestBody.getMergeUpdate(),
                                    false,
                                    false,
                                    effectiveTime,
                                    methodName);

                if (requestBody.getProcessStatus() != null)
                {
                    handler.updateBeanStatusInRepository(userId,
                                                         null,
                                                         null,
                                                         processGUID,
                                                         processGUIDParameterName,
                                                         OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_GUID,
                                                         OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                         false,
                                                         false,
                                                         instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                         this.getProcessStatus(requestBody.getProcessStatus()),
                                                         newStatusParameterName,
                                                         effectiveTime,
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
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to publish
     * @param requestBody null request body
     *
     * @return
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse publishGovernanceActionProcess(String          serverName,
                                                       String          serviceURLMarker,
                                                       String          userId,
                                                       String          processGUID,
                                                       NullRequestBody requestBody)
    {
        final String methodName = "publishGovernanceActionProcess";
        final String processGUIDParameterName = "processGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);

            handler.updateAssetZones(userId,
                                     processGUID,
                                     processGUIDParameterName,
                                     instanceHandler.getPublishZones(userId, serverName, serviceURLMarker, methodName),
                                     true,
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
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the process is first created).
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to withdraw
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawGovernanceActionProcess(String          serverName,
                                                        String          serviceURLMarker,
                                                        String          userId,
                                                        String          processGUID,
                                                        NullRequestBody requestBody)
    {
        final String methodName = "withdrawGovernanceActionProcess";
        final String processGUIDParameterName = "processGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);

            handler.updateAssetZones(userId,
                                     processGUID,
                                     processGUIDParameterName,
                                     instanceHandler.getDefaultZones(userId, serverName, serviceURLMarker, methodName),
                                     true,
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
     * Remove the metadata element representing a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeGovernanceActionProcess(String          serverName,
                                                      String          serviceURLMarker,
                                                      String          userId,
                                                      String          processGUID,
                                                      NullRequestBody requestBody)
    {
        final String methodName = "removeGovernanceActionProcess";
        final String processGUIDParameterName = "processGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);

            handler.deleteBeanInRepository(userId,
                                           null,
                                           null,
                                           processGUID,
                                           processGUIDParameterName,
                                           OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_GUID,
                                           OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                           null,
                                           null,
                                           false,
                                           false,
                                           instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Retrieve the list of governance action process metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
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
                                                                                 String                  serviceURLMarker,
                                                                                 String                  userId,
                                                                                 int                     startFrom,
                                                                                 int                     pageSize,
                                                                                 SearchStringRequestBody requestBody)
    {
        final String methodName = "findGovernanceActionProcesses";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionProcessElementsResponse response = new GovernanceActionProcessElementsResponse();
        AuditLog                                auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                List<String> supportedZones = instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName);
                AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId,
                                                                                                                         serverName,
                                                                                                                         methodName);

                List<GovernanceActionProcessElement> results = handler.findBeans(userId,
                                                                                 requestBody.getSearchString(),
                                                                                 searchStringParameterName,
                                                                                 OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_GUID,
                                                                                 OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                                 false,
                                                                                 false,
                                                                                 supportedZones,
                                                                                 null,
                                                                                 startFrom,
                                                                                 pageSize,
                                                                                 new Date(),
                                                                                 methodName);

                response.setElements(addGovernanceActionProcessSpecification(userId, handler, supportedZones, results));
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
     * Add the specification to each of the elements returned.
     *
     * @param userId calling user
     * @param handler generic handler
     * @param supportedZones supported zones
     * @param elements retrieved elements.
     * @return elements plus specifications
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    private List<GovernanceActionProcessElement> addGovernanceActionProcessSpecification
                                                                 (String                                                        userId,
                                                                  OpenMetadataAPIGenericHandler<GovernanceActionProcessElement> handler,
                                                                  List<String>                                                  supportedZones,
                                                                  List<GovernanceActionProcessElement>                          elements) throws InvalidParameterException,
                                                                                                                                                 PropertyServerException,
                                                                                                                                                 UserNotAuthorizedException
    {
        if (elements != null)
        {
            final String elementGUIDParameterName = "governanceActionProcessGUID";

            List<GovernanceActionProcessElement> results = new ArrayList<>();

            for (GovernanceActionProcessElement element : elements)
            {
                if (element != null)
                {
                    element.setSpecification(handler.getSpecification(userId,
                                                                      element.getElementHeader().getGUID(),
                                                                      elementGUIDParameterName,
                                                                      OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                      supportedZones));

                    results.add(element);
                }
                else
                {
                    results.add(null);
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Retrieve the list of governance action process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessElementsResponse getGovernanceActionProcessesByName(String          serverName,
                                                                                      String          serviceURLMarker,
                                                                                      String          userId,
                                                                                      int             startFrom,
                                                                                      int             pageSize,
                                                                                      NameRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionProcessesByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionProcessElementsResponse response = new GovernanceActionProcessElementsResponse();
        AuditLog                                auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                List<String> supportedZones = instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName);
                AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId,
                                                                                                                         serverName,
                                                                                                                         methodName);

                List<GovernanceActionProcessElement> results = handler.findAssetsByName(userId,
                                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_GUID,
                                                                                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                                        requestBody.getName(),
                                                                                        nameParameterName,
                                                                                        supportedZones,
                                                                                        startFrom,
                                                                                        pageSize,
                                                                                        false,
                                                                                        false,
                                                                                        requestBody.getEffectiveTime(),
                                                                                        methodName);

                response.setElements(addGovernanceActionProcessSpecification(userId, handler, supportedZones, results));
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
     * Retrieve the governance action process metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessElementResponse getGovernanceActionProcessByGUID(String serverName,
                                                                                   String serviceURLMarker,
                                                                                   String userId,
                                                                                   String processGUID)
    {
        final String methodName = "getGovernanceActionProcessByGUID";
        final String processGUIDParameterName = "processGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionProcessElementResponse response = new GovernanceActionProcessElementResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            List<String> supportedZones = instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName);
            AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);

            GovernanceActionProcessElement element = handler.getBeanFromRepository(userId,
                                                                                   processGUID,
                                                                                   processGUIDParameterName,
                                                                                   OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                                   false,
                                                                                   false,
                                                                                   supportedZones,
                                                                                   new Date(),
                                                                                   methodName);

            element.setSpecification(handler.getSpecification(userId,
                                                              element.getElementHeader().getGUID(),
                                                              processGUIDParameterName,
                                                              OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                              supportedZones));

            response.setElement(element);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier
     * along with the flow definition describing its implementation.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessGraphResponse getGovernanceActionProcessGraph(String                   serverName,
                                                                                String                   serviceURLMarker,
                                                                                String                   userId,
                                                                                String                   processGUID,
                                                                                EffectiveTimeRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionProcessGraph";
        final String processGUIDParameterName = "processGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessGraphResponse response = new GovernanceActionProcessGraphResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<GovernanceActionProcessElement> processHandler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);
            EngineActionHandler<EngineActionElement> engineActionHandler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);
            GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement>
                    handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                    serverName,
                                                                                    methodName);

            GovernanceActionProcessGraph governanceActionProcessGraph = new GovernanceActionProcessGraph();

            if (requestBody != null)
            {
                governanceActionProcessGraph.setGovernanceActionProcess(processHandler.getBeanFromRepository(userId,
                                                                                                             processGUID,
                                                                                                             processGUIDParameterName,
                                                                                                             OpenMetadataType.PROCESS.typeName,
                                                                                                             false,
                                                                                                             false,
                                                                                                             instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                                                             requestBody.getEffectiveTime(),
                                                                                                             methodName));
            }
            else
            {
                governanceActionProcessGraph.setGovernanceActionProcess(processHandler.getBeanFromRepository(userId,
                                                                                                             processGUID,
                                                                                                             processGUIDParameterName,
                                                                                                             OpenMetadataType.PROCESS.typeName,
                                                                                                             false,
                                                                                                             false,
                                                                                                             instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                                                             new Date(),
                                                                                                             methodName));
            }

            String processTypeName = governanceActionProcessGraph.getGovernanceActionProcess().getElementHeader().getType().getTypeName();

            if (OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME.equals(processTypeName))
            {
                governanceActionProcessGraph.setFirstProcessStep(this.getFirstProcessStepElement(serverName,
                                                                                                 serviceURLMarker,
                                                                                                 userId,
                                                                                                 processGUID,
                                                                                                 methodName));
            }
            else if (OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName.equals(processTypeName))
            {
                governanceActionProcessGraph.setFirstProcessStep(this.getFirstEngineStepElement(serverName,
                                                                                                serviceURLMarker,
                                                                                                userId,
                                                                                                processGUID,
                                                                                                methodName));
            }

            if (governanceActionProcessGraph.getFirstProcessStep() != null)
            {
                String firstProcessStepGUID = governanceActionProcessGraph.getFirstProcessStep().getElement().getElementHeader().getGUID();

                List<String>  processedGUIDs = new ArrayList<>();

                processedGUIDs.add(firstProcessStepGUID);

                if (OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME.equals(processTypeName))
                {
                    getNextProcessSteps(userId,
                                        handler,
                                        firstProcessStepGUID,
                                        governanceActionProcessGraph,
                                        processedGUIDs,
                                        instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                        invalidParameterHandler.getMaxPagingSize());
                }
                else if (OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName.equals(processTypeName))
                {
                    getNextEngineSteps(userId,
                                        engineActionHandler,
                                        firstProcessStepGUID,
                                        governanceActionProcessGraph,
                                        processedGUIDs,
                                        instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                        invalidParameterHandler.getMaxPagingSize());
                }
            }

            response.setElement(governanceActionProcessGraph);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the next step in the process.
     *
     * @param userId calling user
     * @param handler access to metadata
     * @param processStepGUID current step
     * @param governanceActionProcessGraph current state of the graph
     * @param processedGUIDs the guids we have processed
     * @param supportedZones zones for this service
     * @param pageSize max page size
     * @throws InvalidParameterException bad property
     * @throws PropertyServerException repository in error
     * @throws UserNotAuthorizedException user authorization exception
     */
    private void getNextProcessSteps(String                                                                 userId,
                                     GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler,
                                     String                                                                 processStepGUID,
                                     GovernanceActionProcessGraph                                           governanceActionProcessGraph,
                                     List<String>                                                           processedGUIDs,
                                     List<String>                                                           supportedZones,
                                     int                                                                    pageSize) throws InvalidParameterException,
                                                                                                                             PropertyServerException,
                                                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getNextProcessSteps";

        RepositoryHandler repositoryHandler = handler.getRepositoryHandler();

        int startFrom = 0;
        List<Relationship> nextProcessStepRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                                   processStepGUID,
                                                                                                   OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                                                   OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_GUID,
                                                                                                   OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                                                   2,
                                                                                                   false,
                                                                                                   false,
                                                                                                   startFrom,
                                                                                                   pageSize,
                                                                                                   null,
                                                                                                   methodName);

        while (nextProcessStepRelationships != null)
        {
            for (Relationship relationship : nextProcessStepRelationships)
            {
                this.addProcessStep(userId,
                                    handler,
                                    relationship.getEntityOneProxy().getGUID(),
                                    governanceActionProcessGraph,
                                    processedGUIDs,
                                    supportedZones);

                NextGovernanceActionProcessStepLink processStepLink = new NextGovernanceActionProcessStepLink();

                processStepLink.setPreviousProcessStep(handler.getElementStub(relationship.getEntityOneProxy()));
                processStepLink.setNextProcessStep(handler.getElementStub(relationship.getEntityTwoProxy()));
                processStepLink.setNextProcessStepLinkGUID(relationship.getGUID());
                processStepLink.setGuard(handler.getRepositoryHelper().getStringProperty(handler.getServiceName(),
                                                                                         OpenMetadataType.GUARD_PROPERTY_NAME,
                                                                                         relationship.getProperties(),
                                                                                         methodName));
                processStepLink.setMandatoryGuard(handler.getRepositoryHelper().getBooleanProperty(handler.getServiceName(),
                                                                                                   OpenMetadataType.MANDATORY_GUARD_PROPERTY_NAME,
                                                                                                   relationship.getProperties(),
                                                                                                   methodName));

                List<NextGovernanceActionProcessStepLink> processStepLinks = governanceActionProcessGraph.getProcessStepLinks();

                if (processStepLinks == null)
                {
                    processStepLinks = new ArrayList<>();
                }

                processStepLinks.add(processStepLink);
                governanceActionProcessGraph.setProcessStepLinks(processStepLinks);

                if (! processedGUIDs.contains(relationship.getEntityTwoProxy().getGUID()))
                {
                    this.addProcessStep(userId,
                                        handler,
                                        relationship.getEntityTwoProxy().getGUID(),
                                        governanceActionProcessGraph,
                                        processedGUIDs,
                                        supportedZones);

                    getNextProcessSteps(userId,
                                        handler,
                                        relationship.getEntityTwoProxy().getGUID(),
                                        governanceActionProcessGraph,
                                        processedGUIDs,
                                        supportedZones,
                                        pageSize);
                }
            }

            startFrom = startFrom + pageSize;
            nextProcessStepRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    processStepGUID,
                                                                                    OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                                    OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_GUID,
                                                                                    OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                                    2,
                                                                                    false,
                                                                                    false,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    null,
                                                                                    methodName);
        }
    }



    /**
     * Retrieve the next step in the process.
     *
     * @param userId calling user
     * @param handler access to metadata
     * @param processStepGUID current step
     * @param governanceActionProcessGraph current state of the graph
     * @param processedGUIDs the guids we have processed
     * @param supportedZones zones for this service
     * @param pageSize max page size
     * @throws InvalidParameterException bad property
     * @throws PropertyServerException repository in error
     * @throws UserNotAuthorizedException user authorization exception
     */
    private void getNextEngineSteps(String                                    userId,
                                     EngineActionHandler<EngineActionElement> handler,
                                     String                                   processStepGUID,
                                     GovernanceActionProcessGraph             governanceActionProcessGraph,
                                     List<String>                             processedGUIDs,
                                     List<String>                             supportedZones,
                                     int                                      pageSize) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "getNextEngineSteps";

        RepositoryHandler repositoryHandler = handler.getRepositoryHandler();

        int startFrom = 0;
        List<Relationship> nextProcessStepRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                                   processStepGUID,
                                                                                                   OpenMetadataType.ENGINE_ACTION.typeName,
                                                                                                   OpenMetadataType.NEXT_ENGINE_ACTION.typeGUID,
                                                                                                   OpenMetadataType.NEXT_ENGINE_ACTION.typeName,
                                                                                                   2,
                                                                                                   false,
                                                                                                   false,
                                                                                                   startFrom,
                                                                                                   pageSize,
                                                                                                   null,
                                                                                                   methodName);

        while (nextProcessStepRelationships != null)
        {
            for (Relationship relationship : nextProcessStepRelationships)
            {
                this.addEngineStep(userId,
                                   handler,
                                   relationship.getEntityOneProxy().getGUID(),
                                   governanceActionProcessGraph,
                                   processedGUIDs,
                                   supportedZones);

                NextGovernanceActionProcessStepLink processStepLink = new NextGovernanceActionProcessStepLink();

                processStepLink.setPreviousProcessStep(handler.getElementStub(relationship.getEntityOneProxy()));
                processStepLink.setNextProcessStep(handler.getElementStub(relationship.getEntityTwoProxy()));
                processStepLink.setNextProcessStepLinkGUID(relationship.getGUID());
                processStepLink.setGuard(handler.getRepositoryHelper().getStringProperty(handler.getServiceName(),
                                                                                         OpenMetadataType.GUARD_PROPERTY_NAME,
                                                                                         relationship.getProperties(),
                                                                                         methodName));
                processStepLink.setMandatoryGuard(handler.getRepositoryHelper().getBooleanProperty(handler.getServiceName(),
                                                                                                   OpenMetadataType.MANDATORY_GUARD_PROPERTY_NAME,
                                                                                                   relationship.getProperties(),
                                                                                                   methodName));

                List<NextGovernanceActionProcessStepLink> processStepLinks = governanceActionProcessGraph.getProcessStepLinks();

                if (processStepLinks == null)
                {
                    processStepLinks = new ArrayList<>();
                }

                processStepLinks.add(processStepLink);
                governanceActionProcessGraph.setProcessStepLinks(processStepLinks);

                if (! processedGUIDs.contains(relationship.getEntityTwoProxy().getGUID()))
                {
                    this.addEngineStep(userId,
                                       handler,
                                       relationship.getEntityTwoProxy().getGUID(),
                                       governanceActionProcessGraph,
                                       processedGUIDs,
                                       supportedZones);

                    getNextEngineSteps(userId,
                                       handler,
                                       relationship.getEntityTwoProxy().getGUID(),
                                       governanceActionProcessGraph,
                                       processedGUIDs,
                                       supportedZones,
                                       pageSize);
                }
            }

            startFrom = startFrom + pageSize;
            nextProcessStepRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    processStepGUID,
                                                                                    OpenMetadataType.ENGINE_ACTION.typeName,
                                                                                    OpenMetadataType.NEXT_ENGINE_ACTION.typeGUID,
                                                                                    OpenMetadataType.NEXT_ENGINE_ACTION.typeName,
                                                                                    2,
                                                                                    false,
                                                                                    false,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    null,
                                                                                    methodName);
        }
    }


    /**
     * Save the next step in the process.
     *
     * @param userId calling user
     * @param handler access to metadata
     * @param processStepGUID current step
     * @param governanceActionProcessGraph current state of the graph
     * @param processedGUIDs the guids we have processed
     * @param supportedZones zones for this service
     * @throws InvalidParameterException bad property
     * @throws PropertyServerException repository in error
     * @throws UserNotAuthorizedException user authorization exception
     */
    private void addProcessStep(String                                                                 userId,
                                GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler,
                                String                                                                 processStepGUID,
                                GovernanceActionProcessGraph                                           governanceActionProcessGraph,
                                List<String>                                                           processedGUIDs,
                                List<String>                                                           supportedZones) throws InvalidParameterException,
                                                                                                                              PropertyServerException,
                                                                                                                              UserNotAuthorizedException
    {
        final String methodName = "addProcessStep";
        if (! processedGUIDs.contains(processStepGUID))
        {
            GovernanceActionProcessStepElement processStepElement = handler.getGovernanceActionProcessStepByGUID(userId,
                                                                                                                 processStepGUID,
                                                                                                                 supportedZones,
                                                                                                                 null,
                                                                                                                 methodName);

            List<GovernanceActionProcessStepExecutionElement> processStepElements = governanceActionProcessGraph.getNextProcessSteps();

            if (processStepElements == null)
            {
                processStepElements = new ArrayList<>();
            }

            processStepElements.add(new GovernanceActionProcessStepExecutionElement(processStepElement));

            governanceActionProcessGraph.setNextProcessSteps(processStepElements);

            processedGUIDs.add(processStepGUID);
        }
    }


    /**
     * Save the next step in the process.
     *
     * @param userId calling user
     * @param handler access to metadata
     * @param processStepGUID current step
     * @param governanceActionProcessGraph current state of the graph
     * @param processedGUIDs the guids we have processed
     * @param supportedZones zones for this service
     * @throws InvalidParameterException bad property
     * @throws PropertyServerException repository in error
     * @throws UserNotAuthorizedException user authorization exception
     */
    private void addEngineStep(String                                   userId,
                               EngineActionHandler<EngineActionElement> handler,
                               String                                  processStepGUID,
                               GovernanceActionProcessGraph            governanceActionProcessGraph,
                               List<String>                            processedGUIDs,
                               List<String>                            supportedZones) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "addEngineStep";

        if (! processedGUIDs.contains(processStepGUID))
        {
            EngineActionElement engineActionElement = handler.getEngineAction(userId,
                                                                              processStepGUID,
                                                                              supportedZones,
                                                                              null,
                                                                              methodName);

            List<GovernanceActionProcessStepExecutionElement> processStepElements = governanceActionProcessGraph.getNextProcessSteps();

            if (processStepElements == null)
            {
                processStepElements = new ArrayList<>();
            }

            processStepElements.add(new GovernanceActionProcessStepExecutionElement(engineActionElement));

            governanceActionProcessGraph.setNextProcessSteps(processStepElements);

            processedGUIDs.add(processStepGUID);
        }
    }


    /* =====================================================================================================================
     * A governance action process step describes a step in a governance action process
     */

    /**
     * Create a new metadata element to represent a governance action process step.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody properties about the process to store
     *
     * @return unique identifier of the new governance action process step or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGovernanceActionProcessStep(String                                serverName,
                                                          String                                serviceURLMarker,
                                                          String                                userId,
                                                          GovernanceActionProcessStepProperties requestBody)
    {
        final String methodName = "createGovernanceActionProcessStep";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement>
                        handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                        serverName,
                                                                                        methodName);

                response.setGUID(handler.createGovernanceActionProcessStep(userId,
                                                                           requestBody.getQualifiedName(),
                                                                           requestBody.getDomainIdentifier(),
                                                                           requestBody.getDisplayName(),
                                                                           requestBody.getDescription(),
                                                                           requestBody.getAdditionalProperties(),
                                                                           requestBody.getGovernanceEngineGUID(),
                                                                           requestBody.getRequestType(),
                                                                           requestBody.getFixedRequestParameters(),
                                                                           requestBody.getIgnoreMultipleTriggers(),
                                                                           requestBody.getWaitTime(),
                                                                           null,
                                                                           null,
                                                                           false,
                                                                           false,
                                                                           instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                           new Date(),
                                                                           methodName));
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
     * Update the metadata element representing a governance action process step.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processStepGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGovernanceActionProcessStep(String                                       serverName,
                                                          String                                       serviceURLMarker,
                                                          String                                       userId,
                                                          String                                       processStepGUID,
                                                          UpdateGovernanceActionProcessStepRequestBody requestBody)
    {
        final String methodName = "updateGovernanceActionProcessStep";
        final String propertiesParameterName = "requestBody.getProperties";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                       serverName,
                                                                                                                                                       methodName);

                GovernanceActionProcessStepProperties properties = requestBody.getProperties();

                invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

                handler.updateGovernanceActionProcessStep(userId,
                                                          processStepGUID,
                                                          requestBody.getMergeUpdate(),
                                                          properties.getQualifiedName(),
                                                          properties.getDomainIdentifier(),
                                                          properties.getDisplayName(),
                                                          properties.getDescription(),
                                                          properties.getAdditionalProperties(),
                                                          properties.getGovernanceEngineGUID(),
                                                          properties.getRequestType(),
                                                          properties.getFixedRequestParameters(),
                                                          properties.getIgnoreMultipleTriggers(),
                                                          properties.getWaitTime(),
                                                          null,
                                                          null,
                                                          false,
                                                          false,
                                                          instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Remove the metadata element representing a governance action process step.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processStepGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeGovernanceActionProcessStep(String          serverName,
                                                          String          serviceURLMarker,
                                                          String          userId,
                                                          String          processStepGUID,
                                                          NullRequestBody requestBody)
    {
        final String methodName = "removeGovernanceActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                   serverName,
                                                                                                                                                   methodName);

            handler.removeGovernanceActionProcessStep(userId,
                                                      processStepGUID,
                                                      false,
                                                      false,
                                                      instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Retrieve the list of governance action process step metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessStepsResponse findGovernanceActionProcessSteps(String                  serverName,
                                                                                 String                  serviceURLMarker,
                                                                                 String                  userId,
                                                                                 int                     startFrom,
                                                                                 int                     pageSize,
                                                                                 SearchStringRequestBody requestBody)
    {
        final String methodName = "findGovernanceActionProcessSteps";

        String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionProcessStepsResponse response = new GovernanceActionProcessStepsResponse();
        AuditLog                             auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                List<String> supportedZones = instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName);
                GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                       serverName,
                                                                                                                                                       methodName);

                if (requestBody.getSearchStringParameterName() != null)
                {
                    searchStringParameterName = requestBody.getSearchStringParameterName();
                }

                List<GovernanceActionProcessStepElement> results = handler.findGovernanceActionProcessSteps(userId,
                                                                                                            requestBody.getSearchString(),
                                                                                                            searchStringParameterName,
                                                                                                            startFrom,
                                                                                                            pageSize,
                                                                                                            false,
                                                                                                            false,
                                                                                                            supportedZones,
                                                                                                            new Date(),
                                                                                                            methodName);

                response.setElements(addGovernanceActionProcessStepSpecification(userId, handler, supportedZones, results));
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
     * Add the specification to each of the elements returned.
     *
     * @param userId calling user
     * @param handler generic handler
     * @param supportedZones supported zones
     * @param elements retrieved elements.
     * @return elements plus specifications
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    private List<GovernanceActionProcessStepElement> addGovernanceActionProcessStepSpecification(String                                                            userId,
                                                                                                 OpenMetadataAPIGenericHandler<GovernanceActionProcessStepElement> handler,
                                                                                                 List<String>                                                      supportedZones,
                                                                                                 List<GovernanceActionProcessStepElement>                          elements) throws InvalidParameterException,
                                                                                                                                                                                    PropertyServerException,
                                                                                                                                                                                    UserNotAuthorizedException
    {
        if (elements != null)
        {
            final String elementGUIDParameterName = "governanceActionProcessStepGUID";

            List<GovernanceActionProcessStepElement> results = new ArrayList<>();

            for (GovernanceActionProcessStepElement element : elements)
            {
                if (element != null)
                {
                    element.setSpecification(handler.getSpecification(userId,
                                                                      element.getElementHeader().getGUID(),
                                                                      elementGUIDParameterName,
                                                                      OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                      supportedZones));

                    results.add(element);
                }
                else
                {
                    results.add(null);
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Retrieve the list of governance action process step metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessStepsResponse getGovernanceActionProcessStepsByName(String          serverName,
                                                                                      String          serviceURLMarker,
                                                                                      String          userId,
                                                                                      int             startFrom,
                                                                                      int             pageSize,
                                                                                      NameRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionProcessStepsByName";

        String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionProcessStepsResponse response = new GovernanceActionProcessStepsResponse();
        AuditLog                             auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                List<String> supportedZones = instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName);
                GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                       serverName,
                                                                                                                                                       methodName);

                if (requestBody.getNameParameterName() != null)
                {
                    nameParameterName = requestBody.getNameParameterName();
                }

                List<GovernanceActionProcessStepElement> results = handler.getGovernanceActionProcessStepsByName(userId,
                                                                                                                 requestBody.getName(),
                                                                                                                 nameParameterName,
                                                                                                                 startFrom,
                                                                                                                 pageSize,
                                                                                                                 supportedZones,
                                                                                                                 null,
                                                                                                                 methodName);

                response.setElements(addGovernanceActionProcessStepSpecification(userId, handler, supportedZones, results));
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
     * Retrieve the governance action process step metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processStepGUID unique identifier of the governance action process step
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessStepResponse getGovernanceActionProcessStepByGUID(String serverName,
                                                                                    String serviceURLMarker,
                                                                                    String userId,
                                                                                    String processStepGUID)
    {
        final String methodName = "getGovernanceActionProcessStepByGUID";
        final String elementGUIDParameterName = "processStepGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionProcessStepResponse response = new GovernanceActionProcessStepResponse();
        AuditLog                            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            List<String> supportedZones = instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName);
            GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                   serverName,
                                                                                                                                                   methodName);

            GovernanceActionProcessStepElement element = handler.getGovernanceActionProcessStepByGUID(userId,
                                                                                                      processStepGUID,
                                                                                                      supportedZones,
                                                                                                      null,
                                                                                                      methodName);

            element.setSpecification(handler.getSpecification(userId,
                                                              element.getElementHeader().getGUID(),
                                                              elementGUIDParameterName,
                                                              OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                              supportedZones));

            response.setElement(element);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Set up a link between a governance action process and a governance action process step.  This defines the first
     * step in the process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param processStepGUID unique identifier of the governance action process step
     * @param requestBody optional guard
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupFirstProcessStep(String serverName,
                                              String serviceURLMarker,
                                              String userId,
                                              String processGUID,
                                              String processStepGUID,
                                              String requestBody)
    {
        final String methodName = "setupFirstActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId, serverName, methodName);

            handler.setupFirstProcessStep(userId,
                                          processGUID,
                                          processStepGUID,
                                          requestBody,
                                          null,
                                          null,
                                          false,
                                          false,
                                          instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Return the governance action process step that is the first step in a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     *
     * @return properties of the governance action process step or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public FirstGovernanceActionProcessStepResponse getFirstProcessStep(String serverName,
                                                                        String serviceURLMarker,
                                                                        String userId,
                                                                        String processGUID)
    {
        final String methodName = "getFirstActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        FirstGovernanceActionProcessStepResponse response = new FirstGovernanceActionProcessStepResponse();
        AuditLog                                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElement(this.getFirstProcessStepElement(serverName,
                                                                serviceURLMarker,
                                                                userId,
                                                                processGUID,
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
     * Return the first process step element of a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param methodName calling method
     * @return first process step element
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException the user is not authorized to issue this request
     * @throws UserNotAuthorizedException there is a problem reported in the open metadata server(s)
     */
    private FirstGovernanceActionProcessStepElement getFirstProcessStepElement(String serverName,
                                                                               String serviceURLMarker,
                                                                               String userId,
                                                                               String processGUID,
                                                                               String methodName) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId, serverName, methodName);

        final String processGUIDParameterName = "processGUID";

        Relationship firstActionProcessStepLink = handler.getUniqueAttachmentLink(userId,
                                                                                  processGUID,
                                                                                  processGUIDParameterName,
                                                                                  OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                                  OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_TYPE_GUID,
                                                                                  OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_TYPE_NAME,
                                                                                  null,
                                                                                  OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME,
                                                                                  0,
                                                                                  false,
                                                                                  false,
                                                                                  instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                                  null,
                                                                                  methodName);

        if ((firstActionProcessStepLink != null) && (firstActionProcessStepLink.getEntityTwoProxy() != null))
        {
            FirstGovernanceActionProcessStepElement firstProcessStep = new FirstGovernanceActionProcessStepElement();

            firstProcessStep.setLinkGUID(firstActionProcessStepLink.getGUID());
            firstProcessStep.setGuard(handler.getRepositoryHelper().getStringProperty(instanceHandler.getServiceName(serviceURLMarker),
                                                                                      OpenMetadataType.GUARD_PROPERTY_NAME,
                                                                                      firstActionProcessStepLink.getProperties(),
                                                                                      methodName));

            GovernanceActionProcessStepElement processStepElement = handler.getGovernanceActionProcessStepByGUID(userId,
                                                                                                                 firstActionProcessStepLink.getEntityTwoProxy().getGUID(),
                                                                                                                 instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                                                                 null,
                                                                                                                 methodName);
            firstProcessStep.setElement(new GovernanceActionProcessStepExecutionElement(processStepElement));

            return firstProcessStep;
        }

        return null;
    }



    /**
     * Return the first process step element of a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processInstanceGUID unique identifier of the governance action process
     * @param methodName calling method
     * @return first process step element
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException the user is not authorized to issue this request
     * @throws UserNotAuthorizedException there is a problem reported in the open metadata server(s)
     */
    private FirstGovernanceActionProcessStepElement getFirstEngineStepElement(String serverName,
                                                                              String serviceURLMarker,
                                                                              String userId,
                                                                              String processInstanceGUID,
                                                                              String methodName) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

        final String processGUIDParameterName = "processInstanceGUID";

        Relationship firstActionProcessStepLink = handler.getUniqueAttachmentLink(userId,
                                                                                  processInstanceGUID,
                                                                                  processGUIDParameterName,
                                                                                  OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName,
                                                                                  OpenMetadataType.ENGINE_ACTION_REQUEST_SOURCE.typeGUID,
                                                                                  OpenMetadataType.ENGINE_ACTION_REQUEST_SOURCE.typeName,
                                                                                  null,
                                                                                  OpenMetadataType.ENGINE_ACTION.typeName,
                                                                                  0,
                                                                                  false,
                                                                                  false,
                                                                                  instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                                  null,
                                                                                  methodName);

        if ((firstActionProcessStepLink != null) && (firstActionProcessStepLink.getEntityTwoProxy() != null))
        {
            FirstGovernanceActionProcessStepElement firstProcessStep = new FirstGovernanceActionProcessStepElement();

            firstProcessStep.setLinkGUID(firstActionProcessStepLink.getGUID());

            EngineActionElement engineActionElement = handler.getEngineAction(userId,
                                                                              firstActionProcessStepLink.getEntityTwoProxy().getGUID(),
                                                                              instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                              null,
                                                                              methodName);

            firstProcessStep.setElement(new GovernanceActionProcessStepExecutionElement(engineActionElement));

            return firstProcessStep;
        }

        return null;
    }


    /**
     * Remove the link between a governance process and that governance action process step that defines its first step.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeFirstProcessStep(String          serverName,
                                               String          serviceURLMarker,
                                               String          userId,
                                               String          processGUID,
                                               NullRequestBody requestBody)
    {
        final String methodName = "removeFirstActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                   serverName,
                                                                                                                                                   methodName);

            handler.removeFirstProcessStep(userId,
                                           processGUID,
                                           instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                           null,
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
     * Add a link between two governance action process steps to show that one follows on from the other when a governance action process
     * is executing.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param currentProcessStepGUID unique identifier of the governance action process step that defines the previous step in the governance action process
     * @param nextProcessStepGUID unique identifier of the governance action process step that defines the next step in the governance action process
     * @param requestBody guard required for this next step to proceed - or null for always run the next step plus flags.
     *
     * @return unique identifier of the new link or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse setupNextProcessStep(String                                     serverName,
                                             String                                     serviceURLMarker,
                                             String                                     userId,
                                             String                                     currentProcessStepGUID,
                                             String                                     nextProcessStepGUID,
                                             NextGovernanceActionProcessStepRequestBody requestBody)
    {
        final String methodName = "setupNextActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                       serverName,
                                                                                                                                                       methodName);
                response.setGUID(handler.setupNextProcessStep(userId,
                                                              currentProcessStepGUID,
                                                              nextProcessStepGUID,
                                                              requestBody.getGuard(),
                                                              requestBody.getMandatoryGuard(),
                                                              null,
                                                              null,
                                                              false,
                                                              false,
                                                              instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                              new Date(),
                                                              methodName));
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
     * Update the properties of the link between two governance action process steps that shows that one follows on from the other when a governance
     * action process is executing.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param nextProcessStepLinkGUID unique identifier of the relationship between the governance action process steps
     * @param requestBody guard required for this next step to proceed - or null for always run the next step - and flags
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse updateNextProcessStep(String                                     serverName,
                                              String                                     serviceURLMarker,
                                              String                                     userId,
                                              String                                     nextProcessStepLinkGUID,
                                              NextGovernanceActionProcessStepRequestBody requestBody)
    {
        final String methodName = "updateNextActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                       serverName,
                                                                                                                                                       methodName);
                handler.updateNextProcessStep(userId,
                                              nextProcessStepLinkGUID,
                                              requestBody.getGuard(),
                                              requestBody.getMandatoryGuard(),
                                              null,
                                              null,
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
     * Return the list of next action process step defined for the governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processStepGUID unique identifier of the current governance action process step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return return the list of relationships and attached governance action process steps or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NextGovernanceActionProcessStepsResponse getNextProcessSteps(String serverName,
                                                                        String serviceURLMarker,
                                                                        String userId,
                                                                        String processStepGUID,
                                                                        int    startFrom,
                                                                        int    pageSize)
    {
        final String methodName = "getNextGovernanceActionProcessSteps";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NextGovernanceActionProcessStepsResponse response = new NextGovernanceActionProcessStepsResponse();
        AuditLog                                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                   serverName,
                                                                                                                                                   methodName);

            List<Relationship> relationships = handler.getNextGovernanceActionProcessSteps(userId,
                                                                                           processStepGUID,
                                                                                           startFrom,
                                                                                           pageSize,
                                                                                           instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                                           new Date(),
                                                                                           methodName);

            if (relationships != null)
            {
                OMRSRepositoryHelper repositoryHelper = instanceHandler.getRepositoryHelper(userId, serverName, methodName);

                List<NextGovernanceActionProcessStepElement> elements = new ArrayList<>();

                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        NextGovernanceActionProcessStepElement element = new NextGovernanceActionProcessStepElement();

                        element.setNextProcessStepLinkGUID(relationship.getGUID());
                        element.setGuard(repositoryHelper.getStringProperty(instanceHandler.getServiceName(),
                                                                            OpenMetadataType.GUARD_PROPERTY_NAME,
                                                                            relationship.getProperties(),
                                                                            methodName));
                        element.setMandatoryGuard(repositoryHelper.getBooleanProperty(instanceHandler.getServiceName(),
                                                                                      OpenMetadataType.MANDATORY_GUARD_PROPERTY_NAME,
                                                                                      relationship.getProperties(),
                                                                                      methodName));

                        GovernanceActionProcessStepElement processStepElement = handler.getGovernanceActionProcessStepByGUID(userId,
                                                                                                                             relationship.getEntityTwoProxy().getGUID(),
                                                                                                                             instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                                                                             null,
                                                                                                                             methodName);

                        element.setNextProcessStep(new GovernanceActionProcessStepExecutionElement(processStepElement));

                        elements.add(element);
                    }
                }

                response.setElements(elements);
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
     * Remove a follow-on step from a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param actionLinkGUID unique identifier of the relationship between the governance action process steps
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid or
     *  UserNotAuthorizedException the user is not authorized to issue this request or
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeNextProcessStep(String          serverName,
                                              String          serviceURLMarker,
                                              String          userId,
                                              String          actionLinkGUID,
                                              NullRequestBody requestBody)
    {
        final String methodName = "removeNextProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                   serverName,
                                                                                                                                                   methodName);

            handler.removeNextProcessStep(userId, actionLinkGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return engine action properties and status or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementResponse getEngineAction(String serverName,
                                                       String serviceURLMarker,
                                                       String userId,
                                                       String engineActionGUID)
    {
        final String methodName = "getEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        EngineActionElementResponse response = new EngineActionElementResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

            response.setElement(handler.getEngineAction(userId,
                                                        engineActionGUID,
                                                        instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Retrieve the engine actions that are known to the server.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementsResponse getEngineActions(String serverName,
                                                         String serviceURLMarker,
                                                         String userId,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName = "getEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        EngineActionElementsResponse response = new EngineActionElementsResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

            response.setElements(handler.getEngineActions(userId,
                                                          startFrom,
                                                          pageSize,
                                                          instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Retrieve the engine actions that are still in process.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementsResponse getActiveEngineActions(String serverName,
                                                               String serviceURLMarker,
                                                               String userId,
                                                               int    startFrom,
                                                               int    pageSize)
    {
        final String methodName = "getActiveEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        EngineActionElementsResponse response = new EngineActionElementsResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

            response.setElements(handler.getActiveEngineActions(userId,
                                                                startFrom,
                                                                pageSize,
                                                                instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Retrieve the engine actions that are still in process and that have been claimed by this caller's userId.
     * This call is used when the caller restarts.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId userId of caller
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementsResponse getActiveClaimedEngineActions(String serverName,
                                                                      String serviceURLMarker,
                                                                      String userId,
                                                                      String governanceEngineGUID,
                                                                      int    startFrom,
                                                                      int    pageSize)
    {
        final String methodName = "getActiveClaimedEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        EngineActionElementsResponse response = new EngineActionElementsResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

            response.setElements(handler.getActiveClaimedEngineActions(userId,
                                                                       governanceEngineGUID,
                                                                       startFrom,
                                                                       pageSize,
                                                                       instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Retrieve the list of engine action metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EngineActionElementsResponse findEngineActions(String                  serverName,
                                                          String                  serviceURLMarker,
                                                          String                  userId,
                                                          int                     startFrom,
                                                          int                     pageSize,
                                                          SearchStringRequestBody requestBody)
    {
        final String methodName = "findEngineActions";

        String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EngineActionElementsResponse response = new EngineActionElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId,
                                                                                                          serverName,
                                                                                                          methodName);

                if (requestBody.getSearchStringParameterName() != null)
                {
                    searchStringParameterName = requestBody.getSearchStringParameterName();
                }

                response.setElements(handler.findEngineActions(userId,
                                                               requestBody.getSearchString(),
                                                               searchStringParameterName,
                                                               startFrom,
                                                               pageSize,
                                                               false,
                                                               false,
                                                               instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                               new Date(),
                                                               methodName));
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
     * Retrieve the list of engine action  metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EngineActionElementsResponse getEngineActionsByName(String          serverName,
                                                               String          serviceURLMarker,
                                                               String          userId,
                                                               int             startFrom,
                                                               int             pageSize,
                                                               NameRequestBody requestBody)
    {
        final String methodName = "getEngineActionsByName";

        String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EngineActionElementsResponse response = new EngineActionElementsResponse();
        AuditLog                             auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId,
                                                                                                          serverName,
                                                                                                          methodName);

                if (requestBody.getNameParameterName() != null)
                {
                    nameParameterName = requestBody.getNameParameterName();
                }

                response.setElements(handler.getEngineActionsByName(userId,
                                                                    requestBody.getName(),
                                                                    nameParameterName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                    null,
                                                                    methodName));
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
     * Request that execution of an engine action is allocated to the caller.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse claimEngineAction(String          serverName,
                                          String          serviceURLMarker,
                                          String          userId,
                                          String          engineActionGUID,
                                          NullRequestBody requestBody)
    {
        final String methodName = "claimEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

            handler.claimEngineAction(userId,
                                      engineActionGUID,
                                      instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Request that execution of an engine action is stopped.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse cancelEngineAction(String          serverName,
                                           String          serviceURLMarker,
                                           String          userId,
                                           String          engineActionGUID,
                                           NullRequestBody requestBody)
    {
        final String methodName = "cancelEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

            handler.cancelEngineAction(userId,
                                       engineActionGUID,
                                       instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Update the status of the engine action - providing the caller is permitted.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request
     * @param requestBody new status ordinal
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public VoidResponse updateEngineActionStatus(String                        serverName,
                                                 String                        serviceURLMarker,
                                                 String                        userId,
                                                 String                        engineActionGUID,
                                                 EngineActionStatusRequestBody requestBody)
    {
        final String methodName = "updateEngineActionStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

                int statusOrdinal = EngineActionStatus.ACTIONED.getOrdinal();

                if (requestBody.getStatus() != null)
                {
                    statusOrdinal = requestBody.getStatus().getOrdinal();
                }

                handler.updateEngineActionStatus(userId,
                                                 engineActionGUID,
                                                 statusOrdinal,
                                                 instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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



    /*
     * Duplicates processing
     */

    /**
     * Link elements as peer duplicates. Create a simple relationship between two elements.
     * If the relationship already exists, the properties are updated.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse linkElementsAsDuplicates(String                    serverName,
                                                 String                    serviceURLMarker,
                                                 String                    userId,
                                                 PeerDuplicatesRequestBody requestBody)
    {
        final String methodName = "linkElementsAsDuplicates";

        final String element1GUIDParameterName = "element1GUID";
        final String element2GUIDParameterName = "element2GUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.linkElementsAsPeerDuplicates(userId,
                                                     requestBody.getMetadataElement1GUID(),
                                                     element1GUIDParameterName,
                                                     requestBody.getMetadataElement2GUID(),
                                                     element2GUIDParameterName,
                                                     requestBody.getSetKnownDuplicate(),
                                                     requestBody.getStatusIdentifier(),
                                                     requestBody.getSteward(),
                                                     requestBody.getStewardTypeName(),
                                                     requestBody.getStewardPropertyName(),
                                                     requestBody.getSource(),
                                                     requestBody.getNotes(),
                                                     instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Identify an element that acts as a consolidated version for a set of duplicate elements.
     * (The consolidated element is created using createMetadataElement.)
     * Creates a simple relationship between the elements. If the ConsolidatedDuplicate
     * classification already exists, the properties are updated.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse linkConsolidatedDuplicate(String                            serverName,
                                                  String                            serviceURLMarker,
                                                  String                            userId,
                                                  ConsolidatedDuplicatesRequestBody requestBody)
    {
        final String methodName = "linkConsolidatedDuplicate";

        final String elementGUIDParameterName = "consolidatedElementGUID";
        final String sourceElementGUIDsParameterName = "sourceElementGUIDs";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            MetadataElementHandler<OpenMetadataElement> handler = instanceHandler.getMetadataElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.linkConsolidatedDuplicate(userId,
                                                  requestBody.getConsolidatedElementGUID(),
                                                  elementGUIDParameterName,
                                                  requestBody.getStatusIdentifier(),
                                                  requestBody.getSteward(),
                                                  requestBody.getStewardTypeName(),
                                                  requestBody.getStewardPropertyName(),
                                                  requestBody.getSource(),
                                                  requestBody.getNotes(),
                                                  requestBody.getSourceElementGUIDs(),
                                                  sourceElementGUIDsParameterName,
                                                  instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance action service.  However, if the governance action service has to process name
     * target elements, then setting the status on each individual target will show the progress of the
     * governance action service.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException the action target GUID is not recognized
     *  UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     *  PropertyServerException there is a problem connecting to the metadata store
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse updateActionTargetStatus(String                        serverName,
                                                 String                        serviceURLMarker,
                                                 String                        userId,
                                                 ActionTargetStatusRequestBody requestBody)
    {
        final String methodName = "updateActionTargetStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

                int statusOrdinal = EngineActionStatus.ACTIONED.getOrdinal();

                if (requestBody.getStatus() != null)
                {
                    statusOrdinal = requestBody.getStatus().getOrdinal();
                }
                handler.updateActionTargetStatus(userId,
                                                 requestBody.getActionTargetGUID(),
                                                 statusOrdinal,
                                                 requestBody.getStartDate(),
                                                 requestBody.getCompletionDate(),
                                                 requestBody.getCompletionMessage(),
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
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param governanceActionGUID unique identifier of the governance action to update
     * @param requestBody completion status enum value, optional guard strings for triggering subsequent action(s) plus
     *                    a list of additional elements to add to the action targets for the next phase
     *
     * @return void or
     *  InvalidParameterException the completion status is null
     *  UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     *  PropertyServerException there is a problem connecting to the metadata store
     */
    public VoidResponse recordCompletionStatus(String                      serverName,
                                               String                      serviceURLMarker,
                                               String                      userId,
                                               String                      governanceActionGUID,
                                               CompletionStatusRequestBody requestBody)
    {
        final String methodName = "recordCompletionStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

                int statusOrdinal = EngineActionStatus.ACTIONED.getOrdinal();

                if (requestBody.getStatus() != null)
                {
                    statusOrdinal = requestBody.getStatus().getOpenTypeOrdinal();
                }

                handler.recordCompletionStatus(userId,
                                               governanceActionGUID,
                                               statusOrdinal,
                                               requestBody.getRequestParameters(),
                                               requestBody.getOutputGuards(),
                                               requestBody.getNewActionTargets(),
                                               requestBody.getCompletionMessage(),
                                               instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
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
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestBody properties for the governance action and to pass to the governance action service
     *
     * @return unique identifier of the governance action or
     *  InvalidParameterException null qualified name
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse initiateEngineAction(String                  serverName,
                                             String                  serviceURLMarker,
                                             String                  userId,
                                             String                  governanceEngineName,
                                             InitiateEngineActionRequestBody requestBody)
    {
        final String methodName = "initiateEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

                Date startTime = requestBody.getStartDate();

                if (startTime == null)
                {
                    startTime = new Date();
                }

                String engineActionGUID = handler.createEngineAction(userId,
                                                                     requestBody.getQualifiedName(),
                                                                     requestBody.getDomainIdentifier(),
                                                                     requestBody.getDisplayName(),
                                                                     requestBody.getDescription(),
                                                                     requestBody.getRequestSourceGUIDs(),
                                                                     requestBody.getActionTargets(),
                                                                     null,
                                                                     requestBody.getReceivedGuards(),
                                                                     startTime,
                                                                     governanceEngineName,
                                                                     userId,
                                                                     requestBody.getRequestType(),
                                                                     requestBody.getRequestParameters(),
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     requestBody.getProcessName(),
                                                                     null,
                                                                     null,
                                                                     requestBody.getRequestSourceName(),
                                                                     requestBody.getOriginatorServiceName(),
                                                                     requestBody.getOriginatorEngineName(),
                                                                     instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                     methodName);

                if (engineActionGUID != null)
                {
                    /*
                     * Since there is no process control, the governance action moves immediately into APPROVED
                     * status, and it is picked up by the listening engine hosts.
                     */
                    handler.approveEngineAction(userId,
                                                engineActionGUID,
                                                requestBody.getQualifiedName(),
                                                null,
                                                requestBody.getReceivedGuards(),
                                                requestBody.getStartDate(),
                                                governanceEngineName,
                                                requestBody.getRequestType(),
                                                requestBody.getRequestParameters(),
                                                null,
                                                requestBody.getProcessName(),
                                                instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                methodName);

                    response.setGUID(engineActionGUID);
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
     * Using the named governance action type as a template, initiate an engine action.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param requestBody properties to initiate the new instance of the engine action
     *
     * @return unique identifier of the first governance action of the process or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse initiateGovernanceActionType(String                                  serverName,
                                                     String                                  serviceURLMarker,
                                                     String                                  userId,
                                                     InitiateGovernanceActionTypeRequestBody requestBody)
    {
        final String methodName = "initiateGovernanceActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

                response.setGUID(handler.initiateGovernanceActionType(userId,
                                                                      requestBody.getGovernanceActionTypeQualifiedName(),
                                                                      requestBody.getRequestSourceGUIDs(),
                                                                      requestBody.getActionTargets(),
                                                                      requestBody.getRequestParameters(),
                                                                      requestBody.getStartDate(),
                                                                      requestBody.getOriginatorServiceName(),
                                                                      requestBody.getOriginatorEngineName(),
                                                                      instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                      methodName));
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
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param requestBody properties to initiate the new instance of the process
     *
     * @return unique identifier of the governance action process instance or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse initiateGovernanceActionProcess(String                             serverName,
                                                        String                             serviceURLMarker,
                                                        String                             userId,
                                                        InitiateGovernanceActionProcessRequestBody requestBody)
    {
        final String methodName = "initiateGovernanceActionProcess";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

                response.setGUID(handler.initiateGovernanceActionProcess(userId,
                                                                         requestBody.getProcessQualifiedName(),
                                                                         requestBody.getRequestSourceGUIDs(),
                                                                         requestBody.getActionTargets(),
                                                                         requestBody.getRequestParameters(),
                                                                         requestBody.getStartDate(),
                                                                         requestBody.getOriginatorServiceName(),
                                                                         requestBody.getOriginatorEngineName(),
                                                                         instanceHandler.getSupportedZones(userId, serverName, serviceURLMarker, methodName),
                                                                         methodName));
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
}
