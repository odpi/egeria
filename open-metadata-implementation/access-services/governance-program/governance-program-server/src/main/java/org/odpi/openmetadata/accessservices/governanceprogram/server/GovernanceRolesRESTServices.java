/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;

import org.odpi.openmetadata.accessservices.governanceprogram.handlers.AppointmentHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceRoleElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceRoleProperties;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.PersonRoleHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The GovernanceRolesRESTServices provides the server-side implementation of the GovernanceRolesInterface
 * from the Open Metadata Access Service (OMAS).  This interface provides the ability to governance roles and
 * link/unlink them to individuals.
 */
public class GovernanceRolesRESTServices
{
    static private final GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(GovernanceRolesRESTServices.class),
                                                                                        instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public GovernanceRolesRESTServices()
    {
    }


    /**
     * Create the governance role.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param requestBody  properties of the governance role.
     * @return Unique identifier (guid) of the governance role or
     * InvalidParameterException the governance domain or appointment id is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GUIDResponse createGovernanceRole(String                    serverName,
                                             String                    userId,
                                             GovernanceRoleRequestBody requestBody)

    {
        final String methodName = "createGovernanceRole";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    GovernanceRoleProperties properties = requestBody.getProperties();

                    String typeName = OpenMetadataType.GOVERNANCE_ROLE.typeName;

                    if (properties.getTypeName() != null)
                    {
                        typeName = properties.getTypeName();
                    }

                    Map<String, Object> extendedProperties = properties.getExtendedProperties();

                    if (properties.getDomainIdentifier() != 0)
                    {
                        if (extendedProperties == null)
                        {
                            extendedProperties = new HashMap<>();
                        }

                        extendedProperties.put(OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME, properties.getDomainIdentifier());
                    }

                    PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                    response.setGUID(handler.createPersonRole(userId,
                                                              requestBody.getExternalSourceGUID(),
                                                              requestBody.getExternalSourceName(),
                                                              properties.getQualifiedName(),
                                                              properties.getRoleId(),
                                                              properties.getTitle(),
                                                              properties.getDescription(),
                                                              properties.getScope(),
                                                              properties.getHeadCount(),
                                                              properties.getHeadCountLimitSet(),
                                                              properties.getDomainIdentifier(),
                                                              properties.getAdditionalProperties(),
                                                              typeName,
                                                              extendedProperties,
                                                              properties.getEffectiveFrom(),
                                                              properties.getEffectiveTo(),
                                                              new Date(),
                                                              methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceRoleProperties.class.getName(), methodName);
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
     * Update selected fields for the governance role.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param governanceRoleGUID unique identifier (guid) of the governance role.
     * @param requestBody  properties of the governance role
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance role is either null or invalid or
     * InvalidParameterException the title is null or the governanceDomain/appointmentId does not match
     *                           the existing values associated with the governanceRoleGUID or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse updateGovernanceRole(String                    serverName,
                                             String                    userId,
                                             String                    governanceRoleGUID,
                                             boolean                   isMergeUpdate,
                                             GovernanceRoleRequestBody requestBody)
    {
        final String methodName = "updateGovernanceRole";
        final String governanceRoleGUIDParameterName = "governanceRoleGUID";
        final String roleIdParameterName = "roleId";
        final String titleParameterName = "title";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;


        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    GovernanceRoleProperties properties = requestBody.getProperties();

                    Map<String, Object> extendedProperties = properties.getExtendedProperties();

                    if (properties.getDomainIdentifier() != 0)
                    {
                        if (extendedProperties == null)
                        {
                            extendedProperties = new HashMap<>();
                        }

                        extendedProperties.put(OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME, properties.getDomainIdentifier());
                    }

                    PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                    handler.updatePersonRole(userId,
                                             null,
                                             null,
                                             governanceRoleGUID,
                                             governanceRoleGUIDParameterName,
                                             properties.getQualifiedName(),
                                             roleIdParameterName,
                                             properties.getRoleId(),
                                             properties.getTitle(),
                                             titleParameterName,
                                             properties.getDescription(),
                                             properties.getScope(),
                                             properties.getHeadCount(),
                                             properties.getHeadCountLimitSet(),
                                             properties.getDomainIdentifier(),
                                             properties.getAdditionalProperties(),
                                             properties.getTypeName(),
                                             extendedProperties,
                                             isMergeUpdate,
                                             null,
                                             null,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceRoleProperties.class.getName(), methodName);
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
     * Link a governance role to a governance control that defines a governance responsibility that a person fulfils.
     *
     * @param serverName name of server instance to call
     * @param userId calling user
     * @param governanceRoleGUID unique identifier of the governance role
     * @param responsibilityGUID unique identifier of the governance responsibility control
     * @param requestBody  relationship request body
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse linkRoleToResponsibility(String                  serverName,
                                                 String                  userId,
                                                 String                  governanceRoleGUID,
                                                 String                  responsibilityGUID,
                                                 RelationshipRequestBody requestBody)
    {
        final String   methodName = "linkRoleToResponsibility";
        final String   governanceRoleGUIDParameterName = "governanceRoleGUID";
        final String   responsibilityGUIDParameterName = "responsibilityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;


        try
        {
            if (requestBody != null)
            {
                PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                if (requestBody.getProperties() != null)
                {
                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 governanceRoleGUID,
                                                 governanceRoleGUIDParameterName,
                                                 OpenMetadataType.PERSON_ROLE.typeName,
                                                 responsibilityGUID,
                                                 responsibilityGUIDParameterName,
                                                 OpenMetadataType.GOVERNANCE_RESPONSIBILITY.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.GOVERNANCE_RESPONSIBILITY_ASSIGNMENT.typeGUID,
                                                 OpenMetadataType.GOVERNANCE_RESPONSIBILITY_ASSIGNMENT.typeName,
                                                 null,
                                                 requestBody.getProperties().getEffectiveFrom(),
                                                 requestBody.getProperties().getEffectiveTo(),
                                                 new Date(),
                                                 methodName);
                }
                else
                {
                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 governanceRoleGUID,
                                                 governanceRoleGUIDParameterName,
                                                 OpenMetadataType.PERSON_ROLE.typeName,
                                                 responsibilityGUID,
                                                 responsibilityGUIDParameterName,
                                                 OpenMetadataType.GOVERNANCE_RESPONSIBILITY.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.GOVERNANCE_RESPONSIBILITY_ASSIGNMENT.typeGUID,
                                                 OpenMetadataType.GOVERNANCE_RESPONSIBILITY_ASSIGNMENT.typeName,
                                                 (InstanceProperties) null,
                                                 null,
                                                 null,
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
     * Remove the link between a governance role and a governance responsibility.
     *
     * @param serverName name of server instance to call
     * @param userId calling user
     * @param governanceRoleGUID unique identifier of the governance role
     * @param responsibilityGUID unique identifier of the governance responsibility control
     * @param requestBody  external source request body
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse unlinkRoleFromResponsibility(String                  serverName,
                                                     String                  userId,
                                                     String                  governanceRoleGUID,
                                                     String                  responsibilityGUID,
                                                     RelationshipRequestBody requestBody)
    {
        final String   methodName = "unlinkRoleToResponsibility";
        final String   governanceRoleGUIDParameterName = "governanceRoleGUID";
        final String   responsibilityGUIDParameterName = "responsibilityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;


        try
        {
            if (requestBody != null)
            {
                PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.unlinkElementFromElement(userId,
                                                 false,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 governanceRoleGUID,
                                                 governanceRoleGUIDParameterName,
                                                 OpenMetadataType.PERSON_ROLE.typeName,
                                                 responsibilityGUID,
                                                 responsibilityGUIDParameterName,
                                                 OpenMetadataType.GOVERNANCE_RESPONSIBILITY.typeGUID,
                                                 OpenMetadataType.GOVERNANCE_RESPONSIBILITY.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.GOVERNANCE_RESPONSIBILITY_ASSIGNMENT.typeGUID,
                                                 OpenMetadataType.GOVERNANCE_RESPONSIBILITY_ASSIGNMENT.typeName,
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
     * Link a governance role to the description of a resource that the role is responsible for.
     *
     * @param serverName name of server instance to call
     * @param userId calling user
     * @param governanceRoleGUID unique identifier of the governance role
     * @param resourceGUID unique identifier of the resource description
     * @param requestBody  relationship request body
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse linkRoleToResource(String                  serverName,
                                           String                  userId,
                                           String                  governanceRoleGUID,
                                           String                  resourceGUID,
                                           RelationshipRequestBody requestBody)
    {
        final String   methodName = "linkRoleToResource";
        final String   governanceRoleGUIDParameterName = "governanceRoleGUID";
        final String   resourceGUIDParameterName = "resourceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;


        try
        {
            if (requestBody != null)
            {
                PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                if (requestBody.getProperties() != null)
                {
                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 resourceGUID,
                                                 resourceGUIDParameterName,
                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                 governanceRoleGUID,
                                                 governanceRoleGUIDParameterName,
                                                 OpenMetadataType.PERSON_ROLE.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                 null,
                                                 requestBody.getProperties().getEffectiveFrom(),
                                                 requestBody.getProperties().getEffectiveTo(),
                                                 new Date(),
                                                 methodName);
                }
                else
                {
                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 resourceGUID,
                                                 resourceGUIDParameterName,
                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                 governanceRoleGUID,
                                                 governanceRoleGUIDParameterName,
                                                 OpenMetadataType.PERSON_ROLE.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                 (InstanceProperties) null,
                                                 null,
                                                 null,
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
     * Remove the link between a governance role and a resource.
     *
     * @param serverName name of server instance to call
     * @param userId calling user
     * @param governanceRoleGUID unique identifier of the governance role
     * @param resourceGUID unique identifier of the resource description
     * @param requestBody  external source request body
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse unlinkRoleFromResource(String                  serverName,
                                               String                  userId,
                                               String                  governanceRoleGUID,
                                               String                  resourceGUID,
                                               RelationshipRequestBody requestBody)
    {
        final String   methodName = "unlinkRoleToResource";
        final String   governanceRoleGUIDParameterName = "governanceRoleGUID";
        final String   resourceGUIDParameterName = "resourceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;


        try
        {
            if (requestBody != null)
            {
                PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.unlinkElementFromElement(userId,
                                                 false,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 resourceGUID,
                                                 resourceGUIDParameterName,
                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                 governanceRoleGUID,
                                                 governanceRoleGUIDParameterName,
                                                 OpenMetadataType.PERSON_ROLE.typeGUID,
                                                 OpenMetadataType.PERSON_ROLE.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
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
     * Remove the requested governance role.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceRoleGUID unique identifier (guid) of the governance role.
     * @param requestBody  external source request body
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance role is either null or invalid or
     * InvalidParameterException the appointmentId or governance domain is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse   deleteGovernanceRole(String                    serverName,
                                               String                    userId,
                                               String                    governanceRoleGUID,
                                               ExternalSourceRequestBody requestBody)
    {
        final String methodName = "deleteGovernanceRole";
        final String governanceRoleGUIDParameterName = "governanceRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removePersonRole(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         governanceRoleGUID,
                                         governanceRoleGUIDParameterName,
                                         false,
                                         false,
                                         new Date(),
                                         methodName);
            }
            else
            {
                handler.removePersonRole(userId,
                                         null,
                                         null,
                                         governanceRoleGUID,
                                         governanceRoleGUIDParameterName,
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
     * Retrieve a governance role description by unique guid.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceRoleGUID unique identifier (guid) of the governance role.
     * @return governance role object or
     * UnrecognizedGUIDException the unique identifier of the governance role is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceRoleResponse getGovernanceRoleByGUID(String serverName,
                                                          String userId,
                                                          String governanceRoleGUID)
    {
        final String methodName = "getGovernanceRoleByGUID";
        final String governanceRoleGUIDParameterName = "governanceRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceRoleResponse response = new GovernanceRoleResponse();
        AuditLog               auditLog = null;

        try
        {
            PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getBeanFromRepository(userId,
                                                              governanceRoleGUID,
                                                              governanceRoleGUIDParameterName,
                                                              OpenMetadataType.PERSON_ROLE.typeName,
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
     * Retrieve a governance role description by unique guid along with the history of who has been appointed
     * to the role.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceRoleGUID unique identifier (guid) of the governance role.
     * @return governance role object or
     * UnrecognizedGUIDException the unique identifier of the governance role is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceRoleHistoryResponse getGovernanceRoleHistoryByGUID(String     serverName,
                                                                        String     userId,
                                                                        String     governanceRoleGUID)
    {
        final String methodName = "getGovernanceRoleHistoryByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceRoleHistoryResponse response = new GovernanceRoleHistoryResponse();
        AuditLog                      auditLog = null;

        try
        {
            AppointmentHandler handler = instanceHandler.getAppointmentHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElement(handler.getGovernanceRoleHistoryByGUID(userId,
                                                                       governanceRoleGUID,
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
     * Retrieve the properties of a governance role using its unique name.  The results are returned as a list
     * since it is possible that two roles have the same identifier due to the distributed nature of the
     * open metadata ecosystem.  By returning all the search results here it is possible to manage the
     * duplicates through this interface.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param roleId  the unique identifier of the governance role.
     * @return governance role object or
     * InvalidParameterException the governanceRoleGUID or governance domain is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceRolesResponse getGovernanceRoleByRoleId(String     serverName,
                                                             String     userId,
                                                             String     roleId)
    {
        final String methodName = "getGovernanceRoleByRoleId";
        final String governanceRoleGUIDParameterName = "governanceRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceRolesResponse response = new GovernanceRolesResponse();
        AuditLog                auditLog = null;

        try
        {
            PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getPersonRolesByName(userId,
                                                              roleId,
                                                              governanceRoleGUIDParameterName,
                                                              0,
                                                              0,
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
     * Return all the defined governance roles.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param domainIdentifier domain of interest - 0 means all domains
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     * @return list of governance role objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceRolesResponse getGovernanceRolesByDomainId(String serverName,
                                                                String userId,
                                                                int    domainIdentifier,
                                                                int    startFrom,
                                                                int    pageSize)
    {
        final String methodName = "getGovernanceRolesByDomainId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceRolesResponse response = new GovernanceRolesResponse();
        AuditLog                auditLog = null;

        try
        {
            PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElements(handler.getPersonRolesForDomainId(userId,
                                                                   domainIdentifier,
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
     * Retrieve all the governance roles for a particular title.  The title can include regEx wildcards.
     *
     * @param serverName name of server instance to call
     * @param userId calling user
     * @param title short description of the role
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     * @return list of governance role objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceRolesResponse getGovernanceRolesByTitle(String serverName,
                                                             String userId,
                                                             String title,
                                                             int    startFrom,
                                                             int    pageSize)
    {
        final String methodName = "getGovernanceRolesByTitle";
        final String titleParameterName = "title";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceRolesResponse response = new GovernanceRolesResponse();
        AuditLog                auditLog = null;

        try
        {
            PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElements(handler.getPersonRolesForTitle(userId,
                                                                title,
                                                                titleParameterName,
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
     * Return all the governance roles and their incumbents (if any).
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param domainIdentifier identifier of domain - 0 means all
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     * @return list of governance role objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceRoleAppointeesResponse getCurrentGovernanceRoleAppointments(String serverName,
                                                                                 String userId,
                                                                                 int    domainIdentifier,
                                                                                 int    startFrom,
                                                                                 int    pageSize)
    {
        final String        methodName = "getActiveGovernanceRoles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceRoleAppointeesResponse response = new GovernanceRoleAppointeesResponse();
        AuditLog                         auditLog = null;

        try
        {
            AppointmentHandler handler = instanceHandler.getAppointmentHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElements(handler.getCurrentGovernanceRoleAppointments(userId,
                                                                              domainIdentifier,
                                                                              startFrom,
                                                                              pageSize,
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
     * Link a person to a governance role.  Only one person may be appointed at any one time.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceRoleGUID unique identifier (guid) of the governance role.
     * @param profileGUID unique identifier of the actor profile
     * @param requestBody unique identifier for the profile
     * @return unique identifier (guid) of the appointment relationship or
     * UnrecognizedGUIDException the unique identifier of the governance role or profile is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GUIDResponse appointGovernanceRole(String                  serverName,
                                              String                  userId,
                                              String                  governanceRoleGUID,
                                              String                  profileGUID,
                                              AppointmentRequestBody  requestBody)
    {
        final String methodName = "appointGovernanceRole";
        final String governanceRoleGUIDParameterName = "governanceRoleGUID";
        final String profileGUIDParameterName = "profileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {

                PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                String appointmentGUID = handler.appointPersonToRole(userId,
                                                                     null,
                                                                     null,
                                                                     profileGUID,
                                                                     profileGUIDParameterName,
                                                                     governanceRoleGUID,
                                                                     governanceRoleGUIDParameterName,
                                                                     true,
                                                                     requestBody.getEffectiveTime(),
                                                                     null,
                                                                     false,
                                                                     false,
                                                                     null,
                                                                     methodName);

                response.setGUID(appointmentGUID);
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
     * Unlink a person from a governance role appointment.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceRoleGUID unique identifier (guid) of the governance role.
     * @param profileGUID unique identifier of the actor profile
     * @param requestBody unique identifier for the profile.
     * @param appointmentGUID unique identifier (guid) of the appointment relationship
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance role or profile is either null or invalid or
     * InvalidParameterException the profile is not linked to this governance role or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse relieveGovernanceRole(String                  serverName,
                                              String                  userId,
                                              String                  governanceRoleGUID,
                                              String                  appointmentGUID,
                                              String                  profileGUID,
                                              AppointmentRequestBody  requestBody)
    {
        final String methodName = "relieveGovernanceRole";
        final String governanceRoleGUIDParameterName = "governanceRoleGUID";
        final String profileGUIDParameterName = "profileGUID";
        final String appointmentGUIDParameterName = "appointmentGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                PersonRoleHandler<GovernanceRoleElement> handler = instanceHandler.getGovernanceRoleHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.relievePersonFromRole(userId,
                                              null,
                                              null,
                                              profileGUID,
                                              profileGUIDParameterName,
                                              governanceRoleGUID,
                                              governanceRoleGUIDParameterName,
                                              appointmentGUID,
                                              appointmentGUIDParameterName,
                                              requestBody.getEffectiveTime(),
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
}
